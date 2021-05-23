package nl.zeesoft.zdk.neural.model;

import java.util.List;

import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class SegmentStringConvertor extends ObjectStringConvertor {
	public SynapseStringConvertor	synapseConvertor	= (SynapseStringConvertor) ObjectStringConvertors.getConvertor(Synapse.class);
	public String					separator			= "@";

	@Override
	public Class<?> getObjectClass() {
		return Segment.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof Segment) {
			Segment seg = (Segment) obj;
			for (Synapse syn: seg.synapses) {
				if (r.length()>0) {
					r.append(separator);
				}
				r.append(synapseConvertor.toStringBuilder(syn));
			}
		}
		return r;
	}

	@Override
	public Segment fromStringBuilder(StringBuilder str) {
		Segment r = null;
		List<StringBuilder> elems = StrUtil.split(str, separator);
		for (StringBuilder elem: elems) {
			Synapse syn = (Synapse) synapseConvertor.fromStringBuilder(elem);
			if (syn!=null) {
				if(r==null) {
					r = new Segment();
				}
				r.synapses.add(syn);
			}
		}
		return r;
	}
}
