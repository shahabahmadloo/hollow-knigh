package com.hollow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class False_knight extends GroundEnemy{

    enum State { IDLE, RUNNING, TURNING, ANTIC_RUN,ANTIC_ATTACK,ATTACK,COOLDOWN_ATTACK, COOLDOWN_RUN ,ANTIC_JUMP, JUMPING, LANDING,AIR_SMASH,STUN_FALL,STUN_LAND,STUN_IDLE,STUN_HIT,STUN_RECOVER}
    State currentState ;
    State previousState  ;


    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> runStartAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> stunFallStartAnimation;
    private Animation<TextureRegion> stunFallAnimation;
    private Animation<TextureRegion> stunIdleAnimation;
    private Animation<TextureRegion> stunRecoverAnimation;
    private Animation<TextureRegion> stunLandAnimation;
    private Animation<TextureRegion> stunHitAnimation;
    private Animation<TextureRegion> stunAnimation;
    private Animation<TextureRegion> attackAnticipateAnimation;
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> attackCooldownAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> jumpAnticipationAnimation;
    private Animation<TextureRegion> landAnimation;
    private Animation<TextureRegion> jumpAttackAnimation;





    float behaviorTimer=0;
    java.util.Random rand = new java.util.Random();
    int lastMove = -1;
    float idleTime=5f;
    float runTime=3f;
    boolean seen=false;
    boolean phase2=false;
    float stunTime=6f;
    boolean stunned=false;
    int hitLimit=0;
    float hitLimitDuration=3f;
    float hitLimitTimer=3f;
    public Rectangle attackHitbox = new Rectangle(0, 0, 0, 0);
    float cooldownTime=0.5f;
    float FovRange;
    int distanceLevel=0;
    boolean isJumpDefensive=false;
    boolean jumpedForSmash=false;
    boolean airSmashed=false;
    Array<Projectile> activeProjectiles;

    @Override
    public void turnAround() {
        if (isDead) return;

        if (currentState == State.IDLE || currentState == State.RUNNING || currentState == State.COOLDOWN_RUN) {
            currentState = State.TURNING;
            stateTimer = 0f;
            speed = 0f;
        }
    }

    public False_knight(float startX, float startY, float width, float height, CameraFunctions cameraFunctions, Array<Projectile> activeProjectiles) {
        super(startX, startY, width, height, 40, 100f, cameraFunctions);
        this.cameraFunctions = cameraFunctions;

        currentState = State.IDLE;
        previousState = State.IDLE;

        this.activeProjectiles = activeProjectiles;

        hitbox = new Rectangle(startX + width * 2f / 4, startY, width   / 6, height * 3f / 8f);
        FovRange=width/5;

        Texture tmpSheet = new Texture("animation/False_knight/Idle.png");
        loadedTextures.add(tmpSheet);
        int frameWidth = tmpSheet.getWidth() / 5;
        int frameHeight = tmpSheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 5; i++) {
            frames.add(tmp[0][i]);
        }
        idleAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/False_knight/Run.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);

        Array<TextureRegion> startFrames = new Array<>();
        startFrames.add(tmp[0][0]);
        runStartAnimation = new Animation<>(0.15f, startFrames, Animation.PlayMode.NORMAL);

        Array<TextureRegion> loopFrames = new Array<>();
        for (int i = 1; i < 5; i++) {
            loopFrames.add(tmp[0][i]);
        }
        runAnimation = new Animation<>(0.15f, loopFrames, Animation.PlayMode.LOOP);
        tmpSheet = new Texture("animation/False_knight/Turn.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 2;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 2; i++) {
            frames.add(tmp[0][i]);
        }
        turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/False_knight/DeathFall.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/3 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 1; i++) {
            frames.add(tmp[0][i]);
        }
        stunFallStartAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
        frames = new Array<>();
        for (int i=1;i<3;i++){
            frames.add(tmp[0][i]);
        }
        stunFallAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        tmpSheet = new Texture("animation/False_knight/DeathLand.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/11 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 11; i++) {
            frames.add(tmp[0][i]);
        }
        stunLandAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        tmpSheet = new Texture("animation/False_knight/Stun Recover.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/6 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 6; i++) {
            frames.add(tmp[0][i]);
        }
        stunRecoverAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
        tmpSheet = new Texture("animation/False_knight/Body.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/5 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 5; i++) {
            frames.add(tmp[0][i]);
        }
        stunIdleAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);
        tmpSheet = new Texture("animation/False_knight/DeathHit.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/3 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(tmp[0][i]);
        }
        stunHitAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/False_knight/Attack Recover.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/5 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 5; i++) {
            frames.add(tmp[0][i]);
        }
        attackCooldownAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);
