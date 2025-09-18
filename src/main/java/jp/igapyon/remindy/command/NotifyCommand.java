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

import java.awt.TrayIcon;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.logic.MessageBuilder;

/**
 * リマインダーのメッセージをトレイ通知として表示するコマンド。
 * <p>
 * {@link MessageBuilder} により生成された内容をトレイに通知し、コンソールにも出力します。
 * </p>
 * 
 * 出力例:
 * 
 * <pre>
 * 【通知】12:34:00
 * 🔔時間🔔 ミーティング
 * </pre>
 * 
 * <p>
 * ただし通知を行うのは以下の条件に限定されます:
 * <ul>
 * <li>現在時刻にちょうど一致する 🔔 リマインダーがある場合</li>
 * <li>または分が 00, 10, 20, 30, 40, 50 のいずれかの場合</li>
 * </ul>
 * </p>
 * 
 * @author Toshiki Iga
 */
public class NotifyCommand implements MinuteCommand {
	private final TrayIcon trayIcon;
	private final MessageBuilder messageBuilder;
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

	/**
	 * コンストラクタ。
	 * 
	 * @param trayIcon       トレイ通知に使用するアイコン
	 * @param messageBuilder メッセージ生成用ロジック
	 */
	public NotifyCommand(TrayIcon trayIcon, MessageBuilder messageBuilder) {
		this.trayIcon = trayIcon;
		this.messageBuilder = messageBuilder;
	}

	/**
	 * 現在時刻に基づいてメッセージを生成し、条件を満たす場合のみ トレイ通知およびコンソールに表示します。
	 * 
	 * @param now 現在の時刻
	 */
	@Override
	public void execute(LocalTime now) {
		// 🔔ちょうどのリマインダーがあるか？
		List<String> bellOnly = messageBuilder.buildNowOnly(now);
		boolean hasBellNow = bellOnly != null && !bellOnly.isEmpty();

		// 10分刻みか？（00,10,20,30,40,50分）
		boolean isTenMinuteMark = (now.getMinute() % 10) == 0;

		// どちらでもなければ通知しない
		if (!(hasBellNow || isTenMinuteMark)) {
			return;
		}

		// 通知実行
		String message = messageBuilder.build(now);
		if (trayIcon != null && message != null && !message.isEmpty()) {
			trayIcon.displayMessage("Remindy", message, TrayIcon.MessageType.INFO);
			System.err.println("【通知】" + now.format(TIME_FORMATTER) + "\n" + message);
		}
	}
}
