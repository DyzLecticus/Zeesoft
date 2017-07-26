package nl.zeesoft.ztv;

import com.jme3.system.AppSettings;
import nl.zeesoft.zdk.ZDKFactory;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.ztv.test.TestVisualizer;

/**
 * This is the development entry point to test the Visualizer.
 */
public class Main {
    public static void main(String[] args) {
        ZDKFactory factory = new ZDKFactory();
        Messenger msgr = factory.getMessenger();
        WorkerUnion uni = factory.getWorkerUnion(msgr);
        TestVisualizer visualizer = new TestVisualizer(msgr,uni);
        AppSettings settings = new AppSettings(true);
        settings.setTitle("ZeeTracker Visualizer");
        visualizer.setSettings(settings);
        visualizer.start();
    }
}
