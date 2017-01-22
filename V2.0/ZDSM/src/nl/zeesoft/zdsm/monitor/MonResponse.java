package nl.zeesoft.zdsm.monitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLSocketFactory;

import nl.zeesoft.zdsm.database.model.Domain;
import nl.zeesoft.zdsm.database.model.Response;
import nl.zeesoft.zdsm.database.model.Service;
import nl.zeesoft.zdsm.database.model.ZDSMModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;

public class MonResponse {
	private ReqAdd					addResponseRequest	= new ReqAdd(ZDSMModel.RESPONSE_CLASS_FULL_NAME);
	
	private Domain					domain				= null;
	private Service					service				= null;
	private Response				response			= new Response();
	
	public MonResponse(Domain domain,Service service) {
		this.domain = domain;
		this.service = service;
		response.setDomain(domain);
		response.setService(service);
	}
	
	public void work() {
		Date now = new Date();
		StringBuilder errorLog = new StringBuilder();
		StringBuilder responseText = getResponseText(errorLog);
		if (errorLog.length()>0) {
			String code = "ERR";
			response.setCode(code);
			response.setBody(errorLog);
		} else {
			parseResponseText(responseText);
		}
		determineSuccess();
		response.setDateTime((new Date()).getTime());
		response.setDuration(response.getDateTime() - now.getTime());
		if (service.getId()>0) {
			addResponseRequest.getObjects().add(new ReqDataObject(response.toDataObject()));
			DbRequestQueue.getInstance().addRequest(addResponseRequest,this);
		}
	}

	/**
	 * @return the domain
	 */
	protected Domain getDomain() {
		return domain;
	}
	
	/**
	 * @return the service
	 */
	protected Service getService() {
		return service;
	}
	
	/**
	 * @return the response
	 */
	public Response getResponse() {
		return response;
	}

	private void determineSuccess() {
		StringBuilder body = new StringBuilder();
		StringBuilder expectedBody = new StringBuilder(service.getExpectedBody());
		if (expectedBody.length()>0) {
			body = new StringBuilder(response.getBody());
			body = Generic.stringBuilderReplace(body, "&lt;", "<");
			body = Generic.stringBuilderReplace(body, "&gt;", ">");
			expectedBody = Generic.stringBuilderReplace(expectedBody, "&lt;", "<");
			expectedBody = Generic.stringBuilderReplace(expectedBody, "&gt;", ">");
		}
		
		if (
			(service.getExpectedCode().length()==0 || response.getCode().equals(service.getExpectedCode())) &&
			(service.getExpectedHeader().length()==0 || Generic.stringBuilderEquals(response.getHeader(),service.getExpectedHeader())) &&
			(expectedBody.length()==0 || Generic.stringBuilderEquals(body,expectedBody)) 
			) {
			response.setSuccess(true);
		}
	}
	
	private void parseResponseText(StringBuilder responseText) {
		List<StringBuilder> lines = Generic.stringBuilderSplit(responseText,"\n");
		String code = "";
		StringBuilder header = new StringBuilder();
		StringBuilder body = new StringBuilder();
		StringBuilder firstLine = null;
		if (lines.size()>0) {
			firstLine = lines.get(0);
			List<StringBuilder> words = Generic.stringBuilderSplit(firstLine," ");
			if (words.size()>0 && words.get(0).length()>0) {
				code = words.get(0).toString();
				if (code.startsWith("HTTP") && words.get(0).length()>1) {
					code = words.get(1).toString();
				}
			}
			int l = 0;
			for (StringBuilder line: lines) {
				if (l>0) {
					if (header.length()>0 && line.length()==0) {
						break;
					} else if (line.length()>0 && !line.equals("\r")) {
						header.append(line);
						header.append("\n");
					}
				}
				l++;
			}
			body = new StringBuilder(responseText.substring(firstLine.length() + header.length() + 2));
			body = Generic.stringBuilderReplace(body, "<", "&lt;");
			body = Generic.stringBuilderReplace(body, ">", "&gt;");
		}
		response.setCode(code);
		response.setHeader(header);
		response.setBody(body);
	}
	
