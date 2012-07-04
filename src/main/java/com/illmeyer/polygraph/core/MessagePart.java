package com.illmeyer.polygraph.core;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class MessagePart {
	private byte[] message = null;
	private String encoding = null;
	private Map<String,Object> properties = new HashMap<String,Object>();
	
	public String getStringMessage() {
		try {
			return new String(message, encoding);
		} catch (Exception e) {
			return null;
		}
	}
	
	public void setStringMessage(String s) {
		try {
			setStringMessage(s,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// never happens
		}
	}
	public void setStringMessage(String message, String encoding) throws UnsupportedEncodingException {
		this.message=message.getBytes(encoding);
		this.encoding=encoding;
	}
}
