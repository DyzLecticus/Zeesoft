package nl.zeesoft.zodb.test.tests;

import java.util.List;

import nl.zeesoft.zodb.Generic;

public class GenericStringBuffer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StringBuffer original = new StringBuffer("Hello my name is something different which is why i wrote this text");

		System.out.println("Original     : " + original);
		
		Generic.stringBufferReplace(original, "is", "BLA");
		
		System.out.println("Replaced     : " + original);
		
		original = new StringBuffer("Some stuff to try the splitter with:\nqwerqwerqwer\nasdfasdfasdfasdf\nzxcvzxcv zxcvzxcv zxcvzxcv\nIt works!");
		
		List<StringBuffer> strs = Generic.stringBufferSplit(original,"\n");

		for (StringBuffer sb: strs) {
			System.out.println("Split : " + sb);
		}
		
		original = new StringBuffer("Some other stuff to try the splitter with:fghjqwerqwerqwerfghjasdfasdfasdfasdffghjzxcvzxcv zxcvzxcv zxcvzxcvfghjIt works!");

		List<StringBuffer> strs2 = Generic.stringBufferSplit(original,"fghj");

		for (StringBuffer sb: strs2) {
			System.out.println("Split : " + sb);
		}
	}

}
