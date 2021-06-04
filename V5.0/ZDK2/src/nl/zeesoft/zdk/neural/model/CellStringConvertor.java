package nl.zeesoft.zdk.neural.model;

import java.util.List;

import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.PositionStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class CellStringConvertor extends ObjectStringConvertor {
	public PositionStringConvertor	positionConvertor	= (PositionStringConvertor) ObjectStringConvertors.getConvertor(Position.class);
	public SegmentStringConvertor	segmentConvertor	= (SegmentStringConvertor) ObjectStringConvertors.getConvertor(Segment.class);
	public String					typeSeparator		= "%";
	public String					segmentSeparator	= "^";

	@Override
	public Class<?> getObjectClass() {
		return Cell.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Cell) {
			Cell cell = (Cell) obj;
			r.append(positionConvertor.toStringBuilder(cell.position));
			appendSegments(r, cell);
		}
		return r;
	}

	@Override
	public Cell fromStringBuilder(StringBuilder str) {
		Cell r = null;
		List<StringBuilder> elems = StrUtil.split(str, typeSeparator);
		if (elems.size()==4) {
			r = new Cell((Position) positionConvertor.fromStringBuilder(elems.get(0)), null);
			parseSegments(r, elems);
		}
		return r;
	}
	
	protected void appendSegments(StringBuilder str, Cell cell) {
		appendSegments(str, cell.proximalSegments.segments);
		appendSegments(str, cell.distalSegments.segments);
		appendSegments(str, cell.apicalSegments.segments);
	}
	
	protected void appendSegments(StringBuilder str, List<Segment> segments) {
		str.append(typeSeparator);
		int i = 0;
		for (Segment seg: segments) {
			if (i>0) {
				str.append(segmentSeparator);
			}
			str.append(segmentConvertor.toStringBuilder(seg));
			i++;
		}
	}
	
	protected void parseSegments(Cell cell, List<StringBuilder> segments) {
		parseSegments(cell.proximalSegments.segments, segments.get(1));
		parseSegments(cell.distalSegments.segments, segments.get(2));
		parseSegments(cell.apicalSegments.segments, segments.get(3));
	}
	
	protected void parseSegments(List<Segment> list, StringBuilder segments) {
		List<StringBuilder> elems = StrUtil.split(segments, segmentSeparator);
		for (StringBuilder elem: elems) {
			Segment segment = segmentConvertor.fromStringBuilder(elem);
			if (segment!=null) {
				list.add(segment);
			}
		}
	}
}
