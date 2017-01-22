package nl.zeesoft.zac;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zac.database.cache.ZACCacheConfig;
import nl.zeesoft.zac.database.model.ZACDataGenerator;
import nl.zeesoft.zac.database.model.ZACModel;
import nl.zeesoft.zac.database.server.ZACServer;
import nl.zeesoft.zac.module.ModController;
import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.DbController;
import nl.zeesoft.zodb.database.server.SvrConfig;

/**
 * This class is the entry point to the ZAC application.
 */
public class ZAC {
	public static final String[]	LINE_END_SYMBOLS		= {"." , "?" , "!"};
	public static final String		LINE_END_SYMBOLS_STRING	= LINE_END_SYMBOLS[0] + LINE_END_SYMBOLS[1] + LINE_END_SYMBOLS[2];

	public static void main(String[] args) {
		DbConfig.getInstance().setModelClassName(ZACModel.class.getName());
		DbConfig.getInstance().setCacheConfigClassName(ZACCacheConfig.class.getName());
		SvrConfig.getInstance().setServerClassName(ZACServer.class.getName());
		if (SvrConfig.getInstance().getPort()==4321) {
			SvrConfig.getInstance().setPort(5445);
		}

		if (!DbConfig.getInstance().fileExists()) {
			args = new String[2];
			args[1] = "zac.jar";
			ZACDataGenerator generator = new ZACDataGenerator();
			generator.confirmInstallDemoData();
			DbController.getInstance().addSubscriber(generator);
		} else {
			DbController.getInstance().addSubscriber(ModController.getInstance());
		}

		DbController.getInstance().handleMainMethodsArguments(args);
	}
	
	public static final List<StringBuilder> parseSymbolsFromText(StringBuilder text) {
		List<StringBuilder> symbols = new ArrayList<StringBuilder>();
		
		text = Generic.stringBuilderReplace(text,"("," ( ");
		text = Generic.stringBuilderReplace(text,")"," ) ");
		text = Generic.stringBuilderReplace(text,":"," : ");
		text = Generic.stringBuilderReplace(text,";"," ; ");
		text = Generic.stringBuilderReplace(text,","," , ");
		text = Generic.stringBuilderReplace(text,"'"," ' ");
		text = Generic.stringBuilderReplace(text,"\""," \" ");

		text = Generic.stringBuilderReplace(text,"     "," ");
		text = Generic.stringBuilderReplace(text,"    "," ");
		text = Generic.stringBuilderReplace(text,"   "," ");
		text = Generic.stringBuilderReplace(text,"  "," ");
		
		List<StringBuilder> syms = Generic.stringBuilderSplit(text," ");

		for (StringBuilder sym: syms) {
			if (sym.length()>1 && endsWithLineEndSymbol(sym)) {
				StringBuilder symbol = new StringBuilder(sym.substring(0,sym.length() - 1));
				StringBuilder lineEnd = new StringBuilder(sym.substring(sym.length() - 1));
				symbol = removeLineEndSymbols(symbol); 
				symbols.add(symbol);
				symbols.add(lineEnd);
			} else {
				symbols.add(sym);
			}
		}
		
		return symbols;
	}

	public static final boolean endsWithLineEndSymbol(StringBuilder symbol) {
		boolean r = false;
		if (symbol.length()>1) {
			r = isLineEndSymbol(symbol.substring(symbol.length() - 1));
		} else {
			r = isLineEndSymbol(symbol.toString());
		}
		return r;
	}

	public static final StringBuilder removeLineEndSymbols(StringBuilder symbol) {
		for (String lineEnd: LINE_END_SYMBOLS) {
			symbol = Generic.stringBuilderReplace(symbol,lineEnd,"");
		}
		return symbol;
	}
	
	public static final boolean isLineEndSymbol(String symbol) {
		return isSymbol(symbol,LINE_END_SYMBOLS);
	}

	public static final boolean isSymbol(String symbol,String[] symbols) {
		boolean r = false;
		for (int i = 0; i < symbols.length; i++) {
			if (symbol.equals(symbols[i])) {
				r = true;
				break;
			}
		}
		return r;
	}
}