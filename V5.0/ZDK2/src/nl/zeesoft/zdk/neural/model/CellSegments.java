package nl.zeesoft.zdk.neural.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.matrix.Position;

public class CellSegments {
	public static String	PROXIMAL			= "PROXIMAL";
	public static String	DISTAL				= "DISTAL";
	public static String	APICAL				= "APICAL";
	
	public Position			position			= null;
	public CellConfig		config				= null;
	public String			type				= DISTAL;
	
	public List<Segment>	segments			= new ArrayList<Segment>();
	public List<Segment>	activeSegments		= new ArrayList<Segment>();
	public List<Segment>	matchingSegments	= new ArrayList<Segment>();
	public Segment			matchingSegment		= null;
		
	public CellSegments(Position position, CellConfig config, String type) {
		this.position = position;
		this.config = config;
		this.type = type;
	}
	
	public void clear() {
		clearSegments(segments);
		reset();
	}

	public void reset() {
		resetSegments(segments);
		activeSegments.clear();
		matchingSegments.clear();
		matchingSegment = null;
	}
	
	public int size() {
		return segments.size();
	}
	
	public void add(Segment segment) {
		segments.add(segment);
	}
	
	public Segment get(int index) {
		return segments.get(index);
	}

	public void calculateSegmentActivity(List<Position> activeCellPositions) {
		for (Segment segment: segments) {
			segment.calculateActiveAndPotentialSynapses(activeCellPositions, config.permanenceThreshold);
		}
	}
	
	public void classifySegmentActivity() {
		classifyActiveAndMatchingSegments();
		if (matchingSegment!=null) {
			for (Segment segment: matchingSegments) {
				segments.remove(segment);
				segments.add(0,segment);
			}
			segments.remove(matchingSegment);
			segments.add(0, matchingSegment);
		}
	}
	
	public void adaptActiveSegments(
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
	
	public void adaptMatchingSegment(
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

	public Segment createSegment(List<Position> growPositions, float potentialRadius) {
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

	protected void classifyActiveAndMatchingSegments() {
		matchingSegment = null;
		activeSegments.clear();
		matchingSegments.clear();
		for (Segment segment: segments) {
			if (segment.activeSynapses.size() >= config.activationThreshold) {
				activeSegments.add(segment);
			} else if (segment.potentialSynapses.size() >= config.matchingThreshold) {
				matchingSegments.add(segment);
			}
		}
		int potential = 0;
		for (Segment segment: matchingSegments) {
			if (segment.potentialSynapses.size()>potential) {
				matchingSegment = segment;
				potential = segment.potentialSynapses.size();
			}
		}
	}
	
	protected Segment createSegment(String type) {
		Segment r = new Segment();
		segments.add(0,r);
		while (segments.size()>config.maxSegmentsPerCell) {
			Segment remove = segments.remove(segments.size() - 1);
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
