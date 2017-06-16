package nl.zeesoft.zmmt.gui.panel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.JTable;

@SuppressWarnings("serial")
public class Grid extends JTable {
	public static final Color	COLOR_SELECTED	= new Color(136,136,136);
	public static final Color	COLOR_NORMAL	= new Color(255,255,255);

	public Grid() {
		super.setOpaque(true);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int row = getPlayingRow();
		if (row>=0) {
			Graphics2D g2 = (Graphics2D) g;
			Stroke oldStroke = g2.getStroke();
			g2.setStroke(new BasicStroke(1));
			Rectangle r = getPlayingRectangle(row,0);
			g2.setPaint(Color.BLACK);
			g2.drawRect(r.x,r.y,r.width,r.height);
			g2.setStroke(oldStroke);
		}
	}
	
	protected void repaintBar(int prevRow,int currRow) {
		Rectangle r = null;
		if (prevRow>-1) {
			r = getPlayingRectangle(prevRow,1);
			repaint(r);
		}
		if (currRow>-1) {
			r = getPlayingRectangle(currRow,1);
			repaint(r);
		}
	}
	
	protected int getPlayingRow() {
		int r = -1;
		if (getModel() instanceof NotesGridController) {
			NotesGridController controller = (NotesGridController) getModel();
			r = (controller.getPlayingStep() - 1);
		} else if (getModel() instanceof SequenceGridController) {
			SequenceGridController controller = (SequenceGridController) getModel();
			r = controller.getPlayingIndex();
		}
		return r;
	}
	
	protected Rectangle getPlayingRectangle(int row,int add) {
		Rectangle r = new Rectangle();
		Rectangle cell = getCellRect(row,0,false);
		r.setLocation(cell.x,cell.y);
		r.setSize((((cell.width + 1) * getModel().getColumnCount()) - 1) + add,(cell.height - 1) + add);
		return r;
	}
	
	protected void handlePageDown(int rowsPerPage,boolean select) {
		int row = getSelectedRow();
		if (row<(getRowCount() - 1)) {
			int[] rows = getSelectedRows();
			int[] cols = getSelectedColumns();
			if (row<0) {
				row = 0;
			}
			int rowFrom = row;
			if (rows.length>0 && (rows[0] - rows[(rows.length - 1)])!=0) {
				row = rows[(rows.length - 1)];
			} else if (select) {
				row = row - 1;
			}
			row = row + rowsPerPage;
			if (row>=getRowCount()) {
				row = (getRowCount() - 1);
			}
			int col = getSelectedColumn();
			if (col<0) {
				col = 0;
			}
			int colFrom = col;
			clearSelection();
			if (select) {
				if (cols.length>0) {
					colFrom = cols[0];
					col = cols[(cols.length - 1)];
				}
				selectAndShow(rowFrom,row,colFrom,col,true);
			} else {
				selectAndShow(row,row,col,col,true);
			}
		}
	}
	
	protected void handlePageUp(int rowsPerPage,boolean select) {
		int[] rows = getSelectedRows();
		if (rows.length>0) {
			boolean showTo = false;
			int[] cols = getSelectedColumns();
			int row = rows[0];
			int rowTo = rows[(rows.length - 1)];
			if (row==0 && (rowTo - row)!=0) {
				rowTo = rowTo - rowsPerPage;
				showTo = true;
			} else {
				if ((rowTo - row)==0) {
					row = row + 1;
				}
				row = row - rowsPerPage;
			}
			if (row<0) {
				row = 0;
			}
			if (rowTo<row) {
				rowTo = 0;
			}
			int col = getSelectedColumn();
			if (col<0) {
				col = 0;
			}
			int colFrom = col;
			clearSelection();
			if (select) {
				if (cols.length>0) {
					colFrom = cols[0];
					col = cols[(cols.length - 1)];
				}
				selectAndShow(row,rowTo,colFrom,col,showTo);
			} else {
				selectAndShow(row,row,col,col,false);
			}
		}
	}
	
	protected void selectAndShow(int rowFrom, int rowTo, int colFrom, int colTo,boolean showTo) {
		int max = (getRowCount() - 1);
		if (rowFrom>max) {
			rowFrom = max;
		}
		if (rowTo>max) {
			rowTo = max;
		}
		max = (getColumnCount() - 1);
		if (colFrom>max) {
			colFrom = max;
		}
		if (colTo>max) {
			colTo = max;
		}
		if (rowTo>=0 && colTo>=0) {
			addRowSelectionInterval(rowFrom,rowTo);
			addColumnSelectionInterval(colFrom,colTo);
			Rectangle rect = null;
			if (showTo) {
				rect = getCellRect(rowTo,colTo,true);
			} else {
				rect = getCellRect(rowFrom,colFrom,true);
			}
			rect.height = rect.height + 20;
			rect.width = rect.width + 100;
			scrollRectToVisible(rect);
		}
	}
}
