package nl.zeesoft.zdk.htm3.model;

import java.util.ArrayList;
import java.util.List;

public class Column {
	protected int				index		= 0;
	protected int				posX		= 0;
	protected int				posY		= 0;
	protected List<Cell>		cells		= new ArrayList<Cell>();
	
	protected Column(int index, int posX, int posY) {
		this.index = index;
		this.posX = posX;
		this.posY = posY;
	}
	
	protected String getId() {
		return posX + "-" + posY;
	}
}
