package nl.zeesoft.games.illuminator;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import nl.zeesoft.games.illuminator.controls.Opponent;
import nl.zeesoft.games.illuminator.controls.Player;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.Listener;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import nl.zeesoft.games.illuminator.controls.DeathExplosion;
import nl.zeesoft.games.illuminator.controls.PlayerSpellProvider;
import nl.zeesoft.games.illuminator.controls.PowerUp;
import nl.zeesoft.games.illuminator.controls.spells.BallOfKnowledge;
import nl.zeesoft.games.illuminator.model.GameModel;
import nl.zeesoft.zdk.ZIntegerGenerator;

/**
 * Main entry point of the game.
 * 
 * TODO: Move logic into AppStates or Controls.
 */
public class PlayState extends AbstractAppState implements PhysicsCollisionListener, PlayerSpellProvider {
    private GameModel               gameModel       = null;
    private MouseInput              mouseInput      = null;
    private FlyByCamera             flyCam          = null;
    private Listener                listener        = null;

    private SimpleApplication       app             = null;
    private AppStateManager         stateManager    = null;
    private AssetManager            assetManager    = null;
    private InputManager            inputManager    = null;
    private Node                    rootNode        = null;
    private Camera                  cam             = null;
    
    private Spatial                 sceneModel      = null;
    private RigidBodyControl        scene           = null;
    private float                   spawnHeight     = 10;

    private BulletAppState          bulletAppState  = null;

    private SpotLight               light           = null;
    private PointLight              aura            = null;

    private Player                  player          = null;
    private List<Opponent>          opponents       = new ArrayList<Opponent>();
    private List<DeathExplosion>    deathExplosions = new ArrayList<DeathExplosion>();
    private List<PowerUp>           powerUps        = new ArrayList<PowerUp>();
    private List<GameControlNode>   spellObjects    = new ArrayList<GameControlNode>();
    

    public PlayState(GameModel gameModel,MouseInput mouseInput,FlyByCamera flyCam,Listener listener) {
        this.gameModel = gameModel;
        this.mouseInput = mouseInput;
        this.flyCam = flyCam;
        this.listener = listener;
    }
    
    @Override
    public void initialize(AppStateManager stateManager,Application app) {
        this.stateManager = stateManager;
        this.app = (SimpleApplication) app;
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.rootNode = this.app.getRootNode();
        this.cam = this.app.getCamera();
        
        if (gameModel.isGodMode()) {
            gameModel.getPlayerModel().godMode = true;
        }
        gameModel.loadModels(assetManager);
        
        // Fly cam must be disabled before cursor is disabled
        flyCam.setEnabled(false);
        mouseInput.setCursorVisible(false);
        
        bulletAppState = new BulletAppState();
        bulletAppState.setDebugEnabled(gameModel.isDebug());
        stateManager.attach(bulletAppState);

        bulletAppState.getPhysicsSpace().addCollisionListener(this);
                
        loadScene();

        loadPlayer();
        
        // TODO: Level configuration and corresponding spawn control
        //spawnOpponent();
    }

    @Override
    public void cleanup() {
        for (Opponent opponent: opponents) {
            opponent.detachFromRootNode(rootNode, bulletAppState);
        }
        opponents.clear();
        for (DeathExplosion explosion: deathExplosions) {
            explosion.detachFromRootNode(rootNode, bulletAppState);
        }
        deathExplosions.clear();
        for (PowerUp pup: powerUps) {
            pup.detachFromRootNode(rootNode, bulletAppState);
        }
        powerUps.clear();

        unloadPlayer();

        unloadScene();

        stateManager.detach(bulletAppState);

        mouseInput.setCursorVisible(true);
        flyCam.setEnabled(true);
    }

    @Override
    public void update(float tpf) {
        // Listener follows camera
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        
        player.update(tpf);

        // Light follows player
        Vector3f location = player.getCharacterControl().getPhysicsLocation();
        location.y += 5f;
        Vector3f direction = cam.getDirection();
        direction.y = direction.y - (90f * FastMath.DEG_TO_RAD);

        light.setPosition(location);
        light.setDirection(direction);

        location = player.getImpactControl().getPhysicsLocation();
        location.y += 1f;
        aura.setPosition(location);

        for (Opponent opponent: opponents) {
            opponent.update(tpf);
            
            // TODO: Move to Opponent, implement Zeesoft Symbolic Cognition
            Vector3f opponentPos = opponent.getCharacterControl().getPhysicsLocation();
            Vector3f playerPos = player.getCharacterControl().getPhysicsLocation();
            playerPos.y = opponentPos.y;
                        
            float distance = playerPos.distance(opponentPos);
            if (distance > 0.1) {
                Vector3f turn = opponentPos.subtract(playerPos);
                turn.y = 0;
                opponent.getCharacterControl().setViewDirection(turn);
            }
            if (distance < 1.8) {
                if (!opponent.isAttack()) {
                    opponent.setAttack(true);
                }
            }
            if (distance > 1.7) {
                opponent.setUp(true);
            } else {
                opponent.setUp(false);
            }
        }
        
        List<DeathExplosion> explosions = new ArrayList<DeathExplosion>(deathExplosions);
        for (DeathExplosion explosion: explosions) {
            if (explosion.update(tpf)) {
                deathExplosions.remove(explosion);
                explosion.detachFromRootNode(rootNode, bulletAppState);
            }
        }
        
        List<PowerUp> pups = new ArrayList<PowerUp>(powerUps);
        for (PowerUp pup: pups) {
            if (pup.update(tpf)) {
                powerUps.remove(pup);
                pup.detachFromRootNode(rootNode, bulletAppState);
            }
        }

        List<GameControlNode> objs = new ArrayList<GameControlNode>(spellObjects);
        for (GameControlNode obj: objs) {
            if (obj.update(tpf)) {
                spellObjects.remove(obj);
                obj.detachFromRootNode(rootNode, bulletAppState);
            }
        }
    }

