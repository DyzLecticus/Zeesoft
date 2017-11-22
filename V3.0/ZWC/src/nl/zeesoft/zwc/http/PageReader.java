package nl.zeesoft.zwc.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.messenger.Messenger;

/**
 * A PageReader can be used to read a web page at a specified URL
 */
public class PageReader {
	private Messenger messenger = null;

	public PageReader() {
		// Messenger is optional
	}

	public PageReader(Messenger msgr) {
		messenger = msgr;
	}
	
	public ZStringBuilder getPageAtUrl(String strUrl) {
		boolean error = false;
		
		// Open stuff
		ZStringBuilder r = null;
		URL url = null;
		try {
			url = new URL(strUrl);
		} catch (MalformedURLException e) {
			if (messenger!=null) {
				messenger.error(this,"Malformed URL: " + url,e);
			}
			error = true;
		}
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			if (messenger!=null) {
				messenger.error(this,"HTTP connection error",e);
			}
			error = true;
		}
		InputStream inputStream = null;
		try {
			inputStream = con.getInputStream();
		} catch (IOException e) {
			if (messenger!=null) {
				messenger.error(this,"Input stream error",e);
			}
			error = true;
		}
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			if (messenger!=null) {
				messenger.error(this,"Input stream reader error",e);
			}
			error = true;
		}
		BufferedReader reader = null;
				
		// Read page
		if (!error) {
			reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			r = new ZStringBuilder();
			try {
				while ((line = reader.readLine()) != null) {
					if (line.length()>0) {
						r.append(line);
						r.append("\n");
					}
				}
			} catch (IOException e) {
				if (messenger!=null) {
					messenger.error(this,"Error while reading page",e);
				}
				error = true;
			}
		}

		// Close stuff
		try {
			reader.close();
		} catch (IOException e) {
			if (messenger!=null) {
				messenger.error(this,"Error closing reader",e);
			}
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			if (messenger!=null) {
				messenger.error(this,"Error closing input stream",e);
			}
		}
		con.disconnect();
		
		return r;
	}

}
