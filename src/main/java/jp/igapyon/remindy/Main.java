package jp.igapyon.remindy;

import jp.igapyon.remindy.command.LogCommand;
import jp.igapyon.remindy.core.MinuteTicker;

public class Main {
	public static void main(String[] args) {
		MinuteTicker ticker = new MinuteTicker();
		ticker.addCommand(new LogCommand());
		ticker.start();

		// メインスレッドの維持
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
