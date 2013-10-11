package edu.yu.einstein.wasp.integration.messages.templates;

import org.springframework.batch.core.StepExecution;
import org.springframework.integration.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;

public abstract class AbstractHibernationMessageTemplate extends WaspMessageTemplate {
	
	
	public AbstractHibernationMessageTemplate(Long jobExecutionId, Long stepExecutionId) {
		super();
		addHeader(WaspMessageType.HEADER_KEY, WaspMessageType.HIBERNATION);
		setJobExecutionId(jobExecutionId);
		setStepExecutionId(stepExecutionId);
	}

	public AbstractHibernationMessageTemplate(Long jobExecutionId, Long stepExecutionId, String target) {
		super(target);
		setJobExecutionId(jobExecutionId);
		setStepExecutionId(stepExecutionId);
	}

	public AbstractHibernationMessageTemplate(Long jobExecutionId, Long stepExecutionId, String target, String task) {
		super(target, task);
		setJobExecutionId(jobExecutionId);
		setStepExecutionId(stepExecutionId);
	}
	
	public AbstractHibernationMessageTemplate(StepExecution stepExecution) {
		this(stepExecution.getJobExecutionId(), stepExecution.getId());
	}
	
	public AbstractHibernationMessageTemplate(StepExecution stepExecution, String target) {
		this(stepExecution.getJobExecutionId(), stepExecution.getId(), target);
	}
	
	public AbstractHibernationMessageTemplate(StepExecution stepExecution, String target, String task) {
		this(stepExecution.getJobExecutionId(), stepExecution.getId(), target, task);
	}
	
	

	public AbstractHibernationMessageTemplate(Message<?> message) {
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
		if (message.getHeaders().containsKey(WaspJobParameters.JOB_EXECUTION_ID))
			setJobExecutionId((Long) message.getHeaders().get(WaspJobParameters.JOB_EXECUTION_ID));
		if (message.getHeaders().containsKey(WaspJobParameters.STEP_EXECUTION_ID))
			setStepExecutionId((Long) message.getHeaders().get(WaspJobParameters.STEP_EXECUTION_ID));
	}
	
	public Long getJobExecutionId() {
		return (Long) getHeader(WaspJobParameters.JOB_EXECUTION_ID);
	}

	public void setJobExecutionId(Long jobExecutionId) {
		addHeader(WaspJobParameters.JOB_EXECUTION_ID, jobExecutionId);
	}
	
	public Long getStepExecutionId() {
		return (Long) getHeader(WaspJobParameters.STEP_EXECUTION_ID);
	}

	public void setStepExecutionId(Long jobExecutionId) {
		addHeader(WaspJobParameters.STEP_EXECUTION_ID, jobExecutionId);
	}

	@Override
	public boolean actUponMessage(Message<?> message) {
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getJobExecutionId(), getStepExecutionId());
		return actUponMessage(message, getJobExecutionId(), getStepExecutionId(), task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		if (task == null)
			return actUponMessage(message, getJobExecutionId(), getStepExecutionId());
		return actUponMessage(message, getJobExecutionId(), getStepExecutionId(), null);
	}
	
	/**
	 * Takes a message and checks its headers against the supplied jobExecutionId and stepExecutionId values to see if the message should be acted upon or not
	 * @param message
	 * @param jobExecutionId 
	 * @param stepExecutionId 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, Long jobExecutionId, Long stepExecutionId ){
		if (jobExecutionId != null &&
				message.getHeaders().containsKey(WaspJobParameters.JOB_EXECUTION_ID) && 
				((Long) message.getHeaders().get(WaspJobParameters.JOB_EXECUTION_ID)).equals(jobExecutionId) &&
				message.getHeaders().containsKey(WaspJobParameters.STEP_EXECUTION_ID) && 
				((Long) message.getHeaders().get(WaspJobParameters.STEP_EXECUTION_ID)).equals(stepExecutionId) &&
				message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) && 
				((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(WaspMessageType.HIBERNATION))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied stepExecution to see if the message should be acted upon or not
	 * @param message
	 * @param stepExecution 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, StepExecution stepExecution ){
		if (stepExecution == null) 
			return false;
		return actUponMessage(message, stepExecution.getJobExecutionId(), stepExecution.getId());
	}
	
	/**
	 * Takes a message and checks its headers against the supplied jobExecutionId value and task to see if the message should be acted upon or not
	 * @param message
	 * @param jobExecutionId 
	 * @param stepExecutionId 
	 * @param task
	 * @return
	 */
	private static boolean actUponMessage(Message<?> message, Long jobExecutionId, Long stepExecutionId, String task ){
		if (! actUponMessage(message, jobExecutionId, stepExecutionId) )
			return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) &&	message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the supplied stepExecution to see if the message should be acted upon or not
	 * @param message
	 * @param stepExecution 
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, StepExecution stepExecution, String task ){
		if (stepExecution == null) 
			return false;
		return actUponMessage(message, stepExecution.getJobExecutionId(), stepExecution.getId(), task);
	}
	
	/**
	 * Returns true is the message is of the correct WaspMessageType
	 * @param message
	 * @return
	 */
	public static boolean isMessageOfCorrectType(Message<?> message) {
		return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) &&  
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.HIBERNATION);
	}

}
