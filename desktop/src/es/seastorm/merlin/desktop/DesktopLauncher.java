package es.seastorm.merlin.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import es.seastorm.merlin.MerlinGame;

public class DesktopLauncher {
    private static boolean rebuildAtlas = false;
    private static boolean drawDebugOutline = false;

    public static void main(String[] arg) {
        if (rebuildAtlas) {
            TexturePacker.Settings settings = new TexturePacker.Settings();

            settings.maxWidth = 2048;
            settings.maxHeight = 2048;
            settings.debug = drawDebugOutline;

            TexturePacker.process(settings, "raw/images", "images", "merlin-images.pack");

        }

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new MerlinGame(), config);
    }
}
