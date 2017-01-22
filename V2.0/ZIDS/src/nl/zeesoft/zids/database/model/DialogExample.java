package nl.zeesoft.zids.database.model;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class DialogExample extends HlpObject {
	private StringBuilder	input					= new StringBuilder();
	private StringBuilder	output					= new StringBuilder();
	
	private long			dialogId				= 0; 
	private Dialog			dialog					= null; 
		
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("input")) {
			setInput(obj.getPropertyValue("input"));
		}
		if (obj.hasPropertyValue("output")) {
			setOutput(obj.getPropertyValue("output"));
		}
		if (obj.hasPropertyValue("dialog") && obj.getLinkValue("dialog").size()>0) {
			setDialogId(obj.getLinkValue("dialog").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("input",getInput());
		r.setPropertyValue("output",getOutput());
		r.setLinkValue("dialog",getDialogId());
		return r;
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
	 * @return the input
	 */
	public StringBuilder getInput() {
		return input;
	}

	/**
	 * @param input the input to set
	 */
	public void setInput(StringBuilder input) {
		this.input = input;
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

}
