/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.io.IOUtils;
import org.codehaus.plexus.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.yu.einstein.wasp.exception.GridException;
import edu.yu.einstein.wasp.exception.NullResourceException;
import edu.yu.einstein.wasp.exception.WaspRuntimeException;
import edu.yu.einstein.wasp.grid.GridAccessException;
import edu.yu.einstein.wasp.grid.GridExecutionException;
import edu.yu.einstein.wasp.grid.GridUnresolvableHostException;
import edu.yu.einstein.wasp.grid.MisconfiguredWorkUnitException;
import edu.yu.einstein.wasp.grid.file.GridFileService;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ExecutionMode;
import edu.yu.einstein.wasp.grid.work.WorkUnit.ProcessMode;
import edu.yu.einstein.wasp.model.FileHandle;
import edu.yu.einstein.wasp.service.FileService;
import edu.yu.einstein.wasp.util.PropertyHelper;

/**
 * {@link GridWorkService} implementation for Sun Grid Engine.  Tested with Grid Engine v6.1.
 * 
 * @author calder
 * 
 */
@Transactional("entityManager")
public class SgeWorkService implements GridWorkService, ApplicationContextAware {
    
    @Value("${wasp.developermode:false}")
    protected boolean developerMode;
	
	/**
	 * LOCAL temporary directory
	 */
	@Value("${wasp.temporary.dir:/tmp}")
	protected String localTempDir;
	
	@Value("${wasp.nfs.timeout:0}")
	protected Integer nfsTimeout;
	
	protected ApplicationContext applicationContext;
	
	protected FileService fileService;
	
	/**
	 * This method gets around a circular reference:
	 * 
	 * HostResolver-+
	 *     |        |
	 * WorkService  |
	 *     |        |
	 * FileService -+
	 * @return the getFileService()
	 */
	public FileService getFileService() {
		if (fileService == null) 
			setFileService(applicationContext.getBean(FileService.class));
		return fileService;
	}
	
	public void setFileService(FileService fileService) {
		logger.debug("setting file service");
	    this.fileService = fileService;
	}
	
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	protected String jobNamePrefix = "WASP-";
	
	public void setJobNamePrefix(String np) {
		this.jobNamePrefix = np + "-";
	}

	protected GridTransportConnection transportConnection;

	protected DirectoryPlaceholderRewriter directoryPlaceholderRewriter = new DefaultDirectoryPlaceholderRewriter();
	
	public SgeWorkService(GridTransportConnection transportConnection) {
		this.transportConnection = transportConnection;
		logger.debug("configured transport service: " + transportConnection.getUserName() + "@" + transportConnection.getHostName());
	}
	
	protected GridFileService gridFileService;
	
	protected String name;
	
	protected Set<String> parallelEnvironments = new HashSet<>();
	
	protected String defaultParallelEnvironment;
	
	protected String defaultMpiParallelEnvironment;
	
	protected String queue;
	protected String maxRunTime;
	protected String account;
	protected String project;
	protected String mailRecipient;
	protected String mailCircumstances;
	
	protected boolean isNumProcConsumable = false;
	
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getQueue() {
		return queue;
	}

