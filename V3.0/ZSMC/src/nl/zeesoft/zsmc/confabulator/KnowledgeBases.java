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
	private Analyzer			contextAnalyzer	= new Analyzer();

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
		contextSymbols.clear();
		contextSymbols.add(contextSymbol);
		contextAnalyzer.addSymbols(contextSymbols);
	}

	@Override
	public void handleContextSymbol(ZStringBuilder context) {
		contextSymbols.clear();
		for (ZStringBuilder cs: context.split(" ")) {
			contextSymbols.add(cs.toString());
		}
		contextAnalyzer.addSymbols(contextSymbols);
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
			for (String contextSymbol: contextSymbols) {
				context.learnLink(contextSymbol,symbol);
			}
			s++;
		}
		contextSymbols.clear();
	}

	@Override
	public void calculateProb() {
		super.calculateProb();
		contextAnalyzer.calculateProb();
		for (int i = 1; i<modules; i++) {
			knowledgeBases.get(i - 1).calculateProb(this,null,B,p0);
		}
		context.calculateProb(this,contextAnalyzer,B,p0);
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
