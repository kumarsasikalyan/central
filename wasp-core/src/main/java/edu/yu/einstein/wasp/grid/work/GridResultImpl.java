/**
 * 
 */
package edu.yu.einstein.wasp.grid.work;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Default Implementation of {@link GridResult}.
 * 
 * @author calder
 *
 */
public class GridResultImpl implements GridResult {

	private UUID uuid;
	private String hostname;
	private String username;
	private String workingDirectory;
	protected int finalStatus = 0;
	protected String finalOutput = "";

	private int exitStatus;
	private InputStream stdOutStream;
	private InputStream stdErrStream;
	
	@Override
	public int getExitStatus() {
		return exitStatus;
	}
	
	public void setExitStatus(int exitStatus) {
		this.exitStatus = exitStatus;
	}

	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.GridResult#getOutputStream()
	 */
	@Override
	public InputStream getStdOutStream() {
		return this.stdOutStream;
	}
	
	public void setStdOutStream(InputStream stdOutStream) {
		this.stdOutStream = stdOutStream;
	}
	
	/* (non-Javadoc)
	 * @see edu.yu.einstein.wasp.grid.GridResult#getOutputStream()
	 */
	@Override
	public InputStream getStdErrStream() {
		return this.stdErrStream;
	}
	
	public void setStdErrStream(InputStream stdErrStream) {
		this.stdErrStream = stdErrStream;
	}

	@Override
	public UUID getUuid() {
		return this.uuid;
	}
	
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	/**
	 * @return the hostname
	 */
	@Override
	public String getHostname() {
		return hostname;
	}

	/**
	 * @param hostname the hostname to set
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * @return the workingDirectory
	 */
	@Override
	public String getWorkingDirectory() {
		return workingDirectory;
	}

	/**
	 * @param workingDirectory the workingDirectory to set
	 */
	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public int getFinalStatus() {
		return this.finalStatus;
	}

	@Override
	public String getFinalOutput() {
		return this.finalOutput;
	}
}
