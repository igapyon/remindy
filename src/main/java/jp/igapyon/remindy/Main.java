package jp.igapyon.remindy;

import java.awt.TrayIcon;

import jp.igapyon.remindy.command.NotifyCommand;
import jp.igapyon.remindy.core.MinuteTicker;
import jp.igapyon.remindy.logic.MessageBuilder;
import jp.igapyon.remindy.util.TrayIconSetup;

public class Main {
	public static void main(String[] args) {
		TrayIcon trayIcon = TrayIconSetup.createTrayIcon();
		MessageBuilder builder = new MessageBuilder();

		MinuteTicker ticker = new MinuteTicker();
		ticker.addCommand(new NotifyCommand(trayIcon, builder));
		ticker.start();

		// メインスレッド維持
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
