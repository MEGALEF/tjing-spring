package se.tjing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.tjing.entity.Person;
import se.tjing.exception.TjingException;
import se.tjing.store.PersonRepository;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepo;
	
	public Person addPerson(Person person){
		if (personRepo.findByEmail(person.getEmail())!=null){
			throw new TjingException("User with that email already exists");
		}
		personRepo.save(person);
		return person;
	}
	
	public Person getPerson(Integer userId){
		Person result = personRepo.findOne(userId);
		if (result == null){
			throw new TjingException("User with that id does not exist");
		} else {
			return result;
		}
	}
	
}
