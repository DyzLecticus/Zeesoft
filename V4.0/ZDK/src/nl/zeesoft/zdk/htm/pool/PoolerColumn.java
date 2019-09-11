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
					inputConnections[i] = ZRandomize.getRandomFloat(config.connectionThreshold,0);
				}
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
}
