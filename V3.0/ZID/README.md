Zeesoft Intelligent Dialogs
===========================
Zeesoft Intelligent Dialogs (ZID) is an open source library for Java application development.
It provides support for defining and handling written dialogs while translating the input into parameterized program calls.

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft Symbolic Confabulation](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSC/)  
 * [Zeesoft Symbolic Pattern Recognition](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSPR/)  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/zid-1.0.0.zip) to download the latest ZID release (version 1.0.0).  
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

A *Dialog* requires a controller class name that refers to a class that extends the *SessionDialogController*.  
This controller is instantiated by the *SessionDialogHandler* when it detects that the input requires it.  
The session dialog controller is then notified whenever the *SessionDialogHandler* has updated session dialog variable values.  
Session dialog controllers can use session variables to share objects with other controllers.  

This test uses the *MockDialogs*.
The dialogs created by this mock use the *HandshakeDialogController*.

Class references;  
 * [TestDialog](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/TestDialog.java)
 * [MockDialogs](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/MockDialogs.java)
 * [Dialog](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/dialog/Dialog.java)
 * [SessionDialogController](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/session/SessionDialogController.java)
 * [SessionDialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/session/SessionDialogHandler.java)
 * [HandshakeDialogController](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/HandshakeDialogController.java)

**Test output**  
The output of this test shows the dialog structure of the mock dialogs.  
~~~~
Dialog: Handshake, language: ENG, controller: nl.zeesoft.zid.test.HandshakeDialogController
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
Dialog: Handdruk, language: NLD, controller: nl.zeesoft.zid.test.HandshakeDialogController
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

nl.zeesoft.zid.test.TestSessionManager
--------------------------------------
This test shows how to create a *SessionManager* and then use it to manage sessions.  

**Example implementation**  
~~~~
// Create session manager
SessionManager sessionManager = new SessionManager(messenger);
// Open session
Session session = sessionManager.openSession();
// Close session
sessionManager.closeSession(session.getId());
// Close inactive session
sessionManager.closeInactiveSessions(1000);
~~~~

Class references;  
 * [TestSessionManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/TestSessionManager.java)
 * [SessionManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/session/SessionManager.java)
 * [Session](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/session/Session.java)

**Test output**  
The output of this test shows the result of opening and closing sessions in several ways.  
~~~~
Opened and closed all sessions as expected
~~~~

nl.zeesoft.zid.test.TestSessionDialogHandler
--------------------------------------------
This test shows how to create a *SessionDialogHandler* and then use it to produce dialog output for input.  

**Example implementation**  
~~~~
// Create session dialog handler
SessionDialogHandler handler = new DialogHandler(dialogs,patternManager);
// Initialize session dialog handler
handler.initialize();
// Handle session dialog input
handler.handleSessionInput(session);
~~~~

A *SessionDialogHandler* requires a list of dialogs and a *PatternManager*.  
Sessions can be obtained by implementing a *SessionManager*.  

This test uses the *MockSessionDialogHandler*.
The *MockSessionDialogHandler* uses the *MockDialogs* and the *MockPatternManager*.

Class references;  
 * [TestSessionDialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/TestSessionDialogHandler.java)
 * [MockSessionDialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/MockSessionDialogHandler.java)
 * [MockDialogs](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/MockDialogs.java)
 * [MockPatternManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zspr/test/MockPatternManager.java)
 * [SessionManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/session/SessionManager.java)
 * [SessionDialogHandler](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/session/SessionDialogHandler.java)

**Test output**  
The output of this test shows;  
 * The mock initialization duration.  
 * The scripted session dialog handler input with corresponding output.  
 * The average time spent thinking per response.  
 * The session dialog handler log.  
~~~~
Initializing pattern manager took 416 ms

Initializing dialog handler took 106 ms

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
<<< gek
>>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
<<< van henkie
>>> Wat is jouw voornaam?
<<< gekkie
>>> Wat kan ik voor je doen Gekkie van Henkie?
<<< what is your name?
>>> My name is Dyz Lecticus. What is your name?
<<< hoe heet jij?
>>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

