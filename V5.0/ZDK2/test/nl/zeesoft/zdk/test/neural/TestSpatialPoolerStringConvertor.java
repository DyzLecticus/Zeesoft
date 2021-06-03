package nl.zeesoft.zdk.test.neural;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.json.Json;
import nl.zeesoft.zdk.json.JsonConstructor;
import nl.zeesoft.zdk.matrix.Matrix;
import nl.zeesoft.zdk.matrix.Size;
import nl.zeesoft.zdk.neural.Sdr;
import nl.zeesoft.zdk.neural.processor.ProcessorIO;
import nl.zeesoft.zdk.neural.processor.sp.SpActivations;
import nl.zeesoft.zdk.neural.processor.sp.SpActivationsStringConvertor;
import nl.zeesoft.zdk.neural.processor.sp.SpBoostFactors;
import nl.zeesoft.zdk.neural.processor.sp.SpBoostFactorsStringConvertor;
import nl.zeesoft.zdk.neural.processor.sp.SpConfig;
import nl.zeesoft.zdk.neural.processor.sp.SpConnections;
import nl.zeesoft.zdk.neural.processor.sp.SpConnectionsStringConvertor;
import nl.zeesoft.zdk.neural.processor.sp.SpatialPooler;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

public class TestSpatialPoolerStringConvertor {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		SpConfig config = new SpConfig();
		config.inputSize = new Size(3,3);
		config.outputSize = new Size(4,4);
		SpatialPooler sp = new SpatialPooler();
		sp.initialize(config);
		sp.reset();
		
		Sdr sdr = new Sdr(9);
		sdr.setBit(0, true);
		sp.processIO(new ProcessorIO(sdr));
		
		SpConnectionsStringConvertor spcsc = (SpConnectionsStringConvertor) ObjectStringConvertors.getConvertor(SpConnections.class);
		StringBuilder str = spcsc.toStringBuilder(sp.connections);
		
		SpConnections spc = (SpConnections) spcsc.fromStringBuilder(str);
		assert spc.data[0][0][0] instanceof Matrix;
		
		Matrix m1 = (Matrix) sp.connections.data[0][0][0];
		Matrix m2 = (Matrix) spc.data[0][0][0];
		
		assert m1.data[0][0][0] instanceof Float;
		assert m2.data[0][0][0].equals(m2.data[0][0][0]);
		assert m2.data[1][1][0].equals(m2.data[1][1][0]);
		
		SpBoostFactorsStringConvertor spbfsc = (SpBoostFactorsStringConvertor) ObjectStringConvertors.getConvertor(SpBoostFactors.class);
		str = spbfsc.toStringBuilder(sp.boostFactors);
		
		SpBoostFactors spbf = (SpBoostFactors) spbfsc.fromStringBuilder(str);
		assert spbf.data[0][0][0] instanceof Float;
		assert spbf.data[0][0][0].equals(sp.boostFactors.data[0][0][0]);
		assert spbf.data[1][1][0].equals(sp.boostFactors.data[1][1][0]);
		
		SpActivationsStringConvertor spasc = (SpActivationsStringConvertor) ObjectStringConvertors.getConvertor(SpActivations.class);
		str = spasc.toStringBuilder(sp.activations);
		
		SpActivations spa = (SpActivations) spasc.fromStringBuilder(str);
		assert spa.data[0][0][0] instanceof Float;
		assert spa.data[0][0][0].equals(sp.activations.data[0][0][0]);
		assert spa.data[1][1][0].equals(sp.activations.data[1][1][0]);
		
		assert spcsc.toStringBuilder(sp).length() == 0;
		assert spbfsc.toStringBuilder(sp).length() == 0;
		assert spasc.toStringBuilder(sp).length() == 0;

		assert spcsc.fromStringBuilder(new StringBuilder()) == null;
		assert spbfsc.fromStringBuilder(new StringBuilder()) == null;
		assert spasc.fromStringBuilder(new StringBuilder()) == null;

		sp.executor = null;
		Json json = JsonConstructor.fromObjectUseConvertors(sp);
		assert json.root.children.size() == 8;
		
		// TODO: Test from JSON
		//Console.log(json.toStringBuilderReadFormat());
	}
}
