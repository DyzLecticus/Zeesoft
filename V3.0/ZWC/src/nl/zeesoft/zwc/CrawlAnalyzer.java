package nl.zeesoft.zwc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * Crawler demo output analyzer.
 */
public class CrawlAnalyzer {
	public static void main(String[] args) {
		/*
		List<String> lineEndSymbols = new ArrayList<String>();
		lineEndSymbols.add(".");
		lineEndSymbols.add("?");
		lineEndSymbols.add("!");
		List<String> punctuationSymbols = new ArrayList<String>();
		punctuationSymbols.add("(");
		punctuationSymbols.add(")");
		punctuationSymbols.add(":");
		punctuationSymbols.add(";");
		punctuationSymbols.add(",");
		punctuationSymbols.add("\"");
		punctuationSymbols.add("'");
		*/
		
		String inputFile = "";
		String outputFile = "";
		if (args!=null && args.length>=3) {
			inputFile = args[1].trim();
			outputFile = args[2].trim();
		}
		if (inputFile.length()==0) {
			System.err.println("ERROR: Crawler output analysis expects the full file name of an input and an output file.");
			return;
		}

		System.out.println("Reading input ...");
		ZStringBuilder input = new ZStringBuilder();
		String err = input.fromFile(inputFile);
		if (err.length()>0) {
			System.err.println("ERROR: " + err);
			return;
		}
		
		SortedMap<String,List<String>> symbolUrls = new TreeMap<String,List<String>>();
		SortedMap<String,Integer> symbolCounts = new TreeMap<String,Integer>();
		
		System.out.println("Parsing input ...");
		List<ZStringBuilder> lines = input.split("\n");
		for (ZStringBuilder line: lines) {
			List<ZStringBuilder> lineUrl = line.split("\t");
			String url = lineUrl.get(0).toString();
			if (lineUrl.size()>1 && lineUrl.get(1).length()>0) {
				ZStringSymbolParser symbols = new ZStringSymbolParser(lineUrl.get(1));
				//List<String> lineSymbols = symbols.toSymbolsPunctuated(lineEndSymbols,punctuationSymbols);
				List<String> lineSymbols = symbols.toSymbolsPunctuated();
				for (String symbol: lineSymbols) {
					List<String> urls = symbolUrls.get(symbol);
					Integer count = symbolCounts.get(symbol);
					if (urls==null) {
						urls = new ArrayList<String>();
						count = 0;
						symbolUrls.put(symbol,urls);
					}
					urls.add(url);
					count++;
					symbolCounts.put(symbol,count);
				}
			}
		}
		
		ZStringBuilder output = new ZStringBuilder();
		for (Entry<String,List<String>> entry: symbolUrls.entrySet()) {
			String symbol = entry.getKey();
			Integer count = symbolCounts.get(symbol);
			output.append(symbol);
			output.append("\t");
			output.append(count.toString());
			output.append("\t");
			for (String url: entry.getValue()) {
				output.append(url);
				output.append(" ");
			}
			output.append("\n");
		}

		System.out.println("Writing output ...");
		err = output.toFile(outputFile);
		if (err.length()>0) {
			System.err.println("ERROR: " + err);
		}
		System.out.println("Done");
	}
}
