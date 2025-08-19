# TaskHelper DBスキーマ設計

## 1. Task テーブル

| カラム名        | 型              | 制約                | 説明 |
|----------------|----------------|------------------|------|
| id             | BIGINT         | PK, AUTO_INCREMENT | タスクID |
| title          | VARCHAR(255)   | NOT NULL          | タイトル |
| description    | TEXT           | NULL              | 説明 |
| due_date       | TIMESTAMP      | NULL              | 期限日時 |
| completed      | BOOLEAN        | NOT NULL DEFAULT FALSE | 完了状態 |
| category       | VARCHAR(50)    | NULL              | カテゴリ |
| priority       | VARCHAR(10)    | NULL              | 優先度（高/中/低） |
| created_at     | TIMESTAMP      | NOT NULL DEFAULT CURRENT_TIMESTAMP | 作成日時 |
| updated_at     | TIMESTAMP      | NOT NULL DEFAULT CURRENT_TIMESTAMP | 更新日時 |

---

## 2. Template テーブル

| カラム名        | 型              | 制約                | 説明 |
|----------------|----------------|------------------|------|
| id             | BIGINT         | PK, AUTO_INCREMENT | テンプレートID |
| name           | VARCHAR(255)   | NOT NULL          | テンプレート名 |
| task_title     | VARCHAR(255)   | NOT NULL          | タスクタイトル |
| task_description | TEXT         | NULL              | タスク説明 |
| task_due_date  | TIMESTAMP      | NULL              | タスク期限 |
| task_category  | VARCHAR(50)    | NULL              | タスクカテゴリ |
| task_priority  | VARCHAR(10)    | NULL              | タスク優先度 |
| created_at     | TIMESTAMP      | NOT NULL DEFAULT CURRENT_TIMESTAMP | 作成日時 |
| updated_at     | TIMESTAMP      | NOT NULL DEFAULT CURRENT_TIMESTAMP | 更新日時 |

---

## 3. インデックス設計

- Task テーブル
  - `IDX_TASK_DUE_DATE` : due_date カラムに対して作成（期限通知検索用）
- Template テーブル
  - `IDX_TEMPLATE_NAME` : name カラムに対して作成（テンプレート検索用）

---

## 4. 注意点

- H2DB のローカルファイルとして保存  
- DB マイグレーションは Flyway などを使用してバージョン管理可能  
- 日時カラムは LocalDateTime と対応
