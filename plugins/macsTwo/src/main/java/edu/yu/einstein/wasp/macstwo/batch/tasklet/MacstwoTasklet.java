/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.macstwo.batch.tasklet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.integration.messages.MacstwoSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.software.Macstwo;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author 
 * 
 */
public class MacstwoTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	private Integer jobId;
	private List<Integer> testCellLibraryIdList;//treated, such as IP
	private List<Integer> controlCellLibraryIdList;//contol, such as input 
	private Integer modelScriptGId;
	private Integer peaksXlsGId;
	private Integer narrowPeaksBedGId;
	private Integer summitsBedGId;
	private Integer treatPileupBedGraphGId;
	private Integer controlLambdaBedGraphGId;
	private Integer testSampleId;
	private Integer controlSampleId;
	
	private StepExecution stepExecution;
	
	@Autowired
	private FileType macs2ModelScriptFileType;
	@Autowired
	private FileType macs2PeaksXlsFileType;
	@Autowired
	private FileType macs2NarrowPeaksBedFileType;
	@Autowired
	private FileType macs2SummitsBedFileType;
	@Autowired
	private FileType macs2TreatPileupBedGraphFileType;
	@Autowired
	private FileType macs2ControlLambdaBedGraphFileType;
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private FileType bamFileType;

	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileService fileService;

	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private Macstwo macs2;//TODO: how????
	

	public MacstwoTasklet() {
		// proxy
	}

	public MacstwoTasklet(String jobIdAsString, String testCellLibraryIdListAsString, String controlCellLibraryIdListAsString) throws Exception {
		logger.debug("Starting MacstwoTasklet constructor");
		logger.debug("jobIdAsString: " + jobIdAsString);
		logger.debug("testCellLibraryIdListAsString: " + testCellLibraryIdListAsString);
		logger.debug("controlCellLibraryIdListAsString: " + controlCellLibraryIdListAsString);
		this.jobId = new Integer(jobIdAsString);		
		Assert.assertTrue(this.jobId != null);
		Assert.assertTrue(this.jobId > 0);
		this.testCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(testCellLibraryIdListAsString);//should be all from same job
		Assert.assertTrue(!this.testCellLibraryIdList.isEmpty());
		//oddly enough (and not expected from the code), WaspSoftwareJobParameters.getLibraryCellIdList(controlCellLibraryIdListAsString)
		//throws an exception if controlCellLibraryIdListAsString is an empty string, thus the need for the if-else statement
		if(controlCellLibraryIdListAsString==null || controlCellLibraryIdListAsString.isEmpty()){
			this.controlCellLibraryIdList = new ArrayList<Integer>();
		}
		else{
			this.controlCellLibraryIdList = WaspSoftwareJobParameters.getLibraryCellIdList(controlCellLibraryIdListAsString);//may be empty
		}
		logger.debug("Ending MacstwoTasklet constructor");
	}
	
	//TODO: remove this next method
	@Override
	@Transactional("entityManager")
	public RepeatStatus execute(StepContribution contrib, ChunkContext context) throws Exception {
		this.doExecute(context);
		return RepeatStatus.FINISHED;
	}

	/**
	 * 
	 * @param contrib
	 * @param context
	 * @return
	 * @throws Exception
	 */
	@Transactional("entityManager")
	@Override
	public void doExecute(ChunkContext context) throws Exception {
		
		logger.debug("Starting MacstwoTasklet execute");		

		Map<String,Object> jobParametersMap = context.getStepContext().getJobParameters();		
		for (String key : jobParametersMap.keySet()) {
			logger.debug("jobParameters Key: " + key + " Value: " + jobParametersMap.get(key).toString());
		}
		Map<String,Object> jobExecutionContextMap = context.getStepContext().getJobExecutionContext();		
		for (String key : jobExecutionContextMap.keySet()) {
			logger.debug("jobExecutionContextMap Key: " + key + " Value: " + jobExecutionContextMap.get(key).toString());
		}
		
		SampleSource firstTestCellLibrary = sampleService.getCellLibraryBySampleSourceId(this.testCellLibraryIdList.get(0));
		Sample testSample = sampleService.getLibrary(firstTestCellLibrary);//all these cellLibraries are from the same library or macromoleucle
		while(testSample.getParent()!=null){
			testSample = sampleService.getSampleById(testSample.getParentId());
		}
		logger.debug("testSample.name = " + testSample.getName());		
		this.testSampleId = testSample.getId();

		List<FileHandle> testFileHandleList = new ArrayList<FileHandle>();		
		for(Integer id : this.testCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			logger.debug("test fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					testFileHandleList.add(fileHandle);
					logger.debug("test fileHandle = " + fileHandle.getFileName());
				}				
			}
		}
		Assert.assertTrue(!testFileHandleList.isEmpty());
		logger.debug("test bam files size = " + testFileHandleList.size());

		Sample controlSample = null;
		if(!controlCellLibraryIdList.isEmpty()){
			controlSample = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(controlCellLibraryIdList.get(0)));//all these cellLibraries are from the same library or macromoleucle
			while(controlSample.getParent()!=null){
				controlSample = controlSample.getParent();
			}
		}
		if(controlSample==null){
			logger.debug("controlSample IS NULL");
			this.controlSampleId = 0;
		}
		else{
			logger.debug("controlSample.name = " + controlSample.getName());
			this.controlSampleId = controlSample.getId();
		}		
		
		List<FileHandle> controlFileHandleList = new ArrayList<FileHandle>();
		for(Integer id : this.controlCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			logger.debug("control fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					controlFileHandleList.add(fileHandle);//can be empty
					logger.debug("control fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		logger.debug("controlFileHandleList.size = " + controlFileHandleList.size());
			
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		stepContext.put("testSampleId", this.testSampleId); //place in the step context
		stepContext.put("controlSampleId", this.controlSampleId); //place in the step context	
		
		String prefixForFileName = "TEST_" + testSample.getName().replaceAll("\\s+", "_") + "_CONTROL_";
		if(controlSample == null){
			prefixForFileName = prefixForFileName + "none";
		}
		else{
			prefixForFileName = prefixForFileName + controlSample.getName().replaceAll("\\s+", "_");
		}
		logger.debug("prefixForFileName = " + prefixForFileName);
		logger.debug("preparing to generate workunit");
		WorkUnit w = macs2.getPeaks(prefixForFileName, testFileHandleList, controlFileHandleList, jobParametersMap);//configure
		logger.debug("OK, workunit has been generated");

		///*		
		  
		stepExecution.getExecutionContext().put("jobId", this.jobId);//in case of crash
		 
		FileGroup modelScriptG = new FileGroup();
		FileHandle modelScript = new FileHandle();
		modelScript.setFileName(prefixForFileName + "_model.r");//will eventually run Rscript on this file to generate pdf
		modelScript = fileService.addFile(modelScript);
		modelScriptG.addFileHandle(modelScript);
		modelScriptG.setFileType(macs2ModelScriptFileType);
		modelScriptG.setDescription(modelScript.getFileName());
		modelScriptG = fileService.addFileGroup(modelScriptG);
		this.modelScriptGId = modelScriptG.getId();
		stepExecution.getExecutionContext().put("modelScriptGId", modelScriptGId);//in case of crash
		
		FileGroup peaksXlsG = new FileGroup();
		FileHandle peaksXls = new FileHandle();
		peaksXls.setFileName(prefixForFileName + "_peaks.xls");
		peaksXls = fileService.addFile(peaksXls);
		peaksXlsG.addFileHandle(peaksXls);
		peaksXlsG.setFileType(macs2PeaksXlsFileType);
		peaksXlsG.setDescription(peaksXls.getFileName());
		peaksXlsG = fileService.addFileGroup(peaksXlsG);
		this.peaksXlsGId = peaksXlsG.getId();
		stepExecution.getExecutionContext().put("peaksXlsGId", peaksXlsGId);//in case of crash		
		
		FileGroup narrowPeaksBedG = new FileGroup();
		FileHandle narrowPeaksBed = new FileHandle();
		narrowPeaksBed.setFileName(prefixForFileName + "_peaks.narrowPeak");
		narrowPeaksBed = fileService.addFile(narrowPeaksBed);
		narrowPeaksBedG.addFileHandle(narrowPeaksBed);
		narrowPeaksBedG.setFileType(macs2NarrowPeaksBedFileType);
		narrowPeaksBedG.setDescription(narrowPeaksBed.getFileName());
		narrowPeaksBedG = fileService.addFileGroup(narrowPeaksBedG);
		this.narrowPeaksBedGId = narrowPeaksBedG.getId();
		stepExecution.getExecutionContext().put("narrowPeaksBedGId", narrowPeaksBedGId);//in case of crash
	
		FileGroup summitsBedG = new FileGroup();
		FileHandle summitsBed = new FileHandle();
		summitsBed.setFileName(prefixForFileName + "_summits.bed");
		summitsBed = fileService.addFile(summitsBed);
		summitsBedG.addFileHandle(summitsBed);
		summitsBedG.setFileType(macs2SummitsBedFileType);
		summitsBedG.setDescription(summitsBed.getFileName());
		summitsBedG = fileService.addFileGroup(summitsBedG);
		this.summitsBedGId = summitsBedG.getId();		
		stepExecution.getExecutionContext().put("summitsBedGId", summitsBedGId);//in case of crash
		
		FileGroup treatPileupBedGraphG = new FileGroup();
		FileHandle treatPileupBedGraph = new FileHandle();
		treatPileupBedGraph.setFileName(prefixForFileName + "_treat_pileup.bdg");
		treatPileupBedGraph = fileService.addFile(treatPileupBedGraph);
		treatPileupBedGraphG.addFileHandle(treatPileupBedGraph);
		treatPileupBedGraphG.setFileType(macs2TreatPileupBedGraphFileType);
		treatPileupBedGraphG.setDescription(treatPileupBedGraph.getFileName());
		treatPileupBedGraphG = fileService.addFileGroup(treatPileupBedGraphG);
		this.treatPileupBedGraphGId = treatPileupBedGraphG.getId();
		stepExecution.getExecutionContext().put("treatPileupBedGraphGId", treatPileupBedGraphGId);//in case of crash
	
		FileGroup controlLambdaBedGraphG = new FileGroup();
		FileHandle controlLambdaBedGraph = new FileHandle();
		controlLambdaBedGraph.setFileName(prefixForFileName + "_control_lambda.bdg");
		controlLambdaBedGraph = fileService.addFile(controlLambdaBedGraph);
		controlLambdaBedGraphG.addFileHandle(controlLambdaBedGraph);
		controlLambdaBedGraphG.setFileType(macs2ControlLambdaBedGraphFileType);
		controlLambdaBedGraphG.setDescription(controlLambdaBedGraph.getFileName());
		controlLambdaBedGraphG = fileService.addFileGroup(controlLambdaBedGraphG);
		this.controlLambdaBedGraphGId = controlLambdaBedGraphG.getId();
		stepExecution.getExecutionContext().put("controlLambdaBedGraphGId", controlLambdaBedGraphGId);//in case of crash

		w.getResultFiles().add(modelScriptG);
		w.getResultFiles().add(peaksXlsG);
		w.getResultFiles().add(narrowPeaksBedG);
		w.getResultFiles().add(summitsBedG);
		w.getResultFiles().add(treatPileupBedGraphG);
		w.getResultFiles().add(controlLambdaBedGraphG);
		
		w.setResultsDirectory(WorkUnit.RESULTS_DIR_PLACEHOLDER + "/" + this.jobId.toString());
   
		GridResult result = gridHostResolver.execute(w);
		
		//place the grid result in the step context
		storeStartedResult(context, result);
		
		//*/
		
		//logger.debug("getting ready to thow rob-generated exception");
		//if(1==1){
		//	throw new Exception("throwing Rob-generated exception in MacstwoTasklet.execute()");
		//}
		//logger.debug("just threw rob-generated exception");
		
		/*THIS IS FOR TESTING ONLY !!!!!!!!!!!!!
		logger.debug("****    ******************************************************************");
		logger.debug("just about to declare FINISHED in the macstwoTasklet, to see if we move forward to the generateModel tasklet");
		logger.debug("******    ****************************************************************");
		
		stepExecution.getExecutionContext().put(MacstwoSoftwareJobParameters.MODEL_SCRIPT_FILEGROUP_ID, "38");//TODO remove this line
		//TODO: comment out line immediately above and uncomment next three lines for production
		//Assert.assertTrue(this.modelScriptGId != null);
		//Assert.assertTrue(this.modelScriptGId > 0);
		//stepExecution.getExecutionContext().put(MacstwoSoftwareJobParameters.MODEL_SCRIPT_FILEGROUP_ID, this.modelScriptGId.toString());//needed for the next (Rscript) task
		stepExecution.getExecutionContext().put(MacstwoSoftwareJobParameters.TEST_SAMPLE_ID, testSampleId.toString());
		stepExecution.getExecutionContext().put(MacstwoSoftwareJobParameters.CONTROL_SAMPLE_ID, controlSampleId.toString());
		*/
	}
	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return stepExecution.getExitStatus();
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.debug("StepExecutionListener beforeStep saving StepExecution");
		this.stepExecution = stepExecution;		
		
		//JobExecution jobExecution = stepExecution.getJobExecution();
		//ExecutionContext jobContext = jobExecution.getExecutionContext();
		//this.scratchDirectory = jobContext.get("scrDir").toString();
		//this.cellLibId = (Integer) jobContext.get("cellLibId");
		
		//in case of crash
		this.jobId = (Integer) stepExecution.getExecutionContext().get("jobId");
		this.modelScriptGId = (Integer) stepExecution.getExecutionContext().get("modelScriptGId");
		this.peaksXlsGId = (Integer) stepExecution.getExecutionContext().get("peaksXlsGId");
		this.narrowPeaksBedGId = (Integer) stepExecution.getExecutionContext().get("narrowPeaksBedGId");
		this.summitsBedGId = (Integer) stepExecution.getExecutionContext().get("summitsBedGId");
		this.treatPileupBedGraphGId = (Integer) stepExecution.getExecutionContext().get("treatPileupBedGraphGId");
		this.controlLambdaBedGraphGId = (Integer) stepExecution.getExecutionContext().get("controlLambdaBedGraphGId");
		this.testSampleId = (Integer) stepExecution.getExecutionContext().get("testSampleId");
		this.controlSampleId = (Integer) stepExecution.getExecutionContext().get("controlSampleId");		
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		Sample testSample = sampleService.getSampleById(testSampleId);
		
		if (this.modelScriptGId != null && testSample.getId() != 0){
			////fileService.setSampleFile(fileService.getFileGroupById(modelScriptGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.modelScriptGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseq.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.peaksXlsGId != null && testSample.getId() != 0){
			////fileService.setSampleFile(fileService.getFileGroupById(peaksXlsGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.peaksXlsGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseq.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.narrowPeaksBedGId != null && testSample.getId() != 0){
			////fileService.setSampleFile(fileService.getFileGroupById(narrowPeaksBedGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.narrowPeaksBedGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseq.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.summitsBedGId != null && testSample.getId() != 0){
			///fileService.setSampleFile(fileService.getFileGroupById(summitsBedGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.summitsBedGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseq.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.treatPileupBedGraphGId != null && testSample.getId() != 0){
			///fileService.setSampleFile(fileService.getFileGroupById(treatPileupBedGraphGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.treatPileupBedGraphGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseq.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
		if (this.controlLambdaBedGraphGId != null && testSample.getId() != 0){
			///fileService.setSampleFile(fileService.getFileGroupById(controlLambdaBedGraphGId), testSample);
			FileGroup fg = fileService.getFileGroupById(this.controlLambdaBedGraphGId);
			fileService.setSampleFile(fg, testSample);
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("chipseq.controlId");
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(fg.getId());
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			fgmList.add(fgm);
			fileService.saveFileGroupMeta(fgmList, fg);
		}
	}
}
