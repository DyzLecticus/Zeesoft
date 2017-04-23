Zeesoft Sample Orchestration
============================
Zeesoft Sample Orchestration (ZSO) is an extendable sample based orchestra that can play compositions.
The goal of this software is to demonstrate the scalability, maintainability and fault tolerance of the Zeesoft JSON Machine Orchestration.

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft JSON Machine Orchestration](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZJMO/)  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSO/releases/zso-0.9.10.zip) to download the latest ZSO release (version 0.9.10).  
All ZSO releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSO/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSO](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSO/src/nl/zeesoft/zso/test/ZSO.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zso.test.TestDemoComposition
---------------------------------------
This test shows how to create a *DemoComposition* instance and convert it to JSON.

**Example implementation**  
~~~~
// Create composition
DemoComposition comp = new DemoComposition();
// Convert to JSON
comp.toJson();
~~~~

Class references;  
 * [TestDemoComposition](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSO/src/nl/zeesoft/zso/test/TestDemoComposition.java)
 * [DemoComposition](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSO/src/nl/zeesoft/zso/composition/DemoComposition.java)

**Test output**  
The output of this test shows the JSON structure of the *DemoComposition*.  
~~~~
{
  "name": "Demo",
  "loop": "true",
  "beatsPerMinute": 120,
  "beatsPerBar": 4,
  "stepsPerBeat": 4,
  "steps": [
    {
      "positionName": "Basebeat",
      "bar": 1,
      "number": 1,
      "startMs": 0
    },
    {
      "positionName": "Basebeat",
      "bar": 1,
      "number": 11,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 1,
      "number": 5,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 1,
      "number": 13,
      "startMs": 0
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 1,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 3,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 5,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 7,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 8,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 9,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 10,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 11,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 13,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 1,
      "number": 15,
      "startMs": 0,
      "durationMs": 150
    },
    {
      "positionName": "Basebeat",
      "bar": 2,
      "number": 1,
      "startMs": 0
    },
    {
      "positionName": "Basebeat",
      "bar": 2,
      "number": 7,
      "startMs": 0
    },
    {
      "positionName": "Basebeat",
      "bar": 2,
      "number": 11,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 2,
      "number": 5,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 2,
      "number": 13,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 2,
      "number": 16,
      "startMs": 0,
      "durationMs": 100
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 1,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 3,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 5,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 7,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 8,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 9,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 10,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 11,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 13,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 2,
      "number": 15,
      "startMs": 0,
      "durationMs": 150
    },
    {
      "positionName": "Basebeat",
      "bar": 3,
      "number": 1,
      "startMs": 0
    },
    {
      "positionName": "Basebeat",
      "bar": 3,
      "number": 11,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 3,
      "number": 5,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 3,
      "number": 13,
      "startMs": 0
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 1,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 3,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 5,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 7,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 8,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 9,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 10,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 11,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 13,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 3,
      "number": 15,
      "startMs": 0,
      "durationMs": 150
    },
    {
      "positionName": "Basebeat",
      "bar": 4,
      "number": 1,
      "startMs": 0
    },
    {
      "positionName": "Basebeat",
      "bar": 4,
      "number": 3,
      "startMs": 0
    },
    {
      "positionName": "Basebeat",
      "bar": 4,
      "number": 7,
      "startMs": 0
    },
    {
      "positionName": "Basebeat",
      "bar": 4,
      "number": 11,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 4,
      "number": 5,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 4,
      "number": 13,
      "startMs": 0
    },
    {
      "positionName": "Snare",
      "bar": 4,
      "number": 15,
      "startMs": 0,
      "durationMs": 100
    },
    {
      "positionName": "Snare",
      "bar": 4,
      "number": 16,
      "startMs": 0,
      "durationMs": 100
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 1,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 3,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 5,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 7,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 8,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 9,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 10,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 11,
      "startMs": 100,
      "durationMs": 50
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 13,
      "startMs": 150,
      "durationMs": 10
    },
    {
      "positionName": "Hihat",
      "bar": 4,
      "number": 15,
      "startMs": 0,
      "durationMs": 150
    }
  ]
}
~~~~

Test results
------------
All 1 tests have been executed successfully (1 assertions).  
Total test duration: 143 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zso.test.TestDemoComposition: 265 Kb / 0 Mb
