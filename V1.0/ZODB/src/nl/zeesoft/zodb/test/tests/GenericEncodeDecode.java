package nl.zeesoft.zodb.test.tests;

import nl.zeesoft.zodb.Generic;

public class GenericEncodeDecode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String original = "4838234018234802934819028349083900019992";
		String original = "4";
		System.out.println("Original     : " + original);

		StringBuffer sb = new StringBuffer();
		sb.append(original);
		String compressed = Generic.compress(sb).toString();
		System.out.println("Compressed   : " + compressed);

		sb = new StringBuffer();
		sb.append(compressed);
		String decompressed = Generic.decompress(sb).toString();
		System.out.println("Decompressed : " + decompressed);

		if (decompressed.equals(original)) {
			String key = Generic.generateNewKey(1024);

			String text = "This is a test text 12345 used to test the encryption/decription code<a>2<a> code<a>2<a> code<a>2<a> code<a>2<a> code<a>2<a> code<a>2<a> code<a>2<a>";
			text = text + Generic.SEP_OBJ + text;
			
			System.out.println("Text    : " + text);
			
			StringBuffer encoded = Generic.encodeKey(new StringBuffer(text), key, 5);
			System.out.println("Encoded : " + encoded);

			StringBuffer decoded = Generic.decodeKey(encoded, key, 5);
			System.out.println("Decoded : " + decoded);
		}
	}

}
