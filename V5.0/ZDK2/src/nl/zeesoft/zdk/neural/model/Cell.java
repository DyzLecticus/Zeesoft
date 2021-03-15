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
		reset();
	}

	public void reset() {
		resetSegments(proximalSegments);
		resetSegments(distalSegments);
		resetSegments(apicalSegments);
		
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

	public List<Segment> getMatchingSegments(String type) {
		List<Segment> r = null;
		if (type.equals(Segment.DISTAL)) {
			r = matchingDistalSegments;
		} else if (type.equals(Segment.APICAL)) {
			r = matchingApicalSegments;
		}
		return r;
	}
	
	public void calculateSegmentActivity(
		List<Position> activeCellPositions,
		List<Position> activeApicalCellPositions
		) {
		calculateSegmentActivity(Segment.DISTAL, activeCellPositions);
		calculateSegmentActivity(Segment.APICAL, activeApicalCellPositions);
	}
	
	public void classifySegmentActivity() {
		matchingDistalSegment = classifySegmentActivity(
			distalSegments, activeDistalSegments, matchingDistalSegments
		);
		matchingApicalSegment = classifySegmentActivity(
			apicalSegments, activeApicalSegments, matchingApicalSegments
		);
	}

	public boolean isPredictive(List<Position> activeApicalCellPositions) {
		boolean r = false;
		if ((activeApicalCellPositions.size()==0 && activeDistalSegments.size()>0) ||
			(activeApicalCellPositions.size()>0 && activeDistalSegments.size()>0 && activeApicalSegments.size()>0)
			) {
			r = true;
		}
		return r;
	}
	
	public void adaptActiveSegments(
		List<Position> prevActiveCellPositions,
		List<Position> prevWinnerCellPositions,
		List<Position> prevActiveApicalCellPositions
		) {
		adaptActiveSegments(
			activeDistalSegments, prevActiveCellPositions, prevWinnerCellPositions, config.distalPotentialRadius
		);
		if (prevActiveApicalCellPositions.size()>0) {
			adaptActiveSegments(
				activeApicalSegments, prevActiveApicalCellPositions, prevActiveApicalCellPositions, config.apicalPotentialRadius
			);
		}
	}
	
	public void adaptMatchingSegments(
		List<Position> prevActiveCellPositions,
		List<Position> prevWinnerCellPositions,
		List<Position> prevActiveApicalCellPositions
		) {
		if (matchingDistalSegment!=null) {
			adaptMatchingSegment(matchingDistalSegment, prevActiveCellPositions, prevWinnerCellPositions, config.distalPotentialRadius);
			if (matchingApicalSegment!=null) {
				adaptMatchingSegment(matchingApicalSegment, prevActiveApicalCellPositions, prevActiveApicalCellPositions, config.apicalPotentialRadius);
			}
		}
	}

	public void createSegments(
		List<Position> prevActiveCellPositions,
		List<Position> prevWinnerCellPositions,
		List<Position> prevActiveApicalCellPositions
		) {
		Segment distalSegment = createSegment(Segment.DISTAL, prevWinnerCellPositions, config.distalPotentialRadius);
		if (distalSegment!=null) {
			createSegment(Segment.APICAL, prevActiveApicalCellPositions, config.apicalPotentialRadius);
		}
	}

	protected void calculateSegmentActivity(String type, List<Position> activeCellPositions) {
		List<Segment> list = getSegments(type);
		for (Segment segment: list) {
			segment.calculateActiveAndPotentialSynapses(activeCellPositions, config.permanenceThreshold);
		}
	}
	
	protected Segment classifySegmentActivity(List<Segment> segments, List<Segment> active, List<Segment> matching) {
		Segment r = null;
		active.clear();
		matching.clear();
		r = classifyActiveAndMatchingSegments(
			segments,active,matching
		);
		if (r!=null) {
			List<Segment> list = new ArrayList<Segment>(matching);
			for (Segment segment: list) {
				matching.add(segment);
				segments.remove(segment);
				segments.add(0,segment);
			}
			segments.remove(r);
			segments.add(0, r);
		}
		return r;
	}

	protected Segment classifyActiveAndMatchingSegments(
		List<Segment> segments,
		List<Segment> active,List<Segment> matching
		) {
		Segment r = null;
		for (Segment segment: segments) {
			if (segment.activeSynapses.size() >= config.activationThreshold) {
				active.add(segment);
			} else if (segment.potentialSynapses.size() >= config.matchingThreshold) {
				matching.add(segment);
			}
		}
		int potential = 0;
		for (Segment segment: matching) {
			if (segment.potentialSynapses.size()>potential) {
				r = segment;
				potential = segment.potentialSynapses.size();
			}
		}
		return r;
	}
	
	protected void adaptActiveSegments(
		List<Segment> activeSegments,
		List<Position> prevActiveCellPositions,
		List<Position> prevWinnerCellPositions,
		float potentialRadius
		) {
		List<Position> growPositions = prevWinnerCellPositions;
		if (potentialRadius>0) {
			growPositions = position.selectPositionsLimitDistance(potentialRadius, growPositions);
		}
		for (Segment segment: activeSegments) {
			segment.adaptSynapses(prevActiveCellPositions, config.permanenceIncrement, config.permanenceDecrement);
			int growNum = config.maxNewSynapseCount - segment.activeSynapses.size();
			if (growNum>0) {
				segment.growSynapses(
					position, growNum, growPositions, config.initialPermanence, config.maxSynapsesPerSegment
				);								
			}
		}
	}
	
	protected void adaptMatchingSegment(
		Segment matchingSegment,
		List<Position> prevActiveCellPositions,
		List<Position> prevWinnerCellPositions,
		float potentialRadius
		) {
		List<Position> growPositions = prevWinnerCellPositions;
		if (potentialRadius>0) {
			growPositions = position.selectPositionsLimitDistance(potentialRadius, growPositions);
		}
		matchingSegment.adaptSynapses(prevActiveCellPositions, config.permanenceIncrement, config.permanenceDecrement);
		int growNum = config.maxNewSynapseCount - matchingSegment.potentialSynapses.size();
		if (growNum>0) {
			matchingSegment.growSynapses(
				position, growNum, growPositions, config.initialPermanence, config.maxSynapsesPerSegment
			);								
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
	
	protected static void resetSegments(List<Segment> segments) {
		for (Segment segment: segments) {
			segment.reset();
		}
	}
}
