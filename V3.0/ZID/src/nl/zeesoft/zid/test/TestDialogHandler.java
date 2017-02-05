package nl.zeesoft.zid.test;

import java.util.Date;

import nl.zeesoft.zdk.ZStringSymbolParser;
import nl.zeesoft.zdk.test.TestObject;
import nl.zeesoft.zdk.test.Tester;
import nl.zeesoft.zid.dialog.DialogHandler;

public class TestDialogHandler extends TestObject {
	public TestDialogHandler(Tester tester) {
		super(tester);
	}

	public static void main(String[] args) {
		(new TestDialogHandler(new Tester())).test(args);
	}

	@Override
	protected void describe() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void test(String[] args) {
		DialogHandler handler = (DialogHandler) getTester().getMockedObject(MockDialogHandler.class.getName());
		
		Date started = new Date();
		
		// Known user handshake
		ZStringSymbolParser output = handler.processInput(new ZStringSymbolParser("hello"));
		assertEqual(output.toString(),"Hello. My name is Dyz Lecticus. What is your name?","Output does not match expectation");
		output = handler.processInput(new ZStringSymbolParser("my name is andre van der zee"));
		assertEqual(output.toString(),"Nice to interact with you again Andre van der Zee.","Output does not match expectation");
		
		// Extended handshake
		output = handler.processInput(new ZStringSymbolParser("hallo,ik heet karel de grote."));
		assertEqual(output.toString(),"Wat kan ik voor je doen Karel de Grote?","Output does not match expectation");
		
		// Handshake prompt sequence
		output = handler.processInput(new ZStringSymbolParser("hallo?"));
		assertEqual(output.toString(),"Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?","Output does not match expectation");
		output = handler.processInput(new ZStringSymbolParser("gekke henkie"));
		assertEqual(output.toString(),"Wat kan ik voor je doen Gekke Henkie?","Output does not match expectation");
		output = handler.processInput(new ZStringSymbolParser("gekke"));
		assertEqual(output.toString(),"Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?","Output does not match expectation");
		output = handler.processInput(new ZStringSymbolParser("van henkie"));
		assertEqual(output.toString(),"Wat is jouw achternaam?","Output does not match expectation");

		// Handshake context switch
		output = handler.processInput(new ZStringSymbolParser("what is your name?"));
		assertEqual(output.toString(),"My name is Dyz Lecticus. What is your name?","Output does not match expectation");
		output = handler.processInput(new ZStringSymbolParser("hoe heet jij?"));
		assertEqual(output.toString(),"Mijn naam is Dyz Lecticus. Wat is jouw naam?","Output does not match expectation");

		System.out.println(handler.getLog());

		long time = ((new Date()).getTime() - started.getTime()) / 10; 
		System.out.println("Average time spent thinking per response: " + time + " ms");
	}
}
