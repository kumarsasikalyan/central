package edu.yu.einstein.wasp.daemon.batch.tasklets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.AbstractJob;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.WaspBatchJobExecutionReadinessException;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager;
import edu.yu.einstein.wasp.integration.endpoints.BatchJobHibernationManager.LockType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

@Transactional
public abstract class WaspHibernatingTasklet implements NameAwareTasklet, BeanNameAware, StepExecutionListener {
	
	// In the split-step scenario: 
	// The last WaspHibernatingTasklet in the split-step of this job to call requestHibernation() gets control by 
	// calling BatchJobHibernationManager.addJobExecutionIdLockedForHibernating(jobExecutionId)
	// Only if all active steps have HIBERNATION_REQUESTED set to true then the controlling step will ask the hibernationManager to hibernate the job.
	
	// NOTE: due to transactional behavior, execution contexts are not visible in the database outside of this job thread until job stopped / completed
	// so care is required knowing what is visible where. Any additions to job/step execution contexts here will be visible immediately job-wide from within 
	// tasklet code.	
	
	private static final Logger logger = LoggerFactory.getLogger(WaspHibernatingTasklet.class);
	
	protected boolean wasHibernationRequested = false;
	
	protected boolean wasHibernationRequestGranted = false;
	
	private Set<? extends NameAwareTasklet> parallelSiblingFlowSteps = new HashSet<>();

	
	@Autowired
	@Value("${wasp.hibernation.retry.exponential.initialInterval:5000}")
	private Long initialExponentialInterval;
	
	@Autowired
	@Value("${wasp.hibernation.retry.exponential.maxInterval:3600000}")
	private Long maxExponentialInterval;
	
	@Autowired
	protected BatchJobHibernationManager hibernationManager;
	
	protected String name = "";
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setBeanName(String name) {
		this.name = name;
		
	}
	
	public Set<String> getParallelSiblingFlowStepNames() {
		Set<String> parallelStepNames = new HashSet<>();
		for (NameAwareTasklet tasklet : parallelSiblingFlowSteps)
			parallelStepNames.add(tasklet.getName());
		return parallelStepNames;
	}

	public void setParallelSiblingFlowSteps(Set<? extends NameAwareTasklet> parallelSteps) {
		this.parallelSiblingFlowSteps = parallelSteps;
	}
	
	/**
	 * Request hibernation of this jobExecution
	 * @param context
	 */
	protected void requestHibernation(ChunkContext context){
		logger.debug("Request to hibernate about to be processed ...");
		StepContext stepContext = context.getStepContext();
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		JobExecution jobExecution = stepExecution.getJobExecution();
		Long jobExecutionId = jobExecution.getId();
		Long stepExecutionId = stepExecution.getId();
		wasHibernationRequested = true;
		logContexts(context);
		if (!isHibernationRequestedForStep(stepExecution)){
			setHibernationRequestedForStep(stepExecution, true);
			setStepStatusInJobExecutionContext(stepExecution, BatchStatus.STOPPING);
		}
		if (isHibernationRequestedForJob(jobExecution)){
			// another step execution has already declared itself as handling hibernation so let it do the rest
			logger.debug("StepExecution id=" + stepExecutionId + 
					" will not proceed to request hibernation of JobExecution id=" + jobExecutionId + 
					" because another step has already initiated hibernation");
		} else if (isAllActiveJobStepsRequestingHibernation(stepExecution)){
			if (BatchJobHibernationManager.lockJobExecution(jobExecution, LockType.HIBERNATE)){
				try{
					// we are ready to hibernate so request it now
					logger.info("Going to hibernate job " + stepContext.getJobName() + 
							" (JobExecution id=" + jobExecutionId + ") from step " + 
							stepContext.getStepName() + " (step id=" + stepExecutionId + ")");
					doHibernate(stepExecution);
					wasHibernationRequestGranted = true;
				} catch (WaspBatchJobExecutionReadinessException e) {
					logger.info("Not going to hibernate job " + stepContext.getJobName() + 
							" (JobExecution id=" + jobExecutionId + ") from step " + 
							stepContext.getStepName() + " (step id=" + stepExecutionId + "): " + e.getLocalizedMessage());
				}
			} else {
				logger.info("Not going to hibernate job " + stepContext.getJobName() + 
						" (JobExecution id=" + jobExecutionId + ") from step " + 
						stepContext.getStepName() + " (step id=" + stepExecutionId + ") as currently already locked");
			}
		} else { 
			logger.debug("StepExecution id=" + stepExecutionId + 
					" has requested that it wishes to hibernate but other running steps are preventing hibernation of the JobExecution id=" + 
					jobExecutionId + " at this time");
		}
	}
	
