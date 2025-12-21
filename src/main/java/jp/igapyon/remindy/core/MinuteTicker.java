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
package jp.igapyon.remindy.core;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@code MinuteTicker} は、1分おきに {@link MinuteCommand} を順次実行するタイマーです。
 * <p>
 * コマンド群を登録し、起動することで、毎分定時に {@link MinuteCommand#execute(LocalTime)} を呼び出します。
 * また、特殊な {@code StartupCommand} は起動直後に1回だけ即時実行されます。
 * </p>
 *
 * @author Toshiki Iga
 */
public class MinuteTicker {
	private static final int LEAD_SECONDS = 10;
	private final List<MinuteCommand> commandList = new ArrayList<>();

	/**
	 * 毎分処理として実行する {@link MinuteCommand} を追加します。
	 *
	 * @param command 実行対象コマンド
	 */
	public void addCommand(MinuteCommand command) {
		commandList.add(command);
	}

	/**
	 * 毎分タイマーを開始します。
	 * <p>
	 * 最初の実行タイミングを次の00秒に合わせ、その後は毎分定期実行されます。
	 * </p>
	 * <p>
	 * なお、{@code StartupCommand}（クラス名一致で判定）は、 タイマー開始の直後に1度だけ即時実行されます。
	 * </p>
	 */
	public void start() {
		Timer timer = new Timer(true);

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime nextMinute = now.plusMinutes(1).withSecond(0).withNano(0);
		LocalDateTime firstRun = nextMinute.minusSeconds(LEAD_SECONDS);
		if (firstRun.isBefore(now)) {
			firstRun = firstRun.plusMinutes(1);
		}
		long delay = Duration.between(now, firstRun).toMillis();

		// 起動直後に StartupCommand を実行
		LocalTime currentTime = LocalTime.now().plusSeconds(LEAD_SECONDS);
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
				LocalTime now = LocalTime.now().plusSeconds(LEAD_SECONDS);
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
