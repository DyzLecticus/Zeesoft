package nl.zeesoft.zids.database.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zids.json.JsElem;
import nl.zeesoft.zids.json.JsFile;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Session extends HlpObject {
	private String						sessionId				= "";
	private long						dateTimeStart			= 0;
	private long						dateTimeLastActivity	= 0;
	private long						dateTimeEnd				= 0;
	private StringBuilder				context					= new StringBuilder();
	private long						dialogId				= 0;
	private Dialog						dialog					= null;
	private StringBuilder				log						= new StringBuilder();
	private StringBuilder				logIncludingThoughts	= new StringBuilder();
	private StringBuilder				logAssignments			= new StringBuilder();
	private StringBuilder				output					= new StringBuilder();

	private List<SessionVariable>		variables				= new ArrayList<SessionVariable>();
	private List<SessionDialogVariable>	dialogVariables			= new ArrayList<SessionDialogVariable>();
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("sessionId")) {
			setSessionId(obj.getPropertyValue("sessionId").toString());
		}
		if (obj.hasPropertyValue("dateTimeStart")) {
			setDateTimeStart(Long.parseLong(obj.getPropertyValue("dateTimeStart").toString()));
		}
		if (obj.hasPropertyValue("dateTimeLastActivity")) {
			setDateTimeLastActivity(Long.parseLong(obj.getPropertyValue("dateTimeLastActivity").toString()));
		}
		if (obj.hasPropertyValue("dateTimeEnd")) {
			setDateTimeEnd(Long.parseLong(obj.getPropertyValue("dateTimeEnd").toString()));
		}
		if (obj.hasPropertyValue("dialog") && obj.getLinkValue("dialog")!=null && obj.getLinkValue("dialog").size()>0) {
			setDialogId(obj.getLinkValue("dialog").get(0));
		}
		if (obj.hasPropertyValue("context")) {
			setContext(obj.getPropertyValue("context"));
		}
		if (obj.hasPropertyValue("log")) {
			setLog(obj.getPropertyValue("log"));
		}
		if (obj.hasPropertyValue("logIncludingThoughts")) {
			setLogIncludingThoughts(obj.getPropertyValue("logIncludingThoughts"));
		}
		if (obj.hasPropertyValue("logAssignments")) {
			setLogAssignments(obj.getPropertyValue("logAssignments"));
		}
		if (obj.hasPropertyValue("output")) {
			setOutput(obj.getPropertyValue("output"));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("sessionId",new StringBuilder(getSessionId()));
		r.setPropertyValue("dateTimeStart",new StringBuilder("" + getDateTimeStart()));
		r.setPropertyValue("dateTimeLastActivity",new StringBuilder("" + getDateTimeLastActivity()));
		r.setPropertyValue("dateTimeEnd",new StringBuilder("" + getDateTimeEnd()));
		r.setLinkValue("dialog",getDialogId());
		r.setPropertyValue("context",getContext());
		r.setPropertyValue("log",getLog());
		r.setPropertyValue("logIncludingThoughts",getLogIncludingThoughts());
		r.setPropertyValue("logAssignments",getLogAssignments());
		r.setPropertyValue("output",getOutput());
		return r;
	}

	public JsFile toJSON() {
		JsFile f = new JsFile();
		f.rootElement = new JsElem();
		JsElem c = null;
		
		c = new JsElem();
		c.name = "sessionId";
		f.rootElement.children.add(c);
		c.value = new StringBuilder(getSessionId());
		c.cData = true;
		
		c = new JsElem();
		c.name = "context";
		f.rootElement.children.add(c);
		c.value = getContext();
		c.cData = true;
		
		c = new JsElem();
		c.name = "log";
		f.rootElement.children.add(c);
		c.value = getLog();
		c.cData = true;

		c = new JsElem();
		c.name = "logIncludingThoughts";
		f.rootElement.children.add(c);
		c.value = getLogIncludingThoughts();
		c.cData = true;
		
		c = new JsElem();
		c.name = "output";
		f.rootElement.children.add(c);
		c.value = getOutput();
		c.cData = true;
		
		if (variables.size()>0) {
			c = new JsElem();
			c.name = "variables";
			f.rootElement.children.add(c);
			c.array = true;
			for (SessionVariable sv: variables) {
				JsFile svf = sv.toJSON();
				c.children.add(svf.rootElement);
			}
		}
		
		if (dialogVariables.size()>0) {
			c = new JsElem();
			c.name = "dialogVariables";
			f.rootElement.children.add(c);
			c.array = true;
			for (SessionDialogVariable sv: dialogVariables) {
				JsFile svf = sv.toJSON();
				c.children.add(svf.rootElement);
			}
		}
		
		return f;
	}
	
	public void appendLogLine(boolean debug,String line,boolean isThought) {
		if (debug) {
			Messenger.getInstance().debug(this,line);
		}
		Date d = new Date();
		String ds = Generic.getTimeString(d,true);
		StringBuilder addLine = new StringBuilder();
		addLine.append(ds);
		addLine.append(": ");
		addLine.append(line);
		addLine.append("\n");
		if (!isThought) {
			log.append(addLine);
		}
		logIncludingThoughts.append(addLine);
		setDateTimeLastActivity(d.getTime());
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the log
	 */
	public StringBuilder getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(StringBuilder log) {
		this.log = log;
	}

	/**
	 * @return the variables
	 */
	public List<SessionDialogVariable> getDialogVariables() {
		return dialogVariables;
	}

	/**
	 * @return the dateTimeStart
	 */
	public long getDateTimeStart() {
		return dateTimeStart;
	}

	/**
	 * @param dateTimeStart the dateTimeStart to set
	 */
	public void setDateTimeStart(long dateTimeStart) {
		this.dateTimeStart = dateTimeStart;
	}

	/**
	 * @return the dateTimeEnd
	 */
	public long getDateTimeEnd() {
		return dateTimeEnd;
	}

	/**
	 * @param dateTimeEnd the dateTimeEnd to set
	 */
	public void setDateTimeEnd(long dateTimeEnd) {
		this.dateTimeEnd = dateTimeEnd;
	}

	/**
	 * @return the context
	 */
	public StringBuilder getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(StringBuilder context) {
		this.context = context;
	}

	/**
	 * @return the dialogId
	 */
	public long getDialogId() {
		return dialogId;
	}

	/**
	 * @param dialogId the dialogId to set
	 */
	public void setDialogId(long dialogId) {
		this.dialogId = dialogId;
	}

	/**
	 * @return the dialog
	 */
	public Dialog getDialog() {
		return dialog;
	}

	/**
	 * @param dialog the dialog to set
	 */
	public void setDialog(Dialog dialog) {
		if (dialog==null) {
			dialogId = 0;
		} else {
			dialogId = dialog.getId();
		}
		this.dialog = dialog;
	}

	/**
	 * @return the dateTimeLastActivity
	 */
	public long getDateTimeLastActivity() {
		return dateTimeLastActivity;
	}

	/**
	 * @param dateTimeLastActivity the dateTimeLastActivity to set
	 */
	public void setDateTimeLastActivity(long dateTimeLastActivity) {
		this.dateTimeLastActivity = dateTimeLastActivity;
	}

	/**
	 * @return the output
	 */
	public StringBuilder getOutput() {
		return output;
	}

	/**
	 * @param output the output to set
	 */
	public void setOutput(StringBuilder output) {
		this.output = output;
	}

	/**
	 * @return the logIncludingThoughts
	 */
	public StringBuilder getLogIncludingThoughts() {
		return logIncludingThoughts;
	}

	/**
	 * @param logIncludingThoughts the logIncludingThoughts to set
	 */
	public void setLogIncludingThoughts(StringBuilder logIncludingThoughts) {
		this.logIncludingThoughts = logIncludingThoughts;
	}

	/**
	 * @return the logAssignments
	 */
	public StringBuilder getLogAssignments() {
		return logAssignments;
	}

	/**
	 * @param logAssignments the logAssignments to set
	 */
	public void setLogAssignments(StringBuilder logAssignments) {
		this.logAssignments = logAssignments;
	}

	/**
	 * @return the variables
	 */
	public List<SessionVariable> getVariables() {
		return variables;
	}
}
