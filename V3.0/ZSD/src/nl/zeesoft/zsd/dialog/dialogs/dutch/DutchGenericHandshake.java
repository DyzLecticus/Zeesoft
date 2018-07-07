package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;
import nl.zeesoft.zsd.entity.EntityObject;
import nl.zeesoft.zsd.entity.complex.ComplexObject;

public class DutchGenericHandshake extends GenericHandshake {
	@Override
	public void initialize() {
		setLanguage(EntityObject.LANG_NLD);
		setMasterContext(MASTER_CONTEXT_GENERIC);
		setContext(CONTEXT_GENERIC_HANDSHAKE);
		
		addExample("Hallo. Mijn naam is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		addExample("Hallo. Ik heet {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		addExample("Hallo, mijn naam is {firstName} {lastName}.","Hallo {fullName}.");
		addExample("Hallo, ik heet {firstName}.","Hallo {firstName}. Wat is je achternaam?");
		addExample("Hoi. Mijn naam is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		addExample("Hoi. Ik heet is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		addExample("Hoi, mijn naam is {firstName} {lastName}.","Hallo {fullName}.");
		addExample("Hoi, ik heet {firstName}.","Hallo {firstName}. Wat is je achternaam?");
		
		addExample("Hallo.","Hallo. Mijn naam is " + getIdentity().name + ". Wat is jouw naam?");
		addExample("Hallo!","Hallo. Mijn naam is " + getIdentity().name + ". Wat is jouw naam?");
		addExample("Hoi.","Hoi. Mijn naam is " + getIdentity().name + ". Wat is jouw naam?");
		addExample("Hoi!","Hoi. Mijn naam is " + getIdentity().name + ". Wat is jouw naam?");
		addExample("Hoe heet jij?","Mijn naam is " + getIdentity().name + ". Wat is jouw naam?");
		addExample("Wat is jouw naam?","Mijn naam is " + getIdentity().name + ". Wat is jouw naam?");
		
		addVariable(VARIABLE_FIRSTNAME,ComplexObject.TYPE_NAME,VARIABLE_FIRSTNAME);
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw naam?","Mijn naam is {firstName} {preposition} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw naam?","Mijn naam is {firstName} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw naam?","Mijn naam is {firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw naam?","{firstName} {preposition} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw naam?","{firstName} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw naam?","{firstName} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw naam?","{firstName} {lastName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw naam?","{firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw voornaam?","{firstName}.");
		addVariableQA(VARIABLE_FIRSTNAME,"Wat is jouw voornaam?","Mijn voornaam is {firstName}.");

		addVariable(VARIABLE_LASTNAME,ComplexObject.TYPE_NAME,VARIABLE_LASTNAME);
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","Mijn achternaam is {preposition} {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","Mijn achternaam is {lastName}, {preposition}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","Mijn achternaam is {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","{preposition} {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","{lastName}, {preposition}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","{lastName}.");

		addVariable(VARIABLE_PREPOSITION,EntityObject.TYPE_PREPOSITION);

		addVariable(VARIABLE_NEXT_DIALOG,EntityObject.TYPE_ALPHABETIC);
		addVariableQA(VARIABLE_NEXT_DIALOG,"Wat kan ik voor je doen {fullName}?","{nextDialog}.");
	}
}