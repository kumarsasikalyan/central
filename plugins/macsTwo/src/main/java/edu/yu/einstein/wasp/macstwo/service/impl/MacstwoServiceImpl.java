/**
 * Created by Wasp System Eclipse Plugin
 * @author 
 */
package edu.yu.einstein.wasp.macstwo.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.yu.einstein.wasp.exception.PanelException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.integration.messages.WaspSoftwareJobParameters;
import edu.yu.einstein.wasp.macstwo.service.MacstwoService;
import edu.yu.einstein.wasp.macstwo.webpanels.MacstwoWebPanels;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileGroupMeta;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.service.impl.WaspServiceImpl;
import edu.yu.einstein.wasp.viewpanel.PanelTab;


@Service
@Transactional("entityManager")
public class MacstwoServiceImpl extends WaspServiceImpl implements MacstwoService {
	
	@Autowired
	private SampleService sampleService;
	@Autowired
	private FileUrlResolver fileUrlResolver;
	@Autowired
	private RunService runService;
	
	public class SampleNameComparator implements Comparator<Sample> {
	    @Override
	    public int compare(Sample arg0, Sample arg1) {
	        return arg0.getName().compareToIgnoreCase(arg1.getName());
	    }
	}
	
	public class FileTypeComparator implements Comparator<FileType> {
	    @Override
	    public int compare(FileType arg0, FileType arg1) {
	        return arg0.getIName().compareToIgnoreCase(arg1.getIName());
	    }
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String performAction() {
		// do something
		return "done";
	}

