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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessagingException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.batch.core.extension.JobExplorerWasp;
import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.dao.FileDao;
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
import edu.yu.einstein.wasp.dao.SampleFileDao;
import edu.yu.einstein.wasp.dao.SampleJobCellSelectionDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.dao.UserDao;
import edu.yu.einstein.wasp.dao.WorkflowDao;
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.ParameterValueRetrievalException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.JobStatusMessageTemplate;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
import edu.yu.einstein.wasp.integration.messages.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.WaspTask;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;
import edu.yu.einstein.wasp.model.File;
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
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.SampleJobCellSelection;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.MetaMessageService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TaskService;
import edu.yu.einstein.wasp.service.WorkflowService;
import edu.yu.einstein.wasp.util.StringHelper;


@Service
@Transactional("entityManager")
public class JobServiceImpl extends WaspMessageHandlingServiceImpl implements JobService {

	private final String[] jobApproveArray = {"piApprove", "daApprove", "fmApprove"};
	
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
	
	public void setJobMetaDao(JobMetaDao jobMetaDao) {
		this.jobMetaDao = jobMetaDao;
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
	
	public void setSampleFileDao(SampleFileDao sampleFileDao) {
		this.sampleFileDao = sampleFileDao;	

	}
	
	public void setSampleTypeDao(SampleTypeDao sampleTypeDao) {
		this.sampleTypeDao = sampleTypeDao;	

	}
	
	public void setLogger(Logger logger) {
		
		this.logger = logger;
	}
	
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
	private AuthenticationService authenticationService;

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
	protected SampleFileDao sampleFileDao;

	@Autowired
	protected JobSampleDao jobSampleDao;
	
	@Autowired
	protected SampleTypeDao sampleTypeDao;
	
	@Autowired
	protected SampleSubtypeDao sampleSubtypeDao;

	@Autowired
	protected SampleSubtypeDao subSampleTypeDao;

	@Autowired
	protected FileDao fileDao;

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
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		List<Sample> submittedSamplesList = new ArrayList<Sample>();
		if(job != null && job.getJobId().intValue()>0){
			for(JobSample jobSample : job.getJobSample()){//jobSampleDao.getJobSampleByJobId(job.getJobId())){
				  logger.debug("jobSample: jobSampleId="+jobSample.getJobSampleId()+", jobId="+ jobSample.getJobId() + ", sampleId=" + jobSample.getSampleId());
				  Sample sample  = sampleDao.getSampleBySampleId(jobSample.getSampleId());//includes submitted samples that are macromolecules, submitted samples that are libraries, and facility-generated libraries generated from a macromolecule
				  logger.debug("sample: sampleId="+sample.getSampleId()+", parentId=" + sample.getParentId());
				  if(sample.getParentId() == null){//this sample is NOT a facility-generated library (by contrast, if sample.getParentId() != null this indicates a facility-generated library), so add it to the submittedSample list
					  submittedSamplesList.add(sample);
				  }
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
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		
		List<Sample> submittedSamplesNotYetReceivedList = new ArrayList<Sample>();
		
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getJobId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutions("wasp.sample.step.listenForSampleReceived", parameterMap, false, BatchStatus.STARTED);
		stepExecutions.addAll(batchJobExplorer.getStepExecutions("wasp.library.step.listenForLibraryReceived", parameterMap, false, BatchStatus.STARTED));
		for (StepExecution stepExecution: stepExecutions){
			if (!stepExecution.getJobExecution().isRunning())
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
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		
		List<Sample> submittedSamplesNotYetQCList = new ArrayList<Sample>();
		
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getJobId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		for (StepExecution stepExecution: batchJobExplorer.getStepExecutions("wasp.sample.step.sampleQC", parameterMap, false, BatchStatus.STARTED)){
			if (!stepExecution.getJobExecution().isRunning())
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
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		
		List<Sample> submittedLibrariesNotYetQCList = new ArrayList<Sample>();
		
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add(job.getJobId().toString());
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		for (StepExecution stepExecution: batchJobExplorer.getStepExecutions("wasp.library.step.libraryQC", parameterMap, false, BatchStatus.STARTED)){
			if (!stepExecution.getJobExecution().isRunning())
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
		// jobIds (so use '*'). Also only get those with a BatchStatus of STARTED. Then get the value of the job ids from the parameter
		Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		Set<String> jobIdStringSet = new HashSet<String>();
		jobIdStringSet.add("*");
		parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		List<JobExecution> jobExecutions = batchJobExplorer.getJobExecutions(parameterMap, true, BatchStatus.STARTED);
		logger.debug("getJobExecutions() returned " + jobExecutions.size() + " result(s)");
		for (JobExecution jobExecution: jobExecutions){
			try{
				String parameterVal = batchJobExplorer.getJobParameterValueByKey(jobExecution, WaspJobParameters.JOB_ID);
				Job job = jobDao.getJobByJobId(Integer.valueOf(parameterVal));
				if (job == null || job.getJobId() == null){
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
	public List<Job> getJobsAwaitingReceivingOfSamples(){
		
		List<Job> jobsAwaitingReceivingOfSamples = new ArrayList<Job>();
		
		for (Job job: getActiveJobs()){
			logger.debug("examining sample receive status of job with id='" + job.getJobId() + "'");
			if (! getSubmittedSamplesNotYetReceived(job).isEmpty()) // some samples not yet received
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
			if (! getSubmittedSamplesNotYetReceived(job).isEmpty()) // some samples not yet received
				return true;
		return false;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getJobsAwaitingSampleQC(){
		
		List<Job> jobsAwaitingQCOfSamples = new ArrayList<Job>();
		
		for (Job job: getActiveJobs()){
			logger.debug("examining sample QC status of job with id='" + job.getJobId() + "'");
			if (! getSubmittedSamplesNotYetQC(job).isEmpty()) // some samples not yet QCd
				jobsAwaitingQCOfSamples.add(job);
		}
		
		sortJobsByJobId(jobsAwaitingQCOfSamples);
		
		return jobsAwaitingQCOfSamples;
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
	public boolean isJobsAwaitingSampleQC(){
		for (Job job: getActiveJobs())
			if (! getSubmittedSamplesNotYetQC(job).isEmpty()) // some samples not yet received
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
			logger.debug("examining library QC status of job with id='" + job.getJobId() + "'");
			if (! getLibrariesNotYetQC(job).isEmpty()) // some samples not yet QCd
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
			if (! getLibrariesNotYetQC(job).isEmpty()) // some libraries not yet received
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
			        return arg0.getJobId().compareTo(arg1.getJobId());
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
			Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
			Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new HashSet<String>();
			jobIdStringSet.add(job.getJobId().toString());
			parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			if (!batchJobExplorer.getStepExecutions("step.piApprove", parameterMap, true, BatchStatus.STARTED).isEmpty())
				return true;
			return false;
		}
	
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobAwaitingDaApproval(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
			Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new HashSet<String>();
			jobIdStringSet.add(job.getJobId().toString());
			parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			if (!batchJobExplorer.getStepExecutions("step.daApprove", parameterMap, true, BatchStatus.STARTED).isEmpty())
				return true;
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobAwaitingFmApproval(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
			Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new HashSet<String>();
			jobIdStringSet.add(job.getJobId().toString());
			parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			if (!batchJobExplorer.getStepExecutions("step.fmApprove", parameterMap, true, BatchStatus.STARTED).isEmpty())
				return true;
			return false;
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isJobAwaitingQuote(Job job){
			Assert.assertParameterNotNull(job, "No Job provided");
			Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
			Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
			Set<String> jobIdStringSet = new HashSet<String>();
			jobIdStringSet.add(job.getJobId().toString());
			parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
			if (!batchJobExplorer.getStepExecutions("step.quote", parameterMap, true, BatchStatus.STARTED).isEmpty())
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
		  Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		  LinkedHashMap<String, String> extraJobDetailsMap = new LinkedHashMap<String, String>();

		  List<JobResourcecategory> jobResourceCategoryList = job.getJobResourcecategory();
		  for(JobResourcecategory jrc : jobResourceCategoryList){
			  if(jrc.getResourceCategory().getResourceType().getIName().equals("mps")){
				  extraJobDetailsMap.put("extraJobDetails.machine.label", jrc.getResourceCategory().getName());
				  break;
			  }
		  }
		  for(JobMeta jobMeta : job.getJobMeta()){
			  if(jobMeta.getK().indexOf("readLength") != -1){
				  extraJobDetailsMap.put("extraJobDetails.readLength.label", jobMeta.getV());
			  }
			  if(jobMeta.getK().indexOf("readType") != -1){
				  extraJobDetailsMap.put("extraJobDetails.readType.label", jobMeta.getV().toUpperCase());
			  }
		  }
		  
		  try{
			  Float price = new Float(job.getAcctJobquotecurrent().get(0).getAcctQuote().getAmount());
			  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+String.format("%.2f", price));
		  }
		  catch(Exception e){
			  logger.warn("JobServiceImpl::getExtraJobDetails(): " + e);
			  extraJobDetailsMap.put("extraJobDetails.quote.label", Currency.getInstance(Locale.getDefault()).getSymbol()+"?.??"); 
		  }	
		  
		  return extraJobDetailsMap;	  
	  }

	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public LinkedHashMap<String, String> getJobApprovals(Job job){
		  Assert.assertParameterNotNull(job, "No Job provided");
		  Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		  LinkedHashMap<String, String> jobApprovalsMap = new LinkedHashMap<String, String>();
		  
		  Map<String, Set<String>> parameterMap = new HashMap<String, Set<String>>();
		  Set<String> jobIdStringSet = new HashSet<String>();
		  jobIdStringSet.add(job.getJobId().toString());
		  parameterMap.put(WaspJobParameters.JOB_ID, jobIdStringSet);
		  // when getting stepExecutions from batch job explorer, get status from the most recently started one
		  // in case job was re-run. This is defensive programming as theoretically this shouldn't happen and there
		  // should only be one entry returned anyway.
		  //Did the PI or the designated lab manager (or some facility personnel acting on their behalf) approve this job?
		  //Only two possibilities: approved and rejected, which correspond to ExistStatus.Completed and ExitStatus.Stopped, respectively
		  
		  /*
		  List<StepExecution> stepExecutions =  batchJobExplorer.getStepExecutions("step.piApprove", parameterMap, true);
		  StepExecution stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(stepExecutions);
		  String piStatusLabel = "status.piApproval.label";
		  if (stepExecution == null){
			  jobApprovalsMap.put(piStatusLabel, "status.notYetSet.label");
		  }
		  else {
			  ExitStatus adminApprovalStatus = stepExecution.getExitStatus();
			  if(adminApprovalStatus.getExitCode().equals(ExitStatus.EXECUTING.getExitCode())){
				  jobApprovalsMap.put(piStatusLabel, "status.awaitingResponse.label");
			  }
			  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())){
				  jobApprovalsMap.put(piStatusLabel, "status.approved.label");
			  }
			  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.FAILED.getExitCode())){
				  jobApprovalsMap.put(piStatusLabel, "status.rejected.label");
			  }
			  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.STOPPED.getExitCode())){
				  jobApprovalsMap.put(piStatusLabel, "status.abandoned.label");
			  }
			  else{
				  jobApprovalsMap.put(piStatusLabel, "status.unknown.label");
			  }
		  }
		  
		  String daStatusLabel = "status.daApproval.label";
		  stepExecution = batchJobExplorer.getMostRecentlyStartedStepExecutionInList(
				  batchJobExplorer.getStepExecutions("step.daApprove", parameterMap, true)
				);
		  if (stepExecution == null){
			  jobApprovalsMap.put(daStatusLabel, "status.notYetSet.label");
		  }
		  else {
			  ExitStatus adminApprovalStatus = stepExecution.getExitStatus();
			  if(adminApprovalStatus.getExitCode().equals(ExitStatus.EXECUTING.getExitCode())){
				  jobApprovalsMap.put(daStatusLabel, "status.awaitingResponse.label");
			  }
			  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())){
				  jobApprovalsMap.put(daStatusLabel, "status.approved.label");
			  }
			  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.FAILED.getExitCode())){
				  jobApprovalsMap.put(daStatusLabel, "status.rejected.label");
			  }
			  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.STOPPED.getExitCode())){
				  jobApprovalsMap.put(daStatusLabel, "status.abandoned.label");
			  }
			  else{
				  jobApprovalsMap.put(daStatusLabel, "status.unknown.label");
			  }
		  }
		  */
		  
		  List<String> jobApproveList = new ArrayList<String>();
		  for(int i = 0; i < this.jobApproveArray.length; i++){
			  jobApproveList.add(jobApproveArray[i]);//piApprove, daApprove, fmApprove
		  }	  
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
				  if(adminApprovalStatus.getExitCode().equals(ExitStatus.EXECUTING.getExitCode())){
					  approveStatus = new String("awaitingResponse");
					  //jobApprovalsMap.put(piStatusLabel, "status.awaitingResponse.label");
				  }
				  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.COMPLETED.getExitCode())){
					  approveStatus = new String("approved");
					  //jobApprovalsMap.put(piStatusLabel, "status.approved.label");
				  }
				  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.FAILED.getExitCode())){//most likely not used anymore
					  approveStatus = new String("rejected");
					  //jobApprovalsMap.put(piStatusLabel, "status.rejected.label");
				  }
				  else if(adminApprovalStatus.getExitCode().equals(ExitStatus.STOPPED.getExitCode())){
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
	  /**
	   * {@inheritDoc}
	 * @throws WaspMessageBuildingException 
	   */
	  @Override
	  public Job createJobFromJobDraft(JobDraft jobDraft, User user) throws FileMoveException{
		  	Assert.assertParameterNotNull(jobDraft, "No JobDraft provided");
			Assert.assertParameterNotNullNotZero(jobDraft.getJobDraftId(), "Invalid JobDraft Provided");
			Assert.assertParameterNotNull(user, "No User provided");
			Assert.assertParameterNotNullNotZero(user.getUserId(), "Invalid User Provided");
		  	
			// Copies JobDraft to a new Job
			Job job = new Job();
			job.setUserId(user.getUserId());
			job.setLabId(jobDraft.getLabId());
			job.setName(jobDraft.getName());
			job.setWorkflowId(jobDraft.getWorkflowId());
			job.setIsActive(1);
			job.setCreatets(new Date());
			
			job.setViewablebylab(0); // TODO: get from lab? Not being used yet
			
			Job jobDb = jobDao.save(job); 
			
			// Saves the metadata
			for (JobDraftMeta jdm: jobDraft.getJobDraftMeta()) {
				JobMeta jobMeta = new JobMeta();
				jobMeta.setJobId(jobDb.getJobId());
				jobMeta.setK(jdm.getK());
				jobMeta.setV(jdm.getV());
			
				jobMetaDao.save(jobMeta); 
			}
			
			// save the software selected
			for (JobDraftSoftware jdr: jobDraft.getJobDraftSoftware()) {
				JobSoftware jobSoftware = new JobSoftware();
				jobSoftware.setJobId(jobDb.getJobId());
				jobSoftware.setSoftwareId(jdr.getSoftwareId());
			
				jobSoftwareDao.save(jobSoftware); 
			}
			
			// save the resource category selected
			for (JobDraftresourcecategory jdr: jobDraft.getJobDraftresourcecategory()) {
				JobResourcecategory jobResourceCategory = new JobResourcecategory();
				jobResourceCategory.setJobId(jobDb.getJobId());
				jobResourceCategory.setResourcecategoryId(jdr.getResourcecategoryId());
			
				jobResourcecategoryDao.save(jobResourceCategory); 
			}
			
			
			// Creates the JobUser Permission
			JobUser jobUser = new JobUser(); 
			jobUser.setUserId(user.getUserId());
			jobUser.setJobId(jobDb.getJobId());
			Role role = roleDao.getRoleByRoleName("js");
			jobUser.setRoleId(role.getRoleId());
			jobUserDao.save(jobUser);
			
			// added 10-20-11 by rob dubin: with job submission, add lab PI as job viewer ("jv")
			//note: could use similar logic in loop to assign jv to all the lab members
			Lab lab = labDao.getLabByLabId(jobDb.getLabId());		
			// if the pi is different from the job user
			if (jobUser.getUserId().intValue() != lab.getPrimaryUserId().intValue()) {
				JobUser jobUser2 = new JobUser();		
				jobUser2.setUserId(lab.getPrimaryUserId());//the lab PI
				jobUser2.setJobId(jobDb.getJobId());
				Role role2 = roleDao.getRoleByRoleName("jv");
				jobUser2.setRoleId(role2.getRoleId());
				jobUserDao.save(jobUser2);
			}
			
			// Job Cells (oldid, newobj)
			Map<Integer,JobCellSelection> jobDraftCellMap = new HashMap<Integer,JobCellSelection>();
			
			for (JobDraftCellSelection jdc: jobDraft.getJobDraftCellSelection()) {
				JobCellSelection jobCellSelection = new JobCellSelection();
				jobCellSelection.setJobId(jobDb.getJobId());
				jobCellSelection.setCellIndex(jdc.getCellIndex());
			
				JobCellSelection jobCellSelectionDb =	jobCellSelectionDao.save(jobCellSelection);	
			
				jobDraftCellMap.put(jdc.getJobDraftCellSelectionId(), jobCellSelectionDb);
			}
			
			// Create Samples
			List<Sample> samples = new ArrayList<Sample>();
			for (SampleDraft sd: jobDraft.getSampleDraft()) {
				Sample sample = new Sample();
				sample.setName(sd.getName()); 
				sample.setSampleTypeId(sd.getSampleTypeId()); 
				sample.setSampleSubtypeId(sd.getSampleSubtypeId()); 
				sample.setSubmitterLabId(jobDb.getLabId()); 
				sample.setSubmitterUserId(user.getUserId()); 
				sample.setSubmitterJobId(jobDb.getJobId()); 
				sample.setIsReceived(0);
				sample.setIsActive(1);
		
				Sample sampleDb = sampleDao.save(sample); 
				samples.add(sampleDb);
		
				// sample file
				if (sd.getFileId() != null) {
					SampleFile sampleFile = new SampleFile();
					sampleFile.setSampleId(sampleDb.getSampleId());
					sampleFile.setFileId(sd.getFileId());
		
					sampleFile.setIsActive(1);
		
					// TODO ADD NAME AND INAME
		
					sampleFileDao.save(sampleFile);
				}
		
				// Sample Draft Meta Data
				for (SampleDraftMeta sdm: sd.getSampleDraftMeta()) {
					SampleMeta sampleMeta = new SampleMeta();
		
					sampleMeta.setSampleId(sampleDb.getSampleId());	
					sampleMeta.setK(sdm.getK());	
					sampleMeta.setV(sdm.getV());	
					sampleMeta.setPosition(sdm.getPosition());	
		
					sampleMetaDao.save(sampleMeta); 
				}
			
				// Job Sample
				JobSample jobSample = new JobSample();
				jobSample.setJobId(jobDb.getJobId());
				jobSample.setSampleId(sampleDb.getSampleId());
			
				jobSampleDao.save(jobSample);
			
				for (SampleDraftJobDraftCellSelection sdc: sd.getSampleDraftJobDraftCellSelection()) {
					SampleJobCellSelection sampleJobCellSelection = new SampleJobCellSelection();
					sampleJobCellSelection.setSampleId(sampleDb.getSampleId());
					sampleJobCellSelection.setJobCellSelectionId(jobDraftCellMap.get(sdc.getJobDraftCellSelectionId()).getJobCellSelectionId());
					sampleJobCellSelection.setLibraryIndex(sdc.getLibraryIndex());
					sampleJobCellSelectionDao.save(sampleJobCellSelection);
				}
			}
			
//			TODO: CLEAN UP THIS HORRIBLE SHITE
//			// jobDraftFile -> jobFile
//			for(JobDraftFile jdf: jobDraft.getJobDraftFile()){
//				File file = jdf.getFile();
//				String folderPath = file.getAbsolutePathToFileFolder();
//				String absPath = file.getAbsolutePath();
//				java.io.File folder = new java.io.File(folderPath);
//				String destPath = folderPath.replaceFirst("/jd_"+jobDraft.getJobDraftId()+"$", "/j_"+jobDb.getJobId());
//				if (destPath.equals(folderPath)){
//					throw new FileMoveException("Cannot convert path from '"+destPath+"'");
//				}
//				try{
//					folder.renameTo(new java.io.File(destPath));
//				} catch (Exception e){
//					throw new FileMoveException("Cannot rename path '"+folderPath+"' to '"+destPath+"'");
//				}
//				String newAbsolutePath = absPath.replaceFirst("/jd_"+jobDraft.getJobDraftId(), "/j_"+jobDb.getJobId());
//				file.setFileURI(newAbsolutePath);
//				JobFile jobFile = new JobFile();
//				jobFile.setJob(jobDb);
//				jobFile.setFile(file);
//				jobFileDao.save(jobFile);
//			}
//						
			// update the jobdraft
			jobDraft.setStatus("SUBMITTED");
			jobDraft.setSubmittedjobId(jobDb.getJobId());
			jobDraftDao.save(jobDraft); 
			
			// initiate batch jobs in wasp-daemon
			logger.debug("calling initiateBatchJobForJobSubmission() for job with id='" + jobDb.getJobId() + "'");
			initiateBatchJobForJobSubmission(jobDb);
			for (Sample sample: samples){
				logger.debug("calling initiateBatchJobForSample() for sample with id='" + sample.getSampleId() + "'");
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
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		// send message to initiate job processing
		Map<String, String> jobParameters = new HashMap<String, String>();
		jobParameters.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
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
	public List<Job> getJobsWithLibrariesToGoOnPlatformUnit(ResourceCategory resourceCategory){
		List<Job> jobsFilteredByResourceCategory = new ArrayList<Job>();
		for (Job currentJob: getJobsWithLibrariesToGoOnPlatformUnit()){
			JobResourcecategory jrc = jobResourcecategoryDao.getJobResourcecategoryByResourcecategoryIdJobId(resourceCategory.getResourceCategoryId(), currentJob.getJobId());
			if(jrc!=null && jrc.getJobResourcecategoryId()!=null && jrc.getJobResourcecategoryId().intValue() != 0)
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
			for (Sample sample: job.getSample()){
				boolean foundLibrary = false;
				if (sampleService.isLibrary(sample)){
					try {
						if (sampleService.isLibraryAwaitingPlatformUnitPlacement(sample) && sampleService.isLibraryPassQC(sample)){
							jobsWithLibrariesToGoOnFlowCell.add(job);
							foundLibrary = true;
						}
					} catch (SampleTypeException e){
						logger.warn(e.getLocalizedMessage());
					}
				} else {
					if (sample.getChildren() == null) // no libraries made (TODO: make sure at least one is successful)
						continue;
					for (Sample library: sample.getChildren()){
						try{
							if (sampleService.isLibraryAwaitingPlatformUnitPlacement(library) && sampleService.isLibraryPassQC(library)){
								jobsWithLibrariesToGoOnFlowCell.add(job);
								foundLibrary = true;
								break;
							}
						} catch (SampleTypeException e){
							logger.warn(e.getLocalizedMessage());
						}
					}
				}
				if (foundLibrary)
					break;
			}
		}
		return jobsWithLibrariesToGoOnFlowCell;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsSubmittedOrViewableByUser(User user){
		Assert.assertParameterNotNull(user, "No User provided");
		Assert.assertParameterNotNullNotZero(user.getUserId(), "Invalid User Provided");
		
		List<Job> jobList = new ArrayList<Job>();
		List<JobUser> jobUserList = new ArrayList<JobUser>();
		
		Map m = new HashMap();
		m.put("UserId", user.getUserId().intValue());
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
		Integer sampleId = sample.getSampleId();
		Assert.assertParameterNotNullNotZero(sampleId, "Invalid Sample Provided");
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		boolean sampleIsInJob = false;
		for (Sample s: job.getSample()){
			if (s.getSampleId().equals(sampleId)){
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
		updateJobStatus(job, status, WaspJobTask.QUOTE);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobApprovalStatus(String jobApproveCode, Job job, WaspStatus status) throws WaspMessageBuildingException{
		
		if(jobApproveCode.equals("piApprove")){
			updateJobPiApprovalStatus(job, status);
		}
		else if(jobApproveCode.equals("daApprove")){
			updateJobDaApprovalStatus(job, status);
		}
		else if(jobApproveCode.equals("fmApprove")){
			updateJobFmApprovalStatus(job, status);
		}
		else{
			throw new WaspMessageBuildingException();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobDaApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.DA_APPROVE);
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobPiApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.PI_APPROVE);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateJobFmApprovalStatus(Job job, WaspStatus status) throws WaspMessageBuildingException{
		updateJobStatus(job, status, WaspJobTask.FM_APPROVE);
	}
	
	private void updateJobStatus(Job job, WaspStatus status, String task) throws WaspMessageBuildingException{
		// TODO: Write test!!
		Assert.assertParameterNotNull(job, "No Job provided");
		Assert.assertParameterNotNullNotZero(job.getJobId(), "Invalid Job Provided");
		Assert.assertParameterNotNull(status, "No Status provided");
		if (status != WaspStatus.COMPLETED && status != WaspStatus.ABANDONED)
			throw new InvalidParameterException("WaspStatus is null, or not COMPLETED or ABANDONED");
		Assert.assertParameterNotNull(task, "No Task provided");
		  
		JobStatusMessageTemplate messageTemplate = new JobStatusMessageTemplate(job.getJobId());
		messageTemplate.setTask(task);
		messageTemplate.setStatus(status); // sample received (COMPLETED) or abandoned (ABANDONED)
		try{
			sendOutboundMessage(messageTemplate.build(), false);
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
		  if(job.getJobId()==null || job.getJobId().intValue() <= 0){
			throw new Exception("listJobSamples.jobNotFound.label");			  
		  }
		  
		  User userPerformingThisAction = authenticationService.getAuthenticatedUser();
		  if(userPerformingThisAction.getUserId()==null || userPerformingThisAction.getUserId().intValue()<=0){
			  throw new Exception("listJobSamples.illegalOperation.label");
		  }
		  
		  Boolean userPerformingThisActionIsPermittedToAddJobViewers = false;
		  //does the webviewer have the authority to perform this function?
		  if(authenticationService.isSuperUser() //webviewer (person performing the action) is superuser so OK
				  ||  userPerformingThisAction.getUserId().intValue() == job.getUserId().intValue() //webViewer is the job submitter, so OK
				  || userPerformingThisAction.getUserId().intValue() == job.getLab().getPrimaryUserId().intValue()//webViewer is the job PI, so OK
			)
		  {
			  userPerformingThisActionIsPermittedToAddJobViewers = true; //superuser, job's submitter, job's PI
		  }
		  if(!userPerformingThisActionIsPermittedToAddJobViewers){
			  throw new Exception("listJobSamples.illegalOperation.label");			  
		  }
		  
		  if(newViewerEmailAddress==null || "".equals(newViewerEmailAddress.trim()) || ! StringHelper.isStringAValidEmailAddress(newViewerEmailAddress) ){
			  throw new Exception("listJobSamples.invalidFormatEmailAddress.label");
		  }
		  User newViewerToBeAddedToJob = userDao.getUserByEmail(newViewerEmailAddress.trim());
		  if(newViewerToBeAddedToJob.getUserId()==null || newViewerToBeAddedToJob.getUserId().intValue()<= 0){
			  throw new Exception("listJobSamples.userNotFoundByEmailAddress.label");	
		  }
		  JobUser jobUser = jobUserDao.getJobUserByJobIdUserId(jobId.intValue(), newViewerToBeAddedToJob.getUserId().intValue());
		  if(jobUser.getJobUserId()!=null && jobUser.getJobUserId().intValue() > 0){//viewer to be added is already a viewer for this job.
			  throw new Exception("listJobSamples.alreadyIsViewerOfThisJob.label");
		  }
		  Role role = roleDao.getRoleByRoleName("jv");
		  if(role.getRoleId()==null || role.getRoleId().intValue()<=0){
			  throw new Exception("listJobSamples.roleNotFound.label");
		  }
		  JobUser newJobUser = new JobUser();
		  newJobUser.setJob(job);
		  newJobUser.setUser(newViewerToBeAddedToJob);
		  newJobUser.setLastUpdUser(userPerformingThisAction.getUserId());
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
		  if(job.getJobId()==null || job.getJobId().intValue() <= 0 ){
			  throw new Exception("listJobSamples.jobNotFound.label");			  
		  }
		  User userToBeRemoved = userDao.getUserByUserId(userId.intValue());
		  if(userToBeRemoved.getUserId()==null || userToBeRemoved.getUserId().intValue() <= 0 ){//userToBeRemoved not found in the user table; odd.
			  throw new Exception("listJobSamples.userNotFound.label");			  
		  }

		  
		  User userPerformingThisAction = authenticationService.getAuthenticatedUser();
		  if(userPerformingThisAction.getUserId()==null || userPerformingThisAction.getUserId().intValue()<=0){
			  throw new Exception("listJobSamples.illegalOperation.label");
		  }
		  
		  Boolean userPerformingThisActionIsPermittedToRemoveJobViewers = false;
		  //does the webviewer have the authority to perform this function?
		  if(authenticationService.isSuperUser() //webviewer (person performing the action) is superuser so OK
				  ||  userPerformingThisAction.getUserId().intValue() == job.getUserId().intValue() //webViewer is the job submitter, so OK
				  || userPerformingThisAction.getUserId().intValue() == job.getLab().getPrimaryUserId().intValue()//webViewer is the job PI, so OK
				  || userPerformingThisAction.getUserId().intValue() == userToBeRemoved.getUserId().intValue()//webViewer is attempting to remove him/her self from list, which is allowed (so long as the webviewer is neither the job submitter or the job's PI).
			)
		  {
			  userPerformingThisActionIsPermittedToRemoveJobViewers = true; //superuser, job's submitter, job's PI
		  }
		  if(!userPerformingThisActionIsPermittedToRemoveJobViewers){
			  throw new Exception("listJobSamples.illegalOperation.label");			  
		  }
		  
		  //we checked that webviewer is authorized to do this. Now make certain that the webviewer is not trying to remove the job submitter or job PI. 
		  if(userToBeRemoved.getUserId().intValue() == job.getUserId().intValue()){//trying to remove job's submitter as viewer; not allowed
			  throw new Exception("listJobSamples.submitterRemovalIllegal.label");			  
		  }
		  if(userToBeRemoved.getUserId().intValue() == job.getLab().getPrimaryUserId().intValue()){//trying to remove job pi as viewer; not allowed
			  throw new Exception("listJobSamples.piRemovalIllegal.label");			  
		  }
		  
		  JobUser jobUser = jobUserDao.getJobUserByJobIdUserId(job.getJobId().intValue(), userToBeRemoved.getUserId().intValue());
		  if(jobUser.getJobUserId().intValue() <= 0){//jobuser not found for this job and this user in the jobuser table.
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
			metaMessageService.saveToGroup("facilityJobComments", "Facility Job Comment", comment, jobId, JobMeta.class, jobMetaDao);
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
						if(sampleJobCellSelection.getSampleId().intValue() == sample.getSampleId().intValue()){
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setJobApprovalComment(String jobApproveCode, Integer jobId, String comment) throws Exception{
		try{
			metaMessageService.saveToGroup(jobApproveCode + "Comment", "Job Approve Comment", comment, jobId, JobMeta.class, jobMetaDao);
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

	
}
