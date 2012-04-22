package net.cloudengine.cti;

import junit.framework.Assert;
import net.cloudengine.cti.asterisk.SinglePartyCall;
import net.cloudengine.cti.utils.AsteriskTestModule;
import net.cloudengine.cti.utils.EventGenerator;
import net.cloudengine.cti.utils.SingleCallListener;

import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class SinglePartyCallTest {
	
	/**
	 * Test de creacion de llamada con un solo participante (IVR) con la
	 * secuencia de eventos generados mediante X-Lite.
	 * @throws Exception
	 */
	@Test
	public void singlePartyCreation_XLite() throws Exception {
		String fileName = TwoPartyCallTest.class.getResource("single_party_call_xlite.txt").getFile();
		EventGenerator generator = new EventGenerator(fileName);
		
		SingleCallListener listener = new SingleCallListener();
		
		Injector injector = Guice.createInjector(new AsteriskTestModule(generator));
		CallsMonitor monitor = injector.getInstance(CallsMonitor.class);
		monitor.addListener(listener);
		
		generator.start();
		generator.join();
		
		Call call = listener.getCall();
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", SinglePartyCall.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "4002", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "*43", call.getCalledId());
		Assert.assertEquals("No se finalizo la llamada a partir de los eventos", true, listener.isHungupCall());
	}
	
	/**
	 * Test de creacion de llamada con un solo participante (IVR) con la
	 * secuencia de eventos generados mediante Vippie en BlackBerry.
	 * @throws Exception
	 */
	@Test
	public void singlePartyCreation_VippieBB() throws Exception {
		String fileName = TwoPartyCallTest.class.getResource("single_party_call_vippie.txt").getFile();
		EventGenerator generator = new EventGenerator(fileName);
		
		SingleCallListener listener = new SingleCallListener();
		
		Injector injector = Guice.createInjector(new AsteriskTestModule(generator));
		CallsMonitor monitor = injector.getInstance(CallsMonitor.class);
		monitor.addListener(listener);
		
		generator.start();
		generator.join();
		
		Call call = listener.getCall();
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", SinglePartyCall.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "4003", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "*43", call.getCalledId());
		Assert.assertEquals("No se finalizo la llamada a partir de los eventos", true, listener.isHungupCall());
	}
	
	/**
	 * Test de creacion de llamada con un solo participante (IVR) con la
	 * secuencia de eventos generados mediante Vippie en BlackBerry.
	 * @throws Exception
	 */
	@Test
	public void singlePartyCreation_Zoiper() throws Exception {
		String fileName = TwoPartyCallTest.class.getResource("single_party_call_zoiper.txt").getFile();
		EventGenerator generator = new EventGenerator(fileName);
		
		SingleCallListener listener = new SingleCallListener();
		
		Injector injector = Guice.createInjector(new AsteriskTestModule(generator));
		CallsMonitor monitor = injector.getInstance(CallsMonitor.class);
		monitor.addListener(listener);
		
		generator.start();
		generator.join();
		
		Call call = listener.getCall();
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", SinglePartyCall.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "2001", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "*43", call.getCalledId());
		Assert.assertEquals("No se finalizo la llamada a partir de los eventos", true, listener.isHungupCall());
	}

}
