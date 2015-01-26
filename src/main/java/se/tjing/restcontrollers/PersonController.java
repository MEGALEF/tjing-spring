package se.tjing.restcontrollers;

import org.springframework.beans.factory.annotation.Autowired;
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
	public Person getUser(@PathVariable Integer userId){
		return personRepo.findOne(userId);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public Person addUser(@RequestBody Person user){
		return personRepo.save(user);
	}
	
}
