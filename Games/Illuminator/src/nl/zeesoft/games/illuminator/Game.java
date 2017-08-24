package nl.zeesoft.games.illuminator;

import nl.zeesoft.games.illuminator.controls.Character;
import nl.zeesoft.games.illuminator.controls.Opponent;
import nl.zeesoft.games.illuminator.controls.Player;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionGroupListener;
import com.jme3.bullet.collision.PhysicsCollisionListener;
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
import nl.zeesoft.games.illuminator.controls.DeathExplosion;
import nl.zeesoft.games.illuminator.controls.PowerUp;
import nl.zeesoft.games.illuminator.model.GameModel;
import nl.zeesoft.zdk.ZIntegerGenerator;

/**
 * Main entry point of the game.
 * 
 * TODO: Move logic into AppStates or Controls.
 */
public class Game extends SimpleApplication implements PhysicsCollisionListener {
    private GameModel               gameModel       = null;
    
    private Spatial                 sceneModel      = null;
    private RigidBodyControl        scene           = null;
    private BulletAppState          bulletAppState  = null;
    
    private Player                  player          = null;
    private List<Opponent>          opponents       = new ArrayList<Opponent>();
    private List<DeathExplosion>    deathExplosions = new ArrayList<DeathExplosion>();
    private List<PowerUp>           powerUps        = new ArrayList<PowerUp>();

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

        bulletAppState.getPhysicsSpace().addCollisionListener(this);
        
        loadScene();
        loadPlayer();
        addLight();
        
        // TODO: Move to app state
        spawnOpponent();
    }

    @Override
    public void simpleUpdate(float tpf) {
        // Listener follows camera
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        
        player.update(tpf);
        
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
                rootNode.detachChild(explosion);
            }
        }
        
        List<PowerUp> pups = new ArrayList<PowerUp>(powerUps);
        for (PowerUp pup: pups) {
            if (pup.update(tpf)) {
                powerUps.remove(pup);
                rootNode.detachChild(pup);
                bulletAppState.getPhysicsSpace().remove(pup.getControl());
            }
        }
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
    @Override
    public void collision(PhysicsCollisionEvent event) {
        handleCollision(event.getObjectA(),event.getObjectB());
    }
        
    private boolean handleCollision(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        int attacking = player.getFistAttack(nodeA,nodeB);
        if (attacking>=0) {
            for (Opponent opponent: opponents) {
                if (opponent.applyFistImpact(nodeA,nodeB,attacking)) {
                    if (opponent.getHealth()==0) {
                        // Explode
                        DeathExplosion explosion = opponent.getDeath();
                        rootNode.attachChild(explosion);
                        explosion.setLocalTranslation(opponent.getCharacterControl().getPhysicsLocation());
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
                                rootNode.attachChild(pup);
                                powerUps.add(pup);
                                bulletAppState.getPhysicsSpace().add(pup.getControl());
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
                    rootNode.detachChild(cpup);
                    bulletAppState.getPhysicsSpace().remove(cpup.getControl());
                }
            }
        }
        for (Opponent opponent: opponents) {
            attacking = opponent.getFistAttack(nodeA,nodeB);
            if (attacking>=0) {
                if (player.applyFistImpact(nodeA,nodeB,attacking)) {
                    if (player.getHealth()==0) {
                        // TODO: End game
                    }
                }
            }
        }
        return true;
    }
    
    private void loadScene() {
        sceneModel = assetManager.loadModel("Scenes/ManyLights/Main.scene");
        sceneModel.scale(1f,.5f,1f); //Make scenery short enough to jump on. =P
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        scene = new RigidBodyControl(sceneShape, 0);
        scene.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        scene.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
        sceneModel.addControl(scene);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(scene);
    }

    private void loadPlayer() {
        player = new Player(gameModel.getPlayerModel(), assetManager, inputManager, cam);
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
        Opponent opp = new Opponent(gameModel.getNewOpponentModel(assetManager),assetManager);
        opp.initialize();
        opp.getCharacterControl().setPhysicsLocation(getNewSpawnLocation());
        attachCharacter(opp);
        opponents.add(opp);
    }

    private void removeOpponent(Opponent opp) {
        detachCharacter(opp);
        opponents.remove(opp);
    }

    private void attachCharacter(Character character) {
        rootNode.attachChild(character);
        bulletAppState.getPhysicsSpace().add(character);
        bulletAppState.getPhysicsSpace().add(character.getImpactControl());
        bulletAppState.getPhysicsSpace().add(character.getFistControlLeft());
        bulletAppState.getPhysicsSpace().add(character.getFistControlRight());
    }

    private void detachCharacter(Character character) {
        rootNode.detachChild(character);
        bulletAppState.getPhysicsSpace().remove(character);
        bulletAppState.getPhysicsSpace().remove(character.getImpactControl());
        bulletAppState.getPhysicsSpace().remove(character.getFistControlLeft());
        bulletAppState.getPhysicsSpace().remove(character.getFistControlRight());
    }

    private Vector3f getNewSpawnLocation() {
        Vector3f location = new Vector3f(0,5,0);
        return location;
    }    
}
