package nl.zeesoft.games.illuminator;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.DirectionalLight;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import nl.zeesoft.games.illuminator.model.GameModel;

/**
 * Main entry point of the game.
 * 
 * TODO: Move logic into AppStates or Controls.
 */
public class Game extends SimpleApplication {
    private GameModel           gameModel       = null;
    
    private Spatial             sceneModel      = null;
    private RigidBodyControl    scene           = null;
    private BulletAppState      bulletAppState  = null;
    
    private Player              player          = null;
    private List<Opponent>      opponents       = new ArrayList<Opponent>();

    public Game(GameModel gameModel) {
        this.gameModel = gameModel;
    }
    
    @Override
    public void start() {
        setSettings(gameModel.getAppSettings());
        setDisplayFps(gameModel.isDebug()); 
        setDisplayStatView(gameModel.isDebug());
        super.start();
    }
    
    @Override
    public void simpleInitApp() {
        gameModel.loadModels(assetManager);
        
        mouseInput.setCursorVisible(false);
        flyCam.setEnabled(false);

        bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(gameModel.isDebug());
        stateManager.attach(bulletAppState);

        loadScene();
        loadPlayer();
        addLight();
        
        // TODO: Move to app state
        spawnOpponent();
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.update();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    private void loadScene() {
        sceneModel = assetManager.loadModel("Scenes/ManyLights/Main.scene");
        sceneModel.scale(1f,.5f,1f); //Make scenery short enough to jump on. =P
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        scene = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(scene);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(scene);
    }

    private void loadPlayer() {
        player = new Player(gameModel.getPlayerModel(), inputManager, cam);
        player.initialize();
        player.getCharacterControl().setPhysicsLocation(new Vector3f(-5f,2f,5f));
        attachCharacter(player);
    }
    
    private void addLight() {
        // Add a light to make the player model visible
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-.1f, -.7f, -1f));
        rootNode.addLight(sun);
    }
    
    private void spawnOpponent() {
        Opponent opp = new Opponent(gameModel.getNewOpponentModel(assetManager));
        opp.initialize();
        opp.getCharacterControl().setPhysicsLocation(getNewSpawnLocation());
        attachCharacter(opp);
        opponents.add(opp);
    }

    private void attachCharacter(Character character) {
        rootNode.attachChild(character);
        bulletAppState.getPhysicsSpace().add(character);
    }

    private Vector3f getNewSpawnLocation() {
        Vector3f location = new Vector3f(0,5,0);
        return location;
    }
}
