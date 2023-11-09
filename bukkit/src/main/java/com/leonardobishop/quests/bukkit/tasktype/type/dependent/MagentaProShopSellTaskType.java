package com.leonardobishop.quests.bukkit.tasktype.type.dependent;

import com.github.encryptsl.magenta.api.events.shop.ShopSellEvent;
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

public class MagentaProShopSellTaskType extends BukkitTaskType {
    private final BukkitQuestsPlugin plugin;
    public MagentaProShopSellTaskType(BukkitQuestsPlugin plugin) {
        super("mg_shop_sell", TaskUtils.TASK_ATTRIBUTION_STRING, "Sell item or something from magenta vault shop", "mg_shop_sell_certain");
        this.plugin = plugin;
        super.addConfigValidator(TaskUtils.useItemStackConfigValidator(this, "item"));
        super.addConfigValidator(TaskUtils.useBooleanConfigValidator(this, "price-accepting"));
        super.addConfigValidator(TaskUtils.useRequiredConfigValidator(this, "amount"));
        super.addConfigValidator(TaskUtils.useIntegerConfigValidator(this, "amount"));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMagentaProShopSell(ShopSellEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        Player player = Bukkit.getPlayer(uuid);
        int quantity = event.getQuantity();
        int price = event.getPrice();
        String item = event.getItem();
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

            if (task.hasConfigKey("item")) {
                String taskItemId = (String) task.getConfigValue("item");
                if (taskItemId == null || !taskItemId.equals(item)) {
                    super.debug("Item id does not match required id, continuing...", quest.getId(), task.getId(), player.getUniqueId());
                    continue;
                }
            }

            int amountNeeded = (int) task.getConfigValue("amount");

            int progress = TaskUtils.getIntegerTaskProgress(taskProgress);
            int newProgress = TaskUtils.getConfigBoolean(task, "price-accepting") ? progress + price : progress + quantity;
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
