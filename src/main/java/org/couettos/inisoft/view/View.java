package org.couettos.inisoft.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.Customizer;
import java.io.File;
import java.io.IOException;
import java.util.EventObject;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.couettos.inisoft.controller.IController;
import org.couettos.inisoft.event.IniEventListener;
import org.couettos.inisoft.exceptions.PropertyAlreadyExistsException;
import org.couettos.inisoft.exceptions.SectionAlreadyExistsException;
import org.couettos.inisoft.exceptions.UnknownPropertyException;
import org.couettos.inisoft.exceptions.UnknownSectionException;
import org.couettos.inisoft.model.AbstractData;
import org.couettos.inisoft.model.IniProperty;

/**
 * The app's main frame
 * 
 * @author a.couette
 * 
 */
@SuppressWarnings("serial")
public class View extends JFrame implements IniEventListener, Customizer {

	private IController controller;

	private AbstractData data;

	private JList sectionList;

	private PropertyTableModel propertyTableModel;

	private JTextField addPropertyText;

	private String currentSection = null;

	private DefaultListModel model = new DefaultListModel();

	private static final int DEFAULT_HEIGHT = 25;

	private static final Color BACKGROUND_COLOR = new Color(235, 255, 255);

	private Logger logger = Logger.getLogger(View.class.getName());

	public View(IController controller) {
		this.controller = controller;
		initComponents();
		logger.log(Level.INFO, " constructed");
	}

	/**
	 * Big (too big?) method that initialize every graphical components and give
	 * them a behavior
	 */
	private void initComponents() {

		Border border = new LineBorder(Color.black, 1, true);

		/******* BEGIN SECTION PANEL ************/

		JPanel mainSectionPanel = new JPanel(new BorderLayout(20, 20));
		mainSectionPanel.setPreferredSize(new Dimension(200, 0));

		// List of sections
		sectionList = new JList(new String[0]);
		sectionList.setModel(model);
		// Add section panel
		final JButton addSectionButton = new JButton("add");
		addSectionButton.setEnabled(false);
		final JTextField addSectionText = new JTextField();
		addSectionText.setPreferredSize(new Dimension(120, DEFAULT_HEIGHT));
		FlowLayout trailingFlow = new FlowLayout();
		trailingFlow.setAlignment(FlowLayout.TRAILING);
		JPanel addSectionPanel = new JPanel(trailingFlow);
		addSectionPanel.setPreferredSize(new Dimension(200, 35));
		addSectionPanel.add(addSectionText);
		addSectionPanel.add(addSectionButton);
		addSectionPanel.setBackground(BACKGROUND_COLOR);

		// rename section
		final JButton renameSection = new JButton("rename");
		renameSection.setEnabled(false);
		renameSection.setPreferredSize(new Dimension(90, 35));

		// remove section
		final JButton removeSection = new JButton("remove");
		removeSection.setEnabled(false);
		removeSection.setPreferredSize(new Dimension(90, 35));

		JPanel editSectionPanel = new JPanel(trailingFlow);
		editSectionPanel.add(renameSection);
		editSectionPanel.add(removeSection);
		editSectionPanel.setBackground(BACKGROUND_COLOR);

		// Gobal section Panel
		mainSectionPanel.add(addSectionPanel, BorderLayout.PAGE_START);
		mainSectionPanel.add(sectionList, BorderLayout.CENTER);
		mainSectionPanel.add(editSectionPanel, BorderLayout.PAGE_END);
		mainSectionPanel.setBorder(BorderFactory.createTitledBorder(border, "Sections"));
		mainSectionPanel.setBackground(BACKGROUND_COLOR);

		/******* END SECTION PANEL ************/

		/******* START SECTION PROPERTY PANEL ********/

		JPanel propertyPanel = new JPanel(new BorderLayout(20, 20));
		Dimension propertyDimension = new Dimension(200, 550);
		propertyPanel.setPreferredSize(propertyDimension);
		propertyPanel.setBorder(BorderFactory.createTitledBorder(border, "Section Properties"));

		// Add property
		final JButton addPropertyButton = new JButton("add");
		addPropertyButton.setEnabled(false);

		addPropertyText = new JTextField();
		addPropertyText.setPreferredSize(new Dimension(120, DEFAULT_HEIGHT));
		addPropertyText.setEnabled(false);

		final JPanel addPropertyPanel = new JPanel();
		addPropertyPanel.setLayout(trailingFlow);
		addPropertyPanel.setPreferredSize(new Dimension(200, 35));
		addPropertyPanel.add(addPropertyText);
		addPropertyPanel.add(addPropertyButton);
		addPropertyPanel.setBackground(BACKGROUND_COLOR);

		// table propeties
		final JTable propertiesTable = new JTable();
		propertyTableModel = new PropertyTableModel(controller);
		propertiesTable.setModel(propertyTableModel);
		propertiesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		propertiesTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

		JPanel tablePanel = new JPanel(new BorderLayout(20, 0));
		tablePanel.add(propertiesTable.getTableHeader(), BorderLayout.PAGE_START);
		tablePanel.add(propertiesTable, BorderLayout.CENTER);

		// remove property
		final JButton removeProperty = new JButton("remove");
		removeProperty.setEnabled(false);
		removeProperty.setPreferredSize(new Dimension(200, 35));

		propertyPanel.add(addPropertyPanel, BorderLayout.PAGE_START);
		propertyPanel.add(tablePanel, BorderLayout.CENTER);
		propertyPanel.add(removeProperty, BorderLayout.PAGE_END);
		propertyPanel.setBackground(BACKGROUND_COLOR);

		/******* END SECTION PROPERTY PANEL ********/

		/******* BEGIN MENU ********/
		final JMenuBar menuBar = new JMenuBar();

		final JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		menuBar.add(menu);

		final JMenuItem menuItemClean = new JMenuItem("Clear", KeyEvent.VK_C);
		menu.add(menuItemClean);

		final JMenuItem menuItemOpen = new JMenuItem("Open", KeyEvent.VK_O);
		menu.add(menuItemOpen);

		final JMenuItem menuItemSave = new JMenuItem("Save", KeyEvent.VK_S);
		menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		menu.add(menuItemSave);

		final JFileChooser fileChooser = new JFileChooser();

		/******* END MENU ********/

		/******* BEGIN GLOBAL PUT TOGETHER ******/
		setJMenuBar(menuBar);
		add(mainSectionPanel, BorderLayout.LINE_START);
		add(propertyPanel, BorderLayout.CENTER);

		setTitle("IniSoft : A revolutionary .ini editor !");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		/******* END GLOBAL PUT TOGETHER ******/

		/******* BEGIN DEFINE COMPONENTS BEHAVIOUR ******/

		addSectionText.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				manageAddSectionButton();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				manageAddSectionButton();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				manageAddSectionButton();
			}

