Zeesoft Development Kit
=======================
The Zeesoft Development Kit (ZDK) is an open source library for Java application development.  

It provides support for;  
 * Extended StringBuilder manipulation and validation  
 * Basic file writing and reading  
 * JSON  
 * Multi threading  
 * Application message handling
 * Self documenting and testing libraries  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDK/releases/zdk-1.0.2.zip) to download the latest ZDK release (version 1.0.2).  
All ZDK releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDK/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZDK](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/ZDK.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zdk.test.impl.TestZIntegerGenerator
----------------------------------------------
This test shows how to use the *ZIntegerGenerator* to generate non-negative random integers.

**Example implementation**  
~~~~
// Create the ZIntegerGenerator
ZIntegerGenerator generator = new ZIntegerGenerator(0,9);
// Generate a random integer
int random = generator.getNewInteger();
~~~~

Class references;  
 * [TestZIntegerGenerator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestZIntegerGenerator.java)
 * [ZIntegerGenerator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/ZIntegerGenerator.java)

**Test output**  
The output of this test shows 100 randomly generated integers within the range 0 - 9.
~~~~
9 4 2 8 8 6 8 5 1 7 4 1 3 3 9 9 3 4 4 8
1 8 4 3 1 0 3 2 1 5 6 3 9 0 8 6 5 7 6 0
6 8 3 2 5 8 4 6 1 7 0 7 7 1 3 0 4 2 7 1
0 4 7 7 1 9 7 7 8 2 6 8 9 7 4 3 2 0 8 5
8 8 0 2 4 5 9 0 7 8 4 6 6 2 4 2 3 6 1 4
~~~~

nl.zeesoft.zdk.test.impl.TestZStringEncoder
-------------------------------------------
This test shows how to use the *ZStringEncoder* to generate a key and then use that to encode and decode a text.

**Example implementation**  
~~~~
// Create the ZStringEncoder
ZStringEncoder encoder = new ZStringEncoder("Example text to be encoded.");
// Generate a key
String key = encoder.generateNewKey(1024);
// Use the key to encode the text
encoder.encodeKey(key,0);
// Use the key to decode the encoded text
encoder.decodeKey(key,0);
~~~~

This encoding mechanism can be used to encode and decode passwords and other sensitive data.
The minimum key length is 64. Longer keys provide stronger encoding.

Class references;  
 * [TestZStringEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestZStringEncoder.java)
 * [ZStringEncoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/ZStringEncoder.java)

**Test output**  
The output of this test shows the generated key, the input text, the encoded text, and the decoded text.
~~~~
Key: 2863759310744269748929692629126342883754106323071739176291966692
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).
Encoded text: xgplKjJhJksiQkhhSfheBk7hahOg4ikmIkthEkplmhym2iwlLg#jehPlGf2gQjsgdhUf7kmlUhwjQi8h:fbdVjphLg4fkfhk6fzkOhWl0f#jOj0gNldgwmjjPirjAeQdTftk0iMhIkni0l6fSfvfIk7g9hXghkymtj#iLlblYfsmfkelHfsjcgLl6eZgNjzhchXgrl0jHgHj0
Decoded text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).
~~~~

nl.zeesoft.zdk.test.impl.TestZStringSymbolParser
------------------------------------------------
This test shows how to use the *ZStringSymbolParser* to parse symbols (words and punctuation) from a certain text.

**Example implementation**  
~~~~
// Create the ZStringSymbolParser
ZStringSymbolParser parser = new ZStringSymbolParser("Example text.");
// Parse the string
List<String> symbols = parser.toSymbolsPunctuated();
~~~~

Class references;  
 * [TestZStringSymbolParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestZStringSymbolParser.java)
 * [ZStringSymbolParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/ZStringSymbolParser.java)

