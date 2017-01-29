package es.seastorm.merlin.screens.story.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.cache.Cache;

public class PhaseItem extends SimpleGameObject {
    public int phaseNum;
    String title;
    String subtitle;
    float titleX, titleY, subtitleX, subtitleY;


    public PhaseItem(TextureRegion textureRegion, int phaseNum, String title, String subtitle, float positionX, float positionY) {
        super(textureRegion);
        this.phaseNum = phaseNum;
        this.position.x = positionX;
        this.position.y = positionY;
        this.title = title;
        this.subtitle = subtitle;
        this.titleX = this.position.x + (this.dimension.x / 2) - (title.length() * 10);
        this.titleY = this.position.y + this.dimension.y - 10;

        this.subtitleX = this.position.x + (this.dimension.x / 2) - (subtitle.length() * 10);
        this.subtitleY = this.position.y + 50;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        if (this.visible) {
            Cache.font.draw(batch, title, titleX, titleY);
            Cache.font.draw(batch, subtitle, subtitleX, subtitleY);
        }
    }
}
