package org.couettos.inisoft.exceptions;

@SuppressWarnings("serial")
public class UnknownSectionException extends Exception {

	public UnknownSectionException(String section) {
		super("Section with name '" + section + "' does not exist in the data model");
	}

}
