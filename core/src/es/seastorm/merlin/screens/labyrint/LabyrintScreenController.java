package es.seastorm.merlin.screens.labyrint;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Random;

import dragongames.base.controller.Controller;
import dragongames.base.gameobject.AbstractGameObject;
import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.Constants;
import es.seastorm.merlin.MerlinGame;
import es.seastorm.merlin.Utils;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.cache.Cache;
import es.seastorm.merlin.gameobjects.Animal;
import es.seastorm.merlin.gameobjects.Box;
import es.seastorm.merlin.screens.labyrint.help.HelpItem;
import es.seastorm.merlin.screens.labyrint.logic.Engine2;
import es.seastorm.merlin.screens.labyrint.logic.Labyrinth;
import es.seastorm.merlin.screens.labyrint.logic.Square;


public class LabyrintScreenController extends Controller {
    static boolean isEditing = false;
    static boolean generateRandom = false;

    LabyrintScreen screen;
    private static final int NUM_WALLS = 60;
    private static final int NUM_HELP = 5;

    private static int MODE_PLAYER = 0;
    private static int MODE_BUNNY = 1;
    private static int MODE_DOG1 = 2;
    private static int MODE_DOG2 = 4;
    private static int MODE_FOX = 6;
    private static int MODE_ENDING = 8;
    private static int MODE_CHECK_END = 9;
    private static int MODE_EDIT = 10;
    private static int MODE_HELP = 11;

    private long lastTouch = 0;
    private Vector2 lastPointTouch = new Vector2();


    Animal bunny, dog1, dog2, fox, burrow, currentEnemy, currentEnemy2;
    Array<Animal> enemiesList;
    int enemyMoves = 0;
    int mode = MODE_EDIT;
    int currentSection;
    int currentLevel;
    int numMoves;


    Array<SimpleGameObject> wallsH;
    Array<SimpleGameObject> wallsV;
    Array<HelpItem> helpItems;


    Labyrinth labyrinth;

    SimpleGameObject editFloor1, editFloor2, editFloor3, editFloor4, editMerlin, editBurrow, editDog, currentEdit, editEdit, editPlay, editSave;
    Array<SimpleGameObject> editButtons;
    SimpleGameObject levelComplete, levelLose, starBig, starSmall1, starSmall2, btnHome, btnLevels, btnReload, btnNext;

    Box currentBox;

    Random random;


    public LabyrintScreenController(Game game, LabyrintScreen screen) {
        super(game, Constants.WIDTH, Constants.HEIGHT);
        Gdx.input.setCatchBackKey(true);
        this.screen = screen;
        random = new Random();
    }

