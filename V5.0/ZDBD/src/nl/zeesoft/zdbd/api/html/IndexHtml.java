package nl.zeesoft.zdbd.api.html;

import nl.zeesoft.zdbd.api.HtmlResponse;
import nl.zeesoft.zdbd.test.ZDBD;
import nl.zeesoft.zdk.Str;

public class IndexHtml extends HtmlResponse {
	public IndexHtml() {
		title = ZDBD.NAME;
		onload = "state.onload();";
		styleFiles.add("/main.css");
		scriptFiles.add("/index.js");
	}
	
	@Override
	public Str renderBody() {
		Str r = new Str();
		append(r,"<div id=\"modal\" class=\"app-column\"></div>");
		
		append(r,"<div id=\"app\">");
		
		append(r,"<div id=\"menu\">");
		append(r,"<div class=\"row\">");
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
		append(r,"    <div class=\"column-right column-padding\">");
		append(r,"    <input type=\"button\" value=\"X\" onclick=\"menu.quit();\" />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"</div>"); // end menu

		append(r,"<hr />");

		append(r,"<div class=\"row\">");

		append(r,"<div class=\"column-left\">");
		append(r,"<div class=\"app-column\">");
		
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label\">Name</label >");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <div id=\"theme\"></div>");
		append(r,"    </div>");
		append(r,"</div>");
		
		append(r,"<div id=\"sequencer\"></div>");

		append(r,"</div>");
		append(r,"</div>");
		append(r,"<div class=\"column-left\">");
		append(r,"<div class=\"app-column\">");
		
		append(r,"<div id=\"chordChanges\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" class=\"show-hide\" id=\"showChordChanges\" value=\"+\" onclick=\"chords.toggleShow();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label-header\">Chord changes</label >");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div id=\"chordEditor\"></div>");
		append(r,"</div>"); // end chordChanges
		
		append(r,"<div id=\"trainingSequence\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" class=\"show-hide\" id=\"showTrainingSequence\" value=\"+\" onclick=\"sequence.toggleShow();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label-header\">Training sequence</label >");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Play\" onclick=\"sequencer.startSequence();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Stop\" onclick=\"sequencer.stop();\" />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div id=\"sequenceEditor\"></div>");
		append(r,"</div>"); // end trainingSequence

		append(r,"<div id=\"network\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" class=\"show-hide\" id=\"showNetworkStatistics\" value=\"+\" onclick=\"network.toggleShowStats();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label-header\">Network</label >");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" id=\"describeNetwork\" value=\"Configuration\" onclick=\"network.configuration();\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" id=\"trainNetwork\" value=\"Train\" onclick=\"network.train();\" DISABLED />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" id=\"resetNetwork\" value=\"Reset\" onclick=\"network.reset();\" DISABLED />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div id=\"statistics\"></div>");
		append(r,"</div>"); // end network

		append(r,"<div id=\"generators\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" class=\"show-hide\" value=\"+\" onclick=\"generators.toggleShowList(this);\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label-header\">Generators</label >");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" id=\"generateSequences\" value=\"Generate sequences\" onclick=\"generators.generateAll();\" DISABLED />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div id=\"generatorList\"></div>");
		append(r,"</div>"); // end generators

		append(r,"<div id=\"arpeggiators\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" class=\"show-hide\" value=\"+\" onclick=\"arpeggiators.toggleShowList(this);\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label-header\">Arpeggiators</label >");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div id=\"arpeggiatorList\"></div>");
		append(r,"</div>"); // end arpeggiators

		append(r,"<div id=\"soundpatch\">");
		append(r,"<div class=\"row\">");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" class=\"show-hide\" value=\"+\" onclick=\"soundpatch.toggleShowInstruments(this);\" />");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <label class=\"column-label-header\">Sound patch</label >");
		append(r,"    </div>");
		append(r,"    <div class=\"column-left column-padding\">");
		append(r,"    <input type=\"button\" value=\"Load preset\" onclick=\"soundpatch.load();\" />");
		append(r,"    </div>");
		append(r,"</div>");
		append(r,"<div id=\"instrumentList\"></div>");
		append(r,"</div>"); // end soundpatch

		append(r,"</div>"); // end app column
		append(r,"</div>"); // end column
		
		append(r,"</div>"); // end row

		append(r,"</div>"); // end app

		append(r,"<div id=\"footer\" class=\"footer grey\">");
		append(r,"    <div id=\"progressBar\" class=\"progressBar blue\"></div>");
		append(r,"</div>"); // end footer
		return r;
	}
}
