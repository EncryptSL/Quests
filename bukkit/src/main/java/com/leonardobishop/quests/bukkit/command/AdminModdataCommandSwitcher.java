package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class AdminModdataCommandSwitcher extends CommandSwitcher {

    private final BukkitQuestsPlugin plugin;

    public AdminModdataCommandSwitcher(BukkitQuestsPlugin plugin) {
        super(2);
        this.plugin = plugin;

        super.subcommands.put("fullreset", new AdminModdataFullresetCommandHandler(plugin));
        super.subcommands.put("start", new AdminModdataStartCommandHandler(plugin));
        super.subcommands.put("reset", new AdminModdataResetCommandHandler(plugin));
        super.subcommands.put("complete", new AdminModdataCompleteCommandHandler(plugin));
        super.subcommands.put("random", new AdminModdataRandomCommandHandler(plugin));
    }

    @Override
    public void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------=[" + ChatColor.RED + " Mise Admin: moddata " + ChatColor
                .GRAY.toString() + ChatColor.STRIKETHROUGH + "]=------------");
        sender.sendMessage(StringUtils.colorized( "&7K dispozici jsou následující příkazy: "));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a moddata fullreset <player> &7: clear a players quest data file"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a moddata reset <player> <questid> &7: clear a players data for specifc quest"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a moddata start <player> <questid> &7: start a quest for a player"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a moddata complete <player> <questid> &7: complete a quest for a player"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a moddata random <player> [category] &7: start a random quest for a player [in a specific category]"));
        sender.sendMessage(StringUtils.colorized("&7Tyto příkazy upravují postup hráčů při plnění úkolů. Používejte je opatrně. Změny jsou nevratné."));
    }

    @Override
    public @Nullable String getPermission() {
        return "quests.admin";
    }
}
