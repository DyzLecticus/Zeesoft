package nl.zeesoft.zdk.htm.proc;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.htm.util.SDR;
import nl.zeesoft.zdk.htm.util.SDRMap;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

/**
 * A PoolerConfig is used to configure a single spatial pooler.
 * The configuration cannot be changed once it has been used to instantiate a spatial pooler.
 * The input and output length are automatically translated to a 2D (or 3D single layer) input and output SDR space.
 */
public class PoolerConfig extends ProcessorConfigObject {
	protected int			inputLength						= 100;
	protected int			outputLength					= 100;
	protected int			outputBits						= 2;
	
	protected float			potentialProximalConnections	= 0.75F;
	protected int			proximalRadius					= 5;
	protected float			proximalConnectionThreshold		= 0.1F;
	protected float			proximalConnectionDecrement		= 0.008F;
	protected float			proximalConnectionIncrement		= 0.05F;
	
	protected int			boostStrength					= 10;
	protected int			boostInhibitionRadius			= 10;
	protected int			boostActivityLogSize			= 100;

	protected int			inputSizeX						= 0;
	protected int			inputSizeY						= 0;
	protected int			outputSizeX						= 0;
	protected int			outputSizeY						= 0;

	public PoolerConfig() {
		calculateDimensions();
	}

	public PoolerConfig(int inputLength, int outputLength, int outputBits) {
		this.inputLength = inputLength;
		this.outputLength = outputLength;
		this.outputBits = outputBits;
		calculateDimensions();
	}

	@Override
	public PoolerConfig copy() {
		PoolerConfig r = new PoolerConfig(inputLength,outputLength,outputBits);
		r.inputSizeX = inputSizeX;
		r.inputSizeY = inputSizeY;
		r.outputSizeX = outputSizeX;
		r.outputSizeY = outputSizeY;
		r.potentialProximalConnections = potentialProximalConnections;
		r.proximalRadius = proximalRadius;
		r.proximalConnectionThreshold = proximalConnectionThreshold;
		r.proximalConnectionDecrement = proximalConnectionDecrement;
		r.proximalConnectionIncrement = proximalConnectionIncrement;
		r.boostStrength = boostStrength;
		r.boostInhibitionRadius = boostInhibitionRadius;
		r.boostActivityLogSize = boostActivityLogSize;
		return r;
	}
	
	/**
	 * Sets the potential proximal connections.
	 * Used by spatial pooler connection randomization to initialize proximal links.
	 * 
	 * @param potentialProximalConnections The potential proximal connections
	 */
	public void setPotentialProximalConnections(float potentialProximalConnections) {
		if (!initialized) {
			this.potentialProximalConnections = potentialProximalConnections;
		}
	}
	
	/**
	 * Sets the proximal radius for column proximal connections.
	 * Used to limit and map columns to the encoder input space.
	 * Use a short radius when 2D topology is important.
	 * 
	 * @param proximalRadius The proximal radius
	 */
	public void setProximalRadius(int proximalRadius) {
		if (!initialized) {
			this.proximalRadius = proximalRadius;
		}
	}

	/**
	 * Sets the proximal connection (permanence) threshold.
	 * 
	 * @param proximalConnectionThreshold The connection threshold
	 */
	public void setProximalConnectionThreshold(float proximalConnectionThreshold) {
		if (!initialized) {
			this.proximalConnectionThreshold = proximalConnectionThreshold;
		}
	}

	/**
	 * Sets the proximal connection (permanence) decrement.
	 * 
	 * @param proximalConnectionDecrement The connection decrement
	 */
	public void setProximalConnectionDecrement(float proximalConnectionDecrement) {
		if (!initialized) {
			this.proximalConnectionDecrement = proximalConnectionDecrement;
		}
	}

	/**
	 * Sets the proximal connection (permanence) increment.
	 * 
	 * @param proximalConnectionIncrement The connection increment
	 */
	public void setProximalConnectionIncrement(float proximalConnectionIncrement) {
		if (!initialized) {
			this.proximalConnectionIncrement = proximalConnectionIncrement;
		}
	}

	/**
	 * Sets the boost strength.
	 * Boosting greatly improves column activation distribution.
	 * 
	 * @param boostStrength The boost strength
	 */
	public void setBoostStrength(int boostStrength) {
		if (!initialized) {
			this.boostStrength = boostStrength;
		}
	}

	/**
	 * Sets the boost inhibition radius.
	 * Used to limit the range of inhibition between pooler columns.
	 * Use a short radius when 2D topology is important.
	 * 
	 * @param boostInhibitionRadius The boost inhibition radius
	 */
	public void setBoostInhibitionRadius(int boostInhibitionRadius) {
		if (!initialized) {
			this.boostInhibitionRadius = boostInhibitionRadius;
		}
	}

	/**
	 * Sets the boost activity log size.
	 * A long log size will make the spatial pooler more stable but also less responsive to changes.
	 * 
	 * @param boostActivityLogSize The boost activity log size
	 */
	public void setBoostActivityLogSize(int boostActivityLogSize) {
		if (!initialized) {
			this.boostActivityLogSize = boostActivityLogSize;
		}
	}
	
	/**
	 * Specifies the exact 2D input space dimensions. 
	 * 
	 * @param sizeX The x axis size
	 * @param sizeY The y axis size
	 */
	public void setInputDimensions(int sizeX,int sizeY) {
		if (!initialized && sizeX * sizeY == inputLength) {
			inputSizeX = sizeX;
			inputSizeY = sizeY;
		}
	}
	
	/**
	 * Specifies the exact 2D output space dimensions. 
	 * 
	 * @param sizeX The x axis size
	 * @param sizeY The y axis size
	 */
	public void setOutputDimensions(int sizeX,int sizeY) {
		if (!initialized && sizeX * sizeY == outputLength) {
			outputSizeX = sizeX;
			outputSizeY = sizeY;
		}
	}
	
