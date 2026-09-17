package com.hollow.Charm;

import com.hollow.Player;

public class SoulCatcher extends Charm {
    public SoulCatcher() {
        super("Soul Catcher");
    }
    @Override public void onEquip(Player player) { player.gainingSoul = 8; }
    @Override public void onUnequip(Player player) { player.gainingSoul = 5; }
}

