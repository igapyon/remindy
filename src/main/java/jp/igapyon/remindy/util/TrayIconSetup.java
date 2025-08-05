package jp.igapyon.remindy.util;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.net.URL;

public class TrayIconSetup {
	public static TrayIcon createTrayIcon() {
		try {
			SystemTray tray = SystemTray.getSystemTray();
			URL imageUrl = TrayIconSetup.class.getClassLoader().getResource("images/remindy_icon_32x32.png");
			if (imageUrl == null) {
				System.err.println("アイコン画像が見つかりませんでした");
				return null;
			}

			Image icon = Toolkit.getDefaultToolkit().getImage(imageUrl);
			TrayIcon trayIcon = new TrayIcon(icon, "Remindy");
			trayIcon.setImageAutoSize(true);
			tray.add(trayIcon);
			return trayIcon;
		} catch (Exception e) {
			System.err.println("TrayIcon の初期化に失敗: " + e.getMessage());
			return null;
		}
	}
}
