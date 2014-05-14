/**
 * 
 */
package edu.yu.einstein.wasp.plugin.illumina.batch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.batch.annotations.RetryOnExceptionFixed;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.plugin.illumina.IlluminaIndexingStrategy;
import edu.yu.einstein.wasp.plugin.illumina.software.IlluminaHiseqSequenceRunProcessor;
import edu.yu.einstein.wasp.service.RunService;

/**
 * @author calder
 *
 */
@Component
public class SampleSheetTasklet extends WaspRemotingTasklet {
	
	private RunService runService;
	
	private int runId;
	private Run run;
	private IndexingStrategy method;
	
	@Autowired
	private IlluminaHiseqSequenceRunProcessor casava;

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 
	 */
	public SampleSheetTasklet(Integer runId, IndexingStrategy method) {
		this.runId = runId;
		if (! method.equals(IlluminaIndexingStrategy.TRUSEQ) && ! method.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL)) {
		    logger.error("unable to run illumina pipeline in mode " + method);
		    throw new WaspRuntimeException("unknown illumina pipeline mode: " + method);
		}
		this.method = method;
		logger.debug("SampleSheetTasklet with method type " + method);
	}
	
	public SampleSheetTasklet() {
		// proxy
	}

	@Override
	@Transactional("entityManager")
	public void doExecute(ChunkContext context) throws Exception {
		run = runService.getRunById(runId);
		logger.debug("preparing sample sheet for " + run.getName() + ":" + run.getPlatformUnit().getName());
		storeStartedResult(context, casava.doSampleSheet(run, method));
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
	
    @Override
    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        super.beforeStep(stepExecution);
    }
	
}