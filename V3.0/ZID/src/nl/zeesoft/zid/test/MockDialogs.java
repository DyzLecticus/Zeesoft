package nl.zeesoft.zid.test;

import java.util.ArrayList;
import java.util.List;

import nl.zeesoft.zdk.test.MockObject;
import nl.zeesoft.zid.dialog.Dialog;
import nl.zeesoft.zspr.pattern.PatternObject;

public class MockDialogs extends MockObject {
	@Override
	protected void describe() {
		System.out.println("This test uses the *MockDialogs*.");
	}

	@Override
	protected Object initialzeMock() {
		List<Dialog> dialogs = new ArrayList<Dialog>();
		
		Dialog dialog = new Dialog("EnglishHandshake");
		dialogs.add(dialog);
		
		dialog.addExample("Hello. My name is {firstName} {preposition} {lastName}.","Hello.");
		dialog.addExample("Hello. My name is {firstName} {lastName}.","Hello.");
		dialog.addExample("Hello. My name is {firstName}.","Hello.");
		dialog.addExample("Hi. My name is {firstName} {preposition} {lastName}.","Hello.");
		dialog.addExample("Hi. My name is {firstName} {lastName}.","Hello.");
		dialog.addExample("Hi. My name is {firstName}.","Hello.");
		
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

		dialog = new Dialog("DutchHandshake");
		dialogs.add(dialog);

		dialog.addExample("Hallo. Mijn naam is {firstName} {preposition} {lastName}.","Hallo.");
		dialog.addExample("Hallo. Mijn naam is {firstName} {lastName}.","Hallo.");
		dialog.addExample("Hallo. Mijn naam is {firstName}.","Hello.");
		dialog.addExample("Hoi. Mijn naam is {firstName} {preposition} {lastName}.","Hallo.");
		dialog.addExample("Hoi. Mijn naam is {firstName} {lastName}.","Hallo.");
		dialog.addExample("Hoi. Mijn naam is {firstName}.","Hallo.");
		
		dialog.addExample("Hallo.","Hello. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hallo.","Hello. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hallo!","Hello. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hoi.","Hi. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
		dialog.addExample("Hoi!","Hi. Mijn naam is Dyz Lecticus. Wat is jouw naam?");
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
		
		return dialogs;
	}
}
