package com.hollow.Charm;

import com.hollow.Player;

public class QuickSlash extends Charm {
    public QuickSlash() { super("Quick Slash"); }
    @Override public void onEquip(Player player) { player.attackSpeedMultiplier = 1.5f; }
    @Override public void onUnequip(Player player) { player.attackSpeedMultiplier = 1.0f; }
}
