package es.seastorm.merlin.screens.story;

import com.badlogic.gdx.Game;

import dragongames.base.renderer.Renderer;
import dragongames.base.screen.AbstractGameScreen;
import es.seastorm.merlin.Constants;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.screens.menu.MenuScreen;

public class StoryScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();

    public StoryScreen(Game game) {
        super(game, GameAssets.instance);
    }

    @Override
    public void show() {
        controller = new StoryScreenController(game);
        renderer = new Renderer(controller, Constants.WIDTH, Constants.HEIGHT);
        super.show(controller, renderer);
    }
}
