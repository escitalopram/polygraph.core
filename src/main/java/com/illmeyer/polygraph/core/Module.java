package com.illmeyer.polygraph.core;

import java.util.Map;

public interface Module {
	public void init();
	public Map<String,Object> createContext();
	public void destroy();
}
