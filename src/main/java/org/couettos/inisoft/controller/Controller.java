package org.couettos.inisoft.controller;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.couettos.inisoft.exceptions.PropertyAlreadyExistsException;
import org.couettos.inisoft.exceptions.SectionAlreadyExistsException;
import org.couettos.inisoft.exceptions.UnknownPropertyException;
import org.couettos.inisoft.exceptions.UnknownSectionException;
import org.couettos.inisoft.model.AbstractData;
import org.couettos.inisoft.model.IniProperty;
import org.couettos.inisoft.utils.IniFileUtil;

/**
 * The controller implementation
 * 
 * @author a.couette
 * 
 */
public class Controller implements IController {

	private Logger logger = Logger.getLogger(Controller.class.getName());

	private AbstractData data;

	public Controller(AbstractData data) {
		this.data = data;
		logger.log(Level.INFO, " constructed");
	}

	@Override
	public void addProperty(String section, IniProperty property) throws UnknownSectionException, PropertyAlreadyExistsException {
		logger.log(Level.INFO, " addProperty");
		data.addProperty(section, property);
	}

	@Override
	public void removeProperty(String section, IniProperty property) throws UnknownSectionException, UnknownPropertyException {
		logger.log(Level.INFO, " removeProperty");
		data.removeProperty(section, property);
	}

	@Override
	public void addSection(String section) throws SectionAlreadyExistsException {
		logger.log(Level.INFO, " addSection");
		data.addSection(section);
	}

	@Override
	public void removeSection(String section) throws UnknownSectionException {
		logger.log(Level.INFO, " removeSection");
		data.removeSection(section);
	}

	@Override
	public void updateProperty(String section, IniProperty property) throws UnknownSectionException, UnknownPropertyException {
		logger.log(Level.INFO, " updateProperty");
		data.updateProperty(section, property);
	}

	@Override
	public void save(String filePath) throws IOException {
		logger.log(Level.INFO, " save");
		IniFileUtil.saveData(data, filePath);
	}

	@Override
	public AbstractData loadDataFromFile(File file) throws IOException {
		logger.log(Level.INFO, " loadDataFromFile");
		data = IniFileUtil.getDataFromFile(file);
		return data;
	}

	@Override
	public void renameSection(String oldValue, String newValue) throws UnknownSectionException, SectionAlreadyExistsException {
		logger.log(Level.INFO, " renameSection");
		data.renameSection(oldValue, newValue);
	}

}
