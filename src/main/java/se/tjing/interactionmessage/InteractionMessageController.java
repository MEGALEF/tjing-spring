package se.tjing.interactionmessage;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.interaction.InteractionService;
import se.tjing.user.Person;
import se.tjing.user.PersonService;


@RestController
@RequestMapping("/interactionmessage")
public class InteractionMessageController {
	
	@Autowired
	InteractionService interactionService;
	
	@Autowired
	InteractionMessageService msgService;
	
	@Autowired 
	PersonService personService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<InteractionMessage>> getUnread(){
		List<InteractionMessage> result = msgService.getUnread(personService.getCurrentUser());
		return new ResponseEntity<List<InteractionMessage>>(result, null, HttpStatus.OK);
	}
	
	@RequestMapping(value="{messageId}", method=RequestMethod.PATCH)
	public ResponseEntity<InteractionMessage> markAsRead(@PathVariable Integer messageId, @RequestBody InteractionMessage update){
		InteractionMessage result = msgService.updateMessage(personService.getCurrentUser(), update);
		return new ResponseEntity<InteractionMessage>(result, null, HttpStatus.ACCEPTED);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<InteractionMessage> sendMessage(@RequestBody InteractionMessage msg){
		InteractionMessage result = msgService.relayMessage(personService.getCurrentUser(), msg);
		
		return new ResponseEntity<InteractionMessage>(result, null, HttpStatus.ACCEPTED);
	}
}
