Zeesoft Smart Dialog Server
===========================
Zeesoft Smart Dialog Server (ZSDS) is an open source JSON REST API for natural language understanding and processing. It is designed to provide fast and professional artificial intelligence for chatbots. 

Features include;  
 * A web based administration interface  
 * Out-of-the-box support for basic English and Dutch entities and dialogs  
 * Request specific contextual behavior and response randomization   
 * Three level hierarchical intent classification (including language classification)  
 * Input preprocessing and spelling correction  
 * Automated self and DTAP environment integration testing  
 * Highly configurable, extendable and scalable architecture  

Supported universal, English and Dutch entities include;  
 * Names of people  
 * Country and language names  
 * Currency codes and names  
 * Typed smiley and frowny emoticons  
 * Months, dates, time and durations  
 * Confirmation booleans  
 * Integer numbers  
 * Profanity  

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft Smart Dialogs](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSD/)  

**Downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSDS/zsds-dev.war) to download the latest ZSDS development WAR.  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSDS/zsds.war) to download the latest ZSDS production WAR.  
The only difference between the WAR files is the initial configuration.  
In the development WAR, debugging and automated self testing are enabled by default.  
Both WAR files contain the full source code, documentation and build scripts.  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSDS](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSDS/src/nl/zeesoft/zsds/test/ZSDS.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zsds.test.TestTestConfiguration
------------------------------------------
This test shows how to convert a *TestConfiguration* to and from JSON.

**Example implementation**  
~~~~
// Create the test configuration
TestConfiguration tco = new TestConfiguration();
// Initialize the test configuration
tco.initialize();
// Convert the test configuration to JSON
JsFile json = tco.toJson();
// Convert the test configuration from JSON
tco.fromJson(json);
~~~~

Class references;  
 * [TestTestConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSDS/src/nl/zeesoft/zsds/test/TestTestConfiguration.java)
 * [TestConfiguration](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSDS/src/nl/zeesoft/zsds/tester/TestConfiguration.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "testCaseDir": "tests/",
  "defaultSleep": 20,
  "retryIfBusy": false,
  "maxRetries": 60,
  "environments": [
    {
      "name": "env",
      "url": "http://env:9090",
      "directory": "dir/"
    }
  ]
}
~~~~

nl.zeesoft.zsds.test.TestTestCaseSet
------------------------------------
This test shows how to convert a *TestCaseSet* to and from JSON.

**Example implementation**  
~~~~
// Create the test case set
TestCaseSet tcs = new TestCaseSet();
// Initialize the test case set
tcs.initialize();
// Convert the test case set to JSON
JsFile json = tcs.toJson();
// Convert the test case set from JSON
tcs.fromJson(json);
~~~~

Class references;  
 * [TestTestCaseSet](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSDS/src/nl/zeesoft/zsds/test/TestTestCaseSet.java)
 * [TestCaseSet](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSDS/src/nl/zeesoft/zsds/tester/TestCaseSet.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "testCases": [
    {
      "name": "TC001",
      "io": [
        {
          "request": {
            "prompt": "",
            "input": "What is your name?",
            "language": "",
            "masterContext": "",
            "context": "",
            "isTestRequest": false,
            "appendDebugLog": false,
            "classifyLanguage": false,
            "minLanguageChangeDifference": 0.1,
            "correctInput": false,
            "classifyMasterContext": false,
            "classifyMasterContextThreshold": 0.25,
            "classifyContext": false,
            "classifyContextThreshold": 0.25,
            "checkProfanity": false,
            "translateEntityValues": false,
            "translateEntityTypes": [],
            "matchThreshold": 0.1,
            "randomizeOutput": true,
            "filterContexts": [],
            "dialogVariableValues": []
          },
          "expectedResponse": {
            "classifiedLanguages": [],
            "correctedInput": "",
            "classificationSequence": "",
            "classifiedMasterContexts": [],
            "classifiedContexts": [],
            "entityValueTranslation": "",
            "entityValueTranslationCorrected": "",
            "debugLog": "",
            "contextOutputs": []
          }
        },
        {
          "request": {
            "prompt": "",
            "input": "What is your goal?",
            "language": "",
            "masterContext": "",
            "context": "",
            "isTestRequest": false,
            "appendDebugLog": false,
            "classifyLanguage": false,
            "minLanguageChangeDifference": 0.1,
            "correctInput": false,
            "classifyMasterContext": false,
            "classifyMasterContextThreshold": 0.25,
            "classifyContext": false,
            "classifyContextThreshold": 0.25,
            "checkProfanity": false,
            "translateEntityValues": false,
            "translateEntityTypes": [],
            "matchThreshold": 0.1,
            "randomizeOutput": true,
            "filterContexts": [],
            "dialogVariableValues": []
          },
          "expectedResponse": {
            "classifiedLanguages": [],
            "correctedInput": "",
            "classificationSequence": "",
            "classifiedMasterContexts": [],
            "classifiedContexts": [],
            "entityValueTranslation": "",
            "entityValueTranslationCorrected": "",
            "debugLog": "",
            "contextOutputs": []
          }
        }
      ]
    }
  ]
}
~~~~

Test results
------------
All 2 tests have been executed successfully (6 assertions).  
Total test duration: 44 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zsds.test.TestTestConfiguration: 501 Kb / 0 Mb
 * nl.zeesoft.zsds.test.TestTestCaseSet: 486 Kb / 0 Mb
