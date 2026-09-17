package com.hollow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Zote extends GroundEnemy {

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> fallAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> getUpAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> talkAnimation;
    private Animation<TextureRegion> rollAnimation;

    public enum State {IDLE, TURNING, ROLLING, FALL, ANGRY_RUN, GETUP, DEAD}
    State currentState = State.IDLE;
    State previousState = State.IDLE;

    float angryTimer = 0f;
    float angryDuration = 5f;
    float rollSpeed = 500f;
    float angrySpeed = 300f;

    float behaviorTimer = 0;
    float idleTime = 5f;
    float walkTime = 3f;
    float FovRange;

    public Zote(float startX, float startY, float width, float height, CameraFunctions cameraFunctions) {
        super(startX, startY, width, height, 1000, 100, cameraFunctions);

        hitbox = new Rectangle(startX + width * 2f / 8, startY, width / 2, height * 5f / 8f);
        FovRange = width * 3;

        Texture tmpSheet;
        int frameWidth;
        int frameHeight;
        TextureRegion[][] tmp;
        Array<TextureRegion> frames;

        tmpSheet = new Texture("animation/Zote/Idle.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 5; i++) frames.add(tmp[0][i]);
        idleAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Zote/Attack.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 4;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 4; i++) frames.add(tmp[0][i]);
        attackAnimation = new Animation<>(0.08f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Zote/Turn.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 2;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 2; i++) frames.add(tmp[0][i]);
        turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Zote/Fall.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 5; i++) frames.add(tmp[0][i]);
        fallAnimation= new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Zote/Get Up.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 4;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 4; i++) frames.add(tmp[0][i]);
        getUpAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Zote/Roll.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 3;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) frames.add(tmp[0][i]);
        rollAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Zote/Talk.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 5; i++) frames.add(tmp[0][i]);
        talkAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
    }

    @Override
    public void turnAround() {
        if (isDead) return;

        if (currentState == State.ROLLING) {
            currentState = State.FALL;
            stateTimer = 0f;
            speed = 0f;
        } else if (currentState == State.ANGRY_RUN) {

        }
    }

    @Override
    public void takeDamage(int damage) {
        if (isDead || isInvincible) return;

        currentHealth -= damage;
        isInvincible = true;
        invincibleTimer = 0f;

        if (currentHealth <= 0) {
            currentHealth = 0;
            isDead = true;
            currentState = State.DEAD;
            stateTimer = 0f;
            speed = 0f;
            if (isOnGround) {
                velocityY = -5f;
            }
        } else {
            if (currentState == State.IDLE || currentState == State.TURNING || currentState == State.ANGRY_RUN) {
                currentState = State.ROLLING;
                stateTimer = 0f;
                velocityY = 0f;
                cameraFunctions.shakeCamera(5f, 0.2f);
            }
        }
    }

    @Override
    public void update(float delta, Array<SolidBlock> solidBlocks, Array<Effect> worldEffects, Player player) {
        previousState = currentState;
        stateTimer += delta;

        switch (currentState) {
            case IDLE:
                speed = 0f;
                boolean playerIsBehind = (toRight && player.hitbox.x < hitbox.x) || (!toRight && player.hitbox.x > hitbox.x);
                if (playerIsBehind) {
                    currentState = State.TURNING;
                    stateTimer = 0f;
                }
                break;

            case TURNING:
                speed = 0f;
                if (turnAnimation.isAnimationFinished(stateTimer)) {
                    toRight = !toRight;
                    currentState = State.IDLE;
                    stateTimer = 0f;
                }
                break;

            case ROLLING:
                speed = -rollSpeed;
                break;

            case FALL:
                speed = 0f;
                if (fallAnimation.isAnimationFinished(stateTimer)) {
                    currentState = State.GETUP;
                    stateTimer = 0f;
                }
                break;

            case GETUP:
                speed = 0f;
                if (getUpAnimation.isAnimationFinished(stateTimer)) {
                    currentState = State.ANGRY_RUN;
                    stateTimer = 0f;
                    angryTimer = 0f;
                    toRight = (player.hitbox.x > hitbox.x);
                }
                break;

            case ANGRY_RUN:
                angryTimer += delta;
                speed = angrySpeed;
                toRight = (player.hitbox.x > hitbox.x);

                if (angryTimer >= angryDuration) {
                    currentState = State.IDLE;
                    stateTimer = 0f;
                }
                break;

            case DEAD:
                speed = 0f;
                break;
        }

        boolean wasOnGround = isOnGround;

        applyGravityAndCollision(delta, solidBlocks);

        if (currentState == State.ROLLING && !isOnGround) {
            currentState = State.FALL;
            stateTimer = 0f;
            speed = 0f;
        }

        if (currentState == State.DEAD && !wasOnGround && isOnGround) {
            stateTimer = 0f;
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = null;
        switch (currentState) {
            case IDLE:
                currentFrame = idleAnimation.getKeyFrame(stateTimer);
                break;
            case ANGRY_RUN:
                currentFrame = attackAnimation.getKeyFrame(stateTimer);
                break;
            case TURNING:
                currentFrame = turnAnimation.getKeyFrame(stateTimer);
                break;
            case ROLLING:
                currentFrame = rollAnimation.getKeyFrame(stateTimer);
                break;
            case FALL:
                currentFrame = fallAnimation.getKeyFrame(stateTimer);
                break;
            case GETUP:
                currentFrame = getUpAnimation.getKeyFrame(stateTimer);
                break;
            case DEAD:
                currentFrame = fallAnimation.getKeyFrame(stateTimer, false);
                break;
            default:
                break;
        }

        if (currentFrame != null) {
            boolean drawToRight = toRight;
            if (currentState == State.ROLLING) {
                drawToRight = !toRight;
            }

            if (drawToRight) {
                batch.draw(currentFrame, bounds.x + bounds.width, bounds.y, -bounds.width, bounds.height);
            } else {
                batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
    }

    @Override
    public void updateHitBox() {
        hitbox.x = bounds.x + bounds.width * 2f / 8;
        hitbox.y = bounds.y + bounds.height / 24;
    }

    @Override
    public void drawDebug(ShapeRenderer shape) {
        shape.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.RED);
        shape.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        if(toRight) {
            shape.rect(hitbox.x + hitbox.width, hitbox.y, FovRange, hitbox.height);
        } else {
            shape.rect(hitbox.x - FovRange, hitbox.y, FovRange, hitbox.height);
        }
    }
}
