package com.hollow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.hollow.Charm.*;

import java.util.Iterator;
import java.util.Random;

public class Player extends Entity {
    public enum State { IDLE, RUNNING, JUMPING,GETUP, FALLING, LANDING, DASHING, SLASHING, SLIDING ,FOCUS_STARTING,FOCUS_ANTICIPATING,FOCUS_GETTING,FOCUS_ENDING,CASTING_FIREBALL,KNOCKBACK,SCREAMING,DEAD}

    public State currentState = State.GETUP;
    public State previousState = State.GETUP;




    private boolean isTouchingRightWall = false;
    private boolean isTouchingLeftWall = false;
    private boolean isWallSliding = false;
    private float wallJumpTimer = 0f;
    private float wallJumpForceX = 0f;
    private boolean hasPogoed = false;
    private boolean isWallJumping = false;
    private boolean casted=false;
    private boolean screamed = false;
    private int facing = 0; //-1->down,0->normal,1->up
    private int SlashFacing = 0; //-1->down,0->normal,1->up
    private float knockbackTimer = 0f;
    private float knockbackDuration = 0.3f;
    private float knockbackVelocityX = 0f;
    private boolean hitFromRight = false;
    private float lastSafeX = 0f;
    private float lastSafeY = 0f;
    private float waitForBlackout=0f;
    private boolean toggledBlackedOut;
    private boolean hasShadowHeart=false;
    private boolean hasSharpShadow=false;

    public int nailDamage = 1;
    public float attackSpeedMultiplier = 1.0f;
    public float focusSpeedMultiplier = 1.0f;
    public float knockbackMultiplier = 1.0f;
    public int gainingSoul=5;




    private int doubleJumpCount = 4;
    private int doubleJump = doubleJumpCount;
    private Random rand = new Random();
    private boolean rightSlash = true;

    private float dashBoost = 2.5f;
    private float dashDuration = 0.3f;
    public float dashCooldown = 0.8f;
    private float dashTimer = 0f;
    private float dashCooldownTimer = 0f;
    private boolean hasDashedInAir = false;

    public Hud playerHud;


    float attackHeight;
    float attackWidth;
    float attackX;
    float attackY;

    float currentSoul;
    float maxSoul ;
    float theRealSoul;
    int currentHealth;
    int maxHealth;
    boolean blackedDeath=false;

    public Array<Charm> ownedCharms = new Array<>();
    public Array<Charm> activeCharms = new Array<>();
    public int maxCharms = 3;

    private Animation<TextureRegion> slashAltEffectAnimation;
    private Animation<TextureRegion> slashEffectAnimation;
    private Animation<TextureRegion> DoubleJumpAnimation;
    private Animation<TextureRegion> focusStartAnimation;
    private Animation<TextureRegion> dashEffectAnimation;
    private Animation<TextureRegion> fallStartAnimation;
    private Animation<TextureRegion> wallSlideAnimation;
    private Animation<TextureRegion> runStartAnimation;
    private Animation<TextureRegion> fallLoopAnimation;
    private Animation<TextureRegion> slashAltAnimation;
    private Animation<TextureRegion> wallJumpAnimation;
    private Animation<TextureRegion> focusGetAnimation;
    private Animation<TextureRegion> focusEndAnimation;
    private Animation<TextureRegion> fireBallAnimation;
    private Animation<TextureRegion> shadowFireBallAnimation;
    private Animation<TextureRegion> screamAnimation;
    private Animation<TextureRegion> shadowScreamAnimation;
    private Animation<TextureRegion> landingAnimation;
    private Animation<TextureRegion> runLoopAnimation;
    private Animation<TextureRegion> slashAnimation;
    private Animation<TextureRegion> focusAnimation;
    private Animation<TextureRegion> dashAnimation;
    private Animation<TextureRegion> shadowDashAnimation;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> lookDownAnimation;
    private Animation<TextureRegion> lookDownStartAnimation;
    private Animation<TextureRegion> lookUpAnimation;
    private Animation<TextureRegion> lookUpStartAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> slashUpEffectAnimation;
    private Animation<TextureRegion> slashUpAnimation;
    private Animation<TextureRegion> slashDownEffectAnimation;
    private Animation<TextureRegion> slashDownAnimation;
    private Animation<TextureRegion> soulScreamEffectAnimation;
    private Animation<TextureRegion> shadowScreamEffectAnimation;
    private Animation<TextureRegion> deathAnimation;
    private Animation<TextureRegion> riseAnimation;

    private PlayScreen playScreen;





    public Player(float startX, float startY, float width, float height, CameraFunctions cameraFunctions,PlayScreen playScreen) {

        super(startX, startY, width, height, cameraFunctions);
        currentSoul=0f;
        maxSoul = 100f;
        currentHealth = 7;
        maxHealth = 7;
        this.playScreen=playScreen;

        hitbox = new Rectangle(startX + width * 3.5f / 4, startY, width / 8, height / 1.5f);

        Texture tmpSheet = new Texture("animation/Idle.png");
        loadedTextures.add(tmpSheet);
        int frameWidth = tmpSheet.getWidth() / 9;
        int frameHeight = tmpSheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 9; i++) {
            frames.add(tmp[0][i]);
        }
        idleAnimation = new Animation<>(0.1f, frames);

