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

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Membership>> getMemberships() {
		List<Membership> result; 
		result = poolService.getUserMemberships(personService.getCurrentUser());
		
		return new ResponseEntity<List<Membership>>(result, null,
				HttpStatus.OK);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Membership> join(@RequestBody PostMembership body){ //TODO PostMembership is a workaround because serialization problems with username
		Membership newMembership = membershipService.createMembership(personService.getCurrentUser(), body);
		return new ResponseEntity<Membership>(newMembership, null, HttpStatus.OK);
	}

	@RequestMapping(value = "{requestId}", method = RequestMethod.PATCH)
	public ResponseEntity<Membership> update(@PathVariable Integer requestId, @RequestBody Membership toUpdate) {
		Membership result = poolService.update(
				personService.getCurrentUser(), requestId, toUpdate);
		return new ResponseEntity<Membership>(result, null,
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "{requestId}", method = RequestMethod.DELETE)
	public ResponseEntity<Object> deleteMembership(@PathVariable Integer requestId) {
		poolService.removeMembership(personService.getCurrentUser(),
				requestId);
		return new ResponseEntity<Object>(null, null, HttpStatus.OK);
	}

}
