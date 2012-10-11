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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.service.jta.platform.internal.WeblogicJtaPlatform;
import org.openqa.selenium.browserlaunchers.WindowsProxyManager;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.batch.core.extension.WaspBatchJobExplorer;
import edu.yu.einstein.wasp.batch.exceptions.ParameterValueRetrievalException;
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
import edu.yu.einstein.wasp.exception.FileMoveException;
import edu.yu.einstein.wasp.integration.messages.WaspJobParameters;
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
import edu.yu.einstein.wasp.model.Role;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleDraft;
import edu.yu.einstein.wasp.model.SampleDraftJobDraftCellSelection;
import edu.yu.einstein.wasp.model.SampleDraftMeta;
import edu.yu.einstein.wasp.model.SampleFile;
import edu.yu.einstein.wasp.model.SampleJobCellSelection;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.TaskService;

@Service
@Transactional
public class JobServiceImpl extends WaspServiceImpl implements JobService {

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
	private JobDraftDao jobDraftDao;
	
	@Autowired
	private SampleMetaDao sampleMetaDao;

	@Autowired
	private TaskService taskService;
	 
	@Autowired
	private JobMetaDao jobMetaDao;

	@Autowired
	protected LabDao labDao;

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
	protected WaspBatchJobExplorer batchJobExplorer;

	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Sample> getSubmittedSamples(Job job){
		
		List<Sample> submittedSamplesList = new ArrayList();
		if(job != null && job.getJobId().intValue()>0){
			for(JobSample jobSample : job.getJobSample()){
				  Sample sample  = jobSample.getSample();//includes submitted samples that are macromolecules, submitted samples that are libraries, and facility-generated libraries generated from a macromolecule
				  if(sample.getParent() == null){//this sample is NOT a facility-generated library (by contrast, if sample.getParent() != null this indicates a facility-generated library), so add it to the submittedSample list
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
	public List<Sample> getSubmittedSamplesAwaitingSubmission(Job job){
		
		List<Sample> submittedSamplesAwaitingSubmissionList = new ArrayList<Sample>();
		
		Map<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put(WaspJobParameters.JOB_ID, job.getJobId().toString());
		List<StepExecution> stepExecutions = batchJobExplorer.getStepExecutionsByStepNameAndParameterMap("wasp.sample.step.listenForSampleReceived", parameterMap);
		for (StepExecution se: stepExecutions){
			if (se.getExitStatus().equals(BatchStatus.STARTED)){
				Integer sampleId = null;
				try{
					sampleId = Integer.valueOf(batchJobExplorer.getJobParameterValueByKey(se, WaspJobParameters.SAMPLE_ID));
				} catch (ParameterValueRetrievalException e){
					logger.error(e.getMessage());
					continue;
				}
				Sample sample = sampleDao.getSampleBySampleId(sampleId);
				if (sample == null){
					logger.error("Sample with sample id '"+sampleId+"' does not have a match in the database!");
					continue;
				}
				submittedSamplesAwaitingSubmissionList.add(sample);
			}	
		}
		return submittedSamplesAwaitingSubmissionList;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getActiveJobs(){
		
		List<Job> activeJobList = new ArrayList<Job>();
		
		List<State> states = taskService.getJobCreatedStates(); //return taskDao.getStatesByTaskIName("Start Job", "CREATED");
		if(states==null){
			return activeJobList;
		}
		Set<Job> jobsSet = new HashSet<Job>();//to filter out any duplicates
		for(State state : states){
			List<Statejob> stateJobList = state.getStatejob();
			for(Statejob stateJob : stateJobList){
				jobsSet.add(stateJob.getJob());
			}
		}
		for(Job job : jobsSet){
			activeJobList.add(job);
		}
		
		sortJobsByJobId(activeJobList);
		
		return activeJobList;
	}
	
	 /**
	   * {@inheritDoc}
	   */
	@Override
	public List<Job> getJobsAwaitingSubmittedSamples(){
		
		List<Job> jobsAwaitingSubmittedSamplesList = new ArrayList<Job>();
		
		List<State> states = taskService.getSampleNotYetReceivedStates(); ////////return taskDao.getStatesByTaskIName("Receive Sample", "CREATED");
		if(states==null){
			return jobsAwaitingSubmittedSamplesList;
		}
		Set<Sample> sampleSet = new HashSet<Sample>();//to filter out any duplicates
		for(State state : states){
			List<Statesample> stateSampleList = state.getStatesample();
			for(Statesample stateSample : stateSampleList){
				sampleSet.add(stateSample.getSample());//all samples with task receive sample = created
			}
		}
		Set<Job> jobSet = new HashSet<Job>();
		for(Sample sample : sampleSet){
			List<JobSample> jobSampleList = sample.getJobSample();
			for(JobSample jobSample : jobSampleList){
				jobSet.add(jobSample.getJob());//unique set of jobs that have at least one sample with task receive sample = created
			}
		}
		
		for(Job job : jobSet){
			jobsAwaitingSubmittedSamplesList.add(job);
		}
		
		sortJobsByJobId(jobsAwaitingSubmittedSamplesList);
		
		return jobsAwaitingSubmittedSamplesList;
	}
	
	/**
	   * {@inheritDoc}
	   */
	  @Override
	  public void sortJobsByJobId(List<Job> jobs){
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
	  public Map<String, String> getExtraJobDetails(Job job){
		    
		  Map<String, String> extraJobDetailsMap = new HashMap<String, String>();

		  List<JobResourcecategory> jobResourceCategoryList = job.getJobResourcecategory();
		  for(JobResourcecategory jrc : jobResourceCategoryList){
			  if(jrc.getResourceCategory().getResourceType().getIName().equals("mps")){
				  extraJobDetailsMap.put("Machine", jrc.getResourceCategory().getName());
				  break;
			  }
		  }
		  for(JobMeta jobMeta : job.getJobMeta()){
			  if(jobMeta.getK().indexOf("readLength") != -1){
				  extraJobDetailsMap.put("Read Length", jobMeta.getV());
			  }
			  if(jobMeta.getK().indexOf("readType") != -1){
				  extraJobDetailsMap.put("Read Type", jobMeta.getV().toUpperCase());
			  }
		  }

		  // 6/11/12 added next section related to DA approval, PI approval, and the quote states
		  List<Statejob> statejobs = job.getStatejob();
		  for(Statejob statejob : statejobs){
			  State state = statejob.getState();
			  if(state.getTask().getIName().equals("DA Approval")){
				  if(state.getStatus().equals("CREATED")){
					  extraJobDetailsMap.put("DA Approval", "Awaiting Response");
				  }
				  else if(state.getStatus().equals("COMPLETED") || state.getStatus().equals("FINALIZED")){
					  extraJobDetailsMap.put("DA Approval", "Approved");
				  }
				  else if(state.getStatus().equals("ABANDONED")){
					  extraJobDetailsMap.put("DA Approval", "Rejected");
				  }
				  else{
					  extraJobDetailsMap.put("DA Approval", "Unknown");
				  }
			  }
			  if(state.getTask().getIName().equals("PI Approval")){
				  if(state.getStatus().equals("CREATED")){
					  extraJobDetailsMap.put("PI Approval", "Awaiting Response");
				  }
				  else if(state.getStatus().equals("COMPLETED") || state.getStatus().equals("FINALIZED")){
					  extraJobDetailsMap.put("PI Approval", "Approved");
				  }
				  else if(state.getStatus().equals("ABANDONED")){
					  extraJobDetailsMap.put("PI Approval", "Rejected");
				  }
				  else{
					  extraJobDetailsMap.put("PI Approval", "Unknown");
				  }
			  }
			  if(state.getTask().getIName().equals("Quote Job")){
				  if(state.getStatus().equals("CREATED")){
					  extraJobDetailsMap.put("Quote Job Price", "Awaiting Quote");
				  }
				  else if(state.getStatus().equals("COMPLETED") || state.getStatus().equals("FINALIZED")){
					  try{
						  Float price = new Float(job.getAcctJobquotecurrent().get(0).getAcctQuote().getAmount());
						  extraJobDetailsMap.put("Quote Job Price", "$"+String.format("%.2f", price));
					  }
					  catch(Exception e){
						  extraJobDetailsMap.put("Quote Job Price", "Unknown"); 
					  }					  
				  }
				  else {
					  extraJobDetailsMap.put("Quote Job Price", "Unknown");
				  }
			  }
		  }
		  if(extraJobDetailsMap.containsKey("DA Approval")==false){
			  extraJobDetailsMap.put("DA Approval", "Not Yet Set");
		  }
		  if(extraJobDetailsMap.containsKey("PI Approval")==false){
			  extraJobDetailsMap.put("PI Approval", "Not Yet Set");
		  }
		  if(extraJobDetailsMap.containsKey("Quote Job Price")==false){
			  extraJobDetailsMap.put("Quote Job Price", "Not Yet Set");
		  }
		  
		  return extraJobDetailsMap;	  
	  }
	  
	  /**
	   * {@inheritDoc}
	   */
	  @Override
	  public Job createJobFromJobDraft(JobDraft jobDraft, User user) throws FileMoveException{
		  	
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
			for (SampleDraft sd: jobDraft.getSampleDraft()) {
				// existing sample...
				Sample sampleDb;
			
				if (sd.getSourceSampleId() != null) {
					sampleDb = sampleDao.getSampleBySampleId(sd.getSourceSampleId());
				} else { 
			
					Sample sample = new Sample();
					sample.setName(sd.getName()); 
					sample.setSampleTypeId(sd.getSampleTypeId()); 
					sample.setSampleSubtypeId(sd.getSampleSubtypeId()); 
					sample.setSubmitterLabId(jobDb.getLabId()); 
					sample.setSubmitterUserId(user.getUserId()); 
					sample.setSubmitterJobId(jobDb.getJobId()); 
					sample.setIsReceived(0);
					sample.setIsActive(1);
			
					sampleDb = sampleDao.save(sample); 
			
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
			
			State state = new State(); 
			
			Task jobCreateTask = taskDao.getTaskByIName("Start Job");
			state.setTaskId(jobCreateTask.getTaskId());
			state.setName(jobCreateTask.getName());
			state.setStartts(new Date());
			state.setStatus("CREATED"); 
			stateDao.save(state);
			
			Statejob statejob = new Statejob();
			statejob.setStateId(state.getStateId());
			statejob.setJobId(job.getJobId());
			statejobDao.save(statejob);
			
			// update the jobdraft
			jobDraft.setStatus("SUBMITTED");
			jobDraft.setSubmittedjobId(jobDb.getJobId());
			jobDraftDao.save(jobDraft); 
			
			// jobDraftFile -> jobFile
			for(JobDraftFile jdf: jobDraft.getJobDraftFile()){
				File file = jdf.getFile();
				String folderPath = file.getAbsolutePathToFileFolder();
				String absPath = file.getAbsolutePath();
				java.io.File folder = new java.io.File(folderPath);
				String destPath = folderPath.replaceFirst("/jd_"+jobDraft.getJobDraftId()+"$", "/j_"+jobDb.getJobId());
				if (destPath.equals(folderPath)){
					throw new FileMoveException("Cannot convert path from '"+destPath+"'");
				}
				try{
					folder.renameTo(new java.io.File(destPath));
				} catch (Exception e){
					throw new FileMoveException("Cannot rename path '"+folderPath+"' to '"+destPath+"'");
				}
				String newAbsolutePath = absPath.replaceFirst("/jd_"+jobDraft.getJobDraftId(), "/j_"+jobDb.getJobId());
				file.setAbsolutePath(newAbsolutePath);
				JobFile jobFile = new JobFile();
				jobFile.setJob(jobDb);
				jobFile.setFile(file);
				jobFileDao.save(jobFile);
			}
			
			return jobDb;
	  }
	  
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsWithLibraryCreatedTask(){
		
		Map stateMap = new HashMap(); 
		Task task = taskDao.getTaskByIName("Create Library");
		if(task == null || task.getTaskId() == null){
			//waspErrorMessage("platformunit.taskNotFound.error"); maybe throw exception?????
		}
		stateMap.put("taskId", task.getTaskId()); 	
		stateMap.put("status", "CREATED"); 
		List<State> stateList = stateDao.findByMap(stateMap);
		
		Set<Job> jobs = new HashSet<Job>();//use set to avoid duplicates
		for(State state : stateList){
			List<Statejob> stateJobList = state.getStatejob();
			for(Statejob stateJob : stateJobList){
				jobs.add(stateJob.getJob());
			}
		}
		return new ArrayList<Job>(jobs);//return as list rather than as set
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsWithLibrariesToGoOnFlowCell(){
		
		Map stateMap = new HashMap(); 
		Task task = taskDao.getTaskByIName("assignLibraryToPlatformUnit");
		if(task == null || task.getTaskId() == null){
			//waspErrorMessage("platformunit.taskNotFound.error"); maybe throw exception?????
		}
		stateMap.put("taskId", task.getTaskId()); 	
		stateMap.put("status", "CREATED"); 
		List<State> stateList = stateDao.findByMap(stateMap);
		
		Set<Job> jobs = new HashSet<Job>();//use set to avoid duplicates
		for(State state : stateList){
			List<Statejob> stateJobList = state.getStatejob();
			for(Statejob stateJob : stateJobList){
				jobs.add(stateJob.getJob());
			}
		}
		
		return new ArrayList<Job>(jobs);//return as list rather than as set
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Job> getJobsSubmittedOrViewableByUser(User user){
		
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
}
