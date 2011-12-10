package com.illmeyer.polygraph.core;

import freemarker.cache.TemplateLoader;

public interface GunConfigurator {
	/**
	 * Initialize Modules, Check Dependencies
	 */
	void init();
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
	void destroy();
}
