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
    Animal enemy1, enemy2, player, door;

    Labyrinth labyrinth;
    ArrayList<Position> positions;
    ArrayList<Integer> movements;
    ArrayList<Integer> bestSolution;

    float playerPositionX, playerPositionY, enemyPositionX, enemyPositionY, exitPositionX, exitPositionY, enemy2PositionX, enemy2PositionY;


    public Engine2(int numEnemies) {

        this.player = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BUNNY), Constants.ANIMAL_BUNNY);
        this.enemy1 = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG1), Constants.ANIMAL_DOG1);

        if (numEnemies == 2) {
            this.enemy2 = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG1), Constants.ANIMAL_DOG1);
        } else {
            this.enemy2 = null;
        }

        this.door = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BURROW), Constants.ANIMAL_BURROW);
    }

    public boolean startSolveGame(Labyrinth labyrinth) {
        this.labyrinth = labyrinth;
        enemy1.id = labyrinth.enemy[0];


        if (labyrinth.enemy[0] == Constants.ANIMAL_FOX) {
            enemy1.numMoves = 3;
        } else {
            enemy1.numMoves = 2;
        }

        if (this.enemy2 != null) {
            enemy2.id = labyrinth.enemy[1];
            if (labyrinth.enemy[1] == Constants.ANIMAL_FOX) {
                enemy2.numMoves = 3;
            } else {
                enemy2.numMoves = 2;
            }
            this.enemy2.labyrintCoords(labyrinth.enemyPosition[1].x + 1, labyrinth.enemyPosition[1].y + 1, true);
            this.enemy2PositionX = labyrinth.enemyPosition[1].x;
            this.enemy2PositionY = labyrinth.enemyPosition[1].y;
        }


        this.player.labyrintCoords(labyrinth.playerPosition.x + 1, labyrinth.playerPosition.y + 1, true);
        this.enemy1.labyrintCoords(labyrinth.enemyPosition[0].x + 1, labyrinth.enemyPosition[0].y + 1, true);

        this.door.labyrintCoords(labyrinth.exitPosition.x + 1, labyrinth.exitPosition.y + 1, true);

        this.playerPositionX = labyrinth.playerPosition.x;
        this.playerPositionY = labyrinth.playerPosition.y;
        this.enemyPositionX = labyrinth.enemyPosition[0].x;
        this.enemyPositionY = labyrinth.enemyPosition[0].y;
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
            labyrinth.enemyPosition[0].x = this.enemyPositionX;
            labyrinth.enemyPosition[0].y = this.enemyPositionY;
            labyrinth.exitPosition.x = this.exitPositionX;
            labyrinth.exitPosition.y = this.exitPositionY;

            if (this.enemy2 != null) {
                labyrinth.enemyPosition[1].x = this.enemy2PositionX;
                labyrinth.enemyPosition[1].y = this.enemy2PositionY;
            }


            this.player.labyrintCoords(labyrinth.playerPosition.x + 1, labyrinth.playerPosition.y + 1, true);
            this.enemy1.labyrintCoords(labyrinth.enemyPosition[0].x + 1, labyrinth.enemyPosition[0].y + 1, true);
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
        positions.add(new Position(x, y, 0, 0, 0, 0, 0));
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

                positions.add(new Position(x, y, 0, 0, 0, 0, 0));
            }
        }
        return false;
    }


    private void solveGame() {
        if ((bestSolution.size() > 0)
                && (movements.size() > bestSolution.size())) {
            return;
        }

        int e2x = -1;
        int e2y = -1;
        if (enemy2 != null) {
            e2x = enemy2.getCoordX();
            e2y = enemy2.getCoordY();
        }

        Position p = new Position(player.getCoordX(), player.getCoordY(), enemy1.getCoordX(), enemy1.getCoordY(), e2x, e2y,
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
                int ex = enemy1.getCoordX();
                int ey = enemy1.getCoordY();
                e2x = -1;
                e2y = -1;

                if (enemy2 != null) {
                    e2x = enemy2.getCoordX();
                    e2y = enemy2.getCoordY();
                }

                movements.add(dir);

                boolean ok = movePlayer(labyrinth, player, dir);

                if (ok) {
                    for (int i = 0; i < enemy1.numMoves; i++) {
                        moveEnemy(labyrinth, player, enemy1);
                    }

                    if (enemy2 != null) {
                        System.out.println("Moving: "+enemy2.numMoves);
                        for (int i = 0; i < enemy2.numMoves; i++) {
                            moveEnemy(labyrinth, player, enemy2);
                        }
                    }

                    if (!checkFinalState()) {
                        solveGame();
                    }
                }

                // Undo move
                player.labyrintCoords(px, py, true);
                enemy1.labyrintCoords(ex, ey, true);
                movements.remove(movements.size() - 1);
                if (enemy2 != null) {
                    enemy2.labyrintCoords(e2x, e2y, true);
                }
            }
        }
        return;
    }

    private boolean checkFinalState() {
        if (player.sameCoordinates(enemy1) || player.sameCoordinates(enemy2)) {
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
        txt.append("Enemy1 ("+enemy1.id+"): " + enemy1.getCoordX() + ", " + enemy1.getCoordY() + "\n");
        if (enemy2 != null) {
            txt.append("Enemy2 (" + enemy2.id + "): " + enemy2.getCoordX() + ", " + enemy2.getCoordY() + "\n");
        }

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
