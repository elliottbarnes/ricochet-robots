# Ricochet Robots

A Java desktop implementation developed as group coursework, now accompanied by a playable browser puzzle example and a repeatable desktop build.

**[Play the browser puzzles](https://elliottbarnes.github.io/ricochet-robots/)** · [Browser rules](demo/engine.js) · [Java movement and goals](Ricochet%20Robots/src/view/Move.java)

## Browser example

Select a robot and slide it with the arrow buttons or keyboard. A robot continues until blocked by a wall or another robot. Only the matching color can claim the target, and it must stop there. Four practice puzzles include undo, reset, move counts, and guided hints.

```sh
python3 -m http.server 8000 --bind 127.0.0.1 --directory demo
```

Open `http://127.0.0.1:8000`. The browser example uses the standard 16×16 Java board, with the original pixel barriers snapped to its 37-pixel cell grid. `scripts/sync-board.mjs` derives the wall and token data from `GameBoard.java`; a test prevents the checked-in data from silently drifting. Practice starting positions and guided routes are browser additions. The first puzzle teaches one slide; the others require moving helper robots.

This is a browser example of the core puzzle rules, not the Java application running through a browser. Multiplayer bidding, save files, the complex board, and the desktop settings UI remain in the original. No native images or historical documents are copied into the public demo artifact. All browser visuals are drawn with Canvas and CSS.

## Java desktop original

With a Java 21 JDK and Python 3:

```sh
python3 scripts/build-java.py --test
java -jar build/ricochet-robots.jar
```

The script compiles the preserved source, packages its existing image resources, sets `controller.Driver` as the main class, and runs headless tests against the actual token-color and collision classes. Java 21 compilation and these headless checks have been verified. A complete desktop GUI/game-session acceptance pass has not been performed; historical UI limitations, including incomplete hint/load behavior, may remain. Only load saved games you created locally; the historical save format uses Java serialization.

- `Ricochet Robots/src/controller/`: entry point, bidding, settings, timing, and saved-game support.
- `Ricochet Robots/src/view/`: board, menus, robots, tokens, and movement.
- `Group-3-Iteration-*`: preserved historical course material and packaged iterations.

## Verify the browser example

With Node.js 24:

```sh
node --test
node scripts/build-demo.mjs
```

Tests cover the source-derived board, perimeter and wall stops, robot blocking, the center obstruction, matching-color goals, undo after success, all guided routes, and 3,000 reproducible movement steps preserving legal positions. The Java checks cover all 16 tokens across four robot colors plus wall/robot collisions.

The Pages workflow runs browser tests and the Java build/checks before publishing only the allowlisted `demo/` files. `build.json` identifies the source commit and file hashes. No external libraries, network services, accounts, telemetry, or persistent storage are used by the browser example. Keyboard controls and textual positions support interaction, but understanding the board still benefits from seeing its spatial layout.

## Attribution

Originally named `comp2005-winter20-group3`. This is group coursework; inclusion in this profile does not claim sole authorship. Existing contributor notices and project documents retain their attribution, including the completed PDF metadata cleanup. This is an unofficial educational implementation, not an official Ricochet Robots product. No license has been added or inferred.
