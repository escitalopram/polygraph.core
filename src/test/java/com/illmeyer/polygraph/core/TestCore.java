package com.illmeyer.polygraph.core;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.template.TemplateHashModel;
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
		g.setMt(getMockedMessageType());
		g.initialize();
		g.trigger();
		g.destroy();
	}
	
	private MessageType getMockedMessageType() {
		return new MessageType() {
			boolean initialized=false;
			@Override
			public void initialize() {
				initialized=true;
			}
			
			@Override
			public void destroy() {
			}
			
			@Override
			public Map<String, Object> createContext() {
				return null;
			}
			
			@Override
			public Message createMessage(String messageText,
					Environment environment) {
				Assert.assertTrue(initialized);
				Message result = new Message("test");
				MessagePart mp = new MessagePart();
				mp.setStringMessage(messageText);
				result.getParts().put("main", mp);
				return result;
			}
		};
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
		stub(tl.getReader("main", "UTF-8")).toReturn(new StringReader("<#assign x=\"hallo\">Hallo ${addr.fields.firstname}!"));
		return tl;
	}
	
	public MessageDispatcher getMockedMessageDispatcher() {
		MessageDispatcher md= new MessageDispatcher() {
			private boolean initialized=false;
			public void initialize() {
				initialized=true;
			}
			
			public void dispatchMessage(Message message) {
				Assert.assertTrue(initialized);
				Assert.assertEquals("Hallo Wolfgang!",message.getParts().get("main").getStringMessage());
			}

			@Override
			public void destroy() {
				// TODO Auto-generated method stub
				
			}
		};
		return md;
	}
}
