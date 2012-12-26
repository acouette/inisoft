package org.couettos.inisoft.event;

/**
 * interface that enables Listener to register/unregister to this Notifier
 * 
 * @author a.couette
 * 
 */
public interface IniEventNotifier {

	/**
	 * adds an event listener
	 * 
	 * @param listener
	 */
	void addEventListener(IniEventListener listener);

	/**
	 * removes an event listener
	 * 
	 * @param listener
	 */
	void removeEventListener(IniEventListener listener);

}
