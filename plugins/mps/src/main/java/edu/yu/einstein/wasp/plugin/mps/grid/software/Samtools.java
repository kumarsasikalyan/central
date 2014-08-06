/**
 * Created by Wasp System Eclipse Plugin
 * @author
 */
package edu.yu.einstein.wasp.plugin.mps.grid.software;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridTransportConnection;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.plugin.fileformat.service.BamService;
import edu.yu.einstein.wasp.software.SoftwarePackage;


/**
 * @author dubin
 */

public class Samtools extends SoftwarePackage{
	
	private static final long serialVersionUID = -7208955782745292954L;
	
	private static final String UNIQUELY_ALIGNED_READ_COUNT_FILENAME = "uniquelyAlignedReadCount.txt";
	private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME = "uniquelyAlignedNonRedundantReadCount.txt";
	
	private static final String TEMP_SAM_10M_READS_FILENAME = "tempSamFileWith10MReads.sam";
	private static final String UNIQUELY_ALIGNED_READ_COUNT_FROM_10M_READS_FILENAME = "uniquelyAlignedReadCountFrom10MReads.txt";
	private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_10M_READS_FILENAME = "uniquelyAlignedNonRedundantReadCountFrom10MReads.txt";

	private static final String TEMP_SAM_20M_READS_FILENAME = "tempSamFileWith20MReads.sam";
	private static final String UNIQUELY_ALIGNED_READ_COUNT_FROM_20M_READS_FILENAME = "uniquelyAlignedReadCountFrom20MReads.txt";
	private static final String UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_20M_READS_FILENAME = "uniquelyAlignedNonRedundantReadCountFrom20MReads.txt";

	public Samtools() {
		
	}
	
	public List<String> getCommandsForNonRedundantFraction(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		List<String> commandList = new ArrayList<String>();
		if(bamFileName==null || bamFileName.isEmpty()){
			return commandList;
		}
		commandList.addAll(getCommandsForNonRedundantFractionFromAllReads(bamFileName, alignerSpecificBamTagIndicatingUniqueAlignment));
		commandList.addAll(getCommandsForNonRedundantFractionFrom10Mand20MReads(bamFileName, alignerSpecificBamTagIndicatingUniqueAlignment));		
		return commandList;
	}
	
	public List<String> getCommandsForNonRedundantFractionFromAllReads(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		
		List<String> commandList = new ArrayList<String>();
		if(bamFileName==null || bamFileName.isEmpty()){
			return commandList;
		}
		//order doesn't really matter in this case
		commandList.add(this.getUniquelyAlignedReadCountCmd(bamFileName, alignerSpecificBamTagIndicatingUniqueAlignment));
		commandList.add(this.getUniquelyAlignedNonRedundantReadCountCmd(bamFileName, alignerSpecificBamTagIndicatingUniqueAlignment));
		return commandList;
	}
	
	private String getUniquelyAlignedReadCountCmd(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		String command = "";
		if(bamFileName==null || bamFileName.isEmpty()){
			return command;
		}
		if(alignerSpecificBamTagIndicatingUniqueAlignment != null && !alignerSpecificBamTagIndicatingUniqueAlignment.isEmpty()){
			command = "samtools view -F 0x4 " + bamFileName + " | awk 'BEGIN { c=0 } /" + alignerSpecificBamTagIndicatingUniqueAlignment + 
					"/ { c++ } END { print c }' > "   + UNIQUELY_ALIGNED_READ_COUNT_FILENAME;//includes duplicates
		}
		else{
			command = "samtools view -c -F 0x4 -q 1 " + bamFileName + " > " + UNIQUELY_ALIGNED_READ_COUNT_FILENAME;//includes duplicates
		}
		return command;
	}
	
	private String getUniquelyAlignedNonRedundantReadCountCmd(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		String command = "";
		if(bamFileName==null || bamFileName.isEmpty()){
			return command;
		}
		if(alignerSpecificBamTagIndicatingUniqueAlignment != null && !alignerSpecificBamTagIndicatingUniqueAlignment.isEmpty()){
			command = "samtools view -F 0x404 " + bamFileName + " | awk 'BEGIN { c=0 } /" + alignerSpecificBamTagIndicatingUniqueAlignment + 
					"/ { c++ } END { print c }' > "   + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME;//excludes duplicates
		}
		else{
			command = "samtools view -c -F 0x404 -q 1 " + bamFileName + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME;//excludes duplicates
		}
		return command;
	}
	
