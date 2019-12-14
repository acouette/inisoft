package org.couettos.inisoft.exceptions;

@SuppressWarnings("serial")
public class UnknownPropertyException extends Exception {

	public UnknownPropertyException(String section, String property) {
		super("Property with name '" + property + "' does not exist in section '" + section + "'");
	}

}
