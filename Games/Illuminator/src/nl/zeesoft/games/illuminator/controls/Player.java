package nl.zeesoft.games.illuminator.controls;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import java.util.ArrayList;
import java.util.List;
import nl.zeesoft.games.illuminator.GameControlNode;
import nl.zeesoft.games.illuminator.model.CharacterModel;

public class Player extends Character implements ActionListener, AnalogListener {
    private static final float      MOUSE_LOOK_SPEED    = FastMath.PI;
    
    private PlayerSpellProvider     spellProvider       = null;

    private InputManager            inputManager        = null;

    // PlayerCamera automatically sets itself up to follow a target
    // object. Check the "onAnalog" function here to see how we do mouselook.
    private PlayerCamera            camera              = null;
    private Camera                  cam                 = null;
    private float                   impactCorrect       = 0.0f;
    
    private SpotLight               light           = null;
    private PointLight              aura            = null;

    private ParticleEmitter         flameLeft           = null;
    private ParticleEmitter         flameRight          = null;
    private float                   burst               = 0.0f;
    
    private Node                    spellLocation       = null;
    private List<GameControlNode>   spellObjects        = new ArrayList<GameControlNode>();
    
    public Player(CharacterModel characterModel,AssetManager assetManager, InputManager inputManager, Camera cam,PlayerSpellProvider spellProvider) {
        super(characterModel,assetManager);
        this.inputManager = inputManager;
        this.cam = cam;
        this.spellProvider = spellProvider;
        camera = new PlayerCamera("CamNode", cam, this);
    }
   
    @Override
    public void initialize() {
        super.initialize();
        getCharacterControl().addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        setUpKeys();
        
        flameLeft = getNewFlame(1,1.0f,false);
        flameRight = getNewFlame(1,1.0f,false);
        getCharacterModel().getFist(true).attachChild(flameLeft);
        getCharacterModel().getFist(false).attachChild(flameRight);

        spellLocation = new Node("SpellLocation");
        attachChild(spellLocation);
        spellLocation.setLocalTranslation(new Vector3f(0,0.7f,-1.3f));
    }

    @Override
    public boolean update(float tpf) {
        if (impactCorrect>0.0f) {
            impactCorrect = impactCorrect - tpf;
            camera.verticalRotate(tpf * 0.1f);
        }
        if (impactCorrect<0.0f) {
            impactCorrect = 0.0f;
        }
        
        boolean done = super.update(tpf);
        if (getHealth()>0) {
            burst += tpf;
            if (burst>=0.05) {
                burst = 0.0f;
                flameLeft.emitAllParticles();
                flameRight.emitAllParticles();
            }
        }
        if (spellObjects.size()>0) {
            for (GameControlNode object: spellObjects) {
                object.setLocalTranslation(spellLocation.getWorldTranslation());
                object.setLocalRotation(getWorldRotation());
            }
        }
        
        // Light follows player
        Vector3f location = getCharacterControl().getPhysicsLocation();
        location.y += 5f;
        Vector3f direction = cam.getDirection();
        direction.y = direction.y - (90f * FastMath.DEG_TO_RAD);
        light.setPosition(location);
        light.setDirection(direction);

        location = getImpactControl().getPhysicsLocation();
        location.y += 1f;
        aura.setPosition(location);

        
        return done;
    }

