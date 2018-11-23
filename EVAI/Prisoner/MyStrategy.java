package evolution.prisoner.strategies;

import evolution.prisoner.Result;
import evolution.prisoner.Strategy;

public class MyStrategy extends Strategy{
    private Move lastOp = Move.COOPERATE;
    private int cooperate = 0;
    private int deceive = 0;
    private int myDeceive = 0;
    java.util.Random rnd = new java.util.Random();

    @Override
    public Move nextMove() {

        if(0 < deceive && myDeceive == 0){
            myDeceive = deceive;
        }

        if(2 < myDeceive){
            myDeceive--;
            return Move.DECEIVE;
        }
        if(0 < myDeceive ) myDeceive--;
        return Move.COOPERATE;
        /*if(lastOp == Move.COOPERATE)
        return Move.COOPERATE;
        else return Move.DECEIVE;*/
    }

    private Move getRandMove() {
        double rand = rnd.nextInt(2);
        if(rand == 0) return Move.COOPERATE;
        else return Move.DECEIVE;
    }


    @Override
    public void reward(Result res) {
        lastOp = res.getOponentsMove();
        if(lastOp == Move.COOPERATE) cooperate++;
        else deceive++;
    }

    @Override
    public String getName() {
        return "MyStrategy";
    }

    @Override
    public String authorName() {
        return "Petra Doubravová";
    }

    @Override
    public void reset() {
        deceive  = 0;
        cooperate = 0;
    }
}
