package se.tjing.facebooktest;

import javax.inject.Inject;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/facebooktest")
public class FacebookTestController {

	private Facebook facebook;

	@Inject
	public FacebookTestController(Facebook facebook) {
		this.facebook = facebook;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public String test(@PathVariable String id) {
		System.out.println("Debug");
		System.out.println(facebook.getApplicationNamespace());
		return facebook.userOperations().getUserProfile(id).getUsername();
	}
}
