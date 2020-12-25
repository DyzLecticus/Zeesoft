package nl.zeesoft.zdbd.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

import nl.zeesoft.zdbd.Event;
import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdbd.ThemeControllerSettings;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;

public class MainWindow extends FrameObject implements ActionListener {
	public static String			NAME	= "MidiDreamer";
	
	private static final String		QUIT				= "QUIT";
	private static final String		LOAD				= "LOAD";
	private static final String		SAVE				= "SAVE";
	private static final String		SAVE_AS				= "SAVE_AS";
	private static final String		NEW					= "NEW";
	private static final String		DEMO_1				= "DEMO_1";
	private static final String		DEMO_2				= "DEMO_2";
	private static final String		LOAD_FILE_PREFIX	= "LOAD_FILE:";
	
	private ThemeControllerSettings	settings			= null;
	
	public MainWindow(ThemeController controller, ThemeControllerSettings settings) {
		super(controller);
		this.settings = settings;
	}

	@Override
	public void handleEvent(Event event) {
		// TODO Auto-generated method stub
		if (event.name.equals(ThemeController.INITIALIZING)) {
			// TODO: Show progress in progress bar
		}
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			JFrame.setDefaultLookAndFeelDecorated(true);
		} catch (Exception e) {
			// Ignore
		}
		frame.setTitle(NAME);
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		    	handleQuitRequest();
		    }
		});
		//frame.addWindowListener(getController().getAdapter());
		//frame.addWindowFocusListener(getController().getAdapter());
		//frame.addKeyListener(getController().getPlayerKeyListener());

		frame.setJMenuBar(getMenuBar());

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth() - 100;
		int height = (int) screenSize.getHeight() - 100;
		frame.setSize(width,height);
		frame.setLocation(50,50);
		
		frame.setVisible(true);
		
		CodeRunnerChain chain = controller.initialize(settings);
		if (chain!=null) {
			// TODO: Add progress listener
			chain.start();
		} else {
			// TODO: Self destruct message
			System.exit(1);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getActionCommand().equals(QUIT)) {
			handleQuitRequest();
		}
	}
	
	protected JMenuBar getMenuBar() {
		JMenuBar bar = new JMenuBar();
		
		int evt = ActionEvent.CTRL_MASK;
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		bar.add(fileMenu);

		JMenuItem item = new JMenuItem("Quit",KeyEvent.VK_Q);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,evt));
		item.setActionCommand(QUIT);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Load",KeyEvent.VK_L);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,evt));
		item.setActionCommand(LOAD);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Save",KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,evt));
		item.setActionCommand(SAVE);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Save as",KeyEvent.VK_A);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,evt));
		item.setActionCommand(SAVE_AS);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("New",KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,evt));
		item.setActionCommand(NEW);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Demo 1",KeyEvent.VK_1);
		item.setActionCommand(DEMO_1);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("Demo 2",KeyEvent.VK_2);
		item.setActionCommand(DEMO_2);
		item.addActionListener(this);
		fileMenu.add(item);
		
		return bar;
	}
	
	protected void handleQuitRequest() {
    	int response = JOptionPane.showConfirmDialog(
    		frame,
			"Are you sure you want to quit?",
			"Quit?",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE
		);
        if (response == JOptionPane.YES_OPTION) {
            CodeRunnerChain chain = controller.destroy();
            chain.add(new RunCode() {
				@Override
				protected boolean run() {
					System.exit(0);
					return true;
				}
            	
            });
            chain.start();
        }
	}
}