	private boolean isAllActiveJobStepsRequestingHibernation(StepExecution stepExecution){
		JobExecution je = stepExecution.getJobExecution();
		boolean returnVal = true;
		for (String parallelStepName : getParallelSiblingFlowStepNames()){
			BatchStatus stepStatus = BatchStatus.UNKNOWN;
			stepStatus = getStepStatusInJobExecutionContext(je, parallelStepName);
			logger.debug("Step status with name " + parallelStepName + " for JobExecution id=" + je.getId() + " = " + stepStatus);
			if (stepStatus.equals(BatchStatus.UNKNOWN))
				returnVal = false;
		}
		logger.debug("isAllActiveJobStepsRequestingHibernation=" + returnVal);
		return returnVal;
	}
	
	private void doHibernate(StepExecution stepExecution) throws WaspBatchJobExecutionReadinessException{
		Long requestingStepExecutionId = stepExecution.getId();
		JobExecution jobExecution = stepExecution.getJobExecution();
		setHibernationRequestedForJob(stepExecution.getJobExecution(), true);
		Long jobExecutionId = jobExecution.getId();
		logger.info("Hibernation of JobExecution=" + jobExecutionId + " triggered by StepExecution id=" + stepExecution.getId());
		// register all wake triggers with hibernation manger
		for (StepExecution se : jobExecution.getStepExecutions()){
			if (!se.getStatus().equals(BatchStatus.STARTED))
				continue; // not a currently active StepExecution so ignore
			Long stepExecutionId = se.getId();
			try{
				Set<WaspStatusMessageTemplate> wakeMessages = BatchJobHibernationManager.getWakeMessagesFromStepExecutionContext(se);
				if (!wakeMessages.isEmpty())
					hibernationManager.addMessageTemplatesForWakingJobStep(jobExecutionId, stepExecutionId, wakeMessages);
			} catch (JSONException e) {
				logger.warn("Unable to get Wake Messages for JobExecution id=" + jobExecutionId + ", from StepExecution id=" + stepExecutionId + ": " + 
						e.getLocalizedMessage());
			}
			try{
				Set<WaspStatusMessageTemplate> abandonMessages = BatchJobHibernationManager.getAbandonMessagesForStep(se);
				if (!abandonMessages.isEmpty())
					hibernationManager.addMessageTemplatesForAbandoningJobStep(jobExecutionId, stepExecutionId, abandonMessages);
			} catch (JSONException e) {
				logger.warn("Unable to get Abandon Messages for JobExecution id=" + jobExecutionId + ", from StepExecution id=" + stepExecutionId + ": " + 
						e.getLocalizedMessage());
			}
			Long timeInterval = BatchJobHibernationManager.getWakeTimeInterval(se);
			if (timeInterval != null)
				hibernationManager.addTimeIntervalForJobStep(jobExecutionId, stepExecutionId, timeInterval);
			
		}
		waitUntilStateTransitionsStable(stepExecution);
		// request hibernation
		hibernationManager.processHibernateRequest(jobExecution.getId(),requestingStepExecutionId);
	}
	
