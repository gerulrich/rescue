package net.cloudengine.pbx;

/**
 * Representa una cola de la central telefónica.
 * @author German Ulrich
 *
 */
public class CTIQueue {
	
	private String number;

	public CTIQueue(String number) {
		super();
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

}
