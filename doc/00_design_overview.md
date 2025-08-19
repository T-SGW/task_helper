# TaskHelper 設計書一覧

本設計書は TaskHelper の開発・保守・拡張に必要な情報をまとめたものです。  
各ファイルを Git で管理します。

## 1. 要件定義

- [01_requirements.md](https://github.com/T-SGW/task_helper/blob/main/doc/01_requirements.md)
  - 背景と目的
  - 利用者・利用環境
  - 機能要件
  - 非機能要件
  - データフロー

## 2. 全体設計

- [02_overall_design.md](https://github.com/T-SGW/task_helper/blob/main/doc/02_overall_design.md)
  - システム構成
  - モジュール構成
  - 外部ライブラリ・依存関係

## 3. 機能設計

- [03_function_design.md](https://github.com/T-SGW/task_helper/blob/main/doc/03_function_design.md)
  - 機能一覧
  - 機能詳細

## 4. クラス設計

- [04_class_design.md](https://github.com/T-SGW/task_helper/blob/main/doc/04_class_design.md)
  - クラス概要
  - クラス関係図（Mermaid）
  - クラス説明

- [07_class_methods.md](https://github.com/T-SGW/task_helper/blob/main/doc/07_class_methods.md)
  - クラスごとのメソッド仕様

## 5. 画面設計

- [05_ui_design.md](https://github.com/T-SGW/task_helper/blob/main/doc/05_ui_design.md)
  - 画面レイアウト
  - 画面要素一覧

- [08_ui_detail.md](https://github.com/T-SGW/task_helper/blob/main/doc/08_ui_detail.md)
  - 画面詳細設計
  - イベントフロー

## 6. 操作フロー設計

- [06_operation_flow.md](https://github.com/T-SGW/task_helper/blob/main/doc/06_operation_flow.md)
  - タスク追加・編集・削除フロー
  - テンプレート利用フロー
  - 期限通知フロー

## 7. DB設計

- [09_db_schema.md](https://github.com/T-SGW/task_helper/blob/main/doc/09_db_schema.md)
  - Task テーブル
  - Template テーブル
  - インデックス設計
  - 注意点

## 8. テスト設計

- [10_test_design.md](https://github.com/T-SGW/task_helper/blob/main/doc/10_test_design.md)
  - 単体テスト
  - 結合テスト
  - GUI テスト

## 9. ビルド・配布設計

- [11_build_distribution.md](https://github.com/T-SGW/task_helper/blob/main/doc/11_build_distribution.md)
  - Maven ビルド
  - exe 化
  - 配布方法
  - 注意点

## 10. 運用・保守設計

- [12_maintenance.md](https://github.com/T-SGW/task_helper/blob/main/doc/12_maintenance.md)
  - データバックアップ
  - ログ管理
  - 障害対応
  - 設定管理
  - バージョン管理

## 11. 拡張設計

- [13_extension_design.md](https://github.com/T-SGW/task_helper/blob/main/doc/13_extension_design.md)
  - 多言語対応
  - 通知機能拡張
  - クラウド同期
  - プラグイン機能
  - テーマ・UIカスタマイズ
