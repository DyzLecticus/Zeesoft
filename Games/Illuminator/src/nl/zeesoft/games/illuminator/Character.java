package nl.zeesoft.games.illuminator;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import nl.zeesoft.games.illuminator.model.CharacterModel;

/**
 * Abstract character.
 * 
 * Combines the character model with character control.
 */
public abstract class Character extends Node implements AnimEventListener {
    private CharacterModel      characterModel      = null;
    private CharacterControl    characterControl    = null;
    private RigidBodyControl    rigidControl        = null;

    private AnimChannel         lowerChannel        = null;
    private AnimChannel         upperChannel        = null;
    private AnimControl         animControl         = null;
    private final Vector3f      walkDirection       = new Vector3f();

    private boolean             left                = false;
    private boolean             right               = false;
    private boolean             up                  = false;
    private boolean             down                = false;
    private boolean             attack              = false;
    private boolean             attacking           = false;

    public Character(CharacterModel characterModel) {
        this.characterModel = characterModel;
    }

    public void initialize() {
        characterModel.model.scale(characterModel.scale);
        characterModel.model.rotate(0f,characterModel.rotY,0f);
        characterModel.model.setLocalTranslation(characterModel.translation);
        this.attachChild(characterModel.model);

        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(characterModel.radius,characterModel.height);
        characterControl = new CharacterControl(playerShape,characterModel.stepSize);
        characterControl.setJumpSpeed(characterModel.jumpSpeed);
        characterControl.setFallSpeed(characterModel.fallSpeed);
        characterControl.setGravity(characterModel.gravity);
        this.addControl(characterControl);
        
        animControl = characterModel.model.getChild("Meshes").getControl(AnimControl.class);
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
        lowerChannel.setAnim(characterModel.idleAnim);
        
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
        upperChannel.setAnim(characterModel.idleAnim);
    }
    
    protected void addCollideWithRigidBody() {
        characterControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
    }
    
    protected void addRigidBody() {
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(characterModel.radius,characterModel.height);
        rigidControl = new RigidBodyControl(playerShape,80);
        // Dont want these colliding with anything other than each other to save efficiency
        rigidControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        rigidControl.removeCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        rigidControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        rigidControl.setKinematic(true);
        this.addControl(rigidControl);
    }
    
    public void setLeft(boolean v) {
        left = v;
    }
    
    public void setRight(boolean v) {
        right = v;
    }
    
    public void setUp(boolean v) {
        up = v;
    }
    
    public void setDown(boolean v) {
        down = v;
    }
    
    public void setAttack(boolean v) {
        attack = v;
    }
    
    public void jump() {
        if(characterControl.onGround()) {
            characterControl.jump();
            lowerChannel.setAnim(characterModel.jumpAnim,.3f);
        }
    }
    
    public abstract Vector3f getDirection();
    public abstract Vector3f getLeft();
    
    // Make sure to call this from the main simpleUpdate() loop
    public void update() {
        Vector3f goDir = getDirection();
        Vector3f goLeft = getLeft();
        walkDirection.set(0, 0, 0);

        if (left) {
            walkDirection.addLocal(goLeft);
        }
        if (right) {
            walkDirection.addLocal(goLeft.negate());
        }
        if (up) {
            walkDirection.addLocal(goDir);
        }
        if (down) {
            walkDirection.addLocal(goDir.negate());
        }

        float walk = characterModel.walkSpeed;
        if (attacking && up) {
            walk = walk * characterModel.walkSpeedAttackMult;
        } else if (up && !characterControl.onGround()) {
            walk = walk * characterModel.jumpSpeedMult;
        }
        characterControl.setWalkDirection(walkDirection.normalize().multLocal(walk));

        handleAnimations();
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(channel == upperChannel && attacking && animName.equals(characterModel.attackAnim)) {
            attacking = false;
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        // Not implemented
    }

    public CharacterControl getCharacterControl() {
        return characterControl;
    }
    
    private void handleAnimations() {
        if (attacking) {
            // Waiting for attack animation to finish
        } else if (attack) {
            upperChannel.setAnim(characterModel.attackAnim,0.001f);
            upperChannel.setLoopMode(LoopMode.DontLoop);
            attack = false;
            attacking = true;
        } else {
            upperChannel.setAnim(characterModel.idleAnim);
            upperChannel.setLoopMode(LoopMode.Loop);
        }
        if (characterControl.onGround()) {
            if (left || right || up || down) {
                if(!lowerChannel.getAnimationName().equals(characterModel.walkAnim)) {
                    lowerChannel.setAnim(characterModel.walkAnim,.3f);
                }
            } else {
                if (!lowerChannel.getAnimationName().equals(characterModel.idleAnim)) {
                    lowerChannel.setAnim(characterModel.idleAnim,.3f);
                }
            }
        }
    }
}
