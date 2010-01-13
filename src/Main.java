import gki.game.*;
import othello.base.*;

public class Main 
{
    private static void testPlayer(IGameEvaluator<OthelloMove> evaluator, MyComputerPlayer player) throws Exception
    {
        try
        {
            double erg=OthelloControler.percentageWinsPlayerAgainstRnd(evaluator, player, 2, 100,true);
            System.out.println("Gewonnen: "+erg*100+" %");
        }
        catch(Exception ex)
        {
            System.err.println("Fehler: "+ex.toString());
            ex.printStackTrace(System.err);
        }
    }
    public static void main(String[] args) throws Exception
    {
        MyOthelloEvaluator evaluator=new MyOthelloEvaluator();
        MyComputerPlayer player=new MyComputerPlayer();
        
        //OthelloControler.run(evaluator, player);
        OthelloControler.runConsole(evaluator, player,2);
       // testPlayer(evaluator,player);
    }
}
