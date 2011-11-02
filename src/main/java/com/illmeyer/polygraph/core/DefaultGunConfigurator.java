package com.illmeyer.polygraph.core;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.extern.apachecommons.CommonsLog;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import freemarker.cache.TemplateLoader;

//@CommonsLog // ?!?
public class DefaultGunConfigurator implements GunConfigurator {
	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(DefaultGunConfigurator.class);
	private File basedir;

	private Map<String,Extension> extensions = new HashMap<String, Extension>();
	private Map<String,Template> templates = new HashMap<String,Template>();
	private Map<String,MessageType> messageTypes = new HashMap<String,MessageType>();
	
	private TemplateLoader templateLoader;

	public DefaultGunConfigurator(File basedir) {
		this.basedir=basedir;
	}
	
	public void init() {
		Map<String,Module> modules = new HashMap<String,Module>();

		Reflections ref = new Reflections(
				new ConfigurationBuilder()
				.setUrls(ClasspathHelper.forJavaClassPath())
				.setScanners(new SubTypesScanner())
		);
		Set<Class<? extends Module>> moduleClasses = ref.getSubTypesOf(Module.class);
		for(Class<? extends Module> mc : moduleClasses) {
			try {
				Module m = mc.newInstance();
				modules.put(m.getClass().getName(),m);
			} catch (InstantiationException e) {
				log.error("Couldn't instantiate module "+mc.getName(),e);
			} catch (IllegalAccessException e) {
				log.error("Couldn't instantiate module "+mc.getName(),e);
			}
		}
		registerModules(modules);
		templateLoader = new MessageGunTemplateLoader(basedir);
	}

	private void registerModules(Map<String,Module> modules) {
		for(Entry<String, Module> e : modules.entrySet()) {
			if (e.getValue() instanceof MessageType) {
				e.getValue().init();
				messageTypes.put(e.getKey(),(MessageType)e.getValue());				
			}
		}
		for(Entry<String, Module> e : modules.entrySet()) {
			if (e.getValue() instanceof Extension) {
				extensions.put(e.getKey(),(Extension)e.getValue());
				e.getValue().init();
			}
		}
		for(Entry<String,Module> e : modules.entrySet()) {
			if (e.getValue() instanceof Template) {
				Template t = (Template) e.getValue();
				boolean satisfied = true;
				for (String re : t.getRequiredExtensions()) {
					if (!extensions.containsKey(re)) {
						log.error("Unsatisfied dependency: Template " + e.getKey() + " requires extension " + re);
						satisfied=false;
					}
				}
				if (t.getMessageType()!=null && !templates.containsKey(t.getMessageType())) {
					log.error("Unsatisfied depencency: Template" + e.getKey() + " requires message type "+ t.getMessageType());
					satisfied=false;
				}
				if (satisfied) {
					templates.put(e.getKey(),t);
					e.getValue().init();
				}
			}
		}
		if (templates.isEmpty()) log.error("No usable templates found");
		if (messageTypes.isEmpty()) log.error("No usable message types found");
	}


	public TemplateLoader getTemplateLoader() {
		return templateLoader;
	}

	public void registerModules(Gun g) {
		for (Map<String,Module> modules : new Map[]{messageTypes,extensions,templates})
		for(Entry<String, Module> e: modules.entrySet()) {
			g.getContext().put(e.getKey(), e.getValue().createContext());
		}
	}

	public void destroy() {
		for (Map<String,Module> modules : new Map[]{messageTypes,extensions,templates})
		for (Module m:modules.values()) {
			m.destroy();
		}
	}

}
