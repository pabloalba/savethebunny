package es.seastorm.merlin.screens.labyrint.help;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.MerlinGame;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.cache.Cache;
import es.seastorm.merlin.gameobjects.Animal;

public class InfoWindow extends SimpleGameObject {
    public String text;

    public InfoWindow() {
        super(GameAssets.instance.getTextureRegion(GameAssets.LEVEL_INFO));
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (this.visible && this.text != null) {
            Cache.font.draw(batch, text, 600, 300);
        }
    }
}
