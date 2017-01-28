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
    public String[] title;
    public String[] explanation;

    public InfoWindow() {
        super(GameAssets.instance.getTextureRegion(GameAssets.LEVEL_INFO));
        title = MerlinGame.textBundle.get("BUY_HINT_TITLE").split("\n");
        explanation = MerlinGame.textBundle.get("BUY_HINT_EXPLANATION").split("\n");
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (this.visible && this.text != null) {
            float x = this.position.x + this.dimension.x / 2 - (10 * title[0].length());
            Cache.font.draw(batch, title[0], x, this.position.y + 500);
            x = this.position.x + this.dimension.x / 2 - (10 * title[1].length());
            Cache.font.draw(batch, title[1], x, this.position.y + 465);


            x = this.position.x + this.dimension.x / 2 - (10 * explanation[0].length());
            Cache.font.draw(batch, explanation[0], x, this.position.y + 270);
            x = this.position.x + this.dimension.x / 2 - (10 * explanation[1].length());
            Cache.font.draw(batch, explanation[1], x, this.position.y + 240);
            x = this.position.x + this.dimension.x / 2 - (10 * explanation[2].length());
            Cache.font.draw(batch, explanation[2], x, this.position.y + 210);

            Cache.font.draw(batch, "x " + text, 640, 360);
        }
    }
}
