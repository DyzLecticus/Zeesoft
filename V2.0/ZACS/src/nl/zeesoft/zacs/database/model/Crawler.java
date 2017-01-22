package nl.zeesoft.zacs.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Crawler extends HlpObject {
	private boolean				crawl						= false;
	private String				crawlUrl					= "https://www.edge.org";
	private boolean				respectRobotSpec			= true;
	private int					maxCrawlUrls				= 10;
	private int					minTextLength				= 500;
	private boolean 			parseLists					= false;

	private StringBuilder		crawledUrls					= new StringBuilder();
	private StringBuilder		scrapedText					= new StringBuilder();
	private int					scrapedTextLength			= 0;

	private boolean				convertTextToExamples		= false;
	private int					convertMaxExamples			= 100;
	private int					convertSentenceMinLength	= 20;
	private StringBuilder		convertContext				= new StringBuilder();
	private boolean				convertContextAssociate		= false;

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("crawl")) {
			setCrawl(Boolean.parseBoolean(obj.getPropertyValue("crawl").toString()));
		}
		if (obj.hasPropertyValue("crawlUrl")) {
			setCrawlUrl(obj.getPropertyValue("crawlUrl").toString());
		}
		if (obj.hasPropertyValue("respectRobotSpec")) {
			setRespectRobotSpec(Boolean.parseBoolean(obj.getPropertyValue("respectRobotSpec").toString()));
		}
		if (obj.hasPropertyValue("maxCrawlUrls")) {
			setMaxCrawlUrls(Integer.parseInt(obj.getPropertyValue("maxCrawlUrls").toString()));
		}
		if (obj.hasPropertyValue("minTextLength")) {
			setMinTextLength(Integer.parseInt(obj.getPropertyValue("minTextLength").toString()));
		}
		if (obj.hasPropertyValue("parseLists")) {
			setParseLists(Boolean.parseBoolean(obj.getPropertyValue("parseLists").toString()));
		}
		if (obj.hasPropertyValue("crawledUrls")) {
			setCrawledUrls(obj.getPropertyValue("crawledUrls"));
		}
		if (obj.hasPropertyValue("scrapedText")) {
			setScrapedText(obj.getPropertyValue("scrapedText"));
		}
		if (obj.hasPropertyValue("scrapedTextLength")) {
			setScrapedTextLength(Integer.parseInt(obj.getPropertyValue("scrapedTextLength").toString()));
		}
		if (obj.hasPropertyValue("convertTextToExamples")) {
			setConvertTextToExamples(Boolean.parseBoolean(obj.getPropertyValue("convertTextToExamples").toString()));
		}
		if (obj.hasPropertyValue("convertMaxExamples")) {
			setConvertMaxExamples(Integer.parseInt(obj.getPropertyValue("convertMaxExamples").toString()));
		}
		if (obj.hasPropertyValue("convertSentenceMinLength")) {
			setConvertSentenceMinLength(Integer.parseInt(obj.getPropertyValue("convertSentenceMinLength").toString()));
		}
		if (obj.hasPropertyValue("convertContext")) {
			setConvertContext(obj.getPropertyValue("convertContext"));
		}
		if (obj.hasPropertyValue("convertContextAssociate")) {
			setConvertContextAssociate(Boolean.parseBoolean(obj.getPropertyValue("convertContextAssociate").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("crawl",new StringBuilder("" + isCrawl()));
		r.setPropertyValue("crawlUrl",new StringBuilder(getCrawlUrl()));
		r.setPropertyValue("respectRobotSpec",new StringBuilder("" + isRespectRobotSpec()));
		r.setPropertyValue("maxCrawlUrls",new StringBuilder("" + getMaxCrawlUrls()));
		r.setPropertyValue("minTextLength",new StringBuilder("" + getMinTextLength()));
		r.setPropertyValue("parseLists",new StringBuilder("" + isParseLists()));
		r.setPropertyValue("crawledUrls",getCrawledUrls());
		r.setPropertyValue("scrapedText",getScrapedText());
		r.setPropertyValue("scrapedTextLength",new StringBuilder("" + getScrapedTextLength()));
		r.setPropertyValue("convertTextToExamples",new StringBuilder("" + isConvertTextToExamples()));
		r.setPropertyValue("convertMaxExamples",new StringBuilder("" + getConvertMaxExamples()));
		r.setPropertyValue("convertSentenceMinLength",new StringBuilder("" + getConvertSentenceMinLength()));
		r.setPropertyValue("convertContext",getConvertContext());
		r.setPropertyValue("convertContextAssociate",new StringBuilder("" + isConvertContextAssociate()));
		return r;
	}

	/**
	 * @return the crawl
	 */
	public boolean isCrawl() {
		return crawl;
	}

	/**
	 * @param crawl the crawl to set
	 */
	public void setCrawl(boolean crawl) {
		this.crawl = crawl;
	}

	/**
	 * @return the crawlUrl
	 */
	public String getCrawlUrl() {
		return crawlUrl;
	}

	/**
	 * @param crawlUrl the crawlUrl to set
	 */
	public void setCrawlUrl(String crawlUrl) {
		this.crawlUrl = crawlUrl;
	}

	/**
	 * @return the respectRobotSpec
	 */
	public boolean isRespectRobotSpec() {
		return respectRobotSpec;
	}

	/**
	 * @param respectRobotSpec the respectRobotSpec to set
	 */
	public void setRespectRobotSpec(boolean respectRobotSpec) {
		this.respectRobotSpec = respectRobotSpec;
	}

	/**
	 * @return the maxCrawlUrls
	 */
	public int getMaxCrawlUrls() {
		return maxCrawlUrls;
	}

	/**
	 * @param maxCrawlUrls the maxCrawlUrls to set
	 */
	public void setMaxCrawlUrls(int maxCrawlUrls) {
		this.maxCrawlUrls = maxCrawlUrls;
	}

	/**
	 * @return the minTextLength
	 */
	public int getMinTextLength() {
		return minTextLength;
	}

	/**
	 * @param minTextLength the minTextLength to set
	 */
	public void setMinTextLength(int minTextLength) {
		this.minTextLength = minTextLength;
	}

	/**
	 * @return the crawledUrls
	 */
	public StringBuilder getCrawledUrls() {
		return crawledUrls;
	}

	/**
	 * @param crawledUrls the crawledUrls to set
	 */
	public void setCrawledUrls(StringBuilder crawledUrls) {
		this.crawledUrls = crawledUrls;
	}

	/**
	 * @return the scrapedText
	 */
	public StringBuilder getScrapedText() {
		return scrapedText;
	}

	/**
	 * @param scrapedText the scrapedText to set
	 */
	public void setScrapedText(StringBuilder scrapedText) {
		this.scrapedText = scrapedText;
	}

	/**
	 * @return the convertTextToExamples
	 */
	public boolean isConvertTextToExamples() {
		return convertTextToExamples;
	}

	/**
	 * @param convertTextToExamples the convertTextToExamples to set
	 */
	public void setConvertTextToExamples(boolean convertTextToExamples) {
		this.convertTextToExamples = convertTextToExamples;
	}

	/**
	 * @return the convertMaxExamples
	 */
	public int getConvertMaxExamples() {
		return convertMaxExamples;
	}

	/**
	 * @param convertMaxExamples the convertMaxExamples to set
	 */
	public void setConvertMaxExamples(int convertMaxExamples) {
		this.convertMaxExamples = convertMaxExamples;
	}

	/**
	 * @return the parseLists
	 */
	public boolean isParseLists() {
		return parseLists;
	}

	/**
	 * @param parseLists the parseLists to set
	 */
	public void setParseLists(boolean parseLists) {
		this.parseLists = parseLists;
	}

	/**
	 * @return the scrapedTextLength
	 */
	public int getScrapedTextLength() {
		return scrapedTextLength;
	}

	/**
	 * @param scrapedTextLength the scrapedTextLength to set
	 */
	public void setScrapedTextLength(int scrapedTextLength) {
		this.scrapedTextLength = scrapedTextLength;
	}

	/**
	 * @return the convertContext
	 */
	public StringBuilder getConvertContext() {
		return convertContext;
	}

	/**
	 * @param convertContext the convertContext to set
	 */
	public void setConvertContext(StringBuilder convertContext) {
		this.convertContext = convertContext;
	}

	/**
	 * @return the convertSentenceMinLength
	 */
	public int getConvertSentenceMinLength() {
		return convertSentenceMinLength;
	}

	/**
	 * @param convertSentenceMinLength the convertSentenceMinLength to set
	 */
	public void setConvertSentenceMinLength(int convertSentenceMinLength) {
		this.convertSentenceMinLength = convertSentenceMinLength;
	}

	/**
	 * @return the convertContextAssociate
	 */
	public boolean isConvertContextAssociate() {
		return convertContextAssociate;
	}

	/**
	 * @param convertContextAssociate the convertContextAssociate to set
	 */
	public void setConvertContextAssociate(boolean convertContextAssociate) {
		this.convertContextAssociate = convertContextAssociate;
	}
}