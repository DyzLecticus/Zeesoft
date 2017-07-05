package nl.zeesoft.zeetracker.gui.panel;

import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nl.zeesoft.zeetracker.gui.Controller;
import nl.zeesoft.zeetracker.gui.state.StateChangeEvent;
import nl.zeesoft.zeetracker.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;

public class PanelSequence extends PanelObject implements StateChangeSubscriber, MetaEventListener, SequencePlayerSubscriber, ListSelectionListener {
	public static final String		INSERT						= "INSERT";
	public static final String		REMOVE						= "REMOVE";
	public static final String		SEQUENCE_PATTERN_UP_1		= "SEQUENCE_PATTERN_UP_1";
	public static final String		SEQUENCE_PATTERN_DOWN_1		= "SEQUENCE_PATTERN_DOWN_1";
	public static final String		SEQUENCE_PATTERN_UP_10		= "SEQUENCE_PATTERN_UP_10";
	public static final String		SEQUENCE_PATTERN_DOWN_10	= "SEQUENCE_PATTERN_DOWN_10";
	
	private Grid					grid						= null;
	private SequenceGridController	gridController				= null;
	private SequenceGridKeyListener	gridKeyListener				= null;
	
	private List<Pattern>			compositionPatternsCopy		= new ArrayList<Pattern>();
	private List<Integer>			workingSequence				= new ArrayList<Integer>();
	
	private int[]					selectedRows				= null;
	private int[]					selectedCols				= null;

	private boolean					clearedPlayingIndex			= false;

	public PanelSequence(Controller controller) {
		super(controller);
		controller.getStateManager().addSubscriber(this);
		controller.addSequencerMetaListener(this);
		controller.addSequencerSubscriber(this);
		gridKeyListener = new SequenceGridKeyListener(controller,controller.getStateManager().getSettings().getKeyCodeNoteNumbers(),this);
	}

	@Override
	public void initialize() {
		setValidate(false);
		getPanel().addKeyListener(getController().getPlayerKeyListener());
		getPanel().setLayout(new GridBagLayout());
		getPanel().setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		JButton insert = new JButton("Insert");
		insert.addActionListener(this);
		insert.setActionCommand(INSERT);
		insert.addFocusListener(this);
		insert.addKeyListener(getController().getPlayerKeyListener());
		addControlPageUpDownOverridesToComponent(insert);

		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BoxLayout(wrapper,BoxLayout.X_AXIS));
		wrapper.add(insert);
		
		int row = 0;
		addComponent(getPanel(), row, 0.01,wrapper);
		
