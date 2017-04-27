package nl.zeesoft.zso.orchestra.members;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zdk.json.JsElem;
import nl.zeesoft.zdk.json.JsFile;
import nl.zeesoft.zjmo.orchestra.MemberObject;
import nl.zeesoft.zjmo.orchestra.protocol.ProtocolWork;

public class MidiPlayerProtocol extends ProtocolWork {

	@Override
	protected ZStringBuilder handleJson(MemberObject member,JsFile json) {
		ZStringBuilder output = null;
		if (member instanceof MidiPlayer) {
			MidiPlayer player = (MidiPlayer) member;
			String instrument = "";
			List<Integer> notes = new ArrayList<Integer>();
			int velocity = 100;
			long durationMs = 0;
			for (JsElem cElem: json.rootElement.children) {
				if (cElem.name.equals("instrument")) {
					instrument = cElem.value.toString();
				} else if (cElem.name.equals("notes")) {
					for (JsElem nElem: cElem.children) {
						int note = Integer.parseInt(nElem.value.toString());
						if (note>=0) {
							notes.add(note);
						}
					}
				} else if (cElem.name.equals("velocity")) {
					velocity = Integer.parseInt(cElem.value.toString());
				} else if (cElem.name.equals("durationMs")) {
					durationMs = Long.parseLong(cElem.value.toString());
				}
			}
			if (instrument.length()>0 && durationMs>0 && notes.size()>0) {
				player.playInstrumentNotes(instrument, notes, velocity, durationMs);
			}
		}
		return output;
	}
	
}
