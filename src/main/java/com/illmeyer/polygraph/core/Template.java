package com.illmeyer.polygraph.core;

import java.util.List;

public interface Template extends Module {
	String getMainTemplatePath();
	String getMessageType();
	List<String> getRequiredExtensions();
}
