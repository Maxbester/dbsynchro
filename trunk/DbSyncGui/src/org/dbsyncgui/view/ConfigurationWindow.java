package org.dbsyncgui.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
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

	private JTabbedPane configTabbedPane;
	private JTextPane queryTextPane;

	public ConfigurationWindow() {
		initComponents();
		setDefaultCloseOperation(ConfigurationWindow.EXIT_ON_CLOSE);
		setTitle("ConfigurationWindow");
		getContentPane().setPreferredSize(getSize());
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initComponents() {
		setLayout(new BorderLayout());
		setJMenuBar(getConfigMenuBar());
		add(getTabbedPane(), BorderLayout.CENTER);
		setSize(320, 240);
	}

	private JTabbedPane getConfigTabbedPane() {
		if (configTabbedPane == null) {
			configTabbedPane = new JTabbedPane();
		}
		return configTabbedPane;
	}

	private JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.addTab("Configuration", getConfigTabbedPane());
			tabbedPane.addTab("Queries", getQueryTextPane());
		}
		return tabbedPane;
	}

	private JTextPane getQueryTextPane() {
		if (queryTextPane == null) {
		    StyleContext sc = new StyleContext();
		    final DefaultStyledDocument doc = new DefaultStyledDocument(sc);
		    queryTextPane = new JTextPane(doc);

		    final Style mainStyle = sc.addStyle("Main Style", null);
		    mainStyle.addAttribute(StyleConstants.FontSize, new Integer(16));
		    mainStyle.addAttribute(StyleConstants.FontFamily, "consolas");

		    doc.setLogicalStyle(0, mainStyle);
		}
		return queryTextPane;
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
