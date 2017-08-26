package nl.zeesoft.games.illuminator;

import com.jme3.app.SimpleApplication;
import nl.zeesoft.games.illuminator.model.GameModel;

/**
 * Main entry point of the game.
 */
public class Game extends SimpleApplication {
    private GameModel               gameModel       = null;
    
    private PlayState               playState       = null;
    
    public Game(GameModel gameModel) {
        this.gameModel = gameModel;
    }
    
    @Override
    public void start() {
        setSettings(gameModel.getAppSettings());
        setDisplayFps(gameModel.isDebug()); 
        setDisplayStatView(gameModel.isDebug());
        super.start();
    }
    
    @Override
    public void simpleInitApp() {
        // TODO: Create menu state
        playState = new PlayState(gameModel,mouseInput,flyCam,listener);
        stateManager.attach(playState);
    }
}
