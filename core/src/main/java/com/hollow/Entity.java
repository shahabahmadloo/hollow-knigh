package com.hollow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public abstract class Entity {
    public Rectangle bounds;
    public Rectangle hitbox;
    public float velocityY = 0;
    public boolean toRight = true;
    public boolean isOnGround = false;
    public float stateTimer = 0f;
    public int maxHealth;
    public int currentHealth;
    public boolean isDead;
    public float speed;
    public boolean isInvincible = false;
    public float invincibleTimer = 0f;
    public float invincibilityDuration = 1.5f;
    public CameraFunctions cameraFunctions;


    protected Array<Texture> loadedTextures = new Array<>();

    public Entity(float startX, float startY, float width, float height, CameraFunctions cameraFunctions) {
        bounds = new Rectangle(startX, startY, width, height);
        this.cameraFunctions = cameraFunctions;

        hitbox = new Rectangle(startX, startY, width, height);
    }


//    public abstract void update(float delta, Array<Rectangle> solidBlocks, Array<Effect> worldEffects,Array<Enemy> enemies);
    public abstract void draw(SpriteBatch batch);
    public abstract void updateHitBox();


    public void drawDebug(ShapeRenderer shape) {
        shape.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.RED);
        shape.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
    }

    public void dispose() {
        for (Texture texture : loadedTextures) {
            texture.dispose();
        }
        loadedTextures.clear();
    }
    public void takeDamage(int damage) {
        if (isDead) return;
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            isDead = true;
        }
        isInvincible=true;
        invincibleTimer=0;

    }
}
