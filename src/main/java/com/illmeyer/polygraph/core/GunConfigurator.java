package com.illmeyer.polygraph.core;

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
