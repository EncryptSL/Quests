package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.CommandUtils;
import com.leonardobishop.quests.bukkit.util.MenuUtils;
import com.leonardobishop.quests.bukkit.util.Messages;
import com.leonardobishop.quests.bukkit.util.lang3.StringUtils;
import com.leonardobishop.quests.common.player.QPlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuestsCommandSwitcher extends CommandSwitcher implements TabExecutor {

    private final BukkitQuestsPlugin plugin;

    public QuestsCommandSwitcher(BukkitQuestsPlugin plugin) {
        super(0);
        this.plugin = plugin;

        super.subcommands.put("quest", new QuestCommandHandler(plugin));
        super.subcommands.put("category", new CategoryCommandHandler(plugin));
        super.subcommands.put("random", new RandomCommandHandler(plugin));
        super.subcommands.put("started", new StartedCommandHandler(plugin));
        super.subcommands.put("admin", new AdminCommandSwitcher(plugin));
        super.subcommands.put("start", new StartCommandHandler(plugin));
        super.subcommands.put("track", new TrackCommandHandler(plugin));
        super.subcommands.put("cancel", new CancelCommandHandler(plugin));

        super.aliases.put("q", "quest");
        super.aliases.put("c", "category");
        super.aliases.put("a", "admin");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (plugin.getTaskTypeManager().areRegistrationsAccepted()) {
            sender.sendMessage(ChatColor.RED + "Quests is not ready yet.");
            return true;
        }
        if (!plugin.isValidConfiguration()
                && !(args.length >= 2 && (args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("admin"))
                && args[1].equalsIgnoreCase("reload"))) {
            sender.sendMessage(ChatColor.RED + "Mise nyní nelze použít. Obraťte se prosím na správce.");
            if (sender.hasPermission("quests.admin")) {
                CommandUtils.showProblems(sender, plugin.getConfigProblems());
                sender.sendMessage(ChatColor.RED + "Před použitím misí musí být hlavní konfigurace (config.yml) v pořádku. " +
                        "Použijte prosím výše uvedené informace, které vám pomohou problém odstranit.");
            }
            return true;
        }

        if (args.length == 0 && sender instanceof Player) {
            Player player = (Player) sender;
            QPlayer qPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
            if (qPlayer == null) {
                Messages.COMMAND_DATA_NOT_LOADED.send(player);
                return true;
            }
            MenuUtils.openMainMenu(plugin, qPlayer);
            return true;
        }

        super.handle(sender, args);
        return true;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return super.tabComplete(sender, args);
    }

    @Override
    public void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------=[" + ChatColor.RED + " Quests v" + plugin
                .getDescription().getVersion() + " " + ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "]=------------");
        sender.sendMessage(StringUtils.colorized("&7K dispozici jsou následující příkazy: "));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/quests &7: zobrazí mise"));
        if (sender.hasPermission(subcommands.get("category").getPermission())) {
            sender.sendMessage(StringUtils.colorized("&8&l| &e/mise c/category <categoryid> &7: otevře kategorii podle ID"));
        }
        if (sender.hasPermission(subcommands.get("started").getPermission())) {
            sender.sendMessage(StringUtils.colorized("&8&l| &e/mise started &7: zobrazí mise v průběhu"));
        }
        if (sender.hasPermission(subcommands.get("quest").getPermission())) {
            sender.sendMessage(StringUtils.colorized("&8&l| &e/mise q/quest <questid> (start|cancel|track) &7: začne, zruší nebo bude sledovat misi podle ID"));
        }
        if (sender.hasPermission(subcommands.get("start").getPermission())) {
            sender.sendMessage(StringUtils.colorized("&8&l| &e/mise start <questid> &7: začít misi podle jména"));
        }
        if (sender.hasPermission(subcommands.get("track").getPermission())) {
            sender.sendMessage(StringUtils.colorized("&8&l| &e/mise track <questid> &7: sledovat misi podle jména"));
        }
        if (sender.hasPermission(subcommands.get("cancel").getPermission())) {
            sender.sendMessage(StringUtils.colorized("&8&l| &e/mise cancel [questid] &7: zrušit aktivovanou misi"));
        }
        if (sender.hasPermission(subcommands.get("random").getPermission())) {
            sender.sendMessage(StringUtils.colorized("&8&l| &e/mise random &7: zobrazit náhodnou misi"));
        }
        if (sender.hasPermission(subcommands.get("admin").getPermission())) {
            sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a/admin &7: zobrazit admin příkazy"));
        }
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "--------=[" + ChatColor.RED + " made with <3 by LMBishop " + ChatColor
                .GRAY.toString() + ChatColor.STRIKETHROUGH + "]=--------");
    }

    @Override
    public @Nullable String getPermission() {
        return null;
    }
}
