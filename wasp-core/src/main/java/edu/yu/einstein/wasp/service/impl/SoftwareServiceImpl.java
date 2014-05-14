package edu.yu.einstein.wasp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.dao.JobSoftwareDao;
import edu.yu.einstein.wasp.dao.SoftwareDao;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.Software;
import edu.yu.einstein.wasp.service.SoftwareService;

@Service
@Transactional("entityManager")
public class SoftwareServiceImpl extends WaspServiceImpl implements SoftwareService{
	
	@Autowired
	private SoftwareDao softwareDao;
	
	@Autowired 
	private JobSoftwareDao jobSoftwareDao;

	public SoftwareServiceImpl() {
		
	}
	
	@Override
	public Software getById(Integer id){
		return softwareDao.getById(id);
	}
	
	@Override
	public List<Software> getAll(){
		return softwareDao.findAll();
	}
	
	@Override
	public List<Software> getAllSoftwareForJob(Job job){
		List<Software> s = new ArrayList<>();
		Map<String, Integer> m = new HashMap<>();
		m.put("jobId", job.getId());
		List<JobSoftware> jsForJob = jobSoftwareDao.findByMap(m);
		if (jsForJob != null)
			for (JobSoftware js : jsForJob)
				s.add(js.getSoftware());
		return s;
	}
	
	@Override
	public JobSoftware saveJobSoftware(JobSoftware jobSoftware){
		return jobSoftwareDao.save(jobSoftware);
	}

}