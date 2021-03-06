/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.macstwo.batch.tasklet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.daemon.batch.tasklets.WaspRemotingTasklet;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration;
import edu.yu.einstein.wasp.grid.work.WorkUnitGridConfiguration.ProcessMode;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.service.MacstwoService;
import edu.yu.einstein.wasp.macstwo.software.Macstwo;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Build;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;

/**
 * @author 
 * 
 */
public class MacstwoTasklet extends WaspRemotingTasklet implements StepExecutionListener {

	private Integer jobId;
	private String peakType;
	private String testCellLibraryIdListAsString;
	private String controlCellLibraryIdListAsString;
	private List<Integer> testCellLibraryIdList;//treated, such as IP
	private List<Integer> controlCellLibraryIdList;//contol, such as input 
	
	private Integer testSampleId;
	private Integer controlSampleId;
	private String commandLineCall;	
	private String softwareIdUsedListAsString;
	private Integer macs2AnalysisFileGroupId;
	
	private StepExecution stepExecution;
	
	@Autowired
	private FileType textFileType;//here, the model.r script output file
	@Autowired
	private FileType pdfFileType;//output pdf file after running the model.r script
	@Autowired
	private FileType pngFileType;//output after imagemagik on the pdf file
	@Autowired
	private FileType tsvFileType;//tab-separated values (in this case, the .xls output file)
	@Autowired
	private FileType bedFileType;//bed file (the peaks.narrowPeak BED6+4 output file; the summits.bed file;  peaks.broadPeak is BED6+3; peaks.gappedPeak is BED12+3) 
	@Autowired
	private FileType bedGraphFileType;//bedGraph (treat_pileup.bdg and control_lambda.bdg)
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private FileType bamFileType;

	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileService fileService;
	@Autowired
	private GenomeService genomeService;
	@Autowired
	private RunService runService;

	@Autowired
	private GridHostResolver gridHostResolver;
	
	@Autowired
	private Macstwo macs2;
	
	@Autowired
	private MacstwoService macstwoService;

	public MacstwoTasklet() {
		// proxy
	}