	public List<String> getCommandsForNonRedundantFractionFrom10Mand20MReads(String bamFileName, String alignerSpecificBamTagIndicatingUniqueAlignment){
		
		List<String> commandList = new ArrayList<String>();
		if(bamFileName==null || bamFileName.isEmpty()){
			return commandList;
		}
		//order IS important here
		//first: get the bamfile header and put it into new sam file named tempSamFileWith10MReads.sam (-H means add the header only)
		String command = "samtools view -H -o " + TEMP_SAM_10M_READS_FILENAME + " " + bamFileName;
		commandList.add(command);
		//second: append to named tempSamFileWith10MReads.sam the first 10,000,000 uniquely aligned reads using filter XT:A:U or use -q 1
		//it still works fine if there are less than 10M reads
		String command2 = "";
		if(alignerSpecificBamTagIndicatingUniqueAlignment != null && !alignerSpecificBamTagIndicatingUniqueAlignment.isEmpty()){
			command2 = "samtools view " + bamFileName + " | awk 'BEGIN { c=0 } /" + alignerSpecificBamTagIndicatingUniqueAlignment + "/ { print; c++; if (c>=10000000) exit; } END {}' >> " + TEMP_SAM_10M_READS_FILENAME;
		}
		else{
			command2 = "samtools view -q 1 " + bamFileName + " | awk 'BEGIN { c=0 } { print; c++; if (c>=10000000) exit; } END {}' >> " + TEMP_SAM_10M_READS_FILENAME;
		}
		commandList.add(command2);
		//third: get total count -- should be 10M or in some cases hopefully rare cases, less than 10M (-S since input is samfile)
		String command3 = "samtools view -S -c  " + TEMP_SAM_10M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_READ_COUNT_FROM_10M_READS_FILENAME;
		commandList.add(command3);
		//four: remove duplicates and get non-redundant count from those 10M reads
		String command4 = "samtools view -S -c -F 0x400 " + TEMP_SAM_10M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_10M_READS_FILENAME;
		commandList.add(command4);
		
		
		String command5 = "samtools view -H -o " + TEMP_SAM_20M_READS_FILENAME + " " + bamFileName;
		commandList.add(command5);
		//second: append to named tempSamFileWith20MReads.sam the first 20,000,000 uniquely aligned reads using filter XT:A:U or use -q 1
		//it still works fine if there are less than 20M reads
		String command6 = "";
		if(alignerSpecificBamTagIndicatingUniqueAlignment != null && !alignerSpecificBamTagIndicatingUniqueAlignment.isEmpty()){
			command6 = "samtools view " + bamFileName + " | awk 'BEGIN { c=0 } /" + alignerSpecificBamTagIndicatingUniqueAlignment + "/ { print; c++; if (c>=20000000) exit; } END {}' >> " + TEMP_SAM_20M_READS_FILENAME;
		}
		else{
			command6 = "samtools view -q 1 " + bamFileName + " | awk 'BEGIN { c=0 } { print; c++; if (c>=20000000) exit; } END {}' >> " + TEMP_SAM_20M_READS_FILENAME;
		}
		commandList.add(command6);
		//third: get total count -- should be 20M or in some cases hopefully rare cases, less than 20M (-S since input is samfile)
		String command7 = "samtools view -S -c  " + TEMP_SAM_20M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_READ_COUNT_FROM_20M_READS_FILENAME;
		commandList.add(command7);
		//four: remove duplicates and get non-redundant count from those 20M reads
		String command8 = "samtools view -S -c -F 0x400 " + TEMP_SAM_20M_READS_FILENAME + " > " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_20M_READS_FILENAME;
		commandList.add(command8);
		
		return commandList;
	}	
	
	public Map<String,String> getUniquelyAlignedReadCountMetrics(String scratchDirectory, GridHostResolver gridHostResolver)throws Exception{
		
		logger.debug("entering getUniquelyAlignedReadCountMetrics");
		
		Map<String,String> uniquelyAlignedReadCountMetricsMap = new HashMap<String,String>();
		
		String uniqueReads = "";//all the uniquely mapped reads
		String uniqueNonRedundantReads = "";		
		String uniqueReadsFrom10M = "";//the first 10M uniquely mapped reads
		String uniqueNonRedundantReadsFrom10M = "";//out of uniqueReadsFrom10M
		String uniqueReadsFrom20M = "";
		String uniqueNonRedundantReadsFrom20M = "";
		
		WorkUnit w = new WorkUnit();
		w.setProcessMode(ProcessMode.SINGLE);
		GridWorkService workService = gridHostResolver.getGridWorkService(w);
		GridTransportConnection transportConnection = workService.getTransportConnection();
		w.setWorkingDirectory(scratchDirectory);
		logger.debug("setting cat command in getPicardDedupMetrics");
		
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FROM_10M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_10M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_READ_COUNT_FROM_20M_READS_FILENAME );
		w.addCommand("cat " + UNIQUELY_ALIGNED_NON_REDUNDANT_READ_COUNT_FROM_20M_READS_FILENAME );
		
