package zidl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final Logger log 				= LoggerFactory.getLogger(Config.class);

	private static Config		config				= null;
	
	private String 				zidsPostDialogUrl 	= "http://127.0.0.0/";
	
	private Config() {
		// Singleton
	}
	
	public static Config getInstance() {
		if (config==null) {
			config = new Config();
		}
		return config;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException(); 
	}
	
	/**
	 * @return the zidsPostDialogUrl
	 */
	public String getZidsPostDialogUrl() {
		log.info("ZIDS post dialog URL: {}", zidsPostDialogUrl);
		return zidsPostDialogUrl;
	}
}
