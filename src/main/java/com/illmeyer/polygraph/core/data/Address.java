package com.illmeyer.polygraph.core.data;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * Represents a recipient dataset. Accessible to the template as ${addr}. You can subclass it and add other fields & methods if you like 
 * @author escitalopram
 */
@Data
public class Address {
	/**
	 * represents addressable endpoints (like e-mail address, telephone number, etc.) suitable for sending the generated message.
	 */
	@Setter(AccessLevel.PRIVATE)
	private Map<String,Object> addrs = new HashMap<String, Object>();
	/**
	 * represents other informations about the recipient, e.g. name, etc.
	 */
	@Setter(AccessLevel.PRIVATE)
	private Map<String,Object> fields = new HashMap<String, Object>();
}
