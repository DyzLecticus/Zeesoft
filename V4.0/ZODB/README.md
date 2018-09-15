Zeesoft Object Database
=======================
The Zeesoft Object Database provides a simple JSON API to store JSON objects.

This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZODB/releases/zodb-0.9.0.zip) to download the latest ZODB release (version 0.9.0).  
All ZODB releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZODB/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZODB](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZODB/src/nl/zeesoft/zodb/test/ZODB.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zodb.test.TestConfig
-------------------------------
This test shows how to convert a *Config* instance to and from JSON.

**Example implementation**  
~~~~
// Create the test configuration
Config config = new Config();
// Convert the test configuration to JSON
JsFile json = config.toJson();
// Convert the test configuration from JSON
config.fromJson(json);
~~~~

Class references;  
 * [TestConfig](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZODB/src/nl/zeesoft/zodb/test/TestConfig.java)
 * [Config](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZODB/src/nl/zeesoft/zodb/Config.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "debug": true,
  "dataDir": "dir/",
  "servletUrl": "http://127.0.0.1",
  "modules": [
    {
      "name": "ZODB",
      "url": "http://test.domain",
      "selfTest": true
    }
  ]
}
~~~~

nl.zeesoft.zodb.test.TestDatabaseRequest
----------------------------------------
This test shows how to convert a *DatabaseRequest* to and from JSON.

**Example implementation**  
~~~~
// Create the database request
DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
// Convert the database request to JSON
JsFile json = request.toJson();
// Convert the database request from JSON
request.fromJson(json);
~~~~

Class references;  
 * [TestDatabaseRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZODB/src/nl/zeesoft/zodb/test/TestDatabaseRequest.java)
 * [DatabaseRequest](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZODB/src/nl/zeesoft/zodb/db/DatabaseRequest.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "type": "LIST",
  "startsWith": "testObject",
  "start": 0,
  "max": 10
}

{
  "type": "GET",
  "id": 1
}

{
  "type": "ADD",
  "name": "objectName",
  "object": {
    "data": "addObjectData"
  }
}

{
  "type": "SET",
  "id": 1,
  "object": {
    "data": "setObjectData"
  }
}

{
  "type": "REMOVE",
  "id": 1
}
~~~~

nl.zeesoft.zodb.test.TestDatabaseResponse
-----------------------------------------
This test shows how to convert a *DatabaseResponse* to and from JSON.

**Example implementation**  
~~~~
// Create the database response
DatabaseResponse response = new DatabaseResponse(DatabaseResponse.TYPE_LIST);
// Convert the database response to JSON
JsFile json = response.toJson();
// Convert the database response from JSON
response.fromJson(json);
~~~~

Class references;  
 * [TestDatabaseResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZODB/src/nl/zeesoft/zodb/test/TestDatabaseResponse.java)
 * [DatabaseResponse](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZODB/src/nl/zeesoft/zodb/db/DatabaseResponse.java)

**Test output**  
The output of this test shows the converted JSON.  
~~~~
{
  "statusCode": 503,
  "errors": [
    "Database is busy. Please wait."
  ]
}

{
  "statusCode": 200,
  "results": [
    {
      "name": "testName",
      "id": 1,
      "object": {
        "data": "testObjectData"
      }
    }
  ]
}
~~~~

Test results
------------
All 3 tests have been executed successfully (16 assertions).  
Total test duration: 77 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zodb.test.TestConfig: 551 Kb / 0 Mb
 * nl.zeesoft.zodb.test.TestDatabaseRequest: 400 Kb / 0 Mb
 * nl.zeesoft.zodb.test.TestDatabaseResponse: 405 Kb / 0 Mb
