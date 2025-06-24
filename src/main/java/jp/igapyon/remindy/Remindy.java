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
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Remindy {
	private List<String> proverbs;
	private int proverbIndex = 0;

	public static void main(String[] args) {
		new Remindy().process();
	}

	public void process() {
		if (!SystemTray.isSupported()) {
			System.err.println("この環境では通知がサポートされていません。中断終了します。");
			return;
		}

		displayMessage("Remindy", "名言を毎分表示します");

		// 名言ロード。
		loadProverbs();

		Timer timer = new Timer(true);

		// 次の00.00秒
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextMinute = now.plusMinutes(1).withSecond(0).withNano(0);
		long delay = Duration.between(now, nextMinute).toMillis();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				LocalTime currentTime = LocalTime.now();
				String timeStr = currentTime.format(DateTimeFormatter.ofPattern("HH:mm"));

				int minute = currentTime.getMinute();
				if (minute == 0 || minute == 30) {
					displayMessage("☆☆☆ぴったり時間☆☆☆ - " + timeStr, "今はちょうどの時間です。カレンダー確認してください。");
				} else if (proverbs != null && !proverbs.isEmpty()) {
					String proverb = proverbs.get(proverbIndex);
					displayMessage("格言 - " + timeStr, proverb);
					proverbIndex = (proverbIndex + 1) % proverbs.size();
				}
				pikoMouse(); // マウス移動
			}
		}, delay, 60 * 1000); // 毎分実行

		try {
			while (true) {
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			System.err.println("割り込みにより終了します。");
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
			System.err.println("格言の読み込みに失敗しました: " + e.getMessage());
			proverbs = null;
		}
	}

	private static void displayMessage(String title, String message) {
		try {
			// 指定はするが利用されない。。。
			Image icon = Toolkit.getDefaultToolkit()
					.getImage(Remindy.class.getClassLoader().getResource("images/remindy_icon_32x32.png"));
			TrayIcon trayIcon = new TrayIcon(icon, "Remindy");
			trayIcon.setImageAutoSize(true);

			SystemTray.getSystemTray().add(trayIcon);
			trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
			System.err.println("⏰通知: " + title + ": " + message);

			Thread.sleep(5000);
			SystemTray.getSystemTray().remove(trayIcon);
		} catch (Exception e) {
			System.err.println("通知の表示に失敗: " + e.getMessage());
		}
	}

	private static void pikoMouse() {
		try {
			Robot robot = new Robot();
			Point location = MouseInfo.getPointerInfo().getLocation();
			int x = (int) location.getX();
			int y = (int) location.getY();

			int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, -1 }, { 0, 1 } };
			int[] move = directions[new Random().nextInt(4)];

			int newX = x + move[0];
			int newY = y + move[1];

			robot.mouseMove(newX, newY);
		} catch (Exception e) {
			System.err.println("マウス移動に失敗: " + e.getMessage());
		}
	}
}
