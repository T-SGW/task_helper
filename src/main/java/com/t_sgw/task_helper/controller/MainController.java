package com.t_sgw.task_helper.controller;

import com.t_sgw.task_helper.entity.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class MainController {

    private static MainController instance;

    public static MainController getInstance() {
        return instance;
    }

    @FXML
    private StackPane contentPane;

    @FXML
    private Button homeButton;
    @FXML
    private Button tasksButton;
    @FXML
    private Button newTaskButton;
    @FXML
    private Button notificationsButton;

    @FXML
    public void initialize() {
        instance = this;
        showHome();
    }

    @FXML
    public void showHome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            VBox homeContent = loader.load();
            contentPane.getChildren().setAll(homeContent);
            updateButtonStyles(homeButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showTasks() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tasks.fxml"));
            VBox tasksContent = loader.load();
            contentPane.getChildren().setAll(tasksContent);
            updateButtonStyles(tasksButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showNewTask() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task-form.fxml"));
            VBox formContent = loader.load();
            contentPane.getChildren().setAll(formContent);
            updateButtonStyles(newTaskButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showNotifications() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/home.fxml"));
            VBox notificationsContent = loader.load();
            contentPane.getChildren().setAll(notificationsContent);
            updateButtonStyles(notificationsButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTaskDetail(Long taskId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/task-detail.fxml"));
            VBox detailContent = loader.load();

            // TaskControllerを取得してタスクデータを読み込み
            TaskController taskController = loader.getController();
            if (taskController != null && taskId != null) {
                Task task = taskController.getTask(taskId);
                if (task != null) {
                    taskController.loadTaskDetail(task);
                }
            }

            contentPane.getChildren().setAll(detailContent);
            updateButtonStyles(newTaskButton);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateButtonStyles(Button activeButton) {
        // すべてのボタンのスタイルをリセット
        homeButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #495057; -fx-alignment: center-left;");
        tasksButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #495057; -fx-alignment: center-left;");
        newTaskButton
                .setStyle("-fx-background-color: transparent; -fx-text-fill: #495057; -fx-alignment: center-left;");
        notificationsButton
                .setStyle("-fx-background-color: transparent; -fx-text-fill: #495057; -fx-alignment: center-left;");

        // アクティブなボタンのスタイルを設定
        activeButton.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: #007bff; -fx-font-weight: bold; -fx-alignment: center-left;");
    }
}
