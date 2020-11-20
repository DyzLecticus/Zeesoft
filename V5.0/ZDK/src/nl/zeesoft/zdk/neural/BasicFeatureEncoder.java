package nl.zeesoft.zdk.neural;

public class BasicFeatureEncoder extends BasicScalarEncoder {
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
	
	@Override
	public void setEncodeDimensions(int sizeX, int sizeY) {
		setDerivedValues();
	}
	
	@Override
	public void setOnBits(int onBits) {
		super.setOnBits(onBits);
		setDerivedValues();
	}

	@Override
	public void setMinValue(float minValue) {
		setDerivedValues();
	}

	@Override
	public void setMaxValue(float maxValue) {
		setDerivedValues();
	}

	@Override
	public void setResolution(float resolution) {
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
