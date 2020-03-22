package nl.zeesoft.zdk.htm3.model;

import java.util.ArrayList;
import java.util.List;

public class Cell {
	protected int				index				= 0;
	protected int				posX				= 0;
	protected int				posY				= 0;
	protected int				posZ				= 0;
	
	protected int				dendriteCellIndex	= 0;
	protected List<Dendrite>	dendrites			= new ArrayList<Dendrite>();
	
	protected Cell(int index, int posX, int posY, int posZ) {
		this.index = index;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}
	
	protected String getId() {
		return posX + "-" + posY + "-" + posZ;
	}
	
	protected Dendrite getDendriteById(String type, String id) {
		Dendrite r = null;
		for (Dendrite dendrite: dendrites) {
			if (dendrite.type.equals(type) && dendrite.getId().equals(id)) {
				r = dendrite;
				break;
			}
		}
		return r;
	}
	
	protected Dendrite addDendrite(String type, String cellId) {
		Dendrite r = new Dendrite(type,cellId,dendriteCellIndex);
		dendrites.add(r);
		dendriteCellIndex++;
		return r;
	}
}
