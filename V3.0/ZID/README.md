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
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZID/releases/zid-1.0.1.zip) to download the latest ZID release (version 1.0.1).  
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

nl.zeesoft.zid.test.TestDialogJson
----------------------------------
This test shows how to convert *Dialog* objects to and from JSON.  
ZID provides the *DialogJson* class to do this.  

**Example implementation**  
~~~~
// Create dialog
Dialog dialog = new Dialog("Handshake",Language.ENG,HandshakeController.class.getName());
// Create a dialog JSON io
DialogJson io = new DialogJson();
// Convert dialog to JSON
JsFile json = io.toJson(dialog);
// Convert dialog from JSON
dialog = io.fromJson(json);
~~~~

This test uses the *MockDialogs*.
The dialogs created by this mock use the *HandshakeDialogController*.

Class references;  
 * [TestDialogJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/TestDialogJson.java)
 * [MockDialogs](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/MockDialogs.java)
 * [Dialog](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/dialog/Dialog.java)
 * [DialogJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/dialog/io/DialogJson.java)

**Test output**  
The output of this test shows the JSON structure of the English mock dialog.  
~~~~
{
  "name": "Handshake",
  "languageCode": "ENG",
  "controllerClassName": "nl.zeesoft.zid.test.HandshakeDialogController",
  "examples": [
    "example": {
      "input": "Hello. My name is {firstName} {preposition} {lastName}.",
      "output": "Hello. My name is {firstName} {preposition} {lastName}."
    },
    "example": {
      "input": "Hello, my name is {firstName} {lastName}.",
      "output": "Hello, my name is {firstName} {lastName}."
    },
    "example": {
      "input": "Hello. My name is {firstName}.",
      "output": "Hello. My name is {firstName}."
    },
    "example": {
      "input": "Hi. My name is {firstName} {preposition} {lastName}.",
      "output": "Hi. My name is {firstName} {preposition} {lastName}."
    },
    "example": {
      "input": "Hi, my name is {firstName} {lastName}.",
      "output": "Hi, my name is {firstName} {lastName}."
    },
    "example": {
      "input": "Hi. My name is {firstName}.",
      "output": "Hi. My name is {firstName}."
    },
    "example": {
      "input": "Hello.",
      "output": "Hello."
    },
    "example": {
      "input": "Hello!",
      "output": "Hello!"
    },
    "example": {
      "input": "Hi.",
      "output": "Hi."
    },
    "example": {
      "input": "Hi!",
      "output": "Hi!"
    },
    "example": {
      "input": "What is your name?",
      "output": "What is your name?"
    }
  ],
  "variables": [
    "variable": {
      "name": "firstName",
      "type": "ALPHABETIC",
      "examples": [
        "example": {
          "question": "What is your name?",
          "answer": "My name is {firstName} {preposition} {lastName}."
        },
        "example": {
          "question": "What is your name?",
          "answer": "My name is {firstName} {lastName}."
        },
        "example": {
          "question": "What is your name?",
          "answer": "My name is {firstName}."
        },
        "example": {
          "question": "What is your name?",
          "answer": "{firstName} {preposition} {lastName}."
        },
        "example": {
          "question": "What is your name?",
          "answer": "{firstName} {lastName}."
        },
        "example": {
          "question": "What is your name?",
          "answer": "{firstName} {lastName}."
        },
        "example": {
          "question": "What is your name?",
          "answer": "{firstName} {lastName}."
        },
        "example": {
          "question": "What is your name?",
          "answer": "{firstName}."
        },
        "example": {
          "question": "What is your firstname?",
          "answer": "{firstName}."
        },
        "example": {
          "question": "What is your firstname?",
          "answer": "My firstname is {firstName}."
        }
      ]
    },
    "variable": {
      "name": "lastName",
      "type": "ALPHABETIC",
      "examples": [
        "example": {
          "question": "What is your lastname?",
          "answer": "My lastname is {preposition} {lastName}."
        },
        "example": {
          "question": "What is your lastname?",
          "answer": "My lastname is {lastName}, {preposition}."
        },
        "example": {
          "question": "What is your lastname?",
          "answer": "My lastname is {lastName}."
        },
        "example": {
          "question": "What is your lastname?",
          "answer": "{preposition} {lastName}."
        },
        "example": {
          "question": "What is your lastname?",
          "answer": "{lastName}, {preposition}."
        },
        "example": {
          "question": "What is your lastname?",
          "answer": "{lastName}."
        }
      ]
    },
    "variable": {
      "name": "preposition",
      "type": "PREPOSITION",
      "examples": []
    },
    "variable": {
      "name": "nextDialog",
      "type": "ALPHABETIC",
      "examples": [
        "example": {
          "question": "What can I do for you {fullName}?",
          "answer": "{nextDialog}."
        }
      ]
    }
  ]
}
~~~~

