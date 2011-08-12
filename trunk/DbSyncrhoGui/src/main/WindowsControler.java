package main;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import tools.Tools;
import views.Runner;

import com.dbsynchro.readers.ConfReader;
import com.dbsynchro.readers.SqlReader;
import com.dbsynchro.runners.SqlRunner;

import views.Launcher;

import interfaces.Observable;
import interfaces.Observer;

public class WindowsControler implements Runnable, Observer {
	
	private ConfReader cr;
	private SqlReader sr;
	private SqlRunner srun;
	
	private Map<Observable, Object> observables;
	private Launcher launcher;
	private Runner runner;
	private File configFile;
	private File queriesFile;
	
	private Handler handler;
	
	private static final String title = "Database Synchro Tool";

	
	public WindowsControler() {
		try {
			handler = new FileHandler("DbSynchro.log", false);

			handler.setFormatter(new Formatter() {

				private SimpleDateFormat format	= new SimpleDateFormat("dd-MMM-yyyy HH:mm:SSS");

				@Override
				public String format(LogRecord record) {
					return "[" + format.format(new Date(record.getMillis())) + "] [" + record.getLoggerName() + "] "
							+ record.getMessage() + "\n";
				}
			});
		} catch (SecurityException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		launcher = new Launcher(title);
		observables = new HashMap<Observable,Object>();
		handler.setLevel(launcher.getLevel());
	}
	
	
	public static void main(String[] args) {
		WindowsControler wc = new WindowsControler();
		wc.run();
	}
	
	public void update(Observable arg0, Object arg1) {
		this.observables.put(arg0, arg1);
	}
	
    public synchronized void waitSignal() {
        try {
            this.wait();
        } catch (InterruptedException ex) {

        }
    }

	@Override
	public void run() {
		launcher.setVisible(true);
		launcher.addObserver(this);
        while(true) {
            if(observables.isEmpty())
                this.waitSignal();
            else {
            	// we read the first entry of the map of observables
                Map.Entry<Observable, Object> object = observables.entrySet().iterator().next();
                
                // if the notification comes from the launcher
                if(object.getKey() instanceof Launcher) {

                	// if the notification is an XML file
                	if (object.getValue() instanceof File && Tools.getExtension((File)object.getValue()).equals("xml")) {
                		configFile = (File)object.getValue();
                		confReader();
                	}
                	// if the notification is an SQL file
                	else if (object.getValue() instanceof File && Tools.getExtension((File)object.getValue()).equals("sql")) {
                		queriesFile = (File)object.getValue();
                		sqlReader();
                	}
                	else if (object.getValue() instanceof Level) {
                		handler.setLevel((Level)object.getValue());
                	}
                	// if both queries and config files are known we don't need the launcher anymore
                	if (queriesFile != null && configFile != null) {
                		launcher.dispose();
                		runner = new Runner(title, sr);
                		runner.addObserver(this);
                		runner.setVisible(true);
                	}
                }
                else if(object.getKey() instanceof Runner) {
                	if (object.getValue() instanceof ActionEvent) {
                		sqlRunner();
                	}
                }
                synchronized (this) {
                    observables.remove(object.getKey());
                }
            }
        }
	}
	
	private void confReader() {
		try {
			cr = new ConfReader(handler, configFile.getCanonicalPath());
		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the configuration file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the configuration file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (SAXException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the configuration file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private void sqlReader() {
		try {
			sr = new SqlReader(handler, queriesFile.getCanonicalPath());
		} catch (MalformedInputException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the statements file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the statements file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the statements file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
	
	private void sqlRunner() {
		try {
			new SqlRunner(handler, sr.getsQueries(), sr.getdQueries(), cr.getServers());
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error while running SQL statements", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error while running SQL statements", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (InstantiationException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error while running SQL statements", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error while running SQL statements", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}


	public ConfReader getCr() {
		return cr;
	}


	public void setCr(ConfReader cr) {
		this.cr = cr;
	}


	public SqlReader getSr() {
		return sr;
	}


	public void setSr(SqlReader sr) {
		this.sr = sr;
	}


	public SqlRunner getSrun() {
		return srun;
	}


	public void setSrun(SqlRunner srun) {
		this.srun = srun;
	}


	public void setConfFile(File configFile) {
		this.configFile = configFile;
	}


	public void setQueriesFile(File queriesFile) {
		this.queriesFile = queriesFile;
	}
}
