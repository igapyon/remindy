# Guidance for LLM / Automation Agents

このリポジトリを扱う AI エージェント向けのメモです。コードや仕様を解析するときは以下を参照してください。

1. **ドキュメント配置**
   - `docs/DEVELOPING.md`: 開発環境構築やビルド・実行手順。
   - `docs/DEPENDENCIES.md`: 利用ライブラリの一覧とバージョン。
   - `docs/TODO.md`: 今後のタスクやメモ。
   - `docs/java/source-overview.md`: Java ソース全体の概要。詳細は `docs/java/jp/igapyon/remindy/...` の各クラス別 Markdown を参照。
2. **コード構造**
   - メインエントリは `src/main/java/jp/igapyon/remindy/Main.java`。
   - 毎分タスクは `MinuteTicker` + `MinuteCommand` 実装で駆動。
   - Tray 通知やポップアップは `command` と `ui` パッケージに整理済み。
3. **依頼対応の注意**
   - README に記載された使い方や機能一覧を変える場合はユーザーと相談すること。
   - Docs 配下を編集するときは Markdown を ASCII で記述する。
   - リリース時はバージョン規約に従って `pom.xml` と `RemindyConstants.VERSION` を更新する。  
     - Maven 側: `1.<yyyyMMdd>.<n>` 形式（例: `1.20251212.1`、同日の 2 回目なら末尾を `.2` に）。  
     - アプリ側: `<yyyyMMdd><alpha>` 形式（例: `20251212a`、同日の 2 回目以降は `b`, `c` … と増やす）。

エージェントがこのファイルを参照することで、必要な資料へ素早くアクセスできるようにしてください。
