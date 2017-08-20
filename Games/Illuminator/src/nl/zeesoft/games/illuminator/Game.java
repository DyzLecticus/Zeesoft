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
import nl.zeesoft.games.illuminator.model.GameModel;

/**
 * Main entry point of the game.
 * 
 * TODO: Move logic into AppStates or Controls.
 */
public class Game extends SimpleApplication implements PhysicsCollisionListener, PhysicsCollisionGroupListener {
    private GameModel               gameModel       = null;
    
    private Spatial                 sceneModel      = null;
    private RigidBodyControl        scene           = null;
    private BulletAppState          bulletAppState  = null;
    
    private Player                  player          = null;
    private List<Opponent>          opponents       = new ArrayList<Opponent>();
    private List<DeathExplosion>    deathExplosions = new ArrayList<DeathExplosion>();

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

        bulletAppState.getPhysicsSpace().addCollisionGroupListener(this,PhysicsCollisionObject.COLLISION_GROUP_03);
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
            
            Vector3f opponentPos = opponent.getCharacterControl().getPhysicsLocation();
            Vector3f playerPos = player.getCharacterControl().getPhysicsLocation();
            playerPos.y = opponentPos.y;
            
            float distance = playerPos.distance(opponentPos);
            //System.out.println("Opponent distance: " + distance);
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
                explosion.stop();
                deathExplosions.remove(explosion);
                rootNode.detachChild(explosion);
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
    
    @Override
    public boolean collide(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        /*
        Character chA = getNodeSource(nodeA);
        if (chA==null) {
            //System.out.println("Unable to find source for node A: " + nodeA + " = " + scene);
        }
        Character chB = getNodeSource(nodeB);
        if (chB==null) {
            //System.out.println("Unable to find source for node B: " + nodeB + " = " + scene);
        }
        */
        //return handleCollision(nodeA,nodeB);
        return true;
    }
    
    private boolean handleCollision(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        int attacking = player.getFistAttack(nodeA,nodeB);
        if (attacking>=0) {
            for (Opponent opponent: opponents) {
                if (opponent.applyFistImpact(nodeA,nodeB,attacking)) {
                    //System.out.println("Opponent impact: " + attacking);
                    if (opponent.getHealth()==0) {
                        DeathExplosion explosion = opponent.getDeath();
                        rootNode.attachChild(explosion);
                        Vector3f trans = opponent.getCharacterControl().getPhysicsLocation();
                        trans.y += (opponent.getCharacterModel().height * 0.7);
                        explosion.setLocalTranslation(trans);
                        explosion.start();
                        deathExplosions.add(explosion);
                        removeOpponent(opponent);
                        spawnOpponent();
                    }
                }
            }
        }
        for (Opponent opponent: opponents) {
            attacking = opponent.getFistAttack(nodeA,nodeB);
            if (attacking>=0) {
                if (player.applyFistImpact(nodeA,nodeB,attacking)) {
                    //System.out.println("Player impact: " + attacking);
                    if (player.getHealth()==0) {
                        // TODO: End game
                    }
                }
            }
        }
        return true;
    }
    
    private Character getNodeSource(PhysicsCollisionObject node) {
        Character ch = null;
        if (node==player.getCharacterControl()) {
            ch = player;
            System.out.println("Bad1");
        } else if (node==player.getRigidControl()) {
            ch = player;
            System.out.println("Bad2");
        } else if (node==player.getImpactControl()) {
            ch = player;
        } else if (node==player.getFistControlLeft()) {
            ch = player;
        } else if (node==player.getFistControlRight()) {
            ch = player;
        } else {
            for (Opponent opponent: opponents) {
                if (node==opponent.getCharacterControl()) {
                    ch = opponent;
                    System.out.println("Bad1!");
                    break;
                } else if (node==opponent.getRigidControl()) {
                    ch = opponent;
                    System.out.println("Bad2!");
                    break;
                } else if (node==opponent.getImpactControl()) {
                    ch = opponent;
                    break;
                } else if (node==opponent.getFistControlLeft()) {
                    ch = opponent;
                    break;
                } else if (node==opponent.getFistControlRight()) {
                    ch = opponent;
                    break;
                }
            }
        }
        return ch;
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
