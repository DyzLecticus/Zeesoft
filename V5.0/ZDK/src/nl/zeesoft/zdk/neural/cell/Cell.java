package nl.zeesoft.zdk.neural.cell;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.grid.Position;

public class Cell {
	public Position				position				= null;
	public List<DistalSegment>	distalSegments			= new ArrayList<DistalSegment>();
	public List<DistalSegment>	activeDistalSegments	= new ArrayList<DistalSegment>();
	public List<DistalSegment>	matchingDistalSegments	= new ArrayList<DistalSegment>();
	public DistalSegment		matchingDistalSegment	= null;

	public Cell() {
		
	}
	
	public Cell(Position position) {
		this.position = position;
	}
	
	public void calculateSegmentActivity(List<Position> activeCellPositions, float permanenceThreshold) {
		for (DistalSegment segment: distalSegments) {
			segment.calculateActiveSynapses(activeCellPositions, permanenceThreshold);
		}
	}
	
	public void classifySegmentActivity(int activationThreshold, int matchingThreshold) {
		activeDistalSegments.clear();
		matchingDistalSegments.clear();
		matchingDistalSegment = null;
		for (DistalSegment segment: distalSegments) {
			if (segment.activeSynapses.size() >= activationThreshold) {
				activeDistalSegments.add(segment);
			} else if (segment.potentialSynapses.size() >= matchingThreshold) {
				matchingDistalSegments.add(segment);
			}
		}
		int potential = 0;
		for (DistalSegment segment: matchingDistalSegments) {
			if (segment.potentialSynapses.size()>potential) {
				matchingDistalSegment = segment;
				potential = segment.potentialSynapses.size();
			}
		}
		if (matchingDistalSegment!=null) {
			distalSegments.remove(matchingDistalSegment);
			distalSegments.add(0, matchingDistalSegment);
		}
		for (DistalSegment segment: activeDistalSegments) {
			distalSegments.remove(segment);
			distalSegments.add(0, segment);
		}
	}
	
	public DistalSegment createSegment(int maxSegmentsPerCell) {
		DistalSegment r = new DistalSegment();
		distalSegments.add(0,r);
		while (distalSegments.size()>maxSegmentsPerCell) {
			distalSegments.remove(distalSegments.size() - 1);
		}
		return r;
	}
	
	public void clear() {
		for (DistalSegment segment: distalSegments) {
			segment.synapses.clear();
		}
		distalSegments.clear();
		activeDistalSegments.clear();
		matchingDistalSegments.clear();
	}
}
