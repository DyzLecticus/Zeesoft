Zeesoft MidiDreamer
===================
Zeesoft MidiDreamer is an application that can be used to generate MIDI using HTM.  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/releases/MidiDreamer0.9.0.zip) to download the latest MidiDreamer release (8 MB).  
**Please note** that this is an executable jar file that requires Java 1.8 and 2 GB memory to run.  

**Workflow**  
 * Define a sequence of up to 4 drum, bass and chord stab MIDI patterns  
 * Train an HTM network to learn the sequence  
 * Generate specific variations of the sequence using the trained network  
 * Use the sequencer to continuously play and generate new sequences  
 * Use the arpeggiator to generate a unique solo on top  
 * Record the performance as MIDI and/or WAV audio  

**Architecture**  
MidiDreamer is a locally running Java application that exposes control over its functionality through a custom HTTP server.  
It uses the Java MIDI Syntesizer for audio synthesis and a custom Sequencer implementation for sequencing.  
[ZeeTracker](https://github.com/DyzLecticus/Zeesoft/tree/master/ZeeTracker#zeetracker) sound fonts are used to provide modern electronic sounds.  
The [ZeesoftDevelopmentKit](https://github.com/DyzLecticus/Zeesoft/tree/master/V5.0/ZDK#zeesoft-development-kit) provides high level components for this application.  

**Screen shots**  
<img alt="Main application" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/App.bmp">

<img alt="Chord changes editing panel" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/ChordChanges.bmp">

<img alt="Training sequence editing panel" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/TrainingSequence.bmp">

<img alt="Network trainer and sequence generators" src="https://raw.githubusercontent.com/DyzLecticus/Zeesoft/master/V5.0/ZDBD/resources/screenshots/NetworkAndGenerators.bmp">

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
// Get the group 3 SDRs
sdrs = pattern.getSDRsForGroup(3,rythm.getStepsPerPattern());
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
2021-02-21 14:53:35:281 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-02-21 14:53:35:297 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:53:35:297 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:53:35:298 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:53:35:298 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:53:35:297 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:53:35:299 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:53:35:306 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:53:35:307 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:307 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:53:35:308 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:308 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:310 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:53:35:310 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:311 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:53:35:312 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:53:35:312 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:313 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:317 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:317 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:318 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:53:35:319 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:320 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:321 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:339 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:366 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:53:35:367 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 14:53:35:369 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:369 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:377 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:53:35:377 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 14:53:35:384 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 14:53:35:391 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 14:53:35:395 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:53:35:396 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 14:53:35:398 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 14:53:35:403 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:403 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:403 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:404 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:404 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:404 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:35:405 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:53:35:405 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:53:36:497 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:53:36:497 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 14:53:37:506 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:53:37:506 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 14:53:37:514 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:53:37:514 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 14:53:37:880 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 14:53:37:880 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 14:53:37:880 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 14:53:37:896 nl.zeesoft.zdk.neural.network.Network: Initialized network
Initialized network

Training network ...
2021-02-21 14:53:37:901 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 1/16 ...
2021-02-21 14:53:41:556 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 2/16 ...
2021-02-21 14:53:43:744 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 3/16 ...
2021-02-21 14:53:45:872 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 4/16 ...
2021-02-21 14:53:45:872 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training memory)
2021-02-21 14:53:48:879 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 5/16 ...
2021-02-21 14:53:51:335 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 6/16 ...
2021-02-21 14:53:53:810 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 7/16 ...
2021-02-21 14:53:56:203 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 8/16 ...
2021-02-21 14:53:56:204 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training classifiers)
2021-02-21 14:53:58:821 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.9661017, 0.7241379, 0.86440676, 0.8148148, 0.8363636, 0.7758621, 0.85, 0.6818182] (0.8141881) / [0.9, 0.9, 0.9, 0.3, 0.8, 0.8, 0.7, 0.6]
2021-02-21 14:53:58:821 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 9/16 ...
2021-02-21 14:54:01:406 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.79831934, 0.9756098, 0.8606557, 0.91056913, 0.89830506, 0.9237288, 0.8347826, 0.9159664, 0.8606557, 0.9032258, 0.8518519] (0.8848791) / [1.0, 0.9, 1.0, 0.9, 0.9, 1.0, 1.0, 1.0, 0.9, 0.9, 1.0]
2021-02-21 14:54:01:406 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 10/16 ...
2021-02-21 14:54:04:015 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8579235, 0.973262, 0.9032258, 0.93582886, 0.9175824, 0.9505494, 0.89385474, 0.9453552, 0.89784944, 0.93085104, 0.90697676] (0.9193871) / [0.9, 0.9, 1.0, 1.0, 0.8, 1.0, 1.0, 1.0, 1.0, 0.9, 1.0]
2021-02-21 14:54:04:015 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 11/16 ...
2021-02-21 14:54:06:637 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.88663965, 0.9800797, 0.92, 0.9402391, 0.93495935, 0.95528454, 0.91769546, 0.94736844, 0.92, 0.9484127, 0.9322034] (0.93480754) / [1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 14:54:06:637 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 12/16 ...
2021-02-21 14:54:09:190 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.9765625, 0.98828125, 0.98046875, 0.9609375, 0.97265625, 0.98046875, 0.984375, 0.98046875, 0.96484375, 0.98046875, 0.9921875] (0.97833806) / [1.0, 1.0, 0.9, 0.9, 1.0, 1.0, 1.0, 1.0, 0.9, 1.0, 1.0]
2021-02-21 14:54:09:190 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 13/16 ...
2021-02-21 14:54:11:790 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.9765625, 0.9921875, 0.98046875, 0.96484375, 0.9765625, 0.984375, 0.98828125, 0.98046875, 0.9765625, 0.98828125, 1.0] (0.98259944) / [1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 14:54:11:791 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 14/16 ...
2021-02-21 14:54:14:330 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.984375, 0.99609375, 0.984375, 0.9609375, 0.984375, 0.984375, 0.984375, 0.98046875, 0.984375, 0.984375, 0.99609375] (0.9840199) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.9, 1.0]
2021-02-21 14:54:14:330 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 15/16 ...
2021-02-21 14:54:16:870 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.9921875, 0.99609375, 0.984375, 0.96484375, 0.984375, 0.9921875, 0.98828125, 0.9921875, 0.98046875, 0.984375, 0.99609375] (0.9868608) / [1.0, 1.0, 0.9, 0.9, 1.0, 1.0, 1.0, 1.0, 0.9, 1.0, 1.0]
2021-02-21 14:54:16:870 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 16/16 ...
2021-02-21 14:54:19:376 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.98828125, 0.99609375, 0.98828125, 0.96875, 0.98046875, 0.99609375, 0.984375, 0.99609375, 0.984375, 0.98046875, 0.99609375] (0.98721594) / [1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 14:54:19:376 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Training network took; 41475 ms, accuracies;
- BassClassifier        : 0.98828125
- CrashClassifier       : 0.99609375
- HihatClassifier       : 0.98828125
- KickClassifier        : 0.96875
- NoteClassifier        : 0.98046875
- OctaveClassifier      : 0.99609375
- Percussion1Classifier : 0.984375
- Percussion2Classifier : 0.99609375
- RideClassifier        : 0.984375
- SnareClassifier       : 0.98046875
- StabClassifier        : 0.99609375
------------------------------------
- Average               : 0.98721594
2021-02-21 14:54:19:553 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Statistics;
- Cells             : 49152
- Proximal segments : 3072
- Proximal synapses : 1202169 (active: 642623)
- Distal segments   : 4145
- Distal synapses   : 101982 (active: 86304)
Trained network
2021-02-21 14:54:19:595 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2021-02-21 14:54:19:595 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/Configuration.txt ...
2021-02-21 14:54:19:605 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Merger.txt ...
2021-02-21 14:54:19:607 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Merger.txt ...
2021-02-21 14:54:19:607 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Merger.txt ...
2021-02-21 14:54:19:608 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/RideClassifier.txt ...
2021-02-21 14:54:19:623 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/BassClassifier.txt ...
2021-02-21 14:54:19:634 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/KickClassifier.txt ...
2021-02-21 14:54:19:637 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/SnareClassifier.txt ...
2021-02-21 14:54:19:648 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Percussion2Classifier.txt ...
2021-02-21 14:54:19:648 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/HihatClassifier.txt ...
2021-02-21 14:54:19:658 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/StabClassifier.txt ...
2021-02-21 14:54:19:665 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Memory.txt ...
2021-02-21 14:54:19:737 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Memory.txt ...
2021-02-21 14:54:19:775 nl.zeesoft.zdk.neural.network.Network$9: Writing dist/PreviousIO.txt ...
2021-02-21 14:54:19:779 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Pooler.txt ...
2021-02-21 14:54:19:779 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Pooler.txt ...
2021-02-21 14:54:19:779 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Pooler.txt ...
2021-02-21 14:54:19:819 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/NoteClassifier.txt ...
2021-02-21 14:54:19:817 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/OctaveClassifier.txt ...
2021-02-21 14:54:19:815 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/CrashClassifier.txt ...
2021-02-21 14:54:19:814 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Percussion1Classifier.txt ...
2021-02-21 14:54:19:814 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Memory.txt ...
2021-02-21 14:54:22:826 nl.zeesoft.zdk.neural.network.Network: Saved network
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
2021-02-21 14:54:23:194 nl.zeesoft.zdk.neural.network.Network$1: Configuration;
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
2021-02-21 14:54:23:194 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-02-21 14:54:23:195 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:54:23:195 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:54:23:196 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:54:23:196 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:54:23:196 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:54:23:196 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:54:23:197 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:54:23:198 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:54:23:205 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 14:54:23:205 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 14:54:23:205 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:206 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:208 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 14:54:23:215 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:54:23:218 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:54:23:308 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:54:23:291 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 14:54:23:311 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 14:54:23:291 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:321 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:322 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:322 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:291 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:326 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:54:23:327 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:327 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:327 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:328 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:328 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:328 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:328 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:328 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:329 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:329 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:329 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:329 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:330 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 14:54:23:330 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:330 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 14:54:23:334 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 14:54:23:335 nl.zeesoft.zdk.neural.network.Network: Initialized network
2021-02-21 14:54:23:335 nl.zeesoft.zdk.neural.network.Network: Loading network ...
2021-02-21 14:54:23:335 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Merger.txt ...
2021-02-21 14:54:23:335 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Merger.txt ...
2021-02-21 14:54:23:335 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Memory.txt ...
2021-02-21 14:54:23:336 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Merger.txt ...
2021-02-21 14:54:23:336 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/KickClassifier.txt ...
2021-02-21 14:54:23:337 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Memory.txt ...
2021-02-21 14:54:23:338 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Memory.txt ...
2021-02-21 14:54:23:338 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/BassClassifier.txt ...
2021-02-21 14:54:23:339 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/SnareClassifier.txt ...
2021-02-21 14:54:23:339 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Pooler.txt ...
2021-02-21 14:54:23:415 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Pooler.txt ...
2021-02-21 14:54:23:483 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/StabClassifier.txt ...
2021-02-21 14:54:23:714 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Pooler.txt ...
2021-02-21 14:54:23:732 nl.zeesoft.zdk.neural.network.Network$9: Reading dist/PreviousIO.txt ...
2021-02-21 14:54:23:735 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/HihatClassifier.txt ...
2021-02-21 14:54:23:757 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/OctaveClassifier.txt ...
2021-02-21 14:54:23:773 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/CrashClassifier.txt ...
2021-02-21 14:54:23:791 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Percussion1Classifier.txt ...
2021-02-21 14:54:23:808 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/RideClassifier.txt ...
2021-02-21 14:54:23:824 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/NoteClassifier.txt ...
2021-02-21 14:54:23:843 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Percussion2Classifier.txt ...
2021-02-21 14:54:32:294 nl.zeesoft.zdk.neural.network.Network: Loaded network

2021-02-21 14:54:32:345 nl.zeesoft.zdbd.neural.Generator: Generating sequence ...
2021-02-21 14:54:35:438 nl.zeesoft.zdbd.neural.Generator: Generated sequence
~~~~

Test results
------------
All 3 tests have been executed successfully (21 assertions).  
Total test duration: 60670 ms (total sleep duration: 0 ms).  

Memory usage and duration per test;  
 * nl.zeesoft.zdbd.test.TestEncoders: 643 Kb / 0 Mb, 172 ms
 * nl.zeesoft.zdbd.test.TestInstrumentNetwork: 907 Kb / 0 Mb, 47647 ms
 * nl.zeesoft.zdbd.test.TestGenerator: 919 Kb / 0 Mb, 12581 ms
