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
package jp.igapyon.remindy.util;

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.net.URL;

/**
 * {@code TrayIconSetup} は、システムトレイにアイコンを登録するユーティリティクラスです。
 *
 * <p>
 * リマインダー通知用の {@link TrayIcon} を作成し、ユーザーにポップアップで通知を行う準備をします。
 * </p>
 *
 * <p>
 * アイコン画像は {@code resources/images/remindy_icon_32x32.png} に存在する必要があります。
 * </p>
 *
 * 使用例:
 * 
 * <pre>{@code
 * TrayIcon trayIcon = TrayIconSetup.createTrayIcon();
 * if (trayIcon != null) {
 * 	trayIcon.displayMessage("タイトル", "内容", TrayIcon.MessageType.INFO);
 * }
 * }</pre>
 *
 * @author Toshiki Iga
 * @since 1.0
 */
public class TrayIconSetup {
	/**
	 * システムトレイに {@link TrayIcon} を作成・登録します。
	 *
	 * <p>
	 * アイコン画像が存在しない場合、またはトレイがサポートされていない場合は {@code null} を返します。
	 * </p>
	 *
	 * @return 作成された {@link TrayIcon} オブジェクト、または失敗時は {@code null}
	 */
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
