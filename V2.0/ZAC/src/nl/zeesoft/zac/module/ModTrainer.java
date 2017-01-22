package nl.zeesoft.zac.module;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.ZAC;
import nl.zeesoft.zac.database.model.Module;
import nl.zeesoft.zac.database.model.SymbolLink;
import nl.zeesoft.zac.database.model.SymbolLinkContext;
import nl.zeesoft.zac.database.model.SymbolLinkSequence;
import nl.zeesoft.zac.database.model.SymbolSequenceTraining;
import nl.zeesoft.zac.module.confabulate.ConModuleInstance;
import nl.zeesoft.zac.module.object.ObjSymbolLinkContext;
import nl.zeesoft.zac.module.object.ObjSymbolLinkSequence;
import nl.zeesoft.zac.module.object.ObjSymbolSequenceTraining;
import nl.zeesoft.zodb.Locker;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;
import nl.zeesoft.zodb.database.request.ReqUpdate;

/**
 * This class is used to train modules.
 * Only one instance of this class should be used to train a certain module.
 */
public class ModTrainer extends Locker {
	private Module							module					= null;
	private ObjSymbolSequenceTraining		sequences				= null;
	private ObjSymbolLinkContext			contextLinks			= null;
	private ObjSymbolLinkSequence			sequenceLinks			= null;
	
	private List<SymbolSequenceTraining>	trainingSequences 		= new ArrayList<SymbolSequenceTraining>();
	private int								trainingSequenceIndex 	= 0;
	
	public ModTrainer(Module module) {
		this.module = module;
		sequences = new ObjSymbolSequenceTraining(module.getId());
	}
	
	/**
	 * (Re)Loads all training sequences from the database
	 */
	public void refreshTrainingSequences() {
		sequences.reinitialize();
		trainingSequences.clear();
		trainingSequences.addAll(sequences.getModuleTrainingSequencesAsList());
	}

	/**
	 * Trains the next training sequence
	 */
	public void train() {
		initialize();
		if (trainingSequences.size()>0) {
			if (trainingSequenceIndex>=trainingSequences.size()) {
				trainingSequenceIndex = 0;
			}
			trainSequence(trainingSequences.get(trainingSequenceIndex),false,1);
			trainingSequenceIndex++;
		}
	}

	/**
	 * (Re)loads all links from database
	 */
	public void reinitialize() {
		boolean init = false;
		lockMe(this);
		if (contextLinks!=null && sequenceLinks!=null) {
			contextLinks.reinitialize();
			sequenceLinks.reinitialize();
		} else {
			init = true;
		}
		unlockMe(this);
		if (init) {
			initialize();
		}
	}
	
	/**
	 * Updates all link counters in database
	 */
	public void updateLinks() {
		initialize();
		List<HlpObject> updateCLinks = new ArrayList<HlpObject>();
		List<HlpObject> updateSLinks = new ArrayList<HlpObject>();
		lockMe(this);
		for (SymbolLinkContext cLink: contextLinks.getSymbolLinkContextsAsList()) {
			if (cLink.getId()>0 && cLink.getCount()>0) {
				updateCLinks.add(cLink);
			}
		}
		for (SymbolLinkSequence sLink: sequenceLinks.getSymbolLinkSequencesAsList()) {
			if (sLink.getId()>0 && sLink.getCount()>0) {
				updateSLinks.add(sLink);
			}
		}
		contextLinks.updateHlpObjects(updateCLinks);
		sequenceLinks.updateHlpObjects(updateSLinks);
		unlockMe(this);
	}

	/**
	 * Removes all zero count links from database
	 */
	public void pruneLinks() {
		initialize();
		List<HlpObject> removeCLinks = new ArrayList<HlpObject>();
		List<HlpObject> removeSLinks = new ArrayList<HlpObject>();
		lockMe(this);
		for (SymbolLinkContext cLink: contextLinks.getSymbolLinkContextsAsList()) {
			if (cLink.getId()>0 && cLink.getCount()<=0) {
				removeCLinks.add(cLink);
			}
		}
		for (SymbolLinkSequence sLink: sequenceLinks.getSymbolLinkSequencesAsList()) {
			if (sLink.getId()>0 && sLink.getCount()<=0) {
				removeSLinks.add(sLink);
			}
		}
		contextLinks.removeHlpObjects(removeCLinks);
		sequenceLinks.removeHlpObjects(removeSLinks);
		unlockMe(this);
	}
	
	/**
	 * Removes all links from database
	 */
	public void removeLinks() {
		initialize();
		List<HlpObject> removeCLinks = new ArrayList<HlpObject>();
		List<HlpObject> removeSLinks = new ArrayList<HlpObject>();
		lockMe(this);
		for (SymbolLinkContext cLink: contextLinks.getSymbolLinkContextsAsList()) {
			if (cLink.getId()>0) {
				removeCLinks.add(cLink);
			}
		}
		for (SymbolLinkSequence sLink: sequenceLinks.getSymbolLinkSequencesAsList()) {
			if (sLink.getId()>0) {
				removeSLinks.add(sLink);
			}
		}
		contextLinks.removeHlpObjects(removeCLinks);
		sequenceLinks.removeHlpObjects(removeSLinks);
		unlockMe(this);
	}
	
