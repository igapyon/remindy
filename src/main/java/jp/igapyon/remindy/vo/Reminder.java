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
package jp.igapyon.remindy.vo;

/**
 * {@code Reminder} は、時刻指定のメッセージを表すデータオブジェクトです。
 *
 * <p>
 * 通常は JSON ファイル（reminders.json）などから読み込まれ、 指定時刻になると通知される予定の情報を保持します。
 * </p>
 *
 * <p>
 * 主に {@link jp.igapyon.remindy.logic.MessageBuilder} や
 * {@link jp.igapyon.remindy.command.NotifyCommand} などで使用されます。
 * </p>
 *
 * <pre>
 * 使用例:
 * {
 *   "time": "09:00",
 *   "message": "朝会"
 * }
 * </pre>
 *
 * @author Toshiki Iga
 * @since 1.0
 */
public class Reminder {
	/**
	 * リマインダーを通知する時刻（フォーマット: "HH:mm"）。 例: {@code "09:00"}
	 */
	public String time;

	/**
	 * 指定時刻に表示されるメッセージ内容。 例: {@code "朝会"}
	 */
	public String message;
}
