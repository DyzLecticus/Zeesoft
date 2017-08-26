package nl.zeesoft.games.illuminator.controls;

import com.jme3.light.SpotLight;
import com.jme3.math.FastMath;

public class PlayerLight {
    private SpotLight spot = null;
    
    public void initialize() {
        spot = new SpotLight();
        spot.setSpotRange(10f);
        spot.setSpotInnerAngle(35f * FastMath.DEG_TO_RAD);
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD);
    }
    
    public SpotLight getSpot() {
        return spot;
    }
}
