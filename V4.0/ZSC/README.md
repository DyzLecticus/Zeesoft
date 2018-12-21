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
Confabulator conf = new Confabulator(new Config(),"MockConfabulator",4);
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
21:55:35:810: Initialized module symbol variations;
  01: My:0.006493506493506494
  02: am:0.003246753246753247 name:0.003246753246753247
  03: is:0.006493506493506494
  04: Dyz:0.006493506493506494
  05: agent:0.006493506493506494
  06: .:0.006493506493506494
21:55:35:833: Initialized module symbol expectations;
  01: My:0.02922077922077922 I:0.00974025974025974
  02: name:0.061688311688311695 goal:0.03896103896103896 What:0.01948051948051948 artifically:0.01948051948051948 [1]
  03: is:0.03896103896103896 intelligent:0.01948051948051948 an:0.00974025974025974 and:0.00974025974025974 [1]
  04: Dyz:0.045454545454545456 to:0.01948051948051948 virtual:0.01948051948051948 and:0.00974025974025974 [3]
  05: Lecticus:0.048701298701298704 understand:0.01948051948051948 agent:0.016233766233766232 good:0.00974025974025974 [3]
  06: .:0.045454545454545456 ?:0.00974025974025974 and:0.00974025974025974 virtual:0.00974025974025974
21:55:35:840: Confabulated module symbols;
  01: My:1.0
  02: name:1.0
  03: is:1.0
  04: Dyz:1.0
  05: Lecticus:1.0
  06: .:1.0

Corrected: 'MY NAM IS DYZ AGENT.' -> 'My name is Dyz Lecticus.' (validated links)
Log;
21:55:35:931: Initialized module symbol variations;
  01: My:0.003246753246753247
  02: am:0.003246753246753247 name:0.003246753246753247
  03: I:0.003246753246753247 is:0.003246753246753247
  04: Dyz:0.003246753246753247
  05: agent:0.003246753246753247
  06: .:0.006493506493506494
21:55:35:932: Initialized module symbol expectations;
  01: My:0.025974025974025976 I:0.00974025974025974
  02: name:0.061688311688311695 goal:0.03896103896103896 What:0.01948051948051948 artifically:0.01948051948051948 [1]
  03: is:0.06818181818181819 intelligent:0.03896103896103896 an:0.01948051948051948 and:0.01948051948051948 [2]
  04: Dyz:0.04220779220779221 to:0.01948051948051948 virtual:0.01948051948051948 am:0.00974025974025974 [5]
  05: Lecticus:0.048701298701298704 understand:0.01948051948051948 agent:0.012987012987012988 an:0.00974025974025974 [5]
  06: .:0.045454545454545456 ?:0.00974025974025974 and:0.00974025974025974 artifically:0.00974025974025974 [2]
21:55:35:935: Confabulated module symbols;
  01: My:1.0
  02: name:1.0
  03: is:1.0
  04: Dyz:1.0
  05: Lecticus:1.0
  06: .:1.0

Corrected: 'My goad is to help.' -> 'My goal is to understand.' (validated links)
Log;
21:55:35:936: Initialized module symbol variations;
  01: My:0.006493506493506494
  02: goal:0.003246753246753247 good:0.003246753246753247
  03: is:0.006493506493506494
  04: to:0.006493506493506494
  05: help:0.006493506493506494
  06: .:0.006493506493506494
21:55:35:937: Initialized module symbol expectations;
  01: My:0.02922077922077922 use:0.00974025974025974 very:0.00974025974025974
  02: goal:0.061688311688311695 name:0.03896103896103896 What:0.01948051948051948 that:0.01948051948051948 [2]
  03: is:0.03896103896103896 ?:0.00974025974025974 and:0.00974025974025974 context:0.00974025974025974 [3]
  04: to:0.03571428571428571 Dyz:0.02922077922077922 and:0.01948051948051948 help:0.00974025974025974 [2]
  05: understand:0.03896103896103896 Lecticus:0.02922077922077922 agent:0.00974025974025974 do:0.00974025974025974 [4]
  06: and:0.02922077922077922 .:0.016233766233766232 ?:0.00974025974025974 people:0.00974025974025974 [1]