Average time spent thinking per response: 149 ms

2017-10-08 19:02:15:475: <<< Hello.
2017-10-08 19:02:15:476: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-10-08 19:02:15:477: --- Translated input: Hello .
2017-10-08 19:02:15:477: --- Corrected input: Hello.
2017-10-08 19:02:15:900: --- Extended input to output: Hello. My name is Dyz Lecticus. What is your name?
2017-10-08 19:02:15:900: --- Translated output: Hello . My name is Dyz Lecticus . What is your name ?
2017-10-08 19:02:15:900: >>> Hello. My name is Dyz Lecticus. What is your name?
2017-10-08 19:02:15:900: <<< My name is andre van der zee.
2017-10-08 19:02:15:907: --- Continuing dialog: Handshake
2017-10-08 19:02:15:907: --- Translated input: My name is ALPHABETIC_UNI:andre PREPOSITION_NLD:3 ALPHABETIC_UNI:zee .
2017-10-08 19:02:15:984: --- Corrected input: Hello. My name is Dyz Lecticus. What is your name? My name is {firstName} {preposition} {lastName}.
2017-10-08 19:02:15:985: --- Updated variables: firstName = ALPHABETIC_UNI:andre, lastName = ALPHABETIC_UNI:zee, preposition = PREPOSITION_NLD:3
2017-10-08 19:02:15:985: --- Controller output: Nice to interact with you again {fullName}.
2017-10-08 19:02:15:985: --- Translated controller output: Nice to interact with you again Andre van der Zee.
2017-10-08 19:02:15:985: --- Completed dialog: Handshake
2017-10-08 19:02:15:985: --- Translated output: Nice to interact with you again Andre van der Zee .
2017-10-08 19:02:15:985: >>> Nice to interact with you again Andre van der Zee.
2017-10-08 19:02:15:985: <<< Hallo , ik heet karel de grote.
2017-10-08 19:02:15:988: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-10-08 19:02:15:988: --- Translated input: Hallo , ik heet ALPHABETIC_UNI:karel PREPOSITION_NLD:5|ALPHABETIC_UNI:de ALPHABETIC_UNI:grote .
2017-10-08 19:02:16:002: --- Corrected input: Hallo, ik heet {firstName} {preposition} {lastName}.
2017-10-08 19:02:16:002: --- Updated variables: firstName = ALPHABETIC_UNI:karel, lastName = ALPHABETIC_UNI:grote, preposition = PREPOSITION_NLD:5
2017-10-08 19:02:16:002: --- Controller requests prompt for: nextDialog
2017-10-08 19:02:16:003: --- Translated controller output: Wat kan ik voor je doen Karel de Grote?
2017-10-08 19:02:16:003: --- Completed dialog: Handdruk
2017-10-08 19:02:16:003: --- Translated output: Wat kan ik voor je doen Karel de Grote ?
2017-10-08 19:02:16:003: >>> Wat kan ik voor je doen Karel de Grote?
2017-10-08 19:02:16:003: <<< Hallo?
2017-10-08 19:02:16:003: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-10-08 19:02:16:004: --- Translated input: Hallo ?
2017-10-08 19:02:16:004: --- Corrected input: Hallo?
2017-10-08 19:02:16:238: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-10-08 19:02:16:238: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-10-08 19:02:16:239: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-10-08 19:02:16:239: <<< Gekke henkie.
2017-10-08 19:02:16:241: --- Continuing dialog: Handdruk
2017-10-08 19:02:16:242: --- Translated input: ALPHABETIC_UNI:Gekke ALPHABETIC_UNI:henkie .
2017-10-08 19:02:16:312: --- Corrected input: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam? {firstName} {lastName}.
2017-10-08 19:02:16:312: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke, lastName = ALPHABETIC_UNI:henkie
2017-10-08 19:02:16:313: --- Controller requests prompt for: nextDialog
2017-10-08 19:02:16:313: --- Translated controller output: Wat kan ik voor je doen Gekke Henkie?
2017-10-08 19:02:16:313: --- Completed dialog: Handdruk
2017-10-08 19:02:16:313: --- Translated output: Wat kan ik voor je doen Gekke Henkie ?
2017-10-08 19:02:16:313: >>> Wat kan ik voor je doen Gekke Henkie?
2017-10-08 19:02:16:313: <<< Gek.
2017-10-08 19:02:16:318: --- Unable to determine dialog
2017-10-08 19:02:16:324: --- Translated input: ALPHABETIC_UNI:Gek .
2017-10-08 19:02:16:384: --- Corrected input: Wat kan ik voor je doen Gekke Henkie? {firstName}.
2017-10-08 19:02:16:547: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-10-08 19:02:16:547: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-10-08 19:02:16:547: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-10-08 19:02:16:548: <<< Van henkie.
2017-10-08 19:02:16:548: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-10-08 19:02:16:549: --- Translated input: PREPOSITION_NLD:1|ALPHABETIC_UNI:Van ALPHABETIC_UNI:henkie .
2017-10-08 19:02:16:552: --- Corrected input: {preposition} {lastName}.
2017-10-08 19:02:16:552: --- Updated variables: lastName = ALPHABETIC_UNI:henkie, preposition = PREPOSITION_NLD:1
2017-10-08 19:02:16:552: --- Controller output: Wat is jouw voornaam?
2017-10-08 19:02:16:552: --- Translated controller output: Wat is jouw voornaam?
2017-10-08 19:02:16:552: --- Translated output: Wat is jouw voornaam ?
2017-10-08 19:02:16:552: >>> Wat is jouw voornaam?
2017-10-08 19:02:16:552: <<< Gekkie.
2017-10-08 19:02:16:552: --- Continuing dialog: Handdruk
2017-10-08 19:02:16:552: --- Translated input: ALPHABETIC_UNI:Gekkie .
2017-10-08 19:02:16:559: --- Corrected input: Wat is jouw voornaam? {firstName}.
2017-10-08 19:02:16:559: --- Updated variables: firstName = ALPHABETIC_UNI:Gekkie
2017-10-08 19:02:16:559: --- Controller requests prompt for: nextDialog
2017-10-08 19:02:16:559: --- Translated controller output: Wat kan ik voor je doen Gekkie van Henkie?
2017-10-08 19:02:16:559: --- Completed dialog: Handdruk
2017-10-08 19:02:16:559: --- Translated output: Wat kan ik voor je doen Gekkie van Henkie ?
2017-10-08 19:02:16:559: >>> Wat kan ik voor je doen Gekkie van Henkie?
2017-10-08 19:02:16:559: <<< What is your name?
2017-10-08 19:02:16:559: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-10-08 19:02:16:559: --- Translated input: What is your name ?
2017-10-08 19:02:16:559: --- Corrected input: What is your name?
2017-10-08 19:02:16:754: --- Extended input to output: My name is Dyz Lecticus. What is your name?
2017-10-08 19:02:16:754: --- Translated output: My name is Dyz Lecticus . What is your name ?
2017-10-08 19:02:16:754: >>> My name is Dyz Lecticus. What is your name?
2017-10-08 19:02:16:754: <<< Hoe heet jij?
2017-10-08 19:02:16:754: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-10-08 19:02:16:754: --- Translated input: Hoe heet jij ?
2017-10-08 19:02:16:754: --- Corrected input: Hoe heet jij?
2017-10-08 19:02:16:966: --- Extended input to output: Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-10-08 19:02:16:966: --- Translated output: Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-10-08 19:02:16:966: >>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

~~~~

Test results
------------
All 3 tests have been executed successfully (23 assertions).  
Total test duration: 3392 ms (total sleep duration: 1000 ms).  

Memory usage per test;  
 * nl.zeesoft.zid.test.TestDialog: 531 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestSessionManager: 458 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestSessionDialogHandler: 35957 Kb / 35 Mb
