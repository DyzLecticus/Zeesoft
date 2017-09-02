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
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract character model.
 */
public abstract class CharacterModel {
    public boolean          godMode             = false;
    public String           modelFile           = "Models/SuperHero/SuperHero.j3o";
    public float            scale               = 0.2f;
    public float            rotY                = FastMath.PI;
    public float            radius              = 0.5f;
    public float            height              = 1.8f;
    public Vector3f         translation         = new Vector3f(0.0f,-1.4f,0.0f);
    
    public float            walkSpeed           = 0.05f;
    public float            walkSpeedAttackMult = 1.5f;
    public float            jumpSpeed           = 15;
    public float            jumpSpeedMult       = 3.0f;
    public float            fallSpeed           = 20;
    public float            gravity             = 25;
    public float            stepSize            = 0.3f;

    public String           animRoot            = "Meshes";
    
    public String           idleAnim            = "Idle";
    public String           walkAnim            = "Walk";
    public String           jumpAnim            = "Idle";

    public List<String>     attacks             = new ArrayList<String>();
    public List<Integer>    attackDamages       = new ArrayList<Integer>();
    public List<String>     attackSounds        = new ArrayList<String>();

    public List<String>     impacts             = new ArrayList<String>();
    public List<String>     impactSounds        = new ArrayList<String>();

    public List<String>     spells              = new ArrayList<String>();
    public List<Integer>    spellCost           = new ArrayList<Integer>();
    public List<Integer>    spellDamages        = new ArrayList<Integer>();
    public List<String>     spellSounds         = new ArrayList<String>();

    public Node             model               = null;
    
    public int              maxHealth           = 100;
    public int              maxMana             = 100;

    public CharacterModel() {
        attacks.add("Attack.Jab");
        attacks.add("Attack.Hook");
        attackDamages.add(10);
        attackDamages.add(20);
        attackDamages.add(30);
        attackSounds.add("Sounds/Swoosh01.wav");
        
        impacts.add("Impact.Jab");
        impacts.add("Impact.Hook");
        impacts.add("Impact.Uppercut");
        
        impactSounds.add("Sounds/Impact01.wav");
        impactSounds.add("Sounds/Impact02.wav");
        impactSounds.add("Sounds/Impact02.wav");

        spells.add("Cast.BallOfKnowledge");
        spellCost.add(10);
        spellDamages.add(50);
        spellSounds.add("Sounds/Swoosh01.wav");
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

    public Node getFist(boolean left) {
        String name = "Hand.L";
        if (!left) {
            name = "Hand.R";
        }
        return getBone(name);
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
