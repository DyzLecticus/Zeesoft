package nl.zeesoft.ztv;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import java.awt.Color;
import nl.zeesoft.zdk.messenger.Messenger;
import nl.zeesoft.zdk.thread.WorkerUnion;
import nl.zeesoft.zmmt.synthesizer.Instrument;

/**
 * ZeeTracker Visualizer. 
 * 
 * Only do initialization here.
 * Move logic into AppStates or Controls
 * 
 * Obtain a Canvas to use this within a swing application;
 * 
    Visualizer visualizer = new Visualizer();
    JmeCanvasContext context = (JmeCanvasContext) visualizer.getContext();
    Canvas canvas = context.getCanvas();
 * 
 * 
 */
public class Visualizer extends SimpleApplication {
    private static final float      BOX_SCALE_DISTANCE  = 0.5f;
    private static final float      BOX_SCALE_ON        = 0.2f;
    private static final float      BOX_SCALE_OFF       = 0.05f;
    
    private Messenger               messenger           = null;
    private WorkerUnion             union               = null;
    private SequencePlayerListener  spListener          = null;
    
    private final Geometry[][]      channelGeometry     = new Geometry[16][10];

    public Visualizer(Messenger msgr,WorkerUnion uni) {
        this.messenger = msgr;
        this.union = uni;
        initializeListener();
    }
    
    public Messenger getMessenger() {
        return messenger;
    }

    public WorkerUnion getUnion() {
        return union;
    }
    
    public SequencePlayerListener getSpListener() {
        return spListener;
    }
    
    @Override
    public void simpleInitApp() {
        int c = 0;
        for (int i = 0; i < Instrument.INSTRUMENTS.length; i++) {
            int layers = 2;
            if (Instrument.INSTRUMENTS[i].equals(Instrument.DRUMS)) {
                layers = 1;
            } else if (Instrument.INSTRUMENTS[i].equals(Instrument.ECHO)) {
                layers = 3;
            }
            Color col = Instrument.getColorForInstrument(Instrument.INSTRUMENTS[i]);
            ColorRGBA color = new ColorRGBA();
            float[] colors = col.getRGBColorComponents(null);
            color.set(colors[0],colors[1],colors[2],1.0f);
            for (int l = 0; l<layers; l++) {
                int channel = Instrument.getMidiChannelForInstrument(Instrument.INSTRUMENTS[i],l);
                if (channel>=0) {
                    c++;
                    float x = ((float) c) * BOX_SCALE_DISTANCE;
                    x = x - (8 * BOX_SCALE_DISTANCE);
                    for (int b = 0; b<10; b++) {
                        float y = ((float) b) * BOX_SCALE_DISTANCE;
                        y = y - (4 * BOX_SCALE_DISTANCE);
                        Box box = new Box(1, 1, 1);
                        channelGeometry[channel][b] = new Geometry("Box",box);
                        
                        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                        mat.setColor("Color",color);
                        channelGeometry[channel][b].setMaterial(mat);

                        Vector3f vector = new Vector3f(x,y,0.0f);
                        
                        channelGeometry[channel][b].move(vector);
                        channelGeometry[channel][b].setLocalScale(BOX_SCALE_ON);
                        
                        rootNode.attachChild(channelGeometry[channel][b]);
                    }
                }
            }
        }

    }

    @Override
    public void simpleUpdate(float tpf) {
        boolean[][] state = spListener.getState();
        for (int c = 0; c<16; c++) {
            for (int b = 0; b < 10; b++) {
                if (state[c][b]) {
                    channelGeometry[c][b].setLocalScale(BOX_SCALE_ON);
                } else {
                    channelGeometry[c][b].setLocalScale(BOX_SCALE_OFF);
                }
            }
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void initializeListener() {
        spListener = new SequencePlayerListener(messenger);
    }
}
