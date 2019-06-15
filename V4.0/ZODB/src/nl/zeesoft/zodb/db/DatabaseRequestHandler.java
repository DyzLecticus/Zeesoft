package nl.zeesoft.zodb.db;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.idx.SearchIndex;

public class DatabaseRequestHandler {
	public static final String	RESPONSE_CLOSED		=
		"Database is not open for business right now. Please try again at another time.";
	
	private Database			database			= null;
	private int					maxLenName			= 128;
	private int					maxLenObj			= 32768;
	
	public DatabaseRequestHandler(Database database,int maxLenName,int maxLenObj) {
		this.database = database;
		this.maxLenName = maxLenName;
		this.maxLenObj = maxLenObj;
	}
	
	public DatabaseResponse handleDatabaseRequest(DatabaseRequest request) {
		DatabaseResponse response = new DatabaseResponse();
		response.request = request;
		if (!database.isOpen()) {
			response.statusCode = 503;
			response.errors.add(new ZStringBuilder(RESPONSE_CLOSED));
		} else {
			checkRequest(response);
			if (response.errors.size()==0) {
				if (response.request.type.equals(DatabaseRequest.TYPE_ADD)) {
					database.addObject(request.name,request.object,response.errors);
				} else if (response.request.type.equals(DatabaseRequest.TYPE_GET)) {
					if (request.id>0) {
						IndexElement element = database.getObjectById(request.id);
						if (element!=null) {
							response.results.add(new DatabaseResult(element));
						}
					} else if (request.name.length()>0) {
						IndexElement element = database.getObjectByName(request.name);
						if (element!=null) {
							response.results.add(new DatabaseResult(element));
						}
					} else if (request.index.length()>0) {
						response.resultsFromElements(database.getObjectsUseIndex(request.ascending,request.index,request.invert,request.operator,request.value,request.modAfter,request.modBefore));
					}
					if (request.encoding.length()>0 && response.results.size()>0) {
						for (DatabaseResult res: response.results) {
							ZStringEncoder encoder = new ZStringEncoder(res.object.toStringBuilder());
							if (response.request.encoding.equals(DatabaseRequest.ENC_ASCII)) {
								encoder.encodeAscii();
							} else if (response.request.encoding.equals(DatabaseRequest.ENC_KEY)) {
								encoder.encodeKey(database.getKey(),0);
							}
							res.encoded = encoder;
						}
					}
				} else if (response.request.type.equals(DatabaseRequest.TYPE_LIST)) {
					List<IndexElement> list = null;
					List<Integer> data = new ArrayList<Integer>();
					if (request.index.length()>0) {
						list = database.listObjectsUseIndex(request.start,request.max,request.ascending,request.index,request.invert,request.operator,request.value,request.modAfter,request.modBefore,data);
					} else {
						list = database.listObjects(request.start,request.max,request.modAfter,request.modBefore,data);
					}
					for (IndexElement element: list) {
						DatabaseResult res = new DatabaseResult(element);
						res.object = null;
						response.results.add(res);
					}
					if (data.size()>0) {
						response.size = data.get(0);
					}
				} else if (response.request.type.equals(DatabaseRequest.TYPE_REMOVE)) {
					if (request.index.length()>0) {
						database.removeObjectsUseIndex(request.index,request.invert,request.operator,request.value,request.modAfter,request.modBefore,response.errors);
					} else if (response.request.id>0) {
						database.removeObject(request.id,response.errors);
					}
				} else if (response.request.type.equals(DatabaseRequest.TYPE_SET)) {
					database.setObject(request.id,request.object,response.errors);
					if (request.name.length()>0) {
						database.setObjectName(request.id,request.name,response.errors);
					}
				}
			}
		}
		return response;
	}
	
	private void checkRequest(DatabaseResponse response) {
		if (response.request.name.length()>0) {
			Database.removeSpecialCharacters(response.request.name);
		}
		if (response.request.value.length()>0) {
			Database.removeControlCharacters(response.request.value);
		}
		if (response.request.type.length()==0) {
			response.errors.add(new ZStringBuilder("Request type is mandatory"));
		} else if (
			!response.request.type.equals(DatabaseRequest.TYPE_ADD) &&
			!response.request.type.equals(DatabaseRequest.TYPE_LIST) &&
			!response.request.type.equals(DatabaseRequest.TYPE_GET) &&
			!response.request.type.equals(DatabaseRequest.TYPE_SET) &&
			!response.request.type.equals(DatabaseRequest.TYPE_REMOVE)
			) {
			response.errors.add(new ZStringBuilder("Request type must equal " + DatabaseRequest.TYPE_ADD + ", " + DatabaseRequest.TYPE_LIST + ", " + DatabaseRequest.TYPE_GET + ", " + DatabaseRequest.TYPE_SET + " or " + DatabaseRequest.TYPE_REMOVE));
		} else if (response.request.type.equals(DatabaseRequest.TYPE_ADD)) {
			if (response.request.name.length()==0) {
				response.errors.add(new ZStringBuilder("Request name is mandatory"));
			} else if (response.request.name.length()>maxLenName) {
				response.errors.add(new ZStringBuilder("Request name must be shorter or equal to " + maxLenName));
			}
			checkRequestObjectMandatory(response);
		} else if (response.request.type.equals(DatabaseRequest.TYPE_GET)) {
			if (response.request.id<=0 && response.request.name.length()==0 && response.request.index.length()==0) {
				response.errors.add(new ZStringBuilder("One of request id, name or index is mandatory"));
			}
			checkRequestEncoding(response);
			checkRequestIndex(response);
			checkRequestModAfterModBefore(response);
		} else if (response.request.type.equals(DatabaseRequest.TYPE_LIST)) {
			if (response.request.max<=0) {
				response.errors.add(new ZStringBuilder("Request max is mandatory"));
			}
			checkRequestEncoding(response);
			checkRequestIndex(response);
			checkRequestModAfterModBefore(response);
		} else if (response.request.type.equals(DatabaseRequest.TYPE_REMOVE)) {
			if (response.request.id<=0 && response.request.index.length()==0) {
				response.errors.add(new ZStringBuilder("One of request id or index is mandatory"));
			}
			checkRequestEncoding(response);
			checkRequestIndex(response);
			checkRequestModAfterModBefore(response);
		} else if (response.request.type.equals(DatabaseRequest.TYPE_SET)) {
			if (response.request.id<=0) {
				response.errors.add(new ZStringBuilder("Request id is mandatory"));
			}
			checkRequestObjectMandatory(response);
		}
		if (response.errors.size()>0) {
			response.statusCode = 400;
		}
	}

