package nl.zeesoft.zwc.page;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * A page text parser can be used to parse all text from an HTML page.
 */
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

		List<ZStringBuilder> lines = work.split("\n");
		work = new ZStringBuilder();
		for (ZStringBuilder line: lines) {
			if (line.length()>1 && !line.containsOneOfCharacters("<>")) {
				
				for (Entry<String,String> entry: replacements.entrySet()) {
					line.replace(entry.getKey(),entry.getValue());
				}
				line.replace("  "," ");
				
				boolean add = false;
				for (int i = 0; i < CAPITALS.length(); i++) {
					if (line.startsWith(CAPITALS.substring(i,(i + 1)))) {
						add = true;
						break;
					}
				}
				
				if (add) {
					if (work.length()>0) {
						work.append(" ");
					}
					work.append(removeInvalidAsciiFromLine(line));
				}
			}
		}

		return new ZStringSymbolParser(work);
	}
	
	private ZStringBuilder removeInvalidAsciiFromLine(ZStringBuilder line) {
		ZStringBuilder r = new ZStringBuilder();
		for (int i = 0; i<line.length(); i++) {
			char ch = line.getStringBuilder().charAt(i);
			int chi = (int) ch;
			if (chi < 32 || chi == 127 || chi > 166) {
				r.append(" ");
			} else {
				r.append(line.substring(i,(i + 1)));
			}
		}
		return r;
	}
	
	private void initializeReplacements() {
		replacements.put("&#39;","'");
		replacements.put("&#224;","à");
		replacements.put("&#225;","á");
		replacements.put("&#232;","è");
		replacements.put("&#233;","é");
		replacements.put("&#235;","ë");
		replacements.put("&#239;","ï");
		replacements.put("&#243;","ó");
		replacements.put("&#246;","ö");
		replacements.put("&#250;","ú");
		replacements.put("&#252;","ü");
		replacements.put("&#822;","-");
		replacements.put("&#8220;","\"");
		replacements.put("&#8221;","\"");
		replacements.put("&#8332;","2");
		replacements.put("&#8364;","EURO ");
		
		replacements.put("&lt;","<");
		replacements.put("&gt;",">");
		replacements.put("&bull;","* ");
		replacements.put("&ldquo;","'");
		replacements.put("&rdquo;","'");
		replacements.put("&lsquo;","'");
		replacements.put("&rsquo;","'");
		replacements.put("&amp;","&");
		replacements.put("&hellip;","...");
		replacements.put("&minus;","-");
		replacements.put("&plus;","+");
		replacements.put("&ndash;","-");
		replacements.put("&euro;","EURO ");
	}
}








