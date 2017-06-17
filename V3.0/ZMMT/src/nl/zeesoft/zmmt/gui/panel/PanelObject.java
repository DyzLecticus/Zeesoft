package nl.zeesoft.zmmt.gui.panel;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerListModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.player.InstrumentPlayerKeyListener;

public abstract class PanelObject implements PropertyChangeListener, ChangeListener, ActionListener, FocusListener {
	private static final String			F2_PRESSED				= "F2_PRESSED";
	private static final String			F3_PRESSED				= "F3_PRESSED";
	private static final String			F4_PRESSED				= "F4_PRESSED";
	private static final String			F8_PRESSED				= "F8_PRESSED";
	private static final String			CTRL_PG_DN_PRESSED		= "CTRL_PG_DN_PRESSED";
	private static final String			CTRL_PG_UP_PRESSED		= "CTRL_PG_UP_PRESSED";
	private static final String			TOGGLE_CHECKBOX			= "TOGGLE_CHECKBOX";
	
	private	Controller					controller			= null;
	
	private JScrollPane					scroller			= null;
	private JPanel						panel				= new JPanel();
	private boolean						validate			= true;
	
	public PanelObject(Controller controller) {
		this.controller = controller;
	}

	public abstract void initialize();


	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		handlePropertyChanged(evt.getSource());
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		handlePropertyChanged(evt.getSource());
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(F2_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_INSTRUMENTS);
		} else if (evt.getActionCommand().equals(F3_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_PATTERNS);
		} else if (evt.getActionCommand().equals(F4_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_SEQUENCE);
		} else if (evt.getActionCommand().equals(F8_PRESSED)) {
			getController().stopSequencer();
		} else if (evt.getActionCommand().equals(CTRL_PG_DN_PRESSED)) {
			getController().getStateManager().selectNextPattern(this);
		} else if (evt.getActionCommand().equals(CTRL_PG_UP_PRESSED)) {
			getController().getStateManager().selectPreviousPattern(this);
		} else if (evt.getActionCommand().equals(TOGGLE_CHECKBOX)) {
			handlePropertyChanged(evt.getSource());
		}
	}

	public void requestFocus() {
		// Override to implement
	}

	@Override
	public void focusGained(FocusEvent evt) {
		if (evt.getSource() instanceof Component) {
			boolean found = false;
			Component comp = (Component) evt.getSource();
			for (KeyListener listener: comp.getKeyListeners()) {
				if (listener instanceof InstrumentPlayerKeyListener) {
					found = true;
					break;
				}
			}
			if (!found) {
				controller.stopNotes();
			}
			while (comp.getParent().getParent()!=null) {
				found = false;
				for (Component c: panel.getComponents()) {
					if (c==comp.getParent().getParent()) {
						found = true;
						break;
					}
				}
				if (!found) {
					comp = comp.getParent();
				} else {
					break;
				}
			}
			Rectangle r = comp.getBounds();	
			r.y = r.y - 50;
			r.height = r.height + 100;
			panel.scrollRectToVisible(r);
		}
	}
	
	@Override
	public void focusLost(FocusEvent evt) {
		// Ignore
	}
	
	public JScrollPane getScroller() {
		if (scroller==null) {
			scroller = new JScrollPane(panel);
			scroller.getVerticalScrollBar().setUnitIncrement(20);
		}
		return scroller;
	}
	
	public JPanel getPanel() {
		return panel;
	}

	protected void handlePropertyChanged(Object source) {
		if (validate) {
			String err = validate();
			if (err.length()>0) {
				controller.showErrorMessage(this,err);
			} else {
				handleValidChange();
			}
		}
	}

	protected String validate() {
		return "";
	}

	protected void handleValidChange() {
		// Override to implement
	}

	protected Controller getController() {
		return controller;
	}

	protected JFormattedTextField addLabelTextFieldToPanel(JPanel panel,int row,String label) {
		JFormattedTextField r = getNewTextField();
		addLabelProperty(panel,row,label,r);
		return r;
	}

	protected JSlider addLabelSliderToPanel(JPanel panel,int row,String label,int min, int max, int init) {
		JSlider r = getNewSlider(min,max,init);
		LabelSlider ls = new LabelSlider(new JLabel(),r);
		JPanel lsp = ls.getPanel();
		addLabelProperty(panel,row,label,lsp);
		return r;
	}

	protected JComboBox<String> addLabelComboBox(JPanel panel,int row,String label,List<String> options,ActionListener actionListener,int subtract) {
		JComboBox<String> r = getNewComboBox(options,actionListener);
		LabelComboBox ls = new LabelComboBox(new JLabel(),r,subtract);
		JPanel lsp = ls.getPanel();
		addLabelProperty(panel,row,label,lsp);
		return r;
	}

	protected JCheckBox addLabelCheckBoxToPanel(JPanel panel,int row,String label) {
		JCheckBox r = getNewCheckBox(label);
		addProperty(panel,row,r);
		return r;
	}

	protected void addLabelProperty(JPanel panel,int row,String label,Component c) {
		boolean fill = (c instanceof JFormattedTextField);
		addLabel(panel,row,label);
		addProperty(panel,row,c,fill);
	}

	protected void addSeparator(JPanel panel, int row) {
		addComponent(panel,row,0.01,new JSeparator(JSeparator.HORIZONTAL),true,true);
	}
	
	protected void addLabel(JPanel panel,int row,String text) {
		JLabel lbl = new JLabel(text + " ");
		lbl.setFocusable(false);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.LINE_START;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.01;
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(lbl,gbc);
	}
	
	protected void addProperty(JPanel panel,int row,Component c) {
		addProperty(panel,row,c,false);
	}
	
	protected void addProperty(JPanel panel,int row,Component c,boolean fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		if (fill) {
			gbc.fill = GridBagConstraints.HORIZONTAL;
		}
		gbc.weightx = 0.99;
		gbc.gridx = 1;
		gbc.gridy = row;
		panel.add(c,gbc);
	}

	protected void addFiller(JPanel panel,int row) {
		addComponent(panel,row,0.99,new JPanel());
	}

	protected void addComponent(JPanel panel,int row,double weighty,Component c) {
		addComponent(panel,row,weighty,c,true,false);
	}

	protected void addComponent(JPanel panel,int row,double weighty,Component c,boolean fill) {
		addComponent(panel,row,weighty,c,fill,false);
	}

	protected void addComponent(JPanel panel,int row,double weighty,Component c,boolean fill,boolean centered) {
		GridBagConstraints gbc = new GridBagConstraints();
		if (centered) {
			gbc.anchor = GridBagConstraints.LINE_START;
		} else {
			gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		}
		if (fill) {
			if (centered) {
				gbc.fill = GridBagConstraints.HORIZONTAL;
			} else {
				gbc.fill = GridBagConstraints.BOTH;
			}
		}
		gbc.weightx = 1.0;
		gbc.weighty = weighty;
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = row;
		panel.add(c,gbc);
	}

	protected JFormattedTextField getNewTextField() {
		JFormattedTextField r = new JFormattedTextField();
		r.addFocusListener(this);
		r.addPropertyChangeListener(this);
		r.setColumns(32);
		addControlPageUpDownOverridesToComponent(r);
		return r;
	}

	protected JSlider getNewSlider(int min,int max,int init) {
		JSlider r = new JSlider(JSlider.HORIZONTAL,min,max,init);
		r.addKeyListener(controller.getPlayerKeyListener());
		r.addFocusListener(this);
		r.addChangeListener(this);
		addControlPageUpDownOverridesToComponent(r);
		return r;
	}
	
	protected JComboBox<String> getNewComboBox(List<String> options,ActionListener actionListener) {
		JComboBox<String> comboBox = new JComboBox<String>();
		for (String option: options) {
			comboBox.addItem(option);
		}
		for (int l = 0; l < comboBox.getKeyListeners().length; l++) {
			comboBox.removeKeyListener(comboBox.getKeyListeners()[l]);
		}
		comboBox.addKeyListener(controller.getPlayerKeyListener());
		comboBox.addFocusListener(this);
		
		addFunctionKeyOverridesToComponent(comboBox);
		addControlPageUpDownOverridesToComponent(comboBox);
		return comboBox;
	}
	
	protected JCheckBox getNewCheckBox(String label) {
		JCheckBox r = new JCheckBox();
		r.setText(label);
		r.addFocusListener(this);
		r.addKeyListener(controller.getPlayerKeyListener());
		r.addActionListener(this);
		r.setActionCommand(TOGGLE_CHECKBOX);
		addControlPageUpDownOverridesToComponent(r);
		return r;
	}
	
	protected JSpinner getNewNumberSpinner(int digits,int min,int max) {
		JSpinner r = null;
		String[] options = new String[((max - min) + 1)];
		int opt = 0;
		for (int i = min; i <= max; i++) {
			String fmt = "%0" + digits + "d";
			options[opt] = String.format(fmt,i);
			opt++;
		}
		SpinnerListModel model = new SpinnerListModel(options);
		r = new JSpinner();
		r.setModel(model);
		r.addFocusListener(this);
		r.addChangeListener(this);
		addControlPageUpDownOverridesToComponent(r);
		return r;
	}

	protected void setValidate(boolean validate) {
		this.validate = validate;
	}

	protected void addFunctionKeyOverridesToComponent(JComponent comp) {
		// F2 Override
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F2,0,false);
		comp.registerKeyboardAction(this,F2_PRESSED,stroke,JComponent.WHEN_FOCUSED);
		// F3 Override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F3,0,false);
		comp.registerKeyboardAction(this,F3_PRESSED,stroke,JComponent.WHEN_FOCUSED);
		// F4 Override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F4,0,false);
		comp.registerKeyboardAction(this,F4_PRESSED,stroke,JComponent.WHEN_FOCUSED);
		// F8 Override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F8,0,false);
		comp.registerKeyboardAction(this,F8_PRESSED,stroke,JComponent.WHEN_FOCUSED);
	}

	protected void addControlPageUpDownOverridesToComponent(JComponent comp) {
		KeyStroke stroke = null; 

		// Control page down override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,ActionEvent.CTRL_MASK,false);
		comp.registerKeyboardAction(this,CTRL_PG_DN_PRESSED,stroke,JComponent.WHEN_FOCUSED);

		// Control page down override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,ActionEvent.CTRL_MASK,false);
		comp.registerKeyboardAction(this,CTRL_PG_UP_PRESSED,stroke,JComponent.WHEN_FOCUSED);
	}
}
