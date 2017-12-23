Zeesoft Symbolic Confabulation
==============================
Zeesoft Symbolic Confabulation (ZSC) is an open source library for Java application development.
It provides support for confabulation; the process of learning, generating and forgetting context sensitive symbolic sequences.
This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/).  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V3.0/ZSC/releases/zsc-1.0.1.zip) to download the latest ZSC release (version 1.0.1).  
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
Total Links: 1082 (13 ms)
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
2017-12-23 18:45:42:593: Confabulated winning context symbol: Name
Module 00:  Name (1086) My (1014) Self (912) I (912) Goal (492) ... [1]
Confabulation output: Name (22 ms)
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
2017-12-23 18:45:42:629: Confabulated replacement symbol: name, for: bla
Module 5:  What (1)
Module 6:  is (1)
Module 7:  your (1)
Module 8:  name (4920) goal (4200)
Module 9:  ? (1)
Module 10:  My (6324) I (1460) am (925) through (720) Dyz (720) ... [3]
Module 11:  name (4248) goal (4248) am (1850) an (1810) was (1504) ... [6]
Module 12:  is (8429) an (2715) artificial (2670) created (2196) with (2160) ... [7]
Module 13:  Dyz (5760) to (5632) cognition (3633) artificial (3560) by (2928) ... [12]
Module 14:  model (5760) Lecticus (5760) cognition (4325) . (4080) interactions (3600) ... [13]
Module 15:  . (6320) reality (5760) is (4994) with (4320) van (4320) ... [13]
Module 16:  through (5760) My (5109) people (5040) der (5040) with (3600) ... [21]
Confabulation output: What is your name ? (10 ms)

Confabulation input sequence: What is your bla? (context: Goal)
2017-12-23 18:45:42:682: Confabulated replacement symbol: goal, for: bla
Module 5:  What (1)
Module 6:  is (1)
Module 7:  your (1)
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
2017-12-23 18:45:42:693: Confabulated first symbol: What
Module 8:  What (18848) Who (5816)
2017-12-23 18:45:42:693: Confabulated next symbol: is
Module 7:  What (1)
Module 8:  is (1314) are (995)
2017-12-23 18:45:42:694: Confabulated next symbol: your
Module 6:  What (1)
Module 7:  is (1)
Module 8:  your (2550) cognition (346)
2017-12-23 18:45:42:699: Confabulated next symbol: name
Module 5:  What (1)
Module 6:  is (1)
Module 7:  your (1)
Module 8:  name (4920) goal (4200)
Module 9:  ? (4741) is (1352) I (900) reality (720) . (408) ... [1]
Module 10:  My (6324) am (1850) I (1460) through (1440) Dyz (1440) ... [6]
Module 11:  name (5664) goal (5664) an (2715) model (2160) interactions (2160) ... [9]
Module 12:  is (8429) ? (3606) artificial (3560) with (2880) reality (2880) ... [12]
Module 13:  Dyz (5760) to (5632) My (5109) cognition (4498) through (3600) ... [19]
Module 14:  model (5760) Lecticus (5760) . (4998) name (4956) goal (4956) ... [21]
Module 15:  is (7284) . (6320) reality (5760) with (5040) van (4320) ... [27]
Module 16:  through (5760) people (5760) to (5456) My (5109) der (5040) ... [30]
2017-12-23 18:45:42:700: Confabulated next symbol: ?
Module 4:  What (1)
Module 5:  is (1)
Module 6:  your (1)
Module 7:  name (1)
Confabulation output: What is your name ? (8 ms)

