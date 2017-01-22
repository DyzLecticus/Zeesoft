package nl.zeesoft.zals.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zac.database.model.SymbolSequenceTraining;
import nl.zeesoft.zac.module.confabulate.ConInputOutput;
import nl.zeesoft.zals.database.model.Animal;
import nl.zeesoft.zals.database.model.Carnivore;
import nl.zeesoft.zals.database.model.Environment;
import nl.zeesoft.zals.database.model.Herbivore;
import nl.zeesoft.zals.simulator.object.ObjAnimalModule;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Locker;

public abstract class SimAnimal extends Locker {
	// Buffers
	protected static final String				INPUT_COLOR_LEFT			= "inputColorLeft";
	protected static final String				INPUT_INTENSITY_LEFT		= "inputIntensityLeft";
	protected static final String				INPUT_COLOR_FRONT			= "inputColorFront";
	protected static final String				INPUT_INTENSITY_FRONT		= "inputIntensityFront";
	protected static final String				INPUT_COLOR_RIGHT			= "inputColorRight";
	protected static final String				INPUT_INTENSITY_RIGHT		= "inputIntensityRight";
	protected static final String				OUTPUT_ACTION				= "outputAction";
	protected static final String				OUTPUT_ACTION_FEEDBACK		= "outputActionFeedback";

	protected static final String[]				BUFFER_NAMES				= {
			INPUT_COLOR_LEFT,INPUT_INTENSITY_LEFT,INPUT_COLOR_FRONT,INPUT_INTENSITY_FRONT,INPUT_COLOR_RIGHT,INPUT_INTENSITY_RIGHT,
			OUTPUT_ACTION,OUTPUT_ACTION_FEEDBACK
			};
	
	private Animal								animal						= null;
	private long								trainedStates				= 0;
	private SimEnvironment						environment					= null;

	private ObjAnimalModule						modules						= null;
	private SortedMap<String,SimAnimalModule>	animalModules				= new TreeMap<String,SimAnimalModule>();
	
	private int									bufferSize					= 0;
	
	private long								firedLinks					= 0;
	
	private SortedMap<String,List<String>> 		buffers						= new TreeMap<String,List<String>>();

	protected SimAnimal() {
		// Dynamically instantiated 
	}

	/******************************** Extendable methods ********************************/
	
	protected String getActionForInputs(String[] inputs,List<String> actionsDone) {
		return getRandomAction();
	}
	
	protected void handleActionDone(String action,String feedback,List<String> actionsDone,String[] inputs) {

	}

	protected void handleActionNotDone(String action,String feedback,String[] inputs) {

	}

	protected List<String> getModuleNames() {
		List<String> r = new ArrayList<String>();
		return r;
	}

	protected List<String> getBufferNames() {
		List<String> r = new ArrayList<String>();
		for (String name: BUFFER_NAMES) {
			r.add(name);
		}
		return r;
	}
	
	protected long getMinimumTrainingStatesForModuleConfabulation(String name) {
		return Environment.MINIMUM_TRAINING_STATES;
	}
	
	protected long getMinimumTrainingStatesForModuleTraining(String name) {
		return 0;
	}

	protected long getMaximumTrainingStatesForModuleTraining(String name) {
		return (Environment.MINIMUM_TRAINING_STATES * 10);
	}
	
	protected StringBuilder getPlannedActions() {
		return new StringBuilder();
	}
	
	/******************************** Methods called by worker ********************************/

	protected final void updateEnvironment(Environment env) {
		lockMe(this);
		if (bufferSize!=env.getBufferSize()) {
			for (Entry<String,SimAnimalModule> entry: animalModules.entrySet()) {
				entry.getValue().updateMaxSequenceDistance(env.getBufferSize());
			}
			bufferSize = env.getBufferSize();
		}
		unlockMe(this);
	}
	
