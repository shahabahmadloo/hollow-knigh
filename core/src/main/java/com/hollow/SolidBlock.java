package com.hollow;

import com.badlogic.gdx.math.Rectangle;

public class SolidBlock {
    public Rectangle bounds;
    public boolean isDeadly;
    public boolean breakable;

    public SolidBlock(float x, float y, float width, float height, boolean isDeadly, boolean breakable ) {
        this.bounds = new Rectangle(x, y, width, height);
        this.isDeadly = isDeadly;
        this.breakable = breakable;
    }
}
