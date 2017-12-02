package nl.zeesoft.zwc;

import java.util.List;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zwc.page.PageTextParser;

/**
 * Crawler demo implementation
 */
public class Crawl {
	public static void main(String[] args) {
		boolean showRemaining = false;
		
		JFrame frame = new JFrame();
		String baseUrl = "";
		if (args!=null && args.length>1) {
			baseUrl = args[1];
		} else {
			showRemaining = true;
			baseUrl = JOptionPane.showInputDialog(frame, "Enter the start URL to crawl");
		}
		
		Crawler crawler = new Crawler(baseUrl);
		String err = crawler.initialize();
		if (err.length()>0) {
			System.err.println("ERROR: " + err);
			return;
		}
		crawler.start();
		
		while(!crawler.isDone()) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (showRemaining) {
				System.out.println("Remaining: " + crawler.getRemaining() + "\t");
			}
		}
		
		List<String> crawledUrls = crawler.getCrawledUrls();
		TreeMap<String,ZStringBuilder> pages = crawler.getPages();
		for (String url: crawledUrls) {
			ZStringBuilder page = pages.get(url);
			if (page!=null) {
				PageTextParser parser = new PageTextParser(page);
				System.out.println(url + "\t" + parser.getText());
			} else {
				System.out.println(url + "\t");
			}
		}
		frame.setVisible(false);
		frame.dispose();
	}
}
