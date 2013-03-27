package net.cloudengine.new_.cti.asterisk;

import net.cloudengine.new_.cti.model.asterisk.AbstractCallAsteriskImpl;
import net.cloudengine.new_.cti.model.asterisk.CallCreator;

public interface EventContextProvider {
	
	void addCall(AbstractCallAsteriskImpl call);
	void removeCall(AbstractCallAsteriskImpl call);
		
	void addCreator(CallCreator creator);
	void removeCreator(CallCreator creator);
	
	void changeCall(AbstractCallAsteriskImpl call);

}
