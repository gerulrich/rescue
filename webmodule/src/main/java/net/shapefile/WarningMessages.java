package net.shapefile;

import java.util.ArrayList;
import java.util.List;

public class WarningMessages {
	
	private List<String> messages = new ArrayList<String>();

	public List<String> getMessages() {
		return messages;
	}

	public String getMessage(int index) {
		return messages.get(index);
	}

	public void addMessage(String message) {
		messages.add(message);
	}
}