package se.tjing.pool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pool")
public class PoolController {
	@Autowired
	PoolService poolService;

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Pool> createPool(@RequestBody Pool pool) {
		Pool newPool = poolService.addPool(pool);
		return new ResponseEntity<Pool>(newPool, null, HttpStatus.CREATED);
	}

}
