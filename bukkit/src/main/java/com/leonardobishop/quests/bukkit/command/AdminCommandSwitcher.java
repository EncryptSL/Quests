package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

public class AdminCommandSwitcher extends CommandSwitcher {

    public AdminCommandSwitcher(BukkitQuestsPlugin plugin) {
        super(1);

        super.subcommands.put("opengui", new AdminOpenguiCommandSwitcher(plugin));
        super.subcommands.put("moddata", new AdminModdataCommandSwitcher(plugin));
        super.subcommands.put("types", new AdminTypesCommandHandler(plugin));
        super.subcommands.put("info", new AdminInfoCommandHandler(plugin));
        super.subcommands.put("reload", new AdminReloadCommandHandler(plugin));
        super.subcommands.put("items", new AdminItemsCommandHandler(plugin));
        super.subcommands.put("config", new AdminConfigCommandHandler(plugin));
        super.subcommands.put("migratedata", new AdminMigrateCommandHandler(plugin));
        super.subcommands.put("update", new AdminUpdateCommandHandler(plugin));
        super.subcommands.put("wiki", new AdminWikiCommandHandler(plugin));
        super.subcommands.put("about", new AdminAboutCommandHandler(plugin));
        super.subcommands.put("debug", new AdminDebugCommandSwitcher(plugin));
    }

    @Override
    public void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "------------=[" + ChatColor.RED + " Mise Admin " + ChatColor.GRAY
                .toString() + ChatColor.STRIKETHROUGH + "]=------------");
        sender.sendMessage(StringUtils.colorized( "&7K dispozici jsou následující příkazy: "));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a opengui &7: zobrazit nápovědu pro menu"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a moddata &7: zobrazit nápovědu pro postup v misích"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a types [type] &7: zobrazit registrované typy misí"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a info [quest] &7: zobrazit informace o načtených misích"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a items [import <id>] &7: zobrazit registrované itemy v misích"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a reload &7: znovu načíst konfiguraci"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a config &7: zobrazit problémy v konfiguraci"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a migratedata &7: migrace dat"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a update &7: zkontrolovat aktualizace"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a wiki &7: odkaz na wiki"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a about &7: informace o misích"));
        sender.sendMessage(StringUtils.colorized("&8&l| &e/mise a debug &7: zobrazit nápovědu pro debug"));
    }

    @Override
    public @Nullable String getPermission() {
        return "quests.admin";
    }
}
