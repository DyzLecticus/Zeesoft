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
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/zid-0.9.5.zip) to download the latest ZID release (version 0.9.5).  
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
The output of this test shows the mock initialization duration, the dialog handler log and the average time spent thinking per response.  
~~~~
Initializing pattern manager took 1609 ms

Initializing dialog handler took 835 ms

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
Average time spent thinking per response: 819 ms
2017-02-19 11:23:58:079: <<< Hello.
2017-02-19 11:23:58:082: --- Input language: ENG, input dialog: Handshake
2017-02-19 11:23:58:083: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:23:58:083: --- Translated input: Hello .
2017-02-19 11:23:58:084: --- Corrected input: Hello.
2017-02-19 11:23:59:601: --- Extended input to output: Hello. My name is Dyz Lecticus. What is your name?
2017-02-19 11:23:59:601: --- Translated output: Hello . My name is Dyz Lecticus . What is your name ?
2017-02-19 11:23:59:602: >>> Hello. My name is Dyz Lecticus. What is your name?
2017-02-19 11:23:59:602: <<< My name is andre van der zee.
2017-02-19 11:23:59:609: --- Input language: ENG, input dialog: Handshake
2017-02-19 11:23:59:609: --- Continuing dialog: Handshake
2017-02-19 11:23:59:610: --- Translated input: My name is ALPHABETIC_UNI:andre PREPOSITION_NLD:3 ALPHABETIC_UNI:zee .
2017-02-19 11:23:59:842: --- Corrected input: What is your name? My name is {firstName} {preposition} {lastName}.
2017-02-19 11:23:59:842: --- Updated variables: firstName = ALPHABETIC_UNI:andre, lastName = ALPHABETIC_UNI:zee, preposition = PREPOSITION_NLD:3
2017-02-19 11:23:59:843: --- Controller output: Nice to interact with you again {fullName}.
2017-02-19 11:23:59:844: --- Translated controller output: Nice to interact with you again Andre van der Zee.
2017-02-19 11:23:59:844: --- Translated output: Nice to interact with you again Andre van der Zee .
2017-02-19 11:23:59:845: >>> Nice to interact with you again Andre van der Zee.
2017-02-19 11:23:59:845: <<< Hallo , ik heet karel de grote.
2017-02-19 11:23:59:848: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:23:59:849: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:23:59:849: --- Translated input: Hallo , ik heet ALPHABETIC_UNI:karel PREPOSITION_NLD:5|ALPHABETIC_UNI:de ALPHABETIC_UNI:grote .
2017-02-19 11:23:59:964: --- Corrected input: Hallo, ik heet {firstName} {preposition} {lastName}.
2017-02-19 11:23:59:965: --- Updated variables: firstName = ALPHABETIC_UNI:karel, lastName = ALPHABETIC_UNI:grote, preposition = PREPOSITION_NLD:5
2017-02-19 11:23:59:965: --- Controller requests prompt for: nextDialog
2017-02-19 11:23:59:967: --- Translated controller output: Wat kan ik voor je doen Karel de Grote?
2017-02-19 11:23:59:967: --- Translated output: Wat kan ik voor je doen Karel de Grote ?
2017-02-19 11:23:59:967: >>> Wat kan ik voor je doen Karel de Grote?
2017-02-19 11:23:59:967: <<< Hallo?
2017-02-19 11:23:59:968: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:23:59:968: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:23:59:968: --- Translated input: Hallo ?
2017-02-19 11:23:59:969: --- Corrected input: Hallo?
2017-02-19 11:24:01:630: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:24:01:630: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-19 11:24:01:631: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:24:01:632: <<< Gekke henkie.
2017-02-19 11:24:01:637: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:24:01:638: --- Continuing dialog: Handdruk
2017-02-19 11:24:01:638: --- Translated input: ALPHABETIC_UNI:Gekke ALPHABETIC_UNI:henkie .
2017-02-19 11:24:01:746: --- Corrected input: Wat is jouw naam? {firstName} {lastName}.
2017-02-19 11:24:01:747: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke, lastName = ALPHABETIC_UNI:henkie
2017-02-19 11:24:01:747: --- Controller requests prompt for: nextDialog
2017-02-19 11:24:01:747: --- Translated controller output: Wat kan ik voor je doen Gekke Henkie?
2017-02-19 11:24:01:748: --- Translated output: Wat kan ik voor je doen Gekke Henkie ?
2017-02-19 11:24:01:748: >>> Wat kan ik voor je doen Gekke Henkie?
2017-02-19 11:24:01:749: <<< Gekke.
2017-02-19 11:24:01:754: --- Input language: 
2017-02-19 11:24:01:754: --- Unable to determine dialog
2017-02-19 11:24:01:776: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-19 11:24:01:913: --- Corrected input: Wat kan ik voor je doen Gekke Henkie? {firstName}.
2017-02-19 11:24:03:301: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:24:03:302: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-19 11:24:03:302: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:24:03:303: <<< Van henkie.
2017-02-19 11:24:03:308: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:24:03:309: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:24:03:309: --- Translated input: PREPOSITION_NLD:1|ALPHABETIC_UNI:Van ALPHABETIC_UNI:henkie .
2017-02-19 11:24:03:332: --- Corrected input: {preposition} {lastName}.
2017-02-19 11:24:03:332: --- Updated variables: lastName = ALPHABETIC_UNI:henkie, preposition = PREPOSITION_NLD:1
2017-02-19 11:24:03:332: --- Controller output: Wat is jouw voornaam?
2017-02-19 11:24:03:333: --- Translated controller output: Wat is jouw voornaam?
2017-02-19 11:24:03:333: --- Translated output: Wat is jouw voornaam ?
2017-02-19 11:24:03:333: >>> Wat is jouw voornaam?
2017-02-19 11:24:03:335: <<< Gekke.
2017-02-19 11:24:03:337: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:24:03:337: --- Continuing dialog: Handdruk
2017-02-19 11:24:03:337: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-19 11:24:03:389: --- Corrected input: Wat is jouw voornaam? {firstName}.
2017-02-19 11:24:03:389: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke
2017-02-19 11:24:03:389: --- Controller requests prompt for: nextDialog
2017-02-19 11:24:03:389: --- Translated controller output: Wat kan ik voor je doen Gekke van Henkie?
2017-02-19 11:24:03:390: --- Translated output: Wat kan ik voor je doen Gekke van Henkie ?
2017-02-19 11:24:03:390: >>> Wat kan ik voor je doen Gekke van Henkie?
2017-02-19 11:24:03:390: <<< What is your name?
2017-02-19 11:24:03:392: --- Input language: ENG, input dialog: Handshake
2017-02-19 11:24:03:392: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:24:03:392: --- Translated input: What is your name ?
2017-02-19 11:24:03:393: --- Corrected input: What is your name?
2017-02-19 11:24:04:909: --- Extended input to output: My name is Dyz Lecticus. What is your name?
2017-02-19 11:24:04:909: --- Translated output: My name is Dyz Lecticus . What is your name ?
2017-02-19 11:24:04:910: >>> My name is Dyz Lecticus. What is your name?
2017-02-19 11:24:04:910: <<< Hoe heet jij?
2017-02-19 11:24:04:911: --- Input language: NLD, input dialog: Handdruk
2017-02-19 11:24:04:911: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-19 11:24:04:912: --- Translated input: Hoe heet jij ?
2017-02-19 11:24:04:912: --- Corrected input: Hoe heet jij?
2017-02-19 11:24:06:272: --- Extended input to output: Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-19 11:24:06:272: --- Translated output: Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-19 11:24:06:272: >>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

~~~~

Test results
------------
All 2 tests have been executed successfully (17 assertions).  
Total test duration: 10871 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zid.test.TestDialog: 325 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestDialogHandler: 34555 Kb / 33 Mb
