package com.hollow;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public abstract class Projectile {
//    public Animation<TextureRegion> removeEffectAnimation;
    public Rectangle hitbox;
    public Rectangle bounds;
    public float speed;
    public int damage;
    public boolean isDestroyed = false;
    public boolean isEnemy = false;
    public boolean toRight;
    public boolean pierce;


    public abstract void update(float delta, Array<SolidBlock> solidBlocks);
    public abstract void draw(SpriteBatch batch);
    public abstract void dispose();

    public void drawDebug(ShapeRenderer shape) {
        shape.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.RED);
        shape.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }
//    public Animation<TextureRegion> getRemoveEffectAnimation() {
//        return removeEffectAnimation;
//    }
}
