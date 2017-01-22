package nl.zeesoft.zodb.database.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.event.EvtEvent;

public class GuiMainFrame extends GuiFrameObject {
	private final static String		MIN_LABEL_SPACES		= "   ";
	
	public final static String 		STOP_DATABASE_CLICKED 	= "STOP_DATABASE_CLICKED";
	public final static String 		SET_STATUS_CLICKED 		= "SET_STATUS_CLICKED";
	public final static String 		START_BROWSER_CLICKED	= "START_BROWSER_CLICKED";
	public final static String 		SHOW_DEBUGGER_CLICKED 	= "SHOW_DEBUGGER_CLICKED";
	
	private JPanel					mainPanel				= null;

	private JLabel					startedLabel			= null;
	private JLabel					started					= null;
	
	private JLabel					statusLabel				= null;
	private JLabel					status					= null;
	private JLabel					urlLabel				= null;
	private JLabel					url						= null;

	private JButton					stopDatabase			= null;
	private JButton					setStatus				= null;
	private JButton					startBrowser			= null;
	private JButton					showDebugger			= null;
	
	private boolean					canStartBrowser			= Generic.canStartBrowser();
	
	@Override
	protected void render() {
		setTitle("ZODB - Controller");
		setResizable(false);
		setLayout(new GridBagLayout());
		setIconImage(GuiController.getInstance().getStatusIcon());

		GridBagConstraints gbc = null;

		final GuiMainFrame self = this;
				
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setMinimumSize(new Dimension(500,120));
		mainPanel.setPreferredSize(new Dimension(500,120));

		startedLabel = new JLabel(GuiController.STARTED_LABEL + MIN_LABEL_SPACES);
		started = new JLabel("?");
		stopDatabase = GuiController.getNewButton("Stop database");
		stopDatabase.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopDatabase.setEnabled(false);
				setStatus.setEnabled(false);
				if (startBrowser!=null) {
					startBrowser.setEnabled(false);
				}
				showDebugger.setEnabled(false);
				self.publishEvent(new EvtEvent(STOP_DATABASE_CLICKED,self,e));
			}
		});

		statusLabel = new JLabel(GuiController.SERVER_STATUS_LABEL + MIN_LABEL_SPACES);
		status = new JLabel("?");
		setStatus = GuiController.getNewButton("?");
		setStatus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopDatabase.setEnabled(false);
				setStatus.setEnabled(false);
				if (startBrowser!=null) {
					startBrowser.setEnabled(false);
				}
				showDebugger.setEnabled(false);
				self.publishEvent(new EvtEvent(SET_STATUS_CLICKED,self,e));
			}
		});
		
		if (canStartBrowser) {
			urlLabel = new JLabel(GuiController.SERVER_URL_LABEL + MIN_LABEL_SPACES);
			url = new JLabel("?");
			startBrowser = GuiController.getNewButton("Start browser");
			startBrowser.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					stopDatabase.setEnabled(false);
					setStatus.setEnabled(false);
					if (startBrowser!=null) {
						startBrowser.setEnabled(false);
					}
					showDebugger.setEnabled(false);
					self.publishEvent(new EvtEvent(START_BROWSER_CLICKED,self,e));
				}
			});
		}
		
		showDebugger = GuiController.getNewButton("Show debugger");
		if (!GuiController.getInstance().isShowDebugger()) {
			showDebugger.setVisible(false);
		}
		showDebugger.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				stopDatabase.setEnabled(false);
				setStatus.setEnabled(false);
				if (startBrowser!=null) {
					startBrowser.setEnabled(false);
				}
				showDebugger.setEnabled(false);
				self.publishEvent(new EvtEvent(SHOW_DEBUGGER_CLICKED,self,e));
			}
		});
		
		int row = 0;

		if (canStartBrowser) {
			gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = row;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 0.01;
			gbc.weighty = 0.01;
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.fill = GridBagConstraints.NONE;
			mainPanel.add(urlLabel,gbc);

			gbc = new GridBagConstraints();
			gbc.gridx = 1;
			gbc.gridy = row;
			gbc.gridwidth = 8;
			gbc.gridheight = 1;
			gbc.weightx = 0.98;
			gbc.weighty = 0.01;
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			mainPanel.add(url,gbc);
			
			gbc = new GridBagConstraints();
			gbc.gridx = 9;
			gbc.gridy = row;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.weightx = 0.01;
			gbc.weighty = 0.01;
			gbc.anchor = GridBagConstraints.FIRST_LINE_END;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			mainPanel.add(startBrowser,gbc);

			row++;
		}
				
		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.01;
		gbc.weighty = 0.01;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		mainPanel.add(statusLabel,gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = row;
		gbc.gridwidth = 8;
		gbc.gridheight = 1;
		gbc.weightx = 0.98;
		gbc.weighty = 0.01;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(status,gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 9;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.01;
		gbc.weighty = 0.01;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(setStatus,gbc);

		row++;

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.01;
		gbc.weighty = 0.01;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.NONE;
		mainPanel.add(startedLabel,gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = row;
		gbc.gridwidth = 8;
		gbc.gridheight = 1;
		gbc.weightx = 0.98;
		gbc.weighty = 0.01;
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(started,gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 9;
		gbc.gridy = row;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.01;
		gbc.weighty = 0.01;
		gbc.anchor = GridBagConstraints.FIRST_LINE_END;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(stopDatabase,gbc);

		row++;
		int fillGridHeight = 7;
		double fillWeightY = 0.97;
		
		if (canStartBrowser) {
			fillGridHeight--;
			fillWeightY = fillWeightY - 0.01D;
		}

		gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = row;	
		gbc.gridwidth = 10;
		gbc.gridheight = fillGridHeight;
		gbc.weightx = 1;
		gbc.weighty = fillWeightY;
		gbc.anchor = GridBagConstraints.PAGE_END;
		gbc.fill = GridBagConstraints.BOTH;
		mainPanel.add(new JPanel(),gbc);

		gbc = new GridBagConstraints();
		gbc.gridx = 9;
		gbc.gridy = 10;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0.01;
		gbc.weighty = 0.01;
		gbc.anchor = GridBagConstraints.LAST_LINE_END;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(showDebugger,gbc);
				
		gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.insets = new Insets(10,10,10,10);
		gbc.anchor = GridBagConstraints.PAGE_START;
		gbc.fill = GridBagConstraints.BOTH;
		getContentPane().setLayout(new GridBagLayout());
		getContentPane().add(mainPanel,gbc);
		
		super.render();
	}

	@Override
	protected void refreshPanel() {
		if (status!=null) {
			if (started.getText().equals("?")) {
				Date s = GuiController.getInstance().getStartedDate();
				if (s!=null) {
					started.setText(Generic.getDateTimeString(s,true,false));
				}
			}
			boolean serverIsOpen = GuiController.getInstance().serverIsOpen();
			
			status.setText(GuiController.getInstance().getStatusText());
			if (serverIsOpen) {
				setStatus.setText("Close server");
			} else {
				setStatus.setText("Open server");
			}
			setIconImage(GuiController.getInstance().getStatusIcon());

			if (startBrowser!=null) {
				url.setText(GuiController.getInstance().getUrlText());
			}
			
			showDebugger.setVisible(GuiController.getInstance().isShowDebugger());

			stopDatabase.setEnabled(true);
			setStatus.setEnabled(true);
			if (startBrowser!=null) {
				startBrowser.setEnabled(serverIsOpen);
			}
			showDebugger.setEnabled(true);
		}
		super.refreshPanel();
	}
}
