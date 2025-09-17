package com.t_sgw.task_helper.service;

import com.t_sgw.task_helper.dto.TaskDTO;
import com.t_sgw.task_helper.entity.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskService {

    private final JsonDataService jsonDataService;
    private List<Task> tasks;

    public TaskService() {
        this.jsonDataService = new JsonDataService();
        this.tasks = new ArrayList<>(jsonDataService.loadInitialData());
    }

    /**
     * タスクを作成
     * 
     * @param dto タスクDTO
     * @return 作成されたタスク
     */
    public Task createTask(TaskDTO dto) {
        Task task = new Task();
        task.setId(jsonDataService.generateId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setCompleted(dto.isCompleted());
        task.setCategory(dto.getCategory());
        task.setPriority(dto.getPriority());
        task.updateTimestamps();

        tasks.add(task);
        saveTasks();
        return task;
    }

    /**
     * タスクを更新
     * 
     * @param id  タスクID
     * @param dto タスクDTO
     * @return 更新されたタスク
     * @throws RuntimeException タスクが見つからない場合
     */
    public Task updateTask(Long id, TaskDTO dto) {
        Optional<Task> optionalTask = findTaskById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setDueDate(dto.getDueDate());
            task.setCompleted(dto.isCompleted());
            task.setCategory(dto.getCategory());
            task.setPriority(dto.getPriority());
            task.updateTimestamps();

            saveTasks();
            return task;
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    /**
     * タスクを削除
     * 
     * @param id タスクID
     * @throws RuntimeException タスクが見つからない場合
     */
    public void deleteTask(Long id) {
        if (tasks.removeIf(task -> task.getId().equals(id))) {
            saveTasks();
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    /**
     * 指定IDのタスクを取得
     * 
     * @param id タスクID
     * @return タスク
     * @throws RuntimeException タスクが見つからない場合
     */
    public Task getTask(Long id) {
        Optional<Task> optionalTask = findTaskById(id);
        if (optionalTask.isPresent()) {
            return optionalTask.get();
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    /**
     * 全タスクを取得
     * 
     * @return 全タスクのリスト
     */
    public List<Task> getAllTasks() {
        // デバッグ出力
        for (Task task : tasks) {
            System.out.println("JSONから取得したタイトル: " + task.getTitle());
        }

        return new ArrayList<>(tasks);
    }

    /**
     * 期限が近いタスクを取得
     * 
     * @return 期限が近いタスクのリスト
     */
    public List<Task> getTasksDueSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        return tasks.stream()
                .filter(task -> task.getDueDate() != null)
                .filter(task -> !task.isCompleted())
                .filter(task -> task.getDueDate().isBefore(tomorrow) && task.getDueDate().isAfter(now))
                .collect(Collectors.toList());
    }

    /**
     * 完了状態でフィルタリングしてタスクを取得
     * 
     * @param completed 完了状態
     * @return 指定した完了状態のタスクのリスト
     */
    public List<Task> getTasksByCompleted(boolean completed) {
        return tasks.stream()
                .filter(task -> task.isCompleted() == completed)
                .collect(Collectors.toList());
    }

    /**
     * カテゴリでフィルタリングしてタスクを取得
     * 
     * @param category カテゴリ
     * @return 指定したカテゴリのタスクのリスト
     */
    public List<Task> getTasksByCategory(String category) {
        return tasks.stream()
                .filter(task -> category.equals(task.getCategory()))
                .collect(Collectors.toList());
    }

    /**
     * 優先度でフィルタリングしてタスクを取得
     * 
     * @param priority 優先度
     * @return 指定した優先度のタスクのリスト
     */
    public List<Task> getTasksByPriority(String priority) {
        return tasks.stream()
                .filter(task -> priority.equals(task.getPriority()))
                .collect(Collectors.toList());
    }

    /**
     * キーワードでタスクを検索
     * 
     * @param keyword 検索キーワード
     * @return 検索条件に一致するタスクのリスト
     */
    public List<Task> searchTasks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> (task.getTitle() != null && task.getTitle().toLowerCase().contains(lowerKeyword)) ||
                        (task.getDescription() != null && task.getDescription().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    /**
     * タスクの完了状態を切り替え
     * 
     * @param id タスクID
     * @return 更新されたタスク
     * @throws RuntimeException タスクが見つからない場合
     */
    public Task toggleTaskCompletion(Long id) {
        Optional<Task> optionalTask = findTaskById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(!task.isCompleted());
            task.updateTimestamps();

            saveTasks();
            return task;
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }

    /**
     * IDでタスクを検索
     */
    private Optional<Task> findTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }

    /**
     * タスクを保存
     */
    private void saveTasks() {
        jsonDataService.saveAllTasks(tasks);
    }

    /**
     * TaskをTaskDTOに変換
     * 
     * @param task タスクエンティティ
     * @return タスクDTO
     */
    public TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setCompleted(task.isCompleted());
        dto.setCategory(task.getCategory());
        dto.setPriority(task.getPriority());
        return dto;
    }
}
