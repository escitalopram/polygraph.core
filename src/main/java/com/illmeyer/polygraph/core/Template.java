package com.illmeyer.polygraph.core;

public interface Template extends Module, ExtensionDependent {
	String getMainTemplatePath();
	String getMessageType();
}
