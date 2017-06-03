package nl.zeesoft.zmmt.gui.panel;

import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;

public class PanelSequence extends PanelObject implements ActionListener, StateChangeSubscriber, MetaEventListener, SequencePlayerSubscriber, ListSelectionListener {
	private Grid					grid							= null;
	private SequenceGridController	gridController					= null;
	
	private List<Pattern>			compositionPatternsCopy			= new ArrayList<Pattern>();
	private List<Integer>			workingSequence					= new ArrayList<Integer>();
	
	private int[]					selectedRows					= null;
	private int[]					selectedCols					= null;

	public PanelSequence(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		controller.addSequencerMetaListener(this);
		controller.addSequencerSubscriber(this);
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		int row = 0;
		addComponent(getPanel(), row, 0.01,getSequencePanel());
	}

	@Override
	public void requestFocus() {
		grid.requestFocus();
	}

	@Override
	public void handleStateChange(StateChangeEvent evt) {
		setValidate(false);
		if (evt.getSource()!=this && evt.getType().equals(StateChangeEvent.CHANGED_COMPOSITION)) {
			workingSequence = new ArrayList<Integer>(evt.getComposition().getSequence());
			compositionPatternsCopy.clear();
			for (Pattern ptn: evt.getComposition().getPatterns()) {
				compositionPatternsCopy.add(ptn.copy());
			}
			gridController.setWorkingSequenceAndPatterns(workingSequence,compositionPatternsCopy);
			gridController.fireTableDataChanged();
		}
		setValidate(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().equals(F2_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_INSTRUMENTS);
		} else if (evt.getActionCommand().equals(F3_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_PATTERNS);
		} else if (evt.getActionCommand().equals(F4_PRESSED)) {
			getController().getStateManager().setSelectedTab(this,FrameMain.TAB_SEQUENCE);
		} else if (evt.getActionCommand().equals(FrameMain.STOP_PLAYING)) {
			getController().stopSequencer();
		}
	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType()==CompositionToSequenceConvertor.MARKER) {
			// TODO: Implement
		}
	}

	@Override
	public void started() {
		// Ignore
	}

	@Override
	public void stopped() {
		// TODO: Implement
	}

	@Override
	public void valueChanged(ListSelectionEvent evt) {
		int[] rows = grid.getSelectedRows();
		int[] cols = grid.getSelectedColumns();
		if (rows.length>0 && cols.length>0) {
			selectedRows = rows;
			selectedCols = cols;
		}
	}

	@Override
	public void focusLost(FocusEvent evt) {
		super.focusLost(evt);
		if (evt.getSource()==grid) {
			grid.clearSelection();
		}
	}

	@Override
	public void focusGained(FocusEvent evt) {
		super.focusGained(evt);
		if (evt.getSource()==grid) {
			reselect();
		}
	}
	
	protected void insertPatterns() {
		int[] rows = grid.getSelectedRows();
		int num = 0;
		int add = 1;
		int rowFrom = 0;
		int rowTo = 0;
		if (rows.length>0) {
			rowFrom = rows[0];
			rowTo = rows[(rows.length - 1)];
			add = (rowTo - rowFrom) + 1;
			num = workingSequence.get(rowFrom);
		}
		if (add>0) {
			for (int i = 0; i < add; i++) {
				workingSequence.add(rowFrom,num);
			}
			changedSequence(rowFrom,rowTo);
		}
	}
	
	protected void shiftSeletectPatterns(int mod) {
		if (mod!=0) {
			int[] rows = grid.getSelectedRows();
			if (rows.length>0) {
				int rowFrom = rows[0];
				int rowTo = rows[(rows.length - 1)];
				for (int i = rowFrom; i <= rowTo; i++) {
					int num = workingSequence.get(i);
					if (mod>0) {
						num = num + mod;
					} else if (mod<0) {
						num = num - (mod * -1);
						if (num<0) {
							num = 0;
						}
					}
					workingSequence.set(i,num);
				}
				changedSequence(rowFrom,rowTo);
			}
		}
	}

	protected void removeSelectedPatterns() {
		int[] rows = grid.getSelectedRows();
		int rowFrom = 0;
		int rowTo = 0;
		if (rows.length>0) {
			rowFrom = rows[0];
			rowTo = rows[(rows.length - 1)];
			for (int i = 0; i < ((rowTo - rowFrom) + 1); i++) {
				workingSequence.remove(rowFrom);
			}
			changedSequence(rowFrom,rowTo);
		}
	}
	
	protected void changedSequence(int rowFrom,int rowTo) {
		if (gridController.setWorkingSequence(workingSequence)) {
			gridController.fireTableRowsDeleted(rowFrom,rowTo);
			getController().getStateManager().changedSequence(this,workingSequence);
			reselect();
		}
	}
	
	protected boolean reselect() {
		boolean selected = false;
		if (selectedRows!=null && selectedRows.length>0 && selectedCols!=null && selectedCols.length>0) {
			int rowFrom = selectedRows[0];
			int rowTo = selectedRows[(selectedRows.length - 1)];
			int max = (gridController.getRowCount() - 1);
			if (rowFrom>max) {
				rowFrom = max;
			}
			if (rowTo>max) {
				rowTo = max;
			}
			selectAndShow(
				rowFrom,rowTo,
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

	protected JScrollPane getSequencePanel() {
		gridController = new SequenceGridController();
		SequenceGridKeyListener keyListener = getController().getSequenceKeyListener();
		keyListener.setSequencePanel(this);
		grid = new Grid();
		grid.addKeyListener(keyListener);
		grid.setModel(gridController);
		grid.addFocusListener(this);
		grid.setColumnSelectionAllowed(false);
		grid.setRowSelectionAllowed(true);
		grid.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		grid.getSelectionModel().addListSelectionListener(this);
		grid.setDefaultRenderer(Object.class,new SequenceGridCellRenderer(gridController));
		
		int height = getController().getStateManager().getSettings().getCustomRowHeight();
		if (height>0) {
			grid.setRowHeight(height);
		}
		
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK,false);
		grid.registerKeyboardAction(this,FrameMain.PATTERN_COPY,stroke,JComponent.WHEN_FOCUSED);
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_V,ActionEvent.CTRL_MASK,false);
		grid.registerKeyboardAction(this,FrameMain.PATTERN_PASTE,stroke,JComponent.WHEN_FOCUSED);
		JScrollPane r = new JScrollPane(grid,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		// F2 Override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F2,0,false);
		grid.registerKeyboardAction(this,F2_PRESSED,stroke,JComponent.WHEN_FOCUSED);
		// F3 Override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F3,0,false);
		grid.registerKeyboardAction(this,F3_PRESSED,stroke,JComponent.WHEN_FOCUSED);
		// F4 Override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F4,0,false);
		grid.registerKeyboardAction(this,F4_PRESSED,stroke,JComponent.WHEN_FOCUSED);

		// F8 Override
		stroke = KeyStroke.getKeyStroke(KeyEvent.VK_F8,0,false);
		grid.registerKeyboardAction(this,FrameMain.STOP_PLAYING,stroke,JComponent.WHEN_FOCUSED);
		
		r.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		r.getVerticalScrollBar().setUnitIncrement(20);
		return r;
	}
}
