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
import com.jme3.collision.CollisionResults;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.ArrayList;
import java.util.List;
import nl.zeesoft.games.illuminator.controls.DeathExplosion;
import nl.zeesoft.games.illuminator.controls.GameCharacter;
import nl.zeesoft.games.illuminator.controls.PowerUp;
import nl.zeesoft.games.illuminator.controls.SceneFlame;
import nl.zeesoft.games.illuminator.controls.spells.BallOfKnowledge;
import nl.zeesoft.games.illuminator.model.GameModel;
import nl.zeesoft.zdk.ZIntegerGenerator;

/**
 * Main entry point of the game.
 * 
 * TODO: Move logic into AppStates or Controls.
 */
public class PlayState extends AbstractAppState implements PhysicsCollisionListener, SpellObjectProvider, AttackHandler {
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
    private float                   flameHeight     = 10;

    private BulletAppState          bulletAppState  = null;

    private Player                  player          = null;
    private List<Opponent>          opponents       = new ArrayList<Opponent>();
    private List<DeathExplosion>    deathExplosions = new ArrayList<DeathExplosion>();
    private List<PowerUp>           powerUps        = new ArrayList<PowerUp>();
    private List<GameControlNode>   spellObjects    = new ArrayList<GameControlNode>();
    private List<SceneFlame>        flames          = new ArrayList<SceneFlame>();
    
    private float                   playTime        = 0.0f;
    private int                     level           = 0;
    private int                     kills           = 0;

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

        playTime = 0.0f;
        level = 0;
        kills = 0;
        
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
        playTime += tpf;
        
