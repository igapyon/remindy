package jp.igapyon.remindy.popup;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class JustTimePopup {
	private static final int WIDTH = 480;
	private static final int HEIGHT = 240;
	private static final Color BACKGROUND_COLOR = Color.decode("#D00000"); // 深い赤
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Font FONT_TITLE = new Font("Noto Sans CJK JP", Font.BOLD, 24);
	private static final Font FONT_TIME = new Font("Noto Sans CJK JP", Font.BOLD, 32);
	private static final Font FONT_MSG = new Font("Noto Sans CJK JP", Font.PLAIN, 24);

	public static void show(String time, String message) {
		SwingUtilities.invokeLater(() -> {
			JWindow window = new JWindow();
			window.setSize(WIDTH, HEIGHT);
			window.setAlwaysOnTop(true);
			window.setLocationRelativeTo(null); // 画面中央

			JPanel mainPanel = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(BACKGROUND_COLOR);
					g.fillRect(0, 0, getWidth(), getHeight());

					// 上下の白ライン
					g.setColor(Color.WHITE);
					g.fillRect(0, 10, getWidth(), 6); // 上部ライン
					g.fillRect(0, getHeight() - 16, getWidth(), 6); // 下部ライン
				}
			};

			mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

			JLabel labelHeader = new JLabel("【REMINDER】", SwingConstants.CENTER);
			labelHeader.setFont(FONT_TITLE);
			labelHeader.setForeground(TEXT_COLOR);
			labelHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

			JLabel labelTime = new JLabel(time, SwingConstants.CENTER);
			labelTime.setFont(FONT_TIME);
			labelTime.setForeground(TEXT_COLOR);
			labelTime.setAlignmentX(Component.CENTER_ALIGNMENT);

			JLabel labelMessage = new JLabel(message, SwingConstants.CENTER);
			labelMessage.setFont(FONT_MSG);
			labelMessage.setForeground(TEXT_COLOR);
			labelMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

			mainPanel.add(Box.createVerticalStrut(30));
			mainPanel.add(labelHeader);
			mainPanel.add(Box.createVerticalStrut(10));
			mainPanel.add(labelTime);
			mainPanel.add(Box.createVerticalStrut(10));
			mainPanel.add(labelMessage);

			window.getContentPane().add(mainPanel);
			window.setVisible(true);

			// 5秒後に自動で閉じる
			new Timer(5000, e -> window.dispose()).start();
		});
	}
}