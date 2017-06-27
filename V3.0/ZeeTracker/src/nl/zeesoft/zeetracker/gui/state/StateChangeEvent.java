package nl.zeesoft.zeetracker.gui.state;

public class StateChangeEvent extends StateObject {
	public static final String	SELECTED_TAB				= "SELECTED_TAB";
	public static final String	SELECTED_INSTRUMENT			= "SELECTED_INSTRUMENT";
	public static final String	CHANGED_SHOW_INSTRUMENT_FX	= "CHANGED_SHOW_INSTRUMENT_FX";
	public static final String	CHANGED_PATTERN_EDIT_MODE	= "CHANGED_PATTERN_EDIT_MODE";
	public static final String	SELECTED_PATTERN			= "SELECTED_PATTERN";
	public static final String	CHANGED_PATTERN_SELECTION	= "CHANGED_PATTERN_SELECTION";
	public static final String	CHANGED_SEQUENCE_SELECTION	= "CHANGED_SEQUENCE_SELECTION";
	public static final String	CHANGED_SETTINGS			= "CHANGED_SETTINGS";
	public static final String	CHANGED_COMPOSITION			= "CHANGED_COMPOSITION";
	public static final String	CHANGED_COMPOSITION_STATE	= "CHANGED_COMPOSITION_STATE";

	private String				type						= "";
	private Object				source						= "";
	
	public StateChangeEvent(String type,Object source) {
		super(null);
		this.type = type;
		this.source = source;
	}
	
	public String getType() {
		return type;
	}

	public Object getSource() {
		return source;
	}
}
