/**
 * 
 */
package edu.yu.einstein.wasp.plugin.illumina.software;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.InvalidParameterException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.interfacing.IndexingStrategy;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.illumina.IlluminaIndexingStrategy;
import edu.yu.einstein.wasp.plugin.mps.MpsIndexingStrategy;
import edu.yu.einstein.wasp.plugin.mps.software.sequencer.SequenceRunProcessor;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;

/**
 * @author calder
 * 
 */
public class IlluminaHiseqSequenceRunProcessor extends SequenceRunProcessor {
	
	private static final long serialVersionUID = -3322619814370790116L;
		
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SampleService sampleService;

	@Autowired
	private AdaptorService adaptorService;
	
	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private String truseqIndexedDnaArea;
	
	public static final String SINGLE_INDEX_SAMPLE_SHEET_NAME = "waspSingleSampleSheet.csv";
	public static final String DUAL_INDEX_SAMPLE_SHEET_NAME = "waspDualSampleSheet.csv";
	
	public static final String SINGLE_INDEX_OUTPUT_FOLDER_NAME = "waspSingleUnaligned";
	public static final String DUAL_INDEX_OUTPUT_FOLDER_NAME = "waspDualUnaligned";
	
	public static final String SINGLE_INDEX_SEMAPHORE = "wasp_single_begin.txt";
	public static final String DUAL_INDEX_SEMAPHORE = "wasp_dual_begin.txt";
	
	public static final String ILLUMINA_DATA_STAGE_NAME = "illumina.data.stage";
	
	public IlluminaHiseqSequenceRunProcessor(){
		setSoftwareVersion("1.8.2"); // this default may be overridden in wasp.site.properties
	}

	/**
	 * Called first to set up analysis run.
	 * 
	 * @param run
	 * @param method IlluminaSequenceRunProcessor.SINGLE_INDEX or DUAL_INDEX
	 * @throws GridException
	 */
	public GridResult doSampleSheet(Run run, IndexingStrategy method) throws GridException {
		
		logger.debug("sample sheet for " + run.getName() + ":" + run.getPlatformUnit().getName());
		
		logger.debug(sampleService.toString());
		
		if (! method.equals(IlluminaIndexingStrategy.TRUSEQ) && ! method.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL)) {
		    logger.error("sample sheet method called with unknown strategy: " + method);
		    throw new WaspRuntimeException("sample sheet method called with unknown strategy: " + method);
		}
		
		WorkUnit w = new WorkUnit();
				
		Sample platformUnit = run.getPlatformUnit();
		
		if (sampleService.isPlatformUnit(platformUnit)) {
			// isPlatformUnit?
			logger.debug("platform unit: " + platformUnit.getName());
		} else {
			logger.error("Not a platform unit: " + platformUnit.getName());
			throw new edu.yu.einstein.wasp.exception.InvalidParameterException(platformUnit.getId() + " is not a platform unit");
		}
		
		List<SoftwarePackage> sp = new ArrayList<SoftwarePackage>();
		sp.add(this);
		w.setCommand("touch start-illumina-pipeline.txt");
		w.setSoftwareDependencies(sp);
		w.setProcessMode(ProcessMode.SINGLE);
		
		GridWorkService gws = hostResolver.getGridWorkService(w);
		GridFileService gfs = gws.getGridFileService();
		String hostname = hostResolver.getHostname(w);
		logger.debug("sending illumina processing job to " + hostname);
		
		String directory = "";
		
		String sampleSheetName;
		if (method.equals(IlluminaIndexingStrategy.TRUSEQ)) {
		    sampleSheetName = IlluminaHiseqSequenceRunProcessor.SINGLE_INDEX_SAMPLE_SHEET_NAME;
		} else {
		    sampleSheetName = IlluminaHiseqSequenceRunProcessor.DUAL_INDEX_SAMPLE_SHEET_NAME;
		}
		
		try {
			directory = gws.getTransportConnection().getConfiguredSetting("illumina.data.dir") + "/" + run.getName();
			logger.debug("configured remote directory as " + directory);
			File f = createSampleSheet(run, method);
			String newDir = directory + "/Data/Intensities/BaseCalls/";
			
			w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
			w.setResultsDirectory(newDir);
			
			gfs.put(f, newDir + sampleSheetName);
			logger.debug("deleting temporary local sample sheet " + f.getAbsolutePath());
			f.delete();
		} catch (Exception e) {
			logger.error("problem: " + e.getLocalizedMessage());
			e.printStackTrace();
			throw new GridExecutionException("unable to pre-process illumina sample sheet", e);
		}
		
