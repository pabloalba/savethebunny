package es.seastorm.merlin.gameobjects;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.Utils;
import es.seastorm.merlin.assets.GameAssets;

public class Animal extends SimpleGameObject {
    private int coordX = -1;
    private int coordY = -1;
    private float rotationVelocity = 0;
    public int numMoves = 2;
    public int id;
    TextureRegion shadowTexture;
    public boolean drawShadow = false;

    public Vector2 desiredPosition = new Vector2(-1000, -1000);

    public Animal(TextureRegion textureRegion, int id) {
        super(textureRegion);
        terminalVelocity.x = 200;
        terminalVelocity.y = 200;
        this.id = id;

        shadowTexture = GameAssets.instance.getTextureRegion(GameAssets.SHADOW);
    }

    public Animal(String fileFullName) {
        super(fileFullName);
    }


    public int getCoordX() {
        return coordX;
    }

    public int getCoordY() {
        return coordY;
    }

    public void labyrintCoords(int x, int y, boolean teleport) {
        this.coordX = x;
        this.coordY = y;

        desiredPosition = Utils.coordsToScreen(x, y);

        if (teleport) {
            this.position.x = desiredPosition.x;
            this.position.y = desiredPosition.y;
        }



    }

    public void labyrintCoords(float x, float y, boolean teleport) {
        labyrintCoords((int) Math.floor(x) - 1, (int) Math.floor(y) - 1, teleport);
    }


    public void rotate(float deltaTime) {
        if (desiredPosition.x == position.x && desiredPosition.y == position.y) {
            rotationVelocity = 0;
            rotation = 0;
        } else {
            if (rotationVelocity == 0) {
                rotationVelocity = 500;
            }
            if (rotation > 30) {
                rotation = 30;
                rotationVelocity *= -1;
            } else if (rotation < -30) {
                rotation = -30;
                rotationVelocity *= -1;
            }

            this.rotation += rotationVelocity * deltaTime;
        }

        this.flip = (this.desiredPosition.x > this.position.x);
    }


    public boolean sameCoordinates(Animal a) {
        return ((this.getCoordX() == a.getCoordX()) && (this.getCoordY() == a.getCoordY()));
    }

    @Override
    public void render(SpriteBatch batch) {
        if (this.visible) {
            if (this.drawShadow) {
                batch.draw(shadowTexture.getTexture(), this.position.x, this.position.y, this.origin.x, this.origin.y, this.dimension.x, this.dimension.y, this.scale.x, this.scale.y, 0, this.shadowTexture.getRegionX(), this.shadowTexture.getRegionY(), this.shadowTexture.getRegionWidth(), this.shadowTexture.getRegionHeight(), this.flip, false);
            }
            super.render(batch);
        }

    }


}
