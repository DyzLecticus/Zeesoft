package nl.zeesoft.zals.simulator.animals;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.SymbolSequenceTraining;
import nl.zeesoft.zac.module.confabulate.ConInputOutput;
import nl.zeesoft.zals.database.model.Animal;
import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zals.simulator.SimAnimal;

public class BasicAnimal extends SimAnimal {
	protected static final String				MODULE_BASIC				= "Basic";

	@Override
	protected String getActionForInputs(String[] inputs,List<String> actionsDone) {
		return getBasicActionForInputs(inputs,actionsDone);
	}
	
	@Override
	protected void handleActionDone(String action,String feedback,List<String> actionsDone,String[] inputs) {
		List<String[]> prevInputs = new ArrayList<String[]>(); 
		for (int i = (actionsDone.size() - 1); i>=0; i--) {
			String[] prevInput = getVisualInputs(i);
			prevInputs.add(prevInput);
		}
		int weight = 1;
		learnActionForInputs(action,prevInputs,inputs,weight);
		if (action.equals(Animal.ACTION_TAKE_BITE)) {
			int inputSeqNum = 0;
			for (String actionDone: actionsDone) {
				String[] inputsForAction = getVisualInputs(inputSeqNum);
				prevInputs.clear();
				for (int i = (actionsDone.size() - 1); i>=(inputSeqNum+1); i--) {
					String[] prevInput = getVisualInputs(i);
					prevInputs.add(prevInput);
				}
				learnActionForInputs(actionDone,prevInputs,inputsForAction,weight);
				inputSeqNum++;
			}
		}
	}

	@Override
	protected List<String> getModuleNames() {
		List<String> r = new ArrayList<String>();
		r.add(MODULE_BASIC);
		return r;
	}

	@Override
	protected List<String> getBufferNames() {
		List<String> r = new ArrayList<String>();
		for (String name: BUFFER_NAMES) {
			r.add(name);
		}
		return r;
	}

	@Override
	protected long getMinimumTrainingStatesForModuleConfabulation(String name) {
		return Environment.MINIMUM_TRAINING_STATES;
	}
	
	@Override
	protected long getMinimumTrainingStatesForModuleTraining(String name) {
		return 0;
	}

	@Override
	protected long getMaximumTrainingStatesForModuleTraining(String name) {
		return (Environment.MINIMUM_TRAINING_STATES * 10);
	}

	protected boolean enforceActionForInputs(String action,String[] inputs) {
		return
			// Food
			(inputs[0].equals(getFoodColor()) || inputs[2].equals(getFoodColor()) || inputs[4].equals(getFoodColor())) && (
				(!inputs[2].equals(getFoodColor()) && !inputs[4].equals(getFoodColor()) && inputs[0].equals(getFoodColor()) && action.equals(Animal.ACTION_TURN_LEFT)) ||
				(inputs[2].equals(getFoodColor()) && !inputs[3].equals(Environment.INTENSITY_BRIGHT) && action.equals(Animal.ACTION_MOVE_FORWARD)) ||
				(inputs[2].equals(getFoodColor()) && inputs[3].equals(Environment.INTENSITY_BRIGHT) && action.equals(Animal.ACTION_TAKE_BITE)) ||
				(!inputs[2].equals(getFoodColor()) && !inputs[0].equals(getFoodColor()) && inputs[4].equals(getFoodColor()) && action.equals(Animal.ACTION_TURN_RIGHT))
			) ||
			(!inputs[0].equals(getFoodColor()) && !inputs[2].equals(getFoodColor()) && !inputs[4].equals(getFoodColor())) && (
				// Danger
				(inputs[0].equals(getDangerColor()) || inputs[2].equals(getDangerColor()) || inputs[4].equals(getDangerColor())) && (
					(inputs[2].equals(getDangerColor()) && action.equals(Animal.ACTION_TURN_LEFT)) ||
					(inputs[0].equals(getDangerColor()) && action.equals(Animal.ACTION_MOVE_FORWARD)) ||
					(inputs[0].equals(getDangerColor()) && action.equals(Animal.ACTION_TURN_RIGHT)) ||
					(inputs[4].equals(getDangerColor()) && action.equals(Animal.ACTION_MOVE_FORWARD)) ||
					(inputs[4].equals(getDangerColor()) && action.equals(Animal.ACTION_TURN_LEFT)) ||
					(inputs[2].equals(getDangerColor()) && action.equals(Animal.ACTION_TURN_RIGHT))
				) || 
				(!inputs[0].equals(getDangerColor()) && !inputs[2].equals(getDangerColor()) && !inputs[4].equals(getDangerColor())) && (
					// Move forward when no relevant input
					(action.equals(Animal.ACTION_MOVE_FORWARD) && !inputs[3].equals(Environment.INTENSITY_BRIGHT)) || 
					// Turn left or right in front of non-food
					(inputs[3].equals(Environment.INTENSITY_BRIGHT) && !inputs[1].equals(Environment.INTENSITY_BRIGHT) && action.equals(Animal.ACTION_TURN_LEFT)) ||
					(inputs[3].equals(Environment.INTENSITY_BRIGHT) && !inputs[5].equals(Environment.INTENSITY_BRIGHT) && action.equals(Animal.ACTION_TURN_RIGHT)) ||
					// Stay away from corners
					(inputs[2].equals(Environment.COLOR_WALL) && inputs[4].equals(Environment.COLOR_WALL) && action.equals(Animal.ACTION_TURN_LEFT)) ||
					(inputs[2].equals(Environment.COLOR_WALL) && inputs[0].equals(Environment.COLOR_WALL) && action.equals(Animal.ACTION_TURN_RIGHT))
				)
			)
			;
	}

