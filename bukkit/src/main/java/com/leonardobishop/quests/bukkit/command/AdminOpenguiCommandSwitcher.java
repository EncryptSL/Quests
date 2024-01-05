package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class AdminOpenguiCommandSwitcher extends CommandSwitcher {

    private final BukkitQuestsPlugin plugin;

    public AdminOpenguiCommandSwitcher(BukkitQuestsPlugin plugin) {
        super(2);
        this.plugin = plugin;

        super.subcommands.put("quest", new AdminOpenguiQuestCommandHandler(plugin));
        super.subcommands.put("category", new AdminOpenguiCategoryCommandHandler(plugin));
        super.subcommands.put("started", new AdminOpenguiStartedCommandHandler(plugin));

        super.aliases.put("q", "quest");
        super.aliases.put("quests", "quest");
        super.aliases.put("c", "category");
        super.aliases.put("categories", "category");
        super.aliases.put("s", "started");
    }

    @Override
    public void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------=[" + ChatColor.RED + " Quests Admin: opengui " + ChatColor
                .GRAY.toString() + ChatColor.STRIKETHROUGH + "]=------------");
        sender.sendMessage(StringUtils.colorized("&7K dispozici jsou následující příkazy: "));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a opengui q/quest <player> &7: forcefully show quests for player"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a opengui s/started <player> &7: forcefully show started quests for player"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a opengui c/category <player> <category> &7: forcefully open category by ID for player"));
        sender.sendMessage(StringUtils.colorized("&7Tyto příkazy jsou užitečné pro příkazy NPC. Obejdou obvyklé povolení quests.command."));
    }

    @Override
    public @Nullable String getPermission() {
        return "quests.admin";
    }
}
