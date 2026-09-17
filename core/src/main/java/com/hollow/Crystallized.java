package com.hollow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Crystallized extends GroundEnemy{

    private Texture laserSheet;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> thrownToDeathAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Animation<TextureRegion> attackAnticipateAnimation;
    private Animation<TextureRegion> attackLungeAnimation;
    private Animation<TextureRegion> attackCooldownAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> laserAnticipateAnimation;
    private Animation<TextureRegion> laserCooldownAnimation;
    private Animation<TextureRegion> laserAnimation;
    private Animation<TextureRegion> laserCircleAnticipateAnimation;
    private Animation<TextureRegion> laserCircleCooldownAnimation;
    private Animation<TextureRegion> laserCircleAnimation;


    float behaviorTimer=0;
    float cooldownTime=0.5f;
    float laserBeamTime=3f;
    float FovRange;
    boolean feelDanger;
    float dangerTime=3f;

    float laserRange,laserHeight,laserX,laserY;

    @Override
    public void turnAround() {
        if (isDead) return;

        else if (currentState != State.COOLDOWN && currentState != State.ANTICIPATING && currentState != State.TURNING) {
            previousState = currentState;
            currentState = State.TURNING;
            stateTimer = 0f;
            speed = 0f;
        }
    }

    public Crystallized(float startX, float startY, float width, float height, CameraFunctions cameraFunctions) {
        super(startX, startY, width, height, 5, 300f, cameraFunctions);

        currentState = State.IDLE;
        previousState = State.IDLE;

        hitbox = new Rectangle(startX + width * 2f / 8, startY, width   / 2, height * 5f / 8f);
        FovRange=width*3;

        Texture tmpSheet = new Texture("animation/Crystallized/Idle.png");
        loadedTextures.add(tmpSheet);
        int frameWidth = tmpSheet.getWidth() / 5;
        int frameHeight = tmpSheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 5; i++) {
            frames.add(tmp[0][i]);
        }
        idleAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Crystallized/Run.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 6; i++) {
            frames.add(tmp[0][i]);
        }
        walkAnimation = new Animation<>(0.05f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Crystallized/Turn.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 3;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(tmp[0][i]);
        }
        turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
        tmpSheet = new Texture("animation/Crystallized/Evade.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 7;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 7; i++) {
            frames.add(tmp[0][i]);
        }
        jumpAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Crystallized/Death Air.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/3 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(tmp[0][i]);
        }
        thrownToDeathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);


        tmpSheet = new Texture("animation/Crystallized/Shoot.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/7 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);

        frames = new Array<>();
        for (int i = 0; i < 2; i++) {
            frames.add(tmp[0][i]);
        }
        attackAnticipateAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
        frames = new Array<>();
        for (int i = 2; i < 4; i++) {
            frames.add(tmp[0][i]);
        }
        attackLungeAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        frames = new Array<>();
        for (int i = 3; i < 6; i++) {
            frames.add(tmp[0][i]);
        }
        attackCooldownAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);


        tmpSheet = new Texture("animation/Crystallized/Death Land.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 3;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(tmp[0][i]);
        }
        deathAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        laserSheet = new Texture("animation/Effects/CrystalLaser.png");
        loadedTextures.add(laserSheet);

        frameWidth = laserSheet.getWidth() / 15;
        frameHeight = laserSheet.getHeight();

        tmp = TextureRegion.split(laserSheet, frameWidth, frameHeight);
        Array<TextureRegion> laserFrames = new Array<>();

        for (int i = 0; i < 4; i++) {
            laserFrames.add(tmp[0][i]);
        }
        laserAnticipateAnimation = new Animation<>(0.1f, laserFrames, Animation.PlayMode.NORMAL);

        laserFrames = new Array<>();
        for(int i=4;i<11;i++){
            laserFrames.add(tmp[0][i]);
        }
        laserAnimation = new Animation<>(0.1f, laserFrames, Animation.PlayMode.LOOP);

        laserFrames = new Array<>();
        for(int i=11;i<15;i++){
            laserFrames.add(tmp[0][i]);
        }
        laserCooldownAnimation = new Animation<>(0.1f, laserFrames, Animation.PlayMode.NORMAL);

        laserSheet = new Texture("animation/Effects/LaserCircle.png");
        loadedTextures.add(laserSheet);

        frameWidth = laserSheet.getWidth() / 11;
        frameHeight = laserSheet.getHeight();

        tmp = TextureRegion.split(laserSheet, frameWidth, frameHeight);
        laserFrames = new Array<>();

        for (int i = 0; i < 4; i++) {
            laserFrames.add(tmp[0][i]);
        }
        laserCircleAnticipateAnimation = new Animation<>(0.1f, laserFrames, Animation.PlayMode.NORMAL);

        laserFrames = new Array<>();
        for(int i=4;i<7;i++){
            laserFrames.add(tmp[0][i]);
        }
        laserCircleAnimation = new Animation<>(0.1f, laserFrames, Animation.PlayMode.LOOP);

        laserFrames = new Array<>();
        for(int i=7;i<11;i++){
            laserFrames.add(tmp[0][i]);
        }
        laserCircleCooldownAnimation = new Animation<>(0.1f, laserFrames, Animation.PlayMode.NORMAL);

    }

    @Override
    public void update(float delta, Array<SolidBlock> solidBlocks, Array<Effect> worldEffects, Player player) {
        previousState = currentState;
        stateTimer += delta;
        if(isOnGround&&currentState!=State.JUMPING) {
            if (isDead || currentState == State.IDLE || currentState == State.LUNGING || currentState == State.COOLDOWN || currentState == State.ANTICIPATING || currentState == State.TURNING) {
                speed = 0;
            } else {
                speed = super.baseSpeed;
            }
        }else if (currentState==State.JUMPING){
            speed = -super.baseSpeed;
        }

        if (!isDead && isOnGround && (currentState == State.WALKING || currentState == State.IDLE)) {
            float visionX = toRight ? hitbox.x + hitbox.width : hitbox.x - FovRange;
            Rectangle visionBox = new Rectangle(visionX, hitbox.y, FovRange, hitbox.height);

            if (visionBox.overlaps(player.hitbox)) {
                feelDanger = true;
                currentState = State.JUMPING;
                stateTimer = 0f;
                behaviorTimer = 0f;
                velocityY = -4f;
            }
        }

        if (currentState == State.JUMPING) {
            if (jumpAnimation.isAnimationFinished(stateTimer)) {
                currentState = State.ANTICIPATING;
                behaviorTimer = 0f;
                stateTimer = 0f;
            }
        }

        if (currentState == State.ANTICIPATING) {
            speed = 0f;
            if (attackAnticipateAnimation.isAnimationFinished(stateTimer)) {
                currentState = State.LUNGING;
                behaviorTimer = 0f;
                stateTimer = 0f;
            }
        }

        if (currentState == State.LUNGING && behaviorTimer >= laserBeamTime) {
            currentState = State.COOLDOWN;
            stateTimer = 0f;
            behaviorTimer = 0f;
            speed = 0f;
        }

        if (currentState == State.LUNGING) {
            float anticipateDur = laserAnticipateAnimation.getAnimationDuration();
            float cooldownDur = laserCooldownAnimation.getAnimationDuration();

            if (behaviorTimer >= anticipateDur && behaviorTimer < laserBeamTime - cooldownDur) {

                float startY = bounds.y + (hitbox.height * 8f / 15);
                float segmentWidth = 100f;
                float segmentHeight = 50f;
                int segmentsCount = 30;
                float startX = toRight ? hitbox.x + hitbox.width : hitbox.x;

                laserRange = segmentWidth * segmentsCount;
                laserHeight = segmentHeight / 3;
                laserY = startY + segmentHeight / 3;
                laserX = toRight ? startX : startX - laserRange;

                Rectangle laserHitbox = new Rectangle(laserX, laserY, laserRange, laserHeight);

                if (laserHitbox.overlaps(player.hitbox)) {
                    player.takeDamage(1);
                }
            }
        }

        if (currentState == State.COOLDOWN) {
            speed = 0f;
            if (behaviorTimer >= cooldownTime) {
                currentState = State.IDLE;
                stateTimer = 0f;
                behaviorTimer = 0f;
            }
        }

        if (feelDanger && currentState == State.IDLE) {
            currentState = State.WALKING;
            behaviorTimer = 0f;
        }

        if (currentState == State.WALKING && feelDanger && behaviorTimer >= dangerTime) {
            feelDanger = false;
            currentState = State.IDLE;
            behaviorTimer = 0f;
        }

        if (feelDanger && (currentState == State.WALKING || currentState == State.IDLE)) {
            boolean isPlayerOnLeft = (player.hitbox.x + player.hitbox.width / 2) < (hitbox.x + hitbox.width / 2);

            if ((toRight && isPlayerOnLeft) || (!toRight && !isPlayerOnLeft)) {
                turnAround();
            }
        }

        if (!isDead && currentState == State.TURNING) {
            speed = 0;
            if (turnAnimation.isAnimationFinished(stateTimer)) {
                toRight = !toRight;
                currentState = feelDanger ? State.WALKING : State.IDLE;
                speed = baseSpeed;
                stateTimer = 0f;
            }
        }

        if (!isDead && isOnGround) {
            behaviorTimer += delta;
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
            case JUMPING:
                currentFrame = jumpAnimation.getKeyFrame(stateTimer);
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

        if (currentState == State.LUNGING) {
            TextureRegion currentLaserFrame;
            TextureRegion currentLaserCircleFrame;
            float anticipateDur = laserAnticipateAnimation.getAnimationDuration();
            float cooldownDur = laserCooldownAnimation.getAnimationDuration();

            if (behaviorTimer < anticipateDur) {
                currentLaserFrame = laserAnticipateAnimation.getKeyFrame(behaviorTimer);
                currentLaserCircleFrame = laserCircleAnticipateAnimation.getKeyFrame(behaviorTimer);
            } else if (behaviorTimer < laserBeamTime - cooldownDur) {
                currentLaserFrame = laserAnimation.getKeyFrame(behaviorTimer - anticipateDur);
                currentLaserCircleFrame = laserCircleAnimation.getKeyFrame(behaviorTimer - anticipateDur);
            } else {
                currentLaserFrame = laserCooldownAnimation.getKeyFrame(behaviorTimer - (laserBeamTime - cooldownDur));
                currentLaserCircleFrame = laserCircleCooldownAnimation.getKeyFrame(behaviorTimer - (laserBeamTime - cooldownDur));
            }

            float startY = bounds.y + (hitbox.height * 8f / 15);
            float segmentWidth = 100f;
            float segmentHeight = 50f;
            int segmentsCount = 30;

            float startX = toRight ? hitbox.x + hitbox.width : hitbox.x;

            laserRange = segmentWidth * segmentsCount;
            laserHeight = segmentHeight/3;
            laserY = startY+segmentHeight/3;
            laserX = toRight ? startX : startX - laserRange;

            for (int i = 0; i < segmentsCount; i++) {
                float currentX = toRight ? startX + (i * segmentWidth) : startX - ((i + 1) * segmentWidth);
                boolean flip = (i % 2 != 0);

                if (toRight) {
                    if (!flip) {
                        batch.draw(currentLaserFrame, currentX, startY, segmentWidth, segmentHeight);
                    } else {
                        batch.draw(currentLaserFrame, currentX + segmentWidth, startY, -segmentWidth, segmentHeight);
                    }
                } else {
                    if (!flip) {
                        batch.draw(currentLaserFrame, currentX + segmentWidth, startY, -segmentWidth, segmentHeight);
                    } else {
                        batch.draw(currentLaserFrame, currentX, startY, segmentWidth, segmentHeight);
                    }
                }
            }

            float circleOffset = 60f;
            float circleStartX = toRight ? startX - circleOffset : startX + circleOffset;

            if (toRight) {
                batch.draw(currentLaserCircleFrame, circleStartX, startY, segmentWidth, segmentHeight);
            } else {
                batch.draw(currentLaserCircleFrame, circleStartX, startY, -segmentWidth, segmentHeight);
            }
        }
    }

    @Override
    public void updateHitBox() {
        hitbox.x = bounds.x + bounds.width * 2f / 8;
        hitbox.y = bounds.y;
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

        if (currentState == State.LUNGING) {
            shape.setColor(com.badlogic.gdx.graphics.Color.PINK);
            shape.rect(laserX, laserY, laserRange, laserHeight);
        }
    }
}
