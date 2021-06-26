package nl.zeesoft.zdk.test.http;

import nl.zeesoft.zdk.http.HttpHeader;
import nl.zeesoft.zdk.http.HttpHeaders;
import nl.zeesoft.zdk.http.HttpHeadersStringConvertor;
import nl.zeesoft.zdk.http.HttpRequest;
import nl.zeesoft.zdk.http.HttpRequestStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;

public class TestHttpRequest {
	public static void main(String[] args) {
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
		request.method = "GET";
		request.path = "/index.html";
		request.protocol = "HTTP/9.9";
		request.head = head;
		request.body.append("Body\r\nText");
		
		HttpRequestStringConvertor hrsc = (HttpRequestStringConvertor) ObjectStringConvertors.getConvertor(HttpRequest.class);
		str = hrsc.toStringBuilder(request);
		assert hrsc.toStringBuilder(hrsc).length() == 0;
		assert hrsc.fromStringBuilder(new StringBuilder()) == null;
		assert hrsc.fromStringBuilder(new StringBuilder("\r\n")) != null;
		assert hrsc.fromStringBuilder(new StringBuilder("qwer")) != null;
		
		HttpRequest request2 = hrsc.fromStringBuilder(str);
		assert request2.method.equals("GET");
		assert request2.path.equals("/index.html");
		assert request2.protocol.equals("HTTP/9.9");
		assert request2.head.headers.size() == 2;
		assert request2.head.get("Keep-Alive").value.equals("300");
		assert request2.head.get("Connection").value.equals("keep-alive");
		assert request2.body.toString().equals("Body\r\nText");
	}
}
