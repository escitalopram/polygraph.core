package com.illmeyer.polygraph.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import freemarker.cache.URLTemplateLoader;

@NoArgsConstructor
public class MessageGunTemplateLoader extends URLTemplateLoader {

	private static Pattern pathValidator = Pattern
			.compile("^/(?:(sys)|(?:(mt|tpl|ext)/([^/]+)))/(.+)$");

	File basedir;

	public MessageGunTemplateLoader(File basedir) {
		this.basedir = basedir;
	}

	@Override
	protected URL getURL(String name) {
		Matcher m = pathValidator.matcher(name);
		if (!m.matches())
			return null;

		boolean isSystem = (m.group(1) == null);
		String folder = m.group(2);
		String archive = m.group(3);
		String filename = m.group(4);

		String jarURL = null;

		try {
			if (isSystem) {
				jarURL = basedir.toURI().toURL().toString() + "/sys.jar!/vfs/" + filename;
			} else {
				jarURL = basedir.toURI().toURL().toString() + "/" +folder +"/"+ archive	+ ".jar!/vfs/" + filename;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
		try {
			return new URL(jarURL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
