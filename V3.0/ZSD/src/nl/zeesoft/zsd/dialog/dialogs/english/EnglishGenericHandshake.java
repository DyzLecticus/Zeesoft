package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;

public class EnglishGenericHandshake extends GenericHandshake {
	public EnglishGenericHandshake() {
		setLanguage(BaseConfiguration.LANG_ENG);
	}
	
	@Override
	public void initialize() {
		addExample("Hello.","Hello. My name is {selfName}. What is your name?");
		addExample("Hello!","Hello. My name is {selfName}. What is your name?");
		addExample("Hi.","Hi. My name is {selfName}. What is your name?");
		addExample("Hi!","Hi. My name is {selfName}. What is your name?");
		addExample("What is your name?","My name is {selfName}. What is your name?");
		addExample("Who are you?","My name is {selfName}. What is your name?");
		
		addExample("Hello. My name is {firstName} {preposition} {lastName}.","Hello {fullName}.");
		addExample("Hello, my name is {firstName} {lastName}.","Hello {fullName}.");
		addExample("Hello. My name is {firstName}.","Hello {firstName}. What is your lastname?");
		addExample("Hi. My name is {firstName} {preposition} {lastName}.","Hello {fullName}.");
		addExample("Hi, my name is {firstName} {lastName}.","Hello {fullName}.");
		addExample("Hi. My name is {firstName}.","Hello {firstName}. What is your lastname?");
		
		addVariable(VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_NAME,VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_ALPHABETIC);
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","My name is {firstName} {preposition} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","My name is {firstName} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","My name is {firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","{firstName} {preposition} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","{firstName} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","{firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your firstname?","{firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your firstname?","My firstname is {firstName}.");

		addVariable(VARIABLE_LASTNAME,BaseConfiguration.TYPE_NAME,VARIABLE_LASTNAME,BaseConfiguration.TYPE_ALPHABETIC);
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","My lastname is {preposition} {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","My lastname is {lastName}, {preposition}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","My lastname is {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","{preposition} {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","{lastName}, {preposition}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","{lastName}.");

		addVariable(VARIABLE_PREPOSITION,BaseConfiguration.TYPE_NAME,VARIABLE_PREPOSITION,BaseConfiguration.TYPE_PREPOSITION);

		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariableQA(VARIABLE_NEXT_DIALOG,"What can I do for you {fullName}?","{nextDialog}.");
	}
}
