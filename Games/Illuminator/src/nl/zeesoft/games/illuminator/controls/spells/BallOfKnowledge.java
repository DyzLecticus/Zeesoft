package nl.zeesoft.games.illuminator.controls.spells;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.GhostControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.shapes.EmitterSphereShape;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import nl.zeesoft.games.illuminator.GameControlNode;

public class BallOfKnowledge extends GameControlNode {
    private static final float  SIZE            = 0.5f;
    
    private AssetManager        assetManager    = null;
    private ParticleEmitter     flare           = null;
    
    private GhostControl        control         = null;
    
    private PointLight          glow            = null;
    
    private float               lifeTime        = 0.0f;
    private float               lifeTimeMax     = 10.0f;
    private float               burst           = 0.0f;

    private float               scale           = 0.2f;
    
    private boolean             released        = false;
    private float               speed           = 10.0f;
    
    public BallOfKnowledge(AssetManager assetManager,int lifeTimeMax) {
        this.assetManager = assetManager;
        this.lifeTimeMax = lifeTimeMax;
        this.setLocalScale(new Vector3f(scale,scale,scale));
    }
    
    @Override
    public void initialize() {
        Sphere sphere = new Sphere(10,10,SIZE);
        Geometry geom = new Geometry("Sphere", sphere);
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Blue); 
        geom.setMaterial(mat);
        this.attachChild(geom);
        
        SphereCollisionShape sphereShape = new SphereCollisionShape(SIZE * 1.2f);
        control = new GhostControl(sphereShape);
        control.setCollisionGroup(PhysicsCollisionObject.COLLISION_GROUP_02);
        control.setCollideWithGroups(PhysicsCollisionObject.COLLISION_GROUP_02);
        this.addControl(control);

        flare = getNewFlame(1,1.0f,true);
        this.attachChild(flare);
                
        glow = new PointLight();
        glow.setColor(ColorRGBA.Blue);
        glow.setRadius(SIZE * 30f);
    }

    @Override
    public boolean update(float tpf) {
        boolean done = false;
        lifeTime += tpf;
        if (lifeTime>=lifeTimeMax) {
           done = true; 
        }
        
        if (scale<1.0f) {
            scale += (tpf * 3);
        }
        if (scale>1.0f) {
            scale = 1.0f;
        }
        this.setLocalScale(new Vector3f(scale,scale,scale));
        
        burst += tpf;
        if (burst>=0.1f) {
            burst = 0.0f;
            flare.emitAllParticles();
        }
        
        if (released) {
            if (speed < 100f) {
                speed = speed + 0.5f;
            }
            this.move(getLocalRotation().getRotationColumn(2).mult(-speed * tpf));
        }

        glow.setPosition(control.getPhysicsLocation());

        return done;
    }
    
    @Override
    public void attachToRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.attachToRootNode(rootNode, bulletAppState);
        rootNode.addLight(glow);
    }
    
    @Override
    public void detachFromRootNode(Node rootNode,BulletAppState bulletAppState) {
        super.detachFromRootNode(rootNode, bulletAppState);
        rootNode.removeLight(glow);
    }
    
    public void setReleased(boolean released) {
        this.released = released;
    }
    
    public GhostControl getControl() {
        return control;
    }
    
    public PointLight getGlow() {
        return glow;
    }
    
    private ParticleEmitter getNewFlame(int countFactor, float countFactorF,boolean pointSprite){
        ParticleMesh.Type type = ParticleMesh.Type.Point;
        if (!pointSprite) {
            type = ParticleMesh.Type.Triangle;
        }
        ParticleEmitter flame = new ParticleEmitter("Flame", type, 32 * countFactor);
        flame.setSelectRandomImage(true);
        flame.setRandomAngle(true);
        flame.setStartColor(new ColorRGBA(0.0f,0.0f,0.5f, (float) (1f / countFactorF)));
        flame.setEndColor(new ColorRGBA(0.0f,0.0f,0.1f,0f));
        flame.setStartSize(SIZE);
        flame.setEndSize(0.01f);
        flame.setShape(new EmitterSphereShape(Vector3f.ZERO, 1f));
        flame.setParticlesPerSec(0);
        flame.setGravity(0,0,0);
        flame.setLowLife(0.3f);
        flame.setHighLife(0.8f);
        flame.getParticleInfluencer().setInitialVelocity(new Vector3f(0,3f,0));
        flame.getParticleInfluencer().setVelocityVariation(0.2f);
        flame.setImagesX(2);
        flame.setImagesY(2);
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/Particle.j3md");
        mat.setTexture("Texture",assetManager.loadTexture("Effects/Explosion/flame.png"));
        mat.setBoolean("PointSprite",pointSprite);
        flame.setMaterial(mat);
        return flame;
    }
}
