package nl.zeesoft.zdk.neural.model;

import java.text.DecimalFormat;
import java.util.List;

import nl.zeesoft.zdk.Str;
import nl.zeesoft.zdk.StrAble;
import nl.zeesoft.zdk.grid.Position;

public class Synapse implements StrAble {
	private static final DecimalFormat	df			= new DecimalFormat("0.000");
	
	public Position						connectTo	= new Position();
	public float						permanence	= 0F;
	
	public Synapse() {
		
	}
	
	public Synapse(Synapse synapse) {
		copyFrom(synapse);
	}
	
	public void copyFrom(Synapse synapse) {
		connectTo = synapse.connectTo;
		permanence = synapse.permanence;
	}

	@Override
	public Str toStr() {
		Str r = new Str();
		r.sb().append(connectTo.toStr().sb());
		r.sb().append(";");
		r.sb().append(getStringValue(permanence));
		return r;
	}

	@Override
	public void fromStr(Str str) {
		List<Str> elems = str.split(";");
		if (elems.size()==2) {
			connectTo.fromStr(elems.get(0));
			permanence = Float.parseFloat(elems.get(1).toString());
		}
	}
	
	private String getStringValue(float value) {
		Str r = new Str(df.format(value));
		while (r.endsWith("0")) {
			r = r.substring(0,r.length() - 1);
		}
		if (r.endsWith(".")) {
			r = r.substring(0,r.length() - 1);
		}
		return r.toString();
	}
}
