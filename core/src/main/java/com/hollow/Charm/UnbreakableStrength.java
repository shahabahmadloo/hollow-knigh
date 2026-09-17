package com.hollow.Charm;


import com.hollow.Player;

public class UnbreakableStrength extends Charm {
    public UnbreakableStrength() {
        super("Unbreakable Strength");
    }
    @Override public void onEquip(Player player) { player.nailDamage = 2; }
    @Override public void onUnequip(Player player) { player.nailDamage = 1; }
}
