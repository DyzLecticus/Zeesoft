Zeesoft Smart Dialog Manager
============================
The Zeesoft Smart Dialog Manager provides a simple JSON API to manage smart dialogs.

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft Object Database](https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZODB/)  
 * [Zeesoft Natural Language Base](https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZNLB/)  
 * [Zeesoft Entity Value Translator](https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZEVT/)  
 * [Zeesoft Symbolic Confabulators](https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZSC/)  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSDM/releases/zsdm-0.9.0.zip) to download the latest ZSDM release (version 0.9.0).  
All ZSDM releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSDM/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSDM](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSDM/src/nl/zeesoft/zsdm/test/ZSDM.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zsdm.test.TestDialog
-------------------------------
This test shows how to convert *Dialog* instances to and from JSON.

**Example implementation**  
~~~~
// Create the dialog
Dialog dialog = new Dialog();
// Convert the dialog to JSON
JsFile json = dialog.toJson();
// Convert the dialog from JSON
dialog.fromJson(json);
~~~~

Class references;  
 * [TestDialog](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSDM/src/nl/zeesoft/zsdm/test/TestDialog.java)
 * [Dialog](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSDM/src/nl/zeesoft/zsdm/dialog/Dialog.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "language": "EN",
  "masterContext": "MasterContext",
  "context": "Context",
  "examples": [
    {
      "input": "Test input 1.",
      "output": "Test output 1.",
      "filterContext": "",
      "toLanguageClassifier": true,
      "toMasterClassifier": true,
      "toContextClassifier": true
    },
    {
      "input": "Test input 2.",
      "output": "Test output 2.",
      "filterContext": "",
      "toLanguageClassifier": true,
      "toMasterClassifier": true,
      "toContextClassifier": true
    }
  ],
  "variables": [
    {
      "name": "testVariable1",
      "type": "NUM",
      "session": false,
      "prompts": [
        "Test prompt 1?",
        "Test prompt 2?"
      ]
    },
    {
      "name": "testVariable2",
      "type": "CUR",
      "session": false,
      "prompts": [
        "Test prompt 1?",
        "Test prompt 2?"
      ]
    }
  ]
}
~~~~

Test results
------------
All 1 tests have been executed successfully (0 assertions).  
Total test duration: 16 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zsdm.test.TestDialog: 527 Kb / 0 Mb
