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
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/zid-0.9.2.zip) to download the latest ZID release (version 0.9.2).  
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
Initializing pattern manager took 1641 ms

Initializing dialog handler took 830 ms

2017-02-11 15:32:14:443: <<< Hello.
2017-02-11 15:32:14:447: --- Input language: ENG, input dialog: Handshake
2017-02-11 15:32:14:448: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-11 15:32:14:448: --- Translated input: Hello .
2017-02-11 15:32:14:449: --- Corrected input: Hello.
2017-02-11 15:32:16:124: --- Extended input to output: Hello. My name is Dyz Lecticus. What is your name?
2017-02-11 15:32:16:124: --- Translated output: Hello . My name is Dyz Lecticus . What is your name ?
2017-02-11 15:32:16:124: >>> Hello. My name is Dyz Lecticus. What is your name?
2017-02-11 15:32:16:125: <<< My name is andre van der zee.
2017-02-11 15:32:16:130: --- Input language: ENG, input dialog: Handshake
2017-02-11 15:32:16:130: --- Continuing dialog: Handshake
2017-02-11 15:32:16:131: --- Translated input: My name is ALPHABETIC_UNI:andre PREPOSITION_NLD:3 ALPHABETIC_UNI:zee .
2017-02-11 15:32:16:279: --- Corrected input: What is your name? My name is {firstName} {preposition} {lastName}.
2017-02-11 15:32:16:280: --- Updated variables: firstName = ALPHABETIC_UNI:andre, lastName = ALPHABETIC_UNI:zee, preposition = PREPOSITION_NLD:3
2017-02-11 15:32:16:280: --- Controller output: Nice to interact with you again {fullName}.
2017-02-11 15:32:16:281: --- Translated controller output: Nice to interact with you again Andre van der Zee.
2017-02-11 15:32:16:281: --- Translated output: Nice to interact with you again Andre van der Zee .
2017-02-11 15:32:16:282: >>> Nice to interact with you again Andre van der Zee.
2017-02-11 15:32:16:282: <<< Hallo , ik heet karel de grote.
2017-02-11 15:32:16:285: --- Input language: NLD, input dialog: Handdruk
2017-02-11 15:32:16:285: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-11 15:32:16:286: --- Translated input: Hallo , ik heet ALPHABETIC_UNI:karel PREPOSITION_NLD:5|ALPHABETIC_UNI:de ALPHABETIC_UNI:grote .
2017-02-11 15:32:17:042: --- Corrected input: Nice to interact with you again Andre van der Zee. Hallo, ik heet {firstName} {preposition} {lastName}.
2017-02-11 15:32:17:043: --- Updated variables: firstName = ALPHABETIC_UNI:karel, lastName = ALPHABETIC_UNI:grote, preposition = PREPOSITION_NLD:5
2017-02-11 15:32:17:044: --- Controller requests prompt for: nextDialog
2017-02-11 15:32:17:046: --- Translated controller output: Wat kan ik voor je doen Karel de Grote?
2017-02-11 15:32:17:046: --- Translated output: Wat kan ik voor je doen Karel de Grote ?
2017-02-11 15:32:17:046: >>> Wat kan ik voor je doen Karel de Grote?
2017-02-11 15:32:17:047: <<< Hallo?
2017-02-11 15:32:17:047: --- Input language: NLD, input dialog: Handdruk
2017-02-11 15:32:17:048: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-11 15:32:17:048: --- Translated input: Hallo ?
2017-02-11 15:32:17:263: --- Corrected input: Wat kan ik voor je doen Karel de Grote? Hallo?
2017-02-11 15:32:18:961: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-11 15:32:18:961: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-11 15:32:18:961: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-11 15:32:18:961: <<< Gekke henkie.
2017-02-11 15:32:18:965: --- Input language: NLD, input dialog: Handdruk
2017-02-11 15:32:18:965: --- Continuing dialog: Handdruk
2017-02-11 15:32:18:965: --- Translated input: ALPHABETIC_UNI:Gekke ALPHABETIC_UNI:henkie .
2017-02-11 15:32:19:032: --- Corrected input: Wat is jouw naam? {firstName} {lastName}.
2017-02-11 15:32:19:033: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke, lastName = ALPHABETIC_UNI:henkie
2017-02-11 15:32:19:033: --- Controller requests prompt for: nextDialog
2017-02-11 15:32:19:033: --- Translated controller output: Wat kan ik voor je doen Gekke Henkie?
2017-02-11 15:32:19:034: --- Translated output: Wat kan ik voor je doen Gekke Henkie ?
2017-02-11 15:32:19:034: >>> Wat kan ik voor je doen Gekke Henkie?
2017-02-11 15:32:19:034: <<< Gekke.
2017-02-11 15:32:19:039: --- Input language: 
2017-02-11 15:32:19:040: --- Unable to determine dialog
2017-02-11 15:32:19:061: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-11 15:32:19:179: --- Corrected input: Wat kan ik voor je doen Gekke Henkie? {firstName}.
2017-02-11 15:32:20:584: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-11 15:32:20:584: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-11 15:32:20:584: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-11 15:32:20:584: <<< Van henkie.
2017-02-11 15:32:20:588: --- Input language: NLD, input dialog: Handdruk
2017-02-11 15:32:20:588: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-11 15:32:20:588: --- Translated input: PREPOSITION_NLD:1|ALPHABETIC_UNI:Van ALPHABETIC_UNI:henkie .
2017-02-11 15:32:20:665: --- Corrected input: Wat is jouw naam? {firstName} {lastName}.
2017-02-11 15:32:20:665: --- Updated variables: firstName = ALPHABETIC_UNI:Van, lastName = ALPHABETIC_UNI:henkie, preposition = PREPOSITION_NLD:1
2017-02-11 15:32:20:666: --- Controller output: Wat is jouw voornaam?
2017-02-11 15:32:20:666: --- Translated controller output: Wat is jouw voornaam?
2017-02-11 15:32:20:666: --- Translated output: Wat is jouw voornaam ?
2017-02-11 15:32:20:666: >>> Wat is jouw voornaam?
2017-02-11 15:32:20:666: <<< Gekke.
2017-02-11 15:32:20:669: --- Input language: NLD, input dialog: Handdruk
2017-02-11 15:32:20:669: --- Continuing dialog: Handdruk
2017-02-11 15:32:20:669: --- Translated input: ALPHABETIC_UNI:Gekke .
2017-02-11 15:32:20:699: --- Corrected input: Wat is jouw voornaam? {firstName}.
2017-02-11 15:32:20:699: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke
2017-02-11 15:32:20:700: --- Controller requests prompt for: nextDialog
2017-02-11 15:32:20:700: --- Translated controller output: Wat kan ik voor je doen Gekke van Henkie?
2017-02-11 15:32:20:700: --- Translated output: Wat kan ik voor je doen Gekke van Henkie ?
2017-02-11 15:32:20:700: >>> Wat kan ik voor je doen Gekke van Henkie?
2017-02-11 15:32:20:701: <<< What is your name?
2017-02-11 15:32:20:702: --- Input language: ENG, input dialog: Handshake
2017-02-11 15:32:20:702: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-11 15:32:20:703: --- Translated input: What is your name ?
2017-02-11 15:32:21:009: --- Corrected input: Wat kan ik voor je doen Gekke van Henkie? What is your name?
2017-02-11 15:32:23:075: --- Extended input to output: My name is Dyz Lecticus. What is your name?
2017-02-11 15:32:23:075: --- Translated output: My name is Dyz Lecticus . What is your name ?
2017-02-11 15:32:23:076: >>> My name is Dyz Lecticus. What is your name?
2017-02-11 15:32:23:076: <<< Hoe heet jij?
2017-02-11 15:32:23:078: --- Input language: NLD, input dialog: Handdruk
2017-02-11 15:32:23:078: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeController)
2017-02-11 15:32:23:078: --- Translated input: Hoe heet jij ?
2017-02-11 15:32:23:079: --- Corrected input: What is your name? Hoe heet jij?
2017-02-11 15:32:24:537: --- Extended input to output: Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-02-11 15:32:24:537: --- Translated output: Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-02-11 15:32:24:538: >>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

Average time spent thinking per response: 1009 ms
~~~~

Test results
------------
All 2 tests have been executed successfully (17 assertions).  
Total test duration: 12783 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zid.test.TestDialog: 321 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestDialogHandler: 34559 Kb / 33 Mb
