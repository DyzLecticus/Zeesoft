package nl.zeesoft.zmmt.gui.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import nl.zeesoft.zmmt.composition.Composition;
import nl.zeesoft.zmmt.composition.Pattern;
import nl.zeesoft.zmmt.gui.Controller;
import nl.zeesoft.zmmt.gui.FrameMain;
import nl.zeesoft.zmmt.gui.state.StateChangeEvent;
import nl.zeesoft.zmmt.gui.state.StateChangeSubscriber;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;

public class PanelSequence extends PanelObject implements ActionListener, StateChangeSubscriber, MetaEventListener, SequencePlayerSubscriber, ListSelectionListener {
	private JTable					grid							= null;
	private SequenceGridController	gridController					= null;
	
	private List<Pattern>			compositionPatternsCopy			= new ArrayList<Pattern>();
	private List<Integer>			workingSequence					= new ArrayList<Integer>();

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
		//int[] rows = grid.getSelectedRows();
		//int[] cols = grid.getSelectedColumns();
		
		// TODO: Implement
	}
	
	protected void insertPatterns() {
		int[] rows = grid.getSelectedRows();
		int add = 1;
		int rowFrom = 0;
		int rowTo = 0;
		if (rows.length>0) {
			rowFrom = rows[0];
			rowTo = rows[(rows.length - 1)];
			add = (rowTo - rowFrom) + 1;
		}
		if (add>0) {
			for (int i = 0; i < add; i++) {
				workingSequence.add(rowFrom,0);
			}
			changedSequence(rowFrom,rowTo);
		}
	}
	
	protected void shiftSeletectPatterns(int mod) {
		// TODO: Implement
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
		}
	}
	
	@SuppressWarnings("serial")
	protected JScrollPane getSequencePanel() {
		gridController = new SequenceGridController();
		SequenceGridKeyListener keyListener = getController().getSequenceKeyListener();
		keyListener.setSequencePanel(this);
		grid = new JTable() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (getModel() instanceof SequenceGridController) {
					SequenceGridController controller = (SequenceGridController) getModel();
					if (controller.getPlayingIndex()>=0) {
						Graphics2D g2 = (Graphics2D) g;
						Stroke oldStroke = g2.getStroke();
						g2.setStroke(new BasicStroke(2));
						Rectangle r = getCellRect((controller.getPlayingIndex() - 1),0,false);
						g2.setPaint(Color.BLACK);
						g2.drawRect(r.x,(r.y - 1),(((r.width + 1) * Composition.TRACKS) - 1),(r.height + 1));
						g2.setStroke(oldStroke);
					}
				}
			}
		};
		grid.addKeyListener(keyListener);
		grid.setModel(gridController);
		grid.addFocusListener(this);
		grid.setColumnSelectionAllowed(false);
		grid.setRowSelectionAllowed(true);
		grid.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		grid.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		grid.getSelectionModel().addListSelectionListener(this);
		
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
