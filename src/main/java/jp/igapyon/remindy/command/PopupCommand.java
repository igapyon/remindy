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
package jp.igapyon.remindy.command;

import java.time.LocalTime;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.logic.MessageBuilder;
import jp.igapyon.remindy.ui.JyuWarningPopup;

/**
 * 🔔 時間 🔔 に該当するリマインダーがある場合にポップアップを表示するコマンドです。
 *
 * <p>
 * {@link MessageBuilder} により生成されたメッセージ内に「🔔時間🔔」というキーワードが含まれている場合に、 最初の3行だけを
 * {@link JyuWarningPopup} により警告風ポップアップとして画面に表示します。
 * </p>
 *
 * <p>
 * 通常の通知と異なり、重要なリマインダーを視覚的に強調したい場合に活用されます。
 * </p>
 *
 * @author Toshiki Iga
 */
public class PopupCommand implements MinuteCommand {
	private final MessageBuilder messageBuilder;

	/**
	 * コンストラクタ。
	 *
	 * @param messageBuilder リマインダーおよび格言を元にメッセージを構築するビルダー
	 */
	public PopupCommand(MessageBuilder messageBuilder) {
		this.messageBuilder = messageBuilder;
	}

	/**
	 * メッセージに「🔔時間🔔」が含まれている場合に、警告スタイルのポップアップを表示します。
	 *
	 * @param now 現在時刻（分単位）
	 */
	@Override
	public void execute(LocalTime now) {
		String message = messageBuilder.build(now);
		if (message != null && message.contains("🔔時間🔔")) {
			// 🔔 を含むメッセージだけポップアップ表示
			String[] lines = message.split("\\R");
			String popupMessage = String.join("\n", limit(lines, 3)); // 最初の3行だけ表示
			JyuWarningPopup.showPopup(popupMessage);
		}
	}

	/**
	 * 指定した行数までのメッセージを返します。
	 *
	 * @param lines メッセージ行の配列
	 * @param max   最大行数
	 * @return 最初の max 行までの配列
	 */
	private String[] limit(String[] lines, int max) {
		int limit = Math.min(lines.length, max);
		String[] result = new String[limit];
		System.arraycopy(lines, 0, result, 0, limit);
		return result;
	}
}