	protected final void animate() {
		String[] inputs = environment.takeLook(this);
		if (inputs!=null) {
			String action = getActionForInputs(inputs,getActionOutputDone());
			if (action.length()==0) {
				action = getRandomAction();
			}
			lockMe(this);
			long trained = trainedStates;
			int fired = (int) firedLinks;
			firedLinks = 0;
			StringBuilder trainingModules = new StringBuilder();
			StringBuilder confabulatingModules = new StringBuilder();
			for (String name: getModuleNames()) {
				if (trainedStates>=getMinimumTrainingStatesForModuleTraining(name) &&
					trainedStates<getMaximumTrainingStatesForModuleTraining(name)
					) {
					if (trainingModules.length()>0) {
						trainingModules.append(", ");
					}
					trainingModules.append(name);
				}
				if (trainedStates>=getMinimumTrainingStatesForModuleConfabulation(name)) {
					if (confabulatingModules.length()>0) {
						confabulatingModules.append(", ");
					}
					confabulatingModules.append(name);
				}
			}
			unlockMe(this);
			if (trained<(Environment.MINIMUM_TRAINING_STATES * 8)) {
				int rand = Generic.generateRandom(0,20);
				if (trained<(Environment.MINIMUM_TRAINING_STATES * 2) && rand<=15) {
					action = getRandomAction();
				} else if (trained<(Environment.MINIMUM_TRAINING_STATES * 4) && rand<=10) {
					action = getRandomAction();
				} else if (trained<(Environment.MINIMUM_TRAINING_STATES * 6) && rand<=5) {
					action = getRandomAction();
				} else if (rand<=0) {
					action = getRandomAction();
				}
			}
			StringBuilder plannedActions = getPlannedActions();
			String feedback = environment.takeAction(this,action,fired,trained,plannedActions,trainingModules,confabulatingModules);
			if (actionDone(action,feedback)) {
				handleActionDone(action,feedback,getActionOutputDone(),inputs);
			} else {
				handleActionNotDone(action,feedback,inputs);
			}
			setVisualInputs(inputs[0],inputs[1],inputs[2],inputs[3],inputs[4],inputs[5]);
			setActionOutput(action);
			setActionOuputFeedback(feedback);
			lockMe(this);
			trainedStates++;
			unlockMe(this);
		} else {
			for (String name: getBufferNames()) {
				flushBuffer(name);
			}
		}
	}

	/******************************** Methods called by environment ********************************/
	
	protected final void setAnimalAndEnvironment(Animal animal,SimEnvironment environment,Environment env) {
		this.animal = animal;
		this.environment = environment;
		trainedStates = animal.getTrainedStates();
		for (String name: getBufferNames()) {
			buffers.put(name,new ArrayList<String>());
		}
		modules = new ObjAnimalModule(animal);
		modules.initialize();
		for (String name: getModuleNames()) {
			animalModules.put(name,new SimAnimalModule(modules.getOrAddModuleByName(name,env.getBufferSize())));
			animalModules.get(name).initialize();
		}
	}
	
	protected final void updateModules() {
		lockMe(this);
		for (Entry<String,SimAnimalModule> entry: animalModules.entrySet()) {
			entry.getValue().update();
		}
		unlockMe(this);
	}

	protected final void reinitializeModules() {
		lockMe(this);
		for (Entry<String,SimAnimalModule> entry: animalModules.entrySet()) {
			entry.getValue().reinitialize();
		}
		unlockMe(this);
	}

	protected final void removeModules() {
		lockMe(this);
		for (Entry<String,SimAnimalModule> entry: animalModules.entrySet()) {
			entry.getValue().remove();
		}
		unlockMe(this);
	}
	
	/******************************** Protected final methods ********************************/
	
	protected final boolean trainModuleSequence(String moduleName,SymbolSequenceTraining trainSequence,int weight) {
		boolean done = false;
		lockMe(this);
		if (trainedStates>=getMinimumTrainingStatesForModuleTraining(moduleName) &&
			trainedStates<getMaximumTrainingStatesForModuleTraining(moduleName)
			) {
			animalModules.get(moduleName).trainSequence(trainSequence,false,weight);
			done = true;
		}
		unlockMe(this);
		return done;
	}

