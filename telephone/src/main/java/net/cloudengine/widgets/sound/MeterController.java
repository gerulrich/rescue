package net.cloudengine.widgets.sound;

/**
 * Interface para el envio de los niveles de audio a la gui.
 * Ademas tiene un método para indicar el estado del boton de speaker (on/off).
 */

public interface MeterController {
	
	void setMeterRec(int lvl);

	void setMeterPlay(int lvl);

	void setSpeakerStatus(boolean state);
}