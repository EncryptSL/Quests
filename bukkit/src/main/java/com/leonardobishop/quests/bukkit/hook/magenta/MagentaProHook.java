package com.leonardobishop.quests.bukkit.hook.magenta;

import com.github.encryptsl.magenta.Magenta;
import org.bukkit.Bukkit;

import java.util.UUID;

public class MagentaProHook implements AbstractMagentaProHook {

    private final Magenta magenta;

    public MagentaProHook() {
        magenta = ((Magenta) Bukkit.getPluginManager().getPlugin("MagentaPro"));
    }

    @Override
    public boolean isAfk(UUID uuid) {
        return magenta.getUser().getUser(uuid).isAfk();
    }
}
