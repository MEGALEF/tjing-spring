package se.tjing.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import se.tjing.entity.Person;
import se.tjing.store.PersonRepository;

@RestController
@RequestMapping("/user")
public class PersonController {
	
	@Autowired
	private PersonRepository personRepo;
	
	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Person> getUser(@PathVariable Integer userId){
		return new ResponseEntity<Person>(personRepo.findOne(userId), null, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Person> addUser(@RequestBody Person user){
		return new ResponseEntity(personRepo.save(user), null, HttpStatus.CREATED);
	}
	
}
