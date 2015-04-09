package se.tjing.itemrequest;

import org.springframework.data.repository.CrudRepository;

public interface ItemRequestRepository extends
		CrudRepository<ItemRequest, Integer> {
	
}
