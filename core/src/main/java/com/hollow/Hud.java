package com.hollow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;
    private Player player;
    private TextureRegion croppedSoulFrame;
    private Animation<TextureRegion> healthBarReverseAnimation;
    private float deathTimer = 0f;

    float shineTime=5f;
    float shineTimer=0f;


    Label scoreLabel;
    Label healthLabel;
    Label scoreTextLabel;
    Label healthTextLabel;

    private Animation<TextureRegion> healthBarAnimation;
    private Animation<TextureRegion> soulOrbFullAnimation;
    private Animation<TextureRegion> soulOrbHalfAnimation;
    private Animation<TextureRegion> filledHealthAnimation;
    private Animation<TextureRegion> emptyHealthAnimation;
    private Animation<TextureRegion> shineHealthAnimation;
    private Animation<TextureRegion> breakHealthAnimation;
    private Animation<TextureRegion> refillHealthAnimation;
    private float stateTimer;
    private float behavierTimer;

    protected Array<Texture> loadedTextures = new Array<>();

    private float maskAnimationTimer = 0f;
    private boolean isTakingDamage = false;
    private boolean isHealing = false;
    private int lastMask = -1;

    public Hud(SpriteBatch batch, Player player) {
        this.player = player;
        System.out.println(player.bounds);
        this.croppedSoulFrame = new TextureRegion();

        stateTimer = 0f;
        behavierTimer = 0f;
        Texture tmpSheet = new Texture("animation/HUD/HealthBar.png");
        loadedTextures.add(tmpSheet);
        int frameWidth = tmpSheet.getWidth() / 6;
        int frameHeight = tmpSheet.getHeight();
        TextureRegion[][] tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        Array<TextureRegion> frames = new Array<>();
        for (int i = 0; i < 6; i++) {
            frames.add(tmp[0][i]);
        }
        healthBarAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);
        healthBarReverseAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.REVERSED);

        tmpSheet = new Texture("animation/HUD/SoulOrb_Full.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 1; i++) {
            frames.add(tmp[0][i]);
        }

        soulOrbFullAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/HUD/SoulOrb_Half.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 1; i++) {
            frames.add(tmp[0][i]);
        }
        soulOrbHalfAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/HUD/EmptyHealth.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 1; i++) {
            frames.add(tmp[0][i]);
        }
        emptyHealthAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/HUD/FilledHealth.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth() ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 1; i++) {
            frames.add(tmp[0][i]);
        }
        filledHealthAnimation= new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/HUD/FilledHealthShine.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/5 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 5; i++) {
            frames.add(tmp[0][i]);
        }
        shineHealthAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/HUD/BreakHealth.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/6 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 6; i++) {
            frames.add(tmp[0][i]);
        }
        breakHealthAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        tmpSheet = new Texture("animation/HUD/HealthRefill.png");
        loadedTextures.add(tmpSheet);
        frameWidth = tmpSheet.getWidth()/5 ;
        frameHeight = tmpSheet.getHeight();
        tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
        frames = new Array<>();
        for (int i = 0; i < 5; i++) {
            frames.add(tmp[0][i]);
        }
        refillHealthAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

        viewport = new FitViewport(800, 480, new OrthographicCamera());
        stage = new Stage(viewport, batch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);




        table.add(healthTextLabel).expandX().padTop(50);
        table.add(scoreTextLabel).expandX().padTop(50);

        table.row();

        table.add(healthLabel).expandX();
        table.add(scoreLabel).expandX();

        stage.addActor(table);
    }

    public void drawElements(SpriteBatch batch,float delta) {

        stateTimer += delta;
        behavierTimer += delta;


        TextureRegion currentFrame ;
        if (player.isDead) {
            deathTimer += delta;
            currentFrame = healthBarReverseAnimation.getKeyFrame(deathTimer, false);
        } else {
            currentFrame = healthBarAnimation.getKeyFrame(stateTimer, false);
        }

        float x = 20f;
        float y = 400;
        float barSize = 80f;

        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();
        batch.draw(currentFrame, x, y, barSize*1.561f, barSize);

        float soulPercent =  player.currentSoul / player.maxSoul;
        TextureRegion currentSoulFrame;
        if(soulPercent > 0.5f) {
            currentSoulFrame = soulOrbFullAnimation.getKeyFrame(stateTimer);
        }
        else{
            currentSoulFrame = soulOrbHalfAnimation.getKeyFrame(stateTimer);
        }



        int originalWidth = currentSoulFrame.getRegionWidth();
        int originalHeight = currentSoulFrame.getRegionHeight();

        int paddingBottom = originalHeight/17;
        int paddingTop =originalHeight/4 ;

        int usableHeight = originalHeight - paddingTop - paddingBottom;
        int drawHeight = (int) (usableHeight * soulPercent);
        int startY = (originalHeight - paddingBottom) - drawHeight;

        croppedSoulFrame.setRegion(
            currentSoulFrame,
            0,
            startY,
            originalWidth,
            drawHeight
        );

        float scaleY = barSize / originalHeight;
        float offsetY = paddingBottom * scaleY;
        float barSize2 = drawHeight * scaleY;

        if(healthBarAnimation.isAnimationFinished(stateTimer)&&!player.isDead){
            batch.draw(croppedSoulFrame, x, y + offsetY, barSize * 1.561f, barSize2);

            float cupSize = barSize / 1.5f;
            float cupWidth = cupSize * 0.754491018f;

            if (isTakingDamage || isHealing) {
                maskAnimationTimer += delta;
            }

            shineTimer += delta;

            boolean isShining = false;
            float currentShineTime = 0f;

            if (shineTimer > shineTime) {
                isShining = true;
                currentShineTime = shineTimer - shineTime;

                if (shineHealthAnimation.isAnimationFinished(currentShineTime)) {
                    shineTimer = 0f;
                    isShining = false;
                }
            }

            for(int i = 0; i < player.maxHealth; i++){
                TextureRegion maskFrame;

                float maskX = x + (i + 2) * cupWidth;
                float maskY = y + offsetY + barSize / 5;

                if (isTakingDamage && i ==lastMask) {
                    maskFrame = breakHealthAnimation.getKeyFrame(maskAnimationTimer);

                    if (breakHealthAnimation.isAnimationFinished(maskAnimationTimer)) {
                        isTakingDamage = false;
                    }
                }
                else if (isHealing && i == lastMask) {
                    maskFrame = refillHealthAnimation.getKeyFrame(maskAnimationTimer);
                    if (refillHealthAnimation.isAnimationFinished(maskAnimationTimer)) {
                        isHealing = false;
                    }
                }
                else if (i < player.currentHealth) {
                    if (isShining) {
                        maskFrame = shineHealthAnimation.getKeyFrame(currentShineTime);
                    } else {

                        maskFrame = filledHealthAnimation.getKeyFrame(stateTimer);
                    }

                }
                else {
                    maskFrame = emptyHealthAnimation.getKeyFrame(stateTimer);
                }

                batch.draw(maskFrame, maskX, maskY, cupWidth, cupSize);
            }
        }


        batch.end();
    }



    @Override
    public void dispose() {
        stage.dispose();
        for (Texture tex : loadedTextures) {
            tex.dispose();
        }
    }
    public void triggerDamageAnimation() {
        isTakingDamage = true;
        isHealing = false;
        maskAnimationTimer = 0f;


        lastMask = player.currentHealth;
    }

    public void triggerHealAnimation() {
        isHealing = true;
        isTakingDamage = false;
        maskAnimationTimer = 0f;

        lastMask = player.currentHealth - 1;
    }
}
