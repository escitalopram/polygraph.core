package com.illmeyer.polygraph.core;

import java.util.Map;

public interface Module extends ComponentLifeCycle {
	public Map<String,Object> createContext();
}
