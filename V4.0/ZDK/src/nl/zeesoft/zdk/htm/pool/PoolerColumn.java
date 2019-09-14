package nl.zeesoft.zdk.htm.pool;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.functions.ZRandomize;

public class PoolerColumn {
	private 	PoolerConfig	config				= null;
	protected	int				index				= 0;
	protected	int				posX				= 0;
	protected	int				posY				= 0;
	protected	List<Integer>	inputIndices		= null;
	
	protected	float[]			inputConnections	= null;
	
	protected	int				overlapScore		= 0;
	
	protected PoolerColumn(PoolerConfig config,int index,int posX, int posY) {
		this.config = config;
		this.index = index;
		this.posX = posX;
		this.posY = posY;
		inputIndices = calculateInputIndices();
		inputConnections = new float[inputIndices.size()];
		for (int i = 0; i < inputConnections.length; i++) {
			inputConnections[i] = -1;
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
			thresh = (max / (float) inputIndices.size()) * minPotential;
		}
		//System.out.println("Index " + index + "; thresh: " + thresh + ", indices: " + inputIndices.size() + " / " + max + ", minPotential: " + minPotential);
		List<Integer> availableConnections = new ArrayList<Integer>();
		for (int i = 0; i < inputConnections.length; i++) {
			availableConnections.add(i);
		}
		int sel = (int) ((float) availableConnections.size() * thresh);
		for (int i = 0; i < sel; i++) {
			int index = availableConnections.remove(ZRandomize.getRandomInt(0,availableConnections.size() - 1));
			if (ZRandomize.getRandomInt(0,1)==1) {
				inputConnections[index] = ZRandomize.getRandomFloat(0,config.connectionThreshold);
			} else {
				inputConnections[index] = ZRandomize.getRandomFloat(config.connectionThreshold,1.0F);
			}
		}
	}
	
	protected void calculateOverlapScoreForOnBits(List<Integer> onBits) {
		overlapScore = 0;
		for (Integer onBit: onBits) {
			if (inputIndices.size()==config.inputSize || inputIndices.contains(onBit)) {
				int i = inputIndices.indexOf(onBit);
				if (inputConnections[i]>config.connectionThreshold) {
					overlapScore++;
				}
			}
		}
	}
	
	protected void learnOnBits(List<Integer> onBits) {
		for (int i = 0; i < inputConnections.length; i++) {
			if (inputConnections[i]>=0) {
				Integer index = inputIndices.get(i);
				if (onBits.contains(index)) {
					inputConnections[i] += config.connectionIncrement;
					if (inputConnections[i] > 1) {
						inputConnections[i] = 1;
					}
				} else {
					inputConnections[i] -= config.connectionDecrement;
					if (inputConnections[i] < 0) {
						inputConnections[i] = 0;
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
