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

/**
 * {@code ReminderLoader} は、{@code reminders.json} を読み込み
 * リマインダーの一覧を返すユーティリティクラスです。
 *
 * <p>
 * 読み込みに失敗した場合は空のリストを返します。 リマインダーは時刻順にソートされて返されます。
 * </p>
 *
 * <p>
 * 外部ファイルが {@link RemindyConstants#REMINDER_EXTERNAL_PATH} に
 * 指定されている場合、そちらを優先して読み込みます。
 * </p>
 *
 * <p>
 * 使用例:
 * 
 * <pre>{@code
 * List<Reminder> reminders = ReminderLoader.load();
 * }</pre>
 * </p>
 */
public class ReminderLoader {
	/**
	 * {@code reminders.json} を読み込んで、時刻順にソートされた リマインダーのリストを返します。
	 *
	 * @return 読み込んだリマインダー一覧。失敗時は空のリスト。
	 */
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

	/**
	 * {@code reminders.json} の読み込みに使用する {@link Reader} を生成します。
	 * 外部パスが指定されていればそちらを優先します。
	 *
	 * @return UTF-8 で読み込む Reader
	 * @throws IOException ファイルの読み込みに失敗した場合
	 */
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
