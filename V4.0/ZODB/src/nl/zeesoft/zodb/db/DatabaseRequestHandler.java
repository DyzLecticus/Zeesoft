package nl.zeesoft.zodb.db;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;

import nl.zeesoft.zdk.ZStringBuilder;

public class DatabaseRequestHandler {
	public static final String	RESPONSE_CLOSED		=
		"Database is not open for business right now. Please try again at another time.";
	
	private Database			database			= null;
	
	public DatabaseRequestHandler(Database database) {
		this.database = database;
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
					database.addObject(request.name,request.obj,response.errors);
				} else if (response.request.type.equals(DatabaseRequest.TYPE_GET)) {
					if (request.id>0) {
						IndexElement element = database.getObjectById(request.id);
						if (element!=null) {
							response.results.add(element.toResult());
						}
					} else if (request.name.length()>0) {
						IndexElement element = database.getObjectByName(request.name);
						if (element!=null) {
							response.results.add(element.toResult());
						}
					} else if (request.startsWith.length()>0) {
						List<IndexElement> elements = database.getObjectsByNameStartsWith(request.startsWith);
						for (IndexElement element: elements) {
							response.results.add(element.toResult());
						}
					} else if (request.contains.length()>0) {
						List<IndexElement> elements = database.getObjectsByNameContains(request.contains);
						for (IndexElement element: elements) {
							response.results.add(element.toResult());
						}
					}
				} else if (response.request.type.equals(DatabaseRequest.TYPE_LIST)) {
					SortedMap<String,Long> list = null;
					if (request.startsWith.length()>0) {
						list = database.listObjectsThatStartWith(request.startsWith,request.start,request.max);
					} else if (request.contains.length()>0) {
						list = database.listObjectsThatContain(request.contains,request.start,request.max);
					} else {
						list = database.listObjects(request.start,request.max);
					}
					for (Entry<String,Long> entry: list.entrySet()) {
						DatabaseResult res = new DatabaseResult();
						res.name = entry.getKey();
						res.id = entry.getValue();
						response.results.add(res);
					}
				} else if (response.request.type.equals(DatabaseRequest.TYPE_REMOVE)) {
					if (response.request.id>0) {
						database.removeObject(request.id,response.errors);
					} else if (response.request.startsWith.length()>0) {
						database.removeObjectsThatStartWith(response.request.startsWith,response.errors);
					} else if (response.request.contains.length()>0) {
						database.removeObjectsThatContain(response.request.contains,response.errors);
					}
				} else if (response.request.type.equals(DatabaseRequest.TYPE_SET)) {
					database.setObject(request.id,request.obj,response.errors);
					if (request.name.length()>0) {
						database.setObjectName(request.id,request.name,response.errors);
					}
				}
			}
		}
		return response;
	}
	
	private void checkRequest(DatabaseResponse response) {
		if (response.request.type.length()==0) {
			response.errors.add(new ZStringBuilder("Request type is mandatory"));
		} else if (response.request.type.equals(DatabaseRequest.TYPE_ADD)) {
			if (response.request.name.length()==0) {
				response.errors.add(new ZStringBuilder("Request name is mandatory"));
			} else if (response.request.obj==null || response.request.obj.rootElement==null) {
				response.errors.add(new ZStringBuilder("Request object is mandatory"));
			}
		} else if (response.request.type.equals(DatabaseRequest.TYPE_GET)) {
			if (response.request.id<=0 && response.request.name.length()==0 && response.request.startsWith.length()==0 && response.request.contains.length()==0) {
				response.errors.add(new ZStringBuilder("Request id, name, startsWith or contains is mandatory"));
			}
		} else if (response.request.type.equals(DatabaseRequest.TYPE_LIST)) {
			if (response.request.max<=0) {
				response.errors.add(new ZStringBuilder("Request max is mandatory"));
			}
		} else if (response.request.type.equals(DatabaseRequest.TYPE_REMOVE)) {
			if (response.request.id<=0 && response.request.startsWith.length()==0 && response.request.contains.length()==0) {
				response.errors.add(new ZStringBuilder("Request id, startsWith or contains is mandatory"));
			}
		} else if (response.request.type.equals(DatabaseRequest.TYPE_SET)) {
			if (response.request.id<=0) {
				response.errors.add(new ZStringBuilder("Request id is mandatory"));
			} else if (response.request.obj==null || response.request.obj.rootElement==null) {
				response.errors.add(new ZStringBuilder("Request object is mandatory"));
			}
		}
		if (response.errors.size()>0) {
			response.statusCode = 400;
		}
	}
}
