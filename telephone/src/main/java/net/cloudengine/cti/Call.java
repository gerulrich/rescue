package net.cloudengine.cti;

import java.util.Date;

/**
 * Representa una llamada telefonica.
 * @author German Ulrich
 *
 */
public interface Call {
	
	String getId();
	String getCallerId();
	String getCalledId();
	boolean isHold();
	Date creationDate();
	
}