			private void manageAddSectionButton() {
				if (!"".equals(addSectionText.getText())) {
					addSectionButton.setEnabled(true);
				} else {
					addSectionButton.setEnabled(false);
				}
			}
		});

		addSectionText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && addSectionButton.isEnabled()) {
					addSectionButton.doClick();
				}
			}
		});

		sectionList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				Object value = ((JList) e.getSource()).getSelectedValue();
				if (e.getValueIsAdjusting())
					return;

				if (value != null) {
					String newSection = value.toString();
					boolean isNotRoot = !AbstractData.ROOT_SECTION.equals(newSection);
					removeSection.setEnabled(isNotRoot);
					renameSection.setEnabled(isNotRoot);

					addPropertyText.setEnabled(true);

					if (currentSection != null && currentSection.equals(newSection)) {
						return;
					}
					currentSection = newSection;
					refreshPropertyTable();
				} else {
					removeSection.setEnabled(false);
					renameSection.setEnabled(false);
					if (currentSection == null) {
						addPropertyText.setText("");
						addPropertyText.setEnabled(false);
					}
				}
			}
		});

		addSectionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					currentSection = addSectionText.getText().trim();
					controller.addSection(currentSection);
					addSectionText.setText("");
					refreshPropertyTable();
				} catch (SectionAlreadyExistsException e1) {
					popUpError(e1.getMessage());
				}
			}
		});

		removeSection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String section = sectionList.getSelectedValue().toString();
					controller.removeSection(section);
					if (section.equals(currentSection)) {
						currentSection = null;
						refreshPropertyTable();
					}
				} catch (UnknownSectionException e1) {
					popUpError(e1.getMessage());
				}
			}
		});

		renameSection.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String newSection = (String) JOptionPane.showInputDialog("Section name", currentSection);
				if (newSection.equals(currentSection)) {
					return;
				}
				String oldSession = currentSection;
				currentSection = newSection;
				try {
					controller.renameSection(oldSession, newSection);
				} catch (UnknownSectionException e1) {
					currentSection = oldSession;
					popUpError(e1.getMessage());
				} catch (SectionAlreadyExistsException e1) {
					currentSection = oldSession;
					popUpError(e1.getMessage());
				}
			}
		});

		addPropertyText.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				manageAddSectionButton();
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				manageAddSectionButton();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				manageAddSectionButton();
			}

			private void manageAddSectionButton() {
				if (!"".equals(addPropertyText.getText())) {
					addPropertyButton.setEnabled(true);
				} else {
					addPropertyButton.setEnabled(false);
				}
			}
		});

		addPropertyText.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && addPropertyButton.isEnabled()) {
					addPropertyButton.doClick();
				}
			}
		});

		addPropertyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IniProperty prop = new IniProperty(addPropertyText.getText().trim(), "");
				try {
					controller.addProperty(currentSection, prop);
					addPropertyText.setText("");
				} catch (UnknownSectionException e1) {
					popUpError(e1.getMessage());
				} catch (PropertyAlreadyExistsException e1) {
					popUpError(e1.getMessage());
				}
			}
		});

		propertiesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {

				if (e.getValueIsAdjusting())
					return;

				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					removeProperty.setEnabled(false);
				} else {
					removeProperty.setEnabled(true);
				}
			}
		});

		removeProperty.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String key = propertiesTable.getModel().getValueAt(propertiesTable.getSelectedRow(), 0).toString();

				IniProperty prop = new IniProperty(key, "");
				try {
					controller.removeProperty(currentSection, prop);
				} catch (UnknownSectionException e1) {
					popUpError(e1.getMessage());
				} catch (UnknownPropertyException e1) {
					popUpError(e1.getMessage());
				}
			}
		});

		menuItemOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fileChooser.showOpenDialog(Frame.getFrames()[0]);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						currentSection = null;
						data = controller.loadDataFromFile(file);
						setObject(data);
					} catch (IOException e1) {
						popUpError(e1.getMessage());
					}
				}
			}
		});

		menuItemSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnValue = fileChooser.showSaveDialog(Frame.getFrames()[0]);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						controller.save(file.getAbsolutePath());
						JOptionPane.showMessageDialog(Frame.getFrames()[0], "File has been saved", "Saved", JOptionPane.INFORMATION_MESSAGE);
					} catch (IOException e1) {
						popUpError(e1.getMessage());
					}
				}
			}
		});

		menuItemClean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentSection = null;
				data.clear();
			}
		});

		fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {

			@Override
			public String getDescription() {
				return "*.ini";
			}

			@Override
			public boolean accept(File f) {
				return f.isDirectory() || f.getPath().endsWith(".ini");
			}
		});

		/******* END DEFINE COMPONENTS BEHAVIOUR ******/

		logger.log(Level.INFO, " Components initialized");

	}

	/**
	 * refreshes section properties in the JTable
	 */
	private void refreshPropertyTable() {
		logger.log(Level.INFO, " refreshTable with currentSection = '" + currentSection + "'");
		try {
			propertyTableModel.changeSection(currentSection);
			addPropertyText.setEnabled(currentSection != null);
		} catch (UnknownSectionException e) {
			popUpError(e.getMessage());
		}
	}

	/**
	 * refreshes section list
	 */
	private void refreshSectionList() {
		logger.log(Level.INFO, " refreshSectionTree");

		model.removeAllElements();
		List<String> sectionStringList = data.getSectionList();

		for (String s : sectionStringList) {
			model.addElement(s);
		}

		if (currentSection == null && !sectionStringList.isEmpty()) {
			currentSection = sectionStringList.get(0);
		}
		if (currentSection != null) {
			sectionList.setSelectedIndex(sectionStringList.indexOf(currentSection));
		}

	}

	@Override
	public void sectionChanged(EventObject event) {
		logger.log(Level.INFO, " sectionChanged");
		refreshSectionList();
	}

	@Override
	public void propertyChanged(EventObject event) {
		logger.log(Level.INFO, " propertyChanged");
		refreshPropertyTable();
	}

	private void popUpError(String message) {
		logger.log(Level.SEVERE, message);
		JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	@Override
	public void setObject(Object bean) {
		logger.log(Level.INFO, " updating data model");
		if (data != null) {
			data.removeEventListener(this);
		}
		data = (AbstractData) bean;
		data.addEventListener(this);
		propertyTableModel.setData(data);
		refreshSectionList();
		refreshPropertyTable();
	}

}
