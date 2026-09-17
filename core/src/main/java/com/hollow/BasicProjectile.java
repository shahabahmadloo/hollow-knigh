package com.hollow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.lang.reflect.Type;

public class BasicProjectile extends Projectile {

    private float lifespan = 10f;
    private float timer = 0f;
    private float stateTimer = 0f;
    ProjectileType projectileType;

    private Animation<TextureRegion> SoulBallStartAnimation;
    private Animation<TextureRegion> SoulBallAnimation;
    private Animation<TextureRegion> ShadowBallStartAnimation;
    private Animation<TextureRegion> ShadowBallAnimation;
    private Animation<TextureRegion> ShockWaveAnimation;
    private Animation<TextureRegion> ShockWaveStartAnimation;



    Array<Texture> loadedTextures = new Array<>();

    public BasicProjectile(float startX, float startY, boolean toRight, ProjectileType type,boolean isEnemy) {
        this.toRight = toRight;
        this.isEnemy = isEnemy;
        projectileType = type;
        this.damage = type.damage;
        this.speed = type.speed;
        this.pierce=type.peirce;

        Texture tmpSheet = new Texture("animation/Projectile/SoulBall.png");
        loadedTextures.add(tmpSheet);
        int frameWidthSoulBall = tmpSheet.getWidth() / 4;
        int frameHeightSoulBall = tmpSheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(tmpSheet, frameWidthSoulBall, frameHeightSoulBall);

        Array<TextureRegion> startFrames = new Array<>();
        for (int i = 0; i < 1; i++) {
            startFrames.add(tmp[0][i]);
        }

        SoulBallStartAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);


        Array<TextureRegion> frames = new Array<>();
        for (int i = 1; i < 4; i++) {
            frames.add(tmp[0][i]);
        }
        SoulBallAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        tmpSheet = new Texture("animation/Projectile/ShadowBall.png");
        loadedTextures.add(tmpSheet);
        frameWidthSoulBall = tmpSheet.getWidth() / 6;
        frameHeightSoulBall = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidthSoulBall, frameHeightSoulBall);

         startFrames = new Array<>();
        for (int i = 0; i < 2; i++) {
            startFrames.add(tmp[0][i]);
        }

        ShadowBallStartAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);

         frames = new Array<>();
        for (int i = 2; i < 6; i++) {
            frames.add(tmp[0][i]);
        }
        ShadowBallAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);



        tmpSheet = new Texture("animation/Projectile/Shockwave.png");
        loadedTextures.add(tmpSheet);
        int frameWidthShockwave = tmpSheet.getWidth() / 8;
        int frameHeightShockwave = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidthShockwave, frameHeightShockwave);
         startFrames = new Array<>();
        for (int i = 0; i < 2; i++) {
            startFrames.add(tmp[0][i]);
        }

        ShockWaveStartAnimation = new Animation<>(0.1f, startFrames, Animation.PlayMode.NORMAL);

        frames = new Array<>();
        for (int i = 2; i < 8; i++) {
            frames.add(tmp[0][i]);
        }
        ShockWaveAnimation= new Animation<>(0.1f, frames, Animation.PlayMode.LOOP);

        switch(projectileType) {
            case Wave:
                this.bounds = new Rectangle(startX-frameWidthShockwave*4/10f, startY-frameWidthShockwave/10f, frameWidthShockwave, frameHeightShockwave);
                this.hitbox = new Rectangle(bounds.x+bounds.width/8, bounds.y, bounds.width*3/4, bounds.height*3/5);
                break;
            case SoulBall:
            case ShadowBall:
                this.bounds = new Rectangle(startX-frameWidthSoulBall*7/10f, startY-frameWidthSoulBall/10f, frameWidthSoulBall, frameHeightSoulBall);
                if(!toRight){
                    bounds.x+=frameWidthSoulBall*0.4f;
                }
                this.hitbox = new Rectangle(bounds.x+bounds.width/4, bounds.y+bounds.height/4, bounds.width/2, bounds.height/2);
                break;
        }
        if (!toRight) {
            this.speed = -this.speed;
        }

    }

    @Override
    public void update(float delta, Array<SolidBlock> solidBlocks) {
        if (isDestroyed) return;

        bounds.x += speed * delta;
        updateHitbox();

        timer += delta;
        stateTimer += delta;
        if (timer >= lifespan) {
            isDestroyed = true;
        }

        Rectangle block;
        for (SolidBlock blocks : solidBlocks) {
            block=blocks.bounds;
            if (hitbox.overlaps(block)) {
                isDestroyed = true;
                break;
            }
        }
        if (projectileType == ProjectileType.Wave) {

            float accelerationRate = 2.5f;

            if (speed > 0) {
                speed += (speed * accelerationRate * delta);

                if (speed > 2000f) {
                    speed = 2000f;
                }
            } else {

                speed += (speed * accelerationRate * delta);

                if (speed < -2000f) {
                    speed = -2000f;
                }
            }
        }
    }

    private void updateHitbox() {
        switch(projectileType) {
            case Wave:
                hitbox.setPosition(bounds.x+ bounds.width/8, bounds.y+bounds.height/10);
                break;
            case SoulBall:
            case ShadowBall:
                hitbox.setPosition(bounds.x+bounds.width/4, bounds.y+bounds.height/4);

        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = null;

        switch (projectileType) {
            case SoulBall:
                if (!SoulBallStartAnimation.isAnimationFinished(stateTimer)) {
                    currentFrame = SoulBallStartAnimation.getKeyFrame(stateTimer);
                } else {
                    float loopTime = stateTimer - SoulBallStartAnimation.getAnimationDuration();
                    currentFrame = SoulBallAnimation.getKeyFrame(loopTime, true);
                }
                break;
            case ShadowBall:
                if (!ShadowBallStartAnimation.isAnimationFinished(stateTimer)) {
                    currentFrame = ShadowBallStartAnimation.getKeyFrame(stateTimer);
                } else {
                    float loopTime = stateTimer - ShadowBallStartAnimation.getAnimationDuration();
                    currentFrame = ShadowBallAnimation.getKeyFrame(loopTime, true);
                }
                break;
            case Wave:
                if (!ShockWaveStartAnimation.isAnimationFinished(stateTimer)) {
                    currentFrame = ShockWaveStartAnimation.getKeyFrame(stateTimer);
                } else {
                    float loopTime = stateTimer - ShockWaveStartAnimation.getAnimationDuration();
                    currentFrame = ShockWaveAnimation.getKeyFrame(loopTime, true);
                }
                break;
        }

        if (!isDestroyed && currentFrame != null) {
            if (toRight) {
                batch.draw(currentFrame, bounds.x, bounds.y, bounds.width, bounds.height);
            } else {
                batch.draw(currentFrame, bounds.x + bounds.width, bounds.y, -bounds.width, bounds.height);
            }
        }
    }

    @Override
    public void dispose() {
        for (Texture tex : loadedTextures) {
            tex.dispose();
        }
    }
    public boolean isEnemy(){
        return isEnemy;
    }

}
