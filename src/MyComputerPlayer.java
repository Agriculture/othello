import gki.game.*;

public class MyComputerPlayer<MoveT> implements IComputerPlayer<MoveT>
{
	private Double best = null;
	private MoveT bestMove = null;
    public String getLastMoveDescription()
    {
        return "best: "+best+" bestMove: "+bestMove.toString();
    }

    public MoveT findBestMove(IStdGame<MoveT> game, int maxDepth,
            IGameEvaluator<MoveT> evaluator)
    {
        //++++++++++++++++++++++++++++++++++++
        // student begin

        //Todo: bessere Strategie, beste erreichbare Bewertung genauer setzen
        //simpel: ersten gefundenen Zug durchführen
		Node<MoveT> state = new Node(game, null, 0);

        return state.getBestMove(evaluator, maxDepth);
        // student end
        //+++++++++++++++++++++++++++++++++
    }
}
