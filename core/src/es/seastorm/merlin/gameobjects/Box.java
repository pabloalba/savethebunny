package es.seastorm.merlin.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.assets.GameAssets;

public class Box extends SimpleGameObject {
    public static final TextureRegion squareRegion = GameAssets.instance.getTextureRegion(GameAssets.SQUARE);
    float counter;
    public boolean glowing = false;
    int glowLevel = 0;

    public static final TextureRegion[] glowRegion = {
            GameAssets.instance.getTextureRegion(GameAssets.SQUARE_GLOW1),
            GameAssets.instance.getTextureRegion(GameAssets.SQUARE_GLOW2),
            GameAssets.instance.getTextureRegion(GameAssets.SQUARE_GLOW3),
            GameAssets.instance.getTextureRegion(GameAssets.SQUARE_GLOW4),
            GameAssets.instance.getTextureRegion(GameAssets.SQUARE_GLOW5),
    };


    public Box(float positionX, float positionY) {
        super(squareRegion);
        this.reg = squareRegion;

        this.position.x = positionX;
        this.position.y = positionY;

    }

    public void touch() {
        this.glowing = true;
        this.glowLevel = 0;
        this.reg = glowRegion[glowLevel];
        this.counter = 0.06f;
    }


    public void update(float deltaTime) {
        if (glowing) {
            counter -= deltaTime;
            if (counter <= 0) {
                glowLevel++;
                if (glowLevel < 5) {
                    this.reg = glowRegion[glowLevel];
                    this.counter = 0.06f;
                } else {
                    this.reg = squareRegion;
                    this.counter = 0;
                    glowing = false;
                }
            }
        }
    }

}
