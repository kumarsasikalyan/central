/**
 * 
 * FileServiceImpl.java
 * 
 * @author echeng (table2type.pl)
 * 
 *         the FileService Implmentation
 * 
 * 
 **/

package edu.yu.einstein.wasp.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.persistence.TypedQuery;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import edu.yu.einstein.wasp.Assert;
import edu.yu.einstein.wasp.Hyperlink;
import edu.yu.einstein.wasp.dao.FileGroupDao;
import edu.yu.einstein.wasp.dao.FileHandleDao;
import edu.yu.einstein.wasp.dao.FileTypeDao;
import edu.yu.einstein.wasp.dao.JobDao;
import edu.yu.einstein.wasp.dao.JobDraftFileDao;
import edu.yu.einstein.wasp.dao.JobFileDao;
import edu.yu.einstein.wasp.dao.SampleDao;
import edu.yu.einstein.wasp.exception.FileUploadException;
import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.PluginException;
import edu.yu.einstein.wasp.exception.SampleTypeException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridHostResolver;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.GridResult;
import edu.yu.einstein.wasp.grid.work.GridWorkService;
import edu.yu.einstein.wasp.grid.work.WorkUnit;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.model.FileGroup;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.model.FileType;
import edu.yu.einstein.wasp.model.Job;
import edu.yu.einstein.wasp.model.JobDraft;
import edu.yu.einstein.wasp.model.JobDraftFile;
import edu.yu.einstein.wasp.model.JobFile;
import edu.yu.einstein.wasp.model.Sample;
import edu.yu.einstein.wasp.model.SampleSource;
import edu.yu.einstein.wasp.plugin.FileTypeViewProviding;
import edu.yu.einstein.wasp.plugin.SequencingViewProviding;
import edu.yu.einstein.wasp.plugin.WaspPluginRegistry;
import edu.yu.einstein.wasp.plugin.WebInterfacing;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.service.SampleService;
import edu.yu.einstein.wasp.model.SampleSource;

@Service
@Transactional("entityManager")
public class FileServiceImpl extends WaspServiceImpl implements FileService {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JobDraftFileDao jobDraftFileDao;

	@Autowired
	private SampleService sampleService;
	
	@Autowired
	protected WaspPluginRegistry pluginRegistry;

	@Autowired
	private SampleDao sampleDao;

	@Autowired
	private FileTypeDao fileTypeDao;

	@Autowired
	private FileGroupDao fileGroupDao;

	@Autowired
	private GridHostResolver hostResolver;
	
	@Autowired
	private JobFileDao jobFileDao;
	
	@Autowired
	private JobDao jobDao;

	@Value("${wasp.temporary.dir}")
	protected String tempDir;

	@Value("${wasp.primaryfilehost}")
	protected String fileHost;

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	/**
	 * fileDao;
	 * 
	 */
	private FileHandleDao fileHandleDao;

