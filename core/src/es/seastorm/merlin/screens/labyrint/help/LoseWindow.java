package es.seastorm.merlin.screens.labyrint.help;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.MerlinGame;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.cache.Cache;

public class LoseWindow extends SimpleGameObject {
    String text;

    public LoseWindow() {
        super(GameAssets.instance.getTextureRegion(GameAssets.LEVEL_LOSE));
        text = MerlinGame.textBundle.get("LOSE");
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (this.visible) {
            float x = this.position.x + this.dimension.x / 2 - (10 * text.length());
            Cache.font.draw(batch, text, x, this.position.y + 310);
        }
    }
}
