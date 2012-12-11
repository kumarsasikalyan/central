package edu.yu.einstein.wasp.integration.messages;

import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

import edu.yu.einstein.wasp.batch.launch.BatchJobLaunchContext;
import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;

/**
 * Handling WASP Batch Job Launch messages.
 * @author asmclellan
 *
 */
public class BatchJobLaunchMessageTemplate extends WaspMessageTemplate{
	
	
	private BatchJobLaunchContext batchJobLaunchContext;
	
	
	public BatchJobLaunchContext getBatchJobLaunchContext() {
		return batchJobLaunchContext;
	}

	public void setBatchJobLaunchContext(BatchJobLaunchContext batchJobLaunchContext) {
		this.batchJobLaunchContext = batchJobLaunchContext;
	}

	public BatchJobLaunchMessageTemplate(BatchJobLaunchContext batchJobLaunchContext){
		super();
		this.batchJobLaunchContext = batchJobLaunchContext;
	}
	
		
	/**
	 * Build a Spring Integration Message using the WaspMessageType.HEADER_KEY header and the BatchJobLaunchContext as payload.
	 * @return {@link Message}<{@link BatchJobLaunchContext}>
	 * @throws WaspMessageBuildingException
	 */
	@Override
	public Message<BatchJobLaunchContext> build() throws WaspMessageBuildingException{
		if (this.batchJobLaunchContext == null)
			throw new WaspMessageBuildingException("no BatchJobLaunchContext message defined");
		Message<BatchJobLaunchContext> message = null;

		try {
			if (this.task == null){
				message = MessageBuilder.withPayload(batchJobLaunchContext)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.LAUNCH_BATCH_JOB)
						.setHeader(TARGET_KEY, "batch")
						.setHeader(WaspJobTask.HEADER_KEY, task)
						.build();
			} else {
				message = MessageBuilder.withPayload(batchJobLaunchContext)
						.setHeader(WaspMessageType.HEADER_KEY, WaspMessageType.LAUNCH_BATCH_JOB)
						.setHeader(TARGET_KEY, "batch")
						.setHeader(WaspJobTask.HEADER_KEY, task)
						.build();
			}
		} catch(Exception e){
			throw new WaspMessageBuildingException("build() failed to build message: "+e.getMessage());
		}
		return message;
	}
	
	/**
	 * Takes a message and checks its headers against the WaspMessageType header and the payload type to see if the message should be acted upon or not
	 * @param message
	 * @param batchJobLaunchContext
	 * @param task
	 * @return
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		if (this.task != null)
			return actUponMessage(message);
		return actUponMessage(message, this.task);
	}
	
	// Statics.........

	
	/**
	 * Takes a message and checks its headers against the WaspMessageType header and the payload type to see if the message should be acted upon or not
	 * @param message
	 * @param batchJobLaunchContext
	 * @return
	 */
	public static boolean actUponMessageS(Message<?> message){
		
		if ( message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) &&  message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(WaspMessageType.LAUNCH_BATCH_JOB) &&
				BatchJobLaunchContext.class.isInstance(message.getPayload()) )
			return true;
		return false;
	}
	
	/**
	 * Takes a message and checks its headers against the WaspMessageType header, task and the payload type to see if the message should be acted upon or not
	 * @param message
	 * @param batchJobLaunchContext
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, String task){
		if (task != null && 
				message.getHeaders().containsKey(WaspJobTask.HEADER_KEY) && 
				message.getHeaders().get(WaspJobTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
}