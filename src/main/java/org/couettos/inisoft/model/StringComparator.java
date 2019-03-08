package org.couettos.inisoft.model;

import java.util.Comparator;

/**
 * Comparator used to sort sections
 * 
 * @author a.couette
 * 
 */
public class StringComparator implements Comparator<String> {

	@Override
	public int compare(String o1, String o2) {
		if (AbstractData.ROOT_SECTION.equals(o1)) {
			return -1;
		}
		if (AbstractData.ROOT_SECTION.equals(o2)) {
			return 1;
		}
		return o1.toLowerCase().compareToIgnoreCase(o2);
	}

}
