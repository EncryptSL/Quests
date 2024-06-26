package com.leonardobishop.quests.common.player.questprogressfile;

import com.leonardobishop.quests.common.plugin.Quests;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class QuestProgress {

    private final Quests plugin;

    private final Map<String, TaskProgress> taskProgress = new HashMap<>();
    private final String questid;
    private final UUID player;

    private boolean started;
    private long startedDate;
    private boolean completed;
    private boolean completedBefore;
    private long completionDate;
    private boolean modified;

    public QuestProgress(Quests plugin, String questid, boolean completed, boolean completedBefore, long completionDate, UUID player, boolean started, long startedDate) {
        this.plugin = plugin;
        this.questid = questid;
        this.completed = completed;
        this.completedBefore = completedBefore;
        this.completionDate = completionDate;
        this.player = player;
        this.started = started;
        this.startedDate = startedDate;
    }

    public QuestProgress(Quests plugin, String questid, boolean completed, boolean completedBefore, long completionDate, UUID player, boolean started, long startedDate, boolean modified) {
        this(plugin, questid, completed, completedBefore, completionDate, player, started, startedDate);
        this.modified = modified;
    }

    public QuestProgress(QuestProgress questProgress) {
        this.plugin = questProgress.plugin;
        for (Map.Entry<String, TaskProgress> progressEntry : questProgress.taskProgress.entrySet()) {
            taskProgress.put(progressEntry.getKey(), new TaskProgress(progressEntry.getValue()));
        }
        this.questid = questProgress.questid;
        this.player = questProgress.player;
        this.started = questProgress.started;
        this.startedDate = questProgress.startedDate;
        this.completed = questProgress.completed;
        this.completedBefore = questProgress.completedBefore;
        this.completionDate = questProgress.completionDate;
        this.modified = questProgress.modified;
    }

    public String getQuestId() {
        return questid;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean complete) {
        // do not queue completion for already completed quests
        // https://github.com/LMBishop/Quests/issues/543
        if (this.completed == complete) {
            return;
        }

        this.completed = complete;
        this.modified = true;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
        this.modified = true;
    }

    public long getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(long startedDate) {
        this.startedDate = startedDate;
        this.modified = true;
    }

    public long getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(long completionDate) {
        this.completionDate = completionDate;
        this.modified = true;
    }

    public UUID getPlayer() {
        return player;
    }

    public boolean isCompletedBefore() {
        return completedBefore;
    }

    public void setCompletedBefore(boolean completedBefore) {
        this.completedBefore = completedBefore;
        this.modified = true;
    }

    public void addTaskProgress(TaskProgress taskProgress) {
        this.taskProgress.put(taskProgress.getTaskId(), taskProgress);
    }

    public Collection<TaskProgress> getTaskProgress() {
        return taskProgress.values();
    }

    public Map<String, TaskProgress> getTaskProgressMap() {
        return taskProgress;
    }

    public TaskProgress getTaskProgress(String taskId) {
        TaskProgress tP = taskProgress.getOrDefault(taskId, null);
        if (tP == null) {
            repairTaskProgress(taskId);
            tP = taskProgress.getOrDefault(taskId, null);
        }
        return tP;
    }

    public void repairTaskProgress(String taskid) {
        TaskProgress taskProgress = new TaskProgress(this, taskid, null, player, false, false);
        this.addTaskProgress(taskProgress);
    }

    public boolean isModified() {
        if (modified) return true;
        else {
            for (TaskProgress progress : this.taskProgress.values()) {
                if (progress.isModified()) return true;
            }
            return false;
        }
    }

    public boolean hasNonDefaultValues() {
        if (this.started || this.startedDate != 0 || this.completed || this.completedBefore || this.completionDate != 0) return true;
        else {
            for (TaskProgress progress : this.taskProgress.values()) {
                if (progress.getProgress() != null || progress.isCompleted()) return true;
            }
            return false;
        }
    }

    public void queueForCompletionTest() {
        plugin.getQuestCompleter().queueSingular(this);
    }

    public void resetModified() {
        this.modified = false;
        for (TaskProgress progress : this.taskProgress.values()) {
            progress.resetModified();
        }
    }

    public void setModified(boolean modified) {
        this.modified = modified;
        for (TaskProgress progress : this.taskProgress.values()) {
            progress.setModified(modified);
        }
    }
}