	public void setMaxRunTime(String maxRunTime) {
		this.maxRunTime = maxRunTime;
	}
	public String getMaxRunTime() {
		return maxRunTime;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
	
	/**
	 * Set the project.  If the project is not set, it can be set on the work unit, in which case it will override
	 * the WorkServices setting and fail if the project is not available.
	 * @param project name
	 */
	public void setProject(String project) {
		this.project = project;
	}
	public String getProject() {
		return project;
	}
	public void setMailRecipient(String mailRecipient) {
		this.mailRecipient = mailRecipient;
	}
	public String getMailRecipient() {
		return mailRecipient;
	}
	public void setMailCircumstances(String mailCircumstances) {
		this.mailCircumstances = mailCircumstances;
	}
	public String getMailCircumstances() {
		return mailCircumstances;
	}

	/**
	 * Sun Grid Engine implementation of {@link GridWorkService}.  This method wraps a {@link WorkUnit} in a SGE 
	 * submission and executes using the associated {@link GridTransportService}.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public GridResult execute(WorkUnit w) throws GridException {
		logger.debug("executing WorkUnit: " + w.getId());
		try {
			w.prepare();
			
			// This step needs to take place after the workunit's id has been set and before submission.
			// if the results directory is set to the default and there is no runId set
			// (in jobParameters if object was created from the GridHostResolver lookup method 
			// "createWorkUnit") it will throw an exception. 
			directoryPlaceholderRewriter.replaceDirectoryPlaceholders(transportConnection, w);
			
			if (w.getWorkingDirectory().equals(null) || w.getWorkingDirectory().equals("/")) {
				throw new MisconfiguredWorkUnitException("Must configure working directory.");
			}
			if (w.getResultsDirectory().equals(null) || w.getResultsDirectory().equals("/")) {
				throw new MisconfiguredWorkUnitException("Must configure results directory.");
			}
			w.remoteWorkingDirectory = w.getWorkingDirectory();
			if (!w.isWorkingDirectoryRelativeToRoot())
				w.remoteWorkingDirectory = transportConnection.prefixRemoteFile(w.getWorkingDirectory());
			w.remoteResultsDirectory = transportConnection.prefixRemoteFile(w.getResultsDirectory());
			//end
			
			return startJob(w);
		} catch (MisconfiguredWorkUnitException e) {
			throw new GridAccessException("Misconfigured work unit", e);
		}
	}
	
	/**
	 * @param g
	 * @param jobname
	 * @return
	 * @throws GridException
	 * @throws SAXException
	 * @throws IOException
	 */
	protected Document getQstat(GridResult g, String jobname) throws GridException, SAXException, IOException {
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(g.getWorkingDirectory());
		w.setCommand("qstat -xml -j " + jobname + " 2>&1 | sed 's/<\\([/]\\)*>/<\\1a>/g' | sed 's/  xmlns.*>/>/g' | sed '/<messages>/,/<\\/messages>/d'");
		GridResult result;
		try {
			result = transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridException(e.getLocalizedMessage(), e);
		}
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder;
		try {
			dbFactory.setValidating(false);
			docBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to generate xml parser");
		}
		InputStream is = result.getStdOutStream();
		Document doc = docBuilder.parse(new InputSource(is));
		is.close();
		return doc;
	}
	
	protected boolean isUnknown(Document stdout) {
		stdout.getDocumentElement().normalize();
		return stdout.getDocumentElement().getNodeName().equals("unknown_jobs");
	}
	
	protected boolean isInError(Document stdout) {
		NodeList jatstatus = stdout.getElementsByTagName("JAT_status");
		String jobname = stdout.getElementsByTagName("JB_job_name").item(0).getTextContent(); // .getFirstChild().getNodeValue()??
		boolean retval = false;
		for (int n = 0; n < jatstatus.getLength(); n++) {
			String status = jatstatus.item(n).getTextContent();
			logger.trace(jobname + " task " + n + " status is " + status);
			int bits = new Integer(status).intValue();
			if ((bits & SgeSubmissionScript.ERROR) == SgeSubmissionScript.ERROR) {
				retval = true;
			}
		}
		logger.debug("job is in error: " + retval);
		return retval;
	}
	
	protected boolean didDie(GridResult g, String jobname, boolean started, boolean ended) throws GridException, SAXException {
		
		boolean died = false;
		
		
		try {
			Document stdout =  getQstat(g, jobname);
			
			boolean unknown = isUnknown(stdout);
			
			if (!unknown) {
				died = isInError(stdout);
			} else { 
				logger.debug(jobname + " is unknown");
				if (!ended && started) {
					died = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to parse qstat: problem with IO");
		}
		
		return died;
		
	}
	
	private boolean isJobOrTaskArrayEnded(GridResult g) throws GridException{
		if (!g.getMode().equals(ExecutionMode.TASK_ARRAY)) 
			return isJobEnded(g);
		else
			return isTaskArrayEnded(g);
	}

	/**
	 * {@inheritDoc}
	 * @throws GridUnresolvableHostException 
	 */
	@Override
	public boolean isFinished(GridResult g) throws GridException {

		String jobname = this.jobNamePrefix + g.getUuid().toString();
		
		logger.debug("testing for completion of " + jobname);
		
		boolean died = false;
		boolean started = isJobStarted(g);
		boolean ended = isJobOrTaskArrayEnded(g);
		logger.debug("Job status semaphores (started, ended): " + started + ", " + ended);
		if (started && !ended){
			final int INCREMENT_FACTOR = 2;
			final int NEVER_TIME_OUT = -1;
			final int ZERO_TIME_OUT = 0;
			Integer waitMs = 1000;
			int totalWaitedMs = 0;
			boolean timedOut = false;
			do {
				try {
					died = didDie(g, jobname, started, ended);
				} catch (SAXException e) {
					// TODO: improve this logic
					logger.warn("caught SAXException, skipping as if job has not died");
					e.printStackTrace();
					died = false;
				}
				if (died) { 
					// maybe end file not there yet due to nfs problem so hasn't actually died. 
					if (nfsTimeout == ZERO_TIME_OUT){
						logger.debug("job unknown and 'end' semaphore missing. Assuming job failed.");
						timedOut = true;
					}
					else {
						logger.debug("job unknown and 'end' semaphore missing. Waiting " + waitMs + " ms then checking status again");
						try {
							Thread.sleep(waitMs); // wait a bit and see if file appears on nfs
						} catch (InterruptedException e) {
							logger.warn(e.getLocalizedMessage());
						}
						if ( totalWaitedMs >= nfsTimeout && nfsTimeout != NEVER_TIME_OUT){
							logger.debug("job unknown and 'end' semaphore missing. Wait timeout of " + nfsTimeout + " ms exceeded so job failure assumed");
							timedOut = true;
						}
						totalWaitedMs += waitMs;
						waitMs *= INCREMENT_FACTOR;
						if (totalWaitedMs + waitMs > nfsTimeout && nfsTimeout != NEVER_TIME_OUT)
							waitMs = nfsTimeout - totalWaitedMs;
						ended = isJobOrTaskArrayEnded(g);
					}
				}
			} while (died && !timedOut);
		}
		
		logger.debug("Job Status (ended, died): " + ended + ", " + died);
		if (died) {
			cleanUpAbnormallyTerminatedJob(g);
			g.setArchivedResultOutputPath(getFailedArchiveName(g));
			throw new GridExecutionException("abnormally terminated job");
		}
		if (ended) {
			logger.debug("packaging " + jobname);
			cleanUpCompletedJob(g);
			g.setArchivedResultOutputPath(getCompletedArchiveName(g));
		}
		
		return ended;
	}
	
	protected boolean isTaskArrayEnded(GridResult g) throws GridException {
		logger.debug("checking for task array completion: " + g.getUuid().toString());
		WorkUnit w = new WorkUnit();
		w.setCommand("shopt -s nullglob");
		w.addCommand("NOF=(" + g.getId() + ":*.end)");
		w.addCommand("echo ${#NOF[@]}");
		w.addCommand("shopt -u nullglob");
		w.setWorkingDirectory(g.getWorkingDirectory());
		GridResult r;
		try {
			r = transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.error("workunit is not up to muster");
			e.printStackTrace();
			throw new GridAccessException("bad work unit", e);
		}
		InputStream is = r.getStdOutStream();
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(is, writer);
		} catch (IOException e) {
			logger.error("unable to parse task array test");
			e.printStackTrace();
			throw new GridAccessException("unable to determine status of task array", e);
		}
		Integer numEndFiles = 0;
		try{
			numEndFiles = Integer.parseInt(StringUtils.chomp(writer.toString()));
		} catch (NumberFormatException e){
			throw new GridException("Unable to convert '" + writer.toString() + "' to Integer trying to set numEndFiles");
		}
		int numTasks = g.getNumberOfTasks();
		logger.debug("number of tasks=" + numTasks + " and number of '.end' files=" + numEndFiles);
		if (numEndFiles.equals(numTasks))
			return true;
		return false;
	}

	protected boolean isJobExists(WorkUnit w) throws GridAccessException, GridUnresolvableHostException {
		return isJobExists(w.getWorkingDirectory(), w.getId());
	}

	protected boolean isJobExists(GridResult g) throws GridAccessException {
		return isJobExists(g.getWorkingDirectory(), g.getUuid().toString());
	}

	protected boolean isJobExists(String workingDirectory, String id) throws GridAccessException {
		return testSgeFileExists(workingDirectory, id, "sh");
	}

	protected boolean isJobEnded(GridResult g) throws GridAccessException {
		return testSgeFileExists(g.getWorkingDirectory(), g.getUuid().toString(), "end");
	}

	protected boolean isJobStarted(GridResult g) throws GridAccessException {
		return testSgeFileExists(g.getWorkingDirectory(), g.getUuid().toString(), "start");
	}
	
	protected String getCompletedArchiveName(GridResult g) {
		return g.getWorkingDirectory() + jobNamePrefix + g.getUuid().toString() + ".tar.gz";
	}
	
	protected String getFailedArchiveName(GridResult g) {
		return g.getWorkingDirectory() + jobNamePrefix + g.getUuid().toString() + "-FAILED.tar.gz";
	}

	protected boolean testSgeFileExists(String workingDirectory, String id, String suffix) throws GridAccessException {
		try {
			String root = workingDirectory + jobNamePrefix + id ;
			if (suffix == "sh") {
				boolean completed = gridFileService.exists(root + ".tar.gz");
				boolean failed = gridFileService.exists(root + "-FAILED.tar.gz");
				if (completed || failed) return true;
			}
			String remote = root + "." + suffix;
			boolean exists = gridFileService.exists(remote);
			logger.debug("testing exists: " + transportConnection.getHostName() + ":" + remote + " = " + exists);
			return exists;
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("Unable to connect to remote host: ", e);
		}
	}
	
	protected void cleanUpCompletedJob(GridResult g) throws GridException {
		cleanUpCompletedJob(g.getHostname(), g.getWorkingDirectory(), g.getResultsDirectory(), g.getUuid().toString(), !g.isSecureResults());
		if (g.isSecureResults())
			try {
				secureResultsFiles(g);
			} catch (FileNotFoundException e) {
				logger.warn("File not found: " + e.toString());
				e.printStackTrace();
				throw new GridException("File not found error", e);
			}
	}

	protected void cleanUpCompletedJob(String hostname, String workingDirectory, String resultsDirectory, String id, boolean markUnfinished)
			throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.debug("Cleaning successful job " + id + " at " + transportConnection.getHostName() + ":" + workingDirectory);
		
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(transportConnection.prefixRemoteFile(workingDirectory));
		
		String outputFile = jobNamePrefix + id + ".tar.gz ";
		String manifestFile = jobNamePrefix + id + ".manifest";
		String command = "touch " + manifestFile + " && find . -name '" + jobNamePrefix + id + "*' -print | sed 's/^\\.\\///' > " + manifestFile + " && " + 
				"tar --remove-files -czf " + outputFile + " -T " + manifestFile;
		if (markUnfinished) {
			command += " && touch " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
		} else {
		        command += " && rm -f " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
		}
		String prd = transportConnection.prefixRemoteFile(resultsDirectory);
		if (!w.getWorkingDirectory().equals(prd)) {
			command += " && cp " + outputFile + " " + prd;
		}
		w.setCommand(command);
		
		try {
			if (!gridFileService.exists(resultsDirectory))
				gridFileService.mkdir(resultsDirectory);
		} catch (IOException e) {
			logger.debug("unable to create remote directory");
			throw new GridExecutionException("unable to mkdir " + resultsDirectory);
		}
		
		try {
			transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridExecutionException(e.getLocalizedMessage(), e);
		}
	}
	
	/**
	 * copy declared result files to results directory and register
	 * 
	 * @param g
	 * @throws GridException 
	 * @throws FileNotFoundException 
	 */
	protected void secureResultsFiles(GridResult g) throws GridException, FileNotFoundException {
		if (g.getFileHandleIds() != null && g.getFileHandleIds().size() > 0) {
			copyResultsFiles(g);
			List<FileHandle> fhl = new ArrayList<FileHandle>();
			for (Integer id : g.getFileHandleIds()) {
				logger.debug("calling: getFileService().getFileHandleById(" + id + ") ");
			    FileHandle fh = getFileService().getFileHandleById(id);
			    fhl.add(fh);
			}    
			logger.debug("calling: getFileService().register(" + fhl + ") ");
			getFileService().register(fhl);
		}
	}
	
	protected void copyResultsFiles(GridResult g) throws GridException {
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(g.getWorkingDirectory());
		w.setResultsDirectory(g.getResultsDirectory());
		w.setRegistering(true);
		w.setMode(ExecutionMode.TASK_ARRAY);
		w.addCommand("if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then ls -1 > /dev/null && touch " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME + "; fi");
		int files = 0;
		logger.trace("preparing to copy " + g.getFileHandleIds().size() + " files for GridResult " + g.getUuid().toString());
		for (Integer hid : g.getFileHandleIds()) {
		    logger.trace("adding fileHandleId " + hid);
		    FileHandle fh = getFileService().getFileHandleById(hid);
                    w.addCommand("COPY[" + files + "]=\"" + WorkUnit.OUTPUT_FILE_PREFIX + "_" + fh.getId() + "." + g.getUuid().toString() + " " + fh.getFileName() + "\"");
                    files++;
                    fh.setFileURI(this.gridFileService.remoteFileRepresentationToLocalURI(w.getResultsDirectory() + "/" + fh.getFileName()));
                    getFileService().addFile(fh);
                }
		w.setNumberOfTasks(files);
		w.addCommand("THIS=${COPY[ZERO_TASK_ID]}");
		w.addCommand("read -ra FILE <<< \"$THIS\"");
		w.addCommand("cp -f ${FILE[0]} ${" + WorkUnit.RESULTS_DIRECTORY + "}${FILE[1]}");
		w.addCommand("if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then rm -f " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME + "; fi");
		GridResult r = execute(w);
		logger.trace("waiting for results file copy");
		ScheduledExecutorService ex = Executors.newSingleThreadScheduledExecutor();
		String poll = transportConnection.getConfiguredSetting("host.pollinterval");
		if (poll == null) 
			poll = "10000"; // 10s
		Integer pollint = Integer.parseInt(poll);
		while (!isFinished(r)) {
			ScheduledFuture<?> md5t = ex.schedule(new Runnable() {
				@Override
				public void run() {
				}
			}, pollint, TimeUnit.MILLISECONDS);
			while (!md5t.isDone()) {
				// not done
			}
		}
		ex.shutdownNow();
		logger.trace("copied");

	}
	
	protected void cleanUpAbnormallyTerminatedJob(GridResult g) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		cleanUpAbnormallyTerminatedJob(g.getHostname(), g.getWorkingDirectory(), g.getResultsDirectory(), g.getUuid().toString());
	}
	
