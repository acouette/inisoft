package org.couettos.inisoft;

import java.util.Locale;

import javax.swing.SwingUtilities;

import org.couettos.inisoft.controller.IController;
import org.couettos.inisoft.factory.IniFactory;
import org.couettos.inisoft.model.AbstractData;
import org.couettos.inisoft.view.View;

/**
 * 
 * @author a.couette App Launcher Takes no param
 */
public class Launcher {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Locale.setDefault(Locale.ENGLISH);
				AbstractData data = IniFactory.getModelInstance();
				IController controller = IniFactory.getControllerInstance(data);
				View view = new View(controller);
				view.setObject(data);
				view.setVisible(true);
			}
		});
	}
}