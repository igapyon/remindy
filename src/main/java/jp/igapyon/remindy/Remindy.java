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
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Remindy {
	public static final String VERSION = "20250625a";

	private List<String> proverbs;
	private List<Reminder> reminders;
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

		// TrayIcon を一度だけ登録（ちらつき防止）
		setupTrayIcon();

		displayMessage("Remindy (" + VERSION + ")", "名言とリマインドを毎分通知します");

		loadProverbs();
		loadReminders();

		Timer timer = new Timer(true);

		// 次の 00 秒までの遅延を計算
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextMinute = now.plusMinutes(1).withSecond(0).withNano(0);
		long delay = Duration.between(now, nextMinute).toMillis();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				LocalTime currentTime = LocalTime.now();
				String timeStr = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"));
				int minute = currentTime.getMinute();

				// 時間指定リマインド優先
				for (Reminder r : reminders) {
					if (r.time.equals(timeStr)) {
						displayMessage("⏰⏰リマインド⏰⏰ - " + timeStr, r.message);
						pikoMouse();
					}
				}

				if (minute == 0 || minute == 30) {
					displayMessage("⏰⏰⏰ぴったり時間⏰⏰⏰ - " + timeStr, "今はちょうどの時間です。カレンダー確認してください。");
				} else if (proverbs != null && !proverbs.isEmpty()) {
					String proverb = proverbs.get(proverbIndex);
					displayMessage("⏰格言⏰ - " + timeStr, proverb);
					proverbIndex = (proverbIndex + 1) % proverbs.size();
				}

				pikoMouse(); // マウス1ピクセル移動
			}
		}, delay, 60 * 1000);

		try {
			while (true)
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println("割り込みにより終了します。");
		}
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
			try (InputStreamReader reader = new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream("proverbs.json"), "UTF-8")) {
				proverbs = mapper.readValue(reader, new TypeReference<List<String>>() {
				});
			}
			System.err.println("格言を " + proverbs.size() + " 件読み込みました。");
		} catch (Exception e) {
			System.err.println("格言の読み込みに失敗: " + e.getMessage());
			proverbs = Collections.emptyList();
		}
	}

	private void loadReminders() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			try (InputStreamReader reader = new InputStreamReader(
					getClass().getClassLoader().getResourceAsStream("reminders.json"), "UTF-8")) {
				reminders = mapper.readValue(reader, new TypeReference<List<Reminder>>() {
				});
			}
			System.err.println("リマインドを " + reminders.size() + " 件読み込みました。");
		} catch (Exception e) {
			System.err.println("reminders.json の読み込みに失敗: " + e.getMessage());
			reminders = Collections.emptyList();
		}
	}

	private void displayMessage(String title, String message) {
		if (trayIcon != null) {
			trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
			System.err.println("通知: " + title + ": " + message);
		}
	}

	private void pikoMouse() {
		try {
			Robot robot = new Robot();
			Point location = MouseInfo.getPointerInfo().getLocation();
			int x = (int) location.getX();
			int y = (int) location.getY();

			int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, -1 }, { 0, 1 } };
			int[] move = directions[new Random().nextInt(directions.length)];

			robot.mouseMove(x + move[0], y + move[1]);
		} catch (Exception e) {
			System.err.println("マウス移動に失敗: " + e.getMessage());
		}
	}
}