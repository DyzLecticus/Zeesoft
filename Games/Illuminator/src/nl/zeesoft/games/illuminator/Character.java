package nl.zeesoft.games.illuminator;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.animation.SkeletonControl;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
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
    private final Vector3f      walkDirection       = new Vector3f();

    private boolean             left                = false;
    private boolean             right               = false;
    private boolean             up                  = false;
    private boolean             down                = false;
    private boolean             attack              = false;
    private boolean             attackNext          = false;
    private float               attackDelay         = 0.0f;
    private int                 attacking           = -1;
    private int                 impacting           = -1;

    private SkeletonControl     skeletonControl     = null;
    private Geometry            punchCollisionLeft  = null;
    private Geometry            punchCollisionRight = null;
    
    private AudioNode           attackAudio         = null;
    private AudioNode[]         impactAudio         = null;

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
        
        characterModel.addAnimEventListener(this);
        lowerChannel = characterModel.getNewAnimChannel(true);
        upperChannel = characterModel.getNewAnimChannel(false);

        // TODO: Move to characterModel
        
        skeletonControl = characterModel.model.getChild(characterModel.animRoot).getControl(SkeletonControl.class);
        
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
        
        // Audio
        attackAudio = new AudioNode(assetManager,"Sounds/Swoosh01.wav",DataType.Buffer); 
        attackAudio.setPositional(true);
        attackAudio.setReverbEnabled(true);
        attackAudio.setRefDistance(10f);
        attackAudio.setMaxDistance(50f);
        attackAudio.setVolume(0.1f);
        attackAudio.setLooping(false);

        impactAudio = new AudioNode[2];
        impactAudio[0] = new AudioNode(assetManager,"Sounds/Impact01.wav",DataType.Buffer); 
        impactAudio[1] = new AudioNode(assetManager,"Sounds/Impact02.wav",DataType.Buffer); 

        impactAudio[0].setPositional(true);
        impactAudio[0].setReverbEnabled(true);
        impactAudio[0].setRefDistance(10f);
        impactAudio[0].setMaxDistance(50f);
        impactAudio[0].setVolume(2);
        impactAudio[0].setLooping(false);

        impactAudio[1].setPositional(true);
        impactAudio[1].setReverbEnabled(true);
        impactAudio[1].setRefDistance(10f);
        impactAudio[1].setMaxDistance(50f);
        impactAudio[1].setVolume(2);
        impactAudio[1].setLooping(false);
        
        this.attachChild(impactAudio[0]);
        this.attachChild(impactAudio[1]);
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
    
    protected float getAttackDelay() {
        return 0.0f;
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
        if (v && impacting>=0) {
            v = false;
        }
        attack = v;
        if (attack && attacking>=0 && attacking<(characterModel.attacks.length - 1)) {
            attackNext = true;
        }
    }

    public boolean isAttack() {
        return attack;
    }
    
    public void jump() {
        if(characterControl.onGround()) {
            characterControl.jump();
            lowerChannel.setAnim(characterModel.jumpAnim,.3f);
        }
    }
    
    public int getFistAttack(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        int r = -1;
        if (attacking>=0 && 
            nodeA!=impactControl && nodeB!=impactControl &&
            (nodeA==fistControlLeft||nodeA==fistControlRight||nodeB==fistControlLeft||nodeB==fistControlRight)
            ) {
            r = attacking;
        }
        return r;
    }

    public boolean applyFistImpact(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB, int impact) {
        boolean r = false;
        if (attacking<0 && nodeA!=characterControl && nodeB!=characterControl &&
            (nodeA!=fistControlLeft&&nodeA!=fistControlRight&&nodeB!=fistControlLeft&&nodeB!=fistControlRight) &&
            (nodeA==impactControl||nodeB==impactControl)
            ) {
            if (impacting!=impact) {
                r = true;
                impacting = impact;
                impactAudio[impacting].playInstance();
                upperChannel.setAnim(characterModel.impacts[impacting],0.001f);
                upperChannel.setLoopMode(LoopMode.DontLoop);
            }
        }
        return r;
    }
    
    public abstract Vector3f getDirection();
    public abstract Vector3f getLeft();
    
    // Make sure to call this from the main simpleUpdate() loop
    public void update(float tpf) {
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
        } else if (impacting>=0) {
            walkDirection.addLocal(goDir.negate());
        }

        float walk = characterModel.walkSpeed;
        if (attacking>=0 && up) {
            walk = walk * characterModel.walkSpeedAttackMult;
        } else if (up && !characterControl.onGround()) {
            walk = walk * characterModel.jumpSpeedMult;
        } else if (impacting>=0) {
            walk = walk * 0.5f;
        }
        characterControl.setWalkDirection(walkDirection.normalize().multLocal(walk));

        handleAnimations(tpf);
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(channel == upperChannel) {
            if (attacking>=0 && characterModel.isAttackAnim(animName)) {
                if (attackNext) {
                    attackNext = false;
                    attackDelay = 0.0f;
                    attacking++;
                    upperChannel.setAnim(characterModel.attacks[attacking],0.001f);
                    upperChannel.setLoopMode(LoopMode.DontLoop);
                    attackAudio.playInstance();
                } else {
                    attacking = -1;
                }
            }
            if (impacting>=0 && characterModel.isImpactAnim(animName)) {
                impacting = -1;
            }
            if (attacking<0 && impacting<0) {
                if (!upperChannel.getAnimationName().equals(characterModel.idleAnim)) {
                    upperChannel.setAnim(characterModel.idleAnim,0.001f);
                } else {
                    upperChannel.setLoopMode(LoopMode.Loop);
                }
            }
        }
    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {
        // Not implemented
    }

    public CharacterModel getCharacterModel() {
        return characterModel;
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
    
    private void handleAnimations(float tpf) {
        if (impacting>=0) {
            // Waiting for impact animation to finish
        } else if (attacking>=0) {
            // Waiting for attack animation to finish
        } else if (attack) {
            attackDelay = attackDelay + tpf;
            if (attackDelay>=getAttackDelay()) {
                attackDelay = 0.0f;
                attacking = 0;
                upperChannel.setAnim(characterModel.attacks[attacking],0.001f);
                upperChannel.setLoopMode(LoopMode.DontLoop);
                attackAudio.playInstance();
                attack = false;
            }
        }
        
        if (characterControl.onGround()) {
            if (left || right || up || down) {
                if(!lowerChannel.getAnimationName().equals(characterModel.walkAnim)) {
                    lowerChannel.setAnim(characterModel.walkAnim,0.3f);
                }
            } else {
                if (!lowerChannel.getAnimationName().equals(characterModel.idleAnim)) {
                    lowerChannel.setAnim(characterModel.idleAnim,0.3f);
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
