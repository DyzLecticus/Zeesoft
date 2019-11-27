package nl.zeesoft.zdk.htm.stream;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.proc.Classifier;
import nl.zeesoft.zdk.htm.proc.ClassifierConfig;
import nl.zeesoft.zdk.htm.proc.Memory;
import nl.zeesoft.zdk.htm.proc.MemoryConfig;
import nl.zeesoft.zdk.htm.proc.Pooler;
import nl.zeesoft.zdk.htm.proc.PoolerConfig;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

/**
 * A StreamFactory provides an easy way to configure and instantiate most commonly used SDR streams. 
 * See PoolerConfig, MemoryConfig and ClassifierConfig for processor specific configuration details.
 */
public class StreamFactory implements JsAble {
	protected StreamEncoder		encoder				= null;
	protected PoolerConfig		poolerConfig		= null;
	protected MemoryConfig		memoryConfig		= null;
	protected ClassifierConfig	classifierConfig	= null;
	
	public StreamFactory(int outputLength, int outputBits) {
		initialize(new StreamEncoder(),outputLength,outputBits);
	}
	
	public StreamFactory(StreamEncoder encoder,int outputLength, int outputBits) {
		initialize(encoder,outputLength,outputBits);
	}
	
	public StreamEncoder getEncoder() {
		return encoder;
	}
	
	public PoolerConfig getPoolerConfig() {
		return poolerConfig;
	}
	
	public MemoryConfig getMemoryConfig() {
		return memoryConfig;
	}
	
	public ClassifierConfig getClassifierConfig() {
		return classifierConfig;
	}
	
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append(encoder.getDescription());
		r.append("\n");
		r.append(poolerConfig.getDescription());
		r.append("\n");
		r.append(memoryConfig.getDescription());
		r.append("\n");
		r.append(classifierConfig.getDescription());
		return r;
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		JsElem encoderElem = new JsElem("encoder",true);
		json.rootElement.children.add(encoderElem);
		encoderElem.children.add(encoder.toJson().rootElement);
		
		JsElem pcfgElem = new JsElem("poolerConfig",true);
		json.rootElement.children.add(pcfgElem);
		pcfgElem.children.add(poolerConfig.toJson().rootElement);

		JsElem mcfgElem = new JsElem("memoryConfig",true);
		json.rootElement.children.add(mcfgElem);
		mcfgElem.children.add(memoryConfig.toJson().rootElement);

		JsElem ccfgElem = new JsElem("classifierConfig",true);
		json.rootElement.children.add(ccfgElem);
		ccfgElem.children.add(classifierConfig.toJson().rootElement);

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
			
			JsElem pcfgElem = json.rootElement.getChildByName("poolerConfig");
			if (pcfgElem!=null && pcfgElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = pcfgElem.children.get(0);
				poolerConfig.fromJson(js);
			}

			JsElem mcfgElem = json.rootElement.getChildByName("memoryConfig");
			if (mcfgElem!=null && mcfgElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = mcfgElem.children.get(0);
				memoryConfig.fromJson(js);
			}

			JsElem ccfgElem = json.rootElement.getChildByName("classifierConfig");
			if (ccfgElem!=null && ccfgElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = ccfgElem.children.get(0);
				classifierConfig.fromJson(js);
			}
		}
	}
	
	/**
	 * Returns a new DefaultStream.
	 * 
	 * @param randomizePoolerConnections Indicates the pooler connections should be initialized randomly
	 * @return A new DefaultStream
	 */
	public DefaultStream getNewDefaultStream(boolean randomizePoolerConnections) {
		return getNewDefaultStream(null,null,randomizePoolerConnections);
	}
	
	/**
	 * Returns a new DefaultStream.
	 * 
	 * @param msgr The messenger
	 * @param uni The worker union
	 * @param randomizePoolerConnections Indicates the spatial pooler connections should be initialized randomly
	 * @return A new DefaultStream
	 */
	public DefaultStream getNewDefaultStream(Messenger msgr, WorkerUnion uni,boolean randomizePoolerConnections) {
		PoolerConfig poolerConfig = getNewPoolerConfig();
		MemoryConfig memoryConfig = getNewMemoryConfig(poolerConfig);
		Pooler pooler = getNewPooler(poolerConfig,randomizePoolerConnections);
		Memory memory = getNewMemory(memoryConfig);
		return new DefaultStream(msgr,uni,encoder.copy(),pooler,memory);
	}
	
	/**
	 * Returns a new ClassificationStream.
	 * 
	 * @param randomizePoolerConnections Indicates the spatial pooler connections should be initialized randomly
	 * @return A new ClassificationStream
	 */
	public ClassificationStream getNewClassificationStream(boolean randomizePoolerConnections) {
		return getNewClassificationStream(null,null,randomizePoolerConnections);
	}
	
	/**
	 * Returns a new ClassificationStream.
	 * 
	 * @param msgr The messenger
	 * @param uni The worker union
	 * @param randomizePoolerConnections Indicates the spatial pooler connections should be initialized randomly
	 * @return A new ClassificationStream
	 */
	public ClassificationStream getNewClassificationStream(Messenger msgr, WorkerUnion uni,boolean randomizePoolerConnections) {
		PoolerConfig poolerConfig = getNewPoolerConfig();
		MemoryConfig memoryConfig = getNewMemoryConfig(poolerConfig);
		ClassifierConfig classifierConfig = getNewClassifierConfig();
		Pooler pooler = getNewPooler(poolerConfig,randomizePoolerConnections);
		Memory memory = getNewMemory(memoryConfig);
		Classifier classifier = getNewClassifier(classifierConfig);
		return new ClassificationStream(msgr,uni,encoder.copy(),pooler,memory,classifier);
	}
	
	protected void initialize(StreamEncoder encoder,int outputLength, int outputBits) {
		this.encoder = encoder;
		this.poolerConfig = new PoolerConfig(encoder.length(),outputLength,outputBits);
		this.memoryConfig = new MemoryConfig(poolerConfig);
		this.classifierConfig = new ClassifierConfig();
	}
	
	protected PoolerConfig getNewPoolerConfig() {
		return poolerConfig.copy();
	}

	protected MemoryConfig getNewMemoryConfig(PoolerConfig poolerConfig) {
		return memoryConfig.copy();
	}

	protected ClassifierConfig getNewClassifierConfig() {
		return classifierConfig.copy();
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
