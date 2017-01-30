Zeesoft Development Kit
=======================
The Zeesoft Development Kit (ZDK) is an open source library for Java application development.

It provides support for;
 * [Advanced encoding and decoding](#nlzeesoftzdktestimpltestencoderdecoder).
 * [Extended StringBuilder manipulation](#nlzeesoftzdktestimpltestsymbolparser).
 * [Multi threading](#nlzeesoftzdktestimpltestmessenger).
 * [Application message handling](#nlzeesoftzdktestimpltestmessenger).

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDK/releases/zdk-0.9.12.zip) to download the latest ZDK release (version 0.9.12).  
All ZDK releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZDK/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZDK](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/ZDK.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zdk.test.impl.TestEncoderDecoder
-------------------------------------------
This test shows how to use the *EncoderDecoder* class to generate a key and use that to encode and decode a text.

**Example implementation**  
~~~~
// Generate a key
String key = EncoderDecoder.generateNewKey(1024);
// Use the key to encode a text
StringBuilder encodedText = EncoderDecoder.encodeKey(new StringBuilder("Example text to be encoded."),key,0);
// Use the key to decode an encoded text
StringBuilder decodedText = EncoderDecoder.decodeKey(encodedText,key,0);
~~~~

This encoding mechanism can be used to encode and decode passwords and other sensitive data.
The minimum key length is 64. Longer keys provide stronger encoding.

Class references;  
 * [TestEncoderDecoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestEncoderDecoder.java)
 * [EncoderDecoder](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/EncoderDecoder.java)

**Test output**  
The output of this test shows the generated key, the input text, the encoded text, and the decoded text.
~~~~
Key: 7875793494022926075908257971793326585167266399305212652360302394
Input text: Hello, my name is Dyz Lecticus. How are you feeling today?
Encoded text: OLWM0LLKuLyLPH0JKNgHyFUH~HmMaGWK1GZKgJxMbGwKNHEKkLUNML#G#LAMVHEHNHsKZKGL9JGHpKJK:HuKoKeHRNoM#HIGiJZHeGcGuKpJ2HVIaKRF0
Decoded text: Hello, my name is Dyz Lecticus. How are you feeling today?
~~~~

nl.zeesoft.zdk.test.impl.TestSymbolParser
-----------------------------------------
This test shows how to use the *SymbolParser* class to parse symbols (words and punctuation) from a certain text.

**Example implementation**  
~~~~
List<String> symbols = SymbolParser.parseSymbolsFromText(new StringBuilder("Example text."));
~~~~

Class references;  
 * [TestSymbolParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestSymbolParser.java)
 * [SymbolParser](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/SymbolParser.java)
 * [Generic](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/Generic.java)

**Test output**  
The output of this test shows the input text and the parsed symbols separated by spaces.
~~~~
Input text: Hello, my name is Dyz Lecticus. How are you feeling today?
Parsed symbols: Hello , my name is Dyz Lecticus . How are you feeling today ?
~~~~

nl.zeesoft.zdk.test.impl.TestMessenger
--------------------------------------
This test reflects a default implementation of the *Messenger* singleton combined with the *WorkerUnion* singleton.

**Example implementation**  
~~~~
// Add a debug message
Messenger.getInstance().debug(this,"Example debug message");
// Add a warning message
Messenger.getInstance().warn(this,"Example warning message");
// Enable debug message printing
Messenger.getInstance().setPrintDebugMessages(true);
// Start the messenger
Messenger.getInstance().start();
// Add an error message
Messenger.getInstance().error(this,"Example error message");
// Stop the messenger
Messenger.getInstance().stop();
// Ensure all application workers are stopped
WorkerUnion.getInstance().stopWorkers();
// Trigger the messenger to print the remaining messages
Messenger.getInstance().whileWorking();
~~~~

The *Messenger* can be used to log debug, warning and error messages and print them to the standard and/or error out.
It is implemented as a thread safe singleton to allow easy application wide access.
It implements the *Worker* class to minimize wait time impact for threads that call the *Messenger*.
Classes that implement the *MessengerListener* interface can subscribe to *Messenger* message printing events.
The *WorkerUnion* can be used to ensure all workers that have been started are stopped when stopping the application.
It will log an error if it fails to stop a worker.

Class references;  
 * [TestMessenger](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/test/impl/TestMessenger.java)
 * [Messenger](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/messenger/Messenger.java)
 * [MessengerListener](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/messenger/MessengerListener.java)
 * [Worker](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/thread/Worker.java)
 * [WorkerUnion](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZDK/src/nl/zeesoft/zdk/thread/WorkerUnion.java)

**Test output**  
The output of this test shows the standard (and error) output of the test log messages.
~~~~
2017-01-30 21:32:37:029 DBG nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log debug message before Messenger has started
2017-01-30 21:32:37:329 ERR nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log error message while Messenger is working
2017-01-30 21:32:37:641 WRN nl.zeesoft.zdk.test.impl.TestMessengerListener: Test log warning message after Messenger has stopped
~~~~

Test results
------------
All 3 tests have been executed successfully (7 assertions).  
Total test duration: 732 ms (total sleep duration: 600 ms).  

Memory usage per test;  
 * nl.zeesoft.zdk.test.impl.TestEncoderDecoder: 244 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestSymbolParser: 251 Kb / 0 Mb
 * nl.zeesoft.zdk.test.impl.TestMessenger: 302 Kb / 0 Mb