		GridResult r = transportConnection.sendExecToRemote(w);
		InputStream is = r.getStdOutStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is)); 
		boolean keepReading = true;
		int lineNumber = 0;
		logger.debug("getting ready to read 2 uniquelAlignedMetrics files");
		while (keepReading){
			lineNumber++;
			String line = null;
			line = br.readLine();
			logger.debug("line number = " + lineNumber + " and line = " + line);
			if (line == null){
				keepReading = false;
			}
			else if (lineNumber == 1){
				uniqueReads = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReads = " + uniqueReads);
			} else if (lineNumber == 2){
				uniqueNonRedundantReads = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReads = " + uniqueNonRedundantReads);
			} else if (lineNumber == 3){
				uniqueReadsFrom10M = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReadsFrom10M = " + uniqueReadsFrom10M);
			} else if (lineNumber == 4){
				uniqueNonRedundantReadsFrom10M = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReadsFrom10M = " + uniqueNonRedundantReadsFrom10M);
			} else if (lineNumber == 5){
				uniqueReadsFrom20M = line.replaceAll("\\n", "");//just in case there is a trailing new line				
				logger.debug("uniqueReadsFrom20M = " + uniqueReadsFrom20M);
			} else if (lineNumber == 6){
				uniqueNonRedundantReadsFrom20M = line.replaceAll("\\n", "");//just in case there is a trailing new line;				
				logger.debug("uniqueNonRedundantReadsFrom20M = " + uniqueNonRedundantReadsFrom20M);
			} 
			else {
				keepReading = false;
			}
			 
		}
		br.close();	
		
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS, uniqueReads);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS, uniqueNonRedundantReads);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_10M, uniqueReadsFrom10M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_10M, uniqueNonRedundantReadsFrom10M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_READS_FROM_20M, uniqueReadsFrom20M);
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_UNIQUE_NONREDUNDANT_READS_FROM_20M, uniqueNonRedundantReadsFrom20M);
		
		Double fractionUniqueNonRedundant_double = 0.0;
		String fractionUniqueNonRedundant = fractionUniqueNonRedundant_double.toString();
		if(!uniqueReads.isEmpty()&&!uniqueNonRedundantReads.isEmpty()){
			Integer uniqueReads_integer = Integer.valueOf(uniqueReads);
			Integer uniqueNonRedundantReads_integer = Integer.valueOf(uniqueNonRedundantReads);		
			if(uniqueReads_integer>0 && uniqueNonRedundantReads_integer>0){
				fractionUniqueNonRedundant_double = (double) uniqueNonRedundantReads_integer / uniqueReads_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundant = myFormat.format(fractionUniqueNonRedundant_double);						
			}
		}		
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT, fractionUniqueNonRedundant);
	
		Double fractionUniqueNonRedundantFrom10M_double = 0.0;
		String fractionUniqueNonRedundantFrom10M = fractionUniqueNonRedundantFrom10M_double.toString();
		if(!uniqueReadsFrom10M.isEmpty() && !uniqueNonRedundantReadsFrom10M.isEmpty() ){
			Integer uniqueReadsFrom10M_integer = Integer.valueOf(uniqueReadsFrom10M);
			Integer uniqueNonRedundantReadsFrom10M_integer = Integer.valueOf(uniqueNonRedundantReadsFrom10M);		
			if(uniqueReadsFrom10M_integer>0 && uniqueNonRedundantReadsFrom10M_integer>0){
				fractionUniqueNonRedundantFrom10M_double = (double) uniqueNonRedundantReadsFrom10M_integer / uniqueReadsFrom10M_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundantFrom10M = myFormat.format(fractionUniqueNonRedundantFrom10M_double);						
			}	
		}
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_10M, fractionUniqueNonRedundantFrom10M);

		Double fractionUniqueNonRedundantFrom20M_double = 0.0;
		String fractionUniqueNonRedundantFrom20M = fractionUniqueNonRedundantFrom20M_double.toString();
		if(!uniqueReadsFrom20M.isEmpty() && !uniqueNonRedundantReadsFrom20M.isEmpty() ){
			Integer uniqueReadsFrom20M_integer = Integer.valueOf(uniqueReadsFrom20M);
			Integer uniqueNonRedundantReadsFrom20M_integer = Integer.valueOf(uniqueNonRedundantReadsFrom20M);		
			if(uniqueReadsFrom20M_integer>0 && uniqueNonRedundantReadsFrom20M_integer>0){
				fractionUniqueNonRedundantFrom20M_double = (double) uniqueNonRedundantReadsFrom20M_integer / uniqueReadsFrom20M_integer;
				DecimalFormat myFormat = new DecimalFormat("0.000000");
				fractionUniqueNonRedundantFrom20M = myFormat.format(fractionUniqueNonRedundantFrom20M_double);						
			}	
		}
		uniquelyAlignedReadCountMetricsMap.put(BamService.BAMFILE_ALIGNMENT_METRIC_FRACTION_UNIQUE_NONREDUNDANT_FROM_20M, fractionUniqueNonRedundantFrom20M);

		logger.debug("leaving getUniquelyAlignedReadCountMetrics");
		return uniquelyAlignedReadCountMetricsMap;
		
	}
	

}
