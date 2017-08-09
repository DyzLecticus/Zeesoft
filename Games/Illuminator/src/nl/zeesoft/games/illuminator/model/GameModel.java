package nl.zeesoft.games.illuminator.model;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 * The game model is designed to contain configurable game settings.
 * This pattern should make it easy to change game elements and game play.
 */
public class GameModel {
    public static final String      DIFFICULTY_EASY     = "Easy";
    public static final String      DIFFICULTY_MEDIUM   = "Medium";
    public static final String      DIFFICULTY_HARD     = "Hard";
    
    private boolean                 debug               = true;
    private AppSettings             settings            = null;
    private String                  difficulty          = DIFFICULTY_MEDIUM;
    
    private PlayerModel             playerModel         = null;
    
    public void initialize() {
        settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("Illuminator");
        
        playerModel = new PlayerModel();
    }
    
    public void loadModels(AssetManager assetManager) {
        playerModel.model = (Node) assetManager.loadModel(playerModel.modelFile);
    }
    
    public boolean isDebug() {
        return debug;
    }

    public AppSettings getAppSettings() {
        return settings;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public PlayerModel getPlayerModel() {
        return playerModel;
    }
    
    public OpponentModel getNewOpponentModel(AssetManager assetManager) {
        OpponentModel mod = new OpponentModel();
        mod.model = (Node) assetManager.loadModel(mod.modelFile);
        return mod;
    }
}
