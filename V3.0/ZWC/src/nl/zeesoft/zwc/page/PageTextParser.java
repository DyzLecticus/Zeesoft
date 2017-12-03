package nl.zeesoft.zwc.page;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

public class PageTextParser extends PageParser {
	private static final String 		CAPITALS		= "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	private SortedMap<String,String>	replacements	= new TreeMap<String,String>();
	
	public PageTextParser() {
		// Page can be set after creating the object
		initializeReplacements();
	}
	
	public PageTextParser(ZStringBuilder page) {
		setPage(page);
		initializeReplacements();
	}

	public void addReplacement(String search, String replace) {
		replacements.put(search,replace);
	}
	
	public ZStringSymbolParser getText() {
		ZStringBuilder work = getPage();
		work.replace("<br />"," ");
		work.replace("\n"," ");
		work.replace("&nbsp;"," ");
		work.replace("\t"," ");
		
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

		for (Entry<String,String> entry: replacements.entrySet()) {
			work.replace(entry.getKey(),entry.getValue());
		}
		
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
	
	private void initializeReplacements() {
		replacements.put("&#39;","'");
		replacements.put("&#224;","�");
		replacements.put("&#225;","�");
		replacements.put("&#232;","�");
		replacements.put("&#233;","�");
		replacements.put("&#235;","�");
		replacements.put("&#239;","�");
		replacements.put("&#243;","�");
		replacements.put("&#246;","�");
		replacements.put("&#250;","�");
		replacements.put("&#252;","�");
		replacements.put("&#822;","-");
		replacements.put("&#8220;","\"");
		replacements.put("&#8221;","\"");
		replacements.put("&#8364;","euro");
		
		replacements.put("&lsquo;","'");
		replacements.put("&rsquo;","'");
		replacements.put("&amp;","&");
		replacements.put("&hellip;","...");
		replacements.put("&ndash;","-");
		replacements.put("&euro;","euro");
	}
}








