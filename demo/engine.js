import { BOARD } from "./board.js";
export { BOARD };
export const ROBOTS = [
  { name: "Blue", label: "1", color: "#74aaf5" },
  { name: "Green", label: "2", color: "#72d5a1" },
  { name: "Red", label: "3", color: "#f18f92" },
  { name: "Amber", label: "4", color: "#edc66d" },
];
export const DIRECTIONS = {
  up: [0, -1],
  right: [1, 0],
  down: [0, 1],
  left: [-1, 0],
};
const walls = new Set(BOARD.walls.map((w) => `${w.axis}:${w.x}:${w.y}`));
const blocked = new Set(BOARD.blocked.map((p) => p.join(",")));
export function wallBetween(x, y, nx, ny) {
  if (
    nx < 0 ||
    ny < 0 ||
    nx >= BOARD.size ||
    ny >= BOARD.size ||
    blocked.has(`${nx},${ny}`)
  )
    return true;
  return walls.has(
    nx !== x ? `v:${Math.max(x, nx)}:${y}` : `h:${x}:${Math.max(y, ny)}`,
  );
}
export function slide(robots, index, direction) {
  if (!DIRECTIONS[direction] || !Number.isInteger(index) || !robots[index])
    throw new RangeError("Invalid move");
  let [x, y] = robots[index];
  const [dx, dy] = DIRECTIONS[direction];
  for (let i = 0; i < BOARD.size; i++) {
    const nx = x + dx,
      ny = y + dy;
    if (
      wallBetween(x, y, nx, ny) ||
      robots.some((p, j) => j !== index && p[0] === nx && p[1] === ny)
    )
      break;
    x = nx;
    y = ny;
  }
  return robots.map((p, j) => (j === index ? [x, y] : [...p]));
}
export function won(robots, target) {
  return (
    robots[target.robot][0] === target.x && robots[target.robot][1] === target.y
  );
}
export function createPuzzle(puzzle) {
  return {
    robots: puzzle.robots.map((p) => [...p]),
    target: BOARD.tokens.find((t) => t.number === puzzle.token),
    history: [],
    solved: false,
  };
}
export function move(state, index, direction) {
  if (state.solved) return false;
  const next = slide(state.robots, index, direction);
  if (
    next[index][0] === state.robots[index][0] &&
    next[index][1] === state.robots[index][1]
  )
    return false;
  state.history.push({
    robots: state.robots.map((p) => [...p]),
    index,
    direction,
  });
  state.robots = next;
  state.solved = won(next, state.target);
  return true;
}
export function undo(state) {
  const previous = state.history.pop();
  if (!previous) return false;
  state.robots = previous.robots;
  state.solved = false;
  return true;
}
export const PUZZLES = [
  {
    name: "Find the stopping point",
    token: 1,
    robots: [
      [9, 0],
      [1, 0],
      [2, 0],
      [3, 0],
    ],
    solution: [[0, "down"]],
  },
  {
    name: "Around the corner",
    token: 2,
    robots: [
      [0, 0],
      [1, 0],
      [2, 0],
      [3, 0],
    ],
    solution: [
      [1, "down"],
      [1, "left"],
      [1, "down"],
      [1, "right"],
      [0, "down"],
      [1, "left"],
      [1, "up"],
    ],
  },
  {
    name: "A little help",
    token: 3,
    robots: [
      [0, 0],
      [1, 0],
      [2, 0],
      [3, 0],
    ],
    solution: [
      [3, "down"],
      [2, "right"],
      [3, "up"],
      [2, "left"],
      [2, "down"],
      [2, "right"],
    ],
  },
  {
    name: "Across the board",
    token: 4,
    robots: [
      [0, 0],
      [1, 0],
      [2, 0],
      [3, 0],
    ],
    solution: [
      [2, "down"],
      [3, "down"],
      [3, "right"],
      [3, "down"],
      [3, "left"],
      [3, "up"],
    ],
  },
];
