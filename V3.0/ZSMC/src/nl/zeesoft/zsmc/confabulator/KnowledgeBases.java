package nl.zeesoft.zsmc.confabulator;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.ZStringBuilder;
import nl.zeesoft.zsmc.sequence.Analyzer;

public class KnowledgeBases extends Analyzer {
	public static final int		DEFAULT_B		= 30;
	public static final double	DEFAULT_p0		= 0.0001D;
	public static final int		DEFAULT_MODULES	= 8;
	
	private int					B				= DEFAULT_B;
	private double				p0				= DEFAULT_p0;
	private int 				modules			= DEFAULT_MODULES;

	private int					minCount		= 3;
	
	private List<KnowledgeBase>	knowledgeBases	= new ArrayList<KnowledgeBase>();
	private KnowledgeBase		context			= new KnowledgeBase();
	private String				contextSymbol	= "";

	private List<String>		contextSymbols	= new ArrayList<String>();

	public KnowledgeBases() {
		initializeModules();
	}

	public KnowledgeBases(int modules) {
		if (modules<=0) {
			modules = 2;
		}
		this.modules = modules;
		initializeModules();
	}
	
	public KnowledgeBases(int B, double p0) {
		this.B = B;
		this.p0 = p0;
		initializeModules();
	}

	public KnowledgeBases(int modules,int B, double p0) {
		if (modules<=0) {
			modules = 2;
		}
		this.modules = modules;
		this.B = B;
		this.p0 = p0;
		initializeModules();
	}

	public void initializeModules() {
		for (int i = 1; i<modules; i++) {
			knowledgeBases.add(new KnowledgeBase());
		}
	}
	
	public void setContextSymbol(String contextSymbol) {
		this.contextSymbol = contextSymbol;
	}

	@Override
	public void handleContextSymbol(ZStringBuilder context) {
		String s = context.toString();
		this.contextSymbol = s;
		if (!contextSymbols.contains(s)) {
			contextSymbols.add(s);
		}
	}
	
	@Override
	public void addSymbols(List<String> symbols) {
		super.addSymbols(symbols);
		int s = 0;
		for (String symbol: symbols) {
			for (int i = 1; i<modules; i++) {
				if (s+i<symbols.size()) {
					String nextSymbol = symbols.get(s+i); 
					knowledgeBases.get(i - 1).learnLink(symbol,nextSymbol);
				} else {
					break;
				}
			}
			if (contextSymbol.length()>0) {
				context.learnLink(contextSymbol,symbol);
			}
			s++;
		}
		contextSymbol = "";
	}

	@Override
	public void calculateProb() {
		super.calculateProb();
		for (int i = 1; i<modules; i++) {
			knowledgeBases.get(i - 1).calculateProb(this,contextSymbols,B,p0,minCount);
		}
		context.calculateProb(this,contextSymbols,B,p0,minCount);
	}

	public int getModules() {
		return modules;
	}

	public int getB() {
		return B;
	}

	public double getP0() {
		return p0;
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}

	public List<KnowledgeBase> getKnowledgeBases() {
		return knowledgeBases;
	}

	public void setKnowledgeBases(List<KnowledgeBase> knowledgeBases) {
		this.knowledgeBases = knowledgeBases;
	}

	public KnowledgeBase getContext() {
		return context;
	}

	public void setContext(KnowledgeBase context) {
		this.context = context;
	}
}
