package nl.zeesoft.zwc.page;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * A page parser can be used to parse tags from an HTML page.
 */
public class PageParser {
	private ZStringBuilder page = null;

	public PageParser() {
		// Page can be set after creating the object
	}
	
	public PageParser(ZStringBuilder page) {
		this.page = page;
	}

	public void setPage(ZStringBuilder page) {
		this.page = page;
	}

	public ZStringBuilder getPage() {
		return page;
	}

	public List<ZStringBuilder> getTags(String tagName,boolean single) {
		List<ZStringBuilder> r = new ArrayList<ZStringBuilder>();
		if (page!=null) {
			tagName = tagName.toLowerCase();
			String searchStart = "<" + tagName;
			String searchEnd = "</" + tagName + ">";
			if (single) {
				searchEnd = ">";
			}
			
			ZStringBuilder work = new ZStringBuilder(page);
			work.replace("<" + tagName.toUpperCase(),"<" + tagName.toLowerCase());
			if (!single) {
				work.replace("</" + tagName.toUpperCase() + ">","</" + tagName.toLowerCase() + ">");
			}
			work.replace("\n","");
			work.replace(searchStart,searchStart + " ");
			work.replace("  "," ");
			work.replace("= \"","=\"");
			
			r = splitStartEnd(work,searchStart,searchEnd);
		}
		return r;
	}
	
	private List<ZStringBuilder> splitStartEnd(ZStringBuilder work,String searchStart, String searchEnd) {
		List<ZStringBuilder> r = new ArrayList<ZStringBuilder>();
		if (work!=null) {
			int length = work.length();
			int stLength = searchStart.length();
			int seLength = searchEnd.length();
			if (length>=(stLength + seLength)) {
				boolean start = false;
				int startIdx = 0;
				int endIdx = 0;
				for (int i = 0; i < length; i++) {
					if (!start) {
						if ((i + searchStart.length()) > length) {
							break;
						}
						if (work.substring(i,(i + searchStart.length())).equals(searchStart)) {
							start = true;
							startIdx = i;
							i = i + (searchStart.length() - 1);
						}
					}
					if (start) {
						if ((i + searchEnd.length()) > length) {
							break;
						}
						if (work.substring(i,(i + searchEnd.length())).equals(searchEnd)) {
							start = false;
							endIdx = i + searchEnd.length();
							r.add(new ZStringBuilder(work.substring(startIdx,endIdx)));
							i = endIdx;
						}
					}
				}
			}
		}
		return r;
	}
}
