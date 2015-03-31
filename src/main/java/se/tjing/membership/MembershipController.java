package se.tjing.membership;

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

import se.tjing.pool.PoolService;
import se.tjing.user.PersonService;

@RestController
@RequestMapping("/membership")
public class MembershipController {

	@Autowired
	PoolService poolService;

	@Autowired
	PersonService personService;
	
	@Autowired
	MembershipService membershipService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<Membership>> getPoolRequests(@RequestParam(value="pending", defaultValue="false") Boolean pending) {
		List<Membership> result; 
		if (pending) {
			result = poolService.getPendingMemberships(personService.getCurrentUser());
		} else {
			result = poolService.getUserMemberships(personService.getCurrentUser());
		}
		return new ResponseEntity<List<Membership>>(result, null,
				HttpStatus.OK);
	}
	
	@RequestMapping(value="", method=RequestMethod.POST)
	public ResponseEntity<Membership> join(@RequestBody AddMembership membership){
		Membership newMembership = membershipService.addMembership(personService.getCurrentUser(), membership);
		return new ResponseEntity<Membership>(newMembership, null, HttpStatus.OK);
		
	}

	@RequestMapping(value = "{requestId}", method = RequestMethod.PATCH)
	public ResponseEntity<Membership> approve(@PathVariable Integer requestId) {
		Membership membership = poolService.approveJoin(
				personService.getCurrentUser(), requestId);
		return new ResponseEntity<Membership>(membership, null,
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "{requestId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deny(@PathVariable Integer requestId) {
		Boolean result = poolService.denyJoin(personService.getCurrentUser(),
				requestId);
		return new ResponseEntity<Object>(null, null, HttpStatus.CREATED);
	}

}
