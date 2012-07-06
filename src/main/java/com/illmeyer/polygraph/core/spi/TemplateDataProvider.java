package com.illmeyer.polygraph.core.spi;

import com.illmeyer.polygraph.core.interfaces.ComponentLifeCycle;

public interface TemplateDataProvider extends ComponentLifeCycle {
	public Object getTemplateData();
}
