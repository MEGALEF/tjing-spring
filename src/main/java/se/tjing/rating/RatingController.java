package se.tjing.rating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.user.PersonService;

@RestController
@RequestMapping("/rating")
public class RatingController {
	
	@Autowired
	RatingService ratingService;
	
	@Autowired
	PersonService personService;
	
	@RequestMapping(value="", method = RequestMethod.POST)
	public ResponseEntity<Rating> addRating(@RequestBody AddRating rating){
		Rating result = ratingService.rate(personService.getCurrentUser(),
				rating);
		return new ResponseEntity<Rating>(result, null, HttpStatus.CREATED);
	}

}
