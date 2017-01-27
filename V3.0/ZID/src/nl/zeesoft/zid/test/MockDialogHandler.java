package nl.zeesoft.zid.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zid.dialog.DialogHandler;
import nl.zeesoft.zspr.pattern.PatternManager;
import nl.zeesoft.zspr.pattern.PatternObject;

public class MockDialogHandler extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockDialogHandler*.");
	}

	@Override
	protected Object initialzeMock() {
		List<Dialog> dialogs = new ArrayList<Dialog>();
		
		PatternManager manager = new PatternManager();
		manager.initializePatterns();
		
		Dialog dialog = new Dialog("EnglishHandshake");
		dialogs.add(dialog);
		
		dialog.addExample("Hello.","Hello. My name is Dyz Lecticus. What is your name?");
		dialog.addExample("Hello!","Hello. My name is Dyz Lecticus. What is your name?");
		dialog.addExample("Hi.","Hi. My name is Dyz Lecticus. What is your name?");
		dialog.addExample("Hi!","Hi. My name is Dyz Lecticus. What is your name?");
		dialog.addExample("Who are you?","My name is Dyz Lecticus. What is your name?");
		dialog.addExample("What is your name?","My name is Dyz Lecticus. What is your name?");
		
		dialog.addVariable("firstName",PatternObject.TYPE_ALPHABETIC);
		dialog.addVariableExample("firstName","What is your name?","My name is {firstName} {preposition} {lastName}.");
		dialog.addVariableExample("firstName","What is your name?","My name is {firstName} {lastName}.");
		dialog.addVariableExample("firstName","What is your name?","My name is {firstName}.");
		dialog.addVariableExample("firstName","What is your name?","{firstName} {preposition} {lastName}.");
		dialog.addVariableExample("firstName","What is your name?","{firstName} {lastName}.");
		dialog.addVariableExample("firstName","What is your name?","{firstName}.");

		dialog.addVariable("lastName",PatternObject.TYPE_ALPHABETIC);
		dialog.addVariableExample("lastName","What is your lastname?","My lastname is {preposition} {lastName}.");
		dialog.addVariableExample("lastName","What is your lastname?","My lastname is {lastName}, {preposition}.");
		dialog.addVariableExample("lastName","What is your lastname?","My lastname is {lastName}.");
		dialog.addVariableExample("lastName","What is your lastname?","{preposition} {lastName}.");
		dialog.addVariableExample("lastName","What is your lastname?","{lastName}, {preposition}.");
		dialog.addVariableExample("lastName","What is your lastname?","{lastName}.");
		
		Date start = new Date();
		DialogHandler handler = new DialogHandler(dialogs,manager);
		handler.initialize();
		System.out.println("Initializing dialog handler took " + ((new Date()).getTime() - start.getTime()) + " ms");
		System.out.println();
		return handler;
	}
}