	/**
	 * Step executions start asynchronously and there may be some transitions. We need to check that the flow state has stabilized 
	 * @param stepExecution
	 */
	private void waitUntilStateTransitionsStable(StepExecution stepExecution){
		int repeat = 0;
		Map<String, BatchStatus> previouslyExecutingSteps = new HashMap<>();
		while (repeat++ < 5){
			logger.debug("Waiting for hibernation from step id=" + stepExecution.getId() + " repeat=" + repeat);
			Map<String, BatchStatus> currentlyExecutingSteps = new HashMap<>();
			boolean allStepsRemainSame = true;
			for (StepExecution se : stepExecution.getJobExecution().getStepExecutions()){
				currentlyExecutingSteps.put(se.getStepName(), se.getStatus());
				if (allStepsRemainSame && 
						(!previouslyExecutingSteps.containsKey(se.getStepName()) || !previouslyExecutingSteps.get(se.getStepName()).equals(se.getStatus()))
						)
					allStepsRemainSame = false;
			}
			if (allStepsRemainSame && (currentlyExecutingSteps.size() != previouslyExecutingSteps.size()))
				allStepsRemainSame = false;
			previouslyExecutingSteps.clear();
			previouslyExecutingSteps.putAll(currentlyExecutingSteps);
			if (!allStepsRemainSame)
				repeat = 0;
			try {
				Thread.sleep(5); // delay
			} catch (InterruptedException e) {}
		}
	}
	
	protected void setStepStatusInJobExecutionContext(StepExecution stepExecution, BatchStatus status){
		ExecutionContext ec = stepExecution.getJobExecution().getExecutionContext();
		ec.put(AbstractJob.PARALLEL_TASK_PREFIX + name, status);
	}
	
	private BatchStatus getStepStatusInJobExecutionContext(JobExecution jobExecution, String stepName){
		ExecutionContext ec = jobExecution.getExecutionContext();
		if (ec.containsKey(AbstractJob.PARALLEL_TASK_PREFIX + stepName))
			return (BatchStatus) ec.get(AbstractJob.PARALLEL_TASK_PREFIX + stepName);
		return BatchStatus.UNKNOWN;
	}
	
	protected void setHibernationRequestedForStep(StepExecution stepExecution, boolean isRequested){
		stepExecution.getExecutionContext().put(AbstractJob.HIBERNATION_REQUESTED, isRequested);
	}
	
	protected boolean isHibernationRequestedForStep(StepExecution stepExecution){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		return executionContext.containsKey(AbstractJob.HIBERNATION_REQUESTED) && 
				(boolean) executionContext.get(AbstractJob.HIBERNATION_REQUESTED);
	}
	
	protected void setHibernationRequestedForJob(JobExecution jobExecution, boolean isRequested){
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		executionContext.put(AbstractJob.HIBERNATION_REQUESTED, isRequested);
	}
	
	protected boolean isHibernationRequestedForJob(JobExecution jobExecution){
		ExecutionContext executionContext = jobExecution.getExecutionContext();
		if (executionContext.containsKey(AbstractJob.HIBERNATION_REQUESTED)){
			return (boolean) executionContext.get(AbstractJob.HIBERNATION_REQUESTED);
		} else {
			for (StepExecution se : jobExecution.getStepExecutions())
				if (se.getStatus().equals(BatchStatus.STOPPING)){
					logger.debug("No entry for HIBERNATION_REQUESTED in context but at least one step is stopping");
					return true;
				}
		}
		return false;
	}
	
	
	/**
	 * Display contents of the JobExecutionContext and StepExecution context. Handy for debugging.
	 * @param context
	 */
	
	private void logContexts(ChunkContext context){
		ExecutionContext executionContext = context.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
		ExecutionContext stepExecutionContext = context.getStepContext().getStepExecution().getExecutionContext();
		for (Entry<String,Object> entry: executionContext.entrySet())
			logger.trace("ExecutionContext : " + entry.getKey() + "=" + entry.getValue().toString());
		for (Entry<String,Object> entry: stepExecutionContext.entrySet())
			logger.trace("StepExecutionContext : " + entry.getKey() + "=" + entry.getValue().toString());
	}
	
