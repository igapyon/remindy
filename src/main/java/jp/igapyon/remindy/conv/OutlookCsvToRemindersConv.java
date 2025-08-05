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

import jp.igapyon.remindy.RemindyConstants;
import jp.igapyon.remindy.vo.Reminder;

/**
 * Microsoft Outlook のエクスポートCSVファイル（予定表）を {@code reminders.json}
 * に変換するユーティリティクラスです。
 * 
 * <p>
 * 指定された {@code outlook-calendar.csv} を読み取り、当日の日付の予定を抽出し、 {@code HH:mm} 形式で JSON
 * に出力します。
 * </p>
 *
 * <p>
 * 使用例:
 * 
 * <pre>{@code
 *   java jp.igapyon.remindy.conv.OutlookCsvToRemindersConv
 * }</pre>
 * </p>
 * 
 * <p>
 * 外部パスが指定されている場合は、 {@link RemindyConstants#REMINDER_EXTERNAL_PATH} に従って
 * 入出力ファイルの場所を切り替えます。
 * </p>
 *
 * @author Toshiki Iga
 */
public class OutlookCsvToRemindersConv {
	/**
	 * コマンドライン実行時のエントリポイント。
	 *
	 * @param args 未使用
	 * @throws Exception 何らかの読み書きエラーが発生した場合
	 */
	public static void main(String[] args) throws Exception {
		File csvFile = new File("./src/main/resources/input/outlook-calendar.csv");
		File jsonFile = new File("./src/main/resources/reminders.json");

		// 外部ディレクトリが指定されている場合はそちらを使用
		if (RemindyConstants.REMINDER_EXTERNAL_PATH.trim().length() > 0) {
			csvFile = new File(RemindyConstants.REMINDER_EXTERNAL_PATH, "outlook-calendar.csv");
			jsonFile = new File(RemindyConstants.REMINDER_EXTERNAL_PATH, "reminders.json");
		}

		List<Reminder> reminders = new ArrayList<>();
		LocalDate today = LocalDate.now();

		// 日付と時刻のフォーマッタ
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");
		DateTimeFormatter timeParser = DateTimeFormatter.ofPattern("H:mm:ss"); // CSVからのパース用
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // JSON出力用（秒なし）

		try (Reader in = new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8)) {
			@SuppressWarnings("deprecation")
			CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().parse(in);

			for (CSVRecord record : parser) {
				String subject = record.get(0);
				String startDateStr = record.get(1);
				String startTimeStr = record.get(2);

				LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
				if (startDate.equals(today)) {
					Reminder reminder = new Reminder();
					LocalTime parsedTime = LocalTime.parse(startTimeStr, timeParser);
					reminder.time = parsedTime.format(timeFormatter);
					reminder.message = subject;
					reminders.add(reminder);
				}
			}
		}

		// JSON に書き出し
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		try (Writer out = new OutputStreamWriter(new FileOutputStream(jsonFile), StandardCharsets.UTF_8)) {
			mapper.writeValue(out, reminders);
		}

		System.out.println("reminders.json を出力しました。件数: " + reminders.size());
	}
}
