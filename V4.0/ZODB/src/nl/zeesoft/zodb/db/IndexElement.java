package nl.zeesoft.zodb.db;

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsFile;

public class IndexElement {
	public long								id				= 0L;
	public ZStringBuilder					name			= new ZStringBuilder();
	public long								modified		= 0L;
	public int								indexFileNum	= 0;
	public int								dataFileNum		= 0;
	public JsFile							obj				= null;
	
	public SortedMap<String,ZStringBuilder>	idxValues	= new TreeMap<String,ZStringBuilder>();
	
	protected boolean						added		= false;
	protected boolean						removed		= false;
	
	protected IndexElement() {
		updateModified();
	}
	
	public IndexElement copy() {
		IndexElement r = new IndexElement();
		r.id = this.id;
		r.name = this.name;
		r.modified = this.modified;
		r.indexFileNum = this.indexFileNum;
		r.dataFileNum = this.dataFileNum;
		r.obj = this.obj;
		r.removed = this.removed;
		r.added = this.added;
		for (Entry<String,ZStringBuilder> entry: idxValues.entrySet()) {
			r.idxValues.put(entry.getKey(), entry.getValue());
		}
		return r;
	}
	
	protected void updateModified() {
		modified = (new Date()).getTime();
	}
	
	protected ZStringBuilder toStringBuilder(StringBuilder key) {
		ZStringBuilder r = new ZStringBuilder();
		r.append("" + id);
		r.append("\t");
		r.append(name);
		r.append("\t");
		r.append("" + modified);
		r.append("\t");
		r.append("" + dataFileNum);
		r.append("\t");
		ZStringEncoder encoder = new ZStringEncoder();
		for (Entry<String,ZStringBuilder> entry: idxValues.entrySet()) {
			if (encoder.length()>0) {
				encoder.append("\t");
			}
			encoder.append(entry.getKey());
			encoder.append("\t");
			encoder.append(entry.getValue());
		}
		encoder.encodeKey(key,0);
		r.append(encoder);
		return r;
	}
	
	protected void fromStringBuilder(ZStringBuilder str,StringBuilder key) {
		List<ZStringBuilder> split = str.split("\t");
		if (split.size()==5) {
			String v = split.get(0).toString();
			if (ZStringEncoder.isNumber(v)) {
				id = Long.parseLong(v);
			}
			name = split.get(1);
			v = split.get(2).toString();
			if (ZStringEncoder.isNumber(v)) {
				modified = Long.parseLong(v);
			}
			v = split.get(3).toString();
			if (ZStringEncoder.isNumber(v)) {
				dataFileNum = Integer.parseInt(v);
			}
			ZStringEncoder encoder = new ZStringEncoder(split.get(4));
			encoder.decodeKey(key,0);
			List<ZStringBuilder> idxVals = encoder.split("\t");
			int i = 0;
			String k = "";
			idxValues.clear();
			for (ZStringBuilder idxVal: idxVals) {
				if ((i % 2)==0) {
					k = idxVal.toString();
				} else {
					idxValues.put(k,idxVal);
				}
				i++;
			}
		}
	}
}
