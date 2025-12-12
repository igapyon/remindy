# Reminder.java

## Overview
時刻とメッセージから成るシンプルなデータオブジェクト。`reminders.json` からロードされ、通知判定やメッセージ生成で使用されます。

## Fields
- `public String time` — `HH:mm` 形式の通知時刻。
- `public String message` — その時刻に表示する本文。

## Usage
- `ReminderLoader` が JSON を `Reminder` 配列として読み込みます。
- `MessageBuilder` や `NotifyCommand` がフィールドを直接参照するため、ゲッター/セッターは不要な構造になっています。
