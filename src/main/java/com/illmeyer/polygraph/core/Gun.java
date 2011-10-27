package com.illmeyer.polygraph.core;

import freemarker.template.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Gun {
	private AddressSupplier addressSupplier;
	private MessageDispatcher dispatcher;
	private String initialTemplate;
	private TemplateData templateData;

	public void trigger() {
		addressSupplier.initialize();
		dispatcher.initialize();
		Configuration conf = getConfiguration();
		
	}
	protected Configuration getConfiguration() {
		Configuration result = new Configuration();
		result.setTemplateLoader(new MessageGunTemplateLoader());
		return result;
	}
	protected MessageGunTemplateLoader getTemplateLoader() {
		return new MessageGunTemplateLoader();
	}
}
