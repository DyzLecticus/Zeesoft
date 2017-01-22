package nl.zeesoft.zodb.client;

import java.net.Socket;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.protocol.PtcClient;
import nl.zeesoft.zodb.protocol.PtcObject;

public class ClControlSession extends ClSession {
	public ClControlSession(Socket sock, PtcClient prot) {
		super(sock, prot);
	}

	public ClRequest getNewServerIsWorkingRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.GET_SERVER_IS_WORKING);
		return r;
	}

	public ClRequest getNewStopServerRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.STOP_SERVER);
		return r;
	}

	public ClRequest getNewStartServerRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.START_SERVER);
		return r;
	}

	public ClRequest getNewBatchIsWorkingRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.GET_BATCH_IS_WORKING);
		return r;
	}

	public ClRequest getNewStopBatchRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.STOP_BATCH);
		return r;
	}

	public ClRequest getNewStartBatchRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.START_BATCH);
		return r;
	}

	public ClRequest getNewGetServerCacheRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.GET_SERVER_CACHE);
		return r;
	}

	public ClRequest getNewClearServerCacheRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.CLEAR_SERVER_CACHE);
		return r;
	}

	public ClRequest getNewServerGetPropertiesRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		String request = PtcObject.GET_SERVER_PROPERTIES + Generic.SEP_OBJ;
		int num = 0;
		for (String prop: DbConfig.PROPERTIES) {
			request = request + prop;
			num++;
			if (num<(DbConfig.PROPERTIES.length)) {
				request = request + Generic.SEP_STR;
			}
		}
		r.setActionRequest(request);
		return r;
	}

	public ClRequest getNewServerSetPropertiesRequest(String values[]) {
		if (values.length!=DbConfig.PROPERTIES.length) {
			return getNewServerGetPropertiesRequest();
		}
		ClRequest r = getRequestQueue().getNewRequest(this);
		String request = PtcObject.SET_SERVER_PROPERTIES + Generic.SEP_OBJ;
		int num = 0;
		for (String prop: DbConfig.PROPERTIES) {
			request = request + prop;
			num++;
			if (num<(DbConfig.PROPERTIES.length)) {
				request = request + Generic.SEP_STR;
			}
		}
		request = request + Generic.SEP_OBJ;
		num = 0;
		for (String value: values) {
			request = request + value;
			num++;
			if (num<(DbConfig.PROPERTIES.length)) {
				request = request + Generic.SEP_STR;
			}
		}
		r.setActionRequest(request);
		return r;
	}

	public ClRequest getNewStopZODBProgramRequest() {
		ClRequest r = getRequestQueue().getNewRequest(this);
		r.setActionRequest(PtcObject.STOP_ZODB_PROGRAM);
		return r;
	}

}
