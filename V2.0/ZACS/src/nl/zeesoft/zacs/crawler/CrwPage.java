package nl.zeesoft.zacs.crawler;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zodb.Generic;

public class CrwPage {
	private String 				url				= "";
	private StringBuilder 		content			= null;
	private List<String>		metaTags		= new ArrayList<String>();
	private List<String>		anchorTags		= new ArrayList<String>();
	private List<StringBuilder>	textElements	= new ArrayList<StringBuilder>();

	public CrwPage(String url, StringBuilder content) {
		this.url = url;
		this.content = content;
	}
	
	public boolean canCrawl() {
		boolean r = true;
		if (metaTags.size()==0) {
			parseMetaTags();
		}
		if (metaTags.size()>0) {
			for (String tag: metaTags) {
				if (tag.contains("\"robots\"")) {
					if (tag.contains("nofollow")) {
						r = false;
						break;
					}
				}
			}
		}
		return r;
	}

	public List<String> getUrls() {
		List<String> r = new ArrayList<String>();
		if (anchorTags.size()==0) {
			parseAnchorTags();
		}
		if (anchorTags.size()>0) {
			for (String tag: anchorTags) {
				if (!tag.toLowerCase().contains("nofollow") && tag.contains("href")) {
					String parse = tag.replace("<a ","");
					parse = parse.replace("/>"," />");
					parse = parse.replace(" "," ");
					parse = parse.replace(" =","=");
					parse = parse.replace("= ","=");
					StringBuilder url = new StringBuilder();
					for (int i = parse.indexOf("href") + 4; i<parse.length(); i++) {
						String c = parse.substring(i,(i+1));
						if (c.equals(" ")) {
							break;
						}
						if (!c.equals("=")) {
							url.append(c);
						}
					}
					String f = url.toString().replaceAll("\"","");
					if (!f.equals("/")) {
						r.add(f);
					}
				}
			}
		}
		return r;
	}

	public List<StringBuilder> getTextElements(int minLength,boolean parseLists) {
		if (textElements.size()==0) {
			parseTextElements(minLength,parseLists);
		}
		return textElements; 
	}

	private StringBuilder getContentFormattedForElementTags(String tagName) {
		StringBuilder r = new StringBuilder(content.toString().toLowerCase());
		r = Generic.stringBuilderReplace(r,"\n","");
		r = Generic.stringBuilderReplace(r,"<" + tagName + " ","\n<" + tagName + " ");
		r = Generic.stringBuilderReplace(r,"/>","/>\n");
		r = Generic.stringBuilderReplace(r,"\">","\"/>\n");
		r = Generic.stringBuilderReplace(r,"\" >","\"/>\n");
		return r;
	}

	private void parseMetaTags() {
		metaTags.clear();
		StringBuilder formatted = getContentFormattedForElementTags("meta");
		List<StringBuilder> lines = Generic.stringBuilderSplit(formatted,"\n");
		for (StringBuilder line: lines) {
			if (line.toString().startsWith("<meta")) {
				//Messenger.getInstance().debug(this,"Meta tag: " + line);
				metaTags.add(line.toString());
			}
		}
	}

	private void parseAnchorTags() {
		anchorTags.clear();
		StringBuilder formatted = getContentFormattedForElementTags("a");
		List<StringBuilder> lines = Generic.stringBuilderSplit(formatted,"\n");
		for (StringBuilder line: lines) {
			if (line.toString().startsWith("<a ")) {
				//Messenger.getInstance().debug(this,"Anchor tag: " + line);
				anchorTags.add(line.toString());
			}
		}
	}
	
