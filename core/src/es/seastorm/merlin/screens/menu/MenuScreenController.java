package es.seastorm.merlin.screens.menu;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

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

public class MenuScreenController extends Controller {
    public AbstractGameObject btnPlay;
    public AbstractGameObject btnMusic, btnFx, btnMusicOff, btnFxOff;
    private Preferences prefs;
    Animal bunny, enemy;
    Animal[] enemies;
    Random random;


    public MenuScreenController(Game game) {
        super(game, Constants.WIDTH, Constants.HEIGHT);
        Gdx.input.setCatchBackKey(true);
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.BACK) {
            System.exit(0);
        }
        return false;
    }

    private void createGameObjects() {
        random = new Random();
        //Background
        addGameObject(Cache.backgroundMenuTitle);

        bunny = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BUNNY), Constants.ANIMAL_BUNNY);
        bunny.position.x = -200;
        bunny.position.y = 635;
        bunny.desiredPosition.x = Constants.WIDTH + 150;
        bunny.desiredPosition.y = 635;


        enemies = new Animal[3];
        enemies[0] = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG1), Constants.ANIMAL_DOG1);
        enemies[1] = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_DOG2), Constants.ANIMAL_DOG2);
        enemies[2] = new Animal(GameAssets.instance.getTextureRegion(GameAssets.ASSET_FOX), Constants.ANIMAL_FOX);


        enemies[0].position.x = -200;
        enemies[0].position.y = 635;
        enemies[1].position.x = -200;
        enemies[1].position.y = 635;
        enemies[2].position.x = -200;
        enemies[2].position.y = 635;

        enemies[0].visible = false;
        enemies[1].visible = false;
        enemies[2].visible = false;

        addGameObject(bunny);
        addGameObject(enemies[0]);
        addGameObject(enemies[1]);
        addGameObject(enemies[2]);

        selectEnemy(Constants.WIDTH + 150, 635);


        btnPlay = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_PLAY));
        btnPlay.position.x = 190;
        btnPlay.position.y = 20;
        addGameObject(btnPlay);

        btnMusic = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_MUSIC));
        btnMusic.position.x = 1155;
        btnMusic.position.y = 150;
        btnMusic.visible = ((MerlinGame) game).music;
        addGameObject(btnMusic);

        btnMusicOff = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_MUSIC_OFF));
        btnMusicOff.position.x = 1155;
        btnMusicOff.position.y = 150;
        btnMusicOff.visible = !((MerlinGame) game).music;
        addGameObject(btnMusicOff);

        btnFx = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_FX));
        btnFx.position.x = 1155;
        btnFx.position.y = 30;
        btnFx.visible = ((MerlinGame) game).sound;
        addGameObject(btnFx);

        btnFxOff = new SimpleGameObject(GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_FX_OFF));
        btnFxOff.position.x = 1155;
        btnFxOff.position.y = 30;
        btnFxOff.visible = !((MerlinGame) game).sound;
        addGameObject(btnFxOff);


        startStopMusic();
    }


    @Override
    protected void init() {
        createGameObjects();
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        loadPrefs();
    }

    public void update(float deltaTime) {

        Utils.moveAnimal(bunny, deltaTime);
        Utils.moveAnimal(enemy, deltaTime);
        if (bunny.position.x == bunny.desiredPosition.x && bunny.position.y == bunny.desiredPosition.y) {
            float y = 635;
            if (random.nextBoolean()) {
                y = 0;
            }

            bunny.position.y = y;
            bunny.desiredPosition.y = y;

            if (bunny.position.x <= -250) {
                selectEnemy(Constants.WIDTH + 150, y);
                bunny.desiredPosition.x = Constants.WIDTH + 150;
            } else {
                selectEnemy(-250, y);
                bunny.desiredPosition.x = -250;
            }
        }
    }

    private void selectEnemy(float desiredX, float desiredY) {
        enemies[0].visible = false;
        enemies[1].visible = false;
        enemies[2].visible = false;
        enemy = enemies[random.nextInt(3)];

        if (desiredX <= -200) {
            enemy.position.x = Constants.WIDTH + 300;
        } else {
            enemy.position.x = -350;
        }
        enemy.desiredPosition.x = desiredX;
        enemy.desiredPosition.y = desiredY;
        enemy.position.y = desiredY;

        enemy.rotation = 30;

        enemy.visible = true;

    }

    public void touch(float x, float y) {
        Array<AbstractGameObject> list = touchedGameObjects(x, y);
        for (AbstractGameObject object : list) {
            if (object.equals(btnPlay)) {
                startStory();
            } else if (object.equals(btnMusic)) {
                toggleMusic();
            } else if (object.equals(btnFx)) {
                toggleSound();
            }
        }
    }


    private void startStory() {
        ((MerlinGame) game).currentPhase = -1;
        ((MerlinGame) game).currentFirstLevel = -1;
        ((MerlinGame) game).loadStoryScreen();
    }


    private void toggleMusic() {
        ((MerlinGame) game).setMusic(!((MerlinGame) game).music);
        btnMusic.visible = ((MerlinGame) game).music;
        btnMusicOff.visible = !((MerlinGame) game).music;
        startStopMusic();
    }

    private void startStopMusic() {
        if (((MerlinGame) game).music) {
            playMusic("sound/RussianDance.mp3", 0.3f);
        } else {
            stopMusic();
        }
    }

    private void toggleSound() {
        ((MerlinGame) game).setSound(!((MerlinGame) game).sound);
        btnFx.visible = ((MerlinGame) game).sound;
        btnFxOff.visible = !((MerlinGame) game).sound;

        if (((MerlinGame) game).sound) {
            playSound(Cache.winSound, 1f);
        }
    }


    public void loadPrefs() {
        MerlinGame merlinGame = (MerlinGame) game;
        //prefs.clear();
        //prefs.flush();

        merlinGame.sound = prefs.getBoolean("sound", true);
        merlinGame.music = prefs.getBoolean("music", true);

        merlinGame.maxLevel[0] = prefs.getInteger("maxlevel_0", 0);
        merlinGame.maxLevel[1] = prefs.getInteger("maxlevel_1", 0);
        merlinGame.maxLevel[2] = prefs.getInteger("maxlevel_2", 0);
        merlinGame.maxLevel[3] = prefs.getInteger("maxlevel_3", 0);

    }

    public void savePrefs() {
        MerlinGame merlinGame = (MerlinGame) game;
        prefs.putBoolean("sound", merlinGame.sound);
        prefs.putBoolean("music", merlinGame.music);
        prefs.flush();
    }

}
