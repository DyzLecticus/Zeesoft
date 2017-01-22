package nl.zeesoft.zacs.simulator;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zacs.database.model.Control;
import nl.zeesoft.zacs.database.model.Example;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;

public class SimLearnExamplesWorker extends Worker {
	private static SimLearnExamplesWorker		learnWorker				= null;

	private List<Example>						examples				= null;
	private int									examplePauzeMSecs		= 500;
	private int									countSymbolMaximum		= 1000;
	private int									countContextMaximum		= 1000;
	private boolean								skipCountMaxStructSyms	= true;
	private int									contextSymbolMaximum	= 8;
	private boolean								contextAssociate		= false;
	
	private boolean								gotRandomExample		= false;
	private int									exampleIndex			= 0;
	
	private SimLearnExamplesWorker() {
		// Singleton
	}

	public static SimLearnExamplesWorker getInstance() {
		if (learnWorker==null) {
			learnWorker = new SimLearnExamplesWorker();
		}
		return learnWorker;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	@Override
	public void start() {
		if (SimController.getInstance().getSymbols().getTotalSymbolLinks()==0) {
			setSleep(10);
		}
		Control c = SimController.getInstance().getControl();
		examplePauzeMSecs = c.getExamplePauzeMSecs();
		countSymbolMaximum = c.getCountSymbolMaximum();
		countContextMaximum = c.getCountContextMaximum();
		skipCountMaxStructSyms = c.isSkipCountMaxStructSyms();
		contextSymbolMaximum = c.getContextSymbolMaximum();
		contextAssociate = c.isContextAssociate();
		examples = SimController.getInstance().getExamples().getExamplesAsList();
		gotRandomExample = false;
		exampleIndex = 0;
		if (examples.size()>0) {
			super.start();
		} else {
			Messenger.getInstance().debug(this,"No examples to learn");
		}
	}

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
	}
	
	@Override
	public void whileWorking() {
		Example ex = null;
		if (gotRandomExample) {
			ex = getNextExample();
		} else {
			ex = getRandomExample();
		}
		Date started = null;
		if (SimController.DEBUG_PERFORMANCE) {
			started = new Date();
		}
		learnExample(ex);
		if (SimController.DEBUG_PERFORMANCE) {
			Messenger.getInstance().debug(this,"Learning example took: " + ((new Date()).getTime() - started.getTime()) + " ms");
		}
	}

	private void learnExample(Example ex) {
		StringBuilder text = new StringBuilder(ex.getInput());
		if (ex.getOutput().length()>0) {
			text.append(" ");
			text.append(ex.getOutput());
		}
		SimController.getInstance().getSymbols().learnText(text,ex.getContext(),0,countSymbolMaximum,countContextMaximum,skipCountMaxStructSyms,contextSymbolMaximum,contextAssociate);
	}

	private Example getRandomExample() {
		gotRandomExample = true;
		return examples.get(Generic.generateRandom(0,(examples.size() - 1)));
	}

	private Example getNextExample() {
		gotRandomExample = false;
		Example r = examples.get(exampleIndex);
		exampleIndex++;
		if (exampleIndex>=examples.size()) {
			exampleIndex = 0;
			setSleep(examplePauzeMSecs);
		}
		return r;
	}
}
