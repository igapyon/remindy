# ProverbLoader.java

## Overview
リソース内の `proverbs.json` を読み込み、格言文字列のリストを返すユーティリティ。読み込みに成功すると件数を標準エラーに報告し、失敗時は空リストを返します。

## Workflow
- クラスローダーから `proverbs.json` を取得し、`InputStreamReader` (UTF-8) で `ObjectMapper` に渡す。
- 汎用 `TypeReference<List<String>>` を使って安全にデシリアライズ。

## Notes
- 外部パス切り替えは行わず、常に組み込みリソースを参照。
- 例外を握りつぶさずに内容をログへ出力し、格言がなくてもアプリは動作継続します。
