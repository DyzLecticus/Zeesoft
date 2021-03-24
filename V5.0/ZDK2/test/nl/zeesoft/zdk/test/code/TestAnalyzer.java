package nl.zeesoft.zdk.test.code;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.code.Analyzer;
import nl.zeesoft.zdk.code.CodeFile;

public class TestAnalyzer {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		List<CodeFile> codeFiles = new ArrayList<CodeFile>();
		codeFiles.add(new CodeFile("nl/zeesoft/zdk/Number1.java", getMockCode(0)));
		codeFiles.add(new CodeFile("nl/zeesoft/zdk/Number2.java", getMockCode(1)));
		Analyzer analyzer = new Analyzer(codeFiles);
		analyzer.analyze();
		
		assert analyzer.fileStats.total == 2;
		assert analyzer.fileStats.totalValue == 23;
		assert analyzer.fileStats.avg == 11.5F;
		assert analyzer.fileStats.stdDev == 4.9497476F;
		
		assert analyzer.methodStats.total == 5;
		assert analyzer.methodStats.totalValue == 13;
		assert analyzer.methodStats.avg == 2.6F;
		assert analyzer.methodStats.stdDev == 1.3416407F;

		assert analyzer.toString().length() == 179;
		
		assert analyzer.getErrors().size() == 0;

		analyzer.maxLinesPerFile = 10;
		analyzer.maxLinesPerMethod = 2;
		assert analyzer.getErrors().size() == 2;
		assert analyzer.toString().length() == 339;
		
		analyzer.maxLinesPerFile = 1;
		analyzer.maxLinesPerMethod = 1;
		assert analyzer.getErrors().size() == 7;
		assert analyzer.toString().length() == 669;
	}
	
	public static StringBuilder getMockCode(int num) {
		StringBuilder r = new StringBuilder();
		Util.appendLine(r, "public class MockCode {");
		Util.appendLine(r, "	public int number = 0;");
		Util.appendLine(r, "	");
		Util.appendLine(r, "	public int getNumber() {");
		Util.appendLine(r, "		return number;");
		Util.appendLine(r, "	}");
		Util.appendLine(r, "	");
		Util.appendLine(r, "	public void setNumber(int number) {");
		Util.appendLine(r, "		// Inline comment");
		Util.appendLine(r, "		this.number = number;");
		Util.appendLine(r, "	}");
		if (num==1) {
			Util.appendLine(r, "	");
			Util.appendLine(r, "	/*");
			Util.appendLine(r, "	 * Method comment");
			Util.appendLine(r, "	 */");
			Util.appendLine(r, "	public void increment(int number) {");
			Util.appendLine(r, "		this.number += number;");
			Util.appendLine(r, "		if (number == 10) {");
			Util.appendLine(r, "			number = 10;");
			Util.appendLine(r, "		}");
			Util.appendLine(r, "	}");
			Util.appendLine(r, "}");
		}
		return r;
	}
}
