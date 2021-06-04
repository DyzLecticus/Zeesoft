package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.model.CellConfig;
import nl.zeesoft.zdk.neural.model.CellStringConvertor;
import nl.zeesoft.zdk.neural.model.Cells;
import nl.zeesoft.zdk.neural.model.CellsStringConvertor;
import nl.zeesoft.zdk.neural.model.Segment;
import nl.zeesoft.zdk.neural.model.SegmentStringConvertor;
import nl.zeesoft.zdk.neural.model.Synapse;
import nl.zeesoft.zdk.neural.model.SynapseStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class TestCellsStringConvertor {
	private static TestCellsStringConvertor self = new TestCellsStringConvertor();
	
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		Cell cell = new Cell(new Position(1,0,0), null);
		
		testCell(cell);
		
		addSegments(cell);

		testCell(cell);
		
		CellConfig config = new CellConfig();
		config.size = new Size(2,2,2);
		Cells cells = new Cells(self, config);
		addSegments(cells.getCell(new Position(0,0,0)));
		addSegments(cells.getCell(new Position(0,1,0)));

		assert cells.getCell(new Position(0,0,1)).distalSegments.segments.size() == 0;

		CellsStringConvertor conv = (CellsStringConvertor) ObjectStringConvertors.getConvertor(Cells.class);
		
		StringBuilder str = conv.toStringBuilder(cells);
		Cells cells2 = conv.fromStringBuilder(str);
		StringBuilder str2 = conv.toStringBuilder(cells2);
		assert StrUtil.equals(str2, str);
		
		assert cells2.getCell(new Position(0,0,1)).distalSegments.segments.size() == 0;

		assert conv.toStringBuilder(cell).length() == 0;
		assert conv.fromStringBuilder(new StringBuilder()) == null;
		
		SynapseStringConvertor sysc = (SynapseStringConvertor) ObjectStringConvertors.getConvertor(Synapse.class);
		assert sysc.toStringBuilder(cell).length() == 0;
		SegmentStringConvertor sesc = (SegmentStringConvertor) ObjectStringConvertors.getConvertor(Segment.class);
		assert sesc.toStringBuilder(cell).length() == 0;
		assert sesc.fromStringBuilder(new StringBuilder("")) == null;
		CellStringConvertor csc = (CellStringConvertor) ObjectStringConvertors.getConvertor(Cell.class);
		assert csc.toStringBuilder(cell.position).length() == 0;
		assert csc.fromStringBuilder(new StringBuilder("1,2,3%")) == null;
		assert csc.fromStringBuilder(new StringBuilder("1,2,3%%%")) != null;
	}
	
	private static void addSegments(Cell cell) {
		cell.proximalSegments.segments.add(createSegment(1));
		cell.proximalSegments.segments.add(createSegment(2));
		cell.distalSegments.segments.add(createSegment(3));
		cell.distalSegments.segments.add(createSegment(4));
		cell.apicalSegments.segments.add(createSegment(5));
		cell.apicalSegments.segments.add(createSegment(6));
	}

	private static Segment createSegment(int base) {
		Segment seg = new Segment();
		Synapse syn1 = new Synapse();
		syn1.connectTo = new Position(0,0,base);
		syn1.permanence = 0.01F * (float) base;
		Synapse syn2 = new Synapse();
		syn2.connectTo = new Position(0,0,base+1);
		syn2.permanence = 0.02F * (float) base;
		
		seg.synapses.add(syn1);
		seg.synapses.add(syn2);
		
		return seg;
	}
	
	private static void testCell(Cell cell) {
		CellStringConvertor csc = (CellStringConvertor) ObjectStringConvertors.getConvertor(Cell.class);
		StringBuilder str = csc.toStringBuilder(cell);
		Cell cell2 = csc.fromStringBuilder(str);
		StringBuilder str2 = csc.toStringBuilder(cell2);
		assert StrUtil.equals(str2, str);
	}
}
