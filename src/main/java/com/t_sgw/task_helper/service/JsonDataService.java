package com.t_sgw.task_helper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.t_sgw.task_helper.entity.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class JsonDataService {

    private static final String DATA_DIR = "data";
    private static final String TASKS_FILE = DATA_DIR + "/tasks.json";
    private static final AtomicLong idCounter = new AtomicLong(1);

    private final ObjectMapper objectMapper;

    public JsonDataService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        // データディレクトリの作成
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("データディレクトリの作成に失敗しました: " + e.getMessage());
        }
    }

    /**
     * すべてのタスクを読み込む
     */
    public List<Task> loadAllTasks() {
        try {
            File file = new File(TASKS_FILE);
            if (!file.exists()) {
                // ファイルが存在しない場合は空のリストを返す
                return new ArrayList<>();
            }

            Task[] tasks = objectMapper.readValue(file, Task[].class);
            List<Task> taskList = new ArrayList<>(List.of(tasks));

            // IDカウンターを最大ID+1に設定
            long maxId = taskList.stream()
                    .mapToLong(Task::getId)
                    .max()
                    .orElse(0);
            idCounter.set(maxId + 1);

            return taskList;
        } catch (IOException e) {
            System.err.println("タスクデータの読み込みに失敗しました: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * すべてのタスクを保存する
     */
    public void saveAllTasks(List<Task> tasks) {
        try {
            objectMapper.writeValue(new File(TASKS_FILE), tasks);
        } catch (IOException e) {
            System.err.println("タスクデータの保存に失敗しました: " + e.getMessage());
        }
    }

    /**
     * 新しいIDを生成する
     */
    public Long generateId() {
        return idCounter.getAndIncrement();
    }

    /**
     * 初期データをロードする（ファイルが存在しない場合）
     */
    public List<Task> loadInitialData() {
        List<Task> tasks = loadAllTasks();
        if (tasks.isEmpty()) {
            // 初期データを作成
            tasks = createSampleData();
            saveAllTasks(tasks);
        }
        return tasks;
    }

    /**
     * サンプルデータを作成
     */
    private List<Task> createSampleData() {
        List<Task> tasks = new ArrayList<>();

        tasks.add(createTask(1L, "プロジェクト計画書作成", "新規プロジェクトの計画書を作成する",
                LocalDateTime.of(2025, 9, 20, 17, 0), false, "HIGH", "WORK"));

        tasks.add(createTask(2L, "週次レポート提出", "今週の進捗レポートを上司に提出",
                LocalDateTime.of(2025, 9, 18, 12, 0), false, "MEDIUM", "WORK"));

        tasks.add(createTask(3L, "買い物リスト作成", "週末の買い物リストを作成する",
                LocalDateTime.of(2025, 9, 19, 19, 0), true, "LOW", "PERSONAL"));

        tasks.add(createTask(4L, "歯医者予約", "歯科検診の予約を取る",
                LocalDateTime.of(2025, 9, 25, 10, 0), false, "MEDIUM", "HEALTH"));

        tasks.add(createTask(5L, "書籍返却", "図書館の本を返却する",
                LocalDateTime.of(2025, 9, 17, 18, 0), false, "HIGH", "PERSONAL"));

        idCounter.set(6L);
        return tasks;
    }

    private Task createTask(Long id, String title, String description, LocalDateTime dueDate,
            boolean completed, String priority, String category) {
        Task task = new Task(title, description, dueDate, category, priority);
        task.setId(id);
        task.setCompleted(completed);
        task.setCreatedAt(LocalDateTime.now().minusDays(1));
        task.setUpdatedAt(LocalDateTime.now());
        return task;
    }
}
