package views;

import interfaces.Observable;
import interfaces.Observer;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;

import java.awt.Font;
import javax.swing.SwingConstants;

import tools.Tools;
import main.WindowsControler;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.logging.Level;

import javax.swing.JComboBox;


@SuppressWarnings("serial")
public class Launcher extends JFrame implements Observable, WindowListener {

	private JPanel contentPane;
	private File configFile;
	private File queriesFile;
	private WindowsControler controler;
	private JComboBox comboBox;
	private String level;
	
	private enum LogLevel {
		ALL, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, OFF;
	};

	/**
	 * Create the frame.
	 * @param title 
	 */
	public Launcher(String title) {

		addWindowListener(this);
		
		level = "FINE";

		setTitle(title);
		setLocationRelativeTo(null);
		setBounds(100, 100, 329, 191);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JLabel lblWelcome = new JLabel("Welcome");
		lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
		lblWelcome.setFont(new Font("DejaVu Sans", Font.BOLD, 18));
		contentPane.add(lblWelcome, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblSelectConfigurationFile = new JLabel("Select configuration file");
		GridBagConstraints gbc_lblSelectConfigurationFile = new GridBagConstraints();
		gbc_lblSelectConfigurationFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectConfigurationFile.gridx = 0;
		gbc_lblSelectConfigurationFile.gridy = 1;
		panel.add(lblSelectConfigurationFile, gbc_lblSelectConfigurationFile);
		
		final JButton btnSelectFile = new JButton("Select File");
		btnSelectFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileFilter() {
					@Override
				    public String getDescription() {
					    String type = null;
			            type = "XML File";
			            return type;
				    }
					@Override
					public boolean accept(File f) {
					    if (f.isDirectory()) {
					    	return true;
					    }
					    String extension = Tools.getExtension(f);
					    if (extension != null) {
							if (extension.equalsIgnoreCase("xml")) {
								return true;
							}
					    }
					    return false;
					}
				});
				fc.showOpenDialog(new JFrame());
				if (fc.getSelectedFile() != null) {
					configFile = fc.getSelectedFile();
					actionPerformedFile(configFile);
				}
			}
		});
		GridBagConstraints gbc_btnSelectFile = new GridBagConstraints();
		gbc_btnSelectFile.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelectFile.gridx = 1;
		gbc_btnSelectFile.gridy = 1;
		panel.add(btnSelectFile, gbc_btnSelectFile);
		
		JLabel lblSelectQueriesFile = new JLabel("Select queries file");
		GridBagConstraints gbc_lblSelectQueriesFile = new GridBagConstraints();
		gbc_lblSelectQueriesFile.insets = new Insets(0, 0, 5, 5);
		gbc_lblSelectQueriesFile.gridx = 0;
		gbc_lblSelectQueriesFile.gridy = 3;
		panel.add(lblSelectQueriesFile, gbc_lblSelectQueriesFile);
		
		final JButton btnSelectFile_1 = new JButton("Select file");
		btnSelectFile_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new FileFilter() {
					@Override
				    public String getDescription() {
					    String type = null;
			            type = "SQL File";
			            return type;
				    }
					@Override
					public boolean accept(File f) {
					    if (f.isDirectory()) {
					    	return true;
					    }
					    String extension = Tools.getExtension(f);
					    if (extension != null) {
							if (extension.equalsIgnoreCase("sql")) {
								return true;
							}
					    }
					    return false;
					}
				});
				fc.showOpenDialog(new JFrame());
				if (fc.getSelectedFile() != null) {
					queriesFile = fc.getSelectedFile();
					actionPerformedFile(queriesFile);
				}
			}
		});
		GridBagConstraints gbc_btnSelectFile_1 = new GridBagConstraints();
		gbc_btnSelectFile_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnSelectFile_1.gridx = 1;
		gbc_btnSelectFile_1.gridy = 3;
		panel.add(btnSelectFile_1, gbc_btnSelectFile_1);
		
		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1, BorderLayout.SOUTH);
		
		JLabel lblLogLevel = new JLabel("Log level");
		panel_1.add(lblLogLevel);
		
		comboBox = new JComboBox(LogLevel.values());
		panel_1.add(comboBox);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionPerformedCombo(arg0);
			}
		});
	}

	private void actionPerformedFile(File obj) {
		notifyObservers(obj);
    }
	
	public void actionPerformedCombo(ActionEvent e) {
        level = comboBox.getSelectedItem().toString();
        notifyObservers(Level.parse(level));
    }

	@Override
	public void addObserver(Observer o) {
		this.controler = (WindowsControler) o;
	}

	@Override
	public int countObservers() {
		if (controler != null)
			return 1;
		else return 0;
	}

	@Override
	public void deleteObserver(Observer o) {
		this.controler = null;
	}

	@Override
	public void deleteObservers() {
		this.controler = null;
	}

	@Override
	public void notifyObservers() {
       synchronized(controler) {
    	   controler.update(this, null);
    	   controler.notify();
        }
	}

	@Override
	public void notifyObservers(Object o) {
       synchronized(controler) {
    	   controler.update(this, o);
    	   controler.notify();
        }		
	}

	public Observer getControler() {
		return controler;
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Close program", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (res == 0) {
			controler.quit(0);
//			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			notifyObservers(JFrame.EXIT_ON_CLOSE);
		}
		else
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}
	
	public Level getLevel() {
		return Level.parse(level);
	}

	public String getLevelString() {
		return level;
	}
}