	/**
	 * setFileDao(FileDao fileDao)
	 * 
	 * @param fileDao
	 * 
	 */
	@Override
	@Autowired
	public void setFileHandleDao(FileHandleDao fileHandleDao) {
		this.fileHandleDao = fileHandleDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileHandleDao getFileHandleDao() {
		return this.fileHandleDao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileHandle getFileHandleById(final int id) {
		return this.getFileHandleDao().getFileHandleById(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FileGroup getFileGroupById(final int id) {
		return fileGroupDao.getFileGroupById(id);
	}

	/**
	 * Upload submitted file to a temporary location on the remote host.
	 * {@inheritDoc}
	 */
	@Override
	public FileGroup processUploadedFile(MultipartFile mpFile, JobDraft jobDraft, String description) {
		String noSpacesFileName = mpFile.getOriginalFilename().replaceAll("\\s+", "_");

		File temporaryDirectory = new File(tempDir);

		if (!temporaryDirectory.exists()) {
			try {
				temporaryDirectory.mkdir();
			} catch (Exception e) {
				throw new FileUploadException("FileHandle upload failure trying to create '" + tempDir + "': " + e.getMessage());
			}
		}
		
		File localFile;
		try {
			localFile = File.createTempFile("wasp.", ".tmp", temporaryDirectory);
		} catch (IOException e) {
			String mess = "Unable to create local temporary file: " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}


		FileGroup retGroup = new FileGroup();
		FileHandle file = new FileHandle();
		file = fileHandleDao.save(file);
		retGroup.addFileHandle(file);
		retGroup = fileGroupDao.save(retGroup);

		if (fileHost == null) {
			String mess = "Primary file host has not been configured!  Please set \"wasp.primaryfilehost\" in wasp-config.";
			logger.warn(mess);
			throw new FileUploadException(mess);
		}

		GridWorkService gws;
		GridFileService gfs;
		try {
			gws = hostResolver.getGridWorkService(fileHost);
			gfs = gws.getGridFileService();
		} catch (GridUnresolvableHostException e) {
			String mess = "Unable to resolve remote host ";
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}

		String draftDir = gws.getTransportConnection().getConfiguredSetting("draft.dir");

		if (draftDir == null) {
			String mess = "Attempted to configure for file copy to " + fileHost + ", but hostname.settings.draft.dir has not been set.";
			logger.warn(mess);
			throw new FileUploadException(mess);
		}

		String remoteDir = draftDir + "/" + jobDraft.getId() + "/";
		String remoteFile;
		try {
			gfs.mkdir(remoteDir);
			remoteFile = remoteDir + "/" + noSpacesFileName;

			if (gfs.exists(remoteFile)) {
				noSpacesFileName = retGroup.getId() + "__" + noSpacesFileName;
				remoteFile = remoteDir + "/" + noSpacesFileName;
			}
		} catch (IOException e) {
			String mess = "problem creating resources on remote host " + gws.getTransportConnection().getHostName();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		}

		file.setFileName(noSpacesFileName);

		try {
			OutputStream tmpFile = new FileOutputStream(localFile);

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = mpFile.getInputStream().read(bytes)) != -1) {
				tmpFile.write(bytes, 0, read);
			}

			mpFile.getInputStream().close();
			tmpFile.flush();
			tmpFile.close();
		} catch (IOException e) {
			String mess = "Unable to generate local temporary file with contents of multipart file";
			logger.warn(mess);
			e.printStackTrace();
			localFile.delete();
			throw new FileUploadException(mess);
		}

		retGroup.setDescription(description);

		// TODO: Determine file type and set on the group.
		// probably not.  Figure out where to put or whether to
		// automatically determine mime type.

		file.setFileURI(gfs.remoteFileRepresentationToLocalURI(remoteFile));

		try {
			gfs.put(localFile, remoteFile);
			register(retGroup);
		} catch (GridException e) {
			String mess = "Problem accessing remote resources " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		} catch (IOException e) {
			String mess = "Problem putting remote file " + e.getLocalizedMessage();
			logger.warn(mess);
			e.printStackTrace();
			throw new FileUploadException(mess);
		} finally {
			localFile.delete();
		}

		fileHandleDao.save(file);
		fileGroupDao.save(retGroup);
		
		return retGroup;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JobDraftFile linkFileGroupWithJobDraft(FileGroup filegroup, JobDraft jobDraft) {
		JobDraftFile jobDraftFile = new JobDraftFile();
		jobDraftFile.setFileGroup(filegroup);
		jobDraftFile.setJobDraft(jobDraft);
		return jobDraftFileDao.save(jobDraftFile);
	}

	@Override
	public Set<FileType> getFileTypes() {
		return fileTypeDao.getFileTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<FileGroup> getFilesByType(FileType fileType) {
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		Map<String, Integer> m = new HashMap<String, Integer>();
		m.put("fileTypeId", fileType.getId());
		Set<FileGroup> result = new HashSet<FileGroup>();
		result.addAll(fileGroupDao.findByMap(m));
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForLibrary(Sample library) throws SampleTypeException {
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Sample lib = sampleService.getSampleById(library.getId());
		return lib.getFileGroups();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForLibraryByType(Sample library, FileType fileType) throws SampleTypeException {
		Assert.assertParameterNotNull(library, "must provide a library");
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		Map<FileType, Set<FileGroup>> filesByType = getFilesForLibraryMappedToFileType(library);
		if (!filesByType.containsKey(fileType))
			return new HashSet<FileGroup>();
		return filesByType.get(fileType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Map<FileType, Set<FileGroup>> getFilesForLibraryMappedToFileType(Sample library) throws SampleTypeException {
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Sample s = sampleDao.findById(library.getId());
		Map<FileType, Set<FileGroup>> filesByType = new HashMap<FileType, Set<FileGroup>>();
		for (FileGroup fg : s.getFileGroups()) {
			FileType ft = fg.getFileType();
			if (!filesByType.containsKey(ft))
				filesByType.put(ft, new HashSet<FileGroup>());
			filesByType.get(ft).add(fg);
		}
		return filesByType;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForCellLibraryByType(Sample cell, Sample library, FileType fileType) throws SampleTypeException {
		Assert.assertParameterNotNull(cell, "must provide a cell");
		if (!sampleService.isCell(cell))
			throw new SampleTypeException("sample is not of type cell");
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		
		Map<FileType, Set<FileGroup>> filesByType = getFilesForCellLibraryMappedToFileType(cell, library);
		if (!filesByType.containsKey(fileType))
			return new HashSet<FileGroup>();
		return filesByType.get(fileType);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Map<FileType, Set<FileGroup>> getFilesForCellLibraryMappedToFileType(Sample cell, Sample library) throws SampleTypeException {
		Assert.assertParameterNotNull(cell, "must provide a cell");
		if (!sampleService.isCell(cell))
			throw new SampleTypeException("sample is not of type cell");
		Assert.assertParameterNotNull(library, "must provide a library");
		if (!sampleService.isLibrary(library))
			throw new SampleTypeException("sample is not of type library");
		
		SampleSource ss = sampleService.getCellLibrary(cell, library);
		Map<FileType, Set<FileGroup>> filesByType = new HashMap<FileType, Set<FileGroup>>();
		for (FileGroup fg : ss.getFileGroups()) {
			FileType ft = fg.getFileType();
			if (!filesByType.containsKey(ft))
				filesByType.put(ft, new HashSet<FileGroup>());
			filesByType.get(ft).add(fg);
		}
		return filesByType;
	}

	
	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForPlatformUnit(Sample platformUnit) throws SampleTypeException {
		Assert.assertParameterNotNull(platformUnit, "must provide a platformUnit");
		if (!sampleService.isPlatformUnit(platformUnit))
			throw new SampleTypeException("sample is not of type platformUnit");
		Sample lib = sampleService.getSampleById(platformUnit.getId());
		return lib.getFileGroups();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Set<FileGroup> getFilesForPlatformUnitByType(Sample platformUnit, FileType fileType) throws SampleTypeException {
		Assert.assertParameterNotNull(platformUnit, "must provide a platformUnit");
		Assert.assertParameterNotNull(fileType, "must provide a fileType");
		Assert.assertParameterNotNull(fileType.getId(), "fileType has no valid fileTypeId");
		Map<FileType, Set<FileGroup>> filesByType = getFilesForPlatformUnitMappedToFileType(platformUnit);
		if (!filesByType.containsKey(fileType))
			return new HashSet<FileGroup>();
		return filesByType.get(fileType);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws SampleTypeException
	 */
	@Override
	public Map<FileType, Set<FileGroup>> getFilesForPlatformUnitMappedToFileType(Sample platformUnit) throws SampleTypeException {
		Assert.assertParameterNotNull(platformUnit, "must provide a platformUnit");
		if (!sampleService.isPlatformUnit(platformUnit))
			throw new SampleTypeException("sample is not of type platformUnit");
		Sample s = sampleDao.findById(platformUnit.getId());
		Map<FileType, Set<FileGroup>> filesByType = new HashMap<FileType, Set<FileGroup>>();
		for (FileGroup fg : s.getFileGroups()) {
			FileType ft = fg.getFileType();
			if (!filesByType.containsKey(ft))
				filesByType.put(ft, new HashSet<FileGroup>());
			filesByType.get(ft).add(fg);
		}
		return filesByType;
	}

	@Override
	public void addFile(FileHandle file) {
		fileHandleDao.save(file);
	}

	@Override
	public void addFileGroup(FileGroup group) {
		fileGroupDao.save(group);
	}

	@Override
	public void setSampleFile(FileGroup group, Sample sample) {
		Sample s = sampleService.getSampleById(sample.getId());
		s.getFileGroups().add(group);
		sampleDao.save(s);
	}

	@Override
	public FileType getFileType(String iname) {
		return fileTypeDao.getFileTypeByIName(iname);
	}

	@Override
	public FileType getFileType(Integer id) {
		return fileTypeDao.getById(id);
	}

	/**
	 * Take a new (isActive == 0) entity managed file and register it.
	 * 
	 * @throws FileNotFoundException
	 * @throws GridUnresolvableHostException
	 */
	@Override
	@Deprecated
	public void register(FileHandle file) throws FileNotFoundException, GridException {
		file = fileHandleDao.merge(file);

		validateFile(file);

		logger.debug("attempting to register " + file.getFileURI().toString());
		setMD5(file);
		fileHandleDao.save(file);
	}

	private enum Md5 { YES, NO };
	
	@Override
	public void register(FileGroup group) throws FileNotFoundException, GridException {
		register(group, Md5.YES);
	}
	
	@Override
	public void registerWithoutMD5(FileGroup group) throws FileNotFoundException, GridException {
		register(group, Md5.NO);
	}
	
	public void register(FileGroup group, Md5 md5) throws FileNotFoundException, GridException {
		group = fileGroupDao.merge(group);
		for (FileHandle f : group.getFileHandles()) {
			validateFile(f);
		}
		logger.debug("attempting to register FileGroup: " + group.getId());

		if (md5.equals(Md5.YES))
			setMD5(group);

		group.setIsActive(1);
		group.setIsArchived(0);
		fileGroupDao.save(group);
	}

	private void validateFile(FileHandle file) throws FileNotFoundException {

		URI uri = file.getFileURI();
		if (uri == null)
			throw new FileNotFoundException("FileHandle URI was null");

		// TODO: implement grid resolution of URNs
		if (!uri.getScheme().equals("file")) {
			String message = "unable to locate " + uri.toString() + ", unimplemented scheme: " + uri.getScheme();
			logger.warn(message);
			throw new FileNotFoundException(message);
		}

	}

	private void setMD5(FileGroup fileGroup) throws GridException, FileNotFoundException {

		if (fileGroup.getFileHandles().isEmpty())
			throw new FileNotFoundException("No file handles in file group: " + fileGroup.getId());

		String host = fileGroup.getFileHandles().iterator().next().getFileURI().getHost();

		// use task array to submit in one batch
		WorkUnit w = new WorkUnit();
		w.setRegistering(true);
		w.setSecureResults(false);
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.setMode(ExecutionMode.TASK_ARRAY);

		int numFiles = 0;

		Set<FileHandle> fileH = new LinkedHashSet<FileHandle>();
		fileH.addAll(fileGroup.getFileHandles());

		for (FileHandle f : fileH) {
			String fileHost = f.getFileURI().getHost().toString();
			if (!fileHost.equals(host))
				throw new GridAccessException("files must all reside on the same host for calculating MD5 by file group");

			numFiles++;
			w.addRequiredFile(f);

			w.setCommand("md5sum ${WASPFILE[WASP_TASK_ID]} | awk '{print $1}' > $WASP_TASK_OUTPUT");

			try {
				// TODO: URN resolution
				URL url = f.getFileURI().toURL();
			} catch (MalformedURLException e) {
				String message = "malformed url " + f.getFileURI().toString();
				logger.warn(message);
				throw new FileNotFoundException(message);
			}
			logger.debug("added " + f.getFileURI() + " to get MD5");
		}

		w.setNumberOfTasks(numFiles);

		GridWorkService gws = hostResolver.getGridWorkService(host);
		GridResult r = gws.execute(w);

		logger.debug("waiting for file registration");
		ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		while (!gws.isFinished(r)) {
			ScheduledFuture<?> md5t = ex.schedule(new Runnable() {
				@Override
				public void run() {
				}
			}, 1, TimeUnit.SECONDS);
			while (!md5t.isDone()) {
				// not done
			}
		}
		ex.shutdownNow();
		logger.debug("registered, getting results");
		
		int numFile = 0;
		
		try {
			Map<String, String> output = gws.getMappedTaskOutput(r);
			Iterator<String> keys = output.keySet().iterator();
			for (FileHandle f : fileH) {

				String key = keys.next();
				logger.debug("task data: " + key + " : " + numFile);
				f.setMd5hash(StringUtils.chomp(output.get(key)));
				fileHandleDao.save(f);
				logger.debug("file registered with MD5: " + f.getMd5hash());
				numFile++;
			}
		} catch (IOException e) {
			throw new GridException("unable to read output of task array", e);
		}
		
	}

	/**
	 * 
	 * @param file
	 * @throws GridException
	 * @throws FileNotFoundException
	 */
	@Deprecated
	private MD5Result setMD5(FileHandle file) throws GridException, FileNotFoundException {
		URL url;
		logger.debug("calculating MD5 for: " + file.getFileURI());
		try {
			url = file.getFileURI().toURL();
		} catch (MalformedURLException e) {
			String message = "malformed url " + file.getFileURI().toString();
			logger.warn(message);
			throw new FileNotFoundException(message);
		}

		GridWorkService gws = hostResolver.getGridWorkService(url.getHost());

		String path = url.getPath();

		try {
			if (!gws.getGridFileService().exists(path)) {
				String message = "unable to locate" + url.toString() + ", file does not exist";
				logger.warn(message);
				throw new FileNotFoundException(message);
			}
		} catch (IOException e) {
			String message = e.getLocalizedMessage();
			logger.error(message);
			throw new GridAccessException(message);
		}

		logger.debug("checking MD5 of " + file.getFileURI().toString());

		WorkUnit w = new WorkUnit();
		w.setRegistering(true);
		w.setResultsDirectory(WorkUnit.SCRATCH_DIR_PLACEHOLDER);
		w.addRequiredFile(file);
		w.setCommand("md5sum ${WASPFILE[0]} | awk '{print $1}'");
		GridResult r = gws.execute(w);
		logger.debug("returning MD5 result");
		return new MD5Result(file, gws, r);

	}

	@Deprecated
	private class MD5Result {

		FileHandle file;
		GridWorkService gws;
		GridResult r;

		public MD5Result(FileHandle file, GridWorkService gws, GridResult r) {
			this.file = file;
			this.gws = gws;
			this.r = r;
		}

		public boolean isRegistered() throws GridException {
			boolean running = gws.isFinished(r);
			if (running)
				return false;
			try {
				InputStream is = gws.readResultStdOut(r);
				StringWriter sw = new StringWriter();
				IOUtils.copy(is, sw);
				String md5 = sw.toString();
				file.setMd5hash(StringUtils.chomp(md5));
				fileHandleDao.save(file);
				logger.debug("file registered with MD5: " + file.getMd5hash());
			} catch (IOException e) {
				String message = "Unable to obtain stdout: " + e.getLocalizedMessage();
				logger.warn(message);
				throw new GridExecutionException(message);
			}
			return true;
		}

	}

	@Override
	public FileGroup promoteJobDraftFileGroupToJob(Job job, FileGroup filegroup) throws GridUnresolvableHostException, IOException {
		for (FileHandle fh : filegroup.getFileHandles()) {
			URI uri = fh.getFileURI();
			String host = uri.getHost();

			if (!host.equals(fileHost)) {
				String mess = "Job Draft File is located on " + host + ", not the primary file host " + fileHost;
				logger.warn(mess);
				throw new GridUnresolvableHostException(mess);
			}

			GridWorkService gws = hostResolver.getGridWorkService(host);
			GridFileService gfs = hostResolver.getGridWorkService(uri.getHost()).getGridFileService();

			String resultsDir = gws.getTransportConnection().getConfiguredSetting("results.dir");

			String path = uri.getPath();

			String basename = path.substring(path.lastIndexOf('/') + 1);

			String resultPath = resultsDir + "/" + job.getId() + "/submitted/";

			gfs.mkdir(resultPath);
			String resultFile = resultPath + basename;
			gfs.copy(path, resultFile);

			URI newUri = gfs.remoteFileRepresentationToLocalURI(resultFile);

			fh.setFileURI(newUri);
			fileHandleDao.save(fh);
		}

		JobFile jf = new JobFile();
		jf.setFileGroup(filegroup);
		jf.setIsActive(1);
		jobFileDao.save(jf);
		
		job.getJobFile().add(jf);
		jobDao.save(job);
		
		return fileGroupDao.findById(filegroup.getId());
	}

	@Override
	public FileHandle getFileHandle(UUID uuid) throws FileNotFoundException {
		TypedQuery<FileHandle> fhq = fileHandleDao.getEntityManager()
				.createQuery("select f from FileHandle f where f.uuid = :uuid", FileHandle.class)
				.setParameter("uuid", uuid);
		FileHandle fh = fhq.getSingleResult();
		if (fh == null)
			throw new FileNotFoundException("File represented by " + uuid.toString() + " was not found.");
		return fh;
	}
	
	@Override
	public Map<String, Hyperlink> getFileDetailsByFileType(FileGroup filegroup) {
		FileType ft = filegroup.getFileType();
		String area = ft.getIName();
		List<FileTypeViewProviding> plugins = pluginRegistry.getPluginsHandlingArea(area, FileTypeViewProviding.class);
		// we expect one (and ONLY one) plugin to handle the area otherwise we do not know which one to show so programming defensively:
		if (plugins.size() == 0)
			throw new PluginException("No plugins found for area=" + area + " with class=SequencingViewProviding");
		if (plugins.size() > 1)
			throw new PluginException("More than one plugin found for area=" + area + " with class=SequencingViewProviding");
		return plugins.get(0).getFileDetails(filegroup.getId());
	}

	@Override
	public Set<FileGroup> getFilesForCellLibraryByType(SampleSource cellLibrary, FileType fileType) {
		TypedQuery<FileGroup> fgq = fileGroupDao.getEntityManager()
				.createQuery("SELECT DISTINCT fg from FileGroup fg " +
						"JOIN fg.sampleSources cl " +
						"WHERE cl = :cellLibrary AND cl = fg.fileType = :fileType", FileGroup.class)
				.setParameter("cellLibrary", cellLibrary)
				.setParameter("fileType", fileType);
		HashSet<FileGroup> result = new HashSet<FileGroup>();
		result.addAll(fgq.getResultList());
		return result;
	}

	@Override
	public Set<FileGroup> getFilesForCellLibrary(SampleSource cellLibrary) {
		TypedQuery<FileGroup> fgq = fileGroupDao.getEntityManager()
				.createQuery("SELECT DISTINCT fg from FileGroup fg " +
						"JOIN fg.sampleSources cl " +
						"WHERE cl = :cellLibrary", FileGroup.class)
				.setParameter("cellLibrary", cellLibrary);
		HashSet<FileGroup> result = new HashSet<FileGroup>();
		result.addAll(fgq.getResultList());
		return result;
	}

}