	/**
	 * Returns a description of this configuration.
	 * 
	 * @return A description
	 */
	public ZStringBuilder getDescription() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("Pooler input dimensions: ");
		r.append("" + inputSizeX);
		r.append("*");
		r.append("" + inputSizeY);
		r.append(", output dimensions: ");
		r.append("" + outputSizeX);
		r.append("*");
		r.append("" + outputSizeY);
		return r;
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		
		json.rootElement.children.add(new JsElem("inputLength","" + inputLength));
		json.rootElement.children.add(new JsElem("outputLength","" + outputLength));
		json.rootElement.children.add(new JsElem("outputBits","" + outputBits));
		json.rootElement.children.add(new JsElem("inputSizeX","" + inputSizeX));
		json.rootElement.children.add(new JsElem("inputSizeY","" + inputSizeY));
		json.rootElement.children.add(new JsElem("outputSizeX","" + outputSizeX));
		json.rootElement.children.add(new JsElem("outputSizeY","" + outputSizeY));
		
		json.rootElement.children.add(new JsElem("potentialProximalConnections","" + potentialProximalConnections));
		json.rootElement.children.add(new JsElem("proximalRadius","" + proximalRadius));
		json.rootElement.children.add(new JsElem("proximalConnectionThreshold","" + proximalConnectionThreshold));
		json.rootElement.children.add(new JsElem("proximalConnectionDecrement","" + proximalConnectionDecrement));
		json.rootElement.children.add(new JsElem("proximalConnectionIncrement","" + proximalConnectionIncrement));
		json.rootElement.children.add(new JsElem("boostStrength","" + boostStrength));
		json.rootElement.children.add(new JsElem("boostInhibitionRadius","" + boostInhibitionRadius));
		json.rootElement.children.add(new JsElem("boostActivityLogSize","" + boostActivityLogSize));
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (!initialized && json.rootElement!=null) {
			inputLength = json.rootElement.getChildInt("inputLength",inputLength);
			outputLength = json.rootElement.getChildInt("outputLength",outputLength);
			outputBits = json.rootElement.getChildInt("outputBits",outputBits);
			inputSizeX = json.rootElement.getChildInt("inputSizeX",inputSizeX);
			inputSizeY = json.rootElement.getChildInt("inputSizeY",inputSizeY);
			outputSizeX = json.rootElement.getChildInt("outputSizeX",outputSizeX);
			outputSizeY = json.rootElement.getChildInt("outputSizeY",outputSizeY);
			
			potentialProximalConnections = json.rootElement.getChildFloat("potentialProximalConnections",potentialProximalConnections);
			proximalRadius = json.rootElement.getChildInt("proximalRadius",proximalRadius);
			proximalConnectionThreshold = json.rootElement.getChildFloat("proximalConnectionThreshold",proximalConnectionThreshold);
			proximalConnectionDecrement = json.rootElement.getChildFloat("proximalConnectionDecrement",proximalConnectionDecrement);
			proximalConnectionIncrement = json.rootElement.getChildFloat("proximalConnectionIncrement",proximalConnectionIncrement);
			boostStrength = json.rootElement.getChildInt("boostStrength",boostStrength);
			boostInhibitionRadius = json.rootElement.getChildInt("boostInhibitionRadius",boostInhibitionRadius);
			boostActivityLogSize = json.rootElement.getChildInt("boostActivityLogSize",boostActivityLogSize);
		}
	}
	
	/**
	 * Returns a new SDR based on the output length of the spatial pooler.
	 * 
	 * @return A new SDR
	 */
	public SDR getNewSDR() {
		return new SDR(outputLength);
	}
	
	/**
	 * Returns a new SDR map based on the output length and bits of the spatial pooler.
	 * 
	 * @return A new SDR map
	 */
	public SDRMap getNewSDRMap() {
		return new SDRMap(outputLength,outputBits);
	}

	public int getInputLength() {
		return inputLength;
	}

	public int getOutputLength() {
		return outputLength;
	}

	public int getOutputBits() {
		return outputBits;
	}

	public float getPotentialProximalConnections() {
		return potentialProximalConnections;
	}

	public int getProximalRadius() {
		return proximalRadius;
	}

	public float getProximalConnectionThreshold() {
		return proximalConnectionThreshold;
	}

	public float getProximalConnectionDecrement() {
		return proximalConnectionDecrement;
	}

	public float getProximalConnectionIncrement() {
		return proximalConnectionIncrement;
	}

	public int getBoostStrength() {
		return boostStrength;
	}

	public int getBoostInhibitionRadius() {
		return boostInhibitionRadius;
	}

	public int getBoostActivityLogSize() {
		return boostActivityLogSize;
	}

	public int getInputSizeX() {
		return inputSizeX;
	}

	public int getInputSizeY() {
		return inputSizeY;
	}

	public int getOutputSizeX() {
		return outputSizeX;
	}

	public int getOutputSizeY() {
		return outputSizeY;
	}

	protected void calculateDimensions() {
		inputSizeX = (int) Math.sqrt(inputLength);
		inputSizeY = inputSizeX;
		if (inputSizeX * inputSizeY < inputLength) {
			inputSizeX += 1;
		}
		if (inputSizeX * inputSizeY < inputLength) {
			inputSizeY += 1;
		}
		outputSizeX = (int) Math.sqrt(outputLength);
		outputSizeY = outputSizeX;
		if (outputSizeX * outputSizeY < outputLength) {
			outputSizeX += 1;
		}
		if (outputSizeX * outputSizeY < outputLength) {
			outputSizeY += 1;
		}
	}
}
