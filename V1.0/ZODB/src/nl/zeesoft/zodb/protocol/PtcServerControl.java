package nl.zeesoft.zodb.protocol;

import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.batch.BtcBatchWorker;
import nl.zeesoft.zodb.database.DbCache;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbControlServer;
import nl.zeesoft.zodb.database.DbControlServerWorker;
import nl.zeesoft.zodb.database.DbSessionServerWorker;
import nl.zeesoft.zodb.model.impl.DbSession;

public class PtcServerControl extends PtcServer {
	
	public PtcServerControl(DbSession s) {
		super(s);
	}

	@Override
	public StringBuffer processInputAndReturnOutput(StringBuffer input) {
		StringBuffer output = new StringBuffer();
		
		if ((input!=null) && (input.length()>0)) {
			output = super.processInputAndReturnOutput(input);
			if ((output==null) || (output.length()==0)) {
				// Check user just to be sure
				if ((getSession().getDbUser()!=null) && 
					(getSession().getDbUser().getAdmin().getValue())) {
					try {
						if (Generic.stringBufferStartsWith(input,GET_SERVER_IS_WORKING)) {
							output.append(GET_SERVER_IS_WORKING);
							output.append(Generic.SEP_STR);
							output.append(serverIsWorking());
						} else if (Generic.stringBufferStartsWith(input,STOP_SERVER)) {
							output.append(GET_SERVER_IS_WORKING);
							output.append(Generic.SEP_STR);
							output.append(stopServer());
						} else if (Generic.stringBufferStartsWith(input,START_SERVER)) {
							output.append(GET_SERVER_IS_WORKING);
							output.append(Generic.SEP_STR);
							output.append(startServer());
						} else if (Generic.stringBufferStartsWith(input,GET_BATCH_IS_WORKING)) {
							output.append(GET_BATCH_IS_WORKING);
							output.append(Generic.SEP_STR);
							output.append(batchIsWorking());
						} else if (Generic.stringBufferStartsWith(input,STOP_BATCH)) {
							boolean working = stopBatch();
							DbConfig.getInstance().setStartBatches(working);
							DbConfig.getInstance().serialize();
							output.append(GET_BATCH_IS_WORKING);
							output.append(Generic.SEP_STR);
							output.append(working);
						} else if (Generic.stringBufferStartsWith(input,START_BATCH)) {
							boolean working = startBatch();
							DbConfig.getInstance().setStartBatches(working);
							DbConfig.getInstance().serialize();
							output.append(GET_BATCH_IS_WORKING);
							output.append(Generic.SEP_STR);
							output.append(working);
						} else if (Generic.stringBufferStartsWith(input,SET_SERVER_PROPERTIES)) {
							List<String> objs = Generic.getObjectsFromString(Generic.stringBufferReplace(input,"\n","").toString());
							String[] props = Generic.getValuesFromString(objs.get(1));
							String[] vals = Generic.getValuesFromString(objs.get(2));
							boolean serializeConfig = false;
							boolean restartServer = false;
							boolean restartControlServer = false;
							boolean disconnectControlClient = false;
							boolean encrypt = DbConfig.getInstance().isEncrypt();
							for (int p = 0; p < props.length; p++) {
								String name = props[p];
								String val = vals[p];
								if (name.equals(DbConfig.PROPERTY_PORT)) {
									try {
										int i = Integer.parseInt(val);
										if (DbConfig.getInstance().getPort()!=i) {
											DbConfig.getInstance().setPort(i);
											serializeConfig = true;
											restartServer = true;
											disconnectControlClient = true;
											restartControlServer = true;
										}
									} catch (NumberFormatException e) {
										// Ignore
									}
								} else if (name.equals(DbConfig.PROPERTY_MAX_SESSIONS)) {
									try {
										int i = Integer.parseInt(val);
										if (DbConfig.getInstance().getMaxSessions()!=i) {
											DbConfig.getInstance().setMaxSessions(i);
											serializeConfig = true;
											restartServer = true;
										}
									} catch (NumberFormatException e) {
										// Ignore
									}
								} else if (name.equals(DbConfig.PROPERTY_DEBUG)) {
									boolean b = Boolean.parseBoolean(val);
									if (DbConfig.getInstance().isDebug()!=b) {
										DbConfig.getInstance().setDebug(b);
										serializeConfig = true;
									}
								} else if (name.equals(DbConfig.PROPERTY_DEBUG_CLASS_PREFIX)) {
									if (!DbConfig.getInstance().getDebugClassPrefix().equals(val)) {
										DbConfig.getInstance().setDebugClassPrefix(val);
										serializeConfig = true;
									}
								} else if (name.equals(DbConfig.PROPERTY_START_BATCH)) {
									boolean b = Boolean.parseBoolean(val);
									if (DbConfig.getInstance().isStartBatches()!=b) {
										DbConfig.getInstance().setStartBatches(b);
										serializeConfig = true;
									}
								} else if (name.equals(DbConfig.PROPERTY_REMOTE_BACKUP_DIR)) {
									if (!DbConfig.getInstance().getRemoteBackupDir().equals(val)) {
										DbConfig.getInstance().setRemoteBackupDir(val);
										serializeConfig = true;
									}
								} else if (name.equals(DbConfig.PROPERTY_ENCRYPT)) {
									encrypt = Boolean.parseBoolean(val);
									if (DbConfig.getInstance().isEncrypt()!=encrypt) {
										serializeConfig = true;
										restartServer = true;
										disconnectControlClient = true;
									}
								} else if (name.equals(DbConfig.PROPERTY_XML_COMPRESSION)) {
									if (!DbConfig.getInstance().getXmlCompression().equals(val)) {
										DbConfig.getInstance().setXmlCompression(val);
										serializeConfig = true;
									}
								} else if (name.equals(DbConfig.PROPERTY_CACHE)) {
									boolean b = Boolean.parseBoolean(val);
									if (DbConfig.getInstance().isCache()!=b) {
										DbConfig.getInstance().setCache(b);
										serializeConfig = true;
									}
								} else if (name.equals(DbConfig.PROPERTY_CACHE_SIZE)) {
									try {
										int i = Integer.parseInt(val);
										if (DbConfig.getInstance().getCacheSize()!=i) {
											DbConfig.getInstance().setCacheSize(i);
											serializeConfig = true;
										}
									} catch (NumberFormatException e) {
										// Ignore
									}
								} else if (name.equals(DbConfig.PROPERTY_CACHE_PERSIST_SIZE)) {
									try {
										int i = Integer.parseInt(val);
										if (DbConfig.getInstance().getCachePersistSize()!=i) {
											DbConfig.getInstance().setCachePersistSize(i);
											serializeConfig = true;
										}
									} catch (NumberFormatException e) {
										// Ignore
									}
								} else if (name.equals(DbConfig.PROPERTY_MAX_FETCH_LOAD)) {
									try {
										int i = Integer.parseInt(val);
										if (DbConfig.getInstance().getMaxFetchLoad()!=i) {
											DbConfig.getInstance().setMaxFetchLoad(i);
											serializeConfig = true;
										}
									} catch (NumberFormatException e) {
										// Ignore
									}
								} else if (name.equals(DbConfig.PROPERTY_MAX_FETCH_RESULTS)) {
									try {
										int i = Integer.parseInt(val);
										if (DbConfig.getInstance().getMaxFetchResults()!=i) {
											DbConfig.getInstance().setMaxFetchResults(i);
											serializeConfig = true;
										}
									} catch (NumberFormatException e) {
										// Ignore
									}
								} else if (name.equals(DbConfig.PROPERTY_PRELOAD_SPEED)) {
									try {
										int i = Integer.parseInt(val);
										if (DbConfig.getInstance().getPreloadSpeed()!=i) {
											DbConfig.getInstance().setPreloadSpeed(i);
											serializeConfig = true;
										}
									} catch (NumberFormatException e) {
										// Ignore
									}
								}
							}
							if (restartServer) {
								stopServer();
							}
							if (disconnectControlClient) {
								DbControlServer.getInstance().stopSession(getSession().getId().getValue());
								if (DbConfig.getInstance().isEncrypt()!=encrypt) {
									DbConfig.getInstance().setEncrypt(encrypt);
								}
								output.append(PtcObject.STOP_SESSION);
							} else {
								output.append(PtcObject.SET_SERVER_PROPERTIES);
							}
							if (restartControlServer) {
								DbControlServerWorker.getInstance().stop();
								DbControlServerWorker.getInstance().start();
							}
							if (restartServer) {
								startServer();
							}
							if (serializeConfig) {
								DbConfig.getInstance().serialize();
							}
						} else if (Generic.stringBufferStartsWith(input,GET_SERVER_PROPERTIES)) {
							StringBuffer objs = new StringBuffer();
							String[] props = Generic.getValuesFromString(Generic.getObjectsFromString(Generic.stringBufferReplace(input,"\n","").toString()).get(1));
							for (int p = 0; p < props.length; p++) {
								String name = props[p];
								if (objs.length()>0) {
									objs.append(Generic.SEP_STR);
								}
								if (name.equals(DbConfig.PROPERTY_PORT)) {
									objs.append(DbConfig.getInstance().getPort());
								} else if (name.equals(DbConfig.PROPERTY_MAX_SESSIONS)) {
									objs.append(DbConfig.getInstance().getMaxSessions());
								} else if (name.equals(DbConfig.PROPERTY_DEBUG)) {
									objs.append(DbConfig.getInstance().isDebug());
								} else if (name.equals(DbConfig.PROPERTY_DEBUG_CLASS_PREFIX)) {
									objs.append(DbConfig.getInstance().getDebugClassPrefix());
								} else if (name.equals(DbConfig.PROPERTY_START_BATCH)) {
									objs.append(DbConfig.getInstance().isStartBatches());
								} else if (name.equals(DbConfig.PROPERTY_REMOTE_BACKUP_DIR)) {
									objs.append(DbConfig.getInstance().getRemoteBackupDir());
								} else if (name.equals(DbConfig.PROPERTY_ENCRYPT)) {
									objs.append(DbConfig.getInstance().isEncrypt());
								} else if (name.equals(DbConfig.PROPERTY_XML_COMPRESSION)) {
									objs.append(DbConfig.getInstance().getXmlCompression());
								} else if (name.equals(DbConfig.PROPERTY_CACHE)) {
									objs.append(DbConfig.getInstance().isCache());
								} else if (name.equals(DbConfig.PROPERTY_CACHE_SIZE)) {
									objs.append(DbConfig.getInstance().getCacheSize());
								} else if (name.equals(DbConfig.PROPERTY_CACHE_PERSIST_SIZE)) {
									objs.append(DbConfig.getInstance().getCachePersistSize());
								} else if (name.equals(DbConfig.PROPERTY_MAX_FETCH_LOAD)) {
									objs.append(DbConfig.getInstance().getMaxFetchLoad());
								} else if (name.equals(DbConfig.PROPERTY_MAX_FETCH_RESULTS)) {
									objs.append(DbConfig.getInstance().getMaxFetchResults());
								} else if (name.equals(DbConfig.PROPERTY_PRELOAD_SPEED)) {
									objs.append(DbConfig.getInstance().getPreloadSpeed());
								}
							}
							output.append(input);
							output.append(Generic.SEP_OBJ);
							output.append(objs);
						} else if (Generic.stringBufferStartsWith(input,GET_SERVER_CACHE)) {
							output.append(GET_SERVER_CACHE);
							output.append(Generic.SEP_STR);
							output.append(DbCache.getInstance().getSize(this));
						} else if (Generic.stringBufferStartsWith(input,CLEAR_SERVER_CACHE)) {
							DbCache.getInstance().clearCache(this);
							output.append(GET_SERVER_CACHE);
							output.append(Generic.SEP_STR);
							output.append(DbCache.getInstance().getSize(this));
						} else if (Generic.stringBufferStartsWith(input,STOP_ZODB_PROGRAM)) {
							stopZODBProgram();
							output = null;
						}
					} catch (Exception e) {
						String callStack = Generic.getCallStackString(e.getStackTrace(),"");
						String msg =  "An error occured while processing input: " + input + ", error: " + e.getMessage() + "\n" + callStack;
						Messenger.getInstance().error(this,msg);
						output.append(msg);
					}
				} else {
					if (getSession().getDbUser()==null) {
						Messenger.getInstance().error(this,"Unauthorized action request: " + input + ", for user: " + getSession().getDbUser());
					} else {
						Messenger.getInstance().error(this,"Unauthorized action request: " + input + ", for user: " + getSession().getDbUser().getName().getValue());
					}
				}
			}
		}
		
		return output;
	}
	
