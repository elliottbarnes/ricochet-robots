package view;

import java.awt.Rectangle;

/** Replays generated routes through Move rather than trusting the solver's model. */
public final class HintSolverTest {
    private static final ID[] ROBOTS = {ID.Robot, ID.Robot2, ID.Robot3, ID.Robot4};
    private static int routesChecked;

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    private static Robot robot(Handler handler, int number) {
        for (GameObject object : handler.object) {
            if (object.getId() == ROBOTS[number]) return (Robot) object;
        }
        throw new AssertionError("Missing test robot");
    }

    private static GameObject token(Handler handler, int number) {
        for (GameObject object : handler.object) {
            if (object.getId() == ID.valueOf("Token" + number)) return object;
        }
        throw new AssertionError("Missing test token");
    }

    private static Handler openBoard(int tokenNumber) {
        Handler handler = new Handler();
        for (int x = 0; x < 592; x += 37) {
            for (int y = 0; y < 592; y += 37) {
                handler.addObject(new BoardCell(x, y, 36, 36, ID.Cell));
            }
        }
        handler.addObject(new Barrier(0, 0, 6, 600, ID.Barrier));
        handler.addObject(new Barrier(0, 0, 600, 6, ID.Barrier));
        handler.addObject(new Barrier(588, 0, 6, 600, ID.Barrier));
        handler.addObject(new Barrier(0, 588, 600, 6, ID.Barrier));
        for (int robot = 0; robot < 4; robot++) {
            handler.addObject(new Robot(37 * (8 + robot) + 7, 377, 22, 22, ROBOTS[robot], handler));
        }
        handler.addObject(new Token(555, 0, 28, 28, ID.valueOf("Token" + tokenNumber), tokenNumber));
        return handler;
    }

    private static String geometry(Handler handler) {
        StringBuilder data = new StringBuilder();
        for (GameObject object : handler.object) {
            data.append(object.getId()).append(':').append(object.getBounds())
                    .append(':').append(object.getVelX()).append(':').append(object.getVelY()).append(';');
        }
        return data.toString();
    }

    private static Move silentMove(Handler handler, int tokenNumber) {
        Move move = new Move(handler, null, null) {
            @Override public void message() { setFlag(true); }
        };
        move.setTokenNum(tokenNumber);
        return move;
    }

    private static void perform(Move move, int robot, int direction) {
        switch (direction) {
            case 0 -> move.up(robot + 1);
            case 1 -> move.down(robot + 1);
            case 2 -> move.left(robot + 1);
            case 3 -> move.right(robot + 1);
            default -> throw new AssertionError("Invalid direction");
        }
    }

    private static void replay(Handler handler, int target, HintSolver.Result result) {
        check(result.solved(), result.message());
        Move move = silentMove(handler, target);
        for (HintSolver.Step step : result.steps()) perform(move, step.robot(), step.direction());
        check(move.tokenTaken(robot(handler, (target - 1) % 4), move.getIDToken(target)),
                "Suggested route does not reach native target " + target + ": " + result.message());
        routesChecked++;
    }

    private static HintSolver.Result searchUnchanged(Handler handler, int target) {
        String before = geometry(handler);
        HintSolver.Result result = HintSolver.search(handler, target, 150_000, 3_000);
        check(before.equals(geometry(handler)), "Solver mutated the live board");
        return result;
    }

    private static void colorsAndBorders() {
        for (int target = 1; target <= 16; target++) {
            Handler handler = openBoard(target);
            int color = (target - 1) % 4;
            robot(handler, color).setX(7);
            robot(handler, color).setY(7);
            HintSolver.Result result = searchUnchanged(handler, target);
            check(result.steps().size() == 1 && result.steps().get(0).robot() == color
                    && result.steps().get(0).direction() == 3, "Wrong one-move color or border hint");
            replay(handler, target, result);
        }
    }

