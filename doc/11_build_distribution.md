# TaskHelper ビルド・配布設計

## 1. ビルドツール

- Maven を使用  
  - Java 17, Spring Boot 2.5.10, H2DB, JavaFX を管理
- 依存関係は `pom.xml` で管理
- プロファイル設定
  - `dev`：開発環境
  - `prod`：本番配布用

---

## 2. exe 化

- JavaFX アプリを exe 化する方法
  1. **jpackage** を使用
     - JDK 17 に含まれるツール
     - 依存する JavaFX ライブラリをバンドル
     - 自動で launcher を生成
  2. コマンド例
     ```
     jpackage --input target/dependency --name TaskHelper --main-jar TaskHelper-1.0.jar --main-class com.example.MainApp --type exe
     ```
  3. 出力結果
     - TaskHelper.exe（Windows で単独実行可能）

- 配布ファイルには以下を含める
  - exe 本体
  - H2DB データファイル（ローカル保存用）
  - 設定ファイル（必要に応じて）

---

## 3. 配布方法

- ZIP 圧縮して配布
  - 例：`TaskHelper.zip` に exe と DB ファイルを含める
- 配布先 PC では Java ランタイム不要（jpackage によりバンドル済み）

---

## 4. 注意点

- JavaFX のライブラリは exe にバンドルする
- H2DB はローカルファイル保存なので、ファイルパスの権限に注意
- 今後のアップデート時は、バージョン番号を pom.xml と一致させて再ビルド
