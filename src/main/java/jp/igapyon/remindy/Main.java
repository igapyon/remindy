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

/**
 * Remindy アプリケーションのエントリポイント。
 * <p>
 * Java + AWT による CLI リマインダーアプリ。1分ごとに状態を確認し、予定時刻や10分刻みのタイミングで通知・表示します。
 * </p>
 * 
 * <ul>
 * <li>Outlook CSV から reminders.json を生成（オプション）</li>
 * <li>Tray アイコンの初期化</li>
 * <li>リマインダーと格言の読み込み</li>
 * <li>各種コマンドを MinuteTicker に登録し、1分ごとにチェック</li>
 * </ul>
 * 
 * @author Toshiki Iga
 */
public class Main {
	/**
	 * Remindy アプリケーションの起動メソッド。
	 * 
	 * @param args 実行時引数（現在未使用）
	 */
	public static void main(String[] args) {
		// ⬇ 外部CSVが指定されていれば reminders.json を生成
		if (RemindyConstants.REMINDER_EXTERNAL_PATH.trim().length() > 0) {
			try {
				OutlookCsvToRemindersConv.main(new String[] {});
			} catch (Exception e) {
				System.err.println("Outlook変換に失敗: " + e.getMessage());
			}
		}

		// トレイアイコン初期化
		TrayIcon trayIcon = TrayIconSetup.createTrayIcon();

		// リマインダーと格言を読み込み
		List<Reminder> reminders = ReminderLoader.load();
		List<String> proverbs = ProverbLoader.load();

		// 通知用メッセージ構築ビルダー
		MessageBuilder builder = new MessageBuilder(reminders, proverbs);

		// 毎分チェックタイマの初期化とコマンド登録
		MinuteTicker ticker = new MinuteTicker();
		ticker.addCommand(new StartupCommand(RemindyConstants.VERSION, trayIcon, reminders)); // 起動時通知
		ticker.addCommand(new NotifyCommand(trayIcon, builder)); // 通知判定 (10分刻み/予定時刻)
		ticker.addCommand(new PopupCommand(builder)); // ポップアップ判定 (10分刻み/予定時刻)
		ticker.addCommand(new PikoMouseCommand()); // マウスピコピコ
		ticker.start();

		// メインスレッドを維持（Timer のため）
		while (true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
}
