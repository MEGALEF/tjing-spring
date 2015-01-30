package se.tjing.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Something went wrong..")
public class TjingException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	public TjingException(String msg){
		super(msg);
	}
}
