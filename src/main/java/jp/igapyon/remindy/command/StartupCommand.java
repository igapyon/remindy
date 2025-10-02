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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.ui.JyuWarningPopup;
import jp.igapyon.remindy.vo.Reminder;

/**
 * アプリケーションの起動時に一度だけ実行されるコマンドです。
 *
 * <p>
 * 起動時に以下の処理を行います：
 * <ul>
 * <li>バージョン情報と通知予定をコンソールに出力</li>
 * <li>警告風の GUI ポップアップで起動メッセージを表示</li>
 * <li>Tray アイコンに通知を表示</li>
 * </ul>
 * </p>
 *
 * このクラスは {@link jp.igapyon.remindy.core.MinuteTicker} により明示的に
 * 起動直後に一度だけ呼び出されるように設計されています。
 *
 * @author Toshiki Iga
 */
public class StartupCommand implements MinuteCommand {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private final String version;
	private final TrayIcon trayIcon;
	private final List<Reminder> reminders;

	/**
	 * コンストラクタ。
	 *
	 * @param version   バージョン番号文字列
	 * @param trayIcon  システムトレイに表示するトレイアイコン
	 * @param reminders リマインダー一覧
	 */
	public StartupCommand(String version, TrayIcon trayIcon, List<Reminder> reminders) {
		this.version = version;
		this.trayIcon = trayIcon;
		this.reminders = reminders != null ? reminders : Collections.emptyList();
	}

	/**
	 * 起動時に実行される処理本体。
	 *
	 * @param now 現在時刻（未使用だがインターフェースに従って受け取る）
	 */
	@Override
	public void execute(LocalTime now) {
		// コンソール出力
		System.err.println("Remindy 起動 (" + version + ")");
		System.err.println("今後の通知予定:");

		LocalTime current = LocalTime.now();
		List<String> futureReminders = new ArrayList<>();

		for (Reminder r : reminders) {
			try {
				LocalTime reminderTime = LocalTime.parse(r.time, TIME_FORMATTER);
				if (reminderTime.isAfter(current)) {
					futureReminders.add(String.format("🗓 %s - %s", r.time, r.message));
				}
			} catch (Exception e) {
				System.err.println("無効なリマインダー時刻: " + r.time);
			}
		}

		if (futureReminders.isEmpty()) {
			System.err.println("（なし）");
		} else {
			for (String line : futureReminders) {
				System.err.println(line);
			}
		}

		// GUIポップアップ表示
		String popupMsg = "Remindy (" + version + ")" + "\nリマインドと名言を10分ごとと予定時刻にお知らせします";
		JyuWarningPopup.showPopup(popupMsg);

		// Trayアイコン通知
		if (trayIcon != null) {
			trayIcon.displayMessage("Remindy", "リマインドと名言を10分ごとと予定時刻にお知らせします", TrayIcon.MessageType.INFO);
		}
	}
}
