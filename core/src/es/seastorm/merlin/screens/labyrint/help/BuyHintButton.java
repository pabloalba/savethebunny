package es.seastorm.merlin.screens.labyrint.help;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dragongames.base.gameobject.SimpleGameObject;
import es.seastorm.merlin.MerlinGame;
import es.seastorm.merlin.assets.GameAssets;
import es.seastorm.merlin.cache.Cache;

public class BuyHintButton extends SimpleGameObject {
    public static final TextureRegion disabledRegion = GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_BUY_HINT_DISABLED);
    public static final TextureRegion enabledRegion = GameAssets.instance.getTextureRegion(GameAssets.ASSET_BTN_BUY_HINT);

    public BuyHintButton() {
        super(enabledRegion);
        this.reg = enabledRegion;
    }

    public void enable(boolean e) {
        if (e) {
            this.reg = enabledRegion;
        } else {
            this.reg = disabledRegion;
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (this.visible) {
            Cache.font.draw(batch, MerlinGame.textBundle.get("BUY_HINT_BTN"), this.position.x + 40, this.position.y + 50);
        }
    }
}
