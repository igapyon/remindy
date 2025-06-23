package jp.igapyon.remindy;

import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

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

		System.err.println("Remindy CLI起動: 通知と格言を定期実行します");

		loadProverbs();

		Timer timer = new Timer(true);

		// 通常の休憩通知（25分ごと）
		long intervalMillis = 25 * 60 * 1000;
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				displayMessage("リマインド", "そろそろ休憩しませんか？");
			}
		}, 0, intervalMillis);

		// マウスジグル（25分ごと）
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				jiggleMouse();
			}
		}, 0, intervalMillis);

		// 格言表示（1分ごと）
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (proverbs != null && !proverbs.isEmpty()) {
					String proverb = proverbs.get(proverbIndex);
					displayMessage("格言", proverb);
					proverbIndex = (proverbIndex + 1) % proverbs.size();
				}
			}
		}, 0, 60 * 1000);

		// 無限ループで維持
		try {
			while (true)
				Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println("割り込みにより終了します。");
		}
	}

	private void loadProverbs() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			proverbs = mapper.readValue(new File("proverbs.json"), new TypeReference<List<String>>() {
			});
			System.err.println("格言を " + proverbs.size() + " 件読み込みました。");
		} catch (Exception e) {
			System.err.println("格言の読み込みに失敗しました: " + e.getMessage());
			proverbs = null;
		}
	}

	private static void displayMessage(String title, String message) {
		try {
			Image image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			TrayIcon trayIcon = new TrayIcon(image, "Remindy");
			trayIcon.setImageAutoSize(true);

			SystemTray.getSystemTray().add(trayIcon);
			trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
			System.err.println("通知: 【" + title + "】" + message);

			Thread.sleep(5000);
			SystemTray.getSystemTray().remove(trayIcon);
		} catch (Exception e) {
			System.err.println("通知の表示に失敗: " + e.getMessage());
		}
	}

	private static void jiggleMouse() {
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
			System.err.printf("マウス移動: (%d, %d) → (%d, %d)%n", x, y, newX, newY);
		} catch (Exception e) {
			System.err.println("マウス移動に失敗: " + e.getMessage());
		}
	}
}
