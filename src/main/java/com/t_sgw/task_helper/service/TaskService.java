package com.t_sgw.task_helper.service;

import com.t_sgw.task_helper.dto.TaskDTO;
import com.t_sgw.task_helper.entity.Task;
import com.t_sgw.task_helper.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * タスクを作成
     * 
     * @param dto タスクDTO
     * @return 作成されたタスク
     */
    public Task createTask(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setCompleted(dto.isCompleted());
        task.setCategory(dto.getCategory());
        task.setPriority(dto.getPriority());

        return taskRepository.save(task);
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
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(dto.getTitle());
            task.setDescription(dto.getDescription());
            task.setDueDate(dto.getDueDate());
            task.setCompleted(dto.isCompleted());
            task.setCategory(dto.getCategory());
            task.setPriority(dto.getPriority());

            return taskRepository.save(task);
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
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
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
        Optional<Task> optionalTask = taskRepository.findById(id);
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
        return taskRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * 期限が近いタスクを取得
     * 
     * @return 期限が近いタスクのリスト
     */
    public List<Task> getTasksDueSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);
        return taskRepository.findUpcomingIncompleteTasks(tomorrow);
    }

    /**
     * 完了状態でフィルタリングしてタスクを取得
     * 
     * @param completed 完了状態
     * @return 指定した完了状態のタスクのリスト
     */
    public List<Task> getTasksByCompleted(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    /**
     * カテゴリでフィルタリングしてタスクを取得
     * 
     * @param category カテゴリ
     * @return 指定したカテゴリのタスクのリスト
     */
    public List<Task> getTasksByCategory(String category) {
        return taskRepository.findByCategory(category);
    }

    /**
     * 優先度でフィルタリングしてタスクを取得
     * 
     * @param priority 優先度
     * @return 指定した優先度のタスクのリスト
     */
    public List<Task> getTasksByPriority(String priority) {
        return taskRepository.findByPriority(priority);
    }

    /**
     * キーワードでタスクを検索
     * 
     * @param keyword 検索キーワード
     * @return 検索条件に一致するタスクのリスト
     */
    public List<Task> searchTasks(String keyword) {
        return taskRepository.findByTitleOrDescriptionContaining(keyword);
    }

    /**
     * タスクの完了状態を切り替え
     * 
     * @param id タスクID
     * @return 更新されたタスク
     * @throws RuntimeException タスクが見つからない場合
     */
    public Task toggleTaskCompletion(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(!task.isCompleted());
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
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
