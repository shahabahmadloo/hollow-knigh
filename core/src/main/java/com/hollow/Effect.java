package com.hollow;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Effect {
    private Animation<TextureRegion> animation;
    private float x, y;
    private float stateTimer;
    private boolean remove;
    private boolean flipX;
    private float width ;
    private float height ;
    public boolean behindPlayer;

    public Effect(Animation<TextureRegion> animation, float x, float y,float width,float height, boolean flipX,boolean behindPlayer) {
        this.animation = animation;
        this.x = x;
        this.y = y;
        this.flipX = flipX;
        this.stateTimer = 0f;
        this.remove = false;
        this.width =width;
        this.height = height;
        this.behindPlayer = behindPlayer;


    }

    public void update(float delta) {
        stateTimer += delta;
        if (animation.isAnimationFinished(stateTimer)) {
            remove = true;
        }
    }

    public void draw(SpriteBatch batch) {
        TextureRegion currentFrame = animation.getKeyFrame(stateTimer);

        if (flipX) {
            batch.draw(currentFrame, x + width, y, -width, height);
        } else {
            batch.draw(currentFrame, x, y, width, height);
        }
    }

    public boolean isFinished() {
        return remove;
    }
}
