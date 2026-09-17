package com.hollow.Charm;

import com.hollow.Player;

public class DashMaster extends Charm {
    public DashMaster() {
        super("Dash Master");
    }
    @Override public void onEquip(Player player) { player.dashCooldown = 0.4f; }
    @Override public void onUnequip(Player player) { player.dashCooldown = 0.8f; }
}

