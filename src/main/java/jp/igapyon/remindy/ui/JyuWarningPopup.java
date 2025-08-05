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
package jp.igapyon.remindy.ui;

import java.awt.Color;
import java.awt.Window;

import javax.swing.JWindow;
import javax.swing.SwingUtilities;

/**
 * {@code JyuWarningPopup} は、全画面中央に警告スタイルのポップアップを表示するユーティリティクラスです。
 *
 * <p>
 * 警告用のカスタムパネル {@link JyuWarningPanel} を使って、
 * アプリの通知や起動時のメッセージなどを視覚的に印象的に伝えるために使用されます。
 * </p>
 *
 * <p>
 * 非同期で表示され、指定時間後に自動で非表示になります。
 * </p>
 *
 * <p>
 * 例: {@code JyuWarningPopup.showPopup("Hello");}
 * </p>
 *
 * @see JyuWarningPanel
 */
public final class JyuWarningPopup {

	/** インスタンス化禁止のための private コンストラクタ */
	private JyuWarningPopup() {
	}

	/**
	 * デフォルト設定でポップアップを表示します。
	 * <ul>
	 * <li>幅: 800 px</li>
	 * <li>高さ: 320 px</li>
	 * <li>表示時間: 5秒</li>
	 * </ul>
	 *
	 * @param message 表示するメッセージ（複数行は改行で区切り）
	 */
	public static void showPopup(String message) {
		showPopup(message, 800, 320, 5000);
	}

	/**
	 * カスタム設定でポップアップを表示します。
	 *
	 * @param message        表示するメッセージ（複数行は改行で区切り）
	 * @param width          ポップアップの幅（ピクセル）
	 * @param height         ポップアップの高さ（ピクセル）
	 * @param durationMillis 表示する時間（ミリ秒）
	 */
	public static void showPopup(String message, int width, int height, int durationMillis) {
		SwingUtilities.invokeLater(() -> {
			JWindow window = new JWindow((Window) null); // オーナー無しのウィンドウ
			window.setAlwaysOnTop(true); // 最前面表示
			window.setSize(width, height);
			window.setLocationRelativeTo(null); // 画面中央
			window.setBackground(new Color(0, 0, 0, 255)); // 不透明な黒

			// カスタム警告パネルを内容に設定
			window.setContentPane(new JyuWarningPanel(message));
			window.setVisible(true);

			// 指定時間経過後に自動で閉じる
			new javax.swing.Timer(durationMillis, e -> window.dispose()).start();
		});
	}
}
