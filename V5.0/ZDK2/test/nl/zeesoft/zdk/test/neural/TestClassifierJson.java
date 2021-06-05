package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.cl.ClBit;
import nl.zeesoft.zdk.neural.processor.cl.ClBitStringConvertor;
import nl.zeesoft.zdk.neural.processor.cl.ClBits;
import nl.zeesoft.zdk.neural.processor.cl.ClBitsStringConvertor;
import nl.zeesoft.zdk.neural.processor.cl.ClConfig;
import nl.zeesoft.zdk.neural.processor.cl.Classifier;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class TestClassifierJson {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		ClConfig config = new ClConfig();
		config.size = new Size(4,4,4);
		Classifier cl = new Classifier();
		cl.initialize(config);
		
		Sdr sdr = new Sdr(64);
		sdr.setBit(0, true);
		sdr.setBit(1, true);
		sdr.setBit(2, true);
		sdr.setBit(3, true);
		ProcessorIO io = new ProcessorIO(sdr);
		io.inputValue = 1;
		cl.processIO(io);
		assert io.error.length() == 0;
		assert io.outputs.size() == 1;
		assert io.outputValue != null;
		cl.processIO(io);
		assert cl.bits.bits.size() == 4;
		io.inputValue = 2;
		cl.processIO(io);
		assert cl.bits.bits.size() == 4;
		
		ClBitsStringConvertor bsc = (ClBitsStringConvertor) ObjectStringConvertors.getConvertor(ClBits.class);
		StringBuilder str = bsc.toStringBuilder(cl.bits);

		ClBitStringConvertor sc = (ClBitStringConvertor) ObjectStringConvertors.getConvertor(ClBit.class);
		assert sc.toStringBuilder(new ClBit()).length() == 0;
		assert sc.toStringBuilder(cl).length() == 0;

		ClBits bits = (ClBits) bsc.fromStringBuilder(str);
		assert bits.bits.size() == cl.bits.bits.size();
		
		assert bsc.toStringBuilder(cl).length() == 0;
		assert bsc.toStringBuilder(new ClBits(null,null)).length() == 0;
		assert bsc.fromStringBuilder(new StringBuilder()) != null;
		bits = bsc.fromStringBuilder(new StringBuilder("@1###@2###"));
		assert bits.bits.size() == 0;
		
		Json json = JsonConstructor.fromObjectUseConvertors(cl);
		assert json.root.children.size() == 5;
		
		Classifier cl2 = (Classifier) ObjectConstructor.fromJson(json);
		Json json2 = JsonConstructor.fromObjectUseConvertors(cl2);
		
		assert StrUtil.equals(json.toStringBuilder(), json2.toStringBuilder());
		
		sdr = new Sdr(64);
		sdr.setBit(0, true);
		sdr.setBit(1, true);
		sdr.setBit(2, true);
		sdr.setBit(3, true);
		io = new ProcessorIO(sdr);
		cl2.processIO(io);
		assert io.error.length() == 0;
		assert io.outputs.size() == 1;
		assert io.outputValue != null;
	}
}