	public MacstwoTasklet(String jobIdAsString, String peakType, String testCellLibraryIdListAsString, String controlCellLibraryIdListAsString) throws Exception {
		
		logger.trace("Starting MacstwoTasklet constructor");
		logger.trace("jobIdAsString: " + jobIdAsString);
		logger.trace("peakType (the parameter): " + peakType);
		logger.trace("testCellLibraryIdListAsString: " + testCellLibraryIdListAsString);
		logger.trace("controlCellLibraryIdListAsString: " + controlCellLibraryIdListAsString);
		
		Assert.assertTrue(!jobIdAsString.isEmpty());
		this.jobId = Integer.parseInt(jobIdAsString);		
		Assert.assertTrue(this.jobId > 0);
		
		Assert.assertTrue(!peakType.isEmpty());
		this.peakType = peakType;//new, as of 9-8-14
		
		this.testCellLibraryIdListAsString = testCellLibraryIdListAsString;
		Assert.assertTrue(!testCellLibraryIdListAsString.isEmpty());
		this.testCellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(testCellLibraryIdListAsString);//should be all from same job

		Assert.assertTrue(!this.testCellLibraryIdList.isEmpty());
		
		//oddly enough (and not expected from the code), WaspSoftwareJobParameters.getLibraryCellIdList(controlCellLibraryIdListAsString)
		//throws an exception if controlCellLibraryIdListAsString is an empty string, thus the need for the if-else statement
		if(controlCellLibraryIdListAsString==null || controlCellLibraryIdListAsString.isEmpty()){//could be empty!!
			this.controlCellLibraryIdListAsString = "";
			this.controlCellLibraryIdList = new ArrayList<Integer>();
		}
		else{
			this.controlCellLibraryIdListAsString = controlCellLibraryIdListAsString;
			this.controlCellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(controlCellLibraryIdListAsString);//may be empty
			Assert.assertTrue(!this.controlCellLibraryIdList.isEmpty());
		}
		
		logger.trace("in constructor this.jobId: " + this.jobId);
		logger.trace("in constructor this.peakType: " + this.peakType);
		logger.trace("in constructor testCellLibraryIdList.size(): " + testCellLibraryIdList.size());
		logger.trace("in constructor controlCellLibraryIdList.size(): " + controlCellLibraryIdList.size());
		logger.trace("Ending MacstwoTasklet constructor");
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
	public GridResult doExecute(ChunkContext context) throws Exception {
		
		logger.debug("Starting MacstwoTasklet execute");		
		logger.debug("in doExecute this.jobId: " + this.jobId);
		logger.debug("in doExecute this.peakType: " + this.peakType);
		logger.debug("in doExecute testCellLibraryIdList.size(): " + testCellLibraryIdList.size());
		logger.debug("in doExecute controlCellLibraryIdList.size(): " + controlCellLibraryIdList.size());
		this.stepExecution = context.getStepContext().getStepExecution();
		Job job = jobService.getJobByJobId(jobId);
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
		String antibodyTarget = null;
		while(testSample.getParentId()!=null){
			testSample = sampleService.getSampleById(testSample.getParentId());
		}
		for(SampleMeta sm : testSample.getSampleMeta()){
			if(sm.getK().toLowerCase().contains("antibody")){
				antibodyTarget = sm.getV();
			}
		}
		logger.debug("testSample.name = " + testSample.getName());		
		this.testSampleId = testSample.getId();
		
		Set<FileGroup> derrivedFromFileGroups = new HashSet<FileGroup>();
		
		int shortestReadLengthFromAllTestRuns = 0;
		
		List<FileHandle> testFileHandleList = new ArrayList<FileHandle>();		
		for(Integer id : this.testCellLibraryIdList){
			SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(id);
			//setOfCellLibrariesForDerivedFrom.add(cellLibrary);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			derrivedFromFileGroups.addAll(fileGroups);
			logger.debug("test fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					testFileHandleList.add(fileHandle);
					logger.debug("test fileHandle = " + fileHandle.getFileName());
				}				
			}
			
			//get shortestReadLengthFromAllRuns using smallest of all IP's run.readLength
			Sample cell = sampleService.getCell(cellLibrary);
			Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
			List<Run> runList = runService.getRunsForPlatformUnit(platformUnit);
			Run run = runList.get(0);//could be a problem
			for(RunMeta runMeta : run.getRunMeta()){
				if(runMeta.getK().endsWith("readLength")){
					String readLengthAsString = runMeta.getV();
					try{
						int readLengthAsInt = Integer.parseInt(readLengthAsString);
						if(readLengthAsInt > 0){
							if(shortestReadLengthFromAllTestRuns==0){//first time
								shortestReadLengthFromAllTestRuns = readLengthAsInt;
							}
							else if(readLengthAsInt < shortestReadLengthFromAllTestRuns){
								shortestReadLengthFromAllTestRuns = readLengthAsInt;
							}
						}
					}catch(Exception e){logger.debug("unable to parse readLength to int");}
				}
			}
		}
		Assert.assertTrue(!testFileHandleList.isEmpty());
		logger.debug("test bam files size = " + testFileHandleList.size());
		logger.debug("shortestReadLengthFromAllTestRuns = " + shortestReadLengthFromAllTestRuns);
		
		Sample controlSample = null;
		if(!controlCellLibraryIdList.isEmpty()){
			controlSample = sampleService.getLibrary(sampleService.getCellLibraryBySampleSourceId(controlCellLibraryIdList.get(0)));//all these cellLibraries are from the same library or macromoleucle
			while(controlSample.getParentId()!=null){
				controlSample = sampleService.getSampleById(controlSample.getParentId());//controlSample.getParent();
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
			//setOfCellLibrariesForDerivedFrom.add(cellLibrary);
			Set<FileGroup> fileGroups = fileService.getFilesForCellLibraryByType(cellLibrary, bamFileType);
			derrivedFromFileGroups.addAll(fileGroups);
			logger.debug("control fileGroups size = " + fileGroups.size());
			for(FileGroup fileGroup : fileGroups){
				for(FileHandle fileHandle : fileGroup.getFileHandles()){
					controlFileHandleList.add(fileHandle);//can be empty
					logger.debug("control fileHandle = " + fileHandle.getFileName());
				}
			}
		}
		logger.debug("controlFileHandleList.size = " + controlFileHandleList.size());
			
		Date dateNow = new Date( );
	    SimpleDateFormat ft = new SimpleDateFormat ("yyyyMMdd");
	    //new
	    String prefixForFileName = ft.format(dateNow) + "_MACS2_IP_" + testSample.getName();
	    if(antibodyTarget!=null && !antibodyTarget.isEmpty()){
	    	prefixForFileName = prefixForFileName + "_TARGET_" + antibodyTarget;
	    }
	    else{
	    	prefixForFileName = prefixForFileName + "_TARGET_unspecified";
	    }
	    if(controlSample != null){
	    	prefixForFileName = prefixForFileName + "_CONTROL_" + controlSample.getName();
	    }
	    else{
	    	prefixForFileName = prefixForFileName + "_CONTROL_none";
	    }
	    prefixForFileName = fileService.getSanitizedName(prefixForFileName);
		logger.debug("prefixForFileName = " + prefixForFileName);
		logger.debug("preparing to generate workunit");
		
/*	due to possible model building problem, move this elsewhere		
		String modelFileName = prefixForFileName + "_model.r";
		String pdfFileName = modelFileName.replaceAll(".r$", ".pdf");//abc_model.r will be used to generate abc_model.pdf
		String pngFileName = modelFileName.replaceAll(".r$", ".png");//abc_model.pdf will be used to generate abc_model.png
*/
		
		Build build = genomeService.getBuild(testSample);
		String speciesName = build.getGenome().getOrganism().getName();//Homo sapiens
		String speciesCode = "";//for Homo sapiens, species code will be hs
		String [] stringArray = speciesName.split("\\s+"); 
		if(stringArray.length==2){
			speciesCode = stringArray[0].substring(0, 1).toLowerCase();
			speciesCode += stringArray[1].substring(0, 1).toLowerCase();
		}
		if(speciesCode.length()!=2){
			speciesCode = "";
		}
		
		WorkUnit w = macs2.getPeaks(this.peakType, shortestReadLengthFromAllTestRuns, testSample, controlSample, prefixForFileName, testFileHandleList, controlFileHandleList, jobParametersMap/*, modelFileName, pdfFileName, pngFileName*/);//configure
		logger.debug("OK, workunit has been generated");
		this.commandLineCall = w.getCommand();
		this.commandLineCall = this.commandLineCall.replaceAll("\\n", "<br /><br />");//the workunit tagged on a newline at the end of the command; so remove it for db storage and replace with <br /> for display purposes

		List<String> listOfFileHandleNames = new ArrayList<String>();
		
		LinkedHashSet<FileHandle> files = new LinkedHashSet<FileHandle>();
		Set<FileGroup> macstwoEnclosedFileGroups = new LinkedHashSet<FileGroup>();
/*	due to possible model building problem, move this elsewhere 	
		Imagemagick imagemagickSoftware = (Imagemagick) macs2.getSoftwareDependencyByIname("imagemagick"); 
		R rSoftware = (R) macs2.getSoftwareDependencyByIname("rPackage");
		this.softwareIdUsedListAsString = macs2.getId().toString() + ":" + rSoftware.getId().toString() + ":" + imagemagickSoftware.getId().toString();
*/
		this.softwareIdUsedListAsString = macs2.getId().toString();

/*	due to possible model building problem, move this elsewhere 	
		FileHandle modelScriptFileHandle = macstwoService.createAndSaveInnerFileHandle(modelFileName, textFileType);
		listOfFileHandleNames.add(modelScriptFileHandle.getFileName());
		files.add(modelScriptFileHandle);
		FileGroup modelScriptFileGroup = macstwoService.createAndSaveInnerFileGroup(modelScriptFileHandle, macs2, "xxx_model.r is a macs2-generated R script that can be converted into a pdf (using RScript) and illustrates the peak model");
		macstwoEnclosedFileGroups.add(modelScriptFileGroup);
*/		
		FileHandle peaksXlsFileHandle = macstwoService.createAndSaveInnerFileHandle(prefixForFileName + "_peaks.xls", tsvFileType);
		listOfFileHandleNames.add(peaksXlsFileHandle.getFileName());
		files.add(peaksXlsFileHandle);
		FileGroup peaksXlsFileGroup = macstwoService.createAndSaveInnerFileGroup(peaksXlsFileHandle, macs2, "xxx_peaks.xls is a Macs2-generated Excel-based tabular file which contains information about called peaks");
		macstwoEnclosedFileGroups.add(peaksXlsFileGroup);
		
		//macs2 will generate peaks.narrowPeak and summits.bed for punctate peaks (so we will NOT invoke --broad in the macs2 command)
		if(this.peakType.equalsIgnoreCase("punctate")){
			
			FileHandle narrowPeaksBedFileHandle = macstwoService.createAndSaveInnerFileHandle(prefixForFileName + "_peaks.narrowPeak.bed", bedFileType);
			listOfFileHandleNames.add(prefixForFileName + "_peaks.narrowPeak");//name that macstwo will generate (in database, and therefore in results directory it's _peaks.narrowPeak.bed)
			files.add(narrowPeaksBedFileHandle);
			FileGroup narrowPeaksBedFileGroup = macstwoService.createAndSaveInnerFileGroup(narrowPeaksBedFileHandle, macs2, "xxx_peaks.narrowPeak.bed is a Macs2-generated BED6+4 format file which contains peak locations together with peak summit, pvalue and qvalue that can be load into UCSC genome browser");
			macstwoEnclosedFileGroups.add(narrowPeaksBedFileGroup);
						
			FileHandle summitsBedFileHandle = macstwoService.createAndSaveInnerFileHandle(prefixForFileName + "_summits.bed", bedFileType);
			listOfFileHandleNames.add(summitsBedFileHandle.getFileName());
			files.add(summitsBedFileHandle);
			FileGroup summitsBedFileGroup = macstwoService.createAndSaveInnerFileGroup(summitsBedFileHandle, macs2, "xxx_summits.bed is Macs2-generated BED file which contains the peak summits locations for every peak and can be loaded into the UCSC genome browser");
			macstwoEnclosedFileGroups.add(summitsBedFileGroup);
			
		}
		//NOTE: mixed peaks option no longer available as of 12-30-14
		//macs2 will generate peaks.broadPeak and peaks.gappedPeaks for broad or mixed peaks (so we will invoke --broad in the macs2 command)
		else{ //peakType.equals broad OR mixed
			
			FileHandle broadPeaksBedFileHandle = macstwoService.createAndSaveInnerFileHandle(prefixForFileName + "_peaks.broadPeak.bed", bedFileType);
			listOfFileHandleNames.add(prefixForFileName + "_peaks.broadPeak");//name that macstwo will generate
			files.add(broadPeaksBedFileHandle);
			FileGroup broadPeaksBedFileGroup = macstwoService.createAndSaveInnerFileGroup(broadPeaksBedFileHandle, macs2, "xxx_peaks.broadPeak.bed is a Macs2-generated BED6+3 format file which is similar to peaks.narrowPeak file, except for missing the 10th column for annotating peak summits");
			macstwoEnclosedFileGroups.add(broadPeaksBedFileGroup);
			
			FileHandle gappedPeaksBedFileHandle = macstwoService.createAndSaveInnerFileHandle(prefixForFileName + "_peaks.gappedPeak.bed", bedFileType);
			listOfFileHandleNames.add(prefixForFileName + "_peaks.gappedPeak");//name that macstwo will generate
			files.add(gappedPeaksBedFileHandle);
			FileGroup gappedPeaksBedFileGroup = macstwoService.createAndSaveInnerFileGroup(gappedPeaksBedFileHandle, macs2, "xxx_peaks.gappedPeak.bed is a Macs2-generated BED12+3 file which contains both the broad region and narrow peaks and can be loaded into the genome browser");
			macstwoEnclosedFileGroups.add(gappedPeaksBedFileGroup);
		}
				
		FileHandle treatPileupBedGraphFileHandle = macstwoService.createAndSaveInnerFileHandle(prefixForFileName + "_treat_pileup.bdg", bedGraphFileType);
		listOfFileHandleNames.add(treatPileupBedGraphFileHandle.getFileName());
		files.add(treatPileupBedGraphFileHandle);
		FileGroup treatPileupBedGraphFileGroup = macstwoService.createAndSaveInnerFileGroup(treatPileupBedGraphFileHandle, macs2, "xxx_treat_pileup.bdg is a Macs2-generated bedGraph file that describes the read distrubution along the entire genome (for treatment data) and can be imported to UCSC genome browser or converted into even smaller bigWig file");
		macstwoEnclosedFileGroups.add(treatPileupBedGraphFileGroup);
						
		FileHandle controlLambdaBedGraphFileHandle = macstwoService.createAndSaveInnerFileHandle(prefixForFileName + "_control_lambda.bdg", bedGraphFileType);
		listOfFileHandleNames.add(controlLambdaBedGraphFileHandle.getFileName());
		files.add(controlLambdaBedGraphFileHandle);
		FileGroup controlLambdaBedGraphFileGroup = macstwoService.createAndSaveInnerFileGroup(controlLambdaBedGraphFileHandle, macs2, "xxx_control_lambda.bdg is a Macs2-generated bedGraph file that describes the read distrubution along the entire genome (for local lambda values from control) and can be imported to UCSC genome browser or converted into even smaller bigWig file");
		macstwoEnclosedFileGroups.add(controlLambdaBedGraphFileGroup);
/*	due to possible model building problem, move this elsewhere 			
		//the pdf (generated from running Rscript on xx_model.r file)
		FileHandle modelPdfFileHandle = macstwoService.createAndSaveInnerFileHandle(pdfFileName, pdfFileType);
		listOfFileHandleNames.add(modelPdfFileHandle.getFileName());
		files.add(modelPdfFileHandle);
		FileGroup modelPdfFileGroup = macstwoService.createAndSaveInnerFileGroup(modelPdfFileHandle, rSoftware, "xxx_model.pdf is a pdf file generated by running RScript against xxx_model.r and is an image of the read distribution in model peaks and fragment size estimation");
		macstwoEnclosedFileGroups.add(modelPdfFileGroup);
		
		logger.debug("very very very very new -------   recorded fileGroup and fileHandle for rscript to create pdf in MacstwoGenerateModelAsPdfTasklet.doExecute()");

		//the png (converted from the pdf using ImageMagick)
		FileHandle modelPngFileHandle = macstwoService.createAndSaveInnerFileHandle(pngFileName, pngFileType);
		listOfFileHandleNames.add(modelPngFileHandle.getFileName());
		files.add(modelPngFileHandle);
		FileGroup modelPngFileGroup = macstwoService.createAndSaveInnerFileGroup(modelPngFileHandle, imagemagickSoftware, "xxx_model.png is a png image file converted from xxx_model.pdf using ImageMagick");
		macstwoEnclosedFileGroups.add(modelPngFileGroup);	
		
		logger.debug("very very very very new ------- recorded fileGroup and fileHandle for ImageMagick to create png in MacstwoGenerateModelAsPdfTasklet.doExecute()");
*/		
		//9/16/14
		FileGroup macs2AnalysisFileGroup = fileService.createFileGroupCollection(macstwoEnclosedFileGroups);//will create enclosing fileGroup, save it to db, and also set/save parent for each enclosed child fileGroup; it will set it's own filetype
		macs2AnalysisFileGroup.setDescription(prefixForFileName);
		macs2AnalysisFileGroup.setSoftwareGeneratedBy(macs2);
		macs2AnalysisFileGroup.setDerivedFrom(derrivedFromFileGroups);//this is actually adding reference to samplesourcefilegroup and I think, samplefilegroup
		macs2AnalysisFileGroup.setIsActive(0);
		macs2AnalysisFileGroup = fileService.addFileGroup(macs2AnalysisFileGroup);
		
		this.macs2AnalysisFileGroupId = macs2AnalysisFileGroup.getId();
		logger.debug("new ------- recorded all encompassing fileGroup macs2AnalysisFileGroup as a container for files outputted by macs2");
	
		logger.debug("recorded fileGroups and fileHandles for macs2 files in MacstwoTasklet.doExecute()");
		

		//place in the step context in case of crash
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		stepContext.put("jobId", this.jobId); 
		stepContext.put("peakType", this.peakType); 
		stepContext.put("testCellLibraryIdListAsString", this.testCellLibraryIdListAsString); 
		stepContext.put("controlCellLibraryIdListAsString", this.controlCellLibraryIdListAsString); 		
		stepContext.put("testSampleId", this.testSampleId); 
		stepContext.put("controlSampleId", this.controlSampleId); 	 
				
		stepContext.put("macs2AnalysisFileGroupId", this.macs2AnalysisFileGroupId);
		stepContext.put("softwareIdUsedListAsString", this.softwareIdUsedListAsString);
		
		stepContext.put("commandLineCall", this.commandLineCall);
		logger.debug("saved variables in stepContext in case of crash in MacstwoTasklet.doExecute()");
		
		w.setResultFiles(files);
		
		logger.debug("executed w.getResultFiles().add(x) for " + files.size() + " FileHandles");
		
		w.getConfiguration().setResultsDirectory(fileService.generateJobSoftwareBaseFolderName(job, macs2));	
		
		
		int counter = 0;
		for(String fileName : listOfFileHandleNames){//need to make these symbolic links in order to properly copy files
			w.addCommand("ln -sf " + fileName + " ${" + WorkUnit.OUTPUT_FILE+"["+counter+"]}");
			logger.debug("add command: " + "ln -sf " + fileName + " ${" + WorkUnit.OUTPUT_FILE+"["+counter+"]}");
			counter++;
		}
		
		logger.debug("executed w.setResultsDirectory(a/jobId) in MacstwoTasklet.doExecute()");

		GridResult result = gridHostResolver.execute(w);
		logger.debug("****Executed gridHostResolver.execute(w) in MactwoTasklet.doExecute()");
		
		//these will be promoted if necessary to deal with Rscript and imagemagic if model is generated; they will be used in subsequent tasklet if model is generated
		stepContext.put(MacstwoService.NAME_OF_FILE_WHOSE_CREATION_MUST_BE_CONFIRMED, prefixForFileName + "_model.r");//this one doesn't need to be promoted; it's only used below in doPreFinish
		stepContext.put(MacstwoService.IS_MODEL_FILE_CREATED, "false");//promote; note value is reset in doPreFinish()
		
		stepContext.put(MacstwoService.JOB_ID_AS_STRING, jobId.toString());//promote; may not actually be required
		stepContext.put(MacstwoService.PREFIX_FOR_FILE_NAME, prefixForFileName);//promote
		stepContext.put(MacstwoService.MACSTWO_ANALYSIS_FILEGROUP_ID_AS_STRING, macs2AnalysisFileGroupId.toString());//promote
		stepContext.put(MacstwoService.WORKING_DIRECTORY, result.getWorkingDirectory());//promote
		stepContext.put(MacstwoService.RESULTS_DIRECTORY, result.getResultsDirectory());//promote
		
		return result;
	
	}
	

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		return super.afterStep(stepExecution);
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.beforeStep(stepExecution);
		logger.debug("*****  StepExecutionListener beforeStep saving StepExecution in MacstwoTasklet.beforeStep");
		this.stepExecution = stepExecution;				
	
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		//in case of crash
		if(this.jobId==null){//set initially in constructor
			this.jobId = (Integer) stepContext.get("jobId");
		}
		if(this.peakType==null){//set initially in constructor
			this.peakType = (String) stepContext.get("peakType");
		}
		if(this.testCellLibraryIdListAsString==null){//set initially in constructor
			this.testCellLibraryIdListAsString = (String) stepContext.get("testCellLibraryIdListAsString");
		}
		if(this.controlCellLibraryIdListAsString==null){//set initially in constructor
			this.controlCellLibraryIdListAsString = (String) stepContext.get("controlCellLibraryIdListAsString");
		}
		
		this.testSampleId = (Integer) stepContext.get("testSampleId");
		this.controlSampleId = (Integer) stepContext.get("controlSampleId");	
		this.commandLineCall = (String) stepContext.get("commandLineCall");	
		
		this.macs2AnalysisFileGroupId = (Integer) stepContext.get("macs2AnalysisFileGroupId");
		this.softwareIdUsedListAsString = (String) stepContext.get("softwareIdUsedListAsString");
	}
	
