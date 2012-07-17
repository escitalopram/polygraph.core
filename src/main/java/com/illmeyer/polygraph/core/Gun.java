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

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.StringWriter;

import com.illmeyer.polygraph.core.data.Address;
import com.illmeyer.polygraph.core.data.Message;
import com.illmeyer.polygraph.core.interfaces.ComponentLifeCycle;
import com.illmeyer.polygraph.core.spi.AddressSupplier;
import com.illmeyer.polygraph.core.spi.GunConfigurator;
import com.illmeyer.polygraph.core.spi.MessageDispatcher;
import com.illmeyer.polygraph.core.spi.MessageType;
import com.illmeyer.polygraph.core.spi.TemplateDataProvider;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import lombok.AccessLevel;
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
	@Setter(AccessLevel.NONE)
	private Object templateData;
	private TemplateLoader loader;
	private GunConfigurator configurator;
	@Setter(AccessLevel.NONE)
	private Map<String,Object> context = new HashMap<String,Object>();
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private Template template = null;
	
	/**
	 * Triggers the message Generation process.
	 * @throws IOException
	 */
	
	public void trigger() throws IOException {
		addressSupplier.reset();
		Address a=null;
		while (addressSupplier.hasMoreElements()) {
			try {
				a=addressSupplier.nextElement();
				Message m = createMessage(a);
				if (m==null) continue;
				dispatcher.dispatchMessage(m);
			} catch (TemplateException e) {
				log.error("Error processing address " + a.getAddrs().toString(), e);
				
			} catch (Exception e) {
				log.error("Error processing address " + a.getAddrs().toString(), e);
			}
		}
	}
	public Message createMessage(Address a) throws IOException, TemplateException {
			context.put("addr",a);
			StringWriter output = new StringWriter(4096);
			Environment e = template.createProcessingEnvironment(context, output);
			e.process();
			output.close();
			// if (e.skip) return null;
			String tplresult = output.toString();
			return mt.createMessage(tplresult,e);
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
		if (templateDataProvider!=null) {
			templateDataProvider.initialize();
			templateData=templateDataProvider.getTemplateData();
		}
		try {
			prepareTemplate();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void prepareTemplate() throws IOException {
		Configuration conf = getConfiguration();
		template = conf.getTemplate(initialTemplate);
		context.put("td", templateData);
	}
	
	@Override
	public void destroy() {
		if (configurator!=null) configurator.destroy();
		addressSupplier.destroy();
		dispatcher.destroy();
		if (templateDataProvider!=null)
			templateDataProvider.destroy();
	}
	
}
