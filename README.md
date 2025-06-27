# Remindy

Java (AWT) で動作するデスクトップ常駐型のリマインダーツールです。システムトレイに常駐し、毎分通知を行います。

基本的に Windows 11 上で動作します。

## 主な機能

- **毎分通知**: 今後の予定、およびランダムな格言を毎分システム通知で表示します。
- **リマインダー**: `reminders.json` に登録した予定を、指定時刻に通知します。
- **今後の予定**: 次の予定までの残り時間を表示します。
- **格言**: `proverbs.json` に登録した格言をランダムに表示します。
- **Outlook連携**: OutlookからエクスポートしたCSVファイルを、リマインダー形式に変換するツール (`OutlookCsvToRemindersConv.java`) を同梱しています。

## 必要なもの

- Java 1.8 以降
- Maven (ビルドする場合)

## 使用方法

### 1. 設定

リソースファイルにリマインドしたいことや格言を記述します。

- **リマインダー**: `src/main/resources/reminders.json`
- **格言**: `src/main/resources/proverbs.json`

#### `reminders.json`

時刻 (`HH:mm`) とメッセージのリストを記述します。

```json
[
  {
    "time": "09:00",
    "message": "朝のミーティング"
  },
  {
    "time": "12:30",
    "message": "昼休み"
  },
  {
    "time": "18:00",
    "message": "業務終了"
  }
]
```

#### `proverbs.json`

表示したい格言のリストを記述します。

```json
[
  "時は金なり。",
  "継続は力なり。",
  "明日は明日の風が吹く。"
]
```

### 2. 実行

Mavenを使用してプロジェクトを実行します。

```sh
# Mavenで直接実行
mvn exec:java
```

または、実行可能なjarファイルをビルドして実行することもできます。

```sh
# ビルド
mvn package

# 実行
java -jar target/remindy-*-jar-with-dependencies.jar
```

## ビルド

ソースコードからビルドする場合は、以下のコマンドを実行します。

```sh
mvn package
```

## ライセンス

このプロジェクトは Apache License, Version 2.0 の下でライセンスされています。詳細については、プロジェクトルートにある `LICENSE` ファイルを参照してください。

