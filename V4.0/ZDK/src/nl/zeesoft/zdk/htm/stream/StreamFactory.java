package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.BufferedPredictor;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.proc.Predictor;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class StreamFactory {
	protected StreamEncoder	encoder							= null;
	protected int			outputLength					= 0;
	protected int			outputBits						= 0;

	// Pooler configuration
	protected float			potentialProximalConnections	= 0.75F;
	protected int			proximalRadius					= 5;
	protected float			proximalConnectionThreshold		= 0.1F;
	protected float			proximalConnectionDecrement		= 0.008F;
	protected float			proximalConnectionIncrement		= 0.05F;
	protected float			boostStrength					= 10;
	protected int			boostInhibitionRadius			= 10;
	protected int			boostActivityLogSize			= 100;

	// Memory configuration
	protected int			depth							= 4;
	protected int			maxDistalConnectionsPerCell		= 9999;
	protected int			localDistalConnectedRadius		= 25;
	protected float			distalConnectionThreshold		= 0.2F;
	protected float			distalConnectionDecrement		= 0.003F;
	protected float			distalConnectionIncrement		= 0.1F;

	protected String		valueKey						= StreamEncoder.VALUE_KEY;
	
	public StreamFactory(int outputLength, int outputBits) {
		initialize(new StreamEncoder(),outputLength,outputBits);
	}
	
	public StreamFactory(StreamEncoder encoder,int outputLength, int outputBits) {
		initialize(encoder,outputLength,outputBits);
	}
	
	public StreamEncoder getEncoder() {
		return encoder;
	}
	
	public ZStringBuilder getDescription() {
		PoolerConfig poolerConfig = getNewPoolerConfig();
		MemoryConfig memoryConfig = getNewMemoryConfig(poolerConfig);
		ZStringBuilder r = new ZStringBuilder();
		r.append(encoder.getClass().getSimpleName() + " length: " + encoder.length() + ", bits: " + encoder.bits());
		r.append("\n");
		r.append(poolerConfig.getDescription());
		r.append("\n");
		r.append(memoryConfig.getDescription());
		return r;
	}
	
	public PredictionStream getNewPredictionStream(boolean randomizePoolerConnections) {
		return getNewPredictionStream(null,null,randomizePoolerConnections);
	}
	
	public PredictionStream getNewPredictionStream(Messenger msgr, WorkerUnion uni,boolean randomizePoolerConnections) {
		PoolerConfig poolerConfig = getNewPoolerConfig();
		MemoryConfig memoryConfig = getNewMemoryConfig(poolerConfig);
		Pooler pooler = getNewPooler(poolerConfig,randomizePoolerConnections);
		Predictor predictor = new Predictor(memoryConfig);
		return new PredictionStream(msgr,uni,encoder.copy(),pooler,predictor);
	}

	public BufferedPredictionStream getNewBufferedPredictionStream(boolean randomizePoolerConnections) {
		return getNewBufferedPredictionStream(null,null,valueKey,randomizePoolerConnections);
	}

	public BufferedPredictionStream getNewBufferedPredictionStream(String valueKey,boolean randomizePoolerConnections) {
		return getNewBufferedPredictionStream(null,null,valueKey,randomizePoolerConnections);
	}

	public BufferedPredictionStream getNewBufferedPredictionStream(Messenger msgr, WorkerUnion uni,boolean randomizePoolerConnections) {
		return getNewBufferedPredictionStream(null,null,valueKey,randomizePoolerConnections);
	}

	public BufferedPredictionStream getNewBufferedPredictionStream(Messenger msgr, WorkerUnion uni,String valueKey,boolean randomizePoolerConnections) {
		PoolerConfig poolerConfig = getNewPoolerConfig();
		MemoryConfig memoryConfig = getNewMemoryConfig(poolerConfig);
		Pooler pooler = getNewPooler(poolerConfig,randomizePoolerConnections);
		BufferedPredictor predictor = new BufferedPredictor(memoryConfig,valueKey);
		return new BufferedPredictionStream(msgr,uni,encoder.copy(),pooler,predictor);
	}

	public void setPotentialProximalConnections(float potentialProximalConnections) {
		this.potentialProximalConnections = potentialProximalConnections;
	}

	public void setProximalRadius(int proximalRadius) {
		this.proximalRadius = proximalRadius;
	}

	public void setProximalConnectionThreshold(float proximalConnectionThreshold) {
		this.proximalConnectionThreshold = proximalConnectionThreshold;
	}

	public void setProximalConnectionDecrement(float proximalConnectionDecrement) {
		this.proximalConnectionDecrement = proximalConnectionDecrement;
	}

	public void setProximalConnectionIncrement(float proximalConnectionIncrement) {
		this.proximalConnectionIncrement = proximalConnectionIncrement;
	}

	public void setBoostStrength(float boostStrength) {
		this.boostStrength = boostStrength;
	}

	public void setBoostInhibitionRadius(int boostInhibitionRadius) {
		this.boostInhibitionRadius = boostInhibitionRadius;
	}

	public void setBoostActivityLogSize(int boostActivityLogSize) {
		this.boostActivityLogSize = boostActivityLogSize;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	public void setMaxDistalConnectionsPerCell(int maxDistalConnectionsPerCell) {
		this.maxDistalConnectionsPerCell = maxDistalConnectionsPerCell;
	}

	public void setLocalDistalConnectedRadius(int localDistalConnectedRadius) {
		this.localDistalConnectedRadius = localDistalConnectedRadius;
	}

	public void setDistalConnectionThreshold(float distalConnectionThreshold) {
		this.distalConnectionThreshold = distalConnectionThreshold;
	}

	public void setDistalConnectionDecrement(float distalConnectionDecrement) {
		this.distalConnectionDecrement = distalConnectionDecrement;
	}

	public void setDistalConnectionIncrement(float distalConnectionIncrement) {
		this.distalConnectionIncrement = distalConnectionIncrement;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}
	
	protected void initialize(StreamEncoder encoder,int outputLength, int outputBits) {
		this.encoder = encoder;
		this.outputLength = outputLength;
		this.outputBits = outputBits;
	}
	
	protected PoolerConfig getNewPoolerConfig() {
		PoolerConfig r = new PoolerConfig(encoder.length(),outputLength,outputBits);
		r.setPotentialProximalConnections(potentialProximalConnections);
		r.setProximalRadius(proximalRadius);
		r.setProximalConnectionThreshold(proximalConnectionThreshold);
		r.setProximalConnectionIncrement(proximalConnectionIncrement);
		r.setProximalConnectionDecrement(proximalConnectionDecrement);
		r.setBoostStrength(boostStrength);
		r.setBoostInhibitionRadius(boostInhibitionRadius);
		r.setBoostActivityLogSize(boostActivityLogSize);
		return r;
	}

	protected MemoryConfig getNewMemoryConfig(PoolerConfig poolerConfig) {
		MemoryConfig r = new MemoryConfig(poolerConfig);
		r.setDepth(depth);
		r.setMaxDistalConnectionsPerCell(maxDistalConnectionsPerCell);
		r.setLocalDistalConnectedRadius(localDistalConnectedRadius);
		r.setDistalConnectionThreshold(distalConnectionThreshold);
		r.setDistalConnectionIncrement(distalConnectionIncrement);
		r.setDistalConnectionDecrement(distalConnectionDecrement);
		return r;
	}

	protected Pooler getNewPooler(PoolerConfig poolerConfig,boolean randomizePoolerConnections) {
		Pooler r = new Pooler(poolerConfig);
		if (randomizePoolerConnections) {
			r.randomizeConnections();
		}
		return r;
	}
}