//
        tmpSheet = new Texture("animation/False_knight/Body.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i <1; i++) {
            frames.add(tmp[0][i]);
        }
        stunAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
//
        tmpSheet = new Texture("animation/False_knight/Attack.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 3;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(tmp[0][i]);
        }
        attackAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/False_knight/Attack Antic.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 6; i++) {
            frames.add(tmp[0][i]);
        }
        attackAnticipateAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/False_knight/Jump Antic.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 3;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 3; i++) {
            frames.add(tmp[0][i]);
        }
        jumpAnticipationAnimation = new Animation<>(0.2f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/False_knight/Jump.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 4;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 4; i++) {
            frames.add(tmp[0][i]);
        }
        jumpAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/False_knight/Land.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 5; i++) {
            frames.add(tmp[0][i]);
        }
        landAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/False_knight/Jump Attack.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 8;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 8; i++) {
            frames.add(tmp[0][i]);
        }
        jumpAttackAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);



    }



    @Override
    public void update(float delta, Array<SolidBlock> solidBlocks, Array<Effect> worldEffects, Player player) {
        setDistanceLevel(player.hitbox.x+player.hitbox.width/2,player.hitbox.y+player.hitbox.height/2,hitbox.x+hitbox.width/2,hitbox.y+hitbox.height/2);
        if(stunned&&!isDead){
            stunTime-=delta;
            if(stunTime<0){
                stunned = false;
                stunTime=6f;
                stateTimer=0;
                behaviorTimer=0;
                currentState=State.STUN_RECOVER;
                bounds.y += (bounds.height / 10f) - (bounds.height / 20f);
            }
        }
        if (hitLimit > 0&&!isDead) {
            hitLimitTimer += delta;

            if (hitLimit >= 3) {
                currentState = State.ANTIC_JUMP;

                isJumpDefensive = rand.nextBoolean();

                jumpedForSmash = false;
                hitLimit = 0;
                stateTimer = 0;
                behaviorTimer = 0;
            }

            if (hitLimitTimer >= hitLimitDuration) {
                hitLimit = 0;
                hitLimitTimer = 0;
            }
        }
        previousState = currentState;
        stateTimer += delta;
        if (isDead) {
            if (isOnGround) {
                speed = 0;
            }
        }

        applyGravityAndCollision(delta, solidBlocks);
        if ((currentState == State.ATTACK || currentState == State.AIR_SMASH) && attackHitbox.overlaps(player.hitbox)) {
            if (!player.isInvincible) {
                player.setHitFromRight(player.hitbox.x < hitbox.x);
                player.takeDamage(2);
            }
        }

        if (currentState == State.RUNNING) {
            speed = 900f;
        }else if(currentState==State.JUMPING&&!jumpedForSmash){
            if(isJumpDefensive){
                speed = -800f;
            }else {
                speed = 600f;
            }
        }else{
                speed = 0f;
            }
            if(phase2) speed*=1.5f;


        if (currentState == State.IDLE && isFacingToPlayer(player) && isOnGround) {
            behaviorTimer += delta;

            if (behaviorTimer > 1f) {
                decideNextMove();
            }
        }

        switch (currentState) {

            case TURNING:
                if (turnAnimation.isAnimationFinished(stateTimer)) {
                    toRight = !toRight;
                    currentState = State.IDLE;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
                break;

            case ANTIC_ATTACK:
                if (attackAnticipateAnimation.isAnimationFinished(stateTimer)) {
                    currentState = State.ATTACK;
                    stateTimer = 0f;
                }
                break;

            case ATTACK:
                if (attackAnimation.isAnimationFinished(stateTimer)) {
                    cameraFunctions.shakeCamera(15f, 0.4f);
                    if(phase2) {
                        float spawnX = bounds.x + bounds.width / 2;
                        if (toRight) {
                            spawnX += hitbox.width * 1.8f;
                        } else {
                            spawnX -= hitbox.width * 1.8f;
                        }
                        float spawnY = hitbox.y + hitbox.width / 100f;
                        activeProjectiles.add(new BasicProjectile(spawnX, spawnY, toRight, ProjectileType.Wave, true));
                    }

                    currentState = State.COOLDOWN_ATTACK;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
                break;

            case COOLDOWN_ATTACK:
                behaviorTimer += delta;
                if (behaviorTimer > cooldownTime) {
                    currentState = State.IDLE;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
                break;
            case ANTIC_RUN:
                if (runStartAnimation.isAnimationFinished(stateTimer)) {
                    currentState = State.RUNNING;
                    stateTimer = 0f;
                }
                break;

            case RUNNING:
                    cameraFunctions.shakeCamera(2f, 0.2f);
                break;

            case COOLDOWN_RUN:
                behaviorTimer += delta;
                if (behaviorTimer > cooldownTime) {
                    currentState = State.IDLE;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
                break;
            case ANTIC_JUMP:
                if (jumpAnticipationAnimation.isAnimationFinished(stateTimer)) {
                    cameraFunctions.shakeCamera(2f, 0.2f);
                    if(!jumpedForSmash) {
                        currentState = State.JUMPING;
                        stateTimer = 0f;
                        behaviorTimer = 0f;
                        velocityY = -10f;
                        speed = -800;
                    }
                    else{
                        currentState = State.JUMPING;
                        stateTimer = 0f;
                        behaviorTimer = 0f;
                        velocityY = -15f;
                        speed = 0;
                    }
                }
                break;

            case JUMPING:
                if(jumpedForSmash&&stateTimer>2f){
                    currentState = State.AIR_SMASH;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
                if(isOnGround&&velocityY>0) {
                    cameraFunctions.shakeCamera(5f, 0.2f);
                    currentState = State.LANDING;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
                break;
            case AIR_SMASH:
                if(jumpAttackAnimation.isAnimationFinished(stateTimer)) {
                    currentState = State.IDLE;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                    airSmashed=false;
                }else if(!airSmashed&&jumpAttackAnimation.isAnimationFinished(stateTimer*1.33f)){
                    cameraFunctions.shakeCamera(15f, 0.4f);
                    float spawnX =  bounds.x + bounds.width/2;
                    if(toRight){
                        spawnX += hitbox.width*1.8f;
                    }else{
                        spawnX -= hitbox.width*1.8f;
                    }
                    float spawnY = hitbox.y + hitbox.width / 100f;
                    airSmashed = true;
                    activeProjectiles.add(new BasicProjectile(spawnX, spawnY, toRight, ProjectileType.Wave,true));

                }
                break;

            case LANDING:
                behaviorTimer += delta;
                if (behaviorTimer > cooldownTime) {
                    currentState = State.IDLE;
                    stateTimer = 0f;
                    behaviorTimer = 0f;
                }
                break;
            case STUN_FALL:
                if(isOnGround) {
                    stateTimer=0;
                    behaviorTimer = 0f;
                    currentState = State.STUN_LAND;
                }
                break;
            case STUN_LAND:
                if(stunLandAnimation.isAnimationFinished(stateTimer)) {
                    stateTimer=0;
                    behaviorTimer = 0f;
                    currentState = State.STUN_IDLE;
                    stunned=true;
                }
                break;
            case STUN_HIT:
                if(stunHitAnimation.isAnimationFinished(stateTimer)) {
                    stateTimer=0;
                    behaviorTimer = 0f;
                    currentState = State.STUN_IDLE;
                }
                break;
            case STUN_RECOVER:
                if(stunRecoverAnimation.isAnimationFinished(stateTimer)) {
                    stateTimer=0;
                    behaviorTimer = 0f;
                    currentState = State.IDLE;
                }
                break;
            default:
                if(player.hitbox.x+player.hitbox.width<hitbox.x && toRight || player.hitbox.x>hitbox.x+hitbox.width&&!toRight){
                    turnAround();
                }
                break;
        }

    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame=null;
        switch (currentState) {
                        case IDLE:
                currentFrame = idleAnimation.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                if (!runStartAnimation.isAnimationFinished(stateTimer)) {
                    currentFrame = runStartAnimation.getKeyFrame(stateTimer);
                } else {
                    float loopTime = stateTimer - runStartAnimation.getAnimationDuration();
                    currentFrame = runAnimation.getKeyFrame(loopTime, true);
                }
                break;
            case ANTIC_RUN:
                currentFrame = runStartAnimation.getKeyFrame(stateTimer);
                break;
            case COOLDOWN_RUN:
                currentFrame = runStartAnimation.getKeyFrame(stateTimer);
                break;
            case TURNING:
                currentFrame = turnAnimation.getKeyFrame(stateTimer);
                break;
            case ANTIC_ATTACK:
                currentFrame = attackAnticipateAnimation.getKeyFrame(stateTimer);
                break;
            case ATTACK:
                currentFrame = attackAnimation.getKeyFrame(stateTimer);
                break;
            case COOLDOWN_ATTACK:
                currentFrame = attackCooldownAnimation.getKeyFrame(stateTimer);
                break;
            case ANTIC_JUMP:
                currentFrame = jumpAnticipationAnimation.getKeyFrame(stateTimer);
                break;
            case JUMPING:
                currentFrame = jumpAnimation.getKeyFrame(stateTimer);
                break;
            case LANDING:
                currentFrame = landAnimation.getKeyFrame(stateTimer);
                break;
            case AIR_SMASH:
                currentFrame = jumpAttackAnimation.getKeyFrame(stateTimer);
                break;
            case STUN_FALL:
                if (velocityY<0) {
                    currentFrame = stunFallStartAnimation.getKeyFrame(stateTimer);
                } else {
                    float loopTime = stateTimer - stunFallStartAnimation.getAnimationDuration();
                    currentFrame = stunFallAnimation.getKeyFrame(loopTime, true);
                }
                break;
            case STUN_LAND:
                currentFrame = stunLandAnimation.getKeyFrame(stateTimer);
                break;
            case STUN_IDLE:
                currentFrame = stunIdleAnimation.getKeyFrame(stateTimer);
                break;
            case STUN_HIT:
                currentFrame = stunHitAnimation.getKeyFrame(stateTimer);
                break;
            case STUN_RECOVER:
                currentFrame = stunRecoverAnimation.getKeyFrame(stateTimer);
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
        if(!isDead&&!stunned) {

            hitbox.width = bounds.width / 6f;
            hitbox.height = bounds.height * 3f / 8f;

            hitbox.y = bounds.y + bounds.height / 20f;
            hitbox.x = bounds.x + bounds.width * 5f / 12f;

        }else{
            hitbox.y = bounds.y + bounds.height / 10;
            hitbox.x = bounds.x + bounds.width *4/ 12;
            hitbox.width = bounds.width /4;
            hitbox.height = bounds.height /4;
        }
        if (currentState == State.ATTACK) {
            int frameIndex = attackAnimation.getKeyFrameIndex(stateTimer);

            switch (frameIndex) {
                case 0:
                    attackHitbox.width = bounds.width / 20f;
                    attackHitbox.height = bounds.height / 7f;
                    attackHitbox.x = hitbox.x + hitbox.width / 2f - attackHitbox.width / 2f;
                    attackHitbox.y = hitbox.y + hitbox.height*1.9f;
                    break;

                case 1:
                    attackHitbox.width = bounds.width / 10f;
                    attackHitbox.height = bounds.height / 6f;
                    attackHitbox.y = hitbox.y + hitbox.height / 1.5f;
                    if (toRight) {
                        attackHitbox.x = hitbox.x + hitbox.width*2.2f;
                    } else {
                        attackHitbox.x = hitbox.x-hitbox.width*1.1f - attackHitbox.width;
                    }
                    break;

                case 2:
                    attackHitbox.width = bounds.width / 9f;
                    attackHitbox.height = bounds.height / 6f;
                    attackHitbox.y = hitbox.y;
                    if (toRight) {
                        attackHitbox.x = hitbox.x + hitbox.width*2f;
                    } else {
                        attackHitbox.x = hitbox.x-hitbox.width - attackHitbox.width;
                    }
                    break;
            }
        }
    }
    @Override
    public void takeDamage(int damage) {
        if (stunned) {
            if (currentState == State.STUN_IDLE) {
                currentState = State.STUN_HIT;
                stateTimer = 0f;
            }
            return;
        }

        currentHealth -= damage;
        if (currentHealth <= maxHealth/2&&!phase2) {
            currentHealth = maxHealth/2;
            phase2 = true;
            if (toRight) {
                speed = 300;
            } else {
                speed = -300;
            }
            velocityY = -5f;
            stunned = true;
            currentState = State.STUN_FALL;
            stateTimer = 0f;
        }else if (currentHealth <= 0) {
            currentHealth = 0;
            if (toRight) {
                speed = 300;
            } else {
                speed = -300;
            }
            velocityY = -10f;
            isDead = true;
            currentState = State.STUN_FALL;
            stateTimer = 0f;
        }

        isInvincible = true;
        invincibleTimer = 0;
        hitLimit++;
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
        if (currentState == State.ATTACK || currentState == State.AIR_SMASH) {
            shape.setColor(com.badlogic.gdx.graphics.Color.PURPLE);
            shape.rect(attackHitbox.x, attackHitbox.y, attackHitbox.width, attackHitbox.height);
        }
    }
    private void setDistanceLevel(float playerX, float playerY,float bossX,float bossY) {
        float distanceX = Math.abs(playerX - bossX);
        float distanceY = Math.abs(playerY - bossY);
        if(distanceX<500&&distanceY<300) {
            distanceLevel=1;
        }else if(distanceX<600&&distanceY<300) {
            distanceLevel=2;
        }else if(distanceX<1000&&distanceY<300) {
            distanceLevel=3;
        }else{
            distanceLevel=4;
        }
    }
    private boolean isFacingToPlayer(Player player) {
        return toRight&&player.hitbox.x+player.hitbox.width/2>hitbox.x+hitbox.width/2||!toRight&&player.hitbox.x+player.hitbox.width/2<hitbox.x+hitbox.width/2
        ;
    }
    private void decideNextMove() {

        int weightAttack = 10;
        int weightSmash = 10;
        int weightRun = 10;
        int weightRetreat = 10;
        int weightJump = 0;
        if(phase2){
            if (distanceLevel == 1) {
                seen = true;
                weightAttack = 60; weightSmash = 30;weightRetreat=40; weightRun = 30; weightJump = 30;
            } else if (distanceLevel == 2) {
                weightAttack = 10; weightSmash = 50; weightRetreat = 20; weightRun = 70;weightJump = 50;
            } else if (distanceLevel == 3 || distanceLevel == 4) {
                weightAttack = 0; weightSmash = 50; weightRetreat = 0; weightRun = 70;weightJump = 50;
            }
        }else{
            if (distanceLevel == 1) {
                seen = true;
                weightAttack = 60; weightSmash = 0;weightRetreat=40; weightRun = 30; weightJump = 30;
            } else if (distanceLevel == 2) {
                weightAttack = 10; weightSmash = 0; weightRetreat = 20; weightRun = 70;weightJump = 50;
            } else if (distanceLevel == 3 || distanceLevel == 4) {
                weightAttack = 0; weightSmash = 0; weightRetreat = 0; weightRun = 70;weightJump = 50;
            }
        }

        if (lastMove == 0) weightAttack = 0;
        if (lastMove == 1) weightSmash = 0;
        if (lastMove == 2) weightRun = 0;
        if (lastMove == 3) weightRetreat = 0;
        if (lastMove == 4) weightJump = 0;

        int totalWeight = weightAttack + weightSmash + weightRun + weightRetreat+weightJump;

        if (totalWeight == 0) {
            weightAttack = 10; weightSmash = 10; totalWeight = 50;weightRun =10; weightRetreat = 10; weightJump = 10;
        }

        int randomValue = rand.nextInt(totalWeight);
        if(!seen){
            currentState = State.IDLE;
        }
        else if (randomValue < weightAttack) {
            currentState = State.ANTIC_ATTACK;
            lastMove = 0;
        } else if (randomValue < weightAttack + weightSmash) {
            currentState = State.ANTIC_JUMP;
            jumpedForSmash = true;
            isJumpDefensive = false;
            lastMove = 1;
        } else if (randomValue < weightAttack + weightSmash + weightRun) {
            currentState = State.ANTIC_RUN;
            lastMove = 2;
        } else if (randomValue < weightAttack + weightSmash+weightRun + weightJump) {
            currentState = State.ANTIC_JUMP;
            jumpedForSmash = false;
            isJumpDefensive = false;
            lastMove = 4;
        } else {
            currentState = State.ANTIC_JUMP;
            isJumpDefensive = true;
            jumpedForSmash = false;
            lastMove = 3;
        }

        stateTimer = 0f;
        behaviorTimer = 0f;
    }
}
