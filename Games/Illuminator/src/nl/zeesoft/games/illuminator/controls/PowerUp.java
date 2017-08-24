package nl.zeesoft.games.illuminator.controls;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;

public class PowerUp extends Node {
    private static final float  SIZE            = 0.3f;
    
    private AssetManager        assetManager    = null;
    private GhostControl        control         = null;
    
    private float               lifeTimeMax     = 0;
    private float               lifeTime        = 0;

    public PowerUp(AssetManager assetManager,float lifeTimeMax) {
        this.assetManager = assetManager;
        this.lifeTimeMax = lifeTimeMax;
    }
    
    public void initialize() {
        Box b = new Box(SIZE * 0.5f,SIZE * 2.0f,SIZE * 0.5f);
        Geometry geom = new Geometry("Box", b);
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green); 
        geom.setMaterial(mat);
        attachChild(geom);
        
        BoxCollisionShape boxShape = new BoxCollisionShape(new Vector3f(SIZE * 0.5f,SIZE * 2.0f,SIZE * 0.5f));
        control = new GhostControl(boxShape);
        control.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_01);
        control.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_01);
        this.addControl(control);
    }
    
    public GhostControl getControl() {
        return control;
    }
    
    public boolean update(float tpf) {
        boolean done = false;
        lifeTime += tpf;
        if (lifeTime>=lifeTimeMax) {
            done = true;
        }
        return done;
    }
}
