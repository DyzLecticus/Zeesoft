package nl.zeesoft.zacs.simulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zacs.crawler.CrwController;
import nl.zeesoft.zacs.database.model.Crawler;
import nl.zeesoft.zacs.database.model.Example;
import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zacs.database.model.ZACSModel;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.Worker;
import nl.zeesoft.zodb.database.DbRequestQueue;
import nl.zeesoft.zodb.database.request.ReqAdd;
import nl.zeesoft.zodb.database.request.ReqDataObject;

public class SimConvertScrapedTextWorker extends Worker {
	private static SimConvertScrapedTextWorker	learnWorker			= null;
	private CrwController						controller			= null;
	private List<Crawler>						crawlers			= new ArrayList<Crawler>();
	private int									maxContextSymbols	= 8;
	
	private SimConvertScrapedTextWorker() {
		setSleep(10000);
	}

	public static SimConvertScrapedTextWorker getInstance() {
		if (learnWorker==null) {
			learnWorker = new SimConvertScrapedTextWorker();
		}
		return learnWorker;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}

	@Override
	public void start() {
		maxContextSymbols = SimController.getInstance().getControl().getContextSymbolMaximum();
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
		waitForStop(10,false);
	}
	
	@Override
	public void whileWorking() {
		if (controller==null) {
			controller = new CrwController();
			controller.initialize();
		} else {
			controller.reinitialize();
		}
		crawlers.clear();
		List<Crawler> crws = controller.getCrawlersAsList();
		for (Crawler crw: crws) {
			if (!crw.isCrawl() && crw.isConvertTextToExamples() && crw.getScrapedText().length()>0) {
				crawlers.add(crw);
			}
		}
		if (crawlers.size()>0) {
			Crawler crawler = crawlers.remove(0);
			Messenger.getInstance().debug(this,"Converting scraped text from URL: " + crawler.getCrawlUrl() + " ...");
			Date started = null;
			if (SimController.DEBUG_PERFORMANCE) {
				started = new Date();
			}
			boolean added = false;
			List<String> iol = new ArrayList<String>();
			List<String> sentences = Symbol.parseTextSentences(crawler.getScrapedText(),99999,crawler.getConvertSentenceMinLength());
			if (sentences.size()>1) {
				for (int i = 1; i<sentences.size(); i++) {
					String in = sentences.get(i-1);
					String out = sentences.get(i);
					String io = in + out;
					if (in.length()<1024 && out.length()<4096 && !iol.contains(io)) {
						StringBuilder context = SimController.getInstance().getSymbols().getContextForTextAndContext(new StringBuilder(io),crawler.getConvertContext(),0,maxContextSymbols,crawler.isConvertContextAssociate());
						if (context.length()<1024) {
							iol.add(io);
							ReqAdd addRequest = new ReqAdd(ZACSModel.EXAMPLE_CLASS_FULL_NAME);
							Example ex = new Example();
							ex.setContext(SimController.getInstance().getSymbols().getContextForTextAndContext(new StringBuilder(io),crawler.getConvertContext(),0,maxContextSymbols,crawler.isConvertContextAssociate()));
							ex.setInput(new StringBuilder(in));
							ex.setOutput(new StringBuilder(out));
							addRequest.getObjects().add(new ReqDataObject(ex.toDataObject()));
							DbRequestQueue.getInstance().addRequest(addRequest,this);
							sleep(10);
							added = true;
							if (iol.size()>=crawler.getConvertMaxExamples()) {
								break;
							}
						}
					}
				}
			}
			if (added) {
				crawler.setConvertTextToExamples(false);
				DbRequestQueue.getInstance().addRequest(crawler.getNewUpdateRequest(null),this);
				sleep(1000);
			}
			if (SimController.DEBUG_PERFORMANCE) {
				Messenger.getInstance().debug(this,"Converting scraped text from URL: " + crawler.getCrawlUrl() + " (length: " + crawler.getScrapedText().length() + ") took: " + ((new Date()).getTime() - started.getTime()) + " ms");
			} else {
				Messenger.getInstance().debug(this,"Converted scraped text from URL: " + crawler.getCrawlUrl());
			}
		}
	}
}