        tmpSheet = new Texture("animation/Run.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 13;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        Array<TextureRegion> startFrames = new Array<>();
        for (int i = 0; i < 3; i++) startFrames.add(tmp[0][i]);
        runStartAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);
        Array<TextureRegion> loopFrames = new Array<>();
        for (int i = 3; i < 13; i++) loopFrames.add(tmp[0][i]);
        runLoopAnimation = new Animation<>(0.1f, loopFrames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Airborne.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 12;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 6; i++) startFrames.add(tmp[0][i]);
        jumpAnimation = new Animation<>(0.09f, startFrames, Animation.PlayMode.NORMAL);
        startFrames = new Array<>();
        for (int i = 6; i < 9; i++) startFrames.add(tmp[0][i]);
        fallStartAnimation = new Animation<>(0.05f, startFrames, Animation.PlayMode.NORMAL);
        loopFrames = new Array<>();
        for (int i = 9; i < 12; i++) loopFrames.add(tmp[0][i]);
        fallLoopAnimation = new Animation<>(0.06f, loopFrames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Landing.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 4;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 4; i++) startFrames.add(tmp[0][i]);
        landingAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Double Jump.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 8;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 8; i++) startFrames.add(tmp[0][i]);
        DoubleJumpAnimation = new Animation<>(0.06f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Dash.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 12;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 12; i++) startFrames.add(tmp[0][i]);
        dashAnimation = new Animation<>(0.04f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Shadow Dash.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 11;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 11; i++) startFrames.add(tmp[0][i]);
        shadowDashAnimation = new Animation<>(0.04f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Slash.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 5; i++) startFrames.add(tmp[0][i]);
        slashAnimation = new Animation<>(0.08f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/SlashAlt.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 5; i++) startFrames.add(tmp[0][i]);
        slashAltAnimation = new Animation<>(0.08f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Effects/SlashEffect.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 6; i++) startFrames.add(tmp[0][i]);
        slashEffectAnimation = new Animation<>(0.015f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Effects/SlashEffectAlt.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 6; i++) startFrames.add(tmp[0][i]);
        slashAltEffectAnimation = new Animation<>(0.015f, startFrames, Animation.PlayMode.NORMAL);
//
        tmpSheet = new Texture("animation/UpSlash.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 5; i++) startFrames.add(tmp[0][i]);
        slashUpAnimation = new Animation<>(0.08f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Effects/UpSlashEffect.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 6; i++) startFrames.add(tmp[0][i]);
        slashUpEffectAnimation = new Animation<>(0.015f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/DownSlash.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 5;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 5; i++) startFrames.add(tmp[0][i]);
        slashDownAnimation = new Animation<>(0.08f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Effects/DownSlashEffect.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 6; i++) startFrames.add(tmp[0][i]);
        slashDownEffectAnimation = new Animation<>(0.015f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/LookDown.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        startFrames.add(tmp[0][0]);
        lookDownStartAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);
        startFrames = new Array<>();
        for (int i = 1; i < 6; i++) startFrames.add(tmp[0][i]);
        lookDownAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/LookUp.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        startFrames.add(tmp[0][0]);
        lookUpStartAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);
        startFrames = new Array<>();
        for (int i = 1; i < 6; i++) startFrames.add(tmp[0][i]);
        lookUpAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.LOOP);
