package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.CellStats;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.model.Synapse;

public class TestCells {
	private static TestCells	self	= new TestCells();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		CellConfig config = new CellConfig();
		config.size = new Size(4,4,4);
		config.segmentCreationSubsample = 1F;
		
		Cells cells = new Cells(self,config);
		
		Position position = new Position(0,0,0);
		Cell cell = cells.getCell(position);
		assert cell != null;
		assert cell.position.equals(position);
		assert cell.config == config;
		
		assert cells.isInitialized();
		cell.config = null;
		assert !cells.isInitialized();
		cell.config = config;
		cells.data[0][0][0] = null;
		assert !cells.isInitialized();
		cells.data[0][0][0] = cell;
		assert cells.isInitialized();
		cells.size = null;
		assert !cells.isInitialized();
		
		cells = new Cells(self,config);
		cell = cells.getCell(position);
		cell.proximalSegments.add(new Segment());
		cell.distalSegments.add(new Segment());
		cell.apicalSegments.add(new Segment());
		cells.reset(self);
		assert cell.proximalSegments.size() == 0; 
		assert cell.distalSegments.size() == 0; 
		assert cell.apicalSegments.size() == 0;
		
		config.matchingThreshold = 1;
		
		List<Position> winners1 = new ArrayList<Position>();
		winners1.add(new Position(0,0,0));
		winners1.add(new Position(1,0,0));
		winners1.add(new Position(2,0,0));
		List<Position> apical1 = new ArrayList<Position>();
		apical1.add(new Position(0,1,0));
		apical1.add(new Position(0,2,0));
		apical1.add(new Position(0,3,0));

		List<Position> winners2 = new ArrayList<Position>();
		winners2.add(new Position(0,0,0));
		winners2.add(new Position(1,1,0));
		winners2.add(new Position(2,1,0));
		List<Position> apical2 = new ArrayList<Position>();
		apical2.add(new Position(0,1,0));
		apical2.add(new Position(1,2,0));
		apical2.add(new Position(1,3,0));
		
		List<Position> columnPositions = new ArrayList<Position>();
		columnPositions.add(position);
		columnPositions.add(new Position(1,0,0));
		
		SortedMap<Integer,List<Cell>> cellsByPotential = cells.getCellsByPotential(columnPositions);
		assert cellsByPotential.size() == 0;

		SortedMap<Integer,List<Cell>> cellsBySegments = cells.getCellsBySegments(columnPositions);
		assert cellsBySegments.size() == 1;
		assert cellsBySegments.lastKey() == 0;
		List<Cell> segmentsCells = cellsBySegments.get(cellsBySegments.lastKey());
		assert segmentsCells.size() == 2;

		cell.createSegments(winners1, winners1, apical1);
		cell.createSegments(winners2, winners2, apical2);
		cell.calculateSegmentActivity(winners1,apical1);
		cell.classifySegmentActivity();

		Cell cell2 = cells.getCell(new Position(1,0,0));
		cell2.createSegments(winners1, winners1, apical1);
		cell2.createSegments(winners2, winners2, apical2);
		cell2.calculateSegmentActivity(winners1, apical1);
		cell2.classifySegmentActivity();
		
		cellsByPotential = cells.getCellsByPotential(columnPositions);
		assert cellsByPotential.size() == 1;
		assert cellsByPotential.firstKey() == 5;
		List<Cell> potentialCells = cellsByPotential.get(cellsByPotential.firstKey());
		assert potentialCells.size() == 2;
		
		cellsBySegments = cells.getCellsBySegments(columnPositions);
		assert cellsBySegments.size() == 1;
		assert cellsBySegments.lastKey() == 4;
		segmentsCells = cellsBySegments.get(cellsBySegments.lastKey());
		assert segmentsCells.size() == 2;
		
		Synapse synapse1 = cell.getMatchingSegments(Segment.DISTAL).get(0).synapses.get(0);
		Synapse synapse2 = cell.getMatchingSegments(Segment.APICAL).get(0).synapses.get(0);
		assert synapse1.permanence == 0.21F;
		assert synapse2.permanence == 0.21F;
		cells.punishPredictedColumn(cell.position, Segment.DISTAL, winners1, 0F);
		assert synapse1.permanence == 0.21F;
		cells.punishPredictedColumn(cell.position, Segment.DISTAL, winners1, 0.1F);
		cells.punishPredictedColumn(cell.position, Segment.APICAL, apical1, 0.1F);
		assert synapse1.permanence == 0.00999999F;
		assert synapse2.permanence == 0.00999999F;
		
		Segment segment = new Segment();
		Synapse synapse = new Synapse();
		synapse.permanence = 0.2F;
		segment.synapses.add(synapse);
		synapse = new Synapse();
		synapse.permanence = 0F;
		segment.synapses.add(synapse);
		cell.proximalSegments.add(segment);
		cell.distalSegments.get(0).synapses.get(0).permanence = 0;
		cell.apicalSegments.get(0).synapses.get(0).permanence = 0;
		
		CellStats stats = new CellStats();
		stats.addModelCells(self, cells);
		assert stats.cells == 64;
		assert stats.proximalSegments == 1;
		assert stats.proximalSynapses == 1;
		assert stats.activeProximalSynapses == 0;
		assert stats.distalSegments == 4;
		assert stats.distalSynapses == 8;
		assert stats.activeDistalSynapses == 0;
		assert stats.apicalSegments == 4;
		assert stats.apicalSynapses == 11;
		assert stats.activeApicalSynapses == 0;
		
		stats = new CellStats();
		cells.config.permanenceThreshold = 0.1F;
		stats.addModelCells(self, cells);
		assert stats.cells == 64;
		assert stats.proximalSegments == 1;
		assert stats.proximalSynapses == 1;
		assert stats.activeProximalSynapses == 1;
		assert stats.distalSegments == 4;
		assert stats.distalSynapses == 8;
		assert stats.activeDistalSynapses == 7;
		assert stats.apicalSegments == 4;
		assert stats.apicalSynapses == 11;
		assert stats.activeApicalSynapses == 8;
		
		stats.addStats(stats);
		assert stats.cells == 128;
		assert stats.proximalSegments == 2;
		assert stats.proximalSynapses == 2;
		assert stats.activeProximalSynapses == 2;
		assert stats.distalSegments == 8;
		assert stats.distalSynapses == 16;
		assert stats.activeDistalSynapses == 14;
		assert stats.apicalSegments == 8;
		assert stats.apicalSynapses == 22;
		assert stats.activeApicalSynapses == 16;
		
		assert stats.toString().length() == 209;
		stats = new CellStats();
		assert stats.toString().length() == 23;
	}
}
