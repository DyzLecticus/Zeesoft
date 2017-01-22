package nl.zeesoft.zodb.protocol;


public abstract class PtcObject {
	// Session management
	public static final String 	START_SESSION 				= "START_SESSION"; 
	public static final String 	STOP_SESSION 				= "STOP_SESSION"; 
	public static final String 	AUTHORIZE_SESSION 			= "AUTHORIZE_SESSION"; 
	public static final String 	AUTHORIZE_SESSION_SUCCESS 	= "AUTHORIZE_SESSION_SUCCESS"; 
	public static final String 	AUTHORIZE_SESSION_FAILED 	= "AUTHORIZE_SESSION_FAILED"; 

	// Remote server control 
	public static final String 	GET_SERVER_IS_WORKING		= "GET_SERVER_IS_WORKING"; 
	public static final String 	START_SERVER				= "START_SERVER"; 
	public static final String 	STOP_SERVER 				= "STOP_SERVER"; 
	
	public static final String 	GET_BATCH_IS_WORKING		= "GET_BATCH_IS_WORKING"; 
	public static final String 	START_BATCH					= "START_BATCH"; 
	public static final String 	STOP_BATCH 					= "STOP_BATCH"; 

	public static final String 	GET_SERVER_CACHE			= "GET_SERVER_CACHE"; 
	public static final String 	CLEAR_SERVER_CACHE			= "CLEAR_SERVER_CACHE"; 
	
	public static final String 	GET_SERVER_PROPERTIES		= "GET_SERVER_PROPERTIES"; 
	public static final String 	SET_SERVER_PROPERTIES		= "SET_SERVER_PROPERTIES"; 

	public static final String 	STOP_ZODB_PROGRAM			= "STOP_ZODB_PROGRAM"; 

	public abstract StringBuffer processInputAndReturnOutput(StringBuffer input);
}
