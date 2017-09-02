package nl.zeesoft.games.illuminator.model;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;
import java.util.ArrayList;
import java.util.List;

/**
 * The game model is designed to contain configurable game settings.
 * This pattern should make it easy to change game elements and game play.
 */
public class GameModel {
    public static final String      DIFFICULTY_EASY     = "Easy";
    public static final String      DIFFICULTY_MEDIUM   = "Medium";
    public static final String      DIFFICULTY_HARD     = "Hard";
    
    private boolean                 debug               = false;
    private boolean                 godMode             = false;
    private AppSettings             settings            = null;
    private String                  difficulty          = DIFFICULTY_MEDIUM;
    private List<Level>             levels              = new ArrayList<Level>();
    
    private PlayerModel             playerModel         = null;
    
    public void initialize() {
        settings = new AppSettings(true);
        settings.setFrameRate(60);
        settings.setTitle("Illuminator");
        
        levels.add(new Level(2,1,10));
        levels.add(new Level(3,1,20));
        levels.add(new Level(4,2,30));
        
        playerModel = new PlayerModel();
    }
    
    public void loadModels(AssetManager assetManager) {
        playerModel.model = (Node) assetManager.loadModel(playerModel.modelFile);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
    
    public boolean isDebug() {
        return debug;
    }

    public void setGodMode(boolean godMode) {
        this.godMode = godMode;
    }
    
    public boolean isGodMode() {
        return godMode;
    }

    public AppSettings getAppSettings() {
        return settings;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public List<Level> getLevels() {
        return levels;
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
