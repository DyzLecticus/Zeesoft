package nl.zeesoft.zadf.gui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;

import nl.zeesoft.zadf.controller.GuiController;
import nl.zeesoft.zodb.Messenger;

public class GuiGrid extends GuiPanelObject {
	public final static String		GRID_DOUBLE_CLICKED		= "GRID_DOUBLE_CLICKED";
	public final static String		GRID_COLUMN_CLICKED		= "GRID_COLUMN_CLICKED";

	private JScrollPane				scrollPanel				= null;

	private JTable	 				grid 					= new JTable();
    private AbstractTableModel		gridModel				= null;
	private List<Object[]>			properties				= new ArrayList<Object[]>();
	private List<Long>				idList					= new ArrayList<Long>();
	private List<Object[]>			data					= new ArrayList<Object[]>();
	private String					selectedProperty		= "";
	
	private Object					gridIsLockedBy			= null;
	
	public GuiGrid(String name,int row,int column) {
		super(name,row,column);
		gridModel = new DefaultGridModel();
		grid = new JTable(gridModel);
		grid.setFillsViewportHeight(true);
		grid.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		grid.setRowSelectionAllowed(true);
		ListSelectionListener l = new ListSelectionListener (){
			public void valueChanged(ListSelectionEvent arg0) {
				GuiController.getInstance().valueChanged(new ListSelectionEvent(grid, arg0.getFirstIndex(), arg0.getFirstIndex(), arg0.getValueIsAdjusting()));
			}
		};
		grid.getSelectionModel().addListSelectionListener(l);
		grid.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount()>1) {
					ActionEvent edc = new ActionEvent(grid,0,GRID_DOUBLE_CLICKED);
					GuiController.getInstance().actionPerformed(edc);
				}
			};
		});
		
		final JTableHeader header = grid.getTableHeader();  
        header.setReorderingAllowed(false);  
        header.addMouseListener(new MouseAdapter() {  
        	public void mouseClicked(MouseEvent e) {  
        		int col = header.columnAtPoint(e.getPoint());  
    			if(header.getCursor().getType() == Cursor.E_RESIZE_CURSOR) {  
    				e.consume();  
    			} else {
    				if (properties.size()>col) {
    					selectedProperty = properties.get(col)[0].toString();
    					ActionEvent ecc = new ActionEvent(grid,0,GRID_COLUMN_CLICKED);
    					GuiController.getInstance().actionPerformed(ecc);
    				}
    			}  
    		}  
        });  
		
        scrollPanel = new JScrollPane(grid);
	}

	@Override
	public void renderComponent() {
		if (getComponent()==null) {
			setComponent(scrollPanel);
		}
	}
	
	@Override
	public GuiObject getGuiObjectForSourceComponent(Component source) {
		GuiObject object = super.getGuiObjectForSourceComponent(source);
		if (object==null) {
			if (source==grid) {
				object = this;
			}
		}
		return object;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		grid.setEnabled(enabled);
		grid.setFocusable(enabled);
	}

	public void addProperty(String name,String label,Object value) {
		lockGrid(this);
		Object[] prop = new Object[3];
		prop[0] = name;
		prop[1] = label;
		prop[2] = value;
		
		properties.add(prop);
		unlockGrid(this);
	}

	public void fireStructureChanged() {
		gridModel.fireTableStructureChanged();
		
		for (int i = 0; i < properties.size(); i++) {
			Object val = properties.get(i)[2];
			if (
				(val instanceof Long) ||
				(val instanceof Float) ||
				(val instanceof Integer)
				) {
				grid.getColumnModel().getColumn(i).setPreferredWidth(50);
			} else if (val instanceof Boolean) {
				grid.getColumnModel().getColumn(i).setPreferredWidth(30);
			} else if (val instanceof String) {
				grid.getColumnModel().getColumn(i).setPreferredWidth(200);
			} else if (val instanceof Date) {
				grid.getColumnModel().getColumn(i).setPreferredWidth(200);
			} else {
				grid.getColumnModel().getColumn(i).setPreferredWidth(100);
			}
		}
	}
	
	public void clearProperties() {
		lockGrid(this);
		selectedProperty = "";
		properties.clear();
		unlockGrid(this);
		gridModel.fireTableStructureChanged();
	}
	
	public void setData(List<Long> idLst, List<Object[]> dat) {
		lockGrid(this);
		idList = idLst;
		data = dat;
		unlockGrid(this);
		gridModel.fireTableDataChanged();
	}

	public void setValueAt(int row, int col, Object value) {
		boolean fire = false;
		lockGrid(this);
    	if ((data!=null) && (row<=data.size()) && (col<=data.get(row).length)) {
    		data.get(row)[col] = value;
    		fire = true;
    	}
    	unlockGrid(this);
    	if (fire) {
    		gridModel.fireTableCellUpdated(row, col);
    	}
	}

	public void clearData() {
		lockGrid(this);
		idList.clear();
		data.clear();
		unlockGrid(this);
		gridModel.fireTableDataChanged();
	}

    public List<Long> getSelectedRowIds() {
    	List<Long> rowids = new ArrayList<Long>();
		lockGrid(this);
    	int[] rows = grid.getSelectedRows();
    	for (int r = 0; r < rows.length; r++) {
    		try {
    			int row = grid.convertRowIndexToModel(rows[r]);
	    		rowids.add(idList.get(row));
    		} catch (Exception e) {
    			// Ignore
    		}
    	}
		unlockGrid(this);
    	return rowids;
    }
	
    public void setSelectedRowId(long id) {
    	int selIdx = -1;
		lockGrid(this);
    	if (idList!=null) {
        	int idx = 0;
			List<Long> idLst = new ArrayList<Long>(idList);
	    	for (long rid: idLst) {
	    		if (rid==id) {
	    			selIdx = idx;
        			break;
	    		}
	    		idx++;
	    	}
    	}
		unlockGrid(this);
		if (selIdx>=0) {
			grid.setRowSelectionInterval(selIdx, selIdx);
			scrollToVisible(selIdx,0);
		}
    }

	private void scrollToVisible(int rowIndex, int vColIndex) {
        if (!(grid.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) grid.getParent();

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = grid.getCellRect(rowIndex, vColIndex, true);

        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x-pt.x, rect.y-pt.y);

        grid.scrollRectToVisible(rect);

        // Scroll the area into view
        //viewport.scrollRectToVisible(rect);
    }	

	private class DefaultGridModel extends AbstractTableModel {
		public static final long serialVersionUID = 0;
		
		@Override
        public int getColumnCount() {
			int count = 0;
			lockGrid(this);
			count = properties.size();
			unlockGrid(this);
			return count;
        }

		@Override
        public int getRowCount() {
			int count = 0;
			lockGrid(this);
			count = data.size();
			unlockGrid(this);
			return count;
        }

		@Override
        public String getColumnName(int col) {
			String s = "";
			lockGrid(this);
			if (col<properties.size()) {
				Object[] p = properties.get(col);
				s = (String) p[1];
			}
			unlockGrid(this);
    		return s;
        }

		@Override
        public Class<?> getColumnClass(int col) {
			Class<?> r = super.getColumnClass(col);
			lockGrid(this);
			if (col<properties.size()) {
	        	Object[] p = properties.get(col);
	        	if (p[2]!=null) {
	        		r = p[2].getClass();
	        	}
			}
			unlockGrid(this);
    		return r;
        }

		@Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Object value = null;
			lockGrid(this);
        	if ((data.size()>0) && 
        		(rowIndex<data.size()) && 
        		(columnIndex<data.get(rowIndex).length)) {
        		value = data.get(rowIndex)[columnIndex];
        	}
        	unlockGrid(this);
        	return value;
		}

    }

	private synchronized void lockGrid(Object source) {
		int attempt = 0;
		while (gridIsLocked()) {
		    try {
                wait();
            } catch (InterruptedException e) { 
            	
            }
            attempt ++;
			if (attempt>=1000) {
				Messenger.getInstance().warn(this,"Lock failed after " + attempt + " attempts. Source:" + source);
				attempt = 0;
			}
		}
		gridIsLockedBy = source;
	}

	private synchronized void unlockGrid(Object source) {
		if (gridIsLockedBy==source) {
			gridIsLockedBy=null;
			notifyAll();
		}
	}
	
	private synchronized boolean gridIsLocked() {
		return (gridIsLockedBy!=null);
	}
	
	/**
	 * @return the scrollPanel
	 */
	public JScrollPane getScrollPanel() {
		return scrollPanel;
	}

	/**
	 * @return the selectedProperty
	 */
	public String getSelectedProperty() {
		return selectedProperty;
	}
}
