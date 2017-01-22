package nl.zeesoft.zadf.gui;

public abstract class GuiPanelObject extends GuiObject {
	private int		row			= 0;
	private int		column		= 0;
	private int		anchor		= 0;
	private int		fill		= 0;
	private int		gridWidth	= 1;
	private int		gridHeight	= 1;
	
	public GuiPanelObject(String name, int row, int column) {
		super(name);
		this.row = row;
		this.column = column;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return the anchor
	 */
	public int getAnchor() {
		return anchor;
	}

	/**
	 * @param anchor the anchor to set
	 */
	public void setAnchor(int anchor) {
		this.anchor = anchor;
	}

	/**
	 * @return the fill
	 */
	public int getFill() {
		return fill;
	}

	/**
	 * @param fill the fill to set
	 */
	public void setFill(int fill) {
		this.fill = fill;
	}

	/**
	 * @return the gridWidth
	 */
	public int getGridWidth() {
		return gridWidth;
	}

	/**
	 * @param gridWidth the gridWidth to set
	 */
	public void setGridWidth(int gridWidth) {
		this.gridWidth = gridWidth;
	}

	/**
	 * @return the gridHeight
	 */
	public int getGridHeight() {
		return gridHeight;
	}

	/**
	 * @param gridHeight the gridHeight to set
	 */
	public void setGridHeight(int gridHeight) {
		this.gridHeight = gridHeight;
	}

}

