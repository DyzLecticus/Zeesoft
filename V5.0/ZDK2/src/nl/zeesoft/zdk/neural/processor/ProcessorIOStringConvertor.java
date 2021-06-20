package nl.zeesoft.zdk.neural.processor;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.SdrStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class ProcessorIOStringConvertor extends ObjectStringConvertor {
	public String				dataSeparator	= ";";
	public String				valueSeparator	= "#";
	public SdrStringConvertor	sdrConvertor	= (SdrStringConvertor) ObjectStringConvertors.getConvertor(Sdr.class);
	
	@Override
	public Class<?> getObjectClass() {
		return ProcessorIO.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof ProcessorIO) {
			ProcessorIO io = (ProcessorIO) obj;
			r.append(getDataTypeStringBuilderForObject(io.inputValue, valueSeparator));
			r.append(dataSeparator);
			r.append(getDataTypeStringBuilderForObject(io.outputValue, valueSeparator));
			r.append(dataSeparator);
			appendSdrs(r, io.inputs);
			r.append(dataSeparator);
			appendSdrs(r, io.outputs);
			r.append(dataSeparator);
			r.append(io.error);
		}
		return r;
	}

	@Override
	public ProcessorIO fromStringBuilder(StringBuilder str) {
		ProcessorIO r = null;
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()==5) {
			r = new ProcessorIO();
			r.inputValue = getObjectForDataTypeStringBuilder(data.get(0), valueSeparator);
			r.outputValue = getObjectForDataTypeStringBuilder(data.get(1), valueSeparator);
			r.inputs = parseSdrs(data.get(2));
			r.outputs = parseSdrs(data.get(3));
			r.error = data.get(4).toString();
		}
		return r;
	}
	
	protected void appendSdrs(StringBuilder str, List<Sdr> sdrs) {
		int i = 0;
		for (Sdr sdr: sdrs) {
			if (i>0) {
				str.append(valueSeparator);
			}
			str.append(sdrConvertor.toStringBuilder(sdr));
			i++;
		}
	}
	
	protected List<Sdr> parseSdrs(StringBuilder dat) {
		List<Sdr> r = new ArrayList<Sdr>();
		List<StringBuilder> sdrs = StrUtil.split(dat, valueSeparator);
		for (StringBuilder sdr: sdrs) {
			r.add(sdrConvertor.fromStringBuilder(sdr));
		}
		return r;
	}
}
