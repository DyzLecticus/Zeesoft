package nl.zeesoft.zdk.htm2.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import nl.zeesoft.zdk.htm2.mdl.Column;
import nl.zeesoft.zdk.htm2.mdl.ModelConfig;

public class SpatialPoolerColumn extends Column {	
	public Queue<Boolean>	activityLog			= new LinkedList<Boolean>();
	public float			totalActive			= 0;
	public float			averageActivity		= 0;
	public float			boostFactor			= 1;
	
	public SpatialPoolerColumn(ModelConfig config,Column column) {
		super(config,column.index,column.posX,column.posY);
		setId(column.getId());
	}
	
	@Override
	public SpatialPoolerColumn copy() {
		SpatialPoolerColumn copy = new SpatialPoolerColumn(config,this);
		copyTo(copy,true,true);
		return copy;
	}
	
	protected void logActivity(boolean active, SpatialPoolerConfig poolerConfig) {
		activityLog.add(active);
		if (active) {
			totalActive++;
		}
		while (activityLog.size() > poolerConfig.maxActivityLogSize) {
			boolean act = activityLog.remove();
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
	
	protected void updateBoostFactors(SpatialPoolerConfig poolerConfig,float localAverageActivity) {
		if (localAverageActivity>0) {
			if (averageActivity!=localAverageActivity) {
				boostFactor = (float) Math.exp((float)poolerConfig.boostStrength * - 1 * (averageActivity - localAverageActivity));
			}
		}
	}
	
	protected List<Integer> calculateInputIndices(SpatialPoolerColumn column) {
		List<Integer> r = new ArrayList<Integer>();
		int inputPosX = column.getInputPosX();
		int inputPosY = column.getInputPosY();
		
		int minPosX = inputPosX - config.getProximalRadius();
		int minPosY = inputPosY - config.getProximalRadius();
		int maxPosX = inputPosX + 1 + config.getProximalRadius();
		int maxPosY = inputPosY + 1 + config.getProximalRadius();
		
		if (minPosX < 0) {
			minPosX = 0;
		}
		if (minPosY < 0) {
			minPosY = 0;
		}
		if (maxPosX > config.getInputSizeX()) {
			maxPosX = config.getInputSizeX();
		}
		if (maxPosY > config.getInputSizeY()) {
			maxPosY = config.getInputSizeY();
		}
		
		int posX = 0;
		int posY = 0;
		for (int i = 0; i < config.getInputLength(); i++) {
			if (posX>=minPosX && posX<maxPosX && posY>=minPosY && posY<maxPosY) {
				r.add(i);
			}
			posX++;
			if (posX % config.getInputSizeX() == 0) {
				posX = 0;
				posY++;
			}
			if (posY>maxPosY) {
				break;
			}
		}
		return r;
	}

	protected int getInputPosX() {
		return getRelativePos(getFloatPosX(),config.getProximalRadius(),config.getInputSizeX());
	}
	
	protected int getInputPosY() {
		return getRelativePos(getFloatPosY(),config.getProximalRadius(),config.getInputSizeY());
	}
}
