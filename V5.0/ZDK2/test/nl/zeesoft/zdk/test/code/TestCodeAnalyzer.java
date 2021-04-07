package nl.zeesoft.zdk.test.code;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.code.CodeAnalyzer;
import nl.zeesoft.zdk.code.CodeFile;
import nl.zeesoft.zdk.code.MethodParser;

public class TestCodeAnalyzer {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);

		assert MethodParser.parseMethodPrefix(new StringBuilder("test"), 2).length() == 0;
		
		List<CodeFile> codeFiles = new ArrayList<CodeFile>();
		codeFiles.add(new CodeFile("nl/zeesoft/zdk/Number1.java", getMockCode(0)));
		codeFiles.add(new CodeFile("nl/zeesoft/zdk/Number2.java", getMockCode(1)));
		CodeAnalyzer analyzer = new CodeAnalyzer(codeFiles);
		analyzer.analyze();
		
		assert analyzer.fileStats.total == 2;
		assert analyzer.fileStats.totalValue == 32;
		assert analyzer.fileStats.avg == 16.0F;
		assert analyzer.fileStats.stdDev == 4.2426405F;
		
		assert analyzer.methodStats.total == 5;
		assert analyzer.methodStats.totalValue == 13;
		assert analyzer.methodStats.avg == 2.6F;
		assert analyzer.methodStats.stdDev == 1.3416407F;

		assert analyzer.toString().length() == 179;
		
		assert analyzer.getErrors().size() == 0;

		analyzer.maxLinesPerFile = 10;
		analyzer.maxLinesPerMethod = 2;
		assert analyzer.getErrors().size() == 3;
		assert analyzer.toString().length() == 393;
		
		analyzer.maxLinesPerFile = 1;
		analyzer.maxLinesPerMethod = 1;
		assert analyzer.getErrors().size() == 7;
		assert analyzer.toString().length() == 677;
		
		codeFiles.clear();
		analyzer = new CodeAnalyzer(codeFiles);
		analyzer.analyze();
		assert analyzer.toString().length() == 0;
		
		codeFiles.add(new CodeFile("nl/zeesoft/zdk/Number1.java", getMockCode(2)));
		analyzer = new CodeAnalyzer(codeFiles);
		analyzer.analyze();
		assert analyzer.toString().length() == 79;
	}
	
	public static StringBuilder getMockCode(int num) {
		StringBuilder r = new StringBuilder();
		Util.appendLine(r, "public class MockCode {");
		Util.appendLine(r, "	public int		number	= 0;");
		Util.appendLine(r, "	public String	test1	= \"}\";");
		Util.appendLine(r, "	public String	test2	= '}';");
		Util.appendLine(r, "	public String	test3	= \"\\\"\";");
		Util.appendLine(r, "	public String	test4	= \"'\";");
		if (num<2) {
			Util.appendLine(r, "	");
			Util.appendLine(r, "	public int getNumber() {");
			Util.appendLine(r, "		return number;");
			Util.appendLine(r, "	}");
			Util.appendLine(r, "	");
			Util.appendLine(r, "	protected void setNumber(int number) {");
			Util.appendLine(r, "		// Inline comment");
			Util.appendLine(r, "		this.number = number;");
			Util.appendLine(r, "	}");
			if (num==1) {
				Util.appendLine(r, "	");
				Util.appendLine(r, "	/*");
				Util.appendLine(r, "	 * Method comment");
				Util.appendLine(r, "	 */");
				Util.appendLine(r, "	private void increment(int number) {");
				Util.appendLine(r, "		this.number += number;");
				Util.appendLine(r, "		if (number == 10) {");
				Util.appendLine(r, "			number = 10;");
				Util.appendLine(r, "		}");
				Util.appendLine(r, "	}");
				Util.appendLine(r, "	");
			}
		}
		Util.appendLine(r, "}");
		return r;
	}
}
