package se.tjing.interaction;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.rating.Rating;
import se.tjing.rating.RatingService;
import se.tjing.user.PersonService;

@RestController
@RequestMapping("/interaction")
public class InteractionController {

	@Autowired
	InteractionService interactionService;

	@Autowired
	RatingService ratingService;

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

	@RequestMapping(value = "/{interactionId}/rate", method = RequestMethod.POST)
	public ResponseEntity<Rating> rate(@PathVariable Integer interactionId,
			@RequestBody String ratingStr) {
		Rating result = ratingService.rate(personService.getCurrentUser(),
				interactionId, ratingStr);
		return new ResponseEntity<Rating>(result, null, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/incoming", method = RequestMethod.GET)
	public ResponseEntity<List<Interaction>> getIncomingRequests() {
		List<Interaction> result = interactionService
				.getUserIncomingInteractions(personService.getCurrentUser());
		return new ResponseEntity<List<Interaction>>(result, null,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/outgoing", method = RequestMethod.GET)
	public ResponseEntity<Set<Interaction>> getOutgoingRequests() {
		Set<Interaction> result = interactionService.getOutgoing(personService
				.getCurrentUser());
		return new ResponseEntity<Set<Interaction>>(result, null, HttpStatus.OK);
	}

}
