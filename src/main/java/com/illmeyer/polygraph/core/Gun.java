package com.illmeyer.polygraph.core;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.cache.TemplateLoader;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is the central Class to polygraph. It pieces everything together and starts the main message generation loop.
 * @author escitalopram
 *
 */
@Data @AllArgsConstructor @NoArgsConstructor // @CommonsLog
public class Gun {
	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Gun.class);

	private AddressSupplier addressSupplier;
	private MessageDispatcher dispatcher;
	private String initialTemplate;
	private MessageType mt;
	private Object templateData;
	private TemplateLoader loader;
	private Map<String,Object> context = new HashMap<String,Object>();
	
	public void configure(GunConfigurator configurator) {
		configurator.registerModules(this);
		loader=configurator.getTemplateLoader();
	}
	
	/**
	 * Triggers the message Generation process.
	 * @throws IOException
	 */
	
	public void trigger() throws IOException {
		addressSupplier.initialize();
		dispatcher.initialize();
		Configuration conf = getConfiguration();
		Template tpl = conf.getTemplate(initialTemplate);
		context.put("tpl", templateData);
		while (addressSupplier.hasMoreElements()) {
			Address a=null;
			try {
				a=addressSupplier.nextElement();
				context.put("addr",a);
				StringWriter output = new StringWriter(4096);
				tpl.process(context, output);
				output.close();
				String result = output.toString();
				if(mt!=null) result=mt.postProcessMessage(result);
				dispatcher.dispatchMessage(result);
			} catch (TemplateException e) {
				log.error("Error processing address " + a.getAddrs().toString());
				
			} catch (Exception e) {
				log.error("Error processing address " + a.getAddrs().toString());
			}
		}
	}
	/**
	 * Generate default Freemarker Configuration object
	 * @return Freemarker Configuration Object
	 */
	protected Configuration getConfiguration() {
		Configuration result = new Configuration();
		
		result.setTemplateLoader(loader);
		return result;
	}
	
}
