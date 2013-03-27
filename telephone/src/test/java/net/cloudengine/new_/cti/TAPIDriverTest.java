package net.cloudengine.new_.cti;

import junit.framework.Assert;
import net.cloudengine.cti.utils.SingleCallEventListener;
import net.cloudengine.new_.cti.model.Call;
import net.cloudengine.new_.cti.model.asterisk.TwoPartyCallImpl;

import org.junit.Test;

public class TAPIDriverTest {
	
	@Test
	public void testTwoPartyCall1() {
		String fileName = TAPIDriverTest.class.getResource("two_party_call.txt").getFile();
		FileEventReaderDriver driver = new FileEventReaderDriver(fileName, false);

		SingleCallEventListener listener = new SingleCallEventListener();
		driver.createEventProvider().addListener(listener);
		driver.init();
		driver.close();
		
		Call call = listener.getCall();
		
		Assert.assertTrue("No se establecio la llamada con el servidor", listener.isConnected());
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", TwoPartyCallImpl.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "4003", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "4002", call.getCalledId());
		
		Assert.assertTrue("No se finalizo la llamada a partir de los eventos", listener.isTerminateCall());
		Assert.assertTrue("No se efectuó la desconexión con el servidor", listener.isDisconected());
	}
	
	@Test
	public void testTwoPartyCall2() {
		String fileName = TAPIDriverTest.class.getResource("two_party_call_2.txt").getFile();
		FileEventReaderDriver driver = new FileEventReaderDriver(fileName, false);

		SingleCallEventListener listener = new SingleCallEventListener();
		driver.createEventProvider().addListener(listener);
		driver.init();
		driver.close();
		
		Call call = listener.getCall();
		
		Assert.assertTrue("No se establecio la llamada con el servidor", listener.isConnected());
		Assert.assertNotNull("No se genero la llamada a partir de los eventos", call);
		Assert.assertEquals("Llamada entre dos participantes", TwoPartyCallImpl.class, call.getClass());		
		Assert.assertEquals("CallerId incorrecto", "4002", call.getCallerId());
		Assert.assertEquals("CallerId incorrecto", "4003", call.getCalledId());
		Assert.assertTrue("No se finalizo la llamada a partir de los eventos", listener.isTerminateCall());
		Assert.assertTrue("No se efectuó la desconexión con el servidor", listener.isDisconected());
		
	}

}
