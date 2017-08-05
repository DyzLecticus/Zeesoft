package nl.zeesoft.games.illuminator;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    private Spatial             sceneModel      = null;
    private RigidBodyControl    scene           = null;
    private BulletAppState      bulletAppState  = null;
    private Player              player          = null;

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        mouseInput.setCursorVisible(false);
	flyCam.setEnabled(false);

	bulletAppState = new BulletAppState();
	stateManager.attach(bulletAppState);
	//bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        sceneModel = assetManager.loadModel("Scenes/ManyLights/Main.scene");
	sceneModel.scale(1f,.5f,1f); //Make scenery short enough to jump on. =P
	CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
	scene = new RigidBodyControl(sceneShape, 0);
	sceneModel.addControl(scene);
        rootNode.attachChild(sceneModel);
	bulletAppState.getPhysicsSpace().add(scene);

	Node playerModel = (Node) assetManager.loadModel("Models/SuperHero/SuperHero.j3o");
	playerModel.scale(0.2f);
	playerModel.setLocalTranslation(0f, -1f, 0f);
	playerModel.rotate(0f, FastMath.PI, 0f);

        player = new Player(playerModel, inputManager, cam);
	player.getCharacterControl().setPhysicsLocation(new Vector3f(-5f,2f,5f));
	rootNode.attachChild(player);
	bulletAppState.getPhysicsSpace().add(player);

        // You must add a light to make the model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-.1f, -.7f, -1f));
        rootNode.addLight(sun);
    }

    @Override
    public void simpleUpdate(float tpf) {
	player.update();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
