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

public class StartupCommand implements MinuteCommand {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private final String version;
	private final TrayIcon trayIcon;
	private final List<Reminder> reminders;

	public StartupCommand(String version, TrayIcon trayIcon, List<Reminder> reminders) {
		this.version = version;
		this.trayIcon = trayIcon;
		this.reminders = reminders != null ? reminders : Collections.emptyList();
	}

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
		String popupMsg = "Remindy (" + version + ")" + "\nリマインドと名言を毎分通知します";
		JyuWarningPopup.showPopup(popupMsg);

		// Trayアイコン通知
		if (trayIcon != null) {
			trayIcon.displayMessage("Remindy", "リマインドと名言を毎分通知します", TrayIcon.MessageType.INFO);
		}
	}
}
