package nl.zeesoft.games.illuminator;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class Player extends Node implements ActionListener, AnalogListener, AnimEventListener {
    // PlayerCamera automatically sets itself up to follow a target
    // object. Check the "onAnalog" function here to see how we do mouselook.
    private PlayerCamera        camera              = null;
    private Camera              cam                 = null;

    private Spatial             model               = null;
    private CharacterControl    characterControl    = null;
    private AnimChannel         lowerChannel        = null;
    private AnimChannel         upperChannel        = null;
    private AnimControl         animControl         = null;
    private InputManager        inputManager        = null;
    private final Vector3f      walkDirection       = new Vector3f();

    private boolean             left                = false;
    private boolean             right               = false;
    private boolean             up                  = false;
    private boolean             down                = false;
    private boolean             attack              = false;
    private boolean             attacking           = false;

    // These can all be changed according to your whims.
    private float               walkSpeed           = 0.04f;
    private float               walkSpeedAttackMult = 4.0f;
    private float               mouselookSpeed      = FastMath.PI;
    private float               jumpSpeed           = 15;
    private float               jumpSpeedMult       = 4.0f;
    private float               fallSpeed           = 20;
    private float               gravity             = 25;
    private float               stepSize            = 0.05f;

    // Animations
    private String              idleAnim            = "Idle";
    private String              walkAnim            = "Walk";
    private String              attackAnim          = "Jab";
    private String              jumpAnim            = "Idle";

    public Player(Node model, InputManager inputManager, Camera cam) {
	super();
	this.cam = cam;
	camera = new PlayerCamera("CamNode", cam, this);

        this.model = model;
	model.scale(0.2f);
	model.setLocalTranslation(0f, -1.0f, 0f);
	model.rotate(0f, FastMath.PI, 0f);
	this.attachChild(this.model);

	CapsuleCollisionShape playerShape = new CapsuleCollisionShape(.5f,1f);
	characterControl = new CharacterControl(playerShape, stepSize);
	characterControl.setJumpSpeed(jumpSpeed);
	characterControl.setFallSpeed(fallSpeed);
	characterControl.setGravity(gravity);
	this.addControl(characterControl);

	animControl = model.getChild("Meshes").getControl(AnimControl.class);
	animControl.addListener(this);
	lowerChannel = animControl.createChannel();
        lowerChannel.addBone("Root");
        lowerChannel.addBone("Hip.L");
        lowerChannel.addBone("Leg.Upper.L");
        lowerChannel.addBone("Leg.Lower.L");
        lowerChannel.addBone("Foot.L");
        lowerChannel.addBone("Hip.R");
        lowerChannel.addBone("Leg.Upper.R");
        lowerChannel.addBone("Leg.Lower.R");
        lowerChannel.addBone("Foot.R");
	lowerChannel.setAnim(idleAnim);
        
	upperChannel = animControl.createChannel();
        upperChannel.addBone("Back");
        upperChannel.addBone("Chest");
        upperChannel.addBone("Neck");
        upperChannel.addBone("Head");
        upperChannel.addBone("Shoulder.L");
        upperChannel.addBone("Arm.Upper.L");
        upperChannel.addBone("Arm.Lower.L");
        upperChannel.addBone("Hand.L");
        upperChannel.addBone("Fingers.L");
        upperChannel.addBone("Shoulder.R");
        upperChannel.addBone("Arm.Upper.R");
        upperChannel.addBone("Arm.Lower.R");
        upperChannel.addBone("Hand.R");
        upperChannel.addBone("Fingers.R");
	upperChannel.setAnim(idleAnim);
        
	this.inputManager = inputManager;
	setUpKeys();
    }

    // Make sure to call this from the main simpleUpdate() loop
    public void update() {
	Vector3f camDir = cam.getDirection().clone();
	camDir.y = 0;
	Vector3f camLeft = cam.getLeft().clone();
	camLeft.y = 0;
	walkDirection.set(0, 0, 0);

	if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(camDir);
        }
	if (down) {
            walkDirection.addLocal(camDir.negate());
        }

        float walk = walkSpeed;
        if (attacking && up) {
            walk = walk * walkSpeedAttackMult;
        } else if (up && !characterControl.onGround()) {
            walk = walk * jumpSpeedMult;
        }
	characterControl.setWalkDirection(walkDirection.normalize().multLocal(walk));
       
	handleAnimations();
    }
    
    private void handleAnimations() {
	if (attacking) {
	    // Waiting for attack animation to finish
	} else if (attack) {
	    upperChannel.setAnim(attackAnim,0.001f);
            upperChannel.setLoopMode(LoopMode.DontLoop);
	    attack = false;
	    attacking = true;
	} else {
	    upperChannel.setAnim(idleAnim);
            upperChannel.setLoopMode(LoopMode.Loop);
        }
        if (characterControl.onGround()) {
	    if (left || right || up || down) {
		if(!lowerChannel.getAnimationName().equals(walkAnim)) {
		    lowerChannel.setAnim(walkAnim,.3f);
		}
	    } else {
		if (!lowerChannel.getAnimationName().equals(idleAnim)) {
		    lowerChannel.setAnim(idleAnim,.3f);
		}
	    }
	}
    }

    private void setUpKeys() {
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

    public void onAction(String binding, boolean value, float tpf) {
	if (binding.equals("Left")) {
	    left = value;
	} else if (binding.equals("Right")) {
	    right = value;
	} else if (binding.equals("Up")) {
	    up = value;
	} else if (binding.equals("Down")) {
	    down = value;
	} else if (binding.equals("Jump")) {
	    if(characterControl.onGround()) {
		characterControl.jump();
                lowerChannel.setAnim(jumpAnim,.3f);
	    }
	} else if (binding.equals("Attack")) {
	    attack = value;
	}
    }

    // Analog handler for mouse movement events.
    // It is assumed that we want horizontal movements to turn the character,
    // while vertical movements only make the camera rotate up or down.
    public void onAnalog(String binding, float value, float tpf) {
        if (binding.equals("TurnLeft")) {
	    Quaternion turn = new Quaternion();
	    turn.fromAngleAxis((mouselookSpeed*value), Vector3f.UNIT_Y);
	    characterControl.setViewDirection(turn.mult(characterControl.getViewDirection()));
	} else if (binding.equals("TurnRight")) {
	    Quaternion turn = new Quaternion();
	    turn.fromAngleAxis((-mouselookSpeed*value), Vector3f.UNIT_Y);
	    characterControl.setViewDirection(turn.mult(characterControl.getViewDirection()));
	} else if (binding.equals("MouselookDown")) {
	    camera.verticalRotate(-mouselookSpeed*value);
	} else if (binding.equals("MouselookUp")) {
	    camera.verticalRotate(mouselookSpeed*value);
	}
    }

    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
	if(channel == upperChannel && attacking && animName.equals(attackAnim)) {
	    attacking = false;
	}
    }

    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        // Not implemented
    }

    public CharacterControl getCharacterControl() {
	return characterControl;
    }

    public PlayerCamera getCameraNode() {
	return camera;
    }
}