Confabulation input sequence: What is artificial cognition?
2017-12-23 18:45:42:701: Confabulated next symbol: My
Module 3:  What (1)
Module 4:  is (1)
Module 5:  artificial (1)
Module 6:  cognition (1)
Module 7:  ? (1)
Module 8:  My (3645) I (1460) Cognition (400)
2017-12-23 18:45:42:706: Confabulated next symbol: name
Module 2:  What (1)
Module 3:  is (1)
Module 4:  artificial (1)
Module 5:  cognition (1)
Module 6:  ? (1)
Module 7:  My (1)
Module 8:  name (4228) goal (3508)
Module 9:  is (5932) ? (1336) an (905) artificial (890) created (732) ... [3]
Module 10:  Dyz (4320) to (4224) My (2679) cognition (1903) artificial (1780) ... [9]
Module 11:  model (4320) Lecticus (4320) name (3540) goal (3540) cognition (2595) ... [10]
Module 12:  is (4994) . (4484) reality (4320) ? (3773) I (3640) ... [10]
Module 13:  My (5109) through (5040) der (3600) Dyz (3600) artificial (3560) ... [18]
Module 14:  interactions (5760) name (4956) goal (4956) cognition (4325) model (4320) ... [18]
Module 15:  . (8972) is (7284) with (5760) reality (5040) artificial (4450) ... [20]
Module 16:  through (5760) people (5760) cognition (5363) My (5109) der (5040) ... [24]
2017-12-23 18:45:42:707: Confabulated next symbol: is
Module 1:  What (1)
Module 2:  is (1)
Module 3:  artificial (1)
Module 4:  cognition (1)
Module 5:  ? (1)
Module 6:  My (1)
Module 7:  name (1)
Module 8:  is (5256) ? (668)
2017-12-23 18:45:42:708: Confabulated next symbol: Dyz
Module 0:  What (1)
Module 1:  is (1)
Module 2:  artificial (1)
Module 3:  cognition (1)
Module 4:  ? (1)
Module 5:  My (1)
Module 6:  name (1)
Module 7:  is (1)
Module 8:  Dyz (4320) to (3520) your (1275) cognition (1038)
2017-12-23 18:45:42:710: Confabulated next symbol: Lecticus
Module 0:  is (1)
Module 1:  artificial (1)
Module 2:  cognition (1)
Module 3:  ? (1)
Module 4:  My (1)
Module 5:  name (1)
Module 6:  is (1)
Module 7:  Dyz (1)
2017-12-23 18:45:42:711: Confabulated next symbol: .
Module 0:  artificial (1)
Module 1:  cognition (1)
Module 2:  ? (1)
Module 3:  My (1)
Module 4:  name (1)
Module 5:  is (1)
Module 6:  Dyz (1)
Module 7:  Lecticus (1)
2017-12-23 18:45:42:711: Failed to confabulate next symbol
Module 0:  cognition (1)
Module 1:  ? (1)
Module 2:  My (1)
Module 3:  name (1)
Module 4:  is (1)
Module 5:  Dyz (1)
Module 6:  Lecticus (1)
Module 7:  . (1)
Confabulation output: My name is Dyz Lecticus . (12 ms)

Confabulation input sequence: What is artificial cognition? (context: Cognition)
2017-12-23 18:45:42:786: Confabulated next symbol: Cognition
Module 3:  What (1)
Module 4:  is (1)
Module 5:  artificial (1)
Module 6:  cognition (1)
Module 7:  ? (1)
Module 8:  Cognition (400) I (180)
2017-12-23 18:45:42:795: Confabulated next symbol: refers
Module 2:  What (1)
Module 3:  is (1)
Module 4:  artificial (1)
Module 5:  cognition (1)
Module 6:  ? (1)
Module 7:  Cognition (1)
2017-12-23 18:45:42:805: Confabulated next symbol: to
Module 1:  What (1)
Module 2:  is (1)
Module 3:  artificial (1)
Module 4:  cognition (1)
Module 5:  ? (1)
Module 6:  Cognition (1)
Module 7:  refers (1)
2017-12-23 18:45:42:811: Confabulated next symbol: mental
Module 0:  What (1)
Module 1:  is (1)
Module 2:  artificial (1)
Module 3:  cognition (1)
Module 4:  ? (1)
Module 5:  Cognition (1)
Module 6:  refers (1)
Module 7:  to (1)
2017-12-23 18:45:42:816: Confabulated next symbol: processes
Module 0:  is (1)
Module 1:  artificial (1)
Module 2:  cognition (1)
Module 3:  ? (1)
Module 4:  Cognition (1)
Module 5:  refers (1)
Module 6:  to (1)
Module 7:  mental (1)
2017-12-23 18:45:42:819: Confabulated next symbol: within
Module 0:  artificial (1)
Module 1:  cognition (1)
Module 2:  ? (1)
Module 3:  Cognition (1)
Module 4:  refers (1)
Module 5:  to (1)
Module 6:  mental (1)
Module 7:  processes (1)
2017-12-23 18:45:42:823: Confabulated next symbol: the
Module 0:  cognition (1)
Module 1:  ? (1)
Module 2:  Cognition (1)
Module 3:  refers (1)
Module 4:  to (1)
Module 5:  mental (1)
Module 6:  processes (1)
Module 7:  within (1)
2017-12-23 18:45:42:825: Confabulated next symbol: brain
Module 0:  ? (1)
Module 1:  Cognition (1)
Module 2:  refers (1)
Module 3:  to (1)
Module 4:  mental (1)
Module 5:  processes (1)
Module 6:  within (1)
Module 7:  the (1)
2017-12-23 18:45:42:828: Confabulated next symbol: .
Module 0:  Cognition (1)
Module 1:  refers (1)
Module 2:  to (1)
Module 3:  mental (1)
Module 4:  processes (1)
Module 5:  within (1)
Module 6:  the (1)
Module 7:  brain (1)
2017-12-23 18:45:42:833: Failed to confabulate next symbol
Module 0:  refers (1)
Module 1:  to (1)
Module 2:  mental (1)
Module 3:  processes (1)
Module 4:  within (1)
Module 5:  the (1)
Module 6:  brain (1)
Module 7:  . (1)
Confabulation output: Cognition refers to mental processes within the brain . (53 ms)

