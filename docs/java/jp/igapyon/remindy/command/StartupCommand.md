# StartupCommand.java

## Overview
アプリ起動直後に一度だけ実行される初期通知コマンド。バージョン情報と当日以降の予定を告知し、GUI/トレイ両方でユーザーに存在を知らせます。

## Key Steps
- 起動ログ: `System.err` にバージョンと今後の予定（現在時刻より後の `Reminder`）を列挙。
- GUI: `JyuWarningPopup` でアプリ説明を赤黒ポップアップ表示。
- Tray: トレイアイコンが存在すれば案内メッセージを `displayMessage` で送信。

## Notes
- `MinuteTicker` がクラス名で `StartupCommand` を判別し、他コマンドより前に別枠で実行します。
- `Reminder` リストは null 安全化されており、ソート済み想定ですが本クラス内でも現在時刻判定を行います。
