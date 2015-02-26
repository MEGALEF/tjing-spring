package se.tjing.membership;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/membership")
public class MembershipController {
	@Autowired
	MembershipService membershipService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Membership> addMembership(
			@RequestBody Membership membership) {
		Membership newMembership = membershipService.addMembership(membership);
		return new ResponseEntity<Membership>(newMembership, null,
				HttpStatus.CREATED);
	}
}