	protected final boolean confabulateModuleExtend(String moduleName,ConInputOutput io) {
		boolean done = false;
		lockMe(this);
		if (trainedStates>=getMinimumTrainingStatesForModuleConfabulation(moduleName)) {
			if (io.getMaxOutputSymbols()>16) {
				io.setMaxOutputSymbols(16);
			}
			animalModules.get(moduleName).confabulateExtend(io);
			firedLinks+=io.getFiredLinks();
			done = true;
		}	
		unlockMe(this);
		return done;
	}

	protected final void setVisualInputs(String colorLeft,String intensityLeft,String colorFront,String intensityFront,String colorRight,String intensityRight) {
		lockMe(this);
		addBufferValueNoLock(INPUT_COLOR_LEFT,colorLeft);
		addBufferValueNoLock(INPUT_INTENSITY_LEFT,intensityLeft);
		addBufferValueNoLock(INPUT_COLOR_FRONT,colorFront);
		addBufferValueNoLock(INPUT_INTENSITY_FRONT,intensityFront);
		addBufferValueNoLock(INPUT_COLOR_RIGHT,colorRight);
		addBufferValueNoLock(INPUT_INTENSITY_RIGHT,intensityRight);
		unlockMe(this);
	}

	protected final String[] getVisualInputs(int index) {
		String[] inputs = new String[6];
		lockMe(this);
		inputs[0] = getBufferValueNoLock(INPUT_COLOR_LEFT,index);
		inputs[1] = getBufferValueNoLock(INPUT_INTENSITY_LEFT,index);
		inputs[2] = getBufferValueNoLock(INPUT_COLOR_FRONT,index);
		inputs[3] = getBufferValueNoLock(INPUT_INTENSITY_FRONT,index);
		inputs[4] = getBufferValueNoLock(INPUT_COLOR_RIGHT,index);
		inputs[5] = getBufferValueNoLock(INPUT_INTENSITY_RIGHT,index);
		unlockMe(this);
		return inputs;
	}
	
	protected final void setActionOutput(String action) {
		lockMe(this);
		addBufferValueNoLock(OUTPUT_ACTION,action);
		unlockMe(this);
	}

	protected final String getActionOutput(int index) {
		String r = "";
		lockMe(this);
		r = getBufferValueNoLock(OUTPUT_ACTION,index);
		unlockMe(this);
		return r;
	}

	protected final void setActionOuputFeedback(String feedback) {
		lockMe(this);
		addBufferValueNoLock(OUTPUT_ACTION_FEEDBACK,feedback);
		unlockMe(this);
	}

	protected final String getActionOutputFeedback(int index) {
		String r = "";
		lockMe(this);
		r = getBufferValueNoLock(OUTPUT_ACTION_FEEDBACK,index);
		unlockMe(this);
		return r;
	}

	protected final List<String> getActionOutputDone() {
		List<String> r = new ArrayList<String>();
		lockMe(this);
		for (int i = 0; i < buffers.get(OUTPUT_ACTION).size(); i++) {
			String action = buffers.get(OUTPUT_ACTION).get(i);
			String feedback = buffers.get(OUTPUT_ACTION_FEEDBACK).get(i);
			if (actionDone(action,feedback)) {
				r.add(action);
				if (r.size()>=getBufferSize()) {
					break;
				}
			} else {
				break;
			}
		}
		unlockMe(this);
		return r;
	}
	
	protected final void addBufferValue(String name,String value) {
		lockMe(this);
		addBufferValueNoLock(name,value);
		unlockMe(this);
	}

	protected final String getBufferValue(String name,int index) {
		lockMe(this);
		String r = getBufferValueNoLock(name,index);
		unlockMe(this);
		return r;
	}

	protected final String removeBufferValue(String name) {
		lockMe(this);
		String r = removeBufferValueNoLock(name);
		unlockMe(this);
		return r;
	}

	protected final void flushBuffer(String name) {
		lockMe(this);
		flushBufferNoLock(name);
		unlockMe(this);
	}

