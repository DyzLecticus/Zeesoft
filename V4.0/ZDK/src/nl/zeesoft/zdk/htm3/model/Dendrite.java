package nl.zeesoft.zdk.htm3.model;

import java.util.SortedMap;
import java.util.TreeMap;

public class Dendrite {
	protected final static String		PROXIMAL			= "PROXIMAL";
	protected final static String		DISTAL				= "DISTAL";

	protected String					type				= "";
	
	protected String					cellId				= "";
	protected int						cellIndex			= 0;
	
	protected SortedMap<String,Float>	synapses			= new TreeMap<String,Float>();
	
	protected Dendrite(String type, String cellId, int cellIndex) {
		this.type = type;
		this.cellId = cellId;
		this.cellIndex = cellIndex;
	}
	
	protected String getId() {
		return cellId + "-" + cellIndex;
	}
	
	protected void incrementSynapsePermanence(String toCellId, float amount) {
		Float permanence = synapses.get(toCellId);
		if (permanence!=null) {
			permanence += amount;
			if (permanence>1) {
				permanence = 1F;
			}
		} else {
			permanence = new Float(amount);
		}
		synapses.put(toCellId,permanence);
	}
	
	protected void decrementSynapsePermanence(String toCellId, float amount) {
		Float permanence = synapses.get(toCellId);
		if (permanence!=null) {
			permanence -= amount;
			if (permanence<0) {
				synapses.remove(toCellId);
			} else {
				synapses.put(toCellId,permanence);
			}
		}
	}
}
