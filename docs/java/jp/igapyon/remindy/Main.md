# Main.java

## Overview
アプリケーションのエントリポイント。外部 CSV からのリマインダー変換、トレイアイコン初期化、設定ファイル読み込み、毎分コマンド群の登録と開始までを一括で行います。

## Key Responsibilities
- `RemindyConstants.REMINDER_EXTERNAL_PATH` が設定されていれば `OutlookCsvToRemindersConv` を実行して reminders.json を生成。
- `TrayIconSetup` でトレイアイコンを初期化し、通知チャネルを確保。
- `ReminderLoader`/`ProverbLoader` でデータをロードし `MessageBuilder` を組み立て。
- `MinuteTicker` に `StartupCommand`、`NotifyCommand`、`PopupCommand`、`PikoMouseCommand` を登録し、毎分監視を開始。
- メインスレッドをスリープループで維持し Timer スレッドを生かす。

## Interactions
- Command 層とは `MinuteTicker` 経由でやりとり。
- データは loader パッケージから取得し、UI には tray/popup を通して通知します。
