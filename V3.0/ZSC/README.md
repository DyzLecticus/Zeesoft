Zeesoft Symbolic Confabulation
==============================
Zeesoft Symbolic Confabulation (ZSC) is an open source library for Java application development.
It provides support for confabulation; the process of learning, generating and forgetting context sensitive symbolic sequences.
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSC/releases/zsc-0.9.4.zip) to download the latest ZSC release (version 0.9.4).  
All ZSC releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSC/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSC](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/ZSC.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zsc.test.TestConfabulatorTraining
--------------------------------------------
This test shows how to create and train a *Confabulator* instance.

**Example implementation**  
~~~~
// Create confabulator
Confabulator confabulator = new Confabulator();
// Train confabulator
confabulator.learnSequence("Example symbol sequence.","Optional Example Context Symbols");
~~~~

A *Confabulator* can learn symbol sequences (i.e. sentences), optionally combined with certain context symbols (i.e. subject(s)).
When trained, a *Confabulator* can be used to;
 * Confabulate one or more context symbols for a certain input sequence.
 * Confabulate a correction for a certain input sequence, optionally restricted by one or more context symbols.
 * Confabulate a starter sequence or an extension for an input sequence, optionally restricted by one or more context symbols.

By default, confabulators limit their maximum link distance to 8 and their maximum link count to 1000.
Deviations from these defaults can be specified using the *Confabulator* initialization method.
When the link count of one of the links hits the specified count maximum, all *Confabulator* link counts are divided by 2.
Links that have a count of 1 are removed by this division process.
When repeatedly confronted with a slowly changing training set, this mechanism allows the *Confabulator* to slowly forget links that are no longer part of the training set.

This test uses the *MockConfabulator*.  
The training set used to train this *MockConfabulator* consists of the following sequence and context combinations;  
 * *'What are you? I am an artificial cognition.' - 'I Self Am Artificial Cognition'*
 * *'What is cognition? Cognition refers to mental processes within the brain.' - 'Cognition'*
 * *'What is your name? My name is Dyz Lecticus.' - 'I Self My Name'*
 * *'What is your goal? My goal is to model reality through interactions with people.' - 'I Self My Goal'*
 * *'Who created you? I was created by Andre van der Zee.' - 'I Self My Creator'*

Class references;  
 * [TestConfabulatorTraining](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/TestConfabulatorTraining.java)
 * [MockConfabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/MockConfabulator.java)
 * [Confabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/confabulator/Confabulator.java)

**Test output**  
The output of this test shows a brief summary of the link data that is created based on the training set.
Please note the relatively large amount of links compared to the size of the training set.
~~~~
Link: 1, from: 'What', to: 'are', distance: 1, count: 2, context: 'I'
Link: 2, from: 'What', to: 'are', distance: 1, count: 2, context: 'Self'
Link: 3, from: 'What', to: 'are', distance: 1, count: 2, context: 'Am'
Link: 4, from: 'What', to: 'are', distance: 1, count: 2, context: 'Artificial'
Link: 5, from: 'What', to: 'are', distance: 1, count: 2, context: 'Cognition'
Link: 6, from: 'What', to: 'you', distance: 2, count: 2, context: 'I'
Link: 7, from: 'What', to: 'you', distance: 2, count: 2, context: 'Self'
Link: 8, from: 'What', to: 'you', distance: 2, count: 2, context: 'Am'
Link: 9, from: 'What', to: 'you', distance: 2, count: 2, context: 'Artificial'
Link: 10, from: 'What', to: 'you', distance: 2, count: 2, context: 'Cognition'

[ ... ]

Link: 1073, from: 'der', to: 'Zee', distance: 1, count: 2, context: 'My'
Link: 1074, from: 'der', to: 'Zee', distance: 1, count: 2, context: 'Creator'
Link: 1075, from: 'der', to: '.', distance: 2, count: 2, context: 'I'
Link: 1076, from: 'der', to: '.', distance: 2, count: 2, context: 'Self'
Link: 1077, from: 'der', to: '.', distance: 2, count: 2, context: 'My'
Link: 1078, from: 'der', to: '.', distance: 2, count: 2, context: 'Creator'
Link: 1079, from: 'Zee', to: '.', distance: 1, count: 2, context: 'I'
Link: 1080, from: 'Zee', to: '.', distance: 1, count: 2, context: 'Self'
Link: 1081, from: 'Zee', to: '.', distance: 1, count: 2, context: 'My'
Link: 1082, from: 'Zee', to: '.', distance: 1, count: 2, context: 'Creator'
Total Links: 1082 (47 ms)
~~~~

nl.zeesoft.zsc.test.TestConfabulatorContextConfabulation
--------------------------------------------------------
This test shows how to use a trained *Confabulator* instance to confabulate one or more context symbols for a certain input sequence.

**Example implementation**  
~~~~
// Create confabulator
Confabulator confabulator = new Confabulator();
// Train confabulator
confabulator.learnSequence("Example symbol sequence.","Optional Context Symbols");
// Create ContextConfabulation
ContextConfabulation confabulation = new ContextConfabulation("Example");
// Confabulate
confabulator.confabulate(confabulation);
~~~~

Class references;  
 * [TestConfabulatorContextConfabulation](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/TestConfabulatorContextConfabulation.java)
 * [MockConfabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/MockConfabulator.java)
 * [Confabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/confabulator/Confabulator.java)
 * [ContextConfabulation](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/confabulator/confabulations/ContextConfabulation.java)

**Test output**  
The output of this test shows the confabulation input sequence, log summary, and output.  
Please note how the context confabulation favours the most significant context symbol over the stronger associated context symbols in the *MockConfabulator*.  
~~~~
Confabulation input sequence: What is your name?
2017-02-04 21:15:28:713: Confabulated winning context symbol: Name
Module 00:  Name (786) My (564) Self (462) I (462) Goal (342) ... [1]
Confabulation output: Name (47 ms)
~~~~

nl.zeesoft.zsc.test.TestConfabulatorCorrectionConfabulation
-----------------------------------------------------------
This test shows how to use a trained *Confabulator* instance to confabulate a correction for a certain input sequence, optionally restricted by one or more context symbols.

**Example implementation**  
~~~~
// Create confabulator
Confabulator confabulator = new Confabulator();
// Train confabulator
confabulator.learnSequence("Example symbol sequence.","Optional Context Symbols");
// Create CorrectionConfabulation
CorrectionConfabulation confabulation = new CorrectionConfabulation("Example symbol bla.","Optional Context");
// Confabulate
confabulator.confabulate(confabulation);
~~~~

Class references;  
 * [TestConfabulatorCorrectionConfabulation](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/TestConfabulatorCorrectionConfabulation.java)
 * [MockConfabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/MockConfabulator.java)
 * [Confabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/confabulator/Confabulator.java)
 * [CorrectionConfabulation](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/confabulator/confabulations/CorrectionConfabulation.java)

**Test output**  
The output of this test shows the confabulation input sequence, log summary, and output for several confabulations;  
 * One confabulation without context.  
 * One confabulation with context.  

Please note how the modules are used in the first confabulation to search through all possible symbol combinations.
~~~~
Confabulation input sequence: What is your bla?
2017-02-04 21:15:28:802: Confabulated replacement symbol: name, for: bla
Module 5:  What (1)
Module 6:  is (1)
Module 7:  your (1)
Module 8:  name (3520) goal (3000)
Module 9:  ? (1)
Module 10:  My (4324) I (1010) Cognition (150)
Module 11:  name (3048) goal (3048) am (1350) was (1104) refers (298)
Module 12:  is (5629) an (1965) created (1596) ? (936) to (378)
Module 13:  Dyz (4160) to (4032) artificial (2560) by (2128) My (1879) ... [7]
Module 14:  model (4160) Lecticus (4160) cognition (3075) Andre (2600) name (2540) ... [9]
Module 15:  reality (4160) is (3394) . (3220) van (3120) I (2540) ... [9]
Module 16:  through (4160) der (3640) My (3509) Dyz (2600) artificial (2560) ... [17]
Confabulation output: What is your name ? (26 ms)

Confabulation input sequence: What is your bla? (context: Goal)
2017-02-04 21:15:28:804: Confabulated replacement symbol: goal, for: bla
Module 5:  What (1)
Module 6:  is (1)
Module 7:  your (1)
Module 8:  goal (381)
Module 9:  ? (1)
Confabulation output: What is your goal ? (2 ms)
~~~~

nl.zeesoft.zsc.test.TestConfabulatorExtensionConfabulation
----------------------------------------------------------
This test shows how to use a trained *Confabulator* instance to confabulate a starter sequence or an extension for an input sequence, optionally restricted by one or more context symbols.

**Example implementation**  
~~~~
// Create confabulator
Confabulator confabulator = new Confabulator();
// Train confabulator
confabulator.learnSequence("Example symbol sequence.","Optional Context Symbols");
// Create ExtensionConfabulation
ExtensionConfabulation confabulation = new ExtensionConfabulation("Optional sequence symbols","Optional Context");
// Confabulate
confabulator.confabulate(confabulation);
~~~~

Class references;  
 * [TestConfabulatorExtensionConfabulation](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/TestConfabulatorExtensionConfabulation.java)
 * [MockConfabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/test/MockConfabulator.java)
 * [Confabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/confabulator/Confabulator.java)
 * [ExtensionConfabulation](https://github.com/DyzLecticus/Zeesoft/blob/master/V3.0/ZSC/src/nl/zeesoft/zsc/confabulator/confabulations/ExtensionConfabulation.java)

**Test output**  
The output of this test shows the confabulation input sequence, log summary, and output for several confabulations;  
 * One confabulation without an input sequence.  
 * One confabulation without context.  
 * Two confabulations with context.  

Please note how the confabulated extensions depend on the (lack of) context.
~~~~
Confabulation input sequence: 
2017-02-04 21:15:28:822: Confabulated first symbol: What
Module 8:  What (13248) Who (4216)
2017-02-04 21:15:28:823: Confabulated next symbol: is
Module 7:  What (1)
Module 8:  is (864) are (745)
2017-02-04 21:15:28:824: Confabulated next symbol: your
Module 6:  What (1)
Module 7:  is (1)
Module 8:  your (1750) Dyz (520) to (504) cognition (246)
2017-02-04 21:15:28:856: Confabulated next symbol: name
Module 5:  What (1)
Module 6:  is (1)
Module 7:  your (1)
Module 8:  name (3520) goal (3000)
Module 9:  ? (3141) is (952)
Module 10:  My (4324) Dyz (1040) I (1010) to (1008) your (875) ... [2]
Module 11:  name (4064) goal (4064) model (1560) Lecticus (1560) am (1350) ... [5]
Module 12:  is (5629) ? (2406) reality (2080) an (1965) created (1596) ... [6]
Module 13:  Dyz (4160) to (4032) My (3509) through (2600) artificial (2560) ... [14]
Module 14:  model (4160) Lecticus (4160) name (3556) goal (3556) interactions (3120) ... [15]
Module 15:  is (4884) reality (4160) with (3640) . (3220) van (3120) ... [19]
Module 16:  through (4160) people (4160) der (3640) Dyz (3640) to (3528) ... [24]
2017-02-04 21:15:28:857: Confabulated next symbol: ?
Module 4:  What (1)
Module 5:  is (1)
Module 6:  your (1)
Module 7:  name (1)
Module 8:  ? (2673) is (476)
Confabulation output: What is your name ? (37 ms)

Confabulation input sequence: What is artificial cognition?
2017-02-04 21:15:28:879: Confabulated next symbol: My
Module 3:  What (1)
Module 4:  is (1)
Module 5:  artificial (1)
Module 6:  cognition (1)
Module 7:  ? (1)
Module 8:  My (2445) I (1010) Cognition (300)
2017-02-04 21:15:28:908: Confabulated next symbol: name
Module 2:  What (1)
Module 3:  is (1)
Module 4:  artificial (1)
Module 5:  cognition (1)
Module 6:  ? (1)
Module 7:  My (1)
Module 8:  name (3028) goal (2508)
Module 9:  is (3932) ? (936)
Module 10:  Dyz (3120) to (3024) My (1879) I (1010) your (875) ... [2]
Module 11:  model (3120) Lecticus (3120) name (2540) goal (2540) am (1350) ... [5]
Module 12:  is (3394) reality (3120) ? (2406) an (1965) created (1596) ... [6]
Module 13:  through (3640) My (3509) Dyz (2600) artificial (2560) to (2520) ... [14]
Module 14:  interactions (4160) name (3556) goal (3556) model (3120) Lecticus (3120) ... [15]
Module 15:  is (4884) with (4160) reality (3640) van (3120) . (3012) ... [19]
Module 16:  through (4160) people (4160) der (3640) Dyz (3640) to (3528) ... [24]
2017-02-04 21:15:28:909: Confabulated next symbol: is
Module 1:  What (1)
Module 2:  is (1)
Module 3:  artificial (1)
Module 4:  cognition (1)
Module 5:  ? (1)
Module 6:  My (1)
Module 7:  name (1)
Module 8:  is (3456) ? (468)
2017-02-04 21:15:28:910: Confabulated next symbol: Dyz
Module 0:  What (1)
Module 1:  is (1)
Module 2:  artificial (1)
Module 3:  cognition (1)
Module 4:  ? (1)
Module 5:  My (1)
Module 6:  name (1)
Module 7:  is (1)
Module 8:  Dyz (3120) to (2520) your (875) cognition (738)
2017-02-04 21:15:28:911: Confabulated next symbol: Lecticus
Module 0:  is (1)
Module 1:  artificial (1)
Module 2:  cognition (1)
Module 3:  ? (1)
Module 4:  My (1)
Module 5:  name (1)
Module 6:  is (1)
Module 7:  Dyz (1)
Module 8:  Lecticus (3120)
2017-02-04 21:15:28:912: Confabulated next symbol: .
Module 0:  artificial (1)
Module 1:  cognition (1)
Module 2:  ? (1)
Module 3:  My (1)
Module 4:  name (1)
Module 5:  is (1)
Module 6:  Dyz (1)
Module 7:  Lecticus (1)
Module 8:  . (1504)
2017-02-04 21:15:28:913: Failed to confabulate next symbol
Module 0:  cognition (1)
Module 1:  ? (1)
Module 2:  My (1)
Module 3:  name (1)
Module 4:  is (1)
Module 5:  Dyz (1)
Module 6:  Lecticus (1)
Module 7:  . (1)
Confabulation output: My name is Dyz Lecticus . (36 ms)

Confabulation input sequence: What is artificial cognition? (context: Cognition)
2017-02-04 21:15:28:915: Confabulated next symbol: Cognition
Module 3:  What (1)
Module 4:  is (1)
Module 5:  artificial (1)
Module 6:  cognition (1)
Module 7:  ? (1)
Module 8:  Cognition (300) I (130)
2017-02-04 21:15:28:916: Confabulated next symbol: refers
Module 2:  What (1)
Module 3:  is (1)
Module 4:  artificial (1)
Module 5:  cognition (1)
Module 6:  ? (1)
Module 7:  Cognition (1)
Module 8:  refers (447)
2017-02-04 21:15:28:917: Confabulated next symbol: to
Module 1:  What (1)
Module 2:  is (1)
Module 3:  artificial (1)
Module 4:  cognition (1)
Module 5:  ? (1)
Module 6:  Cognition (1)
Module 7:  refers (1)
Module 8:  to (504)
2017-02-04 21:15:28:918: Confabulated next symbol: mental
Module 0:  What (1)
Module 1:  is (1)
Module 2:  artificial (1)
Module 3:  cognition (1)
Module 4:  ? (1)
Module 5:  Cognition (1)
Module 6:  refers (1)
Module 7:  to (1)
Module 8:  mental (740)
2017-02-04 21:15:28:919: Confabulated next symbol: processes
Module 0:  is (1)
Module 1:  artificial (1)
Module 2:  cognition (1)
Module 3:  ? (1)
Module 4:  Cognition (1)
Module 5:  refers (1)
Module 6:  to (1)
Module 7:  mental (1)
Module 8:  processes (882)
2017-02-04 21:15:28:921: Confabulated next symbol: within
Module 0:  artificial (1)
Module 1:  cognition (1)
Module 2:  ? (1)
Module 3:  Cognition (1)
Module 4:  refers (1)
Module 5:  to (1)
Module 6:  mental (1)
Module 7:  processes (1)
Module 8:  within (1029)
2017-02-04 21:15:28:923: Confabulated next symbol: the
Module 0:  cognition (1)
Module 1:  ? (1)
Module 2:  Cognition (1)
Module 3:  refers (1)
Module 4:  to (1)
Module 5:  mental (1)
Module 6:  processes (1)
Module 7:  within (1)
Module 8:  the (1176)
2017-02-04 21:15:28:924: Confabulated next symbol: brain
Module 0:  ? (1)
Module 1:  Cognition (1)
Module 2:  refers (1)
Module 3:  to (1)
Module 4:  mental (1)
Module 5:  processes (1)
Module 6:  within (1)
Module 7:  the (1)
Module 8:  brain (1176)
2017-02-04 21:15:28:925: Confabulated next symbol: .
Module 0:  Cognition (1)
Module 1:  refers (1)
Module 2:  to (1)
Module 3:  mental (1)
Module 4:  processes (1)
Module 5:  within (1)
Module 6:  the (1)
Module 7:  brain (1)
Module 8:  . (416)
2017-02-04 21:15:28:926: Failed to confabulate next symbol
Module 0:  refers (1)
Module 1:  to (1)
Module 2:  mental (1)
Module 3:  processes (1)
Module 4:  within (1)
Module 5:  the (1)
Module 6:  brain (1)
Module 7:  . (1)
Confabulation output: Cognition refers to mental processes within the brain . (12 ms)

Confabulation input sequence: What is artificial cognition? (context: Artificial)
2017-02-04 21:15:28:981: Confabulated next symbol: I
Module 3:  What (1)
Module 4:  is (1)
Module 5:  artificial (1)
Module 6:  cognition (1)
Module 7:  ? (1)
Module 8:  I (130)
2017-02-04 21:15:28:982: Confabulated next symbol: am
Module 2:  What (1)
Module 3:  is (1)
Module 4:  artificial (1)
Module 5:  cognition (1)
Module 6:  ? (1)
Module 7:  I (1)
Module 8:  am (270)
2017-02-04 21:15:28:984: Confabulated next symbol: an
Module 1:  What (1)
Module 2:  is (1)
Module 3:  artificial (1)
Module 4:  cognition (1)
Module 5:  ? (1)
Module 6:  I (1)
Module 7:  am (1)
Module 8:  an (393)
2017-02-04 21:15:28:985: Confabulated next symbol: artificial
Module 0:  What (1)
Module 1:  is (1)
Module 2:  artificial (1)
Module 3:  cognition (1)
Module 4:  ? (1)
Module 5:  I (1)
Module 6:  am (1)
Module 7:  an (1)
Module 8:  artificial (512)
2017-02-04 21:15:28:987: Confabulated next symbol: cognition
Module 0:  is (1)
Module 1:  artificial (1)
Module 2:  cognition (1)
Module 3:  ? (1)
Module 4:  I (1)
Module 5:  am (1)
Module 6:  an (1)
Module 7:  artificial (1)
Module 8:  cognition (615)
2017-02-04 21:15:28:989: Confabulated next symbol: .
Module 0:  artificial (1)
Module 1:  cognition (1)
Module 2:  ? (1)
Module 3:  I (1)
Module 4:  am (1)
Module 5:  an (1)
Module 6:  artificial (1)
Module 7:  cognition (1)
Module 8:  . (312)
2017-02-04 21:15:28:989: Failed to confabulate next symbol
Module 0:  cognition (1)
Module 1:  ? (1)
Module 2:  I (1)
Module 3:  am (1)
Module 4:  an (1)
Module 5:  artificial (1)
Module 6:  cognition (1)
Module 7:  . (1)
Confabulation output: I am an artificial cognition . (10 ms)
~~~~

Test results
------------
All 4 tests have been executed successfully (12 assertions).  
Total test duration: 400 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zsc.test.TestConfabulatorTraining: 349 Kb / 0 Mb
 * nl.zeesoft.zsc.test.TestConfabulatorContextConfabulation: 388 Kb / 0 Mb
 * nl.zeesoft.zsc.test.TestConfabulatorCorrectionConfabulation: 394 Kb / 0 Mb
 * nl.zeesoft.zsc.test.TestConfabulatorExtensionConfabulation: 270 Kb / 0 Mb
