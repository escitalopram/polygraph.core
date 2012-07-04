package com.illmeyer.polygraph.core;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Message {

	private Map<String,MessagePart> parts = new HashMap<String,MessagePart>();
	private Map<String,Object> properties = new HashMap<String,Object>();

}