nl.zeesoft.zid.test.TestDialogQnATsv
------------------------------------
This test shows how to convert 'Question and Answer' *Dialog* objects to and from TSV (Tab Separated Values).  
ZID provides the *DialogQnATsv* class to do this.  

**Example implementation**  
~~~~
// Create dialog
Dialog dialog = new Dialog("Handshake",Language.ENG,HandshakeController.class.getName());
// Create a dialog QnA TSV io
DialogQnATsv io = new DialogQnATsv();
// Convert dialog to QnA TSV
ZStringBuilder tsv = io.toQnATsv(dialog);
// Convert dialog from QnA TSV
dialog = io.fromQnATsv(tsv,Language.ENG,HandshakeDialogController.class.getName());
~~~~

This test uses the *MockDialogs*.
The dialogs created by this mock use the *HandshakeDialogController*.

Class references;  
 * [TestDialogQnATsv](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/TestDialogQnATsv.java)
 * [MockDialogs](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/test/MockDialogs.java)
 * [Dialog](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/dialog/Dialog.java)
 * [DialogQnATsv](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZID/src/nl/zeesoft/zid/dialog/io/DialogQnATsv.java)

**Test output**  
The output of this test shows the TSV structure of the English mock dialog.  
~~~~
Question	Answer	Variable
Hello. My name is {firstName} {preposition} {lastName}.	Hello {fullName}.	
Hello, my name is {firstName} {lastName}.	Hello {fullName}.	
Hello. My name is {firstName}.	Hello {firstName}. What is your lastname?	
Hi. My name is {firstName} {preposition} {lastName}.	Hello {fullName}.	
Hi, my name is {firstName} {lastName}.	Hello {fullName}.	
Hi. My name is {firstName}.	Hello {firstName}. What is your lastname?	
Hello.	Hello. My name is Dyz Lecticus. What is your name?	
Hello!	Hello. My name is Dyz Lecticus. What is your name?	
Hi.	Hi. My name is Dyz Lecticus. What is your name?	
Hi!	Hi. My name is Dyz Lecticus. What is your name?	
What is your name?	My name is Dyz Lecticus. What is your name?	
What is your name?	My name is {firstName} {preposition} {lastName}.	firstName
What is your name?	My name is {firstName} {lastName}.	firstName
What is your name?	My name is {firstName}.	firstName
What is your name?	{firstName} {preposition} {lastName}.	firstName
What is your name?	{firstName} {lastName}.	firstName
What is your name?	{firstName} {lastName}.	firstName
What is your name?	{firstName} {lastName}.	firstName
What is your name?	{firstName}.	firstName
What is your firstname?	{firstName}.	firstName
What is your firstname?	My firstname is {firstName}.	firstName
What is your lastname?	My lastname is {preposition} {lastName}.	lastName
What is your lastname?	My lastname is {lastName}, {preposition}.	lastName
What is your lastname?	My lastname is {lastName}.	lastName
What is your lastname?	{preposition} {lastName}.	lastName
What is your lastname?	{lastName}, {preposition}.	lastName
What is your lastname?	{lastName}.	lastName
		preposition
What can I do for you {fullName}?	{nextDialog}.	nextDialog

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
Initializing pattern manager took 417 ms

Initializing dialog handler took 83 ms

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

Average time spent thinking per response: 156 ms

