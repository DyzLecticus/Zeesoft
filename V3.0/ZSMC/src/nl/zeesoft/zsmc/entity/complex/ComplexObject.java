package nl.zeesoft.zsmc.entity.complex;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zsmc.SequenceMatcher;
import nl.zeesoft.zsmc.entity.EntityObject;

public class ComplexObject extends EntityObject {
	private List<String> 			patterns 		= new ArrayList<String>();
	private List<ComplexVariable>	variables		= new ArrayList<ComplexVariable>();
	
	private SequenceMatcher			sequenceMatcher	= new SequenceMatcher();
	
	
}
