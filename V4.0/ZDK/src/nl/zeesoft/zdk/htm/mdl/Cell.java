package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.List;

public class Cell implements ModelObject {
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
	public String getId() {
		return getClass().getSimpleName() + ":" + posX + "-" + posY + "-" + posZ;
	}
}
