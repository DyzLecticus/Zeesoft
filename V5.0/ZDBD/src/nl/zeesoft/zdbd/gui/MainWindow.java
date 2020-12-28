package nl.zeesoft.zdbd.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import nl.zeesoft.zdbd.Event;
import nl.zeesoft.zdbd.ThemeController;
import nl.zeesoft.zdbd.ThemeControllerSettings;
import nl.zeesoft.zdbd.ThemeSequenceSelector;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunnerChain;
import nl.zeesoft.zdk.thread.RunCode;

public class MainWindow extends FrameObject implements ActionListener {
	public static String			NAME				= "ZDBD";
	
	private static final String		QUIT				= "QUIT";
	private static final String		LOAD				= "LOAD";
	private static final String		SAVE				= "SAVE";
	private static final String		SAVE_AS				= "SAVE_AS";
	private static final String		DELETE				= "DELETE";
	private static final String		NEW					= "NEW";
	private static final String		DEMO				= "DEMO";
	
	private ThemeControllerSettings	settings			= null;
	
	private ThemeSequenceSelector	selector			= new ThemeSequenceSelector();
	private SequencerPanel			sequencerPanel		= new SequencerPanel();
	private ProgressHandler			progressHandler		= new ProgressHandler();
	
	
	public MainWindow(ThemeController controller, ThemeControllerSettings settings) {
		super(controller);
		this.settings = settings;
	}

	@Override
	public void handleEvent(Event event) {
		progressHandler.getLabel().setText(event.name);
		updateTitle();
		sequencerPanel.refresh();
	}

