import gki.game.*;
import othello.base.*;

public class MyOthelloEvaluator implements IGameEvaluator<OthelloMove>
{
    public double evaluateGame(IStdGame<OthelloMove> gameSetting)
    {
        OthelloGame game=(OthelloGame) gameSetting;

        //+++++++++++++++++++++++++++++++++++++++++++
        //student begin

        return 0.5;
        //student end
        //+++++++++++++++++++++++++++++++++++++++++++
    }
}
