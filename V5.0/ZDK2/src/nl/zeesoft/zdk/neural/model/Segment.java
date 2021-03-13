package nl.zeesoft.zdk.neural.model;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.matrix.Position;

public class Segment {
	public static String	PROXIMAL			= "PROXIMAL";
	public static String	DISTAL				= "DISTAL";
	public static String	APICAL				= "APICAL";
	
	public String			type				= "";	
	public List<Synapse>	synapses			= new ArrayList<Synapse>();
	
	public List<Synapse>	activeSynapses		= new ArrayList<Synapse>();
	public List<Synapse>	potentialSynapses	= new ArrayList<Synapse>();
	
	public void clear() {
		synapses.clear();
		activeSynapses.clear();
		potentialSynapses.clear();
	}

	public Synapse getSynapseTo(Position position) {
		Synapse r = null;
		for (Synapse synapse: synapses) {
			if (synapse.connectTo.equals(position)) {
				r = synapse;
				break;
			}
		}
		return r;
	}
	
	public void calculateActiveAndPotentialSynapses(List<Position> activeCellPositions, float permanenceThreshold) {
		activeSynapses.clear();
		potentialSynapses.clear();
		for (Position pos: activeCellPositions) {
			Synapse synapse = getSynapseTo(pos);
			if (synapse!=null) {
				if (synapse.permanence > permanenceThreshold) {
					activeSynapses.add(synapse);
				}
				potentialSynapses.add(synapse);
			}
		}
	}
	
	public void adaptSynapses(List<Position> prevActiveCellPositions, float increment, float decrement) {
		List<Synapse> removeSynapses = new ArrayList<Synapse>();
		for (Synapse synapse: synapses) {
			if (prevActiveCellPositions.contains(synapse.connectTo)) {
				synapse.permanence = synapse.permanence + increment;
				if (synapse.permanence > 1F) {
					synapse.permanence = 1F;
				}
			} else {
				synapse.permanence = synapse.permanence - decrement;
				if (synapse.permanence <= 0F) {
					removeSynapses.add(synapse);
				}
			}
		}
		for (Synapse remove: removeSynapses) {
			synapses.remove(remove);
		}
	}

	public void growSynapses(
		Position cellPosition,
		int growNum, 
		List<Position> prevWinnerCellPositions,
		float initialPermanence,
		int maxSynapsesPerSegment
		) {
		List<Position> growPositions = selectGrowPositions(cellPosition, growNum, prevWinnerCellPositions);
		if (growPositions.size()>0) {
			for (Position pos: growPositions) {
				Synapse synapse = new Synapse();
				synapse.connectTo = pos;
				synapse.permanence = initialPermanence;
				synapses.add(synapse);
			}
			applySynapsePerSegmentLimit(maxSynapsesPerSegment,prevWinnerCellPositions);
		}
	}
	
	protected List<Position> selectGrowPositions(Position cellPosition, int growNum, List<Position> prevWinnerCellPositions) {
		List<Position> r = new ArrayList<Position>(prevWinnerCellPositions);
		r.remove(cellPosition);
		for (Position pos: prevWinnerCellPositions) {
			if (getSynapseTo(pos)!=null) {
				r.remove(pos);
			}
		}
		while (r.size()>growNum) {
			r.remove(Rand.getRandomInt(0,(r.size() - 1)));
		}
		return r;
	}
	
	protected void applySynapsePerSegmentLimit(int maxSynapsesPerSegment, List<Position> prevWinnerCellPositions) {
		if (synapses.size()>maxSynapsesPerSegment) {
			SortedMap<Float,List<Synapse>> synapsesByPermanence = getSynapsesByPermanence(prevWinnerCellPositions);
			while (synapses.size()>maxSynapsesPerSegment && synapsesByPermanence.size()>0) {
				Float firstKey = synapsesByPermanence.firstKey();
				List<Synapse> list = synapsesByPermanence.get(firstKey);
				Synapse remove = list.remove(Rand.getRandomInt(0,(list.size() - 1)));
				if (list.size()==0) {
					synapsesByPermanence.remove(firstKey);
				}
				synapses.remove(remove);
			}
		}
	}
	
	protected SortedMap<Float,List<Synapse>> getSynapsesByPermanence(List<Position> excludePositions) {
		SortedMap<Float,List<Synapse>> r = new TreeMap<Float,List<Synapse>>();
		for (Synapse synapse: synapses) {
			if (!excludePositions.contains(synapse.connectTo)) {
				float permanence = synapse.permanence;
				List<Synapse> list = r.get(permanence);
				if (list==null) {
					list = new ArrayList<Synapse>();
					r.put(permanence, list);
				}
				list.add(synapse);
			}
		}
		return r;
	}
}
