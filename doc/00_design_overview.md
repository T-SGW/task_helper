# TaskHelper 設計書一覧

本設計書は TaskHelper の開発・保守・拡張に必要な情報をまとめたものです。  
各ファイルを Git で管理します。

## 1. 要件定義

- 01_requirements.md
  - 背景と目的
  - 利用者・利用環境
  - 機能要件
  - 非機能要件
  - データフロー

## 2. 全体設計

- 02_system_overview.md
  - システム構成
  - モジュール構成
  - 外部ライブラリ・依存関係

## 3. クラス設計

- 04_class_design.md
  - クラス概要
  - クラス関係図（Mermaid）
  - クラス説明

- 07_class_methods.md
  - クラスごとのメソッド仕様

## 4. 画面設計

- 05_ui_design.md
  - 画面レイアウト
  - 画面要素一覧

- 08_ui_detail.md
  - 画面詳細設計
  - イベントフロー

## 5. 操作フロー設計

- 06_operation_flow.md
  - タスク追加・編集・削除フロー
  - テンプレート利用フロー
  - 期限通知フロー

## 6. DB設計

- 09_db_schema.md
  - Task テーブル
  - Template テーブル
  - インデックス設計
  - 注意点

## 7. テスト設計

- 10_test_design.md
  - 単体テスト
  - 結合テスト
  - GUI テスト

## 8. ビルド・配布設計

- 11_build_distribution.md
  - Maven ビルド
  - exe 化
  - 配布方法
  - 注意点

## 9. 運用・保守設計

- 12_maintenance.md
  - データバックアップ
  - ログ管理
  - 障害対応
  - 設定管理
  - バージョン管理

## 10. 拡張設計

- 13_extension_design.md
  - 多言語対応
  - 通知機能拡張
  - クラウド同期
  - プラグイン機能
  - テーマ・UIカスタマイズ
