package edu.yu.einstein.wasp.plugin.rnaseq.integration.messages;

import org.springframework.messaging.Message;

import edu.yu.einstein.wasp.exception.WaspMessageInitializationException;
import edu.yu.einstein.wasp.integration.messages.WaspMessageType;
import edu.yu.einstein.wasp.integration.messages.WaspStatus;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspJobTask;
import edu.yu.einstein.wasp.integration.messages.tasks.WaspTask;
import edu.yu.einstein.wasp.integration.messages.templates.WaspStatusMessageTemplate;

/**
 * @author 
 * 
 */
public class SimpleRnaseqStatusMessageTemplate extends WaspStatusMessageTemplate {


	public SimpleRnaseqStatusMessageTemplate() {
		super();
		addHeader(WaspMessageType.HEADER_KEY, RnaseqMessageType.RNASEQ);
	}
	
	public SimpleRnaseqStatusMessageTemplate(Message<WaspStatus> message){
		super(message);
		if (!isMessageOfCorrectType(message))
			throw new WaspMessageInitializationException("message is not of the correct type");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessage(Message<?> message){
		String task = (String) getHeader(WaspJobTask.HEADER_KEY);
		return actUponMessage(message, task);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean actUponMessageIgnoringTask(Message<?> message){
		return actUponMessage(message, null);
	}
	
	// Statics.........

	
	/**
	 * Takes a message and checks its headers to see if the message should be acted upon or not
	 * @param message
	 * @param task
	 * @return
	 */
	public static boolean actUponMessage(Message<?> message, String task ){
		if (!message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) ||
				!((String) message.getHeaders().get(WaspMessageType.HEADER_KEY)).equals(RnaseqMessageType.RNASEQ))
				return false;
		if (task == null)
			return true;
		if (message.getHeaders().containsKey(WaspTask.HEADER_KEY) && message.getHeaders().get(WaspTask.HEADER_KEY).equals(task))
			return true;
		return false;
	}
	
	/**
	 * Returns true is the message is of the correct RnaseqMessageType
	 * @param message
	 * @return
	 */
	public static boolean isMessageOfCorrectType(Message<?> message) {
		return message.getHeaders().containsKey(WaspMessageType.HEADER_KEY) &&  
				message.getHeaders().get(WaspMessageType.HEADER_KEY).equals(RnaseqMessageType.RNASEQ);
	}
	
	@Override
	public SimpleRnaseqStatusMessageTemplate getNewInstance(WaspStatusMessageTemplate messageTemplate){
		SimpleRnaseqStatusMessageTemplate newTemplate = new SimpleRnaseqStatusMessageTemplate();
		copyCommonProperties(messageTemplate, newTemplate);
		return newTemplate;
	}

}
