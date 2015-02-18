package se.tjing.interaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.user.PersonService;

@RestController
@RequestMapping("/interaction")
public class InteractionController {

	@Autowired
	InteractionService interactionService;

	@Autowired
	PersonService personService;

	@RequestMapping(value = "/{interactionId}/accept", method = RequestMethod.PUT)
	public ResponseEntity<Interaction> approveRequest(
			@PathVariable Integer interactionId) {
		Interaction result = interactionService.accept(interactionId,
				personService.getCurrentUser());
		return new ResponseEntity<Interaction>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{interactionId}/handoverconfirm", method = RequestMethod.PUT)
	public ResponseEntity<Interaction> confirmHandover(
			@PathVariable Integer interactionId) {
		Interaction result = interactionService.confirmHandover(interactionId,
				personService.getCurrentUser());
		return new ResponseEntity<Interaction>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{interactionId}/returnconfirm", method = RequestMethod.PUT)
	public ResponseEntity<Interaction> confirmReturn(
			@PathVariable Integer interactionId) {
		Interaction result = interactionService.confirmReturn(interactionId,
				personService.getCurrentUser());
		return new ResponseEntity<Interaction>(result, null, HttpStatus.OK);
	}

}
