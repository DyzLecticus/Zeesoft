package nl.zeesoft.zwc;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zwc.page.PageParser;
import nl.zeesoft.zwc.page.PageReader;
import nl.zeesoft.zwc.page.RobotsParser;

/**
 * Web crawler.
 */
public class Crawler extends Locker {
	private Messenger							messenger		= null;
	private WorkerUnion							union			= null;
	
	private	String								startUrl		= "";
	private int									delayMs			= 200;

	private	String								baseUrl			= "";
	private PageReader							pageReader		= null;
	private RobotsParser						robotsParser	= null;
	private List<String>						disallowedUrls	= new ArrayList<String>();
	private CrawlerWorker						worker			= null;
	private	int									crawlIndex		= -1;
	private List<String>						crawlUrls		= new ArrayList<String>();
	
	private boolean								done			= false;
	private List<String>						crawledUrls		= new ArrayList<String>();
	private SortedMap<String,ZStringBuilder>	pages			= new TreeMap<String,ZStringBuilder>();
	
	public Crawler(Messenger msgr, WorkerUnion uni, String startUrl) {
		super(msgr);
		messenger = msgr;
		union = uni;
		this.startUrl = startUrl;
	}
	
	public Crawler(String startUrl) {
		super(null);
		this.startUrl = startUrl;
	}
	
	/**
	 * Sets the pause milliseconds between page reads in order to prevent denial of service.
	 * 
	 * @param delayMs The number of milliseconds to pause between page reads
	 */
	public void setDelayMs(int delayMs) {
		this.delayMs = delayMs;
	}
	
	/**
	 * Initializes the crawler.
	 * 
	 * @return An error message if applicable
	 */
	public String initialize() {
		String err = "";
		lockMe(this);
		if (startUrl.length()==0) {
			err = "Start URL is empty";
		} else {
			try {
				URI url = new URI(startUrl).normalize();
				baseUrl = url.getScheme() + "://" + url.getHost();
				if (url.getPort()>0 && url.getPort()!=80) {
					baseUrl += ":" + url.getPort();
				}
				if (url.getPath().endsWith("/")) {
					baseUrl += url.getPath();
				} else {
					String[] p = url.getPath().split("/");
					for (int i = 0; i < (p.length - 1); i++) {
						baseUrl += p[i] + "/";
					}
				}
			} catch (URISyntaxException e) {
				err = e.toString();
			}
		}
		if (err.length()>0 && messenger!=null) {
			messenger.error(this,err);
		}
		if (err.length()==0) {
			pageReader = new PageReader(messenger);
			robotsParser = new RobotsParser(pageReader,baseUrl);
			worker = new CrawlerWorker(messenger,union,this);
			worker.setSleep(delayMs);
		}
		if (err.length()==0 && messenger!=null && messenger.isError()) {
			err = "An error occured while initializing the crawler";
		}
		unlockMe(this);
		return err;
	}
	
	/**
	 * Starts the crawler.
	 */
	public void start() {
		lockMe(this);
		if (worker!=null) {
			disallowedUrls = robotsParser.getDisallowedUrls();
			crawlIndex = -1;
			crawlUrls.clear();
			crawlUrls.add(startUrl);
			crawledUrls.clear();
			pages.clear();
			done = false;
			worker.start();
		} else {
			done = true;
		}
		unlockMe(this);
	}

	/**
	 * Returns true if the crawler is done.
	 * 
	 * @return True if the crawler is done
	 */
	public boolean isDone() {
		lockMe(this);
		boolean r = done;
		unlockMe(this);
		return r;
	}

	/**
	 * Returns the remaining number of URLs to crawl.
	 * 
	 * @return The remaining number of URLs to crawl
	 */
	public int getRemaining() {
		int remaining = 0;
		lockMe(this);
		remaining = crawlUrls.size() - crawledUrls.size();
		unlockMe(this);
		return remaining;
	}
	
	/**
	 * Returns a list of crawled URLs.
	 * 
	 * @return A list of crawled URLs
	 */
	public List<String> getCrawledUrls() {
		lockMe(this);
		List<String> r = new ArrayList<String>(crawledUrls);
		unlockMe(this);
		return r;
	}

