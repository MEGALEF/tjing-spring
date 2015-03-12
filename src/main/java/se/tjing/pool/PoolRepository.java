package se.tjing.pool;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface PoolRepository extends CrudRepository<Pool, Integer> {

	@Query
	public List<Pool> findByTitle(String title);
}
