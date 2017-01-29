package es.seastorm.merlin.screens.labyrint;

import com.badlogic.gdx.Game;

import dragongames.base.renderer.Renderer;
import dragongames.base.screen.AbstractGameScreen;
import es.seastorm.merlin.Constants;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.screens.menu.MenuScreen;
import es.seastorm.merlin.screens.story.StoryScreenController;

public class LabyrintScreen extends AbstractGameScreen {
    private static final String TAG = MenuScreen.class.getName();
    public static int level = 0;
    public static int section = 0;

    public LabyrintScreen(Game game) {
        super(game, GameAssets.instance);
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void show() {
        controller = getController();
        renderer = getRenderer();
        reset();
        //System.out.println("Post reset");
        super.show(controller, renderer);
    }


    public LabyrintScreenController getController() {
        if (controller == null) {
            controller = new LabyrintScreenController(game, this);
        }
        return ((LabyrintScreenController) controller);
    }

    public Renderer getRenderer() {
        return new Renderer(controller, Constants.WIDTH, Constants.HEIGHT);
    }

    public void reset() {
        getController().reset(section, level);
    }
}
