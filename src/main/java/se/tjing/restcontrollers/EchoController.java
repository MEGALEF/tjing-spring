package se.tjing.restcontrollers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/echo")
public class EchoController {

	@RequestMapping(value="/{echoThis}", method=RequestMethod.GET)
	@ResponseBody
	public String getPerson(@PathVariable String echoThis){
		return echoThis;
	}
	
}
