package nl.zeesoft.zwc.output;

import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * Crawler demo output converter.
 */
public class CrawlConverter {
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	public static void main(String[] args) {
		String inputFile = "";
		String outputFile = "";
		if (args!=null && args.length>=3) {
			inputFile = args[1].trim();
			outputFile = args[2].trim();
		}
		if (inputFile.length()==0) {
			System.err.println("ERROR: Crawler output conversion expects the full file name of an input and an output file.");
			return;
		}

		System.out.println("Reading input ...");
		ZStringBuilder input = new ZStringBuilder();
		String err = input.fromFile(inputFile);
		if (err.length()>0) {
			System.err.println("ERROR: " + err);
			return;
		}

		SortedMap<String,String> qna = new TreeMap<String,String>();
		SortedMap<String,String> qnaV = new TreeMap<String,String>();

		System.out.println("Parsing input ...");
		List<ZStringBuilder> lines = input.split("\n");
		for (ZStringBuilder line: lines) {
			List<ZStringBuilder> lineUrl = line.split("\t");
			String url = lineUrl.get(0).toString();
			url = url.replace(".","");
			url = url.replace("-","");
			url = url.replace(".html","");
			url = url.replace(".htm","");
			if (lineUrl.size()>1 && lineUrl.get(1).length()>0) {
				ZStringSymbolParser symbols = new ZStringSymbolParser(lineUrl.get(1));
				List<String> lineSymbols = symbols.toSymbolsPunctuated();
				String question = "";
				String answer = "";
				String variable = "";
				String[] urlElem = url.split("/");
				if (urlElem.length>3) {
					int max = urlElem.length;
					if (max > 6) {
						max = 6;
					}
					for (int i = 3; i< max; i++) {
						if (urlElem[i].length()>0) {
							if (variable.length()<=0) {
								variable += urlElem[i];
							} else {
								if (urlElem[i].length()>0) {
									variable += urlElem[i].substring(0,1).toUpperCase();
								}
								if (urlElem[i].length()>1) {
									variable += urlElem[i].substring(1).toLowerCase();
								}
							}
						}
					}
				}
				ZStringBuilder text = new ZStringBuilder();
				for (String symbol: lineSymbols) {
					if (symbol.equals("?")) {
						text.append(symbol);
						boolean valid = false;
						for (int i = 0; i<ALPHABET.length();i++) {
							String c = ALPHABET.substring(i,(i+1));
							if (text.startsWith(c)) {
								valid = true;
								break;
							}
						}
						if (valid) {
							question = text.toString();
						}
						answer = "";
						text = new ZStringBuilder();
					} else if (symbol.equals(".") || symbol.equals("!")) {
						text.append(symbol);
						answer = text.toString();
						if (question.length()>0) {
							String qnaAnswer = qna.get(question);
							if (qnaAnswer!=null && qnaAnswer.length()>0 && !qnaAnswer.contains(answer)) {
								String[] spl = qnaAnswer.split("\\. ");
								if (spl.length<3) {
									answer = qnaAnswer + " " + answer;
								}
							}
							qna.put(question,answer);
							qnaV.put(question,variable);
						}
						text = new ZStringBuilder();
						answer = "";
					} else {
						if (text.length()>0) {
							text.append(" ");
						}
						text.append(symbol);
					}
				}
			}
		}
		
		ZStringBuilder output = new ZStringBuilder();
		output.append("Question");
		output.append("\t");
		output.append("Answer");
		output.append("\t");
		output.append("Variable");
		output.append("\n");
		for (Entry<String,String> entry: qna.entrySet()) {
			String variable = qnaV.get(entry.getKey());
			output.append(entry.getKey());
			output.append("\t");
			output.append(entry.getValue());
			output.append("\t");
			if (variable!=null) {
				output.append(variable);
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
