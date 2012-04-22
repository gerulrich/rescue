package jnlp.sample.jardiff;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JarSnapshots {
	
	private static final ConcurrentMap<String, String> versions = new ConcurrentHashMap<String, String>();
	
	public static String getVersion(String jarFile) {
		return versions.get(jarFile);
	}
	
	public static void putVersion(String jarFile, String version) {
		versions.put(jarFile, version);
	}
	
}
