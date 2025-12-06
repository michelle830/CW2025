# Tetris JFX - COMP 2042 Software Maintenance & Evolution Coursework 
## Refactored & Extended JavaFX Tetris Game

**Author:** Chan Michelle  
**Academic Year:** 2025 / 2026

---

## 1. Introduction
  
This project is a refactored and extended version of a JavaFX-based Tetris game provided for the COMP2042 coursework.

The main objectives are to:

- Improve code quality and maintainability through systematic refactoring
- Introduce multiple new gameplay features
- Enhance the graphical user interface and overall user experience
- Implement structured documentation using Javadoc and this README
- Add automated unit testing using JUnit
- Produce a short video demonstration explaining and showcasing the improvements

---

## 2. Features Implemented

### 2.1 Gameplay Enhancements
 - **Ghost Piece System** - shows landing position of the falling brick, helping the player plan placements.
 - **Brick Hold / Swap (C key)**  - Allows the player to store one Tetromino and swap it later during play.
 - **Next Brick Preview** - Next piece is clearly previewed in a 4x4 box and visually centered.
 - **Updated Scoring System** - Points are awarded for **line clears only**.
 - **Smooth Brick Movement & Rotation** - Cleaned up collision and movement logic for more consistent behaviour.

---

### 2.2 UI/UX Improvements
- Fully redesigned HUD layout (score, timer, hold, next)
- Rounded Tetromino blocks with subtle borders
- White border playing frame around the main grid
- Transparent backgrounds and improved spacing for a cleaner look
- In-game menu overlay with:
  - **Pause / Resume**
  - **Restart**
  - **Back to Home**
  - **Exit**
- **Leaderboard pop-up** displayed on Game Over

---

### 2.3 Themes & Visual Customization
- Theme selector with **color themes** and **image backgrounds**
- Central **`ThemeManager`** class:
  - Holds a list of `Theme` objects (name, type, value)
  - Provides `next()` / `previous()` to cycle through themes
  - Applies the currently selected theme to the game and start screen

---

### 2.4 Game Modes
- **Normal mode** - infinite play until Game Over
- **Timed modes**:
  - 1-minute
  - 2-minute
  - 3-minute
- Dynamic countdown timer:
  - Time displayed in the HUD
  - Changes colour when time is low (e.g. turns red at the last 10 seconds)
- Each timed mode saves to a **separate leaderboard file** (e.g. `time_60.txt`)

---

### 2.5 Persistence Features (Leaderboards)
- File-based leaderboard system (`LeaderboardManager`)
- Saves scores into simple `.txt` files under `src/main/resources/`
- Each line stored as:
  `playerName, score`
- Loads and sorts scores in descending order
- Keeps **top 10** scores per mode
- Displayed via `leaderboard.fxml` in a simple pop-up window

---

## 3. Refactoring Summary

### 3.1 Structural Improvements (MVC)

Codebase was reorganised to better follow an **MVC-style structure**:

- **Model**
  - `Board` (interface)
  - `SimpleBoard` (implementation, game rules)
  - `Score`, `ClearRow`. `ViewData`, `MatrixOperations`
  - Brick system: `Brick`, `BrickRotator`, `RandomBrickGenerator`, individual brick classes
  - `Theme`, `ThemeManager`
  
- **Controller / Logic**
  - `GameController` (connects model and GUI, processes events, manages timer)
  - `InputEventListener` (interface for movement and control events)

- **View / UI (JavaFX)**
  - `GuiController` (main in-game controller)
  - `StartController`, `TimeSelectController`,`InstructionController`,`LeaderboardController`
  - UI components: `GameOverPanel`, `NotificationPanel`
  - FXML files: `startScreen.fxml`, `gameLayout.fxml`, `timeSelect.fxml`, `instructions.fxml`, `leaderboard.fxml`
  
**Key structural changes:**

- Reduced duplicated logic in movement and rotation
- Removed "magic numbers" where possible and replaced with named constants
- Centralised collision detection in `canPlace(...)` in `SimpleBoard`
- Introduced small helper / data classes:
  - `MoveEvent`
  - `NextShapeInfo`
  - `ViewData`
- Separated concern of:
  - game rules (`Simpleboard`)
  - timing and mode (`GameController`)
  - drawing and input (`GuiController`)

---

### 3.2 Readability Enhancements
- Clear method responsibilities and more descriptive naming
- Many long or complex methods broken into smaller helpers
- **Full Javadoc coverage** on the main classes:
  - Explanation of roles and responsibilities
  - Documentation for key methods and parameters
  - Notes about immutability and defensive copying (e.g. in `ViewData`, `MatrixOperations`)

---

### 3.3 Performance & Stability Fixes
- Fixed brick spawn alignment for pieces at the top of the board
- Fixed ghost piece Y-offset to match actual landing position
- Fixed hold-brick reset behaviour when starting a new game
- Ensured pause/resume properly controls the `Timeline`
- Game-over logic now runs only once per game
- Scoring exploits removed:
  - No score gained from repeated soft drops
  - Line clear score calculated consistently via `ClearRow` and `Score`
  
---

## 4. How to Run the Project


### 4.1 Requirements
- Java **17** or **Java 21**
- **Maven**
- JavaFX SDK - handled automatically via the Maven JavaFX plugin in `pom.xml`

### 4.2 Run via Maven
```bash
mvn clean javafx:run
```

### Run in IntelliJ
1. Open project
2. Let Maven load dependencies
3. Run `Main.java`
4. VM options automatically handled by JavaFX Maven Plugin

---

## 5. Javadoc Documentation

Generate full documentation with:

```bash
mvn javadoc:javadoc
```

Output folder:
```
target/site/apidocs/index.html
```

Javadoc includes:
- Class descriptions
- Method parameter and return 
- MVC relationships
- Behaviour descriptions

---

## 6. JUnit Testing

Automated tests were written for all non-JavaFX logic.

### Test Coverage Table
| Component    | Tests Included                         |
|--------------|----------------------------------------|
| SimpleBoard  | movement, collision, line clear, ghost |
| MatrixOperations | merge, copy, row clear                 |
| Brick classes | rotation accuracy                      |
| Score        | add + reset                            |
| LeaderboardManager | save/load/sorting                      |
| ThemeManager | theme switching                        |
| GameController | non-GUI logic                          |

### **Run Tests**
```bash
mvn test
```

---

## 7. UML Class Diagram

A complete UML class diagram is included:

```
tetris-class-diagram.png
```

Diagram covers:
- MVC layout
- Game logic flow
- Brick hierarchy
- All class relationships

---

## 8. Git Usage Summary

- Frequent commits
- Clear commit messages
- Proper branching workflow
- Github used as remote version control

---

## 9. Video Demonstration

Submitted video:

```
COMP2042-Video-Demo-Michelle.mp4
```
Includes:
- Explanation of refactoring 
- Feature demonstrations
- Before & After comparison
- JUnit + Javadoc demonstration
- Gameplay showcase

---

## 10. Conclusion

This coursework demonstrates:

- Understanding of refactoring principles
- Ability to improve maintainability and readability
- MVC architecture awareness
- Implementation of new game features
- Writing automated tests
- Use of documentation and version control

The final Tetris game is more modular, stable, readable, and enjoyable than the original version.

---

## 11. Acknowledgements

- JavaFX Official Documentation
- COMP2042 Lecture Notes

---