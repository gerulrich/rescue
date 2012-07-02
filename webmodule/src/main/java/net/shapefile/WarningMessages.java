package net.shapefile;

import java.util.ArrayList;

public class WarningMessages {
	
	private ArrayList<String> messages = new ArrayList<String>();

	public ArrayList<String> getMessages() {
		return messages;
	}

	public String getMessage(int index) {
		return messages.get(index);
	}

	public void addMessage(String message) {
		messages.add(message);
	}
}