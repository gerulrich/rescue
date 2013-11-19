package net.cloudengine.model.console

import java.util.ArrayList;
import java.util.List;

import net.cloudengine.util.HexString;

class Table {
	
	String name;
	String engine;
	long rows;
	long size;
	long indexSize;
	
	List<Column> columns = new ArrayList<Column>();
	
	String getIndexSizeHumanReadable() {
		return humanReadableByteCount(indexSize,false);
	}
	
	String getSizeHumanReadable() {
		return humanReadableByteCount(size,false);
	}
	
	public String getEncodedName() {
		return HexString.encode(name);
	}
	
	private String humanReadableByteCount(long bytes, boolean si) {
		int unit = si ? 1000 : 1024;
		if (bytes < unit) return bytes + " B";
		int exp = (int) (Math.log(bytes) / Math.log(unit));
		char pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1);
		return String.format("%.1f %cB", bytes / Math.pow(unit, exp), pre);
	}
}
