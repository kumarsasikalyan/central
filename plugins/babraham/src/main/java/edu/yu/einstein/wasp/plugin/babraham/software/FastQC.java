/**
 * 
 */
package edu.yu.einstein.wasp.plugin.babraham.software;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.filetype.FastqComparator;
import edu.yu.einstein.wasp.filetype.service.FastqService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.mps.illumina.IlluminaSequenceRunProcessor;
import edu.yu.einstein.wasp.plugin.babraham.exception.FastQCDataParseException;
import edu.yu.einstein.wasp.plugin.babraham.service.BabrahamService;
import edu.yu.einstein.wasp.software.SoftwarePackage;



/**
 * @author calder
 *
 */
public class FastQC extends SoftwarePackage {

	// Static variables for metadata keys
	public static final String BASIC_STATISTICS = "fastqc basic statistics";
	public static final String BASE_SEQUENCE_QUALITY = "fastqc base sequence quality";
	public static final String SEQUENCE_QUALITY = "fastqc sequence quality";
	public static final String BASE_SEQUENCE_CONTENT = "fastqc base sequence content"; 
	public static final String BASE_GC_CONTENT = "fastqc base GC content";
	public static final String SEQUENCE_GC_CONTENT = "fastqc sequence GC content";
	public static final String BASE_N_CONTENT = "fastqc base N content";
	public static final String SEQUENCE_LENGTH_DISTRIBUTION = "fastqc sequence length dist"; 
	public static final String SEQUENCE_DUPLICATION = "fastqc sequence duplication";
	public static final String OVERREPRESENTED_SEQUENCES = "fastqc overrepresented sequences";
	public static final String KMER_CONTENT = "fastqc k-mer content";

	@Autowired
	private FastqService fastqService;
	
	@Autowired
	private IlluminaSequenceRunProcessor casava;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7075104587205964069L;
	
	public static class PlotType{
		// meta keys for charts produced
		public static final String BASIC_STATISTICS = "basic_statistics";
		public static final String DUPLICATION_LEVELS = "duplication_levels";
		public static final String KMER_PROFILES = "kmer_profiles";
		public static final String PER_BASE_GC_CONTENT = "per_base_gc_content";
		public static final String PER_BASE_N_CONTENT = "per_base_n_content";
		public static final String PER_BASE_QUALITY = "per_base_quality";
		public static final String PER_BASE_SEQUENCE_CONTENT = "per_base_sequence_content";
		public static final String PER_SEQUENCE_GC_CONTENT = "per_sequence_gc_content";
		public static final String PER_SEQUENCE_QUALITY = "per_sequence_quality";
		public static final String SEQUENCE_LENGTH_DISTRIBUTION = "sequence_length_distribution"; 
		public static final String OVERREPRESENTED_SEQUENCES = "overrepresented_sequences";
	}
	
	@Autowired
	BabrahamService babrahamService;
	
	/**
	 * 
	 */
	public FastQC() {
		// TODO Auto-generated constructor stub
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getSoftwareVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public void setSoftwareVersion(String softwareVersion) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Takes a FileGroup and returns a configured WorkUnit to run FastQC on the file group.
	 * The output will be placed in folders that are numbered by their read segment number.
	 * e.g. a paired end run will generate two output folders, 1 and 2.
	 * 
	 * @param fileGroup
	 * @return
	 */
	public WorkUnit getFastQC(FileGroup fileGroup) {
		
		WorkUnit w = new WorkUnit();
		
		// require fastqc
		List<SoftwarePackage> software = new ArrayList<SoftwarePackage>();
		software.add(this);
		w.setSoftwareDependencies(software);
		
		// require 1GB memory
		w.setMemoryRequirements(1);
		
		// require a single thread, execution mode PROCESS
		// indicates this is a vanilla exectuion.
		w.setProcessMode(ProcessMode.SINGLE);
		w.setMode(ExecutionMode.PROCESS);
		
		// set working directory to scratch
		w.setWorkingDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		
		// we aren't actually going to retain any files, so we will set the output
		// directory to the scratch directory.  Also set "secure results" to
		// false to indicate that we don't care about the output.
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setSecureResults(false);
		
		// add the files to the work unit
		// files will be represented as bash variables in the work unit and
		// are sorted using the FastqComparator.  For single samples (what we have here)
		// this ensures that they will be in groups of read segments.
		// e.g.
		// s1.R1.001
		// s1.R2.001
		// s1.R1.002
		// s1.R2.002
		List<FileHandle> files = new ArrayList<FileHandle>(fileGroup.getFileHandles());
		Collections.sort(files, new FastqComparator(fastqService));
		w.setRequiredFiles(files);
		
		// set the command
		w.setCommand(getCommand(fileGroup));
		
		return w;
	}
	
	/**
	 * Set the fastqc command.  Choose casava mode if the data was generated by the Illumina plugin.
	 * 
	 * @param fileGroup
	 * @param workUnit
	 * @return
	 */
	private String getCommand(FileGroup fileGroup) {
		
		String command = "";
		int segments = fastqService.getNumberOfReadSegments(fileGroup);
		int files = fileGroup.getFileHandles().size();
		
		String[] fileList = new String[segments];
		
		for (int i = 0; i < segments; i++) {
			for (int j = 0; j < (files/segments); j++) {
				int index = ((j-1) * segments) + (i-1);
				fileList[i] += " ${" + WorkUnit.INPUT_FILE + "[" + index + "]}";
			}
		}
		
		String opts = "--noextract --nogroup --quiet";
		
		for (int i = 0; i < segments; i++) {
			int dir = i+1;
			command += "mkdir " + dir + "\n";
			// if casava, use casava mode
			if (fileGroup.getSoftwareGeneratedBy().equals(casava)) {
				command += "fastqc --casava " + opts + " --outdir " + i + " " + fileList[i] + "\n";
			} else {
				// otherwise treat like fastq
				String name = i + ".fq";
				command += "zcat " + fileList[i] + " > " + name + " && fastqc " + opts + " --outdir " + i + " " + name; 
			}
		}

		return command;
	}
	
	/**
	 * This method takes a grid result of a successfully run FastQC job, gets the working directory
	 * and uses it to parse the <em>fastqc_data.txt</em> file into a Map which contains
	 * static Strings defining the output keys (see above) and JSONObjects representing the
	 * data.  
	 * @param result
	 * @return
	 * @throws GridException
	 * @throws FastQCDataParseException
	 */
	public Map<String,JSONObject> parseOutput(GridResult result) throws GridException, FastQCDataParseException {
		Map<String, FastQCDataModule> dataModuleMap = babrahamService.parseFastQCOutput(result);
		return null;
	}

}
