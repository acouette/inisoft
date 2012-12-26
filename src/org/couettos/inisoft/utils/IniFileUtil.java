package org.couettos.inisoft.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.couettos.inisoft.exceptions.PropertyAlreadyExistsException;
import org.couettos.inisoft.exceptions.SectionAlreadyExistsException;
import org.couettos.inisoft.exceptions.UnknownSectionException;
import org.couettos.inisoft.factory.IniFactory;
import org.couettos.inisoft.model.AbstractData;
import org.couettos.inisoft.model.IniProperty;

/**
 * Util class that handle AbstractData to File conversions
 * 
 * @author a.couette
 * 
 */
public class IniFileUtil {

	private static String CARRIAGE_RETURN = "\r\n";

	/**
	 * builds a data object from File
	 * 
	 * @param file
	 * @return data model generated
	 * @throws IOException
	 */
	public static AbstractData getDataFromFile(File file) throws IOException {

		AbstractData data = IniFactory.getModelInstance();

		BufferedReader br = new BufferedReader(new FileReader(file));
		String s = null;
		String currentSection = AbstractData.ROOT_SECTION;

		while ((s = br.readLine()) != null) {
			s = s.trim();
			if ("".equals(s) || CARRIAGE_RETURN.equals(s)) {
				continue;
			}
			if (s.startsWith("[") && s.endsWith("]")) {
				currentSection = s.substring(1, s.length() - 1);
				try {
					data.addSection(currentSection);
				} catch (SectionAlreadyExistsException e) {
					e.printStackTrace();
					throw new IOException(e.getMessage());
				}
			} else {

				if (s.indexOf("=") < 1) {
					throw new IOException("Property '" + s + "' does not contain an '=' character at the right spot");
				}

				String[] splittedProperty = s.split("=");
				String propertyValue = "";
				if (splittedProperty.length > 1) {
					propertyValue = splittedProperty[1].trim();
				}
				IniProperty prop = new IniProperty(splittedProperty[0].trim(), propertyValue);
				try {
					data.addProperty(currentSection, prop);
				} catch (UnknownSectionException e) {
					e.printStackTrace();
					throw new IOException(e.getMessage());
				} catch (PropertyAlreadyExistsException e) {
					e.printStackTrace();
					throw new IOException(e.getMessage());
				}
			}
		}

		br.close();

		return data;

	}

	/**
	 * Saves the data model to a given file
	 * 
	 * @param data
	 * @param filePath
	 * @throws IOException
	 */
	public static void saveData(AbstractData data, String filePath) throws IOException {

		if (filePath == null || "".equals(filePath.trim())) {
			throw new IOException("Incorrect path '" + filePath + "'");
		}
		if (!filePath.endsWith(".ini")) {
			filePath = filePath + ".ini";
		}

		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			FileWriter fw = new FileWriter(file);

			List<String> sections = data.getSectionList();
			for (String section : sections) {
				if (!AbstractData.ROOT_SECTION.equals(section)) {
					fw.write("[" + section + "]");
					fw.write(CARRIAGE_RETURN);
				}
				List<IniProperty> props = data.getPropertyList(section);

				for (IniProperty prop : props) {
					fw.write(prop.getKey() + "=" + prop.getValue());
					fw.write(CARRIAGE_RETURN);
				}
				fw.write(CARRIAGE_RETURN);
			}
			fw.flush();
			fw.close();

		} catch (UnknownSectionException e) {
			e.printStackTrace();
		}
	}

}
