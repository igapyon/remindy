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
import java.util.List;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.logic.MessageBuilder;
import jp.igapyon.remindy.ui.JyuWarningPopup;

/**
 * 🔔 時間 🔔 に該当するリマインダーがある場合にポップアップを表示するコマンドです。
 *
 * <p>
 * {@link MessageBuilder} の「今ちょうどの🔔のみ」を使用し、
 * 最初の3行だけを {@link JyuWarningPopup} により警告風ポップアップとして画面に表示します。
 * </p>
 *
 * <p>
 * 通常の通知（トレイ右下）は {@link NotifyCommand} が従来通り全文を表示します。
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
	 * メッセージに「ちょうどの時間の🔔」が存在する場合に、警告スタイルのポップアップを表示します。
	 *
	 * @param now 現在時刻（分単位）
	 */
	@Override
	public void execute(LocalTime now) {
		List<String> bellOnly = messageBuilder.buildNowOnly(now);
		if (bellOnly == null || bellOnly.isEmpty()) {
			return;
		}
		// 先頭3行まで表示
		String popupMessage = String.join("\n", bellOnly.subList(0, Math.min(3, bellOnly.size())));
		JyuWarningPopup.showPopup(popupMessage);
	}
}
