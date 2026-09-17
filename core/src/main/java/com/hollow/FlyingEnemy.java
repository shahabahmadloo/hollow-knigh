package com.hollow;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public abstract class FlyingEnemy extends Enemy {

    public float speed;
    public float baseSpeed;

    public FlyingEnemy(float startX, float startY, float width, float height, int maxHealth, float speed, CameraFunctions cameraFunctions) {
        super(startX, startY, width, height, maxHealth, cameraFunctions);
        this.speed = speed;
        this.baseSpeed = speed;
    }

    abstract public void turnAround();

    protected void applyCollision(float delta, Array<SolidBlock> solidBlocks) {
        if (isInvincible) {
            invincibleTimer += delta;
            if (invincibleTimer >= invincibilityDuration) {
                isInvincible = false;
                invincibleTimer = 0f;
            }
        }

        float tmpX = bounds.x;

        if (toRight) {
            bounds.x += speed * delta;
        } else {
            bounds.x -= speed * delta;
        }

        updateHitBox();

        boolean hitWall = false;
        Rectangle block;
        for (SolidBlock blocks : solidBlocks) {
            block=blocks.bounds;
            if (hitbox.overlaps(block)) {
                bounds.x = tmpX;
                updateHitBox();
                hitWall = true;
                turnAround();
                break;
            }
        }

        if (isDead && !isOnGround) {
            velocityY += 0.2f;
        }

        float tmpY = bounds.y;
        bounds.y -= velocityY;
        updateHitBox();

        for (SolidBlock blocks : solidBlocks) {
            block=blocks.bounds;
            if (hitbox.overlaps(block)) {
                if (velocityY > 0) {
                    bounds.y = tmpY;
                } else if (velocityY < 0) {
                    bounds.y = tmpY;
                }
                velocityY = 0;
                updateHitBox();
                break;
            }
        }

        isOnGround = false;
        Rectangle feetSensor = new Rectangle(hitbox.x + 2, hitbox.y - 2, hitbox.width - 4, 2);

        for (SolidBlock blocks : solidBlocks) {
            block=blocks.bounds;
            if (feetSensor.overlaps(block)) {
                isOnGround = true;
                break;
            }
        }

    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);

        if (isDead && currentState != State.DEAD) {
            currentState = State.DEAD;
            stateTimer = 0f;
            speed = 0f;

            velocityY = -2f;
        }
    }
}
