package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.Generic;

public class TestGenericEncodeDecode {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//String original = "4838234018234802934819028349083900019992";
		String original = "4";
		System.out.println("Original     : " + original);

		StringBuilder sb = new StringBuilder();
		sb.append(original);
		String compressed = Generic.compress(sb).toString();
		System.out.println("Compressed   : " + compressed);

		sb = new StringBuilder();
		sb.append(compressed);
		String decompressed = Generic.decompress(sb).toString();
		System.out.println("Decompressed : " + decompressed);

		if (decompressed.equals(original)) {
			String key = Generic.generateNewKey(1024);

			String text = "This is a test text 12345 used to test the encryption/decription code<a>2<a> code<a>2<a> code<a>2<a> code<a>2<a> code<a>2<a> code<a>2<a> code<a>2<a>";
			text = text + Generic.SEP_OBJ + text;
			
			System.out.println("Text    : " + text);
			
			StringBuilder encoded = Generic.encodeKey(new StringBuilder(text), key, 5);
			System.out.println("Encoded : " + encoded);

			StringBuilder decoded = Generic.decodeKey(encoded, key, 5);
			System.out.println("Decoded : " + decoded);
		}
		
		StringBuilder str = new StringBuilder("asciiEncodeDecodeTest");
		System.out.println("Ascii encode/decode: " + str);
		str = Generic.encodeAscii(str);
		System.out.println("Ascii encoded: " + str);
		str = Generic.decodeAscii(str);
		System.out.println("Ascii decoded: " + str);
		
	}

}
