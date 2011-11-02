package com.illmeyer.polygraph.core;

import freemarker.cache.TemplateLoader;

public interface GunConfigurator {
	void init();
	TemplateLoader getTemplateLoader();
	void registerModules(Gun g);
	void destroy();
}
