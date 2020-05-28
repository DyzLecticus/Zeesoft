package nl.zeesoft.zdk.test.impl;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.collection.PersistableProperty;

public class PCTestParent extends PCTestObject {
	@PersistableProperty()
	private List<String>			testStringList	= new ArrayList<String>();
	@PersistableProperty()
	private List<Float>				testFloatList	= new ArrayList<Float>();
	@PersistableProperty()
	private String[]				testStringArray	= null;
	@PersistableProperty()
	private int[]					testIntArray	= null;
	@PersistableProperty()
	private List<PCTestChild>		testChildren	= new ArrayList<PCTestChild>();
	@PersistableProperty()
	private PCTestParent			testPartner		= null;
	
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
	
	public List<PCTestChild> getTestChildren() {
		return testChildren;
	}
	
	public void setTestChildren(List<PCTestChild> testChildren) {
		this.testChildren = testChildren;
	}
	
	public PCTestParent getTestPartner() {
		return testPartner;
	}
	
	public void setTestPartner(PCTestParent testPartner) {
		this.testPartner = testPartner;
	}

	public int[] getTestIntArray() {
		return testIntArray;
	}

	public void setTestIntArray(int[] testIntArray) {
		this.testIntArray = testIntArray;
	}
}