	private void parseTextElements(int minLength,boolean parseLists) {
		textElements.clear();
		List<StringBuilder> elems = Generic.stringBuilderSplit(content,"<body");
		if (elems.size()>1) {
			StringBuilder formatted = elems.get(1);

			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<script","</script>","");
			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<script","/>","");

			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<div",">","");
			formatted = Generic.stringBuilderReplace(formatted,"</div>","");

			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<a ",">","");
			formatted = Generic.stringBuilderReplace(formatted,"</a>","");

			if (parseLists) {
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<ul",">","");
				formatted = Generic.stringBuilderReplace(formatted,"</ul>","");
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<ol",">","");
				formatted = Generic.stringBuilderReplace(formatted,"</ol>","");
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<li",">","");
				formatted = Generic.stringBuilderReplace(formatted,"</li>",". ");
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<dl",">","");
				formatted = Generic.stringBuilderReplace(formatted,"</dl>","");
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<dt",">","");
				formatted = Generic.stringBuilderReplace(formatted,"</dt>",". ");
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<dd",">","");
				formatted = Generic.stringBuilderReplace(formatted,"</dd>",". ");
			} else {
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<ul","</ul>","");
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<ol","</ol>","");
				formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<dl","</dl>","");
			}
			
			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<img ","/>","");
			
			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<label","</label>","");
			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<sup","</sup>","");
			
			formatted = Generic.stringBuilderReplace(formatted,"\r"," ");
			formatted = Generic.stringBuilderReplace(formatted,"\n"," ");
			formatted = Generic.stringBuilderReplace(formatted,"\t"," ");
			
			formatted = Generic.stringBuilderReplace(formatted,"<b>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</b>"," ");
			
			formatted = Generic.stringBuilderReplace(formatted,"<p>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</p>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"<em>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</em>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"<strong>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</strong>"," ");

			formatted = Generic.stringBuilderReplace(formatted,"<h1>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</h1>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"<h2>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</h2>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"<h3>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</h3>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"<h4>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</h4>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"<h5>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</h5>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"<h6>"," ");
			formatted = Generic.stringBuilderReplace(formatted,"</h6>"," ");
			
			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<br","/>"," ");
			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<hr","/>"," ");

			formatted = Generic.stringBuilderReplaceStartEnd(formatted,".<span",">",". ");
			formatted = Generic.stringBuilderReplaceStartEnd(formatted,"<span",">","");
			formatted = Generic.stringBuilderReplace(formatted,".</span>",". ");
			formatted = Generic.stringBuilderReplace(formatted,"</span>",". ");
			
			// Entities
			formatted = Generic.stringBuilderReplace(formatted,"&#8364;","EURO");
			formatted = Generic.stringBuilderReplace(formatted,"&#250;","u");
			formatted = Generic.stringBuilderReplace(formatted,"&#233;","e");
			formatted = Generic.stringBuilderReplace(formatted,"&#235;","e");
			formatted = Generic.stringBuilderReplace(formatted,"&#239;","i");
			formatted = Generic.stringBuilderReplace(formatted,"&#243;","o");
			formatted = Generic.stringBuilderReplace(formatted,"&#246;","o");
			formatted = Generic.stringBuilderReplace(formatted,"&#39;","'");
			
			formatted = Generic.stringBuilderReplace(formatted,"&nbsp;"," ");
			formatted = Generic.stringBuilderReplace(formatted,"&amp;","&");
			formatted = Generic.stringBuilderReplace(formatted,"&apos;","'");
			formatted = Generic.stringBuilderReplace(formatted,"&quot;","\"");
			formatted = Generic.stringBuilderReplace(formatted,"&copy;","(c)");

			formatted = Generic.stringBuilderReplace(formatted,"&times;","X");

			formatted = Generic.stringBuilderReplace(formatted,"&rarr;",".");
			formatted = Generic.stringBuilderReplace(formatted,"&larr;",".");
			formatted = Generic.stringBuilderReplace(formatted,"&lt;"," ");
			formatted = Generic.stringBuilderReplace(formatted,"&gt;"," ");

			formatted = Generic.stringBuilderReplace(formatted," * "," . ");
			
			formatted = Generic.stringBuilderReplace(formatted,"     "," ");
			formatted = Generic.stringBuilderReplace(formatted,"    "," ");
			formatted = Generic.stringBuilderReplace(formatted,"   "," ");
			formatted = Generic.stringBuilderReplace(formatted,"  "," ");

			formatted = Generic.stringBuilderReplace(formatted," .",".");

			formatted = Generic.stringBuilderReplace(formatted,"...",".");
			formatted = Generic.stringBuilderReplace(formatted,"..",".");
			
			elems = Generic.stringBuilderSplit(formatted,"<");
			for (StringBuilder textElem: elems) {
				List<StringBuilder> subElems = Generic.stringBuilderSplit(textElem,">");
				if (subElems.size()>1) {
					StringBuilder tElem = subElems.get((subElems.size() - 1));
					tElem = new StringBuilder(tElem.toString().trim());
					if (tElem.length()>minLength && 
						!Generic.stringBuilderContainsOneOfCharacters(tElem,"{}")
						) {
						if (Generic.stringBuilderContainsOneOfCharacters(tElem,Symbol.STRUCTURE_SYMBOLS_STR)) {
							textElements.add(tElem);
						}
					}
				}
			}
		}
	}
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
}
