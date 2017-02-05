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
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/zid-0.9.0.zip) to download the latest ZID release (version 0.9.0).  
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
    - What is your name? {firstName}.
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
    - Wat is jouw naam? {firstName}.
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
The output of this test shows the mock initialization duration, the dialog handler log and the average time spent thinking per response.  
~~~~
Initializing pattern manager took 1095 ms

Initializing dialog handler took 465 ms

2017-02-05 17:35:56:259: <<< Hello.
2017-02-05 17:35:56:261: --- Input language: ENG, input dialog: Handshake
2017-02-05 17:35:56:262: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-05 17:35:56:262: --- Translated input: Hello .
2017-02-05 17:35:56:262: --- Corrected input: Hello.
2017-02-05 17:35:57:191: --- Extended input to output: Hello. My name is Dyz Lecticus. What is your name?
2017-02-05 17:35:57:191: --- Translated output: Hello . My name is Dyz Lecticus . What is your name ?
2017-02-05 17:35:57:191: >>> Hello. My name is Dyz Lecticus. What is your name?
2017-02-05 17:35:57:191: <<< My name is andre van der zee.
2017-02-05 17:35:57:194: --- Input language: ENG, input dialog: Handshake
2017-02-05 17:35:57:194: --- Continuing dialog: Handshake
2017-02-05 17:35:57:195: --- Translated input: My name is ALPHABETIC_UNI:andre PREPOSITION_NLD:3 ALPHABETIC_UNI:zee .
2017-02-05 17:35:57:198: --- Corrected input: What is your name? My name is {firstName} {preposition} {lastName}.
2017-02-05 17:35:57:199: --- Updated variables: firstName = ALPHABETIC_UNI:andre, lastName = ALPHABETIC_UNI:zee, preposition = PREPOSITION_NLD:3, nextDialog = ALPHABETIC_UNI:andre
2017-02-05 17:35:57:199: --- Controller output: Nice to interact with you again {fullName}.
2017-02-05 17:35:57:199: --- Translated controller output: Nice to interact with you again Andre van der Zee.
2017-02-05 17:35:57:199: --- Translated output: Nice to interact with you again Andre van der Zee .
2017-02-05 17:35:57:199: >>> Nice to interact with you again Andre van der Zee.
2017-02-05 17:35:57:199: <<< Hallo , ik heet karel de grote.
2017-02-05 17:35:57:201: --- Input language: NLD, input dialog: Handdruk
2017-02-05 17:35:57:201: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-05 17:35:57:201: --- Translated input: Hallo , ik heet ALPHABETIC_UNI:karel PREPOSITION_NLD:5|ALPHABETIC_UNI:de ALPHABETIC_UNI:grote .
2017-02-05 17:35:57:209: --- Corrected input: Nice to interact. You naam is {firstName} {preposition} {lastName}. Hallo, ik heet {firstName} {preposition} {lastName}.
2017-02-05 17:35:57:210: --- Updated variables: firstName = ALPHABETIC_UNI:karel, preposition = PREPOSITION_NLD:5, lastName = ALPHABETIC_UNI:grote
2017-02-05 17:35:57:210: --- Controller requests prompt for: nextDialog
2017-02-05 17:35:57:211: --- Translated controller output: Wat kan ik voor je doen Karel de Grote?
2017-02-05 17:35:57:211: --- Translated output: Wat kan ik voor je doen Karel de Grote ?
2017-02-05 17:35:57:211: >>> Wat kan ik voor je doen Karel de Grote?
2017-02-05 17:35:57:211: <<< Hallo?
2017-02-05 17:35:57:211: --- Input language: NLD, input dialog: Handdruk
2017-02-05 17:35:57:211: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-05 17:35:57:212: --- Translated input: Hallo ?
2017-02-05 17:35:57:213: --- Corrected input: Wat kan ik voor je doen {fullName}? {nextDialog}? Hallo?
2017-02-05 17:35:58:563: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-05 17:35:58:563: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-05 17:35:58:563: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-05 17:35:58:564: <<< Gekke henkie.
2017-02-05 17:35:58:570: --- Input language: NLD, input dialog: Handdruk
2017-02-05 17:35:58:570: --- Continuing dialog: Handdruk
2017-02-05 17:35:58:570: --- Translated input: ALPHABETIC_UNI:Gekke ALPHABETIC_UNI:henkie .
2017-02-05 17:35:58:573: --- Corrected input: Wat is jouw naam? Mijn naam.
2017-02-05 17:35:58:573: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke, lastName = ALPHABETIC_UNI:henkie
2017-02-05 17:35:58:573: --- Controller requests prompt for: nextDialog
2017-02-05 17:35:58:574: --- Translated controller output: Wat kan ik voor je doen Gekke Henkie?
2017-02-05 17:35:58:574: --- Translated output: Wat kan ik voor je doen Gekke Henkie ?
2017-02-05 17:35:58:574: >>> Wat kan ik voor je doen Gekke Henkie?
2017-02-05 17:35:58:575: <<< Gekke.
2017-02-05 17:35:58:580: --- Input language: 
2017-02-05 17:35:58:580: --- Unable to determine dialog
2017-02-05 17:35:58:603: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-05 17:35:58:607: --- Corrected input: Wat kan ik voor je doen {fullName}?? {lastName}.
2017-02-05 17:36:00:670: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-05 17:36:00:670: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-05 17:36:00:670: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-05 17:36:00:670: <<< Van henkie.
2017-02-05 17:36:00:674: --- Input language: NLD, input dialog: Handdruk
2017-02-05 17:36:00:674: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-05 17:36:00:675: --- Translated input: PREPOSITION_NLD:1|ALPHABETIC_UNI:Van ALPHABETIC_UNI:henkie .
2017-02-05 17:36:00:677: --- Corrected input: Wat is jouw naam? Mijn naam.
2017-02-05 17:36:00:677: --- Updated variables: preposition = PREPOSITION_NLD:1, firstName = ALPHABETIC_UNI:henkie
2017-02-05 17:36:00:678: --- Controller requests prompt for: lastName
2017-02-05 17:36:00:678: --- Translated controller output: Wat is jouw achternaam?
2017-02-05 17:36:00:678: --- Translated output: Wat is jouw achternaam ?
2017-02-05 17:36:00:678: >>> Wat is jouw achternaam?
2017-02-05 17:36:00:679: <<< What is your name?
2017-02-05 17:36:00:680: --- Input language: ENG, input dialog: Handshake
2017-02-05 17:36:00:680: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-05 17:36:00:681: --- Translated input: What is your name ?
2017-02-05 17:36:00:681: --- Corrected input: Wat is jouw achternaam? What is your name?
2017-02-05 17:36:02:269: --- Extended input to output: My name is Dyz Lecticus. What is your name?
2017-02-05 17:36:02:269: --- Translated output: My name is Dyz Lecticus . What is your name ?
2017-02-05 17:36:02:269: >>> My name is Dyz Lecticus. What is your name?
2017-02-05 17:36:02:269: <<< Hoe heet jij?
2017-02-05 17:36:02:270: --- Input language: NLD, input dialog: Handdruk
2017-02-05 17:36:02:271: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-05 17:36:02:271: --- Translated input: Hoe heet jij ?
2017-02-05 17:36:02:271: --- Corrected input: What is your name? Hoe heet jij?
2017-02-05 17:36:04:490: --- Extended input to output: Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-05 17:36:04:490: --- Translated output: Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-05 17:36:04:491: >>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

Average time spent thinking per response: 824 ms
~~~~

Test results
------------
All 2 tests have been executed successfully (16 assertions).  
Total test duration: 10072 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zid.test.TestDialog: 318 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestDialogHandler: 34567 Kb / 33 Mb