	/** 
	 * {@inheritDoc}
	 */
	@Override
	@Transactional("entityManager")
	public void doPreFinish(ChunkContext context) throws Exception {
		
		logger.debug("starting doPreFinish() in MacstwoTasklet");
		
		//at Andy's suggestion, do this here too:
		this.stepExecution = context.getStepContext().getStepExecution();
		ExecutionContext stepContext = this.stepExecution.getExecutionContext();
		this.jobId = (Integer) stepContext.get("jobId");//currently, not really needed here
		this.testCellLibraryIdListAsString = (String) stepContext.get("testCellLibraryIdListAsString");
		this.controlCellLibraryIdListAsString = (String) stepContext.get("controlCellLibraryIdListAsString");
		
		this.testSampleId = (Integer) stepContext.get("testSampleId");
		this.controlSampleId = (Integer) stepContext.get("controlSampleId");	
		this.commandLineCall = (String) stepContext.get("commandLineCall");		
		
		this.macs2AnalysisFileGroupId = (Integer) stepContext.get("macs2AnalysisFileGroupId");
		this.softwareIdUsedListAsString = (String) stepContext.get("softwareIdUsedListAsString");
		
		String nameOfFileWhoseCreationMustBeConfirmed = (String) stepContext.get(MacstwoService.NAME_OF_FILE_WHOSE_CREATION_MUST_BE_CONFIRMED);
		logger.trace("nameOfFileWhoseCreationMustBeConfirmed: " + nameOfFileWhoseCreationMustBeConfirmed);
		String isModelFileCreated = "false";
		
		// associate test sample with the new file groups		
		Sample testSample = sampleService.getSampleById(testSampleId);		
		
		logger.trace("in middle of doPreFinish() in MacstwoTasklet");
		
		//new, 5-9-14
		logger.trace("new stuff, added 5-9-14, to get numbers from txt files that are not saved");
		//context.getStepContext().attributeNames().
		GridResult result = getGridResult(context);
		String workingDir = result.getWorkingDirectory();//is this the scratch, I think so
		WorkUnitGridConfiguration c = new WorkUnitGridConfiguration();
		c.setProcessMode(ProcessMode.SINGLE);
		String totalCountMappedReadsAsString = "";
		String totalCountMappedReadsInPeaksAsString = "";
		
		try {
			GridWorkService workService = gridHostResolver.getGridWorkService(c);
			GridTransportConnection transportConnection = workService.getTransportConnection();
			c.setWorkingDirectory(workingDir);
			WorkUnit w = new WorkUnit(c);
			w.addCommand("cat totalCountMappedReads.txt");//will appear on first line of output
			w.addCommand("cat totalCountMappedReadsInPeaks.txt");//will appear on second line of output
			w.addCommand("[ -f " + nameOfFileWhoseCreationMustBeConfirmed + " ] && echo true || echo false");
			GridResult r = transportConnection.sendExecToRemote(w);
			InputStream is = r.getStdOutStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
			boolean keepReading = true;
			int lineNumber = 0;
			while (keepReading){
				lineNumber++;
				String line = null;
				line = br.readLine();
				logger.debug("line number = " + lineNumber + " and line = " + line);
				if (line == null)
					keepReading = false;
				else{
					if (lineNumber == 1){
						totalCountMappedReadsAsString = line.replaceAll("\\n", "");//just in case there is a trailing new line
						logger.debug("totalCountMappedReadsAsString = " + totalCountMappedReadsAsString);
					} else if (lineNumber == 2){
						totalCountMappedReadsInPeaksAsString = line.replaceAll("\\n", "");//just in case there is a trailing new line;
						logger.debug("totalCountMappedReadsInPeaksAsString = " + totalCountMappedReadsInPeaksAsString);
					} else if (lineNumber == 3){
						isModelFileCreated = line.replaceAll("\\n", "");//just in case there is a trailing new line;
						logger.debug("isModelFileCreated = " + isModelFileCreated);
						//update this (decider will act on this information):
						stepContext.put(MacstwoService.IS_MODEL_FILE_CREATED, isModelFileCreated);
					} else {
						keepReading = false;
					}
				}
			}
			br.close();
			
		} catch (Exception e) {
			logger.debug("unable to get totalCountMappedReads.txt value and/or totalCountMappedReadsInPeaks.txt in MacsTwo or isModelFileCreated");
		} 		
		
		logger.debug("getting ready to save testSample metadata  in MacstwoTasklet");
		
		//register commandLineCall, testCellLibraryIdList, controlCellLibraryIdList and  controlId and testId with fileGroupMeta 
		//and record totalCountMappedReads, totalCountMappedReadsInPeaks, [FRIP statistic - will be derived from totalCountMappedReadsInPeaks / totalCountMappedReadsInPeaks]
		//new 6-18-14
		if (this.macs2AnalysisFileGroupId != null && testSample.getId() != 0){
			FileGroup enclosingFG = fileService.getFileGroupById(this.macs2AnalysisFileGroupId);
			enclosingFG.setIsActive(1);
			fileService.addFileGroup(enclosingFG);//save new active setting
			for(FileGroup childFG : enclosingFG.getChildren()){
				childFG.setIsActive(1);
				fileService.addFileGroup(childFG);//save new active setting
			}
			fileService.setSampleFile(enclosingFG, testSample);//may be executed through derivedFrom (but just to be safe)
			
			List<FileGroupMeta> fgmList = new ArrayList<FileGroupMeta>();
			
			FileGroupMeta fgm = new FileGroupMeta();
			fgm.setK("macs2Analysis.controlId");//could be gotten from the derivedFrom data; stored but never used
			fgm.setV(this.controlSampleId.toString());
			fgm.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm);
			
			FileGroupMeta fgm2 = new FileGroupMeta();
			fgm2.setK("macs2Analysis.testId");//sample --> sampleFileGroup table; but could be gotten from the derivedFrom data; ; stored but never used
			fgm2.setV(testSample.getId().toString());
			fgm2.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm2);
			
