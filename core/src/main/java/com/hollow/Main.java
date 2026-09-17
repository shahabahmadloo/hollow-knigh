package com.hollow;

import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        setScreen(new PlayScreen());
    }

    @Override
    public void render() {
        super.render();
    }
}
