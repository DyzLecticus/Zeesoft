package nl.zeesoft.zdbd;

import java.util.HashMap;

public interface StateListener {
	public void changedState(String name, HashMap<String,Object> params);
}
