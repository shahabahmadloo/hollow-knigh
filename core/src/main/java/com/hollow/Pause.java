package com.hollow;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hollow.Charm.Charm;

public class Pause {
    public Stage stage;
    private Player player;
    private boolean isVisible = false;
    private Array<Texture> charmTextures = new Array<>();

    public Pause(Viewport viewport, Player player) {
        this.player = player;
        stage = new Stage(viewport);

        Table table = new Table();
        table.setFillParent(true);

        int col = 0;
        System.out.println("hdk[h");
        for (final Charm charm : player.ownedCharms) {
            Texture tex = new Texture("charms/" + charm.name + ".png");
            if(tex != null) {
                System.out.println("Loading charm " + charm.name);
            }
            charmTextures.add(tex);

            final Image charmImage = new Image(tex);

            if (charm.isEquipped) {
                charmImage.setColor(Color.WHITE);
            } else {
                charmImage.setColor(Color.DARK_GRAY);
            }

            charmImage.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (charm.isEquipped) {
                        player.unequipCharm(charm);
                        charmImage.setColor(Color.DARK_GRAY); // تاریکش کن
                    } else {
                        boolean equipped = player.equipCharm(charm);
                        if (equipped) {
                            charmImage.setColor(Color.WHITE);
                        } else {
                            System.out.println("Maximum 3 charms allowed!");
                        }
                    }
                }
            });

            table.add(charmImage).size(64, 64).pad(20);

            col++;
            if (col == 4) {
                table.row();
                col = 0;
            }
        }

        stage.addActor(table);
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
        if (visible) {
            Gdx.input.setInputProcessor(stage);
        } else {
            Gdx.input.setInputProcessor(null);
        }
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void draw() {
        if (isVisible) {
            stage.act();
            stage.draw();
        }
    }

    public void dispose() {
        stage.dispose();
        for (Texture tex : charmTextures) {
            tex.dispose();
        }
    }
}
