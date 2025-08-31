package com.t_sgw.task_helper.controller;

import com.t_sgw.task_helper.dto.TaskDTO;
import com.t_sgw.task_helper.entity.Task;
import com.t_sgw.task_helper.service.NotificationManager;
import com.t_sgw.task_helper.service.TaskService;
import com.t_sgw.task_helper.service.TemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TemplateManager templateManager;

    @Autowired
    private NotificationManager notificationManager;

    /**
     * タスク一覧画面を表示
     * 
     * @param model モデル
     * @return タスク一覧画面
     */
    @GetMapping
    public String listTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        List<Task> dueTasks = notificationManager.checkDueTasks();

        model.addAttribute("tasks", tasks);
        model.addAttribute("dueTasks", dueTasks);
        model.addAttribute("taskDTO", new TaskDTO());

        return "tasks/list";
    }

    /**
     * タスク詳細画面を表示
     * 
     * @param id    タスクID
     * @param model モデル
     * @return タスク詳細画面
     */
    @GetMapping("/{id}")
    public String getTask(@PathVariable Long id, Model model) {
        try {
            Task task = taskService.getTask(id);
            model.addAttribute("task", task);
            return "tasks/detail";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * タスク作成画面を表示
     * 
     * @param model モデル
     * @return タスク作成画面
     */
    @GetMapping("/new")
    public String newTaskForm(Model model) {
        model.addAttribute("taskDTO", new TaskDTO());
        model.addAttribute("templates", templateManager.getAllTemplates());
        return "tasks/form";
    }

    /**
     * タスクを作成
     * 
     * @param taskDTO タスクDTO
     * @param model   モデル
     * @return リダイレクト先
     */
    @PostMapping
    public String addTask(@ModelAttribute TaskDTO taskDTO, Model model) {
        try {
            taskService.createTask(taskDTO);
            return "redirect:/tasks";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("taskDTO", taskDTO);
            model.addAttribute("templates", templateManager.getAllTemplates());
            return "tasks/form";
        }
    }

    /**
     * タスク編集画面を表示
     * 
     * @param id    タスクID
     * @param model モデル
     * @return タスク編集画面
     */
    @GetMapping("/{id}/edit")
    public String editTaskForm(@PathVariable Long id, Model model) {
        try {
            Task task = taskService.getTask(id);
            TaskDTO taskDTO = taskService.convertToDTO(task);

            model.addAttribute("taskDTO", taskDTO);
            model.addAttribute("taskId", id);
            model.addAttribute("templates", templateManager.getAllTemplates());

            return "tasks/form";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    /**
     * タスクを更新
     * 
     * @param id      タスクID
     * @param taskDTO タスクDTO
     * @param model   モデル
     * @return リダイレクト先
     */
    @PostMapping("/{id}")
    public String editTask(@PathVariable Long id, @ModelAttribute TaskDTO taskDTO, Model model) {
        try {
            taskService.updateTask(id, taskDTO);
            return "redirect:/tasks";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("taskDTO", taskDTO);
            model.addAttribute("taskId", id);
            model.addAttribute("templates", templateManager.getAllTemplates());
            return "tasks/form";
        }
    }

    /**
     * タスクを削除
     * 
     * @param id タスクID
     * @return リダイレクト先
     */
    @PostMapping("/{id}/delete")
    public String removeTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return "redirect:/tasks";
        } catch (RuntimeException e) {
            return "redirect:/tasks?error=" + e.getMessage();
        }
    }

    /**
     * タスクの完了状態を切り替え
     * 
     * @param id タスクID
     * @return リダイレクト先
     */
    @PostMapping("/{id}/toggle")
    public String toggleTask(@PathVariable Long id) {
        try {
            taskService.toggleTaskCompletion(id);
            return "redirect:/tasks";
        } catch (RuntimeException e) {
            return "redirect:/tasks?error=" + e.getMessage();
        }
    }

    /**
     * 期限通知を表示
     * 
     * @param model モデル
     * @return 通知画面
     */
    @GetMapping("/notifications")
    public String notifyDueTasks(Model model) {
        List<Task> dueTasks = notificationManager.checkDueTasks();
        List<Task> overdueTasks = notificationManager.getOverdueTasks();

        String dueMessage = notificationManager.generateBulkNotificationMessage(dueTasks);
        String overdueMessage = notificationManager.generateBulkNotificationMessage(overdueTasks);

        model.addAttribute("dueTasks", dueTasks);
        model.addAttribute("overdueTasks", overdueTasks);
        model.addAttribute("dueMessage", dueMessage);
        model.addAttribute("overdueMessage", overdueMessage);

        return "tasks/notifications";
    }

    // REST API エンドポイント

    /**
     * 全タスクを取得（REST API）
     * 
     * @return タスクのリスト
     */
    @GetMapping("/api")
    @ResponseBody
    public List<Task> getTasksApi() {
        return taskService.getAllTasks();
    }

    /**
     * タスクを作成（REST API）
     * 
     * @param taskDTO タスクDTO
     * @return 作成されたタスク
     */
    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Task> createTaskApi(@RequestBody TaskDTO taskDTO) {
        try {
            Task task = taskService.createTask(taskDTO);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * タスクを更新（REST API）
     * 
     * @param id      タスクID
     * @param taskDTO タスクDTO
     * @return 更新されたタスク
     */
    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Task> updateTaskApi(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        try {
            Task task = taskService.updateTask(id, taskDTO);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * タスクを削除（REST API）
     * 
     * @param id タスクID
     * @return レスポンス
     */
    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteTaskApi(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * タスク検索（REST API）
     * 
     * @param keyword 検索キーワード
     * @return 検索結果のタスクリスト
     */
    @GetMapping("/api/search")
    @ResponseBody
    public List<Task> searchTasksApi(@RequestParam String keyword) {
        return taskService.searchTasks(keyword);
    }
}
