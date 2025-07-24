package jp.igapyon.remindy.ui;

import java.awt.Color;
import java.awt.Window;

import javax.swing.JWindow;
import javax.swing.SwingUtilities;

public final class JyuWarningPopup {

	private JyuWarningPopup() {
	}

	public static void showPopup(String message) {
		showPopup(message, 800, 320, 5000);
	}

	public static void showPopup(String message, int width, int height, int durationMillis) {
		SwingUtilities.invokeLater(() -> {
			JWindow window = new JWindow((Window) null); // オーナー無し
			window.setAlwaysOnTop(true);
			window.setSize(width, height);
			window.setLocationRelativeTo(null);
			window.setBackground(new Color(0, 0, 0, 255)); // 不透明黒

			window.setContentPane(new JyuWarningPanel(message));
			window.setVisible(true);

			// javax.swing.Timer を使う（java.util.Timer と混同しない）
			new javax.swing.Timer(durationMillis, e -> window.dispose()).start();
		});
	}
}
