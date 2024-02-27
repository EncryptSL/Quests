package com.leonardobishop.quests.bukkit.tasktype.type.dependent;

import com.github.encryptsl.magenta.api.events.vote.VotePartyEvent;
import com.leonardobishop.quests.bukkit.BukkitQuestsPlugin;
import com.leonardobishop.quests.bukkit.tasktype.BukkitTaskType;
import com.leonardobishop.quests.bukkit.util.TaskUtils;
import com.leonardobishop.quests.common.player.QPlayer;
import com.leonardobishop.quests.common.player.questprogressfile.TaskProgress;
import com.leonardobishop.quests.common.quest.Quest;
import com.leonardobishop.quests.common.quest.Task;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

public class MagentaProVotePartyTaskType extends BukkitTaskType {
    private final BukkitQuestsPlugin plugin;
    public MagentaProVotePartyTaskType(BukkitQuestsPlugin plugin) {
        super("magenta_vote_party", TaskUtils.TASK_ATTRIBUTION_STRING, "Be online on voteparty", "mg_vparty");
        this.plugin = plugin;
        super.addConfigValidator(TaskUtils.useBooleanConfigValidator(this, "players-expect"));
        super.addConfigValidator(TaskUtils.useIntegerConfigValidator(this, "online-players"));
        super.addConfigValidator(TaskUtils.useRequiredConfigValidator(this, "amount"));
        super.addConfigValidator(TaskUtils.useIntegerConfigValidator(this, "amount"));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onVoteParty(VotePartyEvent event) {
        Player player = event.getPlayer();
        int onlinePlayers = event.getPlayers();

        if (player.hasMetadata("NPC")) return;

        QPlayer qPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (qPlayer == null) {
            return;
        }

        for (TaskUtils.PendingTask pendingTask : TaskUtils.getApplicableTasks(player, qPlayer, this)) {
            Quest quest = pendingTask.quest();
            Task task = pendingTask.task();
            TaskProgress taskProgress = pendingTask.taskProgress();

            super.debug("Voteparty player is rewarded", quest.getId(), task.getId(), player.getUniqueId());

            if (TaskUtils.getConfigBoolean(task, "players-expect")) {
                int playersNeeded = (int) task.getConfigValue("online-players");
                if (onlinePlayers <= playersNeeded) {
                    super.debug("Required players not found, continuing...", quest.getId(), task.getId(), player.getUniqueId());
                    continue;
                }
            }

            int votesNeeded = (int) task.getConfigValue("amount");

            int progress = TaskUtils.incrementIntegerTaskProgress(taskProgress);
            super.debug("Incrementing task progress (now " + progress + ")", quest.getId(), task.getId(), player.getUniqueId());

            if (progress >= votesNeeded) {
                super.debug("Marking task as complete", quest.getId(), task.getId(), player.getUniqueId());
                taskProgress.setCompleted(true);
            }
            TaskUtils.sendTrackAdvancement(player, quest, task, pendingTask, votesNeeded);
        }
    }
}
