package nl.zeesoft.zdk.neural.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Rand;
import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.grid.Position;

public class Cell implements StrAble {
	public Position					position				= null;
	public List<ProximalSegment>	proximalSegments		= new ArrayList<ProximalSegment>();
	public List<DistalSegment>		distalSegments			= new ArrayList<DistalSegment>();
	public List<ApicalSegment>		apicalSegments			= new ArrayList<ApicalSegment>();

	public List<DistalSegment>		activeDistalSegments	= new ArrayList<DistalSegment>();
	public List<DistalSegment>		matchingDistalSegments	= new ArrayList<DistalSegment>();
	public DistalSegment			matchingDistalSegment	= null;
	
	public List<ApicalSegment>		activeApicalSegments	= new ArrayList<ApicalSegment>();
	public List<ApicalSegment>		matchingApicalSegments	= new ArrayList<ApicalSegment>();
	public ApicalSegment			matchingApicalSegment	= null;

	public Cell() {
		
	}
	
	public Cell(Position position) {
		this.position = position;
	}
	
	public Cell(Cell cell) {
		copyFrom(cell);
	}
	
	public void copyFrom(Cell cell) {
		clear();
		this.position = cell.position;
		for (ProximalSegment segment : cell.proximalSegments) {
			proximalSegments.add(new ProximalSegment(segment));
		}
		for (DistalSegment segment : cell.distalSegments) {
			distalSegments.add(new DistalSegment(segment));
		}
		for (ApicalSegment segment : cell.apicalSegments) {
			apicalSegments.add(new ApicalSegment(segment));
		}
	}
	
	public void calculateDistalSegmentActivity(List<Position> activeCellPositions, float permanenceThreshold) {
		for (DistalSegment segment: distalSegments) {
			segment.calculateActiveSynapses(activeCellPositions, permanenceThreshold);
		}
	}
	
	public void calculateApicalSegmentActivity(List<Position> activeApicalCellPositions, float permanenceThreshold) {
		for (ApicalSegment segment: apicalSegments) {
			segment.calculateActiveSynapses(activeApicalCellPositions, permanenceThreshold);
		}
	}
	
	public void classifySegmentActivity(int activationThreshold, int matchingThreshold) {
		activeDistalSegments.clear();
		matchingDistalSegments.clear();
		matchingDistalSegment = null;
		
		activeApicalSegments.clear();
		matchingApicalSegments.clear();
		matchingApicalSegment = null;
		
		List<Segment> active = new ArrayList<Segment>();
		List<Segment> matching = new ArrayList<Segment>();
		List<Segment> segments = new ArrayList<Segment>(distalSegments);
		Segment match = classifyActiveAndMatching(segments,activationThreshold,matchingThreshold,active,matching);
		for (Segment segment: active) {
			activeDistalSegments.add((DistalSegment)segment);
		}
		if (match!=null) {
			matchingDistalSegment = (DistalSegment) match;
			if (matchingDistalSegment!=null) {
				distalSegments.remove(matchingDistalSegment);
				distalSegments.add(0, matchingDistalSegment);
			}
			for (Segment segment: matching) {
				matchingDistalSegments.add((DistalSegment)segment);
				distalSegments.remove(segment);
				distalSegments.add(0, (DistalSegment)segment);
			}
		}
		
		active.clear();
		matching.clear();
		segments = new ArrayList<Segment>(apicalSegments);
		match = classifyActiveAndMatching(segments,activationThreshold,matchingThreshold,active,matching);
		for (Segment segment: active) {
			activeApicalSegments.add((ApicalSegment)segment);
		}
		if (match!=null) {
			matchingApicalSegment = (ApicalSegment) match;
			if (matchingApicalSegment!=null) {
				apicalSegments.remove(matchingApicalSegment);
				apicalSegments.add(0, matchingApicalSegment);
			}
			for (Segment segment: matching) {
				matchingApicalSegments.add((ApicalSegment)segment);
				apicalSegments.remove(segment);
				apicalSegments.add(0, (ApicalSegment)segment);
			}
		}
	}
	
