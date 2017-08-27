package nl.zeesoft.games.illuminator.controls;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import nl.zeesoft.games.illuminator.GameControlNode;
import nl.zeesoft.games.illuminator.model.CharacterModel;

/**
 * Abstract character.
 * 
 * Combines the character model with character control.
 */
public abstract class Character extends GameControlNode implements AnimEventListener {
    private AssetManager        assetManager        = null;
    
    private CharacterModel      characterModel      = null;
    private CharacterControl    characterControl    = null;
    private RigidBodyControl    rigidControl        = null;

    private GhostControl        impactControl       = null; 
    private GhostControl        fistControlLeft     = null; 
    private GhostControl        fistControlRight    = null;

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

    private int                 selectedSpell       = 0;
    private boolean             cast                = false;
    private int                 casting             = -1;

    private AudioNode           attackAudio         = null;
    private AudioNode[]         impactAudio         = null;
    private AudioNode[]         castAudio           = null;

    private CharacterStatusBar  statusBar           = null;
    private int                 health              = 0;
    
    private DeathExplosion      death               = null;
    
    public Character(CharacterModel characterModel,AssetManager assetManager) {
        this.assetManager = assetManager;
        this.characterModel = characterModel;
    }
    
    @Override
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

        // Audio
        attackAudio = getNewAudioNode(characterModel.attackSounds.get(0),0.1f);
        this.attachChild(attackAudio);
        castAudio = new AudioNode[characterModel.spellSounds.size()];
        for (int i = 0; i < characterModel.spellSounds.size(); i++) {
            castAudio[i] = getNewAudioNode(characterModel.spellSounds.get(i)); 
            this.attachChild(castAudio[i]);
        }
        impactAudio = new AudioNode[characterModel.impactSounds.size()];
        for (int i = 0; i < characterModel.impactSounds.size(); i++) {
            impactAudio[i] = getNewAudioNode(characterModel.impactSounds.get(i)); 
            this.attachChild(impactAudio[i]);
        }
        
        // Status bar
        statusBar = new CharacterStatusBar(assetManager,this);
        statusBar.initialize();
        statusBar.setLocalTranslation(0.5f,2,0);
        setHealth(characterModel.maxHealth);

        // Impact control
        CapsuleCollisionShape impactShape = new CapsuleCollisionShape(characterModel.radius * 0.4f,characterModel.height * 0.7f);
        impactControl = new GhostControl(impactShape);
        impactControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        impactControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
        characterModel.addImpactControl(impactControl);
        
        // Fist controls
        fistControlLeft = getNewFistControl();
        fistControlRight = getNewFistControl();
        characterModel.getFist(true).addControl(fistControlLeft);
        characterModel.getFist(false).addControl(fistControlRight);
        