	protected final boolean bufferIsFull(String name) {
		boolean r = false;
		lockMe(this);
		r = bufferIsFullNoLock(name);
		unlockMe(this);
		return r;
	}

	protected final boolean bufferIsEmpty(String name) {
		boolean r = false;
		lockMe(this);
		r = bufferIsEmptyNoLock(name);
		unlockMe(this);
		return r;
	}
	
	/******************************** Lock free protected final methods ********************************/
	
	protected final Animal getAnimal() {
		return animal;
	}

	protected final boolean actionDone(String action, String feedback) {
		return ( 
			(action.equals(Animal.ACTION_TURN_LEFT) && feedback.equals(Animal.FEEDBACK_TURNED_LEFT)) || 
			(action.equals(Animal.ACTION_MOVE_FORWARD) && feedback.equals(Animal.FEEDBACK_MOVED_FORWARD)) || 
			(action.equals(Animal.ACTION_TURN_RIGHT) && feedback.equals(Animal.FEEDBACK_TURNED_RIGHT)) || 
			(action.equals(Animal.ACTION_TAKE_BITE) && feedback.equals(Animal.FEEDBACK_TOOK_BITE))
		); 
	}
	
	protected final String getRandomAction() {
		String r = "";
		int rand = Generic.generateRandom(0,9);
		if (rand==0) {
			r = Animal.ACTION_TURN_LEFT;
		} else if (rand==1) {
			r = Animal.ACTION_TURN_LEFT;
		} else if (rand==2) {
			r = Animal.ACTION_TURN_RIGHT;
		} else if (rand==3) {
			r = Animal.ACTION_TURN_RIGHT;
		} else if (rand==4) {
			r = Animal.ACTION_TAKE_BITE;
		} else if (rand==5) {
			r = Animal.ACTION_TAKE_BITE;
		} else if (rand==6) {
			r = Animal.ACTION_TAKE_BITE;
		} else if (rand==7) {
			r = Animal.ACTION_MOVE_FORWARD;
		} else if (rand==8) {
			r = Animal.ACTION_MOVE_FORWARD;
		} else if (rand==9) {
			r = Animal.ACTION_MOVE_FORWARD;
		}
		return r;
	}
	
	protected final String getFoodColor() {
		String foodColor = "";
		if (getAnimal() instanceof Herbivore) {
			foodColor = Environment.COLOR_PLANT;
		} else if (getAnimal() instanceof Carnivore) {
			foodColor = Environment.COLOR_HERBIVORE;
		}
		return foodColor;
	}

	protected final String getDangerColor() {
		String dangerColor = "";
		if (getAnimal() instanceof Herbivore) {
			dangerColor = Environment.COLOR_CARNIVORE;
		} else if (getAnimal() instanceof Carnivore) {
			dangerColor = Environment.COLOR_CARNIVORE;
		}
		return dangerColor;
	}
	
	/******************************** Lock free private methods ********************************/
	
	private int getBufferSize() {
		return bufferSize;
	}

	private void addBufferValueNoLock(String name,String value) {
		buffers.get(name).add(0,value);
		trimListToBufferSize(buffers.get(name),getBufferSize());
	}

	private String getBufferValueNoLock(String name,int index) {
		String r = "";
		if (buffers.get(name).size()>index) {
			r = buffers.get(name).get(index);
		}
		return r;
	}

	private String removeBufferValueNoLock(String name) {
		String r = "";
		if (buffers.get(name).size()>0) {
			r = buffers.get(name).remove(0);
		}
		return r;
	}

	private void flushBufferNoLock(String name) {
		if (buffers.get(name).size()>0) {
			buffers.get(name).clear();
		}
	}

	private boolean bufferIsFullNoLock(String name) {
		return buffers.get(name).size()==getBufferSize();
	}

	private boolean bufferIsEmptyNoLock(String name) {
		return buffers.get(name).size()==0;
	}

	private void trimListToBufferSize(List<String> list, int size) {
		if (list.size()>size) {
			for (int i = 0; i < list.size() - size; i++) {
				list.remove((list.size() - 1) - i);
			}
		}
	}
}
