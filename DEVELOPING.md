# 開発者向け情報

このドキュメントは `remindy` の開発者向けに、プロジェクトの構造やビルド方法について説明します。

## ディレクトリ構造

```
remindy/
├── .settings/      # Eclipseプロジェクト設定
├── src/
│   ├── main/
│   │   ├── java/jp/igapyon/remindy/
│   │   │   ├── Remindy.java                # メインの処理
│   │   │   ├── conv/
│   │   │   │   └── OutlookCsvToRemindersConv.java # Outlook CSVコンバーター
│   │   │   └── vo/
│   │   │       └── Reminder.java             # リマインダーVO
│   │   ├── resources/
│   │   │   ├── images/
│   │   │   │   └── remindy_icon_32x32.png  # トレイアイコン
│   │   │   ├── input/                      # 入力用ディレクトリ
│   │   │   ├── proverbs.json               # 格言リスト
│   │   │   └── reminders.json              # リマインダーリスト
│   └── module-info.java
├── target/         # ビルド成果物
├── .classpath      # Eclipseクラスパス設定
├── .gitignore      # Git無視ファイル
├── .project        # Eclipseプロジェクト設定
├── pom.xml         # Mavenプロジェクト設定
└── README.md       # プロジェクト概要
```

## 主要ファイル解説

- **`Remindy.java`**: アプリケーションのメインクラスです。システムトレイの管理、通知のスケジューリング、メッセージの構築など、中心的な処理を担います。
- **`OutlookCsvToRemindersConv.java`**: OutlookからエクスポートされたCSVファイルを `reminders.json` 形式に変換するためのユーティリティクラスです。
- **`Reminder.java`**: リマインダーの情報を格納するValue Object (VO) です。
- **`proverbs.json`**: 通知に表示される格言のリストです。
- **`reminders.json`**: 通知されるリマインダーのリストです。
- **`pom.xml`**: Mavenのビルド設定ファイルです。依存ライブラリやプラグインを定義しています。

## ビルドと実行

### 開発中の実行

```sh
mvn exec:java
```

### 配布用パッケージの作成

```sh
mvn package
```

`target` ディレクトリに `remindy-*-jar-with-dependencies.jar` という名前の実行可能なJARファイルが生成されます。
