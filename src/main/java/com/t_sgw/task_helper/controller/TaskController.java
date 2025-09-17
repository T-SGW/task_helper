package com.t_sgw.task_helper.controller;

import com.t_sgw.task_helper.dto.TaskDTO;
import com.t_sgw.task_helper.entity.Task;
import com.t_sgw.task_helper.service.NotificationManager;
import com.t_sgw.task_helper.service.TaskService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class TaskController {

    @FXML
    private TableView<Task> tasksTable;

    @FXML
    private TableColumn<Task, Long> idColumn;
    @FXML
    private TableColumn<Task, String> titleColumn;
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    @FXML
    private TableColumn<Task, String> dueDateColumn;
    @FXML
    private TableColumn<Task, Boolean> completedColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, String> categoryColumn;

    // タスク作成フォーム用のフィールド
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private ComboBox<String> priorityComboBox;
    @FXML
    private TextField categoryField;
    @FXML
    private CheckBox completedCheckBox;

    // タスク詳細フォーム用のフィールド
    @FXML
    private TextField detailTitleField;
    @FXML
    private TextArea detailDescriptionField;
    @FXML
    private DatePicker detailDueDatePicker;
    @FXML
    private ComboBox<String> detailPriorityComboBox;
    @FXML
    private TextField detailCategoryField;
    @FXML
    private CheckBox detailCompletedCheckBox;

    private Task currentTask; // 現在編集中のタスク

    private final TaskService taskService;
    private final NotificationManager notificationManager;

    public TaskController() {
        this.taskService = new TaskService();
        this.notificationManager = new NotificationManager(taskService);
    }

    @FXML
    public void initialize() {
        // テーブルカラムが存在する場合のみ設定（タスク一覧画面用）
        if (tasksTable != null) {
            setupTableColumns();
            loadTasks();
        }

        // 優先度コンボボックスを初期化（タスク作成フォーム用）
        initializePriorityComboBox();
    }

    private void initializePriorityComboBox() {
        if (priorityComboBox != null) {
            priorityComboBox.getItems().addAll("高", "中", "低");
        }
        if (detailPriorityComboBox != null) {
            detailPriorityComboBox.getItems().addAll("高", "中", "低");
        }
    }

    private void setupTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        completedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

        // テーブルの行をクリックしたときに詳細画面を表示
        tasksTable.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Task> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && !row.isEmpty()) {
                    Task task = row.getItem();
                    showTaskDetail(task);
                }
            });
            return row;
        });
    }

    public void loadTasks() {
        List<Task> tasks = taskService.getAllTasks();
        tasksTable.getItems().setAll(tasks);
    }

    public void createTask(TaskDTO taskDTO) {
        try {
            taskService.createTask(taskDTO);
            // テーブルが存在する場合のみリロード（タスク一覧画面用）
            if (tasksTable != null) {
                loadTasks();
            }
            showAlert(Alert.AlertType.INFORMATION, "成功", "タスクを作成しました");
        } catch (Exception e) {
            String errorMessage = "タスクの作成に失敗しました: " + e.getMessage();
            System.err.println("ERROR: " + errorMessage);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "エラー", errorMessage);
        }
    }

    public void updateTask(Long id, TaskDTO taskDTO) {
        try {
            taskService.updateTask(id, taskDTO);
            // テーブルが存在する場合のみリロード（タスク一覧画面用）
            if (tasksTable != null) {
                loadTasks();
            }
            showAlert(Alert.AlertType.INFORMATION, "成功", "タスクを更新しました");
        } catch (RuntimeException e) {
            String errorMessage = "タスクの更新に失敗しました: " + e.getMessage();
            System.err.println("ERROR: " + errorMessage);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "エラー", errorMessage);
        }
    }

    public void deleteTask(Long id) {
        try {
            taskService.deleteTask(id);
            // テーブルが存在する場合のみリロード（タスク一覧画面用）
            if (tasksTable != null) {
                loadTasks();
            }
            showAlert(Alert.AlertType.INFORMATION, "成功", "タスクを削除しました");
        } catch (RuntimeException e) {
            String errorMessage = "タスクの削除に失敗しました: " + e.getMessage();
            System.err.println("ERROR: " + errorMessage);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "エラー", errorMessage);
        }
    }

    public void toggleTaskCompletion(Long id) {
        try {
            taskService.toggleTaskCompletion(id);
            // テーブルが存在する場合のみリロード（タスク一覧画面用）
            if (tasksTable != null) {
                loadTasks();
            }
            showAlert(Alert.AlertType.INFORMATION, "成功", "タスクの状態を変更しました");
        } catch (RuntimeException e) {
            String errorMessage = "タスク状態の変更に失敗しました: " + e.getMessage();
            System.err.println("ERROR: " + errorMessage);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "エラー", errorMessage);
        }
    }

    public List<Task> getDueTasks() {
        return notificationManager.checkDueTasks();
    }

    public List<Task> getOverdueTasks() {
        return notificationManager.getOverdueTasks();
    }

    public String generateBulkNotificationMessage(List<Task> tasks) {
        return notificationManager.generateBulkNotificationMessage(tasks);
    }

    public List<Task> searchTasks(String keyword) {
        return taskService.searchTasks(keyword);
    }

    public Task getTask(Long id) {
        return taskService.getTask(id);
    }

    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    public List<Task> getTasksByCompleted(boolean completed) {
        return taskService.getTasksByCompleted(completed);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
        // テーブルが存在する場合のみリロード（タスク一覧画面用）
        if (tasksTable != null) {
            loadTasks();
        }
    }

    @FXML
    private void handleSaveTask() {
        try {
            // バリデーション
            if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "エラー", "タイトルは必須です");
                return;
            }

            // TaskDTOを作成
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTitle(titleField.getText().trim());
            taskDTO.setDescription(descriptionField.getText() != null ? descriptionField.getText().trim() : null);

            if (dueDatePicker.getValue() != null) {
                taskDTO.setDueDate(dueDatePicker.getValue().atStartOfDay());
            }

            taskDTO.setPriority(priorityComboBox.getValue());
            taskDTO.setCategory(categoryField.getText() != null ? categoryField.getText().trim() : null);
            taskDTO.setCompleted(completedCheckBox.isSelected());

            // タスクを作成
            createTask(taskDTO);

            // フォームをクリア
            clearForm();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "エラー", "タスクの保存に失敗しました: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        clearForm();
    }

    private void clearForm() {
        titleField.clear();
        descriptionField.clear();
        dueDatePicker.setValue(null);
        priorityComboBox.getSelectionModel().clearSelection();
        categoryField.clear();
        completedCheckBox.setSelected(false);
    }

    // タスク詳細画面用のメソッド
    public void loadTaskDetail(Task task) {
        this.currentTask = task;

        if (detailTitleField != null) {
            detailTitleField.setText(task.getTitle());
            detailDescriptionField.setText(task.getDescription() != null ? task.getDescription() : "");

            if (task.getDueDate() != null) {
                detailDueDatePicker.setValue(task.getDueDate().toLocalDate());
            } else {
                detailDueDatePicker.setValue(null);
            }

            detailPriorityComboBox.setValue(task.getPriority());
            detailCategoryField.setText(task.getCategory() != null ? task.getCategory() : "");
            detailCompletedCheckBox.setSelected(task.isCompleted());
        }
    }

    public void loadTaskForEdit(Long taskId) {
        try {
            Task task = taskService.getTask(taskId);
            if (task != null) {
                this.currentTask = task;

                // フォームにタスクデータを設定
                titleField.setText(task.getTitle());
                descriptionField.setText(task.getDescription() != null ? task.getDescription() : "");

                if (task.getDueDate() != null) {
                    dueDatePicker.setValue(task.getDueDate().toLocalDate());
                } else {
                    dueDatePicker.setValue(null);
                }

                priorityComboBox.setValue(task.getPriority());
                categoryField.setText(task.getCategory() != null ? task.getCategory() : "");
                completedCheckBox.setSelected(task.isCompleted());
            }
        } catch (Exception e) {
            System.err.println("タスクの読み込みに失敗しました: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateTask() {
        try {
            if (currentTask == null) {
                showAlert(Alert.AlertType.ERROR, "エラー", "編集中のタスクがありません");
                return;
            }

            // バリデーション
            if (detailTitleField.getText() == null || detailTitleField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "エラー", "タイトルは必須です");
                return;
            }

            // TaskDTOを作成
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setTitle(detailTitleField.getText().trim());
            taskDTO.setDescription(
                    detailDescriptionField.getText() != null ? detailDescriptionField.getText().trim() : null);

            if (detailDueDatePicker.getValue() != null) {
                taskDTO.setDueDate(detailDueDatePicker.getValue().atStartOfDay());
            }

            taskDTO.setPriority(detailPriorityComboBox.getValue());
            taskDTO.setCategory(detailCategoryField.getText() != null ? detailCategoryField.getText().trim() : null);
            taskDTO.setCompleted(detailCompletedCheckBox.isSelected());

            // タスクを更新
            updateTask(currentTask.getId(), taskDTO);

            showAlert(Alert.AlertType.INFORMATION, "成功", "タスクを更新しました");

        } catch (Exception e) {
            String errorMessage = "タスクの更新に失敗しました: " + e.getMessage();
            System.err.println("ERROR: " + errorMessage);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "エラー", errorMessage);
        }
    }

    @FXML
    private void handleDeleteTask() {
        try {
            if (currentTask == null) {
                showAlert(Alert.AlertType.ERROR, "エラー", "削除するタスクがありません");
                return;
            }

            // 確認ダイアログ
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("確認");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("このタスクを削除しますか？");

            if (confirmAlert.showAndWait().get().getButtonData().isDefaultButton()) {
                deleteTask(currentTask.getId());
                showAlert(Alert.AlertType.INFORMATION, "成功", "タスクを削除しました");
                handleCancelDetail();
            }

        } catch (Exception e) {
            String errorMessage = "タスクの削除に失敗しました: " + e.getMessage();
            System.err.println("ERROR: " + errorMessage);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "エラー", errorMessage);
        }
    }

    @FXML
    private void handleCancelDetail() {
        // 詳細画面を閉じてホーム画面に戻る
        try {
            MainController mainController = MainController.getInstance();
            if (mainController != null) {
                mainController.showHome();
            } else {
                System.err.println("MainControllerのインスタンスが取得できません");
            }
        } catch (Exception e) {
            String errorMessage = "画面の切り替えに失敗しました: " + e.getMessage();
            System.err.println("ERROR: " + errorMessage);
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "エラー", errorMessage);
        }
    }
}
