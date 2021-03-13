package nl.zeesoft.zdk.test.neural;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.neural.model.Segment;

public class TestSegment {
	public static void main(String[] args) {
		Position cellPosition = new Position(0,0,0);
		
		Position winner1 = new Position(0,0,1);
		Position winner2 = new Position(0,1,1);
		Position winner3 = new Position(1,1,1);
		Position winner4 = new Position(0,1,0);
		Position winner5 = new Position(1,1,0);
		Position winner6 = new Position(0,1,0);
		Position winnerSelf = new Position(0,0,0);
		
		List<Position> prevWinnerCellPositions = new ArrayList<Position>();
		prevWinnerCellPositions.add(winner1);
		prevWinnerCellPositions.add(winner2);
		prevWinnerCellPositions.add(winner3);
		prevWinnerCellPositions.add(winner4);
		prevWinnerCellPositions.add(winner5);
		prevWinnerCellPositions.add(winner6);
		prevWinnerCellPositions.add(winnerSelf);
		
		List<Position> prevWinnerCellPositionSubset1 = new ArrayList<Position>();
		prevWinnerCellPositionSubset1.add(winner1);
		prevWinnerCellPositionSubset1.add(winner2);
		prevWinnerCellPositionSubset1.add(winner3);
		
		List<Position> prevWinnerCellPositionSubset2 = new ArrayList<Position>();
		prevWinnerCellPositionSubset2.add(winner4);
		prevWinnerCellPositionSubset2.add(winner5);
		prevWinnerCellPositionSubset2.add(winner6);

		Segment segment = new Segment();
		segment.growSynapses(cellPosition, 1, prevWinnerCellPositions, 0.3F, 3);
		assert segment.synapses.size() == 1;
		assert segment.synapses.get(0).permanence == 0.3F;
		
		segment.growSynapses(cellPosition, 10, prevWinnerCellPositionSubset1, 0.5F, 3);
		assert segment.synapses.size() == 3;
		assert segment.getSynapseTo(winner1) != null;
		assert segment.getSynapseTo(winner2) != null;
		assert segment.getSynapseTo(winner3) != null;
		
		segment.growSynapses(cellPosition, 10, prevWinnerCellPositionSubset2, 0.7F, 3);
		assert segment.synapses.size() == 3;
		assert segment.getSynapseTo(winner4) != null;
		assert segment.getSynapseTo(winner5) != null;
		assert segment.getSynapseTo(winner6) != null;

		segment = new Segment();
		segment.growSynapses(cellPosition, 10, prevWinnerCellPositions, 0.5F, 3);
		assert segment.synapses.size() == 6;

		segment = new Segment();
		segment.growSynapses(cellPosition, 5, prevWinnerCellPositions, 0.5F, 10);
		assert segment.synapses.size() == 5;

		segment = new Segment();
		segment.growSynapses(cellPosition, 10, prevWinnerCellPositions, 0.5F, 10);
		assert segment.synapses.size() == 6;
		segment.growSynapses(cellPosition, 10, prevWinnerCellPositions, 0.5F, 10);
		assert segment.synapses.size() == 6;

		segment.getSynapseTo(winner1).permanence = 0.45F;
		segment.calculateActiveAndPotentialSynapses(prevWinnerCellPositionSubset1, 0.49F);
		assert segment.activeSynapses.size() == 2;
		assert segment.potentialSynapses.size() == 3;

		segment.adaptSynapses(prevWinnerCellPositionSubset1, 0.3F, 0.3F);
		assert segment.getSynapseTo(winner1).permanence == 0.75F;
		assert segment.getSynapseTo(winner2).permanence == 0.8F;
		assert segment.getSynapseTo(winner3).permanence == 0.8F;
		assert segment.getSynapseTo(winner4).permanence == 0.19999999F;
		assert segment.getSynapseTo(winner5).permanence == 0.19999999F;
		assert segment.getSynapseTo(winner6).permanence == 0.19999999F;

		segment.adaptSynapses(prevWinnerCellPositionSubset1, 0.3F, 0.3F);
		assert segment.synapses.size() == 3;
		assert segment.getSynapseTo(winner1).permanence == 1F;
		assert segment.getSynapseTo(winner2).permanence == 1F;
		assert segment.getSynapseTo(winner3).permanence == 1F;
		
		segment.calculateActiveAndPotentialSynapses(prevWinnerCellPositionSubset2, 0.49F);
		assert segment.activeSynapses.size() == 0;
		assert segment.potentialSynapses.size() == 0;
	}
}