		row++;
		addComponent(getPanel(), row, 0.99,getSequencePanel());
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
			reselect();
		}
		setValidate(true);
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		super.actionPerformed(evt);
		if (evt.getActionCommand().equals(SEQUENCE_PATTERN_UP_1)) {
			shiftSeletectPatterns(1);
		} else if (evt.getActionCommand().equals(SEQUENCE_PATTERN_DOWN_1)) {
			shiftSeletectPatterns(-1);
		} else if (evt.getActionCommand().equals(SEQUENCE_PATTERN_UP_10)) {
			shiftSeletectPatterns(10);
		} else if (evt.getActionCommand().equals(SEQUENCE_PATTERN_DOWN_10)) {
			shiftSeletectPatterns(-10);
		} else if (evt.getActionCommand().equals(INSERT)) {
			insertPatterns();
		} else if (evt.getActionCommand().equals(REMOVE)) {
			removeSelectedPatterns();
		}
	}

	@Override
	public void meta(MetaMessage meta) {
		if (meta.getType()==CompositionToSequenceConvertor.MARKER) {
			String txt = new String(meta.getData());
			if (txt.startsWith(CompositionToSequenceConvertor.SEQUENCE_MARKER)) {
				String[] d = txt.split(":");
				int index = Integer.parseInt(d[1]);
				gridController.setPlayingIndex(index);
				if (index<0) {
					if (!clearedPlayingIndex) {
						grid.repaint();
						clearedPlayingIndex = true;
					}
				} else if (index<grid.getRowCount()) {
					clearedPlayingIndex = false;
					if (index>0) {
						grid.repaintBar((index - 1),index);
					} else {
						grid.repaintBar((grid.getRowCount() - 1),index);
					}
				}
			}
		}
	}

	@Override
	public void started() {
		grid.repaint();
	}

	@Override
	public void stopped() {
		gridController.setPlayingIndex(-1);
		grid.repaint();
		clearedPlayingIndex = true;
	}

	@Override
	public void valueChanged(ListSelectionEvent evt) {
		int[] rows = grid.getSelectedRows();
		int[] cols = grid.getSelectedColumns();
		if (rows.length>0 && cols.length>0) {
			selectedRows = rows;
			selectedCols = cols;
			getController().getStateManager().setSelectedSequenceSelection(
				this,
				selectedRows[0],
				selectedRows[selectedRows.length-1]
				);
		}
	}

	@Override
	public void focusLost(FocusEvent evt) {
		super.focusLost(evt);
		if (!(evt.getOppositeComponent() instanceof JRootPane)) {
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
			if (grid.hasFocus()) {
				reselect();
			}
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
		if (rowFrom>=0 && rowTo>=0) {
			grid.addRowSelectionInterval(rowFrom,rowTo);
			grid.addColumnSelectionInterval(colFrom,colTo);
			Rectangle rect = grid.getCellRect(rowFrom,colFrom,true);
			rect.height = rect.height + 20;
			rect.width = rect.width + 100;
			grid.scrollRectToVisible(rect);
		}
	}

	protected JScrollPane getSequencePanel() {
		gridController = new SequenceGridController();
		grid = new Grid();
		grid.addKeyListener(gridKeyListener);
		grid.setModel(gridController);
		grid.addFocusListener(this);
		grid.setColumnSelectionAllowed(false);
		grid.setRowSelectionAllowed(true);
		grid.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		grid.getSelectionModel().addListSelectionListener(this);
		grid.setDefaultRenderer(Object.class,new SequenceGridCellRenderer(gridController));
		grid.getTableHeader().setReorderingAllowed(false);
		grid.getTableHeader().setResizingAllowed(false);
		
		grid.setComponentPopupMenu(getMenu());
		
		int height = getController().getStateManager().getSettings().getCustomRowHeight();
		if (height>0) {
			grid.setRowHeight(height);
		}
		
		addFunctionKeyOverridesToComponent(grid);
		addControlPageUpDownOverridesToComponent(grid);
		addAltOverridesToComponent(grid);
				
		JScrollPane r = new JScrollPane(grid,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		r.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));
		r.getVerticalScrollBar().setUnitIncrement(20);
		return r;
	}

	protected JPopupMenu getMenu() {
		JPopupMenu r = new JPopupMenu();
		addMenuOptions(r);
		return r;
	}
	
	protected void addMenuOptions(JComponent r) {
		JMenuItem item = null;
		
		int evt = ActionEvent.ALT_MASK;

		item = new JMenuItem("Insert");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0));
		item.setActionCommand(INSERT);
		item.addActionListener(this);
		r.add(item);

		item = new JMenuItem("Pattern up 1");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,evt));
		item.setActionCommand(SEQUENCE_PATTERN_UP_1);
		item.addActionListener(this);
		r.add(item);

		item = new JMenuItem("Pattern down 1");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,evt));
		item.setActionCommand(SEQUENCE_PATTERN_DOWN_1);
		item.addActionListener(this);
		r.add(item);

		evt = ActionEvent.ALT_MASK + ActionEvent.SHIFT_MASK;
		
		item = new JMenuItem("Pattern up 10");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP,evt));
		item.setActionCommand(SEQUENCE_PATTERN_UP_10);
		item.addActionListener(this);
		r.add(item);

		item = new JMenuItem("Pattern down 10");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN,evt));
		item.setActionCommand(SEQUENCE_PATTERN_DOWN_10);
		item.addActionListener(this);
		r.add(item);

		item = new JMenuItem("Remove");
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
		item.setActionCommand(REMOVE);
		item.addActionListener(this);
		r.add(item);
	}
}
