package nl.zeesoft.games.illuminator;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import nl.zeesoft.games.illuminator.model.CharacterModel;

public class Player extends Character implements ActionListener, AnalogListener {
    private static final float  MOUSE_LOOK_SPEED    = FastMath.PI;
    
    private InputManager        inputManager        = null;

    // PlayerCamera automatically sets itself up to follow a target
    // object. Check the "onAnalog" function here to see how we do mouselook.
    private PlayerCamera        camera              = null;
    private Camera              cam                 = null;
    
    public Player(CharacterModel characterModel,AssetManager assetManager, InputManager inputManager, Camera cam) {
        super(characterModel,assetManager);
        this.inputManager = inputManager;
        this.cam = cam;
        camera = new PlayerCamera("CamNode", cam, this);
    }
   
    @Override
    public void initialize() {
        super.initialize();
        addCollideWithRigidBody();
        setUpKeys();
    }

    @Override
    public  Vector3f getDirection() {
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
        inputManager.addListener(this, "TurnLeft");
        inputManager.addListener(this, "TurnRight");
        inputManager.addListener(this, "MouselookDown");
        inputManager.addListener(this, "MouselookUp");
    }
}
