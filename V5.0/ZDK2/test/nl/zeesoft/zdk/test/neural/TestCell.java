package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.Segment;

public class TestCell {
	public static void main(String[] args) {
		CellConfig config = new CellConfig();
		Position position = new Position(0,0,0);
		Cell cell = new Cell();
		cell.position = position;
		cell.config = config;
		
		List<Position> winners = new ArrayList<Position>();
		winners.add(new Position(1,0,0));
		winners.add(new Position(2,0,0));
		winners.add(new Position(3,0,0));
		winners.add(new Position(1,1,0));
		winners.add(new Position(2,2,0));
		winners.add(new Position(3,3,0));
		
		List<Position> apical = new ArrayList<Position>();
		apical.add(new Position(1,0,1));
		apical.add(new Position(2,0,1));
		apical.add(new Position(3,0,1));
		apical.add(new Position(1,1,1));
		apical.add(new Position(2,2,1));
		apical.add(new Position(3,3,1));
		
		assert cell.getSegments(Segment.PROXIMAL) == cell.proximalSegments;
		assert cell.getSegments(Segment.DISTAL) == cell.distalSegments;
		assert cell.getSegments(Segment.APICAL) == cell.apicalSegments;
		assert cell.getSegments("Pizza") == null;
		
		config.segmentCreationSubsample = 1F;
		cell.createSegments(null, winners, new ArrayList<Position>());
		assert cell.distalSegments.size() == 1;
		cell.clear();
		assert cell.distalSegments.size() == 0;

		config.segmentCreationSubsample = 2F;
		cell.createSegments(null, winners, new ArrayList<Position>());
		assert cell.distalSegments.size() == 1;
		cell.clear();

		config.segmentCreationSubsample = -1F;
		cell.createSegments(null, winners, new ArrayList<Position>());
		assert cell.distalSegments.size() == 0;
		cell.clear();
		
		config.segmentCreationSubsample = 1F;
		cell.createSegments(null, winners, apical);
		assert cell.distalSegments.size() == 1;
		assert cell.apicalSegments.size() == 1;
		assert cell.apicalSegments.get(0).synapses.size() == 6;
		cell.clear();
		assert cell.apicalSegments.size() == 0;
		
		config.maxSegmentsPerCell = 1;
		config.maxNewSynapseCount = 5;
		cell.createSegments(null, winners, new ArrayList<Position>());
		cell.createSegments(null, apical, new ArrayList<Position>());
		assert cell.distalSegments.size() == 1;
		assert cell.distalSegments.get(0).synapses.size() == 5;
		cell.clear();
		
		config.distalPotentialRadius = 2;
		cell.createSegments(null, winners, new ArrayList<Position>());
		assert cell.distalSegments.size() == 1;
		assert cell.distalSegments.get(0).synapses.size() == 3;
		cell.clear();
		
		config.apicalPotentialRadius = 2;
		cell.createSegments(null, winners, apical);
		assert cell.apicalSegments.size() == 1;
		assert cell.apicalSegments.get(0).synapses.size() == 2;
		cell.clear();
		
		config.distalPotentialRadius = 0.1F;
		cell.createSegments(null, winners, new ArrayList<Position>());
		assert cell.distalSegments.size() == 0;
		cell.clear();
	
		config.segmentCreationSubsample = 1F;
		config.maxSegmentsPerCell = 10;
		config.maxNewSynapseCount = 20;
		config.distalPotentialRadius = 0;
		config.apicalPotentialRadius = 0;
		cell.createSegments(null, winners, apical);
		
		List<Position> active = new ArrayList<Position>();
		active.add(winners.get(0));
		active.add(winners.get(1));
		active.add(apical.get(0));
		active.add(apical.get(1));
		cell.calculateSegmentActivity(Segment.DISTAL, active);
		assert cell.distalSegments.get(0).activeSynapses.size() == 0;
		config.permanenceThreshold = config.initialPermanence - 0.01F;
		cell.calculateSegmentActivity(Segment.DISTAL, active);
		assert cell.distalSegments.get(0).activeSynapses.size() == 2;
	}
}
