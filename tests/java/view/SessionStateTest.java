package view;

import controller.*;
import java.nio.file.*;
import java.util.Arrays;

public final class SessionStateTest {
    private static void check(boolean condition,String message) { if(!condition) throw new AssertionError(message); }
    private static SaveData sample(boolean complex) {
        Handler board=complex?new GameBoardComplex().getHandler():new GameBoard().getHandler();
        int[][] positions=SessionState.positions(board);
        return new SaveData(complex,true,2,15,positions[0],positions[1],9,false,
            new int[]{1,2,3,0},new int[]{8,7,0,6},3,2,4,23);
    }
    private static void rejected(Path file,String content) throws Exception {
        Files.writeString(file,content);
        try { ResourceManager.load(file.toString());throw new AssertionError("Accepted malformed save"); }
        catch(java.io.IOException expected) {}
    }
    public static void main(String[] args) throws Exception {
        Path directory=Files.createTempDirectory("ricochet-session-test-");
        Path file=directory.resolve("game.rrsave");
        try {
            for(boolean complex:new boolean[]{false,true}) {
                SaveData before=sample(complex);
                ResourceManager.save(before,file.toString());
                String text=Files.readString(file);
                check(text.contains("format=ricochet-robots-1"),"Versioned plain text");
                SaveData after=ResourceManager.load(file.toString());
                check(after.complex()==complex && after.hints() && after.palette()==2 && after.players()==15,"Settings round trip");
                check(after.token()==9 && !after.complete() && after.moves()==3 && after.timerSeconds()==23,"Round/time round trip");
                check(after.selectedRobot()==2 && after.selectedPlayer()==4,"Selection round trip");
                check(Arrays.equals(before.scores(),after.scores()) && Arrays.equals(before.bids(),after.bids()),"Scores/bids round trip");
                Handler handler=complex?new GameBoardComplex().getHandler():new GameBoard().getHandler();
                SessionState.restore(handler,after);
                check(Arrays.deepEquals(SessionState.positions(handler),new int[][]{before.x(),before.y()}),"Robot positions round trip");
                ChangeToken next=new ChangeToken();next.restore(after.token(),after.complete(),handler);
                check(next.getCurrentToken()==9 && next.remaining()==8 && !next.isFlag(),"Restored target sequence");
                next.restore(1,true,handler);check(next.isFlag() && next.remaining()==0,"Completed round restores");
                next.restore(0,false,handler);check(next.remaining()==16,"Undrawn game restores");
                SettingsController settings=after.settings();check(settings.isComplex()==complex && settings.isProtanope() && settings.isHintsEnabled() && settings.isPlayer4(),"Restored display settings");
                int[] copy=after.x();copy[0]=999;check(after.x()[0]!=999,"Defensive arrays");
                rejected(file,text.replace("x0=10","x0=-1"));
                rejected(file,text.replace("players=15","players=0"));
                rejected(file,text.replace("hints=true","hints=perhaps"));
                rejected(file,text.replace("timer=23","timer=999"));
                rejected(file,text.replace("token=9","token=17"));
            }
            rejected(file,"format=old-format\n");
            rejected(file,"format=ricochet-robots-1\n");
            rejected(file,"x".repeat(65537));
            Files.write(file,new byte[]{(byte)0xac,(byte)0xed,0,5});
            try {ResourceManager.load(file.toString());throw new AssertionError("Legacy serialization accepted");}catch(java.io.IOException expected){}
            SaveData base=sample(false);
            Handler board=new GameBoard().getHandler();int[][] old=SessionState.positions(board);
            int[] x=base.x(),y=base.y();x[0]=260;y[0]=260;
            SaveData inWall=new SaveData(false,true,0,15,x,y,9,false,base.scores(),base.bids(),0,0,0,-1);
            try {SessionState.restore(board,inWall);throw new AssertionError("Wall overlap accepted");}catch(IllegalArgumentException expected){}
            check(Arrays.deepEquals(old,SessionState.positions(board)),"Rejected restore must not partially mutate board");
            x=base.x();y=base.y();x[1]=x[0];y[1]=y[0];
            SaveData overlap=new SaveData(false,true,0,15,x,y,9,false,base.scores(),base.bids(),0,0,0,-1);
            try {SessionState.restore(board,overlap);throw new AssertionError("Robot overlap accepted");}catch(IllegalArgumentException expected){}
            BidController bids=new BidController();check(!bids.newBid("Player1",0) && !bids.newBid("Player1",-1),"Nonpositive bids rejected");
            check(bids.newBid("Player1",8) && bids.newBid("Player1",7) && !bids.newBid("Player1",9),"Decreasing bids preserved");
            System.out.println("Session round trips, settings, token sequence, malformed/legacy/oversized saves, overlap rejection, and positive bidding passed.");
        } finally { Files.deleteIfExists(file);Files.deleteIfExists(directory); }
    }
}
