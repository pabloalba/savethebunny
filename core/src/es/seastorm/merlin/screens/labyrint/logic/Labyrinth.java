package es.seastorm.merlin.screens.labyrint.logic;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

import java.util.HashMap;

import es.seastorm.merlin.Constants;


public class Labyrinth implements java.io.Serializable {
    public int[] enemy;
    private Square[][] squares;
    int width;
    int height;
    public int minMoves;

    public Vector2 playerPosition = new Vector2();
    public Vector2 enemyPosition = new Vector2();
    public Vector2 exitPosition = new Vector2();

    public Square getSquare(int x, int y) {
        return squares[x][y];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    // Define a new Labyrinth with borders
    public Labyrinth(int width, int height) {
        squares = new Square[width][height];
        this.width = width;
        this.height = height;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                squares[i][j] = new Square(j == 0, i == width - 1, i == 0,
                        j == height - 1);
            }
        }
    }

    public String toString() {
        StringBuffer txt = new StringBuffer(" ");
        for (int i = 0; i < width - 1; i++) {
            txt.append("__");
        }
        txt.append("\n");

        for (int j = 0; j < height; j++) {
            txt.append("|");
            for (int i = 0; i < width; i++) {
                txt.append(squares[i][j]);
            }
            txt.append("\n");
        }
        return txt.toString();
    }

    public String toJsonString() {
        //"{squares:[[{limitUp:true,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:true,limitLeft:true,limitRight:false}],[{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false}],[{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:true},{limitUp:false,limitDown:false,limitLeft:true,limitRight:true},{limitUp:false,limitDown:true,limitLeft:true,limitRight:true},{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false}],[{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:true,limitLeft:true,limitRight:false},{limitUp:true,limitDown:true,limitLeft:true,limitRight:false},{limitUp:true,limitDown:true,limitLeft:true,limitRight:false},{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false}],[{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false}],[{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:true,limitLeft:false,limitRight:true},{limitUp:true,limitDown:true,limitLeft:false,limitRight:true},{limitUp:true,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false}],[{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false},{limitUp:true,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:true,limitRight:true},{limitUp:false,limitDown:false,limitLeft:true,limitRight:true},{limitUp:false,limitDown:true,limitLeft:false,limitRight:true},{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false}],[{limitUp:true,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:true,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:false,limitLeft:false,limitRight:false},{limitUp:false,limitDown:true,limitLeft:false,limitRight:false}],[{limitUp:true,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:false,limitLeft:false,limitRight:true},{limitUp:false,limitDown:true,limitLeft:false,limitRight:true}]],",
        StringBuffer txt = new StringBuffer();
        //:9,height:9,playerPosition:{x:2,y:5},enemyPosition:{x:3,y:4},exitPosition:{x:6,y:5}}
        txt.append("{");
        txt.append("enemy:[");
        for (int i = 0; i < enemy.length; i++) {
            txt.append(this.enemy[i]);
            if (i < enemy.length - 1) {
                txt.append(",");
            }
        }

        txt.append("],minMoves:");
        txt.append(this.minMoves);

        txt.append(",squares:[");
        for (int sx = 0; sx < width; sx++) {
            txt.append("[");
            for (int sy = 0; sy < height; sy++) {
                txt.append(getSquare(sx, sy).toJsonString());
                if (sy < height - 1) {
                    txt.append(",");
                }
            }
            txt.append("]");
            if (sx < width - 1) {
                txt.append(",");
            }
        }
        txt.append("]");

        txt.append(",width:");
        txt.append(width);
        txt.append(",height:");
        txt.append(height);
        txt.append(",playerPosition:{x:");
        txt.append((int) playerPosition.x);
        txt.append(",y:");
        txt.append((int) playerPosition.y);
        txt.append("},enemyPosition:{x:");
        txt.append((int) enemyPosition.x);
        txt.append(",y:");
        txt.append((int) enemyPosition.y);
        txt.append("},exitPosition:{x:");
        txt.append((int) exitPosition.x);
        txt.append(",y:");
        txt.append((int) exitPosition.y);
        txt.append("}");

        txt.append("}");
        return txt.toString();
    }


    public boolean canMove(int x, int y, int direction) {
        switch (direction) {
            case Constants.DIRECTION_UP:
                return !squares[x][y].limitUp;
            case Constants.DIRECTION_RIGHT:
                return !squares[x][y].limitRight;
            case Constants.DIRECTION_DOWN:
                return !squares[x][y].limitDown;
            case Constants.DIRECTION_LEFT:
                return !squares[x][y].limitLeft;
            case Constants.DIRECTION_PASS:
                return true;
            default:
                return false;
        }
    }

    public void addWall(int x, int y, int direction) {
        switch (direction) {
            case Constants.DIRECTION_UP:
                squares[x][y].setLimitUp(true);
                if (y > 0) {
                    squares[x][y - 1].setLimitDown(true);
                }
                break;
            case Constants.DIRECTION_RIGHT:
                squares[x][y].setLimitRight(true);
                if (x < width - 1) {
                    squares[x + 1][y].setLimitLeft(true);
                }
                break;
            case Constants.DIRECTION_DOWN:
                squares[x][y].setLimitDown(true);
                if (y < height - 1) {
                    squares[x][y + 1].setLimitUp(true);
                }
                break;
            case Constants.DIRECTION_LEFT:
                squares[x][y].setLimitLeft(true);
                if (x > 0) {
                    squares[x - 1][y].setLimitRight(true);
                }
                break;
        }

    }

    public void toggleWall(int x, int y, int direction) {
        switch (direction) {
            case Constants.DIRECTION_UP:
                squares[x][y].toggleLimitUp();
                if (y > 0) {
                    squares[x][y - 1].toggleLimitDown();
                }
                break;
            case Constants.DIRECTION_RIGHT:
                squares[x][y].toggleLimitRight();
                if (x < width - 1) {
                    squares[x + 1][y].toggleLimitLeft();
                }
                break;
            case Constants.DIRECTION_DOWN:
                squares[x][y].toggleLimitDown();
                if (y < height - 1) {
                    squares[x][y + 1].toggleLimitUp();
                }
                break;
            case Constants.DIRECTION_LEFT:
                squares[x][y].toggleLimitLeft();
                if (x > 0) {
                    squares[x - 1][y].toggleLimitRight();
                }
                break;
        }

    }

}
