package nl.zeesoft.ztv.test;

import nl.zeesoft.ztv.*;
import com.jme3.system.JmeContext;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;

public class TestVisualizer extends Visualizer {
    private TestSequencePlayer sequencePlayer = null;
    
    // TODO: Create test sequence player, attach to sequence listener
    public TestVisualizer(Messenger msgr,WorkerUnion uni) {
        super(msgr,uni);
        sequencePlayer = new TestSequencePlayer(msgr,uni);
    }
        
    @Override
    public void start(JmeContext.Type contextType, boolean waitFor){
        getMessenger().setPrintDebugMessages(true);
        getMessenger().start();

        sequencePlayer.initialize(getSpListener());
        sequencePlayer.startWorkers();

        /*
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e) {
           // Ignore
        }
        */
        super.start(contextType,waitFor);

        sequencePlayer.start();
    }
    
    @Override
    public void stop(boolean waitFor){
        super.stop(waitFor);
        sequencePlayer.stop();
        sequencePlayer.stopWorkers();
        getMessenger().stop();
        getUnion().stopWorkers();
        getMessenger().whileWorking();
    }
}
