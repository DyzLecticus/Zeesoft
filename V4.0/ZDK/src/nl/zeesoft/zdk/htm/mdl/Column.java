package nl.zeesoft.zdk.htm.mdl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.functions.ZRandomize;

public class Column implements ModelObject {
	protected ModelConfig	config				= null;
	
	public int				index				= 0;
	public int				posX				= 0;
	public int				posY				= 0;
	
	public ColumnGroup		columnGroup			= null;
	public List<Cell>		cells				= new ArrayList<Cell>();
	public ProximalDendrite	proximalDendrite	= null;
	
	public Column(ModelConfig config, int index,int posX,int posY) {
		this.config = config;
		this.index = index;
		this.posX = posX;
		this.posY = posY;
	}
	
	@Override
	public String getId() {
		return getClass().getSimpleName() + ":" + posX + "-" + posY;
	}
	
	protected List<ProximalSynapse> initializeProximalDendriteSynapses() {
		List<ProximalSynapse> r = new ArrayList<ProximalSynapse>();
		proximalDendrite.synapses.clear();
		List<Integer> inputIndices = calculateInputIndices();
		List<ProximalSynapse> availableSynapses = new ArrayList<ProximalSynapse>();
		int i = 0;
		for (Integer idx: inputIndices) {
			ProximalSynapse synapse = new ProximalSynapse(proximalDendrite.getId(),i);
			synapse.inputIndex = idx;
			availableSynapses.add(synapse);
		}
		int sel = (int) ((float) availableSynapses.size() * config.proximalConnections);
		for (i = 0; i < sel; i++) {
			ProximalSynapse synapse = availableSynapses.remove(ZRandomize.getRandomInt(0,availableSynapses.size() - 1));
			proximalDendrite.synapses.add(synapse);
			r.add(synapse);
		}
		return r;
	}
	
	protected List<Integer> calculateInputIndices() {
		List<Integer> r = new ArrayList<Integer>();
		int inputPosX = getInputPosX();
		int inputPosY = getInputPosY();
		
		int minPosX = inputPosX - config.proximalRadius;
		int minPosY = inputPosY - config.proximalRadius;
		int maxPosX = inputPosX + 1 + config.proximalRadius;
		int maxPosY = inputPosY + 1 + config.proximalRadius;
		
		if (minPosX < 0) {
			minPosX = 0;
		}
		if (minPosY < 0) {
			minPosY = 0;
		}
		if (maxPosX > config.inputSizeX) {
			maxPosX = config.inputSizeX;
		}
		if (maxPosY > config.inputSizeY) {
			maxPosY = config.inputSizeY;
		}
		
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.inputLength; i++) {
			if (posX>=minPosX && posX<maxPosX && posY>=minPosY && posY<maxPosY) {
				r.add(i);
			}
			posX++;
			if (posX % config.inputSizeX == 0) {
				posX = 0;
				posY++;
			}
			if (posY>maxPosY) {
				break;
			}
		}
		return r;
	}

	protected float getFloatPosX() {
		return (float) posX / (float) config.columnSizeX;  
	}
	
	protected float getFloatPosY() {
		return (float) posY / (float) config.columnSizeY;  
	}
	
	protected int getInputPosX() {
		int r = 0;
		int min = config.proximalRadius;
		int max = config.inputSizeX - config.proximalRadius;
		if (min>=max) {
			r = config.inputSizeX / 2;
		} else {
			max = max - config.proximalRadius;
			r = min + ((int) (getFloatPosX() * (float) max));
		}
		return r;
	}
	
	protected int getInputPosY() {
		int r = 0;
		int min = config.proximalRadius;
		int max = config.inputSizeY - config.proximalRadius;
		if (min>=max) {
			r = config.inputSizeY / 2;
		} else {
			max = max - config.proximalRadius;
			r = min + ((int) (getFloatPosY() * (float) max));
		}
		return r;  
	}
	
	protected int getRelativePosX() {
		int r = 0;
		int min = config.distalRadius;
		float max = config.columnSizeX - config.distalRadius;
		if (min>=max) {
			r = config.columnSizeX / 2;
		} else {
			max = max - config.distalRadius;
			r = min + (int) (getFloatPosX() * max);
		}
		return r;
	}
	
	protected int getRelativePosY() {
		int r = 0;
		int min = config.distalRadius;
		float max = config.columnSizeY - config.distalRadius;
		if (min>=max) {
			r = config.columnSizeY / 2;
		} else {
			max = max - config.distalRadius;
			r = min + (int) (getFloatPosY() * max);
		}
		return r;
	}
}
