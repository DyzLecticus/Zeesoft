package nl.zeesoft.games.illuminator.controls;

import com.jme3.light.SpotLight;
import com.jme3.math.FastMath;
import com.jme3.scene.Node;

public class PlayerLight extends Node {
    private SpotLight spot = null;
    
    public void initialize() {
        spot = new SpotLight();
        spot.setSpotRange(30f);
        spot.setSpotInnerAngle(35f * FastMath.DEG_TO_RAD);
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD);
        this.addLight(spot);
    }
    
    public SpotLight getSpot() {
        return spot;
    }
}
