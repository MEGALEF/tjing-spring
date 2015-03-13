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

@RestController
@RequestMapping("/user")
public class PersonController {

	@Autowired
	PersonService pService;

	@Autowired
	InteractionService interactionService;

	@Autowired
	ItemService itemService;

	@Autowired
	PoolService poolService;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<String> getCurrentUser() {
		String name;
		name = pService.getCurrentUser().getFirstName();
		return new ResponseEntity<String>(name, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/search/{searchStr}", method = RequestMethod.GET)
	public ResponseEntity<List<Person>> searchUser(
			@PathVariable String searchStr) {
		List<Person> result = pService.search(searchStr);
		return new ResponseEntity<List<Person>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "{userId}", method = RequestMethod.GET)
	public ResponseEntity<Person> getUser(@PathVariable Integer userId) {
		Person user = pService.getPerson(userId);
		return new ResponseEntity<Person>(user, null, HttpStatus.OK);
	}
}
