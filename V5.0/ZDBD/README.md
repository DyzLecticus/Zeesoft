Zeesoft MidiDreamer
===================
Zeesoft MidiDreamer is an application that can be used to generate MIDI using HTM.  

This library depends on the [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V5.0/ZDK/).  

**Workflow**  
 * Define a sequence of up to 4 drum, bass and chord stab MIDI patterns  
 * Train an HTM network to learn the sequence  
 * Generate specific variations of the sequence using the trained network  
 * Use the sequencer to continuously play and generate new sequences  
 * Use the arpeggiator to generate a unique solo on top  
 * Record the performance as MIDI and/or WAV audio  

**Architecture**  
MidiDreamer is a locally running Java application that exposes control over its functionality through a custom HTTP web server.  
It uses the Java MIDI Syntesizer for audio synthesis and a custom Sequencer implementation for sequencing.  
[ZeeTracker](https://github.com/DyzLecticus/Zeesoft/tree/master/ZeeTracker#zeetracker) sound fonts are used to provide modern electronic sounds.  

**Screen shots**  
<img alt="Main application" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/App.bmp">

<img alt="Chord changes editing panel" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/ChordChanges.bmp">

<img alt="Training sequence editing panel" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/TrainingSequence.bmp">

<img alt="Network trainer and sequence generators" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/NetworkAndGenerators.bmp">

Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/releases/MidiDreamer0.9.0.zip) to download the latest MidiDreamer release (8 MB).  
**Please note** that this is an executable jar file that requires Java 1.8 and 2 GB memory to run.  

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

Group 2 pattern (9*8);
(Percussion, Bass, Octave, Note)
01: 110000000000000000110000000000000000000000000000000011000011000000
02: 110000000000000000110000000000000000000000000000000011000011000000
03: 000000110000000000000011000000000000000000000000000011000011000000
04: 000000000000110000000011000000000000000000000000000011000011000000
05: 110000000000000000110000000000000000000000000000000011000011000000
06: 110000000000000000110000000000000000000000000000000011000011000000
07: 000011000000000000000011000000000000000000000000000000110011000000
08: 110000000000000000000011000000000000000000000000000011000011000000
09: 110000000000000000110000000000000000000000000000000011000011000000
10: 110000000000000000110000000000000000000000000000000011000011000000
11: 000000110000000000000011000000000000000000000000000011000011000000
12: 000000000000110000000011000000000000000000000000000000110011000000
13: 110000000000000000110000000000000000000000000000000011000011000000
14: 110000000000000000110000000000000000000000000000000011000011000000
15: 000011000000000000000000001100000000000000000000000011000011000000
16: 110000000000000000110000000000000000000000000000000011000011000000

Group 3 pattern (6*6);
(Stab)
01: 1100000000000000000000000000000000
02: 1100000000000000000000000000000000
03: 0011000000000000000000000000000000
04: 1100000000000000000000000000000000
05: 0000110000000000000000000000000000
06: 1100000000000000000000000000000000
07: 1100000000000000000000000000000000
08: 0000110000000000000000000000000000
09: 1100000000000000000000000000000000
10: 1100000000000000000000000000000000
11: 1100000000000000000000000000000000
12: 1100000000000000000000000000000000
13: 1100000000000000000000000000000000
14: 1100000000000000000000000000000000
15: 1100000000000000000000000000000000
16: 1100000000000000000000000000000000
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
<- Group3
Layer: 0
  Group1Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group1
  -> SDR: 24*24
  Group2Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group2
  -> SDR: 25*25
  Group3Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group3
  -> SDR: 24*24
Layer: 1
  Group1Pooler: SpatialPoolerConfig: 24*24*1
  <- 0 = Group1Merger/0
  -> 0 = Active columns: 32*32, on bits: 20
  Group2Pooler: SpatialPoolerConfig: 25*25*1
  <- 0 = Group2Merger/0
  -> 0 = Active columns: 32*32, on bits: 20
  Group3Pooler: SpatialPoolerConfig: 24*24*1
  <- 0 = Group3Merger/0
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
  Group3Memory: TemporalMemoryConfig: 32*32*16
  <- 0 = Group3Pooler/0
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
  StabClassifier: ClassifierConfig: 512*32 (Stab)
  <- 0 = Group3Memory/0
  <- 1 = Group3
  -> 0 = Classifications: 1*1

Initializing network ...
2021-02-21 14:39:40:400 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-02-21 14:39:40:415 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:39:40:416 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:39:40:415 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:39:40:416 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:39:40:416 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:39:40:416 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:39:40:418 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:39:40:419 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:39:40:418 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:39:40:421 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:39:40:422 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:39:40:422 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:422 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:39:40:423 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:424 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:425 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:425 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:425 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:425 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:428 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:428 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:429 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:431 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:431 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:433 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:433 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:439 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:439 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:477 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:492 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:492 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:39:40:498 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:499 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:40:504 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:39:40:504 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 14:39:40:538 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:39:40:558 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 14:39:40:565 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:39:40:565 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 14:39:40:568 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 14:39:40:571 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 14:39:41:407 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:39:41:407 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 14:39:41:432 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:39:41:432 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 14:39:42:724 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:39:42:727 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 14:39:42:757 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 14:39:42:712 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:39:43:026 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 14:39:43:041 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 14:39:43:041 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 14:39:43:057 nl.zeesoft.zdk.neural.network.Network: Initialized network
Initialized network

