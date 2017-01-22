package nl.zeesoft.zadf.format.impl;

import nl.zeesoft.zadf.format.FmtSeparatedValues;

public class FmtCSVNoHeader extends FmtSeparatedValues {
	public FmtCSVNoHeader() {
		setHeader(false);
	}
}
