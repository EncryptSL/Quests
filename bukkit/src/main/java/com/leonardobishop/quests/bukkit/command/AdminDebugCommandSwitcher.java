package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class AdminDebugCommandSwitcher extends CommandSwitcher {

    public AdminDebugCommandSwitcher(BukkitQuestsPlugin plugin) {
        super(2);

        super.subcommands.put("quest", new AdminDebugQuestCommandHandler(plugin));
        super.subcommands.put("report", new AdminDebugReportCommandHandler(plugin));
    }

    @Override
    public void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------=[" + ChatColor.RED + " Quests Admin: debug " + ChatColor
                .GRAY + ChatColor.STRIKETHROUGH + "]=------------");
        sender.sendMessage(StringUtils.colorized( "&7K dispozici jsou následující příkazy: "));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/quests a debug report &7: generate a debug report"));
        sender.sendMessage("&8&l| &e/quests a debug quest <player> <self|all> &7: enable debug logging for a specific quest");
    }

    @Override
    public @Nullable String getPermission() {
        return "quests.admin";
    }
}
