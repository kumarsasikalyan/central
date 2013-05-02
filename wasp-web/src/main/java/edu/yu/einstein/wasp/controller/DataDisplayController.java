package edu.yu.einstein.wasp.controller;


import java.io.PrintWriter;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.MetaMessage;
import edu.yu.einstein.wasp.controller.util.MetaHelperWebapp;
import edu.yu.einstein.wasp.controller.util.SampleWrapperWebapp;
import edu.yu.einstein.wasp.dao.AdaptorDao;
import edu.yu.einstein.wasp.dao.AdaptorsetDao;
import edu.yu.einstein.wasp.dao.AdaptorsetResourceCategoryDao;
import edu.yu.einstein.wasp.dao.JobCellSelectionDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobSampleDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.dao.SampleMetaDao;
import edu.yu.einstein.wasp.dao.SampleSubtypeDao;
import edu.yu.einstein.wasp.dao.SampleTypeDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.MetadataException;
import edu.yu.einstein.wasp.exception.SampleParentChildException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.file.FileUrlResolver;
import edu.yu.einstein.wasp.model.Adaptor;
import edu.yu.einstein.wasp.model.Adaptorset;
import edu.yu.einstein.wasp.model.AdaptorsetResourceCategory;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.JobResourcecategory;
import edu.yu.einstein.wasp.model.JobUser;
import edu.yu.einstein.wasp.model.Run;
import edu.yu.einstein.wasp.model.RunMeta;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleMeta;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.model.SampleSourceMeta;
import edu.yu.einstein.wasp.model.SampleSubtype;
import edu.yu.einstein.wasp.model.User;
import edu.yu.einstein.wasp.plugin.supplemental.organism.Organism;
import edu.yu.einstein.wasp.service.AdaptorService;
import edu.yu.einstein.wasp.service.AuthenticationService;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.GenomeService;
import edu.yu.einstein.wasp.service.JobService;
import edu.yu.einstein.wasp.service.RoleService;
import edu.yu.einstein.wasp.service.RunService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.util.CellWrapper;
import edu.yu.einstein.wasp.util.MetaHelper;
import edu.yu.einstein.wasp.util.SampleWrapper;

@Controller
@RequestMapping("/datadisplay")
public class DataDisplayController extends WaspController {

  private SampleDao sampleDao;
  @Autowired
  public void setSampleDao(SampleDao sampleDao) {
    this.sampleDao = sampleDao;
  }
  public SampleDao getSampleDao() {
    return this.sampleDao;
  }

  @Autowired
  private SampleMetaDao sampleMetaDao;
  @Autowired
  private JobCellSelectionDao jobCellSelectionDao;
  @Autowired
  private JobDao jobDao;
  @Autowired
  private AdaptorDao adaptorDao;
  @Autowired
  private AdaptorsetDao adaptorsetDao;
  @Autowired
  private AdaptorsetResourceCategoryDao adaptorsetResourceCategoryDao;
  @Autowired
  private SampleTypeDao sampleTypeDao;
  @Autowired
  private SampleSubtypeDao sampleSubtypeDao;
  @Autowired
  private JobSampleDao jobSampleDao;
  
  @Autowired
  private RoleService roleService; 
  @Autowired
  private RunService runService; 
  @Autowired
  private SampleService sampleService;
  @Autowired
  private JobService jobService;
  @Autowired
  private FileService fileService;
  @Autowired
  private AuthenticationService authenticationService;
  @Autowired
  private FileUrlResolver fileUrlResolver;
  @Autowired
  private GenomeService genomeService;
  @Autowired
  private AdaptorService adaptorService;
  

