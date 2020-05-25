package nl.zeesoft.zdk.test.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.persist.PersistableProperty;

public class PersistableParent extends PersistableAbstract {
	@PersistableProperty()
	private List<String>			testStringList	= new ArrayList<String>();
	@PersistableProperty()
	private List<Float>				testFloatList	= new ArrayList<Float>();
	@PersistableProperty()
	private String[]				testStringArray	= null;
	@PersistableProperty()
	private Integer[]				testIntArray	= null;
	@PersistableProperty()
	private List<PersistableChild>	testChildren	= new ArrayList<PersistableChild>();
	@PersistableProperty()
	private PersistableParent		testPartner		= null;
	
	public List<String> getTestStringList() {
		return testStringList;
	}
	
	public void setTestStringList(List<String> testStringList) {
		this.testStringList = testStringList;
	}
	
	public List<Float> getTestFloatList() {
		return testFloatList;
	}
	
	public void setTestFloatList(List<Float> testFloatList) {
		this.testFloatList = testFloatList;
	}
	
	public String[] getTestStringArray() {
		return testStringArray;
	}
	
	public void setTestStringArray(String[] testStringArray) {
		this.testStringArray = testStringArray;
	}
	
	public List<PersistableChild> getTestChildren() {
		return testChildren;
	}
	
	public void setTestChildren(List<PersistableChild> testChildren) {
		this.testChildren = testChildren;
	}
	
	public PersistableParent getTestPartner() {
		return testPartner;
	}
	
	public void setTestPartner(PersistableParent testPartner) {
		this.testPartner = testPartner;
	}

	public Integer[] getTestIntArray() {
		return testIntArray;
	}

	public void setTestIntArray(Integer[] testIntArray) {
		this.testIntArray = testIntArray;
	}
}
