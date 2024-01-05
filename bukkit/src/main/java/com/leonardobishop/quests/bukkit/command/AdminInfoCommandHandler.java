package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.Messages;
import com.leonardobishop.quests.common.quest.Quest;
import com.leonardobishop.quests.common.quest.Task;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AdminInfoCommandHandler implements CommandHandler {

    private final BukkitQuestsPlugin plugin;

    public AdminInfoCommandHandler(BukkitQuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (args.length == 2) {
            sender.sendMessage(ChatColor.GRAY + "Načtené mise:");
            int i = 0;
            for (Quest quest : plugin.getQuestManager().getQuests().values()) {
                sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.RED + quest.getId() + ChatColor.GRAY + " [" + quest.getTasks().size() + " mise]");
                i++;
                if (i == 25 && plugin.getQuestManager().getQuests().size() > 25) {
                    sender.sendMessage(ChatColor.DARK_GRAY + " ... a " + (plugin.getQuestManager().getQuests().size() - 25) + " víc ...");
                    break;
                }
            }
            sender.sendMessage(ChatColor.GRAY + "Mise controller: " + ChatColor.RED + plugin.getQuestController().getName());
            sender.sendMessage(ChatColor.GRAY.toString() + plugin.getQuestManager().getQuests().size() + " registrované.");
            sender.sendMessage(ChatColor.DARK_GRAY + "[?] /mise a info [quest].");
        } else {
            Quest quest = plugin.getQuestManager().getQuestById(args[2]);
            if (quest == null) {
                Messages.COMMAND_QUEST_GENERAL_DOESNTEXIST.send(sender, "{quest}", args[2]);
            } else {
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Informace o misi '" + quest.getId() + "'");
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE + "Task configurations (" + quest.getTasks().size() + ")");
                for (Task task : quest.getTasks()) {
                    sender.sendMessage(ChatColor.RED + "Task '" + task.getId() + "':");
                    for (Map.Entry<String, Object> config : task.getConfigValues().entrySet()) {
                        sender.sendMessage(ChatColor.DARK_GRAY + " | " + ChatColor.GRAY + config.getKey() + ": " + ChatColor.GRAY + ChatColor.ITALIC + config.getValue());
                    }
                }
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE +  "Start string");
                for (String s : quest.getStartString()) {
                    sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.GRAY + s);
                }
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE +  "Reward string");
                for (String s : quest.getRewardString()) {
                    sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.GRAY + s);
                }
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE +  "Odměny");
                for (String s : quest.getRewards()) {
                    sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.GRAY + s);
                }
                sender.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE +  "Quest options");
                sender.sendMessage(ChatColor.RED + "Kategorie: " + ChatColor.GRAY + quest.getCategoryId());
                sender.sendMessage(ChatColor.RED + "Opakovatelný: " + ChatColor.GRAY + quest.isRepeatable());
                sender.sendMessage(ChatColor.RED + "Požadavky: " + ChatColor.GRAY + String.join(", ", quest.getRequirements()));
                sender.sendMessage(ChatColor.RED + "Cooldown enabled: " + ChatColor.GRAY + quest.isCooldownEnabled());
                sender.sendMessage(ChatColor.RED + "Cooldown time: " + ChatColor.GRAY + quest.getCooldown());
                sender.sendMessage(ChatColor.RED + "Atomatický start: " + ChatColor.GRAY + quest.isAutoStartEnabled());
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 3) {
            return TabHelper.tabCompleteQuests(args[2]);
        }
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPermission() {
        return "quests.admin";
    }
}
