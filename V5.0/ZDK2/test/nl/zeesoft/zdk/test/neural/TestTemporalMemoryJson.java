package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.json.ObjectConstructor;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.model.Cell;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.tm.TemporalMemory;
import nl.zeesoft.zdk.neural.processor.tm.TmCells;
import nl.zeesoft.zdk.neural.processor.tm.TmCellsStringConvertor;
import nl.zeesoft.zdk.neural.processor.tm.TmColumns;
import nl.zeesoft.zdk.neural.processor.tm.TmColumnsStringConvertor;
import nl.zeesoft.zdk.neural.processor.tm.TmConfig;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class TestTemporalMemoryJson {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		TmConfig config = new TmConfig();
		config.size = new Size(4,4,4);
		TemporalMemory tm = new TemporalMemory();
		tm.initialize(config);
		
		Sdr sdr = new Sdr(16);
		sdr.setBit(0, true);
		sdr.setBit(1, true);
		ProcessorIO io = new ProcessorIO(sdr);
		tm.processIO(io);
		assert io.error.length() == 0;
		assert io.outputs.size() == 4;
		assert io.outputs.get(0).onBits.size() == 8;
		assert io.outputs.get(0).length == 64;

		TmColumnsStringConvertor tmcsc = (TmColumnsStringConvertor) ObjectStringConvertors.getConvertor(TmColumns.class);
		StringBuilder str = tmcsc.toStringBuilder(tm.columns);
		
		TmColumns tmc = (TmColumns) tmcsc.fromStringBuilder(str);
		assert tmc.data[0][0][0] instanceof Boolean;
		assert tmc.data[0][0][0] == tm.columns.data[0][0][0];
		assert tmc.data[1][0][0] == tm.columns.data[1][0][0];

		TmCellsStringConvertor tmcesc = (TmCellsStringConvertor) ObjectStringConvertors.getConvertor(TmCells.class);
		str = tmcesc.toStringBuilder(tm.cells);
		
		TmCells tmce = (TmCells) tmcesc.fromStringBuilder(str);
		assert tmce.data[0][0][0] instanceof Cell;
		
		assert tmcsc.toStringBuilder(tm).length() == 0;
		assert tmcesc.toStringBuilder(tm).length() == 0;

		assert tmcsc.fromStringBuilder(new StringBuilder()) == null;
		assert tmcesc.fromStringBuilder(new StringBuilder()) == null;

		Json json = JsonConstructor.fromObjectUseConvertors(tm);
		assert json.root.children.size() == 5;
		
		TemporalMemory tm2 = (TemporalMemory) ObjectConstructor.fromJson(json);
		Json json2 = JsonConstructor.fromObjectUseConvertors(tm2);
		
		assert StrUtil.equals(json.toStringBuilder(), json2.toStringBuilder());
		
		sdr = new Sdr(9);
		sdr.setBit(0, true);
		sdr.setBit(1, true);
		io = new ProcessorIO(sdr);
		tm2.processIO(io);
		assert io.error.length() == 0;
		assert io.outputs.size() == 4;
		assert io.outputs.get(0).onBits.size() == 8;
		assert io.outputs.get(0).length == 64;
	}
}
