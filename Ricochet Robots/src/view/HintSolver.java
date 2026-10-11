package view;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Bounded, read-only hints using the same slide rules as the desktop controls. */
public final class HintSolver {
    private static final ID[] ROBOTS = {ID.Robot, ID.Robot2, ID.Robot3, ID.Robot4};
    private static final String[] DIRECTIONS = {"up", "down", "left", "right"};
    private static final int STATE_LIMIT = 150_000;
    private static final int DEPTH_LIMIT = 12;
    private static final long TIME_LIMIT_MS = 3_000;
    private static final int POSITION_BITS = 10;
    private static final int POSITION_MASK = (1 << POSITION_BITS) - 1;

    private HintSolver() { }

    /** Run off the Swing event thread; the returned route describes the starting snapshot. */
    public static String solve(Handler handler, int tokenNumber) {
        return search(handler, tokenNumber, STATE_LIMIT, TIME_LIMIT_MS).message();
    }

    /** Capture on the Swing event thread, then pass this detached geometry to solve on a worker. */
    public static Handler snapshot(Handler handler) {
        if (handler == null) return null;
        Handler copy = new Handler();
        for (GameObject object : handler.object.toArray(new GameObject[0])) {
            Rectangle bounds = object.getBounds();
            FrozenObject frozen = new FrozenObject(bounds, object.getId());
            frozen.setVelX(object.getVelX());
            frozen.setVelY(object.getVelY());
            copy.addObject(frozen);
        }
        return copy;
    }

    private static final class FrozenObject extends GameObject {
        FrozenObject(Rectangle bounds, ID id) {
            super(bounds.x, bounds.y, bounds.width, bounds.height, id);
        }
        @Override public void tick() { }
        @Override public void render(Graphics graphics) { }
        @Override protected Rectangle getBounds() { return new Rectangle(x, y, width, height); }
    }

    record Step(int robot, int direction) { }
    record Result(String message, List<Step> steps, boolean solved) { }

    // Package visibility lets the headless tests replay routes through the real Move class.
    static Result search(Handler handler, int tokenNumber, int stateLimit, long timeLimitMs) {
        if (tokenNumber == 17) {
            return failure("Token 17 has no capture rule in this desktop game. Choose a token from 1 to 16.");
        }
        if (tokenNumber < 1 || tokenNumber > 16) {
            return failure("Choose a token from 1 to 16 before asking for a hint.");
        }
        if (stateLimit < 1 || stateLimit > STATE_LIMIT || timeLimitMs < 1 || timeLimitMs > TIME_LIMIT_MS) {
            throw new IllegalArgumentException("Hint limits must be positive and no larger than the default limits.");
        }
        long deadline = System.nanoTime() + timeLimitMs * 1_000_000L;
        final Board board;
        try {
            board = new Board(handler, tokenNumber);
        } catch (IllegalArgumentException exception) {
            return failure("Hint unavailable: " + exception.getMessage());
        }
        if (board.atGoal(board.initial)) {
            return new Result("R" + (board.targetRobot + 1) + " is already on the target.", List.of(), true);
        }

        long[] states = new long[stateLimit];
        int[] parents = new int[stateLimit];
        byte[] moves = new byte[stateLimit];
        byte[] depths = new byte[stateLimit];
        Set<Long> visited = new HashSet<>();
        states[0] = board.initial;
        parents[0] = -1;
        visited.add(board.initial);
        int size = 1;
        boolean depthLimited = false;
        for (int cursor = 0; cursor < size; cursor++) {
            if (Thread.currentThread().isInterrupted()) return failure("Hint search cancelled.");
            if (System.nanoTime() >= deadline) return limited(size, "time");
            if (depths[cursor] == DEPTH_LIMIT) {
                depthLimited = true;
                continue;
            }
            for (int robot = 0; robot < 4; robot++) {
                for (int direction = 0; direction < 4; direction++) {
                    long next = board.move(states[cursor], robot, direction);
                    if (next == states[cursor] || next < 0 || visited.contains(next)) continue;
                    if (size == stateLimit) return limited(size, "state");
                    states[size] = next;
                    parents[size] = cursor;
                    moves[size] = (byte) (robot * 4 + direction);
                    depths[size] = (byte) (depths[cursor] + 1);
                    visited.add(next);
                    if (board.atGoal(next)) return route(size, parents, moves);
                    size++;
                }
            }
        }
        if (depthLimited) return limited(size, "12-move");
        return failure("No route reaches this target from the current positions under the desktop movement rules.");
    }