    private static void wallsAndRobotStops() {
        for (boolean wall : new boolean[] {false, true}) {
            Handler handler = openBoard(1);
            robot(handler, 0).setX(7);
            robot(handler, 0).setY(118);
            if (wall) handler.addObject(new Barrier(182, 111, 6, 36, ID.Barrier));
            else {
                robot(handler, 1).setX(192);
                robot(handler, 1).setY(118);
            }
            token(handler, 1).setX(148);
            token(handler, 1).setY(111);
            HintSolver.Result result = searchUnchanged(handler, 1);
            check(result.steps().size() == 1, "Expected a one-move obstacle hint");
            replay(handler, 1, result);
        }
    }

    private static Handler nativeBoard(boolean complex) {
        return complex ? new GameBoardComplex().getHandler() : new GameBoard().getHandler();
    }

    private static void sourceBoards() {
        for (boolean complex : new boolean[] {false, true}) {
            // Each target is placed at a real Move result, then independently searched/replayed.
            for (int color = 0; color < 4; color++) {
                for (int direction = 0; direction < 4; direction++) {
                    Handler expected = nativeBoard(complex);
                    perform(silentMove(expected, 0), color, direction);
                    Rectangle stop = robot(expected, color).getBounds();
                    Handler handler = nativeBoard(complex);
                    GameObject goal = token(handler, color + 1);
                    goal.setX(stop.x);
                    goal.setY(stop.y);
                    HintSolver.Result result = searchUnchanged(handler, color + 1);
                    check(result.steps().size() <= 1, "Native one-move target needed multiple moves");
                    replay(handler, color + 1, result);
                }
            }
        }
        // Exercise multi-robot routes on the unmodified coursework board and target geometry.
        int solved = 0;
        for (int target = 1; target <= 4; target++) {
            Handler handler = nativeBoard(false);
            HintSolver.Result result = searchUnchanged(handler, target);
            if (result.solved()) {
                check(!result.steps().isEmpty(), "An untouched board was already solved");
                replay(handler, target, result);
                solved++;
            } else {
                check(result.message().contains("hint limit"), "Unexpected source-board failure");
            }
        }
        check(solved >= 2, "Solver could not provide enough useful native-board routes");
    }

    private static void snapshotAndLimits() {
        Handler handler = openBoard(1);
        robot(handler, 0).setX(7);
        robot(handler, 0).setY(7);
        Handler snapshot = HintSolver.snapshot(handler);
        String before = geometry(snapshot);
        robot(handler, 0).setX(100);
        check(before.equals(geometry(snapshot)), "Snapshot shares live geometry");
        check(HintSolver.solve(snapshot, 1).contains("1. R1 right"), "Detached snapshot did not solve");
        check(before.equals(geometry(snapshot)), "Solver mutated its detached snapshot");
        HintSolver.Result limited = HintSolver.search(handler, 1, 1, 3_000);
        check(!limited.solved() && limited.message().contains("state hint limit")
                && limited.message().contains("does not mean"), "State exhaustion was misreported");
        check(HintSolver.solve(handler, 17).contains("no capture rule"), "Token 17 gained a false goal rule");
        check(HintSolver.solve(handler, 0).contains("Choose a token"), "Missing target was accepted");
        check(HintSolver.solve(null, 1).contains("no active board"), "Null board was accepted");
        check(HintSolver.solve(new Handler(), 1).contains("geometry"), "Malformed board was accepted");
        Thread.currentThread().interrupt();
        try {
            check(HintSolver.solve(handler, 1).contains("cancelled"), "Search ignored interruption");
        } finally {
            Thread.interrupted();
        }
    }

    public static void main(String[] args) {
        colorsAndBorders();
        wallsAndRobotStops();
        sourceBoards();
        snapshotAndLimits();
        System.out.println("Hint solver passed " + routesChecked
                + " native Move route replays, all token colors, snapshot isolation, and search limits.");
    }
}
