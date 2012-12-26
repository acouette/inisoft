package org.couettos.inisoft.exceptions;

@SuppressWarnings("serial")
public class SectionAlreadyExistsException extends Exception {

	public SectionAlreadyExistsException(String section) {
		super("Section with name '" + section + "' already exists in the data model");
	}

}