    public void update(float deltaTime) {
        if (mode == MODE_BUNNY) {
            Utils.moveAnimal(bunny, deltaTime);
            if (bunny.position.x == bunny.desiredPosition.x && bunny.position.y == bunny.desiredPosition.y) {
                enemyMoves = 0;
                thinkFox();
            }
        } else if (mode == MODE_DOG1) {
            Utils.moveAnimal(dog1, deltaTime);
            if (dog1.position.x == dog1.desiredPosition.x && dog1.position.y == dog1.desiredPosition.y) {
                enemyMoves++;
                if (enemyMoves < 2) {
                    thinkDog1();
                } else {
                    enemyMoves = 0;
                    thinkDog2();
                }
            }
        } else if (mode == MODE_DOG2) {
            Utils.moveAnimal(dog2, deltaTime);
            if (dog2.position.x == dog2.desiredPosition.x && dog2.position.y == dog2.desiredPosition.y) {
                enemyMoves++;
                if (enemyMoves < 2) {
                    thinkDog2();
                } else {
                    checkEnd();
                }
            }
        } else if (mode == MODE_FOX) {
            Utils.moveAnimal(fox, deltaTime);
            if (fox.position.x == fox.desiredPosition.x && fox.position.y == fox.desiredPosition.y) {
                enemyMoves++;
                if (enemyMoves < 3) {
                    thinkFox();
                } else {
                    enemyMoves = 0;
                    thinkDog1();
                }
            }
        }

        if (currentBox != null) {
            currentBox.update(deltaTime);
            if (currentBox.glowing == false) {
                currentBox = null;
            }
        }


    }


    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK) {
            ((MerlinGame) game).loadStoryScreen();
        }
        return false;
    }


    public void reset(int section, int level) {
        System.out.println("Reset " + section + " - " + level);
        currentLevel = level;
        currentSection = section;
        numMoves = 0;

        Cache.backgroundBlack.visible = false;
        Cache.backgroundHelp.visible = false;
        levelComplete.visible = false;
        levelLose.visible = false;
        starBig.visible = false;
        starSmall1.visible = false;
        starSmall2.visible = false;
        btnHome.visible = false;
        btnLevels.visible = false;
        btnReload.visible = false;
        btnNext.visible = false;

        generateLabyrint(section, level);
    }

    private void createGameObjects() {
        addGameObject(Cache.backgroundPlay);

        // Draw squares
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Box box = new Box(285 + 80 * x, 645 - (80 * y));
                addGameObject(box);
            }
        }


        wallsH = new Array<SimpleGameObject>(NUM_WALLS);
        wallsV = new Array<SimpleGameObject>(NUM_WALLS);

        enemiesList = new Array<Animal>();

        bunny = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BUNNY), Constants.ANIMAL_BUNNY);
        bunny.drawShadow = true;
        dog1 = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG1), Constants.ANIMAL_DOG1);
        dog1.drawShadow = true;
        dog2 = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG2), Constants.ANIMAL_DOG2);
        dog2.drawShadow = true;
        fox = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_FOX), Constants.ANIMAL_FOX);
        fox.drawShadow = true;
        burrow = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BURROW), Constants.ANIMAL_BURROW);

        enemiesList.add(fox);
        enemiesList.add(dog1);
        enemiesList.add(dog2);


        dog1.visible = false;
        dog2.visible = false;
        fox.visible = false;

        for (int i = 0; i < NUM_WALLS; i++) {
            SimpleGameObject wall = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BARRIER_H));
            wallsH.add(wall);
            addGameObject(wall);

            wall = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BARRIER_V));
            wallsV.add(wall);
            addGameObject(wall);

        }

        addGameObject(burrow);
        addGameObject(bunny);
        addGameObject(dog1);
        addGameObject(dog2);
        addGameObject(fox);

        Cache.backgroundHelp.visible = false;
        addGameObject(Cache.backgroundHelp);

        helpItems = new Array<HelpItem>(NUM_HELP);
        for (int i = 0; i < NUM_HELP; i++) {
            HelpItem hi = new HelpItem();
            helpItems.add(hi);
            addGameObject(hi);
        }


        Cache.backgroundBlack.visible = false;
        addGameObject(Cache.backgroundBlack);


        levelComplete = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.LEVEL_COMPLETE));
        levelComplete.position.x = posMiddleX(levelComplete);
        levelComplete.position.y = posMiddleY(levelComplete);
        levelComplete.visible = false;
        addGameObject(levelComplete);

        levelLose = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.LEVEL_LOSE));
        levelLose.position.x = posMiddleX(levelLose);
        levelLose.position.y = posMiddleY(levelLose);
        levelLose.visible = false;
        addGameObject(levelLose);

        starBig = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.STAR_BIG));
        starBig.position.x = 570;
        starBig.position.y = 325;
        starBig.visible = false;
        addGameObject(starBig);


        starSmall1 = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.STAR_SMALL));
        starSmall1.position.x = 450;
        starSmall1.position.y = 296;
        starSmall1.visible = false;
        addGameObject(starSmall1);


        starSmall2 = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.STAR_SMALL));
        starSmall2.position.x = 704;
        starSmall2.position.y = 296;
        starSmall2.visible = false;
        addGameObject(starSmall2);

        btnHome = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.BTN_HOME));

        btnHome.visible = false;
        addGameObject(btnHome);

        btnLevels = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.BTN_LEVELS));
        btnLevels.visible = false;
        addGameObject(btnLevels);

        btnReload = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.BTN_RELOAD));
        btnReload.visible = false;
        addGameObject(btnReload);

        btnNext = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.BTN_NEXT));
        btnNext.visible = false;
        addGameObject(btnNext);


        if (isEditing) {

            editButtons = new Array<SimpleGameObject>(7);

            editFloor1 = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.EDIT_FLOOR_1));
            editFloor2 = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.EDIT_FLOOR_2));
            editFloor3 = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.EDIT_FLOOR_3));
            editFloor4 = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.EDIT_FLOOR_4));
            editMerlin = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BUNNY));
            editBurrow = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BURROW));
            editDog = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG1));

            editButtons.add(editFloor1);
            editButtons.add(editFloor2);
            editButtons.add(editFloor3);
            editButtons.add(editFloor4);
            editButtons.add(editMerlin);
            editButtons.add(editBurrow);
            editButtons.add(editDog);

            float y = Constants.HEIGHT - 100;

            for (SimpleGameObject btn : editButtons) {
                btn.position.x = 20;
                btn.position.y = y;
                y = y - 100;
                addGameObject(btn);
            }
            selectEditButton(editMerlin);


            editPlay = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.EDIT_PLAY));
            editPlay.position.x = Constants.WIDTH - 100;
            editPlay.position.y = 500;
            addGameObject(editPlay);
            editEdit = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.EDIT_EDIT));
            editEdit.position.x = Constants.WIDTH - 100;
            editEdit.position.y = 350;
            addGameObject(editEdit);
            editSave = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.EDIT_SAVE));
            editSave.position.x = Constants.WIDTH - 100;
            editSave.position.y = 200;
            addGameObject(editSave);


        }


    }

    void selectEditButton(SimpleGameObject editButton) {
        if (currentEdit != null) {
            currentEdit.position.x = 20;
        }
        currentEdit = editButton;
        currentEdit.position.x = 175;
    }

    void setHelpItemsInvisible() {
        for (HelpItem hi : helpItems) {
            if (hi.visible) {
                hi.visible = false;
                hi.dismissed = true;
            }
        }
    }

    void resetHelpItems() {
        for (HelpItem hi : helpItems) {
            hi.visible = false;
            hi.dismissed = false;
            hi.setTarget(-100, -100);
        }
    }


    Labyrinth generateValidRandomLabyrint() {
        Engine2 engine = new Engine2(2);
        boolean ok = false;
        int i = 0;
        setHelpItemsInvisible();
        int size = 5 + random.nextInt(5);
        while (!ok) {
            System.out.println("---------> " + (i++));
            labyrinth = createRandomLabyrint(size, size, 2);
            drawLabyrint();

            ok = engine.startSolveGame(labyrinth);
        }

        serializeLabyrint();
        return labyrinth;
    }

    void generateLabyrint(int section, int level) {
        if (isEditing) {
            if (level == 0) {
                generateLabyrintEmpty();
            } else {
                labyrinth = deSerializeLabyrint(section, level);
            }
            currentEnemy = dog1;
            currentEnemy2 = null;
            dog1.visible = true;
            drawLabyrint();
            mode = MODE_EDIT;
        } else {
            if (generateRandom) {
                int numGenerate = 1;
                String[] llist = new String[numGenerate];
                for (int i = 0; i < numGenerate; i++) {
                    labyrinth = generateValidRandomLabyrint();
                    llist[i] = labyrinth.toJsonString();
                }

                for (int i = 0; i < numGenerate; i++) {
                    System.out.println("\"" + llist[i] + "\",");
                }


                drawLabyrint();
            } else {
                labyrinth = deSerializeLabyrint(section, level);
                drawLabyrint();
                showHelp(level);
            }
            setModeHelpOrPlayer();

        }
    }

    void setModeHelpOrPlayer() {

        boolean showHelp = false;
        for (HelpItem helpItem : helpItems) {
            helpItem.updateVisibility();
            if (helpItem.visible) {
                showHelp = true;
                break;
            }
        }

        if (showHelp) {
            mode = MODE_HELP;
            Cache.backgroundHelp.visible = true;
        } else {
            mode = MODE_PLAYER;
            Cache.backgroundHelp.visible = false;
        }


    }

    void showHelp(int level) {
        Cache.backgroundHelp.visible = false;
        resetHelpItems();
        Vector2 pos;
        if (currentSection == 0) {
            if (level == 0) {
                pos = Utils.coordsToScreen(2, 5);
                helpItems.get(0).initialize(
                        "HELP_0_0_0",
                        Utils.coordsToScreen(2, 5),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

                pos = Utils.coordsToScreen(3, 4);
                helpItems.get(1).initialize(
                        "HELP_0_0_1",
                        Utils.coordsToScreen(2, 2),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

                helpItems.get(2).initialize(
                        "HELP_0_0_2",
                        Utils.coordsToScreen(3, 2),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

                pos = Utils.coordsToScreen(5, 3);
                helpItems.get(3).initialize(
                        "HELP_0_0_3",
                        Utils.coordsToScreen(6, 3),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

                pos = Utils.coordsToScreen(6, 5);
                helpItems.get(4).initialize(
                        "HELP_0_0_4",
                        Utils.coordsToScreen(6, 4),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

            } else if (level == 1) {
                pos = Utils.coordsToScreen(5, 2);
                helpItems.get(0).initialize(
                        "HELP_0_1_0",
                        Utils.coordsToScreen(5, 5),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

                pos = Utils.coordsToScreen(3, 5);
                helpItems.get(1).initialize(
                        "HELP_0_1_1",
                        Utils.coordsToScreen(4, 5),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

                pos = Utils.coordsToScreen(3, 4);
                helpItems.get(2).initialize(
                        "HELP_0_1_2",
                        Utils.coordsToScreen(3, 5),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );
            } else if (level == 2) {
                pos = Utils.coordsToScreen(3, 5);
                helpItems.get(0).initialize(
                        "HELP_0_2_0",
                        Utils.coordsToScreen(3, 5),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

            } else if (level == 3) {
                pos = Utils.coordsToScreen(4, 5);
                helpItems.get(0).initialize(
                        "HELP_0_3_0",
                        Utils.coordsToScreen(4, 5),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );
            } else if (level == 4) {
                pos = Utils.coordsToScreen(3, 5);
                helpItems.get(0).initialize(
                        "HELP_0_4_0",
                        Utils.coordsToScreen(3, 5),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

            } else if (level == 9) {
                pos = Utils.coordsToScreen(2, 2);
                helpItems.get(0).initialize(
                        "HELP_0_9_0",
                        Utils.coordsToScreen(3, 2),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );
            }
        } else if (currentSection == 1) {
            if (level == 0) {
                pos = Utils.coordsToScreen(6, 2);
                helpItems.get(0).initialize(
                        "HELP_1_0_0",
                        Utils.coordsToScreen(3, 3),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

                pos = Utils.coordsToScreen(2, 4);
                helpItems.get(1).initialize(
                        "HELP_1_0_1",
                        Utils.coordsToScreen(2, 6),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );
            }
        } else if (currentSection == 2) {
            if (level == 0) {
                pos = Utils.coordsToScreen(2, 5);
                helpItems.get(0).initialize(
                        "HELP_2_0_0",
                        Utils.coordsToScreen(6, 6),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

            } else if (level == 1) {
                pos = Utils.coordsToScreen(3, 5);
                helpItems.get(0).initialize(
                        "HELP_2_1_0",
                        Utils.coordsToScreen(2, 5),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

            } else if (level == 2) {
                pos = Utils.coordsToScreen(5, 6);
                helpItems.get(0).initialize(
                        "HELP_2_2_0",
                        Utils.coordsToScreen(6, 4),
                        bunny,
                        pos.x - 17,
                        pos.y - 20
                );

            }
        }

    }

    void generateLabyrintEmpty() {

        labyrinth = new Labyrinth(9, 9);

        labyrinth.playerPosition.x = 1;
        labyrinth.playerPosition.y = 1;

        labyrinth.enemyPosition[0].x = 1;
        labyrinth.enemyPosition[0].y = 1;

        labyrinth.enemyPosition[1].x = 1;
        labyrinth.enemyPosition[1].y = 1;

        labyrinth.exitPosition.x = 1;
        labyrinth.exitPosition.y = 1;

        labyrinth.enemy = new int[1];
        labyrinth.enemy[0] = 0;

    }


    void drawLabyrint() {
        bunny.labyrintCoords(labyrinth.playerPosition.x + 1, labyrinth.playerPosition.y + 1, true);
        currentEnemy.labyrintCoords(labyrinth.enemyPosition[0].x + 1, labyrinth.enemyPosition[0].y + 1, true);
        if (currentEnemy2 != null) {
            currentEnemy2.labyrintCoords(labyrinth.enemyPosition[1].x + 1, labyrinth.enemyPosition[1].y + 1, true);
        }
        burrow.labyrintCoords(labyrinth.exitPosition.x + 1, labyrinth.exitPosition.y + 1, true);


        for (int i = 0; i < NUM_WALLS; i++) {
            wallsH.get(i).visible = false;
            wallsV.get(i).visible = false;
        }


        int numWallH = 0;
        int numWallV = 0;

        int x = 0;
        int y = 0;

        SimpleGameObject barrier;


        for (int i = 0; i < Constants.LABYRINT_WIDTH * Constants.LABYRINT_HEIGHT; i++) {

            Square square = labyrinth.getSquare(x, y);
            if ((square.isLimitUp()) && (y > 0)) {
                barrier = wallsH.get(numWallH);
                Vector2 pos = Utils.coordsToScreen(x, y - 1);
                barrier.position.x = pos.x;
                barrier.position.y = pos.y - 10;
                barrier.visible = true;
                numWallH++;
            }
            if (square.isLimitRight() && (x < Constants.LABYRINT_WIDTH - 1)) {
                barrier = wallsV.get(numWallV);
                Vector2 pos = Utils.coordsToScreen(x + 1, y);
                barrier.position.x = pos.x - 10;
                barrier.position.y = pos.y;
                barrier.visible = true;
                numWallV++;
            }
            /*if (square.isLimitDown()) {
                SimpleGameObject barrier = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BARRIER_H));
                barrier.position = Utils.coordsToScreen(x, y);
                addGameObject(barrier);
            }
            if (square.isLimitLeft()) {
                SimpleGameObject barrier = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BARRIER_V));
                barrier.position = Utils.coordsToScreen(x, y);
                addGameObject(barrier);
            }*/


            x += 1;
            if (x == Constants.LABYRINT_WIDTH) {
                x = 0;
                y++;
            }


        }

    }

    // Priority horizontal
    private void thinkDog1() {
        if (dog1.visible) {
            if (currentEnemy == dog1) {
                Engine2.moveEnemy(labyrinth, bunny, dog1, currentEnemy2, false);
            } else {
                Engine2.moveEnemy(labyrinth, bunny, dog1, currentEnemy, false);
            }
            mode = MODE_DOG1;
        } else {
            thinkDog2();
        }

    }

    // Priority vertical
    private void thinkDog2() {
        if (dog2.visible) {
            if (currentEnemy == dog2) {
                Engine2.moveEnemy(labyrinth, bunny, dog2, currentEnemy2, false);
            } else {
                Engine2.moveEnemy(labyrinth, bunny, dog2, currentEnemy, false);
            }
            mode = MODE_DOG2;
        } else {
            checkEnd();
        }

    }

    // Priority horizontal
    private void thinkFox() {
        if (fox.visible) {
            if (currentEnemy == fox) {
                Engine2.moveEnemy(labyrinth, bunny, fox, currentEnemy2, false);
            } else {
                Engine2.moveEnemy(labyrinth, bunny, fox, currentEnemy, false);
            }
            mode = MODE_FOX;
        } else {
            thinkDog1();
        }

    }

    private void checkEnd() {
        if (!isEditing) {
            if (bunny.sameCoordinates(currentEnemy) || bunny.sameCoordinates(currentEnemy2)) {
                lose();
            } else if (bunny.sameCoordinates(burrow)) {
                win();

            } else {
                setModeHelpOrPlayer();
            }
        }
    }

    private void win() {
        if (mode != MODE_ENDING) {
            if (((MerlinGame) game).sound) {
                playSound(Cache.winSound, 1f);
            }
            mode = MODE_ENDING;
            btnReload.position.x = 644;
            btnReload.position.y = 100;
            btnHome.position.x = 460;
            btnHome.position.y = 100;
            btnLevels.position.x = 552;
            btnLevels.position.y = 100;
            btnNext.position.x = 736;
            btnNext.position.y = 100;

            Cache.backgroundBlack.visible = true;
            levelComplete.visible = true;
            System.out.println(numMoves + " / " + labyrinth.minMoves);
            int stars = 0;
            if (numMoves < labyrinth.minMoves + 4) {
                starSmall1.visible = true;
                stars = 1;
            }

            if (numMoves < labyrinth.minMoves + 2) {
                starBig.visible = true;
                stars = 2;
            }

            if (numMoves == labyrinth.minMoves) {
                starSmall2.visible = true;
                stars = 3;
            }


            btnHome.visible = true;
            btnLevels.visible = true;
            btnReload.visible = true;
            btnNext.visible = true;
            ((MerlinGame) screen.getGame()).setStars(currentSection, currentLevel, stars);
            ((MerlinGame) screen.getGame()).setMaxLevel(currentSection, currentLevel + 2);
        }

    }

    private void lose() {
        if (mode != MODE_ENDING) {
            if (((MerlinGame) game).sound) {
                playSound(Cache.ouchSound, 1f);
            }
            mode = MODE_ENDING;
            Cache.backgroundBlack.visible = true;

            btnHome.position.x = 500;
            btnHome.position.y = 100;
            btnLevels.position.x = 600;
            btnLevels.position.y = 100;
            btnReload.position.x = 700;
            btnReload.position.y = 100;

            Cache.backgroundBlack.visible = true;
            levelLose.visible = true;
            btnHome.visible = true;
            btnLevels.visible = true;
            btnReload.visible = true;
        }
    }


    @Override
    protected void init() {
        createGameObjects();
    }

    public void touch(float x, float y) {
        long now = System.currentTimeMillis();
        if ((now - lastTouch > 500) && (lastPointTouch.x != x) && (lastPointTouch.y != y)) {
            lastTouch = now;
            lastPointTouch.x = x;
            lastPointTouch.y = y;

            if (mode == MODE_PLAYER) {
                if (currentBox == null) {
                    Vector2 coords = Utils.screenToCoords(x, y);


                    Array<AbstractGameObject> list = touchedGameObjects(x, y);
                    for (AbstractGameObject object : list) {
                        if (object instanceof Box) {
                            ((Box) object).touch();
                            currentBox = (Box) object;
                            break;
                        }
                    }


                    int cx = (int) Math.floor(coords.x);
                    int cy = (int) Math.floor(coords.y);

                    boolean moveOk = false;

                    if (cx == bunny.getCoordX()) {
                        if (cy - bunny.getCoordY() == -1) {
                            moveOk = Engine2.movePlayer(labyrinth, bunny, Constants.DIRECTION_UP);
                        } else if (cy - bunny.getCoordY() == 1) {
                            moveOk = Engine2.movePlayer(labyrinth, bunny, Constants.DIRECTION_DOWN);
                        } else if (cy == bunny.getCoordY()) {
                            moveOk = Engine2.movePlayer(labyrinth, bunny, Constants.DIRECTION_PASS);
                        }
                    } else if (cy == bunny.getCoordY()) {
                        if (cx - bunny.getCoordX() == -1) {
                            moveOk = Engine2.movePlayer(labyrinth, bunny, Constants.DIRECTION_LEFT);
                        } else if (cx - bunny.getCoordX() == 1) {
                            moveOk = Engine2.movePlayer(labyrinth, bunny, Constants.DIRECTION_RIGHT);
                        }
                    }
                    if (moveOk) {
                        numMoves++;
                        mode = MODE_BUNNY;
                    }
                }
            } else if (mode == MODE_ENDING) {
                Array<AbstractGameObject> list = touchedGameObjects(x, y);
                for (AbstractGameObject object : list) {
                    if (object.equals(btnReload)) {
                        reset(currentSection, currentLevel);
                        break;
                    }
                    if (object.equals(btnHome)) {
                        ((MerlinGame) game).loadMenuScreen();
                        break;
                    }
                    if (object.equals(btnLevels)) {
                        ((MerlinGame) game).loadStoryScreen();
                        break;
                    }
                    if (object.equals(btnNext)) {
                        if (currentLevel + 1 < Constants.LEVEL_LIST[currentSection].length) {
                            reset(currentSection, currentLevel + 1);
                        } else {
                            ((MerlinGame) game).loadStoryScreen();
                        }
                        break;
                    }
                }

            } else if (mode == MODE_HELP) {
                if (y < 170) {
                    Cache.backgroundHelp.visible = false;
                    setHelpItemsInvisible();
                    mode = MODE_PLAYER;
                }

            } else if (isEditing) {
                touchEditing(x, y);
            }
        }
    }

    private void touchEditing(float x, float y) {
        boolean touchBtn = false;
        Array<AbstractGameObject> list = touchedGameObjects(x, y);
        for (AbstractGameObject object : list) {
            SimpleGameObject btn = (SimpleGameObject) object;
            if (editButtons.contains(btn, false)) {
                selectEditButton(btn);
                touchBtn = true;
            } else if (editEdit.equals(btn)) {
                reset(0, 0);
            } else if (editSave.equals(btn)) {
                System.out.print("Saving!!!");
                serializeLabyrint();
                touchBtn = true;
            }
        }

        if (!touchBtn) {

            Vector2 coords = Utils.screenToCoords(x, y);
            int cx = (int) Math.floor(coords.x);
            int cy = (int) Math.floor(coords.y);

            int direction = -1;

            if (currentEdit.equals(editFloor1)) {
                direction = Constants.DIRECTION_UP;
            } else if (currentEdit.equals(editFloor2)) {
                direction = Constants.DIRECTION_RIGHT;
            } else if (currentEdit.equals(editFloor3)) {
                direction = Constants.DIRECTION_DOWN;
            } else if (currentEdit.equals(editFloor4)) {
                direction = Constants.DIRECTION_LEFT;
            }

            if (direction != -1) {
                labyrinth.toggleWall(cx, cy, direction);

            } else if (currentEdit.equals(editMerlin)) {
                labyrinth.playerPosition.x = cx;
                labyrinth.playerPosition.y = cy;
            } else if (currentEdit.equals(editBurrow)) {
                labyrinth.exitPosition.x = cx;
                labyrinth.exitPosition.y = cy;
            } else if (currentEdit.equals(editDog)) {
                labyrinth.enemyPosition[0].x = cx;
                labyrinth.enemyPosition[0].y = cy;
            }


            drawLabyrint();


        }
    }

    private Labyrinth deSerializeLabyrint(int section, int level) {
        generateLabyrintEmpty();
        JsonValue root = new JsonReader().parse(Constants.LEVEL_LIST[section][level]);

        dog1.visible = false;
        dog2.visible = false;
        fox.visible = false;

        labyrinth.enemy = root.get("enemy").asIntArray();
        currentEnemy = enemiesList.get(labyrinth.enemy[0]);
        currentEnemy.visible = true;

        dog1.labyrintCoords(100, 100, true);
        dog2.labyrintCoords(100, 100, true);
        fox.labyrintCoords(100, 100, true);

        labyrinth.minMoves = root.get("minMoves").asInt();

        labyrinth.enemyPosition[0].x = root.get("enemyPosition").get("x").asInt();
        labyrinth.enemyPosition[0].y = root.get("enemyPosition").get("y").asInt();


        if (labyrinth.enemy.length == 2) {
            currentEnemy2 = enemiesList.get(labyrinth.enemy[1]);
            currentEnemy2.visible = true;
            labyrinth.enemyPosition[1].x = root.get("enemyPosition2").get("x").asInt();
            labyrinth.enemyPosition[1].y = root.get("enemyPosition2").get("y").asInt();
        } else {
            currentEnemy2 = null;
            labyrinth.enemyPosition[1].x = -1;
            labyrinth.enemyPosition[1].y = -1;
        }


        labyrinth.playerPosition.x = root.get("playerPosition").get("x").asInt();
        labyrinth.playerPosition.y = root.get("playerPosition").get("y").asInt();

        labyrinth.exitPosition.x = root.get("exitPosition").get("x").asInt();
        labyrinth.exitPosition.y = root.get("exitPosition").get("y").asInt();


        for (int y = 0; y < root.get("height").asInt(); y++) {
            for (int x = 0; x < root.get("width").asInt(); x++) {
                JsonValue sq = root.get("squares").get(x).get(y);
                if (sq.get("limitDown").asBoolean()) {
                    labyrinth.addWall(x, y, Constants.DIRECTION_DOWN);
                }
                /*if (sq.get("limitUp").asBoolean()) {
                    labyrinth.addWall(x, y, Constants.DIRECTION_UP);
                }
                if (sq.get("limitLeft").asBoolean()) {
                    labyrinth.addWall(x, y, Constants.DIRECTION_LEFT);
                }*/
                if (sq.get("limitRight").asBoolean()) {
                    labyrinth.addWall(x, y, Constants.DIRECTION_RIGHT);
                }
            }
        }


        return labyrinth;


    }


    private Labyrinth createRandomLabyrint(int width, int height, int numEnemies) {
        generateLabyrintEmpty();
        currentEnemy2 = null;
        dog1.labyrintCoords(100, 100, true);
        dog2.labyrintCoords(100, 100, true);
        fox.labyrintCoords(100, 100, true);
        dog1.visible = false;
        dog2.visible = false;
        fox.visible = false;


        labyrinth.enemy = new int[numEnemies];
        labyrinth.enemy[0] = 0; //random.nextInt(3);
        currentEnemy = enemiesList.get(labyrinth.enemy[0]);

        if (numEnemies == 2) {
            labyrinth.enemy[1] = 1;//random.nextInt(3);
            while (labyrinth.enemy[0] == labyrinth.enemy[1]) {
                labyrinth.enemy[1] = random.nextInt(3);
            }

            if (labyrinth.enemy[1] < labyrinth.enemy[0]) {
                int a = labyrinth.enemy[1];
                labyrinth.enemy[1] = labyrinth.enemy[0];
                labyrinth.enemy[0] = a;
            }

            currentEnemy = enemiesList.get(labyrinth.enemy[0]);
            currentEnemy2 = enemiesList.get(labyrinth.enemy[1]);
            currentEnemy2.visible = true;
        }
        currentEnemy.visible = true;


        labyrinth.minMoves = 0;

        int despX = (int) Math.floor((Constants.LABYRINT_WIDTH - width) / 2);
        int despY = (int) Math.floor((Constants.LABYRINT_HEIGHT - height) / 2);


        labyrinth.enemyPosition[0].x = random.nextInt(width) + despX;
        labyrinth.enemyPosition[0].y = random.nextInt(height) + despY;

        if (numEnemies == 2) {
            labyrinth.enemyPosition[1].x = random.nextInt(width) + despX;
            labyrinth.enemyPosition[1].y = random.nextInt(height) + despY;
        }


        labyrinth.playerPosition.x = random.nextInt(width) + despX;
        labyrinth.playerPosition.y = random.nextInt(height) + despY;

        labyrinth.exitPosition.x = random.nextInt(width) + despX;
        labyrinth.exitPosition.y = random.nextInt(height) + despY;


        System.out.println("Desp: " + despX + ", " + despY);


        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                if (x == 0) {
                    labyrinth.addWall(x + despX, y + despY, Constants.DIRECTION_LEFT);
                }
                if (y == 0) {
                    labyrinth.addWall(x + despX, y + despY, Constants.DIRECTION_UP);
                }

                if ((y == height - 1) || (random.nextInt(100) > 85)) {
                    labyrinth.addWall(x + despX, y + despY, Constants.DIRECTION_DOWN);
                }
                if ((x == width - 1) || (random.nextInt(100) > 85)) {
                    labyrinth.addWall(x + despX, y + despY, Constants.DIRECTION_RIGHT);
                }
            }
        }

        return labyrinth;
    }


    private void serializeLabyrint() {
        System.out.println(labyrinth.toJsonString());
    }


}

