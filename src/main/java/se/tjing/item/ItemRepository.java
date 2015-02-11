package se.tjing.item;

import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

public interface ItemRepository extends CrudRepository<Item, Integer>,
		QueryDslPredicateExecutor<Item> {

}
