package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

/**
 * Member states. 
 */
public class MemberState {
	public static final String			UNKNOWN 			= "UNKNOWN";
	public static final String			ONLINE 				= "ONLINE";
	public static final String			GOING_OFFLINE		= "GOING_OFFLINE";
	public static final String			DRAINING_OFFLINE	= "DRAINING_OFFLINE";
	public static final String			OFFLINE 			= "OFFLINE";
	public static final String			COMING_ONLINE		= "COMING_ONLINE";
	public static final String			STOPPING 			= "STOPPING";
	public static final String			RESTARTING 			= "RESTARTING";
	
	private static List<MemberState>	states 				= new ArrayList<MemberState>();
	
	private String 						code 				= "";
	
	public MemberState(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
	
	public static MemberState getState(String code) {
		MemberState r = null;
		if (states.size()==0) {
			initializeDefaultStates();
		}
		if (states.size()>0) {
			for (MemberState state: states) {
				if (state.getCode().equals(code)) {
					r = state;
					break;
				}
			}
		}
		return r;
	}

	public static void initializeStates(List<String> addStates) {
		for (String state: addStates) {
			states.add(new MemberState(state));
		}
		initializeDefaultStates();
	}
	
	private static void initializeDefaultStates() {
		states.add(new MemberState(UNKNOWN));
		states.add(new MemberState(ONLINE));
		states.add(new MemberState(GOING_OFFLINE));
		states.add(new MemberState(DRAINING_OFFLINE));
		states.add(new MemberState(OFFLINE));
		states.add(new MemberState(COMING_ONLINE));
		states.add(new MemberState(STOPPING));
		states.add(new MemberState(RESTARTING));
	}
}
