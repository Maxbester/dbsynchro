package views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JProgressBar;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Process extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public Process(String title, String label) {
		setTitle(title);
		setResizable(false);
		setAlwaysOnTop(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 323, 124);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setBounds(72, 39, 158, 30);
		contentPane.add(progressBar);
		
		JLabel lblRunning = new JLabel(label);
		lblRunning.setBounds(72, 12, 158, 15);
		contentPane.add(lblRunning);
	}
}
