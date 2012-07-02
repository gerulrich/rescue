package net.cloudengine.new_.cti;

import net.cloudengine.new_.cti.asterisk.AsteriskEventProvider;

/**
 * {@link TAPIDriver} para test que lee los eventos de un archivo de
 * texto. Los eventos que lee del archivo son eventos de asterisk.
 * @author German Ulrich
 *
 */
public class FileEventReaderDriver implements TAPIDriver {

	private AsteriskEventProvider provider;
	private EventFileReader reader;
	
	public FileEventReaderDriver(String fileName, boolean realTime) {
		this.provider = new AsteriskEventProvider();
		this.reader = new EventFileReader(fileName, realTime);
		this.provider.connectionEstablished(reader);
	}
	
	@Override
	public EventProvider createEventProvider() {
		return provider;
	}

	@Override
	public boolean isConnected() {
		return true;
	}
	
	public void start() {
		reader.exec();
	}

}
