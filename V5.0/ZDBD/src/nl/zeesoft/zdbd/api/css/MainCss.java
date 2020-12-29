package nl.zeesoft.zdbd.api.css;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class MainCss extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		
		append(r,".row {");
		append(r,"    display:inline-block;");
		append(r,"}");
		append(r,".column-left {");
		append(r,"    width:auto;");
		append(r,"    float:left;");
		append(r,"}");
		append(r,".column-right {");
		append(r,"    width:auto;");
		append(r,"    float:left;");
		append(r,"}");
		append(r,".column-padding {");
		append(r,"    padding:2px;");
		append(r,"}");
		
		
		return r;
	}
}
