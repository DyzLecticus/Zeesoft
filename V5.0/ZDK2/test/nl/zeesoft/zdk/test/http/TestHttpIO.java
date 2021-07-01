package nl.zeesoft.zdk.test.http;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.http.HttpHeader;
import nl.zeesoft.zdk.http.HttpHeaders;
import nl.zeesoft.zdk.http.HttpHeadersStringConvertor;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpRequestStringConvertor;
import nl.zeesoft.zdk.http.HttpResponse;
import nl.zeesoft.zdk.http.HttpResponseStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class TestHttpIO {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		
		HttpHeader header = new HttpHeader();
		header.name = "name";
		header.value = "value";
		
		HttpHeaders head = new HttpHeaders();
		assert head.get("") == null;
		head.add("Keep-Alive", "300");
		head.add("Connection", "keep-alive");
		head.add("Test", "removeMe");
		assert head.remove("Test").value.equals("removeMe");
		assert head.remove("Test") == null;

		HttpHeadersStringConvertor hhsc = (HttpHeadersStringConvertor) ObjectStringConvertors.getConvertor(HttpHeaders.class);
		StringBuilder str = hhsc.toStringBuilder(head);
		assert hhsc.toStringBuilder(hhsc).length() == 0;
		assert hhsc.fromStringBuilder(new StringBuilder()) == null;
		
		HttpHeaders head2 = hhsc.fromStringBuilder(str);
		assert head2.headers.size() == 2;
		assert head2.get("Keep-Alive").value.equals("300");
		assert head2.get("Connection").value.equals("keep-alive");
		
		HttpRequest request = new HttpRequest();
		assert request.head.headers.size() == 0;
		assert request.isConnectionClose();
		assert request.getContentLength() == 0;
		try {
			request.setDefaultHeaders(new URL("http://www.test.com"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		assert request.head.headers.size() == 3;
		assert request.head.get(HttpHeader.HOST).value.equals("www.test.com");
		assert request.head.get(HttpHeader.CONNECTION).value.equals("close");
		assert request.head.get(HttpHeader.CONTENT_LENGTH).value.equals("0");
		try {
			request.setDefaultHeaders(new URL("http://www.test.com"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		assert request.head.headers.size() == 3;
		request.method = "GET";
		request.path = "/index.html";
		request.protocol = "HTTP/9.9";
		request.head = head;
		request.setBody(new StringBuilder("Body" + StrUtil.CRLF + "Text"));
		assert !request.isConnectionClose();
		request.setConnectionClose();
		assert request.isConnectionClose();
		request.setConnectionKeepAlive();
		assert !request.isConnectionClose();
		
		HttpRequestStringConvertor hrsc = (HttpRequestStringConvertor) ObjectStringConvertors.getConvertor(HttpRequest.class);
		str = hrsc.toStringBuilder(request);
		assert hrsc.toStringBuilder(hrsc).length() == 0;
		assert hrsc.fromStringBuilder(new StringBuilder()) == null;
		assert hrsc.fromStringBuilder(new StringBuilder(StrUtil.CRLF)) != null;
		assert hrsc.fromStringBuilder(new StringBuilder("qwer")) != null;
		
		HttpRequest request2 = hrsc.fromStringBuilder(str);
		assert request2.method.equals("GET");
		assert request2.path.equals("/index.html");
		assert request2.protocol.equals("HTTP/9.9");
		assert request2.head.headers.size() == 2;
		assert request2.head.get("Keep-Alive").value.equals("300");
		assert request2.head.get("Connection").value.equals("keep-alive");
		assert request2.getBody().toString().equals("");
		
		request2.setContentLength();
		assert request2.head.headers.size() == 3;
		request2.setContentLength();
		assert request2.head.headers.size() == 3;
		
		HttpResponse response = new HttpResponse();
		response.protocol = "HTTP/9.9";
		assert response.code == 200;
		assert response.message.equals("OK");
		response.setNotImplemented();
		assert response.code == HttpURLConnection.HTTP_NOT_IMPLEMENTED;
		assert response.message.equals("Not Implemented");
		response.setNotFound();
		assert response.code == HttpURLConnection.HTTP_NOT_FOUND;
		assert response.message.equals("Not Found");
		response.head.add("Keep-Alive", "300");
		response.head.add("Connection", "keep-alive");
		response.setBody(new StringBuilder("Body" + StrUtil.CRLF + "Text"));
		
		HttpResponseStringConvertor hpsc = (HttpResponseStringConvertor) ObjectStringConvertors.getConvertor(HttpResponse.class);
		str = hpsc.toStringBuilder(response);
		
		HttpResponse response2 = hpsc.fromStringBuilder(str);
		assert response2.protocol.equals("HTTP/9.9");
		assert response2.code == HttpURLConnection.HTTP_NOT_FOUND;
		assert response2.message.equals("Not Found");
		assert response2.head.headers.size() == 2;
		assert response2.head.get("Keep-Alive").value.equals("300");
		assert response2.head.get("Connection").value.equals("keep-alive");
		assert response2.getBody().toString().equals("");
		
		assert hpsc.toStringBuilder(hpsc).length() == 0;
		assert hpsc.fromStringBuilder(new StringBuilder()) == null;
		assert hpsc.fromStringBuilder(new StringBuilder(StrUtil.CRLF)) != null;
		assert hpsc.fromStringBuilder(new StringBuilder("qwer")) != null;
		
		response = new HttpResponse(HttpURLConnection.HTTP_NOT_FOUND, "Not Found");
		assert response.code == HttpURLConnection.HTTP_NOT_FOUND;
		assert response.message.equals("Not Found");
	}
}
