package nl.zeesoft.zids.database.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class DialogVariable extends HlpObject {
	private String							code					= "";
	private String							contextSymbol			= "";
	private StringBuilder					prompt1					= new StringBuilder();
	private StringBuilder					prompt2					= new StringBuilder();
	private StringBuilder					prompt3					= new StringBuilder();
	
	private long							dialogId				= 0; 
	private Dialog							dialog					= null; 

	private long							typeId					= 0; 
	private VariableType					type					= null; 
	
	private List<DialogVariableExample>		examples				= new ArrayList<DialogVariableExample>();

	public String getPromptInput() {
		return "@PROMPT " + getCode() + " ==";
	}
	
	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("code")) {
			setCode(obj.getPropertyValue("code").toString());
		}
		if (obj.hasPropertyValue("contextSymbol")) {
			setContextSymbol(obj.getPropertyValue("contextSymbol").toString());
		}
		if (obj.hasPropertyValue("prompt1")) {
			setPrompt1(obj.getPropertyValue("prompt1"));
		}
		if (obj.hasPropertyValue("prompt2")) {
			setPrompt2(obj.getPropertyValue("prompt2"));
		}
		if (obj.hasPropertyValue("prompt3")) {
			setPrompt3(obj.getPropertyValue("prompt3"));
		}
		if (obj.hasPropertyValue("dialog") && obj.getLinkValue("dialog").size()>0) {
			setDialogId(obj.getLinkValue("dialog").get(0));
		}
		if (obj.hasPropertyValue("type") && obj.getLinkValue("type").size()>0) {
			setTypeId(obj.getLinkValue("type").get(0));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("code",new StringBuilder(getCode()));
		r.setPropertyValue("contextSymbol",new StringBuilder(getContextSymbol()));
		r.setPropertyValue("prompt1",getPrompt1());
		r.setPropertyValue("prompt2",getPrompt2());
		r.setPropertyValue("prompt3",getPrompt3());
		r.setLinkValue("dialog",getDialogId());
		r.setLinkValue("type",getTypeId());
		return r;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
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
	 * @return the contextSymbol
	 */
	public String getContextSymbol() {
		return contextSymbol;
	}

	/**
	 * @param contextSymbol the contextSymbol to set
	 */
	public void setContextSymbol(String contextSymbol) {
		this.contextSymbol = contextSymbol;
	}

	/**
	 * @return the examples
	 */
	public List<DialogVariableExample> getExamples() {
		return examples;
	}

	/**
	 * @return the typeId
	 */
	public long getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the type
	 */
	public VariableType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(VariableType type) {
		if (type==null) {
			typeId = 0;
		} else {
			typeId = type.getId();
		}
		this.type = type;
	}

	/**
	 * @return the prompt1
	 */
	public StringBuilder getPrompt1() {
		return prompt1;
	}

	/**
	 * @param prompt1 the prompt1 to set
	 */
	public void setPrompt1(StringBuilder prompt1) {
		this.prompt1 = prompt1;
	}

	/**
	 * @return the prompt2
	 */
	public StringBuilder getPrompt2() {
		return prompt2;
	}

	/**
	 * @param prompt2 the prompt2 to set
	 */
	public void setPrompt2(StringBuilder prompt2) {
		this.prompt2 = prompt2;
	}

	/**
	 * @return the prompt3
	 */
	public StringBuilder getPrompt3() {
		return prompt3;
	}

	/**
	 * @param prompt3 the prompt3 to set
	 */
	public void setPrompt3(StringBuilder prompt3) {
		this.prompt3 = prompt3;
	}

}
