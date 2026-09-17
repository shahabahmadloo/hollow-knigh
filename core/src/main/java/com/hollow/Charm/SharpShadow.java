package com.hollow.Charm;

import com.hollow.Player;

public class SharpShadow extends Charm{

    public SharpShadow() { super("Sharp Shadow"); }

    @Override
    public void onEquip(Player player) {
        player.setSharpShadow(true);
    }

    @Override
    public void onUnequip(Player player) {
        player.setSharpShadow(false);
    }
}
