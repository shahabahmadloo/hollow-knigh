package com.hollow;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Crawler extends GroundEnemy {

    private Animation<TextureRegion> walkAnimation;
    private Animation<TextureRegion> turnAnimation;
    private Animation<TextureRegion> thrownToDeathAnimation;
    private Animation<TextureRegion> deathAnimation;
    private CrawlerType crawlerType;

    public Crawler(float startX, float startY, float width, float height,CrawlerType crawlerType, CameraFunctions cameraFunctions) {
        super(startX, startY, width, height, crawlerType.hp, crawlerType.speed, cameraFunctions);

        this.crawlerType = crawlerType;
        currentState = State.WALKING;
        previousState = State.WALKING;
        Texture tmpSheet;
        int frameWidth;
        int frameHeight;
        TextureRegion[][] tmp;
        Array<TextureRegion> frames;
        switch(crawlerType) {
            case Crawlid:
                hitbox = new Rectangle(startX + width * 3f / 4, startY, width * 3f / 8, height * 3f / 8f);
                tmpSheet = new Texture("animation/Crawlid/Walk.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 4;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 4; i++) {
                    frames.add(tmp[0][i]);
                }
                walkAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Crawlid/Turn.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Crawlid/Death Air.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 3;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 3; i++) {
                    frames.add(tmp[0][i]);
                }
                thrownToDeathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Crawlid/Death Land.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                deathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);
                break;
            case Crystal_Crawler:
                hitbox = new Rectangle(startX + width * 3f / 4, startY, width * 3f / 8, height * 3f / 8f);
                tmpSheet = new Texture("animation/Crystal_Crawler/Walk.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 4;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 4; i++) {
                    frames.add(tmp[0][i]);
                }
                walkAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Crystal_Crawler/Turn.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Crystal_Crawler/Death Air.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 3;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 3; i++) {
                    frames.add(tmp[0][i]);
                }
                thrownToDeathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Crystal_Crawler/Death Land.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                deathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);
                break;
            case MossCreep:
                hitbox = new Rectangle(startX + width * 3f / 4, startY, width * 3f / 8, height * 3f / 8f);
                tmpSheet = new Texture("animation/Mosscreep/Walk.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 3;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 3; i++) {
                    frames.add(tmp[0][i]);
                }
                walkAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Mosscreep/Turn.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 3;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 3; i++) {
                    frames.add(tmp[0][i]);
                }
                turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Mosscreep/Death Air.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 3;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 3; i++) {
                    frames.add(tmp[0][i]);
                }
                thrownToDeathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Mosscreep/Death Land.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                deathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);
                break;
            case Tiktik:
                hitbox = new Rectangle(startX + width * 3f / 4, startY, width * 3f / 8, height * 3f / 8f);
                tmpSheet = new Texture("animation/Tiktik/Walk.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 4;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 4; i++) {
                    frames.add(tmp[0][i]);
                }
                walkAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.LOOP);

                tmpSheet = new Texture("animation/Tiktik/Turn.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                turnAnimation = new Animation<>(0.1f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Tiktik/Death Air.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 4;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 4; i++) {
                    frames.add(tmp[0][i]);
                }
                thrownToDeathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);

                tmpSheet = new Texture("animation/Tiktik/Death Land.png");
                loadedTextures.add(tmpSheet);
                frameWidth = tmpSheet.getWidth() / 2;
                frameHeight = tmpSheet.getHeight();
                tmp = TextureRegion.split(tmpSheet, frameWidth, frameHeight);
                frames = new Array<>();
                for (int i = 0; i < 2; i++) {
                    frames.add(tmp[0][i]);
                }
                deathAnimation = new Animation<>(0.15f, frames, Animation.PlayMode.NORMAL);
                break;

        }
    }

    @Override
    public void update(float delta, Array<SolidBlock> solidBlocks, Array<Effect> worldEffects, Player player) {
        previousState = currentState;
        stateTimer += delta;

        if(isOnGround) {
            if (isDead) {
                speed = 0;
            } else if (currentState != State.TURNING) {
                speed = super.baseSpeed;
            }
        }

        if (!isDead && currentState == State.TURNING) {
            speed = 0;

            if (turnAnimation.isAnimationFinished(stateTimer)) {
                toRight = !toRight;
                currentState = State.WALKING;
                speed = super.baseSpeed;
                stateTimer = 0f;
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
        TextureRegion currentFrame = null;

        switch (currentState) {
            case DEAD:
                if (!isOnGround) {
                    currentFrame = thrownToDeathAnimation.getKeyFrame(stateTimer);
                } else {
                    currentFrame = deathAnimation.getKeyFrame(stateTimer);
                }
                break;
            case WALKING:
                currentFrame = walkAnimation.getKeyFrame(stateTimer);
                break;
            case TURNING:
                currentFrame = turnAnimation.getKeyFrame(stateTimer);
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
        if(crawlerType==CrawlerType.Tiktik) {
            hitbox.width = bounds.width*5/8;
            hitbox.height = bounds.height/1.2f;
            hitbox.x = bounds.x + bounds.width * 1.5f / 8;
            hitbox.y = bounds.y+bounds.height * 1.5f / 8;

        }else if(crawlerType==CrawlerType.Crystal_Crawler) {
            hitbox.x = bounds.x + bounds.width * 1.5f / 8;
            hitbox.width = bounds.width * 5 / 8;
            hitbox.y = bounds.y ;
            hitbox.height = bounds.height / 1.2f;
        }else if(crawlerType==CrawlerType.MossCreep) {
            hitbox.x = bounds.x + bounds.width * 1.5f / 8;
            hitbox.width = bounds.width * 5 / 8;
            hitbox.y = bounds.y ;
            hitbox.height = bounds.height / 1.2f;
            if(isDead){
                hitbox.x = bounds.x + bounds.width * 1.5f / 8;
                hitbox.y = bounds.y+bounds.height * 1.0f / 8;
            }else{
                hitbox.x = bounds.x + bounds.width * 1.5f / 8;
                hitbox.y = bounds.y;
            }
        }else {
            hitbox.x = bounds.x + bounds.width * 2.5f / 8;
            hitbox.y = bounds.y;
        }

    }

    @Override
    public void turnAround() {
        if (isDead) return;
        if ( currentState != State.TURNING) {
            previousState = currentState;
            currentState = State.TURNING;
            stateTimer = 0f;
            speed = 0f;
        }
    }

    public boolean isReadyToDespawn() {
        return currentState == State.DEAD && isOnGround && deathAnimation.isAnimationFinished(stateTimer);
    }
}
