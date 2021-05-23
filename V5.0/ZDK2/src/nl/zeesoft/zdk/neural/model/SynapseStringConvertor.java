package nl.zeesoft.zdk.neural.model;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.matrix.Position;
import nl.zeesoft.zdk.matrix.PositionStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class SynapseStringConvertor extends ObjectStringConvertor {
	public PositionStringConvertor	positionConvertor	= (PositionStringConvertor) ObjectStringConvertors.getConvertor(Position.class);
	public String					separator			= ":";
	public DecimalFormat			permanenceFormat	= new DecimalFormat("0.000");

	@Override
	public Class<?> getObjectClass() {
		return Synapse.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Synapse) {
			Synapse syn = (Synapse) obj;
			r.append(positionConvertor.toStringBuilder(syn.connectTo));
			r.append(separator);
			r.append(permanenceFormat.format(syn.permanence));
		}
		return r;
	}

	@Override
	public Synapse fromStringBuilder(StringBuilder str) {
		Synapse r = null;
		List<StringBuilder> elems = StrUtil.split(str, separator);
		if (elems.size()==2) {
			r = new Synapse();
			r.connectTo = (Position) positionConvertor.fromStringBuilder(elems.get(0));
			r.permanence = Util.parseFloat(elems.get(1).toString());
		}
		return r;
	}
}
