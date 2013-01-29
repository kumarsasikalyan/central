package edu.yu.einstein.wasp.daemon.batch.tasklets.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.SoftwareConfigurationException;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.integration.messages.tasks.BatchJobTask;
import edu.yu.einstein.wasp.integration.messages.templates.BatchJobLaunchMessageTemplate;
import edu.yu.einstein.wasp.integration.messaging.MessageChannelRegistry;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.ResourceType;
import edu.yu.einstein.wasp.plugin.WaspPlugin;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.SoftwareConfiguration;
import edu.yu.einstein.wasp.util.WaspJobContext;

public class WaspJobSoftwareLaunchTasklet implements Tasklet {
	
	private static Logger logger = LoggerFactory.getLogger("WaspJobSoftwareLaunchTasklet");
	
	private int messageTimeoutInMillis;
	
	/**
	 * Set the timeout when waiting for reply (in millis).  Default 5000 (5s).
	 */
	@Value(value="${wasp.message.timeout:5000}")
	public void setMessageTimeoutInMillis(int messageTimeout) {
		this.messageTimeoutInMillis = messageTimeout;
	}
	
	private DirectChannel launchChannel; // channel to send messages out of system
	
	@Autowired
	@Qualifier(MessageChannelRegistry.LAUNCH_MESSAGE_CHANNEL)
	public void setLaunchChannel(DirectChannel launchChannel) {
		this.launchChannel = launchChannel;
	}
	
	private JobService jobService;
	
	private SampleService sampleService;
	
	private WaspPluginRegistry waspPluginRegistry;
	
	private List<Integer> libraryCellIds;
	
	// jobId may be set via setter in which case it overrides any values associated with the libraryCells.
	// If not set, at initialization step an attempt is made to obtain a unique jobId across the supplied libraryCells.
	private Integer jobId;
	
	private ResourceType softwareResourceType;
	 
	@Autowired
	public void setWaspPluginRegistry(WaspPluginRegistry waspPluginRegistry) {
		this.waspPluginRegistry = waspPluginRegistry;
	}
	
	@Autowired
	public void setJobService(JobService jobService) {
		this.jobService = jobService;
	}
	
	@Autowired
	public void setSampleService(SampleService sampleService) {
		this.sampleService = sampleService;
	}
	
	public WaspJobSoftwareLaunchTasklet(List<Integer> libraryCellIds, ResourceType softwareResourceType) {
		this.libraryCellIds = libraryCellIds;
		this.softwareResourceType = softwareResourceType;
	}
	
	public WaspJobSoftwareLaunchTasklet(Integer libraryCellId, ResourceType softwareResourceType) {
		setLibraryCellId(libraryCellId);
		this.softwareResourceType = softwareResourceType;
	}

	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws Exception {
		WaspJobContext waspJobContext = new WaspJobContext(jobService.getJobByJobId(jobId));
		SoftwareConfiguration softwareConfig = waspJobContext.getConfiguredSoftware(softwareResourceType);
		if (softwareConfig == null){
			throw new SoftwareConfigurationException("No software could be configured for jobId=" + jobId + " with resourceType iname=" + softwareResourceType.getIName());
		}
		Map<String, String> jobParameters = softwareConfig.getParameters();
		jobParameters.put(WaspSoftwareJobParameters.LIBRARY_CELL_ID_LIST, WaspSoftwareJobParameters.getLibraryCellListAsParameterValue(libraryCellIds));
		Job job = jobService.getJobByJobId(jobId);
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(messageTimeoutInMillis);
		for (WaspPlugin plugin : waspPluginRegistry.getPluginsHandlingArea(job.getWorkflow().getIName())) {
			String flowName = plugin.getBatchJobName(BatchJobTask.GENERIC);
			BatchJobLaunchMessageTemplate batchJobLaunchMessageTemplate = new BatchJobLaunchMessageTemplate( 
					new BatchJobLaunchContext(flowName, jobParameters) );
			try {
				Message<BatchJobLaunchContext> launchMessage = batchJobLaunchMessageTemplate.build();
				logger.debug("Sending the following launch message via channel " + launchChannel + " : " + launchMessage);
				messagingTemplate.send(launchChannel, launchMessage);
			} catch (WaspMessageBuildingException e) {
				throw new MessagingException(e.getLocalizedMessage(), e);
			}
		}
		return RepeatStatus.FINISHED;
	}

	public void setLibraryCellId(Integer libraryCellId) {
		this.libraryCellIds = new ArrayList<Integer>();
		this.libraryCellIds.add(libraryCellId);
	}
	
	public void setLibraryCellIds(List<Integer> libraryCellIds) {
		this.libraryCellIds = libraryCellIds;
	}
	
	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}
	
	@PostConstruct
	public void init(){
		// if jobId is not set, get it from the first libraryCell in the list and check it is unique across all in the list
		// in this scenario (otherwise we have no idea which is supposed to be used)
		if (this.jobId == null){
			for (Integer libraryCellId: libraryCellIds){
				if (this.jobId == null){
					this.jobId = sampleService.getJobOfLibraryOnCell(
							sampleService.getSampleSourceDao().getSampleSourceBySampleSourceId(libraryCellId) ).getJobId();
					continue;
				}
				if (!sampleService.getJobOfLibraryOnCell(sampleService.getSampleSourceDao().getSampleSourceBySampleSourceId(libraryCellId)).getJobId()
						.equals(jobId))
					throw new RuntimeException("No master Wasp jobId was provided and the libraryCells do not all reference the same job so no master can be determined");
			}
		}
			
	}
	

}