Confabulation input sequence: What is artificial cognition? (context: Artificial)
2017-12-23 18:45:42:838: Confabulated next symbol: I
Module 3:  What (1)
Module 4:  is (1)
Module 5:  artificial (1)
Module 6:  cognition (1)
Module 7:  ? (1)
2017-12-23 18:45:42:842: Confabulated next symbol: am
Module 2:  What (1)
Module 3:  is (1)
Module 4:  artificial (1)
Module 5:  cognition (1)
Module 6:  ? (1)
Module 7:  I (1)
2017-12-23 18:45:42:844: Confabulated next symbol: an
Module 1:  What (1)
Module 2:  is (1)
Module 3:  artificial (1)
Module 4:  cognition (1)
Module 5:  ? (1)
Module 6:  I (1)
Module 7:  am (1)
2017-12-23 18:45:42:848: Confabulated next symbol: artificial
Module 0:  What (1)
Module 1:  is (1)
Module 2:  artificial (1)
Module 3:  cognition (1)
Module 4:  ? (1)
Module 5:  I (1)
Module 6:  am (1)
Module 7:  an (1)
2017-12-23 18:45:42:851: Confabulated next symbol: cognition
Module 0:  is (1)
Module 1:  artificial (1)
Module 2:  cognition (1)
Module 3:  ? (1)
Module 4:  I (1)
Module 5:  am (1)
Module 6:  an (1)
Module 7:  artificial (1)
2017-12-23 18:45:42:854: Confabulated next symbol: .
Module 0:  artificial (1)
Module 1:  cognition (1)
Module 2:  ? (1)
Module 3:  I (1)
Module 4:  am (1)
Module 5:  an (1)
Module 6:  artificial (1)
Module 7:  cognition (1)
2017-12-23 18:45:42:857: Failed to confabulate next symbol
Module 0:  cognition (1)
Module 1:  ? (1)
Module 2:  I (1)
Module 3:  am (1)
Module 4:  an (1)
Module 5:  artificial (1)
Module 6:  cognition (1)
Module 7:  . (1)
Confabulation output: I am an artificial cognition . (23 ms)
~~~~

Test results
------------
All 4 tests have been executed successfully (12 assertions).  
Total test duration: 363 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zsc.test.TestConfabulatorTraining: 572 Kb / 0 Mb
 * nl.zeesoft.zsc.test.TestConfabulatorContextConfabulation: 679 Kb / 0 Mb
 * nl.zeesoft.zsc.test.TestConfabulatorCorrectionConfabulation: 684 Kb / 0 Mb
 * nl.zeesoft.zsc.test.TestConfabulatorExtensionConfabulation: 688 Kb / 0 Mb
