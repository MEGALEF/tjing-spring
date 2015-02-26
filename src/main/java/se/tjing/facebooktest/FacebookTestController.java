package se.tjing.facebooktest;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facebooktest")
public class FacebookTestController {
	@Inject
	Facebook facebook;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<FacebookProfile> getUser(@PathVariable String id) {
		FacebookProfile profile = facebook.userOperations().getUserProfile(id);
		return new ResponseEntity<FacebookProfile>(profile, null, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<FacebookProfile> getUser() {
		FacebookProfile profile = facebook.userOperations().getUserProfile();
		return new ResponseEntity<FacebookProfile>(profile, null, HttpStatus.OK);
	}
}
