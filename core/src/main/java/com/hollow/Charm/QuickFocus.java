package com.hollow.Charm;


import com.hollow.Player;

public class QuickFocus extends Charm {
    public QuickFocus() { super("Quick Focus"); }
    @Override public void onEquip(Player player) { player.focusSpeedMultiplier = 1.5f; }
    @Override public void onUnequip(Player player) { player.focusSpeedMultiplier = 1.0f; }
}
