package se.tjing.pool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.item.Item;
import se.tjing.membership.Membership;
import se.tjing.user.Person;
import se.tjing.user.PersonService;

@RestController
@RequestMapping("/pool")
public class PoolController {
	@Autowired
	PoolService poolService;

	@Autowired
	PersonService personService;

	@RequestMapping(value = "/mine", method = RequestMethod.GET)
	public ResponseEntity<List<Pool>> getOwnPools() {
		List<Pool> result = poolService.getUsersPools(personService
				.getCurrentUser());
		return new ResponseEntity<List<Pool>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Pool> createPool(@RequestBody Pool pool) {
		Pool newPool = poolService
				.addPool(personService.getCurrentUser(), pool);
		return new ResponseEntity<Pool>(newPool, null, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{poolId}/join", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Membership> joinPool(@PathVariable Integer poolId) {
		Membership newMembership = poolService.joinPool(
				personService.getCurrentUser(), poolId);
		return new ResponseEntity<Membership>(newMembership, null,
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{poolId}/invite/{userId}", method = RequestMethod.POST)
	public ResponseEntity<Membership> inviteUserToPool(
			@PathVariable Integer poolId, @PathVariable Integer userId) {
		return null; // TODO: Not yet implemented;
	}

	@RequestMapping(value = "{poolId}", method = RequestMethod.GET)
	public ResponseEntity<Pool> getPool(@PathVariable Integer poolId) {
		Pool result = poolService.getPool(poolId);
		return new ResponseEntity<Pool>(result, null, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Pool>> listPools() {
		List<Pool> result = poolService.getPools();
		return new ResponseEntity<List<Pool>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{poolId}/items", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> listItemsInPool(
			@PathVariable Integer poolId) {
		Person currentUser = personService.getCurrentUser();
		List<Item> result = poolService.getItemsInPool(currentUser, poolId);
		return new ResponseEntity<List<Item>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/search/{searchString}", method = RequestMethod.GET)
	public ResponseEntity<List<Pool>> searchPools(
			@PathVariable String searchString) {
		List<Pool> result = poolService.search(searchString);
		return new ResponseEntity<List<Pool>>(result, null, HttpStatus.OK);
	}
}
