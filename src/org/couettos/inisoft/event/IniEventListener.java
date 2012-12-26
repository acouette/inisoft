/**
 * 
 */
package org.couettos.inisoft.event;

import java.util.EventListener;
import java.util.EventObject;

/**
 * @author a.couette An event listener interface for ini model listeners
 */
public interface IniEventListener extends EventListener {

	/**
	 * method called by the observed ini model to notify that a section has
	 * changed
	 * 
	 * @param event
	 */
	void sectionChanged(EventObject event);

	/**
	 * method called by the observed ini model to notify that a property has
	 * changed
	 * 
	 * @param event
	 */
	void propertyChanged(EventObject event);

}
