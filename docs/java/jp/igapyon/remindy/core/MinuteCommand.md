# MinuteCommand.java

## Overview
1 分おきに実行される処理の共通インターフェース。`MinuteTicker` から呼び出されるエントリポイントとして `execute(LocalTime now)` を定義します。

## Usage
- コマンド実装は現在時刻を受け取り、必要な処理（通知、UI 表示、入力デバイス操作など）を行います。
- 起動時のみ走らせたい処理も `MinuteCommand` を実装し、`MinuteTicker` が特別扱いできます（例: `StartupCommand`).

## Notes
- `now` の分精度に依存する設計なので、重い処理を入れすぎないようにします。