	public void adaptActiveSegments(
		List<Position> prevActiveCellPositions, List<Position> prevWinnerCellPositions, List<Position> prevActiveApicalCellPositions,
		float initialPermanence, float permanenceIncrement, float permanenceDecrement, int maxNewSynapseCount, int maxSynapsesPerSegment,
		int distalPotentialRadius, int apicalPotentialRadius
		) {
		List<Position> growPositions = prevWinnerCellPositions;
		if (distalPotentialRadius>0) {
			growPositions = Position.selectMaxDistance(position.x, position.y, distalPotentialRadius, growPositions);
		}
		for (DistalSegment segment: activeDistalSegments) {
			segment.adaptSynapses(prevActiveCellPositions, permanenceIncrement, permanenceDecrement);
			int growNum = maxNewSynapseCount - segment.activeSynapses.size();
			if (growNum>0) {
				segment.growSynapses(
					position, growNum, growPositions, initialPermanence, maxSynapsesPerSegment);								
			}
		}
		if (prevActiveApicalCellPositions.size()>0) {
			growPositions = prevActiveApicalCellPositions;
			if (apicalPotentialRadius>0) {
				// Assumes apical input XY dimensions match model XY dimensions 
				growPositions = Position.selectMaxDistance(position.x, position.y, apicalPotentialRadius, growPositions);
			}
			for (ApicalSegment segment: activeApicalSegments) {
				segment.adaptSynapses(prevActiveApicalCellPositions, permanenceIncrement, permanenceDecrement);
				int growNum = maxNewSynapseCount - segment.activeSynapses.size();
				if (growNum>0) {
					segment.growSynapses(
						position, growNum, growPositions, initialPermanence, maxSynapsesPerSegment);								
				}
			}
		}
	}
	
	public void adaptMatchingSegments(
		List<Position> prevActiveCellPositions, List<Position> prevWinnerCellPositions, List<Position> prevActiveApicalCellPositions,
		float initialPermanence, float permanenceIncrement, float permanenceDecrement, int maxNewSynapseCount, int maxSynapsesPerSegment,
		int distalPotentialRadius, int apicalPotentialRadius
		) {
		if (matchingDistalSegment!=null) {
			List<Position> growPositions = prevWinnerCellPositions;
			if (distalPotentialRadius>0) {
				growPositions = Position.selectMaxDistance(position.x, position.y, distalPotentialRadius, growPositions);
			}
			matchingDistalSegment.adaptSynapses(prevActiveCellPositions, permanenceIncrement, permanenceDecrement);
			int growNum = maxNewSynapseCount - matchingDistalSegment.potentialSynapses.size();
			if (growNum>0) {
				matchingDistalSegment.growSynapses(
					position, growNum, growPositions, initialPermanence, maxSynapsesPerSegment);								
			}
			if (matchingApicalSegment!=null) {
				growPositions = prevActiveApicalCellPositions;
				if (apicalPotentialRadius>0) {
					// Assumes apical input XY dimensions match model XY dimensions 
					growPositions = Position.selectMaxDistance(position.x, position.y, apicalPotentialRadius, growPositions);
				}
				matchingApicalSegment.adaptSynapses(prevActiveApicalCellPositions, permanenceIncrement, permanenceDecrement);
				growNum = maxNewSynapseCount - matchingApicalSegment.potentialSynapses.size();
				if (growNum>0) {
					matchingApicalSegment.growSynapses(
						position, growNum, growPositions, initialPermanence, maxSynapsesPerSegment);								
				}
			}
		}
	}
	
	public void createSegments(
		List<Position> prevActiveCellPositions, List<Position> prevWinnerCellPositions, List<Position> prevActiveApicalCellPositions,
		float initialPermanence, float permanenceIncrement, float permanenceDecrement, int maxNewSynapseCount, int maxSegmentsPerCell, int maxSynapsesPerSegment,
		int distalPotentialRadius, int apicalPotentialRadius,
		float segmentCreationSubsample
		) {
		List<Position> growPositions = prevWinnerCellPositions;
		if (distalPotentialRadius>0) {
			growPositions = Position.selectMaxDistance(position.x, position.y, distalPotentialRadius, growPositions);
		}
		int growNum = maxNewSynapseCount;
		if (growPositions.size()<growNum) {
			growNum = growPositions.size();
		}
		if (growNum>0 && (segmentCreationSubsample==1F || Rand.getRandomFloat(0F, 1F) <= segmentCreationSubsample)) {
			DistalSegment distalSegment = (DistalSegment) createSegment(false,maxSegmentsPerCell);
			distalSegment.growSynapses(
				position, growNum, growPositions, initialPermanence, maxSynapsesPerSegment);								
			
			if (prevActiveApicalCellPositions.size()>0) {
				growPositions = prevActiveApicalCellPositions;
				if (apicalPotentialRadius>0) {
					// Assumes apical input XY dimensions match model XY dimensions 
					growPositions = Position.selectMaxDistance(position.x, position.y, apicalPotentialRadius, growPositions);
				}
				growNum = maxNewSynapseCount;
				if (growPositions.size()<growNum) {
					growNum = growPositions.size();
				}
				if (growNum>0) {
					ApicalSegment apicalSegment = (ApicalSegment) createSegment(true,maxSegmentsPerCell);
					apicalSegment.growSynapses(
						position, growNum, growPositions, initialPermanence, maxSynapsesPerSegment);								
				}
			}
		}

	}
	
