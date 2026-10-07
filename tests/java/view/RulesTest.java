package view;

/** Headless checks against the actual coursework movement and goal classes. */
public final class RulesTest {
    private static void check(boolean result, String message) {
        if (!result) throw new AssertionError(message);
    }
    public static void main(String[] args) {
        ID[] robots = {ID.Robot, ID.Robot2, ID.Robot3, ID.Robot4};
        for (int token = 1; token <= 16; token++) {
            Handler handler = new Handler();
            Move move = new Move(handler, null, null);
            move.setTokenNum(token);
            ID tokenId = move.getIDToken(token);
            handler.addObject(new Token(100, 100, 28, 28, tokenId, token));
            for (int color = 0; color < 4; color++) {
                Robot robot = new Robot(103, 103, 22, 22, robots[color], handler);
                check(move.tokenTaken(robot, tokenId) == (color == (token - 1) % 4), "Goal color mismatch");
                robot.setX(200);
                check(!move.tokenTaken(robot, tokenId), "Distant robot took a token");
            }
        }
        Handler handler = new Handler();
        Move move = new Move(handler, null, null);
        Robot robot = new Robot(20, 20, 22, 22, ID.Robot, handler);
        handler.addObject(robot);
        handler.addObject(new Robot(30, 20, 22, 22, ID.Robot2, handler));
        check(move.collision2(robot, ID.Robot), "Robot collision missed");
        handler.object.clear();
        handler.addObject(new Barrier(30, 20, 6, 36, ID.Barrier));
        check(move.collision2(robot, ID.Robot), "Wall collision missed");
        robot.setX(100);
        check(!move.collision2(robot, ID.Robot), "False wall collision");
        System.out.println("Java goal checks passed for all 16 tokens and four robot colors; collision checks passed.");
    }
}
