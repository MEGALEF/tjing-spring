package se.tjing.config;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;

public class TopicSubscriptionInterceptor extends ChannelInterceptorAdapter{
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
	    StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
	    if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand()) && headerAccessor.getUser() !=null ) {
	        Principal userPrincipal = headerAccessor.getUser();
	        if(!validateSubscription(userPrincipal, headerAccessor.getDestination()))
	        {
	            throw new IllegalArgumentException("No permission for this topic");
	        }
	    }
	    return message;
	}

	private boolean validateSubscription(Principal principal, String topicDestination)
	{
	    //Validation logic coming here
	    return true;
	}
}
