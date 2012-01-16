
/**
 *
 * JobServiceImpl.java 
 * @author echeng (table2type.pl)
 *  
 * the JobService Implmentation 
 *
 *
 **/

package edu.yu.einstein.wasp.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.service.JobService;

@Service
public class JobServiceImpl extends WaspServiceImpl<Job> implements JobService {

	/**
	 * jobDao;
	 *
	 */
	private JobDao jobDao;

	/**
	 * setJobDao(JobDao jobDao)
	 *
	 * @param jobDao
	 *
	 */
	@Autowired
	public void setJobDao(JobDao jobDao) {
		this.jobDao = jobDao;
		this.setWaspDao(jobDao);
	}

	/**
	 * getJobDao();
	 *
	 * @return jobDao
	 *
	 */
	public JobDao getJobDao() {
		return this.jobDao;
	}


  public Job getJobByJobId (final int jobId) {
    return this.getJobDao().getJobByJobId(jobId);
  }

  public Job getJobByNameLabId (final String name, final int labId) {
    return this.getJobDao().getJobByNameLabId(name, labId);
  }

  public Map<Integer,List<Job>> getJobSamplesByWorkflow(final int workflowId) {
	  return this.getJobDao().getJobSamplesByWorkflow(workflowId);
  }
}

