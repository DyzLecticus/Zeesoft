package nl.zeesoft.zdk.blackbox;

import java.util.ArrayList;
import java.util.List;

public class BoxRules {
	public List<BoxRule> 	rules		= new ArrayList<BoxRule>();
	
	public void apply(BoxElementValue[] inputs, BoxElementValue[] outputs) {
		for (BoxRule rule: rules) {
			rule.apply(inputs, outputs);
		}
	}
}
