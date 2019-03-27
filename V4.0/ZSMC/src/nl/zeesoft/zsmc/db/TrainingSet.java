package nl.zeesoft.zsmc.db;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;

public class TrainingSet implements InitializerDatabaseObject {
	private String					name		= "";
	private List<TrainingSequence>	sequences	= new ArrayList<TrainingSequence>();
	
	public void addSequence(String sequence, String context) {
		addSequence(new ZStringSymbolParser(sequence),new ZStringSymbolParser(context));
	}
	
	public void addSequence(ZStringSymbolParser sequence, ZStringSymbolParser context) {
		TrainingSequence seq = new TrainingSequence();
		seq.sequence = sequence;
		seq.context = context;
		sequences.add(seq);
	}
	
	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("name",name,true));
		JsElem seqsElem = new JsElem("sequences",true);
		json.rootElement.children.add(seqsElem);
		for (TrainingSequence seq: sequences) {
			JsElem seqElem = new JsElem();
			seqsElem.children.add(seqElem);
			JsFile seqJs = seq.toJson();
			seqElem.children = seqJs.rootElement.children;
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			sequences.clear();
			name = json.rootElement.getChildString("name",name);
			JsElem seqsElem = json.rootElement.getChildByName("sequences");
			for (JsElem seqElem: seqsElem.children) {
				JsFile seqJs = new JsFile();
				seqJs.rootElement = seqElem;
				TrainingSequence seq = new TrainingSequence();
				seq.fromJson(seqJs);
				sequences.add(seq);
			}
		}
	}

	@Override
	public String getObjectName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public List<TrainingSequence> getSequences() {
		return sequences;
	}
}
