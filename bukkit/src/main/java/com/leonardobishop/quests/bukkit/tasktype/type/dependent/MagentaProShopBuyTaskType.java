package com.leonardobishop.quests.bukkit.tasktype.type.dependent;

import com.github.encryptsl.magenta.api.events.shop.ShopBuyEvent;
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

public class MagentaProShopBuyTaskType extends BukkitTaskType {

    private final BukkitQuestsPlugin plugin;

    public MagentaProShopBuyTaskType(BukkitQuestsPlugin plugin) {
        super("magenta_shop_buy", TaskUtils.TASK_ATTRIBUTION_STRING, "Buy something from magenta vault shop", "mg_shop_buy");
        this.plugin = plugin;
        super.addConfigValidator(TaskUtils.useBooleanConfigValidator(this, "price-accepting"));
        super.addConfigValidator(TaskUtils.useRequiredConfigValidator(this, "amount"));
        super.addConfigValidator(TaskUtils.useIntegerConfigValidator(this, "amount"));
        super.addConfigValidator(TaskUtils.useMaterialListConfigValidator(this, TaskUtils.MaterialListConfigValidatorMode.ITEM, "item", "items"));
        super.addConfigValidator(TaskUtils.useEnumConfigValidator(this, TaskUtils.StringMatchMode.class, "item-match-mode"));
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMagentaProShopBuy(ShopBuyEvent event) {
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

            super.debug("Player buy item from shop", quest.getId(), task.getId(), player.getUniqueId());

            if (!TaskUtils.matchString(this, pendingTask, item, player.getUniqueId(), "item", "items", false,"item-match-mode", true)) {
                super.debug("Continuing...", quest.getId(), task.getId(), player.getUniqueId());
                continue;
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
            TaskUtils.sendTrackAdvancement(player, quest, task, pendingTask, amountNeeded);
        }
    }

}