    private static Result route(int index, int[] parents, byte[] moves) {
        List<Step> reversed = new ArrayList<>();
        for (int at = index; parents[at] >= 0; at = parents[at]) {
            reversed.add(new Step(moves[at] / 4, moves[at] % 4));
        }
        List<Step> steps = new ArrayList<>();
        for (int at = reversed.size() - 1; at >= 0; at--) steps.add(reversed.get(at));
        StringBuilder text = new StringBuilder("Solution in ").append(steps.size())
                .append(steps.size() == 1 ? " move:\n" : " moves:\n");
        for (int at = 0; at < steps.size(); at++) {
            Step step = steps.get(at);
            text.append(at + 1).append(". R").append(step.robot() + 1)
                    .append(' ').append(DIRECTIONS[step.direction()]).append('\n');
        }
        return new Result(text.toString().stripTrailing(), List.copyOf(steps), true);
    }

    private static Result failure(String message) {
        return new Result(message, List.of(), false);
    }

    private static Result limited(int states, String reason) {
        return failure("No route found before the " + reason + " hint limit (" + states
                + " positions checked). This does not mean the puzzle is impossible. "
                + "Try repositioning a robot and ask again.");
    }

    private static int position(long state, int robot) {
        return (int) ((state >>> (robot * POSITION_BITS)) & POSITION_MASK);
    }

    private static long withPosition(long state, int robot, int position) {
        int shift = robot * POSITION_BITS;
        return (state & ~((long) POSITION_MASK << shift)) | ((long) position << shift);
    }

    /** Copies primitive geometry only; no Robot, Token, or Handler is mutated by a search. */
    private static final class Board {
        final List<int[]> positions = new ArrayList<>();
        final Map<Integer, Integer> positionIds = new HashMap<>();
        final int targetRobot;
        final Rectangle target;
        final long initial;
        final SlideRules.Board geometry;
        final int[] currentX = new int[4];
        final int[] currentY = new int[4];
        long currentState = -1;

        Board(Handler handler, int tokenNumber) {
            if (handler == null) throw new IllegalArgumentException("there is no active board.");
            Rectangle[] robots = new Rectangle[4];
            Rectangle goal = null;
            int cells = 0;
            int barriers = 0;
            ID targetId = ID.valueOf("Token" + tokenNumber);
            // The caller should prevent moves while taking this short snapshot.
            for (GameObject object : handler.object.toArray(new GameObject[0])) {
                Rectangle bounds = new Rectangle(object.getBounds());
                if (object.getId() == ID.Cell) cells++;
                if (object.getId() == ID.Barrier) barriers++;
                // ChangeToken adds a 36-pixel central display with the same ID as the goal.
                if (object.getId() == targetId && bounds.width == 28 && bounds.height == 28) {
                    if (goal != null) throw new IllegalArgumentException("the selected target is duplicated.");
                    goal = bounds;
                }
                for (int robot = 0; robot < 4; robot++) {
                    if (object.getId() != ROBOTS[robot]) continue;
                    if (robots[robot] != null) throw new IllegalArgumentException("robot IDs must be unique.");
                    if (bounds.width != 22 || bounds.height != 22) {
                        throw new IllegalArgumentException("the board must use the standard 22-pixel robots.");
                    }
                    robots[robot] = bounds;
                }
            }
            if (cells != 256 || barriers > 256) {
                throw new IllegalArgumentException("the board geometry is missing or exceeds the supported size.");
            }
            if (goal == null) throw new IllegalArgumentException("the selected token is missing from this board.");
            for (int cell = 0; cell < SlideRules.SIDE * SlideRules.SIDE; cell++) {
                addPosition(SlideRules.x(cell), SlideRules.y(cell));
            }
            long state = 0;
            for (int robot = 0; robot < 4; robot++) {
                if (robots[robot] == null) throw new IllegalArgumentException("all four robots must be on the board.");
                state = withPosition(state, robot, addPosition(robots[robot].x, robots[robot].y));
            }
            targetRobot = (tokenNumber - 1) % 4;
            target = goal;
            initial = state;
            geometry = new SlideRules.Board(handler);
        }

        private int addPosition(int x, int y) {
            if (x < 0 || y < 0 || x >= GameBoard.WIDTH || y >= GameBoard.HEIGHT) {
                throw new IllegalArgumentException("a cell or robot is outside the board.");
            }
            int key = x * GameBoard.HEIGHT + y;
            Integer present = positionIds.get(key);
            if (present != null) return present;
            int id = positions.size();
            positions.add(new int[] {x, y});
            positionIds.put(key, id);
            return id;
        }

        boolean atGoal(long state) {
            int[] point = positions.get(position(state, targetRobot));
            return target.intersects(point[0], point[1], 22, 22);
        }

        long move(long state, int robot, int direction) {
            if (currentState != state) {
                for (int other = 0; other < 4; other++) {
                    int[] point = positions.get(position(state, other));
                    currentX[other] = point[0];
                    currentY[other] = point[1];
                }
                currentState = state;
            }
            int cell = geometry.slide(currentX, currentY, robot, direction);
            return cell < 0 ? state : withPosition(state, robot, cell);
        }
    }
}
