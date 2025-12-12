# 開発者向け情報

このドキュメントは `remindy` の開発者向けに、プロジェクトの構造やビルド方法について説明します。

---

## 📁 ディレクトリ構造

```
remindy/
├── .settings/      # Eclipseプロジェクト設定（任意）
├── src/
│   ├── main/
│   │   ├── java/jp/igapyon/remindy/
│   │   │   ├── Main.java                  # メインエントリポイント
│   │   │   ├── RemindyConstants.java      # 定数管理クラス
│   │   │   ├── command/                   # Commandパターンによる機能実装
│   │   │   │   ├── NotifyCommand.java
│   │   │   │   ├── PopupCommand.java
│   │   │   │   ├── StartupCommand.java
│   │   │   │   ├── PikoMouseCommand.java
│   │   │   │   └── LogCommand.java
│   │   │   ├── conv/
│   │   │   │   └── OutlookCsvToRemindersConv.java
│   │   │   ├── core/
│   │   │   │   ├── MinuteTicker.java      # 毎分のイベント駆動
│   │   │   │   └── MinuteCommand.java     # コマンドインタフェース
│   │   │   ├── loader/
│   │   │   │   ├── ReminderLoader.java
│   │   │   │   └── ProverbLoader.java
│   │   │   ├── logic/
│   │   │   │   └── MessageBuilder.java
│   │   │   ├── ui/
│   │   │   │   ├── JyuWarningPanel.java   # カスタムポップアップ描画
│   │   │   │   └── JyuWarningPopup.java   # ポップアップ表示ユーティリティ
│   │   │   ├── util/
│   │   │   │   └── TrayIconSetup.java     # トレイアイコン設定
│   │   │   └── vo/
│   │   │       └── Reminder.java
│   └── module-info.java                   # （未使用またはJavaモジュール用）
│
├── src/main/resources/
│   ├── images/
│   │   └── remindy_icon_32x32.png
│   ├── input/
│   │   └── outlook-calendar.csv           # Outlook連携入力用CSV
│   ├── reminders.json                     # リマインダーリスト
│   └── proverbs.json                      # 格言リスト
│
├── target/                                # Mavenビルド成果物
├── pom.xml                                # Mavenビルド設定
├── .classpath                             # Eclipseクラスパス設定
├── .project                               # Eclipseプロジェクト設定
├── .gitignore
└── README.md
```

---

## 🔧 主要ファイル解説

| ファイル・クラス名                                    | 役割                                                  |
| -------------------------------------------- | --------------------------------------------------- |
| `Main.java`                                  | アプリケーションのエントリーポイント。トレイアイコンや `MinuteTicker` の初期化を担当。 |
| `MinuteTicker.java`                          | 毎分タイミングで `MinuteCommand` 群を順に実行。                    |
| `StartupCommand.java`                        | 起動時にリマインド予定とバージョン情報をコンソール・ポップアップ・トレイに表示。            |
| `NotifyCommand.java`                         | 10分刻みと予定時刻で通知メッセージをシステムトレイに表示。                |
| `PopupCommand.java`                          | 🔔リマインダー時刻と一致した場合にカスタムポップアップを表示。                    |
| `PikoMouseCommand.java`                      | 毎分マウスを微小移動し、スリープ抑止（お好みで有効化）。                        |
| `MessageBuilder.java`                        | リマインダーと格言を統合し、メッセージを構築。                             |
| `ReminderLoader.java` / `ProverbLoader.java` | `reminders.json` / `proverbs.json` の読み込み処理。         |
| `OutlookCsvToRemindersConv.java`             | Outlook予定表CSV → JSON変換ツール。                          |

---

## 🛠 ビルドと実行方法

### 開発中のテスト実行

```sh
mvn exec:java
```

### 実行可能 JAR の作成

```sh
mvn package
```

* 出力先: `target/remindy-*-jar-with-dependencies.jar`

### 実行例

```sh
java -jar target/remindy-1.20250805.1-jar-with-dependencies.jar
```

---

## 💡 備考

* `StartupCommand` は `MinuteTicker.start()` の冒頭で1度だけ明示的に実行される仕組みです。
* トレイアイコンは `SystemTray` 非対応環境では表示されない場合がありますが、動作自体には影響しません。
* `reminders.json` や `proverbs.json` は `resources/` に配置し、Maven 実行時はクラスパス経由で読み込まれます。
* 外部パスの設定には `RemindyConstants.REMINDER_EXTERNAL_PATH` を使用できます。
