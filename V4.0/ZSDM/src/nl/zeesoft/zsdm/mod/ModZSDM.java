package nl.zeesoft.zsdm.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsdm.dialog.DialogSet;
import nl.zeesoft.zsdm.mod.handler.HtmlZSDMIndexHandler;

public class ModZSDM extends ModObject implements StateListener {
	public static final String		NAME			= "ZSDM";
	public static final String		DESC			= 
		"The Zeesoft Smart Dialog Manager provides a simple JSON API to manage smart dialogs.";
	
	private DialogSet				dialogSet		= null;
	
	public ModZSDM(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		dialogSet = getNewDialogSet();
		dialogSet.addListener(this);
	}
	
	@Override
	public void install() {
		DialogSet ds = getNewDialogSet();
		ds.install();
	}
	
	@Override
	public void initialize() {
		handlers.add(new HtmlZSDMIndexHandler(configuration,this));
		handlers.add(new JsonModTestResultsHandler(configuration,this));
		dialogSet.initialize();
		super.initialize();
	}
	
	@Override
	public void destroy() {
		super.destroy();
		dialogSet.destroy();
	}

	@Override
	public void stateChanged(Object source, boolean open) {
		if (source instanceof DialogSet && open) {
			// TODO Create and implement tester
		}
	}
	
	protected DialogSet getNewDialogSet() {
		return new DialogSet(configuration);
	}
}
