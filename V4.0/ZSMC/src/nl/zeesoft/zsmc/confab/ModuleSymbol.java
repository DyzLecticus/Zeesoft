package nl.zeesoft.zsmc.confab;

public class ModuleSymbol {
	public String		symbol			= "";
	public double		prob			= 0;
	public double		probNormalized	= 0;

	public ModuleSymbol copy() {
		ModuleSymbol r = new ModuleSymbol();
		r.symbol = this.symbol;
		r.prob = this.prob;
		r.probNormalized = this.probNormalized;
		return r;
	}
}
