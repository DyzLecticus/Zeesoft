package nl.zeesoft.zodb.test.impl;

import java.util.List;

import nl.zeesoft.zodb.Generic;

public class TestGenericStringBuilder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringBuilder original = new StringBuilder("Hello my name is something different which is why i wrote this text");

		System.out.println("Original     : " + original);
		
		Generic.stringBuilderReplace(original, "is", "BLA");
		
		System.out.println("Replaced     : " + original);
		
		original = new StringBuilder("Some stuff to try the splitter with:\nqwerqwerqwer\nasdfasdfasdfasdf\nzxcvzxcv zxcvzxcv zxcvzxcv\nIt works!");
		
		List<StringBuilder> strs = Generic.stringBuilderSplit(original,"\n");

		for (StringBuilder sb: strs) {
			System.out.println("Split : " + sb);
		}
		
		original = new StringBuilder("Some other stuff to try the splitter with:fghjqwerqwerqwerfghjasdfasdfasdfasdffghjzxcvzxcv zxcvzxcv zxcvzxcvfghjIt works!");

		List<StringBuilder> strs2 = Generic.stringBuilderSplit(original,"fghj");

		for (StringBuilder sb: strs2) {
			System.out.println("Split : " + sb);
		}

		original = new StringBuilder("Some other stuff to try the replace<a href=\"http://qwerqwer\">start end</a> works to remove anchor start tag with href!");
		original = Generic.stringBuilderReplaceStartEnd(original,"<a ",">"," ");
		System.out.println("Removed anchor : " + original);
	}

}
