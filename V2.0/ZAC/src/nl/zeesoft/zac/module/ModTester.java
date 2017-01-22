package nl.zeesoft.zac.module;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.Module;
import nl.zeesoft.zac.database.model.SymbolSequenceTest;
import nl.zeesoft.zac.module.confabulate.ConInputOutput;
import nl.zeesoft.zac.module.confabulate.ConModuleInstance;
import nl.zeesoft.zac.module.object.ObjSymbolSequenceTest;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqUpdate;

/**
 * This class is used to test modules.
 */
public class ModTester {
	private Module							module					= null;
	private ObjSymbolSequenceTest			sequences				= null;
	
	private List<SymbolSequenceTest>		testSequences 			= new ArrayList<SymbolSequenceTest>();
	private List<ConModuleInstance>			testInstances 			= new ArrayList<ConModuleInstance>();
	private int								testSequenceIndex 		= 0;
	
	public ModTester(Module module) {
		this.module = module;
		sequences = new ObjSymbolSequenceTest(module.getId());
	}
	
	public void refreshTestSequences() {
		sequences.reinitialize();
		testSequences.clear();
		testInstances.clear();
		for (SymbolSequenceTest testSequence: sequences.getTestSequencesAsList()) {
			testSequences.add(testSequence);
			ConModuleInstance moduleInstance = new ConModuleInstance(module);
			moduleInstance.refreshLinks();
			testInstances.add(moduleInstance);
		}
	}

	public void test() {
		if (testSequences.size()>0) {
			if (testSequenceIndex>=testSequences.size()) {
				testSequenceIndex = 0;
			}
			testSequence(testSequences.get(testSequenceIndex),testInstances.get(testSequenceIndex));
			testSequenceIndex++;
		}
	}
	
	private void testSequence(SymbolSequenceTest sequence,ConModuleInstance instance) {
		ConInputOutput io = new ConInputOutput(
			sequence.getSequence(),
			sequence.getContextSymbol1(),
			sequence.getContextSymbol2(),
			sequence.getContextSymbol3(),
			sequence.getContextSymbol4(),
			sequence.getContextSymbol5(),
			sequence.getContextSymbol6(),
			sequence.getContextSymbol7(),
			sequence.getContextSymbol8()
			);
		io.setThinkWidth(sequence.getThinkWidth());
		io.setMaxOutputSymbols(sequence.getMaxOutputSymbols());
		if (sequence.getType().equals(SymbolSequenceTest.CONFABULATE_CONTEXT)) {
			instance.confabulateContext(io);
			StringBuilder output = new StringBuilder();
			for (String symbol: io.getOutputContextAsSymbolList()) {
				if (output.length()>0) {
					output.append(" ");
				}
				output.append(symbol);
			}
			sequence.setOutput(output);
		} else if (sequence.getType().equals(SymbolSequenceTest.CONFABULATE_CORRECT)) {
			instance.confabulateCorrect(io);
			sequence.setOutput(io.getOutputSequence());
		} else if (sequence.getType().equals(SymbolSequenceTest.CONFABULATE_EXTEND)) {
			instance.confabulateExtend(io);
			sequence.setOutput(io.getOutputSequence());
		}
		sequence.setFiredLinks(io.getFiredLinks());
		sequence.setLog(io.getLog());
		ReqUpdate request = sequence.getNewUpdateRequest(null);
		for (String propertyName: request.getUpdateObject().getProperties()) {
			if (
				!propertyName.equals("firedLinks") &&
				!propertyName.equals("log") &&
				!propertyName.equals("output")
				) {
				request.getUpdateObject().removePropertyValue(propertyName);
			}
		}
		DbRequestQueue.getInstance().addRequest(request,this);
	}
}
