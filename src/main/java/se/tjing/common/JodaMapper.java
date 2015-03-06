package se.tjing.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class JodaMapper extends ObjectMapper {
	public JodaMapper() {
		super();
		registerModule(new JodaModule());
	}
}
