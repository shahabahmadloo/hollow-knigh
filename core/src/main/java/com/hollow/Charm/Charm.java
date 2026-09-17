package com.hollow.Charm;

import com.hollow.Player;

public abstract class Charm {
    public String name;
    public boolean isEquipped = false;

    public Charm(String name) {
        this.name = name;
    }

    public abstract void onEquip(Player player);

    public abstract void onUnequip(Player player);
}
