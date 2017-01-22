package nl.zeesoft.zadf.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;

public class GuiPanelObjectList extends GuiObjectList {
	private			int					padding				= 2;
	private			List<Double>		columnWeights		= new ArrayList<Double>();
	private			List<Double>		rowWeights			= new ArrayList<Double>();
	
	public void setEnabled(boolean enabled) {
		for (GuiObject obj: getObjects()) {
			obj.setEnabled(enabled);
			if (obj instanceof GuiPanel) {
				GuiPanel panel = (GuiPanel) obj;
				panel.getPanelObjects().setEnabled(enabled);
			}
		}
	}

	public GuiObject getGuiObjectByName(String name) {
		GuiObject r = null;
		for (GuiObject obj: getObjects()) {
			if (obj.getName().equals(name)) {
				r = obj;
			}
			if ((r==null) && (obj instanceof GuiPanelAbstract)) {
				GuiPanelAbstract panel = (GuiPanelAbstract) obj;
				r = panel.getGuiObjectByName(name);
			}
			if (r!=null) {
				break;
			}
		}
		return r;
	}

	public GuiObject getGuiObjectForSourceComponent(Component source) {
		GuiObject r = null;
		for (GuiObject obj: getObjects()) {
			r = obj.getGuiObjectForSourceComponent(source);
			if (r!=null) {
				break;
			}
		}
		return r;
	}

	public void calculateWeights() {
		calculateColumnWeights();
		calculateRowWeights();
		boolean calculated = false;
		String objects = "";
		for (GuiObject obj: getObjects()) {
			if (!objects.equals("")) {
				objects = objects + ", ";
			}
			objects = objects + obj.getName();
			GuiPanelObject pnlObj = (GuiPanelObject) obj;
			if (pnlObj instanceof GuiPanel) {
				GuiPanel panel = (GuiPanel) pnlObj;
				panel.getPanelObjects().calculateWeights();
			}
			calculated = true;
		}
		if ((calculated) && (DbConfig.getInstance().isDebug())) {
			String rowW = "";
			String colW = "";
			for (Double d: rowWeights) {
				if (!rowW.equals("")) {
					rowW = rowW + ",";
				}
				rowW = rowW + d;
			}
			rowW = "rows: " + rowW;
			for (Double d: columnWeights) {
				if (!colW.equals("")) {
					colW = colW + ",";
				}
				colW = colW + d;
			}
			colW = "columns: " + colW;
			objects = "objects : " + objects;
		}
	}
	
	@Override
	public void clear() {
		super.clear();
		columnWeights.clear();
		rowWeights.clear();
	}

	@Override
	public void add(GuiObject object) {
		if (object instanceof GuiPanelObject) {
			GuiPanelObject pnlObj = (GuiPanelObject) object;
			GuiPanelObject curr = get(pnlObj.getRow(),pnlObj.getColumn());
			if (curr==null) {
				super.add(object);
			} else {
				Messenger.getInstance().error(this,"Panel object already has a component at row: " + pnlObj.getRow() + ", column: " + pnlObj.getColumn() + ", name: " + curr.getName());
			}
		}
	}
	
	@Override
	public GuiPanelObject get(String name) {
		GuiObject obj = super.get(name);
		GuiPanelObject panelObj = null;
		if (obj!=null) {
			panelObj = (GuiPanelObject) obj;
		}
		return panelObj;
	}
		
	public GuiPanelObject get(int row, int column) {
		GuiPanelObject panelObj = null;
		for (GuiObject obj: getObjects()) {
			GuiPanelObject pnlObj = (GuiPanelObject) obj;
			if ((pnlObj.getRow()==row) && (pnlObj.getColumn()==column)) {
				panelObj = pnlObj;
				break;
			}
		}
		return panelObj;
	}
	
	public int getRows() {
		int rows = 0;
		for (GuiObject obj: getObjects()) {
			GuiPanelObject pnlObj = (GuiPanelObject) obj;
			if (rows < (pnlObj.getRow() + 1)) {
				rows = (pnlObj.getRow() + 1);
			}
		}
		return rows;
	}

	public int getColumns() {
		int columns = 0;
		for (GuiObject obj: getObjects()) {
			GuiPanelObject pnlObj = (GuiPanelObject) obj;
			if (columns < (pnlObj.getColumn() + 1)) {
				columns = (pnlObj.getColumn() + 1);
			}
		}
		return columns;
	}

	private void calculateColumnWeights() {
		columnWeights.clear();
		int columns = getColumns();
		Double weight = (1.0 / columns);
		for (int c = 0; c < columns; c++) {
			columnWeights.add(weight);
		}
	}

	private void calculateRowWeights() {
		rowWeights.clear();
		int rows = getRows();
		Double weight = (1.0 / rows);
		for (int c = 0; c < rows; c++) {
			rowWeights.add(weight);
		}
	}
	
	public List<Double> getColumnWeights() {
		return columnWeights;
	}

	public void setColumnWeights(List<Double> cw) {
		columnWeights = cw;
	}

	public List<Double> getRowWeights() {
		return rowWeights;
	}
	
	public void setRowWeights(List<Double> rw) {
		rowWeights = rw;
	}

	public JPanel renderJPanel() {
		JPanel rp = new JPanel();
		
		rp.setLayout(new GridBagLayout());
		
		int rows = getRows();
		int columns = getColumns();
		
		GridBagConstraints gbc = new GridBagConstraints();

		if (columns > columnWeights.size()) {
			calculateWeights();
		}
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++) {
				GuiObject obj = get(r,c);
				if (obj!=null) {
					GuiPanelObject pObj = (GuiPanelObject) obj;
					obj.renderComponent();
					
					gbc.anchor = GridBagConstraints.CENTER;
					gbc.fill = GridBagConstraints.NONE;

					if (pObj.getAnchor()>0) {
						gbc.anchor = pObj.getAnchor();
					}
					if (pObj.getFill()>0) {
						gbc.fill = pObj.getFill();
					}
					
					double colW = columnWeights.get(c);
					double rowW = rowWeights.get(r);
					
					if (pObj.getGridWidth()>1) {
						for (int cc = (c + 1); cc < (c + pObj.getGridWidth()); cc++) {
							if (cc>=columnWeights.size()) {
								Messenger.getInstance().error(this, "Error calculating column weight for object: " + pObj.getName() + ", gridWidth: " + pObj.getGridWidth());
								break;
							} else {
								colW = colW + columnWeights.get(cc);
							}
						}
					}
					if (pObj.getGridHeight()>1) {
						for (int rr = (r + 1); rr < (r + pObj.getGridHeight()); rr++) {
							if (rr>=rowWeights.size()) {
								Messenger.getInstance().error(this, "Error calculating row weight for object: " + pObj.getName() + ", gridHeight: " + pObj.getGridHeight());
							} else {
								rowW = rowW + rowWeights.get(rr);
							}
						}
					}
					
					gbc.gridwidth = pObj.getGridWidth();
					gbc.gridheight = pObj.getGridHeight();
					gbc.weightx = colW;
					gbc.weighty = rowW;
					gbc.gridx = c;
					gbc.gridy = r;
					gbc.ipadx = padding;
					gbc.ipady = padding;
					
					rp.add(obj.getComponent(),gbc);
				}
			}
		}
		
		return rp;
	}

	/**
	 * @param padding the padding to set
	 */
	public void setPadding(int padding) {
		this.padding = padding;
	}
}
