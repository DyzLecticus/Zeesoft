package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.CellPruner;
import nl.zeesoft.zdk.neural.model.CellSegments;
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
		
		cells.setConfig(self, new CellConfig());
		assert cell.config != config;
		assert cell.proximalSegments.config != config;
		
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
		
		Segment segment1 = cell.distalSegments.matchingSegments.get(0);
		Synapse synapse1 = segment1.synapses.get(segment1.synapses.firstKey());
		Segment segment2 = cell.apicalSegments.matchingSegments.get(0);
		Synapse synapse2 = segment2.synapses.get(segment2.synapses.firstKey());
		assert synapse1.permanence == 0.21F;
		assert synapse2.permanence == 0.21F;
		cells.punishPredictedColumn(cell.position, CellSegments.DISTAL, winners1, 0F);
		assert synapse1.permanence == 0.21F;
		cells.punishPredictedColumn(cell.position, CellSegments.DISTAL, winners1, 0.1F);
		cells.punishPredictedColumn(cell.position, CellSegments.APICAL, apical1, 0.1F);
		assert synapse1.permanence == 0.10999999F;
		assert synapse2.permanence == 0.10999999F;
		
		Segment segment = new Segment();
		synapse1 = new Synapse();
		synapse1.permanence = 0.2F;
		synapse1.connectTo = new Position(0,0,0);
		segment.addSynapse(synapse1);
		synapse2 = new Synapse();
		synapse2.permanence = 0F;
		synapse2.connectTo = new Position(0,0,1);
		segment.addSynapse(synapse2);
		cell.proximalSegments.add(segment);
		
		segment1 = cell.distalSegments.get(0);
		segment1.synapses.get(segment1.synapses.firstKey()).permanence = 0;
		segment2 = cell.apicalSegments.get(0);
		segment2.synapses.get(segment2.synapses.firstKey()).permanence = 0;
		
		CellStats stats = new CellStats();
		stats.addModelCells(self, cells, true);
		assert stats.cells == 64;
		assert stats.proximalStats.segments == 1;
		assert stats.proximalStats.synapses == 1;
		assert stats.proximalStats.activeSynapses == 0;
		assert stats.distalStats.segments == 4;
		assert stats.distalStats.synapses == 8;
		assert stats.distalStats.activeSynapses == 0;
		assert stats.apicalStats.segments == 4;
		assert stats.apicalStats.synapses == 11;
		assert stats.apicalStats.activeSynapses == 0;
		
		stats = new CellStats();
		cells.config.permanenceThreshold = 0.1F;
		stats.addModelCells(self, cells, true);
		assert stats.cells == 64;
		assert stats.proximalStats.segments == 1;
		assert stats.proximalStats.synapses == 1;
		assert stats.proximalStats.activeSynapses == 1;
		assert stats.distalStats.segments == 4;
		assert stats.distalStats.synapses == 8;
		assert stats.distalStats.activeSynapses == 8;
		assert stats.apicalStats.segments == 4;
		assert stats.apicalStats.synapses == 11;
		assert stats.apicalStats.activeSynapses == 11;
		
		stats.addStats(stats);
		assert stats.cells == 128;
		assert stats.proximalStats.segments == 2;
		assert stats.proximalStats.synapses == 2;
		assert stats.proximalStats.activeSynapses == 2;
		assert stats.distalStats.segments == 8;
		assert stats.distalStats.synapses == 16;
		assert stats.distalStats.activeSynapses == 16;
		assert stats.apicalStats.segments == 8;
		assert stats.apicalStats.synapses == 22;
		assert stats.apicalStats.activeSynapses == 22;
		assert stats.toString().length() == 168;
		
		stats = new CellStats();
		assert stats.toString().length() == 8;

		// Test pruner
		cells.config.pruneSample = 1.0F;
		segment1 = cell.distalSegments.get(0);
		segment1.synapses.get(segment1.synapses.firstKey()).permanence = 0.4F;
		CellPruner pruner = new CellPruner();
		pruner.prune(cells);
		stats = new CellStats();
		stats.addModelCells(self, cells, true);
		assert stats.proximalStats.segments == 0;
		assert stats.proximalStats.synapses == 0;
		assert stats.distalStats.segments == 1;
		assert stats.distalStats.synapses == 1;
		assert stats.apicalStats.segments == 0;
		assert stats.apicalStats.synapses == 0;
	}
}
