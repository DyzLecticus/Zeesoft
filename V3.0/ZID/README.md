Zeesoft Intelligent Dialogs
==============================
Zeesoft Intelligent Dialogs (ZID) is an open source library for Java application development.
It provides support for defining and handling written dialogs while translating the input into parameterized program calls.

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft Symbolic Confabulation](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSC/)  
 * [Zeesoft Symbolic Pattern Recognition](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSPR/)  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/zid-0.9.7.zip) to download the latest ZID release (version 0.9.7).  
All ZID releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZID](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/ZID.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zid.test.TestDialog
------------------------------
This test shows how to create *Dialog* objects.  

**Example implementation**  
~~~~
// Create dialog
Dialog dialog = new Dialog("Handshake",Language.ENG,HandshakeController.class.getName());
// Add an example
dialog.addExample("Hello. My name is {firstName} {preposition} {lastName}.","Hello {fullName}.");
// Add a variable
dialog.addVariable("firstName",PatternObject.TYPE_ALPHABETIC);
// Add a variable example
dialog.addVariableExample("firstName","What is your name?","My name is {firstName} {preposition} {lastName}.");
~~~~

A *Dialog* requires a controller class name that refers to a class that extends the *DialogControllerObject*.  
This controller is instantiated by the *DialogHandler* when it detects that the input requires it.  
The dialog controller is then notified whenever the *DialogHandler* has updated dialog variable values.  
Dialog controller can update dialog handler variables which can then be accessed by other dialog controllers.  

This test uses the *MockDialogs*.
The dialogs created by this mock use the *HandshakeController*.

Class references;  
 * [TestDialog](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/TestDialog.java)
 * [MockDialogs](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/MockDialogs.java)
 * [Dialog](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/dialog/Dialog.java)
 * [DialogControllerObject](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/dialog/DialogControllerObject.java)
 * [HandshakeController](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/HandshakeController.java)

**Test output**  
The output of this test shows the dialog structure of the mock dialogs.  
~~~~
Dialog: Handshake, language: ENG, controller: nl.zeesoft.zid.test.HandshakeController
- Examples;
  - Hello. My name is {firstName} {preposition} {lastName}. Hello {fullName}.
  - Hello, my name is {firstName} {lastName}. Hello {fullName}.
  - Hello. My name is {firstName}. Hello {firstName}. What is your lastname?
  - Hi. My name is {firstName} {preposition} {lastName}. Hello {fullName}.
  - Hi, my name is {firstName} {lastName}. Hello {fullName}.
  - Hi. My name is {firstName}. Hello {firstName}. What is your lastname?
  - Hello. Hello. My name is Dyz Lecticus. What is your name?
  - Hello! Hello. My name is Dyz Lecticus. What is your name?
  - Hi. Hi. My name is Dyz Lecticus. What is your name?
  - Hi! Hi. My name is Dyz Lecticus. What is your name?
  - What is your name? My name is Dyz Lecticus. What is your name?
- Variables;
  - Variable: firstName, type: ALPHABETIC
    - Variable examples;
      - What is your name? My name is {firstName} {preposition} {lastName}.
      - What is your name? My name is {firstName} {lastName}.
      - What is your name? My name is {firstName}.
      - What is your name? {firstName} {preposition} {lastName}.
      - What is your name? {firstName} {lastName}.
      - What is your name? {firstName} {lastName}.
      - What is your name? {firstName} {lastName}.
      - What is your name? {firstName}.
      - What is your firstname? {firstName}.
      - What is your firstname? My firstname is {firstName}.
  - Variable: lastName, type: ALPHABETIC
    - Variable examples;
      - What is your lastname? My lastname is {preposition} {lastName}.
      - What is your lastname? My lastname is {lastName}, {preposition}.
      - What is your lastname? My lastname is {lastName}.
      - What is your lastname? {preposition} {lastName}.
      - What is your lastname? {lastName}, {preposition}.
      - What is your lastname? {lastName}.
  - Variable: preposition, type: PREPOSITION
  - Variable: nextDialog, type: ALPHABETIC
    - Variable examples;
      - What can I do for you {fullName}? {nextDialog}.
Dialog: Handdruk, language: NLD, controller: nl.zeesoft.zid.test.HandshakeController
- Examples;
  - Hallo. Mijn naam is {firstName} {preposition} {lastName}. Hallo {fullName}.
  - Hallo. Ik heet {firstName} {preposition} {lastName}. Hallo {fullName}.
  - Hallo, mijn naam is {firstName} {lastName}. Hallo {fullName}.
  - Hallo, ik heet {firstName}. Hallo {firstName}. Wat is je achternaam?
  - Hoi. Mijn naam is {firstName} {preposition} {lastName}. Hallo {fullName}.
  - Hoi. Ik heet is {firstName} {preposition} {lastName}. Hallo {fullName}.
  - Hoi, mijn naam is {firstName} {lastName}. Hallo {fullName}.
  - Hoi, ik heet {firstName}. Hallo {firstName}. Wat is je achternaam?
  - Hallo. Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
  - Hallo! Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
  - Hoi. Hoi. Mijn naam is Dyz Lecticus. Wat is jouw naam?
  - Hoi! Hoi. Mijn naam is Dyz Lecticus. Wat is jouw naam?
  - Hoe heet jij? Mijn naam is Dyz Lecticus. Wat is jouw naam?
  - Wat is jouw naam? Mijn naam is Dyz Lecticus. Wat is jouw naam?
- Variables;
  - Variable: firstName, type: ALPHABETIC
    - Variable examples;
      - Wat is jouw naam? Mijn naam is {firstName} {preposition} {lastName}.
      - Wat is jouw naam? Mijn naam is {firstName} {lastName}.
      - Wat is jouw naam? Mijn naam is {firstName}.
      - Wat is jouw naam? {firstName} {preposition} {lastName}.
      - Wat is jouw naam? {firstName} {lastName}.
      - Wat is jouw naam? {firstName} {lastName}.
      - Wat is jouw naam? {firstName} {lastName}.
      - Wat is jouw naam? {firstName}.
      - Wat is jouw voornaam? {firstName}.
      - Wat is jouw voornaam? Mijn voornaam is {firstName}.
  - Variable: lastName, type: ALPHABETIC
    - Variable examples;
      - Wat is jouw achternaam? Mijn achternaam is {preposition} {lastName}.
      - Wat is jouw achternaam? Mijn achternaam is {lastName}, {preposition}.
      - Wat is jouw achternaam? Mijn achternaam is {lastName}.
      - Wat is jouw achternaam? {preposition} {lastName}.
      - Wat is jouw achternaam? {lastName}, {preposition}.
      - Wat is jouw achternaam? {lastName}.
  - Variable: preposition, type: PREPOSITION
  - Variable: nextDialog, type: ALPHABETIC
    - Variable examples;
      - Wat kan ik voor je doen {fullName}? {nextDialog}.
~~~~

nl.zeesoft.zid.test.TestDialogHandler
-------------------------------------
This test shows how to create a *DialogHandler* and then use it to produce dialog output for input.  

**Example implementation**  
~~~~
// Create dialog handler
DialogHandler handler = new DialogHandler(dialogs,patternManager);
// Initialize dialog handler
handler.initialize();
// Handle dialog input
ZStringSymbolParser output = handler.handleInput(new ZStringSymbolParser("hello"));
~~~~

A *DialogHandler* requires a list of dialogs and a *PatternManager*.  

This test uses the *MockDialogHandler*.
The *MockDialogHandler* uses the *MockDialogs* and the *MockPatternManager*.

Class references;  
 * [TestDialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/TestDialogHandler.java)
 * [MockDialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/MockDialogHandler.java)
 * [MockDialogs](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/MockDialogs.java)
 * [DialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/dialog/DialogHandler.java)

**Test output**  
The output of this test shows;  
 * The mock initialization duration.  
 * The scripted dialog handler input with corresponding output.  
 * The average time spent thinking per response.  
 * The dialog handler log.  
~~~~
Initializing pattern manager took 1549 ms

Initializing dialog handler took 768 ms

<<< hello
>>> Hello. My name is Dyz Lecticus. What is your name?
<<< my name is andre van der zee
>>> Nice to interact with you again Andre van der Zee.
<<< hallo,ik heet karel de grote.
>>> Wat kan ik voor je doen Karel de Grote?
<<< hallo?
>>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
<<< gekke henkie
>>> Wat kan ik voor je doen Gekke Henkie?
<<< gekke
>>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
<<< van henkie
>>> Wat is jouw voornaam?
<<< gekke
>>> Wat kan ik voor je doen Gekke van Henkie?
<<< what is your name?
>>> My name is Dyz Lecticus. What is your name?
<<< hoe heet jij?
>>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

Average time spent thinking per response: 770 ms

2017-02-20 22:53:38:245: <<< Hello.
2017-02-20 22:53:38:248: --- Input language: ENG, input dialog: Handshake
2017-02-20 22:53:38:249: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-20 22:53:38:249: --- Translated input: Hello .
2017-02-20 22:53:38:250: --- Corrected input: Hello.
2017-02-20 22:53:39:651: --- Extended input to output: Hello. My name is Dyz Lecticus. What is your name?
2017-02-20 22:53:39:652: --- Translated output: Hello . My name is Dyz Lecticus . What is your name ?
2017-02-20 22:53:39:652: >>> Hello. My name is Dyz Lecticus. What is your name?
2017-02-20 22:53:39:653: <<< My name is andre van der zee.
2017-02-20 22:53:39:659: --- Input language: ENG, input dialog: Handshake
2017-02-20 22:53:39:659: --- Continuing dialog: Handshake
2017-02-20 22:53:39:660: --- Translated input: My name is ALPHABETIC_UNI:andre PREPOSITION_NLD:3 ALPHABETIC_UNI:zee .
2017-02-20 22:53:39:883: --- Corrected input: What is your name? My name is {firstName} {preposition} {lastName}.
2017-02-20 22:53:39:884: --- Updated variables: firstName = ALPHABETIC_UNI:andre, lastName = ALPHABETIC_UNI:zee, preposition = PREPOSITION_NLD:3
2017-02-20 22:53:39:885: --- Controller output: Nice to interact with you again {fullName}.
2017-02-20 22:53:39:885: --- Translated controller output: Nice to interact with you again Andre van der Zee.
2017-02-20 22:53:39:886: --- Translated output: Nice to interact with you again Andre van der Zee .
2017-02-20 22:53:39:886: >>> Nice to interact with you again Andre van der Zee.
2017-02-20 22:53:39:886: <<< Hallo , ik heet karel de grote.
2017-02-20 22:53:39:889: --- Input language: NLD, input dialog: Handdruk
2017-02-20 22:53:39:891: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-20 22:53:39:892: --- Translated input: Hallo , ik heet ALPHABETIC_UNI:karel PREPOSITION_NLD:5|ALPHABETIC_UNI:de ALPHABETIC_UNI:grote .
2017-02-20 22:53:39:994: --- Corrected input: Hallo, ik heet {firstName} {preposition} {lastName}.
2017-02-20 22:53:39:995: --- Updated variables: firstName = ALPHABETIC_UNI:karel, lastName = ALPHABETIC_UNI:grote, preposition = PREPOSITION_NLD:5
2017-02-20 22:53:39:995: --- Controller requests prompt for: nextDialog
2017-02-20 22:53:39:996: --- Translated controller output: Wat kan ik voor je doen Karel de Grote?
2017-02-20 22:53:39:996: --- Translated output: Wat kan ik voor je doen Karel de Grote ?
2017-02-20 22:53:39:997: >>> Wat kan ik voor je doen Karel de Grote?
2017-02-20 22:53:39:997: <<< Hallo?
2017-02-20 22:53:39:998: --- Input language: NLD, input dialog: Handdruk
2017-02-20 22:53:39:998: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-20 22:53:39:998: --- Translated input: Hallo ?
2017-02-20 22:53:39:998: --- Corrected input: Hallo?
2017-02-20 22:53:41:528: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-20 22:53:41:529: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-20 22:53:41:529: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-20 22:53:41:529: <<< Gekke henkie.
2017-02-20 22:53:41:532: --- Input language: NLD, input dialog: Handdruk
2017-02-20 22:53:41:532: --- Continuing dialog: Handdruk
2017-02-20 22:53:41:533: --- Translated input: ALPHABETIC_UNI:Gekke ALPHABETIC_UNI:henkie .
2017-02-20 22:53:41:625: --- Corrected input: Wat is jouw naam? {firstName} {lastName}.
2017-02-20 22:53:41:625: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke, lastName = ALPHABETIC_UNI:henkie
2017-02-20 22:53:41:625: --- Controller requests prompt for: nextDialog
2017-02-20 22:53:41:626: --- Translated controller output: Wat kan ik voor je doen Gekke Henkie?
2017-02-20 22:53:41:626: --- Translated output: Wat kan ik voor je doen Gekke Henkie ?
2017-02-20 22:53:41:626: >>> Wat kan ik voor je doen Gekke Henkie?
2017-02-20 22:53:41:626: <<< Gekke.
2017-02-20 22:53:41:631: --- Input language: 
2017-02-20 22:53:41:631: --- Unable to determine dialog
2017-02-20 22:53:41:653: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-20 22:53:41:785: --- Corrected input: Wat kan ik voor je doen Gekke Henkie? {firstName}.
2017-02-20 22:53:43:104: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-20 22:53:43:104: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-20 22:53:43:105: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-20 22:53:43:105: <<< Van henkie.
2017-02-20 22:53:43:111: --- Input language: NLD, input dialog: Handdruk
2017-02-20 22:53:43:111: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-20 22:53:43:112: --- Translated input: PREPOSITION_NLD:1|ALPHABETIC_UNI:Van ALPHABETIC_UNI:henkie .
2017-02-20 22:53:43:135: --- Corrected input: {preposition} {lastName}.
2017-02-20 22:53:43:135: --- Updated variables: lastName = ALPHABETIC_UNI:henkie, preposition = PREPOSITION_NLD:1
2017-02-20 22:53:43:135: --- Controller output: Wat is jouw voornaam?
2017-02-20 22:53:43:136: --- Translated controller output: Wat is jouw voornaam?
2017-02-20 22:53:43:136: --- Translated output: Wat is jouw voornaam ?
2017-02-20 22:53:43:136: >>> Wat is jouw voornaam?
2017-02-20 22:53:43:137: <<< Gekke.
2017-02-20 22:53:43:139: --- Input language: NLD, input dialog: Handdruk
2017-02-20 22:53:43:139: --- Continuing dialog: Handdruk
2017-02-20 22:53:43:140: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-20 22:53:43:190: --- Corrected input: Wat is jouw voornaam? {firstName}.
2017-02-20 22:53:43:190: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke
2017-02-20 22:53:43:190: --- Controller requests prompt for: nextDialog
2017-02-20 22:53:43:190: --- Translated controller output: Wat kan ik voor je doen Gekke van Henkie?
2017-02-20 22:53:43:190: --- Translated output: Wat kan ik voor je doen Gekke van Henkie ?
2017-02-20 22:53:43:191: >>> Wat kan ik voor je doen Gekke van Henkie?
2017-02-20 22:53:43:191: <<< What is your name?
2017-02-20 22:53:43:193: --- Input language: ENG, input dialog: Handshake
2017-02-20 22:53:43:193: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-20 22:53:43:193: --- Translated input: What is your name ?
2017-02-20 22:53:43:193: --- Corrected input: What is your name?
2017-02-20 22:53:44:625: --- Extended input to output: My name is Dyz Lecticus. What is your name?
2017-02-20 22:53:44:625: --- Translated output: My name is Dyz Lecticus . What is your name ?
2017-02-20 22:53:44:626: >>> My name is Dyz Lecticus. What is your name?
2017-02-20 22:53:44:626: <<< Hoe heet jij?
2017-02-20 22:53:44:627: --- Input language: NLD, input dialog: Handdruk
2017-02-20 22:53:44:627: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-20 22:53:44:628: --- Translated input: Hoe heet jij ?
2017-02-20 22:53:44:628: --- Corrected input: Hoe heet jij?
2017-02-20 22:53:45:945: --- Extended input to output: Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-20 22:53:45:945: --- Translated output: Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-20 22:53:45:945: >>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

~~~~

Test results
------------
All 2 tests have been executed successfully (17 assertions).  
Total test duration: 10288 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zid.test.TestDialog: 325 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestDialogHandler: 34556 Kb / 33 Mb
