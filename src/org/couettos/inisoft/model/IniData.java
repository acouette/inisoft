package org.couettos.inisoft.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.couettos.inisoft.exceptions.PropertyAlreadyExistsException;
import org.couettos.inisoft.exceptions.SectionAlreadyExistsException;
import org.couettos.inisoft.exceptions.UnknownPropertyException;
import org.couettos.inisoft.exceptions.UnknownSectionException;

/**
 * The model implementation
 * 
 * @author a.couette
 * 
 */
public class IniData extends AbstractData {

	private Map<String, List<IniProperty>> sections;

	private Logger logger = Logger.getLogger(IniData.class.getName());

	public IniData() {
		super();
		this.sections = new HashMap<String, List<IniProperty>>();
		this.sections.put(ROOT_SECTION, new ArrayList<IniProperty>());
		logger.log(Level.INFO, " constructed");
	}

	@Override
	public List<IniProperty> getPropertyList(String section) throws UnknownSectionException {
		logger.log(Level.INFO, " getPropertyList");

		if (!sections.containsKey(section)) {
			throw new UnknownSectionException(section);
		}
		List<IniProperty> properties = sections.get(section);
		Collections.sort(properties);
		return properties;
	}

	@Override
	public List<String> getSectionList() {
		logger.log(Level.INFO, " getSectionList");
		if (sections.isEmpty()) {
			return new ArrayList<String>();
		}
		List<String> sectionList = new ArrayList<String>(sections.keySet());
		Collections.sort(sectionList, new StringComparator());
		return sectionList;
	}

	@Override
	public void addProperty(String section, IniProperty property) throws UnknownSectionException, PropertyAlreadyExistsException {
		logger.log(Level.INFO, " addProperty");
		if (!sections.containsKey(section)) {
			throw new UnknownSectionException(section);
		}
		if (sections.get(section).contains(property)) {
			throw new PropertyAlreadyExistsException(section, property.getKey());
		}
		sections.get(section).add(property);
		firePropertyChangeEvent();
	}

	@Override
	public void removeProperty(String section, IniProperty property) throws UnknownSectionException, UnknownPropertyException {
		logger.log(Level.INFO, " removeProperty");
		if (!sections.containsKey(section)) {
			throw new UnknownSectionException(section);
		}
		if (!sections.get(section).contains(property)) {
			throw new UnknownPropertyException(section, property.getKey());
		}
		Iterator<IniProperty> iter = sections.get(section).iterator();
		while (iter.hasNext()) {
			if (iter.next().getKey().equals(property.getKey())) {
				iter.remove();
			}
		}
		firePropertyChangeEvent();
	}

	@Override
	public void addSection(String section) throws SectionAlreadyExistsException {
		logger.log(Level.INFO, " addSection");
		if (sections.containsKey(section)) {
			throw new SectionAlreadyExistsException(section);
		}
		sections.put(section, new ArrayList<IniProperty>());
		fireSectionChangeEvent();
	}

	@Override
	public void removeSection(String section) throws UnknownSectionException {
		logger.log(Level.INFO, " removeSection");
		if (!sections.containsKey(section)) {
			throw new UnknownSectionException(section);
		}
		sections.remove(section);
		fireSectionChangeEvent();
	}

	@Override
	public void updateProperty(String section, IniProperty property) throws UnknownSectionException, UnknownPropertyException {
		logger.log(Level.INFO, " updateProperty");
		if (!sections.containsKey(section)) {
			throw new UnknownSectionException(section);
		}
		if (!sections.get(section).contains(property)) {
			throw new UnknownPropertyException(section, property.getKey());
		}
		sections.get(section).get(sections.get(section).indexOf(property)).setValue(property.getValue());
		firePropertyChangeEvent();
	}

	public void renameSection(String oldValue, String newValue) throws UnknownSectionException, SectionAlreadyExistsException {
		logger.log(Level.INFO, " renameSection");
		if (!sections.containsKey(oldValue)) {
			throw new UnknownSectionException(oldValue);
		}
		if (sections.containsKey(newValue)) {
			throw new SectionAlreadyExistsException(newValue);
		}
		List<IniProperty> propList = sections.get(oldValue);
		sections.remove(oldValue);
		sections.put(newValue, propList);
		fireSectionChangeEvent();
		firePropertyChangeEvent();
	}

	@Override
	public void clear() {
		this.sections = new HashMap<String, List<IniProperty>>();
		this.sections.put(ROOT_SECTION, new ArrayList<IniProperty>());
		fireSectionChangeEvent();
		firePropertyChangeEvent();
	}

}
