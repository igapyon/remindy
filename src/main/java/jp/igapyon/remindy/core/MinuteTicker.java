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
		// ===== 即時実行：StartupCommand を起動直後に実行 =====
		LocalTime now = LocalTime.now();
		for (MinuteCommand cmd : commandList) {
			if (cmd.getClass().getSimpleName().equals("StartupCommand")) {
				try {
					cmd.execute(now);
				} catch (Exception e) {
					System.err.println("StartupCommand 実行エラー: " + e.getMessage());
				}
			}
		}

		// ===== 00秒タイミングでの定期実行 =====
		Timer timer = new Timer(true);
		LocalDateTime nowDateTime = LocalDateTime.now();
		LocalDateTime nextMinute = nowDateTime.plusMinutes(1).withSecond(0).withNano(0);
		long delay = Duration.between(nowDateTime, nextMinute).toMillis();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				LocalTime currentTime = LocalTime.now();
				for (MinuteCommand cmd : commandList) {
					try {
						cmd.execute(currentTime);
					} catch (Exception e) {
						System.err.println("コマンドの実行中にエラー: " + e.getMessage());
					}
				}
			}
		}, delay, 60 * 1000);
	}
}
