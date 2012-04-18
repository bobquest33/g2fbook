package de.g2fbook.gui;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EtchedBorder;

import de.g2fbook.services.BusinessService;

public class MainWindow {

	private JFrame frmGfbook;
	private final Action action = new SwingAction();
	private JTextField txtpressTransferTo;
	private JProgressBar progressBar;
	private JTextArea logList;
	private BusinessService businessService = new BusinessService();
	SimpleDateFormat formater = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					MainWindow window = new MainWindow();
					window.frmGfbook.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
		try {
			businessService.init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmGfbook = new JFrame();
		frmGfbook.setTitle("g2fbook");
		frmGfbook.setBounds(100, 100, 499, 274);
		frmGfbook.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGfbook.getContentPane().setLayout(new GridLayout(0, 1, 0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frmGfbook.getContentPane().add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("main", null, panel, null);

		txtpressTransferTo = new JTextField();
		txtpressTransferTo.setBounds(55, 113, 372, 30);
		txtpressTransferTo
				.setText("...press transfer to get googleBook to FritzBox!");
		txtpressTransferTo.setToolTipText("");
		txtpressTransferTo.setEditable(false);
		txtpressTransferTo.setColumns(10);
		panel.setLayout(null);

		progressBar = new JProgressBar();
		progressBar.setBounds(193, 71, 234, 29);

		JButton btnNewButton_1 = new JButton("transfer");
		btnNewButton_1.setBounds(55, 71, 128, 30);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new TransferWorker().execute();

			}
		});

		JLabel label = new JLabel("");
		label.setBounds(0, 0, 0, 0);

		panel.add(btnNewButton_1);
		panel.add(progressBar);
		panel.add(label);
		panel.add(txtpressTransferTo);

		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setEnabled(false);
		tabbedPane.addTab("config", null, tabbedPane_2, null);
		
		 logList = new JTextArea();
		tabbedPane.addTab("logList", null, logList, null);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "SwingAction");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {
		}
	}

	class TransferWorker extends SwingWorker<Void, Void> {
		@Override
		public Void doInBackground() {
			logList.append("\n"+formater.format(new Date())+" transfer pressed...");
			txtpressTransferTo.setText("loading GoogleBook...");
			logList.append("\n"+formater.format(new Date())+" loading GoogleBook...");
			
			try {
				businessService.getGoogleBook();
			} catch (Exception e) {

				logList.append("\n"+formater.format(new Date())+e.getMessage());
			}
			progressBar.setValue(25);
			
			txtpressTransferTo.setText("transform to FritzBook...");
			logList.append("\n"+formater.format(new Date())+" transform to FritzBook...");
			businessService.transformToFritzBook();
			progressBar.setValue(50);
			
			txtpressTransferTo.setText("transform the ImageUrls...");
			logList.append("\n"+formater.format(new Date())+" transform the ImageUrls...");
			businessService.transformToImageUrls();
			progressBar.setValue(75);
			
			txtpressTransferTo.setText("transfer to FritzBox!...");
			logList.append("\n"+formater.format(new Date())+" transfer to FritzBox!...");
			businessService.transferToFritzBox();
			progressBar.setValue(90);
			
			txtpressTransferTo.setText("activate on FritzBox!...");
			logList.append("\n"+formater.format(new Date())+" activate on FritzBox!...");
			businessService.activateOnFritzBox();
			progressBar.setValue(100);
			
			txtpressTransferTo.setText("transfer ready!...");
			logList.append("\n"+formater.format(new Date())+" transfer ready!...");
			return null;
		}

		@Override
		protected void done() {
			try {
				System.out.println("lulu");
			}

			catch ( /* InterruptedException, ExecutionException */Exception e) {
			}
		}
	}
}
