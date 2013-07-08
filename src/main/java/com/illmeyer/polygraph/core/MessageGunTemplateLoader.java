/*
This file is part of the Polygraph bulk messaging framework
Copyright (C) 2012 Wolfgang Illmeyer

The Polygraph framework is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

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
	public URL getURL(String name) {
		
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
