package org.couettos.inisoft.view;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.AbstractTableModel;

import org.couettos.inisoft.controller.IController;
import org.couettos.inisoft.exceptions.PropertyAlreadyExistsException;
import org.couettos.inisoft.exceptions.UnknownPropertyException;
import org.couettos.inisoft.exceptions.UnknownSectionException;
import org.couettos.inisoft.model.AbstractData;
import org.couettos.inisoft.model.IniProperty;

/**
 * Custom Table model that - updates the model via the controller - represents
 * itself with the AbstractData model
 * 
 * @author a.couette
 * 
 */
@SuppressWarnings("serial")
public class PropertyTableModel extends AbstractTableModel {

	private Logger logger = Logger.getLogger(PropertyTableModel.class.getName());

	private static String[] COL_NAMES = { "key", "value" };

	List<Object[]> paramList = new ArrayList<Object[]>();

	private String currentSection;

	private AbstractData data;

	private final IController controller;

	public PropertyTableModel(IController controller) {
		this.controller = controller;
	}

	public void setData(AbstractData data) {
		this.data = data;
	}

	public void changeSection(String section) throws UnknownSectionException {
		currentSection = section;
		refreshData();
	}

	private void refreshData() throws UnknownSectionException {
		paramList = new ArrayList<Object[]>();
		if (currentSection != null) {
			for (IniProperty prop : data.getPropertyList(currentSection)) {
				Object[] row = new Object[2];
				row[0] = prop.getKey();
				row[1] = prop.getValue();
				paramList.add(row);
			}
		}
		fireTableDataChanged();
	}

	public String getColumnName(int col) {
		return COL_NAMES[col].toString();
	}

	public int getRowCount() {
		return paramList.size();
	}

	public int getColumnCount() {
		return COL_NAMES.length;
	}

	public Object getValueAt(int row, int col) {

		return paramList.get(row)[col];
	}

	public boolean isCellEditable(int row, int col) {
		return true;
	}

	/**
	 * binds the edited value to the model through the controller
	 */
	public void setValueAt(Object value, int row, int col) {

		Object oldValue = paramList.get(row)[col];

		try {
			IniProperty oldproperty = new IniProperty(paramList.get(row)[0].toString(), paramList.get(row)[1].toString());
			paramList.get(row)[col] = value;
			IniProperty newProperty = new IniProperty(paramList.get(row)[0].toString(), paramList.get(row)[1].toString());

			if (col == 0) {
				controller.addProperty(currentSection, newProperty);
				controller.removeProperty(currentSection, oldproperty);
			} else {
				controller.updateProperty(currentSection, newProperty);
			}
		} catch (UnknownSectionException e) {
			paramList.get(row)[col] = oldValue;
			logger.log(Level.SEVERE, e.getMessage());
		} catch (UnknownPropertyException e) {
			paramList.get(row)[col] = oldValue;
			logger.log(Level.SEVERE, e.getMessage());
		} catch (PropertyAlreadyExistsException e) {
			paramList.get(row)[col] = oldValue;
			logger.log(Level.SEVERE, e.getMessage());
		}
	}

}
