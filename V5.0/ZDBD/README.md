Zeesoft MidiDreamer
===================
Zeesoft MidiDreamer is an application that can be used to generate MIDI using HTM.  
Click [here](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/releases/MidiDreamer0.9.0.jar) to download the latest MidiDreamer release (8 MB).  
**Please note** that this is an executable jar file that requires Java 1.8 and 2 GB memory to run.  

**Workflow**  
 * Define a sequence of up to 4 drum, bass and chord stab MIDI patterns  
 * Train an HTM network to learn the sequence  
 * Generate specific variations of the sequence using the trained network  
 * Use the sequencer to continuously play and generate new sequences  
 * Use the arpeggiator to generate a unique solo on top  
 * Record the performance as MIDI and/or WAV audio  

**Example**  
Given [this](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/resources/DemoTrainingSequence.wav) training sequence one can create [this](https://github.com/DyzLecticus/Zeesoft/raw/master/V5.0/ZDBD/resources/DemoRecording.wav) recording.      

**Architecture**  
MidiDreamer is a locally running Java application that exposes control over its functionality through a custom HTTP server.  
It uses the Java MIDI Syntesizer for audio synthesis and a custom Sequencer implementation for sequencing.  
[ZeeTracker](https://github.com/DyzLecticus/Zeesoft/tree/master/ZeeTracker#zeetracker) sound fonts are used to provide modern electronic sounds.  
The [Zeesoft Development Kit](https://github.com/DyzLecticus/Zeesoft/tree/master/V5.0/ZDK#zeesoft-development-kit) provides high level components for this application.  

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
2021-02-21 15:11:41:263 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-02-21 15:11:41:267 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 15:11:41:267 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 15:11:41:268 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 15:11:41:267 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 15:11:41:270 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:270 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:270 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:271 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:271 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:272 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 15:11:41:273 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 15:11:41:273 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 15:11:41:274 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:274 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:275 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:275 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:277 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:281 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 15:11:41:290 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 15:11:41:295 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 15:11:41:295 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 15:11:41:322 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:272 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 15:11:41:323 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 15:11:41:323 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:323 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:326 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:328 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 15:11:41:331 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 15:11:41:332 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 15:11:41:332 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 15:11:41:332 nl.zeesoft.zdk.neural.processors.TemporalMemory: Resetting TemporalMemory connections ...
2021-02-21 15:11:41:345 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:345 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:360 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 15:11:41:361 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:362 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:363 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 15:11:41:363 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:11:41:363 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:364 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:395 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:11:41:401 nl.zeesoft.zdk.neural.processors.TemporalMemory: Reset connections
2021-02-21 15:11:42:403 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 15:11:42:405 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 15:11:42:408 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 15:11:42:408 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 15:11:43:522 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 15:11:43:522 nl.zeesoft.zdk.neural.processors.SpatialPooler: Resetting SpatialPooler connections ...
2021-02-21 15:11:43:829 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 15:11:43:832 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 15:11:43:834 nl.zeesoft.zdk.neural.processors.SpatialPooler: Reset connections
2021-02-21 15:11:43:841 nl.zeesoft.zdk.neural.network.Network: Initialized network
Initialized network

Training network ...
2021-02-21 15:11:43:845 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 1/16 ...
2021-02-21 15:11:47:675 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 2/16 ...
2021-02-21 15:11:49:894 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 3/16 ...
2021-02-21 15:11:52:085 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 4/16 ...
2021-02-21 15:11:52:085 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training memory)
2021-02-21 15:11:55:088 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 5/16 ...
2021-02-21 15:11:57:817 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 6/16 ...
2021-02-21 15:12:00:441 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 7/16 ...
2021-02-21 15:12:02:941 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 8/16 ...
2021-02-21 15:12:02:941 nl.zeesoft.zdbd.neural.NetworkTrainer: (Training classifiers)
2021-02-21 15:12:05:531 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.62222224, 0.6296296, 0.85714287, 0.82, 0.6818182, 0.74, 0.8333333, 0.59615386] (0.7225375) / [0.5, 0.7, 0.6, 0.5, 0.6, 0.5, 0.7, 0.6]
2021-02-21 15:12:05:531 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 9/16 ...
2021-02-21 15:12:07:933 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8440367, 0.96666664, 0.80508476, 0.8907563, 0.9292035, 0.9122807, 0.8611111, 0.877193, 0.83898306, 0.90677965, 0.7758621] (0.87345064) / [1.0, 0.9, 1.0, 0.9, 1.0, 0.9, 1.0, 0.9, 1.0, 0.9, 1.0]
2021-02-21 15:12:07:933 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 10/16 ...
2021-02-21 15:12:10:442 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.8959538, 0.97282606, 0.85714287, 0.92349726, 0.9491525, 0.93820226, 0.9127907, 0.9101124, 0.88461536, 0.9285714, 0.8388889] (0.91015947) / [0.9, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 0.9, 1.0, 1.0, 1.0]
2021-02-21 15:12:10:442 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 11/16 ...
2021-02-21 15:12:12:910 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.9240506, 0.9717742, 0.8902439, 0.9311741, 0.9585062, 0.95454544, 0.9364407, 0.9338843, 0.91056913, 0.9430894, 0.8729508] (0.929748) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 15:12:12:910 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 12/16 ...
2021-02-21 15:12:15:430 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.99609375, 0.98046875, 0.96875, 0.97265625, 0.984375, 0.9921875, 0.99609375, 0.98828125, 0.97265625, 0.9765625, 0.953125] (0.9801136) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 15:12:15:430 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 13/16 ...
2021-02-21 15:12:17:879 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.99609375, 0.98046875, 0.98046875, 0.98046875, 0.984375, 0.9921875, 1.0, 0.98828125, 0.98046875, 0.98046875, 0.96875] (0.9847301) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 15:12:17:880 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 14/16 ...
2021-02-21 15:12:20:236 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.99609375, 0.98046875, 0.9921875, 0.98046875, 0.984375, 0.9921875, 1.0, 0.9921875, 0.98828125, 0.98828125, 0.9765625] (0.98828125) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 15:12:20:236 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 15/16 ...
2021-02-21 15:12:22:646 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.98828125, 0.984375, 0.9921875, 0.9921875, 0.984375, 0.98828125, 1.0, 0.984375, 0.98828125, 0.98828125, 0.984375] (0.9886364) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 15:12:22:646 nl.zeesoft.zdbd.neural.NetworkTrainer: Training cycle 16/16 ...
2021-02-21 15:12:25:111 nl.zeesoft.zdbd.neural.NetworkTrainer: Accuracies (average) / trends: [0.98828125, 0.984375, 0.99609375, 0.984375, 0.984375, 0.984375, 1.0, 0.98046875, 0.9921875, 0.9921875, 0.9921875] (0.9889915) / [1.0, 0.9, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0]
2021-02-21 15:12:25:111 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Training network took; 41266 ms, accuracies;
- BassClassifier        : 0.98828125
- CrashClassifier       : 0.984375
- HihatClassifier       : 0.99609375
- KickClassifier        : 0.984375
- NoteClassifier        : 0.984375
- OctaveClassifier      : 0.984375
- Percussion1Classifier : 1.0
- Percussion2Classifier : 0.98046875
- RideClassifier        : 0.9921875
- SnareClassifier       : 0.9921875
- StabClassifier        : 0.9921875
------------------------------------
- Average               : 0.9889915
2021-02-21 15:12:25:242 nl.zeesoft.zdbd.neural.NetworkTrainer$3: Statistics;
- Cells             : 49152
- Proximal segments : 3072
- Proximal synapses : 1200473 (active: 642902)
- Distal segments   : 4494
- Distal synapses   : 115687 (active: 93974)
Trained network
2021-02-21 15:12:25:304 nl.zeesoft.zdk.neural.network.Network: Saving network ...
2021-02-21 15:12:25:304 nl.zeesoft.zdk.neural.network.Network$7: Writing dist/Configuration.txt ...
2021-02-21 15:12:25:304 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Merger.txt ...
2021-02-21 15:12:25:307 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Percussion1Classifier.txt ...
2021-02-21 15:12:25:307 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/RideClassifier.txt ...
2021-02-21 15:12:25:307 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Merger.txt ...
2021-02-21 15:12:25:345 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/HihatClassifier.txt ...
2021-02-21 15:12:25:346 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/CrashClassifier.txt ...
2021-02-21 15:12:25:361 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/SnareClassifier.txt ...
2021-02-21 15:12:25:379 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Merger.txt ...
2021-02-21 15:12:25:380 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Percussion2Classifier.txt ...
2021-02-21 15:12:25:383 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/BassClassifier.txt ...
2021-02-21 15:12:25:387 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/StabClassifier.txt ...
2021-02-21 15:12:25:392 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/OctaveClassifier.txt ...
2021-02-21 15:12:25:399 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/KickClassifier.txt ...
2021-02-21 15:12:25:399 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/NoteClassifier.txt ...
2021-02-21 15:12:25:402 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Memory.txt ...
2021-02-21 15:12:25:408 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Memory.txt ...
2021-02-21 15:12:25:408 nl.zeesoft.zdk.neural.network.Network$9: Writing dist/PreviousIO.txt ...
2021-02-21 15:12:25:408 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group3Pooler.txt ...
2021-02-21 15:12:25:455 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Pooler.txt ...
2021-02-21 15:12:25:471 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group1Pooler.txt ...
2021-02-21 15:12:25:486 nl.zeesoft.zdk.neural.network.Network$8: Writing dist/Group2Memory.txt ...
2021-02-21 15:12:28:367 nl.zeesoft.zdk.neural.network.Network: Saved network
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
2021-02-21 15:12:28:673 nl.zeesoft.zdk.neural.network.Network$1: Configuration;
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
2021-02-21 15:12:28:673 nl.zeesoft.zdk.neural.network.Network: Initializing network ...
2021-02-21 15:12:28:679 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 15:12:28:679 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 15:12:28:679 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 15:12:28:679 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 15:12:28:681 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 15:12:28:682 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 15:12:28:682 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 15:12:28:683 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 15:12:28:688 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 15:12:28:690 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 15:12:28:691 nl.zeesoft.zdk.neural.processors.Merger: Initializing Merger ...
2021-02-21 15:12:28:691 nl.zeesoft.zdk.neural.processors.Merger: Initialized
2021-02-21 15:12:28:691 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initializing SpatialPooler ...
2021-02-21 15:12:28:698 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initializing TemporalMemory ...
2021-02-21 15:12:28:700 nl.zeesoft.zdk.neural.processors.TemporalMemory: Initialized
2021-02-21 15:12:28:779 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:779 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:780 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:780 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:780 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:780 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:796 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 15:12:28:805 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 15:12:28:805 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:805 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:806 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:806 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:806 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:806 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:807 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:807 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:807 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:807 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:807 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:807 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:808 nl.zeesoft.zdk.neural.processors.SpatialPooler: Initialized
2021-02-21 15:12:28:808 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:808 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:808 nl.zeesoft.zdk.neural.processors.Classifier: Initializing Classifier ...
2021-02-21 15:12:28:808 nl.zeesoft.zdk.neural.processors.Classifier: Initialized
2021-02-21 15:12:28:809 nl.zeesoft.zdk.neural.network.Network: Initialized network
2021-02-21 15:12:28:809 nl.zeesoft.zdk.neural.network.Network: Loading network ...
2021-02-21 15:12:28:809 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Memory.txt ...
2021-02-21 15:12:28:809 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Merger.txt ...
2021-02-21 15:12:28:809 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Merger.txt ...
2021-02-21 15:12:28:809 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Memory.txt ...
2021-02-21 15:12:28:809 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Merger.txt ...
2021-02-21 15:12:28:811 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/CrashClassifier.txt ...
2021-02-21 15:12:28:843 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Memory.txt ...
2021-02-21 15:12:28:865 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/KickClassifier.txt ...
2021-02-21 15:12:28:930 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/OctaveClassifier.txt ...
2021-02-21 15:12:28:982 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group1Pooler.txt ...
2021-02-21 15:12:29:170 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/BassClassifier.txt ...
2021-02-21 15:12:29:185 nl.zeesoft.zdk.neural.network.Network$9: Reading dist/PreviousIO.txt ...
2021-02-21 15:12:29:185 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Percussion1Classifier.txt ...
2021-02-21 15:12:29:216 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group3Pooler.txt ...
2021-02-21 15:12:29:232 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/StabClassifier.txt ...
2021-02-21 15:12:29:232 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/SnareClassifier.txt ...
2021-02-21 15:12:29:238 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/RideClassifier.txt ...
2021-02-21 15:12:29:248 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Percussion2Classifier.txt ...
2021-02-21 15:12:29:271 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/HihatClassifier.txt ...
2021-02-21 15:12:29:271 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/NoteClassifier.txt ...
2021-02-21 15:12:29:271 nl.zeesoft.zdk.neural.network.Network$8: Reading dist/Group2Pooler.txt ...
2021-02-21 15:12:37:493 nl.zeesoft.zdk.neural.network.Network: Loaded network

2021-02-21 15:12:37:556 nl.zeesoft.zdbd.neural.Generator: Generating sequence ...
2021-02-21 15:12:40:379 nl.zeesoft.zdbd.neural.Generator: Generated sequence
~~~~

Test results
------------
All 3 tests have been executed successfully (21 assertions).  
Total test duration: 59650 ms (total sleep duration: 0 ms).  

Memory usage and duration per test;  
 * nl.zeesoft.zdbd.test.TestEncoders: 642 Kb / 0 Mb, 291 ms
 * nl.zeesoft.zdbd.test.TestInstrumentNetwork: 906 Kb / 0 Mb, 47216 ms
 * nl.zeesoft.zdbd.test.TestGenerator: 917 Kb / 0 Mb, 11998 ms