    @Override
    public void render(RenderManager rm) {
        //TODO: add render code
    }
    
    @Override
    public void collision(PhysicsCollisionEvent event) {
        handleCollision(event.getObjectA(),event.getObjectB());
    }
    
    @Override
    public List<GameControlNode> initializeSpellObjects(String spellName, Vector3f location) {
        List<GameControlNode> objects = new ArrayList<GameControlNode>();
        if (spellName.equals("Cast.BallOfKnowledge")) {
            BallOfKnowledge bof = new BallOfKnowledge(assetManager,10);
            objects.add(bof);
            bof.initialize();
            spellObjects.add(bof);
            bof.setLocalTranslation(location);
            bof.attachToRootNode(rootNode,bulletAppState);
        }
        return objects;
    }
    
    @Override
    public void releaseSpellObjects(List<GameControlNode> objects) {
        for (GameControlNode object: objects) {
            if (object instanceof BallOfKnowledge) {
                ((BallOfKnowledge) object).setReleased(true);
            }
        }
    }
        
    private boolean handleCollision(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        int attacking = player.getFistAttack(nodeA,nodeB);
        if (attacking>=0) {
            for (Opponent opponent: opponents) {
                if (opponent.applyFistImpact(nodeA,nodeB,attacking)) {
                    if (opponent.getHealth()==0) {
                        // Explode
                        DeathExplosion explosion = opponent.getDeath();
                        explosion.attachToRootNode(rootNode, bulletAppState);
                        explosion.setLocalTranslation(opponent.getImpactControl().getPhysicsLocation());
                        explosion.start();
                        deathExplosions.add(explosion);
                        removeOpponent(opponent);
                        spawnOpponent();

                        if (player.getHealth()<100) {
                            ZIntegerGenerator generator = new ZIntegerGenerator(0,3);
                            if (generator.getNewInteger()==0) {
                                // Generate power up
                                PowerUp pup = new PowerUp(assetManager,10f);
                                pup.initialize();
                                pup.setLocalTranslation(opponent.getCharacterControl().getPhysicsLocation());
                                powerUps.add(pup);
                                pup.attachToRootNode(rootNode, bulletAppState);
                            }
                        }
                    }
                }
            }
        } else {
            if (nodeA==player.getCharacterControl() || nodeB==player.getCharacterControl()) {
                // Check powerup
                PowerUp cpup = null;
                for (PowerUp pup: powerUps) {
                    if (nodeA==pup.getControl() || nodeB==pup.getControl()) {
                        cpup = pup;
                        break;
                    }
                }
                if (cpup!=null) {
                    player.setHealth(player.getHealth() + 50);
                    powerUps.remove(cpup);
                    cpup.detachFromRootNode(rootNode, bulletAppState);
                }
            }
        }
        for (Opponent opponent: opponents) {
            attacking = opponent.getFistAttack(nodeA,nodeB);
            if (attacking>=0) {
                if (player.applyFistImpact(nodeA,nodeB,attacking)) {
                    if (player.getHealth()==0) {
                        // TODO: End play state
                    }
                }
            }
        }
        return true;
    }
    
    private void loadScene() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.39f, -0.32f, -0.74f));
        rootNode.addLight(sun); 
        
        // TODO: Create custom scene
        sceneModel = assetManager.loadModel("Scenes/PlayScene.j3o");
        //sceneModel.scale(1f,.5f,1f);
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        scene = new RigidBodyControl(sceneShape, 0);
        scene.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        scene.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
        sceneModel.addControl(scene);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(scene);
    }

    private void unloadScene() {
        rootNode.detachChild(sceneModel);
        bulletAppState.getPhysicsSpace().remove(scene);
    }

    private void loadPlayer() {
        player = new Player(gameModel.getPlayerModel(), assetManager, inputManager, cam, this);
        player.initialize();
        player.getCharacterControl().setPhysicsLocation(new Vector3f(-5f,spawnHeight,5f));
        player.attachToRootNode(rootNode, bulletAppState);
        addLight();
        addAura();
    }

    private void unloadPlayer() {
        removeAura();
        removeLight();
        player.detachFromRootNode(rootNode, bulletAppState);
    }
    
    private void addLight() {
        light = new SpotLight();
        light.setSpotRange(20f);
        light.setSpotInnerAngle(35f * FastMath.DEG_TO_RAD);
        light.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD);
        rootNode.addLight(light);
    }

    private void removeLight() {
        rootNode.removeLight(light);
    }
    
    private void addAura() {
        aura = new PointLight();
        aura.setColor(ColorRGBA.Blue);
        aura.setRadius(4f);
        rootNode.addLight(aura);
    }

    private void removeAura() {
        rootNode.removeLight(aura);
    }

    private void spawnOpponent() {
        Opponent opp = new Opponent(gameModel.getNewOpponentModel(assetManager),assetManager);
        opp.initialize();
        opp.getCharacterControl().setPhysicsLocation(getNewSpawnLocation());
        opp.attachToRootNode(rootNode, bulletAppState);
        opponents.add(opp);
    }

    private void removeOpponent(Opponent opp) {
        opp.detachFromRootNode(rootNode,bulletAppState);
        opponents.remove(opp);
    }
    
    private Vector3f getNewSpawnLocation() {
        Vector3f location = new Vector3f(0,spawnHeight,0);
        return location;
    }
}
