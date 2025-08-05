package jp.igapyon.remindy.command;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.igapyon.remindy.core.MinuteCommand;
import jp.igapyon.remindy.vo.Reminder;

public class StartupCommand implements MinuteCommand {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

	private final String version;
	private final List<Reminder> reminders;

	public StartupCommand(String version, List<Reminder> reminders) {
		this.version = version;
		this.reminders = reminders != null ? reminders : Collections.emptyList();
	}

	@Override
	public void execute(LocalTime now) {
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
				// 無効な時刻フォーマットなどはスキップ
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
	}
}
