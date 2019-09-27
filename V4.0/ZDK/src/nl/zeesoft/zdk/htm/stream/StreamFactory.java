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

	protected boolean		randomizePoolerConnections		= true;
	protected int			scale							= 1;

	protected PoolerConfig	poolerConfig					= null;
	protected MemoryConfig	memoryConfig					= null;
	
	public StreamFactory(int outputLength, int outputBits) {
		initialize(new StreamEncoder(),outputLength,outputBits);
	}
	
	public StreamFactory(StreamEncoder encoder,int outputLength, int outputBits) {
		initialize(encoder,outputLength,outputBits);
	}
	
	public void setScale(int scale) {
		if (scale>0) {
			this.scale = scale;
			encoder.setScale(scale);
			initializeConfig();
		}
	}
	
	public void setRandomizePoolerConnections(boolean randomizePoolerConnections) {
		this.randomizePoolerConnections = randomizePoolerConnections;
	}
	
	public PoolerConfig getPoolerConfig() {
		return poolerConfig;
	}
	
	public MemoryConfig getMemoryConfig() {
		return memoryConfig;
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append(encoder.getClass().getSimpleName() + " length: " + encoder.length() + ", bits: " + encoder.bits());
		r.append("\n");
		r.append(poolerConfig.getDescription());
		r.append("\n");
		r.append(memoryConfig.getDescription());
		return r;
	}
	
	public PredictionStream getNewPredictionStream() {
		return getNewPredictionStream(null,null);
	}
	
	public PredictionStream getNewPredictionStream(Messenger msgr, WorkerUnion uni) {
		Pooler pooler = getNewPooler();
		Predictor predictor = new Predictor(memoryConfig);
		return new PredictionStream(msgr,uni,encoder,pooler,predictor);
	}

	public BufferedPredictionStream getNewBufferedPredictionStream() {
		return getNewBufferedPredictionStream(null,null,null);
	}

	public BufferedPredictionStream getNewBufferedPredictionStream(String valueKey) {
		return getNewBufferedPredictionStream(null,null,valueKey);
	}
	
	public BufferedPredictionStream getNewBufferedPredictionStream(Messenger msgr, WorkerUnion uni,String valueKey) {
		Pooler pooler = getNewPooler();
		BufferedPredictor predictor = new BufferedPredictor(memoryConfig,valueKey);
		return new BufferedPredictionStream(msgr,uni,encoder,pooler,predictor);
	}
	
	protected void initialize(StreamEncoder encoder,int outputLength, int outputBits) {
		this.encoder = encoder;
		this.outputLength = outputLength;
		this.outputBits = outputBits;
		initializeConfig();
	}
	
	protected void initializeConfig() {
		poolerConfig = new PoolerConfig(encoder.length(),outputLength * scale,outputBits * scale);
		memoryConfig = new MemoryConfig(poolerConfig);
	}
	
	protected Pooler getNewPooler() {
		Pooler r = new Pooler(poolerConfig);
		if (randomizePoolerConnections) {
			r.randomizeConnections();
		}
		return r;
	}
}
