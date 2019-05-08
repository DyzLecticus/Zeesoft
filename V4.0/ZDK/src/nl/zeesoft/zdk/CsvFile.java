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
		String pChr = "";
		boolean inQuote = false;
		List<ZStringSymbolParser> values = new ArrayList<ZStringSymbolParser>();
		ZStringSymbolParser value = new ZStringSymbolParser();
		
		for (int i = 0; i < line.length(); i++) {
			String chr = line.substring(i,i+1).toString();
			if (chr.equals("\"") && !pChr.equals("\\")) {
				if (!inQuote) {
					inQuote = true;
				} else {
					inQuote = false;
				}
			} else if (!chr.equals(seperator) || inQuote) {
				value.append(chr);
			}
			if (chr.equals(seperator) && !inQuote) {
				values.add(transformValue(value));
				value = new ZStringSymbolParser();
			}
			pChr = chr;
		}
		if (value.length()>0 || pChr.equals(seperator)) {
			values.add(transformValue(value));
		}
		
		return values;
	}
	
	private ZStringSymbolParser transformValue(ZStringSymbolParser value) {
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
