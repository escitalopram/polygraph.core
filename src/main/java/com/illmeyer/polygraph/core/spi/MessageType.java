package com.illmeyer.polygraph.core.spi;

import com.illmeyer.polygraph.core.data.Message;
import com.illmeyer.polygraph.core.interfaces.Module;

import freemarker.core.Environment;

public interface MessageType extends Module {
	public Message createMessage(String messageText, Environment environment);
}