	public void clear() {
		for (ProximalSegment segment: proximalSegments) {
			segment.synapses.clear();
		}
		proximalSegments.clear();
		
		for (DistalSegment segment: distalSegments) {
			segment.synapses.clear();
		}
		distalSegments.clear();

		for (ApicalSegment segment: apicalSegments) {
			segment.synapses.clear();
		}
		distalSegments.clear();
		
		reset();
	}
	
	public void reset() {
		for (ProximalSegment segment: proximalSegments) {
			segment.reset();
		}
		
		for (DistalSegment segment: distalSegments) {
			segment.reset();
		}
		activeDistalSegments.clear();
		matchingDistalSegments.clear();
		matchingDistalSegment = null;
		
		for (ApicalSegment segment: apicalSegments) {
			segment.reset();
		}
		activeDistalSegments.clear();
		matchingDistalSegments.clear();
		matchingApicalSegment = null;
	}

	@Override
	public Str toStr() {
		Str r = new Str();
		List<Segment> allSegments = new ArrayList<Segment>();
		allSegments.addAll(proximalSegments);
		allSegments.addAll(distalSegments);
		allSegments.addAll(apicalSegments);
		for (Segment segment: allSegments) {
			if (r.length()>0) {
				r.sb().append("!");
			}
			r.sb().append(segment.getClass().getSimpleName().substring(0, 1));
			r.sb().append("@");
			r.sb().append(segment.toStr().sb());
		}
		return r;
	}

	@Override
	public void fromStr(Str str) {
		clear();
		List<Str> elems = str.split("!");
		for (Str elem: elems) {
			List<Str> sElems = elem.split("@");
			Segment segment = null;
			if (sElems.get(0).toString().equals(ProximalSegment.class.getSimpleName().substring(0, 1))) {
				segment = new ProximalSegment();
				proximalSegments.add((ProximalSegment)segment);
			} else if (sElems.get(0).toString().equals(DistalSegment.class.getSimpleName().substring(0, 1))) {
				segment = new DistalSegment();
				distalSegments.add((DistalSegment)segment);
			} else if (sElems.get(0).toString().equals(ApicalSegment.class.getSimpleName().substring(0, 1))) {
				segment = new ApicalSegment();
				apicalSegments.add((ApicalSegment)segment);
			}
			segment.fromStr(sElems.get(1));
		}
	}
	
	protected Segment createSegment(boolean apical, int maxSegmentsPerCell) {
		Segment r = null;
		if (apical) {
			ApicalSegment s = new ApicalSegment();
			apicalSegments.add(0,s);
			while (distalSegments.size()>maxSegmentsPerCell) {
				DistalSegment remove = distalSegments.remove(distalSegments.size() - 1);
				remove.clear();
			}
			r = s;
		} else {
			DistalSegment s = new DistalSegment();
			distalSegments.add(0,s);
			while (distalSegments.size()>maxSegmentsPerCell) {
				DistalSegment remove = distalSegments.remove(distalSegments.size() - 1);
				remove.clear();
			}
			r = s;
		}
		return r;
	}
	
	protected Segment classifyActiveAndMatching(List<Segment> segments, int activationThreshold, int matchingThreshold, List<Segment> active, List<Segment> matching) {
		Segment r = null;
		for (Segment segment: segments) {
			if (segment.activeSynapses.size() >= activationThreshold) {
				active.add(segment);
			} else if (segment.potentialSynapses.size() >= matchingThreshold) {
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
}
