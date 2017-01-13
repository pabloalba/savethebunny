package es.seastorm.merlin.screens.labyrint.help;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.cache.Cache;
import es.seastorm.merlin.gameobjects.Animal;

public class HelpItem extends SimpleGameObject {
    Vector2 target = new Vector2(-100, -100);
    Animal bunny;
    String text;
    public boolean dismissed = false;

    public HelpItem() {
        super(GameAssets.instance.getTextureRegion(GameAssets.HELP));
    }

    public void initialize(String text, Vector2 target, Animal bunny, float positionX, float positionY) {
        this.target.x = target.x;
        this.target.y = target.y;
        this.bunny = bunny;
        this.text = text;
        this.visible = false;
        this.position.x = positionX;
        this.position.y = positionY;
        this.dismissed = false;
    }

    public void setTarget(float targetX, float targetY) {
        if (this.target != null) {
            this.target.x = targetX;
            this.target.y = targetY;
        }
    }

    public void updateVisibility() {
        if ((!dismissed) && (bunny != null) && (target != null) && (bunny.position.x == target.x) && (bunny.position.y == target.y)) {
            this.visible = true;
        } else {
            this.visible = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (this.visible && this.text!=null) {
            String[] lines = text.split("\n");
            float y = 80 + (lines.length * 15);
            for (String line : lines) {
                float x = 640 - (10 * line.length());
                Cache.font.draw(batch, line, x, y);
                y -= 30;
            }
        }
    }
}
