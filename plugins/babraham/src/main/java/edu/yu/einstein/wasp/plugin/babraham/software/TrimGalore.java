/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.software;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.filetype.service.FileTypeService;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.babraham.batch.tasklet.jobparameters.TrimGaloreParameters;
import edu.yu.einstein.wasp.plugin.babraham.exception.BabrahamDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqComparator;
import edu.yu.einstein.wasp.plugin.fileformat.plugin.FastqFileTypeAttribute;
import edu.yu.einstein.wasp.plugin.fileformat.service.FastqService;
import edu.yu.einstein.wasp.plugin.mps.software.sequencer.SequenceRunProcessor;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.MessageService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.software.SoftwarePackage;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * @author calder / asmclellan
 * 
 */
@Transactional("entityManager")
public class TrimGalore extends SoftwarePackage {

    /**
     * 
     */
    private static final long serialVersionUID = -1340678727011621662L;

    @Autowired
    private FastqService fastqService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private SampleService sampleService;

    // cannot autowire as IlluminaHiseqSequenceRunProcessor here which is all we
    // really need. Beans referenced by base type so must
    // as Software and use @Qualifier to specify the casava bean.
    // Seems to be an issue for batch but not Web which accepts
    // IlluminaHiseqSequenceRunProcessor.
    @Autowired
    @Qualifier("casava")
    private SoftwarePackage casava;

    public static String TRIM_GALORE_RESULTS_KEY = "TRIM_GALORE_SIZES";

    @Autowired
    BabrahamService babrahamService;

    @Autowired
    FileService fileService;

    @Autowired
    private RunService runService;

    @Autowired
    private GridHostResolver hostResolver;

    @Autowired
    private FileType fastqFileType;

    @Autowired
    @Qualifier("fileTypeServiceImpl")
    private FileTypeService fileTypeService;

    public static final String MANY_FLOW_NAME = "edu.yu.einstein.wasp.plugin.babraham.trim_galore.fileTrim";

    public static final String FLOW_NAME = "edu.yu.einstein.wasp.plugin.babraham.trim_galore.mainFlow";

    public static final String MANY_REGISTRATION_NAME = "edu.yu.einstein.wasp.plugin.babraham.trim_galore.register";

    /**
	 * 
	 */
    public TrimGalore() {
        setSoftwareVersion("0.3.3"); // this default may be overridden in
                                     // wasp.site.properties
    }

    public WorkUnit getTrimCommand(TrimGaloreParameters params, String softwareName, int runId, int cellLibraryId, int fileGroupId, int firstFile)
            throws GridException, SampleTypeException, SampleParentChildException {
        return getTrimCommand(params.toString(), softwareName, runId, cellLibraryId, fileGroupId, firstFile);
    }

    private WorkUnit getTrimCommand(String parameterString, String softwareName, int runId, int cellLibraryId, int fileGroupId, int firstFile)
            throws GridException, SampleTypeException, SampleParentChildException {
        boolean paired = parameterString.contains("--paired");

        SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
        Sample library = sampleService.getLibrary(cellLibrary);

        Run run = runService.getRunById(runId);

        List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
        sd.add(this);
        logger.debug("attempting to get bean for " + softwareName.toString());
        SequenceRunProcessor sequencer = (SequenceRunProcessor) this.getApplicationContext().getBean(softwareName);
        sd.add(sequencer);

        logger.debug("About to generate WorkUnit for preforming trim_galore trimming on library " + library.getId() + " cell library id instance "
                + cellLibraryId);

        WorkUnit w = new WorkUnit();
        w.setProcessMode(ProcessMode.SINGLE);
        w.setMode(ExecutionMode.PROCESS);
        w.setSoftwareDependencies(sd);

        GridWorkService workService = hostResolver.getGridWorkService(w);
        GridTransportConnection transportConnection = workService.getTransportConnection();

        String stageDir = transportConnection.getConfiguredSetting(sequencer.getStageDirectoryName());
        if (!PropertyHelper.isSet(stageDir))
            throw new GridException(sequencer.getStageDirectoryName() + " is not defined at " + transportConnection.getHostName() + "!");

        // TODO: fix hardcode
        String jobRoot = transportConnection.getConfiguredSetting("results.dir");
        if (!PropertyHelper.isSet(jobRoot))
            throw new GridException("results.dir is not defined at " + transportConnection.getHostName() + "!");

        // TODO: fix hardcode
        String workingDirectory = stageDir + "/" + run.getName() + "/wasp/sequence";

        w.setWorkingDirectory(workingDirectory);
        w.setResultsDirectory(workingDirectory);
        w.setSecureResults(false);

        FileGroup fg = fileService.getFileGroupById(fileGroupId);

        Set<FileHandle> fastq = new TreeSet<FileHandle>(new FastqComparator(fastqService));
        logger.trace("file group: " + fg.getId() + " contains " + fg.getFileHandles().size() + " fileHandles.");

        fastq.addAll(fg.getFileHandles());

        Object[] fqa = (Object[]) fastq.toArray();

        w.addRequiredFile((FileHandle) fqa[firstFile]);
        if (paired)
            w.addRequiredFile((FileHandle) fqa[firstFile + 1]);

        String command = "trim_galore " + parameterString + " ${" + WorkUnit.INPUT_FILE + "[" + 0 + "]}";
        if (paired)
            command += " ${" + WorkUnit.INPUT_FILE + "[" + 1 + "]}";

        w.setCommand(command);

        return w;

    }