	/**
	 * Returns a map of URLs and corresponding pages that have been crawled.
	 * 
	 * @return A map of URLs and corresponding pages that have been crawled
	 */
	public TreeMap<String,ZStringBuilder> getPages() {
		lockMe(this);
		TreeMap<String,ZStringBuilder> r = new TreeMap<String,ZStringBuilder>(pages);
		unlockMe(this);
		return r;
	}

	protected boolean parseNextUrl() {
		boolean r = false;
		lockMe(this);
		crawlIndex++;
		if (crawlIndex<crawlUrls.size()) {
			String pageUrl = crawlUrls.get(crawlIndex);
			ZStringBuilder page = pageReader.getPageAtUrl(pageUrl);
			crawledUrls.add(pageUrl);
			if (page!=null) {
				pages.put(pageUrl,page);
				if (canFollow(page)) {
					List<String> pageUrls = getUrlsFromPage(pageUrl,page);
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
							crawl = !crawlUrls.contains(url);
						}
						if (crawl) {
							crawlUrls.add(url);
						}
					}
				}
			}
		} else {
			done = true;
		}
		r = done;
		unlockMe(this);
		return r;
	}

	private boolean canFollow(ZStringBuilder page) {
		boolean r = true;
		PageParser parser = new PageParser(page);
		List<ZStringBuilder> tags = parser.getTags("meta",true);
		for (ZStringBuilder tag: tags) {
			String t = tag.toString().toLowerCase();
			if (t.contains("=\"robots\"") && t.contains("nofollow")) {
				r = false;
				break;
			}
		}
		return r;
	}

	private String getUrlFromTag(String tag) {
		tag = tag.toLowerCase();
		StringBuilder url = new StringBuilder();
		boolean start = false;
		for (int i = 0; i<tag.length(); i++) {
			if (!start && (i + 6)<tag.length() && tag.substring(i,(i + 6)).equals("href=\"")) {
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
		return url.toString();
	}
	
	private String getBaseUrl(String pageUrl,ZStringBuilder page) {
		String url = pageUrl;
		PageParser parser = new PageParser(page);
		List<ZStringBuilder> tags = parser.getTags("base",true);
		if (tags.size()>0) {
			url = getUrlFromTag(tags.get(0).toString());
		}
		return url;
	}
	
	private List<String> getUrlsFromPage(String pageUrl,ZStringBuilder page) {
		List<String> r = new ArrayList<String>();
		PageParser parser = new PageParser(page);
		List<ZStringBuilder> tags = parser.getTags("a",false);
		pageUrl = getBaseUrl(pageUrl,page);
		for (ZStringBuilder tag: tags) {
			String add = getUrlFromTag(tag.toString());
			if (add.length()>0 && !add.startsWith("mailto") && !add.startsWith("#")) {
				add = getFullUrl(pageUrl,add);
				if (add.length()>0 && !r.contains(add)) {
					r.add(add);
				}
			}
		}
		return r;
	}
	
	private String getFullUrl(String pageUrl,String url) {
		String r = "";
		boolean err = false;
		if (url.startsWith("http")) {
			pageUrl = "";
		} else {
			String[] elems = pageUrl.split("/");
			pageUrl = "";
			for (int i = 0; i<elems.length; i++) {
				if (i<(elems.length - 1) || !elems[i].contains(".")) {
					if (pageUrl.length()>0) {
						pageUrl += "/";
					}
					pageUrl += elems[i];
				}
			}
			try {
				URI pUri = new URI(pageUrl).normalize();
				pageUrl = pUri.getScheme() + "://" + pUri.getHost();
				if (pUri.getPort()>0 && pUri.getPort()!=80) {
					pageUrl += ":" + pUri.getPort();
				}
				if (!url.startsWith("/")) {
					pageUrl += pUri.getPath() + "/";
				}
			} catch (URISyntaxException e) {
				err = true;
			}
		}
		if (!err) {
			try {
				URI uri = new URI(pageUrl + url).normalize();
				r = uri.getScheme() + "://" + uri.getHost();
				if (uri.getPort()>0 && uri.getPort()!=80) {
					r += ":" + uri.getPort();
				}
				r += uri.getPath();
			} catch (URISyntaxException e) {
				err = true;
			}
		}
		if (err) {
			r = "";
		} else if (!r.endsWith(".htm") && !r.endsWith(".html")) {
			String[] split = r.split("/");
			if (split[(split.length - 1)].contains(".")) {
				r = "";
			}
		}
		return r;
	}
}
