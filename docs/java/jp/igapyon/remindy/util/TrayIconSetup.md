# TrayIconSetup.java

## Overview
システムトレイに `TrayIcon` を登録し、通知を行うための初期化ヘルパー。アイコン画像を読み込み、サポートされていればトレイへ追加します。

## Behavior
- `SystemTray.getSystemTray()` で環境を取得し、`images/remindy_icon_32x32.png` をクラスローダーから読み出して `Image` 化。
- `TrayIcon` を生成し `setImageAutoSize(true)` で高 DPI にも対応、トレイへ `add`。
- アイコンが見つからない／トレイ非対応／例外発生時は `null` を返し、呼び出し側で degrade。

## Notes
- 追加済みアイコンの参照を返すため、通知コマンドはこれを使って `displayMessage` を呼び出します。
