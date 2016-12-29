package es.seastorm.merlin.assets;

import com.badlogic.gdx.assets.AssetManager;

import dragongames.base.asset.AssetsKeeper;
import es.seastorm.merlin.Constants;

public class GameAssets extends AssetsKeeper {
    public static final String FILE_NAME = Constants.TEXTURE_ATLAS_OBJECTS;
    public static final String ASSET_BTN_PLAY = "btn_play";
    public static final String ASSET_BTN_MUSIC = "btn_music";
    public static final String ASSET_BTN_MUSIC_OFF = "btn_music_off";
    public static final String ASSET_BTN_FX = "btn_fx";
    public static final String ASSET_BTN_FX_OFF = "btn_fx_off";
    public static final String ASSET_BTN_LEVEL0 = "btn_level0";
    public static final String ASSET_BTN_LEVEL1 = "btn_level1";
    public static final String ASSET_BTN_LEVEL2 = "btn_level2";
    public static final String ASSET_BTN_LEVEL3 = "btn_level3";
    public static final String ASSET_LEVEL_SELECT_BG = "level_select_bg";
    public static final String ASSET_BTN_LOCK = "btn_lock";
    public static final String ASSET_BUNNY = "bunny";
    public static final String ASSET_DOG1 = "dog1";
    public static final String ASSET_DOG2 = "dog2";
    public static final String ASSET_FOX = "fox";
    public static final String ASSET_BURROW = "burrow";
    public static final String ASSET_BARRIER_V = "barrier_v";
    public static final String ASSET_BARRIER_H = "barrier_h";
    public static final String EDIT_FLOOR_1 = "edit_floor1";
    public static final String EDIT_FLOOR_2 = "edit_floor2";
    public static final String EDIT_FLOOR_3 = "edit_floor3";
    public static final String EDIT_FLOOR_4 = "edit_floor4";
    public static final String EDIT_SAVE = "edit_save";
    public static final String EDIT_PLAY = "edit_play";
    public static final String EDIT_EDIT = "edit_edit";
    public static final String LEVEL_COMPLETE = "level_complete";
    public static final String LEVEL_LOSE = "level_lose";
    public static final String STAR_SMALL = "star_small";
    public static final String STAR_BIG = "star_big";
    public static final String BTN_HOME = "btn_home";
    public static final String BTN_LEVELS = "btn_levels";
    public static final String BTN_NEXT = "btn_next";
    public static final String BTN_RELOAD = "btn_reload";
    public static final String BTN_PHASE1 = "phase1";
    public static final String BTN_PHASE2 = "phase2";
    public static final String BTN_PHASE3 = "phase3";
    public static final String BTN_PHASE4 = "phase4";
    public static final String ARROW_LEFT = "arrow_left";
    public static final String ARROW_RIGHT = "arrow_right";
    public static final String HELP = "help";
    public static final String SHADOW = "shadow";
    public static final String SQUARE = "square";
    public static final String SQUARE_GLOW1 = "square_glow1";
    public static final String SQUARE_GLOW2 = "square_glow2";
    public static final String SQUARE_GLOW3 = "square_glow3";
    public static final String SQUARE_GLOW4 = "square_glow4";
    public static final String SQUARE_GLOW5 = "square_glow5";


    public static final GameAssets instance = new GameAssets();


    // singleton: prevent instantiation from other classes
    private GameAssets() {
    }


    public void init(AssetManager assetManager) {
        super.init(assetManager, FILE_NAME);
    }
}
