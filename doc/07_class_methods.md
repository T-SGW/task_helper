# TaskHelper クラスメソッド仕様

## 1. TaskController

| メソッド | 引数 | 戻り値 | 説明 |
|----------|------|--------|------|
| addTask | TaskDTO dto | void | 新規タスクを追加 |
| editTask | Long id, TaskDTO dto | void | 指定IDのタスクを編集 |
| removeTask | Long id | void | 指定IDのタスクを削除 |
| listTasks | なし | List<TaskDTO> | 全タスクを取得 |
| notifyDueTasks | なし | void | 期限が近いタスクを通知 |

## 2. TaskService

| メソッド | 引数 | 戻り値 | 説明 |
|----------|------|--------|------|
| createTask | TaskDTO dto | Task | タスク作成 |
| updateTask | Long id, TaskDTO dto | Task | タスク更新 |
| deleteTask | Long id | void | タスク削除 |
| getTask | Long id | Task | 指定IDのタスク取得 |
| getAllTasks | なし | List<Task> | 全タスク取得 |
| getTasksDueSoon | なし | List<Task> | 期限が近いタスク取得 |

## 3. TaskRepository

| メソッド | 引数 | 戻り値 | 説明 |
|----------|------|--------|------|
| save | Task task | Task | DBに保存 |
| findById | Long id | Optional<Task> | 指定IDのタスク検索 |
| findAll | なし | List<Task> | 全タスク取得 |
| deleteById | Long id | void | 指定IDのタスク削除 |
| findByDueDateBefore | LocalDateTime date | List<Task> | 指定日時前のタスク検索 |

## 4. TemplateManager

| メソッド | 引数 | 戻り値 | 説明 |
|----------|------|--------|------|
| createTemplate | String name, TaskDTO dto | Template | テンプレート作成 |
| editTemplate | Long id, TaskDTO dto | Template | テンプレート編集 |
| deleteTemplate | Long id | void | テンプレート削除 |
| applyTemplate | Long id | Task | テンプレートからタスク作成 |

## 5. NotificationManager

| メソッド | 引数 | 戻り値 | 説明 |
|----------|------|--------|------|
| checkDueTasks | なし | List<Task> | 期限が近いタス
