package jp.igapyon.remindy.popup;

import java.awt.BorderLayout;
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
import javax.swing.border.LineBorder;

public class JustTimePopup {
	private static final int WIDTH = 640;
	private static final int HEIGHT = 360;
	private static final Color BLACK = Color.BLACK;
	private static final Color RED = Color.decode("#D80000");
	private static final Font FONT_KANJI = new Font("MS Mincho", Font.BOLD, 72); // または Noto Serif CJK
	private static final Font FONT_LABEL = new Font("Meiryo UI", Font.BOLD, 20);

	public static void show(String jpText, String enText) {
		SwingUtilities.invokeLater(() -> {
			JWindow window = new JWindow();
			window.setSize(WIDTH, HEIGHT);
			window.setAlwaysOnTop(true);
			window.setLocationRelativeTo(null);

			JPanel mainPanel = new JPanel() {
				private static final long serialVersionUID = 1L;

				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(BLACK);
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			mainPanel.setLayout(new BorderLayout());

			// 上部ストライプ
			mainPanel.add(createStripePanel(), BorderLayout.NORTH);

			// 中央コンテンツ
			JPanel centerPanel = new JPanel();
			centerPanel.setBackground(BLACK);
			centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

			JLabel labelWarning = new JLabel("注意喚起", SwingConstants.CENTER);
			labelWarning.setFont(FONT_KANJI);
			labelWarning.setForeground(RED);
			labelWarning.setAlignmentX(Component.CENTER_ALIGNMENT);
			centerPanel.add(labelWarning);
			centerPanel.add(Box.createVerticalStrut(20));

			// パネル1（日本語）
			JPanel panelJp = createTextPanel(jpText);
			centerPanel.add(panelJp);
			centerPanel.add(Box.createVerticalStrut(10));

			// パネル2（英語）
			JPanel panelEn = createTextPanel(enText);
			centerPanel.add(panelEn);

			mainPanel.add(centerPanel, BorderLayout.CENTER);

			// 下部ストライプ
			mainPanel.add(createStripePanel(), BorderLayout.SOUTH);

			window.getContentPane().add(mainPanel);
			window.setVisible(true);

			// 5秒後に自動で閉じる
			new Timer(5000, e -> window.dispose()).start();
		});
	}

	private static JPanel createTextPanel(String text) {
		JPanel panel = new JPanel();
		panel.setBackground(BLACK);
		panel.setBorder(new LineBorder(RED, 2));
		JLabel label = new JLabel(text);
		label.setFont(FONT_LABEL);
		label.setForeground(RED);
		panel.add(label);
		return panel;
	}

	private static JPanel createStripePanel() {
		return new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());

				g.setColor(RED);
				int stripeWidth = 20;
				for (int x = 0; x < getWidth(); x += stripeWidth) {
					g.fillPolygon(new int[] { x, x + stripeWidth, x }, new int[] { 0, 0, getHeight() }, 3);
				}
			}
		};
	}
}