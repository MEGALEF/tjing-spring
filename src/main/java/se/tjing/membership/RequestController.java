package se.tjing.membership;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.pool.PoolService;
import se.tjing.user.PersonService;

@RestController
@RequestMapping("/request")
public class RequestController {

	@Autowired
	PoolService poolService;

	@Autowired
	PersonService personService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<JoinRequest>> getPoolRequests() {
		List<JoinRequest> result = poolService
				.getRelevantRequests(personService.getCurrentUser());
		return new ResponseEntity<List<JoinRequest>>(result, null,
				HttpStatus.OK);
	}

	@RequestMapping(value = "{requestId}/approve", method = RequestMethod.POST)
	public ResponseEntity<Membership> approve(@PathVariable Integer requestId) {
		Membership membership = poolService.approveJoin(
				personService.getCurrentUser(), requestId);
		return new ResponseEntity<Membership>(membership, null,
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "{requestId}/deny", method = RequestMethod.POST)
	public ResponseEntity<Boolean> deny(@PathVariable Integer requestId) {
		Boolean result = poolService.denyJoin(personService.getCurrentUser(),
				requestId);
		return new ResponseEntity<Boolean>(result, null, HttpStatus.CREATED);
	}

}
