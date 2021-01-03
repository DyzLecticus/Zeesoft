package nl.zeesoft.zdbd.api.html;

import nl.zeesoft.zdbd.api.HtmlResponse;
import nl.zeesoft.zdk.Str;

public class IndexHtml extends HtmlResponse {
	public IndexHtml() {
		title = "ZDBD";
		onload = "state.onload();";
		styleFiles.add("/main.css");
		scriptFiles.add("/index.js");
	}
	
	@Override
	public Str renderBody() {
		Str r = new Str();
		append(r,"<div id=\"modal\"></div>");
		
		append(r,"<div id=\"app\">");
		
		append(r,"<div id=\"menu\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-right column-padding\" id=\"state\"></div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label-small\">App</label>");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Quit\" onclick=\"menu.quit();\" />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label-small\">Theme</label>");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Load\" onclick=\"menu.load();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Save\" onclick=\"menu.save();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Save as\" onclick=\"menu.saveAs();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Delete\" onclick=\"menu.delete();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"New\" onclick=\"menu.new();\" />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"</div>"); // end menu

		append(r,"<hr />");
		append(r,"<div id=\"theme\" class=\"row\"></div>");
		append(r,"<div id=\"sequencer\"></div>");

		append(r,"<hr />");
		append(r,"<div id=\"trainingSequence\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label\">Training sequence</label >");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Play\" onclick=\"sequencer.startSequence();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Stop\" onclick=\"sequencer.stop();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-right column-padding\">");
		append(r,"    <input type=\"button\" id=\"showTrainingSequence\" value=\"+\" onclick=\"sequence.toggleShow();\" />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div id=\"sequenceEditor\"></div>");
		append(r,"</div>"); // end trainingSequence

		append(r,"<hr />");
		append(r,"<div id=\"network\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label\">Network</label >");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" id=\"trainNetwork\" value=\"Train\" onclick=\"network.train();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-right column-padding\">");
		append(r,"    <input type=\"button\" id=\"showNetworkStatistics\" value=\"+\" onclick=\"network.toggleShowStats();\" />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div id=\"statistics\"></div>");
		append(r,"</div>"); // end network

		append(r,"<hr />");
		append(r,"<div id=\"generators\"></div>");

		append(r,"</div>"); // end app
		return r;
	}
}
