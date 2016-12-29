package es.seastorm.merlin.cache;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import dragongames.base.gameobject.AbstractGameObject;
import dragongames.base.gameobject.SimpleGameObject;

public class Cache {
    // Font
    public static BitmapFont font = new BitmapFont(Gdx.files.internal("font/BitmapFont.fnt"), false);
    public static AbstractGameObject backgroundMenu = new SimpleGameObject("backgrounds/bg_menu.jpg");
    public static AbstractGameObject backgroundMenuTitle = new SimpleGameObject("backgrounds/bg_menu_title.jpg");
    public static AbstractGameObject backgroundPlay = new SimpleGameObject("backgrounds/bg_play.jpg");
    public static AbstractGameObject backgroundBlack = new SimpleGameObject("backgrounds/bg_black.png");
    public static AbstractGameObject backgroundHelp = new SimpleGameObject("backgrounds/bg_help.png");

    public static Sound ouchSound = Gdx.audio.newSound(Gdx.files.internal("sound/ouch.wav"));
    public static Sound winSound = Gdx.audio.newSound(Gdx.files.internal("sound/win.mp3"));
}
