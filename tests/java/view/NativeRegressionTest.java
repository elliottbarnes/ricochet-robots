package view;

import controller.SaveData;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Random;

/** Reproductions from the independent save/load and hint review. */
public class NativeRegressionTest {
    static Move silent(Handler handler, ChangeToken next) {
        return new Move(handler, null, next) {
            @Override public void message() { throw new AssertionError("Unexpected score after completion"); }
        };
    }
    static void perform(Move move, int robot, int direction) {
        switch(direction) {
            case 0 -> move.up(robot); case 1 -> move.down(robot);
            case 2 -> move.left(robot); case 3 -> move.right(robot);
            default -> throw new AssertionError("Invalid direction");
        }
    }
    static void check(boolean condition, String message) { if(!condition) throw new AssertionError(message); }
    public static void main(String[] args) {
        Handler handler = new GameBoard().getHandler();
        Rectangle actual = null;
        for(GameObject object:handler.object) if(object.getId()==ID.Token1) actual=object.getBounds();
        ChangeToken round = new ChangeToken(); round.restore(1,false,handler);
        var result = HintSolver.search(HintSolver.snapshot(handler),1,150000,3000);
        check(result.solved(),"Active round hint should solve its real target");
        Move replay = new Move(handler,null,null) { @Override public void message() {} };
        replay.setTokenNum(1);
        for(var step:result.steps()) perform(replay,step.robot()+1,step.direction());
        int[][] position=SessionState.positions(handler);
        check(actual.intersects(position[0][0],position[1][0],22,22),"Hint reached decorative indicator instead of target");
        for(boolean complex:new boolean[]{false,true}) {
            handler=complex?new GameBoardComplex().getHandler():new GameBoard().getHandler();
            Move move=silent(handler,null); Random random=new Random(10);
            for(int i=0;i<1000;i++) {
                perform(move,random.nextInt(4)+1,random.nextInt(4));
                position=SessionState.positions(handler);
                SaveData save=new SaveData(complex,true,0,15,position[0],position[1],5,false,new int[4],new int[4],i,1,1,-1);
                Handler restored=complex?new GameBoardComplex().getHandler():new GameBoard().getHandler();
                SessionState.restore(restored,save);
                check(Arrays.deepEquals(position,SessionState.positions(restored)),"Reachable game failed save/load round trip");
            }
        }
        handler=new GameBoard().getHandler();round=new ChangeToken();round.restore(1,true,handler);
        position=SessionState.positions(handler);Move completed=silent(handler,round);completed.setTokenNum(1);
        for(int robot=1;robot<=4;robot++) for(int direction=0;direction<4;direction++) perform(completed,robot,direction);
        check(Arrays.deepEquals(position,SessionState.positions(handler)),"Completed game accepted a move");
        check(round.isFlag() && round.getCurrentToken()==1,"Completed target sequence changed");
        System.out.println("Active-target hint, 2,000 reachable save/load states, and completed-game movement regressions passed.");
    }
}
