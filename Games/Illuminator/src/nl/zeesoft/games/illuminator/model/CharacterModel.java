package nl.zeesoft.games.illuminator.model;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.SkeletonControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Abstract character model.
 */
public abstract class CharacterModel {
    public String   modelFile           = "Models/SuperHero/SuperHero.j3o";
    public float    scale               = 0.2f;
    public float    rotY                = FastMath.PI;
    public float    radius              = 0.5f;
    public float    height              = 1.8f;
    public Vector3f translation         = new Vector3f(0.0f,-1.4f,0.0f);
    
    public float    walkSpeed           = 0.05f;
    public float    walkSpeedAttackMult = 3.0f;
    public float    jumpSpeed           = 15;
    public float    jumpSpeedMult       = 4.0f;
    public float    fallSpeed           = 20;
    public float    gravity             = 25;
    public float    stepSize            = 0.3f;

    public String   animRoot            = "Meshes";
    
    public String   idleAnim            = "Idle";
    public String   walkAnim            = "Walk";
    public String   jumpAnim            = "Idle";

    public String[] attacks             = {"Attack.Jab","Attack.Hook"};
    public String[] impacts             = {"Impact.Jab","Impact.Hook"};

    public String[] attackSounds        = {"Sounds/Swoosh01.wav"};
    public String[] impactSounds        = {"Sounds/Impact01.wav","Sounds/Impact02.wav"};
    
    public Node     model               = null;
    
    public int      maxHealth           = 100;
    public int      maxMana             = 100;

    public int[]    attackDamages       = {10,20};
    
    public boolean isAttackAnim(String animName) {
        boolean r = false;
        for (int i = 0; i<attacks.length; i++) {
            if (attacks[i].equals(animName)) {
                r = true;
                break;
            }
        }
        return r;
    }

    public boolean isImpactAnim(String animName) {
        boolean r = false;
        for (int i = 0; i<impacts.length; i++) {
            if (impacts[i].equals(animName)) {
                r = true;
                break;
            }
        }
        return r;
    }
    
    public void addAnimEventListener(AnimEventListener listener) {
        if (model!=null) {
            AnimControl animControl = model.getChild("Meshes").getControl(AnimControl.class);
            animControl.addListener(listener);
        }
    }

    public AnimChannel getNewAnimChannel(boolean lowerBody) {
        AnimChannel r = null;
        if (model!=null) {
            AnimControl animControl = model.getChild("Meshes").getControl(AnimControl.class);
            r = animControl.createChannel();
            if (lowerBody) {
                r.addBone("Root");
                r.addBone("Hip.L");
                r.addBone("Leg.Upper.L");
                r.addBone("Leg.Lower.L");
                r.addBone("Foot.L");
                r.addBone("Hip.R");
                r.addBone("Leg.Upper.R");
                r.addBone("Leg.Lower.R");
                r.addBone("Foot.R");
            } else {
                r.addBone("Back");
                r.addBone("Chest");
                r.addBone("Neck");
                r.addBone("Head");
                r.addBone("Shoulder.L");
                r.addBone("Arm.Upper.L");
                r.addBone("Arm.Lower.L");
                r.addBone("Hand.L");
                r.addBone("Fingers.L");
                r.addBone("Shoulder.R");
                r.addBone("Arm.Upper.R");
                r.addBone("Arm.Lower.R");
                r.addBone("Hand.R");
                r.addBone("Fingers.R");
            }
            r.setAnim(idleAnim);
        }
        return r;
    }

    public void addImpactControl(GhostControl impactControl) {
        getBone("Chest").addControl(impactControl);
    }

    public void addFistControl(GhostControl fistControl,boolean left) {
        String name = "Hand.L";
        if (!left) {
            name = "Hand.R";
        }
        getBone(name).addControl(fistControl);
    }

    public void attachParticleEmitterToHead(ParticleEmitter emitter) {
        getBone("Head").attachChild(emitter);
    }
    
    private Node getBone(String name) {
        Node r = null;
        if (model!=null) {
            SkeletonControl skeletonControl = model.getChild(animRoot).getControl(SkeletonControl.class);
            r = skeletonControl.getAttachmentsNode(name);
        }
        return r;
    }
}
