package zidl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;

import nl.zeesoft.zids.json.JsElem;
import nl.zeesoft.zids.json.JsFile;
import nl.zeesoft.zids.server.SvrSessionHandler;

public class ZIDLSpeechlet implements Speechlet {
    private static final Logger log 					= LoggerFactory.getLogger(ZIDLSpeechlet.class);
    
    private static final String	INTENT_DIALOG			= "DialogIntent";

    private static final String	INTENT_AMZ_HELP 		= "AMAZON.HelpIntent";
    private static final String	INTENT_AMZ_STOP 		= "AMAZON.StopIntent";
    private static final String	INTENT_AMZ_CANCEL 		= "AMAZON.CANCELIntent";
    
    private static final String	HELP					= "You can say the word input, followed by something you want to say";

    private String				zidsPostDialogUrl		= "";
    
    public ZIDLSpeechlet(String zidsPostDialogUrl) {
    	this.zidsPostDialogUrl = zidsPostDialogUrl;
    }
    
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),session.getSessionId());
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),session.getSessionId());        
        return handleDialogIntent(null,session);
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;
        
        SpeechletResponse response = null;

        if (intentName!=null) {
        	if (intentName.equals(INTENT_DIALOG)) {
        		response = handleDialogIntent(intent,session);
        	} else if (intentName.equals(INTENT_AMZ_HELP)) {
        		response = handleHelpIntent(intent,session);
        	} else if (intentName.equals(INTENT_AMZ_STOP)) {
        		response = handleStopCancelIntent(intent,session);
        	} else if (intentName.equals(INTENT_AMZ_CANCEL)) {
        		response = handleStopCancelIntent(intent,session);
        	} else {
                throw new SpeechletException("Invalid Intent: " + intentName);
        	}
        }
        return response;
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),session.getSessionId());
        sendSessionEndedRequest(session);
    }

    /**
     * Passes input slot value to ZIDS and returns the output in response
     *
     * @param intent the intent object
     * @param session the session object
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse handleDialogIntent(final Intent intent, final Session session) {
        String speechOutput = "";

        // Reprompt speech will be triggered if the user doesn't respond.
        String repromptText = HELP;

        if (intent!=null) {
        	Map<String, Slot> slots = intent.getSlots();
        	Slot input = slots.get("input");

        	log.info("handleDialogIntent, input={}", input.getValue());
        	speechOutput = getSessionDialogResponse(session,input.getValue(),"");
        	log.info("handleDialogIntent, output={}", speechOutput);
        }
        
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ZIDL");
        card.setContent(speechOutput);

        SpeechletResponse response = null;
        if (speechOutput.endsWith("?")) {
        	response = newAskResponse(speechOutput, false,repromptText, false);
        } else {
        	response = newTellResponse(speechOutput,false);
        }
        response.setCard(card);
        return response;
    }

    /**
     * Sends a session ended request to ZIDS
     *
     * @param intent the intent object
     * @param session the session object
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse handleStopCancelIntent(final Intent intent, final Session session) {
        String speechOutput = "Goodbye";

    	log.info("handleStopCancelIntent");
    	sendSessionEndedRequest(session);
    	log.info("handleStopCancelIntent, output={}", speechOutput);
        
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ZIDL");
        card.setContent(speechOutput);

        SpeechletResponse response = newTellResponse(speechOutput, false);
        response.setCard(card);
        return response;
    }

    /**
     * @param intent the intent object
     * @param session the session object
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse handleHelpIntent(final Intent intent, final Session session) {
        String speechOutput = HELP;

    	log.info("handleHelpIntent");
    	sendSessionEndedRequest(session);
    	log.info("handleHelpIntent, output={}", speechOutput);
        
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("ZIDL");
        card.setContent(speechOutput);

        SpeechletResponse response = newTellResponse(speechOutput, false);
        response.setCard(card);
        return response;
    }

    /**
     * Wrapper for creating the Tell response from the input strings.
     * 
     * @param stringOutput
     *            the output to be spoken
     * @param isOutputSsml
     *            whether the output text is of type SSML
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newTellResponse(String stringOutput, boolean isOutputSsml) {
        OutputSpeech outputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }
        return SpeechletResponse.newTellResponse(outputSpeech);
    }
    
    /**
     * Wrapper for creating the Ask response from the input strings.
     * 
     * @param stringOutput
     *            the output to be spoken
     * @param isOutputSsml
     *            whether the output text is of type SSML
     * @param repromptText
     *            the reprompt for if the user doesn't reply or is misunderstood.
     * @param isRepromptSsml
     *            whether the reprompt text is of type SSML
     * @return SpeechletResponse the speechlet response
     */
    private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml, String repromptText, boolean isRepromptSsml) {
        OutputSpeech outputSpeech, repromptOutputSpeech;
        if (isOutputSsml) {
            outputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
        } else {
            outputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
        }

        if (isRepromptSsml) {
            repromptOutputSpeech = new SsmlOutputSpeech();
            ((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
        } else {
            repromptOutputSpeech = new PlainTextOutputSpeech();
            ((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
        }
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(repromptOutputSpeech);
        return SpeechletResponse.newAskResponse(outputSpeech, reprompt);
    }
    
    private String getSessionDialogResponse(final Session session,final String input, final String context) {
    	StringBuilder response = postSessionDialog(SvrSessionHandler.SESSION_PROCESS_INPUT_REQUEST,session.getSessionId(),input,context);
    	JsFile jsf = new JsFile();
    	jsf.fromStringBuilder(response);
    	JsElem el = jsf.rootElement.getChildByName("output");
    	return el.value.toString();
    }
    
    private void sendSessionEndedRequest(final Session session) {
    	postSessionDialog(SvrSessionHandler.SESSION_ENDED_REQUEST,session.getSessionId(),"","");
    }
    
    private StringBuilder postSessionDialog(final String type,final  String sessionId,final  String input,final  String context) {
    	StringBuilder response = new StringBuilder();
    	
    	JsFile jsf = new JsFile();
    	jsf.rootElement = new JsElem();

    	JsElem el = new JsElem();	
    	el.name = "type";
    	el.value = new StringBuilder(type);
    	el.cData = true;
    	jsf.rootElement.children.add(el);

    	el = new JsElem();	
    	el.name = "sessionId";
    	el.value = new StringBuilder(sessionId);
    	el.cData = true;
    	jsf.rootElement.children.add(el);

    	if (input.length()>0) {
        	el = new JsElem();	
        	el.name = "input";
        	el.value = new StringBuilder(input);
        	el.cData = true;
        	jsf.rootElement.children.add(el);
    	}
    	if (context.length()>0) {
        	el = new JsElem();	
        	el.name = "context";
        	el.value = new StringBuilder(context);
        	el.cData = true;
        	jsf.rootElement.children.add(el);
    	}
    	
		URL obj = null;
		HttpURLConnection con = null;
		DataOutputStream wr = null;
		BufferedReader in = null;
		try {
			obj = new URL(zidsPostDialogUrl);
			con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
	
			con.setDoOutput(true);
			con.setDoInput(true);

			wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(jsf.toStringBuilder().toString());
			wr.flush();
			wr.close();
	
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in!=null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (wr!=null) {
				try {
					wr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (con!=null) {
				con.disconnect();
			}
		}
    	
    	return response;
    }

}
