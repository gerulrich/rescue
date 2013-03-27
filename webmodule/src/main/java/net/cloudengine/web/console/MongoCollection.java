package net.cloudengine.web.console;

import com.mongodb.CommandResult;

public class MongoCollection {
	private String name;
	private CommandResult command;

	public MongoCollection(String name, CommandResult command) {
		super();
		this.name = name;
		this.command = command;
	}

	public String getName() {
		return name;
	}

	public String getStorage() {
		return humanReadableByteCount(command.getLong("storageSize"), false);
	}
	
	public String getSize() {
		return humanReadableByteCount(command.getLong("size"), false);
	}
	
	public String getIndexes() {
		return command.getString("nindexes");
	}
	
	public String getIndexSize() {
		return humanReadableByteCount(command.getLong("totalIndexSize"),false);
	}
	
	public long getCount() {
		return command.getLong("count");
	}
	
	
	private String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    char pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1);
	    return String.format("%.1f %cB", bytes / Math.pow(unit, exp), pre);
	}
	
	
}