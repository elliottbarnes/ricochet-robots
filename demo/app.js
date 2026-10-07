import { BOARD, ROBOTS, PUZZLES, createPuzzle, move, undo } from "./engine.js";
const $ = (id) => document.getElementById(id),
  canvas = $("board"),
  ctx = canvas.getContext("2d"),
  cell = 40;
let puzzleIndex = 0,
  state = createPuzzle(PUZZLES[0]),
  selected = 0;
const buttons = [...document.querySelectorAll("[data-robot]")];
function select(index) {
  selected = index;
  render();
}
function describe() {
  return (
    ROBOTS.map(
      (r, i) =>
        `${r.name}: column ${state.robots[i][0] + 1}, row ${state.robots[i][1] + 1}`,
    ).join(". ") +
    `. Target: column ${state.target.x + 1}, row ${state.target.y + 1}.`
  );
}
function render() {
  $("moves").textContent = state.history.length;
  $("number").textContent = `${puzzleIndex + 1} / ${PUZZLES.length}`;
  $("puzzle-name").textContent = PUZZLES[puzzleIndex].name;
  $("goal-label").textContent = state.solved
    ? "TARGET REACHED"
    : `${ROBOTS[state.target.robot].name.toUpperCase()} TARGET`;
  $("undo").disabled = !state.history.length;
  $("positions").textContent = describe();
  buttons.forEach((button, index) =>
    button.setAttribute("aria-pressed", String(index === selected)),
  );
  document
    .querySelectorAll("[data-direction]")
    .forEach((button) => (button.disabled = state.solved));
  ctx.fillStyle = "#10151e";
  ctx.fillRect(0, 0, 640, 640);
  ctx.strokeStyle = "#293444";
  ctx.lineWidth = 1;
  for (let i = 0; i <= 16; i++) {
    ctx.beginPath();
    ctx.moveTo(i * cell, 0);
    ctx.lineTo(i * cell, 640);
    ctx.moveTo(0, i * cell);
    ctx.lineTo(640, i * cell);
    ctx.stroke();
  }
  for (const [x, y] of BOARD.blocked) {
    ctx.fillStyle = "#313a48";
    ctx.fillRect(x * cell, y * cell, cell, cell);
  }
  ctx.strokeStyle = "#b7c2d0";
  ctx.lineWidth = 5;
  ctx.strokeRect(2.5, 2.5, 635, 635);
  for (const wall of BOARD.walls) {
    ctx.beginPath();
    ctx.moveTo(wall.x * cell, wall.y * cell);
    ctx.lineTo(
      (wall.x + (wall.axis === "h" ? 1 : 0)) * cell,
      (wall.y + (wall.axis === "v" ? 1 : 0)) * cell,
    );
    ctx.stroke();
  }
  const tx = (state.target.x + 0.5) * cell,
    ty = (state.target.y + 0.5) * cell;
  ctx.strokeStyle = ROBOTS[state.target.robot].color;
  ctx.lineWidth = 3;
  ctx.beginPath();
  ctx.moveTo(tx, ty - 15);
  ctx.lineTo(tx + 15, ty);
  ctx.lineTo(tx, ty + 15);
  ctx.lineTo(tx - 15, ty);
  ctx.closePath();
  ctx.stroke();
  state.robots.forEach(([x, y], index) => {
    const px = (x + 0.5) * cell,
      py = (y + 0.5) * cell;
    if (index === selected) {
      ctx.strokeStyle = "#fff";
      ctx.lineWidth = 2;
      ctx.beginPath();
      ctx.arc(px, py, 18, 0, Math.PI * 2);
      ctx.stroke();
    }
    ctx.fillStyle = ROBOTS[index].color;
    ctx.beginPath();
    ctx.arc(px, py, 13, 0, Math.PI * 2);
    ctx.fill();
    ctx.fillStyle = "#10151e";
    ctx.font = "bold 14px system-ui";
    ctx.textAlign = "center";
    ctx.textBaseline = "middle";
    ctx.fillText(ROBOTS[index].label, px, py);
  });
}
function reset() {
  state = createPuzzle(PUZZLES[puzzleIndex]);
  selected = state.target.robot;
  $("status").textContent = "Select a robot and choose a direction.";
  $("hint-text").textContent = "Follow a guided route when you need a hand.";
  render();
}
function step(direction) {
  const moved = move(state, selected, direction);
  $("hint-text").textContent = state.solved
    ? "Solved. Choose another puzzle."
    : "Request a hint for the current position.";
  $("status").textContent = state.solved
    ? `Target reached in ${state.history.length} ${state.history.length === 1 ? "move" : "moves"}. Choose another puzzle to keep playing.`
    : moved
      ? `${ROBOTS[selected].name} slid ${direction}. ${state.history.length} moves so far.`
      : `${ROBOTS[selected].name} is blocked ${direction}. Try another direction or robot.`;
  render();
}
buttons.forEach((button) =>
  button.addEventListener("click", () => select(Number(button.dataset.robot))),
);
for (const button of document.querySelectorAll("[data-direction]"))
  button.addEventListener("click", () => step(button.dataset.direction));
$("undo").addEventListener("click", () => {
  undo(state);
  $("status").textContent = "Move undone.";
  $("hint-text").textContent = "Request a hint for the current position.";
  render();
});
$("reset").addEventListener("click", reset);
$("puzzle").addEventListener("change", () => {
  puzzleIndex = Number($("puzzle").value);
  reset();
});
$("hint").addEventListener("click", () => {
  const route = PUZZLES[puzzleIndex].solution;
  if (state.solved) {
    $("hint-text").textContent = "Solved. Try the next puzzle.";
    return;
  }
  const onRoute = state.history.every(
    (h, i) => route[i]?.[0] === h.index && route[i]?.[1] === h.direction,
  );
  if (!onRoute) {
    $("hint-text").textContent =
      "Your route has diverged. Undo or reset to follow the guided example.";
    return;
  }
  const next = route[state.history.length];
  $("hint-text").textContent =
    `Next on the ${route.length}-move example route: ${ROBOTS[next[0]].name} → ${next[1]}.`;
});
canvas.addEventListener("pointerdown", (event) => {
  const r = canvas.getBoundingClientRect(),
    x = Math.floor(((event.clientX - r.left) * 16) / r.width),
    y = Math.floor(((event.clientY - r.top) * 16) / r.height),
    index = state.robots.findIndex((p) => p[0] === x && p[1] === y);
  if (index >= 0) select(index);
  canvas.focus();
});
canvas.addEventListener("keydown", (event) => {
  const directions = {
    ArrowUp: "up",
    ArrowRight: "right",
    ArrowDown: "down",
    ArrowLeft: "left",
  };
  if (directions[event.key]) {
    event.preventDefault();
    step(directions[event.key]);
  } else if (/^[1-4]$/.test(event.key)) {
    event.preventDefault();
    select(Number(event.key) - 1);
  } else if (event.key.toLowerCase() === "z") {
    event.preventDefault();
    undo(state);
    $("status").textContent = "Move undone.";
    $("hint-text").textContent = "Request a hint for the current position.";
    render();
  }
});
render();