    public WorkUnit getRegisterTrimmedCommand(int runId, int cellLibraryId, String softwareName) throws SampleTypeException, SampleParentChildException,
            GridException, MetadataException {

        SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
        Job job = sampleService.getJobOfLibraryOnCell(cellLibrary);

        Run run = runService.getRunById(runId);

        List<SoftwarePackage> sd = new ArrayList<SoftwarePackage>();
        sd.add(this);
        logger.trace("attempting to get bean for " + softwareName.toString());
        SequenceRunProcessor sequencer = (SequenceRunProcessor) this.getApplicationContext().getBean(softwareName);
        sd.add(sequencer);

        WorkUnit w = new WorkUnit();
        w.setProcessMode(ProcessMode.SINGLE);
        w.setMode(ExecutionMode.PROCESS);
        w.setSoftwareDependencies(sd);

        GridWorkService workService = hostResolver.getGridWorkService(w);
        GridTransportConnection transportConnection = workService.getTransportConnection();

        String stageDir = transportConnection.getConfiguredSetting(sequencer.getStageDirectoryName());
        if (!PropertyHelper.isSet(stageDir))
            throw new GridException(sequencer.getStageDirectoryName() + " is not defined at " + transportConnection.getHostName() + "!");

        // TODO: fix hardcode
        String jobRoot = transportConnection.getConfiguredSetting("results.dir");
        if (!PropertyHelper.isSet(jobRoot))
            throw new GridException("results.dir is not defined!");

        // TODO: fix hardcode
        String workingDirectory = stageDir + "/" + run.getName() + "/wasp/sequence";

        // TODO: fix hardcode
        String resultsDirectory = jobRoot + "/" + job.getId() + "/fastq";

        w.setWorkingDirectory(workingDirectory);
        w.setResultsDirectory(resultsDirectory);
        w.setSecureResults(true);

        Set<FileGroup> fgs = fileService.getFilesForCellLibraryByType(cellLibrary, fastqService.getFastqFileType());
        if (fgs.size() != 1) {
            String m = "cell library " + cellLibrary.getId() + ":" + cellLibrary.getUUID() + " was expected to have 1 set of FASTQ files, found " + fgs.size();
            logger.error(m);
            throw new WaspRuntimeException(m);
        }

        Set<FileHandle> fastq = new TreeSet<FileHandle>(new FastqComparator(fastqService));

        FileGroup fastqG = fgs.iterator().next();
        logger.debug("Found FASTQ file group " + fastqG.getId() + " harboring " + fastqG.getFileHandles().size() + " handles.");
        fastq.addAll(fastqG.getFileHandles());
        Iterator<FileHandle> fhi = fastq.iterator();

        Set<FileHandle> trimmed_fastq = new HashSet<FileHandle>();

        Integer rs = fastqService.getNumberOfReadSegments(fastqG);

        w.setCommand("shopt -u nullglob");
        w.addCommand("rm -f " + fastqG.getId() + "_*_trim_counts.txt");

        int fileN = 0;
        Sample library = sampleService.getLibrary(cellLibrary);

        while (fhi.hasNext()) {
            FileHandle fh = fhi.next();
            w.addRequiredFile(fh);
            FileHandle newF = doFile(w, fileN++, fh, fastqG);
            fileTypeService.copyMetaByArea(fh, newF, FileTypeService.FILETYPE_AREA);
            fastqService.copyFastqFileHandleMetadata(fh, newF);
            newF.setFileName(library.getName() + "_" + newF.getFileName());
            trimmed_fastq.add(newF);

            if (rs == 2) {
                fh = fhi.next();
                trimmed_fastq.add(doFile(w, fileN++, fh, fastqG));
            }
        }

        w.addCommand(sortCommand(fastqG.getId(), 1));
        if (rs == 2) {
            w.addCommand(sortCommand(fastqG.getId(), 2));
        }

        FileGroup resultFiles = new FileGroup();
        resultFiles.setSoftwareGeneratedBy(this);

        for (SampleSource ss : fastqG.getSampleSources()) {
            ss.getFileGroups().add(resultFiles);
        }

        
        resultFiles.setDescription(library.getName() + " trimmed FASTQ files");
        resultFiles.setFileType(fastqService.getFastqFileType());
        resultFiles.getDerivedFrom().add(fastqG);
        resultFiles.setFileHandles(trimmed_fastq);
        fileService.addFileGroup(resultFiles);
        fastqService.copyFastqFileGroupMetadata(fastqG, resultFiles);
        fileTypeService.addAttribute(resultFiles, FastqFileTypeAttribute.TRIMMED);

        w.getResultFiles().add(resultFiles);

        fastqG.setIsActive(0);

        return w;
    }

