package nl.zeesoft.zsds.dialogs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogResponse;
import nl.zeesoft.zsd.dialog.DialogResponseOutput;
import nl.zeesoft.zsd.dialog.DialogVariableValue;

public abstract class StateAccuracyHandler extends DialogInstanceHandler {
	// TODO: add to base configuration?
	private static final String	SELF_TEST_SUMMARY_URL	= "http://localhost:8080/ZSDS/selfTestSummary.json";
	
	@Override
	public void buildDialogResponseOutput(DialogResponse r,DialogResponseOutput dro,List<DialogVariableValue> updatedValues,String promptVariable) {
		JsFile json = sendGetJsonRequest(SELF_TEST_SUMMARY_URL);
		if (json.rootElement!=null) {
			if (json.rootElement.getChildByName("totals")!=null) {
				String exact = getExactly();
				float successPercentage = json.rootElement.getChildByName("totals").getChildFloat("successPercentage");
				String[] str = ("" + successPercentage).split("\\.");
				int i = Integer.parseInt(str[0]);
				float d = 0F;
				if (str.length>1) {
					d = Float.parseFloat("0." + str[1]);
					if (d>0) {
						exact = getAbout();
						if (d>=0.5F) {
							i++;
						}
					}
				}
				String percentage = getExternalValueForNumber(i);
				dro.setDialogVariableValue(r,StateAccuracy.VARIABLE_EXACT,exact);
				dro.setDialogVariableValue(r,StateAccuracy.VARIABLE_PERCENTAGE,percentage);
			} else if (json.rootElement.getChildByName("error")!=null) {
				dro.output = new ZStringSymbolParser(getBusyResponse());
			}
		} else {
			dro.output = new ZStringSymbolParser(getJsonRequestFailedResponse());
		}
		super.buildDialogResponseOutput(r,dro,updatedValues,promptVariable);
	}
	
	protected String getExactly() {
		return "==";
	}
	
	protected String getAbout() {
		return "+-";
	}
	
	protected abstract String getBusyResponse();
	
	protected abstract String getJsonRequestFailedResponse();
	
	// TODO: Move to dialog instance handler?
	protected JsFile sendGetJsonRequest(String url) {
		JsFile r = new JsFile();
		SortedMap<String,String> properties = new TreeMap<String,String>();
		properties.put("Content-Type","application/json");
		ZStringBuilder json = sendGetRequest(url,properties);
		r.fromStringBuilder(json);
		return r;
	}
	
	// TODO: Move to dialog instance handler?
	protected ZStringBuilder sendGetRequest(String url,SortedMap<String,String> properties) {
		ZStringBuilder r = null;
		boolean error = false;

		// Open stuff
		URL conUrl = null;
		try {
			conUrl = new URL(url);
		} catch (MalformedURLException e) {
			error = true;
		}
		HttpURLConnection con = null;
		if (!error) {
			try {
				con = (HttpURLConnection) conUrl.openConnection();
				con.setRequestMethod("GET");
				for (Entry<String,String> entry: properties.entrySet()) {
					con.addRequestProperty(entry.getKey(),entry.getValue());
				}
			} catch (IOException e) {
				error = true;
			}
		}
		InputStream inputStream = null;
		if (!error) {
			try {
				inputStream = con.getInputStream();
			} catch (IOException e) {
				error = true;
			}
		}
		InputStreamReader inputStreamReader = null;
		if (!error) {
			try {
				inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				error = true;
			}
		}
		BufferedReader reader = null;
				
		// Read response
		if (!error) {
			reader = new BufferedReader(inputStreamReader);
			
			String line = "";
			r = new ZStringBuilder();
			try {
				while ((line = reader.readLine()) != null) {
					if (line.length()>0) {
						if (r.length()>0) {
							r.append("\n");
						}
						r.append(line);
					}
				}
			} catch (IOException e) {
				error = true;
			}
		}

		// Close stuff
		if (reader!=null) {
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
		if (inputStreamReader!=null) {
			try {
				inputStreamReader.close();
			} catch (IOException e) {
			}
		}
		if (inputStream!=null) {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
		}
		if (con!=null) {
			con.disconnect();
		}
		
		r.trim();
		
		return r;
	}
	
	private String getExternalValueForNumber(int num) {
		return getConfig().getEntityValueTranslator().getEntityObject(getDialog().getLanguage(),BaseConfiguration.TYPE_NUMERIC).getExternalValueForInternalValue("" + num);
	}
}
