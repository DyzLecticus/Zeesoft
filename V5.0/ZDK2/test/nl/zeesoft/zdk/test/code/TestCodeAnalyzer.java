package nl.zeesoft.zdk.test.code;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.code.CodeAnalyzer;
import nl.zeesoft.zdk.code.CodeFile;
import nl.zeesoft.zdk.code.MethodParser;
import nl.zeesoft.zdk.str.StrUtil;

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
		StrUtil.appendLine(r, "public class MockCode {");
		StrUtil.appendLine(r, "	public int		number	= 0;");
		StrUtil.appendLine(r, "	public String	test1	= \"}\";");
		StrUtil.appendLine(r, "	public String	test2	= '}';");
		StrUtil.appendLine(r, "	public String	test3	= \"\\\"\";");
		StrUtil.appendLine(r, "	public String	test4	= \"'\";");
		if (num<2) {
			StrUtil.appendLine(r, "	");
			StrUtil.appendLine(r, "	public int getNumber() {");
			StrUtil.appendLine(r, "		return number;");
			StrUtil.appendLine(r, "	}");
			StrUtil.appendLine(r, "	");
			StrUtil.appendLine(r, "	protected void setNumber(int number) {");
			StrUtil.appendLine(r, "		// Inline comment");
			StrUtil.appendLine(r, "		this.number = number;");
			StrUtil.appendLine(r, "	}");
			if (num==1) {
				StrUtil.appendLine(r, "	");
				StrUtil.appendLine(r, "	/*");
				StrUtil.appendLine(r, "	 * Method comment");
				StrUtil.appendLine(r, "	 */");
				StrUtil.appendLine(r, "	private void increment(int number) {");
				StrUtil.appendLine(r, "		this.number += number;");
				StrUtil.appendLine(r, "		if (number == 10) {");
				StrUtil.appendLine(r, "			number = 10;");
				StrUtil.appendLine(r, "		}");
				StrUtil.appendLine(r, "	}");
				StrUtil.appendLine(r, "	");
			}
		}
		StrUtil.appendLine(r, "}");
		return r;
	}
}
