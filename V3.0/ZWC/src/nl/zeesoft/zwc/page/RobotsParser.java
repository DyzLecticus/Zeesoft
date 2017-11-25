package nl.zeesoft.zwc.page;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class RobotsParser {
	private	String			baseUrl			= "";
	private PageReader 		pageReader		= null;

	public RobotsParser(PageReader pageReader,String baseUrl) {
		this.pageReader = pageReader;
		StringBuilder url = new StringBuilder();
		if (baseUrl.startsWith("http://")) {
			baseUrl = baseUrl.substring(7);
		}
		for (int i = 0; i < baseUrl.length(); i++) {
			if (baseUrl.substring(i,i+1).equals("/")) {
				break;
			}
			url.append(baseUrl.substring(i,(i + 1)));
		}
		this.baseUrl = "http://" + url.toString();
	}
	
	public List<String> getDisallowedUrls() {
		List<String> r = new ArrayList<String>();
		ZStringBuilder robotsTxt = pageReader.getPageAtUrl(baseUrl + "/robots.txt");
		if (robotsTxt!=null && robotsTxt.length()>0) {
			List<ZStringBuilder> rules = robotsTxt.split("\n");
			boolean start = false;
			for (ZStringBuilder rule: rules) {
				if (rule.toString().trim().equals("User-agent: *")) {
					start = true;
				}
				if (start && rule.toString().trim().startsWith("Disallow: ")) {
					String url = baseUrl + rule.toString().trim().substring(10);
					if (!r.contains(url)) {
						r.add(url);
					}
				}
			}
		}
		return r;
	}
}
