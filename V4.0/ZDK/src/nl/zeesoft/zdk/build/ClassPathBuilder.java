package nl.zeesoft.zdk.build;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * This class can be used to scan a java source directory and build a class path for it.
 */
public class ClassPathBuilder {
	private List<String>	classPathElems = new ArrayList<String>();
	
	public String getClassPath(String sourceDir) {
		classPathElems.clear();
		
		File src = new File(sourceDir);
		if (src.exists()) {
			getPackages(src);
		} else {
			System.err.println("Source directory does not exist: " + src.getAbsolutePath());
		}
		
		ZStringBuilder classPath = new ZStringBuilder();
		for (String path: classPathElems) {
			if (classPath.length()>0) {
				classPath.append(" ");
			}
			path = path.substring((src.getAbsolutePath().length() + 1));
			classPath.append(path.replaceAll("\\\\","/"));
		}
		
		return classPath.toString();
	}
	
	protected void getPackages(File parent)  {
		for (File child: parent.listFiles()) {
			if (child.isFile()) {
				if (child.getName().endsWith(".java") ||
					child.getName().endsWith(".class")
					) {
					if (!classPathElems.contains(parent.getAbsolutePath())) {
						classPathElems.add(parent.getAbsolutePath());
					}
				}
			} else {
				getPackages(child);
			}
		}
	}
}
