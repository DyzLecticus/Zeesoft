package nl.zeesoft.zacs.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zacs.database.model.Crawler;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqUpdate;


public class CrwCrawler {
	private Crawler					crawler				= null;
	
	private List<String>			disallowUrls		= new ArrayList<String>();
	private List<String>			urls				= new ArrayList<String>();
	private int						currentUrl			= 0;
	private List<CrwPage>			pages				= new ArrayList<CrwPage>();

	private List<String>			crawledUrls			= new ArrayList<String>();
	
	public CrwCrawler(Crawler crawler) {
		this.crawler = crawler;
	}

	protected boolean initialize() {
		boolean start = true;

		// Clear results
		crawler.setCrawledUrls(new StringBuilder());
		crawler.setScrapedText(new StringBuilder());
		crawler.setScrapedTextLength(0);
		updateCrawlerState(crawler,false);
		
		Messenger.getInstance().debug(this,"Crawl URL: " + crawler.getCrawlUrl() + " ...");
		crawledUrls.clear();
		List<StringBuilder> cUrls = Generic.stringBuilderSplit(crawler.getCrawledUrls(),"\n");
		for (StringBuilder cUrl: cUrls) {
			crawledUrls.add(cUrl.toString());
		}
		disallowUrls.clear();
		urls.clear();
		currentUrl = 0;
		pages.clear();
		String rootUrl = crawler.getCrawlUrl();
		if (rootUrl.length()>0) {
			if (crawler.isRespectRobotSpec()) {
				StringBuilder robotSpec = getPageAtUrl(getAbsoluteUrl("robots.txt"),true);
				if (robotSpec!=null) {
					String robots = robotSpec.toString().toLowerCase();
					String[] rules = robots.split("\n");
					for (String rule: rules) {
						if (rule.startsWith("disallow: ")) {
							rule = rule.replaceAll("disallow: ","");
							String url = getAbsoluteUrl(rule);
							if (url.endsWith("*")) {
								url = url.substring(0,(url.length() - 1));
							}
							if (!disallowUrls.contains(url)) {
								//Messenger.getInstance().debug(this,"disallow: " + url);
								disallowUrls.add(url);
							}
						}
					}
				}
			}
			urls.add(rootUrl);
		} else {
			Messenger.getInstance().debug(this,"Crawler URL not configured");
			start = false;
		}
		return start;
	}
	
	protected boolean parseNextUrl() {
		boolean done = false;
		if (currentUrl<crawler.getMaxCrawlUrls() && currentUrl<urls.size()) {
			String url = urls.get(currentUrl);
			currentUrl++;
			//Messenger.getInstance().debug(this,"Parse page URL: " + url + " (" + currentUrl + "/" + urls.size() +")");
			parseUrl(url);
		} else {
			updateCrawlerState(crawler,true);
			done();
			Messenger.getInstance().debug(this,"Crawled URL: " + crawler.getCrawlUrl());
			done = true;
		}
		return done;
	}
	
	protected long getCrawlerId() {
		return crawler.getId();
	}
	
	private void done() {
		disallowUrls.clear();
		urls.clear();
		currentUrl = 0;
		pages.clear();
		crawledUrls.clear();
	}
		
	private void parseUrl(String url) {
		StringBuilder content = getPageAtUrl(url,true);
		if (content!=null) {
			CrwPage page = new CrwPage(url,content);
			pages.add(page);
			
			if (!crawledUrls.contains(url)) {
				crawledUrls.add(url);
				if (crawler.getCrawledUrls().length()>0) {
					crawler.getCrawledUrls().append("\n");
				}
				crawler.getCrawledUrls().append(url);
				List<StringBuilder> textElements = page.getTextElements(crawler.getMinTextLength(),crawler.isParseLists());
				for (StringBuilder textElem: textElements) {
					if (crawler.getScrapedText().length()>0) {
						crawler.getScrapedText().append(" ");
					}
					crawler.getScrapedText().append(textElem);
					crawler.setScrapedTextLength(crawler.getScrapedText().length());
				}
				updateCrawlerState(crawler,false);
			}
			
			// Add URLs if allowed
			if (!crawler.isRespectRobotSpec() || page.canCrawl()) {
				List<String> cUrls = page.getUrls();
				for (String cUrl: cUrls) {
					cUrl = getAbsoluteUrl(cUrl);
					//Messenger.getInstance().debug(this,"Got child URL: " + cUrl);
					if (canParsePage(cUrl)) {
						if (urls.size()<crawler.getMaxCrawlUrls()) { 
							urls.add(cUrl);
						} else {
							break;
						}
					}
				}
			}
		}
	}
	
