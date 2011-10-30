package com.illmeyer.polygraph.core;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.cache.TemplateLoader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Gun {
	private AddressSupplier addressSupplier;
	private MessageDispatcher dispatcher;
	private String initialTemplate;
	private Object templateData;
	private File baseDir;
	private TemplateLoader loader;
	
	public void trigger() throws IOException {
		addressSupplier.initialize();
		dispatcher.initialize();
		Configuration conf = getConfiguration();
		Template tpl = conf.getTemplate(initialTemplate);
		Map<String,Object> root = getContext();
		while (addressSupplier.hasMoreElements()) {
			try {
				root.put("addr",addressSupplier.nextElement());
				StringWriter output = new StringWriter(4096);
				tpl.process(root, output);
				output.close();
				dispatcher.dispatchMessage(output.toString());
			} catch (TemplateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	protected Configuration getConfiguration() {
		Configuration result = new Configuration();
		
		result.setTemplateLoader(loader);
		return result;
	}
	
	protected Map<String,Object> getContext() {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("tpl", templateData);
		return result;
	}
}
