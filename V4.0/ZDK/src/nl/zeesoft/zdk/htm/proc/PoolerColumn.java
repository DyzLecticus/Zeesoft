package nl.zeesoft.zdk.htm.proc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.zeesoft.zdk.functions.ZRandomize;
import nl.zeesoft.zdk.htm.util.HistoricalBits;

public class PoolerColumn {
	private 	PoolerConfig			config			= null;
	protected	int						index			= 0;
	protected	int						posX			= 0;
	protected	int						posY			= 0;

	protected	PoolerColumnGroup		columnGroup		= null;
	
	protected	Set<ProximalLink>		proxLinks		= new HashSet<ProximalLink>();
	
	protected	HistoricalBits			activityLog		= null;
	protected	float					boostFactor		= 1;
	
	protected PoolerColumn(PoolerConfig config,int index,int posX, int posY) {
		this.config = config;
		this.index = index;
		this.posX = posX;
		this.posY = posY;
		this.activityLog = new HistoricalBits();
		activityLog.window = config.boostActivityLogSize;
	}
	
	protected void randomizeConnections(PoolerConnections connections) {
		proxLinks.clear();
		List<Integer> inputIndices = calculateInputIndices();
		List<ProximalLink> availableLinks = new ArrayList<ProximalLink>();
		for (Integer idx: inputIndices) {
			ProximalLink lnk = new ProximalLink();
			lnk.inputIndex = idx;
			availableLinks.add(lnk);
		}
		int sel = (int) ((float) availableLinks.size() * config.potentialProximalConnections);
		for (int i = 0; i < sel; i++) {
			ProximalLink lnk = availableLinks.remove(ZRandomize.getRandomInt(0,availableLinks.size() - 1));
			proxLinks.add(lnk);
			if (ZRandomize.getRandomInt(0,1)==1) {
				lnk.connection = ZRandomize.getRandomFloat(0,config.proximalConnectionThreshold);
			} else {
				lnk.connection = ZRandomize.getRandomFloat(config.proximalConnectionThreshold,1.0F);
			}
			connections.addColumnLink(this,lnk);
		}
	}

	protected void learnOnBits(List<Integer> onBits,PoolerConnections connections) {
		for (ProximalLink lnk: proxLinks) {
			if (onBits.contains(lnk.inputIndex)) {
				if (lnk.connection <= config.proximalConnectionThreshold && lnk.connection + config.proximalConnectionIncrement > config.proximalConnectionThreshold) {
					connections.addColumnLink(this,lnk);
				}
				lnk.connection += config.proximalConnectionIncrement;
				if (lnk.connection > 1) {
					lnk.connection = 1;
				}
			} else {
				if (lnk.connection > config.proximalConnectionThreshold && lnk.connection - config.proximalConnectionDecrement <= config.proximalConnectionThreshold) {
					connections.removeColumnLink(this,lnk);
				}
				lnk.connection -= config.proximalConnectionDecrement;
				if (lnk.connection < 0) {
					lnk.connection = 0;
				}
			}
		}
	}
	
	protected void logActivity(boolean active) {
		if (config.boostStrength>0) {
			activityLog.addBit(active);
		}
	}
	
	protected void updateBoostFactor(float localAverageActivity) {
		if (config.boostStrength>0 && localAverageActivity>0) {
			if (activityLog.average!=localAverageActivity) {
				boostFactor = (float) Math.exp((float)config.boostStrength * - 1 * (activityLog.average - localAverageActivity));
			} else {
				boostFactor = 1;
			}
		}
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

	protected float getOutputPosX() {
		return (float) posX / (float) config.outputSizeX;  
	}
	
	protected float getOutputPosY() {
		return (float) posY / (float) config.outputSizeY;  
	}
	
	protected int getInputPosX() {
		int r = 0;
		int min = config.proximalRadius;
		int max = config.inputSizeX - config.proximalRadius;
		if (min>=max) {
			r = config.inputSizeX / 2;
		} else {
			max = max - config.proximalRadius;
			r = min + ((int) (getOutputPosX() * (float) max));
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
			r = min + ((int) (getOutputPosY() * (float) max));
		}
		return r;  
	}
	
	protected int getRelativePosX() {
		int r = 0;
		float rel = (float) posX / (float) config.outputSizeX;
		int min = config.boostInhibitionRadius;
		float max = config.outputSizeX - config.boostInhibitionRadius;
		if (min>=max) {
			r = config.outputSizeX / 2;
		} else {
			max = max - config.boostInhibitionRadius;
			r = min + (int) (rel * max);
		}
		return r;
	}
	
	protected int getRelativePosY() {
		int r = 0;
		float rel = (float) posY / (float) config.outputSizeY;
		int min = config.boostInhibitionRadius;
		float max = config.outputSizeY - config.boostInhibitionRadius;
		if (min>=max) {
			r = config.outputSizeY / 2;
		} else {
			max = max - config.boostInhibitionRadius;
			r = min + (int) (rel * max);
		}
		return r;
	}
}
