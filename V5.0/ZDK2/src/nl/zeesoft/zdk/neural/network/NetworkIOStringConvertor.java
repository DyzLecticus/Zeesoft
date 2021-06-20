package nl.zeesoft.zdk.neural.network;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.ProcessorIOStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class NetworkIOStringConvertor extends ObjectStringConvertor {
	public String						dataSeparator	= "$";
	public String						keyValSeparator	= "%";
	public String						keySeparator	= "@";
	public String						valueSeparator	= "#";
	public ProcessorIOStringConvertor	pioConvertor	= (ProcessorIOStringConvertor) ObjectStringConvertors.getConvertor(ProcessorIO.class);
	
	@Override
	public Class<?> getObjectClass() {
		return NetworkIO.class;
	}
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof NetworkIO) {
			NetworkIO io = (NetworkIO) obj;
			r.append(io.timeoutMs);
			r.append(dataSeparator);
			r.append(getInputsStringBuilder(io.inputs));
			r.append(dataSeparator);
			r.append(getProcessorIOStringBuilder(io.processorIO));
			r.append(dataSeparator);
			r.append(getErrorsStringBuilder(io.errors));
		}
		return r;
	}
	
	@Override
	public NetworkIO fromStringBuilder(StringBuilder str) {
		NetworkIO r = null;
		List<StringBuilder> data = StrUtil.split(str, dataSeparator);
		if (data.size()==4) {
			r = new NetworkIO();
			r.timeoutMs = Util.parseInt(data.get(0).toString());
			r.inputs = parseInputsFromStringBuilder(data.get(1));
			r.processorIO = parseProcessorIOFromStringBuilder(data.get(2));
			r.errors = parseErrorsFromStringBuilder(data.get(3));
		}
		return r;
	}
	
	protected StringBuilder getInputsStringBuilder(SortedMap<String,Object> inputs) {
		StringBuilder r = new StringBuilder();
		for (Entry<String,Object> entry: inputs.entrySet()) {
			if (r.length()>0) {
				r.append(keyValSeparator);
			}
			r.append(entry.getKey());
			r.append(keySeparator);
			r.append(getDataTypeStringBuilderForObject(entry.getValue(), valueSeparator));
		}
		return r;
	}
	
	protected StringBuilder getProcessorIOStringBuilder(ConcurrentMap<String,ProcessorIO> processorIO) {
		StringBuilder r = new StringBuilder();
		for (Entry<String,ProcessorIO> entry: processorIO.entrySet()) {
			if (r.length()>0) {
				r.append(keyValSeparator);
			}
			r.append(entry.getKey());
			r.append(keySeparator);
			r.append(pioConvertor.toStringBuilder(entry.getValue()));
		}
		return r;
	}
	
	protected StringBuilder getErrorsStringBuilder(CopyOnWriteArrayList<String> errors) {
		StringBuilder r = new StringBuilder();
		for (String error: errors) {
			if (r.length()>0) {
				r.append(valueSeparator);
			}
			r.append(error);
		}
		return r;
	}

	protected SortedMap<String,Object> parseInputsFromStringBuilder(StringBuilder dat) {
		SortedMap<String,Object> r = new TreeMap<String,Object>();
		List<StringBuilder> keyVals = StrUtil.split(dat, keyValSeparator);
		for (StringBuilder keyVal: keyVals) {
			List<StringBuilder> kv = StrUtil.split(keyVal, keySeparator);
			Object value = getObjectForDataTypeStringBuilder(kv.get(1), valueSeparator);
			if (value!=null) {
				r.put(kv.get(0).toString(), value);
			}
		}
		return r;
	}

	protected ConcurrentMap<String,ProcessorIO> parseProcessorIOFromStringBuilder(StringBuilder dat) {
		ConcurrentMap<String,ProcessorIO> r = new ConcurrentHashMap<String,ProcessorIO>();
		List<StringBuilder> keyVals = StrUtil.split(dat, keyValSeparator);
		for (StringBuilder keyVal: keyVals) {
			List<StringBuilder> kv = StrUtil.split(keyVal, keySeparator);
			ProcessorIO pio = pioConvertor.fromStringBuilder(kv.get(1));
			if (pio!=null) {
				r.put(kv.get(0).toString(), pio);
			}
		}
		return r;
	}

	protected CopyOnWriteArrayList<String> parseErrorsFromStringBuilder(StringBuilder dat) {
		CopyOnWriteArrayList<String> r = new CopyOnWriteArrayList<String>();
		List<StringBuilder> errors = StrUtil.split(dat, valueSeparator);
		for (StringBuilder error: errors) {
			if (error.length()>0) {
				r.add(error.toString());
			}
		}
		return r;
	}
}
