package net.cloudengine.new_.cti;

import junit.framework.Assert;
import net.cloudengine.cti.utils.SingleCallEventListener;
import net.cloudengine.new_.cti.model.Call;
import net.cloudengine.new_.cti.model.asterisk.SinglePartyCallImpl;

import org.junit.Test;


public class SinglePartyCallTest {

	/**
	 * Test de creacion de llamada con un solo participante (IVR) con la
	 * secuencia de eventos generados mediante X-Lite.
	 * @throws Exception
	 */
	@Test
	public void singlePartyCreation_XLite() throws Exception {
		
		String fileName = TAPIDriverTest.class.getResource("single_party_call_xlite.txt").getFile();
		FileEventReaderDriver driver = new FileEventReaderDriver(fileName, false);

		SingleCallEventListener listener = new SingleCallEventListener();
		driver.createEventProvider().addListener(listener);
		
		driver.init();
		driver.close();
		
		Call call = listener.getCall();		
		
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", SinglePartyCallImpl.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "4002", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "*43", call.getCalledId());
		Assert.assertEquals("No se finalizo la llamada a partir de los eventos", true, listener.isTerminateCall());
	}
	
	/**
	 * Test de creacion de llamada con un solo participante (IVR) con la
	 * secuencia de eventos generados mediante Vippie en BlackBerry.
	 * @throws Exception
	 */
	@Test
	public void singlePartyCreation_VippieBB() {
		
		String fileName = TAPIDriverTest.class.getResource("single_party_call_vippie.txt").getFile();
		FileEventReaderDriver driver = new FileEventReaderDriver(fileName, false);

		SingleCallEventListener listener = new SingleCallEventListener();
		driver.createEventProvider().addListener(listener);
		
		driver.init();
		driver.close();		
		
		Call call = listener.getCall();
		
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", SinglePartyCallImpl.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "4003", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "*43", call.getCalledId());
		Assert.assertEquals("No se finalizo la llamada a partir de los eventos", true, listener.isTerminateCall());
	}
	
	/**
	 * Test de creacion de llamada con un solo participante (IVR) con la
	 * secuencia de eventos generados mediante Vippie en BlackBerry.
	 * @throws Exception
	 */
	@Test
	public void singlePartyCreation_Zoiper() throws Exception {
		
		String fileName = TAPIDriverTest.class.getResource("single_party_call_zoiper.txt").getFile();
		FileEventReaderDriver driver = new FileEventReaderDriver(fileName, false);

		SingleCallEventListener listener = new SingleCallEventListener();
		driver.createEventProvider().addListener(listener);
		
		driver.init();
		driver.close();		
		
		Call call = listener.getCall();
		
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", SinglePartyCallImpl.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "2001", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "*43", call.getCalledId());
		Assert.assertEquals("No se finalizo la llamada a partir de los eventos", true, listener.isTerminateCall());
	}

}
