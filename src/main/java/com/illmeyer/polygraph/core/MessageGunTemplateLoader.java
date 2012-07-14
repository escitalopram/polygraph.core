package com.illmeyer.polygraph.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.illmeyer.polygraph.core.interfaces.Module;
import com.illmeyer.polygraph.core.spi.Extension;
import com.illmeyer.polygraph.core.spi.MessageType;
import com.illmeyer.polygraph.core.spi.Template;

import lombok.NoArgsConstructor;
import freemarker.cache.URLTemplateLoader;

/**
 * Implements the template loading mechanism using the polygraph directory structure
 * @author escitalopram
 *
 */
@NoArgsConstructor
public class MessageGunTemplateLoader extends URLTemplateLoader {
	
	enum ModuleType {
		mt,
		tpl,
		ext
	}

	private static Pattern pathValidator = Pattern
			.compile("^(?:(sys)|(?:(mt|tpl|ext)/([^/]+)))(/.+)$");

	Map<String, Module> modules;

	public MessageGunTemplateLoader(Map<String, Module> modules) {
		this.modules = modules;
	}

	@Override
	protected URL getURL(String name) {
		
		Matcher m = pathValidator.matcher(name);
		if (!m.matches())
			return null;

		boolean isSystem = (m.group(1) != null);
		ModuleType moduletype = ModuleType.valueOf(m.group(2));
		String modulename = m.group(3);
		String file = m.group(4);
		
		if (isSystem) {
			modulename="com.illmeyer.polygraph.syslib.Syslib";
		} else {
			Module mod = modules.get(modulename);
			if(moduletype==ModuleType.ext && !(mod instanceof Extension)) return null;
			if(moduletype==ModuleType.mt && !(mod instanceof MessageType)) return null;
			if(moduletype==ModuleType.tpl && !(mod instanceof Template)) return null;
		}
		
		Module mod=modules.get(modulename);
		URL base = mod.getClass().getProtectionDomain().getCodeSource().getLocation();
		URL result=null;
		try {
			result = new URL("jar:"+base+"!/vfs"+file);
			System.out.println("Looking up "+ result);
		} catch (MalformedURLException e) {
		}
		try {
			result.openStream().close();
		} catch (Exception e) {
			return null;
		}
		return result;
	}
}
