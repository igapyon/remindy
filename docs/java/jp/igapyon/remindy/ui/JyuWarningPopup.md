# JyuWarningPopup.java

## Overview
`JyuWarningPanel` をフレームレスな `JWindow` に載せて最前面に表示するユーティリティ。非同期にポップアップを開き、指定時間後に自動で閉じます。

## Behavior
- `showPopup(message)` はデフォルトサイズ (800x320)・5 秒表示で `showPopup(message, width, height, durationMillis)` を呼び出し。
- `SwingUtilities.invokeLater` 内で `JWindow` を生成し、常に最前面・画面中央に配置。
- `javax.swing.Timer` で指定ミリ秒後に `dispose()` を呼び出し、自動クローズ。

## Notes
- 背景を不透明黒に固定し、Panel 側のデザインを際立たせます。
- 1 文呼び出しで完結する API なので、他のコマンドからも警告表示を簡単に追加できます。