	private StringBuilder getResponseText(StringBuilder errorLog) {
		StringBuilder responseText = new StringBuilder();
		
		String pathQuery = service.getPath();
		if (!pathQuery.startsWith("/")) {
			pathQuery = "/" + pathQuery;
		}
		if (service.getQuery().length()>0) {
			if (!Generic.stringBuilderStartsWith(service.getQuery(),"?")) {
				pathQuery += "?";
			}
			pathQuery += service.getQuery();
		}
		
		StringBuilder writeBody = new StringBuilder(service.getBody());
		writeBody = Generic.stringBuilderReplace(writeBody, "&lt;", "<");
		writeBody = Generic.stringBuilderReplace(writeBody, "&gt;", ">");
		
		StringBuilder header = new StringBuilder();
		if (domain.isAddHeaderHost()) {
			header.append("Host: ");
			header.append(domain.getAddress());
			if (domain.isAddHeaderHostPort()) {
				header.append(":");
				header.append(domain.getPort());
			}
			header.append("\n");
		}
		if (domain.isAddHeaderAuthBasic()) {
			StringBuilder auth = new StringBuilder(domain.getUserName());
			auth.append(":");
			auth.append(domain.getUserPassword());
			header.append("Authorization: Basic ");
			header.append(Generic.base64Encode(auth));
			header.append("\n");
		}
		header.append("Connection: close\n");
		if (writeBody.length()>0) {
			header.append("Content-Length");
			header.append(": ");
			header.append(writeBody.length());
			header.append("\n");
		}
		if (service.getHeader().length()>0) {
			for (StringBuilder head: Generic.stringBuilderSplit(service.getHeader(),"\n")) {
				if ((head.length()>0) &&
					(!Generic.stringBuilderStartsWith(head,"Connection:")) &&
					(!domain.isAddHeaderAuthBasic() || !Generic.stringBuilderStartsWith(head,"Authorization:")) &&
					(!domain.isAddHeaderHost() || !Generic.stringBuilderStartsWith(head,"Host:")) &&
					(!Generic.stringBuilderStartsWith(head,"Content-Length:"))
					) {
					header.append(head);
					header.append("\n");
				}
			}
		}
		
		StringBuilder saveBody = new StringBuilder(service.getBody());
		saveBody = Generic.stringBuilderReplace(saveBody, "<", "&lt;");
		saveBody = Generic.stringBuilderReplace(saveBody, ">", "&gt;");
		
		String protocol = "http";
		if (domain.isHttps()) {
			protocol = "https";
		}
		
		response.setMethod(service.getMethod());
		response.setFullUrl(protocol + "://" + domain.getAddress() + ":" + domain.getPort() + pathQuery);
		response.setRequestHeader(header);
		response.setRequestBody(saveBody);
		
		header = Generic.stringBuilderReplace(header,"\n","\r\n");
		
		//Messenger.getInstance().debug(this,service.getMethod() + " " + response.getFullUrl());
		
		StringBuilder request = new StringBuilder();
		if (domain.isAddDomainToPath()) {
			request.append(service.getMethod() + " " + protocol + "://" + domain.getAddress() + ":" + domain.getPort() + pathQuery + " HTTP/1.1");
		} else {
			request.append(service.getMethod() + " " + pathQuery + " HTTP/1.1");
		}
		request.append("\r\n");
		if (header.length()>0) {
			request.append(header.toString());
			if (!Generic.stringBuilderEndsWith(header,"\r\n")) {
				request.append("\r\n");
			}
		}
		if (writeBody.length()>0) {
			request.append("\r\n");
			request.append(writeBody.toString());
			if (!Generic.stringBuilderEndsWith(writeBody,"\n")) {
				request.append("\n");
			}
		}

		//Messenger.getInstance().debug(this,"Request:\n" + request);
		//Messenger.getInstance().debug(this,"Response:\n");
		
		Socket socket = null;
		PrintWriter writer = null;
		BufferedReader reader = null;
		try {
			if (domain.isHttps()) {
			    SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			    socket = ssf.createSocket(domain.getAddress(),domain.getPort());
			} else {
				socket = new Socket(domain.getAddress(),domain.getPort());
			}
			if (service.isTimeOut()) {
				socket.setSoTimeout((service.getTimeOutSeconds() * 1000));
			}
			writer = new PrintWriter(socket.getOutputStream(), true);
			writer.write(request.toString());
			writer.write("\n");
			writer.flush();
			
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				responseText.append(line);
				responseText.append("\n");
				//Messenger.getInstance().debug(this,line);
			}
		} catch (SocketException e) {
			errorLog.append("ERROR: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),"") + "\n");
		} catch (IOException e) {
			errorLog.append("ERROR: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),"") + "\n");
		}

		if (writer!=null) {
			writer.close();
		}
		if (reader!=null) {
			try {
				reader.close();
			} catch (IOException e) {
				errorLog.append("ERROR: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),"") + "\n");
			}
		}
		if (socket!=null) {
			try {
				socket.close();
			} catch (IOException e) {
				errorLog.append("ERROR: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),"") + "\n");
			}
		}
		return responseText;
	}
}
