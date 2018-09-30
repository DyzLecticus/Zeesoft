package nl.zeesoft.zsdm.mod;

import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.StateListener;
import nl.zeesoft.zodb.mod.ModObject;
import nl.zeesoft.zodb.mod.handler.JsonModTestResultsHandler;
import nl.zeesoft.zsdm.confab.ConfabConfigurator;
import nl.zeesoft.zsdm.confab.ConfabTrainingSetGenerator;
import nl.zeesoft.zsdm.dialog.DialogSet;
import nl.zeesoft.zsdm.mod.handler.HtmlZSDMIndexHandler;

public class ModZSDM extends ModObject implements StateListener {
	public static final String			NAME			= "ZSDM";
	public static final String			DESC			= 
		"The Zeesoft Smart Dialog Manager provides a simple JSON API to manage smart dialogs.";
	
	private DialogSet					dialogSet		= null;
	private ConfabConfigurator			configurator	= null;
	private ConfabTrainingSetGenerator	generator		= null;
	
	public ModZSDM(Config config) {
		super(config);
		name = NAME;
		desc.append(DESC);
		dialogSet = getNewDialogSet();
		dialogSet.addListener(this);
		configurator = new ConfabConfigurator(configuration);
		configurator.addListener(this);
		generator = new ConfabTrainingSetGenerator(configuration,configurator);
		generator.addListener(this);
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
		if (open) {
			if (source instanceof DialogSet) {
				configurator.configureConfabulators();
			} else if (source instanceof ConfabConfigurator) {
				if (configurator.getReinitialize()) {
					generator.generateTrainingSets();
				}
			} else if (source instanceof ConfabTrainingSetGenerator) {
				if (configurator.getReinitialize()) {
					configurator.reinitializeConfabulators();
				}
			} else {
				// TODO Create and implement tester
				
			}
		}
	}
	
	protected DialogSet getNewDialogSet() {
		return new DialogSet(configuration);
	}
}
