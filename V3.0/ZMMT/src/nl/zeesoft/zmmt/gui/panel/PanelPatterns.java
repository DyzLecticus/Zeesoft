package nl.zeesoft.zmmt.gui.panel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Control;
import nl.zeesoft.zmmt.composition.Note;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;
import nl.zeesoft.zmmt.synthesizer.Instrument;

public class PanelPatterns extends PanelObject implements StateChangeSubscriber, MetaEventListener, SequencePlayerSubscriber, ListSelectionListener {
	public static final String		EDIT_NOTES						= "EDIT_NOTES";
	public static final String		EDIT_CONTROLS					= "EDIT_CONTROLS";

	public static final String		EDIT_EXPRESSION					= "EDIT_EXPRESSION";
	public static final String		EDIT_MODULATION					= "EDIT_MODULATION";
	public static final String		EDIT_FILTER						= "EDIT_FILTER";
	
	private static final String		PAGE_DOWN						= "PAGE_DOWN";
	private static final String		PAGE_UP							= "PAGE_UP";
	private static final String		SHIFT_PAGE_DOWN					= "SHIFT_PAGE_DOWN";
	private static final String		SHIFT_PAGE_UP					= "SHIFT_PAGE_UP";
	
	private JComboBox<String>		pattern							= null;
	private int						selectedPattern					= 0;
	
	private JRadioButton			editNotes						= null;
	private JRadioButton			editExpression					= null;
	private JRadioButton			editModulation					= null;
	private JRadioButton			editFilter						= null;
	private String					selectedEditMode				= "";

	private int						barsPerPattern					= 0;
	private JComboBox<String>		bars							= null;

	private JCheckBox				insertMode						= null;
	
	private JPanel					cardPanel						= null;
	private Grid					notesGrid						= null;
	private NotesGridController		notesGridController				= null;
	private NotesGridKeyListener	notesGridKeyListener			= null;
	private Grid					controlsGrid					= null;
	private ControlsGridController	controlsGridController			= null;
	private ControlsGridKeyListener	controlsGridKeyListener			= null;
	
	private	Composition				compositionCopy					= null;
	private Pattern					workingPattern					= null;
	private List<Note>				workingNotes					= new ArrayList<Note>();

	private List<Note>				copyNotes						= new ArrayList<Note>();
	private int						copySteps						= 0;
	private int						copyTracks						= 0;
	
	private int[]					selectedRows					= null;
	private int[]					selectedCols					= null;
	
	private boolean					clearedPlayingStep				= false;

	// TODO: Copy instrument controls when copying full track length pattern notes
	// TODO: Keyboard shortcuts for accent toggle and length increase
	
	public PanelPatterns(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		controller.addSequencerMetaListener(this);
		controller.addSequencerSubscriber(this);
		selectedEditMode = controller.getStateManager().getPatternEditMode();
		selectedPattern = controller.getStateManager().getSelectedPattern();
		notesGridKeyListener = new NotesGridKeyListener(controller,controller.getStateManager().getSettings().getKeyCodeNoteNumbers(),this);
		controlsGridKeyListener = new ControlsGridKeyListener(controller,controller.getStateManager().getSettings().getKeyCodeNoteNumbers(),this);
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
		addComponent(getPanel(), row, 0.99,getDetailsPanel());
	}

