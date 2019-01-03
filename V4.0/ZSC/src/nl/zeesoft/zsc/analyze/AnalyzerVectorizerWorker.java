package nl.zeesoft.zsc.analyze;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.thread.Worker;

public class AnalyzerVectorizerWorker extends Worker {
	private SymbolAnalyzer	analyzer	= null;
	
	public List<String>		symbols		= new ArrayList<String>();
	public int				index		= 0;

	public AnalyzerVectorizerWorker(SymbolAnalyzer analyzer) {
		super(analyzer.getConfabulator().getConfiguration().getMessenger(),analyzer.getConfabulator().getConfiguration().getUnion());
		this.analyzer = analyzer;
		setSleep(0);
	}

	@Override
	public void whileWorking() {
		// TODO Auto-generated method stub
		String symbol = "";
		lockMe(this);
		if (index<symbols.size()) {
			symbol = symbols.get(index);
			index++;
		}
		unlockMe(this);
		if (symbol.length()==0) {
			stop();
		} else {
			analyzer.getVectorForSymbol(symbol);
		}
	}

}
