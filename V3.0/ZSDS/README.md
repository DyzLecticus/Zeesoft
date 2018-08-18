Zeesoft Smart Dialog Server
===========================
Zeesoft Smart Dialog Server (ZSDS) is an open source application server exposes the Zeesoft Smart Dialogs (ZSD) API.  

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft Smart Dialogs](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZSD/)  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSDS/releases/zsds-0.9.0.zip) to download the latest ZSDS release (version 0.9.0).  
All ZSDS releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSDS/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSDS](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSDS/src/nl/zeesoft/zsds/test/ZSDS.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zsds.test.TestTestConfiguration
------------------------------------------
~~~~
{
  "testCaseDir": "tests/",
  "defaultSleep": 20,
  "retryIfBusy": false,
  "maxRetries": 60,
  "environments": [
    {
      "name": "env",
      "url": "http://env.url",
      "fileName": "fileName.json"
    }
  ]
}
~~~~

nl.zeesoft.zsds.test.TestTestCaseSet
------------------------------------
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
Total test duration: 63 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zsds.test.TestTestConfiguration: 490 Kb / 0 Mb
 * nl.zeesoft.zsds.test.TestTestCaseSet: 480 Kb / 0 Mb
