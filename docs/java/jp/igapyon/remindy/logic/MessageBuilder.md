# MessageBuilder.java

## Overview
通知用メッセージを構築する中心ロジック。現在時刻に一致する予定、これからの予定、格言を組み合わせて複数行テキストを生成します。

## Key Responsibilities
- 🔔 現在時刻の `Reminder` を検出し、`"🔔時間🔔 メッセージ"` 形式で列挙。
- 🗓 未来の予定に対し、分差または時間差を算出して「HH:mm (n分後/時間後) メッセージ」を生成。
- 💡 格言リストを巡回し、呼び出し毎に次の格言を付与（内部インデックスで管理）。
- `buildNowOnly` で副作用なしに現在時刻の 🔔 行だけを返すユーティリティも提供。

## Implementation Notes
- 全メッセージは最大 24 文字でトリムし UI を見やすく維持。
- `Reminders`/`proverbs` が null の場合でも空コレクションに差し替えて安全に処理。
- 時刻処理は `LocalTime` + `DateTimeFormatter`（HH:mm）で統一し、分→時間変換は 0.1 時間刻みで表現しています。
