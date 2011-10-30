package com.illmeyer.polygraph.core;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class Address {
	@Setter(AccessLevel.PRIVATE)
	private Map<String,Object> addrs = new HashMap<String, Object>();
	@Setter(AccessLevel.PRIVATE)
	private Map<String,Object> fields = new HashMap<String, Object>();
}
