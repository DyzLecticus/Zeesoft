package nl.zeesoft.zdk.htm2.mdl;

import java.util.ArrayList;
import java.util.List;

public class Cell extends ModelObject {
	public int						posX				= 0;
	public int						posY				= 0;
	public int						posZ				= 0;
	
	public List<DistalDendrite>		distalDendrites		= new ArrayList<DistalDendrite>(); 
	public List<ProximalDendrite>	proximalDendrites	= new ArrayList<ProximalDendrite>();
	
	public Cell(int posX, int posY, int posZ) {
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	@Override
	public Cell copy() {
		Cell copy = new Cell(posX,posY,posZ);
		for (DistalDendrite dendrite: distalDendrites) {
			copy.distalDendrites.add(dendrite.copy());
		}
		return copy;
	}
	
	public int getDistanceToCell(Cell toCell) {
		int r = 0;
		int distX = 0;
		int distY = 0;
		int distZ = 0;
		if (toCell.posX>posX) {
			distX = toCell.posX - posX;
		} else {
			distX = posX - toCell.posX;
		}
		if (toCell.posY>posY) {
			distY += toCell.posY - posY;
		} else {
			distY += posY - toCell.posY;
		}
		if (toCell.posZ>posZ) {
			distZ += toCell.posZ - posZ;
		} else {
			distZ += posZ - toCell.posZ;
		}
		r = (int) Math.sqrt((distX * distX) + (distY * distY));
		r = (int) Math.sqrt((r * r) + (distZ * distZ));
		return r;
	}
}
