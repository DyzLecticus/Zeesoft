package nl.zeesoft.zwc.page;

import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class PageTextParser extends PageParser {
	private static final String CAPITALS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public PageTextParser() {
		// Page can be set after creating the object
	}
	
	public PageTextParser(ZStringBuilder page) {
		setPage(page);
	}

	public ZStringSymbolParser getText() {
		ZStringBuilder work = getPage();
		work.replace("\n"," ");
		work.replace("&nbsp;"," ");

		work.replaceStartEnd("<!DOCTYPE",">","");
		work.replaceStartEnd("<html",">","");
		work.replaceStartEnd("<html",">","");
		work.replaceStartEnd("<!--","-->","");
		work.replaceStartEnd("<head>","</head>","");
		work.replaceStartEnd("<body",">","");
		
		work.replaceStartEnd("<a ",">","");
		work.replace("</a>","");
		work.replaceStartEnd("<a ",">","");
		work.replace("</a>","");

		work.replace("    "," ");
		work.replace("   "," ");
		work.replace("  "," ");

		work.replace(">",">\n");
		
		work.replace(".<",".\n<");
		work.replace("?<","?\n<");
		work.replace("!<","!\n<");

		work.replace(". <",".\n<");
		work.replace("? <","?\n<");
		work.replace("! <","!\n<");

		List<ZStringBuilder> lines = work.split("\n");
		work = new ZStringBuilder();
		for (ZStringBuilder line: lines) {
			if (line.length()>1 && !line.containsOneOfCharacters("<>")) {
				boolean add = false;
				for (int i = 0; i < CAPITALS.length(); i++) {
					if (line.startsWith(CAPITALS.substring(i,(i + 1)))) {
						add = true;
					}
				}
				if (add) {
					if (work.length()>0) {
						work.append(" ");
					}
					work.append(line);
				}
			}
		}

		return new ZStringSymbolParser(work);
	}
}