	/**
	 * {@inheritDoc}
	 */
	@Transactional("entityManager")
	@Override
	public Set<PanelTab> getMacstwoDataToDisplay(Job job)throws PanelException{
		 
		 try{
			 //First, assemble the data			
			//samplePairs (for the samplePairsTab)
			Map<Sample, List<Sample>> testSampleControlSampleListMap = getTestSampleControlSampleListMap(job);//reviewed and OK		
			List<Sample> testSampleList = getTestSampleList(testSampleControlSampleListMap);//reviewed and OK	//basically will end up being an ordered set of the testSamples
			
			//commandLine (for the samplePairsTab)
			Map<String, String> sampleIdControlIdCommandLineMap = getSampleIdControlIdCommandLineMap(testSampleList);//reviewed and OK
			
			//sample library runs (for the runs tab)
			Map<Sample, List<SampleSource>> sampleCellLibraryListMap = getSampleCellLibraryListMap(testSampleList);//reviewed and OK
			Map<Sample,List<Sample>> sampleLibraryListMap = getSampleLibraryListMap(sampleCellLibraryListMap);//reviewed and OK
			Map<Sample, List<String>> libraryRunInfoListMap = getLibraryRunInfoListMap(sampleCellLibraryListMap);//reviewed and OK
	
			//get the fileGroups, fileHandles, resolvedURLs and fileTypeList 
			Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap = getCodedFileGroupMap(testSampleList);//new HashMap<String, FileGroup>();
			Map<String, FileHandle> sampleIdControlIdFileTypeIdFileHandleMap = getCodedFileHandleMap(sampleIdControlIdFileTypeIdFileGroupMap);//new HashMap<String, FileHandle>();
			Map<FileHandle, String> fileHandleResolvedURLMap = getFileHandleResolvedURLMap(sampleIdControlIdFileTypeIdFileHandleMap);//new HashMap<FileHandle, String>();
			List<FileType> fileTypeList = getOrderedFileTypeList(sampleIdControlIdFileTypeIdFileGroupMap);//actually, ordered set
			
			//SECOND, present the data within an ordered set of panel tabs (recall that the summary panel has already been taken care of)
			Set<PanelTab> panelTabSet = new LinkedHashSet<PanelTab>();

			PanelTab samplePairsPanelTab = MacstwoWebPanels.getSamplePairs(testSampleList, testSampleControlSampleListMap, sampleIdControlIdCommandLineMap);
			if(samplePairsPanelTab!=null){panelTabSet.add(samplePairsPanelTab);}
			PanelTab sampleLibraryRunsPanelTab = MacstwoWebPanels.getSampleLibraryRuns(testSampleList, sampleLibraryListMap, libraryRunInfoListMap);
			if(sampleLibraryRunsPanelTab!=null){panelTabSet.add(sampleLibraryRunsPanelTab);}
			PanelTab fileTypeDefinitionsPanelTab = MacstwoWebPanels.getFileTypeDefinitions(fileTypeList);
			if(fileTypeDefinitionsPanelTab!=null){panelTabSet.add(fileTypeDefinitionsPanelTab);}
			PanelTab allFilesDisplayedBySampleUsingGroupingGridPanelTab = MacstwoWebPanels.getFilesBySample(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
			if(allFilesDisplayedBySampleUsingGroupingGridPanelTab!=null){panelTabSet.add(allFilesDisplayedBySampleUsingGroupingGridPanelTab);}
			PanelTab allFilesDisplayedByFileTypeUsingGroupingGridPanelTab = MacstwoWebPanels.getFilesByFileType(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
			if(allFilesDisplayedByFileTypeUsingGroupingGridPanelTab!=null){panelTabSet.add(allFilesDisplayedByFileTypeUsingGroupingGridPanelTab);}
			
			PanelTab allModelPNGFilesDisplayedInPanelsTab = MacstwoWebPanels.getModelImages(testSampleList, testSampleControlSampleListMap, fileTypeList, sampleIdControlIdFileTypeIdFileHandleMap, fileHandleResolvedURLMap, sampleIdControlIdFileTypeIdFileGroupMap);
			if(allModelPNGFilesDisplayedInPanelsTab!=null){panelTabSet.add(allModelPNGFilesDisplayedInPanelsTab);}
			
			return panelTabSet;
			
		}catch(Exception e){
			logger.debug("exception in macstwoService.getChipSeqDataToDisplay(job): "+ e.getStackTrace());
			throw new PanelException(e.getMessage());
		}		
	}

	private Map<Sample, List<Sample>> getTestSampleControlSampleListMap(Job job){//reviewed and OK
		
		Map<Sample, List<Sample>> testSampleControlSampleListMap = new HashMap<Sample, List<Sample>>();
		
		Sample noControlSample = new Sample();
		noControlSample.setId(0);
		noControlSample.setName("None");
		
		for(Sample sample : job.getSample()){
			for(SampleMeta sm : sample.getSampleMeta()){
				if(sm.getK().startsWith("chipseqAnalysis.controlId::")){//there exists at least one chipseq analysis for this sample, but could be more than one (so do not put a break; at end of this meta for loop)
					if(!testSampleControlSampleListMap.containsKey(sample)){
						testSampleControlSampleListMap.put(sample, new ArrayList<Sample>());
					}
					Sample controlSample = null;
					Integer controlId = Integer.parseInt(sm.getV());
					if(controlId.intValue()==0){
						controlSample = noControlSample;
					}
					else{
						controlSample = sampleService.getSampleById(controlId);
					}
					testSampleControlSampleListMap.get(sample).add(controlSample);
				}
			}
		}
		
		//finally, order the controlSampleLists by controlSample.getName()
		/* 
		 class SampleNameComparator implements Comparator<Sample> {
		    @Override
		    public int compare(Sample arg0, Sample arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
		*/
		for(Sample testSample : testSampleControlSampleListMap.keySet()){
			Collections.sort(testSampleControlSampleListMap.get(testSample), new SampleNameComparator());
		}
		return testSampleControlSampleListMap;
	}
	
	private List<Sample> getTestSampleList(Map<Sample, List<Sample>> testSampleControlSampleListMap){
		List<Sample> testSampleList = new ArrayList<Sample>();
		for(Sample sample : testSampleControlSampleListMap.keySet()){
			testSampleList.add(sample);
		}	
		/*
		class SampleNameComparator implements Comparator<Sample> {
		    @Override
		    public int compare(Sample arg0, Sample arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
		*/
		Collections.sort(testSampleList, new SampleNameComparator());	//order list by sample name	
		return testSampleList;		
	}
	
	private Map<String, String> getSampleIdControlIdCommandLineMap(List<Sample> testSampleList){//reviewed and OK

		Map<String, String> sampleIdControlIdCommandLineMap = new HashMap<String, String>();		
		for(Sample testSample : testSampleList){			
			for(SampleMeta sm : testSample.getSampleMeta()){
				if(sm.getK().startsWith("chipseqAnalysis.commandLineCall::")){//capture for each distinct chipseq analysis (following the :: is controlId)
					String[] splitK = sm.getK().split("::");
					String controlIdAsString = splitK[1];
					sampleIdControlIdCommandLineMap.put(testSample.getId().toString() + "::" + controlIdAsString, sm.getV());
				}
			}
		}
		return sampleIdControlIdCommandLineMap;
	}
	
	private Map<Sample, List<SampleSource>> getSampleCellLibraryListMap(List<Sample> testSampleList)throws SampleTypeException{//reviewed and OK
		
		Map<Sample, List<SampleSource>> sampleCellLibraryListMap = new HashMap<Sample, List<SampleSource>>();
		for(Sample testSample : testSampleList){
			for(SampleMeta sm : testSample.getSampleMeta()){
				if(sm.getK().startsWith("chipseqAnalysis.testCellLibraryIdList::")){//while there could be more than one entry for a single sample, we only need to capture this meta once for each test sample (as it is the same for each entry: thus the break; command) (note: following the :: is controlId)
					String cellLibraryIdListAsString = sm.getV();
					List<Integer> cellLibraryIdList = WaspSoftwareJobParameters.getCellLibraryIdList(cellLibraryIdListAsString);
					sampleCellLibraryListMap.put(testSample, new ArrayList<SampleSource>());
					for(Integer cellLibraryId : cellLibraryIdList){
						SampleSource cellLibrary = sampleService.getCellLibraryBySampleSourceId(cellLibraryId);
						sampleCellLibraryListMap.get(testSample).add(cellLibrary);						
					}
					break;//break out of meta loop and move on to next testSample (since we only need to get this repeated info once)
				}
			}
		}	
		return sampleCellLibraryListMap;
	}
	
	private Map<Sample,List<Sample>>  getSampleLibraryListMap(Map<Sample, List<SampleSource>> sampleCellLibraryListMap){//reviewed and OK
		Map<Sample,List<Sample>> sampleLibraryListMap = new HashMap<Sample,List<Sample>>();
		for(Sample sample : sampleCellLibraryListMap.keySet()){
			List<SampleSource> cellLibraryList = sampleCellLibraryListMap.get(sample);
			sampleLibraryListMap.put(sample, new ArrayList<Sample>());
			
			for(SampleSource cellLibrary : cellLibraryList){
				Sample library = sampleService.getLibrary(cellLibrary);				
				if(!sampleLibraryListMap.get(sample).contains(library)){//the LibraryList is acting as a set (since a library could be run more than once)
					sampleLibraryListMap.get(sample).add(library);
				}
			}
		}
		
		//finally, order each LibraryList by library name
		/*
		class SampleNameComparator implements Comparator<Sample> {
		    @Override
		    public int compare(Sample arg0, Sample arg1) {
		        return arg0.getName().compareToIgnoreCase(arg1.getName());
		    }
		}
		*/
		for(Sample testSample : sampleLibraryListMap.keySet()){
			Collections.sort(sampleLibraryListMap.get(testSample), new SampleNameComparator());	//order each list by sample name				
		}
		return sampleLibraryListMap;
	}
	
	private Map<Sample, List<String>> getLibraryRunInfoListMap(Map<Sample, List<SampleSource>> sampleCellLibraryListMap)throws SampleTypeException, SampleParentChildException{//reviewed and OK
		Map<Sample, List<String>> libraryRunInfoListMap = new HashMap<Sample, List<String>>();
		for(Sample sample : sampleCellLibraryListMap.keySet()){
			List<SampleSource> cellLibraryList = sampleCellLibraryListMap.get(sample);
			for(SampleSource cellLibrary : cellLibraryList){
				Sample library = sampleService.getLibrary(cellLibrary);
				if(!libraryRunInfoListMap.containsKey(library)){
					libraryRunInfoListMap.put(library, new ArrayList<String>());
				}
				Sample cell = sampleService.getCell(cellLibrary);
				String lane = sampleService.getCellIndex(cell).toString(); 
				Sample platformUnit = sampleService.getPlatformUnitForCell(cell);
				List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);//WHY IS THIS A LIST rather than a singleton?
				if(runList.isEmpty()){
					libraryRunInfoListMap.get(library).add("Lane " + lane + ": " + platformUnit.getName()); 
				}
				else{
					libraryRunInfoListMap.get(library).add("Lane " + lane + ": " + runList.get(0).getName()); 
				}				
			}
		}		
		return libraryRunInfoListMap;
	}
	private Map<String, FileGroup> getCodedFileGroupMap(List<Sample> testSampleList){
		Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap = new HashMap<String, FileGroup>();
		for(Sample testSample : testSampleList){
			for(FileGroup fg : testSample.getFileGroups()){
				for(FileGroupMeta fgm : fg.getFileGroupMeta()){
					if(fgm.getK().equalsIgnoreCase("chipseqAnalysis.controlId")){
						String controlId = fgm.getV();						
						String fileTypeId = fg.getFileType().getId().toString();
						String sampleIdControlIdFileTypeIdKey = testSample.getId().toString()+"::"+controlId+"::"+fileTypeId;
						sampleIdControlIdFileTypeIdFileGroupMap.put(sampleIdControlIdFileTypeIdKey, fg);
						break;//from filegroupmeta for loop
					}
				}
			}
		}
		return sampleIdControlIdFileTypeIdFileGroupMap;
	}
	private Map<String, FileHandle> getCodedFileHandleMap(Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){
		Map<String, FileHandle> sampleIdControlIdFileTypeIdFileHandleMap = new HashMap<String, FileHandle>();
		for(String key : sampleIdControlIdFileTypeIdFileGroupMap.keySet()){
			FileGroup fg = sampleIdControlIdFileTypeIdFileGroupMap.get(key);
			FileHandle fileHandle = new ArrayList<FileHandle>(fg.getFileHandles()).get(0);
			sampleIdControlIdFileTypeIdFileHandleMap.put(key, fileHandle);
		}
		return sampleIdControlIdFileTypeIdFileHandleMap;				
	}
	private Map<FileHandle, String>  getFileHandleResolvedURLMap(Map<String, FileHandle> sampleIdControlIdFileTypeIdFileHandleMap){
		Map<FileHandle, String> fileHandleResolvedURLMap = new HashMap<FileHandle,String>();
		for(String key : sampleIdControlIdFileTypeIdFileHandleMap.keySet()){
			FileHandle fh = sampleIdControlIdFileTypeIdFileHandleMap.get(key);
			//******************ROBERT A DUBIN comment-out next line and uncomment the line following it for production!!!!!!!!
			String resolvedURL = "file://c3.einstein.yu.edu/wasp/results/3/submitted/215882339_Job3_Quote_2014_04_02.pdf";
			//String resolvedURL = "";
			try{
				resolvedURL = fileUrlResolver.getURL(fh).toString();
			}catch(Exception e){logger.debug("***************UNABLE TO RESOLVE URL for file: " + fh.getFileName());}
			
			fileHandleResolvedURLMap.put(fh, resolvedURL);
		}
		return fileHandleResolvedURLMap;
	}
	private List<FileType> getOrderedFileTypeList(Map<String, FileGroup> sampleIdControlIdFileTypeIdFileGroupMap){
		Set<FileType> fileTypeSet = new HashSet<FileType>();
		List<FileType> orderedFileTypeList = new ArrayList<FileType>();
		for(String key : sampleIdControlIdFileTypeIdFileGroupMap.keySet()){
			FileGroup fg = sampleIdControlIdFileTypeIdFileGroupMap.get(key);
			fileTypeSet.add(fg.getFileType());
		}
		orderedFileTypeList.addAll(fileTypeSet);//next, sort the list
		/*
		class FileTypeComparator implements Comparator<FileType> {
		    @Override
		    public int compare(FileType arg0, FileType arg1) {
		        return arg0.getIName().compareToIgnoreCase(arg1.getIName());
		    }
		}
		*/
		Collections.sort(orderedFileTypeList, new FileTypeComparator());
		
		return orderedFileTypeList;		
	}
}

