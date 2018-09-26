package nl.zeesoft.zsc.confab.confabs;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsc.confab.Module;

public class ContextConfabulation extends ConfabulationObject {
	public List<ContextResult>	results		= new ArrayList<ContextResult>();
	
	@Override
	public void initialize() {
		super.initialize();
		modules.add(new Module());
	}
}
