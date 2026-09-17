package com.hollow.Charm;

import com.hollow.Player;

public class ShadowHeart extends Charm{

    public ShadowHeart() { super("Void Heart"); }

    @Override
    public void onEquip(Player player) {
        player.setShadowHeart(true);
    }

    @Override
    public void onUnequip(Player player) {
        player.setShadowHeart(false);
    }
}
