package se.tjing.store;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import se.tjing.entity.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
	@Query 
	Person findByEmail(String email);
}
