package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.http.HttpConnection;

public class MockHttpConnection extends HttpConnection {
	
	@Override
	public boolean createIO(boolean mockException) {
		return super.createIO(mockException);
	}
	
	@Override
	public boolean close() {
		return super.close();
	}

	@Override
	public boolean isOpen() {
		return super.isOpen();
	}
	
	@Override
	protected boolean destroyIO() {
		return false;
	}

	@Override
	public boolean destroyReaderAndSocket(boolean mockException) {
		return super.destroyReaderAndSocket(mockException);
	}

	@Override
	public boolean readLine(StringBuilder input, boolean mockException) {
		return super.readLine(input, mockException);
	}
	
	public void setOpen(boolean open) {
		this.open = open;
	}
}
