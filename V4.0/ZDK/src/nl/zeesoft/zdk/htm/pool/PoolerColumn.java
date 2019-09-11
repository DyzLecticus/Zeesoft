package nl.zeesoft.zdk.htm.pool;

import java.util.List;

import nl.zeesoft.zdk.functions.ZRandomize;

public class PoolerColumn {
	private 	PoolerConfig	config				= null;
	protected	int				index				= 0;
	protected	float[]			inputConnections	= null;
	protected	int				overlapScore		= 0;
	
	protected PoolerColumn(PoolerConfig config,int index) {
		this.config = config;
		this.index = index;
		inputConnections = new float[config.inputSize];
		for (int i = 0; i < inputConnections.length; i++) {
			inputConnections[i] = 0;
		}
	}
	
	protected void randomizeConnections() {
		for (int i = 0; i < inputConnections.length; i++) {
			float connect = ZRandomize.getRandomFloat(0,1);
			if (connect<=config.potentialConnections) {
				if (ZRandomize.getRandomInt(0,1)==1) {
					inputConnections[i] = ZRandomize.getRandomFloat(0,config.connectionThreshold);
				} else {
					inputConnections[i] = ZRandomize.getRandomFloat(config.connectionThreshold,1.0F);
				}
			} else {
				inputConnections[i] = -1;
			}
		}
	}
	
	protected void calculateOverlapScoreForOnBits(List<Integer> onBits) {
		overlapScore = 0;
		for (Integer onBit: onBits) {
			if (inputConnections[onBit]>config.connectionThreshold) {
				overlapScore++;
			}
		}
	}
	
	protected void learnOnBits(List<Integer> onBits) {
		for (int i = 0; i < inputConnections.length; i++) {
			if (inputConnections[i]>=0) {
				if (onBits.contains(i)) {
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
}
