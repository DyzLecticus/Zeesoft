package nl.zeesoft.zdk.http;

import java.util.List;

import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public class HttpRequestStringConvertor extends ObjectStringConvertor {
	public HttpHeadersStringConvertor	headConvertor	= (HttpHeadersStringConvertor) ObjectStringConvertors.getConvertor(HttpHeaders.class);
	
	@Override
	public Class<?> getObjectClass() {
		return HttpRequest.class;
	}

	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof HttpRequest) {
			HttpRequest req = (HttpRequest) obj;
			appendFirst(r, req);
			r.append(headConvertor.toStringBuilder(req.head));
			r.append("\r\n");
			r.append("\r\n");
			r.append(req.body);
		}
		return r;
	}

	@Override
	public HttpRequest fromStringBuilder(StringBuilder str) {
		HttpRequest r = null;
		if (str.length()>0) {
			r = new HttpRequest();
			List<StringBuilder> data = StrUtil.split(str, "\r\n");
			parseFirst(r, data.remove(0));
			parseHead(r, data);
			parseBody(r, data);
		}
		return r;
	}
	
	protected void appendFirst(StringBuilder str, HttpRequest req) {
		str.append(req.method);
		str.append(" ");
		str.append(req.path);
		str.append(" ");
		str.append(req.protocol);
	}
	
	protected void parseFirst(HttpRequest req, StringBuilder dat) {
		List<StringBuilder> elems = StrUtil.split(dat, " ");
		if (elems.size()==3) {
			req.method = elems.get(0).toString();
			req.path = elems.get(1).toString();
			req.protocol = elems.get(2).toString();
		}
	}
	
	protected void parseHead(HttpRequest req, List<StringBuilder> data) {
		StringBuilder header = getHeader(data);
		if (header.length()>0) {
			req.head = headConvertor.fromStringBuilder(header);
		}
	}
	
	protected StringBuilder getHeader(List<StringBuilder> data) {
		StringBuilder r = new StringBuilder();
		for (StringBuilder dat: data) {
			if (dat.length()==0) {
				break;
			}
			if (r.length()>0) {
				r.append("\r\n");
			}
			r.append(dat);
		}
		return r;
	}
	
	protected void parseBody(HttpRequest req, List<StringBuilder> data) {
		boolean start = false; 
		for (StringBuilder dat: data) {
			if (start) {
				if (req.body.length()>0) {
					req.body.append("\r\n");
				}
				req.body.append(dat);
			}
			if (dat.length()==0) {
				start = true;
			}
		}
	}
}
