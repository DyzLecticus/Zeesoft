package nl.zeesoft.zodb.db;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringEncoder;
import nl.zeesoft.zdk.json.JsFile;

public class IndexElement {
	public long			id			= 0L;
	public String		name		= "";
	public long			modified	= 0L;
	public int			fileNum		= 0;
	public JsFile		obj			= null;
	
	protected boolean	added		= false;
	protected boolean	removed		= false;
	
	protected IndexElement() {
		updateModified();
	}
	
	protected IndexElement copy() {
		IndexElement r = new IndexElement();
		r.id = this.id;
		r.name = this.name;
		r.modified = this.modified;
		r.fileNum = this.fileNum;
		r.obj = this.obj;
		r.removed = this.removed;
		r.added = this.added;
		return r;
	}
	
	protected void updateModified() {
		this.modified = (new Date()).getTime();
	}
	
	protected ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("" + id);
		r.append("\t");
		r.append(name);
		r.append("\t");
		r.append("" + modified);
		return r;
	}
	
	protected void fromStringBuilder(ZStringBuilder str) {
		List<ZStringBuilder> split = str.split("\t");
		if (split.size()==3) {
			String v = split.get(0).toString();
			if (ZStringEncoder.isNumber(v)) {
				id = Long.parseLong(v);
			}
			name = split.get(1).toString();
			v = split.get(2).toString();
			if (ZStringEncoder.isNumber(v)) {
				modified = Long.parseLong(v);
			}
		}
	}
}
