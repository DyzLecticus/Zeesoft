package nl.zeesoft.ztv;

import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import nl.zeesoft.zdk.messenger.Messenger;

import nl.zeesoft.zdk.thread.Locker;
import nl.zeesoft.zmmt.sequencer.CompositionToSequenceConvertor;
import nl.zeesoft.zmmt.sequencer.SequencePlayerSubscriber;
import nl.zeesoft.zmmt.synthesizer.Instrument;

/**
 * Listens to the SequencePlayer.
 * Maintains the current visual state.
 */
public class SequencePlayerListener extends Locker implements MetaEventListener, SequencePlayerSubscriber {
    private int             index       = -1;
    private int             pattern     = -1;
    private int             step        = -1;
    private boolean[][]     state       = new boolean[16][10];
    
    public SequencePlayerListener(Messenger msgr) {
        super(msgr);
    }
    
    @Override
    public void started() {
        // TODO: Implement
        getMessenger().debug(this,"Started sequence player");
    }

    @Override
    public void stopped() {
        // TODO: Implement
        index = -1;
        pattern = -1;
        step = -1;
        getMessenger().debug(this,"Stopped sequence player");
    }

    @Override
    public void meta(MetaMessage meta) {
        if (meta.getType()==CompositionToSequenceConvertor.MARKER) {
            String txt = new String(meta.getData());
            String[] d = txt.split(":");
            if (txt.startsWith(CompositionToSequenceConvertor.VELOCITY_MARKER)) {
                lockMe(this);
                for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
                    int layers = 2;
                    if (Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
                        layers = 1;
                    } else if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO)) {
                        layers = 3;
                    }
                    for (int l = 0; l < layers; l++) {
                        int c = Instrument.getMidiChannelForInstrument(Instrument.INSTRUMENTS[i],l);
                        if (c>=0) {
                            int value = Integer.parseInt(d[c + 1]);
                            int stat = (value * 10) / 127;
                            if (value==0) {
                                stat = -1;
                            }
                            for (int b = 0; b < 10; b++) {
                                if (b<stat) {
                                    state[c][b] = true;
                                } else {
                                    state[c][b] = false;
                                }
                            }
                        }
                    }
                }
                unlockMe(this);
            } else if (txt.startsWith(CompositionToSequenceConvertor.SEQUENCE_MARKER)) {
                lockMe(this);
                index = Integer.parseInt(d[1]);
                unlockMe(this);
            } else if (txt.startsWith(CompositionToSequenceConvertor.PATTERN_STEP_MARKER)) {
                lockMe(this);
                pattern = Integer.parseInt(d[1]);
                step = Integer.parseInt(d[2]);
                unlockMe(this);
            }
        }
    }

    public boolean[][] getState() {
        boolean[][] returnValue = new boolean[16][10];
        lockMe(this);
        for (int c = 0; c<16; c++) {
            for (int b = 0; b < 10; b++) {
                returnValue[c][b] = state[c][b];
            }
        }
        unlockMe(this);
        return returnValue;
    }
}
