package main;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
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
import views.Process;

import com.dbsynchro.readers.ConfReader;
import com.dbsynchro.readers.SqlReader;
import com.dbsynchro.runners.SqlRunner;

import views.Launcher;

import interfaces.Observable;
import interfaces.Observer;

public class WindowsControler extends Thread implements Observer {
	
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
		Thread thisThread = Thread.currentThread();
		try {
			handler = new FileHandler("DbSynchro.log", false);

			handler.setFormatter(new Formatter() {

				private SimpleDateFormat format	= new SimpleDateFormat("dd/MM/yy HH:mm:SSS");

				@Override
				public String format(LogRecord record) {
					return "[" + format.format(new Date(record.getMillis())) + "] [" + record.getLoggerName() + "] "
							+ record.getMessage() + "\n";
				}
			});
		} catch (SecurityException e2) {
			e2.printStackTrace();
			thisThread.interrupt();
		} catch (IOException e2) {
			e2.printStackTrace();
			thisThread.interrupt();
		}
		launcher = new Launcher(title);
		observables = new HashMap<Observable,Object>();
		handler.setLevel(launcher.getLevel());
	}
	
	
	public static void main(String[] args) {
		new WindowsControler().run();
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
                	} else if (object.getValue() instanceof Integer) {
                		if (((Integer)object.getValue()) == 3) {
                			System.out.println("Close launcher");
                			System.exit(0);
                		}
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
                	} else if (object.getValue() instanceof Integer) {
                		if (((Integer)object.getValue()) == 3) {
                			System.out.println("Close runner");
                			System.exit(0);
                		}
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
			System.exit(-1);
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the configuration file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the configuration file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		} catch (SAXException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the configuration file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void sqlReader() {
		try {
			sr = new SqlReader(handler, queriesFile.getCanonicalPath());
		} catch (ParseException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the statements file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the statements file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error in the statements file", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void sqlRunner() {
		Process p = new Process("Running queries","Please wait...");
		p.setVisible(true);
		SqlRunner srun = new SqlRunner(handler, sr.getsQueries(), sr.getdQueries(), cr.getServers());
		p.dispose();

		if (srun.getEmailContent().length() > 5) {
			JOptionPane.showMessageDialog(null, srun.getEmailContent(), "Error while running statements", JOptionPane.ERROR_MESSAGE);
			JOptionPane.showMessageDialog(null, "Queries were executed with some problems.", "Warning", JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Queries were executed successfully.", "Done", JOptionPane.INFORMATION_MESSAGE);
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

	public void quit(int status) {
		System.exit(status);
	}
}
