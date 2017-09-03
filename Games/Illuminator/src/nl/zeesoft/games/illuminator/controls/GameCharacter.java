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
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import nl.zeesoft.games.illuminator.GameControlNode;
import nl.zeesoft.games.illuminator.controls.spells.BallOfKnowledge;
import nl.zeesoft.games.illuminator.model.CharacterModel;
import nl.zeesoft.games.illuminator.AttackHandler;

/**
 * Abstract character.
 * 
 * Combines the character model with character control.
 */
public abstract class GameCharacter extends GameControlNode implements AnimEventListener {
    private AssetManager        assetManager        = null;
    private CharacterModel      characterModel      = null;
    private AttackHandler       collisionCollector  = null;
    
    private CharacterControl    characterControl    = null;
    private GhostControl        ghostControl        = null;

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
    private float               attackTime          = 0.0f;
    private int                 impacting           = -1;

    private GameControlNode     impactingObject     = null;

    private int                 selectedSpell       = 0;
    private boolean             cast                = false;
    private int                 casting             = -1;

    private AudioNode           attackAudio         = null;
    private AudioNode[]         impactAudio         = null;
    private AudioNode[]         castAudio           = null;

    private CharacterStatusBar  statusBar           = null;
    private int                 health              = 0;
    private int                 mana                = 0;
    
    private DeathExplosion      death               = null;
    
    public GameCharacter(CharacterModel characterModel,AssetManager assetManager,AttackHandler collisionCollector) {
        this.assetManager = assetManager;
        this.characterModel = characterModel;
        this.collisionCollector = collisionCollector;
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
        
        ghostControl = new GhostControl(playerShape);
        ghostControl.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        ghostControl.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_02);
        this.addControl(ghostControl);

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
        setMana(0);
        
        // Death
        death = new DeathExplosion(assetManager);
        death.initialize();
    }
    
    public abstract Vector3f getDirection();
    public abstract Vector3f getLeft();
    
    @Override
    public boolean update(float tpf) {
        if (attacking>=0) {
            attackTime += tpf;
            if (attackTime>characterModel.attackDelays.get(attacking) &&
                attackTime<(characterModel.attackDelays.get(attacking) + 0.2f)
                ) {
                if (collisionCollector.getAttackCollisions(this,attacking).size()>0) {
                    attackTime += 1.0f;
                }
            }
        }
        
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
            bulletAppState.getPhysicsSpace().add(getGhostControl());
        }
    }

    @Override
    public void detachFromRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.detachFromRootNode(rootNode,bulletAppState);
        if (bulletAppState!=null) {
            bulletAppState.getPhysicsSpace().remove(getGhostControl());
        }
    }
    
    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if(channel == upperChannel) {
            if (attacking>=0 && characterModel.attacks.contains(animName)) {
                if (attackNext) {
                    attackNext = false;
                    attackDelay = 0.0f;
                    attackTime = 0.0f;
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
                impactingObject = null;
                stopShockWave();
            }
            if (casting>=0 && characterModel.spells.contains(animName)) {
                casting = -1;
                stopCast();
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
    
    protected void startShockWave(int impacting) {
        // Override to implement
    }

    protected void stopShockWave() {
        // Override to implement
    }

    protected void startCast(int casting) {
        // Override to implement
    }

    protected void stopCast() {
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

    public void setMana(int mana) {
        if (mana<0) {
            mana = 0;
        } else if (mana>100) {
            mana = 100;
        }
        this.mana = mana;
        statusBar.setMana(mana);
    }

    public int getMana() {
        return mana;
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
        if (v && (impacting>=0 || casting>=0)) {
            v = false;
        }
        attack = v;
        if (attack && attacking>=0 && attacking<(characterModel.attacks.size() - 1)) {
            attackNext = true;
        }
    }

    public boolean isAttack() {
        return attack;
    }

    public void setCast(boolean v) {
        if (v && (impacting>=0 || attacking>=0 || casting>=0)) {
            v = false;
        }
        if (mana<characterModel.spellCost.get(selectedSpell)) {
            v = false;
        }
        cast = v;
    }

    public void jump() {
        if(characterControl.onGround()) {
            characterControl.jump();
            lowerChannel.setAnim(characterModel.jumpAnim,0.3f);
        }
    }

    public boolean applyFistImpact(int impact) {
        boolean r = false;
        if (attacking<0 && casting<0 && impacting!=impact) {
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
        return r;
    }

    public boolean applySpellImpact(GameControlNode spellObject) {
        boolean r = false;
        if (attacking<0 && casting<0 && impactingObject!=spellObject) {
            impactingObject = spellObject;
            if (spellObject instanceof BallOfKnowledge) {
                setHealth(health - characterModel.spellDamages.get(0));
                impacting = 2;
                startShockWave(impacting);
                impactAudio[impacting].playInstance();
                upperChannel.setAnim(characterModel.impacts.get(impacting),0.001f);
                upperChannel.setLoopMode(LoopMode.DontLoop);
                r = true;
            }
        }
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

    public GhostControl getGhostControl() {
        return ghostControl;
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
                attackTime = 0.0f;
                attacking = 0;
                upperChannel.setAnim(characterModel.attacks.get(attacking),0.001f);
                upperChannel.setLoopMode(LoopMode.DontLoop);
                attackAudio.playInstance();
                attack = false;
            }
        } else if (cast) {
            casting = selectedSpell;
            if (!characterModel.godMode) {
                setMana(mana - characterModel.spellCost.get(selectedSpell));
            }
            upperChannel.setAnim(characterModel.spells.get(casting),0.001f);
            upperChannel.setLoopMode(LoopMode.DontLoop);
            castAudio[casting].playInstance();
            startCast(casting);
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