        // Death
        death = new DeathExplosion(assetManager);
        death.initialize();
    }
    
    public abstract Vector3f getDirection();
    public abstract Vector3f getLeft();
    
    @Override
    public boolean update(float tpf) {
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
        
        return getHealth()>0;
    }

    @Override
    public void attachToRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.attachToRootNode(rootNode,bulletAppState);
        if (bulletAppState!=null) {
            bulletAppState.getPhysicsSpace().add(getImpactControl());
            bulletAppState.getPhysicsSpace().add(getFistControlLeft());
            bulletAppState.getPhysicsSpace().add(getFistControlRight());
        }
    }

    @Override
    public void detachFromRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.detachFromRootNode(rootNode,bulletAppState);
        if (bulletAppState!=null) {
            bulletAppState.getPhysicsSpace().remove(getImpactControl());
            bulletAppState.getPhysicsSpace().remove(getFistControlLeft());
            bulletAppState.getPhysicsSpace().remove(getFistControlRight());
        }
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(channel == upperChannel) {
            if (attacking>=0 && characterModel.attacks.contains(animName)) {
                if (attackNext) {
                    attackNext = false;
                    attackDelay = 0.0f;
                    attacking++;
                    upperChannel.setAnim(characterModel.attacks.get(attacking),0.001f);
                    upperChannel.setLoopMode(LoopMode.DontLoop);
                    attackAudio.playInstance();
                } else {
                    attacking = -1;
                }
            }
            if (impacting>=0 && characterModel.impacts.contains(animName)) {
                impacting = -1;
                stopShockWave();
            }
            if (casting>=0 && characterModel.spells.contains(animName)) {
                casting = -1;
            }
            if (attacking<0 && impacting<0 && casting<0) {
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
    
    protected void addRigidBody() {
        CapsuleCollisionShape playerShape = new CapsuleCollisionShape(characterModel.radius,characterModel.height);
        rigidControl = new RigidBodyControl(playerShape,80);
        rigidControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        rigidControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_02);
        rigidControl.setKinematic(true);
        this.addControl(rigidControl);
    }
    
    protected void startShockWave(int impacting) {
        // Override to implement
    }

    protected void stopShockWave() {
        // Override to implement
    }

    protected float getAttackDelay() {
        return 0.0f;
    }
    
    public void setHealth(int health) {
        if (health<0) {
            health = 0;
        } else if (health>100) {
            health = 100;
        }
        this.health = health;
        statusBar.setHealth(health);
    }

    public int getHealth() {
        return health;
    }
    
    public DeathExplosion getDeath() {
        return death;
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
        if (attack && attacking>=0 && attacking<(characterModel.attacks.size() - 1)) {
            attackNext = true;
        }
    }

    public void setCast(boolean v) {
        if (v && impacting>=0) {
            v = false;
        }
        cast = v;
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
            (nodeA!=impactControl&&nodeB!=impactControl) &&
            (nodeA==fistControlLeft || nodeA==fistControlRight || nodeB==fistControlLeft ||nodeB==fistControlRight)
            ) {
            r = attacking;
        }
        return r;
    }

    public boolean applyFistImpact(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB, int impact) {
        boolean r = false;
        if (attacking<0 && casting<0 && 
            (nodeA!=characterControl&&nodeB!=characterControl) &&
            (nodeA!=fistControlLeft&&nodeA!=fistControlRight&&nodeB!=fistControlLeft&&nodeB!=fistControlRight) &&
            (nodeA==impactControl || nodeB==impactControl)
            ) {
            if (impacting!=impact) {
                r = true;
                impacting = impact;
                if (!characterModel.godMode) {
                    setHealth(health - characterModel.attackDamages.get(impacting));
                }
                startShockWave(impacting);        
                impactAudio[impacting].playInstance();
                upperChannel.setAnim(characterModel.impacts.get(impacting),0.001f);
                upperChannel.setLoopMode(LoopMode.DontLoop);
            }
        }
        return r;
    }

    public boolean applySpellImpact(PhysicsCollisionObject nodeA, PhysicsCollisionObject nodeB) {
        boolean r = false;
        // TODO: Implement
        return r;
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
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

    public GhostControl getImpactControl() {
        return impactControl;
    }
    
    public GhostControl getFistControlLeft() {
        return fistControlLeft;
    }
    
    public GhostControl getFistControlRight() {
        return fistControlRight;
    }
    
    private void handleAnimations(float tpf) {
        if (impacting>=0) {
            // Waiting for impact animation to finish
        } else if (attacking>=0) {
            // Waiting for attack animation to finish
        } else if (casting>=0) {
            // Waiting for spell casting animation to finish
        } else if (attack) {
            attackDelay = attackDelay + tpf;
            if (attackDelay>=getAttackDelay()) {
                attackDelay = 0.0f;
                attacking = 0;
                upperChannel.setAnim(characterModel.attacks.get(attacking),0.001f);
                upperChannel.setLoopMode(LoopMode.DontLoop);
                attackAudio.playInstance();
                attack = false;
            }
        } else if (cast) {
            casting = 0;
            upperChannel.setAnim(characterModel.spells.get(casting),0.001f);
            upperChannel.setLoopMode(LoopMode.DontLoop);
            castAudio[casting].playInstance();
            cast = false;
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

    private GhostControl getNewFistControl() {
        BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(0.1f,0.2f,0.1f));
        GhostControl fistControl = new GhostControl(boxShape);
        // Dont want these colliding with anything other than each other to save efficiency
        fistControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_03);
        fistControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_03);
        return fistControl;
    }

    private AudioNode getNewAudioNode(String fileName) {
        return getNewAudioNode(fileName,2.0f);
    }
    
    private AudioNode getNewAudioNode(String fileName,float volume) {
        AudioNode audio = new AudioNode(assetManager,fileName,DataType.Buffer); 
        audio.setPositional(true);
        audio.setReverbEnabled(true);
        audio.setRefDistance(10f);
        audio.setMaxDistance(50f);
        audio.setVolume(volume);
        audio.setLooping(false);
        return audio;
    }
}
