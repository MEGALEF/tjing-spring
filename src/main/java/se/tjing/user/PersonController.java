package se.tjing.user;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.interaction.Interaction;
import se.tjing.interaction.InteractionService;
import se.tjing.item.Item;
import se.tjing.item.ItemService;
import se.tjing.pool.Pool;
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

	@RequestMapping(value = "/items/borrowed", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> getBorrowedItems() {
		List<Item> result = itemService.getUsersBorrowedItems(pService
				.getCurrentUser());
		return new ResponseEntity<List<Item>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/interactions/incoming", method = RequestMethod.GET)
	public ResponseEntity<List<Interaction>> getIncomingRequests() {
		List<Interaction> result = interactionService
				.getUserIncomingInteractions(pService.getCurrentUser());
		return new ResponseEntity<List<Interaction>>(result, null,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/interactions/outgoing", method = RequestMethod.GET)
	public ResponseEntity<Set<Interaction>> getOutgoingRequests() {
		Set<Interaction> result = interactionService.getOutgoing(pService
				.getCurrentUser());
		return new ResponseEntity<Set<Interaction>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/items", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> getOwnItems() {
		Person currentUser = pService.getCurrentUser();
		List<Item> result = itemService.getUsersItems(currentUser);
		return new ResponseEntity<List<Item>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/pools", method = RequestMethod.GET)
	public ResponseEntity<List<Pool>> getOwnPools() {
		List<Pool> result = poolService.getUsersPools(currentUser());
		return new ResponseEntity<List<Pool>>(result, null, HttpStatus.OK);
	}

	private Person currentUser() {
		return pService.getCurrentUser();
	}
}
