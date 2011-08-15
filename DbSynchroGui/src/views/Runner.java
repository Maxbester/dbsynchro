package views;

import interfaces.Observable;
import interfaces.Observer;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JTree;
import javax.swing.JButton;

import com.dbsynchro.readers.SqlReader;
import com.dbsynchro.util.Query;
import javax.swing.JScrollPane;

import main.WindowsControler;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class Runner extends JFrame implements WindowListener, Observable {

	private JPanel contentPane;
	private WindowsControler controler;

	/**
	 * Create the frame.
	 * @param sr 
	 */
	public Runner(String title, SqlReader sr) {
		addWindowListener(this);

		setTitle(title);
		setBounds(100, 100, 800, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		DefaultMutableTreeNode sources = new DefaultMutableTreeNode("Statements", true);
		
		int i = 0;
		for (Query qSource : sr.getsQueries()) {
			DefaultMutableTreeNode source = new DefaultMutableTreeNode(qSource.getServerName()+" - "+qSource.getStatement());
			for (Query qDistant : sr.getdQueries().get(i)) {
				source.add(new DefaultMutableTreeNode(qDistant.getServerName()+" - "+qDistant.getStatement()));
			}
			sources.add(source);
			i++;
		}
		
		JTree tree = new JTree(sources);
		
		JScrollPane scrollPane = new JScrollPane(tree);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Run");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				actionPerformedRun(arg0);
			}
		});
		panel.add(btnNewButton);
	}
	
	private void actionPerformedRun(Object obj) {
		int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to run those queries?", "Run", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == 0)
			notifyObservers(obj);
    }

	@Override
	public void windowClosing(WindowEvent e) {
		int res = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Close program", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (res == 0)
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		else
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
	public void notifyObservers(Object arg) {
       synchronized(controler) {
    	   controler.update(this, arg);
    	   controler.notify();
        }
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
}
