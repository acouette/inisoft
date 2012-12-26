package org.couettos.inisoft.exceptions;

@SuppressWarnings("serial")
public class PropertyAlreadyExistsException extends Exception {

	public PropertyAlreadyExistsException(String section, String key) {
		super("Property with key '" + key + "' already exists in section '" + section + "'");
	}

}