  /*
  private final MetaHelperWebapp getMetaHelperWebapp() {
    return new MetaHelperWebapp(SampleMeta.class, request.getSession());
  }


  final public String defaultPageFlow = "/sampleDnaToLibrary/detail/{n};/sampleDnaToLibrary/addLibraryMeta/{n};/sampleDnaToLibrary/verify/{n}";

  public String nextPage(Sample sample) {
     String pageFlow = this.defaultPageFlow;

    String context = request.getContextPath();
    String uri = request.getRequestURI();
    // strips context, lead slash ("/"), spring mapping
    String currentMapping = uri.replaceFirst(context, "").replaceFirst("\\.do.*$", "");


    String pageFlowArray[] = pageFlow.split(";");

    int found = -1;
    for (int i=0; i < pageFlowArray.length -1; i++) {
      String page = pageFlowArray[i];
      page = page.replaceAll("\\{n\\}", ""+sample.getId());

      if (currentMapping.equals(page)) {
        found = i;
        break;
      }
    }

    String targetPage = pageFlowArray[found+1] + ".do";

    targetPage = targetPage.replaceAll("\\{n\\}", ""+sample.getId());

    return "redirect:" + targetPage;
  }


  @RequestMapping(value="/detail/{sampleId}", method=RequestMethod.GET)
  public String detail(@PathVariable("sampleId") Integer sampleId, ModelMap m) {
    Sample sample = sampleDao.getSampleBySampleId(sampleId); 

    MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
    List<SampleMeta> coreSampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());

    metaHelperWebapp.setArea("sampleDnaToLibrary");

    List<SampleMeta> sampleDnaLToLibrarySampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());
    
   
    m.put("sample", sample); 
    m.put("sampleId", sampleId); 
    m.put("coreMeta", coreSampleMeta); 
    m.put("sampleDnaToLibSampleMeta", sampleDnaLToLibrarySampleMeta); 

    m.put("area", metaHelperWebapp.getArea());
    m.put("parentarea", metaHelperWebapp.getParentArea());
  
    return "sampleDnaToLibrary/detail";
  }


  @RequestMapping(value="/addLibraryMeta/{sampleId}", method=RequestMethod.GET)
  public String addLibraryMetaForm(@PathVariable("sampleId") Integer sampleId, ModelMap m) {
    Sample sample = sampleDao.getSampleBySampleId(sampleId); 

    MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
    List<SampleMeta> coreSampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());

    metaHelperWebapp.setArea("sampleDnaToLibraryAddLibraryMeta");

    List<SampleMeta> sampleDnaLToLibrarySampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());
    
    m.put("sample", sample); 
    m.put("sampleId", sampleId); 
    m.put("coreMeta", coreSampleMeta); 
    m.put("sampleDnaToLibSampleMeta", sampleDnaLToLibrarySampleMeta); 

    m.put("area", metaHelperWebapp.getArea());
    m.put("parentarea", metaHelperWebapp.getParentArea());
  
    return "sampleDnaToLibrary/addLibraryMeta";
  }

  @RequestMapping(value="/detail/{sampleId}", method=RequestMethod.POST)
  public String modifyDetail (
     @PathVariable Integer sampleId,
     @Valid Sample sampleForm,
     BindingResult result,
     SessionStatus status,
     ModelMap m) {

     return appendMeta(sampleId, sampleForm, result, status, m, "sampleDnaToLibrary", "sampleDnaToLibrary/detail");
  }

  @RequestMapping(value="/addLibraryMeta/{sampleId}", method=RequestMethod.POST)
  public String modifyLibraryMeta (
     @PathVariable Integer sampleId,
     @Valid Sample sampleForm,
     BindingResult result,
     SessionStatus status,
     ModelMap m) {

     return appendMeta(sampleId, sampleForm, result, status, m, "sampleDnaToLibraryAddLibraryMeta",  "sampleDnaToLibrary/addLibraryMeta");
  }


  protected String appendMeta(
       @PathVariable Integer sampleId,
       @Valid Sample sampleForm,
       BindingResult result,
       SessionStatus status,
       ModelMap m, 
       String area,
       String returnPageDef
     ) {
     Sample sample = sampleDao.getSampleBySampleId(sampleId);

     MetaHelperWebapp metaHelperWebapp = getMetaHelperWebapp();
     metaHelperWebapp.setArea(area);

     List<SampleMeta> sampleMetaList = metaHelperWebapp.getFromRequest(request, SampleMeta.class);

     sampleForm.setSampleMeta(sampleMetaList);
     metaHelperWebapp.validate(result);

     if (result.hasErrors()) {
    	waspErrorMessage("sampleDetail.unexpected.error");

        metaHelperWebapp = getMetaHelperWebapp();
        List<SampleMeta> coreSampleMeta = metaHelperWebapp.syncWithMaster(sample.getSampleMeta());

        metaHelperWebapp.setArea(area);
    
        List<SampleMeta> sampleDnaLToLibrarySampleMeta = sampleMetaList;
    
        m.put("sample", sample); 
        m.put("sampleId", sampleId); 
        m.put("coreMeta", coreSampleMeta); 
        m.put("sampleDnaToLibSampleMeta", sampleDnaLToLibrarySampleMeta); 

        m.put("area", metaHelperWebapp.getArea());
        m.put("parentarea", metaHelperWebapp.getParentArea());
  
        return returnPageDef;
     }
 
     try {
		sampleMetaDao.setMeta(sampleMetaList, sampleId);
     } catch (MetadataException e) {
		logger.warn(e.getLocalizedMessage());
		waspErrorMessage("sampleDetail.unexpected.error");
     }
     return nextPage(sample);
  } 
  
  @RequestMapping(value="/listJobSamples/{jobId}", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String listJobSamples(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
    
	
	  if(jobId == null ){
		  waspErrorMessage("sampleDetail.jobParameter.error");
		  return "redirect:/dashboard.do";		  
	  }
	  Job job = jobDao.getJobByJobId(jobId);
	  if(job==null || job.getId()==null){
		  waspErrorMessage("sampleDetail.jobNotFound.error");
		  return "redirect:/dashboard.do";
	  }
	  m.addAttribute("job", job);
	  
	  List<JobUser> jobUserList = job.getJobUser();
	  List<User> additionalJobViewers = new ArrayList<User>();
	  for(JobUser jobUser : jobUserList){
		  if(jobUser.getUser().getUserId().intValue() != job.getUserId().intValue() && jobUser.getUser().getUserId().intValue() != job.getLab().getPrimaryUserId().intValue()){
			  additionalJobViewers.add(jobUser.getUser());
		  }
	  }
	  class SubmitterLastNameFirstNameComparator implements Comparator<User> {
			@Override
			public int compare(User arg0, User arg1) {
				return arg0.getLastName().concat(arg0.getFirstName()).compareToIgnoreCase(arg1.getLastName().concat(arg1.getFirstName()));
			}
		}
	  Collections.sort(additionalJobViewers, new SubmitterLastNameFirstNameComparator());
	  m.addAttribute("additionalJobViewers", additionalJobViewers);
	  
	  User currentWebViewer = authenticationService.getAuthenticatedUser();
	  Boolean currentWebViewerIsSuperuserSubmitterOrPI = false;
	  if(authenticationService.isSuperUser() || currentWebViewer.getUserId().intValue() == job.getUserId().intValue() || currentWebViewer.getUserId().intValue() == job.getLab().getPrimaryUserId().intValue()){
		  currentWebViewerIsSuperuserSubmitterOrPI = true; //superuser, job's submitter, job's PI
	  }
	  m.addAttribute("currentWebViewerIsSuperuserSubmitterOrPI", currentWebViewerIsSuperuserSubmitterOrPI);
	  m.addAttribute("currentWebViewer", currentWebViewer);
	  
	  //linkedHashMap because insert order is guarranteed
	  LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	  
	  LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
	 
	  m.addAttribute("jobApprovalsMap", jobApprovalsMap);	  
	  //get the jobApprovals Comments (if any)
	  HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
	  m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	  //get the current jobStatus
	  m.addAttribute("jobStatus", jobService.getJobStatus(job));
	  
	  List<Adaptorset> adaptorsetList = adaptorsetDao.findAll();
	  m.addAttribute("adaptorsets", adaptorsetList);
		
		
		
	  List<Sample> submittedSamplesList = jobService.getSubmittedSamples(job);
	  List<Sample> macromoleculeSubmittedSamplesList = new ArrayList<Sample>();
	  List<Sample> librarySubmittedSamplesList = new ArrayList<Sample>();
	  Map<Sample, String> organismMap = new HashMap<Sample, String>();
	  Map<Sample, String> receivedStatusMap = new HashMap<Sample, String>();
	  Map<Sample, String> qcStatusMap = new HashMap<Sample, String>();
	  Map<Sample, List<MetaMessage>> qcStatusCommentsMap = new HashMap<Sample, List<MetaMessage>>();
	  Map<Sample, Boolean> receiveSampleStatusMap = new HashMap<Sample, Boolean>();// created 5/7/12
	  Map<Sample, Boolean> createLibraryStatusMap = new HashMap<Sample, Boolean>();
	  Map<Sample, Boolean> assignLibraryToPlatformUnitStatusMap = new HashMap<Sample, Boolean>();
	  Map<Sample, List<Sample>> facilityLibraryMap = new HashMap<Sample, List<Sample>>();//key is a submitted sample (macromolecule) and value is list of facility-generated libraries created from that macromolecule)
	  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();//key is library and value is the adaptor used for that library
	  Map<Sample, String> showPlatformunitViewMap = new HashMap<Sample, String>();;
	  for(Sample sample : submittedSamplesList){
		  	if (!sampleService.isDnaOrRna(sample) && !sampleService.isLibrary(sample))
				throw new SampleTypeException("sample is not of expected type of DNA, RNA, Library or Facility Library");
			if(sampleService.isDnaOrRna(sample)){
				macromoleculeSubmittedSamplesList.add(sample);
				List<Sample> facilityGeneratedLibrariesList = sampleService.getFacilityGeneratedLibraries(sample);//get list of facility-generated libraries from a user-submitted macromolecule
				facilityLibraryMap.put(sample, facilityGeneratedLibrariesList);
				boolean isSampleWaitingForLibraryCreation = sampleService.isSampleAwaitingLibraryCreation(sample);
				logger.debug("setting sample " + sample.getId() + " (" + sample.getName() + ") is waiting for library creation = "+ isSampleWaitingForLibraryCreation);
				createLibraryStatusMap.put(sample, isSampleWaitingForLibraryCreation);
				qcStatusMap.put(sample, sampleService.convertSampleQCStatusForWeb(sampleService.getSampleQCStatus(sample)));
				qcStatusCommentsMap.put(sample, sampleService.getSampleQCComments(sample.getId()));
				for (Sample facilityLibrary: facilityGeneratedLibrariesList){
					qcStatusMap.put(facilityLibrary, sampleService.convertSampleQCStatusForWeb(sampleService.getLibraryQCStatus(facilityLibrary)));
					qcStatusCommentsMap.put(facilityLibrary, sampleService.getSampleQCComments(facilityLibrary.getId()));
					assignLibraryToPlatformUnitStatusMap.put(facilityLibrary, sampleService.isLibraryAwaitingPlatformUnitPlacement(facilityLibrary));
				}
			}
			else if(sampleService.isLibrary(sample)){
				librarySubmittedSamplesList.add(sample);
				assignLibraryToPlatformUnitStatusMap.put(sample, sampleService.isLibraryAwaitingPlatformUnitPlacement(sample));
				qcStatusMap.put(sample, sampleService.convertSampleQCStatusForWeb(sampleService.getLibraryQCStatus(sample)));
				qcStatusCommentsMap.put(sample, sampleService.getSampleQCComments(sample.getId()));
			}
			organismMap.put(sample, sampleService.getNameOfOrganism(sample, "Other"));
			receivedStatusMap.put(sample, sampleService.convertSampleReceivedStatusForWeb(sampleService.getReceiveSampleStatus(sample)));
			
			receiveSampleStatusMap.put(sample, sampleService.isSampleReceived(sample));
				
		}
		//sampleService.sortSamplesBySampleName(macromoleculeSubmittedSamplesList);
		//sampleService.sortSamplesBySampleName(librarySubmittedSamplesList);
		
		//for each library (those in facilityLibraryMap and those in librarySubmittedSamplesList) get flowcells/runs and add to map Map<Sample, List<Sample>>
	  	Map<Sample, List<CellWrapper>> cellsByLibrary = new HashMap<Sample, List<CellWrapper>>();
	  	Set<Sample> allLibraries = new HashSet<Sample>();
	  	allLibraries.addAll(librarySubmittedSamplesList);
	  	for(List<Sample> libraryList : facilityLibraryMap.values())
	  		allLibraries.addAll(libraryList);
	  	for (Sample library: allLibraries){
			Adaptor adaptor = sampleService.getLibraryAdaptor(library);
			if(adaptor==null){
				//message and get out of here
			}
			libraryAdaptorMap.put(library, adaptor);	
			List<Sample> cells = sampleService.getCellsForLibrary(library);
			for (Sample cell : cells){
				if (cellsByLibrary.get(library) == null){
					cellsByLibrary.put(library, new ArrayList<CellWrapper>());
					try {
						CellWrapper cellWrapper = new CellWrapper(cell, sampleService);
						Sample platformunit = cellWrapper.getPlatformUnit();
						if (platformunit != null)
							showPlatformunitViewMap.put(platformunit, sampleService.getPlatformunitViewLink(platformunit));
						cellsByLibrary.get(library).add(cellWrapper);
					} catch (SampleParentChildException e) {
						logger.warn(e.getLocalizedMessage());
					}
				}
			}
			
		}
	  	
	  	
		for(List<Sample> libraryList : facilityLibraryMap.values()){
			for(Sample library : libraryList){
				Adaptor adaptor = sampleService.getLibraryAdaptor(library);
				if(adaptor==null){
					//message and get out of here
				}
				libraryAdaptorMap.put(library, adaptor);
			}
		}
		
		List<Sample> availableAndCompatibleFlowCells = sampleService.getAvailableAndCompatiblePlatformUnits(job);//available flowCells that are compatible with this job
		for(Sample flowCell : availableAndCompatibleFlowCells){
			try{
				for (Sample cell: sampleService.getIndexedCellsOnPlatformUnit(flowCell).values()){
					for (Sample library: sampleService.getLibrariesOnCell(cell)){
						Adaptor adaptor = sampleService.getLibraryAdaptor(library);
						if(adaptor==null){
							logger.warn("Expected an adaptor but found none in library id="+library.getId().toString());
						}
						libraryAdaptorMap.put(library, adaptor);
					}
				}
			} catch(SampleTypeException e){
				logger.warn(e.getMessage());
			}
		}
	
		m.addAttribute("coverageMap", jobService.getCoverageMap(job));
		m.addAttribute("totalNumberCellsRequested", job.getJobCellSelection().size());

		List<FileGroup> fileGroups = new ArrayList<FileGroup>();
		Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
		List<FileHandle> fileHandlesThatCanBeViewedList = new ArrayList<FileHandle>();
		for(JobFile jf: job.getJobFile()){
			FileGroup fileGroup = jf.getFile();//returns a FileGroup
			fileGroups.add(fileGroup);
			List<FileHandle> fileHandles = new ArrayList<FileHandle>();
			for(FileHandle fh : fileGroup.getFileHandles()){
				fileHandles.add(fh);
				String mimeType = fileService.getMimeType(fh.getFileName());
				if(!mimeType.isEmpty()){
					fileHandlesThatCanBeViewedList.add(fh);
				}
			}
			fileGroupFileHandlesMap.put(fileGroup, fileHandles);
		}
		
		m.addAttribute("showPlatformunitViewMap", showPlatformunitViewMap);
		m.addAttribute("macromoleculeSubmittedSamplesList", macromoleculeSubmittedSamplesList);
		m.addAttribute("facilityLibraryMap", facilityLibraryMap);
		m.addAttribute("librarySubmittedSamplesList", librarySubmittedSamplesList);
		m.addAttribute("organismMap", organismMap);
		m.addAttribute("receivedStatusMap", receivedStatusMap);
		m.addAttribute("qcStatusMap", qcStatusMap);
		m.addAttribute("qcStatusCommentsMap", qcStatusCommentsMap);
		m.addAttribute("receiveSampleStatusMap", receiveSampleStatusMap);
		m.addAttribute("createLibraryStatusMap", createLibraryStatusMap);
		m.addAttribute("assignLibraryToPlatformUnitStatusMap", assignLibraryToPlatformUnitStatusMap);
		m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
		m.addAttribute("availableAndCompatibleFlowCells", availableAndCompatibleFlowCells);
		m.addAttribute("cellsByLibrary", cellsByLibrary);
		m.addAttribute("fileGroups", fileGroups);
		m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
		m.addAttribute("fileHandlesThatCanBeViewedList", fileHandlesThatCanBeViewedList);
		
		return "sampleDnaToLibrary/listJobSamples";
  }
  
  @RequestMapping(value="/removeViewerFromJob/{jobId}/{UserId}.do", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('jv-' + #jobId)")
  public String removeViewerFromJob(@PathVariable("jobId") Integer jobId, @PathVariable("UserId") Integer userId, ModelMap m) {
	  
	  try{
		  jobService.removeJobViewer(jobId, userId);//performs checks to see if this is a legal action. 
	  }
	  catch(Exception e){
		  logger.warn(e.getMessage());
		  waspErrorMessage(e.getMessage());
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }	
	  // if i am the user, reauth
	  User me = authenticationService.getAuthenticatedUser();
	  if (me.getUserId().intValue() == userId.intValue()) {
		doReauth();//do this if the person performing the action is the person being removed from viewing this job (note: it cannot be the submitter or the pi)
	  }
	  waspMessage("listJobSamples.jobViewerRemoved.label");
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
  }
  
  @RequestMapping(value="/addJobViewer.do", method=RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('jv-' + #jobId)")
  public String addJobViewer(@RequestParam("jobId") Integer jobId,
		  @RequestParam("newViewerEmailAddress") String newViewerEmailAddress,
		  ModelMap m)  {	
	  try{
		   jobService.addJobViewer(jobId, newViewerEmailAddress);//performs checks to see if this is a legal action. 
	  }
	  catch(Exception e){		    
		  logger.warn(e.getMessage());
		  waspErrorMessage(e.getMessage());
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }
	  waspMessage("listJobSamples.jobViewerAdded.label");
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";	  
  }
  
    //// **
  // * get adaptorsets and adaptors for populating model. If a selected adaptor is found in the provided SampleDraftMeta
   // * it is used to find appropriate adaptors
  //  * @param jobDraft
   // * @param sampleDraftMeta
  //  * @param m
  //  * /
	private void prepareAdaptorsetsAndAdaptors(Job job, List<SampleMeta> sampleMeta, ModelMap m){
		List<Adaptorset> adaptorsets = new ArrayList<Adaptorset>();
		for (JobResourcecategory jrc: job.getJobResourcecategory()){
			Map<String, Integer> adaptorsetRCQuery = new HashMap<String, Integer>();
			adaptorsetRCQuery.put("resourcecategoryId", jrc.getResourcecategoryId());
			for (AdaptorsetResourceCategory asrc: adaptorsetResourceCategoryDao.findByMap(adaptorsetRCQuery))
				adaptorsets.add(asrc.getAdaptorset());
		}
		m.addAttribute("adaptorsets", adaptorsets); // required for adaptorsets metadata control element (select:${adaptorsets}:adaptorsetId:name)
		
		List<Adaptor> adaptors = new ArrayList<Adaptor>();
		Adaptorset selectedAdaptorset = null;
		try{	
  			selectedAdaptorset = adaptorsetDao.getAdaptorsetByAdaptorsetId(Integer.valueOf( MetaHelper.getMetaValue("genericLibrary", "adaptorset", sampleMeta)) );
  		} catch(MetadataException e){
  			logger.warn("Cannot get metadata genericLibrary.adaptorset. Presumably not be defined: " + e.getMessage());
  		} catch(NumberFormatException e){
  			logger.warn("Cannot convert to numeric value for metadata " + e.getMessage());
  		}
		if (selectedAdaptorset != null){
			adaptors = selectedAdaptorset.getAdaptor();
		} else if (adaptorsets.size() == 1){
			adaptors = adaptorsets.get(0).getAdaptor();
		}
		m.addAttribute("adaptors", adaptors); // required for adaptors metadata control element (select:${adaptors}:adaptorId:barcodenumber)
	}
	

  @RequestMapping(value = "/createLibraryFromMacro/{jobId}/{macromolSampleId}", method = RequestMethod.GET)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
  @PreAuthorize("hasRole('su') or hasRole('ft')")
  public String createLibrary(@PathVariable("macromolSampleId") Integer macromolSampleId,
		  @PathVariable("jobId") Integer jobId,
		  ModelMap m) {

	  String returnString = validateJobIdAndSampleId(jobId, macromolSampleId, null);
	  if (returnString != null)
		  return returnString;

	  Job job = jobDao.getJobByJobId(jobId);
	  LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	  
	  LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
	  m.addAttribute("jobApprovalsMap", jobApprovalsMap);
	  //get the jobApprovals Comments (if any)
	  HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
	  m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	  //get the current jobStatus
	  m.addAttribute("jobStatus", jobService.getJobStatus(job));
	  
	  Sample macromoleculeSample = sampleDao.getSampleBySampleId(macromolSampleId);

	  m.put("macromoleculeSample", macromoleculeSample);
	  m.put("job", job);
	  m.addAttribute("organism", sampleService.getNameOfOrganism(macromoleculeSample, "Other"));

	   
	  String[] roles = {"ft"};
	  List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(job.getWorkflow().getId(), roles, "library");
	  if(librarySampleSubtypes.isEmpty()){
		  waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do"; // no workflowsubtype sample
	  }
	  SampleSubtype librarySampleSubtype = librarySampleSubtypes.get(0); // should be one
	  List<SampleMeta> libraryMeta = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(librarySampleSubtype);
	  prepareAdaptorsetsAndAdaptors(job, libraryMeta, m);
	  
	  Sample library = new Sample();
	  library.setSampleSubtype(librarySampleSubtype);
	  library.setSampleType(sampleTypeDao.getSampleTypeByIName("facilityLibrary"));
	  library.setSampleMeta(libraryMeta);
	  m.put("sample", library); 	

	  return "sampleDnaToLibrary/createLibrary";

  }


  @RequestMapping(value = "/createLibraryFromMacro/{jobId}/{macromolSampleId}", method = RequestMethod.POST)//here, macromolSampleId represents a macromolecule (genomic DNA or RNA) submitted to facility for conversion to a library
  @PreAuthorize("hasRole('su') or hasRole('ft')")
  public String createLibrary(@PathVariable("macromolSampleId") Integer macromolSampleId,
		  @PathVariable("jobId") Integer jobId, 
		  @Valid Sample libraryForm, BindingResult result, 
		  SessionStatus status, 
		  ModelMap m) {

	  String returnString = validateJobIdAndSampleId(jobId, macromolSampleId, null);
	  if (returnString != null)
		  return returnString;

	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  }


	  Sample parentMacromolecule = sampleDao.getSampleBySampleId(macromolSampleId);
	  Job jobForThisSample = jobDao.getJobByJobId(jobId);

	  m.addAttribute("extraJobDetailsMap", jobService.getExtraJobDetails(jobForThisSample));
	  LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(jobForThisSample);
	  m.addAttribute("jobApprovalsMap", jobApprovalsMap);
	  //get the jobApprovals Comments (if any)
	  HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
	  m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	  //get the current jobStatus
	  m.addAttribute("jobStatus", jobService.getJobStatus(jobForThisSample));
	  m.addAttribute("organism", sampleService.getNameOfOrganism(parentMacromolecule, "Other"));
	  
	  libraryForm.setName(libraryForm.getName().trim());
	  SampleSubtype sampleSubtype = sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId());
	  validateSampleNameUnique(libraryForm.getName(), macromolSampleId, jobForThisSample, result);

	  // get validated metadata from 
	  List<SampleMeta> sampleMetaListFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, sampleSubtype, result);

	  if (result.hasErrors()) {
		  libraryForm.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sampleSubtype, sampleMetaListFromForm));
		  libraryForm.setSampleSubtype(sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()));
		  libraryForm.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(libraryForm.getSampleTypeId()));
		  prepareSelectListData(m);//doubt that this is required here; really only needed for meta relating to country or state
		  waspErrorMessage("sampleDetail.updated.error");
		  prepareAdaptorsetsAndAdaptors(jobForThisSample, libraryForm.getSampleMeta(), m);
		  m.put("macromoleculeSample", parentMacromolecule);
		  m.put("job", jobForThisSample);
		  m.put("sample", libraryForm); 
		  return "sampleDnaToLibrary/createLibrary";
	  }

	  //all OK so create/save new library
	  libraryForm.setSubmitterLabId(parentMacromolecule.getSubmitterLabId());//needed??
	  libraryForm.setSubmitterUserId(parentMacromolecule.getSubmitterUserId());//needed??
	  libraryForm.setSubmitterJobId(parentMacromolecule.getSubmitterJobId());//needed??
	  libraryForm.setIsActive(new Integer(1));
	  SampleWrapper managedLibraryFromForm = new SampleWrapperWebapp(libraryForm);
	   try {
		  managedLibraryFromForm.setParent(parentMacromolecule);
		  sampleService.createFacilityLibraryFromMacro(jobForThisSample, managedLibraryFromForm, sampleMetaListFromForm);
		  waspMessage("libraryCreated.created_success.label");
	  } catch (SampleParentChildException e) {
		  logger.warn(e.getLocalizedMessage());
		  waspErrorMessage("libraryCreated.sample_problem.error");
	  } catch (MessagingException e) {
		  logger.warn(e.getLocalizedMessage());
		  waspErrorMessage("libraryCreated.message_fail.error");
	  } 
	  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";

  } 
  

  @RequestMapping(value = "/librarydetail_ro/{jobId}/{libraryId}", method = RequestMethod.GET)//sampleId represents an existing library (at this moment both user supplied or facility created)
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('jv-' + #jobId)")
	public String libraryDetailRO(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, ModelMap m) throws MetadataException{
	  return validateJobIdAndSampleId(jobId, libraryId, 
			  libraryDetail(jobId, libraryId, m, false) );
  }
  
  @RequestMapping(value = "/librarydetail_rw/{jobId}/{libraryId}", method = RequestMethod.GET)
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String libraryDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, ModelMap m) throws MetadataException{
	  return validateJobIdAndSampleId(jobId, libraryId, 
			  libraryDetail(jobId, libraryId, m, true) );
  }
  
  @RequestMapping(value = "/librarydetail_rw/{jobId}/{libraryId}", method = RequestMethod.POST)
  @PreAuthorize("hasRole('su') or hasRole('ft')")
  public String libraryDetailEdit(
		  @PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, 
		  @Valid Sample libraryForm, BindingResult result, 
		  SessionStatus status, 
		  ModelMap m) throws MetadataException{

	  // validate path variables
	  String returnString = validateJobIdAndSampleId(jobId, libraryId, null);
	  if (returnString != null)
		  return returnString;
	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  //return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  } 
	  
	  libraryForm.setName(libraryForm.getName().trim());
	  
	  Sample library = sampleDao.getSampleBySampleId(libraryId); 
	  validateSampleNameUnique(libraryForm.getName(), libraryId, jobDao.getJobByJobId(jobId), result);
	  SampleWrapperWebapp managedLibrary = new SampleWrapperWebapp(library);
	  List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, 
			  sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryForm.getSampleSubtypeId()), result); 
	  if(result.hasErrors()){
		  waspErrorMessage("sampleDetail.updated.error");
		  libraryForm.setSampleMeta(metaFromForm);
		  return libraryDetail(jobId, libraryForm, libraryId, m, true);
	  }
	  // all ok so save 
	  library.setName(libraryForm.getName());
	  sampleService.updateExistingSampleViaSampleWrapper(managedLibrary, metaFromForm);
	  //managedLibrary.updateMetaToList(metaFromForm, sampleMetaDao);//fixed with line above, this line should no longer be neeeded
	  //managedLibrary.saveAll(sampleService);//fixed with above, this line should no longer be neeeded

	  waspMessage("sampleDetail.updated_success.label");
	  return "redirect:/sampleDnaToLibrary/librarydetail_ro/"+jobId+"/"+libraryId+".do";
  }
  	
  // **
   // * Handles preparation of model for display of library details. Makes a detached Sample object.
   // * @param jobId
  //  * @param libraryInId
  //  * @param m
   // * @param isRW
  //  * @return
  //  * @throws MetadataException
   //  * /
  public String libraryDetail(Integer jobId, Integer libraryInId, ModelMap m, boolean isRW) throws MetadataException{
		// get the library subtype for this workflow as the job-viewer sees it. We will use this 
		// to synchronize the metadata for display.
	    // We make a new Sample object 'modelLibrary' so that we can use it with the model and adjust the sample subtype / metadata freely
	    // without affecting the info in the database
		Sample libraryIn = sampleDao.getSampleBySampleId(libraryInId);
		String[] roles = {"lu"};
		List<SampleSubtype> librarySampleSubtypes = sampleService.getSampleSubtypesForWorkflowByRole(jobDao.getJobByJobId(jobId).getWorkflow().getWorkflowId(), roles, "library");
		if(librarySampleSubtypes.isEmpty()){
			waspErrorMessage("sampleDetail.sampleSubtypeNotFound.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do"; // no workflowsubtype sample
		}
		Sample modelLibrary = new Sample();
		modelLibrary.setSampleSubtype(librarySampleSubtypes.get(0)); // should be one
		if (libraryIn.getParentId() != null)
			modelLibrary.setParent(libraryIn.getParent());
		modelLibrary.setId(libraryIn.getId());
		modelLibrary.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(libraryIn.getSampleTypeId()));
		modelLibrary.setName(libraryIn.getName());
		modelLibrary.setSampleMeta(libraryIn.getSampleMeta());
		return libraryDetail(jobId, modelLibrary, libraryInId, m, isRW);
  }
  
  
  // **
  //  * Handles preparation of model for display of library details. 
  //  * @param jobId
  //  * @param libraryIn: should be a detached entity
 //   * @param libraryInId: necessary if libraryIn has no id (e.g. from form)
 //   * @param m
 //   * @param isRW
 //   * @return
 //   * @throws MetadataException
 //   * /
  public String libraryDetail(Integer jobId, Sample libraryIn, Integer libraryInId, ModelMap m, boolean isRW) throws MetadataException{
	  	Job job = jobDao.getJobByJobId(jobId);
	  	
	  	// libraryIn should be a detached Sample object. If the sampleId is null then the Sample object is from a form and all the metadata from the form is 
	  	// assumed to be associated with it. Otherwise we assume that the library info is cloned from a persisted object.
		SampleWrapperWebapp libraryInManaged = new SampleWrapperWebapp(libraryIn);
		
  		
		libraryIn.setSampleMeta(SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(
				sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(libraryIn.getSampleSubtypeId()), 
				libraryInManaged.getAllSampleMeta()));
		

		SampleWrapperWebapp persistentLibraryManaged;
		if (libraryIn.getId() == null){
			persistentLibraryManaged = new SampleWrapperWebapp(sampleDao.getSampleBySampleId(libraryInId));
		} else {
			persistentLibraryManaged = libraryInManaged;
		}
		Sample parentMacromolecule = null;
		if (persistentLibraryManaged.getParentWrapper() != null)
			parentMacromolecule = persistentLibraryManaged.getParentWrapper().getSampleObject();
		
		prepareAdaptorsetsAndAdaptors(job, libraryIn.getSampleMeta(), m);
		if (libraryIn.getId() == null)
			libraryIn.setSampleId(libraryInId);
		m.addAttribute("job", job);
		m.addAttribute("extraJobDetailsMap", jobService.getExtraJobDetails(job));
		LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
		m.addAttribute("jobApprovalsMap", jobApprovalsMap);
		//get the jobApprovals Comments (if any)
		HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
		m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
		//get the current jobStatus
		m.addAttribute("jobStatus", jobService.getJobStatus(job));

		m.addAttribute("sample", libraryIn);
		m.addAttribute("parentMacromolecule", parentMacromolecule);
		return isRW?"sampleDnaToLibrary/librarydetail_rw":"sampleDnaToLibrary/librarydetail_ro";
  }
  

  
  
  @RequestMapping(value = "/sampledetail_ro/{jobId}/{sampleId}", method = RequestMethod.GET)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('jv-' + #jobId)")
	public String sampleDetailRO(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, ModelMap m) {
	  return sampleDetail(jobId, sampleId, m, false);
  }
 
  @RequestMapping(value = "/sampledetail_rw/{jobId}/{sampleId}", method = RequestMethod.GET)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String sampleDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, ModelMap m) {
	  return sampleDetail(jobId, sampleId, m, true);
  }
 
  
  public String sampleDetail(Integer jobId, Integer sampleId, ModelMap m, boolean isRW){
	  // validate path variables
	  String returnString = validateJobIdAndSampleId(jobId, sampleId, null);
	  if (returnString != null)
		  return returnString;
	  
	  Job job = jobDao.getJobByJobId(jobId);
	  
	  LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	  
	  LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
	  m.addAttribute("jobApprovalsMap", jobApprovalsMap);
	  //get the jobApprovals Comments (if any)
	  HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
	  m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	  //get the current jobStatus
	  m.addAttribute("jobStatus", jobService.getJobStatus(job));

	  Sample sample= sampleDao.getSampleBySampleId(sampleId);
	  //confirm these two objects exist and part of same job
	  
	  SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(sample);
	  m.addAttribute("normalizedSampleMeta", SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sample.getSampleSubtype(), sampleManaged.getAllSampleMeta()) );
	  m.put("job", job);
	  m.put("sample", sample); 
	  
	  return isRW?"sampleDnaToLibrary/sampledetail_rw":"sampleDnaToLibrary/sampledetail_ro";
  }

  @RequestMapping(value = "/sampledetail_rw/{jobId}/{sampleId}", method = RequestMethod.POST)//sampleId represents a macromolecule (genomic DNA or RNA) , but that could change as this evolves
	@PreAuthorize("hasRole('su') or hasRole('ft')")
	public String updateSampleDetailRW(@PathVariable("jobId") Integer jobId, @PathVariable("sampleId") Integer sampleId, 
								@Valid Sample sampleForm, BindingResult result, 
								SessionStatus status, ModelMap m) throws MetadataException {
		  
	  // validate path variables
	  String returnString = validateJobIdAndSampleId(jobId, sampleId, null);
	  if (returnString != null)
		  return returnString;
	  
	  if ( request.getParameter("submit").equals("Cancel") ){//equals(messageService.getMessage("userDetail.cancel.label")
		  //return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";
		  return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
	  } 
		
	  Job jobForThisSample = jobDao.getJobByJobId(jobId);
	  
	  LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(jobForThisSample);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	  
	  LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(jobForThisSample);
	  m.addAttribute("jobApprovalsMap", jobApprovalsMap);
	  //get the jobApprovals Comments (if any)
	  HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
	  m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	  //get the current jobStatus
	  m.addAttribute("jobStatus", jobService.getJobStatus(jobForThisSample));
		  	  		  
	  sampleForm.setName(sampleForm.getName().trim());//from the form
	  validateSampleNameUnique(sampleForm.getName(), sampleId, jobForThisSample, result);
	  
	  Sample sample = sampleDao.getSampleBySampleId(sampleId); 
	  SampleWrapperWebapp managedSample = new SampleWrapperWebapp(sample);
	  List<SampleMeta> metaFromForm = SampleWrapperWebapp.getValidatedMetaFromRequestAndTemplateToSubtype(request, sample.getSampleSubtype(), result); // gets meta and adds back to managed sampleForm as it is not persisted
	  if(result.hasErrors()){
		  sampleForm.setSampleType(sampleTypeDao.getSampleTypeBySampleTypeId(sampleForm.getSampleTypeId()));
		  sampleForm.setSampleSubtype(sampleSubtypeDao.getSampleSubtypeBySampleSubtypeId(sampleForm.getSampleSubtypeId()));
		  m.put("job", jobForThisSample);
		  m.put("sample", sampleForm); 
		  m.addAttribute("normalizedSampleMeta",SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(sample.getSampleSubtype(), metaFromForm));
		  return "sampleDnaToLibrary/sampledetail_rw";
	  }
	  sample.setName(sampleForm.getName());
	  
	  sampleService.updateExistingSampleViaSampleWrapper(managedSample, metaFromForm);
	  //managedSample.updateMetaToList(metaFromForm, sampleMetaDao);//fixed with above, this line should no longer be neeeded
	  //managedSample.saveAll(sampleService);//fixed with above, this line should no longer be neeeded

	  waspMessage("sampleDetail.updated_success.label");
	  return "redirect:/sampleDnaToLibrary/sampledetail_ro/" + jobId + "/" + sampleId + ".do";

  }

  // **
  //  * See if Sample name has changed between sample objects and if so check if the new name is unique within the job.
  //  * @param formSample
  //  * @param originalSample
  //  * @param job
  ////  * @param result
   // * /
  private void validateSampleNameUnique(String sampleName, Integer sampleId, Job job, BindingResult result){
	  //confirm that, if a new sample.name was supplied on the form, it is different from all other sample.name in this job
	  List<Sample> samplesInThisJob = job.getSample();
	  for(Sample eachSampleInThisJob : samplesInThisJob){
		  if(eachSampleInThisJob.getId().intValue() != sampleId.intValue()){
			  if( sampleName.equals(eachSampleInThisJob.getName()) ){
				  // adding an error to 'result object' linked to the 'name' field as the name chosen already exists
				  Errors errors=new BindException(result.getTarget(), "sample");
				  // reject value on the 'name' field with the message defined in sampleDetail.updated.nameClashError
				  // usage: errors.rejectValue(field, errorString, default errorString)
				  errors.rejectValue("name", "sampleDetail.nameClash.error", "sampleDetail.nameClash.error (no message has been defined for this property)");
				  result.addAllErrors(errors);
				  break;
			  }
		  }
	  }
  }
  
  // /**
  //  * Validates the job and sample id supplied to make sure they are refer to actual persisted model objects and are linked
  //  * @param jobId
  //  * @param sampleId
 //   * @param defaultReturnString
 //   * @return a redirect path on error, otherwise the defaultResturnString
  //  * /
  private String validateJobIdAndSampleId(Integer jobId, Integer sampleId, String defaultReturnString){
	  	if( jobId == null ){
			waspErrorMessage("sampleDetail.jobParameter.error");
			return "redirect:/dashboard.do";
		}
		if (sampleId == null){	
			waspErrorMessage("sampleDetail.sampleParameter.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		
		if(jobDao.getJobByJobId(jobId).getId()==null){//not found in database
			waspErrorMessage("sampleDetail.jobNotFound.error");
			return "redirect:/dashboard.do";
		}
		
		if(sampleDao.getSampleBySampleId(sampleId).getId()==null){
			waspErrorMessage("sampleDetail.sampleNotFound.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		
		//confirm these two objects exist and part of same job
		if(jobSampleDao.getJobSampleByJobIdSampleId(jobId, sampleId).getJobSampleId()== null){
			waspErrorMessage("sampleDetail.jobSampleMismatch.error");
			return "redirect:/sampleDnaToLibrary/listJobSamples/" + jobId + ".do";
		}
		return defaultReturnString;
  }
  
  // **
   // * Upload a file associated with this job
   // * /
  @RequestMapping(value="/uploadJobFile/{jobId}", method=RequestMethod.POST)
	@PreAuthorize("hasRole('jv-' + #jobId) or hasRole('su') or hasRole('ft')")
	public String uploadJobFile(
			@PathVariable("jobId") Integer jobId, 
			@RequestParam("file_description") String fileDescription,
			@RequestParam("file_upload") MultipartFile mpFile) {

	  	String referer = request.getHeader("Referer");
	  	Job job = jobService.getJobByJobId(jobId);
		if (job==null || job.getId() == null){
			waspErrorMessage("listJobSamples.jobNotFound.error");
			return "redirect:/dashboard.do";
		}
		else if(mpFile.isEmpty()){
			waspErrorMessage("listJobSamples.fileUploadFailed_fileEmpty.error");
			return "redirect:"+ referer;
		}
		else if("".equals(fileDescription)){
			waspErrorMessage("listJobSamples.fileUploadFailed_fileDescriptionEmpty.error");
			return "redirect:"+ referer;
		}
		
		Random randomNumberGenerator = new Random(System.currentTimeMillis());
		try{
			fileService.uploadJobFile(mpFile, job, fileDescription, randomNumberGenerator);//will upload and perform all database updates
			//FileGroup group = fileService.processUploadedJobFile(mpFile, job, fileDescription, randomNumberGenerator);
			//fileService.linkFileGroupWithJob(group, job);
		} catch(FileUploadException e){
			logger.warn(e.getMessage());
			waspErrorMessage("listJobSamples.fileUploadFailed.error");
			return "redirect:"+ referer;
		}

		waspMessage("listJobSamples.fileUploadedSuccessfully.label");	
		return "redirect:"+ referer;		
	}
  */
  
