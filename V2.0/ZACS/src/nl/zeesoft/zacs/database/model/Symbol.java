package nl.zeesoft.zacs.database.model;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;
import nl.zeesoft.zodb.database.DbDataObject;
import nl.zeesoft.zodb.database.model.helpers.HlpObject;

public class Symbol extends HlpObject {
	public static final String[]	STRUCTURE_SYMBOLS		= {"." , "?" , "!" , ":" , ";" , "," , "\"" , "(" , ")"};
	public static final String[]	LINE_END_SYMBOLS		= {"." , "?" , "!"};
	public static final String		STRUCTURE_SYMBOLS_STR	= ".?!:;,\"()";
	
	private String 					code					= "";
	private boolean 				enabled					= true;
	private long 					level 					= 0;
	private List<SymbolLink>		symbolLinks				= new ArrayList<SymbolLink>();
	private List<ContextLink>		contextLinks			= new ArrayList<ContextLink>();

	private List<SymbolLink>		symbolLinksTo			= new ArrayList<SymbolLink>();
	private List<ContextLink>		contextLinksTo			= new ArrayList<ContextLink>();
	
	private long 					totalSymLinksFrom 		= 0;
	private long 					totalSymLinksTo			= 0;
	private long 					totalConLinksFrom		= 0;
	private long 					totalConLinksTo			= 0;
	
	public static boolean isStructureSymbol(String symbol) {
		return isSymbol(symbol,STRUCTURE_SYMBOLS);
	}

	public static boolean isLineEndSymbol(String symbol) {
		return isSymbol(symbol,LINE_END_SYMBOLS);
	}

	public static List<String> parseTextSymbols(StringBuilder text, int maxSymbols) {
		return parseText(text,maxSymbols,false,0);
	}

	public static List<String> parseTextSentences(StringBuilder text, int maxSymbols, int minSentenceLength) {
		return parseText(text,maxSymbols,true,minSentenceLength);
	}

	@Override
	public void fromDataObject(DbDataObject obj) {
		super.fromDataObject(obj);
		if (obj.hasPropertyValue("code")) {
			setCode(obj.getPropertyValue("code").toString());
		}
		if (obj.hasPropertyValue("enabled")) {
			setEnabled(Boolean.parseBoolean(obj.getPropertyValue("enabled").toString()));
		}
		if (obj.hasPropertyValue("level")) {
			setLevel(Long.parseLong(obj.getPropertyValue("level").toString()));
		}
		if (obj.hasPropertyValue("totalSymLinksFrom")) {
			setTotalSymLinksFrom(Long.parseLong(obj.getPropertyValue("totalSymLinksFrom").toString()));
		}
		if (obj.hasPropertyValue("totalSymLinksTo")) {
			setTotalSymLinksTo(Long.parseLong(obj.getPropertyValue("totalSymLinksTo").toString()));
		}
		if (obj.hasPropertyValue("totalConLinksFrom")) {
			setTotalConLinksFrom(Long.parseLong(obj.getPropertyValue("totalConLinksFrom").toString()));
		}
		if (obj.hasPropertyValue("totalConLinksTo")) {
			setTotalConLinksTo(Long.parseLong(obj.getPropertyValue("totalConLinksTo").toString()));
		}
	}

	@Override
	public DbDataObject toDataObject() {
		DbDataObject r = super.toDataObject();
		r.setPropertyValue("code",new StringBuilder(getCode()));
		r.setPropertyValue("enabled",new StringBuilder("" + isEnabled()));
		r.setPropertyValue("level",new StringBuilder("" + getLevel()));
		r.setPropertyValue("totalSymLinksFrom",new StringBuilder("" + getTotalSymLinksFrom()));
		r.setPropertyValue("totalSymLinksTo",new StringBuilder("" + getTotalSymLinksTo()));
		r.setPropertyValue("totalConLinksFrom",new StringBuilder("" + getTotalConLinksFrom()));
		r.setPropertyValue("totalConLinksTo",new StringBuilder("" + getTotalConLinksTo()));
		return r;
	}

