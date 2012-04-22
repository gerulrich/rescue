package net.cloudengine.cti;

import junit.framework.Assert;
import net.cloudengine.cti.asterisk.TwoPartyCall;
import net.cloudengine.cti.utils.AsteriskTestModule;
import net.cloudengine.cti.utils.EventGenerator;
import net.cloudengine.cti.utils.SingleCallListener;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TwoPartyCallTest {
	
	@Test
	public void testCall() throws Exception {
		String fileName = TwoPartyCallTest.class.getResource("two_party_call.txt").getFile();
		EventGenerator generator = new EventGenerator(fileName);
		
		SingleCallListener listener = new SingleCallListener();
		
		Injector injector = Guice.createInjector(new AsteriskTestModule(generator));
		CallsMonitor monitor = injector.getInstance(CallsMonitor.class);
		monitor.addListener(listener);
		
		generator.start();
		generator.join();
		
		Call call = listener.getCall();
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", TwoPartyCall.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "4003", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "4002", call.getCalledId());
		Assert.assertEquals("No se finalizo la llamada a partir de los eventos", true, listener.isHungupCall());
	}
	
	@Test
	public void testCall2() throws Exception {
		String fileName = TwoPartyCallTest.class.getResource("two_party_call_2.txt").getFile();
		EventGenerator generator = new EventGenerator(fileName);
		
		SingleCallListener listener = new SingleCallListener();
		
		Injector injector = Guice.createInjector(new AsteriskTestModule(generator));
		CallsMonitor monitor = injector.getInstance(CallsMonitor.class);
		monitor.addListener(listener);
		
		generator.start();
		generator.join();
		
		Call call = listener.getCall();
		
		Assert.assertNotNull("Creacion de la lamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", TwoPartyCall.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "4002", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "4003", call.getCalledId());
		Assert.assertEquals("No se finalizo la llamada a partir de los eventos", true, listener.isHungupCall());

	}
}