  @RequestMapping(value="/mps/jobs/{jobId}/runs", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsMainPageByJob(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
	  
	  Job job = jobService.getJobByJobId(jobId);

	  //really need !!!!:   jobService.getSuccessfullyCompletedRuns(job)	  
	  
	  /***** NOT USED - instead we're going through sampleService.getCellLibrariesForJob(job); 
	  ///THIS IS NOT GOOD, THE RESULT SHOULD BE A SET!!!!!!
	  List<Sample> platformUnitList = jobService.getPlatformUnitWithLibrariesOnForJob(job);	  
	  System.out.println("platformUnits from the one with the List: " + platformUnitList.size());
	  *******/
	  
	  Set<Sample> platformUnitSet = new HashSet<Sample>();
	  Set<Run> runSet = new HashSet<Run>();
	  List<Sample> platformUnitsSuccessfullyRun = new ArrayList<Sample>();
	  //TODO ***********!!!!!!!!!!************really want to use 	  
	  //List<SampleSource> getCellLibrariesThatPassedQCForJobList =  sampleService.getCellLibrariesThatPassedQCForJob(jobService.getJobByJobId(jobId));
	  Set<SampleSource> cellLibrariesForJob =  sampleService.getCellLibrariesForJob(job);
	  Map<Run,Sample> runPlatformUnitMap = new HashMap<Run, Sample>();
	  Map<Sample, Run> platformUnitRunMap = new HashMap<Sample, Run>();
	  Map<Sample, Set<Sample>> platformUnitCellSetMap = new HashMap<Sample, Set<Sample>>();
	  Map<Sample, List<Sample>> platformUnitOrderedCellListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, List<Sample>> cellLibraryListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, List<Sample>> cellControlLibraryListMap = new HashMap<Sample, List<Sample>>();
	  Map<Sample, Integer> cellIndexMap = new HashMap<Sample, Integer>();
	  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample, Adaptor>();
	  Map<Sample, Sample> libraryMacromoleculeMap = new HashMap<Sample, Sample>();
	  Random randomInt = new Random(System.currentTimeMillis());//for FAKE_name_
	  
	  for(SampleSource cellLibrary : cellLibrariesForJob){
		  try{
			  Sample cell = sampleService.getCell(cellLibrary);//cellLibrary.getSample();
			  Sample library = sampleService.getLibrary(cellLibrary);//cellLibrary.getSourceSample();
			  Adaptor adaptor = sampleService.getLibraryAdaptor(library);
			  Sample platformUnit = sampleService.getPlatformUnitForCell(cell);//sampleService.getPlatformUnitForCell(cellLibrary.getSample());
			  List<Run> runList = runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);//WHY IS THIS A LIST????
			  //really need runService.getSuccessfullyCompletedRuns(job)
			  
			  //TODO MUST FIX THIS*************!!!!!!!!!!!!!***************
			  //if(runList.isEmpty()){
				//  continue;
			  //}
			  
			  if(runList.isEmpty()){//really just want to do a CONTINUE HERE, but for test, show somehting
				  List<Run> otherRunList = runService.getRunsForPlatformUnit(platformUnit);//more fake it
				  if(!otherRunList.isEmpty()){
					  platformUnitRunMap.put(platformUnit, otherRunList.get(0));//for testing only
				  }
				  else{
					  Run run = new Run();				  
					  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
					  Date date = new Date();			  
					  run.setName("FAKE_name_"+dateFormat.format(date)+"_"+platformUnit.getName());
					  run.setId(randomInt.nextInt(500) + 500);
					  platformUnitRunMap.put(platformUnit, run);
				  }
			  }
			  else{
				  platformUnitRunMap.put(platformUnit, runList.get(0));//THIS LIST SHOULD ONLY HAVE ONE ENTRY. WHY IS IT A LIST??
			  }
			  
			  
			  platformUnitSet.add(platformUnit);
			  if(platformUnitCellSetMap.containsKey(platformUnit)){
				  platformUnitCellSetMap.get(platformUnit).add(cell);
			  }
			  else{
				  Set<Sample> cellSet = new HashSet<Sample>();
				  cellSet.add(cell);
				  platformUnitCellSetMap.put(platformUnit, cellSet);
			  }
			  
			  if(cellLibraryListMap.containsKey(cell)){
				  cellLibraryListMap.get(cell).add(library);
			  }
			  else{
				  List<Sample> libraryList = new ArrayList<Sample>();
				  libraryList.add(library);
				  cellLibraryListMap.put(cell, libraryList);
			  }
			
			  Sample macromolecule = library.getParent();
			  if(macromolecule != null && macromolecule.getId()!=null){
				  libraryMacromoleculeMap.put(library, macromolecule);
			  }
			  
			  if(adaptor != null && adaptor.getId()!=null){
				  libraryAdaptorMap.put(library, adaptor);
			  }
			  
			  cellIndexMap.put(cell, sampleService.getCellIndex(cell));
			  
			  /*forget the run for now
			  
			  //WHY DOES THIS RETURN A LIST????????? runService.getSuccessfullyCompletedRunsForPlatformUnit(platformUnit);
			  List<Run> runList = runService.getRunsForPlatformUnit(platformUnit);
			  for(Run run : runList){
				  //if( true runService.isRunSuccessfullyCompleted(run)  ){//fake it for now
					  runSet.add(run);
					  runPlatformUnitMap.put(run, platformUnit);
					  Sample cell = sampleService.getCell(cellLibrary);
					  platformUnitCellMap.put(platformUnit, cell);
					  List<Sample> librariessampleService.getLibrariesOnCellWithoutControls(cell);
					  
					  
					  
					  
					  break;//needed because if statement is faking it
					  
				  }
			  }
			   */
			  
		  }catch(Exception e){logger.debug("Unable to properly gather run info: " + e.getMessage());}
	  } 
	  System.out.println("platformUnits from the one with the SET: " + platformUnitSet.size());
	  for(Sample platformUnit : platformUnitSet){
		  
		  Set<Sample> set = platformUnitCellSetMap.get(platformUnit);
		  List<Sample> orderedCellList = new ArrayList<Sample>(set);
		  
		  //ORDER BY INDEX
		  class CellIndexComparator implements Comparator<Sample> {
			    @Override
			    public int compare(Sample cell0, Sample cell1) {
			    	try{
			    		return sampleService.getCellIndex(cell0).compareTo(sampleService.getCellIndex(cell1));
			    	}catch(Exception e){logger.debug("Unable to sort orderedCellList: " + e.getMessage()); return 0;}
			    }
			}
			Collections.sort(orderedCellList, new CellIndexComparator());//sort by cell's index

			platformUnitOrderedCellListMap.put(platformUnit, orderedCellList);	
			
			//while we have a set of cells for each platform unit, use it to retrieve each cell's list of control libraries, if any
			for(Sample cell : orderedCellList){
				List<Sample> controlLibraryList = sampleService.getControlLibrariesOnCell(cell);
				if(!controlLibraryList.isEmpty()){
					cellControlLibraryListMap.put(cell, controlLibraryList);
					for(Sample controlLibrary : controlLibraryList){
						Adaptor adaptor = sampleService.getLibraryAdaptor(controlLibrary);
						if(adaptor != null && adaptor.getId()!=null){
							  libraryAdaptorMap.put(controlLibrary, adaptor);
						}					
					}
				}
			}
	  }
	 
	//MUST GET THE RUN (if any)
	  //MUST get the adaptors/index
	  
	  //check out the data:
	  /*
	    Map<Sample, List<Sample>> platformUnitOrderedCellListMap = new HashMap<Sample, List<Sample>>();
	  	Map<Sample, List<Sample>> cellLibraryListMap = new HashMap<Sample, List<Sample>>();
	  	Map<Sample, Sample> libraryMacromoleculeMap = new HashMap<Sample, Sample>();
	   */
	  for (Sample platformUnit : platformUnitSet){
		  System.out.println("Platform Unit: " + platformUnit.getName());
		  List<Sample> cells = platformUnitOrderedCellListMap.get(platformUnit);
		  for(Sample cell : cells){
			  System.out.println("--Cell: " + cell.getName());
			  List<Sample> libraries = cellLibraryListMap.get(cell);
			  for(Sample library : libraries){
				  System.out.print("----Libraries: " + library.getName());
				  Sample macromolecule = libraryMacromoleculeMap.get(library);
				  if(macromolecule != null && macromolecule.getId()!=null){
					  System.out.println(" (Parent Sample: " + macromolecule.getName() + ")");
				  }
				  else{System.out.println("");}
			  }
		  }
	  }
	  
	  m.addAttribute("job", job);
	  m.addAttribute("platformUnitRunMap", platformUnitRunMap);
	  m.addAttribute("platformUnitSet", platformUnitSet);
	  m.addAttribute("platformUnitOrderedCellListMap", platformUnitOrderedCellListMap);
	  m.addAttribute("cellLibraryListMap", cellLibraryListMap);
	  m.addAttribute("cellIndexMap", cellIndexMap);
	  m.addAttribute("libraryMacromoleculeMap", libraryMacromoleculeMap);
	  m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
	  m.addAttribute("cellControlLibraryListMap", cellControlLibraryListMap);
	  
	  //return "sampleDnaToLibrary/resultsView";
	  return "datadisplay/mps/jobs/runs/mainpage";
  }
/*
  @RequestMapping(value="/showplay", method=RequestMethod.GET)
  //@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public void showPlay(ModelMap m, HttpServletResponse response) throws SampleTypeException {
	  try{
		//FileHandle fileHandle = fileService.getFileHandleById(1);//this is an text file
		  //response.setContentType("text/html");
	  //FileHandle fileHandle = fileService.getFileHandleById(27);//this is an html file
	  //response.setContentType("text/html");
	  FileHandle fileHandle = fileService.getFileHandleById(28);//this is a pdf file
	  response.setContentType("application/pdf");
	  //FileHandle fileHandle = fileService.getFileHandleById(29);//this is a png file
	  //response.setContentType("image/png");
	  
	  fileService.copyFileHandleToOutputStream(fileHandle, response.getOutputStream());
	  //do not flush, it's not needed and it screws things up
	  }catch(Exception e){logger.debug("unable to get file for display");}
  }
  */
  @RequestMapping(value="mps/jobs/{jobId}/jobdetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsJobDetailsByJob(@PathVariable("jobId") Integer jobId, ModelMap m) throws SampleTypeException {
	  
	  Job job = jobService.getJobByJobId(jobId);
	  m.addAttribute("job", job);
	  LinkedHashMap<String, String> extraJobDetailsMap = jobService.getExtraJobDetails(job);
	  m.addAttribute("extraJobDetailsMap", extraJobDetailsMap);	  
	  LinkedHashMap<String,String> jobApprovalsMap = jobService.getJobApprovals(job);
	 
	  m.addAttribute("jobApprovalsMap", jobApprovalsMap);	  
	  //get the jobApprovals Comments (if any)
	  HashMap<String, MetaMessage> jobApprovalsCommentsMap = jobService.getLatestJobApprovalsComments(jobApprovalsMap.keySet(), jobId);
	  m.addAttribute("jobApprovalsCommentsMap", jobApprovalsCommentsMap);	
	  //get the current jobStatus
	  m.addAttribute("jobStatus", jobService.getJobStatus(job));

		List<FileGroup> fileGroups = new ArrayList<FileGroup>();
		Map<FileGroup, List<FileHandle>> fileGroupFileHandlesMap = new HashMap<FileGroup, List<FileHandle>>();
		List<FileHandle> fileHandlesThatCanBeViewedList = new ArrayList<FileHandle>();
		for(JobFile jf: job.getJobFile()){
			FileGroup fileGroup = jf.getFile();//returns a FileGroup
			fileGroups.add(fileGroup);
			List<FileHandle> fileHandles = new ArrayList<FileHandle>();
			for(FileHandle fh : fileGroup.getFileHandles()){
				fileHandles.add(fh);
				String mimeType = fileService.getMimeType(fh.getFileName());
				if(!mimeType.isEmpty()){
					fileHandlesThatCanBeViewedList.add(fh);
				}
			}
			fileGroupFileHandlesMap.put(fileGroup, fileHandles);
		}
		m.addAttribute("fileGroups", fileGroups);
		m.addAttribute("fileGroupFileHandlesMap", fileGroupFileHandlesMap);
		m.addAttribute("fileHandlesThatCanBeViewedList", fileHandlesThatCanBeViewedList);
		
	  return "datadisplay/mps/jobs/jobdetails";
  }
  
  
//  @RequestMapping(value="/runDetails/{runId}", method=RequestMethod.GET)
  //  //@PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
//  public String runDetails(@PathVariable("runId") Integer runId, ModelMap m) throws SampleTypeException {

