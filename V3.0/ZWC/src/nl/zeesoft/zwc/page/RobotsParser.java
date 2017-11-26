package nl.zeesoft.zwc.page;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

public class RobotsParser {
	private	String			baseUrl			= "";
	private PageReader 		pageReader		= null;

	public RobotsParser(PageReader pageReader,String baseUrl) {
		this.pageReader = pageReader;
		try {
			URI uri = new URI(baseUrl).normalize();
			baseUrl = uri.getScheme() + "://" + uri.getHost();
			if (uri.getPort()>0 && uri.getPort()!=80) {
				baseUrl += ":" + uri.getPort();
			}
		} catch (URISyntaxException e) {
			// Ignore
		}
		this.baseUrl = baseUrl;
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
