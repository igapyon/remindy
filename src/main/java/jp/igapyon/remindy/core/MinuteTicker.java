package jp.igapyon.remindy.core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MinuteTicker {
	private final List<MinuteCommand> commandList = new ArrayList<>();

	public void addCommand(MinuteCommand command) {
		commandList.add(command);
	}

	public void start() {
		Timer timer = new Timer(true);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextMinute = now.plusMinutes(1).withSecond(0).withNano(0);
		long delay = Duration.between(now, nextMinute).toMillis();

		// 起動直後に StartupCommand を実行
		LocalTime currentTime = LocalTime.now();
		for (MinuteCommand cmd : commandList) {
			if (cmd.getClass().getSimpleName().equals("StartupCommand")) {
				try {
					cmd.execute(currentTime);
				} catch (Exception e) {
					System.err.println("StartupCommand の実行に失敗: " + e.getMessage());
				}
			}
		}

		// 通常の毎分処理
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				LocalTime now = LocalTime.now();
				for (MinuteCommand cmd : commandList) {
					// StartupCommand は除外
					if (cmd.getClass().getSimpleName().equals("StartupCommand")) {
						continue;
					}
					try {
						cmd.execute(now);
					} catch (Exception e) {
						System.err.println("コマンドの実行中にエラー: " + e.getMessage());
					}
				}
			}
		}, delay, 60 * 1000);
	}
}