21:55:35:939: Confabulated module symbols;
  01: My:1.0
  02: goal:1.0
  03: is:1.0
  04: to:1.0
  05: understand:1.0
  06: .:1.0

Corrected: 'My goad is to help.' -> 'My goal is to help.'
Log;
21:55:35:940: Initialized module symbol variations;
  01: My:0.006493506493506494
  02: goal:0.003246753246753247 good:0.003246753246753247
  03: is:0.006493506493506494
  04: to:0.006493506493506494
  05: help:0.006493506493506494
  06: .:0.006493506493506494
21:55:35:941: Confabulated module symbols;
  01: My:1.0
  02: goal:1.0
  03: is:1.0
  04: to:1.0
  05: help:1.0
  06: .:1.0

Corrected: 'gaad.' -> 'Gaad.'
Log;
21:55:35:993: Initialized module symbol variations;
  01: and:0.003246753246753247 goal:0.003246753246753247 good:0.003246753246753247
  02: .:0.006493506493506494
21:55:35:994: Confabulated module symbols;
  01: goal:0.012987012987012988 good:0.012987012987012988
  02: .:1.0
21:55:35:994: Confabulated module symbols;
  01: goal:0.022727272727272728 good:0.022727272727272728
  02: .:1.0

Corrected: 'gaad.' -> 'Goal.'
Log;
21:55:36:046: Initialized module symbol variations;
  01: and:0.003246753246753247 goal:0.003246753246753247 good:0.003246753246753247
  02: .:0.006493506493506494
21:55:36:047: Confabulated module symbols;
  01: goal:0.013493506493506495 good:0.013006493506493508
  02: .:1.0
21:55:36:047: Confabulated module symbols;
  01: good:0.02351623376623377 goal:0.023292207792207795
  02: .:1.0
21:55:36:047: Confabulated module symbols;
  01: goal:0.033840909090909095 good:0.03328571428571429
  02: .:1.0
21:55:36:047: Confabulated module symbols;
  01: good:0.04387337662337662 goal:0.04385389610389611
  02: .:1.0
21:55:36:048: Confabulated module symbols;
  01: goal:0.05385714285714287 good:0.05368181818181818
  02: .:1.0
21:55:36:051: Confabulated module symbols;
  01: goal:0.06381168831168832 good:0.06346103896103895
  02: .:1.0

Contexts for 'My name is Dyz Lecticus.': 3
 - 'Name' 0.12987012987012989/1.0
 - 'Self' 0.12987012987012989/1.0
 - 'Goal' 0.003246753246753247/0.024999999999999998

Contexts for 'My name is Dyz Lecticus.': 3
 - 'Name' 0.1365909090909091/1.0
 - 'Self' 0.13560714285714287/0.9927977180888995
 - 'Goal' 0.0033214285714285715/0.024316615165200856

Contexts for 'I can learn context sensitive symbol sequences and use that knowledge to do things like correct symbols, classify context and more.': 2
 - 'Description' 0.7987012987012974/1.0
 - 'Self' 0.7987012987012974/1.0

Extension for 'I': can learn context sensitive symbol sequences and use that knowledge
Log;
21:55:36:056: Initialized module symbols;
  01: I:1.0
  02:
  03:
  04:
  05:
  06:
  07:
  08:
  09:
  10:
  11:
21:55:36:057: Initialized module symbol expectations;
  01: I:1.0
  02: am:0.00974025974025974 can:0.00974025974025974
  03: an:0.00974025974025974 learn:0.00974025974025974
  04: artifically:0.00974025974025974 context:0.00974025974025974
  05: intelligent:0.00974025974025974 sensitive:0.00974025974025974
  06:
  07:
  08:
  09:
  10:
  11:
