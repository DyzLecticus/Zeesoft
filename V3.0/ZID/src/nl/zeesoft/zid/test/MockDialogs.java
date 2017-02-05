package nl.zeesoft.zid.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zspr.Language;
import nl.zeesoft.zspr.pattern.PatternObject;

public class MockDialogs extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockDialogs*.");
	}

	@Override
	protected Object initialzeMock() {
		List<Dialog> dialogs = new ArrayList<Dialog>();
		
		Dialog dialog = new Dialog("Handshake",Language.ENG,HandshakeController.class.getName());
		dialogs.add(dialog);
		
		dialog.addExample("Hello. My name is {firstName} {preposition} {lastName}.","Hello {fullName}.");
		dialog.addExample("Hello, my name is {firstName} {lastName}.","Hello {fullName}.");
		dialog.addExample("Hello. My name is {firstName}.","Hello {firstName}. What is your lastname?");
		dialog.addExample("Hi. My name is {firstName} {preposition} {lastName}.","Hello {fullName}.");
		dialog.addExample("Hi, my name is {firstName} {lastName}.","Hello {fullName}.");
		dialog.addExample("Hi. My name is {firstName}.","Hello {firstName}. What is your lastname?");
		
		dialog.addExample("Hello.","Hello. My name is Dyz Lecticus. What is your name?");
		dialog.addExample("Hello!","Hello. My name is Dyz Lecticus. What is your name?");
		dialog.addExample("Hi.","Hi. My name is Dyz Lecticus. What is your name?");
		dialog.addExample("Hi!","Hi. My name is Dyz Lecticus. What is your name?");
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

		dialog.addVariable("preposition",PatternObject.TYPE_PREPOSITION);

		dialog.addVariable("nextDialog",PatternObject.TYPE_ALPHABETIC);
		dialog.addVariableExample("nextDialog","What can I do for you {fullName}?","{nextDialog}.");

		dialog = new Dialog("Handdruk",Language.NLD,HandshakeController.class.getName());
		dialogs.add(dialog);

		dialog.addExample("Hallo. Mijn naam is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		dialog.addExample("Hallo. Ik heet {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		dialog.addExample("Hallo, mijn naam is {firstName} {lastName}.","Hallo {fullName}.");
		dialog.addExample("Hallo, ik heet {firstName}.","Hallo {firstName}. Wat is je achternaam?");
		dialog.addExample("Hoi. Mijn naam is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		dialog.addExample("Hoi. Ik heet is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		dialog.addExample("Hoi, mijn naam is {firstName} {lastName}.","Hallo {fullName}.");
		dialog.addExample("Hoi, ik heet {firstName}.","Hallo {firstName}. Wat is je achternaam?");
		
		dialog.addExample("Hallo.","Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hallo.","Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hallo!","Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hoi.","Hoi. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hoi!","Hoi. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hoe heet jij?","Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Wat is jouw naam?","Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		
		dialog.addVariable("firstName",PatternObject.TYPE_ALPHABETIC);
		dialog.addVariableExample("firstName","Wat is jouw naam?","Mijn naam is {firstName} {preposition} {lastName}.");
		dialog.addVariableExample("firstName","Wat is jouw naam?","Mijn naam is {firstName} {lastName}.");
		dialog.addVariableExample("firstName","Wat is jouw naam?","Mijn naam is {firstName}.");
		dialog.addVariableExample("firstName","Wat is jouw naam?","{firstName} {preposition} {lastName}.");
		dialog.addVariableExample("firstName","Wat is jouw naam?","{firstName} {lastName}.");
		dialog.addVariableExample("firstName","Wat is jouw naam?","{firstName}.");

		dialog.addVariable("lastName",PatternObject.TYPE_ALPHABETIC);
		dialog.addVariableExample("lastName","Wat is jouw achternaam?","Mijn achternaam is {preposition} {lastName}.");
		dialog.addVariableExample("lastName","Wat is jouw achternaam?","Mijn achternaam is {lastName}, {preposition}.");
		dialog.addVariableExample("lastName","Wat is jouw achternaam?","Mijn achternaam is {lastName}.");
		dialog.addVariableExample("lastName","Wat is jouw achternaam?","{preposition} {lastName}.");
		dialog.addVariableExample("lastName","Wat is jouw achternaam?","{lastName}, {preposition}.");
		dialog.addVariableExample("lastName","Wat is jouw achternaam?","{lastName}.");

		dialog.addVariable("preposition",PatternObject.TYPE_PREPOSITION);

		dialog.addVariable("nextDialog",PatternObject.TYPE_ALPHABETIC);
		dialog.addVariableExample("nextDialog","Wat kan ik voor je doen {fullName}?","{nextDialog}.");
		
		return dialogs;
	}
}
