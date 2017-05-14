package nl.zeesoft.zmmt.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Note;
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
	private List<Note>				workingNotes					= new ArrayList<Note>();
	
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
		if (!grid.hasFocus()) {
			pattern.requestFocus();
		}
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
		} else if (evt.getSource()!=this && evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			compositionCopy = evt.getComposition().copy();
			barsPerPattern = compositionCopy.getBarsPerPattern();
			workingPattern = null;
			updateWorkingPattern();
			if (gridController.setLayout(
				compositionCopy.getBarsPerPattern(),
				compositionCopy.getBeatsPerBar(),
				compositionCopy.getStepsPerBeat()
				)) {
				gridController.fireTableStructureChanged();
			} else {
				refreshGridData();
			}
		}
		setValidate(true);
	}

	@Override
	public void setChangesInComposition(Composition composition) {
		// TODO: Implement
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
				}
				changedPattern();
			}
		}
	}

	@Override
	public void focusLost(FocusEvent evt) {
		super.focusLost(evt);
		if (evt.getSource()==grid) {
			workingNotes.clear();
		}
	}

	protected void selectPattern() {
		pattern.requestFocus();
	}

	protected void shiftSelectedNotesNote(int mod) {
		boolean changed = false;
		List<Note> sns = getSelectedNotes();
		for (Note sn: sns) {
			if (mod>0 && sn.note<127) {
				changed = true;
				sn.note += mod;
				if (sn.note>127) {
					sn.note=127;
				}
			} else if (mod<0 && sn.note>0) {
				changed = true;
				sn.note -= (mod * -1);
				if (sn.note<0) {
					sn.note=0;
				}
			}
		}
		if (changed) {
			changedPattern();
		}
	}

	protected void shiftSelectedNotesVelocityPercentage(int mod) {
		boolean changed = false;
		List<Note> sns = getSelectedNotes();
		for (Note sn: sns) {
			if (mod>0 && sn.velocityPercentage<100) {
				changed = true;
				sn.velocityPercentage += mod;
				if (sn.velocityPercentage>100) {
					sn.velocityPercentage=100;
				}
			} else if (mod<0 && sn.velocityPercentage>0) {
				changed = true;
				sn.velocityPercentage -= (mod * -1);
				if (sn.velocityPercentage<0) {
					sn.velocityPercentage=0;
				}
			}
		}
		if (changed) {
			changedPattern();
		}
	}

	protected void deleteSelectedNotes() {
		List<Note> sns = getSelectedNotes();
		for (Note sn: sns) {
			workingPattern.getNotes().remove(sn);
		}
		if (sns.size()>0) {
			changedPattern();
		}
	}

	protected void toggleInsertMode() {
		insertMode.doClick();
	}

	protected void playNote(int note, boolean accent) {
		if (grid.getSelectedColumn()>=0 && grid.getSelectedRow()>=0) {
			Note pn = null;
			for (Note wn: workingNotes) {
				if (wn.note==note) {
					pn = wn;
					break;
				}
			}
			if (pn==null) {
				pn = new Note();
				pn.note = note;
				pn.instrument = getController().getStateManager().getSelectedInstrument();
				pn.track = grid.getSelectedColumn() + 1 + workingNotes.size();
				pn.step = grid.getSelectedRow() + 1;
				pn.accent = accent;
				Note patternNote = workingPattern.getNote(pn.track,pn.step,pn.duration);
				if (patternNote!=null && patternNote.step<pn.step) {
					patternNote.duration = (pn.step - patternNote.step);
					patternNote = null;
				}
				if (patternNote==null) {
					patternNote = pn;
					workingPattern.getNotes().add(pn);
					// TODO Update composition
				} else if (patternNote.step==pn.step) {
					patternNote.instrument = pn.instrument;
					patternNote.note = pn.note;
					patternNote.accent = pn.accent;
					patternNote.velocityPercentage = 100;
				}
				workingNotes.add(patternNote);
				changedPattern();
			}
		}
	}

	protected void stopNote(int note) {
		Note pn = null;
		for (Note wn: workingNotes) {
			if (wn.note==note) {
				pn = wn;
				break;
			}
		}
		if (pn!=null) {
			workingNotes.remove(pn);
			if ((grid.getSelectedRow() + 1)>pn.step) {
				int newDuration = (((grid.getSelectedRow() + 1) - pn.step) + 1);
				List<Note> trackNotes = workingPattern.getTrackNotes(pn.track,(pn.step + 1),newDuration);
				if (trackNotes.size()>0) {
					for (Note tn: trackNotes) {
						if (tn.step>pn.step) {
							newDuration = (tn.step - pn.step);
							break;
						}
					}
				}
				pn.duration = newDuration;
				changedPattern();
			}
		}
	}
	
	protected void updateWorkingPattern() {
		Pattern current = workingPattern;
		if (workingPattern==null || workingPattern.getNumber()!=selectedPattern) {
			if (compositionCopy!=null) {
				workingPattern = compositionCopy.getPattern(selectedPattern);
				if (workingPattern==null) {
					workingPattern = new Pattern();
					workingPattern.setNumber(pattern.getSelectedIndex());
					if (customBars.isSelected()) {
						workingPattern.setBars(bars.getSelectedIndex());
					}
					compositionCopy.getPatterns().add(workingPattern);
				}
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
				refreshGridData();
			}
		}
	}

	protected List<Note> getSelectedNotes() {
		List<Note> r = new ArrayList<Note>();
		if (workingPattern!=null) {
			int[] rows = grid.getSelectedRows();
			int[] cols = grid.getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				for (int row = rows[0]; row <= rows[(rows.length - 1)]; row++) {
					for (int col = cols[0]; col <= cols[(cols.length - 1)]; col++) {
						int track = col + 1;
						int step = row + 1;
						Note sn = workingPattern.getNote(track,step,1);
						if (sn!=null) {
							r.add(sn);
						}
					}
				}
			}
		}
		return r;
	}

	protected void changedPattern() {
		refreshGridData();
		if (workingPattern!=null) {
			// TODO: Publish change
			getController().getStateManager().changedPattern(this,workingPattern);
		}
	}

	protected void refreshGridData() {
		int[] rows = grid.getSelectedRows();
		int[] cols = grid.getSelectedColumns();
		gridController.fireTableDataChanged();
		if (rows.length>0 && cols.length>0) {
			grid.addRowSelectionInterval(rows[0],rows[(rows.length-1)]);
			grid.addColumnSelectionInterval(cols[0],cols[(cols.length-1)]);
		}
	}
	
	protected JScrollPane getPatternPanel() {
		gridController = new PatternGridController();
		PatternGridKeyListener keyListener = getController().getPatternKeyListener();
		keyListener.setPatternPanel(this);
		grid = new JTable();
		grid.setModel(gridController);
		grid.addKeyListener(keyListener);
		grid.addFocusListener(this);
		grid.setCellSelectionEnabled(true);
		grid.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		grid.setDefaultRenderer(Object.class, new PatternGridCellRenderer(gridController));
		JScrollPane r = new JScrollPane(grid,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
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
