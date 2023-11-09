package com.leonardobishop.quests.bukkit.tasktype.type.dependent;

import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.tasktype.BukkitTaskType;
import com.leonardobishop.quests.bukkit.util.TaskUtils;
import com.leonardobishop.quests.common.player.QPlayer;
import com.leonardobishop.quests.common.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.common.quest.Quest;
import com.leonardobishop.quests.common.quest.Task;
import net.erbros.lottery.events.LotteryBuyTicketEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class LotteryBuyTicketsTaskType extends BukkitTaskType {

    private final BukkitQuestsPlugin plugin;
    public LotteryBuyTicketsTaskType(BukkitQuestsPlugin plugin) {
        super("lottery_buy_tickets", TaskUtils.TASK_ATTRIBUTION_STRING, "Lottery buy a ticket", "lot_buy_certain");
        this.plugin = plugin;
        super.addConfigValidator(TaskUtils.useIntegerConfigValidator(this, "amount"));
        super.addConfigValidator(TaskUtils.useRequiredConfigValidator(this, "amount"));
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onLotteryBuyTicket(LotteryBuyTicketEvent event) {
        Player player = event.getPlayer();
        int tickets = event.getAmount();

        if (player == null || player.hasMetadata("NPC")) {
            return;
        }

        QPlayer qPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (qPlayer == null) {
            return;
        }

        for (TaskUtils.PendingTask pendingTask : TaskUtils.getApplicableTasks(player, qPlayer, this)) {
            Quest quest = pendingTask.quest();
            Task task = pendingTask.task();
            TaskProgress taskProgress = pendingTask.taskProgress();

            super.debug("Player buy ticket", quest.getId(), task.getId(), player.getUniqueId());

            int amountNeeded = (int) task.getConfigValue("amount");

            int progress = TaskUtils.getIntegerTaskProgress(taskProgress);
            int newProgress = progress + tickets;
            taskProgress.setProgress(newProgress);
            super.debug("Incrementing task progress (now " + progress + ")", quest.getId(), task.getId(), player.getUniqueId());

            if (progress >= amountNeeded) {
                super.debug("Marking task as complete", quest.getId(), task.getId(), player.getUniqueId());
                taskProgress.setProgress(amountNeeded);
                taskProgress.setCompleted(true);
            }
            TaskUtils.sendTrackAdvancement(player, quest, task, taskProgress);
        }
    }

}
