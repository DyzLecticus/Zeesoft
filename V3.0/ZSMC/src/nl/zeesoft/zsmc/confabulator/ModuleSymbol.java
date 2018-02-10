package nl.zeesoft.zsmc.confabulator;

public class ModuleSymbol {
	public String	symbol		= "";
	public double	excitation	= 0D;
	
	public ModuleSymbol getCopy() {
		ModuleSymbol r = new ModuleSymbol();
		r.symbol = this.symbol;
		r.excitation = this.excitation;
		return r;
	}
}
