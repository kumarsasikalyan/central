package edu.yu.einstein.wasp.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import edu.yu.einstein.wasp.batch.launch.BatchRelaunchRunningJobsOnStartup;
import edu.yu.einstein.wasp.batch.launch.WaspBatchRelaunchRunningJobsOnStartup;

public class StartDaemon {

	private static final Logger logger = LoggerFactory.getLogger(StartDaemon.class);
	
	public static void main(final String[] args) throws Exception {
		@SuppressWarnings("unused")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("META-INF/spring/daemon-launch-context.xml");
		logger.info("\n\nGoing to restart any batch jobs that might have been running before last shutdown...\n\n");
		BatchRelaunchRunningJobsOnStartup batchRelaunchRunningJobsOnStartup = ctx.getBean(WaspBatchRelaunchRunningJobsOnStartup.class);
		batchRelaunchRunningJobsOnStartup.doLaunchAllRunningJobs();
		logger.info("\n\nSpring Batch Daemon Application Launched Successfully...\n\n");
		
	}
}