package se.tjing.interaction;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.rating.Rating;
import se.tjing.rating.RatingService;
import se.tjing.user.PersonService;

import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/interaction")
public class InteractionController {

	@Autowired
	InteractionService interactionService;

	@Autowired
	RatingService ratingService;

	@Autowired
	PersonService personService;
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<Interaction> initiateRequest(@RequestBody AddInteraction interaction){
		Interaction newInteraction = interactionService.initiateRequest(personService.getCurrentUser(),
				interaction);
		return new ResponseEntity<Interaction>(newInteraction, null,
				HttpStatus.CREATED);
	}

	/**
	 * Accept an incoming request
	 * 
	 * @param interactionId
	 * @return
	 */
	@RequestMapping(value = "/{interactionId}/accept", method = RequestMethod.PATCH)
	public ResponseEntity<Interaction> approveRequest(
			@PathVariable Integer interactionId) {
		Interaction result = interactionService.accept(interactionId,
				personService.getCurrentUser());
		return new ResponseEntity<Interaction>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{interactionId}/deny", method = RequestMethod.DELETE)
	public ResponseEntity<Interaction> denyRequest(
			@PathVariable Integer interactionId) {
		Interaction result = interactionService.deny(interactionId,
				personService.getCurrentUser());
		return new ResponseEntity<Interaction>(result, null, HttpStatus.OK);
	}
	
	/**
	 * Cancels an interaction. Similar to deny except it can be done to an accepted interaction and should leave a mark on the users interaction history
	 * @param interactionId
	 * @return
	 */
	@RequestMapping(value ="{interactionId}/cancel", method = RequestMethod.PATCH)
	public ResponseEntity<Interaction> cancelRequest(@PathVariable Integer interactionId){
		Interaction result = interactionService.cancel(interactionId, personService.getCurrentUser());
		return new ResponseEntity<Interaction>(result, null, HttpStatus.OK);
	}

	/**
	 * Confirm that the physical item has been handed over to the requesting
	 * user.
	 * 
	 * @param interactionId
	 * @return
	 */
	@RequestMapping(value = "/{interactionId}/handoverconfirm", method = RequestMethod.PATCH)
	public ResponseEntity<Interaction> confirmHandover(
			@PathVariable Integer interactionId) {
		Interaction result = interactionService.confirmHandover(interactionId,
				personService.getCurrentUser());
		return new ResponseEntity<Interaction>(result, null, HttpStatus.OK);
	}

	/**
	 * Confirm that the item has been physically returned to the owner.
	 * 
	 * @param interactionId
	 * @return
	 */
	@RequestMapping(value = "/{interactionId}/returnconfirm", method = RequestMethod.PATCH)
	public ResponseEntity<Interaction> confirmReturn(
			@PathVariable Integer interactionId) {
		Interaction result = interactionService.confirmReturn(interactionId,
				personService.getCurrentUser());
		return new ResponseEntity<Interaction>(result, null, HttpStatus.OK);
	}

	/**
	 * List the current users incoming requests (Interactions)
	 * 
	 * @return
	 */
	@ApiOperation("Returns interactions initiated by other users, targeting items owned by the user")
	@RequestMapping(value = "/incoming", method = RequestMethod.GET)
	public ResponseEntity<List<Interaction>> getIncomingRequests() {
		List<Interaction> result = interactionService
				.getUserIncomingInteractions(personService.getCurrentUser());
		return new ResponseEntity<List<Interaction>>(result, null,
				HttpStatus.OK);
	}

	/**
	 * Get a list of requests the user has made.
	 * 
	 * @return
	 */
	@ApiOperation(value = "Returns active interactions created by the user (requests)")
	@RequestMapping(value = "/outgoing", method = RequestMethod.GET)
	public ResponseEntity<List<Interaction>> getOutgoingRequests() {
		List<Interaction> result = interactionService.getOutgoing(personService
				.getCurrentUser());
		return new ResponseEntity<List<Interaction>>(result, null,
				HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<Interaction>> getInteractions(@RequestParam(value="param", required = false) String param){
		if (param!=null && param.equals("incoming")){
			return this.getIncomingRequests();
		} else if (param!=null && param.equals("outgoing")){
			return this.getOutgoingRequests();
		} else {
			List<Interaction> result = interactionService.getUserInteractions(personService.getCurrentUser());
			return new ResponseEntity<List<Interaction>>(result, null, HttpStatus.OK);
		}
	}
}
