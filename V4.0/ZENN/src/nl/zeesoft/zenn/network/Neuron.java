package nl.zeesoft.zenn.network;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class Neuron implements JsAble {
	public int					id			= -1;
	public int					posX		= -1;
	public List<NeuronLink>		targets		= new ArrayList<NeuronLink>();
	public List<NeuronLink>		sources		= new ArrayList<NeuronLink>();
	public float				bias		= 0.5F;
	
	public float				value		= 0.0F;
	
	public Neuron(int id,int posX) {
		this.id = id;
		this.posX = posX;
	}
	
	public void destroy() {
		for (NeuronLink link: sources) {
			link.source = null;
			link.target = null;
		}
		targets.clear();
		sources.clear();
	}

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("id","" + id));
		json.rootElement.children.add(new JsElem("x","" + posX));
		json.rootElement.children.add(new JsElem("b","" + bias));
		JsElem sourcesElem = new JsElem("sls",true);
		json.rootElement.children.add(sourcesElem);
		for (NeuronLink source: sources) {
			JsElem sourceElem = new JsElem();
			sourcesElem.children.add(sourceElem);
			sourceElem.children.add(new JsElem("sId","" + source.source.id));
			sourceElem.children.add(new JsElem("w","" + source.weight));
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			id = json.rootElement.getChildInt("id");
			posX = json.rootElement.getChildInt("x");
			bias = json.rootElement.getChildFloat("b");
			JsElem sourcesElem = json.rootElement.getChildByName("sls");
			if (sourcesElem!=null) {
				for (JsElem sourceElem: sourcesElem.children) {
					int sourceId = sourceElem.getChildInt("sId");
					for (NeuronLink link: sources) {
						if (link.source.id==sourceId) {
							link.weight = sourceElem.getChildFloat("w");
							break;
						}
					}
				}
			}
		}
	}
	
	public NeuronLink getSourceByNeuron(Neuron source) {
		NeuronLink r = null;
		for (NeuronLink link: sources) {
			if (link.source==source) {
				r = link;
			}
		}
		return r;
	}
	
	public float getTotalSourceWeight() {
		float r = 0;
		for (NeuronLink source: sources) {
			if (source.weight>0.0F) {
				r += source.weight;
			} else if (source.weight<0.0F) {
				r += source.weight * -1.0F;
			}
		}
		if (bias>0.0F) {
			r += bias;
		} else if (bias<0.0F) {
			r += bias * -1.0F;
		}
		return r;
	}
	
	public float getSourceWeightRatio(float weightOrBias,float totalSourceWeight) {
		float r = 0.0F;
		if (weightOrBias>0.0F) {
			r = weightOrBias / totalSourceWeight;
		} else if (weightOrBias<0.0F) {
			r = (weightOrBias * -1.0F) / totalSourceWeight;
		}
		return r;
	}
}
