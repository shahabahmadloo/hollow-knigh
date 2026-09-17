package com.hollow;

import com.badlogic.gdx.utils.Array;

public abstract class Enemy extends Entity {

    public enum State { IDLE, WALKING, TURNING, DEAD, ANTICIPATING, LUNGING, COOLDOWN ,JUMPING}
    public State currentState = State.WALKING;
    public State previousState = State.WALKING;
    public int dmg=1;


    public Enemy(float startX, float startY, float width, float height, int maxHealth, CameraFunctions cameraFunctions) {
        super(startX, startY, width, height, cameraFunctions);
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
        this.isDead = false;
        invincibilityDuration=0.3f;
    }



    abstract void update(float delta, Array<SolidBlock> solidBlocks, Array<Effect> worldEffects,Player player);
}
