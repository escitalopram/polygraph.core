package com.illmeyer.polygraph.core.spi;

import com.illmeyer.polygraph.core.Gun;
import com.illmeyer.polygraph.core.interfaces.ComponentLifeCycle;

import freemarker.cache.TemplateLoader;

public interface GunConfigurator extends ComponentLifeCycle {
	/**
	 * 
	 * @return readily configured TemplateLoader, able to read module resources
	 */
	TemplateLoader getTemplateLoader();
	/**
	 * Populate Template Context with module variables
	 * @param g The Gun to be configured
	 */
	void registerModules(Gun g);
}
