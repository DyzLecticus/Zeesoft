package nl.zeesoft.zwc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zwc.page.PageReader;
import nl.zeesoft.zwc.page.RobotsParser;

public class Crawler {
	private Messenger		messenger		= null;
	private WorkerUnion		union			= null;
	
	private	String			baseUrl			= "";
	private String			outputDir		= System.getProperty("user.home") + "/ZWC/";
	private int				delayMs			= 1000;
	
	private PageReader		pageReader		= null;
	private RobotsParser	robotsParser	= null;
	private List<String>	disallowedUrls	= new ArrayList<String>();
	
	private List<String>	crawlUrls		= new ArrayList<String>();
	private List<String>	crawledUrls		= new ArrayList<String>();
	
	public Crawler(Messenger msgr, WorkerUnion uni, String baseUrl) {
		messenger = msgr;
		union = uni;
		this.baseUrl = baseUrl;
	}
	
	public Crawler(String baseUrl) {
		this.baseUrl = baseUrl;
	}
	
	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
	public String initialize() {
		String err = "";
		if (baseUrl.length()==0) {
			err = "Base URL is empty";
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
		if (err.length()>0 && messenger!=null) {
			messenger.error(this,err);
		}
		pageReader = new PageReader(messenger);
		robotsParser = new RobotsParser(pageReader,baseUrl);
		disallowedUrls = robotsParser.getDisallowedUrls();
		return err;
	}
	
	public void start() {
		ZStringBuilder page = pageReader.getPageAtUrl(baseUrl);
	}
}
