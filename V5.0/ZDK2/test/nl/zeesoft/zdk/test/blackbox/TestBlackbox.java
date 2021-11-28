package nl.zeesoft.zdk.test.blackbox;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.blackbox.Blackbox;
import nl.zeesoft.zdk.blackbox.BoxConfig;
import nl.zeesoft.zdk.blackbox.BoxElementValue;
import nl.zeesoft.zdk.blackbox.GeneticCode;

public class TestBlackbox {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		GeneticCode code = new GeneticCode();
		code.generate(1000);
		int value = code.getValue(999, 0, 999999);
		assert (value >= 0 && value <= 999999);
		
		GeneticCode code2 = new GeneticCode();
		assert code.compareTo(code2) == 1.0F;

		code2 = code.copy();
		code2.mutate(10);
		assert code.compareTo(code2) > 0.001F;
		assert code.compareTo(code2) < 0.2F;
		
		code2 = code.copy();
		code2.mutate(500);
		assert code.compareTo(code2) > 0.5F;
		assert code.compareTo(code2) <= 1.0F;
		
		BoxConfig config = new BoxConfig();
		
		Blackbox box = new Blackbox(config, code);
		assert box != null;
		
		//Console.log(JsonConstructor.fromObject(box.outputRules).toStringBuilderReadFormat());

		//Console.log("");
		box.inputs[0].setValue(1F);
		box.inputs[1].setValue(2F);
		box.inputs[2].setValue(0.1F);
		box.processInput();
		assert BoxElementValue.toString(box.memory).length() > 0;
		//Console.log(BoxElementValue.toString(box.memory));
		//Console.log(BoxElementValue.toString(box.outputs));
		box.inputs[0].setValue(2F);
		box.inputs[1].setValue(0F);
		box.inputs[2].setValue(0.2F);
		box.processInput();
		assert BoxElementValue.toString(box.memory).length() > 0;
		//Console.log(BoxElementValue.toString(box.memory));
		//Console.log(BoxElementValue.toString(box.outputs));
		
		box = new Blackbox(config, code);
		
		//Console.log("");
		box.inputs[0].setValue(1F);
		box.inputs[1].setValue(2F);
		box.inputs[2].setValue(0.1F);
		box.processInput();
		assert BoxElementValue.toString(box.memory).length() > 0;
		//Console.log(BoxElementValue.toString(box.memory));
		//Console.log(BoxElementValue.toString(box.outputs));
		box.inputs[0].setValue(2F);
		box.inputs[1].setValue(0F);
		box.inputs[2].setValue(0.2F);
		box.processInput();
		assert BoxElementValue.toString(box.memory).length() > 0;
		//Console.log(BoxElementValue.toString(box.memory));
		//Console.log(BoxElementValue.toString(box.outputs));
	}
}
