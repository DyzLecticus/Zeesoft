package nl.zeesoft.zdk.test.code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import nl.zeesoft.zdk.Util;
import nl.zeesoft.zdk.code.CodeFile;

public class CodeFileReader {
	public List<CodeFile> readFiles() {
		return readFiles("src", ".java");
	}
	
	public List<CodeFile> readFiles(String sourceDir, String endsWith) {
		List<CodeFile> r = new ArrayList<CodeFile>();
		File src = new File(sourceDir);
		if (src.exists() && src.canRead()) {
			readFiles(src, endsWith, r);
		}
		return r;
	}
	
	protected void readFiles(File parent, String endsWith, List<CodeFile> files) {
		for (File file: parent.listFiles()) {
			if (file.canRead() && file.getName().endsWith(endsWith)) {
				files.add(new CodeFile(file.getPath(), readFile(file)));
			}
		}
		for (File file: parent.listFiles()) {
			if (file.canRead() && file.isDirectory()) {
				readFiles(file, endsWith, files);
			}
		}
	}

	protected StringBuilder readFile(File file) {
		StringBuilder r = new StringBuilder();
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				Util.appendLine(r, scanner.nextLine());
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return r;
	}
}