**Test output**  
The output of this test shows the input text and the parsed symbols separated by spaces.
~~~~
Input text: Hello, my name is 'Dyz Lecticus'. How are you feeling today? :-) (Don't you know how to: [re;spond]!).
Parsed symbols: Hello , my name is ' Dyz Lecticus ' . How are you feeling today ? :-) ( Don't you know how to : [re;spond] ! ) .
~~~~

nl.zeesoft.zdk.test.impl.TestJson
---------------------------------
This test shows how to create a *JsFile* instance from a JSON string.

**Example implementation**  
~~~~
// Create JSON object
JsFile json = new JsFile();
// Parse JSON from string
json.fromStringBuilder(new ZStringBuilder("{\"command\":\"doStuff\"}"));
~~~~

Class references;  
 * [TestJson](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestJson.java)
 * [JsFile](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/json/JsFile.java)

**Test output**  
The output of this test shows the string input and the resulting JSON structure.  
~~~~
Input:
{
    "qwerValue" : "qwerqwer",
    "qwerObject1" : {"qwerName":"name1" ,"qwerNumber": 1234},
    "qwerObject2" : {
        "qwerName": "name2",
        "qwerNumber": 12345,
        "qwerArray": [],
        "qwerSubObject1": {qwerqwer:"qwer qwer1",qwertqwert:"qwert qwert1"},
        "qwerSubObject2": {qwerqwer:"qwer qwer2",qwertqwert:"qwert qwert2"},
        "qwerObjectArray": [{asdfasdf:"asdf","qwer":["qwerqwer","qwerqwerqwer","qwerqwerqwerqwer"]},{asdf:"asdfasdf"}]
    }
}

JSON structure:
{
  "qwerValue": "qwerqwer",
  "qwerObject1": {
    "qwerName": "name1",
    "qwerNumber": 1234
  },
  "qwerObject2": {
    "qwerName": "name2",
    "qwerNumber": 12345,
    "qwerArray": [],
    "qwerSubObject1": {
      "qwerqwer": "qwer qwer1",
      "qwertqwert": "qwert qwert1"
    },
    "qwerSubObject2": {
      "qwerqwer": "qwer qwer2",
      "qwertqwert": "qwert qwert2"
    },
    "qwerObjectArray": [
      {
        "asdfasdf": "asdf",
        "qwer": [
          "qwerqwer",
          "qwerqwerqwer",
          "qwerqwerqwerqwer"
        ]
      },
      {
        "asdf": "asdfasdf"
      }
    ]
  }
}
~~~~

nl.zeesoft.zdk.test.impl.TestMessenger
--------------------------------------
This test reflects a default implementation of the *Messenger* combined with the *WorkerUnion*.

**Example implementation**  
~~~~
// Create a factory
ZDKFactory factory = new ZDKFactory();
// Get the messenger from the factory
Messenger messenger = factory.getMessenger();
// Add a debug message
messenger.debug(this,"Example debug message");
// Add a warning message
messenger.warn(this,"Example warning message");
// Enable debug message printing
messenger.setPrintDebugMessages(true);
// Start the messenger
messenger.start();
// Add an error message
messenger.error(this,"Example error message");
// Stop the messenger
messenger.stop();
// Ensure all application workers are stopped
factory.getWorkerUnion(messenger).stopWorkers();
// Trigger the messenger to print the remaining messages
messenger.whileWorking();
~~~~

The *Messenger* can be used to log debug, warning and error messages and print them to the standard and/or error out.
It implements the *Worker* to minimize wait time impact for threads that call the *Messenger*.
The *Messenger* is thread safe so it can be shared across the entire application.
Classes that implement the *MessengerListener* interface can subscribe to *Messenger* message printing events.
The *WorkerUnion* can be used to ensure all workers that have been started are stopped when stopping the application.
It will log an error if it fails to stop a worker.

This test uses the *MockMessenger*.

Class references;  
 * [TestMessenger](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestMessenger.java)
 * [MockMessenger](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/MockMessenger.java)
 * [Messenger](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/messenger/Messenger.java)
 * [MessengerListener](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/messenger/MessengerListener.java)
 * [Worker](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/thread/Worker.java)
 * [WorkerUnion](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/thread/WorkerUnion.java)

**Test output**  
The output of this test shows the standard output of the test log messages.
~~~~
2018-08-15 20:20:16:305 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2018-08-15 20:20:16:607 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2018-08-15 20:20:16:608 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log exception stack trace
java.lang.NumberFormatException: For input string: "A"
	at java.lang.NumberFormatException.forInputString(NumberFormatException.java:65)
	at java.lang.Integer.parseInt(Integer.java:580)
	at java.lang.Integer.parseInt(Integer.java:615)
	at nl.zeesoft.zdk.test.impl.TestMessenger.test(TestMessenger.java:83)
	at nl.zeesoft.zdk.test.Tester.test(Tester.java:69)
	at nl.zeesoft.zdk.test.LibraryObject.describeAndTest(LibraryObject.java:39)
	at nl.zeesoft.zdk.test.impl.ZDK.main(ZDK.java:39)

2018-08-15 20:20:16:918 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
~~~~

Test results
------------
All 5 tests have been executed successfully (117 assertions).  
Total test duration: 824 ms (total sleep duration: 600 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestZIntegerGenerator: 454 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringEncoder: 400 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestZStringSymbolParser: 395 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestJson: 413 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 674 Kb / 0 Mb
