package se.tjing.pool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PoolService {
	@Autowired
	PoolRepository poolRepo;

	public Pool addPool(Pool pool) {
		// TODO: Business logic I guess
		return poolRepo.save(pool);
	}

}
