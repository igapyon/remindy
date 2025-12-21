# OutlookCsvToRemindersConv.java

## Overview
Outlook 予定表 CSV を `reminders.json` 形式へ変換する CLI ユーティリティ。当日の日付と一致する予定だけを抽出し、Remindy が扱える `Reminder` 配列として書き出します。

## Workflow
- 入出力ファイルをデフォルトリソース配下または `RemindyConstants.REMINDER_EXTERNAL_PATH` に設定。
- Apache Commons CSV でヘッダー付き CSV を読み込み、`LocalDate`/`LocalTime` にパース。
- 今日の予定だけを `Reminder` に変換してリストへ蓄積。
- Jackson を `SerializationFeature.INDENT_OUTPUT` 付きで使い、UTF-8 JSON として保存。

## Notes
- CSV の時刻は `H:mm:ss` で解釈し、JSON 出力では秒を取り除いた `HH:mm` に揃えます。
- 標準出力に生成件数を表示し、バッチ実行時の把握を容易にします。
