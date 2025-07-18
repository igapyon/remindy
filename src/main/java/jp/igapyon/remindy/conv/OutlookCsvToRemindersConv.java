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
package jp.igapyon.remindy.conv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.igapyon.remindy.vo.Reminder;

public class OutlookCsvToRemindersConv {
	// reminders.json を外部パスに設定する場合
	public static final String REMINDER_EXTERNAL_PATH = "";

	public static void main(String[] args) throws Exception {
		File csvFile = new File("./src/main/resources/input/outlook-calendar.csv");
		File jsonFile = new File("./src/main/resources/reminders.json");
		if (REMINDER_EXTERNAL_PATH.trim().length() > 0) {
			csvFile = new File(REMINDER_EXTERNAL_PATH, "outlook-calendar.csv");
			jsonFile = new File(REMINDER_EXTERNAL_PATH, "reminders.json");
		}

		List<Reminder> reminders = new ArrayList<>();
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
		DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("H:mm:ss"); // ← Outlookの時刻用
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // ← 出力用

		try (Reader in = new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8)) {
			CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().parse(in);

			for (CSVRecord record : parser) {
				String subject = record.get(0);
				String startDateStr = record.get(1);
				String startTimeStr = record.get(2);

				LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
				if (startDate.equals(today)) {
					Reminder reminder = new Reminder();
					LocalTime parsedTime = LocalTime.parse(startTimeStr, timeParser);
					reminder.time = parsedTime.format(timeFormatter); // ← ここで 0埋め＆秒なし

					reminder.message = subject;
					reminders.add(reminder);
				}
			}
		}

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try (Writer out = new OutputStreamWriter(new FileOutputStream(jsonFile), StandardCharsets.UTF_8)) {
			mapper.writeValue(out, reminders);
		}

		System.out.println("reminders.json を出力しました。件数: " + reminders.size());
	}
}
