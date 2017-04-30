package nl.zeesoft.zmmt.syntesizer;

public class DrumConfiguration extends VelocityConfiguration {
	private String		name			= "";
	private	int			noteNum			= 0;

	private	int			layerNoteNum	= 34;
	
	public String getName() {
		return name;
	}
	
	public void setName(String drum) {
		this.name = drum;
	}
	
	public int getNoteNum() {
		return noteNum;
	}
	
	public void setNoteNum(int noteNum) {
		this.noteNum = noteNum;
	}

	public int getLayerNoteNum() {
		return layerNoteNum;
	}

	public void setLayerNoteNum(int layerNoteNum) {
		this.layerNoteNum = layerNoteNum;
	}
}