//

        tmpSheet = new Texture("animation/Effects/Dash Effect.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 8;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 8; i++) startFrames.add(tmp[0][i]);
        dashEffectAnimation = new Animation<>(0.05f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Wall Slide.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 4;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 4; i++) startFrames.add(tmp[0][i]);
        wallSlideAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Walljump.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 9;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 9; i++) startFrames.add(tmp[0][i]);
        wallJumpAnimation = new Animation<>(0.06f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Focus.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 4;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 4; i++) startFrames.add(tmp[0][i]);
        focusAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Focus Get.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 6; i++) startFrames.add(tmp[0][i]);
        focusGetAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Focus Start.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 3;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 3; i++) startFrames.add(tmp[0][i]);
        focusStartAnimation= new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Focus End.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 3;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 3; i++) startFrames.add(tmp[0][i]);
        focusEndAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Fireball Cast.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 9;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 9; i++) startFrames.add(tmp[0][i]);
        fireBallAnimation = new Animation<>(0.06f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Scream.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 8;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 8; i++) startFrames.add(tmp[0][i]);
        screamAnimation= new Animation<>(0.06f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Shadow Fireball Cast.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 6;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 6; i++) startFrames.add(tmp[0][i]);
        shadowFireBallAnimation = new Animation<>(0.09f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Shadow Scream.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 11;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 11; i++) startFrames.add(tmp[0][i]);
        shadowScreamAnimation = new Animation<>(0.06f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Effects/SoulScream.png");
        loadedTextures.add(tmpSheet);
        int frameWidthScream = tmpSheet.getWidth() / 13;
        int frameHeightScream = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidthScream, frameHeightScream);
        startFrames = new Array<>();
        for (int i = 0; i < 13; i++) {
            startFrames.add(tmp[0][i]);
        }

        soulScreamEffectAnimation = new Animation<>(0.05f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Effects/ShadowScream.png");
        loadedTextures.add(tmpSheet);
        frameWidthScream = tmpSheet.getWidth() / 14;
        frameHeightScream = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidthScream, frameHeightScream);
        startFrames = new Array<>();
        for (int i = 0; i < 14; i++) {
            startFrames.add(tmp[0][i]);
        }

        shadowScreamEffectAnimation = new Animation<>(0.05f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Death.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 18;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 18; i++) startFrames.add(tmp[0][i]);
        deathAnimation = new Animation<>(0.2f, startFrames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/Prostrate Rise.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() / 26;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        startFrames = new Array<>();
        for (int i = 0; i < 26; i++) {
            startFrames.add(tmp[0][i]);
        }

        riseAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);


        ownedCharms.add(new DashMaster());
        ownedCharms.add(new SoulCatcher());
        ownedCharms.add(new UnbreakableStrength());
        ownedCharms.add(new QuickSlash());
        ownedCharms.add(new QuickFocus());
        ownedCharms.add(new HeavyBlow());
        ownedCharms.add(new ShadowHeart());
        ownedCharms.add(new SharpShadow());

    }



    public void update(float delta, Array<SolidBlock> solidBlocks, Array<Effect> worldEffects,Array<Enemy> enemies,Array<Projectile> activeProjectiles) {
        if(currentState==State.GETUP){
            if(riseAnimation.isAnimationFinished(stateTimer)){
                currentState=State.IDLE;
                stateTimer=0;
            }
        }if(currentState==State.DEAD){
            velocityY=0;
            if(deathAnimation.isAnimationFinished(stateTimer*1.5f)&&!blackedDeath){
                blackedDeath = true;
                cameraFunctions.triggerBlackOut(2f);
            }if(deathAnimation.isAnimationFinished(stateTimer)){
                cameraFunctions.resetLevel();
            }

        }
        float speed = 600 * delta;
        if(isInvincible&&invincibleTimer<invincibilityDuration) {
            invincibleTimer += delta;

        }else if(isInvincible&&invincibleTimer>invincibilityDuration) {
            invincibleTimer=0;
            isInvincible=false;
        }
        float tmpX = bounds.x;
        float tmpY = bounds.y;
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_8) &&maxSoul>theRealSoul) {
            theRealSoul+=20f;
            if(theRealSoul>maxSoul) {
                theRealSoul=maxSoul;
            }
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_2) &&theRealSoul>0) {
            theRealSoul -= 20f;
            if(theRealSoul<0) {
                theRealSoul=0;
            }
        }


        previousState = currentState;

        if (dashCooldownTimer > 0) dashCooldownTimer -= delta;

        if (Gdx.input.isKeyJustPressed(Input.Keys.SHIFT_LEFT) && dashCooldownTimer <= 0 && currentState != State.DASHING && currentState != State.DEAD && currentState != State.GETUP) {            if (isOnGround || !hasDashedInAir) {
                currentState = State.DASHING;
                dashTimer = 0f;
                dashCooldownTimer = dashCooldown;
                if(toRight) {
                    worldEffects.add(new Effect(dashEffectAnimation, bounds.x - bounds.width / 4, bounds.y - bounds.height / 4, bounds.width, bounds.height, !toRight,false));
                } else {
                    worldEffects.add(new Effect(dashEffectAnimation, bounds.x + bounds.width / 4, bounds.y - bounds.height / 4, bounds.width, bounds.height, !toRight,false));
                }
                if (!isOnGround) hasDashedInAir = true;
            }
        }

        boolean isFocusing = (currentState == State.FOCUS_STARTING ||
            currentState == State.FOCUS_ANTICIPATING ||
            currentState == State.FOCUS_GETTING ||
            currentState == State.FOCUS_ENDING);

        boolean isCasting = (currentState == State.CASTING_FIREBALL);
        boolean isScreaming =  (currentState == State.SCREAMING);

        if (Gdx.input.isKeyJustPressed(Input.Keys.C) &&theRealSoul>=33f&& !isFocusing && !isCasting&& !isScreaming&&currentState!=State.GETUP && currentState != State.DEAD && currentState != State.DASHING) {
            theRealSoul -= 33f;
            currentState = State.CASTING_FIREBALL;
            stateTimer = 0f;
            isCasting = true;


        }else if(Gdx.input.isKeyJustPressed(Input.Keys.S) &&theRealSoul>=33f&& !isFocusing && !isCasting && !isScreaming&&currentState!=State.GETUP && currentState != State.DEAD && currentState != State.DASHING) {
            theRealSoul -= 33f;
            currentState = State.SCREAMING;
            stateTimer = 0f;
            isScreaming = true;
        }

        if (isCasting) {
            velocityY=0;
            Animation castingBallAnimation= hasShadowHeart? shadowFireBallAnimation:fireBallAnimation;
            float throwPoint = hasShadowHeart? 3f:1.5f;
            if (castingBallAnimation.isAnimationFinished(stateTimer)) {

                    currentState = isOnGround ? State.IDLE : State.FALLING;

                stateTimer = 0f;
                casted=false;

            }else if(castingBallAnimation.isAnimationFinished(stateTimer*throwPoint)&&!casted) {
                float spawnX =  bounds.x + bounds.width/2;
                float spawnY = hitbox.y + hitbox.width / 2f;
                if(hasShadowHeart){
                    activeProjectiles.add(new BasicProjectile(spawnX, spawnY, toRight, ProjectileType.ShadowBall,false));
                }else {
                    activeProjectiles.add(new BasicProjectile(spawnX, spawnY, toRight, ProjectileType.SoulBall, false));
                }
                cameraFunctions.shakeCamera(20f, 0.2f);
                casted=true;
            }
        }else if (isScreaming) {
            velocityY=0;
            if(!screamed){
                if(hasShadowHeart){
                    worldEffects.add(new Effect(shadowScreamEffectAnimation, bounds.x,  bounds.y, bounds.width, bounds.width*0.818f, !toRight,true));
                }else {
                    worldEffects.add(new Effect(soulScreamEffectAnimation, bounds.x,  bounds.y, bounds.width, bounds.width*0.818f, !toRight,true));
                }
                cameraFunctions.shakeCamera(20f, 0.2f);
            }
            Rectangle screamRange = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.width*0.818f);
            screamed=true;
            if (screamAnimation.isAnimationFinished(stateTimer)) {

                currentState = isOnGround ? State.IDLE : State.FALLING;

                stateTimer = 0f;
                screamed=false;

            }else if(screamAnimation.isAnimationFinished(stateTimer*3f)) {
                for(Enemy enemy:enemies){
                if (!enemy.isInvincible && screamRange.overlaps(enemy.hitbox)) {
                    cameraFunctions.shakeCamera(4f, 0.3f);
                    enemy.takeDamage(2);
                }
            }
        }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) && !isCasting&& !isScreaming&&currentState!=State.GETUP && currentState != State.DEAD &&isOnGround && !isFocusing && currentState == State.IDLE) {
            if(facing != 1){
                stateTimer = 0f;
            }
            facing = 1;
            speed = 0f;
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !isCasting&& !isScreaming&&currentState!=State.GETUP && currentState != State.DEAD &&isOnGround && !isFocusing && currentState == State.IDLE) {
            if(facing != -1){
                stateTimer = 0f;
            }
            facing = -1;
            speed = 0f;
        }else{
            facing = 0;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F) && !isCasting&& !isScreaming&&currentState!=State.GETUP && currentState != State.DEAD &&theRealSoul >= 20f && currentHealth < maxHealth && isOnGround && !isFocusing && currentState != State.DASHING) {
            currentState = State.FOCUS_STARTING;
            stateTimer = 0f;
            speed = 0f;
            isFocusing = true;
        }

        if (isFocusing) {

            if (currentState == State.FOCUS_STARTING) {
                if (!Gdx.input.isKeyPressed(Input.Keys.F)) {
                    currentState = State.FOCUS_ENDING;
                    stateTimer = 0f;
                } else if (focusStartAnimation.isAnimationFinished(stateTimer)) {
                    currentState = State.FOCUS_ANTICIPATING;
                    stateTimer = 0f;
                }
            }
            else if (currentState == State.FOCUS_ANTICIPATING) {
                if (!Gdx.input.isKeyPressed(Input.Keys.F)) {
                    currentState = State.FOCUS_ENDING;
                    stateTimer = 0f;
                } else if (focusAnimation.isAnimationFinished(stateTimer/2*focusSpeedMultiplier)) {
                    if (theRealSoul >= 20f && currentHealth < maxHealth) {
                        theRealSoul -= 20f;
                        focus(1);

                        playerHud.triggerHealAnimation();


                        currentState = State.FOCUS_GETTING;
                        stateTimer = 0f;
                    } else {
                        currentState = State.FOCUS_ENDING;
                        stateTimer = 0f;
                    }
                }
            }
            else if (currentState == State.FOCUS_GETTING) {
                if (focusGetAnimation.isAnimationFinished(stateTimer)) {
                    if (Gdx.input.isKeyPressed(Input.Keys.F) && theRealSoul >= 20f && currentHealth < maxHealth) {
                        currentState = State.FOCUS_ANTICIPATING;
                        stateTimer = 0f;
                    } else {
                        currentState = State.FOCUS_ENDING;
                        stateTimer = 0f;
                    }
                }
            }
            else if (currentState == State.FOCUS_ENDING) {
                if (focusEndAnimation.isAnimationFinished(stateTimer)) {
                    currentState = State.IDLE;
                    stateTimer = 0f;
                }
            }
        }
        boolean touchingSpike = false;


        boolean isMoving = false;
        boolean wantsToSlash = Gdx.input.isKeyJustPressed(Input.Keys.A);
        if (wallJumpTimer > 0) wallJumpTimer -= delta;

        if (currentState == State.KNOCKBACK) {
            knockbackTimer += delta;
            bounds.x += knockbackVelocityX * delta;

            this.updateHitBox();
            Rectangle block;
            for (SolidBlock blocks : solidBlocks) {
                block = blocks.bounds;
                if (hitbox.overlaps(block)) {
                    bounds.x = tmpX;
                    this.updateHitBox();
                    break;
                }
            }

            if (knockbackTimer >= knockbackDuration) {
                currentState = State.FALLING;
            }

        } else if (currentState == State.DASHING) {
            velocityY = 0;
            dashTimer += delta;
            float currentDashSpeed = dashBoost * speed;

            if (toRight) bounds.x += currentDashSpeed;
            else bounds.x -= currentDashSpeed;

            this.updateHitBox();

            Rectangle block;
            for (SolidBlock blocks : solidBlocks) {
                block=blocks.bounds;
                if (hitbox.overlaps(block)) {
                    bounds.x = tmpX;
                    this.updateHitBox();
                    currentState = State.FALLING;
                    break;
                }
            }

            if (dashTimer >= dashDuration) {
                currentState = isOnGround ? State.IDLE : State.FALLING;
            }
        } else {
            if (!isCasting &&!isScreaming&&currentState!=State.GETUP && currentState != State.DEAD&&!isFocusing &&wallJumpTimer <= 0 && !(Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT))) {
                if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                    bounds.x -= speed;
                    toRight = false;
                    isMoving = true;
                }
                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    bounds.x += speed;
                    toRight = true;
                    isMoving = true;
                }
            } else if (wallJumpTimer > 0) {
                bounds.x += wallJumpForceX * delta;
                isMoving = true;
            }

            this.updateHitBox();
            Rectangle block;
            for (SolidBlock blocks : solidBlocks) {
                block=blocks.bounds;
                if (hitbox.overlaps(block)) {
                    bounds.x = tmpX;
                    this.updateHitBox();
                    break;
                }
            }

            isTouchingLeftWall =  false;
            isTouchingRightWall =  false;
            float sensorWidth =   speed;

            Rectangle rightSensor = new Rectangle(hitbox.x + hitbox.width, hitbox.y + hitbox.height/3, sensorWidth, hitbox.height/3);
            Rectangle leftSensor = new Rectangle(hitbox.x - sensorWidth, hitbox.y + hitbox.height/3, sensorWidth, hitbox.height/3);


            for (SolidBlock blocks : solidBlocks) {
                block=blocks.bounds;
                if (!blocks.isDeadly) {
                    if (rightSensor.overlaps(block)) isTouchingRightWall = true;
                    if (leftSensor.overlaps(block)) isTouchingLeftWall = true;
                }else{
                    if (rightSensor.overlaps(block)) touchingSpike = true;
                    if (leftSensor.overlaps(block)) touchingSpike = true;;
                }
            }
            if (!isCasting &&!isScreaming&&currentState!=State.GETUP && currentState != State.DEAD &&!isFocusing &&Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                if (isWallSliding || (!isOnGround && (isTouchingLeftWall || isTouchingRightWall))) {
                    velocityY = -20f;
                    wallJumpTimer = 0.15f;
                    if (isTouchingRightWall) {
                        wallJumpForceX = -600f;
                        toRight = false;
                    } else if (isTouchingLeftWall) {
                        wallJumpForceX = 600f;
                        toRight = true;
                    }
                    doubleJump = doubleJumpCount;
                    isWallSliding = false;
                    isWallJumping = true;
                } else if (isOnGround || doubleJump > 0) {
                    if (!isOnGround) {
                        doubleJump--;
                        stateTimer = 0;
                    }
                    velocityY = -20;
                    isOnGround = false;
                    isWallJumping = false;
                }
                }
            if (velocityY >= 0) {
                hasPogoed = false;
            }

            if (!Gdx.input.isKeyPressed(Input.Keys.SPACE) && velocityY < 0 && !isWallJumping && !hasPogoed) {
                velocityY = Math.max(velocityY, -2.5f);
            }

            bounds.y -= velocityY;
            velocityY += 0.8f;

            if (velocityY > 16f) {
                velocityY = 16f;
            }
            this.updateHitBox();

            for (SolidBlock blocks : solidBlocks) {
                block=blocks.bounds;
                if (hitbox.overlaps(block)) {
                    if (velocityY > 0) {
                        bounds.y = block.y + block.height;
                    } else if (velocityY < 0) {
                        bounds.y = block.y - hitbox.height;
                    }
                    velocityY = 0;
                    this.updateHitBox();
                    break;
                }
            }
        }

        isOnGround = false;
        Rectangle feetSensor = new Rectangle(hitbox.x + 10, hitbox.y - 2, hitbox.width - 20, 2);

        Rectangle block;
        for (SolidBlock blocks : solidBlocks) {
            block = blocks.bounds;
            if (feetSensor.overlaps(block)) {
                if (!blocks.isDeadly) {
                    isOnGround = true;
                    doubleJump = doubleJumpCount;
                    hasDashedInAir = false;
                    if (currentState != State.DASHING) {
                        velocityY = 0;
                    }

                    if (currentState == State.IDLE || currentState == State.RUNNING || currentState == State.LANDING) {
                        lastSafeX = bounds.x;
                        lastSafeY = bounds.y;
                    }
                    break;
                }else{
                    touchingSpike = true;
                    break;
                }
            }
        }



        if (touchingSpike && !isDead) {
            if(!toggledBlackedOut) {
                if(currentHealth!=1) {
                    cameraFunctions.triggerBlackOut(0.4f);
                }
                takeDamage(1);
                toggledBlackedOut = true;
            }
            waitForBlackout+=delta;
            if(waitForBlackout>0.2f){
                bounds.x = lastSafeX;
                bounds.y = lastSafeY;
                updateHitBox();
                waitForBlackout = 0;
                toggledBlackedOut = false;
                currentState = State.GETUP;
            }

            velocityY = 0;

            knockbackTimer = knockbackDuration;

        }

        isWallSliding = false;
        if (!isOnGround && velocityY > 0) {
            if ((isTouchingRightWall && Gdx.input.isKeyPressed(Input.Keys.RIGHT)) ||
                (isTouchingLeftWall && Gdx.input.isKeyPressed(Input.Keys.LEFT))) {
                isWallSliding = true;
                hasDashedInAir = false;
                velocityY = 5f;

            }
        }
        if (currentState != State.DASHING&&!isFocusing&&!isCasting&&!isScreaming&&currentState!=State.GETUP && currentState != State.DEAD ) {
            boolean currentSlashFinished = rightSlash ? slashAnimation.isAnimationFinished(stateTimer) : slashAltAnimation.isAnimationFinished(stateTimer);

            if (wantsToSlash && (currentState != State.SLASHING || currentSlashFinished)) {
                rightSlash = !rightSlash;

                if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                    SlashFacing=1;
                    worldEffects.add(new Effect(slashUpEffectAnimation, bounds.x, bounds.y + bounds.height / 2, bounds.width , bounds.height, toRight,false));

                    attackWidth = hitbox.width*3;
                    attackHeight = bounds.height * 1.2f;
                    attackX = hitbox.x-hitbox.width;
                    attackY = hitbox.y + hitbox.height;

                } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && !isOnGround) {
                    SlashFacing = -1;
                    worldEffects.add(new Effect(slashDownEffectAnimation, bounds.x , bounds.y - bounds.height/2, bounds.width , bounds.height, toRight,false));

                    attackWidth = hitbox.width*3;
                    attackHeight = bounds.height * 1.2f;
                    attackX = hitbox.x- hitbox.width;
                    attackY = bounds.y-bounds.height/2

                        ;


                } else {
                    SlashFacing = 0;
                    if (rightSlash) {
                        worldEffects.add(new Effect(slashEffectAnimation, bounds.x - bounds.width / 2, bounds.y, bounds.width * 2, bounds.height, toRight,false));
                    } else {
                        worldEffects.add(new Effect(slashAltEffectAnimation, bounds.x - bounds.width / 2, bounds.y, bounds.width * 2, bounds.height, toRight,false));
                    }

                    attackHeight = hitbox.height;
                    attackWidth = bounds.width / 1.33f;
                    attackY = hitbox.y;
                    attackX = toRight ? hitbox.x + hitbox.width : hitbox.x - attackWidth;
                }


                Rectangle attackHitbox = new Rectangle(attackX, attackY, attackWidth, attackHeight);
                if(SlashFacing==-1) {
                    Iterator<SolidBlock> iterator = solidBlocks.iterator();
                    while (iterator.hasNext()) {
                        SolidBlock currentBlock = iterator.next();
                        Rectangle blockBounds = currentBlock.bounds;

                        if (currentBlock.isDeadly && attackHitbox.overlaps(blockBounds)) {
                            velocityY = -20f;
                            hasPogoed = true;
                            doubleJump = doubleJumpCount;
                            dashCooldownTimer = 0;
                            break;
                        }

                        if (currentBlock.breakable && attackHitbox.overlaps(blockBounds)) {
                            iterator.remove();
                            playScreen.removeLayer(3);

                        }
                    }
                }

                for (Enemy enemy : enemies) {
                    if (!enemy.isInvincible && attackHitbox.overlaps(enemy.hitbox)) {
                        cameraFunctions.shakeCamera(5f, 0.3f);
                        enemy.takeDamage(nailDamage);
                        if (!enemy.isDead) {
                            theRealSoul += gainingSoul;
                        }

                        if (SlashFacing == -1) {
                            velocityY = -20f;
                            hasPogoed = true;
                            doubleJump = doubleJumpCount;
                            dashCooldownTimer = 0;
                        }
                        else if (!(enemy instanceof False_knight)&&(!(enemy instanceof Zote))) {
                            enemy.isOnGround = false;
                            int x = enemy.toRight ? 1 : -1;
                            enemy.speed = (toRight ? 200f : -200f) * x*knockbackMultiplier;
                            enemy.velocityY = -5f;
                        }
                    }
                }
                currentState = State.SLASHING;
                stateTimer = 0f;
            } else if (currentState == State.SLASHING && !currentSlashFinished) {
                currentState = State.SLASHING;
            } else if (isWallSliding) {
                currentState = State.SLIDING;
            }else if (currentState == State.GETUP) {
                    currentState = State.GETUP;
            }else if (currentState == State.DEAD) {
                currentState = State.DEAD;
            } else {
                if (!isOnGround) {
                    if (velocityY < 0) {
                        currentState = State.JUMPING;
                    } else if (previousState == State.JUMPING && doubleJump != doubleJumpCount && !DoubleJumpAnimation.isAnimationFinished(stateTimer)) {
                        currentState = State.JUMPING;
                    } else {
                        currentState = State.FALLING;
                    }
                } else {
                    if (isMoving) {
                        currentState = State.RUNNING;
                    } else {
                        if (previousState == State.FALLING) {
                            currentState = State.LANDING;
                        } else if (previousState == State.LANDING) {
                            currentState = State.LANDING;
                            if (landingAnimation.isAnimationFinished(stateTimer)) {
                                currentState = State.IDLE;
                            }
                        } else {
                            currentState = State.IDLE;
                        }
                    }
                }
            }
        }

        if (currentState != previousState) {
            stateTimer = 0;
        } else {
            stateTimer += delta;
            if(currentState == State.SLASHING) {
                stateTimer += delta*(attackSpeedMultiplier-1);
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = null;

        switch (currentState) {
            case GETUP:
                currentFrame = riseAnimation.getKeyFrame(stateTimer, false);
                break;
            case DEAD:
                currentFrame = deathAnimation.getKeyFrame(stateTimer, false);
                break;
            case DASHING:
                if(hasSharpShadow){
                    currentFrame = shadowDashAnimation.getKeyFrame(stateTimer, false);
                }else {
                    currentFrame = dashAnimation.getKeyFrame(stateTimer, false);
                }
                break;
            case SLASHING:
                if (SlashFacing == 1) {
                    currentFrame = slashUpAnimation.getKeyFrame(stateTimer, false);
                } else if (SlashFacing == -1) {
                    currentFrame = slashDownAnimation.getKeyFrame(stateTimer, false);
                } else {
                    if (rightSlash) currentFrame = slashAnimation.getKeyFrame(stateTimer, false);
                    else currentFrame = slashAltAnimation.getKeyFrame(stateTimer, false);
                }
                break;
            case FOCUS_STARTING:
                currentFrame = focusStartAnimation.getKeyFrame(stateTimer, false);
                break;
            case CASTING_FIREBALL:
                if(hasShadowHeart){
                    currentFrame = shadowFireBallAnimation.getKeyFrame(stateTimer, false);
                }else {
                    currentFrame = fireBallAnimation.getKeyFrame(stateTimer, false);
                }
                break;
            case SCREAMING:
                if(hasShadowHeart){
                    currentFrame = shadowScreamAnimation.getKeyFrame(stateTimer, false);
                }else {
                    currentFrame = screamAnimation.getKeyFrame(stateTimer, false);
                }
                break;
            case FOCUS_ANTICIPATING:
                currentFrame = focusAnimation.getKeyFrame(stateTimer, false);
                break;
            case FOCUS_ENDING:
                currentFrame = focusEndAnimation.getKeyFrame(stateTimer, false);
                break;
            case FOCUS_GETTING:
                currentFrame = focusGetAnimation.getKeyFrame(stateTimer, false);
                break;
            case SLIDING:
                currentFrame = wallSlideAnimation.getKeyFrame(stateTimer, true);
                break;
            case JUMPING:
                if (isWallJumping) currentFrame = wallJumpAnimation.getKeyFrame(stateTimer, false);
                else if (doubleJump != doubleJumpCount) currentFrame = DoubleJumpAnimation.getKeyFrame(stateTimer, false);
                else currentFrame = jumpAnimation.getKeyFrame(stateTimer, false);
                break;
            case FALLING:
            case KNOCKBACK:
                if (!fallStartAnimation.isAnimationFinished(stateTimer)) currentFrame = fallStartAnimation.getKeyFrame(stateTimer);
                else {
                    float loopTime = stateTimer - fallStartAnimation.getAnimationDuration();
                    currentFrame = fallLoopAnimation.getKeyFrame(loopTime, true);
                }
                break;
            case RUNNING:
                if (!runStartAnimation.isAnimationFinished(stateTimer)) currentFrame = runStartAnimation.getKeyFrame(stateTimer);
                else {
                    float loopTime = stateTimer - runStartAnimation.getAnimationDuration();
                    currentFrame = runLoopAnimation.getKeyFrame(loopTime, true);
                }
                break;
            case LANDING:
                currentFrame = landingAnimation.getKeyFrame(stateTimer, false);
                break;
            case IDLE:
                switch (facing) {
                    case -1:
                        if (!lookDownStartAnimation.isAnimationFinished(stateTimer)) {
                            currentFrame = lookDownStartAnimation.getKeyFrame(stateTimer);
                        } else {
                            float loopTime = stateTimer - lookDownStartAnimation.getAnimationDuration();
                            currentFrame = lookDownAnimation.getKeyFrame(loopTime, true);
                        }
                        break;
                    case 0:
                        currentFrame = idleAnimation.getKeyFrame(stateTimer, true);
                        break;
                    case 1:
                        if (!lookUpStartAnimation.isAnimationFinished(stateTimer)) {
                            currentFrame = lookUpStartAnimation.getKeyFrame(stateTimer);
                        } else {
                            float loopTime = stateTimer - lookUpStartAnimation.getAnimationDuration();
                            currentFrame = lookUpAnimation.getKeyFrame(loopTime, true);
                        }
                        break;
                }
                break;
            default:
                currentFrame = idleAnimation.getKeyFrame(stateTimer, true);
                break;
        }
        if (isInvincible) {

            if (invincibleTimer % 0.2f < 0.1f) {

                batch.setColor(0f, 0f, 0f, 0.5f);
            } else {
                batch.setColor(1f, 1f, 1f, 1f);
            }
        }

        if (toRight) {
            batch.draw(currentFrame, bounds.x + bounds.width, bounds.y, -bounds.width, bounds.height);
        } else {
            batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
        }
        batch.setColor(1f, 1f, 1f, 1f);


    }

    @Override
    public void updateHitBox() {
        hitbox.x = bounds.x + bounds.width * 3.5f / 8;
        hitbox.y = bounds.y;
    }
    public void drawDebug(ShapeRenderer shape) {
        shape.setColor(com.badlogic.gdx.graphics.Color.GREEN);
        shape.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shape.setColor(com.badlogic.gdx.graphics.Color.RED);
        shape.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        if (currentState == State.SLASHING) {

            shape.setColor(Color.PURPLE);
            shape.rect(attackX, attackY, attackWidth, attackHeight);
        }
    }
    @Override
    public void takeDamage(int damage) {
        if (isDead || isInvincible) return;
        currentHealth -= damage;

        isInvincible = true;
        invincibleTimer = 0;
        cameraFunctions.triggerHitStop(0.3f);
        playerHud.triggerDamageAnimation();

        if (currentHealth <= 0) {
            currentHealth = 0;
            cameraFunctions.shakeCamera(50,3.6f);
            isDead = true;
            currentState = State.DEAD;
            stateTimer = 0f;
            velocityY = -4.5f;
        } else {
            cameraFunctions.shakeCamera(70, 0.3f);
            currentState = State.KNOCKBACK;
            knockbackTimer = 0f;
            velocityY = -4.5f;

            if (hitFromRight) {
                knockbackVelocityX = -450f;
            } else {
                knockbackVelocityX = 450f;
            }
        }
    }
    public void focus(int heal) {
        if (isDead) return;
        currentHealth += heal;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }

    }
    public void setHitFromRight(boolean hit){
        hitFromRight = hit;
    }
    public boolean getHitFromRight(){
        return hitFromRight;
    }
    public boolean hasShadowHeart(){
        return hasShadowHeart;
    }
    public void setShadowHeart(boolean hasShadowHeart){
        this.hasShadowHeart = hasShadowHeart;
    }
    public boolean hasSharpShadow(){
        return hasSharpShadow;
    }
    public void setSharpShadow(boolean hasSharpShadow){
        this.hasSharpShadow = hasSharpShadow;
    }
    public boolean equipCharm(Charm charm) {
        if (!charm.isEquipped && activeCharms.size < maxCharms) {
            activeCharms.add(charm);
            charm.isEquipped = true;
            charm.onEquip(this);
            return true;
        }
        return false;
    }

    public void unequipCharm(Charm charm) {
        if (charm.isEquipped) {
            activeCharms.removeValue(charm, true);
            charm.isEquipped = false;
            charm.onUnequip(this);
        }
    }

}