	private String getAbsoluteUrl(String url) {
		if (url.startsWith("/")) {
			if (crawler.getCrawlUrl().endsWith("/")) {
				url = crawler.getCrawlUrl() + url.substring(1);
			} else {
				url = crawler.getCrawlUrl() + url;
			}
		}
		if (url.startsWith("www.")) {
			url = "http:/" + url;
		}
		if (!url.startsWith("http:/") &&
			!url.startsWith("https:/")
			) {
			String rootUrl = crawler.getCrawlUrl();
			if (!url.startsWith(rootUrl)) {
				if (!url.startsWith("/") && !rootUrl.endsWith("/")) {
					url = "/" + url;
				}
				url = crawler.getCrawlUrl() + url;
			}
		}
		try {
			URL u = new URL(url);
			url = u.getProtocol() + "://" + u.getHost() + u.getPath();
			if (u.getQuery()!=null && u.getQuery().length()>0) {
				url += "?" + u.getQuery();
			}
		} catch (MalformedURLException e) {
			url = "";
		}
		return url; 
	}

	private boolean canParsePage(String url) {
		boolean r = true;
		if (!url.startsWith(crawler.getCrawlUrl()) ||
			url.equals(crawler.getCrawlUrl() + "/") ||
			url.endsWith("#") ||
			urls.contains(url)
			) {
			r = false;
		}
		if (r) {
			for (String notUrl: disallowUrls) {
				if (url.startsWith(notUrl)) {
					//Messenger.getInstance().debug(this,"Respecting robots.txt, skipping URL: " + url);
					r = false;
					break;
				}
			}
		}
		return r;
	}
	
	private void updateCrawlerState(Crawler config, boolean done) {
		ReqUpdate updateRequest = new ReqUpdate(ZACSModel.CRAWLER_CLASS_FULL_NAME,config.getId());
		updateRequest.getUpdateObject().setId(config.getId());
		updateRequest.getUpdateObject().setPropertyValue("crawledUrls",config.getCrawledUrls());
		updateRequest.getUpdateObject().setPropertyValue("scrapedText",config.getScrapedText());
		updateRequest.getUpdateObject().setPropertyValue("scrapedTextLength",new StringBuilder("" + config.getScrapedTextLength()));
		if (done) {
			updateRequest.getUpdateObject().setPropertyValue("crawl",new StringBuilder("" + false));
		}
		DbRequestQueue.getInstance().addRequest(updateRequest,this);
	}

	private StringBuilder getPageAtUrl(String strUrl,boolean silent) {
		// Open stuff
		StringBuilder r = null;
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e) {
			if (!silent) {
				Messenger.getInstance().error(this,"URL error: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
			}
			return r;
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			if (!silent) {
				Messenger.getInstance().error(this,"HTTP connection error: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
			}
			return r;
		}
		InputStream inputStream = null;
		try {
			inputStream = con.getInputStream();
		} catch (IOException e) {
			if (!silent) {
				Messenger.getInstance().error(this,"Input stream error: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
			}
			return r;
		}
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			if (!silent) {
				Messenger.getInstance().error(this,"Input stream reader error: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
			}
			return r;
		}
		final BufferedReader reader = new BufferedReader(inputStreamReader);
		
		// Read page
		String line = "";
		r = new StringBuilder();
		try {
			while ((line = reader.readLine()) != null) {
				if (line.length()>0) {
					line = Generic.replaceNonAllowedCharactersInString(line,Generic.ALPHANUMERIC + " .,!@#$%^&*()_+-=[]{}\\;:<>/?~`’'\""," ");
					line = line.replaceAll("’","'");
					r.append(line);
					r.append("\n");
				}
			}
		} catch (IOException e) {
			if (!silent) {
				Messenger.getInstance().error(this,"Error while reading page: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
			}
		}

		// Close stuff
		try {
			reader.close();
		} catch (IOException e) {
			if (!silent) {
				Messenger.getInstance().error(this,"Error closing reader: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
			}
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			if (!silent) {
				Messenger.getInstance().error(this,"Error closing input stream: " + e + "\n" + Generic.getCallStackString(e.getStackTrace(),""));
			}
		}
		con.disconnect();
		
		return r;
	}
}
