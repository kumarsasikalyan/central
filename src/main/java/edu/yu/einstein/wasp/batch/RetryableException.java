package edu.yu.einstein.wasp.batch; 

import java.lang.Exception;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * Retryable Exception 
 * - used to suppress error messages; 
 *
 */


public class RetryableException extends Exception {
	public RetryableException() {super();}
	public RetryableException(String s) {super(s);}
	public RetryableException(String s, Throwable t) {super(s, t);}
	public RetryableException(Throwable t) {super(t);}

	public void publicStackTrace() {}
	public void publicStackTrace(PrintStream s) {}
	public void publicStackTrace(PrintWriter w) {}
}
