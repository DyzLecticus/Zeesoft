package nl.zeesoft.zso.composition;

import java.util.ArrayList;
import java.util.List;

public class BarPosition {
	private String				positionName		= "";
	private List<PositionStep>	steps				= new ArrayList<PositionStep>();
	
	public String getPositionName() {
		return positionName;
	}
	
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	
	public List<PositionStep> getSteps() {
		return steps;
	}
	
	public void setSteps(List<PositionStep> steps) {
		this.steps = steps;
	}
}
