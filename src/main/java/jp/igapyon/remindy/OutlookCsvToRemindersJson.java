package jp.igapyon.remindy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import jp.igapyon.remindy.vo.Reminder;

public class OutlookCsvToRemindersJson {
	public static void main(String[] args) throws Exception {
		File csvFile = new File("./src/main/resources/input/outlook.csv");
		File jsonFile = new File("./src/main/resources/reminders.json");

		List<Reminder> reminders = new ArrayList<>();
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/M/d");

		try (Reader in = new InputStreamReader(new FileInputStream(csvFile), StandardCharsets.UTF_8)) {
			CSVParser parser = CSVFormat.DEFAULT.withFirstRecordAsHeader().withTrim().parse(in);

			for (CSVRecord record : parser) {
				String subject = record.get("件名");
				String startDateStr = record.get("開始日");
				String startTime = record.get("開始日時");
				String body = record.get("内容");

				LocalDate startDate = LocalDate.parse(startDateStr, dateFormatter);
				if (startDate.equals(today)) {
					Reminder reminder = new Reminder();
					reminder.time = startTime;
					reminder.message = subject + ": " + body;
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
