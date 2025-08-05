package jp.igapyon.remindy;

import java.awt.TrayIcon;
import java.util.List;

import jp.igapyon.remindy.command.NotifyCommand;
import jp.igapyon.remindy.command.PikoMouseCommand;
import jp.igapyon.remindy.command.PopupCommand;
import jp.igapyon.remindy.command.StartupCommand;
import jp.igapyon.remindy.conv.OutlookCsvToRemindersConv;
import jp.igapyon.remindy.core.MinuteTicker;
import jp.igapyon.remindy.loader.ProverbLoader;
import jp.igapyon.remindy.loader.ReminderLoader;
import jp.igapyon.remindy.logic.MessageBuilder;
import jp.igapyon.remindy.util.TrayIconSetup;
import jp.igapyon.remindy.vo.Reminder;

public class Main {
	public static void main(String[] args) {
		// ⬇ 外部CSVが指定されていれば reminders.json を生成
		if (RemindyConstants.REMINDER_EXTERNAL_PATH.trim().length() > 0) {
			try {
				OutlookCsvToRemindersConv.main(new String[] {});
			} catch (Exception e) {
				System.err.println("Outlook変換に失敗: " + e.getMessage());
			}
		}

		TrayIcon trayIcon = TrayIconSetup.createTrayIcon();
		List<Reminder> reminders = ReminderLoader.load(); // 別クラスで読み込み想定
		List<String> proverbs = ProverbLoader.load(); // 同上

		MessageBuilder builder = new MessageBuilder(reminders, proverbs);

		MinuteTicker ticker = new MinuteTicker();
		ticker.addCommand(new StartupCommand(RemindyConstants.VERSION, trayIcon, reminders));
		ticker.addCommand(new NotifyCommand(trayIcon, builder));
		ticker.addCommand(new PopupCommand(builder));
		ticker.addCommand(new PikoMouseCommand());
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
