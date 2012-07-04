package com.illmeyer.polygraph.core;

import freemarker.template.TemplateHashModel;

public interface MessageType extends Module {
	public Message createMessage(String messageText, TemplateHashModel templateHashModel);
}
