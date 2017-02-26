package nl.zeesoft.zjmo.orchestra;

import java.util.ArrayList;
import java.util.List;

public class MemberState {
	public static final String			UNKNOWN 		= "UNKNOWN";
	public static final String			ONLINE 			= "ONLINE";
	
	private static List<MemberState>	states 			= new ArrayList<MemberState>();
	
	private String 						code 			= "";
	
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
	}
}
