package nl.zeesoft.zdk.neural.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.matrix.Position;

public class Cell {
	public Position			position				= null;
	public CellConfig		config					= null;
	
	public List<Segment>	proximalSegments		= new ArrayList<Segment>();
	public List<Segment>	distalSegments			= new ArrayList<Segment>();
	public List<Segment>	apicalSegments			= new ArrayList<Segment>();

	public List<Segment>	activeDistalSegments	= new ArrayList<Segment>();
	public List<Segment>	matchingDistalSegments	= new ArrayList<Segment>();
	public Segment			matchingDistalSegment	= null;
	
	public List<Segment>	activeApicalSegments	= new ArrayList<Segment>();
	public List<Segment>	matchingApicalSegments	= new ArrayList<Segment>();
	public Segment			matchingApicalSegment	= null;
	
	public void clear() {
		clearSegments(proximalSegments);
		clearSegments(distalSegments);
		clearSegments(apicalSegments);

		activeDistalSegments.clear();
		matchingDistalSegments.clear();
		matchingDistalSegment = null;
		
		activeApicalSegments.clear();
		matchingApicalSegments.clear();
		matchingApicalSegment = null;
	}

	public List<Segment> getSegments(String type) {
		List<Segment> r = null;
		if (type.equals(Segment.PROXIMAL)) {
			r = proximalSegments;
		} else if (type.equals(Segment.DISTAL)) {
			r = distalSegments;
		} else if (type.equals(Segment.APICAL)) {
			r = apicalSegments;
		}
		return r;
	}

	public void calculateSegmentActivity(String type, List<Position> activeCellPositions) {
		List<Segment> list = getSegments(type);
		for (Segment segment: list) {
			segment.calculateActiveAndPotentialSynapses(activeCellPositions, config.permanenceThreshold);
		}
	}
	
	public void createSegments(
		List<Position> prevActiveCellPositions, List<Position> prevWinnerCellPositions, List<Position> prevActiveApicalCellPositions
		) {
		Segment distalSegment = createSegment(Segment.DISTAL, prevWinnerCellPositions, config.distalPotentialRadius);
		if (distalSegment!=null) {
			createSegment(Segment.APICAL, prevActiveApicalCellPositions, config.apicalPotentialRadius);
		}
	}
	
	protected Segment createSegment(String type, List<Position> growPositions, float potentialRadius) {
		Segment r = null;
		if (growPositions.size()>0) {
			if (potentialRadius>0) {
				growPositions = position.selectPositionsLimitDistance(potentialRadius, growPositions);
			}
			int growNum = config.maxNewSynapseCount;
			if (growPositions.size()<growNum) {
				growNum = growPositions.size();
			}
			if (growNum>0 && 
				(config.segmentCreationSubsample==1F || Rand.getRandomFloat(0F, 1F) <= config.segmentCreationSubsample)
				) {
				r = createSegment(type);
				r.growSynapses(position, growNum, growPositions, config.initialPermanence, config.maxSynapsesPerSegment);
			}
		}
		return r;
	}
	
	protected Segment createSegment(String type) {
		Segment r = new Segment();
		r.type = type;
		List<Segment> list = getSegments(type);
		list.add(0,r);
		while (list.size()>config.maxSegmentsPerCell) {
			Segment remove = list.remove(list.size() - 1);
			remove.clear();
		}
		return r;
	}
	
	protected static void clearSegments(List<Segment> segments) {
		for (Segment segment: segments) {
			segment.clear();
		}
		segments.clear();
	}
}
