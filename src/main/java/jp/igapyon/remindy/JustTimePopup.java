package jp.igapyon.remindy;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class JustTimePopup {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 200;
	private static final Color BACKGROUND_COLOR = new Color(204, 0, 0); // 赤 (#cc0000)
	private static final Color TEXT_COLOR = Color.WHITE;
	private static final Font FONT_LARGE = new Font("Meiryo UI", Font.BOLD, 24);
	private static final Font FONT_SMALL = new Font("Meiryo UI", Font.PLAIN, 18);

	public static void show(String time, String message) {
		SwingUtilities.invokeLater(() -> {
			JWindow window = new JWindow();

			JPanel panel = new JPanel() {
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(BACKGROUND_COLOR);
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 4));

			JLabel labelHeader = new JLabel("【REMINDER】", SwingConstants.CENTER);
			labelHeader.setFont(FONT_SMALL);
			labelHeader.setForeground(TEXT_COLOR);
			labelHeader.setAlignmentX(Component.CENTER_ALIGNMENT);

			JLabel labelTime = new JLabel(time, SwingConstants.CENTER);
			labelTime.setFont(FONT_LARGE);
			labelTime.setForeground(TEXT_COLOR);
			labelTime.setAlignmentX(Component.CENTER_ALIGNMENT);

			JLabel labelMessage = new JLabel(message, SwingConstants.CENTER);
			labelMessage.setFont(FONT_LARGE);
			labelMessage.setForeground(TEXT_COLOR);
			labelMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

			panel.add(Box.createVerticalStrut(20));
			panel.add(labelHeader);
			panel.add(Box.createVerticalStrut(10));
			panel.add(labelTime);
			panel.add(Box.createVerticalStrut(10));
			panel.add(labelMessage);
			panel.add(Box.createVerticalGlue());

			window.getContentPane().add(panel);
			window.setSize(WIDTH, HEIGHT);
			window.setAlwaysOnTop(true);
			window.setLocationRelativeTo(null); // 画面中央に表示
			window.setVisible(true);

			// 5秒後に自動で閉じる
			new Timer(5000, e -> window.dispose()).start();
		});
	}
}