package view;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Shared, pure cell-by-cell movement for the desktop controls and hint search. */
final class SlideRules {
    static final int SIDE = 16;
    static final int CELL_SIZE = 37;
    static final int ROBOT_SIZE = 22;
    static final int INSET = 7;
    static final ID[] ROBOTS = {ID.Robot, ID.Robot2, ID.Robot3, ID.Robot4};
    private static final int[] DX = {0, 0, -1, 1};
    private static final int[] DY = {-1, 1, 0, 0};

    private SlideRules() { }

    static int robotIndex(ID id) {
        for (int i = 0; i < ROBOTS.length; i++) if (ROBOTS[i] == id) return i;
        return -1;
    }

    static int x(int cell) { return (cell % SIDE) * CELL_SIZE + INSET; }
    static int y(int cell) { return (cell / SIDE) * CELL_SIZE + INSET; }

    static final class Board {
        private final List<Rectangle> walls = new ArrayList<>();
        private final int[][] neighbors = new int[SIDE * SIDE][4];
        private final Rectangle[][] sweeps = new Rectangle[SIDE * SIDE][4];
        private final Rectangle[] cellBounds = new Rectangle[SIDE * SIDE];
        private final boolean[] blocked = new boolean[SIDE * SIDE];

        Board(Handler handler) {
            for (GameObject object : handler.object) {
                if (object.getId() == ID.Barrier) walls.add(new Rectangle(object.getBounds()));
            }
            for (int cell = 0; cell < SIDE * SIDE; cell++) {
                cellBounds[cell] = bounds(x(cell), y(cell));
                blocked[cell] = touchesWall(cellBounds[cell]);
                Arrays.fill(neighbors[cell], -1);
                for (int direction = 0; direction < 4; direction++) {
                    int col = cell % SIDE + DX[direction];
                    int row = cell / SIDE + DY[direction];
                    if (col < 0 || row < 0 || col >= SIDE || row >= SIDE) continue;
                    int next = row * SIDE + col;
                    Rectangle sweep = bounds(x(cell), y(cell)).union(bounds(x(next), y(next)));
                    // Checking only the next cell would skip the thin wall between cells.
                    if (!touchesWall(sweep)) {
                        neighbors[cell][direction] = next;
                        sweeps[cell][direction] = sweep;
                    }
                }
            }
        }

        /** Returns a canonical cell, or -1 if snapping the starting position is unsafe. */
        int snap(int[] x, int[] y, int robot) {
            int col = Math.floorDiv(x[robot] + ROBOT_SIZE / 2, CELL_SIZE);
            int row = Math.floorDiv(y[robot] + ROBOT_SIZE / 2, CELL_SIZE);
            if (col < 0 || row < 0 || col >= SIDE || row >= SIDE) return -1;
            int cell = row * SIDE + col;
            if (x[robot] == x(cell) && y[robot] == y(cell)) {
                return blocked[cell] || touchesRobot(cellBounds[cell], x, y, robot) ? -1 : cell;
            }
            Rectangle sweep = bounds(x[robot], y[robot]).union(bounds(x(cell), y(cell)));
            if (touchesWall(sweep) || touchesRobot(sweep, x, y, robot)) return -1;
            return cell;
        }

        /** Does not mutate the supplied coordinates. Directions: up, down, left, right. */
        int slide(int[] x, int[] y, int robot, int direction) {
            if (robot < 0 || robot >= 4 || direction < 0 || direction >= 4) return -1;
            int cell = snap(x, y, robot);
            if (cell < 0) return -1;
            for (int steps = 0; steps < SIDE - 1; steps++) {
                int next = neighbors[cell][direction];
                if (next < 0 || touchesRobot(sweeps[cell][direction], x, y, robot)) break;
                cell = next;
            }
            return cell;
        }

        private boolean touchesWall(Rectangle bounds) {
            for (Rectangle wall : walls) if (bounds.intersects(wall)) return true;
            return false;
        }

        private static boolean touchesRobot(Rectangle bounds, int[] x, int[] y, int robot) {
            for (int other = 0; other < 4; other++) {
                if (other != robot && bounds.intersects(x[other], y[other], ROBOT_SIZE, ROBOT_SIZE)) return true;
            }
            return false;
        }

        private static Rectangle bounds(int x, int y) {
            return new Rectangle(x, y, ROBOT_SIZE, ROBOT_SIZE);
        }
    }
}
