package nl.zeesoft.ztv.test;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.sequencer.SequencePlayer;
import nl.zeesoft.zmmt.synthesizer.Instrument;
import nl.zeesoft.zmmt.test.MockComposition;
import nl.zeesoft.ztv.SequencePlayerListener;

public class TestSequencePlayer extends SequencePlayer {
    private Sequencer seq = null;
    
    public TestSequencePlayer(Messenger msgr, WorkerUnion uni) {
        super(msgr,uni);
    }
    
    public void initialize(SequencePlayerListener listener) {
        getMessenger().debug(this,"Initializing MIDI sequencer ...");
        try {
            seq = MidiSystem.getSequencer(true);
            if (seq!=null) {
                seq.open();
            }
        } catch (MidiUnavailableException e) {
            getMessenger().error(this,"Failed to initialize MIDI sequencer",e);
        }
        if (seq!=null && seq.isOpen()) {
            getMessenger().debug(this,"Initialized MIDI sequencer");
            seq.addMetaEventListener(listener);
            addSequencerSubscriber(listener);
            setSequencer(seq);
        }
    }
    
    @Override
    public void startWorkers() {
        MockComposition comp = new MockComposition();
        comp.getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setMidiNum(0);
        comp.getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setBaseOctave(2);
        comp.getSynthesizerConfiguration().getInstrument(Instrument.BASS1).getLayer1().setBaseVelocity(127);
        setCompositionPattern(comp,0);
        super.startWorkers();
    }

    @Override
    public void stopWorkers() {
        if (seq!=null) {
            seq.close();
        }
    }
}
