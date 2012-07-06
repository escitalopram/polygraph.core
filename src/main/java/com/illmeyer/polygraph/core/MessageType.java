package com.illmeyer.polygraph.core;

import freemarker.core.Environment;

public interface MessageType extends Module {
	public Message createMessage(String messageText, Environment environment);
}
