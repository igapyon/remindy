# NotifyCommand.java

## Overview
Tray アイコンを使った定期通知処理。`MessageBuilder` が組み立てた全文メッセージを、10 分刻みまたはリマインダー時刻ぴったりの際に表示します。

## Behavior
- `messageBuilder.buildNowOnly` で 🔔 リマインダーを確認し、0 件かつ 10 分刻みでなければ終了。
- 条件を満たす場合、`messageBuilder.build` の結果をトレイ通知 (`TrayIcon.displayMessage`) と標準エラーに出力。
- 時刻ログは `HH:mm:ss` でフォーマットされ、通知の痕跡を CLI 上に残します。

## Notes
- `TrayIcon` が `null` の場合（トレイ非対応環境）は何もしない設計。
- メッセージが空文字の場合の安全策も取っており、ビルダー結果のチェック後にだけ通知。