	/**
	 * This method is made public for implementations that want to bypass the training worker.
	 * @param sequence The sequence to train the module for
	 */
	public void trainSequence(SymbolSequenceTraining sequence,int weight) {
		trainSequence(sequence,true,weight);
	}
	
	/**
	 * This method is made public for implementations that want to bypass the training worker.
	 * @param sequence The sequence to train the module for
	 * @param update Indicates changed links must be updated instantly
	 */
	public void trainSequence(SymbolSequenceTraining sequence,boolean update,int weight) {
		initialize();
		
		lockMe(this);
		List<SymbolLinkContext> addCLinks = new ArrayList<SymbolLinkContext>();
		List<SymbolLinkSequence> addSLinks = new ArrayList<SymbolLinkSequence>();
		
		boolean divideCLinks = false;
		boolean divideSLinks = false;

		List<StringBuilder> symbols = ZAC.parseSymbolsFromText(sequence.getSequence());
		
		String[] pSyms = new String[(module.getMaxSequenceDistance() + 1)];
		for (StringBuilder symbol: symbols) {
			// Context
			if (sequence.getContextSymbol1().length()>0) {
				divideCLinks = learnContextLink(addCLinks,divideCLinks,symbol.toString(),sequence.getContextSymbol1().toString(),update,weight);
			}
			if (sequence.getContextSymbol2().length()>0) {
				divideCLinks = learnContextLink(addCLinks,divideCLinks,symbol.toString().toString(),sequence.getContextSymbol2().toString(),update,weight);
			}
			if (sequence.getContextSymbol3().length()>0) {
				divideCLinks = learnContextLink(addCLinks,divideCLinks,symbol.toString().toString(),sequence.getContextSymbol3().toString(),update,weight);
			}
			if (sequence.getContextSymbol4().length()>0) {
				divideCLinks = learnContextLink(addCLinks,divideCLinks,symbol.toString(),sequence.getContextSymbol4().toString(),update,weight);
			}
			if (sequence.getContextSymbol5().length()>0) {
				divideCLinks = learnContextLink(addCLinks,divideCLinks,symbol.toString(),sequence.getContextSymbol5().toString(),update,weight);
			}
			if (sequence.getContextSymbol6().length()>0) {
				divideCLinks = learnContextLink(addCLinks,divideCLinks,symbol.toString(),sequence.getContextSymbol6().toString(),update,weight);
			}
			if (sequence.getContextSymbol7().length()>0) {
				divideCLinks = learnContextLink(addCLinks,divideCLinks,symbol.toString(),sequence.getContextSymbol7().toString(),update,weight);
			}
			if (sequence.getContextSymbol8().length()>0) {
				divideCLinks = learnContextLink(addCLinks,divideCLinks,symbol.toString(),sequence.getContextSymbol8().toString(),update,weight);
			}
			// Sequence
			for (int i = module.getMaxSequenceDistance(); i>=0; i--) {
				if (i>0) {
					pSyms[i] = pSyms[(i - 1)];
				} else {
					pSyms[i] = symbol.toString();
				}
			}
			if (pSyms[1]!=null && pSyms[1].length()>0) {
				for (int i = 1; i <= module.getMaxSequenceDistance(); i++) {
					if (pSyms[i]!=null && pSyms[i].length()>0) {
						divideSLinks = learnSequenceLink(addSLinks,divideSLinks,pSyms[i],pSyms[0],i,update,weight);
					}
				}
			}
		}

		if (divideCLinks) {
			Messenger.getInstance().debug(this,module.getName() + ": Divide context links ...");
			int zero = 0;
			for (SymbolLinkContext cLink: contextLinks.getSymbolLinkContextsAsList()) {
				divideSymbolLinkCountByTwo(cLink,update);
				if (cLink.getCount()==0) {
					zero++;
				}
			}
			Messenger.getInstance().debug(this,module.getName() + ": Divided context links. Zero count links: " + zero);
		}
		if (divideSLinks) {
			Messenger.getInstance().debug(this,module.getName() + ": Divide sequence links ...");
			int zero = 0;
			for (SymbolLinkSequence sLink: sequenceLinks.getSymbolLinkSequencesAsList()) {
				divideSymbolLinkCountByTwo(sLink,update);
				if (sLink.getCount()==0) {
					zero++;
				}
			}
			Messenger.getInstance().debug(this,module.getName() + ": Divided sequence links. Zero count links: " + zero);
		}

		if (addSLinks.size()>0) {
			sequenceLinks.addSymbolLinkSequences(addSLinks);
		}
		if (addCLinks.size()>0) {
			contextLinks.addSymbolLinkContexts(addCLinks);
		}
		unlockMe(this);
	}

