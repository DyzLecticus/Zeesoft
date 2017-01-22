package nl.zeesoft.zals.simulator.animals;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.model.SymbolSequenceTraining;
import nl.zeesoft.zac.module.confabulate.ConInputOutput;
import nl.zeesoft.zals.database.model.Animal;
import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zodb.Generic;

public class PlanningAnimal extends BasicAnimal {
	
	protected static final String				MODULE_PLANNING			= "Planning";
	
	protected static final String				PROCESS_ACTION			= "processAction";

	@Override
	protected StringBuilder getPlannedActions() {
		StringBuilder plannedActions = super.getPlannedActions();
		if (!bufferIsEmpty(PROCESS_ACTION)) {
			for (int i=0; i<16; i++) {
				String val = getBufferValue(PROCESS_ACTION,i);
				if (val.length()==0) {
					break;
				}
				if (plannedActions.length()>0) {
					plannedActions.append(" ");
				}
				plannedActions.append(val);
			}
		}
		return plannedActions;
	}

	@Override
	protected String getActionForInputs(String[] inputs, List<String> actionsDone) {
		String action = "";
		
		// Use planning module?
		action = getBufferValue(PROCESS_ACTION,0);
		if (action.length()>0) {
			removeBufferValue(PROCESS_ACTION);
		}
		if (action.length()==0) {
			action = getPlannedActionForInputs(inputs, actionsDone);
		}

		// Use basic module?
		if (action.length()==0) {
			action = getBasicActionForInputs(inputs, actionsDone);
		}
		
		return action;
	}
	
	@Override
	protected void handleActionDone(String action, String feedback, List<String> actionsDone, String[] inputs) {
		super.handleActionDone(action, feedback, actionsDone, inputs);
		
		boolean enforce = false;
		int weight = 1;
		String[] startInputs = inputs;
		
		if (
			(action.equals(Animal.ACTION_TURN_LEFT) || action.equals(Animal.ACTION_TURN_RIGHT)) &&
			actionsDone.size()>0
			) {
			List<String> newActionsDone = new ArrayList<String>();
			int vi = 0;
			for (String actionDone: actionsDone) {
				String[] testInputs = getVisualInputs(vi);
				if (testInputs[0].equals(getFoodColor()) || testInputs[2].equals(getFoodColor()) || testInputs[4].equals(getFoodColor())) {
					break;
				} else {
					newActionsDone.add(actionDone);
				}
				if (testInputs[0].equals(getDangerColor()) || testInputs[2].equals(getDangerColor()) || testInputs[4].equals(getDangerColor())) {
					break;
				}
				vi++;
			}
			actionsDone = newActionsDone;

			if (actionsDone.size()>0) {
				startInputs = getVisualInputs((actionsDone.size() - 1));
				String startAction = actionsDone.get((actionsDone.size() - 1));
				enforce =
					// Not food
					(!startInputs[0].equals(getFoodColor()) && !startInputs[2].equals(getFoodColor()) && !startInputs[4].equals(getFoodColor())) &&
					(
						// Move away from danger
						(startInputs[2].equals(getDangerColor()) && ((action.equals(Animal.ACTION_TURN_RIGHT) && startAction.equals(Animal.ACTION_TURN_LEFT)) || (action.equals(Animal.ACTION_TURN_LEFT) && startAction.equals(Animal.ACTION_TURN_RIGHT)))) ||
						(startAction.equals(Animal.ACTION_MOVE_FORWARD) && ((startInputs[0].equals(getDangerColor()) && action.equals(Animal.ACTION_TURN_RIGHT)) || (startInputs[4].equals(getDangerColor()) && action.equals(Animal.ACTION_TURN_LEFT))))
					)
					;
				if (enforce) {
					for (int i = 0; i < (actionsDone.size() - 2); i++) {
						if (!actionsDone.get(i).equals(Animal.ACTION_MOVE_FORWARD)) {
							enforce = false;
							break;
						}
					}
				}
			}
		}
		if (enforce) {
			StringBuilder sequence = getActionSequenceForActionsAndInputs(action,actionsDone,startInputs);
			String context[] = getContextForInputs(startInputs);
			SymbolSequenceTraining trainSequence = new SymbolSequenceTraining();
			trainSequence.setSequence(sequence);
			trainSequence.setContextSymbol1(context[0]);
			trainSequence.setContextSymbol2(context[1]);
			trainSequence.setContextSymbol3(context[2]);
			trainSequence.setContextSymbol4(context[3]);
			enforce = trainModuleSequence(MODULE_PLANNING,trainSequence,weight);
		}
	}
	
