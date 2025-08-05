package jp.igapyon.remindy.logic;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.igapyon.remindy.vo.Reminder;

public class MessageBuilder {
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	private static final int MAX_MESSAGE_LENGTH = 24;

	private final List<Reminder> reminders;
	private final List<String> proverbs;
	private int proverbIndex = 0;

	public MessageBuilder(List<Reminder> reminders, List<String> proverbs) {
		this.reminders = reminders != null ? reminders : Collections.emptyList();
		this.proverbs = proverbs != null ? proverbs : Collections.emptyList();
	}

	public String build(LocalTime now) {
		String nowStr = now.format(TIME_FORMATTER);
		List<String> lines = new ArrayList<>();

		// 🔔 リマインダー一致
		for (Reminder r : reminders) {
			if (nowStr.equals(r.time)) {
				lines.add(truncate("🔔時間🔔 " + r.message));
			}
		}

		// 🗓 今後の予定
		for (Reminder r : reminders) {
			LocalTime rTime = LocalTime.parse(r.time, TIME_FORMATTER);
			if (rTime.isAfter(now)) {
				long minutes = Math.round(Duration.between(now, rTime).getSeconds() / 60.0);
				String future;
				if (minutes > 60) {
					double hours = Math.floor(minutes / 6.0) / 10.0;
					future = truncate(String.format("%s (%.1f時間後) ", r.time, hours) + r.message);
				} else {
					future = truncate(String.format("%s (%d分後) ", r.time, minutes) + r.message);
				}
				lines.add("🗓 " + future);
			}
		}

		// 💡 格言（毎回順繰り）
		if (!proverbs.isEmpty()) {
			lines.add("💡 " + proverbs.get(proverbIndex));
			proverbIndex = (proverbIndex + 1) % proverbs.size();
		}

		return String.join("\n", lines);
	}

	private String truncate(String msg) {
		if (msg == null)
			return "";
		if (msg.length() <= MAX_MESSAGE_LENGTH)
			return msg;
		return msg.substring(0, MAX_MESSAGE_LENGTH);
	}
}
