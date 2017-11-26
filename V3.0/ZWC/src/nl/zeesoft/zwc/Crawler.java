package nl.zeesoft.zwc;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zwc.page.PageParser;
import nl.zeesoft.zwc.page.PageReader;
import nl.zeesoft.zwc.page.RobotsParser;

public class Crawler extends Locker {
	private Messenger		messenger		= null;
	private WorkerUnion		union			= null;
	
	private	String			baseUrl			= "";
	private String			outputDir		= System.getProperty("user.home") + "/ZWC/";
	private int				delayMs			= 200;

	private	String			protocol		= "";
	private	String			domain			= "";
	private	int				port			= 80;

	private PageReader		pageReader		= null;
	private RobotsParser	robotsParser	= null;
	private List<String>	disallowedUrls	= new ArrayList<String>();
	private CrawlerWorker	worker			= null;
	private boolean			done			= false;

	private	int				crawlIndex		= -1;
	private List<String>	crawlUrls		= new ArrayList<String>();
	private List<String>	crawledUrls		= new ArrayList<String>();
	
	public Crawler(Messenger msgr, WorkerUnion uni, String baseUrl) {
		super(msgr);
		messenger = msgr;
		union = uni;
		this.baseUrl = baseUrl;
	}
	
	public Crawler(String baseUrl) {
		super(null);
		this.baseUrl = baseUrl;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	public void setDelayMs(int delayMs) {
		this.delayMs = delayMs;
	}
	
	public String initialize() {
		String err = "";
		if (baseUrl.length()==0) {
			err = "Base URL is empty";
		} else if (!baseUrl.endsWith("/")) {
			baseUrl += "/";
		}
		if (err.length()==0) {
			File dir = new File(outputDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			if (!dir.exists()) {
				err = "Failed to create output directory";
			}
		}
		URI url = null;
		try {
			url = new URI(baseUrl);
		} catch (URISyntaxException e) {
			err = e.toString();
		}
		if (err.length()>0 && messenger!=null) {
			messenger.error(this,err);
		}
		if (err.length()==0) {
			protocol = url.getScheme();
			domain = url.getHost();
			port = url.getPort();
			pageReader = new PageReader(messenger);
			robotsParser = new RobotsParser(pageReader,baseUrl);
			worker = new CrawlerWorker(messenger,union,this);
			worker.setSleep(delayMs);
		}
		return err;
	}
	
	public void start() {
		lockMe(this);
		if (worker!=null) {
			disallowedUrls = robotsParser.getDisallowedUrls();
			crawlIndex = -1;
			crawlUrls.clear();
			crawlUrls.add(baseUrl);
			crawledUrls.clear();
			done = false;
			worker.start();
		} else {
			done = true;
		}
		unlockMe(this);
	}

	public boolean isDone() {
		System.out.println("DONE?");
		lockMe(this);
		boolean r = done;
		unlockMe(this);
		System.out.println("DONE:" + r);
		return r;
	}

	public List<String> getCrawledUrls() {
		lockMe(this);
		List<String> r = new ArrayList<String>(crawledUrls);
		unlockMe(this);
		return r;
	}
	
	protected boolean parseNextUrl() {
		boolean r = false;
		lockMe(this);
		crawlIndex++;
		System.out.println("----> To do: " + (crawlUrls.size() - crawlIndex));
		if (crawlIndex<crawlUrls.size()) {
			String pageUrl = crawlUrls.get(crawlIndex);
			System.out.println("----> Reading page: " + pageUrl);
			ZStringBuilder page = pageReader.getPageAtUrl(pageUrl);
			if (page!=null)
			System.out.println("----> Get URLs from page: " + pageUrl);
			
			List<String> pageUrls = getUrlsFromPage(pageUrl,page);
	
			System.out.println("----> Got URLs: " + pageUrls.size());
			for (String url: pageUrls) {
				boolean crawl = url.startsWith(baseUrl);
				if (crawl) {
					for(String dis: disallowedUrls) {
						if (url.matches(dis)) {
							crawl = false;
							break;
						}
					}
				}
				if (crawl) {
					for(String crawled: crawlUrls) {
						if (url.equals(crawled)) {
							crawl = false;
							break;
						}
					}
				}
				if (crawl) {
					System.out.println("----> " + url);
					crawlUrls.add(url);
				}
			}
			
			crawledUrls.add(pageUrl);
		} else {
			System.out.println("----> DONE!!!!");
			done = true;
		}
		r = done;
		unlockMe(this);
		return r;
	}
	
	private List<String> getUrlsFromPage(String pageUrl,ZStringBuilder page) {
		List<String> r = new ArrayList<String>();
		PageParser parser = new PageParser(page);
		List<ZStringBuilder> tags = parser.getTags("a",false);
		for (ZStringBuilder tag: tags) {
			StringBuilder url = new StringBuilder();
			boolean start = false;
			for (int i = 0; i<(tag.length() - 6); i++) {
				if (tag.substring(i,(i + 6)).equals("href=\"")) {
					i = i + 6;
					start = true;
				}
				if (start) {
					if (tag.substring(i,(i + 1)).equals("\"")) {
						break;
					}
					url.append(tag.substring(i,(i + 1)));
				}
			}
			String add = url.toString();
			if (!add.startsWith("mailto") && !add.startsWith("#")) {
				add = getFullUrl(pageUrl,add);
				if (add.length()>0 && !r.contains(add)) {
					r.add(add);
				}
			}
		}
		return r;
	}
	
	private String getFullUrl(String pageUrl,String url) {
		if (url.startsWith("./")) {
			url = url.substring(2);
		}
		if (url.contains("#")) {
			url = url.split("#")[0];
		}
		if (url.contains("?")) {
			url = url.split("?")[0];
		}
		String r = "";
		if (url.startsWith("http")) {
			r = url;
		} else if (url.startsWith("/")) {
			r = protocol + "://" + domain;
			if (port!=80) {
				r += ":" + port;
			}
			r += url;
		} else {
			r = pageUrl + url;
		}
		r = r.trim();
		String[] split = r.split("/");
		if (!split[(split.length - 1)].contains(".")) {
			r += "/";
		}		
		return r;
	}
}
