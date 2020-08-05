package nl.zeesoft.mftl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Calendar;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.thread.CodeRunner;
import nl.zeesoft.zdk.thread.RunCode;

public class MFTL  {
	private static final String		EXECUTABLE			= "MinecraftLauncher.exe";
	private static final String		USERCACHE			= "/AppData/Roaming/.minecraft/usercache.json";
	private static final String		FIND_PREFIX			= "\"expiresOn\":\"20";

	private static final String		SAVES				= "/AppData/Roaming/.minecraft/saves/";
	private static final String		DEMO_WORLD			= "Demo_World/";
	private static final String		SESSION_LOCK		= "session.lock";

	private static String 			launcherInstallPath	= "C:\\Program Files (x86)\\Minecraft Launcher";
	
	public static void main(String[] args) {
		File u = new File(getUserCacheFilePath());
		File l = new File(launcherInstallPath + "/" + EXECUTABLE);
		if (checkFileExists(u) && checkFileExists(l)) {
			Str err = rewriteUserCache(u);
			if (err.length()>0) {
				System.err.println(err);
			} else {
				CodeRunner runner = getSaveBackupMonitor();
				runner.start();
				runLauncher(l);
				runner.stop();
			}
		}
	}
	
	private static String getUserCacheFilePath() {
		String path = System.getProperty("user.home");
		path += USERCACHE;
		return path;
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
	
	private static void backupDemoWorld() {
		File s = new File(getSavesPath() + DEMO_WORLD);
		if (s.exists()) {
			Calendar cal = Calendar.getInstance();
			int minute = cal.get(Calendar.MINUTE);
			minute = minute - (minute % 10);
			String timeStamp = cal.get(Calendar.YEAR)
			    + "-" + String.format("%02d", (cal.get(Calendar.MONTH) + 1))
			    + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH))
			    + "_"+ String.format("%02d", cal.get(Calendar.HOUR_OF_DAY))
			    + "-"+ String.format("%02d", minute)
				;
			String path = getSavesPath() + timeStamp + "-" + DEMO_WORLD;
			File t = new File(path);
			System.out.println("Creating backup: " + t.getAbsolutePath());
			copyPathRecursive(s.getAbsolutePath(), t.getAbsolutePath());
		}
	}
	
	private static void copyPathRecursive(String sourcePath, String targetPath) {
		if (!sourcePath.endsWith(SESSION_LOCK)) {
			File s = new File(sourcePath);
			File t = new File(targetPath);
	        Path source = Paths.get(s.getAbsolutePath());
	        Path target = Paths.get(t.getAbsolutePath());
	        try {
				Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
			} catch (DirectoryNotEmptyException de) {
				// Ignore
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (s.isDirectory()) {
				for (File f: s.listFiles()) {
					String path = targetPath + "/" + f.getName();
					//System.out.println(path + " ...");
					copyPathRecursive(sourcePath + "/" + f.getName(), path);
				}
			}
		}
	}
	
	private static String getSavesPath() {
		String path = System.getProperty("user.home");
		path += SAVES;
		return path;
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
	
	private static CodeRunner getSaveBackupMonitor() {
		CodeRunner r = new CodeRunner(getSaveBackupMonitorCode());
		r.setSleepMs(1000);
		return r;
	}
	
	private static RunCode getSaveBackupMonitorCode() {
		RunCode r = new RunCode() {
			FileTime mt = null;
			
			@Override
			protected boolean run() {
				Path file = Paths.get(getSavesPath() + DEMO_WORLD + SESSION_LOCK);
				BasicFileAttributes attr = null;
				try {
					attr = Files.readAttributes(file, BasicFileAttributes.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (attr!=null) {
					FileTime nmt = attr.lastModifiedTime();
					if (mt == null || nmt.compareTo(mt) > 0) {
						backupDemoWorld();
						mt = nmt;
					}
				}
				return false;
			}
			
		};
		return r;
	}
}
