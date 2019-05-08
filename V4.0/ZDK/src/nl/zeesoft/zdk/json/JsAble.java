package nl.zeesoft.zdk.json;

/**
 * Interface for objects that can be converted to and from JSON.
 */
public interface JsAble {
	public JsFile toJson();
	public void fromJson(JsFile json);
}
