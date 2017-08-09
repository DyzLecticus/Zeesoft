package nl.zeesoft.games.illuminator.model;

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
    public Vector3f translation         = new Vector3f(0.0f,-1.5f,0.0f);
    
    public float    walkSpeed           = 0.05f;
    public float    walkSpeedAttackMult = 3.0f;
    public float    jumpSpeed           = 15;
    public float    jumpSpeedMult       = 4.0f;
    public float    fallSpeed           = 20;
    public float    gravity             = 25;
    public float    stepSize            = 0.3f;

    public String   idleAnim            = "Idle";
    public String   walkAnim            = "Walk";
    public String   attackAnim          = "Jab";
    public String   jumpAnim            = "Idle";
    
    public Node     model               = null;
}
