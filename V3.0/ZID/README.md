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
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/zid-0.9.6.zip) to download the latest ZID release (version 0.9.6).  
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
Initializing pattern manager took 1693 ms

Initializing dialog handler took 820 ms

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

Average time spent thinking per response: 843 ms

2017-02-19 11:48:14:248: <<< Hello.
2017-02-19 11:48:14:253: --- Input language: ENG, input dialog: Handshake
2017-02-19 11:48:14:254: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:48:14:254: --- Translated input: Hello .
2017-02-19 11:48:14:254: --- Corrected input: Hello.
2017-02-19 11:48:15:725: --- Extended input to output: Hello. My name is Dyz Lecticus. What is your name?
2017-02-19 11:48:15:726: --- Translated output: Hello . My name is Dyz Lecticus . What is your name ?
2017-02-19 11:48:15:726: >>> Hello. My name is Dyz Lecticus. What is your name?
2017-02-19 11:48:15:727: <<< My name is andre van der zee.
2017-02-19 11:48:15:735: --- Input language: ENG, input dialog: Handshake
2017-02-19 11:48:15:735: --- Continuing dialog: Handshake
2017-02-19 11:48:15:737: --- Translated input: My name is ALPHABETIC_UNI:andre PREPOSITION_NLD:3 ALPHABETIC_UNI:zee .
2017-02-19 11:48:15:965: --- Corrected input: What is your name? My name is {firstName} {preposition} {lastName}.
2017-02-19 11:48:15:965: --- Updated variables: firstName = ALPHABETIC_UNI:andre, lastName = ALPHABETIC_UNI:zee, preposition = PREPOSITION_NLD:3
2017-02-19 11:48:15:966: --- Controller output: Nice to interact with you again {fullName}.
2017-02-19 11:48:15:966: --- Translated controller output: Nice to interact with you again Andre van der Zee.
2017-02-19 11:48:15:967: --- Translated output: Nice to interact with you again Andre van der Zee .
2017-02-19 11:48:15:967: >>> Nice to interact with you again Andre van der Zee.
2017-02-19 11:48:15:967: <<< Hallo , ik heet karel de grote.
2017-02-19 11:48:15:970: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:48:15:970: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:48:15:971: --- Translated input: Hallo , ik heet ALPHABETIC_UNI:karel PREPOSITION_NLD:5|ALPHABETIC_UNI:de ALPHABETIC_UNI:grote .
2017-02-19 11:48:16:076: --- Corrected input: Hallo, ik heet {firstName} {preposition} {lastName}.
2017-02-19 11:48:16:076: --- Updated variables: firstName = ALPHABETIC_UNI:karel, lastName = ALPHABETIC_UNI:grote, preposition = PREPOSITION_NLD:5
2017-02-19 11:48:16:076: --- Controller requests prompt for: nextDialog
2017-02-19 11:48:16:078: --- Translated controller output: Wat kan ik voor je doen Karel de Grote?
2017-02-19 11:48:16:078: --- Translated output: Wat kan ik voor je doen Karel de Grote ?
2017-02-19 11:48:16:079: >>> Wat kan ik voor je doen Karel de Grote?
2017-02-19 11:48:16:079: <<< Hallo?
2017-02-19 11:48:16:080: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:48:16:080: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:48:16:080: --- Translated input: Hallo ?
2017-02-19 11:48:16:080: --- Corrected input: Hallo?
2017-02-19 11:48:17:734: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:48:17:735: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-19 11:48:17:735: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:48:17:735: <<< Gekke henkie.
2017-02-19 11:48:17:738: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:48:17:739: --- Continuing dialog: Handdruk
2017-02-19 11:48:17:739: --- Translated input: ALPHABETIC_UNI:Gekke ALPHABETIC_UNI:henkie .
2017-02-19 11:48:17:853: --- Corrected input: Wat is jouw naam? {firstName} {lastName}.
2017-02-19 11:48:17:854: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke, lastName = ALPHABETIC_UNI:henkie
2017-02-19 11:48:17:854: --- Controller requests prompt for: nextDialog
2017-02-19 11:48:17:854: --- Translated controller output: Wat kan ik voor je doen Gekke Henkie?
2017-02-19 11:48:17:855: --- Translated output: Wat kan ik voor je doen Gekke Henkie ?
2017-02-19 11:48:17:855: >>> Wat kan ik voor je doen Gekke Henkie?
2017-02-19 11:48:17:856: <<< Gekke.
2017-02-19 11:48:17:860: --- Input language: 
2017-02-19 11:48:17:861: --- Unable to determine dialog
2017-02-19 11:48:17:885: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-19 11:48:18:015: --- Corrected input: Wat kan ik voor je doen Gekke Henkie? {firstName}.
2017-02-19 11:48:19:376: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:48:19:377: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-19 11:48:19:377: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:48:19:377: <<< Van henkie.
2017-02-19 11:48:19:381: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:48:19:381: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:48:19:382: --- Translated input: PREPOSITION_NLD:1|ALPHABETIC_UNI:Van ALPHABETIC_UNI:henkie .
2017-02-19 11:48:19:409: --- Corrected input: {preposition} {lastName}.
2017-02-19 11:48:19:409: --- Updated variables: lastName = ALPHABETIC_UNI:henkie, preposition = PREPOSITION_NLD:1
2017-02-19 11:48:19:410: --- Controller output: Wat is jouw voornaam?
2017-02-19 11:48:19:410: --- Translated controller output: Wat is jouw voornaam?
2017-02-19 11:48:19:410: --- Translated output: Wat is jouw voornaam ?
2017-02-19 11:48:19:410: >>> Wat is jouw voornaam?
2017-02-19 11:48:19:411: <<< Gekke.
2017-02-19 11:48:19:414: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:48:19:414: --- Continuing dialog: Handdruk
2017-02-19 11:48:19:415: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-19 11:48:19:470: --- Corrected input: Wat is jouw voornaam? {firstName}.
2017-02-19 11:48:19:470: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke
2017-02-19 11:48:19:470: --- Controller requests prompt for: nextDialog
2017-02-19 11:48:19:470: --- Translated controller output: Wat kan ik voor je doen Gekke van Henkie?
2017-02-19 11:48:19:471: --- Translated output: Wat kan ik voor je doen Gekke van Henkie ?
2017-02-19 11:48:19:471: >>> Wat kan ik voor je doen Gekke van Henkie?
2017-02-19 11:48:19:471: <<< What is your name?
2017-02-19 11:48:19:473: --- Input language: ENG, input dialog: Handshake
2017-02-19 11:48:19:473: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:48:19:473: --- Translated input: What is your name ?
2017-02-19 11:48:19:473: --- Corrected input: What is your name?
2017-02-19 11:48:21:326: --- Extended input to output: My name is Dyz Lecticus. What is your name?
2017-02-19 11:48:21:326: --- Translated output: My name is Dyz Lecticus . What is your name ?
2017-02-19 11:48:21:326: >>> My name is Dyz Lecticus. What is your name?
2017-02-19 11:48:21:327: <<< Hoe heet jij?
2017-02-19 11:48:21:328: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:48:21:328: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:48:21:329: --- Translated input: Hoe heet jij ?
2017-02-19 11:48:21:329: --- Corrected input: Hoe heet jij?
2017-02-19 11:48:22:682: --- Extended input to output: Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:48:22:682: --- Translated output: Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-19 11:48:22:682: >>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

~~~~

Test results
------------
All 2 tests have been executed successfully (17 assertions).  
Total test duration: 11286 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zid.test.TestDialog: 325 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestDialogHandler: 34556 Kb / 33 Mb
