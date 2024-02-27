package com.leonardobishop.quests.bukkit.tasktype.type.dependent;

import com.github.encryptsl.magenta.api.events.shop.CreditShopBuyEvent;
import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.tasktype.BukkitTaskType;
import com.leonardobishop.quests.bukkit.util.TaskUtils;
import com.leonardobishop.quests.common.player.QPlayer;
import com.leonardobishop.quests.common.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.common.quest.Quest;
import com.leonardobishop.quests.common.quest.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.UUID;

public class MagentaProCreditShopTaskType extends BukkitTaskType {
    private final BukkitQuestsPlugin plugin;
    public MagentaProCreditShopTaskType(BukkitQuestsPlugin plugin) {
        super("credit_shop_buy", TaskUtils.TASK_ATTRIBUTION_STRING, "Buy something from creditshop", "cshop_buy");
        this.plugin = plugin;
        super.addConfigValidator(TaskUtils.useBooleanConfigValidator(this, "price-expect"));
        super.addConfigValidator(TaskUtils.useRequiredConfigValidator(this, "amount"));
        super.addConfigValidator(TaskUtils.useIntegerConfigValidator(this, "amount"));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreditShopBuy(CreditShopBuyEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        Player player = Bukkit.getPlayer(uuid);
        if (player == null || player.hasMetadata("NPC")) {
            return;
        }

        QPlayer qPlayer = plugin.getPlayerManager().getPlayer(uuid);
        if (qPlayer == null) {
            return;
        }

        for (TaskUtils.PendingTask pendingTask : TaskUtils.getApplicableTasks(player, qPlayer, this)) {
            Quest quest = pendingTask.quest();
            Task task = pendingTask.task();
            TaskProgress taskProgress = pendingTask.taskProgress();

            super.debug("Player buy item from creditshop", quest.getId(), task.getId(), player.getUniqueId());

            int buyNeeded = (int) task.getConfigValue("amount");
            int progress = TaskUtils.getIntegerTaskProgress(taskProgress);
            int newProgress = TaskUtils.getConfigBoolean(task, "price-expect") ? progress + event.getPrice() : progress + event.getQuantity();
            taskProgress.setProgress(newProgress);
            super.debug("Incrementing task progress (now " + progress + ")", quest.getId(), task.getId(), player.getUniqueId());

            if (progress >= buyNeeded) {
                super.debug("Marking task as complete", quest.getId(), task.getId(), player.getUniqueId());
                taskProgress.setProgress(buyNeeded);
                taskProgress.setCompleted(true);
            }
            TaskUtils.sendTrackAdvancement(player, quest, task, pendingTask, buyNeeded);
        }
    }
}
