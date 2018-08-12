package nl.zeesoft.zsds.dialogs;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zsd.dialog.DialogInstanceHandler;
import nl.zeesoft.zsd.dialog.DialogVariableValue;
import nl.zeesoft.zsd.http.ZHttpRequest;
import nl.zeesoft.zsds.AppConfiguration;

public abstract class StateAccuracyHandler extends DialogInstanceHandler {
	@Override
	protected String initializeVariables() {
		String url = getConfig().getBase().getParameters().get(AppConfiguration.PARAMETER_SELF_TEST_SUMMARY_URL);
		ZHttpRequest http = new ZHttpRequest(getMessenger(),"GET",url);
		http.setTimeoutMs(1000);
		JsFile json = http.sendJsonRequest();
		if (json.rootElement!=null) {
			JsElem totals = json.rootElement.getChildByName("totals");
			if (totals!=null) {
				String exact = getExactly();
				float successPercentage = totals.getChildFloat("successPercentage");
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
				setDialogVariableValue(StateAccuracy.VARIABLE_EXACT,exact);
				setDialogVariableValue(StateAccuracy.VARIABLE_PERCENTAGE,percentage);
				float baseLineDifference = totals.getChildFloat("baseLineDifference",0F);
				setDialogVariableValue(StateAccuracy.VARIABLE_DIFFERENCE,"" + baseLineDifference);
			} else if (json.rootElement.getChildByName("error")!=null) {
				String code = json.rootElement.getChildString("code","503");
				setDialogVariableValue(StateAccuracy.VARIABLE_RESPONSE_CODE,code);
				getResponseOutput().output = new ZStringSymbolParser(getBusyResponse());
			}
		} else {
			// TODO: Fix no output handling (when no summary is available)
			getResponseOutput().output = new ZStringSymbolParser(getJsonRequestFailedResponse());
			setDialogVariableValue(StateAccuracy.VARIABLE_RESPONSE_CODE,"500");
		}
		return super.initializeVariables();
	}
	
	@Override
	protected void setPrompt(String promptVariable) {
		DialogVariableValue responseCode = getResponseOutput().values.get(StateAccuracy.VARIABLE_RESPONSE_CODE);
		if (responseCode!=null) {
			if (responseCode.externalValue.equals("503")) {
				getResponseOutput().output = new ZStringSymbolParser(getBusyResponse());
			} else {
				getResponseOutput().output = new ZStringSymbolParser(getJsonRequestFailedResponse());
			}
		} else if (
			!getResponseOutput().values.containsKey(StateAccuracy.VARIABLE_EXACT) ||
			!getResponseOutput().values.containsKey(StateAccuracy.VARIABLE_PERCENTAGE)
			) {
			getResponseOutput().output = new ZStringSymbolParser(getJsonRequestFailedResponse());
		}
		DialogVariableValue difference = getResponseOutput().values.get(StateAccuracy.VARIABLE_DIFFERENCE);
		if (difference!=null) {
			float diff = Float.parseFloat(difference.externalValue);
			if (diff>0F) {
				getResponseOutput().appendOutput(getAccuracyIncreasedResponse());
			} else if (diff<0F) {
				getResponseOutput().appendOutput(getAccuracyIncreasedResponse());
			}
		}
		super.setPrompt(promptVariable);
	}
	
	protected String getExactly() {
		return "==";
	}
	
	protected String getAbout() {
		return "+-";
	}

	protected abstract String getAccuracyIncreasedResponse();
	
	protected abstract String getAccuracyDecreasedResponse();
	
	protected abstract String getBusyResponse();
	
	protected abstract String getJsonRequestFailedResponse();
}
