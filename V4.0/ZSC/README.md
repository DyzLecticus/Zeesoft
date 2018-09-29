Zeesoft Symbolic Confabulators
==============================
Zeesoft Symbolic Confabulators provides a simple JSON API to manage multiple symbolic confabulators.

**Dependencies**  
This library depends on the following libraries;  
 * [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V3.0/ZDK/)  
 * [Zeesoft Object Database](https://github.com/DyzLecticus/Zeesoft/tree/master/V4.0/ZODB/)  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSC/releases/zsc-0.9.0.zip) to download the latest ZSC release (version 0.9.0).  
All ZSC releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V4.0/ZSC/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZSC](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSC/src/nl/zeesoft/zsc/test/ZSC.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zsc.test.TestConfabulator
------------------------------------
This test shows how to train a *Confabulator* to and use it to correct sequences and determine context.

**Example implementation**  
~~~~
// Create the confabulator
DatabaseRequest request = new DatabaseRequest(DatabaseRequest.TYPE_LIST);
// Train the confabulator
conf.learnSequence("A sequence to learn.","OptionalContextSymbolToAssociate");
conf.calculateProbabilities();
// Create a correction confabulation
CorrectionConfabulation confab1 = new CorrectionConfabulation();
confab1.input.append("A sequence to correct");
// Confabulate the correction
conf.confabulate(confab1);
// Create a context confabulation
ContextConfabulation confab2 = new ContextConfabulation();
confab2.input.append("A sequence to determine context for");
// Confabulate the context
conf.confabulate(confab2);
~~~~

This test uses the *MockConfabulator*.  

Class references;  
 * [TestConfabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSC/src/nl/zeesoft/zsc/test/TestConfabulator.java)
 * [MockConfabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSC/src/nl/zeesoft/zsc/test/MockConfabulator.java)
 * [Confabulator](https://github.com/DyzLecticus/Zeesoft/blob/master/V4.0/ZSC/src/nl/zeesoft/zsc/confab/Confabulator.java)

**Test output**  
The output of this test shows;  
 * Some details about the trained confabulator  
 * The result of some confabulations  
~~~~
Confabulator name: MockConfabulator, max. distance: 4
Symbols/links for default context: 40/153
Symbols/links for 'Name' context: 6/14
Symbol/link bandwidth for default context: 0.029411764705882353/0.003246753246753247
Symbol to link bandwidth factor for default context: 0.1103896103896104

Corrected: 'My nam is Dyz agent.' -> 'My name is Dyz Lecticus.' (validated links)
Log;
13:55:06:459: Initialized module symbol variations;
  01: My:0.006493506493506494
  02: am:0.003246753246753247 name:0.003246753246753247
  03: is:0.006493506493506494
  04: Dyz:0.006493506493506494
  05: agent:0.006493506493506494
  06: .:0.006493506493506494
13:55:06:480: Initialized module symbol expectations;
  01: My:0.02922077922077922 I:0.00974025974025974
  02: name:0.061688311688311695 goal:0.03896103896103896 What:0.01948051948051948 artifically:0.01948051948051948 [1]
  03: is:0.03896103896103896 intelligent:0.01948051948051948 an:0.00974025974025974 and:0.00974025974025974 [1]
  04: Dyz:0.045454545454545456 to:0.01948051948051948 virtual:0.01948051948051948 and:0.00974025974025974 [3]
  05: Lecticus:0.048701298701298704 understand:0.01948051948051948 agent:0.016233766233766232 good:0.00974025974025974 [3]
  06: .:0.045454545454545456 ?:0.00974025974025974 and:0.00974025974025974 virtual:0.00974025974025974
13:55:06:488: Confabulated module symbols;
  01: My:1.0
  02: name:1.0
  03: is:1.0
  04: Dyz:1.0
  05: Lecticus:1.0
  06: .:1.0

Corrected: 'MY NAM IS DYZ AGENT.' -> 'My name is Dyz Lecticus.' (validated links)
Log;
13:55:06:569: Initialized module symbol variations;
  01: My:0.003246753246753247
  02: am:0.003246753246753247 name:0.003246753246753247
  03: I:0.003246753246753247 is:0.003246753246753247
  04: Dyz:0.003246753246753247
  05: agent:0.003246753246753247
  06: .:0.006493506493506494
13:55:06:571: Initialized module symbol expectations;
  01: My:0.025974025974025976 I:0.00974025974025974
  02: name:0.061688311688311695 goal:0.03896103896103896 What:0.01948051948051948 artifically:0.01948051948051948 [1]
  03: is:0.06818181818181819 intelligent:0.03896103896103896 an:0.01948051948051948 and:0.01948051948051948 [2]
  04: Dyz:0.04220779220779221 to:0.01948051948051948 virtual:0.01948051948051948 am:0.00974025974025974 [5]
  05: Lecticus:0.048701298701298704 understand:0.01948051948051948 agent:0.012987012987012988 an:0.00974025974025974 [5]
  06: .:0.045454545454545456 ?:0.00974025974025974 and:0.00974025974025974 artifically:0.00974025974025974 [2]
13:55:06:579: Confabulated module symbols;
  01: My:1.0
  02: name:1.0
  03: is:1.0
  04: Dyz:1.0
  05: Lecticus:1.0
  06: .:1.0

Corrected: 'My goad is to help.' -> 'My goal is to understand.' (validated links)
Log;
13:55:06:580: Initialized module symbol variations;
  01: My:0.006493506493506494
  02: goal:0.003246753246753247 good:0.003246753246753247
  03: is:0.006493506493506494
  04: to:0.006493506493506494
  05: help:0.006493506493506494
  06: .:0.006493506493506494
13:55:06:581: Initialized module symbol expectations;
  01: My:0.02922077922077922 use:0.00974025974025974 very:0.00974025974025974
  02: goal:0.061688311688311695 name:0.03896103896103896 What:0.01948051948051948 that:0.01948051948051948 [2]
  03: is:0.03896103896103896 ?:0.00974025974025974 and:0.00974025974025974 context:0.00974025974025974 [3]
  04: to:0.03571428571428571 Dyz:0.02922077922077922 and:0.01948051948051948 help:0.00974025974025974 [2]
  05: understand:0.03896103896103896 Lecticus:0.02922077922077922 agent:0.00974025974025974 do:0.00974025974025974 [4]
  06: and:0.02922077922077922 .:0.016233766233766232 ?:0.00974025974025974 people:0.00974025974025974 [1]
13:55:06:583: Confabulated module symbols;
  01: My:1.0
  02: goal:1.0
  03: is:1.0
  04: to:1.0
  05: understand:1.0
  06: .:1.0

Corrected: 'My goad is to help.' -> 'My goal is to help.'
Log;
13:55:06:585: Initialized module symbol variations;
  01: My:0.006493506493506494
  02: goal:0.003246753246753247 good:0.003246753246753247
  03: is:0.006493506493506494
  04: to:0.006493506493506494
  05: help:0.006493506493506494
  06: .:0.006493506493506494
13:55:06:587: Confabulated module symbols;
  01: My:1.0
  02: goal:1.0
  03: is:1.0
  04: to:1.0
  05: help:1.0
  06: .:1.0

Corrected: 'gaad.' -> 'Gaad.'
Log;
13:55:06:628: Initialized module symbol variations;
  01: and:0.003246753246753247 goal:0.003246753246753247 good:0.003246753246753247
  02: .:0.006493506493506494
13:55:06:628: Confabulated module symbols;
  01: goal:0.012987012987012988 good:0.012987012987012988
  02: .:1.0
13:55:06:628: Confabulated module symbols;
  01: goal:0.022727272727272728 good:0.022727272727272728
  02: .:1.0

Corrected: 'gaad.' -> 'Good.'
Log;
13:55:06:668: Initialized module symbol variations;
  01: and:0.003246753246753247 goal:0.003246753246753247 good:0.003246753246753247
  02: .:0.006493506493506494
13:55:06:670: Confabulated module symbols;
  01: good:0.0132012987012987 goal:0.013181818181818183
  02: .:1.0
13:55:06:671: Confabulated module symbols;
  01: good:0.023876623376623374 goal:0.0231948051948052
  02: .:1.0

Contexts for 'My name is Dyz Lecticus.': 3
 - 'Name' 0.12987012987012989/1.0
 - 'Self' 0.12987012987012989/1.0
 - 'Goal' 0.003246753246753247/0.024999999999999998
Contexts for 'My name is Dyz Lecticus.': 3
 - 'Name' 0.1379448051948052/1.0
 - 'Self' 0.1374577922077922/0.9964695083201919
 - 'Goal' 0.003350649350649351/0.024289782757078635
Contexts for 'I can learn context sensitive symbol sequences and use that knowledge to do things like correct symbols, classify context and more.': 2
 - 'Description' 0.7987012987012974/1.0
 - 'Self' 0.7987012987012974/1.0
~~~~

Test results
------------
All 1 tests have been executed successfully (12 assertions).  
Total test duration: 300 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zsc.test.TestConfabulator: 1216 Kb / 1 Mb
