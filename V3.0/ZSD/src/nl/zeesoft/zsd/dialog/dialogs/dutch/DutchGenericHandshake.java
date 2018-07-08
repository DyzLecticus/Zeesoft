package nl.zeesoft.zsd.dialog.dialogs.dutch;

import nl.zeesoft.zsd.BaseConfiguration;
import nl.zeesoft.zsd.dialog.dialogs.GenericHandshake;

public class DutchGenericHandshake extends GenericHandshake {
	public DutchGenericHandshake() {
		setLanguage(BaseConfiguration.LANG_NLD);
	}
	
	@Override
	public void initialize() {
		addExample("Hallo. Mijn naam is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		addExample("Hallo. Ik heet {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		addExample("Hallo, mijn naam is {firstName} {lastName}.","Hallo {fullName}.");
		addExample("Hallo, ik heet {firstName}.","Hallo {firstName}. Wat is je achternaam?");
		addExample("Hoi. Mijn naam is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		addExample("Hoi. Ik heet is {firstName} {preposition} {lastName}.","Hallo {fullName}.");
		addExample("Hoi, mijn naam is {firstName} {lastName}.","Hallo {fullName}.");
		addExample("Hoi, ik heet {firstName}.","Hallo {firstName}. Wat is je achternaam?");
		
		addExample("Hallo.","Hallo. Mijn naam is {self.name}. Wat is jouw naam?");
		addExample("Hallo!","Hallo. Mijn naam is {self.name}. Wat is jouw naam?");
		addExample("Hoi.","Hoi. Mijn naam is {self.name}. Wat is jouw naam?");
		addExample("Hoi!","Hoi. Mijn naam is {self.name}. Wat is jouw naam?");
		addExample("Hoe heet jij?","Mijn naam is {self.name}. Wat is jouw naam?");
		addExample("Wat is jouw naam?","Mijn naam is {self.name}. Wat is jouw naam?");
		
		addVariable(VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_NAME,VARIABLE_FIRSTNAME,BaseConfiguration.TYPE_ALPHABETIC);
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

		addVariable(VARIABLE_LASTNAME,BaseConfiguration.TYPE_NAME,VARIABLE_LASTNAME,BaseConfiguration.TYPE_ALPHABETIC);
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","Mijn achternaam is {preposition} {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","Mijn achternaam is {lastName}, {preposition}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","Mijn achternaam is {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","{preposition} {lastName}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","{lastName}, {preposition}.");
		addVariableQA(VARIABLE_LASTNAME,"Wat is jouw achternaam?","{lastName}.");

		addVariable(VARIABLE_PREPOSITION,BaseConfiguration.TYPE_NAME,VARIABLE_PREPOSITION,BaseConfiguration.TYPE_PREPOSITION);

		addVariable(VARIABLE_NEXT_DIALOG,BaseConfiguration.TYPE_ALPHABETIC);
		addVariableQA(VARIABLE_NEXT_DIALOG,"Wat kan ik voor je doen {fullName}?","{nextDialog}.");
	}
}