	protected void cleanUpAbnormallyTerminatedJob(String hostname, String workingDirectory, String resultsDirectory, String id) throws GridAccessException, GridExecutionException, GridUnresolvableHostException {
		logger.warn("Cleaning FAILED job " + id + " at " + hostname + ":" + workingDirectory);
		
		WorkUnit w = new WorkUnit();
		w.setWorkingDirectory(transportConnection.prefixRemoteFile(workingDirectory));
		
		String outputFile = jobNamePrefix + id + "-FAILED.tar.gz ";
		String manifestFile = jobNamePrefix + id + ".manifest";
		String command = "touch " + manifestFile + " && find . -name '" + jobNamePrefix + id + "*' -print | sed 's/^\\.\\///' > " + manifestFile + " && " + 
				"tar --remove-files -czf " + outputFile + " -T " + manifestFile;
		command += " && rm -f " + WorkUnit.PROCESSING_INCOMPLETE_FILENAME;
		String prd = transportConnection.prefixRemoteFile(resultsDirectory);
		if (!w.getWorkingDirectory().equals(prd)) {
				command += " && cp " + outputFile + " " + prd;
		}
		w.setCommand(command);
		
		try {
			if (!gridFileService.exists(resultsDirectory))
				gridFileService.mkdir(resultsDirectory);
		} catch (IOException e) {
			logger.debug("unable to create remote directory");
			throw new GridExecutionException("unable to mkdir " + resultsDirectory);
		}
		
		
		try {
			transportConnection.sendExecToRemote(w);
		} catch (MisconfiguredWorkUnitException e) {
			logger.warn(e.getLocalizedMessage());
			throw new GridExecutionException(e.getLocalizedMessage(), e);
		}
	}


