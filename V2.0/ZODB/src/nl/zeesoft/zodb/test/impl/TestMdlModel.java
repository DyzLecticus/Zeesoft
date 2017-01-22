package nl.zeesoft.zodb.test.impl;

import nl.zeesoft.zodb.database.DbConfig;
import nl.zeesoft.zodb.database.model.MdlModel;
import nl.zeesoft.zodb.file.xml.XMLFile;
import nl.zeesoft.zodb.test.TestConfig;

public class TestMdlModel {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestConfig.getInstance();
		
		MdlModel model = DbConfig.getInstance().getModel();
		XMLFile file = model.toXML();
		StringBuilder str = file.toStringReadFormat();
		
		System.out.println(str);
		
		MdlModel modelCompare = new MdlModel();
		modelCompare.fromXML(file);
		XMLFile fileCompare = modelCompare.toXML();
		StringBuilder strCompare = fileCompare.toStringReadFormat();
		
		boolean ok = strCompare.toString().equals(str.toString());
		if (!ok) {
			System.err.println("!!!");
			System.err.println(strCompare);
		} else {
			modelCompare.fromXML(file);

			file.cleanUp();
			fileCompare.cleanUp();
					
			fileCompare = modelCompare.toXML();
			strCompare = fileCompare.toStringReadFormat();
			
			model.fromXML(fileCompare);
			file = model.toXML();
			str = file.toStringReadFormat();
			
			ok = strCompare.toString().equals(str.toString());
			if (!ok) {
				System.err.println("!!!!!!");
				System.err.println(strCompare);
				System.err.println("======");
				System.err.println(str);
				int index = 0;
				String[] lines = str.toString().split("\n");
				for (String lineCompare: strCompare.toString().split("\n")) {
					if (!lines[index].equals(lineCompare)) {
						System.err.println(index + ":>>>" + lines[index]);
						System.err.println(index + ":<<<" + lineCompare);
						break;
					}
					index++;
				}
			} else {
				file = model.toXML(true,true);
				str = file.toStringReadFormat();
				System.out.println("========== EXTENDED REAL MODEL ==========");
				System.out.println(str);
			}
		}
		
		file.cleanUp();
		fileCompare.cleanUp();
	}
}
