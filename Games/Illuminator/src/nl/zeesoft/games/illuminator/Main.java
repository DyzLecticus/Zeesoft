package nl.zeesoft.games.illuminator;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication implements AnimEventListener {
    private final static String PLAYER_ACTION_DEFAULT   = "Idle";
    private final static String PLAYER_ACTION_WALK      = "Walk";
    
    private AnimChannel         channel                 = null;
    private AnimControl         control                 = null;
    private Node                player                  = null;

    private static Main         app                     = null;
    
    public static void main(String[] args) {
        app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        viewPort.setBackgroundColor(ColorRGBA.LightGray);
        initKeys();
        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.1f, -1f, -1).normalizeLocal());
        rootNode.addLight(dl);
        player = (Node) assetManager.loadModel("Models/SuperHero/SuperHero.j3o");
        //player.setLocalScale(0.5f);
        rootNode.attachChild(player);
        control = player.getChild("Meshes").getControl(AnimControl.class);
        control.addListener(app);
        channel = control.createChannel();
        channel.setAnim(PLAYER_ACTION_DEFAULT);

        /*
        Spatial hero = assetManager.loadModel("Models/SuperHero/SuperHero.j3o");
        hero.setLocalTranslation(new Vector3f(0f,0f,-10f));
        rootNode.attachChild(hero);

        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f,-0.5f,-0.5f)));
        sun.setColor(ColorRGBA.White);
        rootNode.addLight(sun);
        */
    }
    
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals(PLAYER_ACTION_WALK)) {
            channel.setAnim(PLAYER_ACTION_DEFAULT, 0.50f);
            channel.setLoopMode(LoopMode.DontLoop);
            channel.setSpeed(1f);
        }
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        // unused
    }

    /** Custom Keybinding: Map named actions to inputs. */
    private void initKeys() {
        inputManager.addMapping(PLAYER_ACTION_WALK, new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(actionListener,PLAYER_ACTION_WALK);
    }
    
    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals(PLAYER_ACTION_WALK) && !keyPressed) {
                if (!channel.getAnimationName().equals(PLAYER_ACTION_WALK)) {
                    channel.setAnim("Walk", 0.50f);
                    channel.setLoopMode(LoopMode.Loop);
                }
            }
        }
    };

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
