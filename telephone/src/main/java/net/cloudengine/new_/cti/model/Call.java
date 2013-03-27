package net.cloudengine.new_.cti.model;

import java.util.Date;

public interface Call {
	
	String getId();
	String getCallerId();
	String getCalledId();
	boolean isHold();
	Date getCreationDate();
	boolean isInQueue();

}
