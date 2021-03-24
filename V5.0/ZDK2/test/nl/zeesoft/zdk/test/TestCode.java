package nl.zeesoft.zdk.test;

import java.util.List;

import nl.zeesoft.zdk.Console;
import nl.zeesoft.zdk.Logger;
import nl.zeesoft.zdk.code.Analyzer;
import nl.zeesoft.zdk.test.code.CodeFileReader;

public class TestCode {
	public static void main(String[] args) {
		Logger.setLoggerDebug(true);
		CodeFileReader reader = new CodeFileReader();
		Analyzer analyzer = new Analyzer(reader.readFiles());
		analyzer.analyze();
		Console.log(analyzer);
		List<String> errors = analyzer.getErrors();
		for (String error: errors) {
			Console.err(error);
		}
		assert errors.size() == 0;
	}
}
