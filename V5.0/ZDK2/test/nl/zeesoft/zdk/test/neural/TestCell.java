package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.CellSegments;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.model.Synapse;

public class TestCell {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		CellConfig config = new CellConfig();
		Position position = new Position(0,0,0);
		Cell cell = new Cell(position, config);
		
		List<Position> winners1 = new ArrayList<Position>();
		winners1.add(new Position(1,0,0));
		winners1.add(new Position(2,0,0));
		winners1.add(new Position(3,0,0));
		winners1.add(new Position(1,1,0));
		winners1.add(new Position(2,2,0));
		winners1.add(new Position(3,3,0));
		
		List<Position> apical1 = new ArrayList<Position>();
		apical1.add(new Position(1,0,1));
		apical1.add(new Position(2,0,1));
		apical1.add(new Position(3,0,1));
		apical1.add(new Position(1,1,1));
		apical1.add(new Position(2,2,1));
		apical1.add(new Position(3,3,1));
		
		assert cell.getSegments(CellSegments.PROXIMAL) == cell.proximalSegments;
		assert cell.getSegments(CellSegments.DISTAL) == cell.distalSegments;
		assert cell.getSegments(CellSegments.APICAL) == cell.apicalSegments;
		assert cell.getSegments("Pizza") == null;

		config.segmentCreationSubsample = 1F;
		cell.createSegments(null, winners1, new ArrayList<Position>());
		assert cell.distalSegments.size() == 1;
		cell.clear();
		assert cell.distalSegments.size() == 0;

		config.segmentCreationSubsample = 2F;
		cell.createSegments(null, winners1, new ArrayList<Position>());
		assert cell.distalSegments.size() == 1;
		cell.clear();

		config.segmentCreationSubsample = -1F;
		cell.createSegments(null, winners1, new ArrayList<Position>());
		assert cell.distalSegments.size() == 0;
		cell.clear();
		
		config.segmentCreationSubsample = 1F;
		cell.createSegments(null, winners1, apical1);
		assert cell.distalSegments.size() == 1;
		assert cell.apicalSegments.size() == 1;
		assert cell.apicalSegments.get(0).synapses.size() == 6;
		cell.clear();
		assert cell.apicalSegments.size() == 0;
		
		config.maxSegmentsPerCell = 1;
		config.maxNewSynapseCount = 5;
		cell.createSegments(null, winners1, new ArrayList<Position>());
		cell.createSegments(null, apical1, new ArrayList<Position>());
		assert cell.distalSegments.size() == 1;
		assert cell.distalSegments.get(0).synapses.size() == 5;
		cell.clear();
		
		config.distalPotentialRadius = 2;
		cell.createSegments(null, winners1, new ArrayList<Position>());
		assert cell.distalSegments.size() == 1;
		assert cell.distalSegments.get(0).synapses.size() == 3;
		cell.clear();
		
		config.apicalPotentialRadius = 2;
		cell.createSegments(null, winners1, apical1);
		assert cell.apicalSegments.size() == 1;
		assert cell.apicalSegments.get(0).synapses.size() == 2;
		cell.clear();
		
		config.distalPotentialRadius = 0.1F;
		cell.createSegments(null, winners1, new ArrayList<Position>());
		assert cell.distalSegments.size() == 0;
		cell.clear();

		List<Position> winners2 = new ArrayList<Position>();
		winners2.add(new Position(1,0,0));
		winners2.add(new Position(2,0,2));
		winners2.add(new Position(3,0,2));
		winners2.add(new Position(1,1,0));
		winners2.add(new Position(2,2,2));
		winners2.add(new Position(3,3,2));
		
		List<Position> apical2 = new ArrayList<Position>();
		apical2.add(new Position(1,0,0));
		apical2.add(new Position(2,0,3));
		apical2.add(new Position(3,0,3));
		apical2.add(new Position(1,1,0));
		apical2.add(new Position(2,2,3));
		apical2.add(new Position(3,3,3));
	
		config.segmentCreationSubsample = 1F;
		config.maxSegmentsPerCell = 10;
		config.maxNewSynapseCount = 20;
		config.distalPotentialRadius = 0;
		config.apicalPotentialRadius = 0;
		cell.createSegments(null, winners1, apical1);
		cell.createSegments(null, winners1, apical1);
		cell.createSegments(null, winners2, apical2);
		assert cell.getPotential() == 0;
		
		List<Position> active = new ArrayList<Position>();
		active.add(winners1.get(0));
		active.add(winners1.get(1));
		active.add(apical1.get(0));
		active.add(apical1.get(1));
		cell.calculateSegmentActivity(active, active);
		assert cell.distalSegments.get(0).activeSynapses.size() == 0;
		assert cell.distalSegments.get(0).potentialSynapses.size() == 1;
		assert cell.apicalSegments.get(0).activeSynapses.size() == 0;
		assert cell.apicalSegments.get(0).potentialSynapses.size() == 1;
		
		config.permanenceThreshold = config.initialPermanence - 0.01F;
		cell.calculateSegmentActivity(active, active);
		assert cell.distalSegments.get(0).activeSynapses.size() == 1;
		assert cell.distalSegments.get(0).potentialSynapses.size() == 1;
		assert cell.apicalSegments.get(0).activeSynapses.size() == 1;
		assert cell.apicalSegments.get(0).potentialSynapses.size() == 1;
		
		cell.classifySegmentActivity();
		assert cell.distalSegments.activeSegments.size() == 0;
		assert cell.distalSegments.matchingSegments.size() == 0;
		assert cell.distalSegments.matchingSegment == null;
		assert cell.apicalSegments.activeSegments.size() == 0;
		assert cell.apicalSegments.matchingSegments.size() == 0;
		assert cell.apicalSegments.matchingSegment == null;