		logger.debug("touching remote file");
		w.setWorkingDirectory(directory);		
		return hostResolver.execute(w);
		
	}

	/**
	 * 
	 * generate an illumina samplesheet. Only useful for sequence conversion and
	 * truseq demultiplexing.
	 * 
	 * @param platformUnit
	 * @return
	 * @throws IOException
	 * @throws MetadataException
	 * @throws SampleTypeException
	 */
	private File createSampleSheet(Run run, IndexingStrategy method) throws IOException,
			MetadataException, SampleTypeException {

		File f = File.createTempFile("wasp_iss", ".txt");
		logger.debug("created temporary file: " + f.getAbsolutePath().toString());
		BufferedWriter bw = new BufferedWriter(new FileWriter(f, false));
		
		bw.write(getSampleSheet(run, method));
		
		bw.close();
		return f;
	}
	
	/**
	 * Method to produce sample sheet for illumina sequencing.  Samples are assigned by the following strategy:
	 * TRUSEQ: IndexingStrategy.NONE, FIVE_PRIME, TRUSEQ
	 * TRUSEQ_DUAL: IndexingStrategy.TRUSEQ_DUAL
	 * 
	 * @param run
	 * @param method
	 * @return
	 * @throws SampleTypeException
	 * @throws MetadataException
	 */
	public String getSampleSheet(Run run, IndexingStrategy method) throws SampleTypeException, MetadataException {

		String sampleSheet = getSampleSheetHeader();
		
		Sample platformUnit = run.getPlatformUnit();
		
		int numberOfCells = sampleService.getNumberOfIndexedCellsOnPlatformUnit(platformUnit);
		
		boolean[] cellMarked = new boolean[numberOfCells];
		
		// throws SampleTypeException
		Map<Integer, Sample> cells = sampleService.getIndexedCellsOnPlatformUnit(platformUnit);
		
		logger.debug(cells.size() + " cells on platform unit " + platformUnit.getName());

		for (Integer cellid : cells.keySet()) { // for each cell in platform unit
			Sample cell = cells.get(cellid);

			// throws SampleTypeException

			List<SampleSource> all = sampleService.getCellLibrariesForCell(cell);
			
			List<Sample> libraries = sampleService.getLibrariesOnCellWithoutControls(cell);

			for (SampleSource cellLibrary : all) {
			    
			    cellMarked[cellid-1] = false;
				
				logger.debug("working with cell library: " + cellLibrary.getId() + " representing " + 
						cellLibrary.getSourceSample().getId() +":"+ cellLibrary.getSourceSample().getName());
				
				// the cell library source sample is the library itself (cellLibrary.getSample() == cell).
				Adaptor adaptor = adaptorService.getAdaptor(cellLibrary.getSourceSample());
				IndexingStrategy strategy = adaptorService.getIndexingStrategy(adaptor.getAdaptorsetId());
				
				// if there is one control sample in the lane and no libraries, set the control flag
                                // the control library will be processed irrespective of method type.
                                if ((libraries.size() == 0) && (all.size() == 1)) {
                                        
                                        SampleSource controlCellLib = all.get(0);
                                        logger.debug("looking to register lone control cell library: " + controlCellLib.getId() + " on cell: " + cellid );
                                        
                                        String line = buildLine(platformUnit, cell, controlCellLib.getSourceSample().getName(), controlCellLib, "Y", "control");
                                        sampleSheet += "\n" + line;
                                        continue;
                                }
				
				// TRUSEQ processing includes all but TRUSEQ_DUAL
				if (method.equals(IlluminaIndexingStrategy.TRUSEQ)) {
				    if (strategy.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL))
				        continue;
				}
				
				// TRUSEQ_DUAL processing includes only TRUSEQ_DUAL
				if (method.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL)) {
                                    if (strategy.equals(IlluminaIndexingStrategy.TRUSEQ) ||
                                            strategy.equals(MpsIndexingStrategy.FIVE_PRIME) ||
                                            strategy.equals(MpsIndexingStrategy.NONE))
                                        continue;
                                }
				
				// Catch-all for unsupported IndexingStrategies
				if (! method.equals(IlluminaIndexingStrategy.TRUSEQ) &&
				        ! method.equals(IlluminaIndexingStrategy.TRUSEQ_DUAL) &&
				        ! method.equals(MpsIndexingStrategy.NONE) &&
				        ! method.equals(MpsIndexingStrategy.FIVE_PRIME)) {
				    String message = "Unsupported indexing strategy: " + method;
				    logger.error(message);
				    throw new WaspRuntimeException(message);
				}
				
				
				cellMarked[cellid-1] = true;

				// necessary?
				String iname = cellLibrary.getSourceSample().getSampleType().getIName();
				if ((!iname.equals("library") && (!iname
						.equals("facilityLibrary")))) {
					logger.warn("Expected library, saw " + iname);
					continue;
				}


				// TODO:implement this job service method to get JobSampleMeta
				// from sample and platform unit
				String genome = "";
				String control = "N";
				String recipe = "WASP";

				String line = buildLine(platformUnit, cell, genome, cellLibrary, control, recipe);

				logger.debug("sample sheet: " + line);

				sampleSheet += "\n" + line;
			}
			
			// if the cell has not been marked (has TruSeq sample), create a dummy sample for 
			// the lane if further demultiplexing is required
			// or a single sample for the entire lane
				
			if (cellMarked[cellid-1] == false && method.equals(IlluminaIndexingStrategy.TRUSEQ)) {
				logger.debug("setting dummy sample cell: " + cellid);
				SampleSource placeholder = new SampleSource();
				//Adaptor adaptor = new Adaptor();
				placeholder.setId(-1);
				
				String line = buildLine(platformUnit, cell, "", placeholder, "N", "WASP");
				
				sampleSheet += "\n" + line;
			}
			
		}
		return sampleSheet;
		
	}

	private String buildLine(Sample platformUnit, Sample cell, String genome, SampleSource cellLibrary, String control, String recipe) {

		String adapter = "";
		String sampleId;
		String sampleName;
		Integer cellIndex;
		try {
			cellIndex = sampleService.getCellIndex(cell);
		} catch (SampleException e) {
			logger.error("sample type exception: " + e.toString());
			throw new InvalidParameterException("sample type exception" + e);
		}
		Sample sample = null;
		if (cellLibrary.getId().equals(-1)) {
			sampleId = "lane_" + cellIndex;
			sampleName = sampleId;
		} else {
			try {
				adapter = adaptorService.getAdaptor(cellLibrary.getSourceSample()).getBarcodesequence();
			} catch (Exception e) {
				// if it is null or otherwise not set, it does not exist
				adapter = "";
			}
			sample = cellLibrary.getSourceSample().getParent();

			if (sample == null)
				sample = cellLibrary.getSourceSample();
			sampleId = cellLibrary.getId().toString();
			sampleName = sample.getName();

		}
		String entry = new StringBuilder()
				.append(platformUnit.getName() + ",")
				.append(cellIndex + ",")
				.append(sampleId + ",")
				.append(genome + ",")
				.append(adapter + ",")
				.append(sampleName + ",")
				.append(control + ",")
				.append(recipe + ",") // TODO:recipe
				.append("WASP" + ",")
				.append("WASP")
				.toString();
		logger.debug("building sample sheet entry: " + entry);
		return entry;
	}

	/**
	 * Returns standard Illumina 1.8.4 SampleSheet header
	 * 
	 * FCID Flow cell ID Lane Positive integer, indicating the lane number (1-8)
	 * SampleID ID of the sample SampleRef The name of the reference Index Index
	 * sequence(s) Description Description of the sample Control Y indicates
	 * this lane is a control lane, N means sample, Recipe Recipe used during
	 * sequencing Operator Name or ID of the operator SampleProject The project
	 * the sample belongs to
	 * 
	 * @return
	 */
	public String getSampleSheetHeader() {
		return "FCID,Lane,SampleID,SampleRef,Index,Description,Control,Recipe,Operator,SampleProject";
	}

    @Override
    public String getStageDirectoryName() {
        return ILLUMINA_DATA_STAGE_NAME;
    }

}