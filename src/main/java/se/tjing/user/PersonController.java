package se.tjing.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.interaction.InteractionService;
import se.tjing.item.ItemService;
import se.tjing.pool.PoolService;
import se.tjing.rating.Rating;
import se.tjing.rating.RatingService;

import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/user")
public class PersonController {

	@Autowired
	PersonService pService;

	@Autowired
	InteractionService interactionService;

	@Autowired
	RatingService ratingservice;

	@Autowired
	ItemService itemService;

	@Autowired
	PoolService poolService;

	@ApiOperation("Returns information on the current user")
	@RequestMapping(value="/me", method = RequestMethod.GET)
	public ResponseEntity<Person> getCurrentUser() {
		Person person = pService.getCurrentUser();
		return new ResponseEntity<Person>(person, null, HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<List<Person>> getVisibleUsers(){
		Person current = pService.getCurrentUser();
		List<Person> result = pService.getVisibleUsers(current);
		return new ResponseEntity<List<Person>>(result, null, HttpStatus.OK);
	}

	@ApiOperation("Search for users based on the users names. Returns a list of matching users")
	@RequestMapping(value = "/search/{searchStr}", method = RequestMethod.GET)
	public ResponseEntity<List<Person>> searchUser(
			@PathVariable String searchStr) {
		List<Person> result = pService.search(searchStr);
		return new ResponseEntity<List<Person>>(result, null, HttpStatus.OK);
	}

	@ApiOperation("Returns information on a specific user")
	@RequestMapping(value = "{userId}", method = RequestMethod.GET)
	public ResponseEntity<Person> getUser(@PathVariable Integer userId) {
		Person user = pService.getPerson(userId);
		return new ResponseEntity<Person>(user, null, HttpStatus.OK);
	}

	@ApiOperation("Returns a list of all ratings the user plays a part in, either as item owner or borrower")
	@RequestMapping(value = "{userId}/rating", method = RequestMethod.GET)
	public ResponseEntity<List<Rating>> getRatings(@PathVariable Integer userId) {
		List<Rating> result = ratingservice.getRatings(pService
				.getPerson(userId));
		return new ResponseEntity<List<Rating>>(result, null, HttpStatus.OK);
	}
}