	private void checkRequestEncoding(DatabaseResponse response) {
		if (response.request.encoding.length()>0) {
			if (!response.request.encoding.equals(DatabaseRequest.ENC_ASCII) &&
				!response.request.encoding.equals(DatabaseRequest.ENC_KEY)
				) {
				response.errors.add(new ZStringBuilder("Request encoding must equal " + DatabaseRequest.ENC_ASCII + " or " + DatabaseRequest.ENC_KEY));
			} else if (response.request.value.length()>0) {
				ZStringEncoder encoder = new ZStringEncoder(response.request.value);
				if (response.request.encoding.equals(DatabaseRequest.ENC_ASCII)) {
					encoder.decodeAscii();
				} else if (response.request.encoding.equals(DatabaseRequest.ENC_KEY)) {
					encoder.decodeKey(database.getKey(),0);
				}
				Database.removeControlCharacters(encoder);
				response.request.value = encoder;
			}
		}
	}

	private void checkRequestEncoded(DatabaseResponse response) {
		if (response.request.encoding.length()>0 && response.request.encoded!=null && response.request.encoded.length()>0) {
			ZStringEncoder encoder = new ZStringEncoder(response.request.encoded);
			if (response.request.encoding.equals(DatabaseRequest.ENC_ASCII)) {
				encoder.decodeAscii();
			} else if (response.request.encoding.equals(DatabaseRequest.ENC_KEY)) {
				encoder.decodeKey(database.getKey(),0);
			}
			JsFile obj = new JsFile();
			obj.fromStringBuilder(encoder);
			if (obj.rootElement!=null && obj.rootElement.children.size()>0) {
				response.request.object = obj;
			} else {
				response.errors.add(new ZStringBuilder("Failed to decode object"));
			}
		}
	}

	private void checkRequestObjectMandatory(DatabaseResponse response) {
		checkRequestEncoding(response);
		checkRequestEncoded(response);
		if (response.request.object==null || response.request.object.rootElement==null) {
			response.errors.add(new ZStringBuilder("Request object is mandatory"));
		} else if (response.request.object.toStringBuilder().length()>maxLenObj) {
			response.errors.add(new ZStringBuilder("Request object must be smaller or equal to " + maxLenObj));
		}
	}
	
	private void checkRequestIndex(DatabaseResponse response) {
		if (response.request.index.length()>0) {
			SearchIndex index = database.getIndexConfig().getListIndex(response.request.index);
			if (index==null) {
				response.errors.add(new ZStringBuilder("Request index does not exist"));
			} else if (response.request.operator.length()>0) {
				if (!index.numeric &&
					!response.request.operator.equals(DatabaseRequest.OP_EQUALS) &&
					!response.request.operator.equals(DatabaseRequest.OP_CONTAINS) &&
					!response.request.operator.equals(DatabaseRequest.OP_STARTS_WITH) &&
					!response.request.operator.equals(DatabaseRequest.OP_ENDS_WITH)
					) {
					response.errors.add(new ZStringBuilder("Request operator must equal " + DatabaseRequest.OP_EQUALS + ", " + DatabaseRequest.OP_CONTAINS + ", " + DatabaseRequest.OP_STARTS_WITH + " or " + DatabaseRequest.OP_ENDS_WITH));
				} else if (index.numeric &&
					!response.request.operator.equals(DatabaseRequest.OP_EQUALS) &&
					!response.request.operator.equals(DatabaseRequest.OP_GREATER) &&
					!response.request.operator.equals(DatabaseRequest.OP_GREATER_OR_EQUAL)
					) {
					response.errors.add(new ZStringBuilder("Request operator must equal " + DatabaseRequest.OP_EQUALS + ", " + DatabaseRequest.OP_GREATER + " or " + DatabaseRequest.OP_GREATER_OR_EQUAL));
				}
				if (response.request.value.length()==0) {
					response.errors.add(new ZStringBuilder("Request value is mandatory"));
				} else if (index.numeric) {
					try {
						new BigDecimal(response.request.value.toCharArray());
					} catch (NumberFormatException e) {
						response.errors.add(new ZStringBuilder("Request value must be numeric"));
					}
				}
			}
		}
	}
	
	private void checkRequestModAfterModBefore(DatabaseResponse response) {
		if (response.request.modAfter>0L && response.request.modBefore>0L && response.request.modAfter>=response.request.modBefore) {
			response.errors.add(new ZStringBuilder("Request modAfter must be lower than modBefore"));
		}
	}
}
