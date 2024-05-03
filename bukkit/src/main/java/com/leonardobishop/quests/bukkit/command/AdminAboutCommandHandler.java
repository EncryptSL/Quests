package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AdminAboutCommandHandler implements CommandHandler {

    private final BukkitQuestsPlugin plugin;

    public AdminAboutCommandHandler(BukkitQuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        sender.sendMessage(StringUtils.colorized("&cQuests fork created from https://github.com/LMBishop/Quests/"));
        sender.sendMessage(StringUtils.colorized("&8- &cZdrojový kód originálu: &ehttps://github.com/LMBishop/Quests/"));
        sender.sendMessage(StringUtils.colorized("&8- &cZdrojový kód forku: &ehttps://github.com/EncryptSL/Quests"));
        sender.sendMessage(" ");
        sender.sendMessage(StringUtils.colorized("&8- cLicencováno pod GPLv3"));
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
