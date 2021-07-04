package nl.zeesoft.zdk.http;

import java.util.List;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.str.StrUtil;

public class HttpResponseStringConvertor extends HttpIOStringConvertor {
	@Override
	public Class<?> getObjectClass() {
		return HttpResponse.class;
	}

	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof HttpResponse) {
			r = super.toStringBuilder(obj);
		}
		return r;
	}

	@Override
	public HttpResponse fromStringBuilder(StringBuilder str) {
		return (HttpResponse) super.fromStringBuilder(str);
	}

	@Override
	protected void appendFirst(StringBuilder str, HttpIO io) {
		HttpResponse res = (HttpResponse) io;
		str.append(res.protocol);
		str.append(" ");
		str.append(res.code);
		str.append(" ");
		str.append(res.message);
	}

	@Override
	protected void parseFirst(HttpIO io, StringBuilder dat) {
		HttpResponse res = (HttpResponse) io;
		List<StringBuilder> elems = StrUtil.split(dat, " ");
		if (elems.size()>=3) {
			res.protocol = elems.remove(0).toString();
			res.code = Util.parseInt(elems.remove(0).toString());
			res.message = buildMessage(elems).toString();
		}
	}
	
	protected StringBuilder buildMessage(List<StringBuilder> elems) {
		StringBuilder r = new StringBuilder();
		for (StringBuilder elem: elems) {
			StrUtil.append(r, elem, " ");
		}
		return r;
	}
}
