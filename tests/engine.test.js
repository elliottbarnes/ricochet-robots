import test from "node:test";
import assert from "node:assert/strict";
import { execFileSync } from "node:child_process";
import {
  BOARD,
  PUZZLES,
  slide,
  wallBetween,
  won,
  move,
  undo,
  createPuzzle,
} from "../demo/engine.js";
test("browser board is derived from the current Java standard board", () =>
  execFileSync(process.execPath, ["scripts/sync-board.mjs", "--check"]));
test("slides stop at perimeter and cannot cross interior walls", () => {
  assert.deepEqual(
    slide(
      [
        [0, 0],
        [1, 0],
        [2, 0],
        [3, 0],
      ],
      0,
      "left",
    )[0],
    [0, 0],
  );
  assert.equal(wallBetween(1, 0, 2, 0), true);
  assert.deepEqual(
    slide(
      [
        [1, 0],
        [5, 5],
        [6, 6],
        [9, 9],
      ],
      0,
      "right",
    )[0],
    [1, 0],
  );
});
test("another robot is a stopper, never overwritten", () => {
  const robots = [
    [0, 4],
    [5, 4],
    [8, 8],
    [14, 14],
  ];
  const result = slide(robots, 0, "right");
  assert.deepEqual(result[0], [4, 4]);
  assert.deepEqual(result[1], [5, 4]);
  assert.deepEqual(robots[0], [0, 4]);
});
test("center block is impassable from every edge", () => {
  for (const [x, y, nx, ny] of [
    [6, 7, 7, 7],
    [9, 7, 8, 7],
    [7, 6, 7, 7],
    [7, 9, 7, 8],
  ])
    assert.equal(wallBetween(x, y, nx, ny), true);
});
test("the matching robot must stop on the target", () => {
  const target = { x: 4, y: 4, robot: 0 };
  assert.equal(
    won(
      [
        [0, 0],
        [4, 4],
        [9, 9],
        [10, 10],
      ],
      target,
    ),
    false,
  );
  assert.equal(
    won(
      slide(
        [
          [0, 4],
          [6, 4],
          [9, 9],
          [10, 10],
        ],
        0,
        "right",
      ),
      target,
    ),
    false,
  );
  assert.equal(
    won(
      [
        [4, 4],
        [6, 4],
        [9, 9],
        [10, 10],
      ],
      target,
    ),
    true,
  );
});
test("blocked moves do not increment move count; undo restores a won board", () => {
  const state = createPuzzle(PUZZLES[0]),
    start = structuredClone(state);
  assert.equal(move(state, 0, "up"), false);
  assert.equal(state.history.length, 0);
  assert.equal(move(state, 0, "down"), true);
  assert.equal(state.solved, true);
  assert.equal(move(state, 0, "left"), false);
  undo(state);
  assert.deepEqual(state, start);
});
test("every published guided route reaches its intended target legally", () => {
  for (const puzzle of PUZZLES) {
    const state = createPuzzle(puzzle);
    for (const [robot, direction] of puzzle.solution)
      assert.equal(move(state, robot, direction), true, puzzle.name);
    assert.equal(state.solved, true, puzzle.name);
    assert.equal(state.history.length, puzzle.solution.length);
  }
});
test("all reachable random slides preserve in-bounds distinct legal robot cells", () => {
  let robots = PUZZLES[1].robots,
    seed = 42;
  for (let i = 0; i < 3000; i++) {
    seed = (Math.imul(seed, 1664525) + 1013904223) >>> 0;
    robots = slide(
      robots,
      seed % 4,
      ["up", "right", "down", "left"][(seed >>> 8) % 4],
    );
    assert.equal(new Set(robots.map((p) => p.join(","))).size, 4);
    for (const [x, y] of robots) {
      assert.ok(x >= 0 && y >= 0 && x < 16 && y < 16);
      assert.ok(!BOARD.blocked.some((p) => p[0] === x && p[1] === y));
    }
  }
});
