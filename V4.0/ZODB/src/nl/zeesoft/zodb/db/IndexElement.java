package nl.zeesoft.zodb.db;

import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsFile;

public class IndexElement {
	public long		id			= 0L;
	public String	name		= "";
	public long		modified	= 0L;
	public int		fileNum		= 0;
	public JsFile	obj			= null;
	
	public boolean	added		= false;
	public boolean	removed		= false;
	
	public IndexElement() {
		updateModified();
	}
	
	public IndexElement copy() {
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
	
	public void updateModified() {
		this.modified = (new Date()).getTime();
	}
	
	public ZStringBuilder toStringBuilder() {
		ZStringBuilder r = new ZStringBuilder();
		r.append("" + id);
		r.append("\t");
		r.append(name);
		r.append("\t");
		r.append("" + modified);
		return r;
	}
	
	public void fromStringBuilder(ZStringBuilder str) {
		List<ZStringBuilder> split = str.split("\t");
		id = Long.parseLong(split.get(0).toString());
		name = split.get(1).toString();
		modified = Long.parseLong(split.get(2).toString());
	}
}
