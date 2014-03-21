/**
 *
 * JobServiceImpl.java 
 * @author dubin
 *  
 * the JobService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.wasp.JobExplorerWasp;
import org.springframework.batch.core.explore.wasp.ParameterValueRetrievalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.integration.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.Strategy;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.dao.AcctQuoteDao;
import edu.yu.einstein.wasp.dao.AcctQuoteMetaDao;
import edu.yu.einstein.wasp.dao.FileHandleDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftDao;
import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.JobMetaDao;
import edu.yu.einstein.wasp.dao.JobResourcecategoryDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.JobUserDao;
import edu.yu.einstein.wasp.dao.LabDao;
import edu.yu.einstein.wasp.dao.LabUserDao;
import edu.yu.einstein.wasp.dao.ResourceCategoryDao;
import edu.yu.einstein.wasp.dao.ResourceDao;
import edu.yu.einstein.wasp.dao.ResourceTypeDao;
import edu.yu.einstein.wasp.dao.RoleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.exception.MetaAttributeNotFoundException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.QuoteException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.templates.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.model.AcctQuote;
import edu.yu.einstein.wasp.model.AcctQuoteMeta;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobCellSelection;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftCellSelection;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobDraftMeta;
import edu.yu.einstein.wasp.model.JobDraftSoftware;
import edu.yu.einstein.wasp.model.JobDraftresourcecategory;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobMeta;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobSample;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Lab;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleJobCellSelection;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.plugin.BatchJobProviding;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.quote.MPSQuote;
import edu.yu.einstein.wasp.sequence.SequenceReadProperties;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.StrategyService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.UserService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.util.StringHelper;
import edu.yu.einstein.wasp.util.WaspJobContext;
import edu.yu.einstein.wasp.viewpanel.JobDataTabViewing;


@Service
@Transactional("entityManager")
public class JobServiceImpl extends WaspMessageHandlingServiceImpl implements JobService {

	private final String[] jobApproveArray = {"piApprove", "daApprove", "fmApprove"};
	//public String[] getJobApproveArray(){return jobApproveArray;}
	
	private JobDao	jobDao;
	
	/**
	 * setJobDao(JobDao jobDao)
	 * 
	 * @param jobDao
	 * 
	 */
	@Override
	@Autowired
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
	}

	/**
	 * getJobDao();
	 * 
	 * @return jobDao
	 * 
	 */
	@Override
	public JobDao getJobDao() {
		return this.jobDao;
	}
	
	@Autowired
	private RunService runService;
	
	@Autowired
	private WaspPluginRegistry waspPluginRegistry;
	
	@Autowired
	public void setJobMetaDao(JobMetaDao jobMetaDao) {
		this.jobMetaDao = jobMetaDao;
	}
	
	@Override
	public JobMetaDao getJobMetaDao() {
		return jobMetaDao;
	}

	public void setJobSoftwareDao(JobSoftwareDao jobSoftwareDao) {
		this.jobSoftwareDao = jobSoftwareDao;
	}
	public void setJobResourcecategoryDao(JobResourcecategoryDao jobResourcecategoryDao) {
		this.jobResourcecategoryDao = jobResourcecategoryDao;
	}
	
	public void setJobSampleDao(JobSampleDao jobSampleDao) {
		this.jobSampleDao = jobSampleDao;
	}
	
	@Override
	public JobSampleDao getJobSampleDao(){
		return this.jobSampleDao;
	}

	public void setSampleDao(SampleDao sampleDao) {
		this.sampleDao = sampleDao;
	}
	
	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
	
	public void setJobUserDao(JobUserDao jobUserDao) {
		this.jobUserDao = jobUserDao;
	}
	
	public void setLabDao(LabDao labDao) {
		this.labDao = labDao;
	}
	
	public void setJobCellSelectionDao(JobCellSelectionDao jobCellSelectionDao) {
		this.jobCellSelectionDao = jobCellSelectionDao;
	}
	
	public void setSampleJobCellSelectionDao(SampleJobCellSelectionDao sampleJobCellSelectionDao) {
		this.sampleJobCellSelectionDao = sampleJobCellSelectionDao;
	}

	public void setWorkflowDao(WorkflowDao workflowDao) {
		this.workflowDao = workflowDao;
	}
	
	public void setJobDraftDao(JobDraftDao jobDraftDao) {
		this.jobDraftDao = jobDraftDao;
	}
	
	public void setSampleMetaDao(SampleMetaDao sampleMetaDao) {
		this.sampleMetaDao = sampleMetaDao;
	}
	
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;	
	}
		
	public void setSampleTypeDao(SampleTypeDao sampleTypeDao) {
		this.sampleTypeDao = sampleTypeDao;	

	}
	
	public void setLogger(Logger logger) {
		
		this.logger = logger;
	}
	
	@Autowired
	private AcctQuoteDao acctQuoteDao;

	@Autowired
	private AcctQuoteMetaDao acctQuoteMetaDao;

	@Autowired
	private JobDraftDao jobDraftDao;
	
	@Autowired
	private MetaMessageService metaMessageService;

	
	@Autowired
	private SampleMetaDao sampleMetaDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private SampleService sampleService;

	@Autowired
	private StrategyService strategyService;

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private MessageService messageService;

	@Autowired
	private JobMetaDao jobMetaDao;

	@Autowired
	protected LabDao labDao;

	@Autowired
	protected LabUserDao labUserDao;

	@Autowired
	protected JobUserDao jobUserDao;

	@Autowired
	protected RoleDao roleDao;

	@Autowired
	protected ResourceDao resourceDao;

	@Autowired
	protected ResourceCategoryDao resourceCategoryDao;

	@Autowired
	protected SoftwareDao softwareDao;

	@Autowired
	protected ResourceTypeDao resourceTypeDao;

	@Autowired
	protected SampleDao sampleDao;

	@Autowired
	protected JobSampleDao jobSampleDao;
	
	@Autowired
	protected SampleTypeDao sampleTypeDao;
	
	@Autowired
	protected SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	protected SampleSubtypeDao subSampleTypeDao;

	@Autowired
	protected FileHandleDao fileDao;

	@Autowired
	protected JobCellSelectionDao jobCellSelectionDao;
	
	@Autowired
	protected SampleJobCellSelectionDao sampleJobCellSelectionDao;
	
	@Autowired
	protected JobSoftwareDao jobSoftwareDao;
	
	@Autowired
	protected JobResourcecategoryDao jobResourcecategoryDao;
	
	@Autowired
	protected JobFileDao jobFileDao;
	
	@Autowired
	protected WorkflowDao workflowDao;

	@Autowired
	protected UserService userService;

	@Autowired
	protected FileService fileService;

	
	protected JobExplorerWasp batchJobExplorer;
	
	@Autowired
	void setJobExplorer(JobExplorer jobExplorer){
		this.batchJobExplorer = (JobExplorerWasp) jobExplorer;
	}
	
		
	@Autowired
	protected WorkflowService workflowService;
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public Job getJobByJobId(Integer jobId){
		return jobDao.getJobByJobId(jobId.intValue());
	}
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Sample> getSubmittedSamples(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNull(job.getId(), "Invalid Job Provided");
		List<Sample> submittedSamplesList = new ArrayList<Sample>();
		
		for(JobSample jobSample : job.getJobSample()){//jobSampleDao.getJobSampleByJobId(job.getJobId())){ 
			  Sample sample  = sampleDao.getSampleBySampleId(jobSample.getSampleId());//includes submitted samples that are macromolecules, submitted samples that are libraries, and facility-generated libraries generated from a macromolecule
			  if(sample.getParentId() == null){//this sample is NOT a facility-generated library (by contrast, if sample.getParentId() != null this indicates a facility-generated library), so add it to the submittedSample list
				  submittedSamplesList.add(sample);
			  }
		}	
		
		return submittedSamplesList;		
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Sample> getSubmittedSamplesNotYetReceived(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		
		List<Sample> submittedSamplesNotYetReceivedList = new ArrayList<Sample>();
		
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.sample.step.listenForSampleReceived", parameterMap, false, ExitStatus.RUNNING);
		stepExecutions.addAll(batchJobExplorer.getStepExecutions("wasp.library.step.listenForLibraryReceived", parameterMap, false, ExitStatus.RUNNING));
		for (StepExecution stepExecution: stepExecutions){
			ExitStatus exitStatus = stepExecution.getJobExecution().getExitStatus();
			if (!exitStatus.isRunning())
				continue;
			Integer sampleId = null;
			try{
				sampleId = Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(stepExecution, WaspJobParameters.SAMPLE_ID));
			} catch (ParameterValueRetrievalException e){
				logger.warn(e.getMessage());
				continue;
			}
			Sample sample = sampleDao.getSampleBySampleId(sampleId);
			if (sample == null){
				logger.warn("Sample with sample id '"+sampleId+"' does not have a match in the database!");
				continue;
			}
			submittedSamplesNotYetReceivedList.add(sample);
		}
		return submittedSamplesNotYetReceivedList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Sample> getSubmittedSamplesNotYetQC(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		
		List<Sample> submittedSamplesNotYetQCList = new ArrayList<Sample>();
		
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		for (StepExecution stepExecution: batchJobExplorer.getStepExecutions("wasp.sample.step.sampleQC", parameterMap, false, ExitStatus.RUNNING)){
			ExitStatus exitStatus = stepExecution.getJobExecution().getExitStatus();
			if (!exitStatus.isRunning())
				continue;
			Integer sampleId = null;
			try{
				sampleId = Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(stepExecution, WaspJobParameters.SAMPLE_ID));
			} catch (ParameterValueRetrievalException e){
				logger.warn(e.getMessage());
				continue;
			}
			Sample sample = sampleDao.getSampleBySampleId(sampleId);
			if (sample == null){
				logger.warn("Sample with sample id '"+sampleId+"' does not have a match in the database!");
				continue;
			}
			submittedSamplesNotYetQCList.add(sample);
		}
		return submittedSamplesNotYetQCList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Sample> getLibrariesNotYetQC(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		
		List<Sample> submittedLibrariesNotYetQCList = new ArrayList<Sample>();
		
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		for (StepExecution stepExecution: batchJobExplorer.getStepExecutions("wasp.library.step.libraryQC", parameterMap, false, ExitStatus.RUNNING)){
			ExitStatus exitStatus = stepExecution.getJobExecution().getExitStatus();
			if (!exitStatus.isRunning())
				continue;
			Integer libraryId = null;
			try{
				libraryId = Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(stepExecution, WaspJobParameters.LIBRARY_ID));
			} catch (ParameterValueRetrievalException e){
				logger.warn(e.getMessage());
				continue;
			}
			Sample library = sampleDao.getSampleBySampleId(libraryId);
			if (library == null){
				logger.warn("Library with sample id '"+libraryId+"' does not have a match in the database!");
				continue;
			}
			submittedLibrariesNotYetQCList.add(library);
		}
		return submittedLibrariesNotYetQCList;
	}
	
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getActiveJobs(){
		
		List<Job> activeJobList = new ArrayList<Job>();
		// get all job executions from the Batch database which only have one parameter which is job_id but we want all
		// jobIds (so use '*'). Also only get those with ExitStatus.RUNNING. Then get the value of the job ids from the parameter
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add("*");
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions("default.waspJob.jobflow", parameterMap, true, ExitStatus.RUNNING);
		logger.debug("getJobExecutions() returned " + jobExecutions.size() + " result(s)");
		for (JobExecution jobExecution: jobExecutions){
			try{
				String parameterVal = batchJobExplorer.getJobParameterValueByKey(jobExecution, WaspJobParameters.JOB_ID);
				Job job = jobDao.getJobByJobId(Integer.valueOf(parameterVal));
				if (job == null || job.getId() == null){
					logger.warn("Expected a job object with id '"+parameterVal+"' but got none");
				} else {
					activeJobList.add(job);
				}
			} catch(ParameterValueRetrievalException e){
				logger.warn(e.getLocalizedMessage());
			} catch(NumberFormatException e){
				logger.warn(e.getLocalizedMessage());
			}
			
		}
		sortJobsByJobId(activeJobList);
		
		return activeJobList;
	}
	
	/**
	   * {@inheritDoc}
	   */
	@Override
	public boolean isJobActive(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions("default.waspJob.jobflow", parameterMap, true, ExitStatus.RUNNING);
		if (!jobExecutions.isEmpty())
			return true;
		return false;
	}
	
	
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getJobsAwaitingReceivingOfSamples(){
		
		List<Job> jobsAwaitingReceivingOfSamples = new ArrayList<Job>();
		
		for (Job job: getActiveJobs()){
			logger.debug("examining sample receive status of job with id='" + job.getId() + "'");
			if (this.isJobAwaitingReceivingOfSamples(job)) // some samples not yet received
				jobsAwaitingReceivingOfSamples.add(job);
		}
		
		sortJobsByJobId(jobsAwaitingReceivingOfSamples);
		
		return jobsAwaitingReceivingOfSamples;
	}
	
	/**
	   * {@inheritDoc}
	   */
	@Override
	public boolean isJobsAwaitingReceivingOfSamples(){
		for (Job job: getActiveJobs())
			if (isJobAwaitingReceivingOfSamples(job)) // some samples not yet received
				return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJobAwaitingReceivingOfSamples(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
		if (! getSubmittedSamplesNotYetReceived(job).isEmpty())
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJobAwaitingCellLibraryQC(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
		for (SampleSource cellLibrary : sampleService.getCellLibrariesForJob(job)){
			try {
				if (sampleService.isCellLibraryAwaitingQC(cellLibrary))
					return true;
			} catch (SampleTypeException e) {
				logger.warn("received unexpected SampleTypeException: " + e.getLocalizedMessage());
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJobsAwaitingCellLibraryQC(){
		for (Job job : getActiveJobs()){
			if (isJobAwaitingCellLibraryQC(job))
				return true;
		}
		return false;
	}
	
	
	@Override
	public List<Job> getJobsAwaitingFmApproval() {
		List<Job> allJobsPendingFmApproval = new ArrayList<Job>();
		for (Job job : getActiveJobs())
			if (isJobAwaitingFmApproval(job))
				allJobsPendingFmApproval.add(job);
		return allJobsPendingFmApproval;
	}
	
	@Override
	public List<Job> getJobsAwaitingPiLmApproval() {
		List<Job> allJobsPendingPiApproval = new ArrayList<Job>();
		for (Job job : getActiveJobs())
			if (isJobAwaitingPiApproval(job))
				allJobsPendingPiApproval.add(job);
		return allJobsPendingPiApproval;
	}
	
	@Override
	public List<Job> getJobsAwaitingDaApproval() {
		List<Job> allJobsPendingDaApproval = new ArrayList<Job>();
		for (Job job : getActiveJobs())
			if (isJobAwaitingDaApproval(job))
				allJobsPendingDaApproval.add(job);
		return allJobsPendingDaApproval;
	}
	
	
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getJobsAwaitingSampleQC(){
		
		List<Job> jobsAwaitingQCOfSamples = new ArrayList<Job>();
		
		for (Job job: getActiveJobs()){
			logger.debug("examining sample QC status of job with id='" + job.getId() + "'");
			if (this.isJobAwaitingSampleQC(job)) // some samples not yet QCd
				jobsAwaitingQCOfSamples.add(job);
		}
		
		sortJobsByJobId(jobsAwaitingQCOfSamples);
		
		return jobsAwaitingQCOfSamples;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJobAwaitingSampleQC(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
		if (!this.getSubmittedSamplesNotYetQC(job).isEmpty())
			return true;
		return false;
	}
	
	/**
	   * {@inheritDoc}
	   */
	@Override
	public boolean isJobsAwaitingSampleQC(){
		for (Job job: getActiveJobs())
			if (this.isJobAwaitingSampleQC(job)) // some samples not yet received
				return true;
		return false;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getJobsAwaitingLibraryQC(){
		
		List<Job> jobsAwaitingQCOfLibraries = new ArrayList<Job>();
		
		for (Job job: getActiveJobs()){
			logger.debug("examining library QC status of job with id='" + job.getId() + "'");
			if (this.isJobAwaitingLibraryQC(job)) // some samples not yet QCd
				jobsAwaitingQCOfLibraries.add(job);
		}
		
		sortJobsByJobId(jobsAwaitingQCOfLibraries);
		
		return jobsAwaitingQCOfLibraries;
	}
	
	/**
	   * {@inheritDoc}
	   */
	@Override
	public boolean isJobsAwaitingLibraryQC(){
		for (Job job: getActiveJobs())
			if (this.isJobAwaitingLibraryQC(job)) // some libraries not yet received
				return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJobAwaitingLibraryQC(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
		if (!this.getLibrariesNotYetQC(job).isEmpty())
			return true;
		return false;
	}
	
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void sortJobsByJobId(List<Job> jobs){
		  Assert.assertParameterNotNull(jobs, "No Job list provided");
		  class JobIdComparator implements Comparator<Job> {
			    @Override
			    public int compare(Job arg0, Job arg1) {
			        return arg0.getId().compareTo(arg1.getId());
			    }
		  }
		  Collections.sort(jobs, new JobIdComparator());//sort by job ID 
	  }
	  
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobAwaitingPiApproval(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
			Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new HashSet<String>();
			jobIdStringSet.add(job.getId().toString());
			parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			if (!batchJobExplorer.getStepExecutions("step.piApprove", parameterMap, true, ExitStatus.RUNNING).isEmpty())
				return true;
			return false;
		}
	
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobAwaitingDaApproval(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
			Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new HashSet<String>();
			jobIdStringSet.add(job.getId().toString());
			parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			if (!batchJobExplorer.getStepExecutions("step.daApprove", parameterMap, true, ExitStatus.RUNNING).isEmpty())
				return true;
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobAwaitingFmApproval(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
			Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new HashSet<String>();
			jobIdStringSet.add(job.getId().toString());
			parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			if (!batchJobExplorer.getStepExecutions("step.fmApprove", parameterMap, true, ExitStatus.RUNNING).isEmpty())
				return true;
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobPendingApprovalOrQuote(Job job){
			if (this.isJobAwaitingDaApproval(job))
				return true;
			if (this.isJobAwaitingFmApproval(job))
				return true;
			if (this.isJobAwaitingPiApproval(job))
				return true;
			if (this.isJobAwaitingQuote(job))
				return true;
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobAwaitingQuote(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
			Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new HashSet<String>();
			jobIdStringSet.add(job.getId().toString());
			parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			if (!batchJobExplorer.getStepExecutions("step.quote", parameterMap, true, ExitStatus.RUNNING).isEmpty())
				return true;
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public List<Job> getJobsAwaitingQuote(){
			List<Job> jobsAwaitingQuote = new ArrayList<Job>();
			for (Job job : getActiveJobs())
				if (isJobAwaitingQuote(job))
					jobsAwaitingQuote.add(job);
			return jobsAwaitingQuote;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobsAwaitingQuote(){
			for (Job job : getActiveJobs())
				if (isJobAwaitingQuote(job))
					return true;
			return false;
		}
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public LinkedHashMap<String, String> getExtraJobDetails(Job job){
		  Assert.assertParameterNotNull(job, "No Job provided");
		  Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		  LinkedHashMap<String, String> extraJobDetailsMap = new LinkedHashMap<String, String>();

		  List<JobResourcecategory> jobResourceCategoryList = job.getJobResourcecategory();
		  String area = null;
		  for(JobResourcecategory jrc : jobResourceCategoryList){
			  if(jrc.getResourceCategory().getResourceType().getIName().equals("mps")){
				  extraJobDetailsMap.put("jobdetail_for_import.Machine.label", jrc.getResourceCategory().getName());
				  area = jrc.getResourceCategory().getIName();
				  break;
			  }
		  }
		  for(JobMeta jobMeta : job.getJobMeta()){
			String str = area+".runType";
			if(str.toLowerCase().equals(jobMeta.getK().toLowerCase().trim())){
				extraJobDetailsMap.put("jobdetail_for_import.Run_Type.label", jobMeta.getV());
			}
		  }
		  String readLength = "?";
		  String readType = "?";
		  try {
			  SequenceReadProperties readProperties = SequenceReadProperties.getSequenceReadProperties(job, area, JobMeta.class);
			  if (readProperties != null){
				  readLength = readProperties.getReadLength().toString();
				  readType = readProperties.getReadType().toUpperCase();
			  }
		  } catch (MetadataException e) {
			  logger.warn("Cannot get sequenceReadProperties: " + e.getLocalizedMessage());
		  }
		  extraJobDetailsMap.put("jobdetail_for_import.Read_Length.label", readLength);
		  extraJobDetailsMap.put("jobdetail_for_import.Read_Type.label", readType);
		 
		  /* replaced with code below
		  try{
			  Float price = new Float(job.getAcctJobquotecurrent().get(0).getAcctQuote().getAmount());
			  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price));
		  }
		  catch(Exception e){
			  logger.debug("JobServiceImpl::getExtraJobDetails(): " + e);
			  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+"?.??"); 
		  }	
		  */
		  AcctQuote currentQuote = job.getCurrentQuote();
		  if(currentQuote == null || currentQuote.getId()==null){
			  extraJobDetailsMap.put("jobdetail_for_import.Quote_Job_Price.label", Currency.getInstance(Locale.getDefault()).getSymbol()+"?.??");
		  }
		  else{
			  Float price = new Float(job.getCurrentQuote().getAmount());
			  extraJobDetailsMap.put("jobdetail_for_import.Quote_Job_Price.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price));
		  }
		  return extraJobDetailsMap;	  
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public LinkedHashMap<String, String> getJobApprovals(Job job){
		  Assert.assertParameterNotNull(job, "No Job provided");
		  Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		  LinkedHashMap<String, String> jobApprovalsMap = new LinkedHashMap<String, String>();
		  
		  List<String> jobApproveList = new ArrayList<String>();
		  for(int i = 0; i < this.jobApproveArray.length; i++){
			  jobApproveList.add(jobApproveArray[i]);//piApprove, daApprove, fmApprove
		  }	  
		  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		  Set<String> jobIdStringSet = new HashSet<String>();
		  jobIdStringSet.add(job.getId().toString());
		  parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);

		  for(String jobApproveCode : jobApproveList){
			  //List<StepExecution> stepExecutions =  batchJobExplorer.getStepExecutions("step.piApprove", parameterMap, true);
			  List<StepExecution> stepExecutions = null;
			  stepExecutions =  batchJobExplorer.getStepExecutions("step." + jobApproveCode, parameterMap, true);
			  StepExecution stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(stepExecutions);
			  String approveStatus = null;
			  if (stepExecution == null){
				  //jobApprovalsMap.put(piStatusLabel, "status.notYetSet.label");
				  approveStatus = new String("notYetSet");
			  }
			  else {
				  ExitStatus adminApprovalStatus = stepExecution.getExitStatus();
				  if( adminApprovalStatus.isRunning()){
					  approveStatus = new String("awaitingResponse");
					  //jobApprovalsMap.put(piStatusLabel, "status.awaitingResponse.label");
				  }
				  else if( adminApprovalStatus.isCompleted()){
					  approveStatus = new String("approved");
					  //jobApprovalsMap.put(piStatusLabel, "status.approved.label");
				  }
				  else if( adminApprovalStatus.isFailed()){
					  approveStatus = new String("rejected");
					  //jobApprovalsMap.put(piStatusLabel, "status.rejected.label");
				  }
				  else if(adminApprovalStatus.isTerminated()){
					  approveStatus = new String("abandoned");
					  //jobApprovalsMap.put(piStatusLabel, "status.abandoned.label");
				  }
				  else{
					  approveStatus = new String("unknown");
					  //jobApprovalsMap.put(piStatusLabel, "status.unknown.label");
				  }
			  }
			  jobApprovalsMap.put(jobApproveCode, approveStatus);
		  } 
		  
		  return jobApprovalsMap;

	  }

public static final String SAMPLE_PAIR_META_KEY = "samplePairsTvsC";
		

	  /**
	   * {@inheritDoc}
	 * @throws WaspMessageBuildingException 
	   */
	  @Override
	  public Job createJobFromJobDraft(JobDraft jobDraft, User user) throws FileMoveException{
		  	Assert.assertParameterNotNull(jobDraft, "No JobDraft provided");
			Assert.assertParameterNotNullNotZero(jobDraft.getId(), "Invalid JobDraft Provided");
			Assert.assertParameterNotNull(user, "No User provided");
			Assert.assertParameterNotNullNotZero(user.getId(), "Invalid User Provided");
		  	
			// Copies JobDraft to a new Job
			Job job = new Job();
			job.setUserId(user.getId());
			job.setLabId(jobDraft.getLabId());
			job.setName(jobDraft.getName());
			job.setWorkflowId(jobDraft.getWorkflowId());
			job.setIsActive(1);
			
			job.setViewablebylab(0); // TODO: get from lab? Not being used yet
			
			Job jobDb = jobDao.save(job); 
			
			// Saves the metadata
			//String sampleDraftPairsKey = null;
			String sampleDraftPairs = null;
			for (JobDraftMeta jdm: jobDraft.getJobDraftMeta()) {
				if(jdm.getK().indexOf(SAMPLE_PAIR_META_KEY)>-1){//we need to deal with this piece of metadata separately; it must occur following the save of all the job's samples
					//sampleDraftPairsKey = jdm.getK();
					sampleDraftPairs = jdm.getV();
					continue; 
				}
				JobMeta jobMeta = new JobMeta();
				jobMeta.setJobId(jobDb.getId());
				jobMeta.setK(jdm.getK());
				jobMeta.setV(jdm.getV());
			
				jobMetaDao.save(jobMeta); 
			}
			
			// save the software selected
			for (JobDraftSoftware jdr: jobDraft.getJobDraftSoftware()) {
				JobSoftware jobSoftware = new JobSoftware();
				jobSoftware.setJobId(jobDb.getId());
				jobSoftware.setSoftwareId(jdr.getSoftwareId());
			
				jobSoftwareDao.save(jobSoftware); 
			}
			
			// save the resource category selected
			for (JobDraftresourcecategory jdr: jobDraft.getJobDraftresourcecategory()) {
				JobResourcecategory jobResourceCategory = new JobResourcecategory();
				jobResourceCategory.setJobId(jobDb.getId());
				jobResourceCategory.setResourcecategoryId(jdr.getResourcecategoryId());
			
				jobResourcecategoryDao.save(jobResourceCategory); 
			}
			
			
			// Creates the JobUser Permission
			JobUser jobUser = new JobUser(); 
			jobUser.setUserId(user.getId());
			jobUser.setJobId(jobDb.getId());
			Role role = roleDao.getRoleByRoleName("js");
			jobUser.setRoleId(role.getId());
			jobUserDao.save(jobUser);
			
			// added 10-20-11 by rob dubin: with job submission, add lab PI as job viewer ("jv")
			//note: could use similar logic in loop to assign jv to all the lab members
			Lab lab = labDao.getLabByLabId(jobDb.getLabId());		
			// if the pi is different from the job user
			if (jobUser.getUserId().intValue() != lab.getPrimaryUserId().intValue()) {
				JobUser jobUser2 = new JobUser();		
				jobUser2.setUserId(lab.getPrimaryUserId());//the lab PI
				jobUser2.setJobId(jobDb.getId());
				Role role2 = roleDao.getRoleByRoleName("jv");
				jobUser2.setRoleId(role2.getId());
				jobUserDao.save(jobUser2);
			}
			
			// Job Cells (oldid, newobj)
			Map<Integer,JobCellSelection> jobDraftCellMap = new HashMap<Integer,JobCellSelection>();
			
			for (JobDraftCellSelection jdc: jobDraft.getJobDraftCellSelection()) {
				JobCellSelection jobCellSelection = new JobCellSelection();
				jobCellSelection.setJobId(jobDb.getId());
				jobCellSelection.setCellIndex(jdc.getCellIndex());
			
				JobCellSelection jobCellSelectionDb =	jobCellSelectionDao.save(jobCellSelection);	
			
				jobDraftCellMap.put(jdc.getId(), jobCellSelectionDb);
			}
			
			// Create Samples
			List<Sample> samples = new ArrayList<Sample>();
			Map<Integer, Integer> sampleDraftIDKeyToSampleIDValueMap = new HashMap<Integer, Integer>();
			for (SampleDraft sd: jobDraft.getSampleDraft()) {
				Sample sample = new Sample();
				sample.setName(sd.getName()); 
				sample.setSampleTypeId(sd.getSampleTypeId()); 
				sample.setSampleSubtypeId(sd.getSampleSubtypeId()); 
				sample.setSubmitterLabId(jobDb.getLabId()); 
				sample.setSubmitterUserId(user.getId()); 
				sample.setSubmitterJobId(jobDb.getId()); 
				sample.setIsReceived(0);
				sample.setIsActive(1);
		
				Sample sampleDb = sampleDao.save(sample); 
				samples.add(sampleDb);
				sampleDraftIDKeyToSampleIDValueMap.put(sd.getId(), sampleDb.getId());
		
				
		
				// Sample Draft Meta Data
				for (SampleDraftMeta sdm: sd.getSampleDraftMeta()) {
					SampleMeta sampleMeta = new SampleMeta();
		
					sampleMeta.setSampleId(sampleDb.getId());	
					sampleMeta.setK(sdm.getK());	
					sampleMeta.setV(sdm.getV());	
					sampleMeta.setPosition(sdm.getPosition());	
		
					sampleMetaDao.save(sampleMeta); 
				}
			
				// Job Sample
				JobSample jobSample = new JobSample();
				jobSample.setJobId(jobDb.getId());
				jobSample.setSampleId(sampleDb.getId());
			
				jobSampleDao.save(jobSample);
			
				for (SampleDraftJobDraftCellSelection sdc: sd.getSampleDraftJobDraftCellSelection()) {
					SampleJobCellSelection sampleJobCellSelection = new SampleJobCellSelection();
					sampleJobCellSelection.setSampleId(sampleDb.getId());
					sampleJobCellSelection.setJobCellSelectionId(jobDraftCellMap.get(sdc.getJobDraftCellSelectionId()).getId());
					sampleJobCellSelection.setLibraryIndex(sdc.getLibraryIndex());
					sampleJobCellSelectionDao.save(sampleJobCellSelection);
				}
			}
			
			//translate sampleDraftPairs to samplePairs, and save them in the samplesource table
			//StringBuffer samplePairsSB = new StringBuffer();
			if(sampleDraftPairs != null){
				for(String pair : sampleDraftPairs.split(";")){
					try{
						String[] pairList = pair.split(":");
						Integer T = sampleDraftIDKeyToSampleIDValueMap.get(Integer.valueOf(pairList[0]));
						Integer C = sampleDraftIDKeyToSampleIDValueMap.get(Integer.valueOf(pairList[1]));
						sampleService.createTestControlSamplePairsByIds(T, C, jobDb);
					}catch(Exception e){
						logger.warn("e.getMessage(): Error translate sampleDraftPairs to samplePairs");
						throw new RuntimeException("Error translate sampleDraftPairs to samplePairs");
					}
				}
			}

			
			// jobDraftFile -> jobFile
			if (jobDraft.getJobDraftFile() != null) {
				for (JobDraftFile jdf : jobDraft.getJobDraftFile()) {
					FileGroup group = jdf.getFileGroup();
	
					JobFile jobFile = new JobFile();
					jobFile.setJob(jobDb);
					try {
						jobFile.setFileGroup(fileService.promoteJobDraftFileGroupToJob(jobDb, group));
					} catch (Exception e) {
						logger.warn(e.getLocalizedMessage());
						throw new RuntimeException(e);
					}
					jobFileDao.save(jobFile);
				}
			}	
			
			// update the jobdraft
			jobDraft.setStatus("SUBMITTED");
			jobDraft.setSubmittedjobId(jobDb.getId());
			jobDraftDao.save(jobDraft); 
			
			// initiate batch jobs in wasp-daemon
			logger.debug("calling initiateBatchJobForJobSubmission() for job with id='" + jobDb.getId() + "'");
			initiateBatchJobForJobSubmission(jobDb);
			for (Sample sample: samples){
				logger.debug("calling initiateBatchJobForSample() for sample with id='" + sample.getId() + "'");
				if (sampleTypeDao.getSampleTypeBySampleTypeId(sample.getSampleTypeId()).getIName().equals("library")){
					sampleService.initiateBatchJobForSample(jobDb, sample, "wasp.userLibrary.jobflow.v1");
				} else {
					sampleService.initiateBatchJobForSample(jobDb, sample, "wasp.sample.jobflow.v1");
				}
			}
			
			return jobDb;
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public void initiateBatchJobForJobSubmission(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		// send message to initiate job processing
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.JOB_ID, job.getId().toString());
		String batchJobName = workflowService.getJobFlowBatchJobName(workflowDao.getWorkflowByWorkflowId(job.getWorkflowId()));
		BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( new BatchJobLaunchContext(batchJobName, jobParameters) );
		try {
			sendOutboundMessage(batchJobLaunchMessageTemplate.build(), true);
		} catch (WaspMessageBuildingException e) {
			throw new MessagingException(e.getLocalizedMessage(), e);
		}
	  }
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsAwaitingLibraryCreation(){
		List<Job> JobsAwaitingLibraryCreation = new ArrayList<Job>();
		for (Job job : getActiveJobs()){
			for (Sample sample: job.getSample()){
				if (sampleService.isSampleAwaitingLibraryCreation(sample)){
					JobsAwaitingLibraryCreation.add(job);
					break;
				}
			}
		}
		return JobsAwaitingLibraryCreation;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJobAwaitingLibraryCreation(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		for (Sample sample: job.getSample()){
			if (sampleService.isSampleAwaitingLibraryCreation(sample))
				return true;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit(ResourceCategory resourceCategory){
		List<Job> jobsFilteredByResourceCategory = new ArrayList<Job>();
		for (Job currentJob: getJobsWithLibrariesToGoOnPlatformUnit()){
			JobResourcecategory jrc = jobResourcecategoryDao.getJobResourcecategoryByResourcecategoryIdJobId(resourceCategory.getId(), currentJob.getId());
			if(jrc!=null && jrc.getId()!=null && jrc.getId().intValue() != 0)
				jobsFilteredByResourceCategory.add(currentJob);
		}
		return jobsFilteredByResourceCategory;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit(){
		List<Job> jobsWithLibrariesToGoOnFlowCell = new ArrayList<Job>();
		for (Job job: getActiveJobs()){
			if (isJobWithLibrariesToGoOnPlatformUnit(job))
				jobsWithLibrariesToGoOnFlowCell.add(job);
		}
		return jobsWithLibrariesToGoOnFlowCell;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJobWithLibrariesToGoOnPlatformUnit(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		for (Sample sample: job.getSample()){
			if (sampleService.isLibrary(sample)){
				try {
					if (sampleService.isLibraryAwaitingPlatformUnitPlacement(sample) && sampleService.isLibraryPassQC(sample))
						return true;
				} catch (SampleTypeException e){
					logger.warn(e.getLocalizedMessage());
				}
			} else {
				if (sample.getChildren() == null) // no libraries made (TODO: make sure at least one is successful)
					continue;
				for (Sample library: sample.getChildren()){
					try{
						if (sampleService.isLibraryAwaitingPlatformUnitPlacement(library) && sampleService.isLibraryPassQC(library))
							return true;
					} catch (SampleTypeException e){
						logger.warn(e.getLocalizedMessage());
					}
				}
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Sample> getPlatformUnitWithLibrariesOnForJob(Job job){
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		
		List<Sample> puList = new ArrayList<Sample>();
		for (SampleSource cellLibrary: sampleService.getCellLibrariesForJob(job)){
			try{
				puList.add(sampleService.getPlatformUnitForCell(cellLibrary.getSample()));
			} catch(SampleTypeException e){
				logger.warn("Expected sampletype of cellLibrary for SampleSource with Id of " + cellLibrary.getId()); 
			} catch (SampleParentChildException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						  
		}

		return puList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsSubmittedOrViewableByUser(User user){
		Assert.assertParameterNotNull(user, "No User provided");
		Assert.assertParameterNotNullNotZero(user.getId(), "Invalid User Provided");
		
		List<Job> jobList = new ArrayList<Job>();
		List<JobUser> jobUserList = new ArrayList<JobUser>();
		
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("userId", user.getId().intValue());
		List<String> orderByColumnNames = new ArrayList<String>();
		orderByColumnNames.add("jobId");
		
		jobUserList = this.jobUserDao.findByMapDistinctOrderBy(m, null, orderByColumnNames, "desc");//default order is by jobId/desc
		
		for(JobUser jobUser : jobUserList){
			jobList.add(jobUser.getJob());
		}		
		
		return jobList;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isJobAwaitingLibraryCreation(Job job, Sample sample){
		Assert.assertParameterNotNull(sample, "No Sample provided");
		Integer sampleId = sample.getId();
		Assert.assertParameterNotNullNotZero(sampleId, "Invalid Sample Provided");
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		boolean sampleIsInJob = false;
		for (Sample s: job.getSample()){
			if (s.getId().equals(sampleId)){
				sampleIsInJob = true;
				break;
			}
		}
		if (!sampleIsInJob){
			logger.warn("supplied sample is not associated with supplied job");
			return false;
		}
		return sampleService.isSampleAwaitingLibraryCreation(sample);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobQuoteStatus(Job job, WaspStatus status) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.QUOTE, "", true);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobApprovalStatus(String jobApproveCode, Job job, WaspStatus status, String comment) throws WaspMessageBuildingException{
		
		if(jobApproveCode.equals("piApprove")){
			updateJobPiApprovalStatus(job, status, comment);
		}
		else if(jobApproveCode.equals("daApprove")){
			updateJobDaApprovalStatus(job, status, comment);
		}
		else if(jobApproveCode.equals("fmApprove")){
			updateJobFmApprovalStatus(job, status, comment);
		}
		else{
			throw new WaspMessageBuildingException();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobDaApprovalStatus(Job job, WaspStatus status, String comment) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.DA_APPROVE, comment, true);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobPiApprovalStatus(Job job, WaspStatus status, String comment) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.PI_APPROVE, comment, true);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobFmApprovalStatus(Job job, WaspStatus status, String comment) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.FM_APPROVE, comment, true);
	}
	
	private void updateJobStatus(Job job, WaspStatus status, String task, String comment, boolean checkForJobActive) throws WaspMessageBuildingException{
		// TODO: Write test!!
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		Assert.assertParameterNotNull(status, "No Status provided");
		if (status != WaspStatus.COMPLETED && status != WaspStatus.ABANDONED)
			throw new InvalidParameterException("WaspStatus is null, or not COMPLETED or ABANDONED");
		Assert.assertParameterNotNull(task, "No Task provided");
		if (checkForJobActive && !this.isJobActive(job))
			throw new WaspMessageBuildingException("Not going to build message because job " + job.getId() + " is not active");
		JobStatusMessageTemplate messageTemplate = new JobStatusMessageTemplate(job.getId());
		messageTemplate.setUserCreatingMessageFromSession(userService);
		messageTemplate.setComment(comment);
		messageTemplate.setTask(task);
		messageTemplate.setStatus(status); // sample received (COMPLETED) or abandoned (ABANDONED)
		try{
			sendOutboundMessage(messageTemplate.build(), true);
		} catch (MessagingException e){
			throw new WaspMessageBuildingException(e.getLocalizedMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addJobViewer(Integer jobId, String newViewerEmailAddress) throws Exception{
		
		  if(jobId == null || newViewerEmailAddress == null){
		  	  throw new Exception("listJobSamples.illegalOperation.label");	
		  }		
		  	  		
		  Job job = jobDao.getJobByJobId(jobId.intValue());
		  if(job.getId()==null || job.getId().intValue() <= 0){
			throw new Exception("listJobSamples.jobNotFound.label");			  
		  }
		  
		  User userPerformingThisAction = authenticationService.getAuthenticatedUser();
		  if(userPerformingThisAction.getId()==null || userPerformingThisAction.getId().intValue()<=0){
			  throw new Exception("listJobSamples.illegalOperation.label");
		  }
		  
		  Boolean userPerformingThisActionIsPermittedToAddJobViewers = false;
		  //does the webviewer have the authority to perform this function?
		  if(authenticationService.isSuperUser() //webviewer (person performing the action) is superuser so OK
				  ||  userPerformingThisAction.getId().intValue() == job.getUserId().intValue() //webViewer is the job submitter, so OK
				  || userPerformingThisAction.getId().intValue() == job.getLab().getPrimaryUserId().intValue()//webViewer is the job PI, so OK
			)
		  {
			  userPerformingThisActionIsPermittedToAddJobViewers = true; //superuser, job's submitter, job's PI
		  }
		  if(!userPerformingThisActionIsPermittedToAddJobViewers){
			  throw new Exception("listJobSamples.illegalOperation.label");			  
		  }
		  
		  if(newViewerEmailAddress==null || "".equals(newViewerEmailAddress.trim())){
			  throw new Exception("listJobSamples.missingEmailAddress.label");
		  }
		  
		  if(! StringHelper.isStringAValidEmailAddress(newViewerEmailAddress) ){
			  throw new Exception("listJobSamples.invalidFormatEmailAddress.label");
		  }
		  User newViewerToBeAddedToJob = userDao.getUserByEmail(newViewerEmailAddress.trim());
		  if(newViewerToBeAddedToJob.getId()==null || newViewerToBeAddedToJob.getId().intValue()<= 0){
			  throw new Exception("listJobSamples.userNotFoundByEmailAddress.label");	
		  }
		  JobUser jobUser = jobUserDao.getJobUserByJobIdUserId(jobId.intValue(), newViewerToBeAddedToJob.getId().intValue());
		  if(jobUser.getId()!=null && jobUser.getId().intValue() > 0){//viewer to be added is already a viewer for this job.
			  throw new Exception("listJobSamples.alreadyIsViewerOfThisJob.label");
		  }
		  Role role = roleDao.getRoleByRoleName("jv");
		  if(role.getId()==null || role.getId().intValue()<=0){
			  throw new Exception("listJobSamples.roleNotFound.label");
		  }
		  JobUser newJobUser = new JobUser();
		  newJobUser.setJob(job);
		  newJobUser.setUser(newViewerToBeAddedToJob);
		  newJobUser.setRole(role);
		  jobUserDao.save(newJobUser);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeJobViewer(Integer jobId, Integer userId)throws Exception{
		
		  if(jobId == null || userId == null){
			  throw new Exception("listJobSamples.illegalOperation.label");	
		  }
		  
		  Job job = jobDao.getJobByJobId(jobId.intValue());
		  if(job.getId()==null || job.getId().intValue() <= 0 ){
			  throw new Exception("listJobSamples.jobNotFound.label");			  
		  }
		  User userToBeRemoved = userDao.getUserByUserId(userId.intValue());
		  if(userToBeRemoved.getId()==null || userToBeRemoved.getId().intValue() <= 0 ){//userToBeRemoved not found in the user table; odd.
			  throw new Exception("listJobSamples.userNotFound.label");			  
		  }

		  
		  User userPerformingThisAction = authenticationService.getAuthenticatedUser();
		  if(userPerformingThisAction.getId()==null || userPerformingThisAction.getId().intValue()<=0){
			  throw new Exception("listJobSamples.illegalOperation.label");
		  }
		  
		  Boolean userPerformingThisActionIsPermittedToRemoveJobViewers = false;
		  //does the webviewer have the authority to perform this function?
		  if(authenticationService.isSuperUser() //webviewer (person performing the action) is superuser so OK
				  ||  userPerformingThisAction.getId().intValue() == job.getUserId().intValue() //webViewer is the job submitter, so OK
				  || userPerformingThisAction.getId().intValue() == job.getLab().getPrimaryUserId().intValue()//webViewer is the job PI, so OK
				  || userPerformingThisAction.getId().intValue() == userToBeRemoved.getId().intValue()//webViewer is attempting to remove him/her self from list, which is allowed (so long as the webviewer is neither the job submitter or the job's PI).
			)
		  {
			  userPerformingThisActionIsPermittedToRemoveJobViewers = true; //superuser, job's submitter, job's PI
		  }
		  if(!userPerformingThisActionIsPermittedToRemoveJobViewers){
			  throw new Exception("listJobSamples.illegalOperation.label");			  
		  }
		  
		  //we checked that webviewer is authorized to do this. Now make certain that the webviewer is not trying to remove the job submitter or job PI. 
		  if(userToBeRemoved.getId().intValue() == job.getUserId().intValue()){//trying to remove job's submitter as viewer; not allowed
			  throw new Exception("listJobSamples.submitterRemovalIllegal.label");			  
		  }
		  if(userToBeRemoved.getId().intValue() == job.getLab().getPrimaryUserId().intValue()){//trying to remove job pi as viewer; not allowed
			  throw new Exception("listJobSamples.piRemovalIllegal.label");			  
		  }
		  
		  JobUser jobUser = jobUserDao.getJobUserByJobIdUserId(job.getId().intValue(), userToBeRemoved.getId().intValue());
		  if(jobUser.getId().intValue() <= 0){//jobuser not found for this job and this user in the jobuser table.
			  throw new Exception("listJobSamples.userNotViewerOfThisJob.label");
		  }
		  else{
			  jobUserDao.remove(jobUser);
			  jobUserDao.flush(jobUser);
		  }		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFacilityJobComment(Integer jobId, String comment) throws Exception{
		try{
			metaMessageService.saveToGroup("facilityJobComments", "Facility Job Comment", comment.trim(), jobId, JobMeta.class, jobMetaDao);
		}catch(Exception e){ throw new Exception(e.getMessage());}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MetaMessage> getAllFacilityJobComments(Integer jobId){
		return metaMessageService.read("facilityJobComments", jobId, JobMeta.class, jobMetaDao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setUserSubmittedJobComment(Integer jobId, String comment) throws Exception{
		try{
			metaMessageService.saveToGroup("userSubmittedJobComment", "User-submitted Job Comment", comment.trim(), jobId, JobMeta.class, jobMetaDao);
		}catch(Exception e){ throw new Exception(e.getMessage());}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MetaMessage> getUserSubmittedJobComment(Integer jobId){
		return metaMessageService.read("userSubmittedJobComment", jobId, JobMeta.class, jobMetaDao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Sample, String> getCoverageMap(Job job){
		
		Map<Sample, String> coverageMap = new LinkedHashMap<Sample, String>();
		for(Sample sample : this.getSubmittedSamples(job)){
			StringBuffer stringBuffer = new StringBuffer("");
			for(int i = 1; i <= job.getJobCellSelection().size(); i++){
				boolean found = false;
				for(JobCellSelection jobCellSelection : job.getJobCellSelection()){
					for(SampleJobCellSelection sampleJobCellSelection : jobCellSelection.getSampleJobCellSelection()){
						if(sampleJobCellSelection.getSampleId().intValue() == sample.getId().intValue()){
							if(jobCellSelection.getCellIndex().intValue() == i){
								stringBuffer.append("1");
								found = true;
							}
						}
					}
				}
				if(found == false){
					stringBuffer.append("0");
				}
			}
			coverageMap.put(sample, new String(stringBuffer));
  		}
		return coverageMap;
	}

	@Override
	public void setJobDao(SampleDao mockSampleDao) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public WaspJobContext getWaspJobContext(Job job) throws JobContextInitializationException{
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getId(), "Invalid Job Provided");
		return new WaspJobContext(job);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJobApprovalComment(String jobApproveCode, Integer jobId, String comment) throws Exception{
		try{
			metaMessageService.saveToGroup(jobApproveCode + "Comment", "Job Approve Comment", comment.trim(), jobId, JobMeta.class, jobMetaDao);
		}catch(Exception e){ throw new Exception(e.getMessage());}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MetaMessage> getJobApprovalComments(String jobApproveCode, Integer jobId){
		return metaMessageService.read(jobApproveCode + "Comment", jobId, JobMeta.class, jobMetaDao);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, MetaMessage> getLatestJobApprovalsComments(Set<String> jobApproveCodeSet, Integer jobId){
		
		HashMap<String, MetaMessage> map = new HashMap<String, MetaMessage>();
		for(String jobApproveCode : jobApproveCodeSet){
			List<MetaMessage> metaMessageList = this.getJobApprovalComments(jobApproveCode, jobId);
			if(metaMessageList.size()>0){
				map.put(jobApproveCode, metaMessageList.get(metaMessageList.size()-1));//get the last one
			}
		}	
		return map;
	}



	@Override
	public Map<String, Object> getTreeViewBranch(Integer id, Integer pid, String type, Integer jid) throws SampleTypeException, SampleParentChildException{
		
		Map <String, Object> curNode = new HashMap<String, Object>();
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		
		if (type.equalsIgnoreCase("job")) {
			Job job = getJobByJobId(id);

			curNode.put("name", "Job: "+job.getName());
			curNode.put("myid", id);
			curNode.put("type", "job");

			if (jid==null || jid<1) {  // jid==null/<1 means this is a analysis view
				List<Sample> sampleList = getSubmittedSamples(job);
				for (Sample sample : sampleList) {
					Map<String,Object> childNode = new HashMap<>();

					if(!sampleService.isLibrary(sample)) {
						// if it's non-library sample
						childNode.put("name", "Sample: "+sample.getName());
						childNode.put("myid", sample.getId());
						childNode.put("type", "sample");
						childNode.put("children", "");
						//sampleNode.put("children", getChildrenByNodeType(sample.getId(), "sample"));
					} else {
						// if it's library sample
						childNode.put("name", "Library: "+sample.getName());
						childNode.put("myid", sample.getId());
						childNode.put("type", "library");
						childNode.put("children", "");
						//sampleNode.put("children", getChildrenByNodeType(sample.getId(), "library"));
					}

					children.add(childNode);
				}
			} else {  //jid>=1 means this is a job-run view which needs the job id on the fly
				curNode.put("jid", id);
				
				// get the list of all successful runs for the job
				List<Sample> puList = this.getPlatformUnitWithLibrariesOnForJob(job);
				Map<Integer, Run> runMap = new HashMap<>();
				for (Sample pu : puList) {
					List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(pu);
					for (Run run : runList) {
						runMap.put(run.getId(), run);
					}
				}
				
				for (Run run : runMap.values()) {
					Map<String,Object> childNode = new HashMap<>();

					childNode.put("name", "Run: "+run.getName());
					childNode.put("myid", run.getId());
					childNode.put("type", "run");
					childNode.put("jid", jid);
					childNode.put("children", "");

					children.add(childNode);
				}
				
				if (children.isEmpty()) {
					Map<String,Object> childNode = new HashMap<>();
					childNode.put("name", "No completed run yet");
					childNode.put("myid", -1);
					childNode.put("type", "dummy");
					childNode.put("children", "");
					
					children.add(childNode);
				}
			}
			
			curNode.put("children", children);
		} else if (type.equalsIgnoreCase("sample")) {
			Sample sample = sampleService.getSampleById(id);

			
			// If the sample has parent facility-made library
			List<Sample> faclibList = sampleService.getFacilityGeneratedLibraries(sample);
			for (Sample faclib : faclibList) {
				Map<String,Object> childNode = new HashMap<>();
				
				childNode.put("name", "Facility-made Library: "+faclib.getName());
				childNode.put("myid", faclib.getId());
				childNode.put("type", "library");
				childNode.put("children", "");
				//faclibNode.put("children", getChildrenByNodeType(faclib.getId(), "library"));
				
				children.add(childNode);
			}
			
			curNode.put("children", children);
		} else if (type.equalsIgnoreCase("library")) {
			Sample library = sampleService.getSampleById(id);

			//curNode.put("name", "Library: "+library.getName());
			//curNode.put("myid", id);
			//curNode.put("type", "library");
			
			//get all cells associated with the library
			List<Sample> cellList = sampleService.getCellsForLibrary(library);
			if (!cellList.isEmpty()) {
				Map<String,Object> childNode = new HashMap<>();
				Sample cell = cellList.get(0);	//only one possible cell for one library
				childNode.put("name", "Cell: "+cell.getName());
				childNode.put("myid", cell.getId());
				childNode.put("type", "cell");
				childNode.put("libid", id);
				childNode.put("children", "");
				//cellNode.put("children", getChildrenByNodeType(cell.getId(), "cell"));

				children.add(childNode);
			}

			// add file type nodes to library
			//dubin 3-21-14 WE DO NOT WANT THIS! (confirmed with AJ)     children.addAll(getFileNodesByLibrary(library, null));

			curNode.put("children", children);
		} else if (type.equalsIgnoreCase("cell")) {
			// if the sample is a cell
			Sample cell = sampleService.getSampleById(id);
			Sample library = sampleService.getSampleById(pid);

			//children.addAll(getFileNodesByCellLibrary(cell, library));
			children.addAll(getFileNodesByLibrary(library, cell));
			
			curNode.put("children", children);
		} else if (type.equalsIgnoreCase("run")) {
			Run run = runService.getRunById(id);
			
			if (jid==null || jid<1) {
				Set<SampleSource> libcellList = runService.getCellLibrariesOnSuccessfulRunCells(run);
				for (SampleSource libcell : libcellList) {
					Sample cell = sampleService.getCell(libcell);
					Map<String,Object> childNode = new HashMap<>();
					
					childNode.put("name", "Cell: "+cell.getName());
					childNode.put("myid", cell.getId());
					childNode.put("type", "cell");
					childNode.put("children", "");
					
					children.add(childNode);
				}
				
				curNode.put("children", children);
			} else {
				List<Sample> cellList = runService.getCellsOnSuccessfulRunCellsWithoutControlsForJob(run, this.getJobByJobId(jid));
				for (Sample cell : cellList) {
					Map<String,Object> childNode = new HashMap<>();
					
					childNode.put("name", "Cell: "+cell.getName());
					childNode.put("myid", cell.getId());
					childNode.put("type", "cell");
					childNode.put("jid", jid);
					childNode.put("children", "");
					
					children.add(childNode);
				}
				
				curNode.put("children", children);
			}
		}
		
		return curNode;
	}

	private List<Map<String,Object>> getFileNodesByLibrary(Sample library, Sample cell) throws SampleTypeException {
		List<Map<String,Object>> fileTypeNodes = new ArrayList<Map<String,Object>>();
		Set<FileGroup> fgSet;
		if (cell == null) {
			fgSet = library.getFileGroups();
		} else {
			fgSet = sampleService.getCellLibrary(cell, library).getFileGroups();
		}
		Map<FileType, Set<FileGroup>> ftMap = new HashMap<FileType, Set<FileGroup>>();
		for (FileGroup fg : fgSet) {
			FileType ft = fg.getFileType();
			if (!ftMap.containsKey(ft)) {
				ftMap.put(ft, new HashSet<FileGroup>());
			}
			
			Set<FileGroup> fgByTypeSet = ftMap.get(ft);
			fgByTypeSet.add(fg);
		}
		
		for (FileType ft : ftMap.keySet()) {
			Map<String,Object> ftNode = new HashMap<>();

			ftNode.put("name", "File Type: " + ft.getName());
			ftNode.put("myid", ft.getId());
			ftNode.put("type", "filetype-"+ft.getIName());
			ftNode.put("libid", library.getId());
			if (cell!=null)
				ftNode.put("cellid", cell.getId());
			
			List<Map<String,Object>> children = new ArrayList<Map<String,Object>>();
			Set<FileGroup> fgByTypeSet = ftMap.get(ft);
			for (FileGroup fg : fgByTypeSet) {
				Map<String,Object> fgNode = new HashMap<>();

				fgNode.put("name", "File Group: " + fg.getDescription());
				fgNode.put("myid", fg.getId());
				fgNode.put("type", "filegroup");
				fgNode.put("children", "");
				
				children.add(fgNode);
			}
			
			ftNode.put("children", children);
			
			fileTypeNodes.add(ftNode);
		}
		return fileTypeNodes;
	}

	@SuppressWarnings("unused")
	private List<Map<String,Object>> getFileNodesByCellLibrary(Sample cell, Sample library) throws SampleTypeException {
		List<Map<String,Object>> fileTypeNodes = new ArrayList<Map<String,Object>>();
		Set<FileGroup> fgSet = sampleService.getCellLibrary(cell, library).getFileGroups();
		Map<Integer, FileType> ftMap = new HashMap<Integer, FileType>();
		for (FileGroup fg : fgSet) {
			ftMap.put(fg.getFileTypeId(), fg.getFileType());
		}
		
		for (FileType ft : ftMap.values()) {
			Map<String,Object> ftNode = new HashMap<>();

			ftNode.put("name", "File Type: " + ft.getName());
			ftNode.put("myid", ft.getId());
			ftNode.put("type", "filetype-"+ft.getIName());
			ftNode.put("libid", library.getId());
			ftNode.put("cellid", cell.getId());
			ftNode.put("children", "");
			
			fileTypeNodes.add(ftNode);
		}
		return fileTypeNodes;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LinkedHashMap<String, Object> getJobDetailWithMeta(int jobId) throws Exception {
		
		LinkedHashMap<String, Object> jsDetails = new LinkedHashMap<String, Object>();
		
		Job job = getJobByJobId(jobId);
		if(job==null || job.getId()==null){
			  throw new Exception("listJobSamples.jobNotFound.label");
		}
		
		jsDetails.put(messageService.getMessage("job.name.label", Locale.US), job.getName());
		jsDetails.putAll(getExtraJobDetails(job));
	
		List<JobMeta> metaList = job.getJobMeta();
		Map <String, Map<String, String>> metaListMap = new HashMap<>();
		for (JobMeta mt : metaList) {
			String key = mt.getK();
			//logger.debug(Arrays.deepToString(metaNameSplit));
			
			try {
				String msg = messageService.getMessage("job."+key+".label", Locale.US);
				jsDetails.put(msg, mt.getV());
			} 
			catch (NoSuchMessageException e) {
				String[] metaKeySplit = key.split("\\.");
				//logger.debug(Arrays.deepToString(metaNameSplit));
				if(metaKeySplit.length == 1) {
					jsDetails.put(key, mt.getV());
				} else if (metaKeySplit.length == 2) {
					Map <String, String> subKeyMap = metaListMap.get(metaKeySplit[0]);
					if(subKeyMap == null) {
						subKeyMap = new HashMap<>();
						metaListMap.put(metaKeySplit[0], subKeyMap);
					}
					subKeyMap.put(metaKeySplit[1], mt.getV());
				}
			}
		}
		jsDetails.putAll(metaListMap);

		return jsDetails;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobStatusComment(Job job){
		if(job==null || job.getId()==null || job.getId().intValue()<=0)
			return null;
		LinkedHashMap<String,String> jobApprovalsMap = this.getJobApprovals(job);
		for(String jobApproveCode : jobApprovalsMap.keySet()){
			//if any single jobStatus is rejected, the rest are set to abandoned, so this job is withdrawn, so break
			if(! "rejected".equalsIgnoreCase(jobApprovalsMap.get(jobApproveCode)))
				continue;
			List<MetaMessage> jobApprovalCommentsList = this.getJobApprovalComments(jobApproveCode, job.getId());		
			if(jobApprovalCommentsList.size()>0){
				
				MetaMessage mm = jobApprovalCommentsList.get(jobApprovalCommentsList.size()-1);
				String currentStatusComment = mm.getValue();
				if(currentStatusComment != null && !currentStatusComment.isEmpty()){
					DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					return currentStatusComment + " (" + mm.getUser().getNameFstLst() + "; " + dateFormat.format(mm.getDate()) + ")";
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJobStatus(Job job){
		if(job==null || job.getId()==null)
			return "Unknown";
		String currentStatus = "Not Yet Set";
		//String approvalStatus = "Not Yet Set";
		LinkedHashMap<String,String> jobApprovalsMap = this.getJobApprovals(job);
		logger.debug("isJobActive for id=" + job.getId() + " : " + isJobActive(job));
		if(isJobActive(job)){
			currentStatus = "In Progress";
			for(String jobApproveCode : jobApprovalsMap.keySet()){
				if("awaitingResponse".equalsIgnoreCase(jobApprovalsMap.get(jobApproveCode))){
					currentStatus = "Awaiting Approval(s)";
					break;
				}
			}
		}
		else{
			currentStatus = "Completed";
			for(String jobApproveCode : jobApprovalsMap.keySet()){
				//if any single jobStatus is rejected, the rest are set to abandoned, so this job is withdrawn, so break
				if("rejected".equalsIgnoreCase(jobApprovalsMap.get(jobApproveCode))){
					if("piApprove".equals(jobApproveCode)){
							currentStatus = "Withdrawn By PI";
					}
					else if("daApprove".equals(jobApproveCode)){
						currentStatus = "Withdrawn By Dept.";
					}
					else if("fmApprove".equals(jobApproveCode)){
						currentStatus = "Withdrawn By Facility";
					}
					else {//should never occur
						currentStatus = "Withdrawn";
					}
					break;
				}
			}
		}
		return currentStatus;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Software> getSoftwareForJob(Job job){
		List<Software> softwareList = new ArrayList<Software>();
		for(JobSoftware js : job.getJobSoftware()){
			Software sw = js.getSoftware();
			softwareList.add(sw);
		}
		return softwareList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Sample, List<String>> decodeSamplePairs(String samplePairs, List<Sample> submittedSamplesList){
		Map<Sample, List<String>> samplePairsMap = new HashMap<Sample, List<String>>();
		if(samplePairs!=null && !samplePairs.isEmpty()){
			for(Sample cSample : submittedSamplesList){
				List<String> stringList = new ArrayList<String>();
				boolean atLeastOneAnalysisPairExists = false;
				List <Sample> tempSubmittedSamplesList = new ArrayList<Sample>(submittedSamplesList);//identical copy of submittedSamplesList (with identical order) 
				for(Sample tSample : tempSubmittedSamplesList){				
					String matchFound = "f";//false
					if(cSample.getId()==tSample.getId()){
						stringList.add("d");//disallowed
						continue;
					}
					String possiblePair = tSample.getId().toString() + ":" + cSample.getId().toString();
					for(String realPair : samplePairs.split(";")){
						if(realPair.equals(possiblePair)){
							matchFound = "t";//true
							atLeastOneAnalysisPairExists = true;
							break;
						}
					}
					stringList.add(matchFound);
				}
				if(atLeastOneAnalysisPairExists){//at least one "t" in the string
					samplePairsMap.put(cSample, stringList);
				}
			}
		}
		return samplePairsMap;
	}
	
	public void decodeSamplePairsWithReference(String samplePairs, List<Sample> submittedSamplesList, List<String> controlIsReferenceList, List<String> testIsReferenceList){
		if(samplePairs!=null && !samplePairs.isEmpty() && controlIsReferenceList != null && testIsReferenceList != null){
	 		if(samplePairs!=null && !samplePairs.isEmpty()){
				for(Sample sample : submittedSamplesList){		
					String matchFoundForControlIsReference = "f";
					String matchFoundForTestIsReference = "f";
					for(String realPair : samplePairs.split(";")){
						String[] stringArray = realPair.split(":");
						Integer T;
						try{
							T = Integer.valueOf(stringArray[0]);
						}catch(Exception e){T = null;}
						Integer C;
						try{
							C = Integer.valueOf(stringArray[1]);
						}catch(Exception e){C = null;}					
						
						if(C == null && T != null && sample.getId().intValue()==T.intValue()){
							matchFoundForControlIsReference = "t";
						}
						else if(T == null && C != null && sample.getId().intValue()==C.intValue()){
							matchFoundForTestIsReference = "t";
						}
					}
					controlIsReferenceList.add(matchFoundForControlIsReference);
					testIsReferenceList.add(matchFoundForTestIsReference);
				}
				boolean foundOne = false;
				for(String s : controlIsReferenceList){
					if(s.equals("t")){
						foundOne = true;
					}
				}
				if(!foundOne){
					controlIsReferenceList.clear();//never found a hit, so empty list
				}
				foundOne = false;
				for(String s2 : testIsReferenceList){
					if(s2.equals("t")){
						foundOne = true;
					}
				}
				if(!foundOne){
					testIsReferenceList.clear();
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isQcPerformedOrPreprocessingFailedForAtLeastOneCellLibraryForAllSubmittedSamples(Job job){
		List<Sample> submittedSamples = getSubmittedSamples(job);
		for (SampleSource cellLibrary: sampleService.getCellLibrariesForJob(job)){
			try{
				SampleSource cellLibraryPreprocessFailedOrPassedQc = null;
				try{
					sampleService.isCellLibraryPassedQC(cellLibrary);
					cellLibraryPreprocessFailedOrPassedQc = cellLibrary;
				} catch (MetaAttributeNotFoundException e){
					// no QC recorded against cell library so see if it failed pre-processing
					ExitStatus preProcessingStatus = sampleService.getCellLibraryPreprocessingStatus(cellLibrary);
					if (preProcessingStatus.isFailed() || preProcessingStatus.isTerminated())
						cellLibraryPreprocessFailedOrPassedQc = cellLibrary;
				}
				if (cellLibraryPreprocessFailedOrPassedQc != null){
					Sample library = sampleService.getLibrary(cellLibraryPreprocessFailedOrPassedQc);
					Sample submittedSample = library.getParent();
					if (submittedSample == null)
						submittedSample = library;
					submittedSamples.remove(submittedSample);
				}
			} catch (SampleTypeException e){
				logger.warn(e.getLocalizedMessage());
			}
		}
		if (submittedSamples.size() == 0)
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isAnySampleCurrentlyBeingProcessed(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job must be valid");
		if (!this.isJobActive(job)){
			logger.debug("job " + job.getId() + ": not active - returning false");
			return false;
		}
		if (this.isAggregationAnalysisBatchJob(job)){
			logger.debug("job " + job.getId() + ": aggregation analysis has been initiated - returning false;");
			return false;
		}
			
		// if cellLibraries exist check these first (for efficiency)
		for (SampleSource cellLibrary: sampleService.getCellLibrariesForJob(job)){
			try{
				List<Run> runs = runService.getRunsForPlatformUnit(sampleService.getPlatformUnitForCell(sampleService.getCell(cellLibrary)));
				if (runs.isEmpty()){
					logger.debug("job " + job.getId() + ": no runs - returning true");
					return true; // library on platform unit but not yet run
				}
				for (Run run : runs){
					if (runService.isRunActive(run)){
						logger.debug("job " + job.getId() + ": a run is active - returning true");
						return true; // the library is on an active run
					}
				}
				try{
					if (sampleService.isCellSequencedSuccessfully(sampleService.getCell(cellLibrary))){
						ExitStatus preProcessingStatus = sampleService.getCellLibraryPreprocessingStatus(cellLibrary);
						if (!preProcessingStatus.isCompleted() && !preProcessingStatus.isFailed() && !preProcessingStatus.isTerminated()){
							logger.debug("job " + job.getId() + ": the library has been run and it's cell has passed QC but has not completed pre-processing yet - returning true");
							return true; // the library has been run and passed QC but has not been pre-processed yet
						} else if (sampleService.isCellLibraryAwaitingQC(cellLibrary)){
							logger.debug("job " + job.getId() + ": the cell-library is awaiting QC - returning true");
							return true; 
						}
					}
				} catch (MetaAttributeNotFoundException e) {
					logger.debug("job " + job.getId() + ": the library has been run but has not been QCd yet - returning true");
					return true; 
				}
			} catch(SampleTypeException e){
				logger.warn("recieved unexpected SampleTypeException: " + e.getLocalizedMessage());
			} catch (SampleParentChildException e1){
				logger.warn("recieved unexpected SampleParentChildException: " + e1.getLocalizedMessage());
			} 
		}
		if (this.isJobWithLibrariesToGoOnPlatformUnit(job)){
			logger.debug("job " + job.getId() + ": At least one library is awaiting platform unit placement - returning true");
			return true; // At least one library is awaiting platform unit placement
		}
		if (this.isJobPendingApprovalOrQuote(job)){
			logger.debug("job " + job.getId() + ":  Job is awaiting approval - returning true");
			return true; // Job is awaiting approval
		}
		if (this.isJobAwaitingReceivingOfSamples(job)){
			logger.debug("job " + job.getId() + ": At least one sample hasn't been received - returning true");
			return true; // at least one sample hasn't been received
		}
		if (this.isJobAwaitingSampleQC(job)){
			logger.debug("job " + job.getId() + ": At least one sample is awaiting QC - returning true");
			return true; // at least one sample is awaiting QC
		}
		if (this.isJobAwaitingLibraryQC(job)){
			logger.debug("job " + job.getId() + ": At least one library is awaiting QC - returning true");
			return true; // At least one library is awaiting QC
		}
		if (this.isJobAwaitingLibraryCreation(job)){
			logger.debug("job " + job.getId() + " At least one library needs to be created - returning true");
			return true; // At least one library needs to be created
		}
		logger.debug("job " + job.getId() + ": seems no active samples in pipeline - returning false");
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initiateAggregationAnalysisBatchJob(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job must be valid");
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.JOB_ID, job.getId().toString());
		jobParameters.put(WaspJobParameters.BATCH_JOB_TASK, BatchJobTask.ANALYSIS_AGGREGATE);
		String workflowIname = job.getWorkflow().getIName();
		List<BatchJobProviding> plugins =  waspPluginRegistry.getPluginsHandlingArea(workflowIname, BatchJobProviding.class);
		logger.debug("Found " +  plugins.size()  + " plugins that handle area '" + workflowIname + "'");
		for (BatchJobProviding plugin : plugins) {
			String flowName = plugin.getBatchJobName(BatchJobTask.ANALYSIS_AGGREGATE);
			if (flowName == null){
				logger.warn("No " + BatchJobTask.ANALYSIS_AGGREGATE + " flow found for plugin handling " + workflowIname);
				continue;
			}
			logger.debug("Launching batch job '" + flowName + "' with parameters " + jobParameters.toString());
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( 
					new BatchJobLaunchContext(flowName, jobParameters) );
			try {
				sendOutboundMessage(batchJobLaunchMessageTemplate.build(), true);
			} catch (WaspMessageBuildingException e) {
				throw new MessagingException(e.getLocalizedMessage(), e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobDataTabViewing getTabViewPluginByJob(Job job) {

		String workflowIname = job.getWorkflow().getIName();
		List<JobDataTabViewing> plugins = waspPluginRegistry.getPluginsHandlingArea(workflowIname, JobDataTabViewing.class);
		Assert.assertTrue(plugins.size()==1 || plugins.size()==0);
		return plugins.get(0);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getActiveJobsWithNoSamplesCurrentlyBeingProcessed(){
		List<Job> activeJobList = this.getActiveJobs();
		List<Job> activeJobsWithNoSamplesCurrentlyBeingProcessed = new ArrayList<Job>();		
		for(Job job : activeJobList){
			if(!this.isAnySampleCurrentlyBeingProcessed(job)){
				activeJobsWithNoSamplesCurrentlyBeingProcessed.add(job);
			}
		}
		return activeJobsWithNoSamplesCurrentlyBeingProcessed;
	}

	public boolean isAggregationAnalysisBatchJob(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job must be valid");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		Set<String> taskSet = new HashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		taskSet.add(BatchJobTask.ANALYSIS_AGGREGATE);
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		parameterMap.put(WaspJobParameters.BATCH_JOB_TASK, taskSet);
		if (batchJobExplorer.getJobExecutions(parameterMap, true).size() > 0)
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTerminated(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		if (batchJobExplorer.getJobExecutions("default.waspJob.jobflow", parameterMap, true, ExitStatus.STOPPED).size() > 0)
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isFinishedSuccessfully(Job job){
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(job.getId(), "job Id cannot be null");
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		if (batchJobExplorer.getJobExecutions("default.waspJob.jobflow", parameterMap, true, ExitStatus.COMPLETED).size() > 0)
			return true;
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void terminate(Job job) throws WaspMessageBuildingException{//will need to provide a comment at some time
		updateJobStatus(job, WaspStatus.ABANDONED, WaspJobTask.NOTIFY_STATUS, "", true);

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJobApprovalStatusAndComment(String jobApproveCode, Job job, WaspStatus status, String comment) throws Exception{
		try{			
			this.setJobApprovalComment(jobApproveCode, job.getId(), comment.trim());//throws Exception
			this.updateJobApprovalStatus(jobApproveCode, job, status, comment.trim());//perform second, as it kicks off a message; throws WaspMessageBuildingException
		}catch(Exception e){
			throw new Exception();
		}
	}

	/**
	 * {@inheritDoc}
	 * @throws QuoteException 
	 * @throws WaspMessageBuildingException 
	 */
	@Override
	public void addNewQuote(Integer jobId, AcctQuote quoteForm, List<AcctQuoteMeta> metaList) throws QuoteException, WaspMessageBuildingException {
		Assert.assertParameterNotNullNotZero(jobId, "jobId cannot be null or zero");
		Job job = jobDao.getById(jobId);
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNullNotZero(job.getId(), "job with id="+jobId+" not found in database");
		Assert.assertParameterNotNull(quoteForm, "quoteForm cannot be null");
		Assert.assertParameterNotNull(quoteForm.getAmount(), "quoteForm.getAmount() cannot be null");
		//Assert.assertParameterNotNullNotEmpty(metaList, "metaList cannot be null or empty");
		quoteForm.setJobId(jobId);
		quoteForm.setId(null);//new one; must leave this - problem without it
		User user = authenticationService.getAuthenticatedUser();
		quoteForm.setUser(user);
		acctQuoteDao.save(quoteForm);	//the save has set the new id in quoteForm	
		Integer quoteId = quoteForm.getId();		
		if(quoteId==null||quoteId==0){		
			String str = "acctQuote not properly saved to database - invalid id";
			logger.warn(str);
			throw new QuoteException(str);
		}
		//might want to confirm the values in the meta are strings representing floats?? not currently checked, in case other fields are later added
		try{
			if(metaList!=null && !metaList.isEmpty()){
				this.acctQuoteMetaDao.setMeta(metaList, quoteId);
			}
		} catch (MetadataException e){
			logger.warn(e.getMessage());
			throw new QuoteException(e);
		}
		if (!job.getAcctQuote().contains(quoteForm)){//it will not contain it, as this is a new quote
					
			job.getAcctQuote().add(quoteForm);//no real reason to do this here
		}
		job.setCurrentQuote(quoteForm);	
		this.getJobDao().save(job);	
		if(this.isJobAwaitingQuote(job)){
			try{	
				this.updateJobQuoteStatus(job, WaspStatus.COMPLETED);	
			} catch (WaspMessageBuildingException e){
				logger.warn(e.getMessage());
				throw new WaspMessageBuildingException(e);
			}
		}
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public Job getJobAndSoftware(Job job) {
		job=jobDao.findById(job.getId());
		List<JobSoftware> jsl = job.getJobSoftware();
		logger.debug("job has " + jsl.size() + " software" );
		job.getJobMeta().size();
		return job;
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	public List<Sample> getLibraries(Job job){
		List<Sample> libraryList = new ArrayList<Sample>();
		for(JobSample js : job.getJobSample()){
			Sample s = js.getSample();//includes userSubmitted Macro, userSubmitted Library, facilityGenerated Library
			if(s.getSampleType().getIName().toLowerCase().contains("library")){
				libraryList.add(s);//userSubmitted Libraries and facilityGenerated Libraries
			  }
		}		
		return libraryList;		
	}
	
	/** 
	 * {@inheritDoc}
	 * @throws FileUploadException 
	 * @throws JSONException 
	 * @throws WaspMessageBuildingException 
	 * @throws QuoteException 
	 * @throws Exception 
	 */
	@Override
	public void createNewQuoteAndSaveQuoteFile(MPSQuote mpsQuote, File file, Float totalFinalCost, boolean saveQuoteAsJSON) throws FileUploadException, JSONException, QuoteException{
			Job job = this.getJobByJobId(mpsQuote.getJobId());
		
			Date now = new Date();
			DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");		 	   
	 	   	Random randomNumberGenerator = new Random(System.currentTimeMillis());
	 	   	//if this is a new quote, save quote; if invoice, save invoice
	 	   	FileGroup fileGroup = fileService.saveLocalQuoteOrInvoiceFile(job, file, "Job"+job.getId()+"_Quote_"+dateFormat.format(now)+".pdf", "Quote", randomNumberGenerator);

	 	   	//if this is a new quote, save quote; if invoice, save invoice
	 	   	AcctQuote acctQuote = new AcctQuote();
	 	   	acctQuote.setAmount(totalFinalCost);
	 	   	List<AcctQuoteMeta> acctQuoteMetaList = new ArrayList<AcctQuoteMeta>();
	 	   	AcctQuoteMeta acctQuoteMeta = new AcctQuoteMeta();
	 	   	acctQuoteMeta.setK("acctQuote.fileGroupId");
	 	   	acctQuoteMeta.setV(fileGroup.getId().toString());
	 	   	acctQuoteMetaList.add(acctQuoteMeta);
	 	   	if(saveQuoteAsJSON==true){
	 	   		AcctQuoteMeta acctQuoteMeta2 = new AcctQuoteMeta();
	 	   		acctQuoteMeta2.setK("acctQuote.json");
	 	   		acctQuoteMeta2.setV(mpsQuote.getAsJSON().toString());
	 	   		acctQuoteMetaList.add(acctQuoteMeta2);
	 	   	}
	 	   	try {
				this.addNewQuote(job.getId(), acctQuote, acctQuoteMetaList);
			} catch (WaspMessageBuildingException e) {
				throw new MessagingException(e.getLocalizedMessage());
			}
	}
	
	/** 
	 * {@inheritDoc}
	 * @throws QuoteException 
	 * @throws WaspMessageBuildingException 
	 */
	@Override
	public void createNewQuoteOrInvoiceAndUploadFile(Job job, MultipartFile mpFile, String fileDescription, Float totalCost) throws FileUploadException, QuoteException, WaspMessageBuildingException{
		if(!fileDescription.equalsIgnoreCase("quote") && !fileDescription.equalsIgnoreCase("invoice")){
			  throw new QuoteException(); 
		}
 	   	FileGroup fileGroup = fileService.uploadFileAndReturnFileGroup(mpFile, job, fileDescription, new Random(System.currentTimeMillis()));
 	   	//if this is a new quote, save quote; if invoice, save invoice
 	   	AcctQuote acctQuote = new AcctQuote();
 	   	acctQuote.setAmount(totalCost);
 	   	List<AcctQuoteMeta> acctQuoteMetaList = new ArrayList<AcctQuoteMeta>();
 	   	AcctQuoteMeta acctQuoteMeta = new AcctQuoteMeta();
 	   	acctQuoteMeta.setK("acctQuote.fileGroupId");
 	   	acctQuoteMeta.setV(fileGroup.getId().toString());
 	   	acctQuoteMetaList.add(acctQuoteMeta);
 	   	this.addNewQuote(job.getId(), acctQuote, acctQuoteMetaList);
	}
	
	/*
	 * 
	 * 
	 */
	@Override
	public Strategy getStrategy(String strategyType, Job job)throws Exception{
		Assert.assertParameterNotNull(job, "job cannot be null");
		Assert.assertParameterNotNull(strategyType, "strategyType cannot be null");
		Assert.assertParameterNotNullNotZero(job.getId(), "job with id="+job.getId()+" not found in database");
		return strategyService.getThisJobsStrategy(strategyType, job);
	}
	
	/*
	 * 
	 * 
	 */
	@Override
	public List<JobMeta> getJobMeta(Integer jobId){
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("jobId", jobId);
		return jobMetaDao.findByMap(m);
	}
}
