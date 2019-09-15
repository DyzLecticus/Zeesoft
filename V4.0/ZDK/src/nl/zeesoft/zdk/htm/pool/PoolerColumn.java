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

	protected	PoolerColumnGroup				columnGroup			= null;
	
	protected	SortedMap<Integer,ProximalLink>	proxLinks			= new TreeMap<Integer,ProximalLink>();
	
	protected	int								overlapScore		= 0;
	
	protected	List<Boolean>					activityLog			= new ArrayList<Boolean>();
	protected	float							totalActive			= 0;
	protected	float							averageActivity		= 0;
	protected	float							boostFactor			= 1;
	
	protected PoolerColumn(PoolerConfig config,int index,int posX, int posY) {
		this.config = config;
		this.index = index;
		this.posX = posX;
		this.posY = posY;
	}
	
	protected void randomizeConnections() {
		proxLinks.clear();
		List<Integer> inputIndices = calculateInputIndices();
		for (Integer idx: inputIndices) {
			ProximalLink lnk = new ProximalLink();
			lnk.inputIndex = idx;
			proxLinks.put(lnk.inputIndex,lnk);
		}
		List<ProximalLink> availableLinks = new ArrayList<ProximalLink>(proxLinks.values());
		int sel = (int) ((float) availableLinks.size() * config.potentialConnections);
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
	
	protected void logActivity(boolean active) {
		if (config.boostStrength>0) {
			activityLog.add(active);
			if (active) {
				totalActive++;
			}
			while (activityLog.size() > config.maxActivityLogSize) {
				boolean act = activityLog.remove(0);
				if (act) {
					totalActive--;
				}
			}
			if (totalActive>0) {
				averageActivity = totalActive / (float) activityLog.size();
			} else {
				averageActivity = 0;
			}
		}
	}
	
	protected void updateBoostFactor() {
		if (config.boostStrength>0) {
			float localAverageActivity = columnGroup.averageActivity;
			if (localAverageActivity>0) {
				if (averageActivity!=localAverageActivity) {
					boostFactor = (float) Math.exp((float)config.boostStrength * - 1 * (averageActivity - localAverageActivity));
				} else {
					boostFactor = 1;
				}
				//if (index==100) {
				//	System.out.println("Activity: " + averageActivity + ", target: " + localAverageActivity + ", boost: " + boostFactor);
				//}
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
		int maxPosX = inputPosX + 1 + config.inputRadius;
		int maxPosY = inputPosY + 1 + config.inputRadius;
		
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
		
		//System.out.println("-> " + index + "; min, max X/Y: " + minPosX + "/" + minPosY + ", " + maxPosX + "/" + maxPosY + " (" + getOutputPosX() + "/" + getOutputPosY() + " => " + inputPosX + "/" + inputPosY + ")");
		
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
		int r = 0;
		int min = config.inputRadius;
		int max = config.inputSizeX - (config.inputRadius * 2);
		if (min>=max) {
			r = config.inputSizeX / 2;
		} else {
			r = min + ((int) (getOutputPosX() * (float) max));
		}
		return r;
	}
	
	protected int getInputPosY() {
		int r = 0;
		int min = config.inputRadius;
		int max = config.inputSizeY - (config.inputRadius * 2);
		if (min>=max) {
			r = config.inputSizeY / 2;
		} else {
			r = min + ((int) (getOutputPosY() * (float) max));
		}
		return r;  
	}
	
	protected int getRelativePosX() {
		int r = 0;
		float rel = (float) posX / (float) config.outputSizeX;
		int min = config.outputRadius;
		float max = config.outputSizeX - (config.outputRadius * 2);
		if (min>=max) {
			r = config.outputSizeX / 2;
		} else {
			r = min + (int) (rel * max);
		}
		return r;
	}
	
	protected int getRelativePosY() {
		int r = 0;
		float rel = (float) posY / (float) config.outputSizeY;
		int min = config.outputRadius;
		float max = config.outputSizeY - (config.outputRadius * 2);
		if (min>=max) {
			r = config.outputSizeY / 2;
		} else {
			r = min + (int) (rel * max);
		}
		return r;
	}
}
