package com.hollow.Charm;

import com.hollow.Player;

public class HeavyBlow extends Charm {
    public HeavyBlow() {
        super("Heavy Blow");
    }
    @Override public void onEquip(Player player) { player.knockbackMultiplier = 1.7f; }
    @Override public void onUnequip(Player player) { player.knockbackMultiplier = 1.0f; }
}
