package com.illmeyer.polygraph.core.spi;

import com.illmeyer.polygraph.core.interfaces.ExtensionDependent;
import com.illmeyer.polygraph.core.interfaces.Module;

public interface Template extends Module, ExtensionDependent {
	String getMainTemplatePath();
	String getMessageType();
}
