package com.illmeyer.polygraph.core.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Message implements Serializable {
	private static final long serialVersionUID = 8010235785726935071L;
	private final String MessageType;
	private final Map<String,MessagePart> parts = new HashMap<String,MessagePart>();
	private final Map<String,Object> properties = new HashMap<String,Object>();

}
