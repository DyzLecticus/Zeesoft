package nl.zeesoft.zdk.blackbox;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.blackbox.rules.AverageRule;
import nl.zeesoft.zdk.blackbox.rules.CopyAddRule;
import nl.zeesoft.zdk.blackbox.rules.CopyDivideRule;
import nl.zeesoft.zdk.blackbox.rules.CopyMultiplyRule;
import nl.zeesoft.zdk.blackbox.rules.CopySubtractRule;

public class BoxConfig {
	private static int		MEM_LEN				= 0;
	private static int		RULE_IDX			= 10;
	private static int		NUM_INPUTS			= 20;
	private static int		INPUT_IDX			= 30;
	
	public int				inputLength			= 4;
	public int				outputLength		= 4;
	
	public int				minMemoryLength		= 8;
	public int				maxMemoryLength		= 1024;
	public int				maxInputsPerRule 	= 16;
	
	public List<BoxRule> 	ruleTemplates		= new ArrayList<BoxRule>();
	
	public BoxConfig() {
		ruleTemplates.add(new CopyAddRule());
		ruleTemplates.add(new CopySubtractRule());
		ruleTemplates.add(new CopyMultiplyRule());
		ruleTemplates.add(new CopyDivideRule());
		ruleTemplates.add(new AverageRule());
	}

	protected void initializeBox(Blackbox box, GeneticCode code) {
		int uid = 0;
		int memoryLength = code.getValue(MEM_LEN, minMemoryLength, maxMemoryLength);
		box.inputs = BoxElementValue.initializeArray(inputLength);
		box.memory = BoxElementValue.initializeArray(memoryLength);
		box.outputs = BoxElementValue.initializeArray(outputLength);
		
		generateRules(code, uid, box.inputRules, box.inputs, box.memory);
		generateRules(code, uid, box.memoryRules, box.memory, box.memory);
		generateRules(code, uid, box.outputRules, box.memory, box.outputs);
	}

	protected void generateRules(GeneticCode code, int uid, BoxRules rules, BoxElementValue[] inputs, BoxElementValue[] outputs) {
		for (int i = 0; i < outputs.length; i++) {
			uid++;
			int ruleIndex = code.getValue(RULE_IDX + uid + i, 0, ruleTemplates.size() - 1);
			BoxRule rule = (BoxRule) Instantiator.getNewClassInstance(ruleTemplates.get(ruleIndex).getClass());
			int max = code.getValue(NUM_INPUTS + uid + (i * 2), 1, maxInputsPerRule);
			rule.inputIndexes = generateIndexes(code, uid, inputs.length, max);
			rule.outputIndex = i;
			rules.rules.add(rule);
		}
	}
	
	protected List<Integer> generateIndexes(GeneticCode code, int uid, int length, int max) {
		List<Integer> r = new ArrayList<Integer>();
		for (int i = 1; i < max; i++) {
			uid++;
			int idx = code.getValue(INPUT_IDX + uid + i, 1, (length - 1));
			if (!r.contains(idx)) {
				r.add(idx);
			}
		}
		return r;
	}
}
