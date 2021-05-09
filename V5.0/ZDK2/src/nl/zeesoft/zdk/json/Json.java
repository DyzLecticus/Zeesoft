package nl.zeesoft.zdk.json;

import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Json {
	private static ScriptEngineManager	scriptEngineMangager	= new ScriptEngineManager();
	private static ScriptEngine			javaScriptEngine		= scriptEngineMangager.getEngineByName("javascript");
	
	public JsonExceptionHandler			exceptionHandler		= null;
	public JElem						root					= null;
	
	public StringBuilder toStringBuilder() {
		StringBuilder r = new StringBuilder();
		if (root!=null) {
			r = root.toStringBuilder();
		}
		return r;
	}
	
	public StringBuilder toStringBuilderReadFormat() {
		StringBuilder r = new StringBuilder();
		if (root!=null) {
			r = root.toStringBuilderReadFormat();
		}
		return r;
	}

	public void fromStringBuilder(StringBuilder str) {
		Map<String,Object> elems = getElements(str);
        root = new JElem();
        root.fromElements(elems);
	}
	
	protected Map<String,Object> getElements(StringBuilder str) {
		Map<String,Object> r = null;
        str.insert(0, "Java.asJSONCompatible(");
        str.append(")");
		try {
			Object result = javaScriptEngine.eval(str.toString());
	        @SuppressWarnings("unchecked")
			Map<String,Object> elems = (Map<String,Object>) result;
	        r = elems;
		} catch (ScriptException ex) {
			handleException(ex);
		}
		return r; 
	}
	
	protected void handleException(Exception ex) {
		if (exceptionHandler!=null) {
			exceptionHandler.handleException(this, ex);
		} else {
			JsonExceptionHandler.handleExceptionDefault(this, ex);
		}
	}
	
	protected static void addStart(StringBuilder str, boolean isArray) {
		if (isArray) {
			str.append("[");
		} else {
			str.append("{");
		}
	}
	
	protected static void addLineFeedIndent(StringBuilder str, int level) {
		str.append("\n");
		for (int i = 0; i < level; i++) {
			str.append("  ");
		}
	}

	protected static void addEnd(StringBuilder str, boolean isArray) {
		if (isArray) {
			str.append("]");
		} else {
			str.append("}");
		}
	}
}
