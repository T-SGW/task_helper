package com.t_sgw.task_helper.service;

import com.t_sgw.task_helper.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationManager {

    @Autowired
    private TaskService taskService;

    /**
     * 期限が近いタスクをチェック
     * 
     * @return 期限が近いタスクのリスト
     */
    public List<Task> checkDueTasks() {
        return taskService.getTasksDueSoon();
    }

    /**
     * 指定した時間内に期限が来るタスクを取得
     * 
     * @param hours 時間（時間単位）
     * @return 指定時間内に期限が来るタスクのリスト
     */
    public List<Task> getTasksDueWithinHours(int hours) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusHours(hours);
        return taskService.getTasksDueSoon(); // 実装では1日以内のタスクを取得
    }

    /**
     * タスクの通知メッセージを生成
     * 
     * @param task タスク
     * @return 通知メッセージ
     */
    public String generateNotificationMessage(Task task) {
        StringBuilder message = new StringBuilder();
        message.append("【期限通知】\n");
        message.append("タスク: ").append(task.getTitle()).append("\n");

        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            message.append("説明: ").append(task.getDescription()).append("\n");
        }

        if (task.getDueDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
            message.append("期限: ").append(task.getDueDate().format(formatter)).append("\n");
        }

        if (task.getPriority() != null && !task.getPriority().isEmpty()) {
            message.append("優先度: ").append(task.getPriority()).append("\n");
        }

        if (task.getCategory() != null && !task.getCategory().isEmpty()) {
            message.append("カテゴリ: ").append(task.getCategory()).append("\n");
        }

        return message.toString();
    }

    /**
     * 複数タスクの通知メッセージを生成
     * 
     * @param tasks タスクのリスト
     * @return 通知メッセージ
     */
    public String generateBulkNotificationMessage(List<Task> tasks) {
        if (tasks.isEmpty()) {
            return "期限が近いタスクはありません。";
        }

        StringBuilder message = new StringBuilder();
        message.append("【期限通知】期限が近いタスクが ").append(tasks.size()).append(" 件あります。\n\n");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            message.append(i + 1).append(". ").append(task.getTitle());

            if (task.getDueDate() != null) {
                message.append(" (").append(task.getDueDate().format(formatter)).append(")");
            }

            if (task.getPriority() != null && !task.getPriority().isEmpty()) {
                message.append(" [").append(task.getPriority()).append("]");
            }

            message.append("\n");
        }

        return message.toString();
    }

    /**
     * タスクが期限切れかどうかを判定
     * 
     * @param task タスク
     * @return 期限切れの場合true
     */
    public boolean isOverdue(Task task) {
        if (task.getDueDate() == null || task.isCompleted()) {
            return false;
        }
        return task.getDueDate().isBefore(LocalDateTime.now());
    }

    /**
     * タスクが指定時間内に期限が来るかどうかを判定
     * 
     * @param task  タスク
     * @param hours 時間（時間単位）
     * @return 指定時間内に期限が来る場合true
     */
    public boolean isDueSoon(Task task, int hours) {
        if (task.getDueDate() == null || task.isCompleted()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusHours(hours);

        return task.getDueDate().isAfter(now) && task.getDueDate().isBefore(targetTime);
    }

    /**
     * 期限切れのタスクを取得
     * 
     * @return 期限切れのタスクのリスト
     */
    public List<Task> getOverdueTasks() {
        List<Task> allTasks = taskService.getTasksByCompleted(false);
        return allTasks.stream()
                .filter(this::isOverdue)
                .toList();
    }

    /**
     * 通知の優先度を判定
     * 
     * @param task タスク
     * @return 通知優先度（1:高, 2:中, 3:低）
     */
    public int getNotificationPriority(Task task) {
        if (isOverdue(task)) {
            return 1; // 期限切れは最高優先度
        }

        if (task.getPriority() != null) {
            switch (task.getPriority().toLowerCase()) {
                case "高":
                case "high":
                    return 1;
                case "中":
                case "medium":
                    return 2;
                case "低":
                case "low":
                    return 3;
                default:
                    return 2;
            }
        }

        return 2; // デフォルトは中優先度
    }
}
