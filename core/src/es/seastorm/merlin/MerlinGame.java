package es.seastorm.merlin;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;
import java.util.Random;

import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.screens.labyrint.LabyrintScreen;
import es.seastorm.merlin.screens.menu.MenuScreen;
import es.seastorm.merlin.screens.story.StoryScreen;

public class MerlinGame extends Game {
    private MenuScreen menuScreen = null;
    private LabyrintScreen labyrintScreen = null;
    private AssetManager assetManager;

    public static I18NBundle textBundle;

    public boolean music = true;
    public boolean sound = true;
    public static int[] maxLevel = {1, 1, 1, 1};

    public int currentPhase = -1;
    public int currentFirstLevel = -1;

    public MenuScreen getMenuScreen() {
        if (menuScreen == null) {
            menuScreen = new MenuScreen(this);
        }
        return menuScreen;
    }


    public LabyrintScreen getLabyrintScreen() {
        if (labyrintScreen == null) {
            labyrintScreen = new LabyrintScreen(this);
        }
        return labyrintScreen;
    }


    @Override
    public void create() {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);


        // Load rest of assets
        this.assetManager = new AssetManager();
        GameAssets.instance.init(this.assetManager);
        //this.assetManager.load("i18n/savethebunny", I18NBundle.class);
        //this.textBundle = assetManager.get("i18n/savethebunny", I18NBundle.class);

        //Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        //prefs.clear();
        //prefs.flush();


        FileHandle baseFileHandle = Gdx.files.internal("i18n/savethebunny");
        this.textBundle = I18NBundle.createBundle(baseFileHandle);


        this.music = getMusic();
        this.sound = getSound();

        // Start game at menu screen
        loadMenuScreen();
        //setMaxLevel(3, 90);


    }


    public void loadMenuScreen() {
        //playScreen = null;
        setScreen(getMenuScreen());
    }

    public void loadStoryScreen() {
        setScreen(new StoryScreen(this));
    }

    public void loadLabyrintScreen(int section, int level) {
        //System.out.println("loadLabyrintScreen " + section + " - " + level);
        getLabyrintScreen().level = level;
        getLabyrintScreen().section = section;
        setScreen(getLabyrintScreen());
    }

    public void setMaxLevel(int section, int newMaxLevel) {
        newMaxLevel = Math.min(newMaxLevel, Constants.LEVEL_LIST[section].length);
        if (maxLevel[section] < newMaxLevel) {
            maxLevel[section] = newMaxLevel;
            Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
            prefs.putInteger("maxlevel_" + section, newMaxLevel);
            prefs.flush();
        }
    }

    public void setStars(int section, int level, int stars) {
        int oldStars = getStars(section, level);
        if (oldStars < stars) {
            Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
            prefs.putInteger("stars_" + section + "_" + level, stars);
            prefs.flush();

            int treasure = getTreasure();
            setTreasure(treasure + stars - oldStars);
        }
    }

    public int getStars(int section, int level) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        int stars = prefs.getInteger("stars_" + section + "_" + level, 0);
        return stars;
    }

    public boolean getMusic() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        return prefs.getBoolean("music", true);
    }

    public void setMusic(boolean music) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        prefs.putBoolean("music", music);
        prefs.flush();
        this.music = music;
    }

    public boolean getSound() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        return prefs.getBoolean("sound", true);
    }

    public void setSound(boolean sound) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        prefs.putBoolean("sound", sound);
        prefs.flush();
        this.sound = sound;
    }

    public int getTreasure() {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        int treasure = prefs.getInteger("treasure", 0);
        return treasure;
    }

    public void setTreasure(int treasure) {
        Preferences prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
        prefs.putInteger("treasure", treasure);
        prefs.flush();
    }

}