	public long increaseLevel(long amount) {
		long r = 0;
		lockMe(this);
		level = level + amount;
		r = level;
		unlockMe(this);
		return r;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		lockMe(this);
		boolean r = enabled;
		unlockMe(this);
		return r;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		lockMe(this);
		this.enabled = enabled;
		unlockMe(this);
	}

	/**
	 * @return the level
	 */
	public long divideSymbolLevelBy(int div) {
		lockMe(this);
		level = (level / div);
		long r = level;
		unlockMe(this);
		return r;
	}

	/**
	 * @return the level
	 */
	public long getLevel() {
		lockMe(this);
		long r = level;
		unlockMe(this);
		return r;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(long level) {
		lockMe(this);
		this.level = level;
		unlockMe(this);
	}

	/**
	 * @return the links
	 */
	public List<SymbolLink> getSymbolLinks() {
		List<SymbolLink> r = null;
		lockMe(this);
		r = new ArrayList<SymbolLink>(symbolLinks);
		unlockMe(this);
		return r;
	}

	/**
	 * @return the links for a certain distance
	 */
	public List<SymbolLink> getSymbolLinks(int distance) {
		List<SymbolLink> r = new ArrayList<SymbolLink>();
		lockMe(this);
		for (SymbolLink link: symbolLinks) {
			if (link.getDistance()==distance) {
				r.add(link);
			}
		}
		unlockMe(this);
		return r;
	}

	public void addSymbolLink(SymbolLink link) {
		boolean update = false;
		lockMe(this);
		if (!symbolLinks.contains(link)) {
			symbolLinks.add(link);
			totalSymLinksFrom = (long) symbolLinks.size(); 
			update = true;
		}
		unlockMe(this);
		if (update) {
			link.getSymbolTo().addSymbolLinkTo(link);
		}
	}
	
	public void removeSymbolLink(SymbolLink link) {
		boolean update = false;
		lockMe(this);
		if (symbolLinks.contains(link)) {
			symbolLinks.remove(link);
			totalSymLinksFrom = (long) symbolLinks.size(); 
			update = true;
		}
		unlockMe(this);
		if (update) {
			link.getSymbolTo().removeSymbolLinkTo(link);
		}
	}
	
	public SymbolLink addSymbolLinkIfNotExists(Symbol to, int distance) {
		boolean update = false;
		SymbolLink link = null;
		lockMe(this);
		for (SymbolLink lnk: symbolLinks) {
			if (lnk.getSymbolToId()==to.getId() && lnk.getDistance()==distance) {
				link = lnk;
				break;
			}
		}
		if (link==null) {
			link = new SymbolLink();
			link.setSymbolFrom(this);
			link.setSymbolTo(to);
			link.setDistance(distance);
			symbolLinks.add(link);
			totalSymLinksFrom = (long) symbolLinks.size();
			update = true;
		}
		unlockMe(this);
		if (update) {
			to.addSymbolLinkTo(link);
		}
		return link;
	}

	protected void addSymbolLinkTo(SymbolLink link) {
		lockMe(this);
		if (!symbolLinksTo.contains(link)) {
			symbolLinksTo.add(link);
			totalSymLinksTo = (long) symbolLinksTo.size(); 
		}
		unlockMe(this);
	}
	
	protected void removeSymbolLinkTo(SymbolLink link) {
		lockMe(this);
		if (symbolLinksTo.contains(link)) {
			symbolLinksTo.remove(link);
			totalSymLinksTo = (long) symbolLinksTo.size(); 
		}
		unlockMe(this);
	}

	public long getTotalSymbolLinkCount() {
		long r = 0;
		lockMe(this);
		if (symbolLinks.size()>0) {
			for (SymbolLink link: symbolLinks) {
				r = r + link.getCount();
			}
		}
		unlockMe(this);
		return r;
	}
	
	public long getTotalSymbolLinks() {
		long r = 0;
		lockMe(this);
		r = symbolLinks.size();
		unlockMe(this);
		return r;
	}

	public List<SymbolLink> divideSymbolLinkCountsByTwo() {
		List<SymbolLink> removeLinks = new ArrayList<SymbolLink>();
		lockMe(this);
		if (symbolLinks.size()>0) {
			for (SymbolLink link: symbolLinks) {
				if (link.getCount()>1) {
					link.divideCountByTwo();
				} else {
					link.setCount(0);
					removeLinks.add(link);
				}
			}
		}
		unlockMe(this);
		return removeLinks;
	}

	/**
	 * @return the links
	 */
	public List<ContextLink> getContextLinks() {
		List<ContextLink> r = null;
		lockMe(this);
		r = new ArrayList<ContextLink>(contextLinks);
		unlockMe(this);
		return r;
	}
	
	public void addContextLink(ContextLink link) {
		boolean update = false;
		lockMe(this);
		if (!contextLinks.contains(link)) {
			contextLinks.add(link);
			totalConLinksFrom = (long) contextLinks.size();
			update = true;
		}
		unlockMe(this);
		if (update) {
			link.getSymbolTo().addContextLinkTo(link);
		}
	}
	
	public void removeContextLink(ContextLink link) {
		boolean update = false;
		lockMe(this);
		if (contextLinks.contains(link)) {
			contextLinks.remove(link);
			totalConLinksFrom = (long) contextLinks.size(); 
			update = true;
		}
		unlockMe(this);
		if (update) {
			link.getSymbolTo().removeContextLinkTo(link);
		}
	}
	
	public ContextLink addContextLinkIfNotExists(Symbol to) {
		boolean update = false;
		ContextLink link = null;
		lockMe(this);
		for (ContextLink lnk: contextLinks) {
			if (lnk.getSymbolToId()==to.getId()) {
				link = lnk;
				break;
			}
		}
		if (link==null) {
			link = new ContextLink();
			link.setSymbolFrom(this);
			link.setSymbolTo(to);
			contextLinks.add(link);
			totalConLinksFrom = (long) contextLinks.size();
			update = true;
		}
		unlockMe(this);
		if (update) {
			to.addContextLinkTo(link);
		}
		return link;
	}
	
	protected void addContextLinkTo(ContextLink link) {
		lockMe(this);
		if (!contextLinksTo.contains(link)) {
			contextLinksTo.add(link);
			totalConLinksTo = (long) contextLinksTo.size(); 
		}
		unlockMe(this);
	}
	
	protected void removeContextLinkTo(ContextLink link) {
		lockMe(this);
		if (contextLinksTo.contains(link)) {
			contextLinksTo.remove(link);
			totalConLinksTo = (long) contextLinksTo.size(); 
		}
		unlockMe(this);
	}
	
	public long getTotalContextLinkCount() {
		long r = 0;
		lockMe(this);
		if (contextLinks.size()>0) {
			for (ContextLink link: contextLinks) {
				r = r + link.getCount();
			}
		}
		unlockMe(this);
		return r;
	}

	public long getTotalContextLinks() {
		long r = 0;
		lockMe(this);
		r = contextLinks.size();
		unlockMe(this);
		return r;
	}

	public List<ContextLink> divideContextLinkCountsByTwo() {
		List<ContextLink> removeLinks = new ArrayList<ContextLink>();
		lockMe(this);
		if (contextLinks.size()>0) {
			for (ContextLink link: contextLinks) {
				if (link.getCount()>1) {
					link.divideCountByTwo();
				} else {
					link.setCount(0);
					removeLinks.add(link);
				}
			}
		}
		unlockMe(this);
		return removeLinks;
	}

	private static boolean isSymbol(String symbol,String[] symbols) {
		boolean r = false;
		for (int i = 0; i < symbols.length; i++) {
			if (symbol.equals(symbols[i])) {
				r = true;
				break;
			}
		}
		return r;
	}

	private static List<String> parseText(StringBuilder text, int maxSymbols, boolean asSentences, int minSentenceLength) {
		text = Generic.stringBuilderReplace(text,"\""," \" ");
		List<String> r = new ArrayList<String>();
		List<StringBuilder> elements = Generic.stringBuilderSplit(text," ");
		List<String> s = new ArrayList<String>();
		StringBuilder sentence = new StringBuilder();
		boolean skippedLineEnd = false;
		for (StringBuilder elem: elements) {
			StringBuilder insert = null;
			StringBuilder clean = null;
			StringBuilder add = null;
			if (Generic.stringBuilderContainsOneOfNonAllowedCharacters(elem,Generic.ALPHANUMERIC)) {
				boolean separated = false;
				for (int i = 0; i<STRUCTURE_SYMBOLS.length; i++) {
					while (elem.length()>1 && Generic.stringBuilderStartsWith(elem,STRUCTURE_SYMBOLS[i])) {
						insert = new StringBuilder(STRUCTURE_SYMBOLS[i]);
						clean = elem.delete(0,1);
						elem = clean;
						separated = true;
						if (elem.length()==0 || !Generic.stringBuilderStartsWith(elem,STRUCTURE_SYMBOLS[i])) {
							i = 0;
						}
						if (clean.length()==0) {
							clean = insert;
							insert = null;
							break;
						}
					}
				}
				for (int i = 0; i<STRUCTURE_SYMBOLS.length; i++) {
					while (elem.length()>1 && Generic.stringBuilderEndsWith(elem,STRUCTURE_SYMBOLS[i])) {
						add = new StringBuilder(STRUCTURE_SYMBOLS[i]);
						clean = elem.delete((elem.length() - 1),elem.length());
						elem = clean;
						separated = true;
						if (clean.length()==0) {
							clean = add;
							add = null;
							break;
						}
						if (elem.length()==0 || !Generic.stringBuilderEndsWith(elem,STRUCTURE_SYMBOLS[i])) {
							i = 0;
						}
					}
				}
				if (!separated) {
					clean = elem;
				}
			} else {
				clean = elem;
			}
			
			boolean endInsert = false;
			boolean endClean = false;
			boolean endAdd = false;
			if (insert!=null && isLineEndSymbol(insert.toString())) {
				endInsert = true;
			}
			if (clean!=null && isLineEndSymbol(clean.toString())) {
				endClean = true;
			}
			if (add!=null && isLineEndSymbol(add.toString())) {
				endAdd = true;
			}
			if (endClean) {
				if (asSentences && sentence.length()==0) {
					clean = null;
				}
				if (endInsert) {
					insert = null;
				}
				if (endAdd) {
					add = null;
				}
			}
			
			if (clean!=null && clean.length()>0) {
				boolean skipLineEnd = false;
				if (endAdd && add.toString().equals(".") && !Generic.stringBuilderContainsOneOfNonAllowedCharacters(clean,Generic.ALPHABETIC + ".",true)) {
					boolean append = true;
					for (int i = 0; i < clean.length(); i++) {
						String c = clean.substring(i,(i+1)); 
						if ((i % 2 == 0) && Generic.ALPHABETIC.indexOf(c)<0) {
							append = false;
							break;
						} else if ((i % 2 == 1) && !c.equals(".")) {
							append = false;
							break;
						}
					}
					if (append) {
						clean.append(add);
						add = null;
						skipLineEnd = true;
					}
				}
				
				if (insert!=null && insert.length()>0) {
					r.add(insert.toString());
					if (asSentences) {
						sentence.append(insert);
						boolean ended = checkLineEnd(sentence,minSentenceLength,s,insert);
						if (ended) {
							sentence = new StringBuilder();
						} else {
							sentence.append(" ");
						}
					}
					if (maxSymbols>0 && r.size()>=maxSymbols) {
						break;
					}
				} else if (skipLineEnd && skippedLineEnd) {
					String t = r.remove((r.size() - 1));
					t = t + clean;
					r.add(t);
				} else {
					if (clean.length()>64) {
						clean = new StringBuilder(clean.substring(0,64));
					}
					r.add(clean.toString());
				}
				if (asSentences) {
					sentence.append(clean);
					if (!skipLineEnd) {
						boolean ended = checkLineEnd(sentence,minSentenceLength,s,clean);
						if (ended) {
							sentence = new StringBuilder();
						} else {
							sentence.append(" ");
						}
					} else {
						sentence.append(" ");
					}
				}
				if (maxSymbols>0 && r.size()>=maxSymbols) {
					break;
				}
				
				if (add!=null && add.length()>0) {
					r.add(add.toString());
					if (asSentences) {
						sentence.append(add);
						boolean ended = checkLineEnd(sentence,minSentenceLength,s,add);
						if (ended) {
							sentence = new StringBuilder();
						} else {
							sentence.append(" ");
						}
					}
					if (maxSymbols>0 && r.size()>=maxSymbols) {
						break;
					}
				}
				if (skipLineEnd) {
					skippedLineEnd = true;
				} else {
					skippedLineEnd = false;
				}
			}
		}
		if (asSentences) {
			r = s;
		}
		return r;
	}
	
	// Helper for parseText
	private static boolean checkLineEnd(StringBuilder sentence, int minSentenceLength,List<String> sentences,StringBuilder addText) {
		boolean ended = false;
		if (sentence.length()>1 && isLineEndSymbol(addText.toString())) {
			if (sentence.length()>=minSentenceLength) {
				sentences.add(sentence.toString());
			}
			ended = true;
		}
		return ended;
	}

	/**
	 * @return the totalSymLinksFrom
	 */
	public long getTotalSymLinksFrom() {
		lockMe(this);
		long r = totalSymLinksFrom;
		unlockMe(this);
		return r;
	}

	/**
	 * @param totalSymLinksFrom the totalSymLinksFrom to set
	 */
	public void setTotalSymLinksFrom(long totalSymLinksFrom) {
		lockMe(this);
		this.totalSymLinksFrom = totalSymLinksFrom;
		unlockMe(this);
	}

	/**
	 * @return the totalSymLinksTo
	 */
	public long getTotalSymLinksTo() {
		lockMe(this);
		long r = totalSymLinksTo;
		unlockMe(this);
		return r;
	}

	/**
	 * @param totalSymLinksTo the totalSymLinksTo to set
	 */
	public void setTotalSymLinksTo(long totalSymLinksTo) {
		lockMe(this);
		this.totalSymLinksTo = totalSymLinksTo;
		unlockMe(this);
	}

	/**
	 * @return the totalConLinksFrom
	 */
	public long getTotalConLinksFrom() {
		lockMe(this);
		long r = totalConLinksFrom;
		unlockMe(this);
		return r;
	}

	/**
	 * @param totalConLinksFrom the totalConLinksFrom to set
	 */
	public void setTotalConLinksFrom(long totalConLinksFrom) {
		lockMe(this);
		this.totalConLinksFrom = totalConLinksFrom;
		unlockMe(this);
	}

	/**
	 * @return the totalConLinksTo
	 */
	public long getTotalConLinksTo() {
		lockMe(this);
		long r = totalConLinksTo;
		unlockMe(this);
		return r;
	}

	/**
	 * @param totalConLinksTo the totalConLinksTo to set
	 */
	public void setTotalConLinksTo(long totalConLinksTo) {
		lockMe(this);
		this.totalConLinksTo = totalConLinksTo;
		unlockMe(this);
	}
}
