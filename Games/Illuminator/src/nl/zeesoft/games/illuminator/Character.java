package nl.zeesoft.games.illuminator;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import nl.zeesoft.games.illuminator.model.CharacterModel;

/**
 * Abstract character.
 * 
 * Combines the character model with character control.
 */
public abstract class Character extends Node implements AnimEventListener {
    private AssetManager        assetManager        = null;
    
    private CharacterModel      characterModel      = null;
    private CharacterControl    characterControl    = null;
    private RigidBodyControl    rigidControl        = null;

    private RigidBodyControl    impactControl       = null;
    private RigidBodyControl    fistControlLeft     = null;
    private RigidBodyControl    fistControlRight    = null;

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

    private SkeletonControl     skeletonControl     = null;
    private Geometry            punchCollisionLeft  = null;
    private Geometry            punchCollisionRight = null;
    
    public Character(CharacterModel characterModel,AssetManager assetManager) {
        this.assetManager = assetManager;
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
        characterControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        characterControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
        this.addControl(characterControl);
        
        // TODO: Create parameter for literal: Meshes
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

        skeletonControl = characterModel.model.getChild("Meshes").getControl(SkeletonControl.class);
        
        // Impact control
        CapsuleCollisionShape impactShape = new CapsuleCollisionShape(characterModel.radius * 0.4f,characterModel.height * 1.3f);
        impactControl = new RigidBodyControl(impactShape,1);
        // Dont want these colliding with anything other than each other to save efficiency
        impactControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        impactControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
        impactControl.setKinematic(true);
        this.addControl(impactControl);

        // Fist controls
        punchCollisionLeft = getNewPunchCollision(true);
        punchCollisionRight = getNewPunchCollision(false);
        
        Node handL = skeletonControl.getAttachmentsNode("Hand.L");
        handL.attachChild(punchCollisionLeft);

        Node handR = skeletonControl.getAttachmentsNode("Hand.R");
        handR.attachChild(punchCollisionRight);

        fistControlLeft = getNewFistControl();
        fistControlRight = getNewFistControl();

        punchCollisionLeft.addControl(fistControlLeft);
        punchCollisionRight.addControl(fistControlRight);
    }
    
    protected void addCollideWithRigidBody() {
        characterControl.addCollideWithGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
    }
    
    protected void addRigidBody() {
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(characterModel.radius,characterModel.height);
        rigidControl = new RigidBodyControl(playerShape,80);
        // Dont want these colliding with anything other than each other to save efficiency
        rigidControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        rigidControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_02);
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
    
    public boolean isFistAttack(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        boolean r = false;
        if (attacking && nodeA!=characterControl && nodeB!=characterControl &&
            (nodeA==fistControlLeft||nodeA==fistControlRight||nodeB==fistControlLeft||nodeB==fistControlRight)
            ) {
            r = true;
        }
        return r;
    }

    public boolean isFistImpact(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        boolean r = false;
        if (!attacking && nodeA!=characterControl && nodeB!=characterControl &&
            (nodeA!=fistControlLeft&&nodeA!=fistControlRight&&nodeB!=fistControlLeft&&nodeB!=fistControlRight) &&
            (nodeA==impactControl||nodeB==impactControl)
            ) {
            r = true;
        }
        return r;
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

    public RigidBodyControl getRigidControl() {
        return rigidControl;
    }

    public RigidBodyControl getImpactControl() {
        return impactControl;
    }
    
    public RigidBodyControl getFistControlLeft() {
        return fistControlLeft;
    }
    
    public RigidBodyControl getFistControlRight() {
        return fistControlRight;
    }
    
    public Geometry getPunchCollision(boolean left) {
        Geometry geom = null;
        if (left) {
            geom = punchCollisionLeft;
        } else {
            geom = punchCollisionRight;
        }
        return geom;
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

    private Geometry getNewPunchCollision(boolean left) {
        Box b = new Box(0.01f,0.01f,0.01f);
        String n = "Hand.Left";
        if (!left) {
            n = "Hand.Right";
        }
        Geometry item = new Geometry(n,b);
        item.setMaterial(new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md"));
        item.getMaterial().getAdditionalRenderState().setBlendMode(BlendMode.Alpha);                
        item.getMaterial().getAdditionalRenderState().setWireframe(true);
        item.setLocalTranslation(new Vector3f(0f,0.5f,0f));
        item.setShadowMode(ShadowMode.Off);
        item.setQueueBucket(Bucket.Transparent);          
        return item;
    }

    private RigidBodyControl getNewFistControl() {
        BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(0.1f,0.1f,0.1f));
        RigidBodyControl fistControl = new RigidBodyControl(boxShape,1);
        // Dont want these colliding with anything other than each other to save efficiency
        fistControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        fistControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
        fistControl.setKinematic(true);
        return fistControl;
    }

}
