package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.CommandUtils;
import com.leonardobishop.quests.bukkit.util.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AdminReloadCommandHandler implements CommandHandler {

    private final BukkitQuestsPlugin plugin;

    public AdminReloadCommandHandler(BukkitQuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        sender.sendMessage(StringUtils.colorized("&7Některé změny, například uložitě mohou vyžadovat úplný restart."));
        plugin.reloadConfig();
        plugin.reloadQuests();
        if (!plugin.getConfigProblems().isEmpty()) CommandUtils.showProblems(sender, plugin.getConfigProblems());
        sender.sendMessage(StringUtils.colorized("&aKonfigurace se znovu načetla."));
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPermission() {
        return "quests.admin";
    }
}
