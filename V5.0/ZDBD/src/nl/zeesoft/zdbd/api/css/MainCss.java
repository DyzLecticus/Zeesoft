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
		append(r,"    min-height:32px;");
		append(r,"}");
		append(r,".row:after {");
		append(r,"    content:\"\";");
		append(r,"    display:table;");
		append(r,"    clear: both;");
		append(r,"}");
		append(r,".row-highlight {");
		append(r,"    background-color:#f0f0f0;");
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
		append(r,".column-label-header {");
		append(r,"    display:block;");
		append(r,"    width:136px;");
		append(r,"}");
		append(r,".column-label-small {");
		append(r,"    display:block;");
		append(r,"    width:96px;");
		append(r,"}");
		append(r,".column-number {");
		append(r,"    text-align:right;");
		append(r,"    width:96px;");
		append(r,"}");
		
		append(r,".show-hide {");
		append(r,"    width:20px;");
		append(r,"    height:20px;");
		append(r,"    font-size:12px;");
		append(r,"    line-height:0px;");
		append(r,"    margin:0;");
		append(r,"    padding:0;");
		append(r,"}");
		append(r,".hidden {");
		append(r,"    display:none;");
		append(r,"    visibility:hidden;");
		append(r,"}");
		append(r,".visible {");
		append(r,"    display:block;");
		append(r,"    visibility:visible;");
		append(r,"}");
		
		append(r,".instrument-label {");
		append(r,"    display:block;");
		append(r,"    width:32px;");
		append(r,"}");
		append(r,".pattern-step {");
		append(r,"    border:2px outset;");
		append(r,"    width:32px;");
		append(r,"}");
		
		append(r,".grey {");
		append(r,"    background-color:#dedede;");
		append(r,"}");
		append(r,".red {");
		append(r,"    background-color:#ff0000;");
		append(r,"}");
		append(r,".orange {");
		append(r,"    background-color:#ff9900;");
		append(r,"}");
		append(r,".yellow {");
		append(r,"    background-color:#ffee00;");
		append(r,"}");
		append(r,".blue {");
		append(r,"    background-color:#0000ff;");
		append(r,"}");
		
		append(r,".footer {");
		append(r,"    position:fixed;");
		append(r,"    left:0;");
		append(r,"    bottom:0;");
		append(r,"    height:20px;");
		append(r,"    width:100%;");
		append(r,"}");
		append(r,".progressBar {");
		append(r,"    width:0%;");
		append(r,"    height:16px;");
		append(r,"    margin:2px;");
		append(r,"    font-size:12px;");
		append(r,"    text-align:center;");
		append(r,"    color:#ffffff;");
		append(r,"}");
		
		return r;
	}
}
