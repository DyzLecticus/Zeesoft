package nl.zeesoft.zodb.db;

import java.util.Date;

import nl.zeesoft.zdk.json.JsFile;

public class IndexElement {
	public long		id			= 0L;
	public String	name		= "";
	public long		modified	= 0L;
	public int		fileNum		= 0;
	public JsFile	obj			= null;
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
		return r;
	}
	
	public void updateModified() {
		this.modified = (new Date()).getTime();
	}
}
