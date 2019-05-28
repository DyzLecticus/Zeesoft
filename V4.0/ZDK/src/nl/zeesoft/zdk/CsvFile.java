package nl.zeesoft.zdk;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;

/**
 * CSV file support class.
 */
public class CsvFile {
	public String						seperator	= ",";
	public int							columns 	= 0;
	public List<ZStringSymbolParser[]> 	rows		= new ArrayList<ZStringSymbolParser[]>();
	
	public ZStringBuilder fromFile(String fileName) {
		ZStringBuilder error = new ZStringBuilder();
		ZStringBuilder csv = new ZStringBuilder();
		error = csv.fromFile(fileName);
		if (error.length()==0) {
			csv.trim();
			fromStringBuilder(csv);
		}
		return error;
	}
	
	public void fromStringBuilder(ZStringBuilder str) {
		columns = 0;
		rows.clear();
		
		str.replace("\r","");
		str.replace("\\\"","\"\"");
		
		List<ZStringBuilder> lines = str.split("\n");
		int lineNum = 0;
		for (ZStringBuilder line: lines) {
			List<ZStringSymbolParser> values = parseValuesFromLine(line);
			if (lineNum==0) {
				columns = values.size();
			}
			addRow(values);
			lineNum++;
		}
	}
	
	private List<ZStringSymbolParser> parseValuesFromLine(ZStringBuilder line) {
		List<ZStringSymbolParser> values = new ArrayList<ZStringSymbolParser>();
		
		String chr = "";
		boolean add = true;
		String nChr = "";
		int quotes = 0;
		boolean inQuote = false;
		ZStringSymbolParser value = new ZStringSymbolParser();
		for (int i = 0; i < line.length(); i++) {
			chr = line.substring(i,i+1).toString();
			add = true;
			if (i < line.length()-1) {
				nChr = line.substring(i+1,i+2).toString();
			}
			if (!inQuote && value.length()==0 && chr.equals("\"")) {
				inQuote = true;
				add = false;
			} else if (inQuote && quotes % 2 == 0 && chr.equals("\"") && nChr.equals(seperator)) {
				inQuote = false;
				add = false;
			}
			if (chr.equals(seperator) && !inQuote) {
				values.add(transformValue(value));
				value = new ZStringSymbolParser();
				quotes = 0;
			} else if (add) {
				value.append(chr);
				if (chr.equals("\"")) {
					quotes++;
				}
			}
		}
		if (value.length()>0) {
			values.add(transformValue(value));
		}
		
		return values;
	}
	
	private ZStringSymbolParser transformValue(ZStringSymbolParser value) {
		value.replace("\"\"","\"");
		value.replace("\\\"","\"");
		value.replace("\\t","\t");
		value.replace("\\r","\r");
		value.replace("\\n","\n");
		return value;
	}
	
	private void addRow(List<ZStringSymbolParser> values) {
		ZStringSymbolParser[] row = new ZStringSymbolParser[columns];
		for (int i = 0; i < columns; i++) {
			ZStringSymbolParser value = null;
			if (i<values.size()) {
				value = values.get(i);
			} else {
				value = new ZStringSymbolParser();
			}
			row[i] = value;
		}
		rows.add(row);
	}
}