    private FileHandle doFile(WorkUnit w, int fileNumber, FileHandle fileHandle, FileGroup fileGroup) throws MetadataException {
        Integer rs = fastqService.getNumberOfReadSegments(fileGroup);
        String prefix = "";
        prefix = "_" + fastqService.getFastqReadSegmentNumber(fileHandle);
        w.addCommand("sed -n '/^length/,/^$/p' ${" + WorkUnit.INPUT_FILE + "[" + fileNumber + "]}_trimming_report.txt | tail -n +2 | head -n -1 >> "
                + fileGroup.getId() + prefix + "_trim_counts.txt");
        if (rs == 2) {
            // paired-end read names end in "_val_?.fq.gz" while single-end
            // reads end with "_trimmed.fq.gz"
            prefix = "_val_" + prefix;
        } else {
            prefix = "_trimmed";
        }
        String trimmedName = fileHandle.getFileName().replace(".fastq.gz", prefix + ".fq.gz");
        w.addCommand("ln -s " + trimmedName + " ${" + WorkUnit.OUTPUT_FILE + "[" + fileNumber + "]} ");
        return createResultFile(fileHandle, trimmedName);
    }

    private String sortCommand(Integer fileId, Integer readSegment) {
        String filePrefix = fileId + "_" + readSegment;
        return "sort -nk1,1 " + filePrefix + "_trim_counts.txt | awk '{if (! a[$1]> 0) { a[$1]==0; b[$1]==0; c[$1]==0 }; "
                + "a[$1]+=$2;b[$1]+=$3;c[$1]=$4;}END{for (i in a) { print i \"\\t\" a[i] \"\\t\" b[i] \"\\t\" c[i] } }' | " + "sort -nk1,1 > " + filePrefix
                + "_sum_trim_counts.txt";
    }

    private FileHandle createResultFile(FileHandle originFile, String fileName) throws MetadataException {
        FileHandle trimmed = new FileHandle();
        trimmed.setFileName(fileName);
        trimmed.setFileType(fastqFileType);
        fileService.addFile(trimmed);
        fastqService.copyFastqFileHandleMetadata(originFile, trimmed);
        return trimmed;
    }

    /**
     * This method takes a grid result of a successfully run FastQC job, gets
     * the working directory and uses it to parse the <em>fastqc_data.txt</em>
     * file into a Map which contains static Strings defining the output keys
     * (see above) and JSONObjects representing the data.
     * 
     * @param result
     * @return
     * @throws GridException
     * @throws BabrahamDataParseException
     * @throws JSONException
     */
    public Map<String, JSONObject> parseOutput(String resultsDir) throws GridException, JSONException {

        return null;
    }

}
