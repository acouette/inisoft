package org.couettos.inisoft.factory;

import org.couettos.inisoft.controller.Controller;
import org.couettos.inisoft.controller.IController;
import org.couettos.inisoft.model.AbstractData;
import org.couettos.inisoft.model.IniData;

/**
 * groups concrete instantiations
 * 
 * @author a.couette
 * 
 */
public class IniFactory {

	public static AbstractData getModelInstance() {
		return new IniData();
	}

	public static IController getControllerInstance(AbstractData data) {
		return new Controller(data);
	}

}
