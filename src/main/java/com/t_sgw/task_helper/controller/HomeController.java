package com.t_sgw.task_helper.controller;

import com.t_sgw.task_helper.entity.Task;
import com.t_sgw.task_helper.service.NotificationManager;
import com.t_sgw.task_helper.service.TaskService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeController {

    @FXML
    private Label totalTasksLabel;
    @FXML
    private Label completedTasksLabel;
    @FXML
    private Label dueTasksLabel;
    @FXML
    private Label overdueTasksLabel;
    @FXML
    private ProgressBar completionProgressBar;
    @FXML
    private Label completionRateLabel;
    @FXML
    private VBox recentTasksContainer;
    @FXML
    private VBox dueTasksContainer;

    private final TaskService taskService;
    private final NotificationManager notificationManager;

    public HomeController() {
        this.taskService = new TaskService();
        this.notificationManager = new NotificationManager(taskService);
    }

    @FXML
    public void initialize() {
        loadDashboardData();
    }

    private void loadDashboardData() {
        // 統計情報を取得
        List<Task> allTasks = taskService.getAllTasks();
        List<Task> completedTasks = taskService.getTasksByCompleted(true);
        List<Task> incompleteTasks = taskService.getTasksByCompleted(false);
        List<Task> dueTasks = notificationManager.checkDueTasks();
        List<Task> overdueTasks = notificationManager.getOverdueTasks();

        // 統計情報を表示
        totalTasksLabel.setText(String.valueOf(allTasks.size()));
        completedTasksLabel.setText(String.valueOf(completedTasks.size()));
        dueTasksLabel.setText(String.valueOf(dueTasks.size()));
        overdueTasksLabel.setText(String.valueOf(overdueTasks.size()));

        // 完了率を計算して表示
        double completionRate = allTasks.isEmpty() ? 0.0 : (double) completedTasks.size() / allTasks.size();
        completionProgressBar.setProgress(completionRate);
        completionRateLabel.setText(String.format("%.1f%%", completionRate * 100));

        // 最近のタスクを表示（最新5件）
        loadRecentTasks(allTasks);

        // 期限間近のタスクを表示
        loadDueTasks(dueTasks);
    }

    private void loadRecentTasks(List<Task> allTasks) {
        recentTasksContainer.getChildren().clear();

        List<Task> recentTasks = allTasks.stream()
                .limit(5)
                .toList();

        if (recentTasks.isEmpty()) {
            Label emptyLabel = new Label("タスクがありません");
            emptyLabel.getStyleClass().add("text-muted");
            recentTasksContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Task task : recentTasks) {
            VBox taskCard = createTaskCard(task, false);
            recentTasksContainer.getChildren().add(taskCard);
        }
    }

    private void loadDueTasks(List<Task> dueTasks) {
        dueTasksContainer.getChildren().clear();

        if (dueTasks.isEmpty()) {
            Label emptyLabel = new Label("期限間近のタスクはありません");
            emptyLabel.getStyleClass().add("text-muted");
            dueTasksContainer.getChildren().add(emptyLabel);
            return;
        }

        for (Task task : dueTasks) {
            VBox taskCard = createTaskCard(task, true);
            dueTasksContainer.getChildren().add(taskCard);
        }
    }

    private VBox createTaskCard(Task task, boolean isDue) {
        VBox card = new VBox(10);
        card.getStyleClass().add("task-card");
        card.setStyle(
                "-fx-border-color: #dee2e6; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-padding: 10px; -fx-cursor: hand;");

        if (isDue) {
            card.setStyle(
                    "-fx-border-color: #ffc107; -fx-border-width: 2px; -fx-border-radius: 5px; -fx-padding: 10px; -fx-background-color: #fff3cd; -fx-cursor: hand;");
        }
        if (task.isCompleted()) {
            card.setStyle(
                    "-fx-border-color: #28a745; -fx-border-width: 1px; -fx-border-radius: 5px; -fx-padding: 10px; -fx-background-color: #d4edda; -fx-cursor: hand;");
        }

        // タスクカードをクリックしたときに詳細画面を表示
        card.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                showTaskDetail(task);
            }
        });

        Label titleLabel = new Label(task.getTitle());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        card.getChildren().add(titleLabel);

        if (task.getDescription() != null && !task.getDescription().isEmpty()) {
            Label descLabel = new Label(task.getDescription());
            descLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 12px;");
            descLabel.setWrapText(true);
            card.getChildren().add(descLabel);
        }

        HBox detailsBox = new HBox(10);
        detailsBox.setAlignment(Pos.CENTER_LEFT);

        if (task.getDueDate() != null) {
            Label dueLabel = new Label(
                    "期限: " + task.getDueDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));
            dueLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11px;");
            detailsBox.getChildren().add(dueLabel);
        }

        if (task.getPriority() != null) {
            Label priorityLabel = new Label("優先度: " + task.getPriority());
            priorityLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11px;");
            detailsBox.getChildren().add(priorityLabel);
        }

        if (task.getCategory() != null && !task.getCategory().isEmpty()) {
            Label categoryLabel = new Label("カテゴリ: " + task.getCategory());
            categoryLabel.setStyle("-fx-text-fill: #6c757d; -fx-font-size: 11px;");
            detailsBox.getChildren().add(categoryLabel);
        }

        if (!detailsBox.getChildren().isEmpty()) {
            card.getChildren().add(detailsBox);
        }

        return card;
    }

    private void showTaskDetail(Task task) {
        try {
            // MainControllerのインスタンスを取得してタスク詳細画面を表示
            MainController mainController = MainController.getInstance();
            if (mainController != null) {
                mainController.showTaskDetail(task.getId());
            } else {
                System.err.println("MainControllerのインスタンスが取得できません");
            }
        } catch (Exception e) {
            System.err.println("タスク詳細画面の表示に失敗しました: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refresh() {
        loadDashboardData();
    }
}
