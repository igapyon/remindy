package jp.igapyon.remindy.loader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.igapyon.remindy.RemindyConstants;
import jp.igapyon.remindy.vo.Reminder;

public class ReminderLoader {
	public static List<Reminder> load() {
		ObjectMapper mapper = new ObjectMapper();

		try (Reader reader = createReader()) {
			List<Reminder> list = mapper.readValue(reader, new TypeReference<List<Reminder>>() {
			});
			list.sort((r1, r2) -> r1.time.compareTo(r2.time));
			System.err.println("リマインダーを " + list.size() + " 件読み込みました。");
			return list;
		} catch (Exception e) {
			System.err.println("reminders.json 読み込みエラー: " + e.getMessage());
			return Collections.emptyList();
		}
	}

	private static Reader createReader() throws IOException {
		if (!RemindyConstants.REMINDER_EXTERNAL_PATH.trim().isEmpty()) {
			File file = new File(RemindyConstants.REMINDER_EXTERNAL_PATH, "reminders.json");
			System.err.println("外部 reminders.json を読み込みます: " + file.getAbsolutePath());
			return new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8);
		} else {
			return new InputStreamReader(ReminderLoader.class.getClassLoader().getResourceAsStream("reminders.json"),
					StandardCharsets.UTF_8);
		}
	}
}
