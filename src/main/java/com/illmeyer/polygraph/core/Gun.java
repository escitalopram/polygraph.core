package com.illmeyer.polygraph.core;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This is the central Class to polygraph. It pieces everything together and starts the main message generation loop.
 * @author escitalopram
 *
 */
@Data

@NoArgsConstructor // @CommonsLog
public class Gun implements ComponentLifeCycle {
	private static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Gun.class);

	private AddressSupplier addressSupplier;
	private MessageDispatcher dispatcher;
	private String initialTemplate;
	private MessageType mt;
	private TemplateDataProvider templateDataProvider;
	@Setter(AccessLevel.PRIVATE)
	private Object templateData;
	private TemplateLoader loader;
	private GunConfigurator configurator;
	@Setter(AccessLevel.PRIVATE)
	private Map<String,Object> context = new HashMap<String,Object>();
	
	/**
	 * Triggers the message Generation process.
	 * @throws IOException
	 */
	
	public void trigger() throws IOException {
		addressSupplier.reset();
		Configuration conf = getConfiguration();
		Template tpl = conf.getTemplate(initialTemplate);
		context.put("td", templateData);
		while (addressSupplier.hasMoreElements()) {
			Address a=null;
			try {
				a=addressSupplier.nextElement();
				context.put("addr",a);
				StringWriter output = new StringWriter(4096);
				Environment e = tpl.createProcessingEnvironment(context, output);
				e.process();
				output.close();
				String result = output.toString();
				Message m=mt.createMessage(result,e);
				dispatcher.dispatchMessage(m);
			} catch (TemplateException e) {
				log.error("Error processing address " + a.getAddrs().toString(), e);
				
			} catch (Exception e) {
				log.error("Error processing address " + a.getAddrs().toString(), e);
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

	@Override
	public void initialize() {
		if (configurator!=null) {
			configurator.registerModules(this);
			loader=configurator.getTemplateLoader();
		}
		addressSupplier.initialize();
		dispatcher.initialize();
		mt.initialize();
		if (templateDataProvider!=null) {
			templateDataProvider.initialize();
			templateData=templateDataProvider.getTemplateData();
		}
	}

	@Override
	public void destroy() {
		if (configurator!=null) configurator.destroy();
		addressSupplier.destroy();
		dispatcher.destroy();
		mt.destroy();
		if (templateDataProvider!=null)
			templateDataProvider.destroy();
	}
	
}
