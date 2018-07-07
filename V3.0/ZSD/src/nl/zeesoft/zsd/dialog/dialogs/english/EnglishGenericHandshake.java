package nl.zeesoft.zsd.dialog.dialogs.english;

import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.complex.ComplexObject;

public class EnglishGenericHandshake extends GenericHandshake {
	@Override
	public void initialize() {
		setLanguage(EntityObject.LANG_ENG);
		setMasterContext(MASTER_CONTEXT_GENERIC);
		setContext(CONTEXT_GENERIC_HANDSHAKE);
		
		addExample("Hello. My name is {firstName} {preposition} {lastName}.","Hello {fullName}.");
		addExample("Hello, my name is {firstName} {lastName}.","Hello {fullName}.");
		addExample("Hello. My name is {firstName}.","Hello {firstName}. What is your lastname?");
		addExample("Hi. My name is {firstName} {preposition} {lastName}.","Hello {fullName}.");
		addExample("Hi, my name is {firstName} {lastName}.","Hello {fullName}.");
		addExample("Hi. My name is {firstName}.","Hello {firstName}. What is your lastname?");
		
		addExample("Hello.","Hello. My name is " + getIdentity().name + ". What is your name?");
		addExample("Hello!","Hello. My name is " + getIdentity().name + ". What is your name?");
		addExample("Hi.","Hi. My name is " + getIdentity().name + ". What is your name?");
		addExample("Hi!","Hi. My name is " + getIdentity().name + ". What is your name?");
		addExample("What is your name?","My name is " + getIdentity().name + ". What is your name?");
		
		addVariable(VARIABLE_FIRSTNAME,ComplexObject.TYPE_NAME,VARIABLE_FIRSTNAME);
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","My name is {firstName} {preposition} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","My name is {firstName} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","My name is {firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","{firstName} {preposition} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","{firstName} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your name?","{firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your firstname?","{firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"What is your firstname?","My firstname is {firstName}.");

		addVariable(VARIABLE_LASTNAME,ComplexObject.TYPE_NAME,VARIABLE_LASTNAME);
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","My lastname is {preposition} {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","My lastname is {lastName}, {preposition}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","My lastname is {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","{preposition} {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","{lastName}, {preposition}.");
		addVariableQA(VARIABLE_LASTNAME,"What is your lastname?","{lastName}.");

		addVariable(VARIABLE_PREPOSITION,EntityObject.TYPE_PREPOSITION);

		addVariable(VARIABLE_NEXT_DIALOG,EntityObject.TYPE_ALPHABETIC);
		addVariableQA(VARIABLE_NEXT_DIALOG,"What can I do for you {fullName}?","{nextDialog}.");
	}
}