21:55:36:059: Confabulated module symbols;
  01: I:1.0
  02: am:0.048701298701298704 can:0.048701298701298704
  03: an:0.048701298701298704 learn:0.048701298701298704
  04: artifically:0.048701298701298704 context:0.048701298701298704
  05: intelligent:0.048701298701298704 sensitive:0.048701298701298704
  06: symbol:0.03896103896103896 virtual:0.03896103896103896
  07: agent:0.02922077922077922 sequences:0.02922077922077922
  08: .:0.01948051948051948 and:0.01948051948051948
  09: use:1.0
  10:
  11:
21:55:36:061: Confabulated module symbols;
  01: I:1.0
  02: am:0.08766233766233768 can:0.08766233766233768
  03: an:0.09740259740259742 learn:0.09740259740259742
  04: artifically:0.10714285714285716 context:0.10714285714285716
  05: intelligent:0.11688311688311691 sensitive:0.11688311688311691
  06: symbol:1.0
  07: sequences:1.0
  08: and:1.0
  09: use:1.0
  10: that:1.0
  11: knowledge:1.0
21:55:36:061: Confabulated module symbols;
  01: I:1.0
  02: am:0.12662337662337664 can:0.12662337662337664
  03: learn:1.0
  04: context:1.0
  05: sensitive:1.0
  06: symbol:1.0
  07: sequences:1.0
  08: and:1.0
  09: use:1.0
  10: that:1.0
  11: knowledge:1.0
21:55:36:062: Confabulated module symbols;
  01: I:1.0
  02: can:1.0
  03: learn:1.0
  04: context:1.0
  05: sensitive:1.0
  06: symbol:1.0
  07: sequences:1.0
  08: and:1.0
  09: use:1.0
  10: that:1.0
  11: knowledge:1.0

Extension for 'My': 
Log;
21:55:36:063: Initialized module symbols;
  01: My:1.0
  02:
  03:
  04:
  05:
  06:
21:55:36:063: Initialized module symbol expectations;
  01: My:1.0
  02: goal:0.00974025974025974 name:0.00974025974025974
  03: is:0.003246753246753247
  04: Dyz:0.00974025974025974 to:0.00974025974025974
  05: Lecticus:0.00974025974025974 understand:0.00974025974025974
  06:
21:55:36:064: Confabulated module symbols;
  01: My:1.0
  02: goal:0.048701298701298704 name:0.048701298701298704
  03: is:1.0
  04: Dyz:0.048701298701298704 to:0.048701298701298704
  05: Lecticus:0.048701298701298704 understand:0.048701298701298704
  06: .:0.03896103896103896 and:0.03896103896103896
21:55:36:065: Confabulated module symbols;
  01: My:1.0
  02: goal:0.08766233766233768 name:0.08766233766233768
  03: is:1.0
  04: Dyz:0.09740259740259742 to:0.09740259740259742
  05: Lecticus:0.09740259740259742 understand:0.09740259740259742
  06: .:0.07792207792207793 and:0.07792207792207793

Extension for 'My': name is Dyz Lecticus.
Log;
21:55:36:121: Initialized module symbols;
  01: My:1.0
  02:
  03:
  04:
  05:
  06:
21:55:36:121: Initialized module symbol expectations;
  01: My:1.0
  02: name:0.003246753246753247
  03: is:0.003246753246753247
  04: Dyz:0.003246753246753247
  05: Lecticus:0.003246753246753247
  06:
21:55:36:122: Confabulated module symbols;
  01: My:1.0
  02: name:1.0
  03: is:1.0
  04: Dyz:1.0
  05: Lecticus:1.0
  06: .:1.0

~~~~

Test results
------------
All 1 tests have been executed successfully (14 assertions).  
Total test duration: 375 ms (total sleep duration: 0 ms).  

Memory usage per test;  
 * nl.zeesoft.zsc.test.TestConfabulator: 1216 Kb / 1 Mb
