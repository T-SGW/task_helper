# TaskHelper テスト設計

## 1. 単体テスト（Unit Test）

### TaskService
- createTask(TaskDTO dto)
  - 正常ケース：TaskDTO を渡して Task が作成される
  - 異常ケース：必須項目が null の場合に例外をスロー
- updateTask(Long id, TaskDTO dto)
  - 正常ケース：既存タスクが更新される
  - 異常ケース：存在しない ID の場合に例外をスロー
- deleteTask(Long id)
  - 正常ケース：指定タスクが削除される
  - 異常ケース：存在しない ID の場合に例外をスロー
- getTask(Long id)
  - 正常ケース：タスクが取得できる
  - 異常ケース：存在しない ID の場合に null または例外
- getAllTasks()
  - 全タスクが取得できる
- getTasksDueSoon()
  - 期限が近いタスクのみ取得できる

### TemplateManager
- createTemplate(String name, TaskDTO dto)
  - 正常ケース：テンプレートが作成される
  - 異常ケース：name が空の場合に例外
- editTemplate(Long id, TaskDTO dto)
- deleteTemplate(Long id)
- applyTemplate(Long id)
  - 正常ケース：テンプレートからタスクが作成される
  - 異常ケース：存在しないテンプレートIDで例外

### NotificationManager
- checkDueTasks()
  - 期限が近いタスクが返る
- sendNotification(Task task)
  - GUI に通知が表示される

---

## 2. 結合テスト（Integration Test）

- TaskController と TaskService の連携
  - タスク追加 → DB に保存 → GUI 表示
- TaskController と TemplateManager の連携
  - テンプレート適用 → TaskService 経由でタスク作成
- Service と Repository の連携
  - CRUD 操作の整合性

---

## 3. GUI テスト

- タスク一覧画面
  - タスク追加・編集・削除操作
  - 並べ替え（ドラッグ＆ドロップ）
  - フィルタ・検索機能
- テンプレート管理画面
  - テンプレート追加・編集・削除操作
  - テンプレート適用
- タスク履歴画面
  - 完了タスクの表示・フィルタ
- 通知ポップアップ
  - 期限通知が正しく表示されるか
