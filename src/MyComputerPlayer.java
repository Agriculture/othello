import gki.game.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import othello.base.OthelloGame;

public class MyComputerPlayer<MoveT> implements IComputerPlayer<MoveT>
{
	private Double best = null;
	private MoveT bestMove = null;
    public String getLastMoveDescription()
    {
        return "best: "+best+" bestMove: "+bestMove.toString();
    }

    public MoveT findBestMove(IStdGame<MoveT> game, int bestDepth,
            IGameEvaluator<MoveT> evaluator)
    {
        //++++++++++++++++++++++++++++++++++++
        // student begin

        //Todo: bessere Strategie, beste erreichbare Bewertung genauer setzen
        //simpel: ersten gefundenen Zug durchführen
		Double value;
		IStdGame<MoveT> tmpGame = null;
		best = null;

        for(MoveT move:game.getPossibleMoves())
        {
			tmpGame = game.copy();
			try {
				tmpGame.doMove(move);
			} catch (Exception ex) {
				Logger.getLogger(MyComputerPlayer.class.getName()).log(Level.SEVERE, null, ex);
			}
			value = evaluator.evaluateGame(tmpGame);
		//	System.out.println("MOVE: "+move+"\tvalue: "+value);

			if((best == null) || (game.isPlayer1sTurn() && value > best) ||
								(!game.isPlayer1sTurn() && value < best)){
				bestMove = move;
				best = value;
			}

        }
        return bestMove;
        // student end
        //+++++++++++++++++++++++++++++++++
    }
}
