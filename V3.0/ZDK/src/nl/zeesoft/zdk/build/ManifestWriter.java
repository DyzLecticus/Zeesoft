package nl.zeesoft.zdk.build;

import nl.zeesoft.zdk.ZStringBuilder;

/**
 * This class can be used to write a manifest file for a certain source directory.
 */
public class ManifestWriter {
	public String writeManifest(String builtBy,String mainClass,String sourceDir,String fileName,String jars) {
		String err = "";
		ZStringBuilder manifest = new ZStringBuilder();
		manifest.append("Manifest-Version: 1.0\n");
		
		if (builtBy.length()>0) {
			manifest.append("Built-By: ");
			manifest.append(builtBy);
			manifest.append("\n");
		}

		if (mainClass.length()>0) {
			manifest.append("Main-Class: ");
			manifest.append(mainClass);
			manifest.append("\n");
		}
		
		if (sourceDir.length()>0 || jars.length()>0) {
			manifest.append("Class-Path: ");
			if (sourceDir.length()>0) {
				ClassPathBuilder cpb = new ClassPathBuilder();
				String classPath = cpb.getClassPath(sourceDir);
				manifest.append(classPath);
				if (jars.length()>0) {
					manifest.append(" ");
				}
			}
			if (jars.length()>0) {
				manifest.append(jars);
			}
			manifest.append("\n");
		}
		
		err = manifest.toFile(fileName);
		
		return err;
	}
}
