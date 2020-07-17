package nl.zeesoft.mftl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import nl.zeesoft.zdk.Str;

public class MFTL  {
	private static final String		EXECUTABLE			= "MinecraftLauncher.exe";
	private static final String		USERCACHE			= "/AppData/Roaming/.minecraft/usercache.json";
	private static final String		FIND_PREFIX			= "\"expiresOn\":\"20";
	
	private static String 			launcherInstallPath	= "C:\\Program Files (x86)\\Minecraft Launcher";
	
	public static void main(String[] args) {
		File u = new File(getUserCacheFilePath());
		File l = new File(launcherInstallPath + "/" + EXECUTABLE);
		if (checkFileExists(u) && checkFileExists(l)) {
			Str err = rewriteUserCache(u);
			if (err.length()>0) {
				System.err.println(err);
			} else {
				runLauncher(l);
			}
		}
	}
	
	private static String getUserCacheFilePath() {
		String dir = System.getProperty("user.home");
		dir += USERCACHE;
		return dir;
	}
	
	private static boolean checkFileExists(File f) {
		boolean r = f.exists();
		if (!r) {
			System.err.println("File not found: " + f.getAbsolutePath());
		}
		return r;
	}
	
	private static Str rewriteUserCache(File u) {
		Str userCache = new Str();
		userCache.fromFile(u.getAbsolutePath());
		for (int i = 20; i <= 40; i++) {
			String find = FIND_PREFIX + String.format("%02d",i) + "-";
			userCache.replace(find,FIND_PREFIX + "40-");
		}
		System.out.println(u.getAbsolutePath() + ": " + userCache);
		return userCache.toFile(u.getAbsolutePath());
	}
	
	private static void runLauncher(File l) {
		System.out.println(l.getAbsolutePath() + "... ");
		try {
		    Process process = Runtime.getRuntime().exec(l.getAbsolutePath());
		    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		    String line;
		    while ((line = reader.readLine()) != null) {
		        System.err.println(line);
		    }
		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
}