	private boolean serverIsWorking() {
		return DbSessionServerWorker.getInstance().isWorking();
	}

	protected boolean stopServer() {
		if (serverIsWorking()) {
			Messenger.getInstance().debug(this, "Server stop requested by session: " + getSession().getId());
			DbSessionServerWorker.getInstance().stop();
		}
		return serverIsWorking();
	}

	private boolean startServer() {
		stopServer();
		if (!serverIsWorking()) {
			Messenger.getInstance().debug(this, "Server start requested by session: " + getSession().getId());
			DbSessionServerWorker.getInstance().start();
			while (!DbSessionServerWorker.getInstance().isWorking()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
			}
		}
		return serverIsWorking();
	}

	private boolean batchIsWorking() {
		return BtcBatchWorker.getInstance().isWorking();
	}
	
	protected boolean stopBatch() {
		if (batchIsWorking()) {
			Messenger.getInstance().debug(this, "Batch stop requested by session: " + getSession().getId());
			BtcBatchWorker.getInstance().stop();
		}
		return batchIsWorking();
	}

	private boolean startBatch() {
		stopBatch();
		if (!batchIsWorking()) {
			Messenger.getInstance().debug(this, "Batch start requested by session: " + getSession().getId());
			BtcBatchWorker.getInstance().start();
			while (!BtcBatchWorker.getInstance().isWorking()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					//e.printStackTrace();
				}
			}
		}
		return batchIsWorking();
	}

	private void stopZODBProgram() {
		Messenger.getInstance().debug(this, "Program stop requested by session: " + getSession().getId());
		PtcServerControlStopProgramWorker worker = new PtcServerControlStopProgramWorker(this);
		worker.start();
	}
}
