package org.couettos.inisoft.controller;

import java.io.File;
import java.io.IOException;

import org.couettos.inisoft.exceptions.PropertyAlreadyExistsException;
import org.couettos.inisoft.exceptions.SectionAlreadyExistsException;
import org.couettos.inisoft.exceptions.UnknownPropertyException;
import org.couettos.inisoft.exceptions.UnknownSectionException;
import org.couettos.inisoft.model.AbstractData;
import org.couettos.inisoft.model.IniProperty;

/**
 * 
 * @author a.couette Controller interface that : - performs modification on it's
 *         model - saves it's model - loads it's model
 */
public interface IController {

	/**
	 * Adds a property to the section
	 * 
	 * @param section
	 * @param property
	 * @throws UnknownSectionException
	 * @throws PropertyAlreadyExistsException
	 */
	void addProperty(String section, IniProperty property) throws UnknownSectionException, PropertyAlreadyExistsException;

	/**
	 * removes a property from the session
	 * 
	 * @param section
	 * @param property
	 * @throws UnknownSectionException
	 * @throws UnknownPropertyException
	 */
	void removeProperty(String section, IniProperty property) throws UnknownSectionException, UnknownPropertyException;

	/**
	 * updates a property value
	 * 
	 * @param section
	 * @param property
	 * @throws UnknownSectionException
	 * @throws UnknownPropertyException
	 */
	void updateProperty(String section, IniProperty property) throws UnknownSectionException, UnknownPropertyException;

	/**
	 * Adds a section
	 * 
	 * @param section
	 * @throws SectionAlreadyExistsException
	 */
	void addSection(String section) throws SectionAlreadyExistsException;

	/**
	 * removes a section
	 * 
	 * @param section
	 * @throws UnknownSectionException
	 */
	void removeSection(String section) throws UnknownSectionException;

	/**
	 * renames a section
	 * @param oldValue
	 * @param newValue
	 * @throws UnknownSectionException
	 * @throws SectionAlreadyExistsException
	 */
	void renameSection(String oldValue, String newValue) throws UnknownSectionException, SectionAlreadyExistsException;

	/**
	 * saves the model to the filepath
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	void save(String filePath) throws IOException;

	/**
	 * loads the model from a file
	 * 
	 * @param file
	 * @return the generated data
	 * @throws IOException
	 */
	AbstractData loadDataFromFile(File file) throws IOException;
}
