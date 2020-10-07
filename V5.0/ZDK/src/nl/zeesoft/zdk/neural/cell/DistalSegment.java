package nl.zeesoft.zdk.neural.cell;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.grid.Position;

public class DistalSegment {
	public List<Synapse>	synapses			= new ArrayList<Synapse>();
	public List<Synapse>	activeSynapses		= new ArrayList<Synapse>();
	public List<Synapse>	potentialSynapses	= new ArrayList<Synapse>();
	
	public void adaptSynapses(List<Position> prevActiveCellPositions, float permanenceIncrement, float permanenceDecrement) {
		List<Synapse> removeSynapses = new ArrayList<Synapse>();
		for (Synapse synapse: synapses) {
			boolean active = Position.posIsInList(synapse.connectToPosX, synapse.connectToPosY, synapse.connectToPosZ, prevActiveCellPositions);
			if (active) {
				synapse.permanence = synapse.permanence + permanenceIncrement;
				if (synapse.permanence > 1F) {
					synapse.permanence = 1F;
				}
			} else {
				synapse.permanence = synapse.permanence - permanenceDecrement;
				if (synapse.permanence <= 0F) {
					removeSynapses.add(synapse);
				}
			}
		}
		for (Synapse remove: removeSynapses) {
			synapses.remove(remove);
		}
	}
	
	public void growSynapses(Position cellPosition, int growNum, List<Position> prevWinnerCellPositions, float initialPermanence, int maxSynapsesPerSegment) {
		List<Position> growPositions = new ArrayList<Position>(prevWinnerCellPositions);
		growPositions.remove(cellPosition);
		for (Position pos: prevWinnerCellPositions) {
			Synapse synapse = getSynapseTo(pos.x, pos.y, pos.z);
			if (synapse!=null) {
				growPositions.remove(pos);
			}
		}
		while (growPositions.size()>growNum) {
			growPositions.remove(Rand.getRandomInt(0,(growPositions.size() - 1)));
		}
		if (growPositions.size()>0) {
			//System.out.println(cellPosition + ", growing: " + growPositions.size());
			for (Position pos: growPositions) {
				Synapse synapse = new Synapse();
				synapse.connectToPosX = pos.x;
				synapse.connectToPosY = pos.y;
				synapse.connectToPosZ = pos.z;
				synapse.permanence = initialPermanence;
				synapses.add(synapse);
			}
			if (synapses.size()>maxSynapsesPerSegment) {
				SortedMap<Float,List<Synapse>> synapsesByPermanence = new TreeMap<Float,List<Synapse>>();
				for (Synapse synapse: synapses) {
					if (!Position.posIsInList(synapse.connectToPosX, synapse.connectToPosY, synapse.connectToPosZ, prevWinnerCellPositions)) {
						float permanence = synapse.permanence;
						List<Synapse> list = synapsesByPermanence.get(permanence);
						if (list==null) {
							list = new ArrayList<Synapse>();
							synapsesByPermanence.put(permanence, list);
						}
						list.add(synapse);
					}
				}
				while (synapses.size()>maxSynapsesPerSegment) {
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
	}
	
	public Synapse getSynapseTo(int posX, int posY, int posZ) {
		Synapse r = null;
		for (Synapse synapse: synapses) {
			if (synapse.connectToPosX == posX && synapse.connectToPosY == posY && synapse.connectToPosZ == posZ) {
				r = synapse;
				break;
			}
		}
		return r;
	}
	
	public void calculateActiveSynapses(List<Position> activeCellPositions, float permanenceThreshold) {
		activeSynapses.clear();
		potentialSynapses.clear();
		for (Position pos: activeCellPositions) {
			Synapse synapse = getSynapseTo(pos.x, pos.y, pos.z);
			if (synapse!=null) {
				if (synapse.permanence > permanenceThreshold) {
					activeSynapses.add(synapse);
				}
				potentialSynapses.add(synapse);
			}
		}
	}
}
