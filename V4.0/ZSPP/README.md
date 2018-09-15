Zeesoft Sequence Preprocessor
=============================
The Zeesoft Sequence Preprocessor provides a simple JSON API for language specific sentence preprocessing.

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft Object Database](https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZODB/)  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSPP/releases/zspp-0.9.0.zip) to download the latest ZSPP release (version 0.9.0).  
All ZSPP releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSPP/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSPP](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSPP/src/nl/zeesoft/zspp/test/ZSPP.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zspp.test.TestPreprocessorRequestResponse
----------------------------------------------------
This test shows how to convert *PreprocessorRequestResponse* instances to and from JSON.

**Example implementation**  
~~~~
// Create the PreprocessorRequestResponse
PreprocessorRequestResponse request = new PreprocessorRequestResponse();
// Convert the PreprocessorRequestResponse to JSON
JsFile json = request.toJson();
// Convert the PreprocessorRequestResponse from JSON
request.fromJson(json);
~~~~

Class references;  
 * [TestPreprocessorRequestResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSPP/src/nl/zeesoft/zspp/test/TestPreprocessorRequestResponse.java)
 * [PreprocessorRequestResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSPP/src/nl/zeesoft/zspp/prepro/PreprocessorRequestResponse.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "languages": [
    "EN"
  ],
  "sequence": "sequence of symbols"
}
~~~~

Test results
------------
All 1 tests have been executed successfully (0 assertions).  
Total test duration: 11 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zspp.test.TestPreprocessorRequestResponse: 475 Kb / 0 Mb