	/**
	 * Returns a new confabulator instance using the links loaded in this trainer
	 */
	public ConModuleInstance getNewConfabulatorInstance() {
		ConModuleInstance r = null;
		initialize();
		List<SymbolLinkContext> cLinks = new ArrayList<SymbolLinkContext>();
		List<SymbolLinkSequence> sLinks = new ArrayList<SymbolLinkSequence>();
		lockMe(this);
		for (SymbolLinkContext cLink: contextLinks.getSymbolLinkContextsAsList()) {
			if (cLink.getId()>0 && cLink.getCount()>0) {
				SymbolLinkContext copy = new SymbolLinkContext();
				copy.fromDataObject(cLink.toDataObject());
				cLinks.add(copy);
			}
		}
		for (SymbolLinkSequence sLink: sequenceLinks.getSymbolLinkSequencesAsList()) {
			if (sLink.getId()>0 && sLink.getCount()>0) {
				SymbolLinkSequence copy = new SymbolLinkSequence();
				copy.fromDataObject(sLink.toDataObject());
				sLinks.add(copy);
			}
		}
		r = new ConModuleInstance(module,cLinks,sLinks);
		unlockMe(this);
		return r;
	}

	private boolean learnContextLink(List<SymbolLinkContext> addCLinks, boolean divideCLinks,String symbolFrom, String symbolTo,boolean update,int weight) {
		if (weight<0) {
			weight = 1;
		} else if (weight>10) {
			weight = 10;
		}
		SymbolLinkContext cLink = contextLinks.getSymbolLinkContextBySymbolFromTo(symbolFrom,symbolTo);
		if (cLink==null) {
			for (SymbolLinkContext cL: addCLinks) {
				if (cL.getSymbolFrom().equals(symbolFrom) && cL.getSymbolTo().equals(symbolTo)) {
					cLink = cL;
					break;
				}
			}
		}
		if (cLink==null) {
			cLink = new SymbolLinkContext();
			cLink.setModuleId(module.getId());
			cLink.setSymbolFrom(symbolFrom);
			cLink.setSymbolTo(symbolTo);
			cLink.setCount(1);
			addCLinks.add(cLink);
		} else {
			cLink.setCount((cLink.getCount() + 1));
			if (cLink.getCount()>=module.getMaxContextCount()) {
				divideCLinks = true;
			}
			if (update && !divideCLinks && cLink.getId()>0) {
				ReqUpdate request = cLink.getNewUpdateRequest(null);
				for (String propertyName: request.getUpdateObject().getProperties()) {
					if (!propertyName.equals("count")) {
						request.getUpdateObject().removePropertyValue(propertyName);
					}
				}
				DbRequestQueue.getInstance().addRequest(request,this);
			}
		}
		return divideCLinks;
	}

	protected void initialize() {
		lockMe(this);
		if (contextLinks==null || sequenceLinks==null) {
			List<String> updateProperties = new ArrayList<String>();
			updateProperties.add("count");
			contextLinks = new ObjSymbolLinkContext(module.getId(),updateProperties);
			contextLinks.initialize();
			sequenceLinks = new ObjSymbolLinkSequence(module.getId(),updateProperties);
			sequenceLinks.initialize();
		}
		unlockMe(this);
	}

	private boolean learnSequenceLink(List<SymbolLinkSequence> addSLinks, boolean divideSLinks,String symbolFrom, String symbolTo,int distance,boolean update,int weight) {
		if (weight<0) {
			weight = 1;
		} else if (weight>10) {
			weight = 10;
		}
		SymbolLinkSequence sLink = sequenceLinks.getSymbolLinkSequenceBySymbolFromToDistance(symbolFrom,symbolTo,distance);
		if (sLink==null) {
			for (SymbolLinkSequence sL: addSLinks) {
				if (sL.getSymbolFrom().equals(symbolFrom) && sL.getSymbolTo().equals(symbolTo) && sL.getDistance()==distance) {
					sLink = sL;
					break;
				}
			}
		}
		if (sLink==null) {
			sLink = new SymbolLinkSequence();
			sLink.setModuleId(module.getId());
			sLink.setSymbolFrom(symbolFrom);
			sLink.setSymbolTo(symbolTo);
			sLink.setDistance(distance);
			sLink.setCount(1);
			addSLinks.add(sLink);
		} else {
			sLink.setCount((sLink.getCount() + weight));
			if (sLink.getCount()>=module.getMaxSequenceCount()) {
				divideSLinks = true;
			}
			if (update && !divideSLinks && sLink.getId()>0) {
				ReqUpdate request = sLink.getNewUpdateRequest(null);
				for (String propertyName: request.getUpdateObject().getProperties()) {
					if (!propertyName.equals("count")) {
						request.getUpdateObject().removePropertyValue(propertyName);
					}
				}
				DbRequestQueue.getInstance().addRequest(request,this);
			}
		}
		return divideSLinks;
	}
	
	private void divideSymbolLinkCountByTwo(SymbolLink symbolLink,boolean update) {
		if (symbolLink.getCount()>0) {
			if (symbolLink.getCount()==1) {
				symbolLink.setCount(0);
			} else {
				symbolLink.setCount((symbolLink.getCount() / 2));
			}
			if (update) {
				DbRequestQueue.getInstance().addRequest(symbolLink.getNewUpdateRequest(null),this);
			}
		}
	}
}
