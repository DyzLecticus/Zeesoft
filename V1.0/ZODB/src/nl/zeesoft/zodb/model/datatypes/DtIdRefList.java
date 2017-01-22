package nl.zeesoft.zodb.model.datatypes;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zodb.Generic;

public class DtIdRefList extends DtObject {
	public DtIdRefList() {
		super(new ArrayList<Long>());
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof ArrayList<?>) {
			super.setValue(value);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getValue() {
		List<Long> r = null;
		if ((super.getValue()!=null) && (super.getValue() instanceof ArrayList<?>)) {
			r = (ArrayList<Long>) super.getValue();
		}
		return r;
	}
	
	public static DtIdRefList copy(DtIdRefList original) {
		DtIdRefList r = new DtIdRefList();
		for (long id: (ArrayList<Long>) original.getValue()) {
			r.getValue().add(new Long(id));
		}
		return r;
	}

	@Override
	public StringBuffer toStringBuffer() {
		StringBuffer s = new StringBuffer();
		for (long id: this.getValue()) {
			if (s.length()>0) {
				s.append(Generic.SEP_STR);
			}
			s.append(id);
		}
		return s;
	}

	public static List<Long> parseDtIdRefList(String s) {
		List<Long> l = new ArrayList<Long>();
		if ((s!=null) && (s.length()>0)) {
			for (String val: Generic.getValuesFromString(s)) {
				l.add(Long.parseLong(val));
			}
		}
		return l;
	}
}
