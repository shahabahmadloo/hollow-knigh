package com.hollow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Cockroach extends GroundEnemy{

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> thrownToDeathAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Animation<TextureRegion> attackAnticipateAnimation;
    private Animation<TextureRegion> attackLungeAnimation;
    private Animation<TextureRegion> attackCooldownAnimation;
    private Animation<TextureRegion> fallAnimation;



    float behaviorTimer=0;
    float idleTime=5f;
    float walkTime=3f;
    float cooldownTime=0.5f;
    float FovRange;

    private CockroachType cockroachType;

    @Override
    public void turnAround() {
        if (isDead) return;


        if (currentState == State.LUNGING) {
            currentState = State.COOLDOWN;
            stateTimer = 0f;
            behaviorTimer = 0f;
            speed = 0f;

            bounds.x += toRight ? -15f : 15f;
        }
        else if (currentState != State.COOLDOWN && currentState != State.ANTICIPATING && currentState != State.TURNING) {
            previousState = currentState;
            currentState = State.TURNING;
            stateTimer = 0f;
            speed = 0f;
        }
    }

    public Cockroach(float startX, float startY, float width, float height,CockroachType cockroachType, CameraFunctions cameraFunctions) {
        super(startX, startY, width, height, cockroachType.hp, cockroachType.speed, cameraFunctions);
        this.cockroachType = cockroachType;

        currentState = State.WALKING;
        previousState = State.WALKING;

        hitbox = new Rectangle(startX + width * 2f / 8, startY, width   / 2, height * 5f / 8f);
        FovRange=width*3;
        Texture tmpSheet;
        int frameWidth;
        int frameHeight;
        TextureRegion[][] tmp;
        Array<TextureRegion> frames;
        switch(cockroachType){
            case Hornless:
                tmpSheet = new Texture("animation/Cockroach/Idle.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 7;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 7; i++) {
                    frames.add(tmp[0][i]);
                }
                idleAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Cockroach/Walk.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 7;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 7; i++) {
                    frames.add(tmp[0][i]);
                }
                walkAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Cockroach/Turn.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Cockroach/Death Air.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() ;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 1; i++) {
                    frames.add(tmp[0][i]);
                }
                thrownToDeathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);


                tmpSheet = new Texture("animation/Cockroach/Attack Cooldown.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() ;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 1; i++) {
                    frames.add(tmp[0][i]);
                }
                attackCooldownAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Cockroach/Death Land.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 8;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 8; i++) {
                    frames.add(tmp[0][i]);
                }
                deathAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Cockroach/Attack Lunge.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 8;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 8; i++) {
                    frames.add(tmp[0][i]);
                }
                attackLungeAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Cockroach/Attack Anticipate.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 5;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 5; i++) {
                    frames.add(tmp[0][i]);
                }
                attackAnticipateAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
                break;


            case Hornhead:
                tmpSheet = new Texture("animation/Husk_Hornhead/Idle.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 6;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 6; i++) {
                    frames.add(tmp[0][i]);
                }
                idleAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Husk_Hornhead/Walk.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 7;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 7; i++) {
                    frames.add(tmp[0][i]);
                }
                walkAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Husk_Hornhead/Turn.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Husk_Hornhead/Death Air.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() ;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 1; i++) {
                    frames.add(tmp[0][i]);
                }
                thrownToDeathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);


                tmpSheet = new Texture("animation/Husk_Hornhead/Attack Cooldown.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() ;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 1; i++) {
                    frames.add(tmp[0][i]);
                }
                attackCooldownAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Husk_Hornhead/Death Land.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 8;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 8; i++) {
                    frames.add(tmp[0][i]);
                }
                deathAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Husk_Hornhead/Attack Lunge.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 12;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 12; i++) {
                    frames.add(tmp[0][i]);
                }
                attackLungeAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Husk_Hornhead/Attack Anticipate.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 5;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 5; i++) {
                    frames.add(tmp[0][i]);
                }
                attackAnticipateAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
                break;
        }


    }



    @Override
    public void update(float delta, Array<SolidBlock> solidBlocks, Array<Effect> worldEffects,Player player) {
        previousState = currentState;
        stateTimer += delta;

        if(isOnGround) {
            if(currentState==State.DEAD) {
                speed=0f;
            }
            else if (currentState == State.LUNGING) {
                speed = cockroachType.lungeSpeed;
            } else if (currentState == State.IDLE) {
                speed = 0f;
            } else if (currentState != State.TURNING) {
                speed = super.baseSpeed;
                dmg = 1;
            }
        }

        if (!isDead && isOnGround && (currentState == State.WALKING || currentState == State.IDLE)) {
            float visionX = toRight ? hitbox.x + hitbox.width : hitbox.x - FovRange;
            Rectangle visionBox = new Rectangle(visionX, hitbox.y, FovRange, hitbox.height);

            if (visionBox.overlaps(player.hitbox)) {
                currentState = State.ANTICIPATING;
                stateTimer = 0f;
                speed = 0f;
                behaviorTimer = 0f;
            }
        }

        if (currentState == State.ANTICIPATING) {
            speed = 0f;
            if (attackAnticipateAnimation.isAnimationFinished(stateTimer)) {
                currentState = State.LUNGING;
                stateTimer = 0f;
                speed = cockroachType.lungeSpeed;
                dmg = cockroachType.lungeDamage;
            }
        }

        if (currentState == State.COOLDOWN) {
            speed = 0f;
            if (behaviorTimer >= cooldownTime) {
                currentState = State.TURNING;
                stateTimer = 0f;
                behaviorTimer = 0f;
            }
        }

        if (!isDead && currentState == State.TURNING) {
            speed = 0;
            dmg=1;
            if (turnAnimation.isAnimationFinished(stateTimer)) {
                toRight = !toRight;
                currentState = State.WALKING;
                speed = baseSpeed;
                stateTimer = 0f;
                behaviorTimer = 0f;
            }
        }

        if (!isDead && isOnGround && (currentState == State.WALKING || currentState == State.IDLE||currentState == State.COOLDOWN)) {
            behaviorTimer += delta;

            if (currentState == State.WALKING && behaviorTimer >= walkTime) {
                currentState = State.IDLE;
                stateTimer = 0f;
                behaviorTimer = 0f;
                speed = 0f;
            } else if (currentState == State.IDLE && behaviorTimer >= idleTime) {
                currentState = State.WALKING;
                stateTimer = 0f;
                behaviorTimer = 0f;
                speed = baseSpeed;
            }
        }

        boolean wasOnGround = isOnGround;

        applyGravityAndCollision(delta, solidBlocks);

        if (currentState == State.DEAD && !wasOnGround && isOnGround) {
            stateTimer = 0f;

        }



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
            case WALKING:
                currentFrame = walkAnimation.getKeyFrame(stateTimer);
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
                currentFrame = attackCooldownAnimation.getKeyFrame(stateTimer);
                break;
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
        hitbox.y = bounds.y+bounds.height  / 24;
    }
    @Override
    public void drawDebug(ShapeRenderer shape) {

        shape.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.RED);
        shape.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
        if(toRight) {
            shape.rect(hitbox.x+hitbox.width, hitbox.y, FovRange, hitbox.height);
        }else{
            shape.rect(hitbox.x-FovRange, hitbox.y, FovRange, hitbox.height);
        }
    }
}
