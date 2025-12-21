# PopupCommand.java

## Overview
ポップアップ専用の通知コマンド。現在時刻と一致する 🔔 予定だけを抽出し、最大 3 行を `JyuWarningPopup` の赤黒警告ウィンドウで表示します。

## Behavior
- `MessageBuilder.buildNowOnly` の結果リストが空なら即終了。
- 先頭 3 件までを `\n` 連結し、`JyuWarningPopup.showPopup` へ渡して非同期表示。
- トレイ通知とは独立しており、画面中央で強めの訴求を行う補助チャンネル。

## Notes
- 表示内容は `MessageBuilder` で切り詰め済みのテキストが使われるため、UI 側ではレイアウトのみを担当します。
