package nl.zeesoft.zmmt.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.state.CompositionChangePublisher;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;

public class PanelPatterns extends PanelObject implements ActionListener, CompositionChangePublisher, StateChangeSubscriber {
	private JComboBox<String>		pattern							= null;
	private int						selectedPattern					= 0;

	private int						barsPerPattern					= 0;
	private JCheckBox				customBars						= null;
	private JComboBox<String>		bars							= null;

	private JCheckBox				insertMode						= null;
	
	private JTable					grid							= null;
	private PatternGridController	gridController					= null;
	
	private	Composition				compositionCopy					= null;
	private Pattern					workingPattern					= null;
	
	public PanelPatterns(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		selectedPattern = controller.getStateManager().getSelectedPattern();
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		int row = 0;
		addComponent(getPanel(), row, 0.01,getEditPanel());

		row++;
		addComponent(getPanel(), row, 0.01,getDetailsPanel());

		row++;
		addComponent(getPanel(),row,0.99,getPatternPanel(),true);		
	}

	@Override
	public void requestFocus() {
		pattern.requestFocus();
	}

	@Override
	public void handleValidChange() {
		getController().getStateManager().addWaitingPublisher(this);
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (evt.getType().equals(StateChangeEvent.SELECTED_PATTERN)) {
			selectedPattern = evt.getSelectedPattern();
			pattern.setSelectedIndex(selectedPattern);
			updateWorkingPattern();
		} else if (evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			compositionCopy = evt.getComposition().copy();
			barsPerPattern = compositionCopy.getBarsPerPattern();
			updateWorkingPattern();
			if (gridController.setLayout(
				compositionCopy.getBarsPerPattern(),
				compositionCopy.getBeatsPerBar(),
				compositionCopy.getStepsPerBeat()
				)) {
				gridController.fireTableStructureChanged();
			}
		}
		setValidate(true);
	}

	@Override
	public void setChangesInComposition(Composition composition) {
		// TODO: Implement
	}

	protected void updateWorkingPattern() {
		Pattern current = workingPattern;
		if (workingPattern==null || workingPattern.getNumber()!=selectedPattern) {
			if (compositionCopy!=null) {
				workingPattern = compositionCopy.getPattern(selectedPattern);
			} else {
				workingPattern = null;
			}
			
			if (workingPattern!=null && workingPattern.getBars()>0) {
				bars.setSelectedIndex((workingPattern.getBars() + 1));
				bars.setEnabled(true);
			} else {
				bars.setEnabled(false);
				bars.setSelectedIndex(0);
			}
			
			if (
				(current==null && workingPattern!=null) ||
				(current!=null && workingPattern==null) ||
				(current!=null && workingPattern!=null)
				) {
				gridController.setWorkingPattern(workingPattern);
				gridController.fireTableDataChanged();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource()==pattern) {
			if (pattern.getSelectedIndex()!=selectedPattern) {
				selectedPattern = pattern.getSelectedIndex();
				getController().getStateManager().setSelectedPattern(this,selectedPattern);
			}
		} else if (evt.getSource()==customBars) {
			if (bars.isEnabled()!=customBars.isSelected()) {
				bars.setEnabled(customBars.isSelected());
				if (!customBars.isSelected() && bars.getSelectedIndex()>0) {
					bars.setSelectedIndex(0);
					if (workingPattern!=null) {
						workingPattern.setBars(0);
						// TODO: publish pattern change
					}
				}
			}
		} else if (evt.getSource()==bars) {
			if (customBars.isSelected()) {
				if (workingPattern!=null && workingPattern.getBars()!=bars.getSelectedIndex()) {
					workingPattern.setBars(bars.getSelectedIndex());
					// TODO: publish pattern change
				}
			}
		}
	}
	
	protected JScrollPane getPatternPanel() {
		gridController = new PatternGridController();
		grid = new JTable();
		grid.addKeyListener(getController().getPlayerKeyListener());
		grid.setModel(gridController);
		JScrollPane r = new JScrollPane(grid);
		r.getVerticalScrollBar().setUnitIncrement(20);
		return r;
	}
	
	protected JComboBox<String> getPatternSelector() {
		JComboBox<String> r = new JComboBox<String>();
		for (int p = 0; p <= 99; p++) {
			r.addItem(String.format("%02d",p));
		}
		r.setSelectedIndex(selectedPattern);
		r.addActionListener(this);
		for (int l = 0; l < r.getKeyListeners().length; l++) {
			r.removeKeyListener(r.getKeyListeners()[l]);
		}
		r.addKeyListener(getController().getPlayerKeyListener());
		return r;
	}

	protected JComboBox<String> getBarsSelector() {
		JComboBox<String> r = new JComboBox<String>();
		for (int b = 0; b <= 16; b++) {
			if (b==0) {
				r.addItem("");
			} else {
				r.addItem(String.format("%02d",b));
			}
		}
		r.setEnabled(false);
		r.setSelectedIndex(barsPerPattern);
		r.addActionListener(this);
		for (int l = 0; l < r.getKeyListeners().length; l++) {
			r.removeKeyListener(r.getKeyListeners()[l]);
		}
		r.addKeyListener(getController().getPlayerKeyListener());
		return r;
	}
	
	protected JPanel getEditPanel() {
		JPanel r = new JPanel();
		r.setLayout(new BorderLayout());

		JPanel labelProp = new JPanel();
		labelProp.add(new JLabel("Pattern "));
		pattern = getPatternSelector();
		labelProp.add(pattern);
		r.add(labelProp,BorderLayout.LINE_START);

		insertMode = new JCheckBox("Insert ");
		insertMode.addKeyListener(getController().getPlayerKeyListener());
		r.add(insertMode,BorderLayout.LINE_END);
		
		return r;
	}

	protected JPanel getDetailsPanel() {
		JPanel r = new JPanel();
		r.setLayout(new BorderLayout());
		r.setBorder(BorderFactory.createTitledBorder("Details"));

		JPanel labelProp = new JPanel();
		customBars = new JCheckBox("Custom bars");
		customBars.addActionListener(this);
		customBars.addKeyListener(getController().getPlayerKeyListener());
		labelProp.add(customBars);
		bars = getBarsSelector();
		labelProp.add(bars);
		r.add(labelProp,BorderLayout.LINE_START);
		
		return r;
	}
}
