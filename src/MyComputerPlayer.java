import gki.game.*;

public class MyComputerPlayer<MoveT> implements IComputerPlayer<MoveT>
{
    private double bestReachableEvaluation=0;
    private MoveT lastMove;
    public String getLastMoveDescription()
    {
        return "Erreichbare Bewertung: "+bestReachableEvaluation+
                " letzter Zug: "+lastMove.toString();
    }

    public MoveT findBestMove(IStdGame<MoveT> game, int maxDepth,
            IGameEvaluator<MoveT> evaluator)
    {
        //++++++++++++++++++++++++++++++++++++
        // student begin

        //Todo: bessere Strategie, beste erreichbare Bewertung genauer setzen
        //simpel: ersten gefundenen Zug durchführen
        for(MoveT move:game.getPossibleMoves())
        {
            bestReachableEvaluation=0;
            lastMove=move;

            return move;
        }

        return null; //falls kein Zug möglich ist
        // student end
        //+++++++++++++++++++++++++++++++++
    }
}
