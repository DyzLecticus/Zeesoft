package nl.zeesoft.zodb.protocol;

import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.client.ClRequest;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbControlServer;
import nl.zeesoft.zodb.database.DbIndex;
import nl.zeesoft.zodb.database.DbSessionServer;
import nl.zeesoft.zodb.database.query.QryFetch;
import nl.zeesoft.zodb.database.query.QryFetchCondition;
import nl.zeesoft.zodb.database.query.QryFetchList;
import nl.zeesoft.zodb.database.query.QryTransaction;
import nl.zeesoft.zodb.database.query.QryUpdate;
import nl.zeesoft.zodb.file.XMLFile;
import nl.zeesoft.zodb.model.MdlObject;
import nl.zeesoft.zodb.model.datatypes.DtBoolean;
import nl.zeesoft.zodb.model.datatypes.DtString;
import nl.zeesoft.zodb.model.datatypes.DtStringBuffer;
import nl.zeesoft.zodb.model.impl.DbSession;
import nl.zeesoft.zodb.model.impl.DbUser;

public class PtcServer extends PtcObject{
	private DbSession 	session 				= null;
	private int			authorizationAttempts	= 0;

	public PtcServer(DbSession s) {
		session = s;
	}

	@Override
	public StringBuffer processInputAndReturnOutput(StringBuffer input) {
		StringBuffer output = new StringBuffer();
		if ((input!=null) && (input.length()>0)) {
    		if (Generic.stringBufferStartsWith(input,PtcObject.STOP_SESSION)) {
    			output.append(PtcObject.STOP_SESSION);
				if (this instanceof PtcServerControl) {
					DbControlServer.getInstance().stopSession(session.getId().getValue());
				} else {
					DbSessionServer.getInstance().stopSession(session.getId().getValue());
				}
    		} else if (Generic.stringBufferStartsWith(input,AUTHORIZE_SESSION)) {
				List<StringBuffer> vals = Generic.getValuesFromStringBuffer(Generic.stringBufferReplace(input,"\n",""));
				boolean succes = false;
				if ((vals.size()>1) && (vals.get(1).length()>0)) {
					DbUser admin = DbConfig.getInstance().getModel().getAdminUser(this);
					
					StringBuffer name = vals.get(1);
					StringBuffer password = new StringBuffer();
					if (!DbConfig.getInstance().isEncrypt()) {
						StringBuffer userNamePassword = Generic.decodeKey(name, DbConfig.getInstance().getEncryptionKey(), getSession().getId().getValue());
						List<StringBuffer> unp = Generic.getValuesFromStringBuffer(userNamePassword);
						name = unp.get(0);
						if (unp.size()>1) {
							password = unp.get(1);
						}
						name = unp.get(0);
					} else if ((vals.size()>2) && (vals.get(2).length()>0)) {
						password = vals.get(2);
					}
					
					QryFetch q = new QryFetch(new DbUser().getClassName().getValue());
					DtString getName = new DtString(name.toString());
					DtStringBuffer getPassword = new DtStringBuffer(new StringBuffer(password));
					
					DtBoolean getActive = new DtBoolean(true);
					q.addCondition(new QryFetchCondition(MdlObject.PROPERTY_NAME,QryFetchCondition.OPERATOR_EQUALS,getName));
					q.addCondition(new QryFetchCondition("password",QryFetchCondition.OPERATOR_EQUALS,getPassword));
					q.addCondition(new QryFetchCondition("active",QryFetchCondition.OPERATOR_EQUALS,getActive));
					if (this instanceof PtcServerControl) {
						DtBoolean getAdmin = new DtBoolean(true);
						q.addCondition(new QryFetchCondition("admin",QryFetchCondition.OPERATOR_EQUALS,getAdmin));
					}
					DbIndex.getInstance().executeFetch(q,admin,this);
					
					if (q.getMainResults().getReferences().size()>0) {
						DbUser user = (DbUser) q.getMainResults().getReferences().get(0).getDataObject();
						session.setDbUser(user);
			    		QryUpdate qu = new QryUpdate(session);
			    		QryTransaction t = new QryTransaction(admin);
			    		t.addQuery(qu);
			    		DbIndex.getInstance().executeTransaction(t, this);
			    		output.append(AUTHORIZE_SESSION_SUCCESS);
			    		output.append(Generic.SEP_STR);
			    		output.append(user.getId());
			    		output.append(Generic.SEP_STR);
			    		output.append(user.getName());
			    		output.append(Generic.SEP_STR);
			    		output.append(user.getAdmin());
			    		output.append(Generic.SEP_STR);
			    		output.append(user.getLevel());
						succes = true;
					}
				}
				if (!succes) {
					output.append(AUTHORIZE_SESSION_FAILED);
					authorizationAttempts++;
					if (authorizationAttempts>=3) {
						Messenger.getInstance().error(this, "Failed authorization attempts: " + authorizationAttempts + ", stopping session: " + session.getId());
						DbSessionServer.getInstance().stopSession(session.getId().getValue());
					}
				}
			} else if (Generic.stringBufferStartsWith(input,"<")) {
				XMLFile f = new XMLFile();
				String err = f.parseXML(input);
				if (err.length()==0) {
					if (f.getRootElement().getName().equals("request")) {
						ClRequest r = null;
						try {
							r = ClRequest.fromXml(f);
						} catch (Exception e) {
							Messenger.getInstance().error(this, "Exception while parsing request: " + e.getMessage());
							output.append(STOP_SESSION);
						}
						if (r!=null) {
							Exception e = null;
							try { 
								if (r.getQueryRequest()!=null) {
									if (session.getDbUser()==null) {
										Messenger.getInstance().error(this, "Unauthorized request: " + r.getId() + ", from: " + session.getIpAndPort());
										r.setActionResponse("Unauthorized request: " + r.getId() + ", from: " + session.getIpAndPort());
									} else {
										if (r.getQueryRequest() instanceof QryTransaction) {
											QryTransaction t = (QryTransaction) r.getQueryRequest();
											t.setUser(session.getDbUser());
											e = DbIndex.getInstance().executeTransaction(t, this);
										} else if (r.getQueryRequest() instanceof QryFetchList) {
											QryFetchList fl = (QryFetchList) r.getQueryRequest();
											fl.setUser(session.getDbUser());
											e = DbIndex.getInstance().executeFetchList(fl, this);
										}
									}
								} else if (!r.getActionRequest().equals("")) {
									if (
										(session.getDbUser()==null) &&
										(
											(r.getActionRequest().trim().startsWith(GET_SERVER_IS_WORKING)) ||
											(r.getActionRequest().trim().startsWith(STOP_SERVER)) ||
											(r.getActionRequest().trim().startsWith(START_SERVER)) ||
											(r.getActionRequest().trim().startsWith(GET_BATCH_IS_WORKING)) ||
											(r.getActionRequest().trim().startsWith(STOP_BATCH)) ||
											(r.getActionRequest().trim().startsWith(START_BATCH)) ||
											(r.getActionRequest().trim().startsWith(GET_SERVER_CACHE)) ||
											(r.getActionRequest().trim().startsWith(CLEAR_SERVER_CACHE)) ||
											(r.getActionRequest().trim().startsWith(GET_SERVER_PROPERTIES)) ||
											(r.getActionRequest().trim().startsWith(SET_SERVER_PROPERTIES)) ||
											(r.getActionRequest().trim().startsWith(STOP_ZODB_PROGRAM))
										)
										) {
										Messenger.getInstance().error(this, "Unauthorized request: " + r.getId() + ", from: " + session.getIpAndPort());
										r.setActionResponse("Unauthorized request: " + r.getId() + ", from: " + session.getIpAndPort());
										r.setError(true);
									} else {
										//Messenger.getInstance().debug(this, "Proces action request: " + r.getActionRequest() + ", from: " + session.getIpAndPort());
										StringBuffer response = processInputAndReturnOutput(new StringBuffer(r.getActionRequest()));
										if (response!=null) {
											r.setActionResponse(response.toString());
										} else {
											r.setActionResponse(null);
										}
									}
								}
							} catch (Exception ex) {
								e = ex;
							}
							if (e!=null) {
								String callStack = Generic.getCallStackString(e.getStackTrace(),"");
								String msg = "Exception while executing request: " + e.toString() + "\n" + callStack;
								r.setActionResponse(msg);
								r.setError(true);
							}
							if ((r.getQueryRequest()!=null) && (!DbConfig.getInstance().getXmlCompression().equals(DbConfig.XML_COMPRESSION_NONE))) {
								if (DbConfig.getInstance().getXmlCompression().equals(DbConfig.XML_COMPRESSION_FULL)) {
									XMLFile rx = ClRequest.toXml(r);
									rx.setCompressNumerics(true);
									output = rx.toStringCompressed();
									rx.cleanUp();
								} else if (DbConfig.getInstance().getXmlCompression().equals(DbConfig.XML_COMPRESSION_TAGS)) {
									XMLFile rx = ClRequest.toXml(r);
									rx.setCompressNumerics(false);
									output = rx.toStringCompressed();
									rx.cleanUp();
								}
							} else {
								if (r.getActionResponse()!=null) {
									output = ClRequest.toXml(r).toStringBuffer();
								} else {
									output = null;
								}
							}
						}
					} else {
						Messenger.getInstance().error(this, "Unknown request type: " + f.getRootElement().getName());
						output.append(STOP_SESSION);
					}
				} else {
					Messenger.getInstance().error(this, "Failed to parse XML: " + err);
					output.append(STOP_SESSION);
				}
				f.cleanUp();
			}
		}
		
		return output;
	}
	
	protected DbSession getSession() {
		return session;
	}
}
