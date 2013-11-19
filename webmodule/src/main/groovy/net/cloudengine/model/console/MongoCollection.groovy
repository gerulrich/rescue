package net.cloudengine.model.console

import net.cloudengine.util.HexString;

import com.mongodb.CommandResult

class MongoCollection {
	
	String name;
	CommandResult command;

	MongoCollection(String name, CommandResult command) {
		super();
		this.name = name;
		this.command = command;
	}

	String getStorage() {
		return humanReadableByteCount(command.getLong("storageSize"));
	}
	
	String getSize() {
		return humanReadableByteCount(command.getLong("size"));
	}
	
	String getEncodedName() {
		return HexString.encode(name);
	}
	
	String getIndexes() {
		return command.getString("nindexes");
	}
	
	String getIndexSize() {
		return humanReadableByteCount(command.getLong("totalIndexSize"));
	}
	
	long getCount() {
		return command.getLong("count");
	}
	
	
	String humanReadableByteCount(long bytes) {
		int unit = 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		char pre = "KMGTPE".charAt(exp-1);
		return String.format("%.1f %cB", bytes / Math.pow(unit, exp), pre);
	}

}
