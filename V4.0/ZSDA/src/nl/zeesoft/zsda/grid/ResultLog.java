package nl.zeesoft.zsda.grid;

import nl.zeesoft.zdk.htm.proc.Anomaly;
import nl.zeesoft.zdk.json.JsAble;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;

public class ResultLog implements JsAble {
	public long						dateTime			= 0;
	public int						predictedValue		= Integer.MIN_VALUE;
	public int						actualValue			= Integer.MIN_VALUE;
	public float					accuracy			= 0;
	public float					averageAccuracy		= 0;
	public Anomaly					detectedAnomaly		= null;

	@Override
	public JsFile toJson() {
		JsFile json = new JsFile();
		json.rootElement = new JsElem();
		json.rootElement.children.add(new JsElem("dateTime","" + dateTime));
		json.rootElement.children.add(new JsElem("predictedValue","" + predictedValue));
		json.rootElement.children.add(new JsElem("actualValue","" + actualValue));
		json.rootElement.children.add(new JsElem("accuracy","" + accuracy));
		json.rootElement.children.add(new JsElem("averageAccuracy","" + averageAccuracy));
		if (detectedAnomaly!=null) {
			JsElem anomElem = new JsElem("detectedAnomaly", true);
			json.rootElement.children.add(anomElem);
			anomElem.children.add(detectedAnomaly.toJson().rootElement);
		}
		return json;
	}

	@Override
	public void fromJson(JsFile json) {
		if (json.rootElement!=null) {
			dateTime = json.rootElement.getChildLong("dateTime",dateTime);
			predictedValue = json.rootElement.getChildInt("predictedValue",predictedValue);
			actualValue = json.rootElement.getChildInt("actualValue",actualValue);
			accuracy = json.rootElement.getChildFloat("accuracy",accuracy);
			averageAccuracy = json.rootElement.getChildFloat("averageAccuracy",averageAccuracy);
			detectedAnomaly = null;
			JsElem anomElem = json.rootElement.getChildByName("detectedAnomaly");
			if (anomElem!=null && anomElem.children.size()>0) {
				JsFile js = new JsFile();
				js.rootElement = anomElem.children.get(0);
				Anomaly anomaly = new Anomaly();
				anomaly.fromJson(js);
				detectedAnomaly = anomaly;
			}
		}

		// TODO Auto-generated method stub
		
	}
}
