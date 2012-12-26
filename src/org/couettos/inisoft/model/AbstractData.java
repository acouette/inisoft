package org.couettos.inisoft.model;

import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.event.EventListenerList;

import org.couettos.inisoft.event.IniEventListener;
import org.couettos.inisoft.event.IniEventNotifier;
import org.couettos.inisoft.exceptions.PropertyAlreadyExistsException;
import org.couettos.inisoft.exceptions.SectionAlreadyExistsException;
import org.couettos.inisoft.exceptions.UnknownPropertyException;
import org.couettos.inisoft.exceptions.UnknownSectionException;

public abstract class AbstractData implements IniEventNotifier {

	public static final String ROOT_SECTION = "UNBOUND PROPERTIES";

	private Logger logger = Logger.getLogger(AbstractData.class.getName());

	// EventListenerList has no direct relation with SWING even if its in
	// javax.swing.event.
	// Therefore its ok to reference it in the controller
	private EventListenerList listeners = new EventListenerList();

	/**
	 * retrieves a property list by section name
	 * 
	 * @param section
	 * @return the list of properties
	 * @throws UnknownSectionException
	 */
	public abstract List<IniProperty> getPropertyList(String section) throws UnknownSectionException;

	/**
	 * retrieves all section lists
	 * 
	 * @return the list of sections
	 */
	public abstract List<String> getSectionList();

	/**
	 * add a property to the section
	 * 
	 * @param section
	 * @param property
	 * @throws UnknownSectionException
	 * @throws PropertyAlreadyExistsException
	 */
	public abstract void addProperty(String section, IniProperty property) throws UnknownSectionException, PropertyAlreadyExistsException;

	/**
	 * removes a property from the section
	 * 
	 * @param section
	 * @param property
	 * @throws UnknownSectionException
	 * @throws UnknownPropertyException
	 */
	public abstract void removeProperty(String section, IniProperty property) throws UnknownSectionException, UnknownPropertyException;

	/**
	 * updates a property in the section
	 * 
	 * @param section
	 * @param property
	 * @throws UnknownSectionException
	 * @throws UnknownPropertyException
	 */
	public abstract void updateProperty(String section, IniProperty property) throws UnknownSectionException, UnknownPropertyException;

	/**
	 * adds a sections
	 * 
	 * @param section
	 * @throws SectionAlreadyExistsException
	 */
	public abstract void addSection(String section) throws SectionAlreadyExistsException;

	/**
	 * removes a section
	 * 
	 * @param section
	 * @throws UnknownSectionException
	 */
	public abstract void removeSection(String section) throws UnknownSectionException;

	/**
	 * renames a section
	 * 
	 * @param oldValue
	 * @param newValue
	 * @throws UnknownSectionException
	 * @throws SectionAlreadyExistsException
	 */
	public abstract void renameSection(String oldValue, String newValue) throws UnknownSectionException, SectionAlreadyExistsException;

	public abstract void clear();

	@Override
	public void addEventListener(IniEventListener listener) {
		listeners.add(IniEventListener.class, listener);
	}

	@Override
	public void removeEventListener(IniEventListener listener) {
		listeners.remove(IniEventListener.class, listener);
	}

	/**
	 * notifies every listener that ini key/value have been modified in the
	 * model
	 */
	protected void firePropertyChangeEvent() {
		logger.log(Level.INFO, " firePropertyChangeEvent");
		for (IniEventListener listener : listeners.getListeners(IniEventListener.class)) {
			listener.propertyChanged(new EventObject(this));
		}
	}

	/**
	 * notifies every listener that ini sections have been modified in the model
	 */
	protected void fireSectionChangeEvent() {
		logger.log(Level.INFO, " fireSectionChangeEvent");
		for (IniEventListener listener : listeners.getListeners(IniEventListener.class)) {
			listener.sectionChanged(new EventObject(this));
		}
	}

}