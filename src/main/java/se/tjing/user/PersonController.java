package se.tjing.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class PersonController {

	@Autowired
	PersonService pService;

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Person> getUser(@PathVariable Integer userId) {
		return new ResponseEntity<Person>(pService.getPerson(userId), null,
				HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Person> addUser(@RequestBody Person user) {
		Person addedUser = pService.addPerson(user);
		return new ResponseEntity<Person>(addedUser, null, HttpStatus.CREATED);
	}

}
