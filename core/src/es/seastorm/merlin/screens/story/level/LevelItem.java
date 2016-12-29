package es.seastorm.merlin.screens.story.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.cache.Cache;
import es.seastorm.merlin.gameobjects.Animal;

public class LevelItem extends SimpleGameObject {
    public static final TextureRegion lockRegion = GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_LOCK);
    public static final TextureRegion[] levelRegion = {
            GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_LEVEL0),
            GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_LEVEL1),
            GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_LEVEL2),
            GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_LEVEL3)
    };

    public int levelNum;
    String text;
    float textX, textY;

    boolean lock;
    int stars;


    public LevelItem(boolean lock, int levelNum, String text, float positionX, float positionY) {
        super(lockRegion);
        this.reg = lockRegion;

        this.position.x = positionX;
        this.position.y = positionY;


        setInfo(levelNum, text, stars, lock);

    }

    @Override
    public void render(SpriteBatch batch) {
        if (this.visible) {
            super.render(batch);
            if (levelNum > -1) {
                Cache.font.draw(batch, text, textX, textY);
            }
        }
    }

    public void setInfo(int levelNum, String text, int stars, boolean lock) {
        this.levelNum = levelNum;
        this.text = text;
        this.textX = this.position.x + (this.dimension.x / 2) - (text.length() * 10);
        this.textY = this.position.y + (this.dimension.y * 2 / 3) + 5;
        this.stars = stars;
        this.lock = lock;
        if (lock) {
            this.reg = lockRegion;
        } else {
            this.reg = levelRegion[stars];
        }
    }
}
