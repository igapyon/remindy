# ReminderLoader.java

## Overview
`reminders.json` を読み込み、通知対象の `Reminder` リストを返すユーティリティ。外部ディレクトリが指定されている場合はそちらを優先し、読み込み後は時刻順にソートします。

## Workflow
- `createReader()` でリソース or 外部ファイルの入力ストリームを生成（UTF-8）。
- Jackson (`ObjectMapper`) で `List<Reminder>` にデシリアライズし、`time` 文字列で昇順ソート。
- 読み込み件数をログ出力し、失敗時は空リストを返してアプリを継続可能にする。

## Notes
- 外部パスが空文字でなければ `RemindyConstants.REMINDER_EXTERNAL_PATH/reminders.json` を使用。
- JSON 形式エラーの際は例外内容を標準エラーに流し、既定では通知機能が無効状態になります。
