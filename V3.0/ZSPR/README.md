Zeesoft Symbolic Pattern Recognition
====================================
Zeesoft Symbolic Pattern Recognition (ZSPR) is an open source library for Java application development.
It provides support for sequential symbolic pattern recognition.
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSPR/releases/zspr-1.0.0.zip) to download the latest ZSPR release (version 1.0.0).  
All ZSPR releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSPR/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSPR](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/src/nl/zeesoft/zspr/test/ZSPR.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

Symbolic Pattern Recognition
----------------------------
When parsing symbolic sequences to discern meaning, certain patterns may be easily translated into primary objects like numbers, dates and duration.
This library provides an extendable, thread safe [PatternManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/src/nl/zeesoft/zspr/pattern/PatternManager.java) to do just that.
Please note that initializing the default *PatternManager* might take a few seconds and that it requires quite a lot of memory.

nl.zeesoft.zspr.test.TestPatternManager
---------------------------------------
This test shows how to create and initialzie a *PatternManager* instance and then use it to find and translate some patterns.

**Example implementation**  
~~~~
// Create pattern manager
PatternManager manager = new PatternManager();
// Initialize patterns
manager.initializePatterns();
// Get patterns for string
List<PatternObject> patterns = manager.getMatchingPatternsForString("one hour and fourtyfive minutes");
// Translate string to pattern value
String value = patterns.get(0).getValueForString("one hour and fourtyfive minutes");
// Translate value back to string
String str = patterns.get(0).getStringForValue(value);
~~~~

This test uses the *MockPatternManager*. It is used to instantiate a shared *PatternManager* for tests.

Class references;  
 * [TestPatternManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/src/nl/zeesoft/zspr/test/TestPatternManager.java)
 * [MockPatternManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/src/nl/zeesoft/zspr/test/MockPatternManager.java)
 * [PatternManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/src/nl/zeesoft/zspr/pattern/PatternManager.java)

**Test output**  
The output of this test shows some test strings and their corresponding pattern values.  
Please note how some test strings trigger multiple patterns that translate to different value types.  
~~~~
Initializing pattern manager took 378 ms

==> Test English order
first = ORDER_ENG:1 (1)
first = ALPHABETIC_UNI:first (2)
fiftythird = ORDER_ENG:53 (1)
fiftythird = ALPHABETIC_UNI:fiftythird (2)
threehundredandsixtyninth = ORDER_ENG:369 (1)
threehundredandsixtyninth = ALPHABETIC_UNI:threehundredandsixtyninth (2)

==> Test Dutch order
eerste = ORDER_NLD:1 (1)
eerste = ALPHABETIC_UNI:eerste (2)
drieenvijftigste = ORDER_NLD:53 (1)
drieenvijftigste = ALPHABETIC_UNI:drieenvijftigste (2)
driehonderdzesennegentigste = ORDER_NLD:396 (1)
driehonderdzesennegentigste = ALPHABETIC_UNI:driehonderdzesennegentigste (2)

==> Test English time
12 o'clock = TIME_ENG:12:00:00
twelve o'clock = TIME_ENG:12:00:00
twelve oclock = TIME_ENG:12:00:00
twelve fifteen = TIME_ENG:12:15:00
fifteen past twelve = TIME_ENG:12:15:00
fifteen minutes past twelve = TIME_ENG:12:15:00
a quarter past twelve = TIME_ENG:12:15:00
one minute past one = TIME_ENG:13:01:00
one minute to twelve = TIME_ENG:11:59:00
twelve thirty = TIME_ENG:12:30:00
half past twelve = TIME_ENG:12:30:00
twelve fourtyfive = TIME_ENG:12:45:00

==> Test Dutch time
12 uur = TIME_NLD:12:00:00 (1)
12 uur = DURATION_NLD:12:00 (2)
twaalf uur = TIME_NLD:12:00:00 (1)
twaalf uur = DURATION_NLD:12:00 (2)
vijftien over twaalf = TIME_NLD:12:15:00
vijftien minuten over twaalf = TIME_NLD:12:15:00
kwart over twaalf = TIME_NLD:12:15:00
een minuut over een = TIME_NLD:13:01:00
een minuut voor twaalf = TIME_NLD:11:59:00
twaalf uur dertig = TIME_NLD:12:30:00
half een = TIME_NLD:12:30:00
twaalf uur vijfenveertig = TIME_NLD:12:45:00

==> Test English number
onehundredandeightyone = NUMBER_ENG:181 (1)
onehundredandeightyone = ALPHABETIC_UNI:onehundredandeightyone (2)

==> Test Dutch number
driehonderdzevenenzestig = NUMBER_NLD:367 (1)
driehonderdzevenenzestig = ALPHABETIC_UNI:driehonderdzevenenzestig (2)

==> Test Universal time
12:14 = TIME_UNI:12:14:00

==> Test Universal number
5348 = NUMBER_UNI:5348

==> Test English date
december twentysecond = DATE_ENG:2017-12-22
january fifth = DATE_ENG:2017-01-05
the fifth of january = DATE_ENG:2017-01-05
the 12th of august = DATE_ENG:2017-08-12
october 2nd = DATE_ENG:2017-10-02
now = DATE_ENG:2017-01-01 (1)
now = TIME_ENG:19:00:00 (2)
now = ALPHABETIC_UNI:now (3)
today = DATE_ENG:2017-01-01 (1)
today = ALPHABETIC_UNI:today (2)
tomorrow = DATE_ENG:2017-01-02 (1)
tomorrow = ALPHABETIC_UNI:tomorrow (2)

==> Test Dutch date
tweeentwintig december = DATE_NLD:2017-12-22
vijf januari = DATE_NLD:2017-01-05
nu = DATE_NLD:2017-01-01 (1)
nu = ALPHABETIC_UNI:nu (2)
vandaag = DATE_NLD:2017-01-01 (1)
vandaag = ALPHABETIC_UNI:vandaag (2)
morgen = DATE_NLD:2017-01-02 (1)
morgen = ALPHABETIC_UNI:morgen (2)

==> Test English duration
two hours = DURATION_ENG:02:00
3 hours and 4 minutes = DURATION_ENG:03:04
one hour and fourtyfive minutes = DURATION_ENG:01:45
one hour and thirtythree minutes = DURATION_ENG:01:33

==> Test Dutch duration
twee uur = TIME_NLD:14:00:00 (1)
twee uur = DURATION_NLD:02:00 (2)
3 uur en 4 minuten = DURATION_NLD:03:04
een uur en vijfenveertig minuten = DURATION_NLD:01:45
~~~~

nl.zeesoft.zspr.test.TestPatternManagerTranslate
------------------------------------------------
This test shows how to create and initialzie a *PatternManager* instance and then use it to translate a symbol sequence to its values and back.

**Example implementation**  
~~~~
// Create pattern manager
PatternManager manager = new PatternManager();
// Initialize patterns
manager.initializePatterns();
// Get values for sequence
ZStringSymbolParser values = manager.translateSequence(new ZStringSymbolParser("I walked one hour and fourtyfive minutes"));
// Get sequence for values
ZStringSymbolParser sequence = manager.translateValues(values);
~~~~

This test uses the *MockPatternManager*. It is used to instantiate a shared *PatternManager* for tests.

Class references;  
 * [TestPatternManagerTranslate](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/src/nl/zeesoft/zspr/test/TestPatternManagerTranslate.java)
 * [MockPatternManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/src/nl/zeesoft/zspr/test/MockPatternManager.java)
 * [PatternManager](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSPR/src/nl/zeesoft/zspr/pattern/PatternManager.java)

**Test output**  
The output of this test shows some test sequences with corresponding sequences to values translation and values to sequences translation.  
Please note how the date of the request is inferred automatically by the pattern translation mechanism.  
~~~~
==> Test English
Input: I want to book a room for five people on december twentysecond at twentyfive minutes past four for one hour and thirtythree minutes.
Values: ALPHABETIC_UNI:I ALPHABETIC_UNI:want ALPHABETIC_UNI:to ALPHABETIC_UNI:book ALPHABETIC_UNI:a ALPHABETIC_UNI:room ALPHABETIC_UNI:for NUMBER_ENG:5|ALPHABETIC_UNI:five ALPHABETIC_UNI:people ALPHABETIC_UNI:on DATE_ENG:2017-12-22 ALPHABETIC_UNI:at TIME_ENG:16:25:00 ALPHABETIC_UNI:for DURATION_ENG:01:33 .
String: I want to book a room for five people on december twentysecond twothousandseventeen at twentyfive past four for one hour and thirtythree minutes .

==> Test Dutch
Input: Ik wil een kamer boeken voor vijf personen op dertig december om vijfentwintig minuten over vier voor twee uur.
Values: ALPHABETIC_UNI:Ik ALPHABETIC_UNI:wil NUMBER_NLD:1|ALPHABETIC_UNI:een ALPHABETIC_UNI:kamer ALPHABETIC_UNI:boeken ALPHABETIC_UNI:voor NUMBER_NLD:5|ALPHABETIC_UNI:vijf ALPHABETIC_UNI:personen ALPHABETIC_UNI:op DATE_NLD:2017-12-30 ALPHABETIC_UNI:om TIME_NLD:16:25:00 ALPHABETIC_UNI:voor TIME_NLD:14:00:00|DURATION_NLD:02:00 .
String: Ik wil een kamer boeken voor vijf personen op dertig december tweeduizendzeventien om vier uur vijfentwintig voor twee uur .
~~~~

Test results
------------
All 2 tests have been executed successfully (112 assertions).  
Total test duration: 889 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zspr.test.TestPatternManager: 35647 Kb / 34 Mb
 * nl.zeesoft.zspr.test.TestPatternManagerTranslate: 35655 Kb / 34 Mb
