package nl.zeesoft.zmmt.gui.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.player.InstrumentPlayerKeyListener;

public abstract class PanelObject implements PropertyChangeListener, FocusListener {
	private	Controller					controller			= null;
	
	private JScrollPane					scroller			= null;
	private JPanel						panel				= new JPanel();
	private boolean						validate			= true;

	private List<NumberSlider>			numberSliders		= new ArrayList<NumberSlider>();
	private List<NumberComboBox>		numberComboBoxes	= new ArrayList<NumberComboBox>();
	
	public PanelObject(Controller controller) {
		this.controller = controller;
	}

	public abstract void initialize();

	public abstract String validate();

	public abstract void handleValidChange();

	public void requestFocus() {
		// Override to implement
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		handlePropertyChanged();
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

	protected void handlePropertyChanged() {
		if (validate) {
			String err = validate();
			if (err.length()>0) {
				controller.showErrorMessage(this,err);
			} else {
				handleValidChange();
			}
		}
	}
	
	protected Controller getController() {
		return controller;
	}

	protected void addLabel(JPanel panel,int row,String text) {
		JLabel lbl = new JLabel(text + " ");
		lbl.addKeyListener(controller.getPlayerKeyListener());
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
		addComponent(panel,row,weighty,c,true);
	}

	protected void addComponent(JPanel panel,int row,double weighty,Component c,boolean fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.FIRST_LINE_START;
		if (fill) {
			gbc.fill = GridBagConstraints.BOTH;
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
		r.addKeyListener(controller.getKeyListener());
		r.setColumns(32);
		return r;
	}

	protected JFormattedTextField getNewNumberTextField(int digits) {
		NumberFormat fmt = NumberFormat.getIntegerInstance(Locale.US);
		fmt.setGroupingUsed(false);
		fmt.setMaximumIntegerDigits(digits);
		JFormattedTextField r = new JFormattedTextField(fmt);
		r.addKeyListener(controller.getKeyListener());
		r.addPropertyChangeListener(this);
		r.setPreferredSize(new Dimension(100,20));
		return r;
	}

	protected NumberSlider getSliderForNumber(JFormattedTextField number) {
		NumberSlider r = null;
		for (NumberSlider ns: numberSliders) {
			if (ns.getNumber()==number) {
				r = ns;
				break;
			}
		}
		return r;
	}
	
	protected JPanel getNewNumberSlider(JFormattedTextField number,int min,int max,int init) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL,min,max,init);
		slider.setPreferredSize(new Dimension(200,20));
		slider.addKeyListener(controller.getPlayerKeyListener());
		NumberSlider ns = new NumberSlider(number,slider);
		numberSliders.add(ns);
		return ns.getPanel();
	}

	protected NumberComboBox getComboBoxForNumber(JFormattedTextField number) {
		NumberComboBox r = null;
		for (NumberComboBox nc: numberComboBoxes) {
			if (nc.getNumber()==number) {
				r = nc;
				break;
			}
		}
		return r;
	}

	protected JPanel getNewNumberComboBox(JFormattedTextField number,List<String> options) {
		return getNewNumberComboBox(number,options,0);
	}

	protected JPanel getNewNumberComboBox(JFormattedTextField number,List<String> options,int subtract) {
		JComboBox<String> comboBox = new JComboBox<String>();
		for (String option: options) {
			comboBox.addItem(option);
		}
		for (int l = 0; l < comboBox.getKeyListeners().length; l++) {
			comboBox.removeKeyListener(comboBox.getKeyListeners()[l]);
		}
		comboBox.addKeyListener(controller.getPlayerKeyListener());
		NumberComboBox nc = new NumberComboBox(number,comboBox,subtract);
		numberComboBoxes.add(nc);
		return nc.getPanel();
	}

	protected void setValidate(boolean validate) {
		this.validate = validate;
	}
}