	@Override
	protected void handleActionNotDone(String action, String feedback, String[] inputs) {
		super.handleActionNotDone(action, feedback, inputs);
		flushBuffer(PROCESS_ACTION);
	}

	protected final String getPlannedActionForInputs(String[] inputs, List<String> actionsDone) {
		String action = "";
		StringBuilder sequence = getActionSequenceForActionsAndInputs("",null,inputs);
		String context[] = getContextForInputs(inputs);
		ConInputOutput io = new ConInputOutput(sequence,context[0],context[1],context[2],context[3]);
		if (confabulateModuleExtend(MODULE_PLANNING,io)) {
			if (io.getOutputSequence().length()>0) {
				List<StringBuilder> plannedActionStrings = Generic.stringBuilderSplit(io.getOutputSequence()," ");
				for (int i = (plannedActionStrings.size() - 1); i>=0; i--) {
					String plannedAction = plannedActionStrings.get(i).toString();
					String[] numberAction = plannedAction.split("-");
					if (i==0) {
						action = numberAction[1];
					} else {
						addBufferValue(PROCESS_ACTION,numberAction[1]);
						if (bufferIsFull(PROCESS_ACTION)) {
							break;
						}
					}
				}
			}
		}
		return action;
	}	
	
	@Override
	protected List<String> getModuleNames() {
		List<String> names = super.getModuleNames();
		names.add(MODULE_PLANNING);
		return names;
	}

	@Override
	protected List<String> getBufferNames() {
		List<String> names = super.getBufferNames();
		names.add(PROCESS_ACTION);
		return names;
	}

	@Override
	protected long getMinimumTrainingStatesForModuleConfabulation(String name) {
		long min = super.getMinimumTrainingStatesForModuleConfabulation(name);
		if (name.equals(MODULE_PLANNING)) {
			min = (Environment.MINIMUM_TRAINING_STATES * 40);
		}
		return min;
	}

	@Override
	protected long getMinimumTrainingStatesForModuleTraining(String name) {
		long min = super.getMinimumTrainingStatesForModuleTraining(name);
		if (name.equals(MODULE_PLANNING)) {
			min = (Environment.MINIMUM_TRAINING_STATES * 2);
		}
		return min;
	}

	@Override
	protected long getMaximumTrainingStatesForModuleTraining(String name) {
		long max = super.getMaximumTrainingStatesForModuleTraining(name);
		if (name.equals(MODULE_PLANNING)) {
			max = (Environment.MINIMUM_TRAINING_STATES * 80);
		}
		return max;
	}

	private StringBuilder getActionSequenceForActionsAndInputs(String action,List<String> actionsDone,String[] startInputs) {
		StringBuilder sequence = new StringBuilder(getSequenceStarterForInputs(startInputs));
		if (actionsDone!=null) {
			for (int i = (actionsDone.size() - 1); i>=0; i--) {
				String actionDone = actionsDone.get(i);
				if (sequence.length()>0) {
					sequence.append(" ");
				}
				sequence.append(action);
				sequence.append((i + 1));
				sequence.append("-");
				sequence.append(actionDone);
			}
		}
		if (action.length()>0) {
			if (sequence.length()>0) {
				sequence.append(" ");
			}
			sequence.append(action);
			sequence.append(0);
			sequence.append("-");
			sequence.append(action);
		}
		return sequence;
	}

	private StringBuilder getSequenceStarterForInputs(String[] inputs) {
		StringBuilder elem = new StringBuilder();
		String focusColor = "";
		if (inputs[0].equals(getFoodColor()) || inputs[2].equals(getFoodColor()) || inputs[4].equals(getFoodColor())) {
			focusColor = getFoodColor();
		} else if (inputs[0].equals(getDangerColor()) || inputs[2].equals(getDangerColor()) || inputs[4].equals(getDangerColor())) {
			focusColor = getDangerColor();
		}
		if (focusColor.length()>0) {
			boolean focused = false;
			if (inputs[2].equals(focusColor)) {
				elem.append("F-");
				elem.append(inputs[2]);
				elem.append("-");
				elem.append(inputs[3]);
				focused = true;
			}
			if (!focused && inputs[0].equals(focusColor)) {
				elem.append("L-");
				elem.append(inputs[0]);
				elem.append("-");
				elem.append(inputs[1]);
				focused = true;
			}
			if (!focused && inputs[4].equals(focusColor)) {
				elem.append("R-");
				elem.append(inputs[4]);
				elem.append("-");
				elem.append(inputs[5]);
				focused = true;
			}
		} else {
			elem.append(inputs[2]);
			if (!inputs[2].equals(Environment.COLOR_NOTHING)) {
				elem.append("-");
				elem.append(inputs[3]);
			}
		}
		return elem;
	}
}
