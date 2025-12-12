# RemindyConstants.java

## Overview
アプリ全体で参照する定数を集約したクラス。バージョン文字列と外部設定ディレクトリの場所を保持し、設定の一元管理を実現します。

## Fields
- `VERSION`: 現在のリリース識別子 (例: 20251001a)。起動ログやポップアップ表示に使用。
- `REMINDER_EXTERNAL_PATH`: 空でなければ reminders.json/outlook CSV の読み書きを外部ディレクトリに切り替えるためのルート。

## Notes
- インスタンス化しない想定のためメンバーはすべて `public static final`。
- 外部パスを設定することで組み込みリソースを書き換えずにユーザー独自の定義を扱えます。
