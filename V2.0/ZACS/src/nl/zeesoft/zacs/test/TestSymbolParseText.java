package nl.zeesoft.zacs.test;

import java.util.List;

import nl.zeesoft.zacs.database.model.Symbol;
import nl.zeesoft.zodb.Messenger;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.event.EvtEvent;
import nl.zeesoft.zodb.event.EvtEventSubscriber;

public class TestSymbolParseText {

	public static void main(String[] args) {
		DbConfig.getInstance().setDebug(true);
		
		String text = "";
		
		text = "Hello, this is a test. Please take care of parsing this A.B.C. text (Example) correctly!?.. . .. When \"this is done\"?! it should result in: 5 sentences.";

		Messenger.getInstance().start();
		Messenger.getInstance().addSubscriber(new EvtEventSubscriber() {
			@Override
			public void handleEvent(EvtEvent e) {
				// Ignore
			}
		});
		
		Messenger.getInstance().debug(new Symbol(),"text: " + text);
		List<String> sentences = Symbol.parseTextSentences(new StringBuilder(text),0,0);
		int i = 0;
		for (String sentence: sentences) {
			i++;
			Messenger.getInstance().debug(new Symbol(),"Sentence " + i + ": " + sentence);
		}

		List<String> symbols = Symbol.parseTextSymbols(new StringBuilder("."),0);
		for (String symbol: symbols) {
			Messenger.getInstance().debug(new Symbol(),"Symbol: " + symbol);
		}
	}

}