  @RequestMapping(value="/mps/jobs/{jobId}/runs/{runId}/rundetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsRunDetailsByJob(@PathVariable("jobId") Integer jobId, @PathVariable("runId") Integer runId, ModelMap m) throws SampleTypeException {

	  Run run = runService.getRunById(runId);
	  List<RunMeta> runMetaList = run.getRunMeta();
	  String runReadLength = "???";
	  String runReadType = "???";
	  String runStartDate = "???";
	  String runEndDate = "???";
	  
	  for(RunMeta runMeta : runMetaList){
		  if(runMeta.getK().contains("readLength")){
			  runReadLength = runMeta.getV();
		  }
		  else if (runMeta.getK().contains("readType")){
			  runReadType = runMeta.getV();			  
		  }
	  }	  
	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	  if(run.getStarted()!=null){
		  runStartDate= dateFormat.format(run.getStarted());
	  }
	  if(run.getFinished()!=null){
		  runEndDate= dateFormat.format(run.getFinished());
	  }
	  	  
	  Sample platformUnit = run.getPlatformUnit();
	  List<SampleMeta> platformUnitMetaList = platformUnit.getSampleMeta();
	  String totalLanesOnPlatformUnit = "???";
	  for(SampleMeta platformUnitMeta : platformUnitMetaList){
		  if(platformUnitMeta.getK().contains("lanecount")){
			  totalLanesOnPlatformUnit = platformUnitMeta.getV();
		  }
	  }
	  m.addAttribute("run", run);
	  m.addAttribute("runReadLength", runReadLength);//actual on run
	  m.addAttribute("runReadType", runReadType);//actual on run
	  m.addAttribute("runStartDate", runStartDate);
	  m.addAttribute("runEndDate", runEndDate);
	  
	  m.addAttribute("platformUnit", platformUnit);
	  m.addAttribute("totalLanesOnPlatformUnit", totalLanesOnPlatformUnit);
	  
	  return "datadisplay/mps/jobs/rundetails";
  }
  
