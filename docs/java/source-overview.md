# Remindy Java ソース概要

このドキュメントは `src/main/java` 直下にある各 Java ファイルの役割を簡潔にまとめたものです。パッケージ毎に主要クラスを列挙し、処理の流れが追いやすいように整理しています。より詳しい説明は、同ディレクトリ配下に生成した個別 Markdown（例: `docs/java/jp/igapyon/remindy/command/NotifyCommand.md`）を参照してください。

## エントリポイントと定数

- `jp/igapyon/remindy/Main.java`  
  アプリのエントリポイント。外部 CSV からの変換実行、トレイアイコン初期化、リマインダー/格言の読み込み、`MinuteTicker` の構築と各種コマンド登録を担います。
- `jp/igapyon/remindy/RemindyConstants.java`  
  バージョン番号や外部 `reminders.json` 参照パスといったアプリ全体で利用する定数を保持します。
- `module-info.java`  
  JPMS モジュール宣言。現在は `Remindy` というモジュール名のみ定義されています。

## コア: 毎分実行フレームワーク

- `jp/igapyon/remindy/core/MinuteCommand.java`  
  毎分実行される処理を表すインターフェース。`execute(LocalTime now)` を実装して実際の処理を記述します。
- `jp/igapyon/remindy/core/MinuteTicker.java`  
  `Timer` を用いて 00 秒にそろえた 1 分周期を作り、登録された `MinuteCommand` を順次呼び出します。`StartupCommand` だけは起動直後にも即時実行します。

## コマンド群（`jp.igapyon.remindy.command`）

- `StartupCommand.java`  
  起動直後 1 回だけ実行され、バージョン表示・今後の予定出力・警告風ポップアップ・トレイ通知を同時に行います。
- `NotifyCommand.java`  
  10 分刻みもしくは予定時刻ぴったりの際に `MessageBuilder` で生成したメッセージをトレイ通知＆コンソールへ出力します。
- `PopupCommand.java`  
  予定時刻ぴったりのメッセージのみを抽出し、最大 3 行を `JyuWarningPopup` で画面中央に警告表示します。
- `PikoMouseCommand.java`  
  `Robot` でマウスカーソルをランダム方向へ 1px 動かし、スクリーンセーバーなどを抑止する補助機能を提供します。
- `LogCommand.java`  
  毎分の tick をログに出すシンプルなコマンド。主にデバッグ用途です（現在は未登録）。

## ローダーとデータ

- `jp/igapyon/remindy/loader/ReminderLoader.java`  
  `reminders.json` を読み込み、時刻文字列でソートした `Reminder` リストを返却します。外部パスが指定されていればそちらを優先します。
- `jp/igapyon/remindy/loader/ProverbLoader.java`  
  `proverbs.json` から格言リストを読み込みます。失敗時は空リストを返します。
- `jp/igapyon/remindy/vo/Reminder.java`  
  `time` と `message` を持つシンプルなデータオブジェクト。JSON バインド用の public フィールドを備えます。

## ロジックとユーティリティ

- `jp/igapyon/remindy/logic/MessageBuilder.java`  
  現在時刻に合わせて通知文を組み立てるクラス。現在時刻の 🔔 リマインダー、未来の予定（分/時間後表示）、格言の順で行を生成します。格言は呼び出しごとに巡回し、各行は最大 24 文字にトリムされます。
- `jp/igapyon/remindy/util/TrayIconSetup.java`  
  リソース内の PNG を読み込みシステムトレイへ `TrayIcon` を登録するヘルパー。環境が非対応の場合は `null` を返して安全に degrade します。

## UI コンポーネント

- `jp/igapyon/remindy/ui/JyuWarningPanel.java`  
  赤黒ストライプを持つ警告デザインの `JPanel`。中央に複数行テキストを描画し、`JyuWarningPopup` の内容として利用されます。
- `jp/igapyon/remindy/ui/JyuWarningPopup.java`  
  `JWindow` を用いた最前面の警告ポップアップ。非同期に表示し、タイマーで自動クローズします。デフォルトでは 800x320px・5 秒表示。

## 変換ツール

- `jp/igapyon/remindy/conv/OutlookCsvToRemindersConv.java`  
  Outlook 予定表の CSV を読み込み、当日分の予定を HH:mm 形式の `reminders.json` に変換する CLI ユーティリティ。`RemindyConstants.REMINDER_EXTERNAL_PATH` が設定されている場合は入出力先を外部ディレクトリに切り替えます。