			FileGroupMeta fgm3 = new FileGroupMeta();
			fgm3.setK("macs2Analysis.testCellLibraryIdList");//these may be now really stored in derivedFrom, as list of Bam files; ; stored but never used
			fgm3.setV(this.testCellLibraryIdListAsString);
			fgm3.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm3);
			
			FileGroupMeta fgm4 = new FileGroupMeta();
			fgm4.setK("macs2Analysis.controlCellLibraryIdList");//these may be now really stored in derivedFrom, as list of Bam files; ; stored but never used
			fgm4.setV(this.controlCellLibraryIdListAsString);
			fgm4.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm4);
			
			FileGroupMeta fgm5 = new FileGroupMeta();
			fgm5.setK("macs2Analysis.commandLineCall");//used
			fgm5.setV(this.commandLineCall);
			fgm5.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm5);
			
			FileGroupMeta fgm6 = new FileGroupMeta();
			fgm6.setK("macs2Analysis.totalCountMappedReads");//used
			fgm6.setV(totalCountMappedReadsAsString);
			fgm6.setFileGroupId(enclosingFG.getId());
			fgmList.add(fgm6);
			
			FileGroupMeta fgm7 = new FileGroupMeta();
			fgm7.setK("macs2Analysis.totalCountMappedReadsInPeaksAsString");//used
			fgm7.setV(totalCountMappedReadsInPeaksAsString);
			fgm7.setFileGroupId(enclosingFG.getId());			
			fgmList.add(fgm7);
			
			FileGroupMeta fgm8 = new FileGroupMeta();
			fgm8.setK("macs2Analysis.softwareIdUsedListAsString");//used; as of 9/16, should be able to get this from the fileGroup children
			fgm8.setV(this.softwareIdUsedListAsString);
			fgm8.setFileGroupId(enclosingFG.getId());			
			fgmList.add(fgm8);
			
			fileService.saveFileGroupMeta(fgmList, enclosingFG);
		}

		logger.debug("ending doPreFinish() in MacstwoTasklet");
	}
	
	/* moved to macstwoService
	private FileHandle createAndSaveInnerFileHandle(String fileName, FileType fileType){
		FileHandle fileHandle = new FileHandle();
		fileHandle.setFileName(fileName);
		fileHandle.setFileType(fileType);
		fileHandle = fileService.addFile(fileHandle);
		return fileHandle;
	}
	private FileGroup createAndSaveInnerFileGroup(FileHandle fileHandle, Software software, String description){
		FileGroup fileGroup = new FileGroup();		
		fileGroup.setDescription(description);
		fileGroup.setFileType(fileHandle.getFileType());
		fileGroup.setSoftwareGeneratedBy(software);
		fileGroup.setIsActive(0);
		//modelScriptFG.setDerivedFrom(derrivedFromFileGroups);
		Set<FileHandle> fileHandleSet = new HashSet<FileHandle>();
		fileHandleSet.add(fileHandle);
		fileGroup.setFileHandles(fileHandleSet);
		fileGroup = fileService.addFileGroup(fileGroup);		
		return fileGroup;
	}
	 */
	@Override
	public void doCleanupBeforeRestart(StepExecution stepExecution) throws Exception {
		ExecutionContext stepContext = stepExecution.getExecutionContext();
		this.macs2AnalysisFileGroupId = (Integer) stepContext.get("macs2AnalysisFileGroupId");
		if (this.macs2AnalysisFileGroupId != null){
			FileGroup enclosingFG = fileService.getFileGroupById(this.macs2AnalysisFileGroupId);
			Set<FileGroup> children = new HashSet<FileGroup>(enclosingFG.getChildren());
			for(FileGroup childFG : children){
				enclosingFG.getChildren().remove(childFG);
				fileService.removeWithAllAssociatedFilehandles(childFG);
			}
			fileService.removeWithAllAssociatedFilehandles(enclosingFG);
		}
	}
}