  @RequestMapping(value="/mps/jobs/{jobId}/runs/{runId}/cells/{cellId}/celldetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsCellDetailsByJobByRun(@PathVariable("jobId") Integer jobId, @PathVariable("runId") Integer runId, 
		  @PathVariable("cellId") Integer cellId, ModelMap m) throws SampleTypeException {
	  
	  Run run = runService.getRunById(runId);
	  List<RunMeta> runMetaList = run.getRunMeta();
	  String runReadLength = "???";
	  String runReadType = "???";
	  String runStartDate = "???";
	  String runEndDate = "???";
	  
	  for(RunMeta runMeta : runMetaList){
		  if(runMeta.getK().contains("readLength")){
			  runReadLength = runMeta.getV();
		  }
		  else if (runMeta.getK().contains("readType")){
			  runReadType = runMeta.getV();			  
		  }
	  }	  
	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	  if(run.getStarted()!=null){
		  runStartDate= dateFormat.format(run.getStarted());
	  }
	  if(run.getFinished()!=null){
		  runEndDate= dateFormat.format(run.getFinished());
	  }
	  	  
	  Sample platformUnit = run.getPlatformUnit();
	  List<SampleMeta> platformUnitMetaList = platformUnit.getSampleMeta();
	  String totalLanesOnPlatformUnit = "???";
	  for(SampleMeta platformUnitMeta : platformUnitMetaList){
		  if(platformUnitMeta.getK().contains("lanecount")){
			  totalLanesOnPlatformUnit = platformUnitMeta.getV();
		  }
	  }
	  m.addAttribute("run", run);
	  m.addAttribute("runReadLength", runReadLength);//actual on run
	  m.addAttribute("runReadType", runReadType);//actual on run
	  m.addAttribute("runStartDate", runStartDate);
	  m.addAttribute("runEndDate", runEndDate);
	  
	  m.addAttribute("platformUnit", platformUnit);
	  m.addAttribute("totalLanesOnPlatformUnit", totalLanesOnPlatformUnit);
	  
	  Sample cell = sampleService.getSampleById(cellId);
	  try{
		  m.addAttribute("cellIndex", sampleService.getCellIndex(cell));
	  }catch(Exception e){}
	  
	  
	  //TODO !!!!****$$$$****MUST MUST REALLY SHOULD BE: sampleService.getCellLibrariesThatPassedQCForJob(jobService.getJobByJobId(jobId));
	  Set<SampleSource> cellLibrariesForJobSet = sampleService.getCellLibrariesForJob(jobService.getJobByJobId(jobId));
	  List<Sample> librariesThatPassedQCForThisCellList = new ArrayList<Sample>();
	  Map<Sample, Adaptor> libraryAdaptorMap = new HashMap<Sample,Adaptor>();
	  Map<Sample, String> libraryAdaptorSetShortNameMap = new HashMap<Sample,String>();
	  Map<Sample, String> librarypMAddedMap = new HashMap<Sample,String>();
	  Map<Sample, Sample> libraryMacromoleculeMap = new HashMap<Sample,Sample>();
	  Map<Sample, String> libraryOrganismMap = new HashMap<Sample,String>();//if a parent macromoleucle was submitted, species is stored with it's meta; if only a library was submitted, species stored with the library's mete
	  List<Sample> contolLibrariesForThisCell = new ArrayList<Sample>();

	  for(SampleSource ss : cellLibrariesForJobSet){
		  if(sampleService.getCell(ss) == cell){
			  Sample library = sampleService.getLibrary(ss);
			  librariesThatPassedQCForThisCellList.add(library);//this is just a fix to display anything now!
			  Adaptor adaptor = sampleService.getLibraryAdaptor(library);
			  libraryAdaptorMap.put(library, adaptor);
			  libraryAdaptorSetShortNameMap.put(library, adaptor.getAdaptorset().getName().split("\\s+")[0]);
			  List<SampleSourceMeta> sampleSourceMetaList = ss.getSampleSourceMeta();
			  String pMApplied = "???";
			  for(SampleSourceMeta ssm : sampleSourceMetaList){
				  if(ssm.getK().contains("libConcInCellPicoM")){
					  pMApplied = ssm.getV();
				  }
			  }
			  librarypMAddedMap.put(library, pMApplied);
			  Sample parentMacromolecule = library.getParent();
			  if(parentMacromolecule!=null && parentMacromolecule.getId()!=null){
				  libraryMacromoleculeMap.put(library, parentMacromolecule);
				  libraryOrganismMap.put(library, sampleService.getNameOfOrganism(parentMacromolecule, "???"));
			  }
			  else{
				  libraryOrganismMap.put(library, sampleService.getNameOfOrganism(library, "???"));
			  }
			  //will also require (from samplesource) the passfilter reads
		  }
	  }
	  
	  //any controls on this lane?
	  List<Sample> controlLibrariesForThisCellList = sampleService.getControlLibrariesOnCell(cell);
	  if(!controlLibrariesForThisCellList.isEmpty()){
		  for(Sample controlLibrary : controlLibrariesForThisCellList){
			  Adaptor adaptor = sampleService.getLibraryAdaptor(controlLibrary);
			  if(adaptor != null && adaptor.getId()!=null){
				  libraryAdaptorMap.put(controlLibrary, adaptor);
				  libraryAdaptorSetShortNameMap.put(controlLibrary, adaptor.getAdaptorset().getName().split("\\s+")[0]);
			  }
			  SampleSource ss = sampleService.getCellLibrary(cell, controlLibrary);
			  List<SampleSourceMeta> sampleSourceMetaList = ss.getSampleSourceMeta();
			  String pMApplied = "???";
			  for(SampleSourceMeta ssm : sampleSourceMetaList){
				  if(ssm.getK().contains("libConcInCellPicoM")){
					  pMApplied = ssm.getV();
				  }
			  }
			  librarypMAddedMap.put(controlLibrary, pMApplied);
			  libraryOrganismMap.put(controlLibrary, sampleService.getNameOfOrganism(controlLibrary, "???"));
			  //will also require (from samplesource) the passfilter reads
		  }
	  }

	  m.addAttribute("controlLibrariesForThisCellList", controlLibrariesForThisCellList);
	  m.addAttribute("librariesThatPassedQCForThisCellList", librariesThatPassedQCForThisCellList);
	  m.addAttribute("libraryMacromoleculeMap", libraryMacromoleculeMap);
	  m.addAttribute("libraryOrganismMap", libraryOrganismMap);
	  m.addAttribute("libraryAdaptorSetShortNameMap", libraryAdaptorSetShortNameMap);
	  m.addAttribute("libraryAdaptorMap", libraryAdaptorMap);
	  m.addAttribute("librarypMAddedMap", librarypMAddedMap);
	  
	  //m.addAttribute("", );passfilter reads
		 	 	 	 	 	 
	  for(Sample library : librariesThatPassedQCForThisCellList){
		  if(libraryMacromoleculeMap.get(library)!=null){
			  System.out.println("Parent: "+ libraryMacromoleculeMap.get(library).getName());
		  }else {System.out.println("Parent: N/A");}		  
		  System.out.println("Lib: "+ library.getName());
		  System.out.println("Species: "+ libraryOrganismMap.get(library));
		  System.out.println("AdaptorSetShortname: "+ libraryAdaptorSetShortNameMap.get(library));
		  System.out.println("Adaptor Index: "+ libraryAdaptorMap.get(library).getBarcodenumber());
		  System.out.println("AdaptorTag: "+ libraryAdaptorMap.get(library).getBarcodesequence());
		  System.out.println("pM applied: "+ librarypMAddedMap.get(library));
		  
		  System.out.println("");
		  
	  }
	  //should order libraries by adaptor index
	  //must get the controls for this lane
	  return "datadisplay/mps/jobs/celldetails";
  }
  
