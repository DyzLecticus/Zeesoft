package nl.zeesoft.zsc.confab.confabs;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsc.confab.Module;

public class SynonymConfabulation extends ConfabulationObject {
	public String				contextSymbol	= "";
	public int					width			= 4;
	public boolean				parallel		= true;
	
	public List<SynonymResult>	results			= new ArrayList<SynonymResult>();

	@Override
	public void initialize() {
		super.initialize();
		for (int m = 0; m < ((width * 2) + 1); m++) {
			Module mod = new Module();
			modules.add(mod);
		}
	}
}
