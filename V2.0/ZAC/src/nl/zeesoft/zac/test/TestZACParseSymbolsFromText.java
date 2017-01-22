package nl.zeesoft.zac.test;

import nl.zeesoft.zac.ZAC;

public class TestZACParseSymbolsFromText {

	public static void main(String[] args) {
		StringBuilder text = new StringBuilder("What is your name? My name is Dyz Lecticus.");
		for (StringBuilder symbol: ZAC.parseSymbolsFromText(text)) {
			System.out.println(symbol);
		}
	}

}
