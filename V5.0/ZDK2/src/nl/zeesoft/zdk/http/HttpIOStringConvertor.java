package nl.zeesoft.zdk.http;

import java.util.List;

import nl.zeesoft.zdk.Instantiator;
import nl.zeesoft.zdk.str.ObjectStringConvertor;
import nl.zeesoft.zdk.str.ObjectStringConvertors;
import nl.zeesoft.zdk.str.StrUtil;

public abstract class HttpIOStringConvertor extends ObjectStringConvertor {
	public HttpHeadersStringConvertor	headConvertor	= (HttpHeadersStringConvertor) ObjectStringConvertors.getConvertor(HttpHeaders.class);
	
	@Override
	public StringBuilder toStringBuilder(Object obj) {
		StringBuilder r = new StringBuilder();
		if (obj instanceof HttpIO) {
			HttpIO io = (HttpIO) obj;
			appendFirst(r, io);
			r.append(headConvertor.toStringBuilder(io.head));
		}
		return r;
	}

	@Override
	public HttpIO fromStringBuilder(StringBuilder str) {
		HttpIO r = null;
		if (str.length()>0) {
			r = (HttpIO) Instantiator.getNewClassInstance(getObjectClass());
			List<StringBuilder> data = StrUtil.split(str, StrUtil.CRLF.toString());
			parseFirst(r, data.remove(0));
			parseHead(r, data);
		}
		return r;
	}
	
	protected abstract void appendFirst(StringBuilder str, HttpIO io);
	
	protected abstract void parseFirst(HttpIO io, StringBuilder dat);
	
	protected void parseHead(HttpIO io, List<StringBuilder> data) {
		StringBuilder header = getHeader(data);
		if (header.length()>0) {
			io.head = headConvertor.fromStringBuilder(header);
		}
	}
	
	protected StringBuilder getHeader(List<StringBuilder> data) {
		StringBuilder r = new StringBuilder();
		for (StringBuilder dat: data) {
			if (dat.length()==0) {
				break;
			}
			StrUtil.append(r, dat, StrUtil.CRLF.toString());
		}
		return r;
	}
}
