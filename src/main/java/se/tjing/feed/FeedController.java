package se.tjing.feed;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.user.PersonService;

@RestController
@RequestMapping("/feed")
public class FeedController {
	@Autowired
	FeedService feedService;
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<List<? extends FeedEvent>> getFeed(){
		List<? extends FeedEvent> result = feedService.getFeed(personService.getCurrentUser());
		return new ResponseEntity<List<? extends FeedEvent>>(result, null, HttpStatus.OK);
	}
}