	@Override
	public void requestFocus() {
		if (selectedRows!=null && selectedCols!=null) {
			if (!getCurrentGrid().hasFocus()) {
				getCurrentGrid().requestFocus();
			} else {
				reselect();
			}
		} else {
			pattern.requestFocus();
		}
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (evt.getType().equals(StateChangeEvent.CHANGED_PATTERN_EDIT_MODE)) {
			selectedEditMode = evt.getPatternEditMode();
			if (selectedEditMode.equals(EDIT_NOTES) && !editNotes.isSelected()) {
				editNotes.doClick();
			} else if (selectedEditMode.equals(EDIT_EXPRESSION) && !editExpression.isSelected()) {
				editExpression.doClick();
			} else if (selectedEditMode.equals(EDIT_MODULATION) && !editModulation.isSelected()) {
				editModulation.doClick();
			} else if (selectedEditMode.equals(EDIT_FILTER) && !editFilter.isSelected()) {
				editFilter.doClick();
			}
			changedSelectedEditMode();
			getCurrentGrid().requestFocus();
		} else if (evt.getType().equals(StateChangeEvent.SELECTED_PATTERN)) {
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
			if (notesGridController.setLayout(
				compositionCopy.getBarsPerPattern(),
				compositionCopy.getBeatsPerBar(),
				compositionCopy.getStepsPerBeat()
				)) {
				notesGridController.fireTableStructureChanged();
			}
			if (controlsGridController.setLayout(
				compositionCopy.getBarsPerPattern(),
				compositionCopy.getBeatsPerBar(),
				compositionCopy.getStepsPerBeat()
				)) {
				controlsGridController.fireTableStructureChanged();
			}
		}
		setValidate(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getActionCommand().equals(FrameMain.PATTERN_EDIT_MODE)) {
			if (editNotes.isSelected()) {
				editExpression.doClick();
			} else if (editExpression.isSelected()) {
				editModulation.doClick();
			} else if (editModulation.isSelected()) {
				editFilter.doClick();
			} else if (editFilter.isSelected()) {
				editNotes.doClick();
			}
		} else if (
			evt.getActionCommand().equals(EDIT_NOTES) ||
			evt.getActionCommand().equals(EDIT_EXPRESSION) ||
			evt.getActionCommand().equals(EDIT_MODULATION) ||
			evt.getActionCommand().equals(EDIT_FILTER)
			) {
			getController().getStateManager().setPatternEditMode(this,evt.getActionCommand());
		} else if (
			evt.getActionCommand().equals(PAGE_DOWN) ||
			evt.getActionCommand().equals(SHIFT_PAGE_DOWN)
			) {
			handlePageDown(evt.getActionCommand());
		} else if (
			evt.getActionCommand().equals(PAGE_UP) ||
			evt.getActionCommand().equals(SHIFT_PAGE_UP)
			) {
			handlePageUp(evt.getActionCommand());
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_SELECT_NEXT)) {
			getController().getStateManager().selectNextPattern(this);
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_SELECT_PREV)) {
			getController().getStateManager().selectPreviousPattern(this);
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_SELECT)) {
			pattern.requestFocus();
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_INSERT)) {
			insertMode.doClick();
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_BARS)) {
			bars.requestFocus();
		} else if (evt.getActionCommand().equals(FrameMain.PATTERN_EDIT)) {
			if (!getCurrentGrid().hasFocus()) {
				getCurrentGrid().requestFocus();
			} else {
				reselect();
			}
		} else if (evt.getActionCommand().equals(FrameMain.NOTES_COPY)) {
			int[] rows = notesGrid.getSelectedRows();
			int[] cols = notesGrid.getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				int row = rows[0];
				int col = cols[0];
				copySteps = (rows[(rows.length - 1)] - rows[0]);
				copyTracks = (cols[(cols.length - 1)] - cols[0]);
				copyNotes.clear();
				List<Note> selNotes = getSelectedNotes();
				for (Note note: selNotes) {
					if (note.step>row) {
						Note copyNote = note.copy();
						copyNote.step = copyNote.step - row;
						copyNote.track = copyNote.track - col;
						copyNotes.add(copyNote);
					}
				}
			}
		} else if (evt.getActionCommand().equals(FrameMain.NOTES_PASTE)) {
			int[] rows = notesGrid.getSelectedRows();
			int[] cols = notesGrid.getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				boolean changed = false;
				int row = rows[0];
				int col = cols[0];
				int rowTo = row + copySteps;
				int colTo = col + copyTracks;
				if (insertMode.isSelected()) {
					if (rows.length>0 && cols.length>0) {
						if (moveNotes(col,colTo,row,(copySteps + 1))) {
							changed = true;
						}
					}
				} else {
					List<Note> removeNotes = getNotes(row,rowTo,col,colTo);
					removeOrCutNotes(removeNotes,row);
					if (removeNotes.size()>0) {
						changed = true;
					}
				}
				boolean addedNotes = false;
				for (Note note: copyNotes) {
					Note addNote = note.copy();
					addNote.step = addNote.step + row;
					addNote.track = addNote.track + col;
					if (addNote.step<=notesGrid.getRowCount() && addNote.track<=notesGrid.getColumnCount()) {
						addOrUpdateNote(addNote);
						addedNotes = true;
						changed = true;
					}
				}
				if (addedNotes) {
					removeOrCutNotes(getNotes(notesGrid.getRowCount(),notesGrid.getRowCount(),0,(notesGrid.getColumnCount() - 1)),notesGrid.getRowCount());
				}
				if ((rowTo + 1) < notesGrid.getRowCount()) {
					row = (rowTo + 1);
				} else {
					row = (notesGrid.getRowCount() - 1);
				}
				notesGrid.clearSelection();
				selectAndShow(notesGrid,row,row,col,col,true);
				if (changed) {
					changedPattern();
				}
			}
		} else if (evt.getSource()==pattern) {
			if (pattern.getSelectedIndex()!=selectedPattern) {
				selectedPattern = pattern.getSelectedIndex();
				getController().getStateManager().setSelectedPattern(this,selectedPattern);
			}
		} else if (evt.getSource()==bars) {
			if (workingPattern!=null && workingPattern.getBars()!=bars.getSelectedIndex()) {
				workingPattern.setBars(bars.getSelectedIndex());
				refreshGridData();
				getController().getStateManager().changedPattern(this,workingPattern);
			}
		}
	}

	@Override
	public void focusLost(FocusEvent evt) {
		super.focusLost(evt);
		if (evt.getSource() instanceof Grid) {
			workingNotes.clear();
			getCurrentGrid().clearSelection();
		}
	}

	@Override
	public void focusGained(FocusEvent evt) {
		super.focusGained(evt);
		if (evt.getSource() instanceof Grid) {
			reselect();
		}
	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType()==CompositionToSequenceConvertor.MARKER) {
			String txt = new String(meta.getData());
			if (txt.startsWith(CompositionToSequenceConvertor.PATTERN_STEP_MARKER)) {
				String[] d = txt.split(":");
				int pattern = Integer.parseInt(d[1]);
				int step = Integer.parseInt(d[2]);
				if (pattern==selectedPattern && step<=notesGrid.getRowCount()) {
					notesGridController.setPlayingStep(step);
					controlsGridController.setPlayingStep(step);
					getCurrentGrid().repaint();
					clearedPlayingStep = false;
				} else if (pattern!=selectedPattern && !clearedPlayingStep) {
					notesGridController.setPlayingStep(-1);
					controlsGridController.setPlayingStep(-1);
					getCurrentGrid().repaint();
					clearedPlayingStep = true;
				}
			}
		}
	}

	@Override
	public void started() {
		// Ignore
	}

	@Override
	public void stopped() {
		notesGridController.setPlayingStep(-1);
		controlsGridController.setPlayingStep(-1);
		getCurrentGrid().repaint();
		clearedPlayingStep = true;
	}

	@Override
	public void valueChanged(ListSelectionEvent evt) {
		int[] rows = getCurrentGrid().getSelectedRows();
		int[] cols = getCurrentGrid().getSelectedColumns();
		if (rows.length>0 && cols.length>0) {
			selectedRows = rows;
			selectedCols = cols;
			getController().getStateManager().setSelectedPatternSelection(
				this,
				selectedRows[0],
				selectedRows[selectedRows.length-1],
				selectedCols[0],
				selectedCols[selectedCols.length-1]
				);
		}
	}

	protected Grid getCurrentGrid() {
		Grid grid = null;
		if (selectedEditMode.equals(EDIT_NOTES)) {
			grid = notesGrid;
		} else if (
			selectedEditMode.equals(EDIT_EXPRESSION) ||
			selectedEditMode.equals(EDIT_MODULATION) ||
			selectedEditMode.equals(EDIT_FILTER)
			) {
			grid = controlsGrid;
		}
		return grid;
	}
	
	protected int getSelectedControl() {
		int r = -1;
		if (selectedEditMode.equals(EDIT_EXPRESSION)) {
			r = Control.EXPRESSION;
		} else if (selectedEditMode.equals(EDIT_MODULATION)) {
			r = Control.MODULATION;
		} else if (selectedEditMode.equals(EDIT_FILTER)) {
			r = Control.FILTER;
		}
		return r;
	}

	protected void handlePageDown(String actionCommand) {
		if (compositionCopy!=null) {
			getCurrentGrid().handlePageDown(compositionCopy.getStepsPerBar(),actionCommand.equals(SHIFT_PAGE_DOWN));
			changedSelection();
		}
	}
	
	protected void handlePageUp(String actionCommand) {
		if (compositionCopy!=null) {
			getCurrentGrid().handlePageUp(compositionCopy.getStepsPerBar(),actionCommand.equals(SHIFT_PAGE_UP));
			changedSelection();
		}
	}

	protected void reselect() {
		if (selectedEditMode.equals(EDIT_NOTES)) {
			reselect(notesGrid,notesGridController);
		} else if (
			selectedEditMode.equals(EDIT_EXPRESSION) ||
			selectedEditMode.equals(EDIT_MODULATION) ||
			selectedEditMode.equals(EDIT_FILTER)
			) {
			reselect(controlsGrid,controlsGridController);
		}
	}

	protected void reselect(Grid grid,NotesGridController controller) {
		if (selectedRows!=null && selectedRows.length>0 && selectedCols!=null && selectedCols.length>0) {
			int rowFrom = selectedRows[0];
			int rowTo = selectedRows[(selectedRows.length - 1)];
			int colFrom = selectedCols[0];
			int colTo = selectedCols[(selectedCols.length - 1)];
			selectAndShow(grid,rowFrom,rowTo,colFrom,colTo,true);
		}
	}

	protected void selectAndShow(Grid grid,int rowFrom, int rowTo, int colFrom, int colTo,boolean showTo) {
		grid.selectAndShow(rowFrom,rowTo,colFrom,colTo,showTo);
		changedSelection();
	}

	protected void changedSelection() {
		int[] rows = getCurrentGrid().getSelectedRows();
		int[] cols = getCurrentGrid().getSelectedColumns();
		changedSelection(rows[0],rows[(rows.length - 1)],cols[0],cols[(cols.length - 1)]);
	}

	protected void changedSelection(int rowFrom, int rowTo, int colFrom, int colTo) {
		selectedRows = new int[2];
		selectedRows[0] = rowFrom;
		selectedRows[1] = rowTo;
		selectedCols = new int[2];
		selectedCols[0] = colFrom;
		selectedCols[1] = colTo;
		getController().getStateManager().setSelectedPatternSelection(
			this,
			selectedRows[0],
			selectedRows[selectedRows.length-1],
			selectedCols[0],
			selectedCols[selectedCols.length-1]
			);
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
	
	protected void shiftSelectedControlsPercentage(int mod) {
		boolean changed = false;
		List<Control> scs = getSelectedControls();
		for (Control sc: scs) {
			if (mod>0 && sc.percentage<100) {
				changed = true;
				sc.percentage = sc.percentage + mod;
				if (sc.percentage>100) {
					sc.percentage=100;
				}
			} else if (mod<0 && sc.percentage>0) {
				changed = true;
				sc.percentage = sc.percentage - (mod * -1);
				if (sc.percentage<0) {
					sc.percentage=0;
				}
			}
		}
		if (changed) {
			changedPattern();
		}
	}

	protected void removeSelectedNotes() {
		List<Note> sns = getSelectedNotes();
		removeOrCutNotes(sns,notesGrid.getSelectedRow());
		boolean changed = sns.size()>0; 
		if (insertMode.isSelected()) {
			int[] rows = notesGrid.getSelectedRows();
			int[] cols = notesGrid.getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				int mod = ((rows[(rows.length - 1)] - rows[0]) + 1) * -1;
				if (moveNotes(cols[0],cols[(cols.length - 1)],rows[0],mod)) {
					changed = true;
				}
			}
		}
		if (changed) {
			changedPattern();
		}
	}

	protected void removeSelectedControls() {
		List<Control> scs = getSelectedControls();
		for (Control c: scs) {
			workingPattern.getControls().remove(c);
		}
		if (scs.size()>0) {
			changedPattern();
		}
	}

	protected void playNote(int note, boolean accent) {
		if (notesGrid.getSelectedColumn()>=0 && notesGrid.getSelectedRow()>=0) {
			Note pn = null;
			for (Note wn: workingNotes) {
				if (wn.note==note) {
					pn = wn;
					break;
				}
			}
			if (pn==null) {
				String instrument = "";
				if (compositionCopy!=null && instrument.equals(Instrument.ECHO)) {
					instrument = compositionCopy.getSynthesizerConfiguration().getEcho().getInstrument();
				}
				if (instrument.length()==0) {
					instrument = getController().getStateManager().getSelectedInstrument();
				}
				pn = new Note();
				pn.note = note;
				getController().getStateManager().getSelectedInstrument();
				pn.instrument = instrument;
				pn.track = notesGrid.getSelectedColumn() + 1 + workingNotes.size();
				pn.step = notesGrid.getSelectedRow() + 1;
				pn.accent = accent;
				Note patternNote = addOrUpdateNote(pn);
				workingNotes.add(patternNote);
				changedPattern();
			}
		}
	}

	protected void stopNote(int note) {
		List<Note> removeNotes = new ArrayList<Note>();
		for (Note wn: workingNotes) {
			if (wn.note==note) {
				removeNotes.add(wn);
			}
		}
		if (removeNotes.size()>0) {
			boolean changed = false;
			for (Note pn: removeNotes) {
				workingNotes.remove(pn);
				int[] rows = notesGrid.getSelectedRows();
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
							changed = true;
						}
					}
				}
			}
			if (changed) {
				changedPattern();
			}
		}
	}
	
	protected void insertSpace() {
		boolean changed = false;
		if (insertMode.isSelected()) {
			int[] rows = getCurrentGrid().getSelectedRows();
			int[] cols = getCurrentGrid().getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				int mod = 1;
				if (getCurrentGrid()==notesGrid) {
					if (moveNotes(cols[0],cols[(cols.length - 1)],rows[0],mod)) {
						changed = true;
					}
				}
			}
		} else if (getCurrentGrid()==controlsGrid) {
			int[] cols = controlsGrid.getSelectedColumns();
			if (cols.length>0 && controlsGrid.getSelectedRow()>=0) {
				int control =  getSelectedControl();
				int step = (controlsGrid.getSelectedRow() + 1);
				for (int col = cols[0]; col <= cols[(cols.length - 1)]; col++) {
					String instrument = Instrument.INSTRUMENTS[col];
					Control ctrl = workingPattern.getInstrumentControl(instrument,control,step);
					if (ctrl==null) {
						ctrl = new Control();
						ctrl.instrument = instrument;
						ctrl.control = control;
						ctrl.step = step;
						if (control==Control.EXPRESSION) {
							ctrl.percentage = 100;
						} else if (control==Control.FILTER) {
							ctrl.percentage = 100;
						}
						workingPattern.getControls().add(ctrl);
						changed = true;
					}
				}
			}
		}
		if (changed) {
			changedPattern();
		}
	}
	
	protected void updateWorkingPattern() {
		Pattern current = workingPattern;
		if (workingPattern==null || workingPattern.getNumber()!=selectedPattern) {
			if (compositionCopy!=null) {
				workingPattern = compositionCopy.getPattern(selectedPattern);
				if (workingPattern==null) {
					workingPattern = new Pattern();
					workingPattern.setNumber(selectedPattern);
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
				notesGridController.setWorkingPattern(workingPattern);
				controlsGridController.setWorkingPattern(workingPattern);
				refreshGridData();
			}
		}
	}

	protected List<Note> getSelectedNotes() {
		List<Note> r = new ArrayList<Note>();
		if (workingPattern!=null) {
			int[] rows = notesGrid.getSelectedRows();
			int[] cols = notesGrid.getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				r = getNotes(rows[0],rows[(rows.length - 1)],cols[0],cols[(cols.length - 1)]);
			}
		}
		return r;
	}

	protected List<Control> getSelectedControls() {
		List<Control> r = new ArrayList<Control>();
		if (workingPattern!=null) {
			int[] rows = controlsGrid.getSelectedRows();
			int[] cols = controlsGrid.getSelectedColumns();
			if (rows.length>0 && cols.length>0) {
				int stepFrom = (rows[0] + 1);
				int stepTo = (rows[(rows.length - 1)] + 1);
				for (int col = cols[0]; col <= cols[(cols.length - 1)]; col++) {
					String instrument = Instrument.INSTRUMENTS[col];
					List<Control> ics = workingPattern.getInstrumentControls(instrument,getSelectedControl(),stepFrom,stepTo);
					for (Control c: ics) {
						r.add(c);
					}
				}
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
	
	protected Note addOrUpdateNote(Note note) {
		Note patternNote = workingPattern.getNote(note.track,note.step,note.duration);				
		if (patternNote!=null) {
			if (patternNote.step<note.step) {
				patternNote.duration = (note.step - patternNote.step);
				patternNote = null;
			} else if (patternNote.step>note.step) {
				patternNote = null;
			}
		}
		if (patternNote==null) {
			if (note.duration>1) {
				List<Note> trackNotes = workingPattern.getTrackNotes(note.track,(note.step + 1),(note.duration - 1));
				if (trackNotes.size()>0) {
					note.duration = (trackNotes.get(0).step - note.step);
				}
			}
			patternNote = note;
			workingPattern.getNotes().add(note);
		} else if (patternNote.step==note.step) {
			patternNote.instrument = note.instrument;
			patternNote.note = note.note;
			patternNote.accent = note.accent;
			patternNote.velocityPercentage = 100;
		}
		return patternNote;
	}

	protected boolean moveNotes(int colFrom,int colTo,int row,int mod) {
		boolean changed = false;
		if (mod!=0 && workingPattern!=null) {
			for (int col = colFrom; col<=colTo; col++) {
				List<Note> trackNotes = workingPattern.getTrackNotes(col+1,row+1,notesGrid.getRowCount());
				if (trackNotes.size()>0) {
					for (Note note: trackNotes) {
						if (note.step>row) {
							if (mod > 0) {
								note.step = note.step + mod;
								if (note.step + (note.duration - 1) > notesGrid.getRowCount()) {
									note.duration = (notesGrid.getRowCount() - note.step) + 1; 
								}
							} else if (mod < 0) {
								note.step = note.step - (mod * -1);
							}
							if (note.step<1 || note.step>notesGrid.getRowCount()) {
								workingPattern.getNotes().remove(note);
							}
						} else {
							note.duration = (row + 1) - note.step;
						}
					}
					changed = true;
				}
			}
		}
		return changed;
	}
	
	protected void changedPattern() {
		refreshGridData(0,(getCurrentGrid().getRowCount() - 1));
		if (workingPattern!=null) {
			getController().getStateManager().changedPattern(this,workingPattern);
		}
	}

	protected void refreshGridData() {
		refreshGridData(-1,-1);
	}
	
	protected void refreshGridData(int rowFrom, int rowTo) {
		NotesGridController controller = notesGridController;
		if (getCurrentGrid()==controlsGrid) {
			controller = controlsGridController;
		}
		if (rowFrom>=0 && rowTo>=0) {
			controller.fireTableRowsUpdated(rowFrom,rowTo);
		} else {
			int[] rows = getCurrentGrid().getSelectedRows();
			int[] cols = getCurrentGrid().getSelectedColumns();
			controller.fireTableDataChanged();
			if (rows.length>0 && cols.length>0) {
				getCurrentGrid().addRowSelectionInterval(rows[0],rows[(rows.length-1)]);
				getCurrentGrid().addColumnSelectionInterval(cols[0],cols[(cols.length-1)]);
			}
		}
	}
	
	protected void changedSelectedEditMode() {
		CardLayout layout = (CardLayout) cardPanel.getLayout();
		String show = EDIT_NOTES;
		if (
			selectedEditMode.equals(EDIT_EXPRESSION) ||
			selectedEditMode.equals(EDIT_MODULATION) ||
			selectedEditMode.equals(EDIT_FILTER)
			) {
			show = EDIT_CONTROLS;
			if (controlsGridController.setSelectedControl(getSelectedControl())) {
				refreshGridData();
			}
		}
		layout.show(cardPanel,show);
	}

	protected void initializeNotesGrid() {
		notesGridController = new NotesGridController();
		notesGrid = new Grid();
		notesGrid.setModel(notesGridController);
		notesGrid.addKeyListener(notesGridKeyListener);
		notesGrid.addFocusListener(this);
		notesGrid.setCellSelectionEnabled(true);
		notesGrid.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		notesGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		notesGrid.setDefaultRenderer(Object.class, new NotesGridCellRenderer(notesGridController));
		notesGrid.getSelectionModel().addListSelectionListener(this);
		notesGrid.getTableHeader().setReorderingAllowed(false);
		notesGrid.getTableHeader().setResizingAllowed(false);

		int height = getController().getStateManager().getSettings().getCustomRowHeight();
		if (height>0) {
			notesGrid.setRowHeight(height);
		}
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK,false);
		notesGrid.registerKeyboardAction(this,FrameMain.NOTES_COPY,stroke,JComponent.WHEN_FOCUSED);
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
		notesGrid.registerKeyboardAction(this,FrameMain.NOTES_PASTE,stroke,JComponent.WHEN_FOCUSED);
		
		addKeyStrokeOverridesToGrid(notesGrid);
	}

	protected void initializeControlsGrid() {
		controlsGridController = new ControlsGridController();
		controlsGrid = new Grid();
		controlsGrid.setModel(controlsGridController);
		controlsGrid.addKeyListener(controlsGridKeyListener);
		controlsGrid.addFocusListener(this);
		controlsGrid.setCellSelectionEnabled(true);
		controlsGrid.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		controlsGrid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		controlsGrid.setDefaultRenderer(Object.class, new ControlsGridCellRenderer(controlsGridController));
		controlsGrid.getSelectionModel().addListSelectionListener(this);
		controlsGrid.getTableHeader().setReorderingAllowed(false);
		controlsGrid.getTableHeader().setResizingAllowed(false);
		
		int height = getController().getStateManager().getSettings().getCustomRowHeight();
		if (height>0) {
			controlsGrid.setRowHeight(height);
		}
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK,false);
		controlsGrid.registerKeyboardAction(this,FrameMain.NOTES_COPY,stroke,JComponent.WHEN_FOCUSED);
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
		controlsGrid.registerKeyboardAction(this,FrameMain.NOTES_PASTE,stroke,JComponent.WHEN_FOCUSED);
		
		addKeyStrokeOverridesToGrid(controlsGrid);
	}
	
	protected void addKeyStrokeOverridesToGrid(Grid grid) {
		KeyStroke stroke = null; 
	
		addFunctionKeyOverridesToComponent(grid);

		// Page down override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,0,false);
		grid.registerKeyboardAction(this,PAGE_DOWN,stroke,JComponent.WHEN_FOCUSED);

		// Page up override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,0,false);
		grid.registerKeyboardAction(this,PAGE_UP,stroke,JComponent.WHEN_FOCUSED);

		// Shift page down override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN,ActionEvent.SHIFT_MASK,false);
		grid.registerKeyboardAction(this,SHIFT_PAGE_DOWN,stroke,JComponent.WHEN_FOCUSED);

		// Shift page up override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP,ActionEvent.SHIFT_MASK,false);
		grid.registerKeyboardAction(this,SHIFT_PAGE_UP,stroke,JComponent.WHEN_FOCUSED);
		
		addControlPageUpDownOverridesToComponent(grid);
	}
	
	protected JComboBox<String> getPatternSelector() {
		JComboBox<String> r = new JComboBox<String>();
		for (int p = 0; p <= 99; p++) {
			r.addItem(String.format("%02d",p));
		}
		r.setSelectedIndex(selectedPattern);
		r.addActionListener(this);

		addFunctionKeyOverridesToComponent(r);
		addControlPageUpDownOverridesToComponent(r);
		
		// Enter override
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false);
		r.registerKeyboardAction(this,FrameMain.PATTERN_EDIT,stroke,JComponent.WHEN_FOCUSED);

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
		
		addFunctionKeyOverridesToComponent(r);
		addControlPageUpDownOverridesToComponent(r);

		// Enter override
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,0,false);
		r.registerKeyboardAction(this,FrameMain.PATTERN_EDIT,stroke,JComponent.WHEN_FOCUSED);

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

		editNotes = new JRadioButton("Notes");
		editNotes.setActionCommand(EDIT_NOTES);
		
		editExpression = new JRadioButton("Volume");
		editExpression.setActionCommand(EDIT_EXPRESSION);

		editModulation = new JRadioButton("Modulation");
		editModulation.setActionCommand(EDIT_MODULATION);

		editFilter = new JRadioButton("Filter");
		editFilter.setActionCommand(EDIT_FILTER);

		addControlPageUpDownOverridesToComponent(editNotes);
		addControlPageUpDownOverridesToComponent(editExpression);
		addControlPageUpDownOverridesToComponent(editModulation);
		addControlPageUpDownOverridesToComponent(editFilter);
		
		ButtonGroup group = new ButtonGroup();
		group.add(editNotes);
		group.add(editExpression);
		group.add(editModulation);
		group.add(editFilter);
		
		if (selectedEditMode.equals(EDIT_NOTES)) {
			editNotes.doClick();
		} else if (selectedEditMode.equals(EDIT_EXPRESSION)) {
			editExpression.doClick();
		} else if (selectedEditMode.equals(EDIT_MODULATION)) {
			editModulation.doClick();
		} else if (selectedEditMode.equals(EDIT_FILTER)) {
			editFilter.doClick();
		}

		editNotes.addActionListener(this);
		editExpression.addActionListener(this);
		editModulation.addActionListener(this);
		editFilter.addActionListener(this);
		
		JPanel edit = new JPanel();
		edit.setLayout(new BoxLayout(edit,BoxLayout.X_AXIS));
		edit.add(editNotes);
		edit.add(editExpression);
		edit.add(editModulation);
		edit.add(editFilter);
		
		JPanel wrapper = new JPanel();
		wrapper.setLayout(new GridBagLayout());
		wrapper.add(edit);
		
		r.add(wrapper,BorderLayout.CENTER);
		
		insertMode = new JCheckBox("Insert ");
		insertMode.addKeyListener(getController().getPlayerKeyListener());
		addControlPageUpDownOverridesToComponent(insertMode);
		r.add(insertMode,BorderLayout.LINE_END);
		
		return r;
	}

	protected JScrollPane getNotesPanel() {
		initializeNotesGrid();
		
		JScrollPane r = new JScrollPane(notesGrid,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		r.getVerticalScrollBar().setUnitIncrement(20);
		return r;
	}

	protected JScrollPane getControlsPanel() {
		initializeControlsGrid();
		
		JScrollPane r = new JScrollPane(controlsGrid,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		r.getVerticalScrollBar().setUnitIncrement(20);
		return r;
	}

	protected JPanel getDetailsPanel() {
		JPanel r = new JPanel();
		r.setLayout(new GridBagLayout());
		r.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		int row = 0;
		
		bars = getBarsSelector();
		addLabelProperty(r,row,"Custom pattern bars",bars);

		cardPanel = new JPanel();
		CardLayout layout = new CardLayout();
		cardPanel.setLayout(layout);

		cardPanel.add(getNotesPanel(),EDIT_NOTES);
		cardPanel.add(getControlsPanel(),EDIT_CONTROLS);

		row++;
		addComponent(r,row,0.99,cardPanel,true);
		
		changedSelectedEditMode();
		
		return r;
	}
}
