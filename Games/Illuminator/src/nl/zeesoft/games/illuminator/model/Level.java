package nl.zeesoft.games.illuminator.model;

public class Level {
    public int      opponents   = 1;
    public int      toughGuys   = 0;
    public int      goal        = 3;
    
    public Level(int opponents,int toughGuys, int goal) {
        this.opponents = opponents;
        this.toughGuys = toughGuys;
        this.goal = goal;
    }
}
