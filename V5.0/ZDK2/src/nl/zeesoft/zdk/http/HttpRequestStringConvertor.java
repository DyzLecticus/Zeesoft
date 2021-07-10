package nl.zeesoft.zdk.http;

import java.util.List;

import nl.zeesoft.zdk.str.StrUtil;

public class HttpRequestStringConvertor extends HttpIOStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return HttpRequest.class;
	}

	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof HttpRequest) {
			r = super.toStringBuilder(obj);
		}
		return r;
	}

	@Override
	public HttpRequest fromStringBuilder(StringBuilder str) {
		return (HttpRequest) super.fromStringBuilder(str);
	}
	
	@Override
	protected void appendFirst(StringBuilder str, HttpIO io) {
		HttpRequest req = (HttpRequest) io;
		str.append(req.method);
		str.append(" ");
		str.append(req.path);
		if (req.query.length()>0) {
			str.append("?");
			str.append(req.query);
		}
		str.append(" ");
		str.append(req.protocol);
	}
	
	@Override
	protected void parseFirst(HttpIO io, StringBuilder dat) {
		HttpRequest req = (HttpRequest) io;
		List<StringBuilder> elems = StrUtil.split(dat, " ");
		if (elems.size()==3) {
			req.method = elems.get(0).toString();
			int index = elems.get(1).indexOf("?");
			if (index>=0) {
				req.path = elems.get(1).substring(0, index); 
				req.query = StrUtil.substring(elems.get(1), index+1, elems.get(1).length()); 
			} else {
				req.path = elems.get(1).toString();
			}
			req.protocol = elems.get(2).toString();
		}
	}	
}