	@Override
	public void initialize() {
		selector.setController(controller);
		sequencerPanel.initialize(controller,selector);
		
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
		
		frame.setContentPane(constructMainPanel());
		
		frame.setVisible(true);
		
		CodeRunnerChain chain = controller.initialize(settings);
		if (chain!=null) {
			progressHandler.startChain(chain);
		} else {
			// TODO: Self destruct message
			System.exit(1);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (
			e.getActionCommand().equals(QUIT) ||
			e.getActionCommand().equals(LOAD) ||
			e.getActionCommand().equals(SAVE) ||
			e.getActionCommand().equals(SAVE_AS) ||
			e.getActionCommand().equals(DELETE) ||
			e.getActionCommand().equals(NEW)
			) {
			SwingWorker<String, Object> sw = new SwingWorker<String, Object>() {
				@Override
				public String doInBackground() {
					if (e.getActionCommand().equals(QUIT)) {
						handleQuitRequest();
					} else if (e.getActionCommand().equals(LOAD)) {
						handleLoadRequest();
					} else if (e.getActionCommand().equals(SAVE)) {
						handleSaveRequest(false);
					} else if (e.getActionCommand().equals(SAVE_AS)) {
						handleSaveRequest(true);
					} else if (e.getActionCommand().equals(DELETE)) {
						handleDeleteRequest();
					} else if (e.getActionCommand().equals(NEW)) {
						handleNewRequest();
					}
					return "";
				}
			};
			sw.execute();
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

		item = new JMenuItem("Delete",KeyEvent.VK_D);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,evt));
		item.setActionCommand(DELETE);
		item.addActionListener(this);
		fileMenu.add(item);

		item = new JMenuItem("New",KeyEvent.VK_N);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,evt));
		item.setActionCommand(NEW);
		item.addActionListener(this);
		fileMenu.add(item);

		// TODO: Handle demo load
		item = new JMenuItem("Demo",KeyEvent.VK_D);
		item.setActionCommand(DEMO);
		item.addActionListener(this);
		fileMenu.add(item);
		
		evt = 0;
		JMenu sequencerMenu = new JMenu("Sequencer");
		sequencerMenu.setMnemonic(KeyEvent.VK_S);
		bar.add(sequencerMenu);
		
		item = new JMenuItem("Play sequence",KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,evt));
		item.setActionCommand(SequencerPanel.PLAY_SEQUENCE);
		item.addActionListener(sequencerPanel);
		sequencerMenu.add(item);

		item = new JMenuItem("Play theme",KeyEvent.VK_T);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6,evt));
		item.setActionCommand(SequencerPanel.PLAY_THEME);
		item.addActionListener(sequencerPanel);
		sequencerMenu.add(item);

		/* TODO: Record toggle
		item = new JMenuItem("Continue",KeyEvent.VK_C);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7,evt));
		item.setActionCommand(CONTINUE_PLAYING);
		item.addActionListener(sequencerPanel);
		sequencerMenu.add(item);
		*/

		item = new JMenuItem("Stop",KeyEvent.VK_S);
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8,evt));
		item.setActionCommand(SequencerPanel.STOP);
		item.addActionListener(sequencerPanel);
		sequencerMenu.add(item);

		return bar;
	}
	
	protected boolean checkNotChanged() {
		boolean r = !controller.themeHasChanges();
		if (!r) {
	    	int response = JOptionPane.showConfirmDialog(
        		frame,
    			"The current theme has changes.\nDo you want to continue?",
    			"Continue?",
    			JOptionPane.YES_NO_OPTION,
    			JOptionPane.QUESTION_MESSAGE
    		);
            if (response == JOptionPane.YES_OPTION) {
            	r = true;
            }
		}
		return r;
	}
	
	protected boolean checkBusy() {
		boolean r = controller.isBusy();
		if (r) {
			JOptionPane.showMessageDialog(frame, "Unable to start the specified operation at this time");
		}
		return r;
	}
	
	protected void handleQuitRequest() {
		int response = JOptionPane.YES_OPTION;
		if (controller.themeHasChanges()) {
	    	response = JOptionPane.showConfirmDialog(
	    		frame,
				"The current theme has changes.\nAre you sure you want to quit?",
				"Quit?",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
			);
		}
        if (response == JOptionPane.YES_OPTION) {
        	if (!checkBusy()) {
	            CodeRunnerChain chain = controller.destroy();
	            chain.add(new RunCode() {
					@Override
					protected boolean run() {
						System.exit(0);
						return true;
					}
	            	
	            });
	            progressHandler.startChain(chain);
        	}
		}
	}
	
	protected void handleLoadRequest() {
		if (checkNotChanged()) {
			List<String> themes =  controller.listThemes();
			String[] names = new String[themes.size()];
			int i = 0;
			for (String name: themes) {
				names[i] = name;
				i++;
			}
		    String response = (String) JOptionPane.showInputDialog(
		    	frame,
		    	"Select a theme to load",
		    	"Load theme",
		    	JOptionPane.QUESTION_MESSAGE,
		    	null,
		    	names,
		    	""
		    );
			if (response!=null) {
	        	if (!checkBusy()) {
					CodeRunnerChain chain = controller.loadTheme(response);
		            progressHandler.startChain(chain);
	        	}
			}
		}
	}
	
	protected void handleSaveRequest(boolean saveAs) {
		if (!saveAs && controller.getName().length()>0) {
        	if (controller.themeHasChanges() && !checkBusy()) {
				CodeRunnerChain chain = controller.saveTheme();
	            progressHandler.startChain(chain);
        	}
		} else {
			String response = (String)JOptionPane.showInputDialog(
                frame,
                "Specify the name of the theme",
                "Save theme as",
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                controller.getName()
            );
	        if (response != null) {
        		boolean save = true;
        		if (controller.listThemes().contains(response)) {
        	    	int overWrite = JOptionPane.showConfirmDialog(
    	        		frame,
    	    			"A theme named '" + response + "' already exists.\nDo you want to overwrite the existing theme?",
    	    			"Overwrite theme?",
    	    			JOptionPane.YES_NO_OPTION,
    	    			JOptionPane.QUESTION_MESSAGE
    	    		);
        	    	save = (overWrite == JOptionPane.YES_OPTION);
        		}
        		if (save) {
    	        	if (!checkBusy()) {
			            CodeRunnerChain chain = controller.saveThemeAs(response);
			            progressHandler.startChain(chain);
	        		}
	        	}
	        }
		}
	}
	
	protected void handleNewRequest() {
		if (checkNotChanged()) {
			String response = (String)JOptionPane.showInputDialog(
	            frame,
	            "Specify the name of the theme",
	            "New theme",
	            JOptionPane.PLAIN_MESSAGE,
	            null,
	            null,
	            ""
	        );
	        if (response != null && response.length()>0) {
	        	if (!checkBusy()) {
		            CodeRunnerChain chain = controller.newTheme(response);
		            progressHandler.startChain(chain);
	        	}
	        }
		}
	}
	
	protected void handleDeleteRequest() {
		List<String> themes =  controller.listThemes();
		String[] names = new String[themes.size()];
		int i = 0;
		for (String name: themes) {
			names[i] = name;
			i++;
		}
	    String response = (String) JOptionPane.showInputDialog(
	    	frame,
	    	"Select a theme to delete",
	    	"Delete theme",
	    	JOptionPane.QUESTION_MESSAGE,
	    	null,
	    	names,
	    	""
	    );
		if (response!=null) {
        	if (!checkBusy()) {
				CodeRunnerChain chain = controller.deleteTheme(response);
	            progressHandler.startChain(chain);
        	}
		}
	}
	
	protected JPanel constructMainPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.insets = new Insets(5,5,5,5);
		pane.add(sequencerPanel.getPanel(), c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.99D;
		c.weightx = 1;
		c.insets = new Insets(5,5,5,5);
		pane.add(constructMiddlePanel(), c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.insets = new Insets(5,5,5,5);
		pane.add(constructFooterPanel(), c);
		return pane;
	}
	
	protected JPanel constructHeaderPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		return pane;
	}
	
	protected JPanel constructMiddlePanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		return pane;
	}
	
	protected JPanel constructFooterPanel() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		c.weightx = 0.2;
		pane.add(progressHandler.getLabel(),c);
		
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		c.insets = new Insets(2,2,2,2);
		c.weightx = 0.8;
		pane.add(progressHandler.getBar(),c);
		
		return pane;
	}
	
	protected void updateTitle() {
		Str title = new Str(NAME);
		String name = controller.getName();
		if (name.length()>0) {
			title.sb().append(" - ");
			title.sb().append(name);
		}
		if (controller.themeHasChanges()) {
			title.sb().append("*");
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				frame.setTitle(title.toString());
			}
        });
	}
}
