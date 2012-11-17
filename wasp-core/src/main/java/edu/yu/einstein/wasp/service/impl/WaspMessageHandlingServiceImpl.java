package edu.yu.einstein.wasp.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.Message;
import org.springframework.integration.MessageHandlingException;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessagingTemplate;

import edu.yu.einstein.wasp.exception.WaspMessageBuildingException;
import edu.yu.einstein.wasp.integration.messages.WaspTask;
import edu.yu.einstein.wasp.integration.messages.payload.WaspStatus;

public abstract class WaspMessageHandlingServiceImpl extends WaspServiceImpl{
	
	public static final Long MESSAGE_RECEIVE_TIMEOUT = new Long(5000); // 5s
	
	private final String OUTBOUND_MESSAGE_CHANNEL = "wasp.channel.remoting.outbound";
	
	@Autowired
	@Qualifier(OUTBOUND_MESSAGE_CHANNEL)
	private DirectChannel outboundRemotingChannel; // channel to send messages out of system
	
	/**
	 * Send an outbound message via Spring Integration
	 * @param message
	 * @throws WaspMessageBuildingException
	 */
	public void sendOutboundMessage(final Message<?> message) throws WaspMessageBuildingException{
		logger.debug("Sending message via '" + OUTBOUND_MESSAGE_CHANNEL + "': "+message.toString());
		MessagingTemplate messagingTemplate = new MessagingTemplate();
		messagingTemplate.setReceiveTimeout(MESSAGE_RECEIVE_TIMEOUT);
		Message<?> replyMessage  = null;
		try{
			replyMessage = messagingTemplate.sendAndReceive(outboundRemotingChannel, message);
		} catch(Throwable t){
			throw new WaspMessageBuildingException("Problem encountered sending message '" + message.toString() + ": " + t.getLocalizedMessage());
		}
		if(replyMessage == null){
			// TODO: send exception
			logger.warn("Did not receive a reply on sending outbound message :"+ message.toString());
			return;
		}
		if (replyMessage.getHeaders().containsKey(WaspTask.EXCEPTION))
			throw new WaspMessageBuildingException("Problem encountered sending message '" + message.toString() + "' : " + replyMessage.getHeaders().get(WaspTask.EXCEPTION));
		if (WaspStatus.class.isInstance(replyMessage.getPayload()) && replyMessage.getPayload().equals(WaspStatus.FAILED))
			throw new WaspMessageBuildingException("Problem encountered sending message'" + message.toString() + "': no exception returned but status was FAILED");
	}	

}
