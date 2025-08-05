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

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				LocalTime now = LocalTime.now();
				for (MinuteCommand cmd : commandList) {
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
