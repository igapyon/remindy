# LogCommand.java

## Overview
デバッグ向けの簡易コマンド。毎分の tick で現在時刻を標準出力に表示し、`MinuteTicker` の駆動状況を確認する目的で使用します。

## Behavior
- `execute(LocalTime now)` で `System.out.println("🔔 Tick at: " + now);` を出力するだけの軽量処理。

## Notes
- デフォルト構成では登録されていませんが、必要に応じて `MinuteTicker` へ追加することでタイマーループの監視に役立ちます。
