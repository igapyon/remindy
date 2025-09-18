/*
 * Copyright 2025 Toshiki Iga
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.igapyon.remindy;

/**
 * Remindy アプリケーションの定数クラス。
 * <p>
 * アプリケーションのバージョン情報や外部設定ファイルのパスなど、定数を定義します。
 * </p>
 * 
 * <ul>
 * <li>{@link #VERSION} は現在のアプリケーションバージョンを示します。</li>
 * <li>{@link #REMINDER_EXTERNAL_PATH} は reminders.json
 * を外部から読み込む場合のディレクトリパスです。</li>
 * </ul>
 * 
 * <p>
 * このクラスはインスタンス化しません。
 * </p>
 * 
 * @author Toshiki Iga
 */
public class RemindyConstants {
	/**
	 * Remindy アプリケーションのバージョン。
	 */
	public static final String VERSION = "20250917a";

	/**
	 * reminders.json を外部パスから読み込む場合のディレクトリパス。
	 * <p>
	 * 空文字列の場合は組み込みリソースから読み込みます。
	 * </p>
	 */
	public static final String REMINDER_EXTERNAL_PATH = "";
}
