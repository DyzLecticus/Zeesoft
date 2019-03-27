package nl.zeesoft.zsmc.confab;

public class ModuleSymbol {
	protected String		symbol			= "";
	protected double		prob			= 0;
	protected double		probNormalized	= 0;

	protected ModuleSymbol copy() {
		ModuleSymbol r = new ModuleSymbol();
		r.symbol = this.symbol;
		r.prob = this.prob;
		r.probNormalized = this.probNormalized;
		return r;
	}
}