Training network ...
2021-02-21 14:39:43:062 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 1/16 ...
2021-02-21 14:39:46:603 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 2/16 ...
2021-02-21 14:39:48:754 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 3/16 ...
2021-02-21 14:39:50:859 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 4/16 ...
2021-02-21 14:39:50:859 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training memory)
2021-02-21 14:39:53:741 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 5/16 ...
2021-02-21 14:39:56:237 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 6/16 ...
2021-02-21 14:39:58:629 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 7/16 ...
2021-02-21 14:40:01:001 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 8/16 ...
2021-02-21 14:40:01:001 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training classifiers)
2021-02-21 14:40:03:531 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.5660377, 0.9491525, 0.6727273, 0.82758623, 0.7818182, 0.9464286, 0.71153843, 0.83928573, 0.8245614, 0.6530612] (0.7772198) / [0.6, 0.9, 0.4, 0.8, 0.1, 0.9, 0.8, 0.6, 0.7, 0.7]
2021-02-21 14:40:03:531 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 9/16 ...
2021-02-21 14:40:05:842 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.7863248, 0.9756098, 0.8487395, 0.89344263, 0.88235295, 0.96666664, 0.8534483, 0.9166667, 0.8666667, 0.90909094, 0.8318584] (0.8846243) / [1.0, 1.0, 1.0, 0.9, 0.9, 1.0, 1.0, 1.0, 0.9, 1.0, 1.0]
2021-02-21 14:40:05:842 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 10/16 ...
2021-02-21 14:40:08:201 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8563536, 0.98395723, 0.90163934, 0.9247312, 0.91803277, 0.97282606, 0.90555555, 0.9402174, 0.9130435, 0.9297297, 0.8926554] (0.92170376) / [1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 14:40:08:201 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 11/16 ...
2021-02-21 14:40:10:627 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8857143, 0.98804784, 0.9271255, 0.944, 0.9352227, 0.9758065, 0.92622954, 0.9435484, 0.9354839, 0.935743, 0.92116183] (0.9380076) / [1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 14:40:10:627 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 12/16 ...
2021-02-21 14:40:13:009 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.9765625, 0.99609375, 1.0, 0.984375, 0.98046875, 0.98828125, 0.98828125, 0.98046875, 0.9921875, 0.97265625, 0.9921875] (0.9865057) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 14:40:13:009 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 13/16 ...
2021-02-21 14:40:15:419 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.984375, 0.99609375, 1.0, 0.99609375, 0.984375, 0.9921875, 0.99609375, 0.984375, 0.99609375, 0.97265625, 1.0] (0.9911222) / [1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 14:40:15:419 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Training network took; 32357 ms, accuracies;
- BassClassifier        : 0.984375
- CrashClassifier       : 0.99609375
- HihatClassifier       : 1.0
- KickClassifier        : 0.99609375
- NoteClassifier        : 0.984375
- OctaveClassifier      : 0.9921875
- Percussion1Classifier : 0.99609375
- Percussion2Classifier : 0.984375
- RideClassifier        : 0.99609375
- SnareClassifier       : 0.97265625
- StabClassifier        : 1.0
------------------------------------
- Average               : 0.9911222
2021-02-21 14:40:15:591 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Statistics;
- Cells             : 49152
- Proximal segments : 3072
- Proximal synapses : 1225349 (active: 668172)
- Distal segments   : 4091
- Distal synapses   : 105245 (active: 85234)
Trained network
2021-02-21 14:40:15:622 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2021-02-21 14:40:15:622 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Merger.txt ...
2021-02-21 14:40:15:622 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/Configuration.txt ...
2021-02-21 14:40:15:622 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Merger.txt ...
2021-02-21 14:40:15:625 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/RideClassifier.txt ...
2021-02-21 14:40:15:625 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Merger.txt ...
2021-02-21 14:40:15:626 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Percussion1Classifier.txt ...
2021-02-21 14:40:15:652 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/BassClassifier.txt ...
2021-02-21 14:40:15:661 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/CrashClassifier.txt ...
2021-02-21 14:40:15:670 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Percussion2Classifier.txt ...
2021-02-21 14:40:15:670 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/KickClassifier.txt ...
2021-02-21 14:40:15:680 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/NoteClassifier.txt ...
2021-02-21 14:40:15:686 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/StabClassifier.txt ...
2021-02-21 14:40:15:692 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/SnareClassifier.txt ...
2021-02-21 14:40:15:693 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Memory.txt ...
2021-02-21 14:40:15:700 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Memory.txt ...
2021-02-21 14:40:15:625 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/HihatClassifier.txt ...
2021-02-21 14:40:15:732 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Pooler.txt ...
2021-02-21 14:40:15:748 nl.zeesoft.zdk.neural.network.Network$9: Writing dist/PreviousIO.txt ...
2021-02-21 14:40:15:748 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/OctaveClassifier.txt ...
2021-02-21 14:40:15:764 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Pooler.txt ...
2021-02-21 14:40:15:768 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Pooler.txt ...
2021-02-21 14:40:15:768 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Memory.txt ...
2021-02-21 14:40:18:905 nl.zeesoft.zdk.neural.network.Network: Saved network
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
2021-02-21 14:40:19:212 nl.zeesoft.zdk.neural.network.Network$1: Configuration;
Inputs: 
<- Context
<- Group1
<- Group2
<- Group3
Layer: 0
  Group1Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group1
  -> SDR: 24*24
  Group2Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group2
  -> SDR: 25*25
  Group3Merger: MergerConfig: ?
  <- 0 = Context
  <- 1 = Group3
  -> SDR: 24*24
Layer: 1
  Group1Pooler: SpatialPoolerConfig: 24*24*1
  <- 0 = Group1Merger/0
  -> 0 = Active columns: 32*32, on bits: 20
  Group2Pooler: SpatialPoolerConfig: 25*25*1
  <- 0 = Group2Merger/0
  -> 0 = Active columns: 32*32, on bits: 20
  Group3Pooler: SpatialPoolerConfig: 24*24*1
  <- 0 = Group3Merger/0
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
  Group3Memory: TemporalMemoryConfig: 32*32*16
  <- 0 = Group3Pooler/0
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
  StabClassifier: ClassifierConfig: 512*32 (Stab)
  <- 0 = Group3Memory/0
  <- 1 = Group3
  -> 0 = Classifications: 1*1
2021-02-21 14:40:19:212 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-02-21 14:40:19:212 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:40:19:212 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:40:19:212 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:40:19:220 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:40:19:220 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:40:19:220 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:40:19:221 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:40:19:222 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:40:19:229 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:40:19:230 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:40:19:231 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:40:19:242 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:40:19:244 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:40:19:339 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:339 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:339 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:40:19:341 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:40:19:341 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:341 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:342 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:40:19:358 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:40:19:360 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:360 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:361 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:361 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:362 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:362 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:362 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:362 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:362 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:362 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:362 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:362 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:363 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:363 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:363 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:364 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:364 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:40:19:364 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:40:19:371 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:40:19:372 nl.zeesoft.zdk.neural.network.Network: Initialized network
2021-02-21 14:40:19:372 nl.zeesoft.zdk.neural.network.Network: Loading network ...
2021-02-21 14:40:19:372 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Merger.txt ...
2021-02-21 14:40:19:372 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Merger.txt ...
2021-02-21 14:40:19:372 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Merger.txt ...
2021-02-21 14:40:19:372 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Memory.txt ...
2021-02-21 14:40:19:372 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Memory.txt ...
2021-02-21 14:40:19:372 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/KickClassifier.txt ...
2021-02-21 14:40:19:383 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/SnareClassifier.txt ...
2021-02-21 14:40:19:472 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Memory.txt ...
2021-02-21 14:40:19:485 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Pooler.txt ...
2021-02-21 14:40:19:493 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Pooler.txt ...
2021-02-21 14:40:19:509 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/NoteClassifier.txt ...
2021-02-21 14:40:19:517 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/BassClassifier.txt ...
2021-02-21 14:40:19:685 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Pooler.txt ...
2021-02-21 14:40:19:696 nl.zeesoft.zdk.neural.network.Network$9: Reading dist/PreviousIO.txt ...
2021-02-21 14:40:19:712 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Percussion1Classifier.txt ...
2021-02-21 14:40:19:715 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/OctaveClassifier.txt ...
2021-02-21 14:40:19:720 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/HihatClassifier.txt ...
2021-02-21 14:40:19:750 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/CrashClassifier.txt ...
2021-02-21 14:40:19:765 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Percussion2Classifier.txt ...
2021-02-21 14:40:19:766 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/StabClassifier.txt ...
2021-02-21 14:40:19:781 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/RideClassifier.txt ...
2021-02-21 14:40:28:233 nl.zeesoft.zdk.neural.network.Network: Loaded network

2021-02-21 14:40:28:295 nl.zeesoft.zdbd.neural.Generator: Generating sequence ...
2021-02-21 14:40:30:911 nl.zeesoft.zdbd.neural.Generator: Generated sequence
~~~~

Test results
------------
All 3 tests have been executed successfully (21 assertions).  
Total test duration: 51074 ms (total sleep duration: 0 ms).  

Memory usage and duration per test;  
 * nl.zeesoft.zdbd.test.TestEncoders: 642 Kb / 0 Mb, 328 ms
 * nl.zeesoft.zdbd.test.TestInstrumentNetwork: 906 Kb / 0 Mb, 38630 ms
 * nl.zeesoft.zdbd.test.TestGenerator: 919 Kb / 0 Mb, 11990 ms
