package nl.zeesoft.zdk.test.impl.collection;

import java.util.ArrayList;
import java.util.List;

public class CollectionTestParent extends CollectionTestObject {
	private List<String>				testStringList	= new ArrayList<String>();
	private List<Float>					testFloatList	= new ArrayList<Float>();
	private String[]					testStringArray	= null;
	private int[]						testIntArray	= null;
	private List<CollectionTestChild>	testChildren	= new ArrayList<CollectionTestChild>();
	private CollectionTestParent		testPartner		= null;
	
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
	
	public List<CollectionTestChild> getTestChildren() {
		return testChildren;
	}
	
	public void setTestChildren(List<CollectionTestChild> testChildren) {
		this.testChildren = testChildren;
	}
	
	public CollectionTestParent getTestPartner() {
		return testPartner;
	}
	
	public void setTestPartner(CollectionTestParent testPartner) {
		this.testPartner = testPartner;
	}

	public int[] getTestIntArray() {
		return testIntArray;
	}

	public void setTestIntArray(int[] testIntArray) {
		this.testIntArray = testIntArray;
	}
}
