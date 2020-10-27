package nl.zeesoft.zdk.neural;

public class SDREncoder {
	protected int	encodeSizeX		= 16;
	protected int	encodeSizeY		= 16;
	protected int	onBits			= 16;
	
	public int getEncodeSizeX() {
		return encodeSizeX;
	}

	public int getEncodeSizeY() {
		return encodeSizeY;
	}

	public void setEncodeDimensions(int encodeSizeX, int encodeSizeY) {
		this.encodeSizeX = encodeSizeX;
		this.encodeSizeY = encodeSizeY;
	}

	public int getOnBits() {
		return onBits;
	}
	
	public void setOnBits(int onBits) {
		this.onBits = onBits;
	}
	
	public SDR getEncodedValue(Object value) {
		return getNewSDR();
	}
	
	public float getBuckets() {
		return getEncodeLength() - onBits;
	}
	
	public int getEncodeLength() {
		return encodeSizeX * encodeSizeY;
	}
	
	protected SDR getNewSDR() {
		return new SDR(encodeSizeX,encodeSizeY);
	}
}
