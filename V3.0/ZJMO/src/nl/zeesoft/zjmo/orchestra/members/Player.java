package nl.zeesoft.zjmo.orchestra.members;

import nl.zeesoft.zjmo.orchestra.MemberObject;

public class Player extends MemberObject {
	private String 	conductorIpAddressOrHostName	= "localhost";
	private int		conductorControlPort			= 5433;
	private int		conductorWorkPort				= 5432;
}
