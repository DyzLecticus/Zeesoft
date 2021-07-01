package nl.zeesoft.zdk.http;

import java.util.List;

import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.StrUtil;

public class HttpHeadersStringConvertor extends ObjectStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return HttpHeaders.class;
	}

	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof HttpHeaders) {
			appendHeaders(r, (HttpHeaders) obj);
		}
		return r;
	}

	@Override
	public HttpHeaders fromStringBuilder(StringBuilder str) {
		HttpHeaders r = null;
		if (str.length()>0) {
			r = new HttpHeaders();
			List<StringBuilder> data = StrUtil.split(str, StrUtil.CRLF.toString());
			for (StringBuilder dat: data) {
				List<StringBuilder> nv = StrUtil.split(dat, ": ");
				if (nv.size()>1) {
					r.add(nv.get(0).toString(), nv.get(1).toString());
				}
			}
		}
		return r;
	}
	
	protected void appendHeaders(StringBuilder str, HttpHeaders head) {
		for (HttpHeader h: head.headers) {
			str.append(StrUtil.CRLF);
			str.append(h.name);
			str.append(": ");
			str.append(h.value);
		}
	}
}
