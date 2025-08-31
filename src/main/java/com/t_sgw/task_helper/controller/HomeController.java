package com.t_sgw.task_helper.controller;

import com.t_sgw.task_helper.entity.Task;
import com.t_sgw.task_helper.service.NotificationManager;
import com.t_sgw.task_helper.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private NotificationManager notificationManager;

    /**
     * ホーム画面を表示
     * 
     * @param model モデル
     * @return ホーム画面
     */
    @GetMapping("/")
    public String home(Model model) {
        // 統計情報を取得
        List<Task> allTasks = taskService.getAllTasks();
        List<Task> completedTasks = taskService.getTasksByCompleted(true);
        List<Task> incompleteTasks = taskService.getTasksByCompleted(false);
        List<Task> dueTasks = notificationManager.checkDueTasks();
        List<Task> overdueTasks = notificationManager.getOverdueTasks();

        // 最近のタスク（最新5件）を取得
        List<Task> recentTasks = allTasks.stream()
                .limit(5)
                .toList();

        // 統計情報をモデルに追加
        model.addAttribute("totalTasks", allTasks.size());
        model.addAttribute("completedTasks", completedTasks.size());
        model.addAttribute("incompleteTasks", incompleteTasks.size());
        model.addAttribute("dueTasks", dueTasks.size());
        model.addAttribute("overdueTasks", overdueTasks.size());

        // タスクリストをモデルに追加
        model.addAttribute("recentTasks", recentTasks);
        model.addAttribute("dueTasksList", dueTasks);
        model.addAttribute("overdueTasksList", overdueTasks);

        // 完了率を計算
        double completionRate = allTasks.isEmpty() ? 0.0 : (double) completedTasks.size() / allTasks.size() * 100;
        model.addAttribute("completionRate", Math.round(completionRate * 10.0) / 10.0);

        return "home";
    }

    /**
     * ダッシュボード画面を表示
     * 
     * @param model モデル
     * @return ダッシュボード画面
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // ホーム画面と同じ内容を表示
        return home(model);
    }
}
