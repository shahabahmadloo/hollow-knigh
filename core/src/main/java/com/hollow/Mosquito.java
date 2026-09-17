package com.hollow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Mosquito extends FlyingEnemy{

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> thrownToDeathAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Animation<TextureRegion> attackAnticipateAnimation;
    private Animation<TextureRegion> attackLungeAnimation;

    float globalAttackCooldown = 0f;




    float behaviorTimer=0;
    float idleTime=5f;
    float walkTime=3f;
    float cooldownTime=1f;
    float FovRange;
    boolean seen=false;
    private float lungeDirX = 0f;
    private float lungeDirY = 0f;

    @Override
    public void turnAround() {
        if (isDead) return;



        if (currentState != State.ANTICIPATING && currentState != State.TURNING) {
            previousState = currentState;
            currentState = State.TURNING;
            stateTimer = 0f;
            speed = 0f;
        }
    }

    public Mosquito(float startX, float startY, float width, float height, CameraFunctions cameraFunctions) {
        super(startX, startY, width, height, 5, 200f, cameraFunctions);

        currentState = State.JUMPING;
        previousState = State.JUMPING;

        hitbox = new Rectangle(startX + width * 2f / 8, startY, width   / 2, height * 5f / 8f);
        FovRange=width;

        Texture tmpSheet = new Texture("animation/Mosquito/Idle.png");
        loadedTextures.add(tmpSheet);
        int frameWidth = tmpSheet.getWidth() / 8;
        int frameHeight = tmpSheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 8; i++) {
            frames.add(tmp[0][i]);
        }
        idleAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);


        tmpSheet = new Texture("animation/Mosquito/Turn.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 2;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 2; i++) {
            frames.add(tmp[0][i]);
        }
        turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Mosquito/Death Air.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/3 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(tmp[0][i]);
        }
        thrownToDeathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);



        tmpSheet = new Texture("animation/Mosquito/Death Land.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() /2;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 2; i++) {
            frames.add(tmp[0][i]);
        }
        deathAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Mosquito/Attack.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 3;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(tmp[0][i]);
        }
        attackLungeAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Mosquito/Attack Anticipate.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 6; i++) {
            frames.add(tmp[0][i]);
        }
        attackAnticipateAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
    }



    @Override
    public void update(float delta, Array<SolidBlock> solidBlocks, Array<Effect> worldEffects, Player player) {
        previousState = currentState;
        stateTimer += delta;

        if (isDead) {
            if (speed > 0) {
                speed -= 800f * delta;
                if (speed < 0) speed = 0f;
            } else if (speed < 0) {
                speed += 800f * delta;
                if (speed > 0) speed = 0f;
            }
        }else if (isInvincible) {

            bounds.x += speed * delta;
            bounds.y -= velocityY;
            velocityY += 0.4f;


            if (speed > 0) {
                speed -= 800f * delta;
                if (speed < 0) speed = 0f;
            } else if (speed < 0) {
                speed += 800f * delta;
                if (speed > 0) speed = 0f;
            }
        }

        else {

            if (globalAttackCooldown > 0) {
                globalAttackCooldown -= delta;
            }

            if (currentState == State.IDLE) {
                if (speed > 0) {
                    speed -= 800f * delta;
                    if (speed < 0) speed = 0f;
                } else if (speed < 0) {
                    speed += 800f * delta;
                    if (speed > 0) speed = 0f;
                }
                velocityY = 0f;
            } else if (currentState != State.LUNGING) {
                velocityY = 0f;
            }

            if (!seen && (currentState == State.JUMPING || currentState == State.IDLE)) {
                float centerX = hitbox.x + hitbox.width / 2;
                float centerY = hitbox.y + hitbox.height / 2;
                Circle visionBox = new Circle(centerX, centerY, FovRange);

                if (com.badlogic.gdx.math.Intersector.overlaps(visionBox, player.hitbox)) {
                    seen = true;
                }
            }

            if (seen && currentState != State.LUNGING && currentState != State.ANTICIPATING && currentState != State.COOLDOWN) {
                float dx = (player.hitbox.x + player.hitbox.width / 2) - (hitbox.x + hitbox.width / 2);
                float dy = (player.hitbox.y + player.hitbox.height / 2) - (hitbox.y + hitbox.height / 2);
                float distance = (float) Math.sqrt((dx * dx) + (dy * dy));

                if (distance <= FovRange && globalAttackCooldown <= 0) {
                    currentState = State.ANTICIPATING;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                    speed = 0f;
                    velocityY = 0f;
                }
                else if (distance > 0) {
                    toRight = (dx > 0);
                    bounds.x += (dx / distance) * baseSpeed * delta;
                    bounds.y += (dy / distance) * baseSpeed * delta;
                }
            }

            if (currentState == State.ANTICIPATING) {
                speed = 0f;
                velocityY = 0f;
                if (attackAnticipateAnimation.isAnimationFinished(stateTimer)) {
                    currentState = State.LUNGING;
                    stateTimer = 0f;
                    behaviorTimer = 0f;

                    float dx = (player.hitbox.x + player.hitbox.width / 2) - (hitbox.x + hitbox.width / 2);
                    float dy = (player.hitbox.y + player.hitbox.height / 2) - (hitbox.y + hitbox.height / 2);
                    float distance = (float) Math.sqrt((dx * dx) + (dy * dy));

                    if (distance > 0) {
                        lungeDirX = dx / distance;
                        lungeDirY = dy / distance;
                        toRight = (dx > 0);
                    } else {
                        lungeDirX = toRight ? 1f : -1f;
                        lungeDirY = 0f;
                    }
                }
            }

            if (currentState == State.LUNGING) {
                behaviorTimer += delta;

                speed = Math.abs(lungeDirX * 1200f);
                toRight = (lungeDirX > 0);
                velocityY = -lungeDirY * 1200f * delta;

                if (behaviorTimer >= 0.5f) {
                    currentState = State.COOLDOWN;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                    speed = 0f;
                    velocityY = 0f;

                    globalAttackCooldown = 10f;
                }
            }

            if (currentState == State.COOLDOWN) {
                speed = 0f;
                velocityY = 0f;
                behaviorTimer += delta;
                if (behaviorTimer >= cooldownTime) {
                    currentState = State.JUMPING;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
            }

            if (currentState == State.TURNING) {
                speed = 0f;
                if (turnAnimation.isAnimationFinished(stateTimer)) {
                    toRight = !toRight;
                    currentState = State.JUMPING;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
            }

            if (!seen && (currentState == State.JUMPING || currentState == State.IDLE)) {
                behaviorTimer += delta;
                if (currentState == State.JUMPING) speed = baseSpeed;

                if (currentState == State.JUMPING && behaviorTimer >= walkTime) {
                    currentState = State.IDLE;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                } else if (currentState == State.IDLE && behaviorTimer >= idleTime) {
                    currentState = State.JUMPING;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
            }
        }

        applyCollision(delta, solidBlocks);
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame=null;
        switch (currentState) {
            case DEAD:
                if (!isOnGround) {
                    currentFrame = thrownToDeathAnimation.getKeyFrame(stateTimer);
                } else {
                    currentFrame = deathAnimation.getKeyFrame(stateTimer);
                }
                break;
            case IDLE:
                currentFrame = idleAnimation.getKeyFrame(stateTimer);
                break;
            case JUMPING:
                currentFrame = idleAnimation.getKeyFrame(stateTimer);
                break;
            case TURNING:
                currentFrame = turnAnimation.getKeyFrame(stateTimer);
                break;
            case ANTICIPATING:
                currentFrame = attackAnticipateAnimation.getKeyFrame(stateTimer);
                break;
            case LUNGING:
                currentFrame = attackLungeAnimation.getKeyFrame(stateTimer);
                break;
            case COOLDOWN:
                currentFrame = idleAnimation.getKeyFrame(stateTimer);

            default:
                break;
        }

        if (currentFrame != null) {
            if (toRight) {
                batch.draw(currentFrame, bounds.x + bounds.width, bounds.y, -bounds.width, bounds.height);
            } else {
                batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }

    }

    @Override
    public void updateHitBox() {
        hitbox.x = bounds.x + bounds.width * 2f / 8;
        hitbox.y = bounds.y+ hitbox.height/3f;
    }
    @Override
    public void drawDebug(ShapeRenderer shape) {

        shape.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.RED);
        shape.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.YELLOW);

        shape.circle(hitbox.x+hitbox.width/2, hitbox.y+ hitbox.height/2, FovRange);

    }
}
