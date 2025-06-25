package jp.igapyon.remindy;

import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.io.InputStreamReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.igapyon.remindy.vo.Reminder;

public class Remindy {
	public static final String VERSION = "20250625c";
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
	private static final String ENCODING_UTF8 = "UTF-8";

	private List<String> proverbs = Collections.emptyList();
	private List<Reminder> reminders = Collections.emptyList();
	private int proverbIndex = 0;
	private TrayIcon trayIcon;

	public static void main(String[] args) {
		new Remindy().process();
	}

	public void process() {
		if (!SystemTray.isSupported()) {
			System.err.println("この環境では通知がサポートされていません。中断終了します。");
			return;
		}

		setupTrayIcon();
		displayMessage("Remindy (" + VERSION + ")", "名言とリマインドを毎分通知します");

		loadProverbs();
		loadReminders();

		Timer timer = new Timer(true);
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextMinute = now.plusMinutes(1).withSecond(0).withNano(0);
		long delay = Duration.between(now, nextMinute).toMillis();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				runNotificationCycle();
			}
		}, delay, 60 * 1000);

		try {
			while (true)
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println("割り込みにより終了します。");
		}
	}

	private void runNotificationCycle() {
		LocalTime now = LocalTime.now();
		String title = buildTitle(now);
		String message = buildMessage(now);
		displayMessage(title, message);
		pikoMouse();
	}

	private String buildTitle(LocalTime now) {
		String title = "⏰ " + now.format(TIME_FORMATTER);
		if (now.getMinute() == 0 || now.getMinute() == 30) {
			title += " - ピッタリ時間";
		}
		return title;
	}

	private String buildMessage(LocalTime now) {
		String nowStr = now.format(TIME_FORMATTER);
		List<String> lines = new ArrayList<>();

		// リマインド
		for (Reminder r : reminders) {
			if (nowStr.equals(r.time)) {
				lines.add("🔔 リマインド: " + r.message);
			}
		}

		// 今後の予定
		for (Reminder r : reminders) {
			LocalTime rTime = LocalTime.parse(r.time, TIME_FORMATTER);
			if (rTime.isAfter(now)) {
				long minutes = Duration.between(now, rTime).toMinutes();
				String future;
				if (minutes >= 60) {
					long hours = (minutes + 30) / 60; // 切り上げ
					future = String.format("%s（%d時間後）%s", r.time, hours, r.message);
				} else {
					future = String.format("%s（%d分後）%s", r.time, minutes, r.message);
				}
				lines.add("🗓 " + future);
			}
		}

		// 格言
		if (!proverbs.isEmpty()) {
			lines.add("💡 " + proverbs.get(proverbIndex));
			proverbIndex = (proverbIndex + 1) % proverbs.size();
		}

		return String.join("\n", lines);
	}

	private void setupTrayIcon() {
		try {
			Image icon = Toolkit.getDefaultToolkit()
					.getImage(getClass().getClassLoader().getResource("images/remindy_icon_32x32.png"));
			trayIcon = new TrayIcon(icon, "Remindy");
			trayIcon.setImageAutoSize(true);
			SystemTray.getSystemTray().add(trayIcon);
		} catch (Exception e) {
			System.err.println("TrayIconの初期化に失敗: " + e.getMessage());
		}
	}

	private void loadProverbs() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStreamReader reader = new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream("proverbs.json"), ENCODING_UTF8);
			proverbs = mapper.readValue(reader, new TypeReference<List<String>>() {
			});
			reader.close();
			System.err.println("格言を " + proverbs.size() + " 件読み込みました。");
		} catch (Exception e) {
			System.err.println("格言の読み込みに失敗: " + e.getMessage());
		}
	}

	private void loadReminders() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			InputStreamReader reader = new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream("reminders.json"), ENCODING_UTF8);
			reminders = mapper.readValue(reader, new TypeReference<List<Reminder>>() {
			});
			reader.close();
			System.err.println("リマインドを " + reminders.size() + " 件読み込みました。");
		} catch (Exception e) {
			System.err.println("reminders.json の読み込みに失敗: " + e.getMessage());
		}
	}

	private void displayMessage(String title, String message) {
		if (trayIcon != null) {
			trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
			System.err.println("通知: " + title + "\n" + message);
		}
	}

	private void pikoMouse() {
		try {
			Robot robot = new Robot();
			Point loc = MouseInfo.getPointerInfo().getLocation();
			int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, -1 }, { 0, 1 } };
			int[] move = directions[new Random().nextInt(directions.length)];
			robot.mouseMove(loc.x + move[0], loc.y + move[1]);
		} catch (Exception e) {
			System.err.println("マウス移動に失敗: " + e.getMessage());
		}
	}
}