package se.tjing.pool;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.GroupMembership;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.item.Item;
import se.tjing.membership.Membership;
import se.tjing.share.Share;
import se.tjing.user.Person;
import se.tjing.user.PersonService;

@RestController
@RequestMapping("/pool")
public class PoolController {
	@Autowired
	PoolService poolService;

	@Autowired
	PersonService personService;

	private ResponseEntity<List<Pool>> getOwnPools() {
		List<Pool> result = poolService.getUsersPools(personService
				.getCurrentUser());
		return new ResponseEntity<List<Pool>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Pool> createPool(@RequestBody Pool pool) {
		Pool newPool = poolService
				.createPool(personService.getCurrentUser(), pool);
		return new ResponseEntity<Pool>(newPool, null, HttpStatus.CREATED);
	}

	@RequestMapping(value = "{poolId}", method = RequestMethod.GET)
	public ResponseEntity<Pool> getPool(@PathVariable Integer poolId) {
		Pool result = poolService.getPool(poolId);
		return new ResponseEntity<Pool>(result, null, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Pool>> getPools(@RequestParam(value="q", required=false) String searchStr, @RequestParam(value="param", required=false) String param) {
		if ("mine".equals(param)){
			return this.getOwnPools();
		} else if (searchStr!= null && !searchStr.isEmpty()){
			return this.searchPools(searchStr);
		} //TODO: Handle multiple parameters at the same time
		List<Pool> result = poolService.getPools();
		return new ResponseEntity<List<Pool>>(result, null, HttpStatus.OK);
	}

	@RequestMapping(value = "/{poolId}/shares", method = RequestMethod.GET)
	public ResponseEntity<List<Share>> listItemsInPool(
			@PathVariable Integer poolId) {
		Person currentUser = personService.getCurrentUser();
		List<Share> result = poolService.getPoolShares(currentUser, poolId);
		return new ResponseEntity<List<Share>>(result, null, HttpStatus.OK);
	}
	
	@RequestMapping(value="/{poolId}/memberships", method = RequestMethod.GET)
	public ResponseEntity<List<Membership>> getPoolMemberships(@PathVariable Integer poolId, @RequestParam(value="approved", required=false) Boolean approved){
		List<Membership> result;
		if (approved != null && approved.booleanValue() == false){
			result = poolService.getPendingMemberships(personService.getCurrentUser(), poolId);
		}
		else {
			result = poolService.getPoolMemberships(personService.getCurrentUser(), poolId);
		}		
		
		return new ResponseEntity<List<Membership>>(result, null, HttpStatus.OK);
	}

	private ResponseEntity<List<Pool>> searchPools(String searchString) {
		List<Pool> result = poolService.searchPools(searchString);
		return new ResponseEntity<List<Pool>>(result, null, HttpStatus.OK);
	}
	
	@RequestMapping(value="/import/facebook")
	public ResponseEntity<List<GroupMembership>> getFacebookGroups(){
		List<GroupMembership> result = poolService.getFacebookGroups(personService.getCurrentUser());
		
		return new ResponseEntity<List<GroupMembership>>(result, null, HttpStatus.OK);
	}
	
	@RequestMapping(value="/import/facebook", method=RequestMethod.POST)
	public ResponseEntity<Membership> importFacebookGroup(@RequestBody FacebookGroup gm){
		Membership result = poolService.importFbGroup(personService.getCurrentUser(), gm);
		return new ResponseEntity<Membership>(result, null, HttpStatus.CREATED);
	}
}
