package nl.zeesoft.zdk.htm.stream;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.ClassifierConfig;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.htm.sdr.DateTimeSDR;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class StreamFactory implements JsAble {
	protected StreamEncoder		encoder								= null;
	protected int				outputLength						= 0;
	protected int				outputBits							= 0;

	// Pooler configuration
	protected float				potentialProximalConnections		= 0.75F;
	protected int				proximalRadius						= 5;
	protected float				proximalConnectionThreshold			= 0.1F;
	protected float				proximalConnectionDecrement			= 0.008F;
	protected float				proximalConnectionIncrement			= 0.05F;
	protected int				boostStrength						= 10;
	protected int				boostInhibitionRadius				= 10;
	protected int				boostActivityLogSize				= 100;

	// Memory configuration
	protected int				depth								= 4;
	protected int				maxDistalConnectionsPerCell			= 9999;
	protected int				localDistalConnectedRadius			= 64;
	protected int				minAlmostActiveDistalConnections	= 5;
	protected float				distalConnectionThreshold			= 0.2F;
	protected float				distalConnectionDecrement			= 0.003F;
	protected float				distalConnectionIncrement			= 0.1F;

	// Classifier configuration
	protected List<Integer>		predictSteps						= new ArrayList<Integer>();
	protected String			valueKey							= DateTimeSDR.VALUE_KEY;
	protected String			labelKey							= DateTimeSDR.LABEL_KEY;
	
	// Buffered predictor value key
	//protected String		valueKey							= DateTimeSDR.VALUE_KEY;
	
	public StreamFactory(int outputLength, int outputBits) {
		initialize(new StreamEncoder(),outputLength,outputBits);
	}
	
	public StreamFactory(StreamEncoder encoder,int outputLength, int outputBits) {
		initialize(encoder,outputLength,outputBits);
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem encoderElem = new JsElem("encoder",true);
		json.rootElement.children.add(encoderElem);
		encoderElem.children.add(encoder.toJson().rootElement);
		json.rootElement.children.add(new JsElem("outputLength","" + outputLength));
		json.rootElement.children.add(new JsElem("outputBits","" + outputBits));
		
		// Pooler configuration
		json.rootElement.children.add(new JsElem("potentialProximalConnections","" + potentialProximalConnections));
		json.rootElement.children.add(new JsElem("proximalRadius","" + proximalRadius));
		json.rootElement.children.add(new JsElem("proximalConnectionThreshold","" + proximalConnectionThreshold));
		json.rootElement.children.add(new JsElem("proximalConnectionDecrement","" + proximalConnectionDecrement));
		json.rootElement.children.add(new JsElem("proximalConnectionIncrement","" + proximalConnectionIncrement));
		json.rootElement.children.add(new JsElem("boostStrength","" + boostStrength));
		json.rootElement.children.add(new JsElem("boostInhibitionRadius","" + boostInhibitionRadius));
		json.rootElement.children.add(new JsElem("boostActivityLogSize","" + boostActivityLogSize));
		
		// Memory configuration
		json.rootElement.children.add(new JsElem("depth","" + depth));
		json.rootElement.children.add(new JsElem("depth","" + depth));
		json.rootElement.children.add(new JsElem("maxDistalConnectionsPerCell","" + maxDistalConnectionsPerCell));
		json.rootElement.children.add(new JsElem("localDistalConnectedRadius","" + localDistalConnectedRadius));
		json.rootElement.children.add(new JsElem("minAlmostActiveDistalConnections","" + minAlmostActiveDistalConnections));
		json.rootElement.children.add(new JsElem("distalConnectionThreshold","" + distalConnectionThreshold));
		json.rootElement.children.add(new JsElem("distalConnectionDecrement","" + distalConnectionDecrement));
		json.rootElement.children.add(new JsElem("distalConnectionIncrement","" + distalConnectionIncrement));
		
		// Classifier configuration
		ZStringBuilder pSteps = new ZStringBuilder();
		for (Integer steps: predictSteps) {
			if (pSteps.length()>0) {
				pSteps.append(",");
			}
			pSteps.append("" + steps);
		}
		json.rootElement.children.add(new JsElem("predictSteps",pSteps,true));
		json.rootElement.children.add(new JsElem("valueKey",valueKey,true));
		json.rootElement.children.add(new JsElem("labelKey",labelKey,true));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			JsElem encoderElem = json.rootElement.getChildByName("encoder");
			if (encoderElem!=null) {
				JsFile encoderJs = new JsFile();
				encoderJs.rootElement = encoderElem.children.get(0);
				encoder = new StreamEncoder();
				encoder.fromJson(encoderJs);
			}
			outputLength = json.rootElement.getChildInt("outputLength",outputLength);
			outputBits = json.rootElement.getChildInt("outputBits",outputBits);

			// Pooler configuration
			potentialProximalConnections = json.rootElement.getChildFloat("potentialProximalConnections",potentialProximalConnections);
			proximalRadius = json.rootElement.getChildInt("proximalRadius",proximalRadius);
			proximalConnectionThreshold = json.rootElement.getChildFloat("proximalConnectionThreshold",proximalConnectionThreshold);
			proximalConnectionDecrement = json.rootElement.getChildFloat("proximalConnectionDecrement",proximalConnectionDecrement);
			proximalConnectionIncrement = json.rootElement.getChildFloat("proximalConnectionIncrement",proximalConnectionIncrement);
			boostStrength = json.rootElement.getChildInt("boostStrength",boostStrength);
			boostInhibitionRadius = json.rootElement.getChildInt("boostInhibitionRadius",boostInhibitionRadius);
			proximalRadius = json.rootElement.getChildInt("proximalRadius",proximalRadius);

			// Memory configuration
			depth = json.rootElement.getChildInt("depth",depth);
			maxDistalConnectionsPerCell = json.rootElement.getChildInt("maxDistalConnectionsPerCell",maxDistalConnectionsPerCell);
			localDistalConnectedRadius = json.rootElement.getChildInt("localDistalConnectedRadius",localDistalConnectedRadius);
			minAlmostActiveDistalConnections = json.rootElement.getChildInt("minAlmostActiveDistalConnections",minAlmostActiveDistalConnections);
			distalConnectionThreshold = json.rootElement.getChildFloat("distalConnectionThreshold",distalConnectionThreshold);
			distalConnectionDecrement = json.rootElement.getChildFloat("distalConnectionDecrement",distalConnectionDecrement);
			distalConnectionIncrement = json.rootElement.getChildFloat("distalConnectionIncrement",distalConnectionIncrement);
			
			// Classifier configuration
			ZStringBuilder pSteps = json.rootElement.getChildZStringBuilder("predictSteps");
			predictSteps.clear();
			if (pSteps.length()>0) {
				List<ZStringBuilder> pElems = pSteps.split(",");
				for (ZStringBuilder pStep: pElems) {
					predictSteps.add(Integer.parseInt(pStep.toString()));
				}
			}
			valueKey = json.rootElement.getChildString("valueKey",valueKey);
			labelKey = json.rootElement.getChildString("labelKey",labelKey);
		}
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
		if (predictSteps.size()>0) {
			ZStringBuilder pSteps = new ZStringBuilder();
			for (Integer steps: predictSteps) {
				if (pSteps.length()>0) {
					pSteps.append(", ");
				}
				pSteps.append("" + steps);
			}
			r.append("\n");
			r.append("Classifier prediction steps: " + pSteps);
		}
		return r;
	}
	
	public DefaultStream getNewDefaultStream(boolean randomizePoolerConnections) {
		return getNewDefaultStream(null,null,randomizePoolerConnections);
	}
	
	public DefaultStream getNewDefaultStream(Messenger msgr, WorkerUnion uni,boolean randomizePoolerConnections) {
		PoolerConfig poolerConfig = getNewPoolerConfig();
		MemoryConfig memoryConfig = getNewMemoryConfig(poolerConfig);
		Pooler pooler = getNewPooler(poolerConfig,randomizePoolerConnections);
		Memory memory = getNewMemory(memoryConfig);
		return new DefaultStream(msgr,uni,encoder.copy(),pooler,memory);
	}
	
	public ClassificationStream getNewClassificationStream(boolean randomizePoolerConnections) {
		return getNewClassificationStream(null,null,randomizePoolerConnections);
	}
	
	public ClassificationStream getNewClassificationStream(Messenger msgr, WorkerUnion uni,boolean randomizePoolerConnections) {
		PoolerConfig poolerConfig = getNewPoolerConfig();
		MemoryConfig memoryConfig = getNewMemoryConfig(poolerConfig);
		ClassifierConfig classifierConfig = getNewClassifierConfig();
		Pooler pooler = getNewPooler(poolerConfig,randomizePoolerConnections);
		Memory memory = getNewMemory(memoryConfig);
		Classifier classifier = getNewClassifier(classifierConfig);
		return new ClassificationStream(msgr,uni,encoder.copy(),pooler,memory,classifier);
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

	public void setBoostStrength(int boostStrength) {
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

	public void setMinAlmostActiveDistalConnections(int minAlmostActiveDistalConnections) {
		this.minAlmostActiveDistalConnections = minAlmostActiveDistalConnections;
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

	public List<Integer> getPredictSteps() {
		return predictSteps;
	}

	public void setValueKey(String valueKey) {
		this.valueKey = valueKey;
	}

	public void setLabelKey(String labelKey) {
		this.labelKey = labelKey;
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
		r.setProximalConnectionDecrement(proximalConnectionDecrement);
		r.setProximalConnectionIncrement(proximalConnectionIncrement);
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
		r.setMinAlmostActiveDistalConnections(minAlmostActiveDistalConnections);
		r.setDistalConnectionThreshold(distalConnectionThreshold);
		r.setDistalConnectionDecrement(distalConnectionDecrement);
		r.setDistalConnectionIncrement(distalConnectionIncrement);
		return r;
	}

	protected ClassifierConfig getNewClassifierConfig() {
		ClassifierConfig r = null;
		int steps = 0;
		if (predictSteps.size()>0) {
			steps = predictSteps.get(0);
		}
		r = new ClassifierConfig(steps);
		if (predictSteps.size()>1) {
			for (int i = 1; i<predictSteps.size(); i++) {
				r.addPredictSteps(predictSteps.get(i));
			}
		}
		r.setValueKey(valueKey);
		r.setLabelKey(labelKey);
		return r;
	}

	protected Pooler getNewPooler(PoolerConfig poolerConfig,boolean randomizePoolerConnections) {
		Pooler r = new Pooler(poolerConfig);
		if (randomizePoolerConnections) {
			r.randomizeConnections();
		}
		return r;
	}

	protected Memory getNewMemory(MemoryConfig memoryConfig) {
		return new Memory(memoryConfig);
	}

	protected Classifier getNewClassifier(ClassifierConfig classifierConfig) {
		return new Classifier(classifierConfig);
	}
}
