package net.cloudengine.service.admin;

/**
 * Esta clase representa un valor de configuración de la aplicación.
 * Cada opción de configuración está dada por un nombre y un valor.
 * @author German Ulrich
 *
 */
public class ConfigOption {
	
	private String name;
	private String value;
	
	public ConfigOption(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}

}
