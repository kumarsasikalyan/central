package edu.yu.einstein.wasp.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.yu.einstein.wasp.exception.JobContextInitializationException;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobSoftware;
import edu.yu.einstein.wasp.model.ResourceCategory;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.model.Software;

/**
 * Class to provide easy access to job information including resource and selected software information 
 * @author andymac
 *
 */
public class WaspJobContext {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Job job; // contains userid. labid, workflowid
	
	private Map<ResourceType, SoftwareConfiguration> configuredSoftwareByType;
	
	public WaspJobContext(Job job) throws JobContextInitializationException {
		this.job = job;
		try{
			configuredSoftwareByType = new HashMap<ResourceType, SoftwareConfiguration>();
			for (JobSoftware js: job.getJobSoftware()){
				Software software = js.getSoftware();
				ResourceType softwareType = software.getResourceType();
				Map<String, String> parameters = MetaHelper.getMap(software.getIName(), job.getJobMeta());
				configuredSoftwareByType.put(softwareType, new SoftwareConfiguration(software, parameters));
			}
		} catch(Exception e){
			throw new JobContextInitializationException(e.getMessage(), e); 
		}
	}
	
	public Job getJob(){
		return this.job;
	}
	
	public ResourceCategory getResourceCategory(){
		return job.getJobResourcecategory().get(0).getResourceCategory(); // should only be one
	}
	
	/**
	 * Get a configured software instance based on softwareType e.g. the ResourceType for an 'aligner'. Returns null if not found
	 * @param getResourceType
	 * @return a {@link SoftwareConfiguration} object or null if no new found
	 */
	public SoftwareConfiguration getConfiguredSoftware(ResourceType softwareType){
		if (softwareType == null){
			logger.error("software ResourceType is null");
			return null;
		}
		if (! configuredSoftwareByType.containsKey(softwareType)){
			logger.error("Configured software does not contain an entry for ResourceType=" + softwareType.getIName());
			return null;
		}
		return configuredSoftwareByType.get(softwareType);
	}

	
	/**
	 * Get a configured software instance for all software types
	 * @param getResourceType
	 * @return Map<ResourceType, SoftwareConfiguration>
	 */
	public Map<ResourceType, SoftwareConfiguration> getConfiguredSoftware(){
		return configuredSoftwareByType;
	}
	

	
	
	
}
