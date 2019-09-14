package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.functions.ZRandomize;

public class PoolerColumn {
	private 	PoolerConfig					config				= null;
	protected	int								index				= 0;
	protected	int								posX				= 0;
	protected	int								posY				= 0;
	
	protected	SortedMap<Integer,ProximalLink>	proxLinks			= new TreeMap<Integer,ProximalLink>();
	
	protected	int								overlapScore		= 0;
	
	protected PoolerColumn(PoolerConfig config,int index,int posX, int posY) {
		this.config = config;
		this.index = index;
		this.posX = posX;
		this.posY = posY;
		
		List<Integer> inputIndices = calculateInputIndices();
		for (Integer idx: inputIndices) {
			ProximalLink lnk = new ProximalLink();
			lnk.inputIndex = idx;
			proxLinks.put(lnk.inputIndex,lnk);
		}
	}
	
	protected void randomizeConnections() {
		float max = (config.inputRadius * 2);
		max = max * max;
		if (config.inputSize<max) {
			max = config.inputSize;
		}
		float min = (config.inputRadius * config.inputRadius);
		if (config.inputSize<min) {
			min = config.inputSize;
		}
		float thresh = config.potentialConnections;
		if (min!=max) {
			float minPotential = (min / max) * config.potentialConnections;
			thresh = (max / (float) proxLinks.size()) * minPotential;
			//System.out.println("Index " + index + "; thresh: " + thresh + ", indices: " + proxLinks.size() + " / " + max + ", minPotential: " + minPotential);
		}
		List<ProximalLink> availableLinks = new ArrayList<ProximalLink>(proxLinks.values());
		int sel = (int) ((float) availableLinks.size() * thresh);
		for (int i = 0; i < sel; i++) {
			ProximalLink lnk = availableLinks.remove(ZRandomize.getRandomInt(0,availableLinks.size() - 1));
			if (ZRandomize.getRandomInt(0,1)==1) {
				lnk.connection = ZRandomize.getRandomFloat(0,config.connectionThreshold);
			} else {
				lnk.connection = ZRandomize.getRandomFloat(config.connectionThreshold,1.0F);
			}
		}
		for (ProximalLink lnk: availableLinks) {
			proxLinks.remove(lnk.inputIndex);
		}
	}
	
	protected void calculateOverlapScoreForOnBits(List<Integer> onBits) {
		overlapScore = 0;
		for (Integer onBit: onBits) {
			ProximalLink lnk = proxLinks.get(onBit);
			if (lnk!=null && lnk.connection>config.connectionThreshold) {
				overlapScore++;
			}
		}
	}
	
	protected void learnOnBits(List<Integer> onBits) {
		for (ProximalLink lnk: proxLinks.values()) {
			if (lnk.connection>=0) {
				if (onBits.contains(lnk.inputIndex)) {
					lnk.connection += config.connectionIncrement;
					if (lnk.connection > 1) {
						lnk.connection = 1;
					}
				} else {
					lnk.connection -= config.connectionDecrement;
					if (lnk.connection < 0) {
						lnk.connection = 0;
					}
				}
			}
		}
	}
	
	protected List<Integer> calculateInputIndices() {
		List<Integer> r = new ArrayList<Integer>();
		int inputPosX = getInputPosX();
		int inputPosY = getInputPosY();
		
		int minPosX = inputPosX - config.inputRadius;
		int minPosY = inputPosY - config.inputRadius;
		int maxPosX = inputPosX + config.inputRadius;
		int maxPosY = inputPosY + config.inputRadius;
		
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
		
		//System.out.println("-> " + index + "; min, max X/Y: " + minPosX + "/" + minPosY + ", " + maxPosX + "/" + maxPosY + " (" + getOutputPosX() + "/" + getOutputPosY() + " => " + inputPosX + "/" + inputPosX + ")");
		
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.inputSize; i++) {
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

	protected float getOutputPosX() {
		return (float) posX / (float) config.outputSizeX;  
	}
	
	protected float getOutputPosY() {
		return (float) posY / (float) config.outputSizeY;  
	}
	
	protected int getInputPosX() {
		return (int) (getOutputPosX() * (float) config.inputSizeX);  
	}
	
	protected int getInputPosY() {
		return (int) (getOutputPosY() * (float) config.inputSizeY);  
	}
}
