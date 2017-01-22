package nl.zeesoft.zacs.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Control extends HlpObject {
	public final static String	STATUS_ACTIVATING		= "Activating ...";
	public final static String	STATUS_ACTIVE			= "Active";
	public final static String	STATUS_DEACTIVATING		= "Deactivating ...";
	public final static String	STATUS_INACTIVE			= "Inactive";
	
	private	Command				command					= null;
	private long				commandId				= 0;
	
	private String				status					= STATUS_INACTIVE;

	private int 				updateControlSeconds	= 3;

	private boolean				autoActivate			= true;
	private boolean				autoReactivate			= true;
	private int 				reactivateMinutes		= 60;
	private boolean				saveModuleSymbolState	= false;

	private boolean				learnExamples			= true;
	private int 				examplePauzeMSecs		= 500;
	private int 				countSymbolMaximum		= 1000;
	private int 				countContextMaximum		= 2000;
	private boolean				skipCountMaxStructSyms	= true;
	private int 				contextSymbolMaximum	= 8;
	private boolean				contextAssociate		= false;

	private int 				assignmentPauzeMSecs	= 500;
	private int 				assignmentReloadSecs	= 10;
	private boolean				doAssignments			= true;
	private int 				levelMaximum 			= 100;
	private int 				levelFireBase			= 100;
	
	private int					stateHistoryMaximum		= 1000;
	private int					addHistorySeconds		= 10;

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("command")) {
			if (obj.getLinkValue("command").size()>0) {
				setCommandId(obj.getLinkValue("command").get(0));
			} else {
				setCommandId(0);
			}
		}
		if (obj.hasPropertyValue("status")) {
			setStatus(obj.getPropertyValue("status").toString());
		}
		if (obj.hasPropertyValue("autoActivate")) {
			setAutoActivate(Boolean.parseBoolean(obj.getPropertyValue("autoActivate").toString()));
		}
		if (obj.hasPropertyValue("autoReactivate")) {
			setAutoReactivate(Boolean.parseBoolean(obj.getPropertyValue("autoReactivate").toString()));
		}
		if (obj.hasPropertyValue("reactivateMinutes")) {
			setReactivateMinutes(Integer.parseInt(obj.getPropertyValue("reactivateMinutes").toString()));
		}
		if (obj.hasPropertyValue("updateControlSeconds")) {
			setUpdateControlSeconds(Integer.parseInt(obj.getPropertyValue("updateControlSeconds").toString()));
		}
		if (obj.hasPropertyValue("countSymbolMaximum")) {
			setCountSymbolMaximum(Integer.parseInt(obj.getPropertyValue("countSymbolMaximum").toString()));
		}
		if (obj.hasPropertyValue("countContextMaximum")) {
			setCountContextMaximum(Integer.parseInt(obj.getPropertyValue("countContextMaximum").toString()));
		}
		if (obj.hasPropertyValue("skipCountMaxStructSyms")) {
			setSkipCountMaxStructSyms(Boolean.parseBoolean(obj.getPropertyValue("skipCountMaxStructSyms").toString()));
		}
		if (obj.hasPropertyValue("contextSymbolMaximum")) {
			setContextSymbolMaximum(Integer.parseInt(obj.getPropertyValue("contextSymbolMaximum").toString()));
		}
		if (obj.hasPropertyValue("contextAssociate")) {
			setContextAssociate(Boolean.parseBoolean(obj.getPropertyValue("contextAssociate").toString()));
		}
		if (obj.hasPropertyValue("saveModuleSymbolState")) {
			setSaveModuleSymbolState(Boolean.parseBoolean(obj.getPropertyValue("saveModuleSymbolState").toString()));
		}
		if (obj.hasPropertyValue("levelMaximum")) {
			setLevelMaximum(Integer.parseInt(obj.getPropertyValue("levelMaximum").toString()));
		}
		if (obj.hasPropertyValue("levelFireBase")) {
			setLevelFireBase(Integer.parseInt(obj.getPropertyValue("levelFireBase").toString()));
		}
		if (obj.hasPropertyValue("learnExamples")) {
			setLearnExamples(Boolean.parseBoolean(obj.getPropertyValue("learnExamples").toString()));
		}
		if (obj.hasPropertyValue("doAssignments")) {
			setDoAssignments(Boolean.parseBoolean(obj.getPropertyValue("doAssignments").toString()));
		}
		if (obj.hasPropertyValue("examplePauzeMSecs")) {
			setExamplePauzeMSecs(Integer.parseInt(obj.getPropertyValue("examplePauzeMSecs").toString()));
		}
		if (obj.hasPropertyValue("assignmentPauzeMSecs")) {
			setAssignmentPauzeMSecs(Integer.parseInt(obj.getPropertyValue("assignmentPauzeMSecs").toString()));
		}
		if (obj.hasPropertyValue("assignmentReloadSecs")) {
			setAssignmentReloadSecs(Integer.parseInt(obj.getPropertyValue("assignmentReloadSecs").toString()));
		}
		if (obj.hasPropertyValue("stateHistoryMaximum")) {
			setStateHistoryMaximum(Integer.parseInt(obj.getPropertyValue("stateHistoryMaximum").toString()));
		}
		if (obj.hasPropertyValue("addHistorySeconds")) {
			setAddHistorySeconds(Integer.parseInt(obj.getPropertyValue("addHistorySeconds").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setLinkValue("command",getCommandId());
		r.setPropertyValue("status",new StringBuilder(getStatus()));
		r.setPropertyValue("autoActivate",new StringBuilder("" + isAutoActivate()));
		r.setPropertyValue("autoReactivate",new StringBuilder("" + isAutoReactivate()));
		r.setPropertyValue("reactivateMinutes",new StringBuilder("" + getReactivateMinutes()));
		r.setPropertyValue("updateControlSeconds",new StringBuilder("" + getUpdateControlSeconds()));
		r.setPropertyValue("countSymbolMaximum",new StringBuilder("" + getCountSymbolMaximum()));
		r.setPropertyValue("countContextMaximum",new StringBuilder("" + getCountContextMaximum()));
		r.setPropertyValue("skipCountMaxStructSyms",new StringBuilder("" + isSkipCountMaxStructSyms()));
		r.setPropertyValue("contextSymbolMaximum",new StringBuilder("" + getContextSymbolMaximum()));
		r.setPropertyValue("contextAssociate",new StringBuilder("" + isContextAssociate()));
		r.setPropertyValue("saveModuleSymbolState",new StringBuilder("" + isSaveModuleSymbolState()));
		r.setPropertyValue("levelMaximum",new StringBuilder("" + getLevelMaximum()));
		r.setPropertyValue("levelFireBase",new StringBuilder("" + getLevelFireBase()));
		r.setPropertyValue("stateHistoryMaximum",new StringBuilder("" + getStateHistoryMaximum()));
		r.setPropertyValue("addHistorySeconds",new StringBuilder("" + getAddHistorySeconds()));
		r.setPropertyValue("learnExamples",new StringBuilder("" + isLearnExamples()));
		r.setPropertyValue("doAssignments",new StringBuilder("" + isDoAssignments()));
		r.setPropertyValue("examplePauzeMSecs",new StringBuilder("" + getExamplePauzeMSecs()));
		r.setPropertyValue("assignmentPauzeMSecs",new StringBuilder("" + getAssignmentPauzeMSecs()));
		r.setPropertyValue("assignmentReloadSecs",new StringBuilder("" + getAssignmentReloadSecs()));
		return r;
	}

	/**
	 * @return the updateControlSeconds
	 */
	public int getUpdateControlSeconds() {
		return updateControlSeconds;
	}

	/**
	 * @param updateControlSeconds the updateControlSeconds to set
	 */
	public void setUpdateControlSeconds(int updateControlSeconds) {
		this.updateControlSeconds = updateControlSeconds;
	}

	/**
	 * @return the countSymbolMaximum
	 */
	public int getCountSymbolMaximum() {
		return countSymbolMaximum;
	}

	/**
	 * @param countSymbolMaximum the countSymbolMaximum to set
	 */
	public void setCountSymbolMaximum(int countSymbolMaximum) {
		this.countSymbolMaximum = countSymbolMaximum;
	}

	/**
	 * @return the skipCountMaxStructSyms
	 */
	public boolean isSkipCountMaxStructSyms() {
		return skipCountMaxStructSyms;
	}

	/**
	 * @param skipCountMaxStructSyms the skipCountMaxStructSyms to set
	 */
	public void setSkipCountMaxStructSyms(boolean skipCountMaxStructSyms) {
		this.skipCountMaxStructSyms = skipCountMaxStructSyms;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the autoActivate
	 */
	public boolean isAutoActivate() {
		return autoActivate;
	}

	/**
	 * @param autoActivate the autoActivate to set
	 */
	public void setAutoActivate(boolean autoActivate) {
		this.autoActivate = autoActivate;
	}

	/**
	 * @return the command
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * @param command the command to set
	 */
	public void setCommand(Command command) {
		this.command = command;
	}

	/**
	 * @return the commandId
	 */
	public long getCommandId() {
		return commandId;
	}

	/**
	 * @param commandId the commandId to set
	 */
	public void setCommandId(long commandId) {
		this.commandId = commandId;
	}

	/**
	 * @return the stateHistoryMaximum
	 */
	public int getStateHistoryMaximum() {
		return stateHistoryMaximum;
	}

	/**
	 * @param stateHistoryMaximum the stateHistoryMaximum to set
	 */
	public void setStateHistoryMaximum(int stateHistoryMaximum) {
		this.stateHistoryMaximum = stateHistoryMaximum;
	}

	/**
	 * @return the addHistorySeconds
	 */
	public int getAddHistorySeconds() {
		return addHistorySeconds;
	}

	/**
	 * @param addHistorySeconds the addHistorySeconds to set
	 */
	public void setAddHistorySeconds(int addHistorySeconds) {
		this.addHistorySeconds = addHistorySeconds;
	}

	/**
	 * @return the reactivateMinutes
	 */
	public int getReactivateMinutes() {
		return reactivateMinutes;
	}

	/**
	 * @param reactivateMinutes the reactivateMinutes to set
	 */
	public void setReactivateMinutes(int reactivateMinutes) {
		this.reactivateMinutes = reactivateMinutes;
	}

	/**
	 * @return the learnExamples
	 */
	public boolean isLearnExamples() {
		return learnExamples;
	}

	/**
	 * @param learnExamples the learnExamples to set
	 */
	public void setLearnExamples(boolean learnExamples) {
		this.learnExamples = learnExamples;
	}

	/**
	 * @return the doAssignments
	 */
	public boolean isDoAssignments() {
		return doAssignments;
	}

	/**
	 * @param doAssignments the doAssignments to set
	 */
	public void setDoAssignments(boolean doAssignments) {
		this.doAssignments = doAssignments;
	}

	/**
	 * @return the examplePauzeMSecs
	 */
	public int getExamplePauzeMSecs() {
		return examplePauzeMSecs;
	}

	/**
	 * @param examplePauzeMSecs the examplePauzeMSecs to set
	 */
	public void setExamplePauzeMSecs(int examplePauzeMSecs) {
		this.examplePauzeMSecs = examplePauzeMSecs;
	}

	/**
	 * @return the assignmentPauzeMSecs
	 */
	public int getAssignmentPauzeMSecs() {
		return assignmentPauzeMSecs;
	}

	/**
	 * @param assignmentPauzeMSecs the assignmentPauzeMSecs to set
	 */
	public void setAssignmentPauzeMSecs(int assignmentPauzeMSecs) {
		this.assignmentPauzeMSecs = assignmentPauzeMSecs;
	}

	/**
	 * @return the levelMaximum
	 */
	public int getLevelMaximum() {
		return levelMaximum;
	}

	/**
	 * @param levelMaximum the levelMaximum to set
	 */
	public void setLevelMaximum(int levelMaximum) {
		this.levelMaximum = levelMaximum;
	}

	/**
	 * @return the contextSymbolMaximum
	 */
	public int getContextSymbolMaximum() {
		return contextSymbolMaximum;
	}

	/**
	 * @param contextSymbolMaximum the contextSymbolMaximum to set
	 */
	public void setContextSymbolMaximum(int contextSymbolMaximum) {
		this.contextSymbolMaximum = contextSymbolMaximum;
	}

	/**
	 * @return the countContextMaximum
	 */
	public int getCountContextMaximum() {
		return countContextMaximum;
	}

	/**
	 * @param countContextMaximum the countContextMaximum to set
	 */
	public void setCountContextMaximum(int countContextMaximum) {
		this.countContextMaximum = countContextMaximum;
	}

	/**
	 * @return the assignmentReloadSecs
	 */
	public int getAssignmentReloadSecs() {
		return assignmentReloadSecs;
	}

	/**
	 * @param assignmentReloadSecs the assignmentReloadSecs to set
	 */
	public void setAssignmentReloadSecs(int assignmentReloadSecs) {
		this.assignmentReloadSecs = assignmentReloadSecs;
	}

	/**
	 * @return the levelFireBase
	 */
	public int getLevelFireBase() {
		return levelFireBase;
	}

	/**
	 * @param levelFireBase the levelFireBase to set
	 */
	public void setLevelFireBase(int levelFireBase) {
		this.levelFireBase = levelFireBase;
	}

	/**
	 * @return the contextAssociate
	 */
	public boolean isContextAssociate() {
		return contextAssociate;
	}

	/**
	 * @param contextAssociate the contextAssociate to set
	 */
	public void setContextAssociate(boolean contextAssociate) {
		this.contextAssociate = contextAssociate;
	}

	/**
	 * @return the saveModuleSymbolState
	 */
	public boolean isSaveModuleSymbolState() {
		return saveModuleSymbolState;
	}

	/**
	 * @param saveModuleSymbolState the saveModuleSymbolState to set
	 */
	public void setSaveModuleSymbolState(boolean saveModuleSymbolState) {
		this.saveModuleSymbolState = saveModuleSymbolState;
	}

	/**
	 * @return the autoReactivate
	 */
	public boolean isAutoReactivate() {
		return autoReactivate;
	}

	/**
	 * @param autoReactivate the autoReactivate to set
	 */
	public void setAutoReactivate(boolean autoReactivate) {
		this.autoReactivate = autoReactivate;
	}

}
