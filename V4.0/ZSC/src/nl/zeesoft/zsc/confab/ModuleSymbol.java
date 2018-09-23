package nl.zeesoft.zsc.confab;

public class ModuleSymbol {
	public String 	symbol	= "";
	public double	prob	= 0;
	
	public ModuleSymbol copy() {
		ModuleSymbol r = new ModuleSymbol();
		r.symbol = this.symbol;
		r.prob = this.prob;
		return r;
	}
}