2017-12-23 15:50:04:193: <<< Hello.
2017-12-23 15:50:04:195: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-12-23 15:50:04:196: --- Translated input: Hello .
2017-12-23 15:50:04:196: --- Corrected input: Hello.
2017-12-23 15:50:04:616: --- Extended input to output: Hello. My name is Dyz Lecticus. What is your name?
2017-12-23 15:50:04:616: --- Translated output: Hello . My name is Dyz Lecticus . What is your name ?
2017-12-23 15:50:04:616: >>> Hello. My name is Dyz Lecticus. What is your name?
2017-12-23 15:50:04:616: <<< My name is andre van der zee.
2017-12-23 15:50:04:624: --- Continuing dialog: Handshake
2017-12-23 15:50:04:625: --- Translated input: My name is ALPHABETIC_UNI:andre PREPOSITION_NLD:3 ALPHABETIC_UNI:zee .
2017-12-23 15:50:04:711: --- Corrected input: Hello. My name is Dyz Lecticus. What is your name? My name is {firstName} {preposition} {lastName}.
2017-12-23 15:50:04:711: --- Updated variables: firstName = ALPHABETIC_UNI:andre, lastName = ALPHABETIC_UNI:zee, preposition = PREPOSITION_NLD:3
2017-12-23 15:50:04:711: --- Controller output: Nice to interact with you again {fullName}.
2017-12-23 15:50:04:711: --- Translated controller output: Nice to interact with you again Andre van der Zee.
2017-12-23 15:50:04:711: --- Completed dialog: Handshake
2017-12-23 15:50:04:711: --- Translated output: Nice to interact with you again Andre van der Zee .
2017-12-23 15:50:04:711: >>> Nice to interact with you again Andre van der Zee.
2017-12-23 15:50:04:712: <<< Hallo , ik heet karel de grote.
2017-12-23 15:50:04:713: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-12-23 15:50:04:714: --- Translated input: Hallo , ik heet ALPHABETIC_UNI:karel PREPOSITION_NLD:5|ALPHABETIC_UNI:de ALPHABETIC_UNI:grote .
2017-12-23 15:50:04:730: --- Corrected input: Hallo, ik heet {firstName} {preposition} {lastName}.
2017-12-23 15:50:04:730: --- Updated variables: firstName = ALPHABETIC_UNI:karel, lastName = ALPHABETIC_UNI:grote, preposition = PREPOSITION_NLD:5
2017-12-23 15:50:04:730: --- Controller requests prompt for: nextDialog
2017-12-23 15:50:04:731: --- Translated controller output: Wat kan ik voor je doen Karel de Grote?
2017-12-23 15:50:04:731: --- Completed dialog: Handdruk
2017-12-23 15:50:04:731: --- Translated output: Wat kan ik voor je doen Karel de Grote ?
2017-12-23 15:50:04:731: >>> Wat kan ik voor je doen Karel de Grote?
2017-12-23 15:50:04:731: <<< Hallo?
2017-12-23 15:50:04:731: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-12-23 15:50:04:731: --- Translated input: Hallo ?
2017-12-23 15:50:04:731: --- Corrected input: Hallo?
2017-12-23 15:50:04:956: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-12-23 15:50:04:956: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-12-23 15:50:04:956: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-12-23 15:50:04:956: <<< Gekke henkie.
2017-12-23 15:50:04:957: --- Continuing dialog: Handdruk
2017-12-23 15:50:04:957: --- Translated input: ALPHABETIC_UNI:Gekke ALPHABETIC_UNI:henkie .
2017-12-23 15:50:05:014: --- Corrected input: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam? {firstName} {lastName}.
2017-12-23 15:50:05:014: --- Updated variables: firstName = ALPHABETIC_UNI:Gekke, lastName = ALPHABETIC_UNI:henkie
2017-12-23 15:50:05:014: --- Controller requests prompt for: nextDialog
2017-12-23 15:50:05:014: --- Translated controller output: Wat kan ik voor je doen Gekke Henkie?
2017-12-23 15:50:05:014: --- Completed dialog: Handdruk
2017-12-23 15:50:05:014: --- Translated output: Wat kan ik voor je doen Gekke Henkie ?
2017-12-23 15:50:05:014: >>> Wat kan ik voor je doen Gekke Henkie?
2017-12-23 15:50:05:014: <<< Gek.
2017-12-23 15:50:05:015: --- Unable to determine dialog
2017-12-23 15:50:05:021: --- Translated input: ALPHABETIC_UNI:Gek .
2017-12-23 15:50:05:075: --- Corrected input: Wat kan ik voor je doen Gekke Henkie? {firstName}.
2017-12-23 15:50:05:312: --- Extended input to output: Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-12-23 15:50:05:312: --- Translated output: Hallo . Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-12-23 15:50:05:312: >>> Hallo. Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-12-23 15:50:05:313: <<< Van henkie.
2017-12-23 15:50:05:313: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-12-23 15:50:05:313: --- Translated input: PREPOSITION_NLD:1|ALPHABETIC_UNI:Van ALPHABETIC_UNI:henkie .
2017-12-23 15:50:05:320: --- Corrected input: {preposition} {lastName}.
2017-12-23 15:50:05:320: --- Updated variables: lastName = ALPHABETIC_UNI:henkie, preposition = PREPOSITION_NLD:1
2017-12-23 15:50:05:320: --- Controller output: Wat is jouw voornaam?
2017-12-23 15:50:05:320: --- Translated controller output: Wat is jouw voornaam?
2017-12-23 15:50:05:320: --- Translated output: Wat is jouw voornaam ?
2017-12-23 15:50:05:320: >>> Wat is jouw voornaam?
2017-12-23 15:50:05:320: <<< Gekkie.
2017-12-23 15:50:05:322: --- Continuing dialog: Handdruk
2017-12-23 15:50:05:323: --- Translated input: ALPHABETIC_UNI:Gekkie .
2017-12-23 15:50:05:332: --- Corrected input: Wat is jouw voornaam? {firstName}.
2017-12-23 15:50:05:332: --- Updated variables: firstName = ALPHABETIC_UNI:Gekkie
2017-12-23 15:50:05:332: --- Controller requests prompt for: nextDialog
2017-12-23 15:50:05:332: --- Translated controller output: Wat kan ik voor je doen Gekkie van Henkie?
2017-12-23 15:50:05:332: --- Completed dialog: Handdruk
2017-12-23 15:50:05:332: --- Translated output: Wat kan ik voor je doen Gekkie van Henkie ?
2017-12-23 15:50:05:332: >>> Wat kan ik voor je doen Gekkie van Henkie?
2017-12-23 15:50:05:332: <<< What is your name?
2017-12-23 15:50:05:333: --- Selected dialog: Handshake (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-12-23 15:50:05:333: --- Translated input: What is your name ?
2017-12-23 15:50:05:333: --- Corrected input: What is your name?
2017-12-23 15:50:05:546: --- Extended input to output: My name is Dyz Lecticus. What is your name?
2017-12-23 15:50:05:546: --- Translated output: My name is Dyz Lecticus . What is your name ?
2017-12-23 15:50:05:546: >>> My name is Dyz Lecticus. What is your name?
2017-12-23 15:50:05:546: <<< Hoe heet jij?
2017-12-23 15:50:05:546: --- Selected dialog: Handdruk (controller: nl.zeesoft.zid.test.HandshakeDialogController)
2017-12-23 15:50:05:546: --- Translated input: Hoe heet jij ?
2017-12-23 15:50:05:546: --- Corrected input: Hoe heet jij?
2017-12-23 15:50:05:760: --- Extended input to output: Mijn naam is Dyz Lecticus. Wat is jouw naam?
2017-12-23 15:50:05:760: --- Translated output: Mijn naam is Dyz Lecticus . Wat is jouw naam ?
2017-12-23 15:50:05:760: >>> Mijn naam is Dyz Lecticus. Wat is jouw naam?

~~~~

Test results
------------
All 5 tests have been executed successfully (31 assertions).  
Total test duration: 3665 ms (total sleep duration: 1000 ms).  

Memory usage per test;  
 * nl.zeesoft.zid.test.TestDialog: 542 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestDialogJson: 422 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestDialogQnATsv: 417 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestSessionManager: 467 Kb / 0 Mb
 * nl.zeesoft.zid.test.TestSessionDialogHandler: 35914 Kb / 35 Mb
