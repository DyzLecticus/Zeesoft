Zeesoft Drum & Bass Dreamer
=================================
Zeesoft Drum & Bass Dreamer is an application that can be used to generate drum & bass MIDI using HTM.  

**Release downloads**  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/releases/zdbd-0.9.0.zip) to download the latest ZDBD release (version 0.9.0).  
All ZDBD releases can be downloaded [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/releases/).  
*All jar files in the release include source code and build scripts.*  

**Self documenting and self testing**  
The tests used to develop this libary are also used to generate this README file.  
Run the [ZDBD](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdbd/test/ZDBD.java) class as a java application to print this documentation to the standard out.  
Click [here](#test-results) to scroll down to the test result summary.  

nl.zeesoft.zdbd.test.TestEncoders
---------------------------------
This test shows how a *Rythm* and *InstrumentPattern* are converted into SDRs for *Network* training. 
The rythm and pattern number are converted into 'context' SDRs. 
The pattern instruments are divided into two groups and then converted into SDRs. 

**Example implementation**  
~~~~
// Create the Rythm (default = 4/4)
Rythm rythm = new Rythm();
// Create the InstrumentPattern
InstrumentPattern pattern = PatternFactory.getFourOnFloorInstrumentPattern(0);
// Get the context SDRs
List<SDR> sdrs = rythm.getSDRsForPattern(pattern.num);
// Get the group 1 SDRs
sdrs = pattern.getSDRsForGroup(1,rythm.getStepsPerPattern());
// Get the group 2 SDRs
sdrs = pattern.getSDRsForGroup(2,rythm.getStepsPerPattern());
~~~~

Class references;  
 * [Rythm](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdbd/pattern/Rythm.java)
 * [InstrumentPattern](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdbd/pattern/InstrumentPattern.java)
 * [SDR](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdk/neural/SDR.java)
 * [Network](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdk/neural/network/Network.java)

**Test output**  
The output of this test shows all SDRs for a rythm and instrument pattern.  
~~~~
Context (23*23);
(Combines pattern number, beat and beat step into a single value)
01: 11000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
02: 00110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
03: 00001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
04: 00000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
05: 00000000000000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
06: 00000000000000000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
07: 00000000000000000000110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
08: 00000000000000000000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
09: 00000000000000000000000000000000110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
10: 00000000000000000000000000000000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
11: 00000000000000000000000000000000000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
12: 00000000000000000000000000000000000000110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
13: 00000000000000000000000000000000000000000000000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
14: 00000000000000000000000000000000000000000000000000110000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
15: 00000000000000000000000000000000000000000000000000001100000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000
16: 00000000000000000000000000000000000000000000000000000011000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000

Group 1 pattern (7*6);
(Kick, Snare, Hihat, Cymbals)
01: 0000111100000000110000000000000000000011
02: 1100001100000011000000110000000000000000
03: 1100001100000000000011110000000000000000
04: 1100001100000011000000110000000000000000
05: 1100000000110000110000000000000000110000
06: 1100001100000011000000110000000000000000
07: 1100001100000000000011110000000000000000
08: 1100001100000011000000110000000000000000
09: 0000111100000000110000000000000000110000
10: 1100001100000011000000110000000000000000
11: 1100001100000000000011110000000000000000
12: 1100001100000011000000110000000000000000
13: 1100000000110000110000000000000000110000
14: 1100001100000011000000110000000000000000
15: 1100001100000000000011000000110000000000
16: 1100001100000011000000110000000000000000

Group 2 pattern (10*9);
(Percussion, Bass, Octave, Note)
01: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
02: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
03: 0000001100000000000000110000000000000000000000000000110000000000110000000000000000
04: 0000000000001100000000110000000000000000000000000000110000000000110000000000000000
05: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
06: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
07: 0000110000000000000000110000000000000000000000000000001100000000110000000000000000
08: 1100000000000000000000110000000000000000000000000000110000000000110000000000000000
09: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
10: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
11: 0000001100000000000000110000000000000000000000000000110000000000110000000000000000
12: 0000000000001100000000110000000000000000000000000000001100000000110000000000000000
13: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
14: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
15: 0000110000000000000000000011000000000000000000000000110000000000110000000000000000
16: 1100000000000000001100000000000000000000000000000000110000000000110000000000000000
~~~~

nl.zeesoft.zdbd.test.TestInstrumentNetwork
------------------------------------------
This test shows how a *PatternSequence* and a *NetworkTrainer* can be used to train a *Network*. 

**Example implementation**  
~~~~
// Create the network configuration
NetworkConfig config = NetworkConfigFactory.getNetworkConfig();
// Create and initialize the network
Network network = new Network();
network.initialize(config,true);
// Create the pattern sequence
PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();
// Create and configure the network trainer
NetworkTrainer trainer = new NetworkTrainer();
trainer.setSequence(sequence);
// Train the network
CodeRunnerChain chain = trainer.getTrainNetworkChain(network);
Waiter.startAndWaitFor(chain, 60000);
~~~~

Class references;  
 * [PatternSequence](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdbd/pattern/PatternSequence.java)
 * [NetworkTrainer](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdbd/neural/NetworkTrainer.java)
 * [Network](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdk/neural/network/Network.java)
 * [CodeRunnerChain](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdk/thread/CodeRunnerChain.java)
 * [Waiter](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdk/thread/Waiter.java)

**Test output**  
The output of this test shows the network configuration and the network / network trainer debug log.  
~~~~
Inputs: 
<- Context
<- Group1
<- Group2
Layer: 0
  Group1Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group1
  -> SDR: 24*24
  Group2Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group2
  -> SDR: 25*25
Layer: 1
  Group1Pooler: SpatialPoolerConfig: 24*24*1
  <- 0 = Group1Merger/0
  -> 0 = Active columns: 32*32, on bits: 20
  Group2Pooler: SpatialPoolerConfig: 25*25*1
  <- 0 = Group2Merger/0
  -> 0 = Active columns: 32*32, on bits: 20
Layer: 2
  Group1Memory: TemporalMemoryConfig: 32*32*16
  <- 0 = Group1Pooler/0
  -> 0 = Active cells: 512*32
  -> 1 = Bursting columns: 32*32
  -> 2 = Predictive cells: 512*32
  -> 3 = Winner cells: 512*32
  Group2Memory: TemporalMemoryConfig: 32*32*16
  <- 0 = Group2Pooler/0
  -> 0 = Active cells: 512*32
  -> 1 = Bursting columns: 32*32
  -> 2 = Predictive cells: 512*32
  -> 3 = Winner cells: 512*32
Layer: 3
  KickClassifier: ClassifierConfig: 512*32 (Kick)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  SnareClassifier: ClassifierConfig: 512*32 (Snare)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  HihatClassifier: ClassifierConfig: 512*32 (Hihat)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  RideClassifier: ClassifierConfig: 512*32 (Ride)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  CrashClassifier: ClassifierConfig: 512*32 (Crash)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  Percussion1Classifier: ClassifierConfig: 512*32 (Percussion1)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1
  Percussion2Classifier: ClassifierConfig: 512*32 (Percussion2)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1
  BassClassifier: ClassifierConfig: 512*32 (Bass)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1
  OctaveClassifier: ClassifierConfig: 512*32 (Octave)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1
  NoteClassifier: ClassifierConfig: 512*32 (Note)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1

Initializing network ...
2021-01-17 11:27:30:508 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-01-17 11:27:30:522 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-01-17 11:27:30:522 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-01-17 11:27:30:524 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-01-17 11:27:30:524 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:525 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:525 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:526 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:526 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:526 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:524 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-01-17 11:27:30:526 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:527 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:527 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:527 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:527 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-01-17 11:27:30:527 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:527 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-01-17 11:27:30:527 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:527 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:529 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:534 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:534 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:534 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-01-17 11:27:30:535 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:535 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:535 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:27:30:535 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:27:30:535 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-01-17 11:27:30:557 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-01-17 11:27:30:558 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-01-17 11:27:30:553 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-01-17 11:27:30:597 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-01-17 11:27:30:609 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-01-17 11:27:30:615 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-01-17 11:27:30:671 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-01-17 11:27:30:671 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-01-17 11:27:31:643 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-01-17 11:27:31:643 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-01-17 11:27:31:806 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-01-17 11:27:31:806 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-01-17 11:27:31:821 nl.zeesoft.zdk.neural.network.Network: Initialized network
Initialized network

Training network ...
2021-01-17 11:27:31:824 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 1/16 ...
2021-01-17 11:27:33:755 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 2/16 ...
2021-01-17 11:27:35:270 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 3/16 ...
2021-01-17 11:27:36:799 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 4/16 ...
2021-01-17 11:27:36:799 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training memory)
2021-01-17 11:27:38:727 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 5/16 ...
2021-01-17 11:27:40:586 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 6/16 ...
2021-01-17 11:27:42:309 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 7/16 ...
2021-01-17 11:27:44:063 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 8/16 ...
2021-01-17 11:27:44:063 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training classifiers)
2021-01-17 11:27:45:917 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.5849057, 0.93442625, 0.78688526, 0.8360656, 0.75438595, 0.875, 0.6603774, 0.8181818, 0.72131145, 0.7903226] (0.7761862) / [0.4, 0.9, 0.8, 0.8, 0.0, 0.8, 0.6, 0.7, 0.6, 0.7]
2021-01-17 11:27:45:917 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 9/16 ...
2021-01-17 11:27:47:782 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8119658, 0.952, 0.88, 0.888, 0.87603307, 0.94166666, 0.84615386, 0.9159664, 0.84, 0.856] (0.8807786) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.8]
2021-01-17 11:27:47:782 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 10/16 ...
2021-01-17 11:27:49:601 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8729282, 0.962963, 0.9047619, 0.9047619, 0.9081081, 0.9619565, 0.89502764, 0.9453552, 0.87830687, 0.8994709] (0.91336405) / [1.0, 0.9, 1.0, 0.9, 0.9, 1.0, 1.0, 1.0, 0.9, 1.0]
2021-01-17 11:27:49:601 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 11/16 ...
2021-01-17 11:27:51:396 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.90163934, 0.96837944, 0.91699606, 0.91699606, 0.9196787, 0.9677419, 0.922449, 0.9554656, 0.90118575, 0.90909094] (0.9279623) / [0.9, 0.9, 1.0, 1.0, 0.8, 1.0, 1.0, 1.0, 1.0, 0.9]
2021-01-17 11:27:51:396 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 12/16 ...
2021-01-17 11:27:53:110 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.984375, 0.9765625, 0.9609375, 0.94921875, 0.96875, 0.9921875, 0.99609375, 0.9921875, 0.9609375, 0.95703125] (0.97382814) / [1.0, 0.9, 1.0, 1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-01-17 11:27:53:110 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 13/16 ...
2021-01-17 11:27:55:175 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.98046875, 0.98046875, 0.96875, 0.953125, 0.96484375, 0.98828125, 0.99609375, 0.98828125, 0.97265625, 0.9765625] (0.97695315) / [1.0, 0.9, 1.0, 1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-01-17 11:27:55:175 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 14/16 ...
2021-01-17 11:27:56:884 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.984375, 0.98046875, 0.98046875, 0.96484375, 0.96484375, 0.984375, 1.0, 0.98046875, 0.984375, 0.9765625] (0.9800781) / [1.0, 0.9, 1.0, 1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-01-17 11:27:56:884 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 15/16 ...
2021-01-17 11:27:58:599 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.98828125, 0.98046875, 0.98828125, 0.96875, 0.96875, 0.984375, 1.0, 0.98046875, 0.984375, 0.984375] (0.9828125) / [1.0, 0.9, 1.0, 1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-01-17 11:27:58:599 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 16/16 ...
2021-01-17 11:28:00:321 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.9921875, 0.984375, 0.9921875, 0.97265625, 0.96875, 0.98828125, 1.0, 0.984375, 0.98828125, 0.984375] (0.9855469) / [1.0, 0.9, 1.0, 1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-01-17 11:28:00:321 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Training network took; 28497 ms, accuracies;
- BassClassifier        : 0.9921875
- CrashClassifier       : 0.984375
- HihatClassifier       : 0.9921875
- KickClassifier        : 0.97265625
- NoteClassifier        : 0.96875
- OctaveClassifier      : 0.98828125
- Percussion1Classifier : 1.0
- Percussion2Classifier : 0.984375
- RideClassifier        : 0.98828125
- SnareClassifier       : 0.984375
------------------------------------
- Average               : 0.9855469
2021-01-17 11:28:00:446 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Statistics;
- Cells             : 32768
- Proximal segments : 2048
- Proximal synapses : 830840 (active: 436323)
- Distal segments   : 2826
- Distal synapses   : 72431 (active: 59102)
Trained network
~~~~

nl.zeesoft.zdbd.test.TestGenerator
----------------------------------
This test shows how a *Generator* can be used to generate pattern sequences using a trained *Network* and the original training *PatternSequence*. 

**Example implementation**  
~~~~
// Create the network configuration
NetworkConfig config = NetworkConfigFactory.getNetworkConfig();
// Create and load the trained network
Network network = new Network();
network.initializeAndLoad(config);
// Get the training sequence
PatternSequence sequence = PatternFactory.getFourOnFloorInstrumentPatternSequence();
// Create the generator
Generator generator = new Generator();
// Generate a new pattern sequence
generator.generatePatternSequence(network, network.getLastIO(), sequence);
PatternSequence generated = generator.generatedPatternSequence;
~~~~

Class references;  
 * [Generator](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdbd/neural/Generator.java)
 * [Network](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdk/neural/network/Network.java)
 * [PatternSequence](https://github.com/DyzLecticus/Zeesoft/blob/master/V5.0/ZDBD/src/nl/zeesoft/zdbd/pattern/PatternSequence.java)

**Test output**  
The output of this test shows the network and generator debug log.  
~~~~
Inputs: 
<- Context
<- Group1
<- Group2
Layer: 0
  Group1Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group1
  -> SDR: 24*24
  Group2Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group2
  -> SDR: 25*25
Layer: 1
  Group1Pooler: SpatialPoolerConfig: 24*24*1
  <- 0 = Group1Merger/0
  -> 0 = Active columns: 32*32, on bits: 20
  Group2Pooler: SpatialPoolerConfig: 25*25*1
  <- 0 = Group2Merger/0
  -> 0 = Active columns: 32*32, on bits: 20
Layer: 2
  Group1Memory: TemporalMemoryConfig: 32*32*16
  <- 0 = Group1Pooler/0
  -> 0 = Active cells: 512*32
  -> 1 = Bursting columns: 32*32
  -> 2 = Predictive cells: 512*32
  -> 3 = Winner cells: 512*32
  Group2Memory: TemporalMemoryConfig: 32*32*16
  <- 0 = Group2Pooler/0
  -> 0 = Active cells: 512*32
  -> 1 = Bursting columns: 32*32
  -> 2 = Predictive cells: 512*32
  -> 3 = Winner cells: 512*32
Layer: 3
  KickClassifier: ClassifierConfig: 512*32 (Kick)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  SnareClassifier: ClassifierConfig: 512*32 (Snare)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  HihatClassifier: ClassifierConfig: 512*32 (Hihat)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  RideClassifier: ClassifierConfig: 512*32 (Ride)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  CrashClassifier: ClassifierConfig: 512*32 (Crash)
  <- 0 = Group1Memory/0
  <- 1 = Group1
  -> 0 = Classifications: 1*1
  Percussion1Classifier: ClassifierConfig: 512*32 (Percussion1)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1
  Percussion2Classifier: ClassifierConfig: 512*32 (Percussion2)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1
  BassClassifier: ClassifierConfig: 512*32 (Bass)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1
  OctaveClassifier: ClassifierConfig: 512*32 (Octave)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1
  NoteClassifier: ClassifierConfig: 512*32 (Note)
  <- 0 = Group2Memory/0
  <- 1 = Group2
  -> 0 = Classifications: 1*1

Initializing network ...
2021-01-17 11:28:00:508 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-01-17 11:28:00:508 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-01-17 11:28:00:508 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-01-17 11:28:00:519 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-01-17 11:28:00:519 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-01-17 11:28:00:523 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:523 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:522 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:530 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:530 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:522 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:522 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:530 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:531 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:522 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:522 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:532 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:522 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:521 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:521 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-01-17 11:28:00:532 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-01-17 11:28:00:533 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:533 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-01-17 11:28:00:534 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:521 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-01-17 11:28:00:535 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:535 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:521 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-01-17 11:28:00:520 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-01-17 11:28:00:536 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-01-17 11:28:00:536 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-01-17 11:28:00:519 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-01-17 11:28:00:538 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-01-17 11:28:00:538 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-01-17 11:28:00:530 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-01-17 11:28:00:558 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-01-17 11:28:00:558 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-01-17 11:28:00:564 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-01-17 11:28:00:564 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-01-17 11:28:00:648 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-01-17 11:28:00:648 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-01-17 11:28:00:664 nl.zeesoft.zdk.neural.network.Network: Initialized network
Initialized network

Training network ...
2021-01-17 11:28:00:665 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 1/16 ...
2021-01-17 11:28:02:387 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 2/16 ...
2021-01-17 11:28:03:998 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 3/16 ...
2021-01-17 11:28:05:619 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 4/16 ...
2021-01-17 11:28:05:619 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training memory)
2021-01-17 11:28:07:392 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 5/16 ...
2021-01-17 11:28:09:107 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 6/16 ...
2021-01-17 11:28:10:894 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 7/16 ...
2021-01-17 11:28:12:636 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 8/16 ...
2021-01-17 11:28:12:636 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training classifiers)
2021-01-17 11:28:14:500 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.6041667, 0.9491525, 0.7457627, 0.7966102, 0.7169811, 0.9056604, 0.6730769, 0.81632656, 0.7288136, 0.8305085] (0.7767059) / [0.5, 0.9, 0.6, 0.7, 0.0, 0.8, 0.5, 0.7, 0.6, 0.7]
2021-01-17 11:28:14:500 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 9/16 ...
2021-01-17 11:28:16:461 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8108108, 0.96747965, 0.8455285, 0.86178863, 0.85470086, 0.95726496, 0.8534483, 0.91150445, 0.80487806, 0.8943089] (0.8761713) / [0.9, 0.9, 0.9, 0.8, 0.9, 1.0, 1.0, 0.9, 0.8, 0.9]
2021-01-17 11:28:16:461 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 10/16 ...
2021-01-17 11:28:18:333 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8685714, 0.973262, 0.8930481, 0.87700534, 0.9005525, 0.9668508, 0.9, 0.9378531, 0.85026735, 0.9037433] (0.90711546) / [1.0, 0.9, 1.0, 0.8, 1.0, 1.0, 1.0, 1.0, 0.9, 0.9]
2021-01-17 11:28:18:333 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 11/16 ...
2021-01-17 11:28:20:244 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.9037657, 0.9721116, 0.9123506, 0.8844622, 0.922449, 0.9714286, 0.92622954, 0.95435685, 0.87649405, 0.9163347] (0.92399824) / [1.0, 0.9, 1.0, 0.8, 1.0, 1.0, 1.0, 1.0, 0.9, 0.9]
2021-01-17 11:28:20:244 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 12/16 ...
2021-01-17 11:28:22:159 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.98046875, 0.98046875, 0.97265625, 0.9140625, 0.98046875, 0.9921875, 0.9921875, 0.98828125, 0.9375, 0.9453125] (0.96835935) / [1.0, 0.9, 1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 0.9]
2021-01-17 11:28:22:159 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 13/16 ...
2021-01-17 11:28:24:162 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.984375, 0.98046875, 0.984375, 0.91796875, 0.984375, 0.98828125, 0.9921875, 0.98828125, 0.95703125, 0.9453125] (0.9722656) / [1.0, 0.9, 1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 0.9]
2021-01-17 11:28:24:162 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 14/16 ...
2021-01-17 11:28:26:283 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.984375, 0.98046875, 0.984375, 0.92578125, 0.984375, 0.98828125, 0.99609375, 0.98828125, 0.96484375, 0.9609375] (0.97578126) / [1.0, 0.9, 1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-01-17 11:28:26:283 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 15/16 ...
2021-01-17 11:28:28:207 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.984375, 0.984375, 0.9921875, 0.93359375, 0.984375, 0.9921875, 0.99609375, 0.98828125, 0.97265625, 0.96484375] (0.97929686) / [1.0, 0.9, 1.0, 0.8, 1.0, 1.0, 1.0, 1.0, 1.0, 0.9]
2021-01-17 11:28:28:207 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 16/16 ...
2021-01-17 11:28:30:096 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.98828125, 0.984375, 0.98828125, 0.9375, 0.984375, 0.9921875, 1.0, 0.9921875, 0.96875, 0.97265625] (0.9808594) / [1.0, 0.9, 1.0, 0.8, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-01-17 11:28:30:096 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Training network took; 29431 ms, accuracies;
- BassClassifier        : 0.98828125
- CrashClassifier       : 0.984375
- HihatClassifier       : 0.98828125
- KickClassifier        : 0.9375
- NoteClassifier        : 0.984375
- OctaveClassifier      : 0.9921875
- Percussion1Classifier : 1.0
- Percussion2Classifier : 0.9921875
- RideClassifier        : 0.96875
- SnareClassifier       : 0.97265625
------------------------------------
- Average               : 0.9808594
2021-01-17 11:28:30:143 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Statistics;
- Cells             : 32768
- Proximal segments : 2048
- Proximal synapses : 827881 (active: 436503)
- Distal segments   : 3056
- Distal synapses   : 77507 (active: 63351)
Trained network

2021-01-17 11:28:30:206 nl.zeesoft.zdbd.neural.Generator: Generating sequence ...
2021-01-17 11:28:32:174 nl.zeesoft.zdbd.neural.Generator: Generated sequence
~~~~

Test results
------------
All 3 tests have been executed successfully (19 assertions).  
Total test duration: 62105 ms (total sleep duration: 0 ms).  

Memory usage and duration per test;  
 * nl.zeesoft.zdbd.test.TestEncoders: 627 Kb / 0 Mb, 204 ms
 * nl.zeesoft.zdbd.test.TestInstrumentNetwork: 886 Kb / 0 Mb, 30079 ms
 * nl.zeesoft.zdbd.test.TestGenerator: 894 Kb / 0 Mb, 31666 ms
