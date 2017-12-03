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
		String baseUrl = "";
		String outputFile = "data.txt";
		
		if (args!=null && args.length>1) {
			baseUrl = args[1];
			if (args.length>2) {
				outputFile = args[2];
			}
		} else {
			JFrame frame = new JFrame();
			baseUrl = JOptionPane.showInputDialog(frame, "Enter the start URL to crawl");
			outputFile = JOptionPane.showInputDialog(frame, "Enter the full output file name");
			frame.setVisible(false);
			frame.dispose();
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
			System.out.println("Remaining: " + crawler.getRemaining());
		}
		
		List<String> crawledUrls = crawler.getCrawledUrls();
		TreeMap<String,ZStringBuilder> pages = crawler.getPages();
		ZStringBuilder output = new ZStringBuilder();
		for (String url: crawledUrls) {
			ZStringBuilder page = pages.get(url);
			if (page!=null) {
				PageTextParser parser = new PageTextParser(page);
				output.append(url);
				output.append("\t");
				output.append(parser.getText());
				output.append("\n");
			} else {
				output.append(url);
				output.append("\t");
				output.append("\n");
			}
		}
		
		err = output.toFile(outputFile);
		if (err.length()>0) {
			System.err.println("ERROR: " + err);
		}
	}
}