	protected GridResult startJob(WorkUnit w) throws MisconfiguredWorkUnitException, GridException {
		
		if (isJobExists(w)) {
			throw new GridAccessException("UUID already exists");
		}
		
		logger.debug("begin start job " + w.getId());
		// verify parallel environement is valid for this GWS if specified in work unit 
			
		SubmissionScript sss = getSubmissionScript(w);
		// TODO: user specific account settings
		if (getAccount() != null)
			sss.setAccount(getAccount());
		if(getMailRecipient() != null)
			sss.setMailRecipient(getMailRecipient());
		if(getMailCircumstances() != null)
			sss.setMailCircumstances(getMailCircumstances());
		if(getMaxRunTime() != null)
			sss.setMaxRunTime(getMaxRunTime());
		if (!w.getProcessMode().equals(ProcessMode.SINGLE)){
			if (w.getParallelEnvironment() != null && !w.getParallelEnvironment().isEmpty()){
				if (!getAvailableParallelEnvironments().contains(w.getParallelEnvironment()))
					throw new MisconfiguredWorkUnitException("Parallel environment specified in work unit is not registered as an available parallel environment in the Grid Work Service");
				sss.setParallelEnvironment(w.getParallelEnvironment(), w.getProcessorRequirements());
			} else if (w.getProcessMode().equals(ProcessMode.MPI)){
				if (getDefaultMpiParallelEnvironment() != null && !w.getParallelEnvironment().isEmpty()){
					sss.setParallelEnvironment(getDefaultMpiParallelEnvironment(), w.getProcessorRequirements());
				} else if (getDefaultParallelEnvironment() != null){
					logger.info("No default MPI parallel environment set so using default parallel environment");
					sss.setParallelEnvironment(getDefaultParallelEnvironment(), w.getProcessorRequirements());
				} else if (getAvailableParallelEnvironments().size() == 1){
					logger.info("No default parallel environments set so falling back to only known parallel environment in set of available parallel environments");
					sss.setParallelEnvironment(getAvailableParallelEnvironments().iterator().next(), w.getProcessorRequirements());
				} else {
					if (!isNumProcConsumable)
						logger.warn("A non-single core process mode was selected but no parallel environment was set and not more than one consumable core was selected. This may be an error.");
				}
			}
		} else {
			if (w.getParallelEnvironment() != null)
				logger.warn("A single core process mode was selected yet a parallel environment was set. This may be an error.");
		}
		sss.setNumProcConsumable(isNumProcConsumable());
		if(getProject() != null)
			sss.setProject(getProject());
		else if (w.getProject() != null)
			// if the host does not have a project set, attempt to set from the work unit (for accounting).
			sss.setProject(w.getProject());
		if(getQueue() != null)
			sss.setQueue(getQueue());
		
		File script;
		try {
			script = File.createTempFile("wasp-", ".sge");
			logger.debug("creating temporary local sge script: " + script.getAbsolutePath().toString());
			BufferedWriter scriptHandle = new BufferedWriter(new FileWriter(script));
			scriptHandle.write(sss.toString());
			scriptHandle.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new GridAccessException("unable to get local temp file ", e);
		}
		try {
			logger.debug("putting script file on remote server");
			gridFileService.put(script, w.getWorkingDirectory() + jobNamePrefix + w.getId() + ".sh");
			logger.debug("script file copied");
		} catch (IOException e) {
			e.printStackTrace();
			throw new GridAccessException("unable to begin grid transaction ", e);
		} finally {
			logger.debug("deleting local temporary script file: " + script.getAbsolutePath().toString());
			script.delete();
		}
		
		//String submit = "cd " +  w.remoteWorkingDirectory + " && qsub " + jobNamePrefix + w.getId() + ".sh 2>&1";
		String submit = "qsub " + jobNamePrefix + w.getId() + ".sh 2>&1";
		w.setWrapperCommand(submit);
		GridResultImpl result = (GridResultImpl) transportConnection.sendExecToRemote(w);
		result.setUuid(UUID.fromString(w.getId()));
		result.setId(jobNamePrefix + w.getId());
		result.setWorkingDirectory(w.getWorkingDirectory());
		result.setResultsDirectory(w.getResultsDirectory());
		result.setHostname(transportConnection.getHostName());
		result.setMode(w.getMode());
		result.setSecureResults(w.isSecureResults());
		if (w.isSecureResults()) {
			for (FileHandle fh : w.getResultFiles()) {
			    logger.trace("WorkUnit " + w.getId() + " is going to register FileHandle " + fh.getId());
				result.getFileHandleIds().add(fh.getId());
			}
			
		}
			
		if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
			if (w.getNumberOfTasks() == null)
				throw new MisconfiguredWorkUnitException("Task arrays need to set numberOfTasks (cannot be null)");
			result.setNumberOfTasks(w.getNumberOfTasks());
		}
		return (GridResult) result;
	}
	
	@Override
	public void setAvailableParallelEnvironments(String commaDelimitedParallelEnvironments){
		for (String parallelEnv : commaDelimitedParallelEnvironments.split(","))
			this.parallelEnvironments.add(parallelEnv);
	}
	
	@Override
	public Set<String> getAvailableParallelEnvironments() {
		return this.parallelEnvironments;
	}
	
	@Override
	public String getDefaultParallelEnvironment() {
		return defaultParallelEnvironment;
	}

	/**
	 * set default parallel environment to be use for non-MPI jobs requiring multi-cores. Also adds this parallel environment to the set of 
	 * available parallel environments
	 * @param defaultParallelEnvironment
	 */
	@Override
	public void setDefaultParallelEnvironment(String defaultParallelEnvironment) {
		this.defaultParallelEnvironment = defaultParallelEnvironment;
		this.parallelEnvironments.add(defaultParallelEnvironment);
		
	}
	
	@Override
	public String getDefaultMpiParallelEnvironment() {
		return defaultMpiParallelEnvironment;
	}

	/**
	 * set default parallel environment to be use for MPI jobs. Also adds this parallel environment to the set of 
	 * available parallel environments
	 * @param defaultMpiParallelEnvironment
	 */
	@Override
	public void setDefaultMpiParallelEnvironment(String defaultMpiParallelEnvironment) {
		this.defaultMpiParallelEnvironment = defaultMpiParallelEnvironment;
		this.parallelEnvironments.add(defaultMpiParallelEnvironment);
	}

	protected SubmissionScript getSubmissionScript(WorkUnit w) throws GridException, MisconfiguredWorkUnitException {
		return new SgeSubmissionScript(w);
	}

	/**
	 * Inner class representing a standard SGE submission script.
	 * 
	 * @author calder
	 * 
	 */
	protected class SgeSubmissionScript implements SubmissionScript {
		
		public static final int HELD = 16;
		public static final int QUEUED = 64;
		public static final int RUNNING = 128;
		public static final int SUSPENDED = 256;
		public static final int WAITING = 2048;
		public static final int ERROR = 32768;
		public static final int SUSPENDED_ON_THRESHOLD = 65536;

		protected String schedulerFlag = "#$";

		protected WorkUnit w;
		protected String name = "not_set";
		public String jobName;
		protected String header = "";
		protected String preamble = "";
		protected String configuration = "";
		protected String command = "";
		protected String postscript = "";
		protected String account = "";
		protected String queue = "";
		protected String maxRunTime = "";
		protected String parallelEnvironment = "";
		protected String project = "";
		protected String mailRecipient = "";
		protected String mailCircumstances = "";
		protected boolean isNumProcConsumable = false;
		
		/**
		 * Default no-arg constructor is unused.
		 */
		@Deprecated
		protected SgeSubmissionScript() {
			super();
		}

		protected SgeSubmissionScript(WorkUnit w) throws GridException, MisconfiguredWorkUnitException {
			this.w = w;
			this.name = w.getId();

			String tid = "";
			
			jobName = jobNamePrefix + name;
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
			    tid = "-$TASK_ID";
			
			header = "#!/bin/bash\n#\n" +
					getFlag() + " -N " + jobName + "\n" +
					getFlag() + " -S /bin/bash\n" +
					getFlag() + " -V\n" +
					getFlag() + " -o " + w.remoteWorkingDirectory + jobNamePrefix + name + tid + ".out\n" +
					getFlag() + " -e " + w.remoteWorkingDirectory + jobNamePrefix + name + tid + ".err\n";
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				header += getFlag() + " -t 1-" + w.getNumberOfTasks() + "\n"; 
			}
			
			preamble = "\nset -o errexit\n" + 	// die if any script returns non 0 exit code
					"set -o pipefail\n" + 		// die if any script in a pipe returns non 0 exit code
					"set -o physical\n"; 		// replace symbolic links with physical path
			
			preamble += "\ncd " + w.remoteWorkingDirectory + "\n" +
					WorkUnit.JOB_NAME + "=" + jobNamePrefix + name + "\n" +
					WorkUnit.WORKING_DIRECTORY + "=" + w.remoteWorkingDirectory + "\n" +
					WorkUnit.RESULTS_DIRECTORY + "=" + w.remoteResultsDirectory + "\n";
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				preamble += WorkUnit.TASK_ARRAY_ID + "=${SGE_TASK_ID}\n" + 
				                WorkUnit.ZERO_TASK_ARRAY_ID + "=$[WASP_TASK_ID - 1]\n" +
						WorkUnit.TASK_OUTPUT_FILE + "=$WASPNAME-${WASP_TASK_ID}.out\n" +
						WorkUnit.TASK_END_FILE + "=$WASPNAME:${WASP_TASK_ID}.end\n";
			}
			
			String metadata = transportConnection.getConfiguredSetting("metadata.root");
			if (!PropertyHelper.isSet(metadata)) {
				throw new NullResourceException("metadata folder not configured!");
			}
			preamble += WorkUnit.METADATA_ROOT + "=" + transportConnection.prefixRemoteFile(metadata) + "\n";
			
			int fi = 0;
			for (edu.yu.einstein.wasp.model.FileHandle f : w.getRequiredFiles()) {
				
				logger.debug("WorkUnit required file: " + f.getFileURI().toString());
				try {
					preamble += WorkUnit.INPUT_FILE + "[" + fi + "]=" + provisionRemoteFile(f) + "\n";
				} catch (FileNotFoundException e) {
					throw new MisconfiguredWorkUnitException("unknown file " + f.getFileURI());
				} 
				fi++;
			}
			fi = 0;
			logger.trace("file inspection for " + w.getResultFiles().size() + " file groups");
			logger.trace("adding file variables for " + w.getResultFiles().size() + " file handles");
			for (FileHandle fh : w.getResultFiles()) {
			    String filestr = WorkUnit.OUTPUT_FILE + "[" + fi + "]=" + WorkUnit.OUTPUT_FILE_PREFIX + "_" + fh.getId() +"."+ w.getId().replaceFirst(jobNamePrefix, "") + "\n";
			    preamble += filestr;
			    fi++;
			}
			
			preamble += "\n# Configured environment variables\n\n";
			
			for (String key : w.getEnvironmentVars().keySet()) {
				preamble += key + "=" + w.getEnvironmentVars().get(key) + "\n";
			}
			
			preamble += "\n####\n";
			
			preamble +=	"\necho $JOB_ID >> " + "${" + WorkUnit.JOB_NAME + "}.start\n" +
					"echo submitted to host `hostname -f` `date` 1>&2";
			// if there is a configured setting to prepare the interpreter, do that here 
			String env = transportConnection.getConfiguredSetting("env");
			if (PropertyHelper.isSet(env)) {
				configuration = env + "\n";
			}
			// If the ProcessMode is set to MAX, get the configuration for this host and set processor reqs
			String pmodeMax = transportConnection.getConfiguredSetting("processmode.max");
			if (!PropertyHelper.isSet(pmodeMax)) {
				pmodeMax = "1";
			}
			logger.debug("WorkUnit was congigured to use " + w.getProcessorRequirements() + " threads");
			if (w.getProcessMode().equals(ProcessMode.MAX)) {
				w.setProcessorRequirements(new Integer(pmodeMax));
				logger.debug("WorkUnit recongigured to use " + w.getProcessorRequirements() + " threads (using ProcessMode.MAX)");
			}
			// If the ProcessMode is set to FIXED, make sure it does not exceed the max setting for this host.
			String pmodeMaximum = transportConnection.getConfiguredSetting("processmode.absolutemaximum");
			if (!PropertyHelper.isSet(pmodeMaximum)) {
				pmodeMaximum = "1";
			}
			
			Integer pmm = new Integer(pmodeMaximum);
			if (w.getProcessorRequirements() > pmm) {
				w.setProcessorRequirements(pmm);
				logger.debug("WorkUnit is recongigured to use " + w.getProcessorRequirements() + " (absolute maximum) threads");
			}
			if (transportConnection.getSoftwareManager() != null) {
				String config = transportConnection.getSoftwareManager().getConfiguration(w);
				if (config != null) {
					configuration += config;
				}
			} 
			command = w.getCommand();
			
			postscript = "echo \"##### begin ${" + WorkUnit.JOB_NAME + "}\" > " + w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.command\n\n" +
					"awk '/^##### preamble/,/^##### postscript|~$/' " + 
						w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.sh | sed 's/^##### .*$//g' | grep -v \"^$\" >> " +
						w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.command\n" +
					"echo \"##### end ${" + WorkUnit.JOB_NAME + "}\" >> " + w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.command\n";
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY))
			    postscript = "if [ \"$" + WorkUnit.TASK_ARRAY_ID + "\" -eq \"1\" ]; then\n" + postscript + "fi\n";
			
			if (w.getMode().equals(ExecutionMode.TASK_ARRAY)) {
				postscript += "touch ${" + WorkUnit.WORKING_DIRECTORY + "}/${" + WorkUnit.TASK_END_FILE + "}\n" +
					"echo completed on `hostname -f` `date` 1>&2\n";
			} else {
				postscript += "touch " + w.remoteWorkingDirectory + "${" + WorkUnit.JOB_NAME + "}.end\n" +
					"echo completed on `hostname -f` `date` 1>&2\n";
			}
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public boolean isNumProcConsumable() {
			return isNumProcConsumable;
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setNumProcConsumable(boolean isNumProcConsumable) {
			this.isNumProcConsumable = isNumProcConsumable;
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getFlag() {
			return schedulerFlag;
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getMemory() {
			// 'mem_free' specifies how much memory should be free on a node before scheduling a job (or task) there 
			// 'h_vmem' specifies maximum memory a scheduled job (or task) may use. Will kill job / task 
			// with a memory allocation error if memory used exceeds this value
			return getFlag() + " -l mem_free=" + w.getMemoryRequirements().toString() + "G\n" +
					WorkUnit.REQUESTED_GB_MEMORY + "=" + w.getMemoryRequirements().toString() + "\n";
		}
		
		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getProcs() {
			return getFlag() + " -l p=" + w.getProcessorRequirements().toString() + "\n" +
					WorkUnit.NUMBER_OF_THREADS + "=" + w.getProcessorRequirements().toString() + "\n";
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(header)
				.append("\n\n##### resource requests\n\n")
				.append(getAccount())
				.append(getQueue())
				.append(getMaxRunTime());
			if (getParallelEnvironment() != null)
				sb.append(getParallelEnvironment());
			if (isNumProcConsumable)
				sb.append(getProcs());
			sb.append(getMemory()) 
				.append(getProject())
				.append(getMailRecipient())
				.append(getMailCircumstances())
				.append("\n\n##### preamble \n\n")
				.append(preamble) 
				.append("\n\n##### configuration \n\n")
				.append(configuration)
				.append("\n\n##### command \n\n")
				.append(command) 
				.append("\n\n##### postscript\n\n")
				.append(postscript);
			logger.debug(sb.toString());
			return sb.toString();
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getAccount() {
			return account;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setAccount(String account) {
			if (PropertyHelper.isSet(account)) this.account = getFlag() + " -A " + account + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getQueue() {
			return queue;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setQueue(String queue) {
			if (PropertyHelper.isSet(queue)) this.queue = getFlag() + " -q " + queue + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getMaxRunTime() {
			return maxRunTime;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setMaxRunTime(String maxRunTime) {
			if (PropertyHelper.isSet(maxRunTime)) this.maxRunTime = getFlag() + " -l h_rt=" + maxRunTime + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getParallelEnvironment() {
			return parallelEnvironment;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setParallelEnvironment(String parallelEnvironment, Integer procs) {
			if (PropertyHelper.isSet(parallelEnvironment)) this.parallelEnvironment = getFlag() + " -pe " + parallelEnvironment + " " + procs + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getProject() {
			return project;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setProject(String project) {
			if (PropertyHelper.isSet(project)) this.project = getFlag() + " -P " + project + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getMailRecipient() {
			return mailRecipient;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setMailRecipient(String mailRecipient) {
			if (PropertyHelper.isSet(mailRecipient)) this.mailRecipient = getFlag() + " -M " + mailRecipient + "\n";
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public String getMailCircumstances() {
			return mailCircumstances;
		}

		/** 
		 * {@inheritDoc}
		 */
		@Override
		public void setMailCircumstances(String mailCircumstances) {
			if (PropertyHelper.isSet(mailCircumstances)) this.mailCircumstances = getFlag() + " -m " + mailCircumstances + "\n";
		}

	}

	@Override
	public GridFileService getGridFileService() {
		return gridFileService;
	}
	
	public void setGridFileService(GridFileService gridFileService) {
		this.gridFileService = gridFileService;
	}
	@Override
	public GridTransportConnection getTransportConnection() {
		return transportConnection;
	}
	
	/**
	 * This needs to be fleshed out.  Files which are not on the compute host need to be copied into place
	 * prior to execution of the work unit.
	 * 
	 */
	protected String provisionRemoteFile(FileHandle file) throws FileNotFoundException, GridException {
		
		String fileName;
		try {
			fileName = transportConnection.prefixRemoteFile(file.getFileURI());
			logger.debug("proceeding with file: " + fileName);
		} catch (GridUnresolvableHostException e) {
			// file is not registered on remote host.
			// provision to temporary directory.
			String tempfile = transportConnection.prefixRemoteFile(file.getFileURI().getPath());
			tempfile = transportConnection.getConfiguredSetting("remote.dir") + "/" + tempfile;
			tempfile = tempfile.replaceAll("//", "").replaceAll("//", "/");
			logger.debug("will attempt to provision file: " + tempfile);
			try {
				if (gridFileService.exists(tempfile)) {
					return tempfile;
				} else {
					doProvisionRemoteFile(file);
					return tempfile;
				}
			} catch (IOException e1) {
				String message = "Unable to test for existence of " + file.getFileURI().toString() + " : " + e.getLocalizedMessage();
				logger.warn(message);
				throw new GridAccessException(message);
			}
		}
		return fileName;
	}
	
	protected void doProvisionRemoteFile(edu.yu.einstein.wasp.model.FileHandle file) throws FileNotFoundException, GridException {
		// TODO: provision remote file from other host.
	}

    /** 
     * {@inheritDoc}
     */
    @Override
    public LinkedHashMap<Integer, String> getMappedTaskOutput(GridResult r) throws IOException {
        if (!r.getMode().equals(ExecutionMode.TASK_ARRAY))
            throw new WaspRuntimeException("Mapped task output is only available from TASK_ARRAY results");
        LinkedHashMap<Integer, String> result = new LinkedHashMap<Integer, String>();
        File f = File.createTempFile("wasp", "work");
        String path = r.getArchivedResultOutputPath();
        logger.debug("temporary tar file " + f.getAbsolutePath() + " for " + path);
        gridFileService.get(path, f);
        FileInputStream afis = new FileInputStream(f);
        GZIPInputStream agz = new GZIPInputStream(afis);
        TarArchiveInputStream a = new TarArchiveInputStream(agz);
        try {
            logger.debug("tar " + a.toString());
            TarArchiveEntry e;
            while ((e = a.getNextTarEntry()) != null) {
                logger.trace("saw tar file entry " + e.getName());
                Matcher filem = Pattern.compile("-([0-9]+?).out").matcher(e.getName());
                if (!filem.find())
                    continue;
                Integer record = Integer.parseInt(filem.group(1));
                logger.trace("record number " + record + " size " + e.getSize());
                result.put(record, IOUtils.toString(a, "UTF-8"));
                
            }
        } finally {
            a.close();
            agz.close();
            afis.close();
            f.delete();
        }
        return result;
    }
	
	protected InputStream readResultFile(GridResult r, String suffix, boolean keep) throws IOException {
		String path = r.getArchivedResultOutputPath();
		if (! gridFileService.exists(path)) {
			throw new FileNotFoundException("file not found " + path);
		}
		File f;
		if (!keep) {
			f = File.createTempFile("wasp", "work");
		} else {
			f = new File(localTempDir + File.separator + r.getUuid());
		}
		if (!f.exists())
			gridFileService.get(path, f);
		logger.trace("temporary tar file " + f.getAbsolutePath());
		String contentName = jobNamePrefix + r.getUuid() + suffix;
		logger.trace("looking for: " + contentName);
		FileInputStream afis = new FileInputStream(f);
		GZIPInputStream agz = new GZIPInputStream(afis);
		TarArchiveInputStream a = new TarArchiveInputStream(agz);
		logger.trace("tar " + a.toString());
		TarArchiveEntry e;
		while ((e = a.getNextTarEntry()) != null) {
			logger.trace("saw tar file entry " + e.getName());
			if (e.getName().equals(contentName)) {
				byte[] content = new byte[(int) e.getSize()];
				a.read(content, 0, content.length);
				ByteArrayInputStream result = new ByteArrayInputStream(content);
			    a.close();
			    agz.close();
			    afis.close();
			    if (!keep) {
			    	f.delete();
			    } else {
			    	f.deleteOnExit();
			    }
				return result;
			}
		}
		f.deleteOnExit();
		a.close();
		agz.close();
	    afis.close();
		throw new FileNotFoundException("Archive " + f.getAbsolutePath() + " did not appear to contain " + contentName);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext = arg0;
		
	}

	@Override
	public String getResultStdOut(GridResult r) throws IOException {
		return getResultOutputFile(r, "out");
	}

	@Override
	public String getResultStdErr(GridResult r) throws IOException {
		return getResultOutputFile(r, "err");
	}
	
	private String getResultOutputFile(GridResult r, String type) throws IOException {
		String result = "";
		File f = File.createTempFile("wasp", "work");
        String path = r.getArchivedResultOutputPath();
        logger.debug("temporary tar file " + f.getAbsolutePath() + " for " + path);
        gridFileService.get(path, f);
        FileInputStream afis = new FileInputStream(f);
        GZIPInputStream agz = new GZIPInputStream(afis);
        TarArchiveInputStream a = new TarArchiveInputStream(agz);
        try {
            logger.trace("tar " + a.toString());
            TarArchiveEntry e;
            while ((e = a.getNextTarEntry()) != null) {
                logger.trace("saw tar file entry " + e.getName());
                Matcher filem = Pattern.compile(r.getId() + "." + type).matcher(e.getName());
                if (!filem.find())
                    continue;
                logger.trace("matched " + e.getName() + " size " + e.getSize());
                result = IOUtils.toString(a, "UTF-8");
                break;
            }
        } finally {
            a.close();
            agz.close();
            afis.close();
            f.delete();
        }
        return result;
	}

	@Override
	public String getUnregisteredFileContents(GridResult r, String filename) throws IOException {
		String result = "";
		File f = File.createTempFile("wasp", "work");
		String path = r.getWorkingDirectory() + "/" + filename;
		gridFileService.get(path, f);
		FileInputStream afis = new FileInputStream(f);
		result = IOUtils.toString(afis, "UTF-8");
		return result;
	}
	
	@Override
	public boolean isNumProcConsumable() {
		return isNumProcConsumable;
	}

	@Override
	public void setNumProcConsumable(boolean isNumProcConsumable) {
		this.isNumProcConsumable = isNumProcConsumable;
	}
	
}