		config.activationThreshold = 2;
		config.matchingThreshold = 1;
		cell.classifySegmentActivity();
		assert cell.distalSegments.activeSegments.size() == 2;
		assert cell.distalSegments.matchingSegments.size() == 1;
		assert cell.distalSegments.matchingSegment != null;
		assert cell.apicalSegments.activeSegments.size() == 2;
		assert cell.apicalSegments.matchingSegments.size() == 1;
		assert cell.apicalSegments.matchingSegment != null;
		assert cell.getPotential() == 2;

		List<Position> subset = new ArrayList<Position>(active);
		subset.remove(1);
		subset.remove(2);
		cell.calculateSegmentActivity(subset, subset);
		cell.classifySegmentActivity();
		assert cell.distalSegments.activeSegments.size() == 0;
		assert cell.distalSegments.matchingSegments.size() == 3;
		assert cell.distalSegments.matchingSegment != null;
		assert cell.apicalSegments.activeSegments.size() == 0;
		assert cell.apicalSegments.matchingSegments.size() == 3;
		assert cell.apicalSegments.matchingSegment != null;
		assert cell.getPotential() == 2;
		
		cell.distalSegments.add(cell.distalSegments.matchingSegments.remove(0));
		cell.apicalSegments.add(cell.apicalSegments.matchingSegments.remove(0));
		cell.reset();
		assert cell.distalSegments.activeSegments.size() == 0;
		assert cell.distalSegments.matchingSegments.size() == 0;
		assert cell.distalSegments.matchingSegment == null;
		assert cell.apicalSegments.activeSegments.size() == 0;
		assert cell.apicalSegments.matchingSegments.size() == 0;
		assert cell.apicalSegments.matchingSegment == null;
		assert !cell.isPredictive(new ArrayList<Position>());
		assert !cell.isPredictive(active);
		cell.distalSegments.activeSegments.add(new Segment());
		assert cell.isPredictive(new ArrayList<Position>());
		assert !cell.isPredictive(active);

		cell.clear();
		cell.createSegments(null, winners1, apical1);
		cell.createSegments(null, winners1, apical1);
		cell.createSegments(null, winners2, apical2);
		cell.calculateSegmentActivity(active, active);
		cell.classifySegmentActivity();
		assert cell.distalSegments.activeSegments.size() == 2;
		assert cell.distalSegments.matchingSegments.size() == 1;
		assert cell.distalSegments.matchingSegment != null;
		assert cell.apicalSegments.activeSegments.size() == 2;
		assert cell.apicalSegments.matchingSegments.size() == 1;
		assert cell.apicalSegments.matchingSegment != null;
		assert cell.isPredictive(new ArrayList<Position>());
		assert cell.isPredictive(active);
		
		assert cell.getSegments(CellSegments.PROXIMAL) == cell.proximalSegments;
		assert cell.getSegments(CellSegments.DISTAL) == cell.distalSegments;
		assert cell.getSegments(CellSegments.APICAL) == cell.apicalSegments;
		assert cell.getSegments("Pizza") == null;
		
		Segment segment1 = cell.distalSegments.activeSegments.get(0);
		Synapse synapse1 = segment1.synapses.get(segment1.synapses.firstKey());
		Segment segment2 = cell.apicalSegments.activeSegments.get(0);
		Synapse synapse2 = segment2.synapses.get(segment2.synapses.firstKey());
		assert synapse1.permanence == 0.21F;
		assert synapse2.permanence == 0.21F;

		cell.adaptActiveSegments(winners1, winners1, new ArrayList<Position>());
		assert synapse1.permanence == 0.31F;
		assert synapse2.permanence == 0.21F;

		cell.adaptActiveSegments(winners1, winners1, apical1);
		assert synapse1.permanence == 0.41F;
		assert synapse2.permanence == 0.31F;
		
		winners1.add(winners2.get(5));
		config.distalPotentialRadius = 0.1F;
		config.apicalPotentialRadius = 0.1F;
		config.maxNewSynapseCount = 0;
		assert cell.distalSegments.get(0).synapses.size() == 6;
		cell.adaptActiveSegments(winners1, winners1, apical1);
		assert synapse1.permanence == 0.51F;
		assert synapse2.permanence == 0.41F;
		assert cell.distalSegments.get(0).synapses.size() == 6;

		config.distalPotentialRadius = 0F;
		config.apicalPotentialRadius = 0F;
		config.maxNewSynapseCount = 10;

		segment1 = cell.distalSegments.matchingSegment;
		synapse1 = segment1.synapses.get(segment1.synapses.firstKey());
		segment2 = cell.apicalSegments.matchingSegment;
		synapse2 = segment2.synapses.get(segment2.synapses.firstKey());
		assert synapse1.permanence == 0.21F;
		assert synapse2.permanence == 0.21F;
		cell.adaptMatchingSegments(winners2, winners2, apical2);
		assert synapse1.permanence == 0.31F;
		assert synapse2.permanence == 0.31F;
		
		config.distalPotentialRadius = 0.1F;
		config.apicalPotentialRadius = 0.1F;
		config.maxNewSynapseCount = 0;
		cell.adaptMatchingSegments(winners2, winners2, apical2);
		assert synapse1.permanence == 0.41F;
		assert synapse2.permanence == 0.41F;
		
		cell.apicalSegments.matchingSegment = null;
		cell.adaptMatchingSegments(winners2, winners2, apical2);
		assert synapse1.permanence == 0.51F;
		assert synapse2.permanence == 0.41F;
		cell.distalSegments.matchingSegment = null;
		cell.adaptMatchingSegments(winners2, winners2, apical2);
		assert synapse1.permanence == 0.51F;
		assert synapse2.permanence == 0.41F;
	}
}