        player.update(tpf);
        // Listener follows camera
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());

        if (playTime>=2.0f && opponents.size()<=0) {
            spawnOpponent();
        }
        
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
            if (distance < 1.7) {
                if (!opponent.isAttack()) {
                    opponent.setAttack(true);
                }
            }
            if (distance > 1.6) {
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

        for (SceneFlame f: flames) {
            f.update(tpf);
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
    public List<GameCharacter> getAttackCollisions(GameCharacter character,int attacking) {
        List<GameCharacter> list = new ArrayList<GameCharacter>();
        CollisionResults results = new CollisionResults();
        Ray ray = new Ray(character.getCharacterControl().getPhysicsLocation(),character.getCharacterControl().getViewDirection().negate());
        
        rootNode.collideWith(ray, results);
        for (int i = 0; i < results.size(); i++) {
            if (results.getCollision(i).getDistance() < 1.6f) {
                GameCharacter impacted = getCharacterForGeometry(results.getCollision(i).getGeometry());
                if (impacted!=null && impacted!=character && impacted.applyFistImpact(attacking)) {
                    list.add(impacted);
                    if (character==player && impacted instanceof Opponent) {
                        player.setMana(player.getMana() + (attacking + 1));
                        handleOpponentImpact((Opponent) impacted);
                    } else if (impacted==player && player.getHealth()==0) {
                        // TODO: player death
                    }
                }
            }
        }
        return list;
    }

    @Override
    public List<GameControlNode> initializeSpellObjects(String spellName, Vector3f location) {
        List<GameControlNode> objects = new ArrayList<GameControlNode>();
        if (spellName.equals("Cast.BallOfKnowledge")) {
            BallOfKnowledge bok = new BallOfKnowledge(assetManager,5);
            objects.add(bok);
            bok.initialize();
            spellObjects.add(bok);
            bok.setLocalTranslation(location);
            bok.attachToRootNode(rootNode,bulletAppState);
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

    private GameCharacter getCharacterForGeometry(Geometry geom) {
        GameCharacter character = null;
        if (player.getCharacterModel().model.getChild(geom.getName())==geom) {
            character = player;
        } else {
            for (Opponent opponent: opponents) {
                if (opponent.getCharacterModel().model.getChild(geom.getName())==geom) {
                    character = opponent;
                    break;
                }
            }
        }
        return character;
    }
    
    private GameCharacter getCharacterForRigidBody(RigidBodyControl control) {
        GameCharacter character = null;
        if (player.getRigidControl()==control) {
            character = player;
        } else {
            for (Opponent opponent: opponents) {
                if (opponent.getRigidControl()==control) {
                    character = opponent;
                    break;
                }
            }
        }
        return character;
    }
    
    private boolean handleCollision(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        if (nodeA instanceof RigidBodyControl && nodeB instanceof RigidBodyControl) {
            RigidBodyControl cA = (RigidBodyControl) nodeA;
            RigidBodyControl cB = (RigidBodyControl) nodeB;
            GameCharacter charA = getCharacterForRigidBody(cA);
            GameCharacter charB = getCharacterForRigidBody(cB);
            
            if (charA!=null && charB!=null) {
                Vector3f posA = charA.getCharacterControl().getPhysicsLocation();
                Vector3f posB = charB.getCharacterControl().getPhysicsLocation();
                posA.y = posB.y;

                charA.getCharacterControl().setWalkDirection(posB.subtract(posA).negate().normalize().multLocal(0.01f));
                charB.getCharacterControl().setWalkDirection(posA.subtract(posB).negate().normalize().multLocal(0.01f));
            }
        }

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
        
        Opponent opponentImpact = null;
        for (Opponent opponent: opponents) {
            if (nodeA==opponent.getRigidControl()) {
                opponentImpact = opponent;
            } else if (nodeB==opponent.getRigidControl()) {
                opponentImpact = opponent;
            }
        }
        if (opponentImpact!=null) {
            // Check spell objects
            for (GameControlNode object: spellObjects) {
                if (object instanceof BallOfKnowledge) {
                    BallOfKnowledge bok = (BallOfKnowledge) object;
                    if (nodeA==bok.getControl() || nodeB==bok.getControl()) {
                        if (opponentImpact.applySpellImpact(object)) {
                            handleOpponentImpact(opponentImpact);
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private void handleOpponentImpact(Opponent opponent) {
        if (opponent.getHealth()==0) {
            kills++;
            if (kills>=gameModel.getLevels().get(level).goal &&
                gameModel.getLevels().size() > (level + 1)
                ) {
                level++;
                kills = 0;
            }
            
            // Explode
            DeathExplosion explosion = opponent.getDeath();
            explosion.attachToRootNode(rootNode, bulletAppState);
            Vector3f location = opponent.getCharacterControl().getPhysicsLocation();
            location.y += 0.5;
            explosion.setLocalTranslation(location);
            explosion.start();
            deathExplosions.add(explosion);
            removeOpponent(opponent);
            spawnOpponent();
            if (opponents.size()<gameModel.getLevels().get(level).opponents) {
                spawnOpponent();
            }

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
    
    private void loadScene() {
        sceneModel = assetManager.loadModel("Scenes/PlayScene.j3o");
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        scene = new RigidBodyControl(sceneShape, 0);
        scene.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        scene.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
        sceneModel.addControl(scene);
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(scene);

        for (int i = 0; i < 4; i++) {
            SceneFlame f = new SceneFlame(assetManager);
            if (i==0) {
                f.setLocalTranslation(-50,flameHeight,-50);
            } else if (i==1) {
                f.setLocalTranslation(50,flameHeight,-50);
            } else if (i==2) {
                f.setLocalTranslation(50,flameHeight,50);
            } else if (i==3) {
                f.setLocalTranslation(-50,flameHeight,50);
            }
            f.initialize();
            flames.add(f);
            f.attachToRootNode(rootNode, bulletAppState);
        }
    }

    private void unloadScene() {
        for (SceneFlame f: flames) {
            f.detachFromRootNode(rootNode, bulletAppState);
        }
        flames.clear();

        rootNode.detachChild(sceneModel);
        bulletAppState.getPhysicsSpace().remove(scene);
    }

    private void loadPlayer() {
        player = new Player(gameModel.getPlayerModel(), assetManager, this, inputManager, cam, this);
        player.initialize();
        player.getCharacterControl().setPhysicsLocation(new Vector3f(0f,sceneModel.getLocalTranslation().getY() + spawnHeight,0f));
        player.attachToRootNode(rootNode, bulletAppState);
    }

    private void unloadPlayer() {
        player.detachFromRootNode(rootNode, bulletAppState);
    }
    
    private void spawnOpponent() {
        Opponent opp = new Opponent(gameModel.getNewOpponentModel(assetManager),assetManager,this);
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
        ZIntegerGenerator generator = new ZIntegerGenerator(0,80);
        int num = generator.getNewInteger();
        
        while (num==40) {
            num = generator.getNewInteger();
        }
        float x = (float) num - 40f;
        
        num = generator.getNewInteger();
        while (num==40) {
            num = generator.getNewInteger();
        }
        float z = (float) num - 40f;
        
        Vector3f location = new Vector3f(x,0,z);
        location.setY(sceneModel.getLocalTranslation().getY() + spawnHeight);
        return location;
    }
}
