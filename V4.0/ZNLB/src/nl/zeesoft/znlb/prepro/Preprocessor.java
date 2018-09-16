package nl.zeesoft.znlb.prepro;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.znlb.lang.Languages;
import nl.zeesoft.znlb.mod.ModZNLB;
import nl.zeesoft.zodb.Config;
import nl.zeesoft.zodb.db.InitializerDatabaseObject;
import nl.zeesoft.zodb.db.InitializerObject;

public class Preprocessor extends InitializerObject {
	public Preprocessor(Config config) {
		super(config,ModZNLB.NAME + "/Preprocessors/");
	}

	public ZStringSymbolParser process(ZStringSymbolParser sequence) {
		return process(sequence,null);
	}
	
	public ZStringSymbolParser process(ZStringSymbolParser sequence,List<String> languages) {
		ZStringSymbolParser r = sequence;
		r.trim();
		if (r.length()>0) {
			List<String> symbols = r.toSymbolsPunctuated();
			String end = symbols.get(symbols.size() - 1);
			if (!ZStringSymbolParser.isLineEndSymbol(end)) {
				symbols.add(".");
			}
			r.fromSymbols(symbols,true,true);
			lockMe(this);
			for (LanguagePreprocessor pp: getPreprocessorsNoLock()) {
				if (languages==null || languages.size()==0 || languages.contains(pp.getLanguage())) {
					r = pp.process(r);
				}
			}
			unlockMe(this);
		}
		return r;
	}

	@Override
	protected void initializeDatabaseObjectsNoLock() {
		addObjectNoLock(new EnglishPreprocessor());
		addObjectNoLock(new DutchPreprocessor());
		Languages languages = ((ModZNLB) getConfiguration().getModule(ModZNLB.NAME)).getLanguages(); 
		for (LanguagePreprocessor pp: getPreprocessorsNoLock()) {
			pp.setName(languages.getNameForCode(pp.getLanguage()));
			pp.initializeReplacements();
		}
	}

	@Override
	protected InitializerDatabaseObject getNewObjectNoLock(String name) {
		LanguagePreprocessor pp = new LanguagePreprocessor();
		pp.setName(name);
		return pp;
	}

	private List<LanguagePreprocessor> getPreprocessorsNoLock() {
		List<LanguagePreprocessor> r = new ArrayList<LanguagePreprocessor>();
		for (InitializerDatabaseObject object: getObjectsNoLock()) {
			r .add((LanguagePreprocessor)object);
		}
		return r;
	}
}
