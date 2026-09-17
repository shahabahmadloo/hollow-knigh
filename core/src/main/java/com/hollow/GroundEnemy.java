package com.hollow;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public abstract class GroundEnemy extends Enemy {

    public float baseSpeed;

    public GroundEnemy(float startX, float startY, float width, float height, int maxHealth, float speed, CameraFunctions cameraFunctions) {
        super(startX, startY, width, height, maxHealth, cameraFunctions);
        this.speed = speed;
        baseSpeed=speed;

    }

    abstract public void turnAround();

    protected void applyGravityAndCollision(float delta, Array<SolidBlock> solidBlocks) {
        float tmpX = bounds.x;

        if (toRight) {
            bounds.x += speed * delta;
        } else {
            bounds.x -= speed * delta;
        }

        if (isInvincible) {
            invincibleTimer += delta;
            if (invincibleTimer >= invincibilityDuration) {
                isInvincible = false;
                invincibleTimer = 0f;
            }
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

        float tmpY = bounds.y;
        bounds.y -= velocityY;
        velocityY += 0.2f;
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

        if (isOnGround && Math.abs(speed) > 0) {
            float edgeCheckX = toRight ? hitbox.x + hitbox.width + 2 : hitbox.x - 2;
            Rectangle edgeSensor = new Rectangle(edgeCheckX, hitbox.y - 4, 2, 2);
            boolean hasGroundAhead = false;

            for (SolidBlock blocks : solidBlocks) {
                block=blocks.bounds;
                if (edgeSensor.overlaps(block)) {
                    hasGroundAhead = true;
                    break;
                }
            }

            if (!hasGroundAhead && !hitWall) {
                turnAround();
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
            if (isOnGround) {
                velocityY = -0.5f;
            }
        }
    }
}
