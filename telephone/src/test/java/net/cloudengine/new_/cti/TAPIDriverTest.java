package net.cloudengine.new_.cti;

import junit.framework.Assert;
import net.cloudengine.cti.TwoPartyCallTest;

import org.junit.Test;

public class TAPIDriverTest {
	
	@Test
	public void test1() throws Exception {
		String fileName = TwoPartyCallTest.class.getResource("two_party_call.txt").getFile();
		FileEventReaderDriver driver = new FileEventReaderDriver(fileName, false);
		driver.start();
		Assert.assertTrue("No se establecio la llamada con el servidor", driver.isConnected());
	}

}