	/**
	 * Sets the timeout interval in the step context. If not previously set, the configured initial value is set. If a value is already present
	 * in the context, it is multiplied by two and re-set in the context. If the new value is greater than the maximum specified in configuration then 
	 * the maximum value is set
	 * @param context
	 * @return the current value of the timeoutInterval
	 */
	protected Long exponentiallyIncreaseTimeoutIntervalInContext(ChunkContext context){
		final Integer MULTIPLICATION_FACTOR = 2;
		StepExecution stepExecution = context.getStepContext().getStepExecution();
		Long previousTimeInterval = BatchJobHibernationManager.getWakeTimeInterval(stepExecution);
		Long newTimeInterval;
		if (previousTimeInterval == null){
			newTimeInterval = initialExponentialInterval;
			previousTimeInterval = 0L;
		} else if (previousTimeInterval * MULTIPLICATION_FACTOR > maxExponentialInterval)
			newTimeInterval = maxExponentialInterval;
		else
			newTimeInterval = previousTimeInterval * MULTIPLICATION_FACTOR;
		logger.debug(String.format("Previously set time interval (in StepExecutionContect) was %d ms, setting new time interval to be %d ms", 
				previousTimeInterval, newTimeInterval));
		if (!newTimeInterval.equals(previousTimeInterval))
			BatchJobHibernationManager.setWakeTimeInterval(stepExecution, newTimeInterval);
		return newTimeInterval;
	}
	
	protected void addStatusMessagesToWakeStepToContext(ChunkContext context, Set<WaspStatusMessageTemplate> templates) throws JSONException{
		BatchJobHibernationManager.setWakeMessagesInStepExecutionContext(context.getStepContext().getStepExecution(), templates);
	}
	
	protected void addStatusMessagesToAbandonStepToContext(ChunkContext context, Set<WaspStatusMessageTemplate> templates) throws JSONException{
		BatchJobHibernationManager.setAbandonMessagesForStep(context.getStepContext().getStepExecution(), templates);
	}
	
	protected Set<WaspStatusMessageTemplate> getStatusMessagesToWakeStepFromContext(ChunkContext context) throws JSONException{
		return BatchJobHibernationManager.getWakeMessagesFromStepExecutionContext(context.getStepContext().getStepExecution());
	}
	
	protected Set<WaspStatusMessageTemplate> getStatusMessagesToAbandonStepFromContext(ChunkContext context) throws JSONException{
		return BatchJobHibernationManager.getAbandonMessagesForStep(context.getStepContext().getStepExecution());
	}
	
	protected boolean wasWokenOnMessage(ChunkContext context){
		return wasWokenOnMessage(context.getStepContext().getStepExecution());
	}
	
	protected boolean wasWokenOnMessage(StepExecution stepExecution){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		return (executionContext.containsKey(BatchJobHibernationManager.WOKEN_ON_MESSAGE_STATUS));
	}
	
	protected WaspStatus getWokenOnMessageStatus(StepExecution stepExecution){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		WaspStatus status = WaspStatus.UNKNOWN;
		if (executionContext.containsKey(BatchJobHibernationManager.WOKEN_ON_MESSAGE_STATUS))
			status = (WaspStatus) executionContext.get(BatchJobHibernationManager.WOKEN_ON_MESSAGE_STATUS);
		logger.debug("StepExecutionId=" + stepExecution.getId() + " was woken with WaspStatus=" + status);
		return status;	
	}
	
	protected boolean wasWokenOnTimeout(ChunkContext context){
		return wasWokenOnTimeout(context.getStepContext().getStepExecution());
	}
	
	protected boolean wasWokenOnTimeout(StepExecution stepExecution){
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		boolean woken = false;
		if (executionContext.containsKey(BatchJobHibernationManager.WOKEN_ON_TIMEOUT))
			woken = (boolean) executionContext.get(BatchJobHibernationManager.WOKEN_ON_TIMEOUT);
		logger.debug("StepExecutionId=" + stepExecution.getId() + " wasWokenByTimeout=" + woken);
		return woken;	
	}
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		// Do Nothing here
	}
	
	@Override
	public ExitStatus afterStep(StepExecution stepExecution){
		logger.debug("In after step for StepExecutionId=" + stepExecution.getId());
		if (stepExecution.getExitStatus().isHibernating()){
			logger.debug("StepExecutionId=" + stepExecution.getId() + " is now Hibernating so may be unlocked");
			BatchJobHibernationManager.unlockJobExecution(stepExecution.getJobExecution(), LockType.HIBERNATE);
		}
		if (!stepExecution.getExitStatus().isRunning()){
			// make sure all messages from this step are removed from the hibernation manager to avoid a memory leak
			hibernationManager.removeStepExecutionFromWakeMessageMap(stepExecution);
			hibernationManager.removeStepExecutionFromAbandonMessageMap(stepExecution);
		}
		return stepExecution.getExitStatus();
	}
	
	
}
