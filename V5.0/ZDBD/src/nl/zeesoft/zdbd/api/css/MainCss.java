package nl.zeesoft.zdbd.api.css;

import nl.zeesoft.zdbd.api.ResponseObject;
import nl.zeesoft.zdk.Str;

public class MainCss extends ResponseObject {
	@Override
	public Str render() {
		Str r = new Str();
		
		append(r,"body {");
		append(r,"    font-family: Tahoma, sans-serif;");
		append(r,"    font-size: 16px;");
		append(r,"}");
		append(r,"input, select {");
		append(r,"    font-family: inherit;");
		append(r,"    font-size: 16px;");
		append(r,"}");
		
		append(r,".row {");
		append(r,"    display:inline-block;");
		append(r,"    width:100%;");
		append(r,"}");
		append(r,".row:after {");
		append(r,"    content:\"\";");
		append(r,"    display:table;");
		append(r,"    clear: both;");
		append(r,"}");
		append(r,".column-left {");
		append(r,"    float:left;");
		append(r,"    width:auto;");
		append(r,"}");
		append(r,".column-right {");
		append(r,"    float:right;");
		append(r,"    width:auto;");
		append(r,"}");
		append(r,".column-padding {");
		append(r,"    padding:2px;");
		append(r,"}");
		append(r,".column-label {");
		append(r,"    display:block;");
		append(r,"    width:160px;");
		append(r,"}");
		append(r,".column-label-small {");
		append(r,"    display:block;");
		append(r,"    width:96px;");
		append(r,"}");
		
		append(r,".hidden {");
		append(r,"    display:none;");
		append(r,"    visibility:hidden;");
		append(r,"}");
		append(r,".visible {");
		append(r,"    display:block;");
		append(r,"    visibility:visible;");
		append(r,"}");
		
		return r;
	}
}
