package es.seastorm.merlin.screens.labyrint.logic;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import es.seastorm.merlin.Constants;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.gameobjects.Animal;

public class Engine2 {
    Animal enemy, player, door;
    Animal enemyOriginal, playerOriginal, doorOriginal;

    Labyrinth labyrinth;
    ArrayList<Position> positions;
    ArrayList<Integer> movements;
    ArrayList<Integer> bestSolution;

    float playerPositionX, playerPositionY, enemyPositionX, enemyPositionY, exitPositionX, exitPositionY;


    public Engine2() {

        this.player = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BUNNY), Constants.ANIMAL_BUNNY);
        this.enemy = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG1), Constants.ANIMAL_DOG1);
        this.door = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BURROW), Constants.ANIMAL_BURROW);

        this.playerOriginal = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BUNNY), Constants.ANIMAL_BUNNY);
        this.enemyOriginal = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG1), Constants.ANIMAL_DOG1);
        this.doorOriginal = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BURROW), Constants.ANIMAL_BURROW);


    }

    public boolean startSolveGame(Labyrinth labyrinth) {
        if (labyrinth.enemy[0] == Constants.ANIMAL_FOX) {
            enemy.numMoves = 3;
        } else {
            enemy.numMoves = 2;
        }
        this.labyrinth = labyrinth;

        this.player.labyrintCoords(labyrinth.playerPosition.x + 1, labyrinth.playerPosition.y + 1, true);
        this.enemy.labyrintCoords(labyrinth.enemyPosition.x + 1, labyrinth.enemyPosition.y + 1, true);
        this.door.labyrintCoords(labyrinth.exitPosition.x + 1, labyrinth.exitPosition.y + 1, true);

        this.playerPositionX = labyrinth.playerPosition.x;
        this.playerPositionY = labyrinth.playerPosition.y;
        this.enemyPositionX = labyrinth.enemyPosition.x;
        this.enemyPositionY = labyrinth.enemyPosition.y;
        this.exitPositionX = labyrinth.exitPosition.x;
        this.exitPositionY = labyrinth.exitPosition.y;


        positions = new ArrayList<Position>();
        movements = new ArrayList<Integer>();
        bestSolution = new ArrayList<Integer>();

        solveGame();
        int minMoves = 10 + labyrinth.getHeight();
        if ((bestSolution.size()) > minMoves && (difficult())) {
            labyrinth.playerPosition.x = this.playerPositionX;
            labyrinth.playerPosition.y = this.playerPositionY;
            labyrinth.enemyPosition.x = this.enemyPositionX;
            labyrinth.enemyPosition.y = this.enemyPositionY;
            labyrinth.exitPosition.x = this.exitPositionX;
            labyrinth.exitPosition.y = this.exitPositionY;

            this.player.labyrintCoords(labyrinth.playerPosition.x + 1, labyrinth.playerPosition.y + 1, true);
            this.enemy.labyrintCoords(labyrinth.enemyPosition.x + 1, labyrinth.enemyPosition.y + 1, true);
            this.door.labyrintCoords(labyrinth.exitPosition.x + 1, labyrinth.exitPosition.y + 1, true);
            labyrinth.minMoves = bestSolution.size();
            System.out.println(toString());
            return true;
        }
        return false;
    }

    private boolean difficult() {
        int x = (int) this.playerPositionX;
        int y = (int) this.playerPositionY;
        positions = new ArrayList<Position>();
        positions.add(new Position(x, y, 0, 0, 0));
        for (int i = 0; i < bestSolution.size(); i++) {
            if (bestSolution.get(i) != Constants.DIRECTION_PASS) {
                if (bestSolution.get(i) == Constants.DIRECTION_UP) {
                    y = y - 1;
                } else if (bestSolution.get(i) == Constants.DIRECTION_DOWN) {
                    y = y + 1;
                } else if (bestSolution.get(i) == Constants.DIRECTION_LEFT) {
                    x = x - 1;
                } else if (bestSolution.get(i) == Constants.DIRECTION_RIGHT) {
                    x = x + 1;
                }

                for (int j = 0; j < positions.size(); j++) {
                    if (positions.get(j).x1 == x && positions.get(j).y1 == y) {
                        return true;
                    }
                }

                positions.add(new Position(x, y, 0, 0, 0));
            }
        }
        return false;
    }


    private void solveGame() {
        if ((bestSolution.size() > 0)
                && (movements.size() > bestSolution.size())) {
            return;
        }

        Position p = new Position(player.getCoordX(), player.getCoordY(), enemy.getCoordX(), enemy.getCoordY(),
                movements.size());
        Position old = positionVisited(p);
        if (old != null) {
            // If we have reach that position before with less movements, return
            if (movements.size() >= old.num) {
                return;
            } else {
                old.num = movements.size();
            }
        } else {
            positions.add(p);
        }

        if (checkFinalState()) {
            return;
        } else {
            for (int dir = 0; dir < 5; dir++) {
                int px = player.getCoordX();
                int py = player.getCoordY();
                int ex = enemy.getCoordX();
                int ey = enemy.getCoordY();

                movements.add(dir);

                boolean ok = movePlayer(labyrinth, player, dir);

                if (ok) {
                    for (int i = 0; i < enemy.numMoves; i++) {
                        moveEnemy(labyrinth, player, enemy);
                    }
                    if (!checkFinalState()) {
                        solveGame();
                    }
                }

                // Undo move
                player.labyrintCoords(px, py, true);
                enemy.labyrintCoords(ex, ey, true);
                movements.remove(movements.size() - 1);
            }
        }
        return;
    }

    private boolean checkFinalState() {
        if (player.sameCoordinates(enemy)) {
            return true;
        } else if (player.sameCoordinates(door)) {
            if ((bestSolution.size() == 0)
                    || (movements.size() < bestSolution.size())) {
                bestSolution.clear();
                for (int i = 0; i < movements.size(); i++) {
                    bestSolution.add(movements.get(i));
                }
            }
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuffer txt = new StringBuffer(" ");
        for (int i = 0; i < labyrinth.getWidth() - 1; i++) {
            txt.append(i + " ");
        }
        txt.append("\n  ");
        for (int i = 0; i < labyrinth.getWidth() - 1; i++) {
            txt.append("__");
        }
        txt.append("\n");

        for (int j = 0; j < labyrinth.getHeight(); j++) {
            txt.append(j + "|");
            for (int i = 0; i < labyrinth.getWidth(); i++) {
                txt.append(labyrinth.getSquare(i, j));
            }
            txt.append("\n");
        }
        txt.append("\n");
        txt.append("Player: " + player.getCoordX() + ", " + player.getCoordY() + "\n");
        txt.append("Enemy: " + enemy.getCoordX() + ", " + enemy.getCoordY() + "\n");
        txt.append("Exit: " + door.getCoordX() + ", " + door.getCoordY() + "\n");
        txt.append("\n\n");

        txt.append("Solution: ");
        txt.append(this.bestSolution);
        txt.append("\n");
        for (int j = 0; j < this.bestSolution.size(); j++) {
            txt.append(movementToText(this.bestSolution.get(j)));
            txt.append(" ");
        }
        txt.append("\n\n");

        return txt.toString();
    }


    String movementToText(int movement) {
        String[] texts = {"UP", "RIGHT", "DOWN", "LEFT", "PASS"};
        return texts[movement];
    }

    public static void moveEnemy(Labyrinth labyrinth, Animal player, Animal enemy) {
        if ((enemy.id == Constants.ANIMAL_DOG1) || (enemy.id == Constants.ANIMAL_FOX)) {
            if ((player.getCoordX() > enemy.getCoordX())
                    && (labyrinth.canMove(enemy.getCoordX(), enemy.getCoordY(), Constants.DIRECTION_RIGHT))) {
                enemy.labyrintCoords(enemy.getCoordX() + 1, enemy.getCoordY(), false);
            } else if ((player.getCoordX() < enemy.getCoordX())
                    && (labyrinth.canMove(enemy.getCoordX(), enemy.getCoordY(), Constants.DIRECTION_LEFT))) {
                enemy.labyrintCoords(enemy.getCoordX() - 1, enemy.getCoordY(), false);
            } else if ((player.getCoordY() < enemy.getCoordY())
                    && (labyrinth.canMove(enemy.getCoordX(), enemy.getCoordY(), Constants.DIRECTION_UP))) {
                enemy.labyrintCoords(enemy.getCoordX(), enemy.getCoordY() - 1, false);
            } else if ((player.getCoordY() > enemy.getCoordY())
                    && (labyrinth.canMove(enemy.getCoordX(), enemy.getCoordY(), Constants.DIRECTION_DOWN))) {
                enemy.labyrintCoords(enemy.getCoordX(), enemy.getCoordY() + 1, false);
            }
        } else if (enemy.id == Constants.ANIMAL_DOG2) {
            if ((player.getCoordY() < enemy.getCoordY())
                    && (labyrinth.canMove(enemy.getCoordX(), enemy.getCoordY(), Constants.DIRECTION_UP))) {
                enemy.labyrintCoords(enemy.getCoordX(), enemy.getCoordY() - 1, false);
            } else if ((player.getCoordY() > enemy.getCoordY())
                    && (labyrinth.canMove(enemy.getCoordX(), enemy.getCoordY(), Constants.DIRECTION_DOWN))) {
                enemy.labyrintCoords(enemy.getCoordX(), enemy.getCoordY() + 1, false);
            } else if ((player.getCoordX() > enemy.getCoordX())
                    && (labyrinth.canMove(enemy.getCoordX(), enemy.getCoordY(), Constants.DIRECTION_RIGHT))) {
                enemy.labyrintCoords(enemy.getCoordX() + 1, enemy.getCoordY(), false);
            } else if ((player.getCoordX() < enemy.getCoordX())
                    && (labyrinth.canMove(enemy.getCoordX(), enemy.getCoordY(), Constants.DIRECTION_LEFT))) {
                enemy.labyrintCoords(enemy.getCoordX() - 1, enemy.getCoordY(), false);
            }
        }
    }


    public static boolean movePlayer(Labyrinth labyrinth, Animal bunny, int dir) {
        if (labyrinth.canMove(bunny.getCoordX(), bunny.getCoordY(), dir)) {
            switch (dir) {
                case Constants.DIRECTION_UP:
                    bunny.labyrintCoords(bunny.getCoordX(), bunny.getCoordY() - 1, false);
                    break;
                case Constants.DIRECTION_RIGHT:
                    bunny.labyrintCoords(bunny.getCoordX() + 1, bunny.getCoordY(), false);
                    break;
                case Constants.DIRECTION_DOWN:
                    bunny.labyrintCoords(bunny.getCoordX(), bunny.getCoordY() + 1, false);
                    break;
                case Constants.DIRECTION_LEFT:
                    bunny.labyrintCoords(bunny.getCoordX() - 1, bunny.getCoordY(), false);
                    break;
                case Constants.DIRECTION_PASS:
                    break;
            }
            return true;
        }
        return false;
    }

    private Position positionVisited(Position p) {
        for (int i = 0; i < positions.size(); i++) {
            if (positions.get(i).equals(p)) {
                return positions.get(i);
            }
        }
        return null;
    }
}
