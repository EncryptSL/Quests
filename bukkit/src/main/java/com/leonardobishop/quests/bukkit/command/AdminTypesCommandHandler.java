package com.leonardobishop.quests.bukkit.command;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.util.Messages;
import com.leonardobishop.quests.bukkit.util.lang3.StringUtils;
import com.leonardobishop.quests.common.tasktype.TaskType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdminTypesCommandHandler implements CommandHandler {

    private final BukkitQuestsPlugin plugin;

    public AdminTypesCommandHandler(BukkitQuestsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void handle(CommandSender sender, String[] args) {
        if (args.length == 2) {
            sender.sendMessage(StringUtils.colorized("&7Registrované typy misí:"));
            for (TaskType taskType : plugin.getTaskTypeManager().getTaskTypes()) {
                sender.sendMessage(ChatColor.DARK_GRAY + " * " + ChatColor.RED + taskType.getType());
            }
            sender.sendMessage(ChatColor.GRAY.toString() + plugin.getTaskTypeManager().getTaskTypes().size() + " registrovaných.");
            sender.sendMessage(StringUtils.colorized("&8Zobrazit informace pomocí /mise a types [type]."));
        } else {
            TaskType taskType = null;
            for (TaskType task : plugin.getTaskTypeManager().getTaskTypes()) {
                if (task.getType().equals(args[2])) {
                    taskType = task;
                }
            }
            if (taskType == null) {
                Messages.COMMAND_TASKVIEW_ADMIN_FAIL.send(sender, "{task}", args[2]);
            } else {
                sender.sendMessage(StringUtils.colorized("&cTyp mise: &7" + taskType.getType()));
                sender.sendMessage(StringUtils.colorized("&cAuthor: &7" + taskType.getAuthor()));
                sender.sendMessage(StringUtils.colorized("&cPopis: &7" + taskType.getDescription()));
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        if (args.length == 3) {
            List<String> options = new ArrayList<>();
            for (TaskType taskType : plugin.getTaskTypeManager().getTaskTypes()) {
                options.add(taskType.getType());
            }
            return TabHelper.matchTabComplete(args[2], options);
        }
        return Collections.emptyList();
    }

    @Override
    public @Nullable String getPermission() {
        return "quests.admin";
    }
}
