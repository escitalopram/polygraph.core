package com.illmeyer.polygraph.core;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import junit.framework.Assert;

import org.junit.Test;
import freemarker.cache.TemplateLoader;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.mock;

public class TestCore {
	@Test
	public void executeGunTest() throws IOException {
		
		Gun g = new Gun();
		g.setAddressSupplier(getMockedAddressSupplier());
		g.setDispatcher(getMockedMessageDispatcher());
		g.setInitialTemplate("tpl/test/main");
		g.setLoader(getMockedTemplateLoader());
		g.setTemplateData(null);
		g.initialize();
		g.trigger();
		g.destroy();
	}
	
	public AddressSupplier getMockedAddressSupplier() {
		Address a = new Address();
		a.getAddrs().put("email", "wolfgang@illmeyer.com");
		a.getFields().put("firstname","Wolfgang");
		
		AddressSupplier asup = mock(AddressSupplier.class);
		stub(asup.hasMoreElements()).toReturn(true).toReturn(false);
		stub(asup.nextElement()).toReturn(a);
		return asup;
	}
	
	public TemplateLoader getMockedTemplateLoader() throws IOException {
		TemplateLoader tl = mock(TemplateLoader.class);
		stub(tl.findTemplateSource("tpl/test/main")).toReturn("main");
		stub(tl.getReader("main", "UTF-8")).toReturn(new StringReader("Hallo ${addr.fields.firstname}!"));
		return tl;
	}
	
	public MessageDispatcher getMockedMessageDispatcher() {
		MessageDispatcher md= new MessageDispatcher() {
			private boolean initialized=false;
			public void initialize() {
				initialized=true;
			}
			
			public void dispatchMessage(String message) {
				Assert.assertTrue(initialized);
				Assert.assertEquals("Hallo Wolfgang!",message);
			}

			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}
		};
		return md;
	}
}
