package nl.zeesoft.zmmt.gui.panel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.syntesizer.Instrument;

public class PanelPatterns extends PanelObject implements ActionListener, StateChangeSubscriber {
	private JComboBox<String>		pattern							= null;
	private int						selectedPattern					= 0;

	private int						barsPerPattern					= 0;
	private JComboBox<String>		bars							= null;

	private JCheckBox				insertMode						= null;
	
	private JTable					grid							= null;
	private PatternGridController	gridController					= null;
	
	private	Composition				compositionCopy					= null;
	private Pattern					workingPattern					= null;
	private List<Note>				workingNotes					= new ArrayList<Note>();

	private List<Note>				copyNotes						= new ArrayList<Note>();
	private int						copySteps						= 0;
	private int						copyTracks						= 0;
	
	private int[]					selectedRows					= null;
	private int[]					selectedCols					= null;

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
		if (reselect()) {
			grid.requestFocus();
		} else {
			pattern.requestFocus();
		}
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
			selectedPattern = evt.getSelectedPattern();
			pattern.setSelectedIndex(selectedPattern);
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
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(FrameMain.PATTERN_SELECT)) {
			pattern.requestFocus();
			pattern.showPopup();
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_INSERT)) {
			insertMode.doClick();
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_EDIT)) {
			if (!grid.hasFocus()) {
				reselect();
				grid.requestFocus();
			}
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_COPY)) {
			int[] rows = grid.getSelectedRows();
			int[] cols = grid.getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				int row = rows[0];
				int col = cols[0];
				copySteps = (rows[(rows.length - 1)] - rows[0]);
				copyTracks = (cols[(cols.length - 1)] - cols[0]);
				copyNotes.clear();
				List<Note> selNotes = getSelectedNotes();
				for (Note note: selNotes) {
					if (note.step>row) {
						copyNotes.add(note.copy());
					}
				}
				for (Note note: copyNotes) {
					note.step = note.step - row;
					note.track = note.track - col;
				}
			}
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_PASTE)) {
			int[] rows = grid.getSelectedRows();
			int[] cols = grid.getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				int row = rows[0];
				int col = cols[0];
				int rowTo = row + copySteps;
				int colTo = row + copyTracks;
				removeOrCutNotes(getNotes(row,rowTo,col,colTo),row);
				for (Note note: copyNotes) {
					Note addNote = note.copy();
					addNote.step = addNote.step + row;
					addNote.track = addNote.track + col;
					if (addNote.step<=grid.getRowCount() && addNote.track<=grid.getColumnCount()) {
						workingPattern.getNotes().add(addNote);
					}
				}
				removeOrCutNotes(getNotes(grid.getRowCount(),grid.getRowCount(),0,(grid.getColumnCount() - 1)),grid.getRowCount());
				if ((rowTo + 1) < grid.getRowCount()) {
					row = (rowTo + 1);
				} else {
					row = (grid.getRowCount() - 1);
				}
				grid.clearSelection();
				selectAndShow(row,row,col,col);
				changedPattern();
			}
		} else if (evt.getSource()==pattern) {
			if (pattern.getSelectedIndex()!=selectedPattern) {
				selectedPattern = pattern.getSelectedIndex();
				getController().getStateManager().setSelectedPattern(this,selectedPattern);
			}
		} else if (evt.getSource()==bars) {
			if (workingPattern!=null && workingPattern.getBars()!=bars.getSelectedIndex()) {
				workingPattern.setBars(bars.getSelectedIndex());
				changedPattern();
			}
		}
	}

	@Override
	public void focusLost(FocusEvent evt) {
		super.focusLost(evt);
		if (evt.getSource()==grid) {
			workingNotes.clear();
			selectedRows = grid.getSelectedRows();
			selectedCols = grid.getSelectedColumns();
			grid.clearSelection();
		}
	}

	protected boolean reselect() {
		boolean selected = false;
		if (selectedRows!=null && selectedRows.length>0 && selectedCols!=null && selectedCols.length>0) {
			selectAndShow(
				selectedRows[0],selectedRows[(selectedRows.length - 1)],
				selectedCols[0],selectedCols[(selectedCols.length - 1)]
				);
			selected = true;
		}
		return selected;
	}

	protected void selectAndShow(int rowFrom, int rowTo, int colFrom, int colTo) {
		grid.addRowSelectionInterval(rowFrom,rowTo);
		grid.addColumnSelectionInterval(colFrom,colTo);
		Rectangle rect = grid.getCellRect(rowFrom,colFrom,true);
		rect.height = rect.height + 20;
		rect.width = rect.width + 100;
		grid.scrollRectToVisible(rect);
	}
	
	protected void shiftSelectedNotesNote(int mod) {
		boolean changed = false;
		List<Note> sns = getSelectedNotes();
		for (Note sn: sns) {
			if (mod>0 && sn.note<127) {
				changed = true;
				sn.note = sn.note + mod;
				if (sn.note>127) {
					sn.note=127;
				}
			} else if (mod<0 && sn.note>0) {
				changed = true;
				sn.note = sn.note - (mod * -1);
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
				sn.velocityPercentage = sn.velocityPercentage + mod;
				if (sn.velocityPercentage>100) {
					sn.velocityPercentage=100;
				}
			} else if (mod<0 && sn.velocityPercentage>0) {
				changed = true;
				sn.velocityPercentage = sn.velocityPercentage - (mod * -1);
				if (sn.velocityPercentage<0) {
					sn.velocityPercentage=0;
				}
			}
		}
		if (changed) {
			changedPattern();
		}
	}

	protected void setSelectedNotesInstrument(int index) {
		List<Note> sns = getSelectedNotes();
		for (Note sn: sns) {
			sn.instrument = Instrument.INSTRUMENTS[index];
		}
		if (sns.size()>0) {
			changedPattern();
		}
	}

	protected void removeSelectedNotes() {
		List<Note> sns = getSelectedNotes();
		removeOrCutNotes(sns,grid.getSelectedRow());
		if (sns.size()>0) {
			changedPattern();
		}
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
			int[] rows = grid.getSelectedRows();
			if (rows.length>0) {
				int lastRow = rows[(rows.length - 1)];
				if ((lastRow + 1)>pn.step) {
					int newDuration = (((lastRow + 1) - pn.step) + 1);
					List<Note> trackNotes = workingPattern.getTrackNotes(pn.track,(pn.step + 1),newDuration);
					if (trackNotes.size()>0) {
						for (Note tn: trackNotes) {
							if (tn.step>pn.step) {
								newDuration = (tn.step - pn.step);
								break;
							}
						}
					}
					if (pn.duration!=newDuration) {
						pn.duration = newDuration;
						changedPattern();
					}
				}
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
					workingPattern.setBars(bars.getSelectedIndex());
					compositionCopy.getPatterns().add(workingPattern);
				}
			} else {
				workingPattern = null;
			}
			
			if (workingPattern!=null && bars.getSelectedIndex()!=workingPattern.getBars()) {
				bars.setSelectedIndex(workingPattern.getBars());
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
				r = getNotes(rows[0],rows[(rows.length - 1)],cols[0],cols[(cols.length - 1)]);
			}
		}
		return r;
	}

	protected List<Note> getNotes(int rowFrom, int rowTo, int colFrom, int colTo) {
		List<Note> r = new ArrayList<Note>();
		if (workingPattern!=null) {
			for (int row = rowFrom; row <= rowTo; row++) {
				for (int col = colFrom; col <= colTo; col++) {
					int track = col + 1;
					int step = row + 1;
					Note sn = workingPattern.getNote(track,step,1);
					if (sn!=null && !r.contains(sn)) {
						r.add(sn);
					}
				}
			}
		}
		return r;
	}

	protected void removeOrCutNotes(List<Note> notes,int removeStartRow) {
		for (Note sn: notes) {
			if (sn.step>removeStartRow) {
				workingPattern.getNotes().remove(sn);
			} else {
				sn.duration = ((removeStartRow - sn.step) + 1);
			}
		}
	}
	
	protected void changedPattern() {
		refreshGridData();
		if (workingPattern!=null) {
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
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK, false);
		grid.registerKeyboardAction(this,FrameMain.PATTERN_COPY,stroke,JComponent.WHEN_FOCUSED);
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK, false);
		grid.registerKeyboardAction(this,FrameMain.PATTERN_PASTE,stroke,JComponent.WHEN_FOCUSED);
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
		labelProp.add(new JLabel("Custom bars"));
		bars = getBarsSelector();
		labelProp.add(bars);
		r.add(labelProp,BorderLayout.LINE_START);
		
		return r;
	}
}
