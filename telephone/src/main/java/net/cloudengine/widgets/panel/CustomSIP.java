package net.cloudengine.widgets.panel;

import javaforce.voip.CallDetails;
import javaforce.voip.SIPClient;

public class CustomSIP extends SIPClient {
	
	/**
	 * Sobre escribo el metodo ya que no se estaba actualizando
	 * el numero de sequencia del mensaje y asterisk no tomaba
	 * cuando se hacian varios holds en la misma llamada.
	 */
	public void buildsdp(CallDetails cd, CallDetails.SideDetails cdsd) {
		cdsd.cseq++;
		super.buildsdp(cd, cdsd);  
	}
	

}
