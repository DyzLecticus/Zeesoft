package nl.zeesoft.zdk.neural.processor.cl;

import java.util.List;

import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class ClBitsStringConvertor extends ObjectStringConvertor {
	public ClBitStringConvertor		bitConvertor		= (ClBitStringConvertor) ObjectStringConvertors.getConvertor(ClBit.class);
	public String					dataSeparator		= "@";
	
	@Override
	public Class<?> getObjectClass() {
		return ClBits.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof ClBits) {
			ClBits bits = (ClBits) obj;
			if (bits.bits.size()>0) {
				appendData(r, bits);
			}
		}
		return r;
	}

	@Override
	public ClBits fromStringBuilder(StringBuilder str) {
		ClBits r = new ClBits(null, null);
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		for (StringBuilder dat: data) {
			ClBit bit = bitConvertor.fromStringBuilder(dat);
			if (bit!=null) {
				r.bits.put(bit.index, bit);
			}
		}
		return r;
	}

	protected void appendData(StringBuilder str, ClBits bits) {
		for (ClBit bit: bits.bits.values()) {
			if (str.length()>0) {
				str.append(dataSeparator);
			}
			str.append(bitConvertor.toStringBuilder(bit));
		}
	}
}