  @RequestMapping(value="/mps/jobs/{jobId}/libraries/{libraryId}/librarydetails", method=RequestMethod.GET)
  @PreAuthorize("hasRole('su') or hasRole('ft') or hasRole('da-*') or hasRole('jv-' + #jobId)")
  public String mpsLibraryDetailsByJob(@PathVariable("jobId") Integer jobId, @PathVariable("libraryId") Integer libraryId, 
		  ModelMap m) throws SampleTypeException {
	  
	  Job job = jobService.getJobByJobId(jobId);
	  List<Sample> jobSamples = job.getSample();//list of samples in this job; should be submitted macromolecules, submitted libraries, and facility-generated libraries
	  Sample library = sampleService.getSampleById(libraryId);
	  Sample parentMacromolecule = library.getParent();//could be null, if user submitted a user-generated library

	  boolean libraryBelongsToJob = true;
	  if(!jobSamples.contains(library)){//unexpected error
		  libraryBelongsToJob = false;
		  logger.debug("Library with id " + libraryId + " does not appear to belong to job with id " + jobId);
		  System.out.println("Library with id " + libraryId + " does not appear to belong to job with id " + jobId);
		  m.addAttribute("libraryBelongsToJob", libraryBelongsToJob);
		  return "datadisplay/mps/jobs/librarydetails";
	  }

	  m.addAttribute("libraryBelongsToJob", libraryBelongsToJob);
	  m.addAttribute("library", library);
	  m.addAttribute("parentMacromolecule", parentMacromolecule);

	  //metadata for parent macromolecule
	  if(parentMacromolecule!=null){
		  SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(parentMacromolecule);
		  List<SampleMeta> normalizedMacromoleculeMetaList = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(parentMacromolecule.getSampleSubtype(), sampleManaged.getAllSampleMeta());
		  m.addAttribute("normalizedMacromoleculeMeta", normalizedMacromoleculeMetaList);
	  }  
	  
	  //metadata for library
	  SampleWrapperWebapp sampleManaged = new SampleWrapperWebapp(library);	  
	  List<SampleMeta> normalizedLibraryMetaList = SampleWrapperWebapp.templateMetaToSubtypeAndSynchronizeWithMaster(library.getSampleSubtype(), sampleManaged.getAllSampleMeta());
	  m.addAttribute("normalizedLibraryMeta", normalizedLibraryMetaList );
	  
	  //this is needed for the adaptor and adaptorset meta to be interpreted properly during metadata display
	  List<Adaptorset> adaptorsets = new ArrayList<Adaptorset>();
	  List<Adaptor> adaptors = new ArrayList<Adaptor>();
	  Adaptor adaptor;
	  try{ 
		  adaptor = adaptorService.getAdaptor(library);
		  adaptors.add(adaptor);
		  adaptorsets.add(adaptor.getAdaptorset());
	  }catch(Exception e){}
	  m.addAttribute("adaptorsets", adaptorsets);
	  m.addAttribute("adaptors", adaptors);
	  
	  //this is needed for the organism meta to be interpreted properly during metadata display
	  m.addAttribute("organisms", sampleService.getOrganismsPlusOther());

	  return "datadisplay/mps/jobs/librarydetails";
  }

}

