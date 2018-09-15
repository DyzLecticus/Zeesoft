Zeesoft Natural Language Base
=============================
The Zeesoft Natural Language Base provides a simple JSON API for language specific sentence preprocessing.

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft Object Database](https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZODB/)  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZNLB/releases/znlb-0.9.0.zip) to download the latest ZNLB release (version 0.9.0).  
All ZNLB releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZNLB/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZNLB](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZNLB/src/nl/zeesoft/znlb/test/ZNLB.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.znlb.test.TestPreprocessorRequestResponse
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
 * [TestPreprocessorRequestResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZNLB/src/nl/zeesoft/znlb/test/TestPreprocessorRequestResponse.java)
 * [PreprocessorRequestResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZNLB/src/nl/zeesoft/znlb/prepro/PreprocessorRequestResponse.java)

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
Total test duration: 13 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.znlb.test.TestPreprocessorRequestResponse: 475 Kb / 0 Mb