    @Override
    public void attachToRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.attachToRootNode(rootNode, bulletAppState);
        light = new SpotLight();
        light.setSpotRange(20f);
        light.setSpotInnerAngle(35f * FastMath.DEG_TO_RAD);
        light.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD);
        rootNode.addLight(light);
        
        aura = new PointLight();
        aura.setColor(ColorRGBA.Blue);
        aura.setRadius(4f);
        rootNode.addLight(aura);
    }
    
    @Override
    public void detachFromRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.detachFromRootNode(rootNode, bulletAppState);
        rootNode.removeLight(light);
        rootNode.removeLight(aura);
    }
    
    @Override
    public Vector3f getDirection() {
        Vector3f camDir = cam.getDirection().clone();
        camDir.y = 0;
        return camDir;
    }

    @Override
    public Vector3f getLeft() {
        Vector3f camLeft = cam.getLeft().clone();
        camLeft.y = 0;
        return camLeft;
    }

    @Override    
    public void onAction(String binding, boolean value, float tpf) {
        if (binding.equals("Left")) {
            setLeft(value);
        } else if (binding.equals("Right")) {
            setRight(value);
        } else if (binding.equals("Up")) {
            setUp(value);
        } else if (binding.equals("Down")) {
            setDown(value);
        } else if (binding.equals("Jump")) {
            jump();
        } else if (binding.equals("Attack")) {
            setAttack(value);
        } else if (binding.equals("Cast")) {
            setCast(value);
        }
    }

    // Analog handler for mouse movement events.
    // Horizontal movements turn the character.
    // Vertical movements rotate the camera up or down.
    @Override    
    public void onAnalog(String binding, float value, float tpf) {
        if (binding.equals("TurnLeft")) {
            Quaternion turn = new Quaternion();
            turn.fromAngleAxis((MOUSE_LOOK_SPEED*value), Vector3f.UNIT_Y);
            getCharacterControl().setViewDirection(turn.mult(getCharacterControl().getViewDirection()));
        } else if (binding.equals("TurnRight")) {
            Quaternion turn = new Quaternion();
            turn.fromAngleAxis((-MOUSE_LOOK_SPEED*value), Vector3f.UNIT_Y);
            getCharacterControl().setViewDirection(turn.mult(getCharacterControl().getViewDirection()));
        } else if (binding.equals("MouselookDown")) {
            camera.verticalRotate(-MOUSE_LOOK_SPEED*value);
        } else if (binding.equals("MouselookUp")) {
            camera.verticalRotate(MOUSE_LOOK_SPEED*value);
        }
    }
    
    @Override
    protected void startShockWave(int impacting) {
        impactCorrect += 0.5f;
        camera.verticalRotate(impactCorrect * -0.1f);
    }

    @Override
    protected void startCast(int casting) {
        String spellName = getCharacterModel().spells.get(casting);
        List<GameControlNode> objects = spellProvider.initializeSpellObjects(spellName,spellLocation.getWorldTranslation());
        for (GameControlNode object: objects) {
            spellObjects.add(object);
        }
    }
    
    @Override
    protected void stopCast() {
        spellProvider.releaseSpellObjects(spellObjects);
        spellObjects.clear();
    }
    
    public PlayerCamera getCamera() {
        return camera;
    }

    protected void setUpKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Attack", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping("Cast", new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
        inputManager.addMapping("TurnLeft", new MouseAxisTrigger(MouseInput.AXIS_X,true));
        inputManager.addMapping("TurnRight", new MouseAxisTrigger(MouseInput.AXIS_X,false));
        inputManager.addMapping("MouselookDown", new MouseAxisTrigger(MouseInput.AXIS_Y,true));
        inputManager.addMapping("MouselookUp", new MouseAxisTrigger(MouseInput.AXIS_Y,false));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Attack");
        inputManager.addListener(this, "Cast");
        inputManager.addListener(this, "TurnLeft");
        inputManager.addListener(this, "TurnRight");
        inputManager.addListener(this, "MouselookDown");
        inputManager.addListener(this, "MouselookUp");
    }
    
    private ParticleEmitter getNewFlame(int countFactor, float countFactorF,boolean pointSprite){
        ParticleMesh.Type type = ParticleMesh.Type.Point;
        if (!pointSprite) {
            type = ParticleMesh.Type.Triangle;
        }
        ParticleEmitter flame = new ParticleEmitter("Flame", type, 32 * countFactor);
        flame.setSelectRandomImage(true);
        flame.setRandomAngle(true);
        flame.setStartColor(new ColorRGBA(0.0f,0.0f,0.5f, (float) (1f / countFactorF)));
        flame.setEndColor(new ColorRGBA(0.0f,0.0f,0.1f,0f));
        flame.setStartSize(0.2f);
        flame.setEndSize(0.01f);
        flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        flame.setParticlesPerSec(0);
        flame.setGravity(0,0,0);
        flame.setLowLife(0.3f);
        flame.setHighLife(0.8f);
        flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0,3f,0));
        flame.getParticleInfluencer().setVelocityVariation(0.2f);
        flame.setImagesX(2);
        flame.setImagesY(2);
        Material mat = new Material(getAssetManager(),"Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture",getAssetManager().loadTexture("Effects/Explosion/flame.png"));
        mat.setBoolean("PointSprite",pointSprite);
        flame.setMaterial(mat);
        return flame;
    }
}