	protected final String getBasicActionForInputs(String[] inputs,List<String> actionsDone) {
		String action = "";
		List<String[]> prevInputs = new ArrayList<String[]>(); 
		for (int i = (actionsDone.size() - 1); i>=0; i--) {
			String[] prevInput = getVisualInputs(i);
			prevInputs.add(prevInput);
		}
		StringBuilder sequence = getSequenceForActionsAndInputs(action,prevInputs,inputs); 
		String context[] = getContextForInputs(inputs);
		ConInputOutput io = new ConInputOutput(sequence,context[0],context[1],context[2],context[3]);
		io.setMaxOutputSymbols(1);
		if (confabulateModuleExtend(MODULE_BASIC,io)) {
			if (io.getOutputSequence().length()>0) {
				action = io.getOutputSequence().toString();
			}
		}
		return action;
	}
	
	protected final StringBuilder getSequenceForActionsAndInputs(String action,List<String[]> prevInputs,String[] inputs) {
		StringBuilder sequence = new StringBuilder();
		sequence.append(inputs[2]);
		if (!inputs[2].equals(Environment.COLOR_NOTHING)) {
			sequence.append("-");
			sequence.append(inputs[3]);
		}

		if (action.length()>0) {
			sequence.append(" ");
			sequence.append(action);
		}
		return sequence;
	}

	protected final String[] getContextForInputs(String[] inputs) {
		String context[] = new String[4];
		context[0] = "";
		context[1] = "";
		context[2] = "";
		context[3] = "";
		String focusColor = "";
		if (inputs[0].equals(getFoodColor()) || inputs[2].equals(getFoodColor()) || inputs[4].equals(getFoodColor())) {
			focusColor = getFoodColor();
		} else if (inputs[0].equals(getDangerColor()) || inputs[2].equals(getDangerColor()) || inputs[4].equals(getDangerColor())) {
			focusColor = getDangerColor();
		}
		if (focusColor.length()>0) {
			boolean focused = false;
			if (inputs[2].equals(focusColor)) {
				for (int i = 0; i < context.length; i++) {
					if (context[i].length()==0) {
						context[i] = "F-" + inputs[2] + "-" + inputs[3];
						focused=true;
						break;
					}
				}
			}
			if (!focused && inputs[0].equals(focusColor)) {
				for (int i = 0; i < context.length; i++) {
					if (context[i].length()==0) {
						context[i] = "L-" + inputs[0] + "-" + inputs[1];
						focused=true;
						break;
					}
				}
			}
			if (!focused && inputs[4].equals(focusColor)) {
				for (int i = 0; i < context.length; i++) {
					if (context[i].length()==0) {
						context[i] = "R-" + inputs[4] + "-" + inputs[5];
						focused=true;
						break;
					}
				}
			}
		} else {
			if (!inputs[0].equals(Environment.COLOR_NOTHING)) {
				for (int i = 0; i < context.length; i++) {
					if (context[i].length()==0) {
						context[i] = "L-" + inputs[0] + "-" + inputs[1];
						break;
					}
				}
			}
			if (!inputs[2].equals(Environment.COLOR_NOTHING)) {
				for (int i = 0; i < context.length; i++) {
					if (context[i].length()==0) {
						context[i] = "F-" + inputs[2] + "-" + inputs[3];
						break;
					}
				}
			}
			if (!inputs[4].equals(Environment.COLOR_NOTHING)) {
				for (int i = 0; i < context.length; i++) {
					if (context[i].length()==0) {
						context[i] = "R-" + inputs[4] + "-" + inputs[5];
						break;
					}
				}
			}
		}
		return context;
	}

	private boolean learnActionForInputs(String action,List<String[]> prevInputs,String[] inputs, int weight) {
		boolean done = false;
		if (enforceActionForInputs(action,inputs)) {
			StringBuilder sequence = getSequenceForActionsAndInputs(action,prevInputs,inputs); 
			SymbolSequenceTraining trainSequence = new SymbolSequenceTraining();
			trainSequence.setSequence(sequence);
			String context[] = getContextForInputs(inputs);
			trainSequence.setContextSymbol1(context[0]);
			trainSequence.setContextSymbol2(context[1]);
			trainSequence.setContextSymbol3(context[2]);
			trainSequence.setContextSymbol4(context[3]);
			done = trainModuleSequence(MODULE_BASIC,trainSequence,weight);
		}
		return done;
	}
}
