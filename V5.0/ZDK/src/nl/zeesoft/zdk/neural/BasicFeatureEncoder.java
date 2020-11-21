package nl.zeesoft.zdk.neural;

public class BasicFeatureEncoder extends AbstractScalarEncoder {
	protected int	features = 3;

	public BasicFeatureEncoder() {
		onBits = 2;
		setDerivedValues();
	}

	public void setFeatures(int features) {
		this.features = features;
		setDerivedValues();
	}

	public int getFeatures() {
		return features;
	}
		
	public void setOnBits(int onBits) {
		this.onBits = onBits;
		setDerivedValues();
	}
	
	protected void setDerivedValues() {
		encodeSizeX = onBits;
		encodeSizeY = features;
		minValue = 0;
		maxValue = features - 1;
		resolution = 1;
	}
}
