package com.leonardobishop.quests.bukkit.hook.versionspecific;

import org.bukkit.entity.*;

public class VersionSpecificHandler11 extends VersionSpecificHandler9 implements VersionSpecificHandler {

    @Override
    public int getMinecraftVersion() {
        return 11;
    }

    @Override
    public boolean isPlayerOnDonkey(Player player) {
        return player.getVehicle() instanceof Donkey;
    }

    @Override
    public boolean isPlayerOnHorse(Player player) {
        return player.getVehicle() instanceof Horse;
    }

    @Override
    public boolean isPlayerOnLlama(Player player) {
        return player.getVehicle() instanceof Llama;
    }

    @Override
    public boolean isPlayerOnMule(Player player) {
        return player.getVehicle() instanceof Mule;
    }

    @Override
    public boolean isPlayerOnSkeletonHorse(Player player) {
        return player.getVehicle() instanceof SkeletonHorse;
    }

    @Override
    public boolean isPlayerOnZombieHorse(Player player) {
        return player.getVehicle() instanceof ZombieHorse;
    }
}
