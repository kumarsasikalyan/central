/**
 * 
 */
package edu.yu.einstein.wasp.daemon.batch.tasklets.illumina;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.mps.illumina.IlluminaSequenceRunProcessor;
import edu.yu.einstein.wasp.service.RunService;

/**
 * @author calder
 *
 */
@Transactional("entityManager")
@Component
public class UpdateRunCompletedTasklet extends WaspTasklet {
	
	@Autowired
	private RunService runService;
	
	private int runId;
	private Run run;
	
	@Autowired
	private IlluminaSequenceRunProcessor casava;

	public UpdateRunCompletedTasklet() {
		
	}
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public UpdateRunCompletedTasklet(int runId) {
		this.runId = runId;
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.daemon.batch.tasklets.WaspTasklet#execute(org.springframework.batch.core.StepContribution, org.springframework.batch.core.scope.context.ChunkContext)
	 */
	@Override
	public RepeatStatus execute(StepContribution arg0, ChunkContext arg1) throws GridException {
		run = runService.getRunById(runId);
		logger.debug("Marking run " + run.getName() + ":" + run.getPlatformUnit().getName() + " as completed.");
		run.setFinished(new Date());
		runService.getRunDao().save(run);
		return RepeatStatus.FINISHED;
	}

	/**
	 * @return the runService
	 */
	public RunService getRunService() {
		return runService;
	}

	/**
	 * @param runService the runService to set
	 */
	@Autowired
	public void setRunService(RunService runService) {
		this.runService = runService;
	}

}
