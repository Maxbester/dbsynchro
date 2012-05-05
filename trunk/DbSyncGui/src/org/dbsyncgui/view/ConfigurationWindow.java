package org.dbsyncgui.view;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

//VS4E -- DO NOT REMOVE THIS LINE!
@SuppressWarnings("serial")
public class ConfigurationWindow extends JFrame {

	private static final String PREFERRED_LOOK_AND_FEEL = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";

	private JMenuBar configMenuBar;
	private JMenu fileMenu;
	private JMenuItem closeMenuButton;

	private JTabbedPane tabbedPane;

	private JPanel queryEditorPanel;
	private JScrollPane queryEditorScrollPane;
	private JTextPane queryTextPane;
	private JPanel queryButtonPanel;
	private JButton saveQueriesButton;
	private JButton exportQueriesButton;
	private JButton importQueriesButton;

	private JPanel configPanel;
	private String[] columnNames = {"First Name",
            "Last Name",
            "Sport",
            "# of Years",
            "Vegetarian"};
	private JTable configTablePanel;
	private JPanel configButtonPanel;
	private JButton addDatabaseButton;
	private JButton editDatabaseButton;
	private JButton removeDatabaseButton;
	private JButton exportDatabaseButton;
	private JButton importDatabaseButton;

	public ConfigurationWindow() {
		initComponents();
		setDefaultCloseOperation(ConfigurationWindow.EXIT_ON_CLOSE);
		setTitle("ConfigurationWindow");
		getContentPane().setPreferredSize(getSize());
		pack();
		setLocationByPlatform(true);
		setVisible(true);
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		setJMenuBar(getConfigMenuBar());
		add(getTabbedPane(), BorderLayout.CENTER);
		setSize(550, 350);
	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Configuration", getConfigPanel());
			tabbedPane.addTab("Queries", getQueryEditorPanel());
		}
		return tabbedPane;
	}

	private JPanel getConfigPanel() {
		if (configPanel == null) {
			configPanel = new JPanel();
			configPanel.setLayout(new BorderLayout());
			configPanel.add(getConfigTablePanel(), BorderLayout.CENTER);
			configPanel.add(getConfigButtonPanel(), BorderLayout.EAST);
		}
		return configPanel;
	}

	private JTable getConfigTablePanel() {
		if (configTablePanel == null) {
			configTablePanel = new JTable();
			
		}
		return configTablePanel;
	}

	private JPanel getConfigButtonPanel() {
		if (configButtonPanel == null) {
			configButtonPanel = new JPanel();
			configButtonPanel.setLayout(new BoxLayout(configButtonPanel, BoxLayout.Y_AXIS));
			configButtonPanel.add(getAddDatabaseButton());
			configButtonPanel.add(getEditDatabaseButton());
			configButtonPanel.add(getRemoveDatabaseButton());
			configButtonPanel.add(getExportDatabaseButton());
			configButtonPanel.add(getImportDatabaseButton());
		}
		return configButtonPanel;
	}

	private JButton getAddDatabaseButton() {
		if (addDatabaseButton == null) {
			addDatabaseButton = new JButton("Add");
		}
		return addDatabaseButton;
	}

	private JButton getEditDatabaseButton() {
		if (editDatabaseButton == null) {
			editDatabaseButton = new JButton("Edit");
		}
		return editDatabaseButton;
	}

	private JButton getRemoveDatabaseButton() {
		if (removeDatabaseButton == null) {
			removeDatabaseButton = new JButton("Remove");
		}
		return removeDatabaseButton;
	}

	private JButton getExportDatabaseButton() {
		if (exportDatabaseButton == null) {
			exportDatabaseButton = new JButton("Export");
		}
		return exportDatabaseButton;
	}

	private JButton getImportDatabaseButton() {
		if (importDatabaseButton == null) {
			importDatabaseButton = new JButton("Import");
		}
		return importDatabaseButton;
	}

	private JPanel getQueryEditorPanel() {
		if (queryEditorPanel == null) {
			queryEditorPanel = new JPanel();
			queryEditorPanel.setLayout(new BorderLayout());
			queryEditorPanel.add(getQueryEditorScrollPane(), BorderLayout.CENTER);
			queryEditorPanel.add(getQueryButtonPanel(), BorderLayout.SOUTH);
		}
		return queryEditorPanel;
	}
	private JScrollPane getQueryEditorScrollPane() {
		if (queryEditorScrollPane == null) {
			queryEditorScrollPane = new JScrollPane(getQueryTextPane());
		}
		return queryEditorScrollPane;
	}

	private JTextPane getQueryTextPane() {
		if (queryTextPane == null) {
		    final StyleContext sc = new StyleContext();
		    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
		    queryTextPane = new JTextPane(doc);

		    final Style mainStyle = sc.addStyle("MainStyle", null);
		    mainStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
		    mainStyle.addAttribute(StyleConstants.FontFamily, "consolas");

		    doc.setLogicalStyle(0, mainStyle);
		}
		return queryTextPane;
	}

	private JPanel getQueryButtonPanel() {
		if (queryButtonPanel == null) {
			queryButtonPanel = new JPanel();
			queryButtonPanel.add(getImportQueriesButton());
			queryButtonPanel.add(getExportQueriesButton());
			queryButtonPanel.add(getSaveQueriesButton());
		}
		return queryButtonPanel;
	}

	private JButton getExportQueriesButton() {
		if (exportQueriesButton == null) {
			exportQueriesButton = new JButton("Export");
		}
		return exportQueriesButton;
	}

	private JButton getImportQueriesButton() {
		if (importQueriesButton == null) {
			importQueriesButton = new JButton("Import");
		}
		return importQueriesButton;
	}

	private JButton getSaveQueriesButton() {
		if (saveQueriesButton == null) {
			saveQueriesButton = new JButton("Save");
		}
		return saveQueriesButton;
	}

	private JMenuBar getConfigMenuBar() {
		if (configMenuBar == null) {
			configMenuBar = new JMenuBar();
			configMenuBar.add(getFileMenu());
		}
		return configMenuBar;
	}

	private JMenu getFileMenu() {
		if (fileMenu == null) {
			fileMenu = new JMenu();
			fileMenu.setText("File");
			fileMenu.setBorderPainted(true);
			fileMenu.add(getCloseMenuButton());
		}
		return fileMenu;
	}

	private JMenuItem getCloseMenuButton() {
		if (closeMenuButton == null) {
			closeMenuButton = new JMenuItem();
			closeMenuButton.setText("Close");
		}
		return closeMenuButton;
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ConfigurationWindow frame = new ConfigurationWindow();
			}
		});
	}

}
